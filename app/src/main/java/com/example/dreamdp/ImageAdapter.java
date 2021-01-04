package com.example.dreamdp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private ArrayList<Bitmap> mList;
    private int realWidth;

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        protected ImageView img;

        public ImageViewHolder(View view) {
            super(view);
            this.img = (ImageView) view.findViewById(R.id.image_thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent i = new Intent(mContext, FullImageActivity.class);
                        i.putExtra("id", pos);
//                        Bitmap bitmap = mList.get(pos);
                        mContext.startActivity(i);
                    }
                }
            });
        }
    }

//    public CustomAdapter(ArrayList<Bitmap> list) {
    public ImageAdapter(Context c) {
//        this.mList = list;
        mContext = c;
        // get realWidth of device's display in pixel
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        realWidth = size.x;
        System.out.println(realWidth);
        mList = new ArrayList<Bitmap>();
        File mFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFiles[] = mFilesDir.listFiles();
        Bitmap thumbnail;
        for (File file : mFiles) {
            thumbnail = createThumbnail(file);
            mList.add(thumbnail);
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
            bitmap = Bitmap.createScaledBitmap(bitmap, realWidth/3, realWidth/3, true);
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
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery, viewGroup, false);

        ImageViewHolder viewHolder = new ImageViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder viewHolder, int position) {
        viewHolder.img.setImageBitmap(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return (mList != null ? mList.size() : 0);
    }

}
