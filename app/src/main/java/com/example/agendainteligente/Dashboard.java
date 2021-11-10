package com.example.agendainteligente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

public class Dashboard extends AppCompatActivity {
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
    }
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
    public void ClickDashboard(View view){
        recreate();
    }
    public void ClickAboutUs(View view){
        interfaz_notas.redirectActivity(this, interfaz_perfil.class);
    }
    public void ClickLogout(View view){
        interfaz_notas.redirectActivity(this,interfaz_cerrar_sesion.class);
    }

    @Override
    protected  void onPause(){
        super.onPause();
        interfaz_notas.closeDrawer(drawerLayout);
    }

}