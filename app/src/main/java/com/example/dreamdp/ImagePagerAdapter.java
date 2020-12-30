package com.example.dreamdp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ImagePagerAdapter extends PagerAdapter {

    private Context mContext = null;
    public Integer[] mThumbIds = {
            R.drawable.android, R.drawable.galbae,
            R.drawable.gazua, R.drawable.google,
            R.drawable.hyu, R.drawable.kaist,
            R.drawable.notimetodie, R.drawable.top_gun_maverick,
            R.drawable.bird, R.drawable.cat,
            R.drawable.cocomong, R.drawable.pororo,
            R.drawable.coke1, R.drawable.pepsi
    };

    public ImagePagerAdapter() {

    }

    public ImagePagerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;

        if (mContext != null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.image_page, container, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.full_image_view);
            imageView.setImageResource(mThumbIds[position]);
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }
}
