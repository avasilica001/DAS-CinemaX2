package com.example.entrega1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

    private final Activity activity=this;

    DBHelper db=new DBHelper(MainActivity.this);

    ArrayList<String> p_id=new ArrayList<>();
    ArrayList<String> p_nombre=new ArrayList<>();
    ArrayList<Integer> p_anio=new ArrayList<>();
    ArrayList<String> p_url=new ArrayList<>();
    ArrayList<Float> p_valoracion=new ArrayList<>();
    ArrayList<String> p_descripcion=new ArrayList<>();
    ArrayList<String> p_subidapor=new ArrayList();

    ListaPeliculasAdapter adapter;
    TextView noPeliculas;
    String s_id;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noPeliculas=findViewById(R.id.lp_tv_nohay);

        s_id= getIntent().getStringExtra("id");

        ActionBar ab=getSupportActionBar();
        ab.setTitle("Página principal");

        limpiarArrayLists();
        c=db.leerPeliculasTodas();
        guardarDatosArray();

        adapter=new ListaPeliculasAdapter(MainActivity.this, MainActivity.this, p_id, p_nombre, p_anio, p_url, p_valoracion, p_descripcion, p_subidapor,s_id);
        ListView l= (ListView) findViewById(R.id.lp_lv_listapeliculas);
        l.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        Button botonaniadir= (Button) findViewById(R.id.lp_b_aniadirpelicula);
        botonaniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AniadirPelicula.class);
                intent.putExtra("id",s_id);
                activity.startActivityForResult(intent, 1);
                //MainActivity.this.startActivity(intent);
            }
        });

        Button ptodas=findViewById(R.id.lp_b_todas);
        ptodas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {actualizarPeliculasTodas();
            }
        });

        Button pusuario=findViewById(R.id.lp_b_deusuario);
        pusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {actualizarPeliculasUsuario();
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
                p_subidapor.add(c.getString(6));
            }
        }
    }

    public void limpiarArrayLists(){
        p_id.clear();
        p_nombre.clear();
        p_anio.clear();
        p_url.clear();
        p_valoracion.clear();
        p_descripcion.clear();
        p_subidapor.clear();
    }

    public void actualizarPeliculasTodas(){
        limpiarArrayLists();
        c=db.leerPeliculasTodas();
        guardarDatosArray();
        adapter.notifyDataSetChanged();
    }

    public void actualizarPeliculasUsuario(){
        limpiarArrayLists();
        c=db.leerPeliculasUsuario(s_id);
        guardarDatosArray();
        adapter.notifyDataSetChanged();
    }
}