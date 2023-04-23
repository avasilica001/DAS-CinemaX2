package com.example.entrega1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PaginaPrincipal extends AppCompatActivity {

    private final Activity activity=this;
    private Context context=this;

    private RequestQueue rq;

    DBCinemax db=new DBCinemax(PaginaPrincipal.this);

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
    String respuesta;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rq=Volley.newRequestQueue(context);

        //se guarda el id del usuario para usarse si hace falta ver sus propias peliculas
        s_id= getIntent().getStringExtra("id");

        //setear la barra superior
        ActionBar ab=getSupportActionBar();
        ab.setTitle("Página principal");

        //los arraylist se vacian porque si se pulsa continuamente los botones para obtener las películas solo se añadirían a la lista y se repetirian
        limpiarArrayLists();
        //se len las peliculas una vez vacios lo arraylists y se guardan

        actualizarPeliculasTodas();

        //se crea el adaptar propio
        adapter=new ListaPeliculasAdapter(activity, activity, p_id, p_nombre, p_anio, p_url, p_valoracion, p_descripcion, p_subidapor,s_id);
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
                Intent intent=new Intent(PaginaPrincipal.this, AniadirPelicula.class);
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

    //se toman los datos de la bd y se guardan en los arraylist
    public void guardarDatosArray(){

            try {
                JSONArray jsona = new JSONArray(respuesta);

                for (int i = 0; i < jsona.length(); i++)
                {
                    JSONObject json = jsona.getJSONObject(i);

                    p_id.add(json.getString("id"));
                    p_nombre.add(json.getString("nombre"));
                    p_anio.add(Integer.valueOf(json.getString("anio")));
                    p_url.add(json.getString("url"));
                    p_valoracion.add(Float.parseFloat(json.getString("valoracion")));
                    p_descripcion.add(json.getString("descripcion"));
                    p_subidapor.add(json.getString("subidapor"));
                }


            }catch (Exception e){
                //no hace nada
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
        StringRequest sr = new StringRequest(Request.Method.POST, "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/avasilica001/WEB/buscarpeliculas.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                respuesta=response;

                if(respuesta.equals("null")){
                    Toast.makeText(context, "No hay películas para mostrar", Toast.LENGTH_SHORT).show();
                }
                else{
                    guardarDatosArray();
                    adapter.notifyDataSetChanged();
                    rq.cancelAll("todas");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //si ha habido algun error con la solicitud
                Toast.makeText(context, "Se ha producido un error", Toast.LENGTH_SHORT).show();
            }
        });

        //se envia la solicitud con los parametros
        sr.setTag("todas");
        rq.add(sr);
    }

    //hacer que se vea en el listview solo las peliculas del usuario
    public void actualizarPeliculasUsuario(){
        limpiarArrayLists();

        StringRequest sr = new StringRequest(Request.Method.POST, "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/avasilica001/WEB/buscarpeliculasusuario.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                respuesta=response;

                if(respuesta.equals("null")){
                    Toast.makeText(context, "No hay películas para mostrar", Toast.LENGTH_SHORT).show();
                }
                else{
                    guardarDatosArray();
                    adapter.notifyDataSetChanged();
                    rq.cancelAll("usuario");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //si ha habido algun error con la solicitud
                Toast.makeText(context, "Se ha producido un error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //se pasan todos los parametros necesarios en la solicitud
                HashMap<String, String> parametros = new HashMap<String, String>();
                parametros.put("id", s_id);

                return parametros;
            }
        };

        //se envia la solicitud con los parametros
        sr.setTag("usuario");
        rq.add(sr);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1){
            recreate();
        }
    }
}