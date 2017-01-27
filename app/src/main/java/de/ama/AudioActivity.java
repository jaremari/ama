package de.ama;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.jar.Manifest;

public class AudioActivity extends AppCompatActivity {

    private Button play, stop, record;
    private MediaRecorder myAudioRecorder = null;
    private String outputFile;

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_audio);

            play = (Button) findViewById(R.id.play);
            stop = (Button) findViewById(R.id.stop);
            record = (Button) findViewById(R.id.record);

            stop.setEnabled(false);
            play.setEnabled(false);

            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setOutputFile(outputFile);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException ise) {
                    System.out.println("Exception: " + ise.getMessage());
                } catch (IOException ioe) {
                    System.out.println("Exception: " + ioe.getMessage());
                }

                record.setEnabled(false);
                stop.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    myAudioRecorder = null;
                    record.setEnabled(true);
                    stop.setEnabled(false);
                    play.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();
                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayer mediaPlayer = new MediaPlayer();

                    try {
                        mediaPlayer.setDataSource(outputFile);
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                        Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        System.out.println("Exception: " + e.getMessage());
                    }
                }
            });
        }
}

