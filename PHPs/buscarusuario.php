<?php
$DB_SERVER = "localhost";
$DB_USER = "Xavasilica001";
$DB_PASS = "fEy8hSnjM";
$DB_DATABASE = "Xavasilica001_CinemaX";

$conn = new mysqli($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}


$usuario = $_POST["id"];
$contrasenia = $_POST["contrasenia"];

$sql = "SELECT id,contrasenia FROM Usuarios WHERE id='$usuario' AND contrasenia='$contrasenia'";
$resultado = $conn->query($sql);

while($row = $resultado->fetch_assoc()) {
    echo json_encode($row);
}
$conn->close();

?>
