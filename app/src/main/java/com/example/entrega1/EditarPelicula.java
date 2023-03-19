package com.example.entrega1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class EditarPelicula extends AppCompatActivity {

    Context context=this;
    EditText id, nombre, anio, url, descripcion;
    RatingBar valoracion;
    Button actualizar,volver;

    String s_id,s_titulo,s_anio, s_url, s_valoracion, s_descripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pelicula);

        //se obtienen los elementos de la vista

        nombre=findViewById(R.id.ep_t_nombre);
        anio=findViewById(R.id.ep_t_anio);
        url=findViewById(R.id.ep_t_portada);
        valoracion=findViewById(R.id.ep_rb_valoracion);
        descripcion=findViewById(R.id.ep_t_descripcion);
        actualizar=findViewById(R.id.ep_b_actualizar);
        volver=findViewById(R.id.ep_b_volver);

        //los datos que se han pasado por el intent al crear esta actividad ahora se guardan para usarse
        obtenerDatosIntent();

        ActionBar ab=getSupportActionBar();
        if (ab !=null){
            ab.setTitle(s_titulo);
        }

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBCinemax db=new DBCinemax(EditarPelicula.this);

                if (nombre.getText().toString().trim().equals("") || anio.getText().toString().trim().equals("")|| url.getText().toString().trim().equals("") || valoracion.toString().trim().equals("") || descripcion.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "Rellena todos los campos",Toast.LENGTH_SHORT).show();

                }
                else{
                    /*Basado en el código extraído de Stack Overflow
                     Pregunta: https://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
                     Respuesta: https://stackoverflow.com/posts/5439600/revisions
                     Modificado para verificar que se trata de un integer de 4 cifras
                     */
                    if(anio.getText().toString().trim().matches("[0-9]+") && anio.getText().toString().trim().length() == 4) {
                        db.modificarPelicula(s_id, nombre.getText().toString().trim(), Integer.valueOf(anio.getText().toString().trim()), url.getText().toString().trim(), valoracion.getRating(), descripcion.getText().toString().trim());
                        finish();
                    }
                    else{
                        Toast.makeText(context, "Introduce un año válido. Ejemplo: 1915",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
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

            //se visualizan los datos de la pelicula que se ha pulsadoen la actividad anterior
            nombre.setText(s_titulo);
            anio.setText(s_anio);
            url.setText(s_url);
            valoracion.setRating(Float.parseFloat(s_valoracion));
            descripcion.setText(s_descripcion);
        }
        else{
            //si ha habido algún error se muestra el siguiente mensaje
            //aunque no debería haberlo, ya que si la película se ha pulsado desde la actividad anterior y se ha abierto esta, es que existe la película y posee datos
            Toast.makeText(this,"No hay datos para mostrar", Toast.LENGTH_SHORT).show();
        }
    }

    private void volver(){
        //se pregunta al usario por si acaso que haya actualizado todos los datos
        //para ello se usa un dialogo de confirmacion

        //codigo basado en las diapositivas de la asignatura, apartado de diálogos
        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setTitle("Volver");
        ad.setMessage("¿Estás seguro de que quieres volver? Perderás los datos no guardados.");
        ad.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            //si el usuario ya ha guardado sus datos o no quiere hacerlo se vuelve a la actividad anterior
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            //si el usuario no está seguro nos mantenemos en esta actividad
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //no hace nada
            }
        });
        //se enseña el dialogo
        ad.create().show();
    }

    //se reescribe el método de pulsar el botón hacia atrás del propio teléfono para que también muestre el diálogo y sus posibles opciones
    @Override
    public void onBackPressed() {
        volver();
    }
}