package com.example.audiomedia;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //Initialize variable
    TextView playerPosittion, playerDuration;
    SeekBar seekBar;
    ImageView btRew, btPlay, btPause, btFf;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Asign variable
        playerPosittion = findViewById(R.id.player_position);
        playerDuration = findViewById(R.id.player_duration);
        seekBar = findViewById(R.id.seek_bar);
        btRew = findViewById(R.id.bt_rew);
        btPause = findViewById(R.id.bt_pause);
        btFf = findViewById(R.id.bt_ff);
        btPlay = findViewById(R.id.bt_play);

        //Initialize media player
        mediaPlayer = MediaPlayer.create(this, R.raw.music);


        //Initialize runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                //Set progress on seek bar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                //Handler post delay for 0.5 second
                handler.postDelayed(this, 500);

            }
        };

        //Get duration of media player
        int duration = mediaPlayer.getDuration();
        //Convert millisecond to minute and second
        String sDuration = convertFormat(duration);
        //Set duration on text view
        playerDuration.setText(sDuration);

    btPlay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Hide play button
            btPlay.setVisibility(View.GONE);
            //Show media player
            btPause.setVisibility(View.VISIBLE);
            //Start media player
            mediaPlayer.start();
            //Set max on seek bar
            seekBar.setMax(mediaPlayer.getDuration());
            //Start handler
            handler.postDelayed(runnable,0);
        }
    });

    btPause.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Hide pause button
            btPause.setVisibility(View.GONE);
            //Show play button
            btPlay.setVisibility(View.VISIBLE);
            //Pause media player
            mediaPlayer.pause();
            //Stop handler
            handler.removeCallbacks(runnable);
        }
    });

    btFf.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Get current position of media play
            int currentPosition = mediaPlayer.getCurrentPosition();
            //Get duration of media player
            int duration = mediaPlayer.getDuration();
            //(Check condition)
            if (mediaPlayer.isPlaying() && duration != currentPosition){
                //When media is playing and duration is not equal to current position
                //Fast forward for 5 seconds
                currentPosition = currentPosition + 5000;
                //Set current position on text view
                playerPosittion.setText(convertFormat(currentPosition));
                //Set progress on seek bar
                mediaPlayer.seekTo(currentPosition);
            }
        }
    });
    btRew.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Get current position of media player
            int currentPosition = mediaPlayer.getCurrentPosition();
            //Check condition
            if (mediaPlayer.isPlaying() && currentPosition > 5000){
                //When media is playing and current position in greater than 5 seconds
                //Rewind for 5 seconds
                currentPosition = currentPosition - 5000;
                //Get current position on text view
                playerPosittion.setText(convertFormat(currentPosition));
                //Set progress on seek bar
                mediaPlayer.seekTo(currentPosition);
            }
        }
    });

    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        // i: progress, b : from User
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            //Check conditon
            if (b){
                //when drag the seek bar
                //set progress on seek bar
                mediaPlayer.seekTo(i);
            }
            //Set current positon on text view
            playerPosittion.setText(convertFormat(mediaPlayer.getCurrentPosition()));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });

    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            //Hide pause button
            btPause.setVisibility(View.GONE);
            //Show play button
            btPlay.setVisibility(View.VISIBLE);
            //Set media player to initial position
            mediaPlayer.seekTo(0);
        }
    });

    }
    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration){
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}