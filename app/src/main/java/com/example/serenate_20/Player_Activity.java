package com.example.serenate_20;

import static com.example.serenate_20.AlbumContentAdapter.albumFiles;
import static com.example.serenate_20.MainActivity.repeatBtnBoolean;
import static com.example.serenate_20.MainActivity.shuffleBtnBoolean;
import static com.example.serenate_20.MusicAdapter.musicFiles2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;


public class Player_Activity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{

        TextView Track_name,Artist_name,Duration_timer,Total_time;
        ImageView Back_btn,CoverArt,Shuffle_btn,Previous_btn,Next_btn,Repeat_btn;
        SeekBar SeekBar;
        FloatingActionButton  Play_Pause_Button;

        int position = -1;
        static ArrayList<MusicFiles> ListOfSongs = new ArrayList<>();
        static Uri uri2;
        MediaPlayer mediaPlayer1;
        private final Handler handler1 = new Handler();
        private Thread playThread,previousThread,nextThread;


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         EdgeToEdge.enable(this);
         setContentView(R.layout.player_activity);

         initial_Views2();
         getIntentMethod();

         Track_name.setText(ListOfSongs.get(position).getTitle());
         Artist_name.setText(ListOfSongs.get(position).getArtist());

         mediaPlayer1.setOnCompletionListener(this);

         SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

             @Override
             public void onProgressChanged(SeekBar SeekBar, int progress, boolean fromUser) {

                 if (mediaPlayer1 != null && fromUser) {

                     mediaPlayer1.seekTo(progress * 1000);
                 }
             }

             @Override
             public void onStartTrackingTouch(SeekBar SeekBar) {
             }

             @Override
             public void onStopTrackingTouch(SeekBar SeekBar) {
             }

         });

         Player_Activity.this.runOnUiThread(new Runnable() {

             @Override
             public void run() {

                 if (mediaPlayer1 != null) {

                     int currentPosition1 = mediaPlayer1.getCurrentPosition() / 1000;
                     SeekBar.setProgress(currentPosition1);
                     Duration_timer.setText(formattedTime(currentPosition1));
                 }

                 handler1.postDelayed(this, 1000);
             }
         });
         Shuffle_btn.setOnClickListener(v -> {

             if (shuffleBtnBoolean) {

                 shuffleBtnBoolean = false;
                 Shuffle_btn.setImageResource(R.drawable.btn_shuffle_off);
                 Toast.makeText(Player_Activity.this,"shuffle off",Toast.LENGTH_SHORT).show();
             } else {

                 shuffleBtnBoolean = true;
                 Shuffle_btn.setImageResource(R.drawable.btn_shuffle_on);
                 Toast.makeText(Player_Activity.this,"shuffle on",Toast.LENGTH_SHORT).show();
             }
         });
          Repeat_btn.setOnClickListener(v -> {

               if(repeatBtnBoolean == 0){

                 Repeat_btn.setImageResource(R.drawable.btn_repeat_off);
                   Toast.makeText(Player_Activity.this,"repeat off",Toast.LENGTH_SHORT).show();
                }
                else if(repeatBtnBoolean == 1){

                 Repeat_btn.setImageResource(R.drawable.btn_repeat_one);
                   Toast.makeText(Player_Activity.this,"repeat one",Toast.LENGTH_SHORT).show();
                }
                else{

                 Repeat_btn.setImageResource(R.drawable.repeat_all_btn);
                   Toast.makeText(Player_Activity.this,"repeat all",Toast.LENGTH_SHORT).show();
                }
               });


}


@Override
protected void onResume(){
                  
                    playThread();
                    previousThread();
                    nextThread();
                 super.onResume();      
}

private void playThread(){
                  
                 playThread = new Thread(){
                             
                          @Override
                          public void run(){
                              super.run();

                               Play_Pause_Button.setOnClickListener(v -> Play_Pause_ButtonClicked());
                                 }
                           };
                          playThread.start();
                    
}


