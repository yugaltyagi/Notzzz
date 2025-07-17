//package com.example.notzzz;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Toast;
//
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.widget.Toolbar;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class editnoteactivity extends AppCompatActivity {
//
//    Intent data;
//    EditText medittitleofnote, meditcontentofnote;
//    FloatingActionButton msaveeditnote;
//
//    FirebaseAuth firebaseauth;
//    FirebaseFirestore firebasefirestore;
//    FirebaseUser firebaseuser;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//
//        meditcontentofnote = findViewById(R.id.editcontentofnote);
//        medittitleofnote = findViewById(R.id.edittitleofnote);
//        msaveeditnote = findViewById(R.id.saveeditnote);
//
//
//        data = getIntent();
//
//        firebasefirestore  =FirebaseFirestore.getInstance();
//        firebaseuser = firebaseauth.getCurrentUser();
//
//        Toolbar toolbar = findViewById(R.id.toolbarofeditnote);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        msaveeditnote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(editnoteactivity.this, "Save Button is clicked", Toast.LENGTH_SHORT).show();
//
//                String newtitle = medittitleofnote.getText().toString();
//                String newcontent = meditcontentofnote.getText().toString();
//
//                if (newtitle.isEmpty() || newcontent.isEmpty()){
//                    Toast.makeText(editnoteactivity.this, "Something is empty", Toast.LENGTH_SHORT).show();
//                    return;
//                }else {
//
//                    DocumentReference documentReference = firebasefirestore.collection("notes").document(firebaseuser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
//                    Map<String, Object> note = new HashMap<>();
//                    note.put("title", newtitle);
//                    note.put("content", newcontent);
//                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(editnoteactivity.this, "Note is updated", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(editnoteactivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//                    });
//                }
//            }
//        });
//
//        String notetitle = data.getStringExtra("title");
//        String notecontent = data.getStringExtra("content");
//        meditcontentofnote.setText(notecontent);
//        medittitleofnote.setText(notetitle);
//
//    }
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == android.R.id.home)
//        {
//            onBackPressed();
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
import android.widget.EditText;
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

public class editnoteactivity extends AppCompatActivity {

    private EditText editTitle, editContent;
    private FloatingActionButton saveButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;

    private String noteId, title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnoteactivity); // Ensure this XML layout exists

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Views
        editTitle = findViewById(R.id.edittitleofnote);
        editContent = findViewById(R.id.editcontentofnote);
        saveButton = findViewById(R.id.saveeditnote);
        Toolbar toolbar = findViewById(R.id.toolbarofeditnote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get Intent Data
        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            content = intent.getStringExtra("content");
            noteId = intent.getStringExtra("noteId");
        }

        if (noteId == null || title == null || content == null) {
            Toast.makeText(this, "Invalid note data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Populate existing content
        editTitle.setText(title);
        editContent.setText(content);

        // Save Button Logic
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = editTitle.getText().toString().trim();
                String newContent = editContent.getText().toString().trim();

                if (newTitle.isEmpty() || newContent.isEmpty()) {
                    Toast.makeText(editnoteactivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                DocumentReference docRef = firestore
                        .collection("notes")
                        .document(firebaseUser.getUid())
                        .collection("myNotes")
                        .document(noteId);

                Map<String, Object> note = new HashMap<>();
                note.put("title", newTitle);
                note.put("content", newContent);

                docRef.set(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(editnoteactivity.this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                                finish(); // Close activity
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(editnoteactivity.this, "Failed to update note", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    // Handle back button in the toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Safer than onBackPressed()
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
