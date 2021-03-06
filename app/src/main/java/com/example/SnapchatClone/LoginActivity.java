package com.example.SnapchatClone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;

    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference , userReference ;

    EditText editText1 , editText2;

    Intent intent= getIntent();

    public void register(View view) {

       Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);

       startActivity(intent);



    }

    public void login(View view){

        final String email = editText1.getText().toString();

        final String password = editText2.getText().toString();

        if(email.isEmpty() || password.isEmpty()){

            Toast.makeText(LoginActivity.this , "Please fill in the above info" , Toast.LENGTH_SHORT).show();

            return;

        }

        else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    if (authResult.getUser().isEmailVerified()) {

                        // Toast.makeText(LoginActivity.this , "Login Successful !" , Toast.LENGTH_SHORT).show();

                        String userID = authResult.getUser().getUid();

                        userReference = databaseReference.child(userID);

                        Map<String, Object> dataToSave = new HashMap<String, Object>();

                        dataToSave.put("Email", email);

                        dataToSave.put("Password", password);


                        userReference.updateChildren(dataToSave);

                        Intent intent = new Intent(LoginActivity.this, CreateSnapActivity.class);

                        startActivity(intent);


                    } else {

                        Toast.makeText(LoginActivity.this, "Please verify your email through link sent on your mail to login !", Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }





    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("Users");



        firebaseAuth = FirebaseAuth.getInstance();

        editText1 = (EditText) findViewById(R.id.emailEditText);
        editText2 = (EditText) findViewById(R.id.passwordEditText);

        if(firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().isEmailVerified() == true){
            startActivity(new Intent(LoginActivity.this,CreateSnapActivity.class));
        }


    }
}