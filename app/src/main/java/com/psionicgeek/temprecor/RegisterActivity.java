package com.psionicgeek.temprecor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class RegisterActivity extends AppCompatActivity {
    private EditText email;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private EditText password;
    private EditText rePassword;
    private EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();
        CircularProgressButton registerButton= findViewById(R.id.cirRegisterButton);
        email= findViewById(R.id.editTextEmail);
        name= findViewById(R.id.editTextName);
        password= findViewById(R.id.editTextPassword);
        rePassword= findViewById(R.id.editTextRePassword);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        final String[] uid = new String[1];
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                registerButton.startAnimation();
                if(rePassword.getText().toString().equals(password.getText().toString())){
                    if(!name.getText().toString().isEmpty()){
                        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                               uid[0] = firebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference=firebaseFirestore.collection("UserDetail").document(uid[0]);
                                Map<String,Object> user= new HashMap<>();
                                user.put("UserName",name.getText().toString());
                                user.put("Email",email.getText().toString());
                                user.put("Password",password.getText().toString());
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        registerButton.revertAnimation();
                                        Toast.makeText(getApplicationContext(),"Please Verify",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),Verify.class));
                                        finish();
                                    }
                                });
                            }
                        });

                    }
                    else {
                        name.setError("!Enter Name");
                        registerButton.revertAnimation();
                    }
                }
                else {
                    rePassword.setError("! Incorrect Confirm Password");
                    registerButton.revertAnimation();
                }
            }
        });

    }
    public void changeStatusBarColor(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window= getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));

        }

    }
    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}