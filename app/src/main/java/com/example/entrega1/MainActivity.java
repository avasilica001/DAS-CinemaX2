package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

        DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //db = new DBHelper(this);

        //String[] peliculas={"Avengers","Batman"};
        //ArrayAdapter adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,peliculas);
        //ListView lp = (ListView) findViewById(R.id.lwpeliculas);
        //lp.setAdapter(adaptador);


        String[] titulos ={
                "Avengers","Batman", "Doraemon"
        };

        Integer[] anios={2012,2022,2023
        };

        String[] descripciones={
                "descripcion1","descripcion2","descripcion3"
        };

        String[] imagenes={"https://www.shutterstock.com/blog/wp-content/uploads/sites/5/2022/07/Avengers-Age-of-Ultron-2015jpeg.jpeg?w=720",
                "https://m.media-amazon.com/images/M/MV5BMDdmMTBiNTYtMDIzNi00NGVlLWIzMDYtZTk3MTQ3NGQxZGEwXkEyXkFqcGdeQXVyMzMwOTU5MDk@._V1_FMjpg_UX1000_.jpg",
                "https://preview.redd.it/d15ezrjih4621.jpg?auto=webp&s=c1f3cf485add4131544bc0d2ed7441acfb2a291f"
        };

        lista adapter=new lista(this, titulos, anios, descripciones, imagenes);
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
}