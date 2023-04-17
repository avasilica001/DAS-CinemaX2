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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IniciarSesion extends AppCompatActivity {

    private FirebaseFirestore firestore;

    private final Activity activity=this;
    private Context context=this;
    private ArrayList<String> lineas=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        firestore=FirebaseFirestore.getInstance();

        Map<String,Object> users= new HashMap<>();
        users.put("firstname","nombre");
        users.put("lastname","apellido");
        users.put("descripcion","guapo");

        firestore.collection("users").add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context, "Firebase funciona", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Firebase no funciona", Toast.LENGTH_SHORT).show();
            }
        });

        DBCinemax db=new DBCinemax(IniciarSesion.this);
        //no se utiliza pero se hace para que se cree la bd la primera vez
        db.eliminarPelicula(String.valueOf(-1));

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
                    //se mira si existe alguien con ese usuario y se guarda en un cursor
                    Cursor c=db.existeUsuario(usuario.getText().toString().trim());
                    //si no hay usuario se manda un mensaje
                    if (c.getCount() == 0) {
                        Toast.makeText(context, "No se ha encontrado el usuario", Toast.LENGTH_SHORT).show();
                    } else {
                        //si hay usuario con ese id
                        Cursor c2 = db.existeUsuarioContrasenia(usuario.getText().toString().trim(), contrasenia.getText().toString().trim());
                        if (c2.getCount() == 0) {
                            //si la contraseña del usuario que ya existe no coincide se manda mensaje
                            Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        } else {
                            //una vez iniciada la bd se añaden los datos por defecto
                            //el usuario admin se prevee para no utilizarse todavia en esta entrega asi que no debería dar problemas
                            if (db.existeUsuario("admin").getCount()==0){
                                db.aniadirUsuario("admin", "admin", "admin@cinemax.com", 900000000, "Admin CinemaX", null);
                                db.aniadirPelicula("Todo en Todas Partes al Mismo Tiempo", 2022, "https://upload.wikimedia.org/wikipedia/en/1/1e/Everything_Everywhere_All_at_Once.jpg", 4.5f, "Cuando una ruptura interdimensional altera la realidad, Evelyn (Michelle Yeoh), una inmigrante china en Estados Unidos, se ve envuelta en una aventura salvaje en la que solo ella puede salvar el mundo. Perdida en los mundos infinitos del multiverso, esta heroína inesperada debe canalizar sus nuevos poderes para luchar contra los extraños y desconcertantes peligros del multiverso mientras el destino del mundo pende de un hilo.", "admin");
                                db.aniadirPelicula("Avatar: El Sentido del Agua", 2022, "https://imageio.forbes.com/specials-images/imageserve/639e09798e6da4e16a8cea7f/0x0.jpg?format=jpg&width=1200", 2.0f, "6 años después de que los Na'vi repelieran la primera invasión humana de Pandora, Jake Sully vive como jefe del clan Omaticaya y forma una familia con Neytiri, que incluye a sus hijos, Neteyam y Lo'ak; su hija biológica, Tuk; su hija adoptiva, Kiri (nacida del avatar Na'vi inerte de Grace Augustine); y un niño humano llamado Spider, hijo del coronel Miles Quaritch, que nació en Pandora y no pudo ser transportado a la Tierra en criostasis debido a su corta edad. Para consternación de los Na'vi, los humanos regresan y después de destruir unas hectáreas de selvas de Pandora con unos cañones de plasma, construyen unas nuevas bases incluyendo la base principal y de operaciones llamada \"Ciudad Cabeza de Puente\" qué está preparando a Pandora para ser colonizada por los humanos, ya que la Tierra agoniza. Entre los recién llegados hay recombinantes, avatares Na'vi con las mentes y recuerdos de marines fallecidos de la RDA, con el recombinante de Quaritch como líder.", "admin");
                            }
                            //si todos los datos introducidos son correctos, es decir, se ha encontrado un usuario con esa contraseña
                            //se realiza el inicio de sesion a la aplicacion
                            Intent intent = new Intent(IniciarSesion.this, PaginaPrincipal.class);
                            intent.putExtra("id", usuario.getText().toString().trim());
                            IniciarSesion.this.startActivity(intent);
                            //se manda una notificación aleatoria de las posibles cmo un tutorial para el usuario
                            notificacionAleatoria();
                            finish();
                        }
                    }
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
            e.printStackTrace();
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

