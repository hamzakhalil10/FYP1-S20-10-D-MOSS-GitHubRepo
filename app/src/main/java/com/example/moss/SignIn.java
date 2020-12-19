package com.example.moss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class SignIn extends AppCompatActivity {

    private EditText emailId, password;
    private TextView signUpText;
    private Button signInButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private TextView forgotPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth =  FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Intent intent1 = new Intent(SignIn.this, Login.class);
            startActivity(intent1);
        }

        signUpText = findViewById(R.id.signUp_text);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(SignIn.this, SignUp.class);
                startActivity(intent1);
            }
        });

        emailId = findViewById(R.id.user_email);
        password = findViewById(R.id.user_password);
        progressBar = findViewById(R.id.progress_signIn);
        signInButton = findViewById(R.id.signIn_button);
        forgotPasswordLink = findViewById(R.id.forgotPassword);
        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setForgotPasswordLink(view);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMasterMain();
            }
        });
    }

    private void openMasterMain() {
        String email = emailId.getText().toString();
        String pwd = password.getText().toString();

        if(email.isEmpty() && pwd.isEmpty()){
            password.setError("Please enter email id");
            emailId.setError("Please enter your password");
            Toast.makeText(SignIn.this,"Please fill the credentials!",Toast.LENGTH_SHORT).show();
        }

        else if(email.isEmpty()){
            emailId.setError("Please enter email id");
            emailId.requestFocus();
        }
        else  if(pwd.isEmpty()){
            password.setError("Please enter your password");
            password.requestFocus();
        }

        else {
            progressBar.setVisibility(View.VISIBLE);

            //sign in
            mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(SignIn.this, "Login Successful!",
                                Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(SignIn.this, Login.class);
                        startActivity(intent1);
                        finish();

                    } else {
                        // If sign in fails, display a message to the user.
                        try
                        {
                            throw task.getException();
                        }
                        // if user enters wrong email.
                        catch (FirebaseAuthInvalidUserException invalidEmail)
                        {
                            emailId.setError("Invalid email");
                            emailId.requestFocus();
                            progressBar.setVisibility(View.GONE);

                        }
                        // if user enters wrong password.
                        catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                        {
                            password.setError("Incorrect password");
                            password.requestFocus();
                            progressBar.setVisibility(View.GONE);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(SignIn.this, "Authentication failed. " +task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }


                    }
                }
            });

        }
    }

    private void setForgotPasswordLink(View v){
        final EditText resetMail = new EditText(v.getContext());
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
        passwordResetDialog.setTitle("Reset Password?");
        passwordResetDialog.setMessage("Enter your email address to receive reset link");
        passwordResetDialog.setView(resetMail);

        passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //extract email and set reset link
                String mail = resetMail.getText().toString();
                mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignIn.this, "Reset link sent successfully!",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignIn.this, "Error! " +e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //close the dialog
            }
        });

        passwordResetDialog.create().show();
    }
}
