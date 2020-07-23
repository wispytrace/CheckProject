<?php
$db =new mysqli('localhost', 'root', 'root', 'studiocheck');
if (mysqli_connect_errno()) {
    echo "<script>
            alert('数据库连接失败');
          </script>";
    exit();
}
$sql ="Select name,intime,outime,lastime,isLegal from AttendanceRecord,Staff where AttendanceRecord.id=Staff.id 
 order by intime DESC limit 0, 100";

//$stmt->bind_param();
$stmt = $db->prepare($sql);
$stmt->execute();
$name = null;
$intime = null;
$outime = null;
$lastime = null;
$isLegal = null;
$stmt->bind_result($name, $intime, $outime, $lastime, $isLegal);
echo
'
<table border="1px" style="background-color: antiquewhite; border-collapse: collapse;margin: auto;width: 800px;height: auto" align="center">
<tr><caption align="top">实验室人员考勤信息记录</caption>
    <td>姓名</td>
    <td>进入时间</td>
    <td>离开时间</td>
    <td>持续时间</td>
    <td>状态</td>
</tr>
';
while ($stmt->fetch()){
    echo '<tr>';
    echo '<td>'.$name.'</td>';
    echo '<td>'.$intime.'</td>';
    echo '<td>'.$outime.'</td>';
    echo '<td>'.$lastime.'</td>';
    if ($isLegal==1) {
        echo '<td style="color: red"> 异常</td>';
    }else{
        echo '<td>正常</td>';
    }
    echo '</tr>';
}
echo '</table>'
?>
