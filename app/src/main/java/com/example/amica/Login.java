package com.example.amica;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import at.markushi.ui.CircleButton;

public class Login extends AppCompatActivity {

    CircleButton btn_google;
    CircleButton btn_facebook;
    FlatButton btn_phone,btn_email;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    CallbackManager mCallbackManager;
    LoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_google=findViewById(R.id.btn_google);
        btn_facebook=findViewById(R.id.btn_facebook);
        btn_phone = findViewById(R.id.btn_phone);
        btn_email = findViewById(R.id.btn_email);
        firebaseAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.LoginButton);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FacebookAuthentication","onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Login.this,Email_login.class);
                startActivity(intent);
            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });
        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this,phone_login.class);
                startActivity(i);
            }
        });
    }


    private void googleSignIn(){
        Intent intent =googleSignInClient.getSignInIntent();
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount acc =completedTask.getResult(ApiException.class);
            Toast.makeText(Login.this,"Signed In Successfully",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e){
            Toast.makeText(Login.this,"Sign In Unsuccessfull ",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }

    }
    private void FirebaseGoogleAuth(GoogleSignInAccount acct){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this,"Successful",Toast.LENGTH_SHORT).show();
                    FirebaseUser user= firebaseAuth.getCurrentUser();
                    updateUI(user);
                }
                else{
                    Toast.makeText(Login.this,"Failed",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }
    private void updateUI(FirebaseUser fu){
        if(fu!=null){
            Intent intent =new Intent(Login.this,Home.class);
            startActivity(intent);
        }
    }
    private void handleFacebookToken(AccessToken token){
        Log.d("FacebookAuthentication","handlrFacebookToken"+ token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("FirebaseAuthentication","sign in with credential successful");
                    FirebaseUser user =firebaseAuth.getCurrentUser();
                    updateUI(user);
                }
                else {
                    updateUI(null);
                }
            }
        });
    }
}
