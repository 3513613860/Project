package service;

import dao.ArticleInfoDao;
import models.ArticleInfo;
import utils.ResultJSONUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class InitArtServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取前端参数
        int id = Integer.parseInt(request.getParameter("id"));

        //返回信息的变量
        int succ = -1;
        String msg = "";
        ArticleInfo articleInfo = null;

        if(id > 0){
            //2.查询数据库
            ArticleInfoDao articleInfoDao = new ArticleInfoDao();
            try {
                succ = 1;
                articleInfo = articleInfoDao.getArtById(id);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else{
            msg = "参数错误";
        }


        //3.返回信息给前端
        HashMap<String,Object> map = new HashMap<>();
        map.put("succ",succ);
        map.put("msg",msg);
        map.put("articleinfo",articleInfo);
        ResultJSONUtils.writeMap(response,map);
    }
}
