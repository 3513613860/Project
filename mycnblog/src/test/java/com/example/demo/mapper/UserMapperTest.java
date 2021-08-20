package com.example.demo.mapper;

import com.example.demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    void addUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123456");
        int ret = userMapper.addUser(user);
        System.out.println(ret);
    }

    @Test
    void getUserById() {
        int id = 19;
        System.out.println(userMapper.getUserById(id));
    }

    @Test
    void getUserByNameAndPassword() {
        String username = "123";
        String password = "123";
        User user = userMapper.getUserByNameAndPassword(username,password);
        System.out.println(user);
    }


    @Test
    void upUser() {
        int id = 19;
        String username = "999";
        System.out.println(userMapper.upUser(id,username));
    }
}
