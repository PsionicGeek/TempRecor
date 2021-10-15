package com.psionicgeek.temprecor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class Search_Fragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    RecyclerView recview;
    DatabaseReference database;
    MyAdaptor adaptor;
    ArrayList<ModelClass> list=new ArrayList<>();
    ProgressBar loading;
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        CircularProgressButton searchButton = root.findViewById(R.id.searchPhoneButton);
        FloatingActionButton downloadFab=root.findViewById(R.id.SaveData);
        EditText searchText = root.findViewById(R.id.editSearchPhone);
        recview = root.findViewById(R.id.recyclerview);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));
        System.out.println("yha2");
        loading=root.findViewById(R.id.loading2);
        loading.setVisibility(View.GONE);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);

                if (searchText.getText().length() == 10) {

                    database = FirebaseDatabase.getInstance().getReference("Userinformation").child(firebaseAuth.getCurrentUser().getUid()).child(searchText.getText().toString());
                    recview.setHasFixedSize(true);
                    list = new ArrayList<>();
                    System.out.println("yha3");
                    adaptor = new MyAdaptor(getContext(), list);
                    recview.setAdapter(adaptor);
                    database.addValueEventListener(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            System.out.println("this is the data i found");
                            adaptor.notifyDataSetChanged();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot another : dataSnapshot.getChildren()) {
//

//                                        StringTokenizer st=new StringTokenizer(anotheragain.getValue().toString());
//                                        System.out.println(st.countTokens());
                                    ModelClass user = another.getValue(ModelClass.class);
//                                            ModelClass user=new ModelClass();
//                                            user.setPhone(st.nextToken());
//                                            user.setTemp(st.nextToken());
//                                            user.setUserName(st.nextToken());
//                                            user.setCropedface(st.nextToken());
//                                            user.setDateandtime(st.nextToken());

                                    list.add(user);


                                }
                            }
                            list.sort(Comparator.comparing(ModelClass::getDateandtime).reversed());
                            loading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    list.clear();
                    searchText.setError("Enter 10 digit phone number");
                    loading.setVisibility(View.GONE);
                }
                searchButton.stopAnimation();

            }
        });
        downloadFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(list.isEmpty()){
                     Toast.makeText(getContext(),
                            "Please search data to download",Toast.LENGTH_LONG).show();
                }
                else{
                    Date date=new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                    String FormatedDate = formatter.format(date);

                }

                }

        });

        return root;
    }
}