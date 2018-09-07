package com.example.youngk.communication.admin;

/**
 * Created by Eun_A on 2017-07-31.
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youngk.communication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.estimote.sdk.EstimoteSDK.getApplicationContext;

//import devs.mulham.horizontalcalendar.HorizontalCalendar;
//import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

/**
 * Created by YoungK on 2017-07-27.
 */

public class Tab3Fragment extends Fragment {

    private View v;
    private HorizontalCalendar horizontalCalendar;
    private String calendar_result=null;
    private String store_name="스타벅스정릉점";
    private ListView listView;
    DataAdpater adpater;
    private String[] calendar_split;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate( R.layout.fragment_tab3, container, false );

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 10);

        /** start before some month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -10);

        final Calendar defaultDate = Calendar.getInstance();
        defaultDate.add(Calendar.MONTH, 0);
        defaultDate.add(Calendar.DATE,0);

        horizontalCalendar = new HorizontalCalendar.Builder(v, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(5)
                .dayNameFormat("EEE")
                .dayNumberFormat("dd")
                .monthFormat("MMM")
                .showDayName(true)
                .showMonthName(true)
                //.defaultSelectedDate(defaultDate.getTime())
                .textColor(Color.LTGRAY, Color.WHITE)
                .selectedDateBackground(Color.TRANSPARENT)
                .build();

        /*horizontalCalendar= new HorizontalCalendar.Builder(getActivity(), R.id.calendarView);*/

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        listView = (ListView) v.findViewById(R.id.to_do_list);

        final ArrayList<CalendarList> alist= new ArrayList<CalendarList>();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                Toast.makeText(getActivity(), dateFormat.format(date) + " is selected!", Toast.LENGTH_SHORT).show();
                adpater = new DataAdpater(getActivity(), alist);

                adpater.clear();

                //database와 연결
                AdminCalendarDAO adminCalendarDAO = new AdminCalendarDAO();
                Log.d("결과 확확인22", dateFormat.format(date));
                try {
                    calendar_result = adminCalendarDAO.execute(dateFormat.format(date), store_name).get();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(calendar_result!=null) {
                    Log.d("calendar_result결과확인", calendar_result);
                }
                calendar_split = calendar_result.split("//");
                for(int i=0; i<calendar_split.length; i=i+2){
                    adpater.add(new CalendarList(getApplicationContext(),dateFormat.format(date),calendar_split[i],calendar_split[i+1]));
                }


                //adpater.add(new CalendarList(getApplicationContext(),dateFormat.format(date),"Test1","Test Contents..."));
                //adpater.add(new CalendarList(getApplicationContext(),dateFormat.format(date),"Test2","Test Contents..."));
                //adpater.add(new CalendarList(getApplicationContext(),dateFormat.format(date),"Test3","Test Contents..."));

                listView.setAdapter(adpater);
            }

        });

        return v;
    }


    private class DataAdpater extends ArrayAdapter<CalendarList> {

        private LayoutInflater mInflater;

        public DataAdpater(Context context, ArrayList<CalendarList> object) {
            super(context, 0, object);
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, View v, ViewGroup parent) {
            View view = null;
            if(v==null){
                view = mInflater.inflate(R.layout.event_item_day_view,null);
            }else{
                view=v;
            }
            final CalendarList c_data = this.getItem(position);
            if(c_data!=null){
                TextView title = (TextView)view.findViewById(R.id.item_title);
                TextView c_date = (TextView)view.findViewById(R.id.to_do_date);
                TextView contents = (TextView)view.findViewById(R.id.item_contents);

                title.setText(c_data.getCalendar_title());
                c_date.setText(c_data.getCalendar_date());
                contents.setText(c_data.getCalendar_contents());

            }
            return view;
        }
    }

    class CalendarList{
        private String calendar_date;
        private String calendar_title;
        private String calendar_contents;

        public CalendarList(Context context, String c_date, String c_title, String c_contents){
            calendar_date = c_date;
            calendar_title = c_title;
            calendar_contents = c_contents;
        }

        public String getCalendar_date(){
            return calendar_date;
        }
        public String getCalendar_title(){
            return calendar_title;
        }
        public String getCalendar_contents(){
            return calendar_contents;
        }
    }
}