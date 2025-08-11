package com.example.notzzz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editnoteactivity extends AppCompatActivity {

    private EditText editTitle, editContent;
    private FloatingActionButton saveButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    private String noteId;
    private String title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editnoteactivity);
        Log.d("editnoteactivity", "onCreate called");

        // Initialize Firebase with error handling
        try {
            firebaseAuth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
        } catch (Exception e) {
            Log.e("editnoteactivity", "Firebase initialization failed: " + e.getMessage());
            Toast.makeText(this, "Firebase error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Check if user is logged in
        if (firebaseUser == null) {
            Log.e("editnoteactivity", "User not logged in");
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        editTitle = findViewById(R.id.edittitleofnote);
        editContent = findViewById(R.id.editcontentofnote);
        saveButton = findViewById(R.id.saveeditnote);
        Toolbar toolbar = findViewById(R.id.toolbarofeditnote);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Note");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Restore state or get intent data
        if (savedInstanceState != null) {
            title = savedInstanceState.getString("title");
            content = savedInstanceState.getString("content");
            noteId = savedInstanceState.getString("noteId");
            Log.d("editnoteactivity", "Restored state: noteId=" + noteId);
        } else {
            Intent intent = getIntent();
            if (intent == null || !intent.hasExtra("title") || !intent.hasExtra("content") || !intent.hasExtra("noteId")) {
                Log.e("editnoteactivity", "Invalid intent data");
                Toast.makeText(this, "Invalid note data", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            title = intent.getStringExtra("title");
            content = intent.getStringExtra("content");
            noteId = intent.getStringExtra("noteId");
            Log.d("editnoteactivity", "Received intent: noteId=" + noteId);
        }

        // Populate fields
        editTitle.setText(title != null ? title : "");
        editContent.setText(content != null ? content : "");

        // Save button logic
        saveButton.setOnClickListener(v -> {
            String newTitle = editTitle.getText().toString().trim();
            String newContent = editContent.getText().toString().trim();

            if (newTitle.isEmpty() || newContent.isEmpty()) {
                Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> note = new HashMap<>();
            note.put("title", newTitle);
            note.put("content", newContent);

            firestore.collection("notes")
                    .document(firebaseUser.getUid())
                    .collection("myNotes")
                    .document(noteId)
                    .set(note)
                    .addOnSuccessListener(unused -> {
                        Log.d("editnoteactivity", "Note updated: noteId=" + noteId);
                        Toast.makeText(editnoteactivity.this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("title", newTitle);
                        resultIntent.putExtra("content", newContent);
                        resultIntent.putExtra("noteId", noteId);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("editnoteactivity", "Failed to update note: " + e.getMessage());
                        Toast.makeText(editnoteactivity.this, "Failed to update note", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("editnoteactivity", "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("editnoteactivity", "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("editnoteactivity", "onDestroy called");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", title);
        outState.putString("content", content);
        outState.putString("noteId", noteId);
        Log.d("editnoteactivity", "onSaveInstanceState called");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.d("editnoteactivity", "Toolbar back button pressed");
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d("editnoteactivity", "System back button pressed");
        // Return result even if no changes are saved
        Intent resultIntent = new Intent();
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("content", content);
        resultIntent.putExtra("noteId", noteId);
        setResult(RESULT_CANCELED, resultIntent);
        super.onBackPressed();
    }
}