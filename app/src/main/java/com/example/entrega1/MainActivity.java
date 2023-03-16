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
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final Activity activity=this;

    DBCinemax db=new DBCinemax(MainActivity.this);

    //arraylist con las columnas de la tabla pelicula
    ArrayList<String> p_id=new ArrayList<>();
    ArrayList<String> p_nombre=new ArrayList<>();
    ArrayList<Integer> p_anio=new ArrayList<>();
    ArrayList<String> p_url=new ArrayList<>();
    ArrayList<Float> p_valoracion=new ArrayList<>();
    ArrayList<String> p_descripcion=new ArrayList<>();
    ArrayList<String> p_subidapor=new ArrayList();

    ListaPeliculasAdapter adapter;
    String s_id;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //se guarda el id del usuario para usarse si hace falta ver sus propias peliculas
        s_id= getIntent().getStringExtra("id");

        //setear la barra superior
        ActionBar ab=getSupportActionBar();
        ab.setTitle("Página principal");

        //los arraylist se vacian porque si se pulsa continuamente los botones para obtener las películas solo se añadirían a la lista y se repetirian
        limpiarArrayLists();
        //se len las peliculas una vez vacios lo arraylists y se guardan
        c=db.leerPeliculasTodas();
        guardarDatosArray();

        //se crea el adaptar propio
        adapter=new ListaPeliculasAdapter(MainActivity.this, MainActivity.this, p_id, p_nombre, p_anio, p_url, p_valoracion, p_descripcion, p_subidapor,s_id);
        ListView l= (ListView) findViewById(R.id.lp_lv_listapeliculas);
        l.setAdapter(adapter);

        //cuando se modifican datos notificar para que actualice
        adapter.notifyDataSetChanged();

        //boton para añadir una nueva pelicula a la lista
        Button botonaniadir= (Button) findViewById(R.id.lp_b_aniadirpelicula);
        botonaniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //se necesita el id para guardar quien ha subido la pelicula
                Intent intent=new Intent(MainActivity.this, AniadirPelicula.class);
                intent.putExtra("id",s_id);
                activity.startActivityForResult(intent, 1);
            }
        });

        //ver todas las peliculas
        Button ptodas=findViewById(R.id.lp_b_todas);
        ptodas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {actualizarPeliculasTodas();
            }
        });

        //ver solo las peliculas del usuario logeado
        Button pusuario=findViewById(R.id.lp_b_deusuario);
        pusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {actualizarPeliculasUsuario();
            }
        });
    }

    //refrescar cuando se vuelve a la actividad main por si se ha añadido una película nueva, pàra que esta aparezca
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1){
            recreate();
        }
    }

    //se toman los datos de la bd y se guardan en los arraylist
    public void guardarDatosArray(){
        if (c.getCount()==0){
            Toast.makeText(this,"No hay películas para mostrar",Toast.LENGTH_SHORT).show();
        }
        else{
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

    //vaciar elementos de los arraylists
    public void limpiarArrayLists(){
        p_id.clear();
        p_nombre.clear();
        p_anio.clear();
        p_url.clear();
        p_valoracion.clear();
        p_descripcion.clear();
        p_subidapor.clear();
    }

    //hacer que se vea en el listview todas las peliculas
    public void actualizarPeliculasTodas(){
        limpiarArrayLists();
        c=db.leerPeliculasTodas();
        guardarDatosArray();
        adapter.notifyDataSetChanged();
    }

    //hacer que se vea en el listview solo las peliculas del usuario
    public void actualizarPeliculasUsuario(){
        limpiarArrayLists();
        c=db.leerPeliculasUsuario(s_id);
        guardarDatosArray();
        adapter.notifyDataSetChanged();
    }
}