package com.example.entrega1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Registro extends AppCompatActivity {

    private Context context = this;
    private Activity activity=this;
    private EditText nombre, usuario, email, telefono, contrasenia;
    private ImageView vistaimagen;
    private Uri imagen;
    private Bitmap bimagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        //se setea la barra superior
        ActionBar ab = getSupportActionBar();
        ab.setTitle("CinemaX");

        //obtener datos de la vista
        nombre = findViewById(R.id.r_t_nombrecompleto);
        usuario = findViewById(R.id.r_tv_usuario);
        contrasenia = findViewById(R.id.r_t_contrasenia);
        email = findViewById(R.id.r_t_email);
        telefono = findViewById(R.id.r_t_telefono);
        vistaimagen = findViewById(R.id.r_iv_previafoto);

        //boton elegir imagen de la galeria
        Button elegirfoto=findViewById(R.id.r_b_elegirfoto);
        elegirfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mirar si la version es mayor o igual a marshmallow
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    //si lo es mirar si se han dado los permisos de lectura
                    if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        //si no hay permisos de lectura darselos
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
                    }
                    else{
                        //si ya hay permisos
                        elegirfoto();
                    }
                }
                else{
                    //si la version es menor a marshmallow
                    elegirfoto();
                }
            }
        });

        //boton sacar foto con la camara
        Button sacarfoto=findViewById(R.id.r_b_sacarfoto);
        sacarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //se ha de mirar primero si la version es igual o superior a la marshmallow
                //si es igual o superior
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    //si no hay permisos para acceder a la cámara, o escribir
                    if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        //dar permisos de camara y escritura
                        requestPermissions(new String[]{android.Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 112);
                    }
                    else{
                        //si ya hay permisos se abre la camara
                        abrirCamara();
                    }
                }
                //si la version es menor a la marshmallow
                else{
                    abrirCamara();
                }
            }
        });


        Button boton = findViewById(R.id.r_b_registrarse);
        //pulsar para registrarse
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBCinemax db = new DBCinemax(Registro.this);

                //si hay algun campo vacio no se deja registrar y manda un mensaje
                if (usuario.getText().toString().trim().equals("") || contrasenia.getText().toString().trim().equals("") || email.getText().toString().trim().equals("") || telefono.getText().toString().trim().equals("") || nombre.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "Por favor introduce todos tus datos, serán obligatorios para el registro.", Toast.LENGTH_SHORT).show();
                } else {
                    //si no hay campos vacios seguimos
                    //si el telefono es correcto (ej: 666666666, 999999999) se sigue

                    /*Basado en el código extraído de Stack Overflow
                     Pregunta: https://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
                     Respuesta: https://stackoverflow.com/posts/5439600/revisions
                     Modificado para verificar que se trata de un integer de 9 cifras
                     */
                    if (telefono.getText().toString().trim().matches("[0-9]+") && telefono.getText().toString().trim().length() == 9) {

                        /*Basado en el código extraído de Stack Overflow
                     Pregunta: https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
                     Respuesta: https://stackoverflow.com/posts/16058059/revisions
                     */

                        //si el telefono es correcto miramos si el email es valido mediante regex
                        java.util.regex.Pattern rp=java.util.regex.Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
                        java.util.regex.Matcher rm=rp.matcher(email.getText().toString().trim());

                        //si el email es valido (ej: cinemax@gmail.com)
                        if (rm.matches()){
                            //se mira si el usuario con ese nombre ya existe
                            Cursor c=db.existeUsuario(usuario.getText().toString().trim());
                            //si no existe se crea
                            if(c.getCount() == 0){
                                byte[] imagen=null;
                                db.aniadirUsuario(usuario.getText().toString().trim(), contrasenia.getText().toString().trim(), email.getText().toString().trim(), Integer.valueOf(telefono.getText().toString().trim()), nombre.getText().toString().trim(), imagen);
                                crearNotificacion("¡Bienvenido!","Hola, " + nombre.getText().toString().trim() + ". Te damos las gracias por unirte a nosotros. No dudes en publicar las películas que has visto para darnos tu opinión.");
                                finish();
                            }
                            else{
                                Toast.makeText(context, "Este nombre de usuario ya está en uso.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            //mensaje para email incorrecto
                            Toast.makeText(context, "Por favor introduce un email válido.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //mensaje para telefono incorrecto
                        Toast.makeText(context, "Por favor introduce un número de teléfono válido.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void crearNotificacion(String titulo, String contenido) {

        //el codigo ha sido tomado de las diapositivas de la asignatura sobre notificaciones
        //Para la api 33 hay que pedir permisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                //se piden los permisos
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 11);
            }
        }
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder b = new NotificationCompat.Builder(this, "IdCanal");

        // Para api oreo o mayores es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("IdCanal", "canal", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("canalNotificaciones");
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            //notificacion
            b.setSmallIcon(android.R.drawable.star_big_on)
                    .setContentTitle(titulo)
                    .setContentText(contenido)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(contenido));
            nm.notify(1, b.build());
        }
    }

    //metodo para cuando se usa la camara
    private void abrirCamara(){
        ContentValues cv=new ContentValues();
        //informacion de la imagen
        cv.put(MediaStore.Images.Media.TITLE, "Nueva Imagen");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Nueva Imagen sacada con la cámara");
        //uri de la imagen
        imagen=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);

        //crear intent para la camara
        Intent camarai=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camarai.putExtra(MediaStore.EXTRA_OUTPUT, imagen);
        startActivityForResult(camarai,1111);
    }

    //metodo para cuando se elige foto de la galeria
    private void elegirfoto(){
        //crear intent para la galeria
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1112);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            //si se esta sacando una foto
            case 111:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //se han dado los permisos necesarios
                    abrirCamara();
                }
                else {
                    //no se han dado los permisos necesarios
                    Toast.makeText(this, "No se han aceptado los permisos", Toast.LENGTH_SHORT).show();
                }
            }
            //si se esta eligiendo una foto de la galeria
            case 112:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //se han dado los permisos necesarios
                    elegirfoto();
                }
                else {
                    //no se han dado los permisos necesarios
                    Toast.makeText(this, "No se han aceptado los permisos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        //cuando ya se ha sacado la foto
        if(resultCode==RESULT_OK && requestCode==1111){
            //ver vista previa de la imagen
            vistaimagen.setImageURI(imagen);
            //guardar la imagen en bitmap para luego subirla a la bd
            BitmapDrawable bmd= (BitmapDrawable) vistaimagen.getDrawable();
            bimagen=bmd.getBitmap();

            //encontrar directorio de la galeria
            File directorio= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            //crear nombre de la foto sacada
            String tiempo=new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
            String nombre="IMG_"+tiempo+".jpg";
            File imagenfinal=new File(directorio,nombre);

            //Toast.makeText(this, String.valueOf(imagenfinal.exists()), Toast.LENGTH_SHORT).show();

            try{
                //guardar la foto en un file y enviarla a la galeria
                 FileOutputStream fos=new FileOutputStream(imagenfinal);
                 bimagen.compress(Bitmap.CompressFormat.PNG,100,fos);
                 fos.flush();
                 fos.close();

                 //crear intent para que guarde la informacion de la imagen
                 Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                 intent.setData(Uri.fromFile(imagenfinal));
                 sendBroadcast(intent);

                Toast.makeText(this, "Se ha guardado la imagen en la galería", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e){
                //no hace nada
            }

        }

        //cuando ya se ha elegido la foto de la galeria
        if(resultCode==RESULT_OK && requestCode==1112){
            //visualizar la imagen
            vistaimagen.setImageURI(data.getData());
        }
    }
}
