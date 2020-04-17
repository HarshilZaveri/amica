package com.example.amica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class phone_login extends AppCompatActivity {

    EditText phone_no;
    Spinner country_code;
    FlatButton btn_continue;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        phone_no = findViewById(R.id.phone_no);
        country_code = findViewById(R.id.country_code);
        country_code.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,CountryDetails.country));
        btn_continue = findViewById(R.id.btn_continue);
        firebaseAuth = FirebaseAuth.getInstance();


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = CountryDetails.code[country_code.getSelectedItemPosition()];
                String phone = phone_no.getText().toString();
                if(phone.isEmpty()){
                    phone_no.setError("Phone number is required");
                    phone_no.requestFocus();
                    return;
                }
                if(phone.length()!=10){
                    phone_no.setError("Please enter a valid phone number");
                    phone_no.requestFocus();
                    return;
                }
                String phoneNumber =  code + phone;
                System.out.println(phoneNumber);
                Intent intent = new Intent(phone_login.this,phone_verification.class);
                intent.putExtra("phoneNumber",phoneNumber);
                startActivity(intent);
            }
        });
    }



}
