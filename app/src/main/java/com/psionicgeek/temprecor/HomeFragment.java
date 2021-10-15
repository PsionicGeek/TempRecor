package com.psionicgeek.temprecor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.StringTokenizer;

public class HomeFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    RecyclerView recview;
    DatabaseReference database;
    MyAdaptor adaptor;
    ArrayList<ModelClass> list;
    ProgressBar loading;
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.home_fragment,container,false);
        firebaseAuth= FirebaseAuth.getInstance();
        FloatingActionButton fab= root.findViewById(R.id.floatingActionButton);
        recview = root.findViewById(R.id.regview);
        loading=root.findViewById(R.id.loading);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));
        System.out.println("yha2");
        database = FirebaseDatabase.getInstance().getReference("Userinformation").child(firebaseAuth.getCurrentUser().getUid());
        recview.setHasFixedSize(true);
        list = new ArrayList<>();
        loading.setVisibility(View.VISIBLE);
        System.out.println("yha3");
        adaptor =new MyAdaptor(getContext(),list);
        recview.setAdapter(adaptor);
        database.addValueEventListener(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loading.setVisibility(View.VISIBLE);
                list.clear();
                System.out.println("this is the data i found");
                adaptor.notifyDataSetChanged();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    System.out.println("this is the data i found2");
//                    System.out.println(dataSnapshot.getKey());
                    for (DataSnapshot another : dataSnapshot.getChildren()){
//                        System.out.println("this is the data i found3");
//                        System.out.println(another);
                        for (DataSnapshot anotheragain : another.getChildren()){
//                            System.out.println("this is the data i found4");
                            System.out.println(anotheragain.getValue());
                            StringTokenizer stringTokenizer=new StringTokenizer(anotheragain.getKey());
                            String getDate=stringTokenizer.nextToken();
                            Date date=new Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                            String todayDate=formatter.format(date);
                            ModelClass user = anotheragain.getValue(ModelClass.class);
                            if(todayDate.equals(getDate)){

                            list.add(user);
                            }
                        }
                       // adaptor.notifyDataSetChanged();
                    }
//                    adaptor.notifyDataSetChanged();

                }
                list.sort(Comparator.comparing(ModelClass::getDateandtime).reversed());
                loading.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

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

//                getFragmentManager().beginTransaction().remove(getTargetFragment()).commit();
//                assert getFragmentManager() != null;
//                getFragmentManager().popBackStackImmediate();
                startActivity(new Intent(getActivity(),CropefaceActivity.class));
            }
        });




        return root;
    }







}
