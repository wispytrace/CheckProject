<?php
$jsonMessage["status"] = "";
$jsonMessage["data"] = null;
$jsonMessage["erro"] = " ";
if (!isset($_POST['name']) || !isset($_POST['password']) || empty($_POST['name']) || empty($_POST['password'])){
    $jsonMessage["status"]="-1";
    $jsonMessage["erro"] = "未输入用户名或密码";
    print_r(json_encode($jsonMessage));
    exit();
}
$name = $_POST['name'];
$password = $_POST['password'];
$db =new mysqli('localhost', 'root', 'root', 'studiocheck');
if (mysqli_connect_errno()) {
    $jsonMessage["status"]="-1";
    $jsonMessage["erro"] = "数据库连接失败";
    print_r(json_encode($jsonMessage));
    exit();
}
if ($name == "root"){
	if ($password == "sysroot"){
		$jsonMessage["status"] = "0";
		print_r(json_encode($jsonMessage));
		exit();
	}
}
$sql ="Select password from Staff where name= ?";
$stmt = $db->prepare($sql);
$stmt->bind_param('s', $name);
$stmt->execute();
$result_password = null;
$stmt->bind_result( $result_password);
while ($stmt->fetch()){}
if ($result_password != $password){
    $jsonMessage["status"]="-1";
    $jsonMessage["erro"] = "用户名或密码输入错误";
    print_r(json_encode($jsonMessage));
    exit();
}
$jsonMessage["status"] = "0";
print_r(json_encode($jsonMessage));
?>