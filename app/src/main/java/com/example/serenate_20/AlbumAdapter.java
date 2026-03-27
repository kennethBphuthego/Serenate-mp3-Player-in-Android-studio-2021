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


public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolderB>{

     private final Context context4;
     private final ArrayList<MusicFiles>albumFiles;

    public AlbumAdapter(Context context4, ArrayList<MusicFiles> albumFiles){

             this.context4 = context4;
             this.albumFiles = albumFiles;
             }

    @NonNull
    @Override
    public ViewHolderB onCreateViewHolder(@NonNull ViewGroup parent, int ViewType){
            View view4 = LayoutInflater.from(context4).inflate(R.layout.album_items,parent, false);
            return new ViewHolderB(view4);
   
          }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderB holder, int position){
           holder.album_name.setText(albumFiles.get(position).getAlbum());
           holder.artist_name.setText(albumFiles.get(position).getArtist());

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

                    Intent intent2= new Intent(context4, AlbumContent.class);
                           intent2.putExtra("AlbumName", albumFiles.get(position).getAlbum());
                           context4.startActivity(intent2);

                       });


        }
   
    @Override
    public int getItemCount(){
           return albumFiles.size();
          }

    public static class ViewHolderB extends RecyclerView.ViewHolder{

            ImageView album_image;
            TextView artist_name;
            TextView album_name;

        public ViewHolderB(@NonNull View itemView){
                   super(itemView);

                 album_image = itemView.findViewById(R.id.album_img1);
                 album_name = itemView.findViewById(R.id.album_name1);
                 artist_name = itemView.findViewById(R.id.artist_name1); 
       }
    }

private byte[] getAlbumArt(String uri) {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art= retriever.getEmbeddedPicture();

        return art;
      }

}
