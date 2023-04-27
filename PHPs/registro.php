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
$contrasenia = $_POST["contrasenia"];
$correo = $_POST["correo"];
$telefono = intval($_POST["telefono"]);
$nombreapellido = $_POST["nombreapellido"];
$foto=$_POST["fotoperfil"];
$token=$_POST["token"];


$sql = "INSERT INTO Usuarios (id, contrasenia, correo, telefono, nombreapellido, fotoperfil, token) VALUES ('$id', '$contrasenia', '$correo', '$telefono', '$nombreapellido', '$foto', '$token')";

if ($conn->query($sql) === TRUE) {
    echo "New user created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();

?>
