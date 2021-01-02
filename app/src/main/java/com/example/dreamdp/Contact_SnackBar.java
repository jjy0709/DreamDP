package com.example.dreamdp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.SharedPreferences;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static com.example.dreamdp.R.id.contacts_list;

public class Contact_SnackBar extends Activity {

    EditText name;
    EditText number;
    Button button1;
    int pic_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__snack_bar);

        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        button1 = findViewById(R.id.save);

        Spinner spinner = (Spinner)findViewById(R.id.spinner_pic);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.picture, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        pic_ = 0;
                        break;
                    case 1:
                        pic_ = 1;
                        break;
                    case 2:
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