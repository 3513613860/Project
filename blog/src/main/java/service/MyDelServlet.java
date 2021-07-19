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

public class MyDelServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.从前端获取参数
        int id = Integer.parseInt(request.getParameter("id"));
        int succ = -1;
        String msg = "";

        //2.操作数据库
        if(id >= 1){
            ArticleInfoDao articleInfoDao = new ArticleInfoDao();
            boolean ret = false;
            try {
                ret = articleInfoDao.del(id);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            if(ret){
                succ = 1;
            }else{
                msg = "删除失败";
            }
        }else{
            msg = "参数无效";
        }


        //3.返回数据给前端
        HashMap<String,Object> map = new HashMap<>();
        map.put("succ",succ);
        map.put("msg",msg);
        ResultJSONUtils.writeMap(response,map);
    }
}
