package com.example.serenate_20;


import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolderA>{

   private final Context context2;
   static  ArrayList<MusicFiles>musicFiles2;

   MusicAdapter(Context context2,ArrayList<MusicFiles>musicFiles2){

             this.context2 = context2;
             this.musicFiles2 = musicFiles2;
             }

   @NonNull
   @Override
   public ViewHolderA onCreateViewHolder(@NonNull ViewGroup parent, int ViewType){
            View view2 = LayoutInflater.from(context2).inflate(R.layout.music_items,parent, false);
            return new ViewHolderA(view2);
   
          }

   @SuppressLint("NonConstantResourceId")
   @Override
   public void onBindViewHolder(@NonNull ViewHolderA holder, int position){
           holder.file_name.setText(musicFiles2.get(position).getTitle());
           holder.artist_name.setText(musicFiles2.get(position).getArtist());

           byte[] image = getAlbumArt(musicFiles2.get(position).getPath());

           if(image != null){

                  Glide.with(context2).asBitmap()
                                      .load(image)
                                      .into(holder.album_art);
              }

             else{
                   Glide.with(context2).asBitmap()
                                       .load(R.drawable.default_cover)
                                       .into(holder.album_art);

                }

                 holder.itemView.setOnClickListener(v -> {

                    Intent intent1= new Intent(context2, Player_Activity.class);
                           intent1.putExtra("position", position);
                           context2.startActivity(intent1);

                       });
                       holder.song_options_btn.setOnClickListener(v -> {

                          PopupMenu popupMenu1 = new PopupMenu(context2, v);
                          popupMenu1.getMenuInflater().inflate(R.menu.menu_options,popupMenu1.getMenu());
                          popupMenu1.show();
                          popupMenu1.setOnMenuItemClickListener((item) -> {

                              if (item.getItemId() == R.id.delete) {
                                  Toast.makeText(context2, "Deleting track", Toast.LENGTH_SHORT).show();
                                  deleteTrack(position, v);
                              }
                                             return true;
                               });
                             });
      
          }

private void deleteTrack(int position, View v){
 
       Uri contentUri1 = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                         Long.parseLong(musicFiles2.get(position).getId()));
       musicFiles2.remove(position);
        
       File files1 = new File(musicFiles2.get(position).getPath());
       boolean deleted= files1.delete();
      
       if(deleted){
       context2.getContentResolver().delete(contentUri1, null, null);
       notifyItemRemoved(position);
       notifyItemRangeChanged(position, musicFiles2.size());
       Snackbar.make(v, "Track Deleted", Snackbar.LENGTH_LONG).show();
         }
       else{
             Snackbar.make(v, "Track Not Deleted", Snackbar.LENGTH_LONG).show();
         }
}
 
   @Override
   public int getItemCount(){
           return musicFiles2.size();
          }

    public static class ViewHolderA extends RecyclerView.ViewHolder{

       ImageView album_art;
       ImageView song_options_btn;
       TextView file_name;
       TextView artist_name;
    
       public ViewHolderA(@NonNull View itemView){
          super(itemView);

          album_art = itemView.findViewById(R.id.song_img1);
          file_name = itemView.findViewById(R.id.song_name1);
          artist_name = itemView.findViewById(R.id.artist_name1);
          song_options_btn = itemView.findViewById(R.id.song_options);
         
         }

  }

  private byte[] getAlbumArt(String uri) {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);

      return retriever.getEmbeddedPicture();
      }

 void listUpdate(ArrayList<MusicFiles>filesArray){

             musicFiles2 = new ArrayList<>();
             musicFiles2.addAll(filesArray); 
             notifyDataSetChanged();
           }
}