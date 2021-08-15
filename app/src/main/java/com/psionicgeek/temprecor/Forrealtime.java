package com.psionicgeek.temprecor;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Forrealtime {
    private DatabaseReference databaseReference  ;
    public Forrealtime(){
        FirebaseDatabase dp = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userid = firebaseAuth.getCurrentUser().getUid().toString();
        databaseReference= dp.getReference("Userinformation").child(userid).child(Constants.UserMobileNumber).child(Constants.Name).child(Constants.DateAndTime);

    }

    public Task<Void> add(Constants constants){

        return  databaseReference.setValue(constants);

    }
}