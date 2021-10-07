package com.example.demo.Controller;

import com.example.demo.Mapper.UserMapper;
import com.example.demo.Model.File;
import com.example.demo.Model.User;
import com.example.demo.Service.UserService;
import com.example.demo.Util.MD5Util;
import com.example.demo.config.AppFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
//    @Resource
//    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private MD5Util md;

    //登录
    @ResponseBody
    @RequestMapping("/login")
    public Object login(@RequestBody User user, HttpServletRequest req){
        User user2 = userService.login(user.getName(),md.getMD5(user.getPassword()));
        if(user2 == null){
            user2 = user;
        }else{
            HttpSession session = req.getSession();
            session.setAttribute(AppFinal.USERINFO_SESSIONKEY,user2);
        }
        return user2;
    }

    //注册
    @ResponseBody
    @RequestMapping("/register")
    public int register(@RequestBody User user){
        User user2 = userService.qurey(user.getName());
        int ret =  -1;
        if(user2 != null){
            ret = 0;
        }else{
            user.setPassword(md.getMD5(user.getPassword()));
            ret = userService.addUser(user);

        }
        return ret;
    }

    //获取所有的文件
    @ResponseBody
    @RequestMapping("/allfile")
    public User getAllFile(int uid,String curname,int cpage, int psize){
        curname = curname.equals("") ? "" : curname;

        //跳过查询的条数
        int skipCount = 0;
        skipCount = (cpage - 1) * psize;

        User user = userService.getAllDetail(uid,curname,skipCount, psize);


        int tcount = 0;
        User total = userService.getCount(uid,curname);
        tcount = total.getList().size();

        //总页数
        int tpage = (int) Math.ceil(tcount / (psize * 1.0));

        user.setTpage(tpage);
        user.setTcount(tcount);

        if(user == null){
            user = new User();
            List<File> curList = new ArrayList<>();
            user.setList(curList);
        }
        user.setId(uid);
        List<File> list = user.getList();

        user.setList(list);
        return user;
    }
}
