<?php
header('Content-Type: application/json');
$conn = mysqli_connect("localhost","root","","parking_app_database") or die(mysql_error());
$group_id = $_GET['group_id'];
//execute query
$result = $conn->query("SELECT * FROM my_users WHERE group_id ='$group_id' ORDER BY sr_id DESC");
//loop through returned data
$data = array();
foreach ($result as $row) {
	$data[] = $row;
}
//free memory associated with result
$result->close();
mysqli_close($conn);
// print the data
print json_encode($data);
?>