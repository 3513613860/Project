package com.example.demo.controller;

import com.example.demo.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

@Controller //spring 初始化此类
@RequestMapping("/mvc")
@Slf4j
public class MVCController {

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping("/index7")
    @ResponseBody  //返回接口（数据），而非视图
    public String getIndex7() throws JsonProcessingException {
        User user = new User();
        user.setUsername("Java");
        user.setPassword("hello");
        String ret = objectMapper.writeValueAsString(user);
        return ret;
    }

    @RequestMapping("/index")
    public String getIndex(){
        log.error("我是请求重定向");
        return "redirect:/index.html";
    }

    @RequestMapping("/index2")
    public String getIndex2(){
        log.error("我是请求转发");
        return "forward:/index.html";
    }

    @RequestMapping("/index3")
    public String getIndex3(HttpServletResponse response){
        response.setStatus(301);
        response.setHeader("location","/index.html");
        return null;
    }

    @GetMapping("/index4")
    public String getIndex4(){
        return "/index.html";
    }

    @PostMapping("/index5")
    public String getIndex5(){
        return "/index.html";
    }

    @RequestMapping("/index6")
    @ResponseBody
    public String getIndex6(){
        return "/index.html";
    }


}
