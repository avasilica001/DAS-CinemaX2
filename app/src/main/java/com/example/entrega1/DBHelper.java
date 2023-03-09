package com.example.entrega1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.ResultSet;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME= "CinemaX.db";
    private static final int DB_VERSION=1;

    public DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlPeliculas="CREATE TABLE Peliculas(id INTEGER PRIMARY KEY AUTOINCREMENT,nombre VARCHAR,anio INTEGER, url VARCHAR, valoracion DOUBLE, descripcion VARCHAR, subidapor VARCHAR, FOREIGN KEY(subidapor) REFERENCES Usuario(id))";
        String sqlUsuarios="CREATE TABLE Usuarios(id VARCHAR PRIMARY KEY, contrasenia VARCHAR, correo VARCHAR, telefono INTEGER, nombreapellido VARCHAR)";
        db.execSQL(sqlUsuarios);
        db.execSQL(sqlPeliculas);
    }

    public Cursor leerPeliculasTodas(){
        SQLiteDatabase db=this.getWritableDatabase();
        String sqldatos= "SELECT * FROM Peliculas";
        Cursor c=null;
        if (db != null){c= db.rawQuery(sqldatos,null);}
        return c;
    }

    public Cursor leerPeliculasUsuario(String usuario){
        SQLiteDatabase db=this.getWritableDatabase();
        String sqldatos= "SELECT * FROM Peliculas WHERE subidapor=?";
        //Toast.makeText(context,"usuario"+String.valueOf(usuario),Toast.LENGTH_SHORT).show();
        Cursor c=null;
        if (db != null){c= db.rawQuery(sqldatos,new String[]{String.valueOf(usuario)});}
        return c;
    }

    public void aniadirPelicula(String nombre, Integer anio, String url, Float valoracion, String descripcion,String usuario){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("nombre", nombre);
        cv.put("anio",anio);
        cv.put("url",url);
        cv.put("valoracion",valoracion);
        cv.put("descripcion",descripcion);
        cv.put("subidapor",usuario);
        long r= db.insert("Peliculas",null,cv);

        if (r==-1){
            Toast.makeText(context,"No se ha podido añadir la película", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Se ha añadido la película", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void aniadirUsuario(String usuario, String contrasenia, String correo, Integer telefono, String nombreapellido){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("id", usuario);
        cv.put("contrasenia", contrasenia);
        cv.put("correo", correo);
        cv.put("telefono", telefono);
        cv.put("nombreapellido", nombreapellido);

        long r=db.insert("Usuarios",null,cv);
        if(r==-1){
            Toast.makeText(context,"No se ha podido registrar el usuario", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Usuario registrado", Toast.LENGTH_SHORT).show();
        }
    }

    public void modificarPelicula(String id, String nombre, Integer anio, String url, Float valoracion, String descripcion){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("nombre", nombre);
        cv.put("anio",anio);
        cv.put("url",url);
        cv.put("valoracion",valoracion);
        cv.put("descripcion",descripcion);

        long r=db.update("Peliculas",cv, "id=?", new String[]{id});
        if(r==-1){
            Toast.makeText(context,"No se ha podido actualizar la película", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Película actualizada", Toast.LENGTH_SHORT).show();
        }

    }

    public Cursor buscarPelicula(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        String sqlp= "SELECT * FROM Peliculas WHERE id=?";
        Cursor c=null;
        if (db != null){
            Toast.makeText(context,id, Toast.LENGTH_SHORT).show();
            c=db.rawQuery(sqlp,new String[]{id});
        }
        return c;
    }

    public void eliminarPelicula(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        long r=db.delete("Peliculas","id=?",new String[]{id});
        if (r==-1){
            Toast.makeText(context,"No se ha podido borrar la película", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Película borrada", Toast.LENGTH_SHORT).show();
        }

    }

    //no se si lo voy a usar de momento pero se queda
    public void eliminarUsuario(String idusuario){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        String sqldelete="DELETE FROM Usuarios where id='"+idusuario+"';";
        db.execSQL(sqldelete);
        db.close();
    }

    public Cursor existeUsuario(String idusuario){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        Cursor c=null;
        if (db != null){c= db.rawQuery("SELECT * FROM Usuarios WHERE id=?",new String[]{idusuario});
        }
        return c;
    }

    public Cursor existeUsuarioContrasenia(String idusuario, String contrasenia){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        Cursor c=null;
        if (db != null){c= db.rawQuery("SELECT * FROM Usuarios WHERE id=? AND contrasenia=?",new String[]{idusuario,contrasenia});
        }
        return c;
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
