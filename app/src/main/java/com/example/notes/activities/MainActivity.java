package com.example.notes.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.notes.R;
import com.example.notes.adapter.NoteAdapter;
import com.example.notes.model.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv_notes;
    private LinearLayoutManager linearLayoutManager ;
    private NoteAdapter adapter ;
    private ArrayList<Note> notes = new ArrayList<>();
    private FirebaseFirestore db ;
    private String email;
    private SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("user info", MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();
        email = sharedPreferences.getString("email", null);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , AddNoteActivity.class);
                startActivity(intent);
            }
        });
        findViews();
        getNotes();
    }


    private void findViews(){
        rv_notes = findViewById(R.id.rv_notes);
        linearLayoutManager =new LinearLayoutManager(this);
        rv_notes.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getNotes();
    }

    private void getNotes(){
        db.collection("Users").document(email).collection("Notes")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    notes.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String title = (String) documentSnapshot.getData().get("title");
                        String content = (String) documentSnapshot.getData().get("content");
                        String id = (String) documentSnapshot.getId();
                        Note note = new Note(title, content);
                        note.setId(id);
                        notes.add(note);
                        adapter = new NoteAdapter(MainActivity.this, notes,email);
                        rv_notes.setAdapter(adapter);
                    }
                }
            }
        });
    }
}
