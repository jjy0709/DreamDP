package com.example.dreamdp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ContactReviseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_revise);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit contact");

        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        String name = i.getStringExtra("name");
        String number = i.getStringExtra("number");
        final int[] pic_ = {i.getIntExtra("picture", 0)};
        int position = i.getIntExtra("position",0);

        ImageView imageView = (ImageView) findViewById(R.id.contact_revise_image);
        EditText nameV = (EditText) findViewById(R.id.contact_revise_name);
        EditText numberV = (EditText) findViewById(R.id.contact_revise_number);

        if(pic_[0] == 0){imageView.setImageResource(R.drawable.finn_circle);}
        else if(pic_[0] == 1){imageView.setImageResource(R.drawable.jake_circle);}
        else{imageView.setImageResource(R.drawable.bmo_circle);}

        nameV.setText(name);
        numberV.setText(number);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_pic_revise);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.picture, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                switch (position){
                    case 0:
                        imageView.setImageResource(R.drawable.finn_circle);
                        pic_[0] = 0;
                        break;
                    case 1:
                        imageView.setImageResource(R.drawable.jake_circle);
                        pic_[0] = 1;
                        break;
                    case 2:
                        imageView.setImageResource(R.drawable.bmo_circle);
                        pic_[0] = 2;
                        break;
                    }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = findViewById(R.id.save_button);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                if(nameV.getText().length() == 0 || numberV.getText().length() == 0){
                    Toast.makeText(ContactReviseActivity.this, "모든 영역을 채워주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String revise_name = nameV.getText().toString();
                String revise_number = numberV.getText().toString();

                Intent i1 = new Intent();
                i1.putExtra("name",revise_name);
                i1.putExtra("number",revise_number);
                i1.putExtra("picture", pic_[0]);
                i1.putExtra("position",position);
                setResult(RESULT_FIRST_USER, i1);
                finish();

            }
        });





    }
}