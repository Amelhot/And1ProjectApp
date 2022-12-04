package com.example.smartnoteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.UtteranceProgressListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteInfoActivity extends AppCompatActivity {

    EditText titleEditText;
    EditText contentEditText;
    ImageButton saveNoteBtn;
    TextView titlePageTxtV;
    String title, content, docID;
    boolean isCheckCurrent = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_info);

        titleEditText = findViewById(R.id.noteTitleTxt);
        contentEditText = findViewById(R.id.noteContentTxt);
        saveNoteBtn = findViewById(R.id.checkSaveBtn);
        titlePageTxtV = findViewById(R.id.titlePage);

        //Get data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docID = getIntent().getStringExtra("docId");

        if (docID != null && !docID.isEmpty()){
            isCheckCurrent = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if (isCheckCurrent){
            titlePageTxtV.setText("Edit note");
        }


        saveNoteBtn.setOnClickListener((v)-> saveNote());


    }

    void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        if (noteTitle == null || noteTitle.isEmpty() ){
            titleEditText.setError("Title is needed");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }

    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if (isCheckCurrent){
        // Edit Note
            documentReference = Utility.getCollectionReferenceForNotes().document(docID);


        }else{
            //Create Note
            documentReference = Utility.getCollectionReferenceForNotes().document();

        }


        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Added note
                    Utility.showToast(NoteInfoActivity.this, "Notes Added!");
                    finish();

                }else{
                    Utility.showToast(NoteInfoActivity.this,"Adding note failed");
                }

            }
        });

    }


}