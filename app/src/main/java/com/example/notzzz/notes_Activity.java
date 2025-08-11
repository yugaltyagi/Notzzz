package com.example.notzzz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class notes_Activity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private FloatingActionButton mCreateNoteFab;
    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> noteAdapter;
    private ActivityResultLauncher<Intent> noteResultLauncher;
    private Map<String, Integer> noteColorCache;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // Check if user is logged in
        if (firebaseUser == null) {
            Toast.makeText(this, "User not logged in. Redirecting to login.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Initialize color cache
        noteColorCache = new HashMap<>();

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Notes");
        }

        // Initialize views
        mCreateNoteFab = findViewById(R.id.createnotefab);
        mRecyclerView = findViewById(R.id.recyclerview);

        // Setup ActivityResultLauncher
        noteResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Delay adapter refresh to ensure layout stability
                        if (noteAdapter != null) {
                            handler.postDelayed(() -> noteAdapter.notifyDataSetChanged(), 100);
                        }
                    }
                });

        // Setup create note button
        mCreateNoteFab.setOnClickListener(v -> {
            Intent intent = new Intent(notes_Activity.this, CreateNote.class);
            noteResultLauncher.launch(intent);
        });

        // Setup RecyclerView
        Query query = firebaseFirestore
                .collection("notes")
                .document(firebaseUser.getUid())
                .collection("myNotes")
                .orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<firebasemodel> options = new FirestoreRecyclerOptions.Builder<firebasemodel>()
                .setQuery(query, firebasemodel.class)
                .setLifecycleOwner(this)
                .build();

        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebasemodel model) {
                String docId = getSnapshots().getSnapshot(position).getId();
                holder.noteTitle.setText(model.getTitle() != null ? model.getTitle() : "");
                holder.noteContent.setText(model.getContent() != null ? model.getContent() : "");

                // Set note background color
                if (holder.mNote != null) {
                    int color = noteColorCache.computeIfAbsent(docId, k -> getRandomColor());
                    holder.mNote.setBackgroundColor(holder.itemView.getResources().getColor(color, null));
                }

                // Navigate to note details
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(notes_Activity.this, notedetails.class);
                    intent.putExtra("title", model.getTitle());
                    intent.putExtra("content", model.getContent());
                    intent.putExtra("noteId", docId);
                    startActivity(intent);
                });

                // Setup popup menu
                ImageView popupButton = holder.itemView.findViewById(R.id.menupopupbutton);
                if (popupButton != null) {
                    popupButton.setOnClickListener(v -> {
                        PopupMenu popupMenu = new PopupMenu(notes_Activity.this, popupButton);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(item -> {
                            Intent intent = new Intent(notes_Activity.this, editnoteactivity.class);
                            intent.putExtra("title", model.getTitle());
                            intent.putExtra("content", model.getContent());
                            intent.putExtra("noteId", docId);
                            noteResultLauncher.launch(intent);
                            return true;
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(item -> {
                            firebaseFirestore.collection("notes")
                                    .document(firebaseUser.getUid())
                                    .collection("myNotes")
                                    .document(docId)
                                    .delete()
                                    .addOnSuccessListener(unused -> Toast.makeText(notes_Activity.this, "Note deleted", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(notes_Activity.this, "Failed to delete note: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            return true;
                        });
                        popupMenu.show();
                    });
                }
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notzzz_layout, parent, false);
                return new NoteViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                // Delay refresh to avoid layout conflicts
                handler.postDelayed(() -> notifyDataSetChanged(), 100);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Toast.makeText(notes_Activity.this, "Error loading notes: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(noteAdapter);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteContent;
        LinearLayout mNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.notetitle);
            noteContent = itemView.findViewById(R.id.notecontent);
            mNote = itemView.findViewById(R.id.note);
        }
    }

    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.light_coral);
        colorCode.add(R.color.pastel_pink);
        colorCode.add(R.color.sunset_orange);
        colorCode.add(R.color.sky_blue);
        colorCode.add(R.color.mint_green);
        colorCode.add(R.color.lavender);
        colorCode.add(R.color.beige);
        colorCode.add(R.color.amethyst);
        colorCode.add(R.color.peach);
        colorCode.add(R.color.baby_blue);
        colorCode.add(R.color.hot_pink);
        colorCode.add(R.color.light_yellow);
        colorCode.add(R.color.turquoise);
        colorCode.add(R.color.azure);
        colorCode.add(R.color.deep_pink);
        colorCode.add(R.color.charcoal_gray);
        colorCode.add(R.color.navy_blue);
        colorCode.add(R.color.teal);
        colorCode.add(R.color.snow);
        colorCode.add(R.color.crimson);
        colorCode.add(R.color.violet);
        colorCode.add(R.color.indigo);

        Random random = new Random();
        return colorCode.isEmpty() ? R.color.navy_blue : colorCode.get(random.nextInt(colorCode.size()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Logout) {
            firebaseAuth.signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure adapter is refreshed after navigation
        if (noteAdapter != null) {
            handler.postDelayed(() -> noteAdapter.notifyDataSetChanged(), 100);
        }
    }
}