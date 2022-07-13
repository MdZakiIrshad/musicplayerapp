package com.example.bjana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySongs extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }

    TextView textView;
ImageView play,previous,next;
ArrayList<File> songs;
MediaPlayer mediaPlayer;
String textContent;
int position;
Thread updateseek;
SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_songs);
    textView=findViewById(R.id.textView);
    play=findViewById(R.id.imageView7);
    previous=findViewById(R.id.imageView6);
    next=findViewById(R.id.imageView8);
    seekBar=findViewById(R.id.seekBar);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        songs=(ArrayList) bundle.getParcelableArrayList("songList");
        textContent=intent.getStringExtra("currentSong");
        textView.setText(textContent);
        position=intent.getIntExtra("position",0);
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaPlayer =MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
      seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {

          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());

          }
      });

updateseek=new Thread(){
    public void run(){
        int currentpositon=0;
        try {
            while(currentpositon<mediaPlayer.getDuration()){
                currentpositon=mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentpositon);
                sleep(800);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
};
updateseek.start();
play.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       if(mediaPlayer.isPlaying()){
           play.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
            mediaPlayer.pause();
       }
       else{
           play.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
             mediaPlayer.start();
       }
    }
});
previous.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        mediaPlayer.stop();
        mediaPlayer.release();
        if(position!=0){
            position=position-1;
        }
        else{
            position=songs.size()-1;
        }
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaPlayer =MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        play.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
        seekBar.setMax(mediaPlayer.getDuration());
        textContent=songs.get(position).getName().toString();
        textView.setText(textContent);
    }

});
next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        mediaPlayer.stop();
        mediaPlayer.release();
           if(position!=songs.size()-1){
               position=position+1;
           }
           else{
               position=0;
           }
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaPlayer =MediaPlayer.create(getApplicationContext(),uri);
        play.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);

        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        textContent=songs.get(position).getName().toString();
        textView.setText(textContent);
    }
});
    }
}