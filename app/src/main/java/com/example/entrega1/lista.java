package com.example.entrega1;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class lista extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] titulos;
    private final Integer[] anios;
    private final String[] descripciones;
    private final String[] imagenes;

    public lista(Activity context, String[] titulo, Integer[] anio, String[] descripcion, String[] imagen) {
        super(context, R.layout.listapeliculas, titulo);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.titulos=titulo;
        this.anios=anio;
        this.descripciones=descripcion;
        this.imagenes=imagen;

    }

    public View getView(int p,View view,ViewGroup parent) {
        LayoutInflater inf=context.getLayoutInflater();
        View rv=inf.inflate(R.layout.listapeliculas, null,true);

        TextView t_titulo = (TextView) rv.findViewById(R.id.lp_tv_titulo);
        TextView t_anio= (TextView) rv.findViewById(R.id.lp_tv_anio);
        TextView t_descripcion = (TextView) rv.findViewById(R.id.lp_tv_descripcion);
        ImageView i_imagen= (ImageView) rv.findViewById(R.id.lp_iv_portada);

        t_titulo.setText(titulos[p]);
        t_anio.setText(String.valueOf(anios[p]));
        t_descripcion.setText(descripciones[p]);
        Glide.with(context).load(imagenes[p]).into(i_imagen);

        return rv;

    };
}