package com.example.demo.Model;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private int id;
    private String name;
    private String password;
    private String phone;
    private String email;
    private List<File> list;
    private int tcount;
    private int tpage;
}
