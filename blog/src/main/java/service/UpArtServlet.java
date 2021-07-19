package service;

import dao.ArticleInfoDao;
import models.ArticleInfo;
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

public class UpArtServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取前端参数
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        //定义返回信息的变量
        int succ = -1;
        String msg = "";


        if(title != null && content != null &&
           !title.equals("") && !content.equals("") && id > 0){
            //2.操作数据库
            ArticleInfoDao articleInfoDao = new ArticleInfoDao();

            try {
                succ = articleInfoDao.upart(title,content,id);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }else{
            msg = "非法操作，参数不完整";
        }



        //3.返回信息给前端
        HashMap<String,Object> map = new HashMap<>();
        map.put("succ",succ);
        map.put("msg",msg);
        ResultJSONUtils.writeMap(response,map);
    }
}
