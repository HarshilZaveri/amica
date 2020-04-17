package com.example.amica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Email_signup extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText et_email,et_password;
    FlatButton _continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_signup);
        mAuth =FirebaseAuth.getInstance();
        et_email=findViewById(R.id.email);
        et_password=findViewById(R.id.password);
        _continue=findViewById(R.id._continue);
        _continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= et_email.getText().toString();
                String password =et_password.getText().toString();
                SignIn(email,password);
            }
        });
    }

    private void SignIn(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Email_signup.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void updateUI(FirebaseUser firebaseUser){
        Intent intent =new Intent(Email_signup.this,Email_login.class);
        startActivity(intent);
    }
}
