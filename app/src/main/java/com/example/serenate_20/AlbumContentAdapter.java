package com.example.serenate_20;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class AlbumContentAdapter extends RecyclerView.Adapter<AlbumContentAdapter.ViewHolderB>{

     private Context context4;
     static  ArrayList<MusicFiles>albumFiles;

    public AlbumContentAdapter(Context context4, ArrayList<MusicFiles> albumFiles){

             this.context4 = context4;
             this.albumFiles = albumFiles;
             }

    @NonNull
    @Override
    public ViewHolderB onCreateViewHolder(@NonNull ViewGroup parent, int ViewType){
            View view4 = LayoutInflater.from(context4).inflate(R.layout.music_items,parent, false);
            return new ViewHolderB(view4);
   
          }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderB holder, int position){
           holder.album_name.setText(albumFiles.get(position).getTitle());

           byte[] image = getAlbumArt(albumFiles.get(position).getPath());

           if(image != null){

                  Glide.with(context4).asBitmap()
                                      .load(image)
                                      .into(holder.album_image);
              }

             else{
                   Glide.with(context4).asBitmap()
                                       .load(R.drawable.default_cover)
                                       .into(holder.album_image);

                }

                  holder.itemView.setOnClickListener(v -> {

                    Intent intent3= new Intent(context4, Player_Activity.class);
                           intent3.putExtra("invoker", "albumContent");
                           intent3.putExtra("position", position);
                           context4.startActivity(intent3);

                       });
                  

        }
   
    @Override
    public int getItemCount(){
           return albumFiles.size();
          }

    public class ViewHolderB extends RecyclerView.ViewHolder{

            ImageView album_image;
            TextView album_name;

        public ViewHolderB(@NonNull View itemView){
                   super(itemView);

                 album_image = itemView.findViewById(R.id.song_img1);
                 album_name = itemView.findViewById(R.id.song_name1);
                 
       }
    }

private byte[] getAlbumArt(String uri) {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art= retriever.getEmbeddedPicture();

        return art;
      }

}
