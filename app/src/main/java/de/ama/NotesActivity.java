package de.ama;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NotesActivity extends AppCompatActivity {

    Button btnSaveNotes;
    Button btnShowNotes;
    EditText editNotes;
    File folder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        InitializeApp();
    }


    private void InitializeApp(){

        folder = new File(Environment.getExternalStorageDirectory(), "AMA-Notes");
        if(!folder.exists()){
            folder.mkdirs();
        }

        editNotes = (EditText)findViewById(R.id.editNotes);
        btnSaveNotes = (Button)findViewById(R.id.btnSaveNotes);
        btnShowNotes = (Button)findViewById(R.id.btnShowNotes);

        btnSaveNotes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                if(editNotes.getText().length() >0){

                    File notesdata = new File(folder, "Text_" + System.currentTimeMillis() + ".txt");
                    try{
                        OutputStream outputStream = new FileOutputStream(notesdata);
                        outputStream.write(editNotes.getText().toString().getBytes());
                        outputStream.close();

                        editNotes.setText(null);
                    } catch (FileNotFoundException e){
                        e.printStackTrace();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Kein Text", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShowNotes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                if(folder.listFiles().length >0){
                    startActivity(new Intent(NotesActivity.this, ShowNotesActivity.class));
                } else{
                    Toast.makeText(getApplicationContext(), "Keine Notizen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
