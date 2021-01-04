package com.example.dreamdp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        TAKE_PICTURE);
                            }
                        }
                        dispatchTakePictureIntent();
                        break;
                    case 2:
                        Intent i = new Intent(getApplicationContext(), NewDiaryActivity.class);
                        startActivity(i);
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
                menu.add("Settings");
                menu.add("Delete");
                break;
            case 2:
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
            System.out.println(f.getClass());
            ContactsFragment contactsFragment;
            if(f.getClass() == ContactsFragment.class){
                contactsFragment = (ContactsFragment) list.get(0);
            }
            else{
                contactsFragment = (ContactsFragment) list.get(1);
            }
            contactsFragment.Delete_all();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                    System.out.println(f.getClass());
                    ContactsFragment contactsFragment;
                    if (f.getClass() == ContactsFragment.class) {
                        contactsFragment = (ContactsFragment) list.get(0);
                    } else {
                        contactsFragment = (ContactsFragment) list.get(1);
                    }


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