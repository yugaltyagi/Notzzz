package com.example.notzzz;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {

    private EditText mSignupemail, msignuppassword;
    private Button mSignUp;          // kept the m‑prefix style
    private TextView mgotologin;

    private FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mSignupemail    = findViewById(R.id.Signupemail);
        msignuppassword = findViewById(R.id.signuppassword);
        mSignUp         = findViewById(R.id.Signup_button);
        mgotologin      = findViewById(R.id.gotologin);

        firebaseauth = FirebaseAuth.getInstance();

        mgotologin.setOnClickListener(v ->
                startActivity(new Intent(Signup.this, MainActivity.class)));

        mSignUp.setOnClickListener(v -> attemptSignUp());
    }

    private void attemptSignUp() {
        String email    = mSignupemail.getText().toString().trim();
        String password = msignuppassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 7) {
            Toast.makeText(this, "Password should be at least 7 characters.", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseauth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "User registered successfully.", Toast.LENGTH_SHORT).show();
                        sendEmailVerification();
                    } else {
                        Toast.makeText(this,
                                "Failed to register: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendEmailVerification() {
        FirebaseUser user = firebaseauth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
            return;
        }

        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                "Verification email sent. Please verify and login again.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this,
                                "Email verification failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                    firebaseauth.signOut();
                    startActivity(new Intent(Signup.this, MainActivity.class));
                    finish();
                });
    }
}
