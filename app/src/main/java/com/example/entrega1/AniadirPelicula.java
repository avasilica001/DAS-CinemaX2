package com.example.entrega1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class AniadirPelicula extends AppCompatActivity {
    Context context=this;
    RequestQueue rq;

    String s_id;

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

        s_id= getIntent().getStringExtra("id");

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

                        StringRequest sr = new StringRequest(Request.Method.POST, "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/avasilica001/WEB/aniadirpelicula.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String respuesta=response;

                                Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show();

                                rq.cancelAll("aniadir");
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
                                parametros.put("nombre", titulo.getText().toString().trim());
                                parametros.put("anio", anio.getText().toString().trim());
                                parametros.put("url", url.getText().toString().trim());
                                parametros.put("valoracion", String.valueOf(valoracion.getRating()));
                                parametros.put("descripcion", descripcion.getText().toString().trim());
                                parametros.put("subidapor", s_id);
                                return parametros;
                            }
                        };

                        //se envia la solicitud con los parametros
                        rq = Volley.newRequestQueue(context);
                        sr.setTag("aniadir");
                        rq.add(sr);

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