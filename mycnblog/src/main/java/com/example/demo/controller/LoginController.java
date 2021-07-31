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
    public Object getIndex(User user, HttpServletRequest req){
        User user2 = userMapper.getUserByNameAndPassword(user.getUsername(),user.getPassword());
        if(user2 == null){
            user2 = user;
        }else{
            HttpSession session = req.getSession();
            session.setAttribute(AppFinal.USERINFO_SESSIONKEY,user2);
        }
        return user2;
//        Map<String,Object> map = new HashMap<>();
//        int state = 0;
//        String msg = "";
//
//        if(user.getUsername().equals("root") && user.getPassword().equals("root")){
//            HttpSession session = req.getSession();
//            session.setAttribute("userinfo",user);
//            msg = "登录成功";
//            state = 1;
//        }else{
//            msg = "登录失败";
//        }
//        map.put("state",state);
//        map.put("msg",msg);
//        return map;
    }

    @RequestMapping("/login2")
    @ResponseBody
    public Map login2(HttpServletRequest resp,HttpServletRequest req) throws IOException {
        Map<String,Object> map = new HashMap<>();
        String msg = "登录失败";
        int state = -1;

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if(username != null && password != null &&
                username.equals("root") && password.equals("root")){
            msg = "登录成功";
            state = 1;
        }
        map.put("msg",msg);
        map.put("state",state);

        return map;
    }
}
