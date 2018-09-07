package com.example.youngk.communication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.youngk.communication.DAO.CalendaDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eun_A on 2017-05-29.
 */

public class ShowCalendar extends Fragment /*implements MainActivity.onKeyBackPressedListener*/ {
    private ListView listView;
    private List data = new ArrayList();
    private ArrayAdapter arrayAdapter;
    private Bundle bundle=new Bundle();
    private String result = null;
    private String[] fullResult=null;
    private String content = null;
    private String phone = null;
    private String count = null;
    private ToggleButton toggleButton;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("캘린더");
        View v = inflater.inflate(R.layout.show_calendar, container, false);
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,data);
        listView=(ListView)v.findViewById(R.id.dateListView);
        toggleButton = (ToggleButton)v.findViewById(R.id.toggle);
        CalendarView calendar = (CalendarView)v.findViewById(R.id.calendar);
        phone = getArguments().getString("phone");
        count = "0";
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    count = "1";
                    Toast.makeText(getActivity(),count,Toast.LENGTH_SHORT).show();
                }
                else{
                    count="0";
                    Toast.makeText(getActivity(),count,Toast.LENGTH_SHORT).show();
                }
            }
        });
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayofMonth){
                int realMonth = month+1;
                data.removeAll(data); //초기화
                bundle.clear(); //초기화

                String date = year+"-"+realMonth+"-"+dayofMonth;
                CalendaDAO cDAO = new CalendaDAO();
                try {
                    result = cDAO.execute(date,phone,count).get();
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.d("캘린더",""+result);
                if(count.equals("1")) {
                    fullResult = result.split("//");
                    for(int i=0; i<fullResult.length; i++)
                        addData(fullResult[i]);
                }
                else {
                    addData(result);
                }
                listView.setAdapter(arrayAdapter);

                bundle.putInt("year",year);
                bundle.putInt("month",realMonth);
                bundle.putInt("day",dayofMonth);
                //Toast.makeText(ShowCalendar.this,""+year+"/"+(month+1)+"/"+dayofMonth, Toast.LENGTH_LONG).show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fragment fragment = new DoList();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
            }
        });
        return v;
    }
    private void addData(String content){
        //임시 test data
        data.add(content);

    }
    /*@Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        ((MainActivity)activity).setOnKeyBackPressedListener(this);
    }
    @Override
    public void onBack() {
        MainActivity activity = (MainActivity)getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.onBackPressed();
    }*/


}