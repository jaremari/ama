package de.ama;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AudioActivity extends AppCompatActivity {

    private ImageButton btnPlayAudio, btnStopAudio, btnStartRecord, btnStopRecord;
    private MediaRecorder myAudioRecorder = null;
    private String audioSavePathInDevice = null;
    private Random random;
    private MediaPlayer myAudioPlayer;

    private String AUDIO_LOCATION = "AMA-Audio";
    private File audioFileDir;

    public static final int REQUEST_RECORD_CODE = 101;

        @Override
        protected void onCreate (Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_audio);

            createAudioDirectory();
            InitializeActivity();

            btnStopRecord.setEnabled(false);
            btnPlayAudio.setEnabled(false);
            btnStopAudio.setEnabled(false);

            btnStartRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRecord();
                }
            });

            btnStopRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopRecord();
                }
            });

            btnPlayAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                    startPlayback();
                }
            });

            btnStopAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopPlayback();
                }
            });
        }

    private void InitializeActivity(){

        btnPlayAudio = (ImageButton) findViewById(R.id.btnPlayAudio);
        btnStopAudio = (ImageButton) findViewById(R.id.btnStopAudio);
        btnStopRecord = (ImageButton) findViewById(R.id.btnStopRecord);
        btnStartRecord = (ImageButton) findViewById(R.id.btnStartRecord);

        random = new Random();
    }

    private void startRecord(){

        if (checkPermission()) {

            audioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + audioFileDir +
                    CreateRandomAudioFileName();

            MediaRecorderReady();

            try {
                myAudioRecorder.prepare();
                myAudioRecorder.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            btnStartRecord.setEnabled(false);
            btnStopRecord.setEnabled(true);

            Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();

        } else {
            requestPermission();
        }
    }

    private void stopRecord(){
        try {
            myAudioRecorder.stop();
        } catch (IllegalStateException e){
            e.printStackTrace();
        }

        btnStopRecord.setEnabled(false);
        btnPlayAudio.setEnabled(true);
        btnStartRecord.setEnabled(true);
        btnStopAudio.setEnabled(false);

        Toast.makeText(AudioActivity.this, "Recording completed", Toast.LENGTH_LONG).show();
    }

    private void startPlayback(){

        btnStopRecord.setEnabled(false);
        btnStartRecord.setEnabled(false);
        btnStopAudio.setEnabled(true);

        myAudioPlayer = new MediaPlayer();
        try {
            myAudioPlayer.setDataSource(audioSavePathInDevice);
            myAudioPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        myAudioPlayer.start();
        Toast.makeText(AudioActivity.this, "Playing last record", Toast.LENGTH_LONG).show();
    }

    private void stopPlayback(){
        btnStopRecord.setEnabled(false);
        btnStartRecord.setEnabled(true);
        btnStopAudio.setEnabled(false);
        btnPlayAudio.setEnabled(true);

        if (myAudioPlayer != null) {
            myAudioPlayer.stop();
            myAudioPlayer.release();
            MediaRecorderReady();

            Toast.makeText(AudioActivity.this, "Stopped playback", Toast.LENGTH_LONG).show();
        }
    }

    private void createAudioDirectory(){

        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        audioFileDir = new File(storageDirectory, AUDIO_LOCATION);
        if(!audioFileDir.exists()){
            audioFileDir.mkdirs();
        }
    }

    public void MediaRecorderReady() {

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setOutputFile(audioSavePathInDevice);
    }

    public String CreateRandomAudioFileName() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String audioFileName = timeStamp + ".3gp";

        return audioFileName;
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RECORD_CODE:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(AudioActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AudioActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
}

