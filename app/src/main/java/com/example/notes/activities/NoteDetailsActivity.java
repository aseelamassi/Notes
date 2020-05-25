package com.example.notes.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class NoteDetailsActivity extends AppCompatActivity {

    private EditText et_title, et_note_content;
    private String  email ;
    private Button btnUpdate;
    private TextView title ;
    private SharedPreferences sharedPreferences ;
    private FirebaseFirestore db ;

    ImageView iv_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        findViews();

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_edit.setVisibility(View.GONE);
                title.setText("Update your note");
                et_title.setEnabled(true);
                et_note_content.setEnabled(true);
                btnUpdate.setVisibility(View.VISIBLE);

            }
        });


        if(getIntent() != null){
            setUpViews(getIntent().getStringExtra("title"), getIntent().getStringExtra("content"));
        }


        sharedPreferences = getSharedPreferences("user info", MODE_PRIVATE);
        email = sharedPreferences.getString("email", null);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
            }
        });
    }

    private void findViews() {
        title = findViewById(R.id.tv_title);
        et_note_content = findViewById(R.id.et_note_content);
        et_title = findViewById(R.id.et_title);
        iv_edit = findViewById(R.id.iv_edit);
        btnUpdate = findViewById(R.id.btn_update);
    }

    private void setUpViews(String title, String content){
        et_title.setText(title);
        et_note_content.setText(content);


    }

    private void updateNote(){

        if (et_title.getText().toString().equals("") && et_note_content.getText().toString().equals(""))
            Toast.makeText(this, "field can't be empty", Toast.LENGTH_SHORT).show();

        else {
            HashMap<String, Object> note = new HashMap<>();
            note.put("title" , et_title.getText().toString());
            note.put("content" , et_note_content.getText().toString());

            db.collection("Users").document(email).collection("Notes").document(getIntent().getStringExtra("id")).
                    update(note)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(NoteDetailsActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(NoteDetailsActivity.this, MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NoteDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
