package com.example.dreamdp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    public GridView gridView;
    //public ImageAdapter adapter;

    public RecyclerView recyclerGrid;
    private ArrayList<Bitmap> mArrayList;
    private ImageAdapter mAdapter;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Implementation with RecyclerView
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerGrid = (RecyclerView) v.findViewById(R.id.gallery);
        int colNum = 3;
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), colNum);
        recyclerGrid.setLayoutManager(mGridLayoutManager);

        mArrayList = new ArrayList<>();

        mAdapter = new ImageAdapter(getContext());
        recyclerGrid.setAdapter(mAdapter);

//        recyclerGrid.addOnItemTouchListener(new RecyclerView.OnItemTouchListener(this, recyclerGrid, new) {
//            @Override
//            public void onClick(View view, int position) {
//                Bitmap image = mArrayList.get(position);
//            }
//        }));

        // end of implementation

        return v;
    }

    public int isGalleryFrag(){
        return 1;
    }
}
