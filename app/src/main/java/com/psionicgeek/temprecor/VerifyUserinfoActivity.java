package com.psionicgeek.temprecor;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class VerifyUserinfoActivity extends AppCompatActivity {
    ProgressBar mainprogressbar;
    TextView maintextview;
    Forrealtime info = new Forrealtime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_userinfo);
        mainprogressbar = findViewById(R.id.progressBar1);
        maintextview=findViewById(R.id.textView1);
        maintextview.setVisibility(View.GONE);
        mainprogressbar.setVisibility(View.VISIBLE);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Bitmap tempBitmap =Constants.bitmapTransfer.getBitmap() ;
        StorageReference mountainImagesRef = storageRef.child("images").child("userimage").child(UUID.randomUUID().toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tempBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Notsucessfull");
                System.out.println(exception.getMessage());
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();
                mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull Uri uri) {
                        Constants.Download_Uri = uri.toString();
                        System.out.println("This is the " + uri.toString());
                        final Handler handler = new Handler();


                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                UploadInfo();
                                final Handler anotherhanler = new Handler();
                                anotherhanler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                       // startActivity(new Intent(getApplicationContext(),MainPage.class));
                                        finish();
                                    }
                                },2000);
                            }
                        }, 1000);

                    }
                });
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });






    }

    private void UploadInfo() {

        Constants newconstants = new Constants(Constants.Download_Uri,Constants.temperature,Constants.UserMobileNumber,Constants.Name,Constants.DateAndTime);
        info.add(newconstants).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull @NotNull Void unused) {
                System.out.println("Infouploaded");
                Toast.makeText(getApplicationContext(),"Information Upload",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                System.out.println("This is error");
                System.out.println(e.getMessage());
            }
        });
        System.out.println("Amiya4");
    }
}