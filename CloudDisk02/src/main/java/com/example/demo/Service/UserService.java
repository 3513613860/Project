package com.example.demo.Service;


import com.example.demo.Mapper.UserMapper;
import com.example.demo.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    //1.注册
    public int addUser(User user){
        int ret = userMapper.addUser(user);
        return ret;
    }

    //2.登录
    public User login(String name,String password){
        User user = userMapper.login(name,password);
        return user;
    }

    //3.查询用户所有信息和所有文件列表
    public User getAllDetail(int uid,String curname,int skipCount,int psize){
        User user = userMapper.getAllDetail(uid,curname,skipCount,psize);
        return user;
    }

    public User getCount(int uid, String curname) {
        User user = userMapper.getCount(uid,curname);
        return user;
    }

    public User qurey(String name) {
        User user = userMapper.query(name);
        return user;
    }
}
