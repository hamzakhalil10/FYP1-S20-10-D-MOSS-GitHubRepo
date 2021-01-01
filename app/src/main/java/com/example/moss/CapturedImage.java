package com.example.moss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class CapturedImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captured_image);

        Intent receiveIntent = getIntent();
        String image = receiveIntent.getStringExtra("alert_image");

        //if back arrow is pressed
        ImageView bArrow = findViewById(R.id.bArrow);
        bArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CapturedImage.this, Notifications.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView capturedImage = findViewById(R.id.capturedImage);
        Picasso.get().load(image).into(capturedImage);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Notifications.class);
        startActivity(intent);
        finish();
    }
}
