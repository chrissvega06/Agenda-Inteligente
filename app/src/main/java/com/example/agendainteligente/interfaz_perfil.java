package com.example.agendainteligente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class interfaz_perfil extends AppCompatActivity {
    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_perfil);
        drawerLayout = findViewById(R.id.drawer_layout);
        firebaseAuth = FirebaseAuth.getInstance();
    }


    //ENVIAR A CAMBIAR CONTRASEÑA
    public void enviarresetpassword (View view){
        Intent intent = new Intent(this, interfaz_recuperar_password.class );
        startActivity(intent);
    }

    //ENVIAR A CAMBIAR CORREO
    public void enviarresetcorreo (View view){
        Intent intent = new Intent(this, interfaz_cambiar_email.class );
        startActivity(intent);
    }

    //AQUI INICIA MENU DESPLEGABLE
    public void ClickMenu(View view){
        //open drawer
        interfaz_notas.openDrawer(drawerLayout);
    }
    public  void ClickLogo(View view){
        interfaz_notas.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view){
        interfaz_notas.redirectActivity(this,interfaz_notas.class);
    }
    public void ClickPublicar(View view){
        interfaz_notas.redirectActivity(this,interfaz_crear_nota.class);
    }
    public void ClickAboutUs(View view){
        recreate();
    }
    public void ClickLogout(View view){
        interfaz_notas.redirectActivity(this,interfaz_cerrar_sesion.class);
    }
    @Override
    protected  void onPause(){
        super.onPause();
        interfaz_notas.closeDrawer(drawerLayout);
    }
    //AQUI TERMINA MENU DESPLEGABLE

    public void Cerrar_sesion (View view){
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(this, MainActivity.class );
        Toast.makeText(interfaz_perfil.this,"Sesion cerrada correctamente",Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
}
