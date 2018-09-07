package com.example.youngk.communication;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.youngk.communication.DAO.SalaryDetailDAO;
import com.example.youngk.communication.DAO.WageDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.example.youngk.communication.R.id.month;
import static com.example.youngk.communication.R.id.year;

/**
 * Created by Eun_A on 2017-07-20.
 */

public class SalaryDetail extends Fragment {
    private String salary_result = null,results,test;
    private String[] result;
    private Context mContext;
    private String[] dateList,wageDetail,wageDetail_getDB;
    private ArrayList<String> yearList;
    private ArrayList<String> monthList;
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private Spinner yearSpinner;
    private Spinner monthSpinner;
    private ArrayList searchList;

    String yearResult = null;
    String monthResult = null;
    String phone=null;
    int monthSalary=0;
    TextView salaryTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.salary_detail, container, false);
        listView = (ListView) view.findViewById(R.id.salaryList);

        //View header = inflater.inflate(R.layout.salary_header, null, false) ;
        //listView.addHeaderView(header);

        salary_result = getArguments().getString("salary_result");
        phone = getArguments().getString("phone");
        result = salary_result.split("-");
        Log.d("급여계산 결과값", "" + salary_result);

        //현재 날짜 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String getDate = dateFormat.format(date);
        Log.d("현재 날짜", getDate);
        String curSalary=null;


        for(int i=4; i<result.length; i=i+7){
            if((result[i]+"-"+result[i+1]).contains(getDate)){
                curSalary = result[i-1];
            }
        }


        if(curSalary==null){
            curSalary="0";
        }
        Log.d("이번달 누적 급여",curSalary);
        salaryTextView = (TextView) view.findViewById(R.id.currentSalaryText);


        yearList = new ArrayList<String>();
        monthList = new ArrayList<String>();
        yearList.add(0, "년");   //yearList.add(1, result[0]);
        monthList.add(0, "월");  //monthList.add(1, result[1]);
        Log.d("yearList 개수 확인", yearList.size()+"");

        //spinner에 들어갈 list를 추가하는 부분
        //중복된 값 제거하고 한 번씩만 추가
        int y_count=1;
        int m_count=1;
        for (int i = 4; i < result.length; i=i+7) {
            for(int k=0; k < yearList.size(); k++) {
                if (yearList.get(k).equals(result[i])) {
                    break;
                }else{
                    if(k==yearList.size()-1){
                        //마지막까지 동일한 값이 없다면 추가
                        yearList.add(y_count, result[i]);
                        y_count++;
                    }
                }
            }
            for(int k=0; k<monthList.size(); k++) {
                if(monthList.get(k).equals(result[i+1])) {
                    break;
                }else{
                    if(k==monthList.size()-1){
                        //마지막까지 동일한 값이 없다면 추가
                        monthList.add(m_count, result[i+1]);
                        m_count++;
                    }
                }
            }
        }
        searchList = new ArrayList();
        //년도 Spinner
        yearSpinner = (Spinner) view.findViewById(year);

        //spinner 동적 할당
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        // Log.d("yearSpinner 개수 확인",yearSpinner.getSelectedItem()+"");
        yearSpinner.setSelection(0);


        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,searchList);
                searchList.clear();
                arrayAdapter.notifyDataSetChanged();
                yearResult = printChecked(view, position);

                Log.d("y선택사항 확인 ",yearResult);

                int count=0;

                dateList = yearResult.split("-");
                int size=dateList[3].length();
                for(int i=0; i<dateList.length; i=i+5){
                    Log.d("확확인22",dateList[i+3].length()+" "+size);
                    if(dateList[i+3].length()<size){
                        for(int k=0; k<(size-dateList[i+3].length());k++) {
                            dateList[i+3] += "\t";
                        }
                    }else{
                        size=dateList[i+3].length();
                    }
                    Log.d("확확인334", dateList[i+3]+"/");
                    int hour=Integer.parseInt(dateList[i+4])/60;
                    int min= Integer.parseInt(dateList[i+4])%60;
                    String minTohour;
                    if(hour>0){
                        minTohour = hour+"시간 "+min+"분";
                    }else {
                        minTohour = min + "분";
                    }
                    searchList.add(count,dateList[i]+"-"+dateList[i+1]+"-"+dateList[i+2]+"\t\t\t\t\t\t"+dateList[i+3]+"\t\t\t\t\t\t\t\t\t\t\t"+minTohour);
                    if(!dateList[i+3].equals("0\t\t")) {
                        String temp;
                        temp = dateList[i+3].replaceAll("\\s","");
                        Log.d("짜증 : ", temp);
                        int x = Integer.parseInt(temp);
                        monthSalary += x;
                        Log.d("누적된급여 : ", String.valueOf(monthSalary));
                    }
                    count++;
                }


                //arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,searchList);
                listView.setAdapter(arrayAdapter);
                salaryTextView.setText(String.valueOf(monthSalary)+"원");
                monthSalary=0;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                searchList.clear();
            }
        });


        //월 Spinner
        monthSpinner = (Spinner) view.findViewById(month);
       /* ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.date_month, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);*/

        //spinner 동적 할당
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, monthList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setSelection(0);
        // searchList = new ArrayList();


        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,searchList);
                searchList.clear();
                //arrayAdapter.notifyDataSetChanged();
                listView.setAdapter(arrayAdapter);

                monthResult = printChecked(view, position);
                Log.d("m선택사항 확인 ",monthResult);

                int count=0;
                int size=dateList[3].length();
                dateList = monthResult.split("-");
                Log.d("확확인",dateList.length+""+size);
                for(int i=0; i<dateList.length; i=i+5){
                    Log.d("month 확확인22",dateList[i+3].length()+" "+size);
                    if(dateList[i+3].length()<size){
                        for(int k=0; k<(size-dateList[i+3].length());k++) {
                            dateList[i+3] += "\t";
                        }
                        Log.d("month 확확인33",dateList[i+3]+"/");
                    }else{
                        size=dateList[i+3].length();
                    }
                    Log.d("확확인33", dateList[i+3]+"/");
                    int hour=Integer.parseInt(dateList[i+4])/60;
                    int min= Integer.parseInt(dateList[i+4])%60;
                    String minTohour;
                    if(hour>0){
                        minTohour = hour+"시간 "+min+"분";
                    }else{
                        minTohour = min+"분";
                    }

                    searchList.add(count,dateList[i]+"-"+dateList[i+1]+"-"+dateList[i+2]+"\t\t\t\t\t\t"+dateList[i+3]+"\t\t\t\t\t\t\t\t\t\t\t"+minTohour);
                    if(!dateList[i+3].equals("0\t\t")) {
                        String temp;
                        temp = dateList[i+3].replaceAll("\\s","");
                        Log.d("짜증 : ", temp);
                        int x = Integer.parseInt(temp);
                        /*int x = Integer.parseInt(dateList[i + 3].replace("\\s",""));*/
                        monthSalary += x;
                        Log.d("누적된급여 : ", String.valueOf(monthSalary));
                    }
                    count++;

                }
                listView.setAdapter(arrayAdapter);
                salaryTextView.setText(String.valueOf(monthSalary)+"원");
                monthSalary=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                searchList.clear();
            }

        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id){
                String vo = (String) parent.getAdapter().getItem(position);
                test = vo.replaceAll("\\s","/");
                wageDetail=test.split("//////");
                Log.d("wageDetail",test+"");
                Log.d("wageDetail22",wageDetail[0]+"");

                WageDAO wdao = new WageDAO();
                try {
                    results = wdao.execute(wageDetail[0],wageDetail[1],"01034075075").get();
                    wageDetail_getDB=results.split("//");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(getActivity())
                        .title("급여 내역 ("+wageDetail[0]+")")
                        .content("일급       :"+wageDetail_getDB[1]+"원\n야간수당 :"+wageDetail_getDB[0]+"원\n총 일급    :"+wageDetail_getDB[2]+"원");

                MaterialDialog dialog = dialogBuilder.build();
                dialog.show();

                return false;
            }
        });

        return view;
    }

    public String printChecked(View v, int position) {
        String sendMsg, receiveMsg=null;

        String result = "";
        Log.d("값 확인", yearSpinner.getSelectedItemPosition()+"");

        if (yearSpinner.getSelectedItemPosition() != 0) {
            Log.d("값 확인22", yearSpinner.getSelectedItemPosition()+"");
            result = (String) yearSpinner.getAdapter().getItem(yearSpinner.getSelectedItemPosition());
            result += "-";

        }
        if (monthSpinner.getSelectedItemPosition() != 0){

            result += (String) monthSpinner.getAdapter().getItem(monthSpinner.getSelectedItemPosition())+"-";
        }
        Log.d("result 값", result);


        String salary_result=null;
        SalaryDetailDAO detailDAO = new SalaryDetailDAO();
        try {

            salary_result = detailDAO.execute("01034075075",result).get();  //phobe수정
            if(salary_result==null){

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("확인", salary_result);

        return salary_result;
    }
}