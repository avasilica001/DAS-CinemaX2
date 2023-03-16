package com.example.entrega1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class Pelicula extends AppCompatActivity {

    Activity activity=this;
    DBCinemax db=new DBCinemax(Pelicula.this);

    TextView id, nombre, anio, descripcion;
    ImageView url;
    RatingBar valoracion;

    Button actualizar,eliminar,volver;

    String s_id,s_titulo,s_anio, s_url, s_valoracion, s_descripcion, s_subidapor, usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        //obtener elementos de la vista
        nombre=findViewById(R.id.p_tv_titulo);
        anio=findViewById(R.id.p_tv_anio);
        url=findViewById(R.id.p_iv_portada);
        valoracion=findViewById(R.id.p_rb_valoracion);
        descripcion=findViewById(R.id.p_tv_descripcion);
        descripcion.setMovementMethod(new ScrollingMovementMethod());
        actualizar=findViewById(R.id.p_b_modificar);
        eliminar=findViewById(R.id.p_b_borrar);
        volver=findViewById(R.id.p_b_volver);

        //se cogen los datos de la pelicula que ha sido pulsada pasados por el intent
        obtenerDatosIntent();

        //setear el titulo para que sea la pelicula
        ActionBar ab=getSupportActionBar();
        if (ab !=null){
            ab.setTitle(s_titulo);
        }

        //si la pelicula está subida por el usuario, se puede modificar o eliminar
        //si no es del usuario solo se puede ver o volver
        if (s_subidapor.equals(usuario)){
            actualizar.setVisibility(View.VISIBLE);
            eliminar.setVisibility(View.VISIBLE);
        }
        else
        {
            actualizar.setVisibility(View.GONE);
            eliminar.setVisibility(View.GONE);
        }

        //se pasan todos los datos de la película para que se puedan modificar a posteriori
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pelicula.this, EditarPelicula.class);
                intent.putExtra("id", s_id);
                intent.putExtra("titulo", s_titulo);
                intent.putExtra("anio", s_anio);
                intent.putExtra("url", s_url);
                intent.putExtra("valoracion", s_valoracion);
                intent.putExtra("descripcion", s_descripcion);
                intent.putExtra("subidapor", s_subidapor);
                activity.startActivityForResult(intent, 1);
            }
        });

        //para borrar pelicula se muestra un dialogo
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarBorrar();
            }
        });

        //nos lleva a la actividad anterior
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void obtenerDatosIntent(){
        //si hay elementos en el intent (que deberia) se pasan a strings
        if(getIntent().hasExtra("id") && getIntent().hasExtra("titulo") && getIntent().hasExtra("anio") && getIntent().hasExtra("url") && getIntent().hasExtra("valoracion") && getIntent().hasExtra("descripcion") ){
            s_id= getIntent().getStringExtra("id");
            s_titulo=getIntent().getStringExtra("titulo");
            s_anio=getIntent().getStringExtra("anio");
            s_url=getIntent().getStringExtra("url");
            s_valoracion=getIntent().getStringExtra("valoracion");
            s_descripcion=getIntent().getStringExtra("descripcion");
            s_subidapor=getIntent().getStringExtra("subidapor");
            usuario=getIntent().getStringExtra("usuario");

            //se busca la pelicula en la bd por el id
            Cursor c=db.buscarPelicula(s_id);

            //si se ha encontrado la pelicula (debería porque si se ha pulsado desde la main activity y eso quiere decir que existe)
            if(c.moveToFirst()){
                //se setean los datos de la pelicula para que no haga falta introducirçtodo de nuevo si solo se quiere modificar un elemento
                nombre.setText(c.getString(1));
                anio.setText(c.getString(2));
                Glide.with(this).load(c.getString(3)).into(url);
                valoracion.setRating(Float.parseFloat(c.getString(4)));
                descripcion.setText(String.valueOf(c.getString(5)));
            }
        }
        else{
            //si ha habido algun error se muestra un mensaje
            Toast.makeText(this,"No hay datos para mostrar", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmarBorrar(){
        //dialogo para confirmar que se quiere borrar la pelicula
        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setTitle("Eliminar "+nombre.getText().toString().trim());
        ad.setMessage("¿Estás seguro de que quieres borrar "+nombre.getText().toString().trim()+"? No podrás recuperar la películas después");
        ad.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            //si se pulsa que si se elimina la pelicula
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBCinemax db=new DBCinemax(Pelicula.this);
                db.eliminarPelicula(s_id);
                finish();
            }
        });
        //si se pulsa que no nos mantenemos en la actividad
        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //no hace nada
            }
        });
        ad.create().show();
    }

    //si venimos de editar la pelicula este metodo actualiza los datos
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1){
            recreate();
        }
    }

}