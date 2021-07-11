package com.psionicgeek.temprecor;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

import static androidx.media.MediaBrowserServiceCompat.RESULT_OK;

public class HomeFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.home_fragment,container,false);
        firebaseAuth= FirebaseAuth.getInstance();
        FloatingActionButton fab= root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),FaceCheck.class));
            }
        });

        return root;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {

        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            System.out.println("Done1");
            processFaceDetection(imageBitmap);
        }
    }
    public void processFaceDetection(Bitmap imageBitmap){
        FirebaseVisionImage firebaseVisionImage= FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionFaceDetectorOptions firebaseVisionFaceDetectorOptions= new FirebaseVisionFaceDetectorOptions.Builder().build();
        FirebaseVisionFaceDetector firebaseVisionFaceDetector= FirebaseVision.getInstance().getVisionFaceDetector(firebaseVisionFaceDetectorOptions);
        firebaseVisionFaceDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {

                System.out.println("Done");
                    getFaceResults(firebaseVisionFaces);
                Toast.makeText(getContext(),"Got Faces",Toast.LENGTH_LONG).show();
            }
            
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(),"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getFaceResults(List<FirebaseVisionFace> firebaseVisionFaces) {
        FirebaseVisionFace face= firebaseVisionFaces.get(0);
    }
}
