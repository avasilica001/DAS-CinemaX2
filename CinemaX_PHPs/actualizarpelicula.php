<?php
$DB_SERVER = "localhost";
$DB_USER = "Xavasilica001";
$DB_PASS = "fEy8hSnjM";
$DB_DATABASE = "Xavasilica001_CinemaX";

$conn = new mysqli($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$id=$_POST["id"];
$nombre=$_POST["nombre"];
$anio=$_POST["anio"];
$url=$_POST["url"];
$valoracion=$_POST["valoracion"];
$descripcion=$_POST["descripcion"];


$sql = "UPDATE Peliculas SET nombre='$nombre', anio='$anio', url='$url', valoracion=$valoracion, descripcion='$descripcion' WHERE id='$id'";
$resultado = $conn->query($sql);

if ($resultado === TRUE) {
  echo "UPDATE correcto";
} else {
  echo "Error";
}



$conn->close();

?>