private void Play_Pause_ButtonClicked(){

                       if(mediaPlayer1.isPlaying()){
                             Play_Pause_Button.setImageResource(R.drawable.btn_play);
                             mediaPlayer1.pause();
                             SeekBar.setMax(mediaPlayer1.getDuration()/1000);

                           Player_Activity.this.runOnUiThread(new Runnable(){

                               @Override
                               public void run(){

                                   if( mediaPlayer1 != null){

                                       int currentPosition1= mediaPlayer1.getCurrentPosition() / 1000;
                                       SeekBar.setProgress(currentPosition1);
                                   }

                                   handler1.postDelayed(this , 1000);
                               }
                           });
                        }

                    else{
                           Play_Pause_Button.setImageResource(R.drawable.btn_pause);
                           mediaPlayer1.start();
                           SeekBar.setMax(mediaPlayer1.getDuration()/1000);

                           Player_Activity.this.runOnUiThread(new Runnable(){

                               @Override
                               public void run(){

                                   if( mediaPlayer1 != null){

                                       int currentPosition1= mediaPlayer1.getCurrentPosition() / 1000;
                                       SeekBar.setProgress(currentPosition1);
                                   }

                                   handler1.postDelayed(this , 1000);
                               }
                           });
                      }
                    
}

private void previousThread(){

           previousThread = new Thread(){
                             
                          @Override
                          public void run(){
                              super.run();

                               Previous_btn.setOnClickListener(v -> Previous_btnClicked());
                                 }
                           };
                          previousThread.start();

}

private void  Previous_btnClicked(){

        if(mediaPlayer1.isPlaying()){

           mediaPlayer1.stop();
           mediaPlayer1.release();

            if(shuffleBtnBoolean && (repeatBtnBoolean == 0)){

              position = getRandomSong(ListOfSongs.size() -1);
            }
            else if (!shuffleBtnBoolean && (repeatBtnBoolean==0)){
                   
              position = ((position - 1) <0  ? (ListOfSongs.size() - 1) : (position -1));
            }

          
           uri2 = Uri.parse(ListOfSongs.get(position).getPath());

           mediaPlayer1= MediaPlayer.create(getApplicationContext(), uri2);
           metaData(uri2);

           
          Track_name.setText(ListOfSongs.get(position).getTitle());
          Artist_name.setText(ListOfSongs.get(position).getArtist());

          SeekBar.setMax(mediaPlayer1.getDuration()/1000);

            Player_Activity.this.runOnUiThread(new Runnable(){

                @Override
                public void run(){

                    if( mediaPlayer1 != null){

                        int currentPosition1= mediaPlayer1.getCurrentPosition() / 1000;
                        SeekBar.setProgress(currentPosition1);
                    }

                    handler1.postDelayed(this , 1000);
                }
            });
                         mediaPlayer1.setOnCompletionListener(this);
                         Play_Pause_Button.setBackgroundResource(R.drawable.btn_pause);
                         mediaPlayer1.start();
              }
              else{
                    mediaPlayer1.stop();
                    mediaPlayer1.release();

                    if(shuffleBtnBoolean && (repeatBtnBoolean==0)){

                      position = getRandomSong(ListOfSongs.size() -1);
                     }
                    else if (!shuffleBtnBoolean && (repeatBtnBoolean==0)){
                   
                      position = ((position - 1) <0  ? (ListOfSongs.size() - 1) : (position -1));
                     }
                    
                    uri2 = Uri.parse(ListOfSongs.get(position).getPath());

                    mediaPlayer1= MediaPlayer.create(getApplicationContext(), uri2);
                    metaData(uri2);

           
                    Track_name.setText(ListOfSongs.get(position).getTitle());
                    Artist_name.setText(ListOfSongs.get(position).getArtist());

                    SeekBar.setMax(mediaPlayer1.getDuration()/1000);

            Player_Activity.this.runOnUiThread(new Runnable(){

                @Override
                public void run(){

                    if( mediaPlayer1 != null){

                        int currentPosition1= mediaPlayer1.getCurrentPosition() / 1000;
                        SeekBar.setProgress(currentPosition1);
                    }

                    handler1.postDelayed(this , 1000);
                }
            });
                         mediaPlayer1.setOnCompletionListener(this);
                         Play_Pause_Button.setBackgroundResource(R.drawable.btn_play);
     
        }

}


