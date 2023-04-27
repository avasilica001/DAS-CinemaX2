<?php
$DB_SERVER = "localhost";
$DB_USER = "Xavasilica001";
$DB_PASS = "fEy8hSnjM";
$DB_DATABASE = "Xavasilica001_CinemaX";

$conn = new mysqli($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}


$id = $_POST["id"];

$sql = "DELETE FROM Peliculas WHERE id='$id'";
$resultado = $conn->query($sql);

echo $resultado;

$conn->close();

?>