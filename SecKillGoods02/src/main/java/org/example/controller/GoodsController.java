package org.example.controller;

import org.example.entity.MiaoshaUser;
import org.example.redis.GoodsKey;
import org.example.redis.RedisService;
import org.example.result.Result;
import org.example.service.GoodsService;
import org.example.service.MiaoshaUserService;
import org.example.vo.GoodsDetailVo;
import org.example.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SpringWebConstraintValidatorFactory;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    MiaoshaUserService userService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;


    //登录成功之后的商品列表页面
    //第一次使用jmeter压测5000*10   Throughout  710/sec
    //第二次使用jmeter压测5000*10   Throughout  2800/sec
    @RequestMapping(value = "/to_list",produces = "text/html")
    //@RequestMapping("/to_list")
    @ResponseBody
    public String toList(HttpServletRequest request,HttpServletResponse response,Model model, MiaoshaUser user){
//        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//        MiaoshaUser user = userService.getByToken(response,token);
        model.addAttribute("user",user);

        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        //取缓存
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
        //return "goods_list";


        SpringWebContext ctx = new SpringWebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
        //html不为空，将html页面缓存到redis当中
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }


    //商品的详情界面
    @RequestMapping(value = "/to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request,HttpServletResponse response,Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId){
        model.addAttribute("user",user);

        String html = redisService.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);
        //取缓存
        if(!StringUtils.isEmpty(html)){
            return html;
        }



        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;

        //秒杀未开始
        if(now < startAt){
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt-now)/1000);
        }else if(now > endAt){
            //秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{
            //秒杀正在开始
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        //return "goods_detail";

        SpringWebContext ctx = new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);
        //html不为空，将html页面缓存到redis当中
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);
        }
        return html;
    }

    //商品的详情界面
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user, @PathVariable("goodsId") long goodsId){

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;

        //秒杀未开始
        if(now < startAt){
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt-now)/1000);
        }else if(now > endAt){
            //秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{
            //秒杀正在开始
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setMiaoshaStatus(miaoshaStatus);
        vo.setRemainSeconds(remainSeconds);
        return Result.success(vo);
    }
}
