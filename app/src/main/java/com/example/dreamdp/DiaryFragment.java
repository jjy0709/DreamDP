package com.example.dreamdp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

        View v =  inflater.inflate(R.layout.fragment_diary, container, false);
        CalendarView calendarView = v.findViewById(R.id.diary_calendar);
        DBHelper dbHelper = new DBHelper(mContext,"diary.db",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM mytable;",null);
        sum = 0;
        num = 0;
        while(c.moveToNext()){
            float val = c.getFloat(2);
            sum += val;
            num++;
        }

        ratingBar = v.findViewById(R.id.monthly_average);
        ratingBar.setRating(sum/num);
        textView = v.findViewById(R.id.star_info);
        textView.setText(String.format("Monthly Rating = %.2f", sum/num));

        return v;
    }

    public void whenDataSetChanged(float rating){
        sum += rating;
        num++;
        ratingBar.setRating(sum/num);
        textView.setText(String.format("Monthly Rating = %.2f", sum/num));
    }
}