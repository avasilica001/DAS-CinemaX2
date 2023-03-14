package com.example.entrega1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registro extends AppCompatActivity {

    Context context=this;
     EditText nombre, usuario, email, telefono, contrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        ActionBar ab=getSupportActionBar();
        ab.setTitle("CinemaX");

        nombre=findViewById(R.id.r_t_nombrecompleto);
        usuario=findViewById(R.id.r_tv_usuario);
        contrasenia=findViewById(R.id.r_t_contrasenia);
        email=findViewById(R.id.r_t_email);
        telefono=findViewById(R.id.r_t_telefono);

        Button boton=findViewById(R.id.r_b_registrarse);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db=new DBHelper(Registro.this);
                if(usuario.getText().toString().trim().equals("") || contrasenia.getText().toString().trim().equals("") || email.getText().toString().trim().equals("") || telefono.getText().toString().trim().equals("") || nombre.getText().toString().trim().equals("")){
                    Toast.makeText(context,"Por favor introduce todos tus datos, serán obligatorios para el registro.",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(telefono.getText().toString().trim().matches("[0-9]+") && telefono.getText().toString().trim().length() == 9) {
                        db.aniadirUsuario(usuario.getText().toString().trim(), contrasenia.getText().toString().trim(), email.getText().toString().trim(), Integer.valueOf(telefono.getText().toString().trim()), nombre.getText().toString().trim());
                        crearNotificacion();
                        finish();
                    }
                    else {
                        Toast.makeText(context,"Por favor introduce un número de teléfono válido.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void crearNotificacion(){
        NotificationCompat.Builder b = new NotificationCompat.Builder(this);
        b.setSmallIcon(R.drawable.jaws)
            .setContentTitle("¡Bienvenido!")
            .setContentText("Hola, "+usuario.getText().toString().trim()+". Te damos las gracias por unirte a nosotros. No dudes en publicar las películas que has visto para darnos tu opinión.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, b.build());
    }
}