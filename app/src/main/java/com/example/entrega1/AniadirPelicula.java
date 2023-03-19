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

        //obtener los elementos de la vista
        EditText titulo= (EditText) findViewById(R.id.ep_t_nombre);
        EditText anio= (EditText) findViewById(R.id.ep_t_anio);
        EditText url= (EditText) findViewById(R.id.ep_t_portada);

        EditText descripcion= (EditText) findViewById(R.id.ep_t_descripcion);
        RatingBar valoracion= (RatingBar) findViewById(R.id.ep_rb_valoracion);

        Button aniadir= (Button) findViewById(R.id.ep_b_actualizar);

        //que hacer si se pulsa añadir
        aniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBCinemax db=new DBCinemax(AniadirPelicula.this);

                if (titulo.getText().toString().trim().equals("") || anio.getText().toString().trim().equals("") || url.getText().toString().trim().equals("")  || valoracion.toString().trim().equals("") || descripcion.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "Rellena todos los campos",Toast.LENGTH_SHORT).show();
                }
                else{
                    //si el año introducido es correcto (ej: 1994,2003) se añade a la bd

                    /*Basado en el código extraído de Stack Overflow
                     Pregunta: https://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
                     Respuesta: https://stackoverflow.com/posts/5439600/revisions
                     Modificado para verificar que se trata de un integer de 4 cifras
                     */
                    if(anio.getText().toString().trim().matches("[0-9]+") && anio.getText().toString().trim().length() == 4) {
                        db.aniadirPelicula(titulo.getText().toString().trim(),Integer.valueOf(anio.getText().toString().trim()),url.getText().toString().trim(),valoracion.getRating(),descripcion.getText().toString().trim(),getIntent().getStringExtra("id"));
                        finish();
                    }
                    else{
                        //si no se da una pista de como deberia representarse el año
                        Toast.makeText(context, "Introduce un año válido. Ejemplo: 1915",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}