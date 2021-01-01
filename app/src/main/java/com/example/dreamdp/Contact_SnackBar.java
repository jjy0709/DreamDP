package com.example.dreamdp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__snack_bar);

        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        button1 = findViewById(R.id.save);

        button1.setOnClickListener(myClickListener);
    }

    final View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                JSONObject jo = new JSONObject();
                jo.put("name", name.getText().toString());
                jo.put("number", number.getText().toString());
                String string = jo.toString();


                Intent i1 = new Intent();
                i1.putExtra("name",name.getText().toString());
                i1.putExtra("number",number.getText().toString());

                setResult(Activity.RESULT_OK, i1);


            }
            catch (JSONException e){
                e.printStackTrace();
            }

            finish();

        }
    };
}