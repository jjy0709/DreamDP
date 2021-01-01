package com.example.dreamdp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

        ImageView imageView = (ImageView) findViewById(R.id.contact_image);
        TextView nameV = (TextView) findViewById(R.id.contact_name);
        TextView numberV = (TextView) findViewById(R.id.contact_number);

        imageView.setImageResource(R.drawable.android);
        nameV.setText("이름: "+ name);
        numberV.setText("번호: "+number);

        ImageButton button1 = (ImageButton) findViewById(R.id.call_button);
        ImageButton button2 = (ImageButton) findViewById(R.id.message_button);

        button1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                startActivity(i1);
            }
        });

        button2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i2 = new Intent(Intent.ACTION_SENDTO);
                Uri uri = Uri.parse("sms:" + number);
                i2.setData(uri);
                startActivity(i2);
            }
        });




    }
}
