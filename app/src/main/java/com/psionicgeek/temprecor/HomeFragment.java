package com.psionicgeek.temprecor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class HomeFragment extends Fragment {
    private FirebaseAuth firebaseAuth;



    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.home_fragment,container,false);
        firebaseAuth= FirebaseAuth.getInstance();
        CircularProgressButton signOut= root.findViewById(R.id.signOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut.startAnimation();
                firebaseAuth.signOut();
                signOut.revertAnimation();
                startActivity(new Intent(getActivity(),LoginActivity.class));
            }
        });

        return root;
    }

}
