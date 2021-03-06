package com.example.agendainteligente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button usuario_nuevo;
    Button ingresar_usuario;
    EditText correo_login;
    EditText password_login;
    TextView recuperar_password;
    TextView ayuda_usuario;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo_login = findViewById(R.id.id_login_email);
        password_login = findViewById(R.id.id_login_password);
        recuperar_password = findViewById(R.id.id_reset_password);
        ayuda_usuario = findViewById(R.id.id_help_user);
        usuario_nuevo = findViewById(R.id.id_button_usernew);
        ingresar_usuario = findViewById(R.id.id_button_loginacess);
        firebaseAuth = FirebaseAuth.getInstance();

      inicialize();

        usuario_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, interfaz_Registrar_usuario.class));
                finish();
            }
        });

        ingresar_usuario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                final Boolean wifitrue = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                final Boolean redtrue = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                String correo = correo_login.getText().toString().trim();
                String contrase??a = password_login.getText().toString();

                if (TextUtils.isEmpty(correo) || TextUtils.isEmpty(contrase??a)) {
                    Toast.makeText(MainActivity.this, "No se ha ingresado el correo y/o la contrase??a.", Toast.LENGTH_LONG).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(correo, contrase??a)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                        startActivity(new Intent(MainActivity.this, interfaz_seguridad.class));
                                        finish();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Debes ingresar a tu email y confirmar la autentificaci??n. ", Toast.LENGTH_SHORT).show();
                                    }

                                } else {

                                    if (!wifitrue && !redtrue) {
                                        Toast.makeText(MainActivity.this, "Por favor verifica que tengas conexi??n a internet.", Toast.LENGTH_LONG).show();
                                    } else {

                                        Toast.makeText(MainActivity.this, "No se pudo iniciar sesi??n. Verifica bien tus datos.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
            }
        });
    }

    private void inicialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {

                    finish();
                    //startActivity(new Intent(MainActivity.this, interfaz_seguridad.class));
                    //PARA LA PRUEBA IRA A LA INTERFAZ PRINCIPAL DE NOTAS
                    startActivity(new Intent(MainActivity.this, interfaz_seguridad.class));
                    finish();

                }else {

                }
            }
        });

    }

    public void newpassword (View view){
        Intent intent = new Intent(this, interfaz_recuperar_password.class );
        finish();
        startActivity(intent);
    }

    public void Ayuda(View view) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setMessage("Te podemos ayudar, envianos un correo a cualquiera de los siguientes emails y redactanos tu problema" +
                "\n\n??christopher.suarezvg@uanl.edu.mx"+
                "\n??mirely.aguilarscd@uanl.edu.mx"+
                "\n??enrique.rebollosoga@uanl.edu.mx"+
                "\n??cesar.gonzalezto@uanl.edu.mx"+
                "\n??carlos.hermosillocnd@uanl.edu.mx").setCancelable(false).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(MainActivity.this,MainActivity.class));
                    }
                }
        ).setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });
        AlertDialog titulo = alerta.create();
        titulo.setTitle("??Necesitas ayuda?");
        titulo.show();

    }
    }
