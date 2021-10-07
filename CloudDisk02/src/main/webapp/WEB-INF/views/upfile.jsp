<%--
  Created by IntelliJ IDEA.
  User: 张华
  Date: 2021/9/9
  Time: 22:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%--    <title>我的text</title>--%>
</head>

<body>

<div>
    <form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
        <input type="submit" class="btn btn-outline-danger" id="button_file" onclick="return checkfile()" value="上传文件"/>
        <input class="btn btn-outline-primary" type="file" onchange="checkfile()" id="fileupload" name="file"
               onpropertychange="getFileSize(this.value)"/><br/>
        <div id="fileinfo"></div>
    </form>
</div>
</body>

<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>

<%--<script type="text/javascript">--%>
<%--    alert("hello");--%>
<%--    var tipMsg = "建议使用chrome firefox ie等浏览器";--%>
<%--    var browserCfg = {};--%>
<%--    //下面一段鉴别使用者的浏览器--%>
<%--    var ua = window.navigator.userAgent;--%>
<%--    if (ua.indexOf("MSIE") >= 1) {--%>
<%--        browserCfg.ie = true;--%>
<%--    } else if (ua.indexOf("Firefox") >= 1) {--%>
<%--        browserCfg.firefox = true;--%>
<%--    } else if (ua.indexOf("Chrome") >= 1) {--%>
<%--        browserCfg.chrome = true;--%>
<%--    }--%>

<%--    function checkfile() {--%>
<%--        try {--%>
<%--            var obj_file = document.getElementById("fileupload");--%>
<%--            var isvip = ${isvip};--%>
<%--            if (obj_file.value == "") {--%>
<%--                alert("请先选择上传文件");--%>
<%--                return;--%>
<%--            }--%>
<%--            var filesize = 0;--%>
<%--            if (browserCfg.firefox || browserCfg.chrome) {--%>
<%--                filesize = obj_file.files[0].size;  //chrome等浏览器支持这个方法拿到文件大小--%>
<%--            } else if (browserCfg.ie) {--%>
<%--                var obj_img = document.getElementById('tempimg');--%>
<%--                obj_img.dynsrc = obj_file.value;--%>
<%--                filesize = obj_img.fileSize;--%>
<%--            } else {--%>
<%--                alert(tipMsg);--%>
<%--                return false;--%>
<%--            }--%>
<%--            if (filesize == -1) {--%>
<%--                alert(tipMsg);--%>
<%--                return false;--%>
<%--            } else {--%>
<%--                return true;--%>
<%--            }--%>
<%--        } catch (e) {--%>
<%--            alert(e);--%>
<%--            return false;--%>
<%--        }--%>
<%--        var file = $("#button_file")[0].files[0];--%>
<%--        var fileName = file.name;--%>
<%--        var fileSize = parseInt(file.size)/1024;--%>
<%--        fileSize = fileSize.toFixed(2);--%>
<%--        var fileType = file.type;--%>

<%--        var info = '<span>文件名：'+fileName+'</span><br/>';--%>
<%--        info += '<span>文件大小：'+fileSize+'KB</span><br/>';--%>
<%--        info += '<span>文件类型：'+fileType+'</span><br/>';--%>
<%--        alert(fileName);--%>
<%--        alert(fileSize);--%>
<%--        alert(fileType);--%>

<%--        $("#fileinfo").html(info);--%>

<%--        var url="toUpload.do";--%>
<%--         var para={"filesize":filesize,"filename":obj_img};--%>
<%--        $.ajax(url,para,function(result){--%>
<%--            if(result==1){--%>
<%--                alert("上传成功");--%>
<%--            }else{--%>
<%--                alert("上传失败");--%>
<%--            }--%>
<%--        });--%>
<%--    }--%>
<%--</script>--%>
</html>
