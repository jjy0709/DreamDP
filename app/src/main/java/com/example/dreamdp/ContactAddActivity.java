package com.example.dreamdp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactAddActivity extends AppCompatActivity {

    EditText name;
    EditText number;
    Button button1;
    int pic_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("New contact");

        actionBar.setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        button1 = findViewById(R.id.save);

        Spinner spinner = (Spinner)findViewById(R.id.spinner_pic);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.picture, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        ImageView profileView = (ImageView) findViewById(R.id.new_profile);
        profileView.setImageResource(R.drawable.finn_circle);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        profileView.setImageResource(R.drawable.finn_circle);
                        pic_ = 0;
                        break;
                    case 1:
                        profileView.setImageResource(R.drawable.jake_circle);
                        pic_ = 1;
                        break;
                    case 2:
                        profileView.setImageResource(R.drawable.bmo_circle);
                        pic_ = 2;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pic_ = 0;
            }
        });


        button1.setOnClickListener(myClickListener);
    }

    final View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if(name.getText().length() == 0 || number.getText().length() == 0){
                    Toast.makeText(ContactAddActivity.this, "모든 영역을 채워주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jo = new JSONObject();
                jo.put("name", name.getText().toString());
                jo.put("number", number.getText().toString());
                jo.put("picture",pic_);
                String string = jo.toString();


                Intent i1 = new Intent();
                i1.putExtra("name",name.getText().toString());
                i1.putExtra("number",number.getText().toString());
                i1.putExtra("picture",pic_);

                setResult(Activity.RESULT_OK, i1);
            }
            catch (JSONException e){
                e.printStackTrace();
            }

            finish();

        }
    };
}