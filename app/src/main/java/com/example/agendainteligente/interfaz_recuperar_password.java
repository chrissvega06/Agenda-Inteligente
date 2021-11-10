package com.example.agendainteligente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class interfaz_recuperar_password extends AppCompatActivity {
    private EditText mEditTextEmail;
    private Button mButtonResetPassword;
    private Button mButtonCancelResetPassword;
    private  String email ="";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_recuperar_password);

        mAuth = FirebaseAuth.getInstance();
        mEditTextEmail = (EditText) findViewById(R.id.id_reset_email);
        mButtonResetPassword = (Button) findViewById(R.id.id_button_continue_reset);

        mButtonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEditTextEmail.getText().toString();
                if(!email.isEmpty()){
                    resetPassword();
                }else{
                    mEditTextEmail.setError("Requerido");
                    //Toast.makeText(reset_password.this,"Debe ingresar su email",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void resetPassword() {
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(interfaz_recuperar_password.this,"Se le envio un correo para el cambio de contraseña",Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent intent = new Intent(interfaz_recuperar_password.this, MainActivity.class );
                    startActivity(intent);
                }else{
                    Toast.makeText(interfaz_recuperar_password.this,"No se pudo enviar el correo",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void returnlogin (View view){
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }
}