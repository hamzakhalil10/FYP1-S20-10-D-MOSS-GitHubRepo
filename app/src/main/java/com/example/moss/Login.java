package com.example.moss;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {

    private Button MasterButton;
    private Button SlaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MasterButton = (Button) findViewById(R.id.login_mast);
        MasterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openLoginMaster();
            }
        });

        SlaveButton = (Button) findViewById(R.id.login_slave);
        SlaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openLoginSlave();
            }
        });
    }

    public void openLoginMaster(){
        Intent intent1 = new Intent(this, MasterMainActivity.class);
        startActivity(intent1);
    }

    public void openLoginSlave(){
        Intent intent = new Intent(this, SlaveMain.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
