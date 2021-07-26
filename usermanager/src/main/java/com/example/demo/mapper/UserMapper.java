package com.example.demo.mapper;

import com.example.demo.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    public UserInfo login(String username, String password);

    public int add(UserInfo userInfo);

    public UserInfo getUser(int uid);

    public int update(UserInfo userInfo);


    public List<UserInfo> getListByPage(String name, String address, String email,
                                        int skipCount, int psize, Integer isadmin);


    //查询满足条件的条数
    public int getCount(String name, String address, String email, Integer isadmin);

    public int del(int id, Integer isadmin);
}
