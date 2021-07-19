package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserInfoDao;
import models.UserInfo;
import utils.ResultJSONUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;

public class RegServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取前端参数
        //2.操作数据库
        //3.返回结果给前端

//        response.setCharacterEncoding("utf-8");
//        response.setContentType("application/json");

        int state = -1;
        String msg = "";

        //接收前端参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        PrintWriter printWriter = response.getWriter();
        if(username == null || password == null){
            msg = "参数不正确";
        }else{
            //操作数据库进行插入
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(username);
            userInfo.setPassword(password);

            UserInfoDao userInfoDao = new UserInfoDao();

            try {
                boolean ret = userInfoDao.add(userInfo);
                if(ret){
                    //操作数据库成功
                    state = 200;
                }else{
                    //操作数据库失败
                    state = -100;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        //{"state":100,"msg":msg}
//        printWriter.println("{\"state\":"+state+",\"msg\":\""+msg+"\"}");


        HashMap<String,Object> map = new HashMap<>();
        map.put("state",state);
        map.put("msg",msg);

        //调用统一的方法
        ResultJSONUtils.writeMap(response,map);
    }
}
