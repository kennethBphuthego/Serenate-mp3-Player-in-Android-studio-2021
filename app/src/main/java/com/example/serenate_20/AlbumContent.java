package com.example.serenate_20;

import static com.example.serenate_20.MainActivity.musicFiles;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AlbumContent extends AppCompatActivity {

    RecyclerView recyclerView4;
    ImageView albumPicture;
    String AlbumName;
    ArrayList<MusicFiles> albumTracks = new ArrayList<>();

    AlbumContentAdapter albumContentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.album_content_activity);

        recyclerView4 = findViewById(R.id.album_songList3);
        albumPicture = findViewById(R.id.albumPicture);
        AlbumName = getIntent().getStringExtra("AlbumName");

        int b = 0;
        for (int a = 0 ; a <musicFiles.size() ; a ++){

            if (AlbumName.equals(musicFiles.get(a).getAlbum())) {

                albumTracks.add(b, musicFiles.get(a));
                b ++;
            }

            byte[] image = getAlbumArt(albumTracks.get(0).getPath());

            if (image != null) {

                Glide.with(this).asBitmap()
                        .load(image)
                        .into(albumPicture);
            } else {
                Glide.with(this).asBitmap()
                        .load(R.drawable.default_cover)
                        .into(albumPicture);
            }


        }

    }
    @Override
    protected void onResume () {

        super.onResume();

        if (!(albumTracks.size() < 1)) {

            albumContentAdapter = new AlbumContentAdapter(this, albumTracks);
            recyclerView4.setAdapter(albumContentAdapter);
            recyclerView4.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }
    }



    private byte[] getAlbumArt (String uri){

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);

        return retriever.getEmbeddedPicture();
    }
}