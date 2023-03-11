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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Pelicula extends AppCompatActivity {

    Activity activity=this;
    DBHelper db=new DBHelper(Pelicula.this);

    TextView id, nombre, anio, descripcion;
    ImageView url;
    RatingBar valoracion;

    Button actualizar,eliminar,volver;

    String s_id,s_titulo,s_anio, s_url, s_valoracion, s_descripcion, s_subidapor, usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        nombre=findViewById(R.id.p_tv_titulo);
        anio=findViewById(R.id.p_tv_anio);
        url=findViewById(R.id.p_iv_portada);
        valoracion=findViewById(R.id.p_rb_valoracion);
        descripcion=findViewById(R.id.p_tv_descripcion);
        descripcion.setMovementMethod(new ScrollingMovementMethod());
        actualizar=findViewById(R.id.p_b_modificar);
        eliminar=findViewById(R.id.p_b_borrar);
        volver=findViewById(R.id.p_b_volver);

        obtenerDatosIntent();

        ActionBar ab=getSupportActionBar();
        if (ab !=null){
            ab.setTitle(s_titulo);
        }

        if (s_subidapor.equals(usuario)){
            actualizar.setVisibility(View.VISIBLE);
            eliminar.setVisibility(View.VISIBLE);
        }
        else
        {
            actualizar.setVisibility(View.GONE);
            eliminar.setVisibility(View.GONE);
        }

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

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarBorrar();
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
            s_subidapor=getIntent().getStringExtra("subidapor");
            usuario=getIntent().getStringExtra("usuario");

            Cursor c=db.buscarPelicula(s_id);

            if(c.moveToFirst()){
                nombre.setText(c.getString(1));
                anio.setText(c.getString(2));
                Glide.with(this).load(c.getString(3)).into(url);
                valoracion.setRating(Float.parseFloat(c.getString(4)));
                descripcion.setText(String.valueOf(c.getString(5)));
            }
        }
        else{
            Toast.makeText(this,"No hay datos para mostrar", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmarBorrar(){
        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setTitle("Eliminar "+nombre.getText().toString().trim());
        ad.setMessage("¿Estás seguro de que quieres borrar "+nombre.getText().toString().trim()+"? No podrás recuperar la películas después");
        ad.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBHelper db=new DBHelper(Pelicula.this);
                db.eliminarPelicula(s_id);
                finish();
            }
        });
        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //no hace nada
            }
        });
        ad.create().show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1){
            recreate();
        }
    }

}