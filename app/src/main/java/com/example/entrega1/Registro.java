package com.example.entrega1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registro extends AppCompatActivity {

    Context context = this;
    EditText nombre, usuario, email, telefono, contrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("CinemaX");

        nombre = findViewById(R.id.r_t_nombrecompleto);
        usuario = findViewById(R.id.r_tv_usuario);
        contrasenia = findViewById(R.id.r_t_contrasenia);
        email = findViewById(R.id.r_t_email);
        telefono = findViewById(R.id.r_t_telefono);

        Button boton = findViewById(R.id.r_b_registrarse);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(Registro.this);
                if (usuario.getText().toString().trim().equals("") || contrasenia.getText().toString().trim().equals("") || email.getText().toString().trim().equals("") || telefono.getText().toString().trim().equals("") || nombre.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "Por favor introduce todos tus datos, serán obligatorios para el registro.", Toast.LENGTH_SHORT).show();
                } else {
                    if (telefono.getText().toString().trim().matches("[0-9]+") && telefono.getText().toString().trim().length() == 9) {
                        db.aniadirUsuario(usuario.getText().toString().trim(), contrasenia.getText().toString().trim(), email.getText().toString().trim(), Integer.valueOf(telefono.getText().toString().trim()), nombre.getText().toString().trim());
                        crearNotificacion("¡Bienvenido!","Hola, " + usuario.getText().toString().trim() + ". Te damos las gracias por unirte a nosotros. No dudes en publicar las películas que has visto para darnos tu opinión.");
                        finish();
                    } else {
                        Toast.makeText(context, "Por favor introduce un número de teléfono válido.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void crearNotificacion(String titulo, String contenido) {
        //Para la api 33 hay que pedir permisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                //PEDIR EL PERMISO
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

            b.setSmallIcon(android.R.drawable.stat_sys_warning)
                    .setContentTitle(titulo)
                    .setContentText(contenido);
            nm.notify(1, b.build());
        }
    }
}