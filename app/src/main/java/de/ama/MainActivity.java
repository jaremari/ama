package de.ama;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnNotes;
    Button btnAudio;
    Button btnPictures;

    private String TAG = "PermissionDemo";
    private static final int REQUEST_RECORD_CODE = 101;
    private static final int REQUEST_WRITE_CODE = 101;
    private static final int REQUEST_READ_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Checking Record Audio Permission
        int permissionRecord = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO);

        if(permissionRecord != PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission to record audio denied");
            makeRequestRecord();
        }

        // Checking Write External Storage Permission
        int permissionWrite = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionWrite != PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission to write external Storage denied");
            makeRequestWrite();
        }

        // Checking Read External Storage Permission
        int permissionRead = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if(permissionRead != PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission to read external Storage denied");
            makeRequestRead();
        }


        InitializeApp();
    }


    private void InitializeApp(){

        btnNotes = (Button) findViewById(R.id.btnNotes);
        btnAudio = (Button) findViewById(R.id.btnAudio);
        btnPictures = (Button) findViewById(R.id.btnPictures);


        btnNotes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                startNotesActivity();
            }
        });

        btnAudio.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                startAudioActivity();
            }
        });

        btnPictures.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                startCameraIntentActivity();
            }
        });

    }

    private void startNotesActivity(){
        startActivity(new Intent(MainActivity.this, NotesActivity.class));
    }

    private void startAudioActivity(){
        startActivity(new Intent(MainActivity.this, AudioActivity.class));
    }

    private void startCameraIntentActivity(){
        startActivity(new Intent(MainActivity.this, CameraIntentActivity.class));
    }

    //Requesting Audio Record Permission
    protected void makeRequestRecord(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_CODE);
    }

    //Requesting Write Permission
    protected void makeRequestWrite(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_CODE);
    }

    //Requesting Read Permission
    protected void makeRequestRead(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_READ_CODE);
    }
}
