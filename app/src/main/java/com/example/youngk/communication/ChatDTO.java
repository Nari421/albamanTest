package com.example.youngk.communication;

/**
 * Created by YoungK on 2017-05-15.
 */


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kkb on 2017-02-24.
 */

public class ChatDTO {
    private String user;
    private String id;
    private String content;
    private String date;

    public ChatDTO(){}

    public ChatDTO(String id, String user, String content) {
        this.user = user;
        this.content = content;
        this.id = id;


        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.KOREA);
        this.date = df.format(date);
        Log.d("currentDay", this.date);

    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }


}
