package com.psionicgeek.temprecor;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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

import java.io.File;
import java.io.FileOutputStream;
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
    ArrayList<ModelClass> list = new ArrayList<>();
    ProgressBar loading;
    private static final int PERMISSION_REQUEST_CODE = 100;
    boolean isFABOpen;
    FloatingActionButton fab;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton downloadFab;
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        isFABOpen=false;
        firebaseAuth = FirebaseAuth.getInstance();
        CircularProgressButton searchButton = root.findViewById(R.id.searchPhoneButton);
         downloadFab = root.findViewById(R.id.SaveData);
         fab =  root.findViewById(R.id.fab);
         fab1 =  root.findViewById(R.id.fab1);
         fab2 =  root.findViewById(R.id.fab2);

        EditText searchText = root.findViewById(R.id.editSearchPhone);
        recview = root.findViewById(R.id.recyclerview);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));
        System.out.println("yha2");
        loading = root.findViewById(R.id.loading2);
        loading.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                hideKeyboard((CircularProgressButton)view);
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
                            if(list.size()==0){
                                Toast.makeText(getContext(),"No entry found for "+searchText.getText(), Toast.LENGTH_SHORT).show();
                            }
                            else{
                            list.sort(Comparator.comparing(ModelClass::getDateandtime).reversed());}
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
                if (list.isEmpty()) {
                    Toast.makeText(getContext(),
                            "Please search data to download", Toast.LENGTH_LONG).show();
                } else {
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                    String FormatedDate = formatter.format(date);
                    StringBuilder data = new StringBuilder();
                    data.append("name,phone,temp,profile,datetime");
                    for (ModelClass user : list) {
                        data.append("\n" + user.getUserName() + "," + user.getPhone() + "," + user.getTemp() + "," + user.getCropedface() + "," + user.getDateandtime());
                    }
                    if(checkPermission())
                    {

                       CreateCSV(data,getContext());
                    }else{
                        // If permission is not granted we will request for the Permission
                        requestPermission();
                    }


                }

            }

        });

        return root;
    }

    private void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch(Exception ignored) {
        }
    }

    private void CreateCSV(StringBuilder data, Context ctx) {
        Calendar calendar = Calendar.getInstance();
        long time= calendar.getTimeInMillis();
        try {
            //
            FileOutputStream out = ctx.openFileOutput("CSV_Data_"+time+".csv", Context.MODE_PRIVATE);

            //store the data in CSV file by passing String Builder data
            out.write(data.toString().getBytes());
            out.close();
            Context context = getContext();
            final File newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"SimpleCVS");
            System.out.println(Environment.getExternalStorageDirectory().getAbsolutePath());
            if(!newFile.exists())
            {
                newFile.mkdir();
            }
            File file = new File(context.getFilesDir(),"CSV_Data_"+time+".csv");
            Uri path = FileProvider.getUriForFile(context,"com.psionicgeek.temprecor",file);
            //once the file is ready a share option will pop up using which you can share
            // the same CSV from via Gmail or store in Google Drive
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            intent.putExtra(Intent.EXTRA_STREAM, path);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(intent,"Excel Data"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        downloadFab.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        downloadFab.animate().translationY(0);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    // request permission for WRITE Access
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
}