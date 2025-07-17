//package com.example.notzzz;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class Forgotpassword extends AppCompatActivity {
//    private EditText mforgotpassword;
//    private Button mPasswordreset;
//    private TextView mgobacktologin;
//
//    FirebaseAuth firebaseAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_forgotpassword);
//
//        mforgotpassword =findViewById(R.id.forgotpassword);
//        mPasswordreset = findViewById(R.id.Passwordreset);
//        mgobacktologin = findViewById(R.id.gobacktologin);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        mgobacktologin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Forgotpassword.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
//        mPasswordreset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               String mail = mforgotpassword.getText().toString();
//               if (mail.isEmpty()){
//                   Toast.makeText(getApplicationContext(), "Enter your mail first.", Toast.LENGTH_SHORT).show();
//               }
//               else {
//                   //password recover
//                   firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
//                       @Override
//                       public void onComplete(@NonNull Task<Void> task) {
//                           if (task.isSuccessful()){
//                               Toast.makeText(getApplicationContext(), "Check your mail.", Toast.LENGTH_SHORT).show();
//                               finish();
//                               startActivity(new Intent(Forgotpassword.this, MainActivity.class));
//                           }
//                           else {
//                               Toast.makeText(getApplicationContext(), "Email is wrong or Account is not Exist!", Toast.LENGTH_SHORT).show();
//                           }
//                       }
//                   });
//               }
//            }
//        });
//
//    }
//}

package com.example.notzzz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgotpassword extends AppCompatActivity {

    private EditText mforgotpassword;
    private Button mPasswordreset;
    private TextView mgobacktologin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgotpassword);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // prevent crash if null
        }

        // ─── View Binding ───────────────────────────────────────
        mforgotpassword  = findViewById(R.id.forgotpassword);
        mPasswordreset   = findViewById(R.id.Passwordreset);
        mgobacktologin   = findViewById(R.id.gobacktologin);

        firebaseAuth = FirebaseAuth.getInstance();

        // ─── Go back to login ───────────────────────────────────
        mgobacktologin.setOnClickListener(v ->
                startActivity(new Intent(Forgotpassword.this, MainActivity.class)));

        // ─── Reset password logic ───────────────────────────────
        mPasswordreset.setOnClickListener(v -> {
            String email = mforgotpassword.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Enter your email first.", Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Check your email to reset password.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Forgotpassword.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Invalid email or account doesn't exist!", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
