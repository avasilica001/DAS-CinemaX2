<?php
$DB_SERVER = "localhost";
$DB_USER = "Xavasilica001";
$DB_PASS = "fEy8hSnjM";
$DB_DATABASE = "Xavasilica001_CinemaX";

$conn = new mysqli($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$nombre=$_POST["nombre"];
$anio=$_POST["anio"];
$url=$_POST["url"];
$valoracion=$_POST["valoracion"];
$descripcion=$_POST["descripcion"];
$subidapor=$_POST["subidapor"];


$sql = "INSERT INTO Peliculas (nombre, anio, url, valoracion, descripcion, subidapor) VALUES ('$nombre', '$anio', '$url', $valoracion, '$descripcion', '$subidapor')";
$resultado = $conn->query($sql);

if ($resultado === TRUE) {
  echo "INSERT correcto";
} else {
  echo "Error";
}



$conn->close();

?>