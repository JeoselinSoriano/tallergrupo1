<?php

require_once 'login.php';

$conexion = new mysqli(HOSTNAME, USERNAME, PASSWORD, DATABASE);

if ($conexion->connect_error) {

    die("Connection failed: " . $conn->connect_error);

}

$sql = "select * from gpslocation";

$result = $conexion->query($sql);

if ($result->num_rows > 0){

    $datos["estado"] = 1;

    $id = 0;

    while($row = $result->fetch_assoc()){

        $jsonArrayObject = (array('id' => $row['id'], 'latitud' => $row["latitud"], 'longitud' => $row["longitud"], 'tiempo' => $row["tiempo"]));

        $datos["horas"][$id] = $jsonArrayObject;

        $id++;

    }

    print json_encode($datos);

}else{

    print json_encode(

            array(

                'estado' => '2',

                'mensaje' => '0 results')

        );

}

$conexion->close();

?>
