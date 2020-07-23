<?php
$jsonMessage["status"] = "";
$jsonMessage["data"] = null;
$jsonMessage["erro"] = " ";

if (!isset($_POST['beginTime']) || !isset($_POST['endTime']) || empty($_POST['beginTime']) || empty($_POST['endTime'])){
    $jsonMessage["status"]="-1";
    $jsonMessage["erro"] = "服务器参数请求出错";
    print_r(json_encode($jsonMessage));
    exit();
}

$beginTime = $_POST['beginTime'];
$endTime = $_POST['endTime'];
$daynum = 7;
$excellent = 0;
$good = 0;
$fair = 0;



$db = new mysqli('localhost', 'root', 'root', 'studiocheck');
if (mysqli_connect_errno()) {
    $jsonMessage["status"]="-1";
    $jsonMessage["erro"] = "数据库连接失败";
    print_r(json_encode($jsonMessage));
    exit();
}
$sql = "Select excellent,good,fair from systemset";
//$stmt->bind_param();
$stmt = $db->prepare($sql);
$stmt->execute();
$stmt->bind_result($excellent, $good, $fair);


$db = new mysqli('localhost', 'root', 'root', 'studiocheck');
if (mysqli_connect_errno()) {
    $jsonMessage["status"]="-1";
    $jsonMessage["erro"] = "数据库连接失败";
    print_r(json_encode($jsonMessage));
    exit();
}
$sql = "Select id,name from Staff";
//$stmt->bind_param();
$stmt = $db->prepare($sql);
$stmt->execute();
$id = null;
$name = null;
$stmt->bind_result($id, $name);
$index = 0;
while ($stmt->fetch()) {

    $totalNum = 0;
    $totalLastime = 0;
    $lastime = 0;
    $isLegal = 0;
    $totalInLegal = 0;
    $dbTemp = new mysqli('localhost', 'root', 'root', 'studiocheck');
    if (mysqli_connect_errno()) {
        $jsonMessage["status"]="-1";
        $jsonMessage["erro"] = "数据库连接失败";
        print_r(json_encode($jsonMessage));
        exit();
    }
    $sql ="Select lastime,isLegal from AttendanceRecord,Staff where AttendanceRecord.id=Staff.id and Staff.id = ? and intime between ? and ?";
    $ltmt = $dbTemp->prepare($sql);
    $ltmt->bind_param('iss', $id, $beginTime, $endTime);
    $ltmt->execute();
    $ltmt->bind_result($lastime, $isLegal);
    while ($ltmt->fetch()){
        $totalLastime += $lastime;
        if ($isLegal!=0){
            $totalInLegal++;
        }else{
            $totalNum++;
        }
    }
    $averageTime = $totalLastime / $daynum;

    $jsonMessage["data"][$index]["name"] = $name;
    $jsonMessage["data"][$index]["totlLastime"] = $totalLastime;
    $jsonMessage["data"][$index]["totalNum"] = $totalNum;
    $jsonMessage["data"][$index]["totalInLegal"] = $totalInLegal;
    if ($averageTime > $excellent){
            $jsonMessage["data"][$index]["evaluate"] = "优秀";
    }else if ($averageTime > $good){
        $jsonMessage["data"][$index]["evaluate"] = "良好";
    }else if ($averageTime > $fair){
        $jsonMessage["data"][$index]["evaluate"] = "及格";
    }else if (($averageTime - 0) < 1e-6){
        $jsonMessage["data"][$index]["evaluate"] = "未记录";
    }else{
        $jsonMessage["data"][$index]["evaluate"] = "未达标";
    }
    $index++;
}
$jsonMessage["status"] = "0";
print_r(json_encode($jsonMessage));
?>
