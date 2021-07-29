package com.example.demo.tools;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {
    public static UserInfo getUserBySession(HttpServletRequest req){
        HttpSession session = req.getSession(false);
        UserInfo userInfo = null;
        if(session != null &&
            session.getAttribute(AppFinal.USERINFO_SESSION_KEY) != null){
            userInfo = (UserInfo) session.getAttribute(AppFinal.USERINFO_SESSION_KEY);
        }
        return userInfo;
    }
}
