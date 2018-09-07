package com.example.youngk.communication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Eun_A on 2017-05-29.
 */

public class DoList extends Fragment {
    private TextView date;
    Bundle bundle;
    int year, month, day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dolist, container, false);
        bundle=getArguments();
        year=bundle.getInt("year");
        month=bundle.getInt("month");
        day=bundle.getInt("day");

        date=(TextView)v.findViewById(R.id.calendarDate);
        date.setText(year+"년 "+month+"월 "+day+"일");
        return v;
    }
   /* protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dolist);

        bundle=getIntent().getExtras();
        year=bundle.getInt("year");
        month=bundle.getInt("month");
        day=bundle.getInt("day");

        date=(TextView)findViewById(R.id.calendarDate);
        date.setText(year+"년 "+month+"월 "+day+"일");

    }*/
}
