<%--
  Created by IntelliJ IDEA.
  User: 张华
  Date: 2021/9/10
  Time: 0:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>注册</title>
</head>
<link href="../../css/register.css" rel="stylesheet" >
<script type="text/javascript" >
    //光标离开账号文本事件
    function checkName(){
        var un=document.getElementById("user_name").value;
        var sun=document.getElementById("namespan");
        var regName=/^\w{5,11}$/;
        if(regName.test(un)){
            return true;
        }else{
            alert("请按合法格式输入");

            return false;
        }
    }
    //光标离开密码文本事件
    function checkPwd(){
        var  up=document.getElementById("user_pwd").value;
        var  sup=document.getElementById("pwdspan");
        var regPWD=/\w{5,15}/;
        if(regPWD.test(up)){

            return true;
        }else{
            alert("请按合法格式输入");
            return false;
        }
    }
    //光标离开确认密码文本事件
    function checkConfirm(){
        var   up=document.getElementById("user_pwd").value;
        var   cup=document.getElementById("user_con").value;
        var  scp=document.getElementById("conspan");

        if(cup==up){

            return true;
        }else{
            alert("密码不一致");

            return false;
        }
    }
    //光标离开邮箱文本事件
    function checkEmail(){
        var ce=document.getElementById("user_email").value;
        var sce=document.getElementById("emailspan");
        var regEmail=/^\w{6,13}@[a-zA-Z0-9]{2,5}\.[a-z]{2,3}$/;
        if(regEmail.test(ce)){

            return true;
        }else{
            alert("请按合法格式输入");
            return false;
        }
    }
    //光标离开电话文本事件
    function checkphone(){
        var ct=document.getElementById("user_phone").value;
        var sct=document.getElementById("phonespan");
        var regPhone=/^1\d{10}$/;
        if(regPhone.test(ct)){

            return true;

        }else{
            alert("请按合法格式输入");
            return false;
        }
    }
    //光标离开按钮事件
    function register(){
        var n=checkName()+checkPwd()+checkConfirm()+checkphone()+checkEmail();
        if(n==5){
             var param=$("#regiserForm").serialize();
            var  url="${basepath}/toRegister";
            $.post(param,url,function (result) {
                 if(result.state()==1){
                     location.href="${basepath}/login";
                 }else{
                     alert("注册失败");
                 }
            });
        }else{
           alert("格式不正确，请检查");

        }


    }
</script>
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>

<body>

<div class="d1" >
    <h1>用户注册</h1>
    <form id="regiserForm">
        <p id="p1">             <!-- onblur:光标离开文本框事件 -->
            账号:<input type="text" placeholder="请输入用户名" id="user_name" onblur="checkName()"/>
            <span id="namespan"></span>
        </p>
        <p id="p2">
            密码:<input type="password" placeholder="请输入密码  "id="user_pwd" onblur="checkPwd()"/>
            <span id="pwdspan"></span>
        </p>
        <p id="p3">
            确认密码:<input type="password" placeholder="请输入确认密码 " id="user_con" onblur="checkConfirm()"/>
            <span id="conspan"></span>
        </p>
        <p id="p4">
            邮箱:<input type="text" placeholder="请输入邮箱" id="user_email" onblur="checkEmail()"/>
            <span id="emailspan"></span>
        </p>
        <p id="p5">             <!-- onblur:光标离开文本框事件 -->
            手机号:<input type="text" placeholder="请输入电话"id="user_phone" onblur="checkphone()"/>
            <span id="phonespan"></span>
        </p>
        <input type="button" value="注册" onclick="register()" id="rr" />
    </form>

</div>

</body>

