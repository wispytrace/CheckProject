<?php
$jsonMessage["status"] = "";
$jsonMessage["data"] = null;
$jsonMessage["erro"] = " ";
$db = new mysqli('localhost', 'root', 'root', 'studiocheck');
if (mysqli_connect_errno()) {
    $jsonMessage["status"]="-1";
    $jsonMessage["erro"] = "数据库连接失败";
    print_r(json_encode($jsonMessage));
    exit();
}
$sql = "Select name,sno,tutor,team,phone from Staff";
//$stmt->bind_param();
$stmt = $db->prepare($sql);
$stmt->execute();
$name = null;
$sno = null;
$tutor = null;
$team = null;
$phone = null;
$stmt->bind_result($name, $sno, $tutor, $team, $phone);
$index = 0;
while ($stmt->fetch()) {
    if ($tutor==null){
        $tutor = "暂无";
    }
    if ($phone==null){
        $phone = "暂无";
    }
    if ($team==null){
        $team = "暂无";
    }
    $jsonMessage["data"][$index]["name"] = $name;
    $jsonMessage["data"][$index]["sno"] = $sno;
    $jsonMessage["data"][$index]["tutor"] = $tutor;
    $jsonMessage["data"][$index]["team"] = $team;
    $jsonMessage["data"][$index]["phone"] = $phone;
    $index++;
}
$jsonMessage["status"] = "0";
print_r(json_encode($jsonMessage));
?>

