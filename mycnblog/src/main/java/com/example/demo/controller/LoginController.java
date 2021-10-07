package com.example.demo.controller;

import com.example.demo.config.AppFinal;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userlogin")
public class LoginController {

    @Resource
    private UserMapper userMapper;

    @RequestMapping("/login")
    @ResponseBody
    public Object getIndex(@RequestBody User user, HttpServletRequest req){
        User user2 = userMapper.getUserByNameAndPassword(user.getUsername(),user.getPassword());
        if(user2 == null){
            user2 = user;
        }else{
            HttpSession session = req.getSession();
            session.setAttribute(AppFinal.USERINFO_SESSIONKEY,user2);
        }
        return user2;
}
