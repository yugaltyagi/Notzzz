package com.example.notzzz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mLoginEmail, mLoginPassword;
    private Button mLoginButton, mSignupButton;
    private TextView mForgotPassword;
    private ProgressBar mProgressBar;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Hide the action bar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize UI components
        mProgressBar = findViewById(R.id.progreebarofmainactivity);
        mLoginEmail = findViewById(R.id.loginemail);
        mLoginPassword = findViewById(R.id.loginpassword);
        mLoginButton = findViewById(R.id.login_button);
        mSignupButton = findViewById(R.id.signup_button);
        mForgotPassword = findViewById(R.id.forgotpassword);

        mFirebaseAuth = FirebaseAuth.getInstance();

        // Redirect if already logged in and email is verified
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            startActivity(new Intent(MainActivity.this, notes_Activity.class));
            finish();
        }

        // Set up click listeners
        mSignupButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Signup.class)));

        mForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Forgotpassword.class)));

        mLoginButton.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = mLoginEmail.getText().toString().trim();
        String password = mLoginPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressBar.setVisibility(ProgressBar.VISIBLE);

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    if (task.isSuccessful()) {
                        checkEmailVerification();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid credentials or account doesn't exist.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkEmailVerification() {
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
            Toast.makeText(this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, notes_Activity.class));
            finish();
        } else {
            Toast.makeText(this, "Please verify your email first.", Toast.LENGTH_SHORT).show();
            mFirebaseAuth.signOut();
        }
    }
}