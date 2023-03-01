package com.example.entrega1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME= "Cinema.db";
    private static final int DB_VERSION=1;



    public DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlPeliculas="CREATE TABLE Peliculas(id INTEGER PRIMARY KEY AUTOINCREMENT,nombre VARCHAR,anio INTEGER, url VARCHAR, valoracion DOUBLE, descripcion VARCHAR)";
        String sqlUsuarios="CREATE TABLE Usuarios(id VARCHAR PRIMARY KEY, contrasenia VARCHAR, correo VARCHAR, telefono INTEGER, nombreapellido VARCHAR)";
        db.execSQL(sqlPeliculas);
        db.execSQL(sqlUsuarios);
    }

    public void aniadirPelicula(String nombre, Integer anio, String url, Float valoracion, String descripcion){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("nombre", nombre);
        cv.put("anio",anio);
        cv.put("url",url);
        cv.put("valoracion",valoracion);
        cv.put("descripcion",descripcion);
        long r= db.insert("Peliculas",null,cv);

        if (r==-1){
            Toast.makeText(context,"No se ha podido añadir la película", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Se ha añadido la película", Toast.LENGTH_SHORT).show();
        }


        db.close();
    }

    public void aniadirUsuario(String id, String contrasenia, String correo, Integer telefono, String nombreapellido){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("id", id);
        cv.put("contrasenia", contrasenia);
        cv.put("correo", correo);
        cv.put("telefono", telefono);
        cv.put("nombreapellido", nombreapellido);
        db.insert("Usuarios",null,cv);
        db.close();
    }

    public void eliminarPelicula(String nombre){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        Cursor idpelicula = db.rawQuery("select id from Peliculs where nombre like '" + nombre + "'", null);
        String sqldelete="DELETE FROM Peliculas where id='"+idpelicula.getInt(0)+"';";
        db.execSQL(sqldelete);
        db.close();
    }

    public void eliminarUsuario(String idusuario){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        String sqldelete="DELETE FROM Usuarios where id='"+idusuario+"';";
        db.execSQL(sqldelete);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlPeliculas="DROP TABLE IF EXISTS Peliculas";
        String sqlUsuarios="DROP TABLE IF EXISTS Usuarios ";
        db.execSQL(sqlPeliculas);
        db.execSQL(sqlUsuarios);

        onCreate(db);
    }
}
