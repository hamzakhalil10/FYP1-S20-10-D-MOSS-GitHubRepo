package com.example.moss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class SlaveMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private LinearLayout fireButton;
    private LinearLayout intrusionButton;
    private Button logoutButton;
    private DrawerLayout drawer;
    private TextView userName_nav;
    private ImageView profilePic;
    FirebaseAuth fAuth;
    StorageReference storageReference;
    View mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slave_main);

        Toolbar toolbar = findViewById(R.id.toolbar_slave);
        setSupportActionBar(toolbar);

        //initializing firebase
        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //displaying navigation drawer
        drawer = findViewById(R.id.drawer_layout_slave);
        NavigationView navigationView = findViewById(R.id.nav_view_slave);

        //initializing nav view variables
        mHeaderView = navigationView.getHeaderView(0);
        userName_nav = mHeaderView.findViewById(R.id.userName_nav_header_slave);
        profilePic = mHeaderView.findViewById(R.id.profilePic_slave);

        //fetching username in nav drawer from firebase
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(fAuth.getCurrentUser().getUid());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name = dataSnapshot.child("Name").getValue(String.class);
                    userName_nav.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                userName_nav.setText("Name");
            }
        });

        //setting profile picture
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profilePic.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePic);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //if fire button gets clicked
        fireButton = findViewById(R.id.fireButton);
        fireButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openActivity();
            }
        });

        //if intrusion button gets clicked
        intrusionButton = findViewById(R.id.IntrusionButton);
        intrusionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openActivity();
            }
        });

        //if logout button gets clicked
        logoutButton = findViewById(R.id.Slave_LogOut);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(SlaveMain.this,SignIn.class);
                startActivity(intent1);
                finish();
            }
        });
    }

    public void openActivity(){
        Intent intent1 = new Intent(SlaveMain.this,CameraActivity.class);
        startActivity(intent1);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_switch:
                Intent intent = new Intent(this, MasterMainActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_logout_slave:
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
