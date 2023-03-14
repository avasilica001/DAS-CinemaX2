package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class AniadirPelicula extends AppCompatActivity {
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_pelicula);

        EditText titulo= (EditText) findViewById(R.id.ep_t_nombre);
        EditText anio= (EditText) findViewById(R.id.ep_t_anio);
        EditText url= (EditText) findViewById(R.id.ep_t_portada);

        EditText descripcion= (EditText) findViewById(R.id.ep_t_descripcion);
        RatingBar valoracion= (RatingBar) findViewById(R.id.ep_rb_valoracion);

        Button aniadir= (Button) findViewById(R.id.ep_b_actualizar);

        aniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db=new DBHelper(AniadirPelicula.this);

                if(anio.getText().toString().trim().matches("[0-9]+") && anio.getText().toString().trim().length() == 4) {
                    db.aniadirPelicula(titulo.getText().toString().trim(),Integer.valueOf(anio.getText().toString().trim()),url.getText().toString().trim(),valoracion.getRating(),descripcion.getText().toString().trim(),getIntent().getStringExtra("id"));
                    finish();
                }
                else{
                    Toast.makeText(context, "Introduce un año válido. Ejemplo: 1915",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}