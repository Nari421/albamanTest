package com.example.youngk.communication;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by YoungK on 2017-04-26.
 */

public class BeaconConnector extends Application {
    private BeaconManager beaconManager;
    @Override
    public void onCreate(){
        super.onCreate();
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region("monitored region", UUID.fromString("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"),40001,10616));
            }
        });
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            CustomTask customTask = new CustomTask();
            @Override
            public void onEnteredRegion(Region region, List< Beacon > list) { //접속했을 때 한번만 인식되게 할려면 여기다가 번호,날짜,시간을 받아야 될 것 같음
               // Log.d("test1:",""+customTask.getPhone());
                Log.d("test1입장:",""+customTask.getDate());
                Log.d("test1입장:",""+customTask.getTime());
                showNotification("들어옴", "비콘 연결됨" + list.get(0).getRssi());

            }

            @Override
            public void onExitedRegion(Region region) {
                Log.d("test1나감:",""+customTask.getTime());
                showNotification("나감","비콘연결끊김");
            }
        });



    }
    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title) .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
    public String getMyphone(){
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phone = telephonyManager.getLine1Number();
        return phone;

    }

    public String currentDate(){ //날짜출력

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String dateformat = dateFormat.format(new Date());
        return dateformat;
    }
    public String currentTime(){ //시간 출력
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss",Locale.KOREA);
        String timeformat = timeFormat.format(new Date());
        return timeformat;
    }
}
