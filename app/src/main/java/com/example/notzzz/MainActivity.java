package com.example.notzzz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mloginemail, mloginpassword;
    private Button mlogin_button, msignup_button;
    private TextView mforgotpassword;

    private FirebaseAuth firebaseauth;
    ProgressBar mprogreebarofmainactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mprogreebarofmainactivity = findViewById(R.id.progreebarofmainactivity);

        mloginemail     = findViewById(R.id.loginemail);
        mloginpassword  = findViewById(R.id.loginpassword);
        mlogin_button   = findViewById(R.id.login_button);
        msignup_button  = findViewById(R.id.signup_button);
        mforgotpassword = findViewById(R.id.forgotpassword);

        firebaseauth = FirebaseAuth.getInstance();



        // Redirect if already logged in and verified
        FirebaseUser firebaseUser = firebaseauth.getCurrentUser();
        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
            finish();
            startActivity(new Intent(MainActivity.this, notes_Activity.class));
        }

        msignup_button.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Signup.class)));

        mforgotpassword.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Forgotpassword.class)));

        mlogin_button.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = mloginemail.getText().toString().trim();
        String password = mloginpassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mprogreebarofmainactivity.setVisibility(ProgressBar.VISIBLE);


            firebaseauth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            checkMailVerification();
                        } else {
                            Toast.makeText(MainActivity.this, "Account doesn't exist or wrong credentials!", Toast.LENGTH_SHORT).show();
                            mprogreebarofmainactivity.setVisibility(ProgressBar.INVISIBLE);
                        }
                    });
        }
    }

    private void checkMailVerification() {
        FirebaseUser firebaseUser = firebaseauth.getCurrentUser();

        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
            Toast.makeText(this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, notes_Activity.class));
            finish();
        } else {
            mprogreebarofmainactivity.setVisibility(ProgressBar.INVISIBLE);
            Toast.makeText(this, "Please verify your email first.", Toast.LENGTH_SHORT).show();
            firebaseauth.signOut();
        }
    }
}
