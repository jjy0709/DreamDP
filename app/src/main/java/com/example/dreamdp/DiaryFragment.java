package com.example.dreamdp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DiaryFragment extends Fragment {

    float sum;
    int num;
    RatingBar ratingBar;
    TextView textView;
    View calendar;
    View list;
    DBHelper dbHelper;
    SQLiteDatabase db;

    private Context mContext;

    public DiaryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        calendar=  inflater.inflate(R.layout.fragment_diary, container, false);
        CalendarView calendarView = calendar.findViewById(R.id.diary_calendar);
        dbHelper = new DBHelper(mContext,"diary.db",null,1);
        db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM mytable;",null);
        sum = 0;
        num = 0;
        while(c.moveToNext()){
            float val = c.getFloat(2);
            sum += val;
            num++;
        }

        ratingBar = calendar.findViewById(R.id.monthly_average);
        ratingBar.setRating(sum/num);
        textView = calendar.findViewById(R.id.star_info);
        textView.setText(String.format("Average Rating = %.2f", sum/num));

        TextView textView2 = calendar.findViewById(R.id.content_view);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Cursor c1 = db.rawQuery("SELECT * from mytable order by date asc",null);
                String content = "";
                while(c1.moveToNext()){
                    int date = c1.getInt(1);
                    if(date == (year*10000 + (month + 1) * 100 + dayOfMonth)){
                        content = c1.getString(3);
                        break;
                    }
                }
                textView2.setText(content);
            }
        });

        textView2.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView2.getText().length() != 0) {
                    Intent i = new Intent(getContext(), NewDiaryActivity.class);
                    i.putExtra("request_code", 3);
                    i.putExtra("content", textView2.getText());
                    Cursor c2 = db.rawQuery("SELECT * from mytable order by date asc", null);
                    float rate = 0;
                    int date = 0;
                    int weather_id = 0;
                    while (c2.moveToNext()) {
                        String content1 = c2.getString(3);
                        if (content1.equals(textView2.getText())) {
                            rate = c2.getFloat(2);
                            date = c2.getInt(1);
                            weather_id = c2.getInt(4);
                            break;
                        }
                    }
                    i.putExtra("rate", rate);
                    i.putExtra("date",date);
                    i.putExtra("weather", weather_id);
                    startActivity(i);
                }
            }
        });

        return calendar;
    }

    public void whenDataSetChanged(float rating, int i) {
        if (i == 1) {
            sum += rating;
            num++;
            ratingBar.setRating(sum / num);
            textView.setText(String.format("Average Rating = %.2f", sum / num));
        }
        else{
            sum -= rating;
            num--;
            ratingBar.setRating(sum / num);
            textView.setText(String.format("Average Rating = %.2f", sum / num));
        }
    }

}