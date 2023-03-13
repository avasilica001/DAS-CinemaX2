package com.example.entrega1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class EditarPelicula extends AppCompatActivity {

    EditText id, nombre, anio, url, descripcion;
    RatingBar valoracion;
    Button actualizar,volver;

    String s_id,s_titulo,s_anio, s_url, s_valoracion, s_descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pelicula);

        nombre=findViewById(R.id.ep_t_nombre);
        anio=findViewById(R.id.ep_t_anio);
        url=findViewById(R.id.ep_t_portada);
        valoracion=findViewById(R.id.ep_rb_valoracion);
        descripcion=findViewById(R.id.ep_t_descripcion);
        actualizar=findViewById(R.id.ep_b_actualizar);
        volver=findViewById(R.id.ep_b_volver);

        obtenerDatosIntent();

        ActionBar ab=getSupportActionBar();
        if (ab !=null){
            ab.setTitle(s_titulo);
        }

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db=new DBHelper(EditarPelicula.this);
                db.modificarPelicula(s_id,nombre.getText().toString().trim(),Integer.valueOf(anio.getText().toString().trim()), url.getText().toString().trim(),valoracion.getRating(), descripcion.getText().toString().trim());
                finish();
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void obtenerDatosIntent(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("titulo") && getIntent().hasExtra("anio") && getIntent().hasExtra("url") && getIntent().hasExtra("valoracion") && getIntent().hasExtra("descripcion") ){
            s_id= getIntent().getStringExtra("id");
            s_titulo=getIntent().getStringExtra("titulo");
            s_anio=getIntent().getStringExtra("anio");
            s_url=getIntent().getStringExtra("url");
            s_valoracion=getIntent().getStringExtra("valoracion");
            s_descripcion=getIntent().getStringExtra("descripcion");

            nombre.setText(s_titulo);
            anio.setText(s_anio);
            url.setText(s_url);
            valoracion.setRating(Float.parseFloat(s_valoracion));
            descripcion.setText(s_descripcion);
        }
        else{
            Toast.makeText(this,"No hay datos para mostrar", Toast.LENGTH_SHORT).show();
        }
    }
}