//package com.example.demo.Controller;
//
//import com.example.demo.Util.OBSUtils;
//import com.example.demo.common.JsonResult;
//import com.obs.services.model.ObsObject;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.io.InputStream;
//import static java.lang.System.out;
//import  com.example.demo.common.JsonResult;
//
//@Controller
//public class FileController2 {
//    //文件下载页面路由
//    @RequestMapping("/upfile")
//    public String upfile(){
//        return "upfile";//返回upfile.html
//    }
//
//    //文件下载页面路由
//    @RequestMapping("/homepage")
//
//    public String load(){
//        return "homepage";//返回upfile.html
//    }
//
//    @RequestMapping("/register")
//    public String register(){
//        return "register";
//    }
//    @RequestMapping("/login")
//    public String login(){
//        return "login";
//    }
//
//
//    //前端请求路由
//    @RequestMapping("/upload")
//    public  String upload(@RequestParam("file") MultipartFile file, HttpSession Session, HttpServletRequest req, Model model) throws IOException, IOException {
//        if(file==null) return "upfile";
//        //将文件存入obs
//        OBSUtils obs = new OBSUtils();
//        String Key = file.getOriginalFilename();
//        //  out.println(Key);
//        InputStream inputStream = file.getInputStream();
//        obs.ObsUpload("qtre",Key,inputStream);
//        out.println("上传成功"+Key);
////        fileService.query(uuid);
////        model.addAllAttributes("fileList",fileList);
//        return "upfile";//返回upfile.html
//    }
//
//    @RequestMapping("/download")
//    public String download(){
//        return "download";//返回upfile.html
//    }
//
//    @RequestMapping("/downloads")
//    public String downloads( String filename, HttpServletRequest req, HttpServletResponse rep) {
//        //obs下载文件
//        //后台收到前端穿过来的filename(文件名)
//        OBSUtils obs = new OBSUtils();
//        String Key = filename;
//        ObsObject obsObject = obs.getFile(Key);
//        InputStream inputStream = obsObject.getObjectContent();
//        obs.dowloadFile(rep,inputStream,filename);
//        return "redirect:/download";//重定向到当前下载页面
//    }
////    @RequestMapping("/toUpload.do")
////    public  JsonResult<Integer> uploadfile(String filename,double filesize ){
////        return JsonResult<Integer>(n);
////    }
//}
