package com.example.dreamdp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dreamdp.ui.main.SectionsPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    final static int TAKE_PICTURE = 1;
    String mCurrentPhotoPath;
    final static int REQUEST_ADD_PERSON = 0;
    final static int REQUEST_TAKE_PHOTO = 1;
    final static int REQUEST_ADD_DIARY = 2;
    DBHelper helper;
    SQLiteDatabase db;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_DreamDP_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new DBHelper(this,"diary.db", null, 1);
        db = helper.getWritableDatabase();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(viewPager.getWindowToken(),0);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewPager.getCurrentItem();
                switch (position) {
                    case 0:
                        Intent i1 = new Intent(getApplicationContext(), ContactAddActivity.class);
                        startActivityForResult(i1, REQUEST_ADD_PERSON);

                        break;
                    case 1:
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            TAKE_PICTURE);
                                }
                            }
                            dispatchTakePictureIntent();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        Intent i = new Intent(getApplicationContext(), NewDiaryActivity.class);
                        i.putExtra("request_code",REQUEST_ADD_DIARY);
                        Cursor c = db.rawQuery("SELECT * from mytable order by date asc",null);
                        startActivityForResult(i, REQUEST_ADD_DIARY);
                        break;
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        viewPager = findViewById(R.id.view_pager);
        int position = viewPager.getCurrentItem();
        switch (position) {
            case 0:
                menu.clear();
                menu.add("Delete All");
                break;
            case 1:
                menu.clear();
                menu.add("Whatever");
                break;
            case 2:
                menu.clear();
                menu.add("Delete");
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
         CharSequence id = item.getTitle();

        //noinspection SimplifiableIfStatement
        if (id == "Settings") {
            return true;
        }
        else if(id == "Delete All"){
            ArrayList<Fragment> list = (ArrayList<Fragment>) getSupportFragmentManager().getFragments();
            Fragment f = list.get(0);
            Fragment f2 = list.get(1);
            ContactsFragment contactsFragment;
            if(f.getClass() == ContactsFragment.class){
                contactsFragment = (ContactsFragment) list.get(0);
            }
            else if(f2.getClass() == ContactsFragment.class){
                contactsFragment = (ContactsFragment) list.get(1);
            }
            else{contactsFragment = (ContactsFragment) list.get(2);}
            contactsFragment.Delete_all();
            return true;
        }
        else if(id == "Delete"){
            TextView textView = findViewById(R.id.content_view);
            String content = textView.getText().toString();
            Cursor cursor = db.rawQuery("SELECT * from mytable order by date asc",null);
            while(cursor.moveToNext()){
                String cont = cursor.getString(3);
                if(cont.equals(content)){
                    int val = cursor.getInt(0);
                    db.execSQL("DELETE from mytable where _id = " + val + ";");

                    ArrayList<Fragment> list = (ArrayList<Fragment>) getSupportFragmentManager().getFragments();
                    Fragment f = list.get(0);
                    Fragment f2 = list.get(1);
                    DiaryFragment diaryFragment;
                    if (f.getClass() == DiaryFragment.class) {
                        diaryFragment = (DiaryFragment) list.get(0);
                    } else if (f2.getClass() == DiaryFragment.class) {
                        diaryFragment = (DiaryFragment) list.get(1);
                    } else {
                        diaryFragment = (DiaryFragment) list.get(2);
                    }
                    float rating = cursor.getFloat(2);
                    diaryFragment.whenDataSetChanged(rating,2);
                    textView.setText("");
                    break;
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "카메라/내부 저장소 접근 권한을 허용해 주세요.", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case REQUEST_ADD_PERSON:
                if(resultCode == RESULT_OK) {
                    String name = intent.getStringExtra("name");
                    String number = intent.getStringExtra("number");
                    int pic_ = intent.getIntExtra("picture", 0);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putString("number", number);
                    bundle.putInt("picture", pic_);
                    ArrayList<Fragment> list = (ArrayList<Fragment>) getSupportFragmentManager().getFragments();

                    Fragment f = list.get(0);
                    Fragment f2 = list.get(1);
                    ContactsFragment contactsFragment;
                    if (f.getClass() == ContactsFragment.class) {
                        contactsFragment = (ContactsFragment) list.get(0);
                    } else if(f2.getClass() == ContactsFragment.class) {
                        contactsFragment = (ContactsFragment) list.get(1);
                    }
                    else{contactsFragment = (ContactsFragment) list.get(2);}


                    contactsFragment.setArguments(bundle);
//                    contactsFragment.adapter.addItem(getResources().getDrawable(R.drawable.android), name, number); // => ERROR
                }
                break;

            case REQUEST_TAKE_PHOTO:
                File file = new File(mCurrentPhotoPath);
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap;
                    if (Build.VERSION.SDK_INT >= 29) {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                        try {
                            bitmap = ImageDecoder.decodeBitmap(source);
                            if (bitmap != null) {
                                // implement something to do here.
                                System.out.println("Image captured.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                            if (bitmap != null) {
                                // implement something to do here.
                                System.out.println("Image captured.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // delete an empty file if no picture is taken
                    if (file.exists()) {
                        file.delete();
                    }
                }
                ArrayList<Fragment> list2 = (ArrayList<Fragment>) getSupportFragmentManager().getFragments();
                GalleryFragment galleryFragment = (GalleryFragment) list2.get(1);
                galleryFragment.updateGrid();

                break;

            case REQUEST_ADD_DIARY:
                if (resultCode == RESULT_OK && intent != null) {
                    int date = intent.getIntExtra("date", 0);
                    float rating = intent.getFloatExtra("rating", 0);
                    String comment = intent.getStringExtra("comment");
                    int weather = intent.getIntExtra("weather", 0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("date", date);
                    contentValues.put("rating", rating);
                    contentValues.put("weather", weather);
                    contentValues.put("content", comment);
                    db.insert("mytable", null, contentValues);
                    ArrayList<Fragment> list = (ArrayList<Fragment>) getSupportFragmentManager().getFragments();
                    Fragment f = list.get(0);
                    Fragment f2 = list.get(1);
                    DiaryFragment diaryFragment;
                    if (f.getClass() == DiaryFragment.class) {
                        diaryFragment = (DiaryFragment) list.get(0);
                    } else if (f2.getClass() == DiaryFragment.class) {
                        diaryFragment = (DiaryFragment) list.get(1);
                    } else {
                        diaryFragment = (DiaryFragment) list.get(2);
                    }

                    diaryFragment.whenDataSetChanged(rating,1);
                }
                break;
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try { photoFile = createImageFile(); }
            catch (IOException ex) {}
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.dreamdp.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}