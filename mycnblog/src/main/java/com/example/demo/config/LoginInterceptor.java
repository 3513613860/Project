package com.example.demo.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginInterceptor implements HandlerInterceptor {

    //自定义拦截方法，返回结果是boolean
    //为true表示可以访问，为false表示不可以访问
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断session是否有值
        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute("userinfo") != null){
            //response.sendRedirect("login.html");
            return true;
        }
        response.setStatus(401);
        return false;
    }
}
