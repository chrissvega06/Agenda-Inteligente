package com.example.agendainteligente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class interfaz_cambiar_email extends AppCompatActivity {
    private EditText mEditTextEmail;
    private Button mButtonResetEmail;
    private  String email ="";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_cambiar_email);


        mAuth = FirebaseAuth.getInstance();
        mEditTextEmail = (EditText) findViewById(R.id.reset_email_ET);
        mButtonResetEmail = (Button) findViewById(R.id.button_reset_email);

        mButtonResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEditTextEmail.getText().toString();
                if(!email.isEmpty()){
                    resetEmail();
                }else{
                    mEditTextEmail.setError("Requerido");
                    //Toast.makeText(reset_password.this,"Debe ingresar su email",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void resetEmail() {
        mAuth.setLanguageCode("es");
        String NewEmail = mEditTextEmail.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(NewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    //ENVIAR EMAIL DE VERIFICACION
                    user.sendEmailVerification();
                    Toast.makeText(interfaz_cambiar_email.this, "Se modifico su correo", Toast.LENGTH_LONG).show();
                    Toast.makeText(interfaz_cambiar_email.this, "Se envio un correo de verificacion a su nuevo email", Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent intent = new Intent(interfaz_cambiar_email.this, MainActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(interfaz_cambiar_email.this, "No se pudo modificar su correo", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void cancelarresetemail (View view){
        Intent intent = new Intent(this,interfaz_perfil.class);
        startActivity(intent);
    }

    //FIN DEL JAVA
}
