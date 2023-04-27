<?php 
$DB_SERVER = "localhost";
$DB_USER = "Xavasilica001";
$DB_PASS = "fEy8hSnjM";
$DB_DATABASE = "Xavasilica001_CinemaX";

$conn = new mysqli($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$cabecera= array('Authorization: key=AAAAr-ucSRM:APA91bEQtNlrelvvhH-eqJ8wfNn1cfkys5HiUJtFi8I-vAfsWD_4ZCnu-RuSz8iKMP3jPTTebzEGiThlDmtsEypopg8cWDQDIqvwdMyHhzWC2p4vKHPcdV92nVH4QriH9EqAKmSFNIpl', 'Content-Type: application/json');

$sql="SELECT DISTINCT token FROM Usuarios";
$resultado = $conn->query($sql);


while($row = mysqli_fetch_row($resultado)) {

	// Cuerpo de la solicitud HTTP POST
	$msg = array( 'to' => $row[0], 'notification' => array( 'title' => "Prueba CinemaX Nofiticacion", 'body' => "Prueba CinemaX Notificacion",'sound' => 'default'));

	$msgJSON= json_encode( $msg);

	$ch= curl_init(); #inicializar el handlerde curl 
	#indicar el destino de la petición, el servicio FCM de google 
	curl_setopt( $ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send'); 
	#indicar que la conexión es de tipo POST 
	curl_setopt( $ch, CURLOPT_POST, true); 
	#agregar las cabeceras 	
	curl_setopt( $ch, CURLOPT_HTTPHEADER, $cabecera); 
	#Indicar que se desea recibir la respuesta a la conexión en forma de string 
	curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true); 
	#agregar los datos de la petición en formato JSON 
	curl_setopt( $ch, CURLOPT_POSTFIELDS, $msgJSON);
	#ejecutar la llamada 
	$resultado2 = curl_exec( $ch); 
	#cerrar el handlerde curl 
	curl_close( $ch);

}

echo "Notificacion enviada.";
?>