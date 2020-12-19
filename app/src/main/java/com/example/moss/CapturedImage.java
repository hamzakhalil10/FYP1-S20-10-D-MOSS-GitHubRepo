package com.example.moss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CapturedImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captured_image);

        //if back arrow is pressed
        ImageView bArrow = findViewById(R.id.bArrow);
        bArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CapturedImage.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        //get the image
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        byte[] bytes = extras.getByteArray("capture");
        if(bytes != null){
            ImageView capturedImage = findViewById(R.id.capturedImage);
            Bitmap decodeBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            Bitmap rotateBitmap = rotate(decodeBitmap);
            capturedImage.setImageBitmap(rotateBitmap);
        }

    }

    private Bitmap rotate(Bitmap decodeBitmap) {
        int width = decodeBitmap.getWidth();
        int height = decodeBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        return Bitmap.createBitmap(decodeBitmap,0,0,width,height,matrix,true);
    }
}
