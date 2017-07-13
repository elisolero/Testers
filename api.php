<?php
 
 header('Content-type:application/json;charset=utf-8');

// connect to the mysql database
$mysqli = mysqli_connect('localhost', '*****', '******', '****');
mysqli_set_charset($mysqli,'utf8');

//Security
$country = $mysqli->real_escape_string($_GET['country']);
$deviceIds = $mysqli->real_escape_string($_GET['deviceIds']);

//IF statement for GET vars
$query_country = ($country != null) ? "WHERE country = '$country'" : null;
$query_deviceIds = ($deviceIds != null) ? "AND a.deviceId IN ( $deviceIds )" : null;

//#Users GET query
$sql_getUsers = "SELECT testerId FROM `testers` $query_country";

// excecute SQL statement
$result_getUsers = mysqli_query($mysqli,$sql_getUsers);

// die if SQL statement failed
if (!$result_getUsers) {
    http_response_code(404);
    die(mysqli_error());
}

echo '[';
// Now loop for each user
while($r = mysqli_fetch_assoc($result_getUsers)) {

    $testerId = $r['testerId'];

    $sql = "SELECT a.description,a.deviceId, COUNT(c.bugId)
        FROM devices AS a
        LEFT JOIN tester_device AS b
        ON a.deviceId = b.deviceId
        LEFT JOIN bugs AS c
        ON c.deviceId = a.deviceId AND c.testerId = $testerId
        WHERE b.testerId = $testerId
		$query_deviceIds
        GROUP BY a.description
        ";
  
        // excecute SQL statement
        $result = mysqli_query($mysqli,$sql);

        // die if SQL statement failed
        if (!$result) {
            http_response_code(404);
            die(mysqli_error());
        }
        
        //LOOP results
        $rows = array();
        while($r = mysqli_fetch_assoc($result)) {
            $rows[] = $r;
        }

        $arr = array($testerId=>$rows);
        $json_string = json_encode($arr , JSON_PRETTY_PRINT);

        print $json_string . ',';
}

echo ']';

  
mysqli_close($link);