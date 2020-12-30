package com.example.dreamdp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

public class GalleryFragment extends Fragment {

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.gallery);
        ImageAdapter adapter = new ImageAdapter(getContext());
        gridView.setAdapter(adapter);
        return v;
    }
}
