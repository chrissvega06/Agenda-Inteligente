package com.example.agendainteligente;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.nota;

public class interfaz_crear_nota extends AppCompatActivity {

    private List<nota> listPublicacion = new ArrayList<nota>();
    ArrayAdapter<nota> arrayAdapterPublicacion;

    DrawerLayout drawerLayout;
    EditText inf,desc,lugar;
    ListView listV_publicacion;

    TextView Tvfecha,TvDesc,Tvlugar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_crear_nota);
        drawerLayout = findViewById(R.id.drawer_layout);
        //LISTA
        listV_publicacion = findViewById(R.id.lv_infopublic);

        desc = findViewById(R.id.pDescription);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        Tvfecha = findViewById(R.id.fechapublic);
        Tvlugar =findViewById(R.id.lugarpublic);

        //listaDatos();

    }
    private void listaDatos () {
        databaseReference.child("Publicaciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPublicacion.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    nota p = objSnapshot.getValue(nota.class);
                    listPublicacion.add(p);

                    arrayAdapterPublicacion = new ArrayAdapter<nota>(interfaz_crear_nota.this, android.R.layout.simple_list_item_1, listPublicacion);
                    listV_publicacion.setAdapter(arrayAdapterPublicacion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //MENU DESPLEGABLE
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
        //Redirect activity
        redirectActivity(this, interfaz_notas.class);
    }
    public void ClickPublicar(View view){
        //recreate activity
        recreate();
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
    //TERMINA MENU DESPLEGABLE

    //aqui inicia funciones iconos
    public void MostrarPublish (View view){
        String descripcion = desc.getText().toString();
        if (descripcion.equals("")) {
            desc.setError("Requerido");
        } else {
            nota p = new nota();
            p.setUid(UUID.randomUUID().toString());
            p.setInfo(descripcion);
            databaseReference.child("Publicaciones").child(p.getUid()).setValue(p);
            Toast.makeText(this, "Guardado", Toast.LENGTH_LONG).show();
            limpiarCajas();
        }
    }
    //aqui termina funciones iconos

    private void limpiarCajas () {
        desc.setText("");
    }

}
