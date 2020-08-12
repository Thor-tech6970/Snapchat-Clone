package com.example.SnapchatClone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText emailEditText , passwordEditText;

    FirebaseAuth firebaseAuth;


    public void create(View view){


    String email = emailEditText.getText().toString();

    String password = passwordEditText.getText().toString();

    if(email.isEmpty() || password.isEmpty()){

        Toast.makeText(RegisterActivity.this , "Please fill in the above info" , Toast.LENGTH_SHORT).show();

        return;

    }

    else {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                authResult.getUser().sendEmailVerification();

                Toast.makeText(RegisterActivity.this, "Account created ! Please verify it through link sent on your given email .", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                startActivity(intent);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = (EditText) findViewById(R.id.emailEditText);

        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        firebaseAuth = FirebaseAuth.getInstance();
    }
}