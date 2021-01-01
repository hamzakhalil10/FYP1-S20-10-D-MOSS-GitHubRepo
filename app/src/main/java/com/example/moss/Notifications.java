package com.example.moss;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends AppCompatActivity {
    private ImageView arrowBack;

    private FirebaseAuth mAuth;
    private DatabaseReference RootReference;
    private String currentUserID;

    private List<ClassAlert> AllAlerts = new ArrayList<>();
    private List<String> NewAlerts = new ArrayList<>();

    private RecyclerView RecyclerView;
    private AdapterAlerts AdapterAlerts;
    private TextView clearAlerts;
    private TextView noNewAlert;

    NotificationCompat.Builder builder;
    NotificationCompat.Builder builder2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("AlertNotifications", "AlertNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        //open from notification
        Intent intent1 = new Intent(this, Notifications.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent1,0);

        builder = new NotificationCompat.Builder(this, "AlertNotifications")
                .setContentTitle("MOSS")
                .setSmallIcon(R.drawable.moss3)
                .setAutoCancel(true)
                .setContentText("WARNING : A fire has been detected")
                .setContentIntent(contentIntent);

        builder2 = new NotificationCompat.Builder(this, "AlertNotifications")
                .setContentTitle("MOSS")
                .setSmallIcon(R.drawable.moss3)
                .setAutoCancel(true)
                .setContentText("WARNING : An intruder has been detected");

        noNewAlert = findViewById(R.id.no_new_alert);

        arrowBack = findViewById(R.id.arrowBackIcon_notifications);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        clearAlerts = findViewById(R.id.clear_alerts);
        clearAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAlerts();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        RootReference = FirebaseDatabase.getInstance().getReference();
        currentUserID = mAuth.getCurrentUser().getUid();

        getAllAlerts();

        if (AllAlerts.isEmpty()) {
            noNewAlert.setVisibility(View.VISIBLE);
        }

        try {
            RecyclerView = (RecyclerView) findViewById(R.id.recycler_view_alerts);
            RecyclerView.setLayoutManager(new LinearLayoutManager(this));
            AdapterAlerts = new AdapterAlerts(AllAlerts, this);
            RecyclerView.setAdapter(AdapterAlerts);
        }
        catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    public void getAllAlerts() {
        RootReference.child("Alerts").child(currentUserID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ClassAlert classAlert = dataSnapshot.getValue(ClassAlert.class);
                AllAlerts.add(classAlert);
                AdapterAlerts.notifyDataSetChanged();
                RecyclerView.smoothScrollToPosition(RecyclerView.getAdapter().getItemCount());
                if (classAlert.getReceived().equals("false")) {
                    NewAlerts.add(dataSnapshot.getKey());
                    if (classAlert.getType().equals("fire")) {
                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(Notifications.this);
                        managerCompat.notify(999, builder.build());
                    }
                    else if (classAlert.getType().equals("intruder")){
                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(Notifications.this);
                        managerCompat.notify(999, builder2.build());
                    }
                }
                if (!AllAlerts.isEmpty()) {
                    noNewAlert.setVisibility(View.GONE);
                }
                updateChildren();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateChildren() {
        for (int i = 0 ; i < NewAlerts.size() ; i++) {
            if(!RootReference.child("Alerts").child(currentUserID).child(NewAlerts.get(i)).child("received").equals(null)) {
                RootReference.child("Alerts").child(currentUserID).child(NewAlerts.get(i)).child("received").setValue("true");
            }
        }
    }

    private void deleteAlerts(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(Notifications.this);
        dialog.setTitle("Delete Alerts");
        dialog.setMessage("Are you sure you want to delete all the alerts?");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RootReference.child("Alerts").child(currentUserID).getRef().removeValue();
                AllAlerts.clear();
                AdapterAlerts.notifyDataSetChanged();
                RecyclerView.smoothScrollToPosition(RecyclerView.getAdapter().getItemCount());
                noNewAlert.setVisibility(View.VISIBLE);
            }
        });

        dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
