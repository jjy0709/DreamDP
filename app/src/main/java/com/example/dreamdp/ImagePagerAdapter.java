package com.example.dreamdp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

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
    public ArrayList<Bitmap> mImgBitmaps = new ArrayList<>();
    public ArrayList<File> mImageFiles = new ArrayList<>();
    public ArrayList<String> mImagePaths = new ArrayList<>();

    public ImagePagerAdapter(Context c) {
        mContext = c;
        File mFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFiles[] = mFilesDir.listFiles();
        Bitmap bitmap;
        for (File file: mFiles) {
//            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//            bitmap = getStraightImage(file.getAbsolutePath());
//            mImgBitmaps.add(bitmap);
//            mImageFiles.add(file);
            mImagePaths.add(file.getAbsolutePath());
        }
    }

    private Bitmap getStraightImage(String filePath) {
        Bitmap image = BitmapFactory.decodeFile(filePath);
        try {
            ExifInterface ei = new ExifInterface(filePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    image = rotateBitmap(image, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    image = rotateBitmap(image, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    image = rotateBitmap(image, 270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    // beginning of rotating matrix
    private Matrix getStraightMatrix(String filePath) {
        Matrix matrix = new Matrix();
        try {
            ExifInterface ei = new ExifInterface(filePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }
    // end of rotating matrix

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;

        if (mContext != null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.image_page, container, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.full_image_view);
//            imageView.setImageBitmap(mImgBitmaps.get(position));
//            imageView.setImageBitmap(getStraightImage(mImageFiles.get(position).getAbsolutePath()));
//            imageView.setScaleType(ImageView.ScaleType.MATRIX);
//            imageView.setImageMatrix(getStraightMatrix(mImageFiles.get(position).getAbsolutePath()));

            try {
                ExifInterface ei = new ExifInterface(mImagePaths.get(position));
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        imageView.setRotation(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        imageView.setRotation(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        imageView.setRotation(270);
                        break;
                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(BitmapFactory.decodeFile(mImagePaths.get(position)));
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
//        return mImgBitmaps.size();
//        return mImageFiles.size();
        return mImagePaths.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }
}