private void nextThread(){

     nextThread = new Thread(){
                             
                          @Override
                          public void run(){
                              super.run();

                               Next_btn.setOnClickListener(v -> Next_btnClicked());
                                 }
                           };
                          nextThread.start();

}

private void Next_btnClicked(){

     if(mediaPlayer1.isPlaying()){

           mediaPlayer1.stop();
           mediaPlayer1.release();

           if(shuffleBtnBoolean && (repeatBtnBoolean==0)){

              position = getRandomSong(ListOfSongs.size() -1);
            }
            else if (!shuffleBtnBoolean && (repeatBtnBoolean==0)){
                   
               position = ((position + 1) % ListOfSongs.size());
            }

          
           uri2 = Uri.parse(ListOfSongs.get(position).getPath());

           mediaPlayer1= MediaPlayer.create(getApplicationContext(), uri2);
           metaData(uri2);

           
          Track_name.setText(ListOfSongs.get(position).getTitle());
          Artist_name.setText(ListOfSongs.get(position).getArtist());

          SeekBar.setMax(mediaPlayer1.getDuration()/1000);

         Player_Activity.this.runOnUiThread(new Runnable(){

             @Override
             public void run(){

                 if( mediaPlayer1 != null){

                     int currentPosition1= mediaPlayer1.getCurrentPosition() / 1000;
                     SeekBar.setProgress(currentPosition1);
                 }

                 handler1.postDelayed(this , 1000);
             }
         });
                         mediaPlayer1.setOnCompletionListener(this);
                         Play_Pause_Button.setBackgroundResource(R.drawable.btn_pause);
                         mediaPlayer1.start();
              }
              else{
                    mediaPlayer1.stop();
                    mediaPlayer1.release();

                    if(shuffleBtnBoolean && repeatBtnBoolean==0){

                     position = getRandomSong(ListOfSongs.size() -1);
                    }
                    else if (!shuffleBtnBoolean && (repeatBtnBoolean == 0)){
                   
                     position = ((position + 1) % ListOfSongs.size());
                    }
                   
                    uri2 = Uri.parse(ListOfSongs.get(position).getPath());

                    mediaPlayer1= MediaPlayer.create(getApplicationContext(), uri2);
                    metaData(uri2);

           
                    Track_name.setText(ListOfSongs.get(position).getTitle());
                    Artist_name.setText(ListOfSongs.get(position).getArtist());

                    SeekBar.setMax(mediaPlayer1.getDuration()/1000);

         Player_Activity.this.runOnUiThread(new Runnable(){

             @Override
             public void run(){

                 if( mediaPlayer1 != null){

                     int currentPosition1= mediaPlayer1.getCurrentPosition() / 1000;
                     SeekBar.setProgress(currentPosition1);
                 }

                 handler1.postDelayed(this , 1000);
             }
         });
                         mediaPlayer1.setOnCompletionListener(this);
                         Play_Pause_Button.setBackgroundResource(R.drawable.btn_play);
     
                 }

}


private int getRandomSong(int i){

      Random random1 = new Random();
      return random1.nextInt(i + 1);          

}


private String formattedTime(int currentPosition1){

                       String totalOut;
                       String totalNew;
                       String seconds=String.valueOf(currentPosition1 % 60);
                       String minutes=String.valueOf(currentPosition1 / 60);

                       totalOut = minutes+ ":" + seconds;
                       totalNew = minutes+ ":"+ "0" + seconds;

                       if(seconds.length() == 1){
                      
                               return totalNew;
                           }
                           else{
                                return totalOut;
                               }
}
     
