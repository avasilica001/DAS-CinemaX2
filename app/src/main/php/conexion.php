<?php
$DB_SERVER="ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/"; #la dirección del servidor
$DB_USER="Xavasilica001"; #el usuario para esa base de datos
$DB_PASS="fEy8hSnjM"; #la clave para ese usuario
$DB_DATABASE="Xavasilica001_CinemaX"; #la base de datos a la que hay que conectarse

# Se establece la conexión:
$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);

#Comprobamos conexión
if (mysqli_connect_errno()) {
    echo 'Error de conexion: ' . mysqli_connect_error();
    exit();
}
?>