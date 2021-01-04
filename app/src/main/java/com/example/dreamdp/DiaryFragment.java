package com.example.dreamdp;

import android.app.DatePickerDialog;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DiaryFragment extends Fragment {

    public DiaryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v =  inflater.inflate(R.layout.fragment_diary, container, false);
        Button button1 = v.findViewById(R.id.date_button);
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(now);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy년 MM월 dd일");
        button1.setText(format1.format(date));

        button1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        button1.setText(year + "년" + (month + 1) + "월" + dayOfMonth + "일");
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                dialog.show();
            }

        });

//        ImageView sunny = v.findViewById(R.id.sunny);
//        ImageView overcast = v.findViewById(R.id.overcast);
//        ImageView cloudy = v.findViewById(R.id.cloudy);
//        ImageView rainy = v.findViewById(R.id.rainy);
//        ImageView snowy = v.findViewById(R.id.snowy);



        return v;
    }
}