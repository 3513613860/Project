package org.example.service;

import org.example.Dao.UserDao;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(int id){
        return userDao.getById(id);
    }
}