private void getIntentMethod(){

              position = getIntent().getIntExtra("position", -1);
       
              String invoker = getIntent().getStringExtra("invoker");


              if(invoker != null && invoker.equals("albumContent")){

                   ListOfSongs = albumFiles;
                 }else{

                   ListOfSongs = musicFiles2; 
                  }

              if( ListOfSongs != null){

                 Play_Pause_Button.setImageResource(R.drawable.btn_pause);
                 uri2 = Uri.parse(ListOfSongs.get(position).getPath());
              }

                if( mediaPlayer1 != null){

                   mediaPlayer1.stop();
                   mediaPlayer1.release();
                   mediaPlayer1 = MediaPlayer.create(getApplicationContext(), uri2);
                   mediaPlayer1.start();
                 }
                 else
                 {

                     mediaPlayer1 = MediaPlayer.create(getApplicationContext(), uri2);
                     mediaPlayer1.start();
                 }

                  SeekBar.setMax(mediaPlayer1.getDuration() / 1000);
                  metaData(uri2);

 }

 private void initial_Views2(){

            Track_name = findViewById(R.id.track_name);
            Artist_name = findViewById(R.id.artist_name);
            Duration_timer = findViewById(R.id.duration_timer);
            Total_time = findViewById(R.id.duration_total);
   
           //ImageViews
            Back_btn = findViewById(R.id.back_btn);
            CoverArt = findViewById(R.id.coverArt);
            Shuffle_btn =findViewById(R.id.shuffle_btn);
            Previous_btn = findViewById(R.id.previous_btn);
            Next_btn = findViewById(R.id.next_btn);
            Repeat_btn = findViewById(R.id.repeat_btn);

           //seekbar
            SeekBar = findViewById(R.id.seekBar);

            //floatButton
            Play_Pause_Button = findViewById(R.id.play_pause_float);
       
}
  
private void metaData(Uri uri2) {

    MediaMetadataRetriever retriever2 = new MediaMetadataRetriever();
    retriever2.setDataSource(uri2.toString());
    int DurationTotal = Integer.parseInt(ListOfSongs.get(position).getDuration()) / 1000;
    Total_time.setText(formattedTime(DurationTotal));
    byte[] art = retriever2.getEmbeddedPicture();
    Bitmap Bitmap3;

    if (art != null) {
  
        Bitmap3= BitmapFactory.decodeByteArray(art, 0, art.length);
        ImageAnimation(this, CoverArt, Bitmap3);
    } else {
        Glide.with(this).asBitmap()
                .load(R.drawable.default_cover)
                .into(CoverArt);

    }
}

               
public void ImageAnimation(Context context3, ImageView Imageview3, Bitmap Bitmap3){

     Animation AnimeOut = AnimationUtils.loadAnimation(context3, android.R.anim.fade_out);
     Animation AnimeIn = AnimationUtils.loadAnimation(context3, android.R.anim.fade_in);

     AnimeOut.setAnimationListener(new Animation.AnimationListener(){

        
          
             @Override
             public void onAnimationStart(Animation animation){

               } 

             @Override
             public void onAnimationEnd(Animation animation){
                  
                  Glide.with(context3).load(Bitmap3).into(Imageview3);
                    AnimeOut.setAnimationListener(new Animation.AnimationListener(){

                          @Override
                          public void onAnimationStart(Animation animation){

                               }
                          @Override
                          public void onAnimationEnd(Animation animation){

                               }

                          @Override
                          public void onAnimationRepeat(Animation animation){

                               }  
                        });
                           Imageview3.startAnimation(AnimeIn);

               } 
 
             @Override
             public void onAnimationRepeat(Animation animation){

               } 


           }); 
               Imageview3.startAnimation(AnimeOut);   
}


@Override
public void onCompletion(MediaPlayer mp){

       Next_btnClicked();

       if(mediaPlayer1 != null){
  
            mediaPlayer1 = MediaPlayer.create(getApplicationContext(), uri2);
            mediaPlayer1.start();
            mediaPlayer1.setOnCompletionListener(this);
           }

}


   
}