package com.psionicgeek.temprecor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class CropefaceActivity extends AppCompatActivity {
    ImageView face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropeface);
        face=findViewById(R.id.cropeface);
        face.setImageBitmap(Constants.bitmapTransfer.getBitmap());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}