package org.example.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.example.entity.MiaoshaOrder;
import org.example.entity.MiaoshaUser;
import org.example.entity.OrderInfo;
import org.example.rabbitmq.MQSender;
import org.example.rabbitmq.MiaoshaMessage;
import org.example.redis.GoodsKey;
import org.example.redis.RedisService;
import org.example.result.CodeMsg;
import org.example.result.Result;
import org.example.service.GoodsService;
import org.example.service.MiaoshaService;
import org.example.service.MiaoshaUserService;
import org.example.service.OrderService;
import org.example.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MiaoshaService miaoshaService;

	@Autowired
	MQSender sender;

	private Map<Long,Boolean> localOverMap = new HashMap<>();

//    @RequestMapping("/do_miaosha")
//    public String list(Model model, MiaoshaUser user,
//					   @RequestParam("goodsId")long goodsId) {
//    	model.addAttribute("user", user);
//
//    	if(user == null) {
//    		return "login";
//    	}
//    	//判断库存
//    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//    	int stock = goods.getStockCount();
//    	if(stock <= 0) {
//    		model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
//    		return "miaosha_fail";
//    	}
//    	//判断是否已经秒杀到了，防止重复秒杀
//    	MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//    	if(order != null) {
//    		model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
//    		return "miaosha_fail";
//    	}
//    	//减库存 下订单 写入秒杀订单
//    	OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
//    	model.addAttribute("orderInfo", orderInfo);
//    	model.addAttribute("goods", goods);
//        return "order_detail";
//    }

	/**
	 * QPS:1306
	 * 5000 * 10
	 * QPS: 2114
	 * */
	@RequestMapping(value="/{path}/do_miaosha", method= RequestMethod.POST)
	@ResponseBody
	public Result<Integer> miaosha(Model model, MiaoshaUser user,
									 @RequestParam("goodsId")long goodsId,@PathVariable("path") String path) {
		model.addAttribute("user", user);
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}


		//验证path
		boolean check = miaoshaService.checkPath(user, goodsId, path);
		if(!check){
			return Result.error(CodeMsg.REQUEST_ILLEGAL);
		}

		//内存标记，减少redis访问
		boolean  over = localOverMap.get(goodsId);
		if(over){
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}
		//预减库存
		long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock,""+goodsId);
		if(stock < 0){
			localOverMap.put(goodsId,true);
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}
		//
		//判断是否已经秒杀到了
		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
		if(order != null) {
			return Result.error(CodeMsg.REPEATE_MIAOSHA);
		}
		//入队
		MiaoshaMessage mm = new MiaoshaMessage();
		mm.setUser(user);
		mm.setGoodsId(goodsId);
		sender.sendMiaoshaMessage(mm);
		return  Result.success(0);  //排队中



//		//判断库存
//		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);//10个商品，req1 req2
//		int stock = goods.getStockCount();
//		if(stock <= 0) {
//			return Result.error(CodeMsg.MIAO_SHA_OVER);
//		}
//		//判断是否已经秒杀到了
//		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//		if(order != null) {
//			return Result.error(CodeMsg.REPEATE_MIAOSHA);
//		}
//		//减库存 下订单 写入秒杀订单
//		OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
//		return Result.success(orderInfo);


	}


	/*
	orderId:成功
	-1  失败
	0  排队中
	 */
	@RequestMapping(value="/result", method= RequestMethod.GET)
	@ResponseBody
	public Result<Long> miaoshaResult(Model model, MiaoshaUser user,
								   @RequestParam("goodsId")long goodsId) {
		model.addAttribute("user", user);
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		long result = miaoshaService.getMiaoshaResult(user.getId(),goodsId);
		return Result.success(result);
	}

	@RequestMapping(value="/path", method= RequestMethod.GET)
	@ResponseBody
	public Result<String> getMiaoshaPath(Model model, MiaoshaUser user,
									  @RequestParam("goodsId")long goodsId) {
		model.addAttribute("user", user);
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		String path = miaoshaService.createMiaoshaPath(user,goodsId);
		return Result.success(path);
	}


	/*
	  系统初始化
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
		if(goodsVoList == null){
			return;
		}
		for(GoodsVo goods:goodsVoList){
			redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goods.getId(),goods.getStockCount());
			localOverMap.put(goods.getId(),false);
		}
	}
}
