package com.example.dreamdp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_image);

        Intent i = getIntent();

        String name = i.getStringExtra("name");
        String number = i.getStringExtra("number");

        System.out.println(name);


        ImageView imageView = (ImageView) findViewById(R.id.contact_image);
        TextView nameV = (TextView) findViewById(R.id.contact_name);
        TextView numberV = (TextView) findViewById(R.id.contact_number);

        imageView.setImageResource(R.drawable.android);
        nameV.setText("이름: "+ name);
        numberV.setText("번호: "+number);




    }
}
