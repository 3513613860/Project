package com.example.demo.mapper;


import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface UserMapper {

    //添加用户方法（注册）
    public int addUser(User user);

    //
    public User getUserById(int id);

    public User getUserByNameAndPassword(String username,String password);

    public List<User> getAll();

    public int delById(int id);

    public int upUser(int id,String username);

    public List<User> getList(String order);

    public List<User> getListByName(String username);

    public List<User> getListByName2(String username);

    public User getFullUser(int id);
}
