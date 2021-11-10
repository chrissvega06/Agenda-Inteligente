package com.example.agendainteligente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class interfaz_Registrar_usuario extends AppCompatActivity {

    private static final int RECOGNIZE_SPEECH_ACTIVITY2 = 1;
    String clave_usu = "";
    String key = "";
    String contra = "";
    String varClave = "";
    int count = 0 ;
    TextView password_capturada;
    ImageView regresar_menu;
    EditText email_capturado;
    ImageView mirar_password;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    Button crear_usuario;
    ImageView boton_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz__registrar_usuario);
        password_capturada = findViewById(R.id.id_password_captured);

        regresar_menu = findViewById(R.id.id_return_menu);
        mirar_password = findViewById(R.id.id_see_password);
        email_capturado = findViewById(R.id.id_email_new);
        crear_usuario = findViewById(R.id.id_usuario_new_save);
        boton_info = findViewById(R.id.id_info);
        mirar_password.setVisibility(View.GONE);
        if(varClave!=null)
        {

                mirar_password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(count==0) {
                            password_capturada.setText(varClave);
                            count = 1;
                        }else{
                            password_capturada.setText("Contraseña de voz capturada.");
                            count = 0;
                        }
                        }
                });


        }

        regresar_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(interfaz_Registrar_usuario.this,MainActivity.class));
                finish();
            }
        });
        crear_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarInformacion();
            }
        });

        boton_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(interfaz_Registrar_usuario.this);
                alerta.setMessage("El correo que registraras será utilizado para tu seguirdad, ya que si requieres de un cambio de contraseña por alguna razón podemos restablecer tu contraseña, además se usara tu huella dactilar como medida de seguirdad para ingresar posteriormente a tu agenda.").setCancelable(false).setPositiveButton("Estoy de acuerdo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }
                );
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Información del correo");
                titulo.show();

            }
        });
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
                    password_capturada.setText("Contraseña de voz capturada.");
                    mirar_password.setVisibility(View.VISIBLE);


                }
                break;
            default:
                break;
        }
    }
    public void onClickImgBtnHablarclave(View view) {
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


    //Proceso para subir la información completa del usuario
    public void cargarInformacion()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String id_correo = email_capturado.getText().toString();
        String id_password_voz_capturada = varClave;
        if(TextUtils.isEmpty(id_correo))
        {
            Toast.makeText(interfaz_Registrar_usuario.this, "Por favor ingresa un correo.", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(id_password_voz_capturada))
        {
            Toast.makeText(interfaz_Registrar_usuario.this, "Por favor ingresa una contraseña de voz.", Toast.LENGTH_LONG).show();
            return;
        }

        //Declaramos variables para saber si hay internet

        ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final Boolean wifitrue = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        final Boolean redtrue = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        if (!wifitrue && !redtrue) {
            Toast.makeText(interfaz_Registrar_usuario.this, "Por favor verifica que tengas conexión a internet.", Toast.LENGTH_LONG).show();
            return;
        }

        //Creamos un cuadro de dialogo para confirmar los datos...
        AlertDialog.Builder alerta = new AlertDialog.Builder(interfaz_Registrar_usuario.this);
        alerta.setMessage("¿Estás seguro que los datos son correctos?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firebaseAuth.createUserWithEmailAndPassword(id_correo, id_password_voz_capturada.trim()).addOnCompleteListener(interfaz_Registrar_usuario.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {


                                    //Obtenemos el Uid para utilizarlo despues
                                    FirebaseUser user = firebaseAuth.getCurrentUser();


                                    //Volvemos a declarar los strings
                                    String uid = user.getUid();
                                    String correo = id_correo;
                                    String contra = id_password_voz_capturada.trim();

                                    //LLenamos la base de datos con los datos obtenidos anteriormente
                                    databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios registrados/" + uid);
                                    databaseReference.child("Correo").setValue(correo);
                                    databaseReference.child("Contraseña").setValue(contra);
                                    startActivity(new Intent(interfaz_Registrar_usuario.this, MainActivity.class));
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Se ha creado exitosamente el usuario con el correo " + correo + ".\nAhora debes confirmar tu cuenta en tu correo.", Toast.LENGTH_LONG).show();

                                    user.sendEmailVerification();


                                } else {

                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                        Toast.makeText(getApplicationContext(), "El usuario ya existe.", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Ocurrió un error en la aplicación.\nPor favor intenta de nuevo.", Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                }
                            }
                        });
                    }
                }
        ).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog titulo = alerta.create();
        titulo.setTitle("Confirmación");
        titulo.show();
    }

}