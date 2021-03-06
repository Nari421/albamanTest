package com.example.youngk.communication.DAO;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by YoungK on 2017-07-14.
 */

public class CalendaDAO extends AsyncTask<String, Void, String> {
    String sendMsg, receiveMsg;
    String result;

    private Context mContext = null;

    @Override
    // doInBackground의 매개값이 문자열 배열인데요. 보낼 값이 여러개일 경우를 위해 배열로 합니다.
    protected String doInBackground(String... strings) {
        try {
            String str;
            URL url = new URL("http://117.17.142.207:80/dbtest/Calendar.jsp");//보낼 jsp 주소를 ""안에 작성합니다. (학교에서)
            //URL url = new URL("http://192.168.123.167:8081/dbtest/login.jsp");//보낼 jsp 주소를 ""안에 작성합니다. (학교에서)
            //URL url = new URL("http://192.168.0.42:8081/dbtest/login.jsp");//보낼 jsp 주소를 ""안에 작성합니다. (학교에서)
            // URL url = new URL("http://192.168.25.5:8081/dbtest/dbdb.jsp");//보낼 jsp 주소를 ""안에 작성합니다. (집에서)
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            // sendMsg = "id="+strings[0]+"&pwd="+strings[1];//보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
            /*sendMsg ="phone = "+getPhone()+"&date = "+getDate()+"&time = "+getTime();*/
            sendMsg ="date="+strings[0]+"&phone="+strings[1]+"&count="+strings[2];
            //회원가입처럼 보낼 데이터가 여러 개일 경우 &로 구분하여 작성합니다.
            Log.d("abcabc",sendMsg);
            osw.write(sendMsg);//OutputStreamWriter에 담아 전송합니다.
            osw.flush();
            //jsp와 통신이 정상적으로 되었을 때 할 코드들입니다.
            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                //jsp에서 보낸 값을 받겠죠?
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();

            } else {
                Log.i("통신 결과", conn.getResponseCode()+"에러");
                // 통신이 실패했을 때 실패한 이유를 알기 위해 로그를 찍습니다.
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //jsp로부터 받은 리턴 값입니다.
        return receiveMsg;
    }
}