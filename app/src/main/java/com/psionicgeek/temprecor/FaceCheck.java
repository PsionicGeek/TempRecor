package com.psionicgeek.temprecor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class FaceCheck extends AppCompatActivity {
    ImageView imageView;
    Button face_submit_button;
    Button take_photo_button;
    Bitmap bitmap;
    FaceDetector detector;
    Bitmap tempBitmap;
    Canvas canvas;
    Button Cropeface;
    private static final int pic_id = 100;
    InputImage image;
    Task<List<Face>> Facetask;
    RectF rectF;
    Bundle bundle;
    Rect rect;
    Uri mImageCaptureUri1;
    Paint boxpaint;
    ContentValues values;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_check);
        imageView = findViewById(R.id.showImage);
        face_submit_button = findViewById(R.id.Face_Read_Submit_button);
        take_photo_button = findViewById(R.id.Take_Photo_button);
        Cropeface = findViewById(R.id.cropefaceactivity);
        Cropeface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CropefaceActivity.class);

                startActivity(intent);
            }
        });

        //Checking For the permission for  Camera
        if (ContextCompat.checkSelfPermission(FaceCheck.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FaceCheck.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    }
                    ,
                    100);
        }
        //permission check completed
        take_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");//for quality
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");//for quality
                mImageCaptureUri1 = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);//for quality

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra("android.intent.extras.CAMERA_FACING", 1);//open front camera only

                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri1);//for quality
                startActivityForResult(intent, pic_id);
            }

        });
        Paint boxPaint = new Paint();
        boxPaint.setStrokeWidth(5);
        boxPaint.setColor(Color.GREEN);
        boxPaint.setStyle(Paint.Style.STROKE);
        face_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();
                detector =  FaceDetection.getClient(options);
                image = InputImage.fromBitmap(bitmap,0);
                Facetask= detector.process(image);
                Facetask.addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull List<Face> faces) {
                        Constants.bitmapTransfer=new BitmapTransfer(bitmap);
                        for (Face singleface : faces){
                            singleface = faces.get(0);
                            rectF = new RectF(singleface.getBoundingBox());
                            rect = singleface.getBoundingBox();
                            canvas.drawRoundRect(rectF, 2, 2, boxPaint);
                            imageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
//                        faceCrop = Bitmap.createBitmap(bitmap,rect.left,(rect.top),(rect.width()),(rect.height()));
                            Constants.bitmapTransfer.setBitmap(cropBitmap(bitmap,rect));
                        }


                    }
                });

            }
        });

    }

    private Bitmap cropBitmap(Bitmap cbitmap, Rect rect) {
        int w = rect.right - rect.left;
        int h = rect.bottom - rect.top;
        Bitmap ret = Bitmap.createBitmap(w, h+rect.top,cbitmap.getConfig());
        Canvas canvas = new Canvas(ret);
        canvas.drawBitmap(cbitmap, -rect.left, (float) (-rect.top+rect.top/1.5), null);
        return ret;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id && resultCode == RESULT_OK && data != null && data.getData() != null ){
//            FilePathUri = data.getData();
            bundle = data.getExtras();
        }
        try {
            Bitmap fbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mImageCaptureUri1);//rotation
            Matrix matrix = new Matrix();//rotation
            matrix.postRotate(90);//angle
            Bitmap scaledbitmap = Bitmap.createScaledBitmap(fbitmap,fbitmap.getWidth(),fbitmap.getHeight(),true);//rotation
            bitmap = Bitmap.createBitmap(scaledbitmap, 0, 0,scaledbitmap.getWidth(), scaledbitmap.getHeight(), matrix, true);
            //bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            tempBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.RGB_565);
            canvas = new Canvas(tempBitmap);
            canvas.drawBitmap(bitmap,0,0,null);
        } catch( Exception e  ) {
            System.out.println(e.getMessage());
        }

    }




    //------------=================Self created Function===================------------------------------

}