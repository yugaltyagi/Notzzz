//package com.example.notzzz;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupMenu;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.StaggeredGridLayoutManager;
//
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.Query;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class notes_Activity extends AppCompatActivity {
//
//    private FirebaseAuth firebaseAuth;
//    private FirebaseUser firebaseUser;
//    private FirebaseFirestore firebaseFirestore;
//
//    private FloatingActionButton mCreateNoteFab;
//    private RecyclerView mRecyclerView;
//    private FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> noteAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notes);
//
//        // Initialize Firebase
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//        firebaseFirestore = FirebaseFirestore.getInstance();
//
//        // Setup Toolbar
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle("My Notes");
//        }
//
//        // Setup FloatingActionButton
//        mCreateNoteFab = findViewById(R.id.createnotefab);
//        mCreateNoteFab.setOnClickListener(v ->
//                startActivity(new Intent(notes_Activity.this, CreateNote.class))
//        );
//
//        // Firestore query
//        Query query = firebaseFirestore
//                .collection("notes")
//                .document(firebaseUser.getUid())
//                .collection("myNotes")
//                .orderBy("title", Query.Direction.ASCENDING);
//
//        FirestoreRecyclerOptions<firebasemodel> options = new FirestoreRecyclerOptions.Builder<firebasemodel>()
//                .setQuery(query, firebasemodel.class)
//                .build();
//
//        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(options) {
//
//
//            @Override
//            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewholder, int i, @NonNull firebasemodel model) {
//
//                ImageView popupbutton = noteViewholder.itemView.findViewById(R.id.menupopupbutton);
//
//
//
//
//                int colourcode = getRandomColor();
//                noteViewholder.mNote.setBackgroundColor(noteViewholder.itemView.getResources().getColor(colourcode, null));
//                noteViewholder.noteTitle.setText(model.getTitle());
//                noteViewholder.noteContent.setText(model.getContent());
//
//                String docID = noteAdapter.getSnapshots().getSnapshot(i).getId();
//
//                noteViewholder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        // Handle item click here
//
//                        Intent intent = new Intent(view.getContext(), notedetails.class);
//
//                        intent.putExtra("title",firebasemodel.getTitle());
//                        intent.putExtra("content",firebasemodel.getContent());
//                        intent.putExtra("noteId",docID);
//                        view.getContext().startActivity(intent);
////                        Toast.makeText(getApplicationContext()," this is clicked",Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                popupbutton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        PopupMenu popupMneu = new PopupMenu(view.getContext(), view);
//                        popupMneu.setGravity(Gravity.END);
//                        popupMneu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
//                                Intent intent = new Intent(view.getContext(), editnoteactivity.class);
//                                intent.putExtra("title",firebasemodel.getTitle());
//                                intent.putExtra("content",firebasemodel.getContent());
//                                intent.putExtra("noteId",docID);
//                                view.getContext().startActivity(intent);
//                                return false;
//                            }
//                        });
//
//                        popupMneu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
//                                DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docID);
//                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Toast.makeText(getApplicationContext(),"Note is deleted",Toast.LENGTH_SHORT).show();
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(getApplicationContext(),"Fail to delete",Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//                                return false;
//                            }
//                        });
//
//                        popupMneu.show();
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notzzz_layout, parent, false);
//                return new NoteViewHolder(view);
//            }
//        };
//
//        // Setup RecyclerView
//        mRecyclerView = findViewById(R.id.recyclerview);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        mRecyclerView.setAdapter(noteAdapter);
//    }
//
//    // Options Menu for Logout
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    // Handle Logout MenuItem
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.Logout) {
//            firebaseAuth.signOut();
//            startActivity(new Intent(notes_Activity.this, MainActivity.class));
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (noteAdapter != null) {
//            noteAdapter.startListening();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (noteAdapter != null) {
//            noteAdapter.stopListening(); // fixed from startListening to stopListening
//        }
//    }
//
//    // ViewHolder class
//    private static class NoteViewHolder extends RecyclerView.ViewHolder {
//        private final TextView noteTitle;
//        private final TextView noteContent;
//        private final LinearLayout mNote;
//
//        public NoteViewHolder(@NonNull View itemView) {
//            super(itemView);
//            noteTitle = itemView.findViewById(R.id.notetitle);
//            noteContent = itemView.findViewById(R.id.notecontent);
//            mNote = itemView.findViewById(R.id.note);
//        }
//    }
//
//    private int getRandomColor(){
//        List<Integer> colorCode =  new ArrayList<>();
//        colorCode.add(R.color.light_coral);
//        colorCode.add(R.color.pastel_pink);
//        colorCode.add(R.color.sunset_orange);
//        colorCode.add(R.color.sky_blue);
//        colorCode.add(R.color.mint_green);
//        colorCode.add(R.color.lavender);
//        colorCode.add(R.color.beige);
//        colorCode.add(R.color.amethyst);
//        colorCode.add(R.color.peach);
//        colorCode.add(R.color.baby_blue);
//        colorCode.add(R.color.hot_pink);
//        colorCode.add(R.color.light_yellow);
//        colorCode.add(R.color.turquoise);
//        colorCode.add(R.color.azure);
//        colorCode.add(R.color.deep_pink);
//        colorCode.add(R.color.charcoal_gray);
//        colorCode.add(R.color.navy_blue);
//        colorCode.add(R.color.teal);
//        colorCode.add(R.color.snow);
//        colorCode.add(R.color.crimson);
//        colorCode.add(R.color.violet);
//        colorCode.add(R.color.indigo);
//
//        Random random = new Random();
//        int number = random.nextInt(colorCode.size());
//        return colorCode.get(number);
//
//
//    }
//}




//package com.example.notzzz;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupMenu;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.StaggeredGridLayoutManager;
//
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.Query;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class notes_Activity extends AppCompatActivity {
//
//    private FirebaseAuth firebaseAuth;
//    private FirebaseUser firebaseUser;
//    private FirebaseFirestore firebaseFirestore;
//
//    private FloatingActionButton mCreateNoteFab;
//    private RecyclerView mRecyclerView;
//    private FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> noteAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notes);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//        firebaseFirestore = FirebaseFirestore.getInstance();
//
//        // Prevent crash: if user is not logged in
//        if (firebaseUser == null) {
//            Toast.makeText(this, "Please login again.", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(notes_Activity.this, MainActivity.class));
//            finish();
//            return;
//        }
//
//        // Toolbar setup
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle("My Notes");
//        }
//
//        // Floating Action Button
//        mCreateNoteFab = findViewById(R.id.createnotefab);
//        mCreateNoteFab.setOnClickListener(v -> {
//            startActivity(new Intent(notes_Activity.this, CreateNote.class));
//        });
//
//        // Firestore query
//        Query query = firebaseFirestore
//                .collection("notes")
//                .document(firebaseUser.getUid())
//                .collection("myNotes")
//                .orderBy("title", Query.Direction.ASCENDING);
//
//        FirestoreRecyclerOptions<firebasemodel> options = new FirestoreRecyclerOptions.Builder<firebasemodel>()
//                .setQuery(query, firebasemodel.class)
//                .build();
//
//        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebasemodel model) {
//
//                ImageView popupButton = holder.itemView.findViewById(R.id.menupopupbutton);
//                int colorCode = getRandomColor();
//                holder.mNote.setBackgroundColor(holder.itemView.getResources().getColor(colorCode, null));
//
//                holder.noteTitle.setText(model.getTitle());
//                holder.noteContent.setText(model.getContent());
//
//                String docId = getSnapshots().getSnapshot(position).getId();
//
//                // Note click to open details
//                holder.itemView.setOnClickListener(view -> {
//                    Intent intent = new Intent(view.getContext(), notedetails.class);
//                    intent.putExtra("title", model.getTitle());
//                    intent.putExtra("content", model.getContent());
//                    intent.putExtra("noteId", docId);
//                    view.getContext().startActivity(intent);
//                });
//
//                // Show popup menu
//                popupButton.setOnClickListener(view -> {
//                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
//                    popupMenu.setGravity(Gravity.END);
//
//                    popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(menuItem -> {
//                        Intent intent = new Intent(view.getContext(), editnoteactivity.class);
//                        intent.putExtra("title", model.getTitle());
//                        intent.putExtra("content", model.getContent());
//                        intent.putExtra("noteId", docId);
//                        view.getContext().startActivity(intent);
//                        return true;
//                    });
//
//                    popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(menuItem -> {
//                        DocumentReference docRef = firebaseFirestore
//                                .collection("notes")
//                                .document(firebaseUser.getUid())
//                                .collection("myNotes")
//                                .document(docId);
//
//                        docRef.delete()
//                                .addOnSuccessListener(unused -> Toast.makeText(view.getContext(), "Note deleted", Toast.LENGTH_SHORT).show())
//                                .addOnFailureListener(e -> Toast.makeText(view.getContext(), "Failed to delete", Toast.LENGTH_SHORT).show());
//                        return true;
//                    });
//
//                    popupMenu.show();
//                });
//            }
//
//            @NonNull
//            @Override
//            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notzzz_layout, parent, false);
//                return new NoteViewHolder(view);
//            }
//        };
//
//        // Setup RecyclerView
//        mRecyclerView = findViewById(R.id.recyclerview);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        mRecyclerView.setAdapter(noteAdapter);
//    }
//
//    // Menu (Logout)
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    // Logout click
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.Logout) {
//            firebaseAuth.signOut();
//            startActivity(new Intent(notes_Activity.this, MainActivity.class));
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (noteAdapter != null) {
//            noteAdapter.startListening();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (noteAdapter != null) {
//            noteAdapter.stopListening();
//        }
//    }
//
//    // ViewHolder class
//    private static class NoteViewHolder extends RecyclerView.ViewHolder {
//        TextView noteTitle, noteContent;
//        LinearLayout mNote;
//
//        public NoteViewHolder(@NonNull View itemView) {
//            super(itemView);
//            noteTitle = itemView.findViewById(R.id.notetitle);
//            noteContent = itemView.findViewById(R.id.notecontent);
//            mNote = itemView.findViewById(R.id.note);
//        }
//    }
//
//    // Random color
//    private int getRandomColor() {
//        List<Integer> colorCode = new ArrayList<>();
//        colorCode.add(R.color.light_coral);
//        colorCode.add(R.color.pastel_pink);
//        colorCode.add(R.color.sunset_orange);
//        colorCode.add(R.color.sky_blue);
//        colorCode.add(R.color.mint_green);
//        colorCode.add(R.color.lavender);
//        colorCode.add(R.color.beige);
//        colorCode.add(R.color.amethyst);
//        colorCode.add(R.color.peach);
//        colorCode.add(R.color.baby_blue);
//        colorCode.add(R.color.hot_pink);
//        colorCode.add(R.color.light_yellow);
//        colorCode.add(R.color.turquoise);
//        colorCode.add(R.color.azure);
//        colorCode.add(R.color.deep_pink);
//        colorCode.add(R.color.charcoal_gray);
//        colorCode.add(R.color.navy_blue);
//        colorCode.add(R.color.teal);
//        colorCode.add(R.color.snow);
//        colorCode.add(R.color.crimson);
//        colorCode.add(R.color.violet);
//        colorCode.add(R.color.indigo);
//
//        Random random = new Random();
//        int number = random.nextInt(colorCode.size());
//        return colorCode.get(number);
//    }
//}



package com.example.notzzz;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class notes_Activity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    private FloatingActionButton mCreateNoteFab;
    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Prevent crash if user is not logged in
        if (firebaseUser == null) {
            Toast.makeText(this, "User not logged in. Redirecting to login.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(notes_Activity.this, MainActivity.class)); // Assuming MainActivity is your login/signup
            finish();
            return; // Important to prevent further execution
        }

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Notes");
        }

        // Setup FloatingActionButton
        mCreateNoteFab = findViewById(R.id.createnotefab);
        mCreateNoteFab.setOnClickListener(v ->
                startActivity(new Intent(notes_Activity.this, CreateNote.class))
        );

        // Firestore query - Ensure "notes" and "myNotes" are correct collection names
        // Also, ensure user.getUid() is a valid document ID where notes are stored.
        Query query = firebaseFirestore
                .collection("notes")
                .document(firebaseUser.getUid())
                .collection("myNotes")
                .orderBy("title", Query.Direction.ASCENDING); // Or "timestamp" or another field for ordering

        FirestoreRecyclerOptions<firebasemodel> options = new FirestoreRecyclerOptions.Builder<firebasemodel>()
                .setQuery(query, firebasemodel.class)
                .build();

        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebasemodel model) {
                ImageView popupbutton = holder.itemView.findViewById(R.id.menupopupbutton);

                holder.noteTitle.setText(model.getTitle());
                holder.noteContent.setText(model.getContent());

                int colourcode = getRandomColor();
                // Ensure mNote is not null and refers to the correct LinearLayout in your item layout
                if (holder.mNote != null) {
                    holder.mNote.setBackgroundColor(holder.itemView.getResources().getColor(colourcode, null));
                }


                String docID = getSnapshots().getSnapshot(holder.getAdapterPosition()).getId();

                holder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(view.getContext(), notedetails.class);
                    intent.putExtra("title", model.getTitle());
                    intent.putExtra("content", model.getContent());
                    intent.putExtra("noteId", docID);
                    view.getContext().startActivity(intent);
                });

                if (popupbutton != null) {
                    popupbutton.setOnClickListener(view -> {
                        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(menuItem -> {
                            Intent intent = new Intent(view.getContext(), editnoteactivity.class);
                            intent.putExtra("title", model.getTitle());
                            intent.putExtra("content", model.getContent());
                            intent.putExtra("noteId", docID);
                            view.getContext().startActivity(intent);
                            return false;
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(menuItem -> {
                            // Consider adding a confirmation dialog before deleting
                            DocumentReference documentReference = firebaseFirestore
                                    .collection("notes")
                                    .document(firebaseUser.getUid())
                                    .collection("myNotes")
                                    .document(docID);

                            documentReference.delete()
                                    .addOnSuccessListener(unused -> Toast.makeText(getApplicationContext(), "Note deleted", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to delete note", Toast.LENGTH_SHORT).show());
                            return false;
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
        };

        // Setup RecyclerView
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true); // Improves performance if item sizes don't change
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(noteAdapter);
    }

    // ViewHolder class - defined as a static inner class
    // (or you can move it to its own .java file if preferred)
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle;
        TextView noteContent;
        LinearLayout mNote; // This is the LinearLayout for the note item background

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.notetitle);
            noteContent = itemView.findViewById(R.id.notecontent);
            mNote = itemView.findViewById(R.id.note); // Ensure R.id.note is the ID of your LinearLayout in notzzz_layout.xml
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
        if (colorCode.isEmpty()) {
            return R.color.navy_blue; // Add a default color in your colors.xml
        }
        int number = random.nextInt(colorCode.size());
        return colorCode.get(number);
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
            // Optional: Clear any other user-specific data if needed
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(notes_Activity.this, MainActivity.class));
            finishAffinity(); // Finishes this activity and all parent activities.
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (noteAdapter != null) {
            noteAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }
}
