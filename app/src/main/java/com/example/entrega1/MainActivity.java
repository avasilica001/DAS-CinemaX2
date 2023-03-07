package com.example.entrega1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBHelper db=new DBHelper(MainActivity.this);

    ArrayList<String> p_id=new ArrayList<>();
    ArrayList<String> p_nombre=new ArrayList<>();
    ArrayList<Integer> p_anio=new ArrayList<>();
    ArrayList<String> p_url=new ArrayList<>();
    ArrayList<Float> p_valoracion=new ArrayList<>();
    ArrayList<String> p_descripcion=new ArrayList<>();

    listaPeliculasAdapter adapter;
    TextView noPeliculas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noPeliculas=findViewById(R.id.lp_tv_nohay);

        ActionBar ab=getSupportActionBar();
        ab.setTitle("Página Principal");

        guardarDatosArray();

        adapter=new listaPeliculasAdapter(MainActivity.this, this, p_id, p_nombre, p_anio, p_url, p_valoracion, p_descripcion);
        ListView l= (ListView) findViewById(R.id.lp_lv_listapeliculas);
        l.setAdapter(adapter);

        Button botonaniadir= (Button) findViewById(R.id.lp_b_aniadirpelicula);
        botonaniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, aniadirPelicula.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1){
            recreate();
        }
    }

    public void guardarDatosArray(){
        Cursor c=db.leerPeliculas();

        if (c.getCount()==0){
            noPeliculas.setVisibility(View.VISIBLE);
            Toast.makeText(this,"No hay películas para mostrar",Toast.LENGTH_SHORT).show();
        }
        else{
            noPeliculas.setVisibility(View.GONE);
            while(c.moveToNext()){
                p_id.add(c.getString(0));
                p_nombre.add(c.getString(1));
                p_anio.add(Integer.valueOf(c.getString(2)));
                p_url.add(c.getString(3));
                p_valoracion.add(Float.parseFloat(c.getString(4)));
                p_descripcion.add(c.getString(5));
            }
        }
    }
}