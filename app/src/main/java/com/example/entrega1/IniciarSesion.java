package com.example.entrega1;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class IniciarSesion extends AppCompatActivity {

    private final Activity activity=this;
    private Context context=this;
    private ArrayList<String> lineas=new ArrayList<>();

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
                DBCinemax db=new DBCinemax(IniciarSesion.this);
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
        int n=0 + (int)(Math.random() * ((9 - 0) + 1));

        //se separa la línea el array mediante el -
        //para obtener titulo notificacion-descripcion notificacion
        String[] s=lineas.get(n).split("-");

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

            NotificationChannel channel = new NotificationChannel("idc", "canal", NotificationManager.IMPORTANCE_DEFAULT);
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

