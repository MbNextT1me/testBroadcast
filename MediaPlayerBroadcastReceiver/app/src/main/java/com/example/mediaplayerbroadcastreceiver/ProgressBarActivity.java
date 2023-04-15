package com.example.mediaplayerbroadcastreceiver;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class ProgressBarActivity extends AppCompatActivity {

    private Button playPauseButton;
    private ProgressBar progressBar;
    private TextView currentTimeTextView;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressbar);

        playPauseButton = findViewById(R.id.play_pause_button);
        progressBar = findViewById(R.id.progress_bar);
        currentTimeTextView = findViewById(R.id.current_time_text_view);
        TextView totalTimeTextView = findViewById(R.id.total_time_text_view);

        mediaPlayer = new MediaPlayer();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaPlayer.setDataSource(getAssets().openFd("testTrack.mp3"));
            }
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int totalTime = mediaPlayer.getDuration();
        @SuppressLint("DefaultLocale") String totalTimeString = String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(totalTime),
                TimeUnit.MILLISECONDS.toSeconds(totalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime)));
        totalTimeTextView.setText(totalTimeString);

        handler = new Handler(Looper.getMainLooper());

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    playPauseButton.setText("Играть");
                    isPlaying = false;
                } else {
                    mediaPlayer.start();
                    playPauseButton.setText("Пауза");
                    isPlaying = true;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null) {
                                int currentPosition = mediaPlayer.getCurrentPosition();
                                int totalDuration = mediaPlayer.getDuration();
                                int progress = (int) (((float) currentPosition / totalDuration) * 100);
                                progressBar.setProgress(progress);

                                @SuppressLint("DefaultLocale") String currentTimeString = String.format("%d:%02d",
                                        TimeUnit.MILLISECONDS.toMinutes(currentPosition),
                                        TimeUnit.MILLISECONDS.toSeconds(currentPosition) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentPosition)));
                                currentTimeTextView.setText(currentTimeString);

                                handler.postDelayed(this, 1000);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
