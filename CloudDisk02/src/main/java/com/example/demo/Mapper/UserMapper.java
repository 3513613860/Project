package com.example.demo.Mapper;

import com.example.demo.Model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    //1.注册
    public int addUser(User user);

    //2.登录
    public User login(String name,String password);

    //3.查询用户所有信息和所有文件列表
    public User getAllDetail(int uid,String curname,int skipCount,int psize);

    public User getCount(int uid, String curname);

    public User query(String name);
}
