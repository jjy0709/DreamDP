package com.example.dreamdp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.dreamdp.MainActivity;

public class NewDiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diary);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("New diary");

        actionBar.setDisplayHomeAsUpEnabled(true);

        Button dateButton = findViewById(R.id.date_button);
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(now);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy년 MM월 dd일");
        dateButton.setText(format1.format(date));
        Button saveButton = findViewById(R.id.save_diary);
        RatingBar ratingBar = findViewById(R.id.daily_rating);
        EditText editText = findViewById(R.id.daily_comment);
        RadioGroup radioGroup = findViewById(R.id.weather_button);
        final int[] date2num = new int[1];
        date2num[0] = calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH)+1) * 100 + calendar.get(Calendar.DATE);

        dateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DatePickerDialog dialog = new DatePickerDialog(NewDiaryActivity.this, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       dateButton.setText(year + "년" + (month + 1) + "월" + dayOfMonth + "일");
                       date2num[0] = year*10000 + (month+1) * 100 + dayOfMonth;
                   }
               }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

               dialog.show();
           }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // stub
                if(radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(NewDiaryActivity.this,"날씨를 선택해 주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(editText.getText().length() == 0){
                    Toast.makeText(NewDiaryActivity.this,"오늘의 한 줄을 입력해 주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(ratingBar.getRating() == 0){
                    Toast.makeText(NewDiaryActivity.this,"오늘을 평가해 주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent();
                i.putExtra("date",date2num[0]);
                i.putExtra("weather",radioGroup.getCheckedRadioButtonId());
                i.putExtra("comment",editText.getText().toString());
                i.putExtra("rating",ratingBar.getRating());
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }
}