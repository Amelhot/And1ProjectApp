package com.example.smartnoteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNoteBtn = findViewById(R.id.newNoteBtn);
        recyclerView = findViewById(R.id.recyclerView);
        menuBtn = findViewById(R.id.menuBtn);


        addNoteBtn.setOnClickListener((v)-> startActivity(new Intent(MainActivity.this, NoteInfoActivity.class)));


        menuBtn.setOnClickListener((v) -> showMenu());


        createRecyclerView();

    }
    void showMenu(){
        //Todo Show Menu



    }

    void createRecyclerView(){

        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(options,this);
        recyclerView.setAdapter(notesAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        notesAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        notesAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        notesAdapter.notifyDataSetChanged();
    }
}