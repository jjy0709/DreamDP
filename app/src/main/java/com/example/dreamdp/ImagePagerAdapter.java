package com.example.dreamdp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

public class ImagePagerAdapter extends PagerAdapter {

    private Context mContext = null;
    public Integer[] mThumbIds = {

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
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(270);
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

            PhotoView imageView = (PhotoView) view.findViewById(R.id.full_image_view);
//            imageView.setImageBitmap(mImgBitmaps.get(position));
//            imageView.setImageBitmap(getStraightImage(mImageFiles.get(position).getAbsolutePath()));
//            imageView.setScaleType(ImageView.ScaleType.MATRIX);
//            imageView.setImageMatrix(getStraightMatrix(mImageFiles.get(position).getAbsolutePath()));
            imageView.setImageMatrix(getStraightMatrix(mImagePaths.get(position)));

            Bitmap bitmap = BitmapFactory.decodeFile(mImagePaths.get(position));
//            Matrix matrix = getStraightMatrix(mImageFiles.get(position).getAbsolutePath());
            Matrix matrix = getStraightMatrix(mImagePaths.get(position));
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//            bitmap.recycle();
            imageView.setImageBitmap(bmRotated);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                }
            });



//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
//                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//                }
//            });

//            try {
//                ExifInterface ei = new ExifInterface(mImagePaths.get(position));
//                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//                int tmp;
//                ViewGroup.LayoutParams params;
//                switch (orientation) {
//                    case ExifInterface.ORIENTATION_ROTATE_90:
//                        imageView.setRotation(90);
//                        params = imageView.getLayoutParams();
//                        tmp = params.width;
//                        params.width = params.height;
//                        params.height = tmp;
//                        imageView.setLayoutParams(params);
//                        break;
//                    case ExifInterface.ORIENTATION_ROTATE_180:
//                        imageView.setRotation(180);
//                        break;
//                    case ExifInterface.ORIENTATION_ROTATE_270:
//                        imageView.setRotation(270);
//                    case ExifInterface.ORIENTATION_NORMAL:
//                    default:
//                        break;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            imageView.setImageBitmap(BitmapFactory.decodeFile(mImagePaths.get(position)));
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
