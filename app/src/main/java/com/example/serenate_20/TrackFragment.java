package com.example.serenate_20;

import static com.example.serenate_20.MainActivity.musicFiles;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/// A simple [Fragment] subclass.
/// Use the [TrackFragment#newInstance] factory method to
/// create an instance of this fragment.

public class TrackFragment extends Fragment {

    RecyclerView recyclerView3;
     @SuppressLint("StaticFieldLeak")
     static MusicAdapter musicAdapter3;

    public TrackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
 
        // Inflate the layout for this fragment
        View view1 = inflater.inflate(R.layout.fragment_track, container, false);
        recyclerView3 = view1.findViewById(R.id.recyclerview3);
        recyclerView3.setHasFixedSize(true);

        if(!(musicFiles .size() <1))
           {

              musicAdapter3 = new MusicAdapter(getContext(),musicFiles );
              recyclerView3.setAdapter(musicAdapter3);
              recyclerView3.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL, false));
           }
        return view1;
    }
}