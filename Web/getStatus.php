<?php
$db =new mysqli('localhost', 'root', 'root', 'studiocheck');
if (mysqli_connect_errno()) {
    echo "<script>
            alert('数据库连接失败');
          </script>";
    exit();
}
$sql ="Select status,name from Staff";
//$stmt->bind_param();
$stmt = $db->prepare($sql);
$stmt->execute();
$status = null;
$name = null;
$stmt->bind_result($status, $name);
echo
'
<table border="1px" style="background-color: antiquewhite; border-collapse: collapse;margin: auto;width: 800px;" align="center">
<caption align="top">实验室人员在线情况</caption>
<tr>
    <td>姓名</td>
    <td>状态</td>
</tr>
';
while ($stmt->fetch()){
    echo '<tr>';
    echo '<td>'.$name.'</td>';
    if ($status==0) {
        echo '<td> 离线</td>';
    }else{
        echo '<td style="color: red">在线</td>';
    }
    echo '</tr>';
}
echo '</table>'
?>
