package de.ama;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AudioActivity extends AppCompatActivity {

    private ImageButton btnPlayAudio, btnStopAudio, btnStartRecord, btnStopRecord;
    private MediaRecorder myAudioRecorder = null;
    private String audioSavePathInDevice = null, randomAudioFilename = "ABCDEFGHIJKLMOP";
    private Random random;
    private MediaPlayer myAudioPlayer;

    public static final int RequestPermissionCode = 1;


        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_audio);

            btnPlayAudio = (ImageButton) findViewById(R.id.btnPlayAudio);
            btnStopAudio = (ImageButton) findViewById(R.id.btnStopAudio);
            btnStopRecord = (ImageButton) findViewById(R.id.btnStopRecord);
            btnStartRecord = (ImageButton) findViewById(R.id.btnStartRecord);

            btnStopRecord.setEnabled(false);
            btnPlayAudio.setEnabled(false);
            btnStopAudio.setEnabled(false);

            random = new Random();

        btnStartRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermission()) {

                    audioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            CreateRandomAudioFileName(5) + "AudioRecording.3gp";

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
        });

            btnStopRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myAudioRecorder.stop();

                    btnStopRecord.setEnabled(false);
                    btnPlayAudio.setEnabled(true);
                    btnStartRecord.setEnabled(true);
                    btnStopAudio.setEnabled(false);

                    Toast.makeText(AudioActivity.this, "Recording completed", Toast.LENGTH_LONG).show();

                }
            });

            btnPlayAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {

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
            });

            btnStopAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnStopRecord.setEnabled(false);
                    btnStartRecord.setEnabled(true);
                    btnStopAudio.setEnabled(false);
                    btnPlayAudio.setEnabled(true);

                    if (myAudioPlayer != null) {
                        myAudioPlayer.stop();
                        myAudioPlayer.release();
                        MediaRecorderReady();
                    }
                }
            });
        }
    public void MediaRecorderReady() {
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setOutputFile(audioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string){
            stringBuilder.append(randomAudioFilename.charAt(random.nextInt(randomAudioFilename.length())));

            i++;
        }
        return stringBuilder.toString();
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(AudioActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
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

