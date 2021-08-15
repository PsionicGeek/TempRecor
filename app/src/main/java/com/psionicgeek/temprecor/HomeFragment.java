package com.psionicgeek.temprecor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    RecyclerView recview;
    MyAdaptor adaptor;
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.home_fragment,container,false);
        firebaseAuth= FirebaseAuth.getInstance();
        FloatingActionButton fab= root.findViewById(R.id.floatingActionButton);
        recview = root.findViewById(R.id.regview);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));
        System.out.println("yha2");
        FirebaseRecyclerOptions<ModelClass> options =
                new FirebaseRecyclerOptions.Builder<ModelClass>()
                        .setQuery(
                                FirebaseDatabase.getInstance().getReference("Userinformation")
                                .child(firebaseAuth.getCurrentUser().getUid()).child("7269047364").child("ravi kumar singh"),ModelClass.class)
                        .build();
        System.out.println("yha3");
        adaptor =new MyAdaptor(options);
        recview.setAdapter(adaptor);

        //Checking For the permission for  Camera
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.CAMERA
                    }
                    ,
                    100);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),CropefaceActivity.class));
            }
        });

        return root;
    }
    @Override
    public void onStart() {
        System.out.println("yha1");
        super.onStart();
        adaptor.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        adaptor.stopListening();
    }



}
