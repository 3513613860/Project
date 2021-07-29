package com.example.demo.Controller;


import com.example.demo.mapper.UserMapper;
import com.example.demo.model.UserInfo;
import com.example.demo.tools.AppFinal;
import com.example.demo.tools.ResponseBody;
import com.example.demo.tools.SessionUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

//controller+ResponseBody
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @RequestMapping("/login")
    public ResponseBody<UserInfo> login(@RequestParam String username, @RequestParam String password, HttpServletRequest req) {
        //UserInfo userInfo = new UserInfo();
        //return userMapper.login(username,password)== null ?userInfo:userMapper.login(username,password);

        UserInfo userInfo = userMapper.login(username, password);
        int status = -1;
        String message = "用户名或密码输入错误";
        if (userInfo != null) {
            status = 0;
            message = "";
            //将用户信息存放到session中
            HttpSession session = req.getSession();
            session.setAttribute(AppFinal.USERINFO_SESSION_KEY, userInfo);
        }
        return new ResponseBody<>(status, message, userInfo);
    }


    //查询当前登录用户的权限
    @RequestMapping("/checkadmin")
    public ResponseBody<UserInfo> checkAdmin(HttpServletRequest req) {
        int status = -1;
        HttpSession session = req.getSession(false);
        UserInfo userInfo = null;
        if (session != null && session.getAttribute(AppFinal.USERINFO_SESSION_KEY) != null) {
            userInfo = (UserInfo) session.getAttribute(AppFinal.USERINFO_SESSION_KEY);
        }
        return new ResponseBody<>(status, "", userInfo);
    }

    //添加用户方法
    @RequestMapping("/add")
    public ResponseBody<Integer> add(UserInfo userInfo, HttpServletRequest req) {
        int status = 0;
        String message = "";
        int data = 0;
        UserInfo user = SessionUtil.getUserBySession(req);
        if (user == null) {
            status = -1;
            message = "未登录";
        } else if (userInfo.getIsadmin() == 1) {
            if (user.getIsadmin() == 0) {
                status = -2;
                message = "登录权限不够";
            } else if (user.getIsadmin() == 1) {
                data = userMapper.add(userInfo);
            }
        } else if (userInfo.getIsadmin() == 0) {
            data = userMapper.add(userInfo);
        }
        return new ResponseBody<>(status, message, data);
    }

    @RequestMapping("/getuser")
    public ResponseBody<UserInfo> getUser(@RequestParam int uid) {
        int status = -1;
        String message = "未知错误";
        UserInfo userInfo = userMapper.getUser(uid);
        if (userInfo != null) {
            status = 0;
        }
        return new ResponseBody<>(status, message, userInfo);
    }

    @RequestMapping("/update")
    public ResponseBody<Integer> update(UserInfo userInfo) {
        int data = 0;
        data = userMapper.update(userInfo);
        return new ResponseBody<>(0, "", data);
    }

    @RequestMapping("/list")
    public ResponseBody<HashMap<String, Object>> getList(String name, String address, String email,
                                                         int cpage, int psize, HttpServletRequest req) {

        //权限效验

        UserInfo userInfo = SessionUtil.getUserBySession(req);
        if (userInfo == null) {
            return new ResponseBody<>(-1, "当前未登录", null);
        }
        Integer isadmin = null;
        if (userInfo.getIsadmin() == 0) {
            isadmin = 0;
        }


        name = name.equals("") ? null : name;
        address = address.equals("") ? null : address;
        System.out.println(address);
        email = email.equals("") ? null : email;

        //跳过查询的条数
        int skipCount = 0;
        skipCount = (cpage - 1) * psize;
        List<UserInfo> list = userMapper.getListByPage(name, address, email, skipCount, psize, isadmin);

        //查询满足条件的总条数
        int tcount = userMapper.getCount(name, address, email, isadmin);
        //总页数
        int tpage = (int) Math.ceil(tcount / (psize * 1.0));
        HashMap<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("tcount", tcount);
        data.put("tpage", tpage);
        return new ResponseBody<>(0, "", data);
    }


    @RequestMapping("/del")
    public ResponseBody<Integer> del(int id, HttpServletRequest req) {
        //权限效验
        UserInfo userInfo = SessionUtil.getUserBySession(req);
        if (userInfo == null) {
            return new ResponseBody<>(-1, "未登录", 0);
        }

        //判断删除的是否是自己
        if (id == userInfo.getId()) {
            return new ResponseBody<>(-2, "不能删除当前登录用户", -1);
        }

        Integer isadmin = null;
        if (userInfo.getIsadmin() == 0) {
            isadmin = 0;
        }
        int data = userMapper.del(id, isadmin);
        System.out.println(data);
        return new ResponseBody<>(-1, "", data);
    }


    //多条数据删除
    @RequestMapping("/dels")
    public ResponseBody<Integer> dels(String ids) {
        int result = 0;
        String[] idCount = ids.split(",");
        result = userMapper.dels(idCount);
        return new ResponseBody<>(0, "", result);
    }
}
