package com.example.notzzz;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {

    ProgressBar mprogreebarofcreatenote;

    private EditText mTitleOfNote, mContentOfNote;
    private FloatingActionButton mSaveNote;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        mprogreebarofcreatenote = findViewById(R.id.progreebarofcreatenote);

        // Initialize Views
        mTitleOfNote = findViewById(R.id.titleofnote);
        mContentOfNote = findViewById(R.id.createcontentofnote);
        mSaveNote = findViewById(R.id.savenote);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Set up Toolbar with back button
        Toolbar toolbar = findViewById(R.id.toolbarofCreatenote);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Save note on button click
        mSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitleOfNote.getText().toString().trim();
                String content = mContentOfNote.getText().toString().trim();

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(CreateNote.this, "Both fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }else {

                    mprogreebarofcreatenote.setVisibility(View.VISIBLE);

                    // Create document reference and save data
                    DocumentReference documentReference = firebaseFirestore.collection("notes")
                            .document(firebaseUser.getUid())
                            .collection("myNotes")
                            .document();

                    Map<String, Object> note = new HashMap<>();
                    note.put("title", title);
                    note.put("content", content);

                    documentReference.set(note)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(CreateNote.this, "Note created successfully", Toast.LENGTH_SHORT).show();
                                    mprogreebarofcreatenote.setVisibility(View.INVISIBLE);
                                    startActivity(new Intent(CreateNote.this, notes_Activity.class));
                                    finish(); // Optional: remove this activity from the back stack
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CreateNote.this, "Failed to create note", Toast.LENGTH_SHORT).show();
                                    mprogreebarofcreatenote.setVisibility(View.INVISIBLE);

                                }
                            });
                }
            }
        });
    }


    // Handle toolbar back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
