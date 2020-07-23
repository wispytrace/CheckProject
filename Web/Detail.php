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
$sql = "Select id,name from Staff";
$stmt = $db->prepare($sql);
$stmt->execute();
$id = null;
$name = null;
$stmt->bind_result($id, $name);
$index = 0;
while ($stmt->fetch()) {
    $dbTemp = new mysqli('localhost', 'root', 'root', 'studiocheck');
    if (mysqli_connect_errno()) {
        $jsonMessage["status"]="-1";
        $jsonMessage["erro"] = "数据库连接失败";
        print_r(json_encode($jsonMessage));
        exit();
    }
    $sql ="Select name,status,intime,outime,lastime from AttendanceRecord,Staff where AttendanceRecord.id=Staff.id and Staff.id = ?
 order by intime DESC";
    $ltmt = $dbTemp->prepare($sql);
    $ltmt->bind_param('i', $id);
    $ltmt->execute();
    $ltmt->bind_result($jsonMessage["data"][$index]["name"], $jsonMessage["data"][$index]["status"], $jsonMessage["data"][$index]["intime"], $jsonMessage["data"][$index]["outime"],
        $jsonMessage["data"][$index]["lastime"]);
//    $ltmt->bind_result($name, $status, $intime, $outime, $lastime);
//    $jsonMessage["data"][$index]["name"] = $name;
//    $jsonMessage["data"][$index]["status"] = $status;
//    $jsonMessage["data"][$index]["intime"] = $intime;
//    $jsonMessage["data"][$index]["outime"] = $outime;
//    $jsonMessage["data"][$index]["lastime"] = $lastime;
    if(!$ltmt->fetch()){
        $jsonMessage["data"][$index]["name"] = $name;
        $jsonMessage["data"][$index]["status"] = 0;
    };
    $index++;
}
$jsonMessage["status"] = "0";
print_r(json_encode($jsonMessage));
?>

