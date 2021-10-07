package com.example.demo.Service;


import com.example.demo.Mapper.FileMapper;
import com.example.demo.Model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FileService {
    @Resource
    FileMapper fileMapper;

    public int upload(File file){
        int ret = fileMapper.upload(file);
        return ret;

    }

    public  int del(int id){
        int ret = fileMapper.del(id);
        return ret;
    }

    public int dels(String[] ids){
        int ret = fileMapper.dels(ids);
        return ret;
    }
}
