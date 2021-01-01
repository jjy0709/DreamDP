package com.example.dreamdp;

import android.content.Intent;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.dreamdp.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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
                        Intent i1 = new Intent(getApplicationContext(), Contact_SnackBar.class);
                        startActivityForResult(i1,0);

                        break;
                    case 1:
                        Snackbar.make(view, "Button clicked on Tab2", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                    case 2:
                        Snackbar.make(view, "Button clicked on Tab3", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
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
                menu.add("Settings");
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println(resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            String number = data.getStringExtra("number");
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("number", number);
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



            contactsFragment.setArguments(bundle);
//            contactsFragment.adapter.addItem(getResources().getDrawable(R.drawable.android), name, number); // => ERROR
        }
    }
}