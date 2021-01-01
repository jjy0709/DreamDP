package com.example.dreamdp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Contact_Revise extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__revise);
        Intent i = getIntent();


        String name = i.getStringExtra("name");
        String number = i.getStringExtra("number");
        int position = i.getIntExtra("position",0);

        ImageView imageView = (ImageView) findViewById(R.id.contact_revise_image);
        EditText nameV = (EditText) findViewById(R.id.contact_revise_name);
        EditText numberV = (EditText) findViewById(R.id.contact_revise_number);

        imageView.setImageResource(R.drawable.android);
        nameV.setText(name);
        numberV.setText(number);

        Button button = findViewById(R.id.save_button);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                String revise_name = nameV.getText().toString();
                String revise_number = numberV.getText().toString();
                Intent i1 = new Intent();
                i1.putExtra("name",revise_name);
                i1.putExtra("number",revise_number);
                i1.putExtra("position", position);
                setResult(RESULT_FIRST_USER, i1);
                finish();

            }
        });





    }
}