package com.example.demo.controller;


import com.example.demo.mapper.ArticleInfoMapper;
import com.example.demo.model.ArticleInfo;
import com.example.demo.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/art")
public class ArticleInfoController {

    @Resource
    private ArticleInfoMapper articleInfoMapper;

    //根据文章id删除文章
    @RequestMapping("/del")
    public int delById(@RequestParam int id){
        return articleInfoMapper.delById(id);
    }

    //根据文章id查询文章详情
    @RequestMapping("/detail")
    public ArticleInfo detail(@RequestParam  int id){
        return articleInfoMapper.getDetail(id);
    }

    //添加文章
    @RequestMapping("/add")
    public ArticleInfo add(@RequestParam String title, @RequestParam String content, HttpServletRequest req){
        HttpSession session = req.getSession(false);
        User user = null;
        if(session == null || session.getAttribute("userinfo") == null){
            return null;
        }
        user = (User) session.getAttribute("userinfo");
        int uid = user.getId();
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setTitle(title);
        articleInfo.setContent(content);
        articleInfo.setUid(uid);
        int ret = articleInfoMapper.add(articleInfo);
        int retsult = 0;
        return articleInfo;
    }

    @RequestMapping("/update")
    public int update(ArticleInfo articleInfo){
        return articleInfoMapper.upArticle(articleInfo);
    }

    @RequestMapping("/upcount")
    public int upCount(int id){
        return articleInfoMapper.upCount(id);
    }
}
