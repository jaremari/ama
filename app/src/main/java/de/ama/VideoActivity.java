package de.ama;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

/**
 * Created by jmari on 31.01.2017.
 */

public class VideoActivity extends Activity{

    private Button mRecordView, mPlayView;
    private VideoView mVideoView;
    private int ACTIVITY_START_CAMERA_APP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mRecordView = (Button)findViewById(R.id.recordButton);
        mPlayView = (Button)findViewById(R.id.playButton);
        mVideoView = (VideoView)findViewById(R.id.videoView);

        mRecordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callVideoAppIntent = new Intent();
                callVideoAppIntent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);

                startActivityForResult(callVideoAppIntent, ACTIVITY_START_CAMERA_APP);
            }
        });

        mPlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.start();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK){
            Uri videoUri = data.getData();
            mVideoView.setVideoURI(videoUri);
        }
    }
}
