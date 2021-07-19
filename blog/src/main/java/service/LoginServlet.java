package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserInfoDao;
import models.UserInfo;
import utils.DBUtils;
import utils.ResultJSONUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int state = -1;
        String msg = "";

        //1.获取前端参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if(username == null || password == null){
            msg = "参数不正确";
        }else{
            // 2.执行数据库操作
            UserInfoDao userInfoDao = new UserInfoDao();
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(username);
            userInfo.setPassword(password);
            try {
                userInfo = userInfoDao.getUserInfo(userInfo);
                if(userInfo.getId() >= 1){
                    //表示登录成功
                    state = 200;

                    //如果登录成功，需要创建一个session信息
                    HttpSession session = request.getSession();
                    session.setAttribute("userinfo",userInfo);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        //3.返回数据给前端
        // ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,Object> map = new HashMap<>();
        map.put("state",state);
        map.put("msg",msg);

        ResultJSONUtils.writeMap(response,map);
    }
}
