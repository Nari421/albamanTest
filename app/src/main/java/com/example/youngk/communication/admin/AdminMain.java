package com.example.youngk.communication.admin;

/**
 * Created by Eun_A on 2017-07-31.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.youngk.communication.R;
import com.example.youngk.communication.login.LoginForm;

/**
 * Created by YoungK on 2017-07-27.
 */

public class AdminMain extends AppCompatActivity {
    private MainTabViewPagerAdapter mainTabViewPagerAdapter;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);

        mainTabViewPagerAdapter = new MainTabViewPagerAdapter(
                this,
                (TabHost)findViewById( android.R.id.tabhost ),
                (ViewPager)findViewById( R.id.pager ) );
        mainTabViewPagerAdapter.selectTab( MainTabsConfig.TABINDEX.FIRST );
        /*floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();
                Toast.makeText(getApplicationContext(), "로그아웃.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminMain.this, LoginForm.class);
                startActivity(intent);
                finish();
            }
        });*/
    }
}
