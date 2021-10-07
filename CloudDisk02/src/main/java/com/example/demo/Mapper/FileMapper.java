package com.example.demo.Mapper;


import com.example.demo.Model.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {
    public int upload(File file);

    public  int del(int id);

    public int dels(String[] ids);
}
