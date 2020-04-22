<?php
header('Content-Type: application/json');
$conn = mysqli_connect("localhost","root","","parking_app_database") or die(mysql_error());

$group_id = $_GET['group_id'];
$user_id = $_GET['user_id'];
$customer_name = $_GET['customer_name'];
$customer_phone = $_GET['customer_phone'];
$customer_car_no = $_GET['customer_car_no'];
$customer_license = $_GET['customer_license'];

if(is_null($group_id))
{
  echo '{"query_result":"FAILURE"}';
}
else {   
    $result = $conn->query("INSERT INTO my_jobs (group_id, user_id, customer_name, customer_phone, customer_car_no, customer_license) VALUES ('$group_id', '$user_id', '$customer_name', '$customer_phone', '$customer_car_no', '$customer_license');");
    if($result == true) {
        echo '{"query_result":"SUCCESS"}';
    } 
    else {
        echo '{"query_result":"FAILURE"}';
    }
}
mysqli_close($conn);
?>