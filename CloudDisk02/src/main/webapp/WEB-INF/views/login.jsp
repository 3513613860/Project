<%--
  Created by IntelliJ IDEA.
  User: 张华
  Date: 2021/9/9
  Time: 23:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>登录</title>
</head>
<link href="../../css/login.css" rel="stylesheet" >
<script type="text/javascript" >
    function check1(){
        var id=document.getElementById("t1").value;

        var regName=/^\w{5,11}$/;
        if(!regName.test(id)){

            alert("账号格式不正确");
            return false;
        }else{
            return true;
        }
    }
    function check2(){
        var pwd=document.getElementById("t2").value;
        var regPwd=/^\w{5,11}$/;
        if(!regPwd.test(pwd)){

            alert("密码格式不正确");
            return false;
        }else{
            return true;
        }
    }



    function tj(){

        var che=check1()+check2();
        if(che==2){
            var param=$("#loginForm").serialize();
            var url="${basepath}/login.do";
            $.post(param,url,function (result) {
                   if(result.state()==1){
                       location.href="${basepath}/homepage";
                   }
            });
        }else{
            alert("登录失败");
        }
    }



</script>
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>

<body>



<div id="d1">
    <form id="loginForm">
        <div class="c1">
            <p class="f0">账号登录</p>
            <p class="f1"><img id="img2" src="img/user.jpg"/><input type="text" id="t1" placeholder="请输入手机号或者邮箱" onblur="check1()"></p>
            <p class="f2"><img id="img3" src="img/pwd.jpg"/><input type="password" id="t2" placeholder="请输入密码" onblur="check2()"></p>
            <input type="button" value="登录" class="login" onclick="tj()" id="login">
            <p id="reg"><a href="${basepath}/register">注册账号</a>
        </div>
    </form>
</div>


</body>
</html>

