package com.psionicgeek.temprecor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class Verify extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_verify);
        CircularProgressButton getLink= findViewById(R.id.getVerificationLink);
        CircularProgressButton signOut= findViewById(R.id.signOut);
        firebaseAuth= FirebaseAuth.getInstance();
        System.out.println(firebaseAuth.getCurrentUser().isEmailVerified()+"Hello");
        firebaseAuth.getCurrentUser().reload();
        if(firebaseAuth.getCurrentUser().isEmailVerified()){

            startActivity(new Intent(this,MainPage.class));
            finish();
        }
        else {
        getLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLink.startAnimation();
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Sent Verification Link",Toast.LENGTH_SHORT).show();
                        getLink.revertAnimation();
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(firebaseAuth.getCurrentUser().isEmailVerified()){

                                    startActivity(new Intent(getApplicationContext(),MainPage.class));
                                    finish();
                                }
                                handler.postDelayed(this,20000);
                            }
                        },20000);
                    }
                });

            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut.startAnimation();
                firebaseAuth.signOut();
                signOut.revertAnimation();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
        }

    }
}