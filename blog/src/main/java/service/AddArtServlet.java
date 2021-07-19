package service;

import dao.ArticleInfoDao;
import dao.UserInfoDao;
import models.UserInfo;
import utils.ResultJSONUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class AddArtServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取前端参数
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        //定义返回参数
        int succ = -1;
        String msg = "";

        //非空效验，永远不要相信前端
        if(title != null && content != null &&
                !title.equals("") && !content.equals("")){

            //获取uid
            HttpSession session = request.getSession(false);
            if(session != null && session.getAttribute("userinfo") != null){
                ArticleInfoDao articleInfoDao = new ArticleInfoDao();
                UserInfo userInfo = (UserInfo)session.getAttribute("userinfo");
                try {
                    succ = articleInfoDao.add(title,content,userInfo.getId());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }else{
                msg = "非法请求，非登录状态";
            }

        }else{
            msg = "非法请求，非登录状态";
        }


        //3.返回数据给前端
        HashMap<String,Object> map = new HashMap<>();
        map.put("succ",succ);
        map.put("msg",msg);
        ResultJSONUtils.writeMap(response,map);
    }
}
