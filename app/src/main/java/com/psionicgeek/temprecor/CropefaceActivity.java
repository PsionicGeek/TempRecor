package com.psionicgeek.temprecor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CropefaceActivity extends AppCompatActivity {
    ImageView face;
    Bitmap bitmap;
    FaceDetector detector;
    private static final int pic_id = 100;
    InputImage image;
    Task<List<Face>> Facetask;
    RectF rectF;
    Bundle bundle;
    Rect rect;
    Uri mImageCaptureUri1;
    ContentValues values;
    Button tempButton;
TextView tempView;
    ProgressBar pgsBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropeface);
        pgsBar = (ProgressBar) findViewById(R.id.pBar);
        pgsBar.setVisibility(View.GONE);
        tempButton= findViewById(R.id.tempButton);
        tempView= findViewById(R.id.tempView);
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Text_Reco.class));

            }
        });
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");//for quality
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");//for quality
        mImageCaptureUri1 = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);//for quality

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri1);//for quality
        startActivityForResult(intent, pic_id);




    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.temperature!=null){
            setTemperature();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
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
            bitmap = Bitmap.createBitmap(scaledbitmap, 0, 0, scaledbitmap.getWidth(), scaledbitmap.getHeight(), matrix, true);

            drawFaceContainer();
            final Handler handler = new Handler();


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms

                    addFace();
                }
            }, 2000);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void setTemperature(){
        tempView.setText(Constants.temperature);
    }

    public void addFace() {

        face=findViewById(R.id.cropeface);
        face.setImageBitmap(Constants.bitmapTransfer.getBitmap());
        pgsBar.setVisibility(View.GONE);
    }

//    public void cropFaceButtonPressed() {
//        Intent intent = new Intent(getApplicationContext(), CropefaceActivity.class);
//
//        startActivity(intent);
//    }

    public void drawFaceContainer() {
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build();
        detector = FaceDetection.getClient(options);
        image = InputImage.fromBitmap(bitmap, 0);
        Facetask = detector.process(image);
        Facetask.addOnSuccessListener(new OnSuccessListener<List<Face>>() {
            @Override
            public void onSuccess(@NonNull @NotNull List<Face> faces) {
                Constants.bitmapTransfer = new BitmapTransfer(bitmap);

                Face  singleface = faces.get(0);
                rect = singleface.getBoundingBox();
//                    imageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                Constants.bitmapTransfer.setBitmap(cropBitmap(bitmap, rect));



            }
        });
    }

    private Bitmap cropBitmap(Bitmap cbitmap, Rect rect) {
        int w = rect.right - rect.left;
        int h = rect.bottom - rect.top;
        Bitmap ret = Bitmap.createBitmap(h + rect.top, h + rect.top, cbitmap.getConfig());
        Canvas canvas = new Canvas(ret);
        canvas.drawBitmap(cbitmap, -rect.left+rect.left/2, (float) (-rect.top + rect.top / 1.9), null);
        return ret;

    }
}