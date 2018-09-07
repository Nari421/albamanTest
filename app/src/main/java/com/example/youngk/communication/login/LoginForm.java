package com.example.youngk.communication.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.youngk.communication.DAO.LoginDAO;
import com.example.youngk.communication.MainActivity;
import com.example.youngk.communication.R;
import com.example.youngk.communication.admin.AdminMain;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by YoungK on 2017-06-06.
 */

public class LoginForm extends AppCompatActivity {
    EditText nameInput,phoneInput;
    CheckBox autoLogin;
    Button loginButton;
    Boolean loginChecked;
    SharedPreferences auto;
    SharedPreferences.Editor editor;
    private LoginDAO loginDAO;
    String result;
    String name,phone;
    private Intent intent;
    String autoName,autoPhone;

    private View layoutProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loginform);
        auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        editor = auto.edit();


        nameInput = (EditText) findViewById(R.id.name);
        phoneInput = (EditText) findViewById(R.id.phone);
        autoLogin = (CheckBox) findViewById(R.id.autologin);
        loginButton = (Button) findViewById(R.id.loginButton);
        autoName = auto.getString("autoName",null);
        autoPhone = auto.getString("autoPhone",null);

        layoutProgress = findViewById(R.id.layout_progress);

        if(auto.getBoolean("autoLogin",false)){
            nameInput.setText(auto.getString("autoName",null));
            phoneInput.setText(auto.getString("autoPhone",null));
            autoLogin.setChecked(true);
        }
         if(autoName != null && autoPhone != null){//문제가 발생 로그아웃이 안됌
             if(autoPhone.equals("01034075075")){
                 intent = new Intent(LoginForm.this, AdminMain.class);
                 intent.putExtra("name", name);
                 intent.putExtra("phone", phone);
                 startActivity(intent);
             }
             else {
                 Intent intent = new Intent(LoginForm.this, MainActivity.class);
                 intent.putExtra("name", autoName);
                 intent.putExtra("phone", autoPhone);
                 startActivity(intent);
                 Log.d("자동로그인", "" + autoName + "   " + autoPhone);
                 finish();
             }
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginDAO = new LoginDAO();
                name = nameInput.getText().toString();
                phone = phoneInput.getText().toString();
                uiProgress();
                Log.d("이름,핸드폰"+"",name+phone);
                try{
                    result = loginDAO.execute(name,phone).get();
                    Log.d("로그인 결과값",""+result);

                        if (result.equals("true")) {
                            editor.putString("autoName", name);
                            editor.putString("autoPhone", phone);
                            editor.putBoolean("autoLogin", true);
                            editor.commit();

                            if(phone.equals("01034075075")){
                                intent = new Intent(LoginForm.this, AdminMain.class);
                            }else{
                                intent = new Intent(LoginForm.this, MainActivity.class);
                            }


                            intent.putExtra("name", name);
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                        }
                    else{
                            uiDefault();
                        Toast.makeText(getApplicationContext(), "로그인 실패",  Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void uiDefault(){
        layoutProgress.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
    }

    private void uiProgress(){
        layoutProgress.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);
        showSoftInput(layoutProgress, false);
    }

    private void showSoftInput(final View view, final boolean show){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(show){
                    inputMethodManager.showSoftInput(view,0);
                }else{
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                }
            }
        },100);
    }
}
