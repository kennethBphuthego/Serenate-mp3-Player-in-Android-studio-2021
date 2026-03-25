package com.example.serenate_20;

import static com.example.serenate_20.MainActivity.albums_list2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment {


         RecyclerView recyclerView3;
         AlbumAdapter AlbumAdapter;

    public AlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
       View view1 = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView3 = view1.findViewById(R.id.recyclerview3);
        recyclerView3.setHasFixedSize(true);

        if(!(albums_list2.size() <1))
           {

              AlbumAdapter = new AlbumAdapter(getContext(),albums_list2);
              recyclerView3.setAdapter(AlbumAdapter);
              recyclerView3.setLayoutManager(new GridLayoutManager(getContext(), 2));
           }
        return view1;
    }
}