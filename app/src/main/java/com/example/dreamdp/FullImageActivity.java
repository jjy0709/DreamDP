package com.example.dreamdp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Intent i = getIntent();

        int position = i.getExtras().getInt("id");
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(this);
        ViewPager imagePager = findViewById(R.id.image_view_pager);
        imagePager.setAdapter(imagePagerAdapter);
        imagePager.setCurrentItem(position);
    }
}