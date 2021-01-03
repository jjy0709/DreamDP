package com.example.dreamdp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
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
    private int imgCount = 0;

    public ImageAdapter(Context c) {
        mContext = c;
        File mFilesDir = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFiles[] = mFilesDir.listFiles();
        Bitmap thumbnail;
        for (File file : mFiles) {
            thumbnail = createThumbnail(file);
            mImgBitmaps.add(thumbnail);
        }
    }

    private Bitmap createThumbnail(File file) {
        Bitmap bitmap = getStraightImage(file.getAbsolutePath());
        if (bitmap != null) {
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            if (width < height) {
                bitmap = Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width);
            } else {
                bitmap = Bitmap.createBitmap(bitmap, (width - height) / 2, 0, height, height);
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
        }
        return bitmap;
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

    @Override
    public int getCount() {
        return mImgBitmaps.size();
        //return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mImgBitmaps.get(position);
        //return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        //imageView.setImageResource(mThumbIds[position]);
        imageView.setImageBitmap(mImgBitmaps.get(position));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(300,300));
        return imageView;
    }
}
