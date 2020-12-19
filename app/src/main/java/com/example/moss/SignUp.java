package com.example.moss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText name, emailId, password, confrimPass;
    private TextView signInText;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private FirebaseFirestore fStore;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //if already have an account link is selected
        signInText = findViewById(R.id.signIn_text);
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(SignUp.this, SignIn.class);
                startActivity(intent1);
            }
        });

        name = findViewById(R.id.user_name);
        emailId = findViewById(R.id.user_email_signUp);
        password = findViewById(R.id.user_password_signUp);
        confrimPass = findViewById(R.id.user_confirm_password);
        signUpButton = findViewById(R.id.signUp_button);
        progressBar = findViewById(R.id.progress_signUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMasterMain();
            }
        });
    }

    public void openMasterMain(){
        final String userName = name.getText().toString();
        final String email = emailId.getText().toString();
        String pwd = password.getText().toString();
        String cpwd = confrimPass.getText().toString();

        if(userName.isEmpty() &&email.isEmpty() && pwd.isEmpty() && cpwd.isEmpty()){
            Toast.makeText(SignUp.this,"Please fill the credentials!",Toast.LENGTH_SHORT).show();
            name.setError("Please enter name");
            emailId.setError("Please enter email id");
            password.setError("Please enter your password");
            confrimPass.setError("Please confirm your password");

        }

        else if(userName.isEmpty()){
            name.setError("Please enter name");
            name.requestFocus();
        }

        else if(email.isEmpty()){
            emailId.setError("Please enter email id");
            emailId.requestFocus();
        }
        else  if(pwd.isEmpty()){
            password.setError("Please enter your password");
            password.requestFocus();
        }

        else if(pwd.length() < 6){
            password.setError("Password must be greater than or equal to 6 characters");
            password.requestFocus();
        }
        else  if(cpwd.isEmpty()){
            confrimPass.setError("Please confirm your password");
            confrimPass.requestFocus();
        }

        else if(!(cpwd.equals(pwd))){
            confrimPass.setError("Password does not match");
            confrimPass.requestFocus();
        }

        else{
            progressBar.setVisibility(View.VISIBLE);
            // register the user
            mAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String,Object> user = new HashMap<>();
                        user.put("Name",userName);
                        user.put("Email",email);
                        //user.put("Password",pwd);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignUp.this,"Account created successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(SignUp.this, Login.class);
                                startActivity(intent1);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUp.this, "Authentication failed. " +e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        // If sign in fails, display a message to the user.

                        try
                        {
                            throw task.getException();
                        }
                        catch (FirebaseAuthUserCollisionException existEmail) //if already registered email is entered
                        {
                            Toast.makeText(SignUp.this, "Authentication failed. Account with this email already exists.",
                                    Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }
                        catch (Exception e)
                        {
                            Toast.makeText(SignUp.this, "Authentication failed. " +task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                }
            });

        }

    }
}
