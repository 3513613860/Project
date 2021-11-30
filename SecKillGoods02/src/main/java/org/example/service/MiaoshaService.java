package org.example.service;

import org.example.entity.MiaoshaOrder;
import org.example.entity.MiaoshaUser;
import org.example.entity.OrderInfo;
import org.example.redis.MiaoshaKey;
import org.example.redis.RedisService;
import org.example.util.MD5Util;
import org.example.util.UUIDUtil;
import org.example.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class MiaoshaService {
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;

	@Autowired
	RedisService redisService;

	@Transactional
	public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
		//减库存 下订单 写入秒杀订单
		boolean success = goodsService.reduceStock(goods);
		//order_info maiosha_order
		if(success){
			return orderService.createOrder(user, goods);
		}else{
			setGoodsOver(goods.getId());
			return null;
		}
	}



	public long getMiaoshaResult(Long userId, long goodsId) {
		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
		if(order != null){
			return order.getOrderId();
		}else{
			boolean isOver = getGoodsOver(goodsId);
			if(isOver){
				return -1;
			}else{
				return 0;
			}
		}
	}

	private boolean getGoodsOver(long goodsId) {
		return  redisService.exists(MiaoshaKey.isGoodsOver,""+goodsId);
	}

	private void setGoodsOver(Long goodsId) {
		redisService.set(MiaoshaKey.isGoodsOver,""+goodsId,true);
	}

	public boolean checkPath(MiaoshaUser user, long goodsId, String path) {
		if(user == null || path == null) {
			return false;
		}
		String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath, ""+user.getId() + "_"+ goodsId, String.class);
		return path.equals(pathOld);
	}

	public String createMiaoshaPath(MiaoshaUser user, long goodsId) {
		if(user == null || goodsId <=0) {
			return null;
		}
		String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
		redisService.set(MiaoshaKey.getMiaoshaPath, ""+user.getId() + "_"+ goodsId, str);
		return str;
	}
}
