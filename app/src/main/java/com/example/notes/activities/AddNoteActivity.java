package com.example.notes.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.model.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddNoteActivity extends AppCompatActivity {

    private EditText et_title, et_note_content ;
    private String title , noteContent, email ;
    private SharedPreferences sharedPreferences ;
    private FirebaseFirestore db ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Button btn_sve =  toolbar.findViewById(R.id.btn_save);
        btn_sve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote();

            }
        });

        findViews();

        sharedPreferences = getSharedPreferences("user info", MODE_PRIVATE);
        email = sharedPreferences.getString("email", null);
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
    }

    private void findViews() {
        et_note_content = findViewById(R.id.et_note_content);
        et_title = findViewById(R.id.et_title);
    }



    private void addNote(){
        title = et_title.getText().toString();
        noteContent = et_note_content.getText().toString();
        if (!noteContent.isEmpty()){
            HashMap<String , Object> note = new HashMap<>();
            note.put("title", title);
            note.put("content" , noteContent);
            db.collection("Users").document(email).collection("Notes").document().set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(AddNoteActivity.this, "from complete", Toast.LENGTH_SHORT).show();
                    if (task.isSuccessful())
                        Toast.makeText(AddNoteActivity.this, "Successfully Add note", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNoteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else
            Toast.makeText(this, "note can't be empty", Toast.LENGTH_SHORT).show();



    }
}
