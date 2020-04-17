package com.example.amica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Email_login extends AppCompatActivity {

    TextView new_user;
    FirebaseAuth mAuth;
    EditText et_email,et_password;
    FlatButton login_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        new_user =findViewById(R.id.new_user);
        et_email=findViewById(R.id.email);
        et_password=findViewById(R.id.password);
        login_email=findViewById(R.id.LoginEmail);
        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Email_login.this,Email_signup.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();

        login_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=et_email.getText().toString();
                String password =et_password.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(Email_login.this,"Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (password.isEmpty()){
                    Toast.makeText(Email_login.this,"Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    signIn(email,password);
                }
            }
        });

    }
    private void updateUI(FirebaseUser user){
        Intent intent =new Intent(Email_login.this,Home.class);
        startActivity(intent);
    }

    private void signIn(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user
                            Toast.makeText(Email_login.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });
    }
}
