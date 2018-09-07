package com.example.youngk.communication.admin;

/**
 * Created by Eun_A on 2017-07-28.
 */

import java.util.Date;

//import devs.mulham.horizontalcalendar.HorizontalCalendarView;

//import devs.mulham.horizontalcalendar.HorizontalCalendarView;

public abstract class HorizontalCalendarListener {

    public abstract void onDateSelected(Date date, int position);

    public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy){}

    public boolean onDateLongClicked(Date date, int position){
        return false;
    }

}