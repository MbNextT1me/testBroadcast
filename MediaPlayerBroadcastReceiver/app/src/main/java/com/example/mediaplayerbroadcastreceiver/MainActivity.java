package com.example.mediaplayerbroadcastreceiver;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button playPauseButton;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playPauseButton = findViewById(R.id.play_pause_button);
        Button secondActivityButton = findViewById(R.id.second_activity_button);

        AssetFileDescriptor afd = null;
        try {
            afd = getAssets().openFd("testTrack.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer = new MediaPlayer();
        try {
            assert afd != null;
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            afd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                mediaPlayer.pause();
                playPauseButton.setText("Играть");
                isPlaying = false;
            } else {
                mediaPlayer.start();
                playPauseButton.setText("Пауза");
                isPlaying = true;
            }
        });

        secondActivityButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProgressBarActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
