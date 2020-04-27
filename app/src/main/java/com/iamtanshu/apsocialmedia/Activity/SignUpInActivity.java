package com.iamtanshu.apsocialmedia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamtanshu.apsocialmedia.R;

public class SignUpInActivity extends AppCompatActivity {
    private EditText etEmail, etUserName, etPassword;
    private Button btnSignUp, btnSignIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigup_in);

        FirebaseApp.initializeApp(this);

        etEmail = findViewById(R.id.et_email);
        etUserName = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        btnSignUp = findViewById(R.id.btn_SignUp);
        btnSignIn = findViewById(R.id.btn_SignIn);


        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (email.equals("") || password.equals("")) {
                    Toast.makeText(SignUpInActivity.this, "Invaild Email id or Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                signup(email, password);
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (email.equals("") || password.equals("")) {
                    Toast.makeText(SignUpInActivity.this, "Invaild Email id or Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                signIn(email, password);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //Transition to next Activity
            transitionActivity();

        }
    }

    private void signup(String email, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Signing Up");
        dialog.setCancelable(false);
        dialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpInActivity.this, "Signing Up Successful", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().
                            child("my_user").
                            child(task.getResult().getUser().getUid()).
                            child("username").
                            setValue(etUserName.getText().toString().trim());
                    transitionActivity();

                } else {
                    Toast.makeText(SignUpInActivity.this, "Signing Up fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signIn(String email, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Signing In");
        dialog.setCancelable(false);
        dialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpInActivity.this, "User Successfully Signed In", Toast.LENGTH_SHORT).show();
                    transitionActivity();
                } else {
                    Toast.makeText(SignUpInActivity.this, "User fails Signed In", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void transitionActivity() {
        Intent intent = new Intent(SignUpInActivity.this, SocialMediaActivity.class);
        startActivity(intent);
    }
}
