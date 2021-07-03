package com.psionicgeek.temprecor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {
        private EditText email;
        private FirebaseAuth firebaseAuth;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
        CircularProgressButton loginButton= findViewById(R.id.cirLoginButton);
         email= findViewById(R.id.editTextEmail);
        password= findViewById(R.id.editTextPassword);
        firebaseAuth= FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                startActivity(new Intent(this,MainPage.class));
                finish();
            }
            else{
                startActivity(new Intent(this,Verify.class));
                finish();
            }
        }
        else {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginButton.startAnimation();
                    if(!email.getText().toString().isEmpty()){
                        if (!password.getText().toString().isEmpty()){
                    String enterEmail=email.getText().toString();
                    String enterPass=password.getText().toString();
                    firebaseAuth.signInWithEmailAndPassword(enterEmail,enterPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            loginButton.stopAnimation();
                            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(getApplicationContext(),"Welcome",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainPage.class));
                                finish();

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Please Verify",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Verify.class));
                                finish();
                            }
                        }
                        else {
                            loginButton.revertAnimation();
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();

                        }
                        }
                    });}
                        else {
                            password.setError("!Enter Password");
                            loginButton.revertAnimation();
                        }
                    }
                    else{
                        email.setError("!Enter Email");
                        loginButton.revertAnimation();
                    }
                }
            });

        }
    }
    public void onLoginClick(View view){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
        finish();
    }
}