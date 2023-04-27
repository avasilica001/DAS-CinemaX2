<?php
$DB_SERVER = "localhost";
$DB_USER = "Xavasilica001";
$DB_PASS = "fEy8hSnjM";
$DB_DATABASE = "Xavasilica001_CinemaX";

$conn = new mysqli($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$usuario=$_POST["id"];


$sql = "SELECT * FROM Peliculas WHERE subidapor='$usuario'";
$resultado = $conn->query($sql);

$cont=0;

while($row = mysqli_fetch_row($resultado)) {
    	$array[$cont]=array('id'=>$row[0],'nombre'=>$row[1],'anio'=>$row[2],'url'=>$row[3],'valoracion'=>$row[4],'descripcion'=>$row[5],'subidapor'=>$row[6]);
	$cont++;
}

echo json_encode($array);

$conn->close();

?>