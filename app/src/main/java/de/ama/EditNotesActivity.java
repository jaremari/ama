package de.ama;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EditNotesActivity extends AppCompatActivity {

    String notetext;
    File notefile;

    EditText editText;
    ImageButton btnDelete;

    boolean isDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        btnDelete = (ImageButton)findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                startDeleteActivity();
            }
        });

        editText = (EditText) findViewById(R.id.editText);

        if (getIntent().hasExtra("EXTRA_NOTE_NEXT") && getIntent().hasExtra("EXTRA_NOTE_FILE")) {

            notetext = getIntent().getStringExtra("EXTRA_NOTE_NEXT");
            notefile = (File) getIntent().getExtras().get("EXTRA_NOTE_FILE");

            editText.setText(notetext);
        }
    }

    private void startDeleteActivity(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(EditNotesActivity.this);
        dialog.setTitle("Delete note?");


        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                notefile.delete();
                Toast.makeText(getApplicationContext(), "Note has been deleted!", Toast.LENGTH_SHORT).show();
                isDeleted = true;
                finish();
                }
        });


        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // nothing happens
            }
        });

        dialog.show();
    }

    @Override
    protected void onPause() {

        if(!isDeleted) {
            try {
                OutputStream outputStream = new FileOutputStream(notefile);
                outputStream.write(editText.getText().toString().getBytes());
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onPause();
    }
}
