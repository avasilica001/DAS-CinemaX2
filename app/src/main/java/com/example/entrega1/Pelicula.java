package com.example.entrega1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Pelicula extends AppCompatActivity {

    Activity activity=this;
    Context context=this;
    RequestQueue rq;
    String respuesta;

    TextView id, nombre, anio, descripcion, creador;
    ImageView url, fotoperfil;
    RatingBar valoracion;

    Button actualizar,eliminar,volver;

    String s_id,usuario, s_nombre, s_anio, s_url, s_valoracion, s_descripcion, s_subidapor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        rq = Volley.newRequestQueue(context);

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
        creador=findViewById(R.id.p_p_subidapor);
        fotoperfil=findViewById(R.id.p_iv_fotoperfil);

        //se cogen los datos de la pelicula que ha sido pulsada pasados por el intent

        s_id= getIntent().getStringExtra("id");
        usuario=getIntent().getStringExtra("usuario");



        StringRequest sr = new StringRequest(Request.Method.POST, "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/avasilica001/WEB/buscarpeliculaid.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                respuesta=response;

                if(respuesta.equals("null")){
                    //no hace nada
                }
                else{

                    try {
                        JSONArray jsona = new JSONArray(respuesta);

                        for (int i = 0; i < jsona.length(); i++) {
                            JSONObject json = jsona.getJSONObject(i);

                            s_nombre=json.getString("nombre");
                            s_anio=json.getString("anio");
                            s_url=json.getString("url");
                            s_valoracion=json.getString("valoracion");
                            s_descripcion=json.getString("descripcion");
                            s_subidapor=json.getString("subidapor");

                            byte[] imagenb = Base64.decode(json.getString("fotoperfil"), Base64.DEFAULT);
                            Bitmap bitmapimagen = BitmapFactory.decodeByteArray(imagenb, 0, imagenb.length);

                            nombre.setText(s_nombre);
                            anio.setText(s_anio);
                            Glide.with(context).load(json.getString("url")).into(url);
                            valoracion.setRating(Float.parseFloat(s_valoracion));
                            descripcion.setText(s_descripcion);
                            creador.setText(s_subidapor);
                            fotoperfil.setImageBitmap(bitmapimagen);

                            //setear el titulo para que sea la pelicula
                            ActionBar ab=getSupportActionBar();
                            if (ab !=null){
                                ab.setTitle(s_nombre);
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
                        }
                    }catch (Exception e){
                        //
                    }
                    rq.cancelAll("pelicula");

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
        sr.setTag("pelicula");
        rq.add(sr);

        //se pasan todos los datos de la película para que se puedan modificar a posteriori
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pelicula.this, EditarPelicula.class);
                intent.putExtra("id", s_id);
                intent.putExtra("titulo", s_nombre);
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

    private void confirmarBorrar(){

        //el cogido pertenece a las diapositivas de la asignatura sobre dialogos

        //dialogo para confirmar que se quiere borrar la pelicula
        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setTitle("Eliminar "+nombre.getText().toString().trim());
        ad.setMessage("¿Estás seguro de que quieres borrar "+nombre.getText().toString().trim()+"? No podrás recuperar la películas después");
        ad.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            //si se pulsa que si se elimina la pelicula
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringRequest sr = new StringRequest(Request.Method.POST, "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/avasilica001/WEB/eliminarpelicula.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rq.cancelAll("eliminar");
                        finish();
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
                sr.setTag("eliminar");
                rq.add(sr);
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