package com.example.entrega1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBCinemax extends SQLiteOpenHelper {

    private Context context;
    //nombre de la bd
    private static final String DB_NAME= "CinemaX.db";
    private static final int DB_VERSION=1;

    public DBCinemax(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //se crean las tablas y los elementos que deben tener
        String sqlPeliculas = "CREATE TABLE Peliculas(id INTEGER PRIMARY KEY AUTOINCREMENT,nombre VARCHAR,anio INTEGER, url VARCHAR, valoracion DOUBLE, descripcion VARCHAR, subidapor VARCHAR, fotoperfil LONGBLOB, FOREIGN KEY(subidapor) REFERENCES Usuario(id))";
        String sqlUsuarios = "CREATE TABLE Usuarios(id VARCHAR PRIMARY KEY, contrasenia VARCHAR, correo VARCHAR, telefono INTEGER, nombreapellido VARCHAR, fotoperfil LONGBLOB)";
        db.execSQL(sqlUsuarios);
        db.execSQL(sqlPeliculas);
        }

    public Cursor leerPeliculasTodas(){
        //se obtienen todas las peliculas de la bd
        SQLiteDatabase db=this.getWritableDatabase();
        String sqldatos= "SELECT * FROM Peliculas";
        Cursor c=null;
        //si la bd esta vacia no hace falta realizar la consulta, se devuelve el cursor como null
        if (db != null){c= db.rawQuery(sqldatos,null);}
        return c;
    }

    public Cursor leerPeliculasUsuario(String usuario){
        //se obtienen todas las peliculas subidas por el usuario que ha iniciado sesion
        SQLiteDatabase db=this.getWritableDatabase();
        String sqldatos= "SELECT * FROM Peliculas WHERE subidapor=?";
        Cursor c=null;
        //si la bd eta vacia no hace falta realizar la consulta, se devuelve el cursor como null
        if (db != null){c= db.rawQuery(sqldatos,new String[]{String.valueOf(usuario)});}
        return c;
    }

    public void aniadirPelicula(String nombre, Integer anio, String url, Float valoracion, String descripcion,String usuario){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();

        //se añaden los elementos de la pelicula uno a uno
        cv.put("nombre", nombre);
        cv.put("anio",anio);
        cv.put("url",url);
        cv.put("valoracion",valoracion);
        cv.put("descripcion",descripcion);
        cv.put("subidapor",usuario);
        //se hace la consulta y se manda un toast dependiendo si se ha podido añadir correctamente o no para notificar al usuario
        long r= db.insert("Peliculas",null,cv);
        db.close();
    }

    public void aniadirUsuario(String usuario, String contrasenia, String correo, Integer telefono, String nombreapellido, byte[] fotoperfil){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        //se añaden los elementos del usuario uno a uno
        cv.put("id", usuario);
        cv.put("contrasenia", contrasenia);
        cv.put("correo", correo);
        cv.put("telefono", telefono);
        cv.put("nombreapellido", nombreapellido);
        cv.put("fotoperfil",fotoperfil);
        //se hace la consulta y se manda un toast dependiendo si se ha podido hacer correctamente el registro o no para notificar al usuario
        long r=db.insert("Usuarios",null,cv);
    }

    public void modificarPelicula(String id, String nombre, Integer anio, String url, Float valoracion, String descripcion){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        //se añaden los elementos de la pelicula para que se modifiquen
        cv.put("nombre", nombre);
        cv.put("anio",anio);
        cv.put("url",url);
        cv.put("valoracion",valoracion);
        cv.put("descripcion",descripcion);

        //se hace la actualización y se manda un mensaje enseñando si se ha podido hacer o no
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

        //se realiza la busqueda de una pelicula concreta por su id
        String sqlp= "select * from Peliculas where id=?";
        Cursor c=null;
        //si la bd esta vacia se devuelve el cursor como null y no hace falta hacer la consulta
        if (db != null){
            c=db.rawQuery(sqlp,new String[]{id});
        }
        return c;
    }

    public void eliminarPelicula(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        //se realiza la consulta y se notifica al usuario si ha sido correcta o no
        long r=db.delete("Peliculas","id=?",new String[]{id});
        if (id!=String.valueOf(-1)){
            if (r==-1){
                Toast.makeText(context,"No se ha podido borrar la película", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context,"Película borrada", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //método que todavía no se usa pero queda para futuras implementaciones
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

        //se busca el nombre de usuario en la base de datos, solo si la bd no esta vacia
        Cursor c=null;
        if (db != null){c= db.rawQuery("SELECT * FROM Usuarios WHERE id=?",new String[]{idusuario});
        }
        return c;
    }

    public Cursor existeUsuarioContrasenia(String idusuario, String contrasenia){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        //se busca si hay algun usuario con ese nombre y esa contraseña
        Cursor c=null;
        if (db != null){c= db.rawQuery("SELECT * FROM Usuarios WHERE id=? AND contrasenia=?",new String[]{idusuario,contrasenia});
        }
        return c;
    }
    //los metodos de existeUsuario y existeUsuarioContrasenia se utilizan (en vez de uno solo para mirar usuario y contraseña) para poder enviar distintos mensajes al suaurio por pantalla

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //restauracion de la base de datos desde 0
        String sqlPeliculas="DROP TABLE IF EXISTS Peliculas";
        String sqlUsuarios="DROP TABLE IF EXISTS Usuarios ";
        db.execSQL(sqlPeliculas);
        db.execSQL(sqlUsuarios);

        onCreate(db);
    }
}