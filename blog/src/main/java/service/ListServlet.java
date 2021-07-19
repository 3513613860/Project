package service;

import dao.ArticleInfoDao;
import models.ArticleInfo;
import models.vo.ArticleInfoVO;
import utils.ResultJSONUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@WebServlet("/list")
public class ListServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int succ = -1;
        String msg = "";
        List<ArticleInfoVO> list = null;
        List<ArticleInfoVO> list2 = null;
        int total = 0;

        //1.从前端获取参数
        //当前页码
        int curpage = Integer.parseInt(req.getParameter("cpage"));

        //每页显示条数
        int psize = Integer.parseInt(req.getParameter("psize"));

        ArticleInfoDao articleInfoDao = new ArticleInfoDao();
        try {
            //2.操作数据库，执行相应的业务逻辑
            list = articleInfoDao.getListByPage(curpage,psize);
            list2 = articleInfoDao.getList();
            total = list2.size();
            succ = 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //3.返回信息给后端
        HashMap<String, Object> map = new HashMap<>();
        map.put("succ",succ);
        map.put("msg",msg);
        map.put("list",list);
        map.put("total",total);
        ResultJSONUtils.writeMap(resp,map);

    }
}
