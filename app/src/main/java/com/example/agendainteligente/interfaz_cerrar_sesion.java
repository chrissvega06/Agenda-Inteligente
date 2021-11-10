package com.example.agendainteligente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class interfaz_cerrar_sesion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_cerrar_sesion);
    }
    public void cerrarsesion (View view){
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }
    public void cancelarcerrar (View view){
        Intent intent = new Intent(this, interfaz_notas.class );
        startActivity(intent);
    }
}