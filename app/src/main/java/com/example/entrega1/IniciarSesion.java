package com.example.entrega1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.scottyab.aescrypt.AESCrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.*;

public class IniciarSesion extends AppCompatActivity {

    private final Activity activity=this;
    private Context context=this;
    private ArrayList<String> lineas=new ArrayList<>();

    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        //se setea para que se pueda ver como título el nombre de la aplicación
        ActionBar ab=getSupportActionBar();
        ab.setTitle("CinemaX");

        //se obtienen los elementos de la vista
        EditText usuario=findViewById(R.id.is_t_usuario);
        EditText contrasenia=findViewById(R.id.is_t_contrasenia);
        Button iniciars=findViewById(R.id.is_b_iniciarsesion);
        Button registrarse=findViewById(R.id.is_b_registrarse);

        iniciars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //si alguno de los campos es distinto del vacio
                if(!usuario.getText().toString().trim().equals("") || !contrasenia.getText().toString().trim().equals("")) {
                    //se encripta la contrasenia

                    /*Basado en el código extraído de Stack Overflow
                     Pregunta: https://stackoverflow.com/questions/41223937/how-can-i-encrypte-my-password-android-studio
                     Respuesta: https://stackoverflow.com/a/60652350
                     Modificado para cambiar la password de encriptado
                     */

                    String  cencriptada="";
                    //se encripta la contrasenia introducida para ver si coincide con la encriptada en la base de datos
                    try {
                        cencriptada = AESCrypt.encrypt("EncriptadoCinemaXAPP", contrasenia.getText().toString().trim());
                    }catch (Exception e){
                        //no hace nada
                    }
                    String finalCencriptada = cencriptada;

                    //se hace una peticion POST al servidor para registrar un usuario
                    StringRequest sr = new StringRequest(Request.Method.POST, "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/avasilica001/WEB/buscarusuario.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                                String respuesta=response.toString();

                                //si la respuesta esta vacia imprime mensaje
                                if(respuesta.isEmpty()){
                                    Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    //si no esta vacia significa que existe el usuario con ese usuario y contrasenia y se abre la siguiente actividad
                                    Intent intent = new Intent(IniciarSesion.this, PaginaPrincipal.class);
                                    intent.putExtra("id", usuario.getText().toString().trim());
                                    IniciarSesion.this.startActivity(intent);
                                    //se manda una notificación aleatoria de las posibles cmo un tutorial para el usuario
                                    notificacionAleatoria();
                                    finish();

                                    rq.cancelAll("login");
                                }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //si ha habido algun error con la solicitud
                            Toast.makeText(context, "Se ha producido un error", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            //se pasan todos los parametros necesarios en la solicitud
                            HashMap<String, String> parametros = new HashMap<String, String>();
                            parametros.put("id", usuario.getText().toString().trim());
                            parametros.put("contrasenia", finalCencriptada);

                            return parametros;
                        }
                    };

                    //se envia la solicitud con los parametros
                    rq = Volley.newRequestQueue(context);
                    sr.setTag("login");
                    rq.add(sr);
                    }
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //se abre la actividad de registro
                Intent intent=new Intent(IniciarSesion.this, Registro.class);
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    //metodo para que se actualice la bd con el usuario que se ha registrado nuevo y así no de error la aplicación
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1){
            recreate();
        }
    }

    public void notificacionAleatoria(){
        //obtenemos la lista, que es un txt con 10 líneas
        //cada una frases para notificación

        //codigo tomado de las diapositivas de la asignatura, apartado de manejo de ficheros internos
        InputStream f=context.getResources().openRawResource(R.raw.listanotificaciones);
        BufferedReader br=new BufferedReader(new InputStreamReader(f));
        try {
            String l=br.readLine();
            while( l!=null) {
                //se guardan todas las líneas en un arraylist
                this.lineas.add(l);
                l=br.readLine();
            }
            br.close();
        } catch (IOException e) {
            //no hace nada
        }

        //obtenemos un número aleatorio entre 0 y 9 para coger un elemento del array (una línea del txt)

        /*Basado en el código extraído de Stack Overflow
         Pregunta: https://stackoverflow.com/questions/5887709/getting-random-numbers-in-java
         Respuesta: https://stackoverflow.com/posts/5887745/revisions
         Modificado para obtener un numero aleatorio entre 0 y 9
         */

        int n=0 + (int)(Math.random() * ((9 - 0) + 1));

        //se separa la línea el array mediante el -
        //para obtener titulo notificacion-descripcion notificacion
        String[] s=lineas.get(n).split("-");

        //codigo obtenido de las diapositivas de la asignatura sobre notificaciones

        //Para la api 33 hay que pedir permisos para emitir notificaciones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                //se pide el permiso
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 11);
            }
        }
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder b = new NotificationCompat.Builder(this, "IdCanal");

        // Para api oreo o mayores es necesario crear un canal de notificaciones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("idCanal", "canal", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("canalNotificaciones");
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            //se crea el formato de la notificacion
            //style para hacer la notificación un poco más grande y que se pueda leer el texto de la descripción por completo
            b.setSmallIcon(android.R.drawable.btn_star_big_on)
                    .setContentTitle(s[0])
                    .setContentText(s[1])
                    .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(s[1]));

            //emitir notificacion
            nm.notify(1, b.build());
        }
    }
}

