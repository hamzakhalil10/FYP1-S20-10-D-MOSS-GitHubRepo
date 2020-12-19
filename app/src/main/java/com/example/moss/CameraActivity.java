package com.example.moss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import static java.security.AccessController.getContext;


public class CameraActivity extends AppCompatActivity{

    Camera camera;
    Camera.PictureCallback mPictureCallback;
    FrameLayout frameLayout;
    showCamera shCamera;
    final int CAMERA_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera1);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        //check camera permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
        else {
            camera = Camera.open();
            shCamera = new showCamera(this, camera);
            frameLayout.addView(shCamera);

            //if back arrow is pressed
            ImageView backArrow = findViewById(R.id.backArrow);
            backArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CameraActivity.this, SlaveMain.class);
                    startActivity(intent);
                }
            });

            //if capture button is pressed
            ImageView captureButton = findViewById(R.id.captureButton);
            captureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    captureImage();
                }
            });

            mPictureCallback = new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] bytes, Camera camera) {
                    Intent intent = new Intent(CameraActivity.this, CapturedImage.class);
                    intent.putExtra("capture", bytes);
                    Toast.makeText(CameraActivity.this, "Picture captured!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            };
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Please provide camera permissions from the settings",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void captureImage(){
        camera.takePicture(null,null, mPictureCallback);
    }
}
