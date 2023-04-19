package com.example.entrega1;

import android.app.Activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListaPeliculasAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private Activity activity;

    //arraylist para cada columna en la bd
    private ArrayList<String> ids=new ArrayList<>();
    private ArrayList<String> titulos=new ArrayList<>();
    private ArrayList<Integer> anios=new ArrayList<>();
    private ArrayList<String> urls=new ArrayList<>();
    private ArrayList<Float> valoraciones=new ArrayList<>();
    private ArrayList<String> descripciones=new ArrayList<>();
    private ArrayList<String> subidaspor=new ArrayList<>();

    LinearLayout l_lp;

    //hace falta el usuario por si se quieren pedir sus películas propias
    String usuario;

    public ListaPeliculasAdapter(Activity activity, Activity context, ArrayList<String> id, ArrayList<String> titulo, ArrayList<Integer> anio, ArrayList<String> url, ArrayList<Float> valoracion, ArrayList<String> descripcion, ArrayList<String> subidapor,String usuario) {
        super(context, R.layout.listapeliculas, titulo);

        this.activity=activity;
        this.context=context;
        this.ids=id;
        this.titulos=titulo;
        this.anios=anio;
        this.urls=url;
        this.valoraciones=valoracion;
        this.descripciones=descripcion;
        this.subidaspor=subidapor;
        this.usuario=usuario;

    }

    public View getView(int p,View view,ViewGroup parent) {
        LayoutInflater inf=context.getLayoutInflater();
        View l=inf.inflate(R.layout.listapeliculas, parent,false);

        //obtener elementos de la vista
        TextView t_titulo = l.findViewById(R.id.lp_tv_titulo);
        TextView t_anio= l.findViewById(R.id.lp_tv_anio);
        ImageView t_url= l.findViewById(R.id.lp_iv_portada);
        RatingBar r_valoracion= l.findViewById(R.id.lp_rb_valoracion);
        TextView t_descripcion = l.findViewById(R.id.lp_tv_descripcion);
        l_lp= l.findViewById(R.id.lp_l_pelicula);

        //se muestran los elementos de la peliculas en la posicion que se ha pasado
        t_titulo.setText(String.valueOf(titulos.get(p)));
        t_anio.setText(String.valueOf(anios.get(p)));
        Glide.with(this.context).load(urls.get(p)).into(t_url);
        r_valoracion.setRating(valoraciones.get(p));
        t_descripcion.setText(String.valueOf(descripciones.get(p)));
        //listener cuando se pulsa la pelicula
        l_lp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //se pasan todos los datos para ver la pelicula
                Intent intent = new Intent(context, Pelicula.class);
                intent.putExtra("id", String.valueOf(ids.get(p)));
                intent.putExtra("usuario", usuario);
                intent.putExtra("uel", urls.get(p));

                activity.startActivityForResult(intent, 1);
            }
        });
        return l;
    };
}