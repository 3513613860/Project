package com.example.demo.Model;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class File {
    private int id;
    private String filename;
    private long fileSize;
    private String updatedate;
    private String fileType;
    private int uid;
    private String url;
}
