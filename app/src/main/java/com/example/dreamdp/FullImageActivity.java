package com.example.dreamdp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

public class FullImageActivity extends AppCompatActivity {

//    private ScaleGestureDetector mScaleGestureDetector;
//    private float mScaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Intent i = getIntent();

        int position = i.getExtras().getInt("id");
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(this);
        ViewPager imagePager = findViewById(R.id.image_view_pager);
        imagePager.setAdapter(imagePagerAdapter);
        imagePager.setPageTransformer(true, new ZoomOutPageTransformer());
        imagePager.setOffscreenPageLimit(2);
        imagePager.setCurrentItem(position);

//        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) {
                view.setAlpha(0f);
            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else {
                view.setAlpha(0f);
            }
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent motionEvent) {
//        mScaleGestureDetector.onTouchEvent(motionEvent);
//        return true;
//    }
//
//    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//        ImageView imageView = findViewById(R.id.full_image_view);
//
//        @Override
//        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
//            mScaleFactor *= scaleGestureDetector.getScaleFactor();
//
//            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
//
//            imageView.setScaleX(mScaleFactor);
//            imageView.setScaleY(mScaleFactor);
//            return true;
//        }
//    }
}