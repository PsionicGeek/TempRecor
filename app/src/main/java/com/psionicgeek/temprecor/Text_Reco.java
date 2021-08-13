package com.psionicgeek.temprecor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.util.List;

public class Text_Reco extends AppCompatActivity {
    private static final int pic_id = 100;
    Uri mImageCaptureUri1;
    ContentValues values;
    Bundle bundle;
    Bitmap bitmap;
    ProgressBar pgsBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_reco);
        pgsBar = (ProgressBar) findViewById(R.id.pBar2);
        pgsBar.setVisibility(View.GONE);
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");//for quality
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");//for quality
        mImageCaptureUri1 = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);//for quality

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri1);//for quality
        startActivityForResult(intent, pic_id);


    }

    private void kill() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pgsBar.setVisibility(View.VISIBLE);
        if (requestCode == pic_id && resultCode == RESULT_OK && data != null && data.getData() != null) {

            bundle = data.getExtras();

        }
        try {
            Bitmap fbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri1);//rotation
            Matrix matrix = new Matrix();//rotation
            matrix.postRotate(90);//angle
            Bitmap scaledbitmap = Bitmap.createScaledBitmap(fbitmap, fbitmap.getWidth(), fbitmap.getHeight(), true);//rotation
            bitmap = Bitmap.createBitmap(scaledbitmap, 0, 0, scaledbitmap.getWidth(), scaledbitmap.getHeight() - scaledbitmap.getHeight() / 3, matrix, true);
//            image.setImageBitmap(bitmap);
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                    .getOnDeviceTextRecognizer();
            Task<FirebaseVisionText> result =
                    detector.processImage(image)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                    String resultText = firebaseVisionText.getText();
                                    if (resultText == null)
                                        resultText = "try again";

                                    Constants.temperature = resultText;

                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Constants.temperature="try again";
                                        }
                                    });

            final Handler handler = new Handler();


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    pgsBar.setVisibility(View.GONE);
                    kill();

                }
            }, 1000);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}