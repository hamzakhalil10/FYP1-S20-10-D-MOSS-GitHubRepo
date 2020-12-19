package com.example.moss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.TimerTask;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
