package com.example.agendainteligente;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class interfaz_seguridad extends AppCompatActivity implements BiometricCallback {
    TextView texto_hablar;
    ImageView microfono;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    String varClave = "";
    String correo_user;
    private static final int RECOGNIZE_SPEECH_ACTIVITY2 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_seguridad);
        texto_hablar = findViewById(R.id.id_text_speak);
        microfono = findViewById(R.id.id_microfono);
        texto_hablar.setVisibility(View.GONE);
        microfono.setVisibility(View.GONE);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {

                    String correo = firebaseUser.getEmail().toString();
                    correo_user = correo;
                }
            }
        });

        new BiometricManager.BiometricBuilder(interfaz_seguridad.this)
                .setTitle("Autentificación dactilar")
                .setSubtitle("")
                .setDescription("Por favor usa tu huella dactilar que tienes guardada en tu correo para poder acceder a tu agenda inteligente.")
                .setNegativeButtonText("Cancelar")
                .build()
                .authenticate(interfaz_seguridad.this);


    }

    @Override
    public void onSdkVersionNotSupported() {

    }

    @Override
    public void onBiometricAuthenticationNotSupported() {

    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {

    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {

    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {

    }

    @Override
    public void onAuthenticationFailed() {

    }

    @Override
    public void onAuthenticationCancelled() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(interfaz_seguridad.this);
        alerta.setMessage("Necesitas acceder con tu huella antes del reconocimiento de voz. \n\nNota: Si registraste tu correo sin una huella, por favor registra un correo con una huella disponible.").setCancelable(false).setPositiveButton("Volver a intentar a colocar mi huella", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(interfaz_seguridad.this,interfaz_seguridad.class));
                    }
                }
        ).setNegativeButton("Ir al Registro de usuarios", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(interfaz_seguridad.this,interfaz_Registrar_usuario.class));
            }
        });
        AlertDialog titulo = alerta.create();
        titulo.setTitle("Aviso");
        titulo.show();

    }

    @Override
    public void onAuthenticationSuccessful() {
        Toast.makeText(this, "Huella ingresada: correcta.", Toast.LENGTH_SHORT).show();
        texto_hablar.setVisibility(View.VISIBLE);
        texto_hablar.setText("Toca el micrófono y di tu contraseña de voz");
        microfono.setVisibility(View.VISIBLE);

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

    }

    public void onClickmicro(View view) {
        Intent intentActionRecognizeSpeech = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Configura el Lenguaje (Español-México)
        intentActionRecognizeSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-MX");
        try {
            startActivityForResult(intentActionRecognizeSpeech,
                    RECOGNIZE_SPEECH_ACTIVITY2);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Tú dispositivo no soporta el reconocimiento por voz",Toast.LENGTH_LONG);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RECOGNIZE_SPEECH_ACTIVITY2:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);
                    varClave=strSpeech2Text;
                    login_user();
                }
                break;
            default:
                break;
        }
    }
    public void login_user()
    {
        String correo = correo_user;
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios registrados");
        Query q = databaseReference.orderByChild(getString(R.string.correo_usuario)).equalTo(correo);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String password_voice = ds.child("Contraseña de voz").getValue().toString();
                        Toast.makeText(interfaz_seguridad.this, password_voice, Toast.LENGTH_SHORT).show();
                        if(varClave.equals(password_voice))
                        {
                            startActivity(new Intent(interfaz_seguridad.this,interfaz_notas.class));
                            finish();
                        }else
                        {
                            Toast.makeText(interfaz_seguridad.this, "Contraseña de voz erronea.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(interfaz_seguridad.this, "No hay contraseña de voz, porfavor comunicarse con el programador.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(interfaz_seguridad.this, "Ocurrio un problema con la app. Intente más tarde", Toast.LENGTH_SHORT).show();
            }
        });


    }


}