package com.example.demo.Controller;

import com.example.demo.Mapper.FileMapper;
import com.example.demo.Model.File;
import com.example.demo.Model.User;
import com.example.demo.Service.FileService;
import com.example.demo.Util.OBSUtils;
import com.obs.services.model.ObsObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static java.lang.System.out;

@Controller
@RequestMapping("/file")
public class FileController {
//    @Resource
//    private FileMapper fileMapper;

    @Autowired
    FileService fileService;

    //上传文件
    @RequestMapping("/upload")
    public String upload(@RequestParam("filename") MultipartFile fileUpload, HttpServletRequest req, RedirectAttributes attr) throws IOException {
        //1.获取文件名
        String fileName = fileUpload.getOriginalFilename();

        //2.获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".")+1);

        //3.获取文件大小
        long size = fileUpload.getSize();

        //4.创建文件对象
        File curFile = new File();
        curFile.setFilename(fileName);
        curFile.setFileSize(size);

        //创建日期对象
        Date date = new Date();

        //时间格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);

        //设置文件的属性
        curFile.setUpdatedate(time);
        curFile.setFileType(suffixName);

        HttpSession session = req.getSession(false);
        User user = null;
        if(session == null || session.getAttribute("userinfo") == null){
            return null;
        }

        //获取用户的uid
        user = (User) session.getAttribute("userinfo");
        int uid = user.getId();
        curFile.setUid(uid);
        int ret = fileService.upload(curFile);

        //上传文件的obs操作
        OBSUtils obs = new OBSUtils();
        String Key = fileUpload.getOriginalFilename();
        //  out.println(Key);
        InputStream inputStream = fileUpload.getInputStream();
        obs.ObsUpload("qtre",Key,inputStream);
        out.println("上传成功"+Key);

        //打印文件的信息
        System.out.println(curFile.getFilename());
        System.out.println(curFile.getFileSize());
        System.out.println(curFile.getFileType());
        System.out.println(curFile.getUpdatedate());
        System.out.println(curFile.getUid());

        //重定向操作
        attr.addAttribute("uid",uid);
        return "redirect:/list.html";
    }

    //单个文件的删除
    @ResponseBody
    @RequestMapping("/del")
    public int del(int  id){
        int ret = fileService.del(id);
        return ret;
    }


    //下载
    @RequestMapping("/download")
    public String download(@RequestParam("name") String filename, HttpServletResponse rep,HttpServletRequest req, RedirectAttributes attr){
        HttpSession session = req.getSession(false);
        User user = null;
        if(session == null || session.getAttribute("userinfo") == null){
            return null;
        }

        //获取用户的uid
        user = (User) session.getAttribute("userinfo");
        int uid = user.getId();
        out.println(filename);
        OBSUtils obs = new OBSUtils();
        ObsObject obsObject = obs.getFile(filename);
        InputStream inputStream = obsObject.getObjectContent();
        obs.dowloadFile(rep,inputStream,filename);
        attr.addAttribute("uid",uid);
        return "redirect:/list.html";
    }

    //分享
    @ResponseBody
    @RequestMapping("/share")
    public Object share(String filename){
        String url = "";
        if(filename != null && filename != ""){
            String bucketName = "qtre";
            String objectkey = filename;
            url = OBSUtils.shareFile(bucketName,objectkey);
            System.out.println(url);
        }
        File file = new File();
        file.setUrl(url);
        return file;
    }


    //多条数据删除
    @ResponseBody
    @RequestMapping("/dels")
    public int dels(String ids) {
        int result = 0;
        ids = ids.substring(0,ids.length()-1);
        String[] idCount = ids.split(",");
        out.println(idCount);
        result = fileService.dels(idCount);
        return result;
    }
}
