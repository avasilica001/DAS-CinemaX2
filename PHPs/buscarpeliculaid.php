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


$sql = "SELECT Peliculas.id, Peliculas.nombre, Peliculas.anio, Peliculas.url, Peliculas.valoracion, Peliculas.descripcion, Peliculas.subidapor, Usuarios.fotoperfil FROM Peliculas INNER JOIN Usuarios ON Usuarios.id=Peliculas.subidapor WHERE Peliculas.id='$id'";
$resultado = $conn->query($sql);

$cont=0;

while($row = mysqli_fetch_row($resultado)) {
    	$array[$cont]=array('id'=>$row[0],'nombre'=>$row[1],'anio'=>$row[2],'url'=>$row[3],'valoracion'=>$row[4],'descripcion'=>$row[5],'subidapor'=>$row[6], 'fotoperfil'=>$row[7]);
	$cont++;
}

echo json_encode($array);


$conn->close();

?>