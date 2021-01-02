package com.example.dreamdp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

public class GalleryFragment extends Fragment {

    public GridView gridView;
    //public ImageAdapter adapter;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        gridView = (GridView) v.findViewById(R.id.gallery);
        ImageAdapter adapter = new ImageAdapter(getContext());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(), FullImageActivity.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Gallery Fragment Resumed");
        ImageAdapter adapter = new ImageAdapter(getContext());
        //adapter.notifyDataSetChanged();
        gridView.setAdapter(adapter);
    }
//        adapter = new ImageAdapter(getContext());
//        gridView.setAdapter(adapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(getContext(), FullImageActivity.class);
//                i.putExtra("id", position);
//                startActivity(i);
//            }
//        });
//    }
}
