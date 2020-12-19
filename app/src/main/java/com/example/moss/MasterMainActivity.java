package com.example.moss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;



import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.text.DateFormat;
import java.util.Calendar;

import javax.annotation.Nullable;

public class MasterMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private RelativeLayout myProfile;
    private RelativeLayout switchMode;
    private RelativeLayout notifications;
    private RelativeLayout logout_button;
    private TextView userName;
    private TextView userName_nav;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    String userName_temp;
    View mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());

        //get username below the toolbar
        userName = findViewById(R.id.userName);
        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                userName_temp = documentSnapshot.getString("Name");
                userName.setText("Welcome " + userName_temp +"!");

            }
        });

        TextView textViewDate = findViewById(R.id.date);
        textViewDate.setText(currentDate);

        //if my profile button is pressed
        myProfile = findViewById(R.id.myProfile);
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MasterMainActivity.this, myProfile.class);
                startActivity(intent1);
            }
        });

        //if switch mode button is pressed
        switchMode = findViewById(R.id.switchMode);
        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MasterMainActivity.this, SlaveMain.class);
                startActivity(intent1);
            }
        });

        //if notifications button is pressed
        notifications = findViewById(R.id.notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MasterMainActivity.this, Notifications.class);
                startActivity(intent1);
            }
        });

        //if logout button is pressed
        logout_button = findViewById(R.id.signOut);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(MasterMainActivity.this, SignIn.class);
                startActivity(intent1);
                finish();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mHeaderView = navigationView.getHeaderView(0);
        userName_nav = mHeaderView.findViewById(R.id.userName_nav_header);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                userName_nav.setText(documentSnapshot.getString("Name"));

            }
        });


        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        //}

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(this, SignIn.class);
                startActivity(intent1);
                finish();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
}

