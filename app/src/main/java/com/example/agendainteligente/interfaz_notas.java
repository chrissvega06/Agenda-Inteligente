package com.example.agendainteligente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import model.nota;

public class interfaz_notas extends AppCompatActivity {
    //initialize variable
    DrawerLayout drawerLayout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<nota> listPublicacion = new ArrayList<nota>();
    ArrayAdapter<nota> arrayAdapterPublicacion;

    EditText inf,desc,lugar;
    ListView listV_publicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_notas);

        //assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
        //firebaseDatabase = FirebaseDatabase.getInstance();
        //databaseReference = firebaseDatabase.getReference();
        listV_publicacion = findViewById(R.id.lv_infopublic);
        inicializarFirebase();
        listaDatos();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listaDatos() {
        databaseReference.child("Publicaciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPublicacion.clear();
                for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    nota p = objSnapshot.getValue(nota.class);
                    listPublicacion.add(p);

                    arrayAdapterPublicacion = new ArrayAdapter<nota>(interfaz_notas.this, android.R.layout.simple_list_item_1,listPublicacion);
                    listV_publicacion.setAdapter(arrayAdapterPublicacion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void ClickMenu(View view){
        //opne drawer
        openDrawer(drawerLayout);
    }
    public static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickLogo (View view){
        //Close drawer
        closeDrawer(drawerLayout);
    }
    public static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        //check condition
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //when drawer is open
            //close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public void ClickHome(View view){
        //recreate activity
        recreate();
    }
    public void ClickPublicar(View view){
        //Redirect activity
        redirectActivity(this, interfaz_crear_nota.class);
    }
    public void ClickAboutUs(View view){
        //Redirect activity
        redirectActivity(this, interfaz_perfil.class);
    }
    public void ClickLogout(View view){
        //Redirect activity
        //logout(this);
        redirectActivity(this, interfaz_cerrar_sesion.class);
    }
    public static void redirectActivity(Activity activity, Class aclass) {
        //initialize intent
        Intent intent = new Intent(activity,aclass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //start activity
        activity.startActivity(intent);
    }

    @Override
    protected  void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }
}