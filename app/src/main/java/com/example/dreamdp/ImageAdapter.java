package com.example.dreamdp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    public Integer[] mThumbIds = {
            R.drawable.android, R.drawable.galbae,
            R.drawable.gazua, R.drawable.google,
            R.drawable.hyu, R.drawable.kaist,
            R.drawable.notimetodie, R.drawable.top_gun_maverick
    };

    public ImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setLayoutParams(new ViewGroup.LayoutParams(70,70));
        return imageView;
    }
}
