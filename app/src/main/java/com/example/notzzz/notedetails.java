package com.example.notzzz;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class notedetails extends AppCompatActivity {

    private TextView mTitle, mContent;
    private FloatingActionButton goToEditNote;
    private String title, content, noteId;

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

        // Restore state or get intent data
        if (savedInstanceState != null) {
            title = savedInstanceState.getString("title");
            content = savedInstanceState.getString("content");
            noteId = savedInstanceState.getString("noteId");
        } else {
            Intent data = getIntent();
            if (data == null || !data.hasExtra("title") || !data.hasExtra("content") || !data.hasExtra("noteId")) {
                finish(); // Close activity if intent data is invalid
                return;
            }
            title = data.getStringExtra("title");
            content = data.getStringExtra("content");
            noteId = data.getStringExtra("noteId");
        }

        // Set text
        mTitle.setText(title != null ? title : "");
        mContent.setText(content != null ? content : "");

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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", title);
        outState.putString("content", content);
        outState.putString("noteId", noteId);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}