package com.example.notes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.activities.NoteDetailsActivity;
import com.example.notes.model.Note;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context ;
    private ArrayList<Note> notes ;
    private String email, originTitle;
    FirebaseFirestore db;

    public NoteAdapter(Context context, ArrayList<Note> notes , String email) {
        this.context = context;
        this.notes = notes;
        this.email = email;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_note_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        FirebaseApp.initializeApp(context);
        db = FirebaseFirestore.getInstance();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        originTitle = notes.get(position).getTitle();
        holder.title.setText(notes.get(position).getTitle());
        holder.content.setText(notes.get(position).getContent());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NoteDetailsActivity.class);
                intent.putExtra("id" , notes.get(position).getId());
                intent.putExtra("title", notes.get(position).getTitle());
                intent.putExtra("content", notes.get(position).getContent());
                context.startActivity(intent);
            }
        });
        holder.ic_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                db.collection("Users").document(email).collection("Notes").
                        document(notes.get(position).getId()).delete();

                notes.remove(position);
                NoteAdapter.this.notifyDataSetChanged();

            }
        });



    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View view ;
        TextView title , content;
        ImageView  ic_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            title=view.findViewById(R.id.tv_title);
            content = view.findViewById(R.id.tv_content);
            ic_delete = view.findViewById(R.id.iv_delete);
        }
    }






}
