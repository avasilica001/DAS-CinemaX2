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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class IniciarSesion extends AppCompatActivity {

    private final Activity activity=this;
    private Context context=this;
    private ArrayList<String> lineas=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        ActionBar ab=getSupportActionBar();
        ab.setTitle("CinemaX");

        EditText usuario=findViewById(R.id.is_t_usuario);
        EditText contrasenia=findViewById(R.id.is_t_contrasenia);
        Button iniciars=findViewById(R.id.is_b_iniciarsesion);
        Button registrarse=findViewById(R.id.is_b_registrarse);

        iniciars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db=new DBHelper(IniciarSesion.this);
                Cursor c=db.existeUsuario(usuario.getText().toString().trim());
                if(!usuario.getText().toString().trim().equals("") || !contrasenia.getText().toString().trim().equals("")) {
                    if (c.getCount() == 0) {
                        Toast.makeText(context, "No se ha encontrado el usuario", Toast.LENGTH_SHORT).show();
                    } else {
                        Cursor c2 = db.existeUsuarioContrasenia(usuario.getText().toString().trim(), contrasenia.getText().toString().trim());
                        if (c2.getCount() == 0) {
                            Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(IniciarSesion.this, MainActivity.class);
                            intent.putExtra("id", usuario.getText().toString().trim());
                            IniciarSesion.this.startActivity(intent);
                            notificacionaleatoria();
                            finish();
                        }
                    }
                }
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(IniciarSesion.this, Registro.class);
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1){
            recreate();
        }
    }

    public void notificacionaleatoria(){

        InputStream f=context.getResources().openRawResource(R.raw.listanotificaciones);
        BufferedReader br=new BufferedReader(new InputStreamReader(f));
        try {
            String l=br.readLine();
            while( l!=null) {
                this.lineas.add(l);
                l=br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int n=0 + (int)(Math.random() * ((9 - 0) + 1));

        Toast.makeText(context, String.valueOf(n), Toast.LENGTH_SHORT).show();


        String[] s=lineas.get(n).split("-");

        //Para la api 33 hay que pedir permisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 11);
            }
        }
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder b = new NotificationCompat.Builder(this, "IdCanal");

        // Para api oreo o mayores es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("idc", "canal", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("canalNotificaciones");
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            b.setSmallIcon(android.R.drawable.btn_star_big_on)
                    .setContentTitle(s[0])
                    .setContentText(s[1])
                    .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(s[1]));

            nm.notify(1, b.build());
        }
    }
}

