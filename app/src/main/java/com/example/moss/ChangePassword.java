package com.example.moss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    private ImageView backArrow;
    private EditText currentPassword, newPassword, confirmNewPassword;
    private Button saveButton;
    private ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();


        backArrow = findViewById(R.id.arrowBackIcon_changPassword);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ChangePassword.this, myProfile.class);
                startActivity(intent1);
            }
        });

        currentPassword = findViewById(R.id.currentPassword);
        progressBar = findViewById(R.id.progress_changePassword);
        newPassword = findViewById(R.id.newPassword);
        confirmNewPassword = findViewById(R.id.confirmNewPassword);
        saveButton = findViewById(R.id.save_new_password);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword(){
        String currPassword = currentPassword.getText().toString();
        String newPass = newPassword.getText().toString();
        String cnfrmNewPass = confirmNewPassword.getText().toString();

        if(currPassword.isEmpty() && newPass.isEmpty() && cnfrmNewPass.isEmpty()){
            Toast.makeText(ChangePassword.this,"Please fill the credentials!",Toast.LENGTH_SHORT).show();
            currentPassword.setError("Please enter current password");
            newPassword.setError("Please enter new password");
            confirmNewPassword.setError("Please confirm your password");

        }
        else if(currPassword.isEmpty()){
            currentPassword.setError("Please enter current password");
            currentPassword.requestFocus();
        }
        else if(newPass.isEmpty()){
            newPassword.setError("Please enter new password");
            newPassword.requestFocus();
        }

        else if(newPass.length() < 6){
            newPassword.setError("Password must be greater than or equal to 6 characters");
            newPassword.requestFocus();
        }
        else if(cnfrmNewPass.isEmpty()){
            confirmNewPassword.setError("Please confirm your password");
            confirmNewPassword.requestFocus();
        }

        else if(!(cnfrmNewPass.equals(newPass))){
            confirmNewPassword.setError("Password does not match");
            confirmNewPassword.requestFocus();
        }

        else{
            progressBar.setVisibility(View.VISIBLE);
            user.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ChangePassword.this,"Password changed successfully!",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChangePassword.this,"Error! "+e.getMessage(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });

        }

    }
}
