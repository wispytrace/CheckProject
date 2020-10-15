<?php

if (!isset($_POST['name']) || !isset($_POST['password']) || empty($_POST['name']) || empty($_POST['password'])){
    echo "<script>
            alert('用户名或密码未填写');
            window.location.href=\"login.html\";
          </script>";
    exit();
}
$name = $_POST['name'];
$passwprd = $_POST['password'];
$db =new mysqli('localhost', 'root', 'root', 'studiocheck');
if (mysqli_connect_errno()) {
    echo "<script>
            alert('数据库连接失败');
            window.location.href=\"login.html\";
          </script>";
    exit();
}
$sql ="Select password from Staff where name= ?";
$stmt = $db->prepare($sql);
$stmt->bind_param('s', $name);
$stmt->execute();
$result_password = null;
$stmt->bind_result( $result_password);
while ($stmt->fetch()){}
if ($result_password != $passwprd){
    echo "<script>
            alert('用户名或密码输入错误');
            window.location.href=\"login.html\"
          </script>";
    exit();
}

echo "<script>alert('欢迎您,登陆成功!')</script>"
?>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8" name="viewport" content="width=device-width"/>

    <title>USTC振动控制与运动体控制实验室考勤系统</title>
    <script src="js/jquery.min.js"></script>
    <style type="text/css">
    *{
        margin: 0;
        padding: 0;
    }
    #foot {

        width: 100%;
        height: 10%;
        background-color: #CC9933;
        position: relative;
    }
    #foot .img{
        text-align: center;
        font-size: 14px;
        color: #FFFFFF;
    }
    html, body {
        height: 100%;
        background-color: antiquewhite;
    }
    .user{
        width: 100%;
        height: 10%;
        background-image: url("logoheader.png");
        /*background-repeat: repeat;*/
    }
    .login_name{
        color:white;
        padding-right:50px;
        float:right;
        padding-top:30px;
    }
    .navigation{
        width: 10%;
        height:80%;
        background-color:wheat;
        /*position: relative;*/
        /*display: block;*/
        display: inline-block;
        float: left;
    }
    .SelectButton{
        width: 100%;
        height: 10%;
        background-color:mediumaquamarine;

    }
    .SelectButton p{
        text-align: center;
        padding-top: 10%;
    }
    .context{
        width:90%;
        height:80%;
        background-color:antiquewhite;
        display: inline-block;
        overflow-y: auto;
    }
    </style>
</head>
<body onload="showMessage(0)">
<div class="user">
    <p class="login_name"><?php echo $_POST['name']?>,欢迎您[<a style="color: yellow"  href="login.html">退出</a>]</p>
    <div style="clear: both"></div>
</div>
<div class="navigation">
    <div class="SelectButton" onclick="showMessage(0)">
        <a href="javascript:;" style="text-decoration: none"> <p>人员在线情况</p> </a>
    </div>
    <hr>
    <div class="SelectButton" onclick="showMessage(1)">
        <a href="javascript:;" style="text-decoration: none"> <p>人员考勤明细</p> </a>
    </div>
</div>
<div class="context">
</div>
<div style="clear: both"></div>
<div class="footer" id="foot">

    <div class="img">
        <p>Copyright © 2019. All Rights Reserved.</p>

        <div class="img">

            <i class="icon"></i><span>联系邮箱：1025489007@qq.com</span>

        </div>
        <div class="img">

            <i class="icon1"></i><span>联系地址：中国科学技术大学</span>

        </div>
        <div class="img">

            <i class="icon2"></i><span>联系电话：17305690156</span>

        </div>
    </div>

</div>
</body>
<script>
    function showMessage(select) {
        content =  document.getElementsByClassName("context")[0];
        if (window.XMLHttpRequest)    {        // IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
            xmlhttp=new XMLHttpRequest();
        } else {                              // IE6, IE5 浏览器执行代码
            xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange=function(){
            if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                content.innerHTML=xmlhttp.responseText;
            }
        }
        if (select == 0) {
            xmlhttp.open("GET", "getStatus.php", true);
        }
        else if (select == 1){
            xmlhttp.open("GET", "getCheck.php", true);
        }
        xmlhttp.send();
    }
</script>
</html>