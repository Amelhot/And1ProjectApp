package com.example.smartnoteapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NotesAdapter extends FirestoreRecyclerAdapter<Note, NotesAdapter.NotesViewHolder> {


   Context context;
    public NotesAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull Note note) {
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timestampTextView.setText(Utility.convertTimestamp(note.timestamp));

        holder.itemView.setOnClickListener((v) ->{
            Intent intent = new Intent(context, NoteInfoActivity.class);
            intent.putExtra("title", note.title);
            intent.putExtra("content", note.content);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_notes, parent, false);
        return new NotesViewHolder(view);
    }

    class NotesViewHolder extends RecyclerView.ViewHolder{

    TextView titleTextView, contentTextView,timestampTextView;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.noteTitleTextView);
        contentTextView = itemView.findViewById(R.id.noteContentTextView);
        timestampTextView = itemView.findViewById(R.id.noteTimestampTextView);
    }
}
}
