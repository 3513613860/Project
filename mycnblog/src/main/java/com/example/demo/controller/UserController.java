package com.example.demo.controller;

import com.example.demo.config.AppFinal;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.ArticleInfo;
import com.example.demo.model.User;
import com.example.demo.tool.HtmlText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.swing.text.html.HTML;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {


    @Value("${myimgpath}")
    private String imgPath;

    @Resource
    private UserMapper userMapper;
    //创建一个当前日志对象
     private Logger logger =  LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/index")
    public String getIndex(String name){
        if(name == null || name.equals("")){
            logger.error("我的日志信息，级别：error");
        }

        logger.warn("我的日志信息，级别：warn");
        logger.info("我的日志信息，级别：info");
        logger.debug("我的日志信息，级别：debug");
        logger.trace("我的日志信息，级别：trace");
        return "你好，SpringBoot";
    }



    @RequestMapping("getalist")
    @ResponseBody
    public Object getFullUser(int uid){
        User user = userMapper.getFullUser(uid);
        //对文章的正文进行截取操作
        List<ArticleInfo> list = user.getAlist();
        for(ArticleInfo item:list){
            //去除html标签
            String desc = HtmlText.Html2Text(item.getContent());
            //截取字符串
            if(desc.length() > 64){
                item.setContent(desc.substring(0,64)+"...");
            }else{
                item.setContent(desc);
            }
        }
        user.setAlist(list);
        return user;
    }


    @RequestMapping("/register")
    public String register(String username,String password, @RequestPart MultipartFile file) throws IOException {
        //1.动态获取当前项目的路径
//        String path = ClassUtils.getDefaultClassLoader().
//                getResource("static").getPath();

        String path = imgPath;
        //path += AppFinal.IMAGE_PATH;
        logger.warn("path: "+path);

        //2.文件名(全局唯一id（时间戳不行）) 全局唯一id（UUID）+文件原始类型
        String fileType = file.getOriginalFilename();
        fileType = fileType.substring(fileType.lastIndexOf("."));
        String filename = UUID.randomUUID().toString()+fileType;

        //3.将文件保存到服务器
        file.transferTo(new File(path+filename));
        //将用户信息保存到服务器
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        //设置头像地址
        user.setPhoto(AppFinal.IMAGE_PATH + filename);
        int ret = userMapper.addUser(user);
        if(ret > 0){
            return "redirect:/reg_success.html";
        }else{
            return "redirect:/reg_error.html";
        }
    }

    @RequestMapping("/getcookie")
    @ResponseBody
    public Object getCoolie(@CookieValue("mysessionid") String cookieid){
        return cookieid;
    }

    @RequestMapping("/test")
    @ResponseBody
    public Object test(){
        int k = 1/0;
        return null;
    }
}
