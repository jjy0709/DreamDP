package com.example.dreamdp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewDiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diary);
        Button button1 = findViewById(R.id.date_button);
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(now);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy년 MM월 dd일");
        button1.setText(format1.format(date));
        Button saveButton = findViewById(R.id.save_diary);

        button1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DatePickerDialog dialog = new DatePickerDialog(NewDiaryActivity.this, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       button1.setText(year + "년" + (month + 1) + "월" + dayOfMonth + "일");
                   }
               }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

               dialog.show();
           }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // stub
                finish();
            }
        });
    }
}