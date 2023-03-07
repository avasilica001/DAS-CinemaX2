package com.example.entrega1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class registrarse extends AppCompatActivity {

     EditText nombre, usuario, email, telefono, contrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        ActionBar ab=getSupportActionBar();
        if (ab !=null){
            ab.setTitle("Resgistrarse");
        }

        nombre=findViewById(R.id.r_t_nombrecompleto);
        usuario=findViewById(R.id.r_tv_usuario);
        contrasenia=findViewById(R.id.r_t_contrasenia);
        email=findViewById(R.id.r_t_email);
        telefono=findViewById(R.id.r_t_telefono);

        Button boton=findViewById(R.id.r_b_registrarse);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db=new DBHelper(registrarse.this);
                db.aniadirUsuario(nombre.getText().toString().trim(),contrasenia.getText().toString().trim(),email.getText().toString().trim(),Integer.valueOf(telefono.getText().toString().trim()),nombre.getText().toString().trim());
                finish();
            }
        });

    }
}