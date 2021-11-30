package org.example.controller;

import org.example.entity.User;
import org.example.rabbitmq.MQSender;
import org.example.redis.RedisService;
import org.example.redis.UserKey;
import org.example.result.Result;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class DemoApplication {
    @Autowired
    RedisService redisService;

    @Autowired
    UserService userService;

    @Autowired
    MQSender sender;

//    @RequestMapping("/mq")
//    @ResponseBody
//    public Result<String> mq(){
//        sender.send("hello,mq");
//        return Result.success("Hello,world");
//    }
//
//    @RequestMapping("/mq/topic")
//    @ResponseBody
//    public Result<String> topic(){
//        sender.sendTopic("hello,mq");
//        return Result.success("Hello,world");
//    }
//
//
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public Result<String> fanout(){
//        sender.sendFanout("hello,mq");
//        return Result.success("Hello,world");
//    }
//
//    @RequestMapping("/mq/header")
//    @ResponseBody
//    public Result<String> header(){
//        sender.sendHeader("hello,header");
//        return Result.success("Hello,world");
//    }

    @RequestMapping("/suc")
    @ResponseBody
    public Result<String> success(){
        return Result.success("hello mooc");
    }

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","zhangsan");
        return "hello";
    }

    @RequestMapping("/get")
    @ResponseBody
    public Result<User> get(){
        User user = userService.getById(2);
        return Result.success(user);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet(){
        User user = redisService.get(UserKey.getById,""+1,User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setId(1);
        user.setName("111111");
        redisService.set(UserKey.getById,""+1,user);
        return Result.success(true);
    }
}
