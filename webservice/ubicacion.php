<?php
require_once 'login.php';
if ($_SERVER['REQUEST_METHOD'] == 'GET') { 
$body = json_decode(file_get_contents("php://input"), true);
$conexion = new mysqli(HOSTNAME, USERNAME, PASSWORD, DATABASE);
$latitud = $_POST['latitud'];
$longitud = $_POST['longitud'];
$tiempo = $_POST['tiempo'];
if ($conexion->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$sql = "insert into tbgps (latitud,longitud,tiempo) values('" .
	$latitud ."','" . $longitud .
	"','" . $tiempo . "')";
if ($conexion->query($sql)===true){
    print json_encode(
            array(
                'estado' => '1',
                'mensaje' => 'Creacion exitosa')
        );
} else{
    print json_encode(
            array(
                'estado' => '2',
                'mensaje' => 'Creacion fallida')
        );
}
$conexion->close();
}
?>
