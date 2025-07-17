//package com.example.notzzz;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//public class notedetails extends AppCompatActivity {
//
//    private TextView mTitle, mContent;
//    private FloatingActionButton goToEditNote;
//
//    private String title, content, noteId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notedetails);
//
//        // Initialize Views
//        mTitle = findViewById(R.id.titelofnotedetail);
//        mContent = findViewById(R.id.contentofnotedetail);
//        goToEditNote = findViewById(R.id.gotoeditnote);
//
//        // Setup Toolbar
//        Toolbar toolbar = findViewById(R.id.toolbarofnotedetails);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        // Retrieve Intent Data Safely
//        Intent data = getIntent();
//        if (data != null) {
//            title = data.getStringExtra("title");
//            content = data.getStringExtra("content");
//            noteId = data.getStringExtra("noteId");
//        }
//
//        // Null Safety Checks
//        if (title == null || content == null || noteId == null) {
//            finish(); // Close activity if data is invalid
//            return;
//        }
//
//        // Set note content
//        mTitle.setText(title);
//        mContent.setText(content);
//
//        // Set Edit button action
//        goToEditNote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent editIntent = new Intent(notedetails.this, editnoteactivity.class);
//                editIntent.putExtra("title", title);
//                editIntent.putExtra("content", content);
//                editIntent.putExtra("noteId", noteId);
//                startActivity(editIntent);
//            }
//        });
//    }
//
//    // Handle back button in the toolbar
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            getOnBackPressedDispatcher().onBackPressed();
//            // Safer than onBackPressed()
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}



package com.example.notzzz;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class notedetails extends AppCompatActivity {

    private TextView mTitle, mContent;
    private FloatingActionButton goToEditNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbarofnotedetails);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Note Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        mTitle = findViewById(R.id.titelofnotedetail);
        mContent = findViewById(R.id.contentofnotedetail);
        goToEditNote = findViewById(R.id.gotoeditnote);

        // Get intent data
        Intent data = getIntent();
        if (data == null || !data.hasExtra("title") || !data.hasExtra("content") || !data.hasExtra("noteId")) {
            finish(); // Invalid intent
            return;
        }

        String title = data.getStringExtra("title");
        String content = data.getStringExtra("content");
        String noteId = data.getStringExtra("noteId");

        // Set text
        mTitle.setText(title);
        mContent.setText(content);

        // Go to edit note
        goToEditNote.setOnClickListener(view -> {
            Intent editIntent = new Intent(notedetails.this, editnoteactivity.class);
            editIntent.putExtra("title", title);
            editIntent.putExtra("content", content);
            editIntent.putExtra("noteId", noteId);
            startActivity(editIntent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle back arrow click here
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed(); // Go back safely
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
