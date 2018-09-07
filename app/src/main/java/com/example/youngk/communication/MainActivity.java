package com.example.youngk.communication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.example.youngk.communication.DAO.BoardDAO;
import com.example.youngk.communication.DAO.SalaryDAO;
import com.example.youngk.communication.DAO.TimeDAO;
import com.example.youngk.communication.login.LoginForm;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.example.youngk.communication.MainActivity.AlbaFragment.LIST_NUMBER;


public class MainActivity extends AppCompatActivity {
    private final long FINISH_INTERVAL_TIME = 2000;//이전 키 2번 눌렀을 때 종료
    private long backPressedTime = 0; //이전 키 2번

    private DrawerLayout mDrawerLayout; //drawLayout 위한 변수
    private ListView mDrawerList;   //drawLayout 위한 변수
    private ActionBarDrawerToggle mDrawerToggle;   //drawLayout 위한 변수
    private CharSequence mDrawTitle,mTitle;
    private String[] mAlbaTitle;
    private Toolbar toolbar;
    public  String phone,name;//loginform에서 전달한 값 저장
    private String boardResult; //공지사항 받을 변수;
    public Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = getIntent();
        name=intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        mTitle = mDrawTitle = getTitle();
        mAlbaTitle = getResources().getStringArray(R.array.alba_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mAlbaTitle));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        /*getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);*/
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME| ActionBar.DISPLAY_HOME_AS_UP);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.app_name,R.string.app_name){

            public void onDrawClosed(View v){
                super.onDrawerClosed(v);
            }
            public void onDrawOpended(View v){
                super.onDrawerOpened(v);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            System.out.println("출력출력");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int positon, long id){
            selectItem(positon);
        }
    }

    private void selectItem(int position){
        Fragment fragment=null;
        Bundle args = new Bundle();
        if(position == 0) {
             fragment = new AlbaFragment();
            args.putInt(LIST_NUMBER, position);
            fragment.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            setTitle(mAlbaTitle[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
        else if(position ==1){ //급여계산
            String salary_result = null;
            fragment = new com.example.youngk.communication.SalaryDetail();
            SalaryDAO salaryDAO = new SalaryDAO();
            try {
                salary_result = salaryDAO.execute("01034075075").get(); //수정해야될 부분
                Log.d("급여계산? :",""+salary_result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            args.putString("phone",phone);
            args.putString("salary_result",salary_result);
            args.putInt(LIST_NUMBER, position);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            setTitle(mAlbaTitle[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
        else if(position == 2){ //다이어리
            fragment = new ShowCalendar();
            args.putString("phone",phone);
            args.putInt(LIST_NUMBER, position);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            setTitle(mAlbaTitle[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
        else if(position == 3){ //채팅

            fragment = new StartChatting();
            args.putString("name",name);
            args.putInt(LIST_NUMBER, position);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            setTitle(mAlbaTitle[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
        else if(position == 4){
            SharedPreferences auto = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = auto.edit();
            //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
            editor.clear();
            editor.commit();
            Toast.makeText(this, "로그아웃.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginForm.class);
            startActivity(intent);
            finish();
        }
    }

    public static class AlbaFragment extends Fragment {
        public static final String LIST_NUMBER = "planet_number";
        private String result; //jsp로부터 결과값 받기 위한 변수
        private String resultValue; //출근시간 받아온 변수
        private BeaconManager beaconManager;
        private Region region;
        private BeaconConnector beaconConnector;
        private Button intime,outtime,board;
        private TextView s_time,e_time,boardText,boardText1;

        private BluetoothAdapter adapter;
        private String refreshedToken;
        private String phone;//loginform에서 전달한 값 저장
        private Bundle bundle;
        private String boardResult; //공지사항 받을 변수;
        private String timeResult;
        private String[] startTime,endTime; //메인에 시간넣을 배열 0=날짜 1=시간
        private String[] notice,time;//메인에 띄울 공지
        private List data = new ArrayList();
        private  TextView noticeDate;
        private  TextView noticeDate2;

        public AlbaFragment() {
            // Empty constructor required for fragment subclasses
        }
//        @Override
//        public void onResume(){
//            super.onResume();
//            SystemRequirementsChecker.checkWithDefaultDialogs(getActivity());
//            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//                @Override
//                public void onServiceReady() { //서비스가 시작되면 처음 호출된다.
//                    beaconManager.startRanging(region);
//                }
//            });
//        }
//        @Override
//        public void onPause() {
//            beaconManager.stopRanging(region);
//            super.onPause();
//        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        getActivity().setTitle("Home");
        int i = getArguments().getInt(LIST_NUMBER);
        bundle = new Bundle();
        String alba = getResources().getStringArray(R.array.alba_array)[i];
        adapter = BluetoothAdapter.getDefaultAdapter();
        beaconConnector = new BeaconConnector();
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Refreshed token: ", "" + refreshedToken);
        intime = (Button) v.findViewById(R.id.intime);
        outtime = (Button) v.findViewById(R.id.outtime);
        board = (Button) v.findViewById(R.id.board);
        s_time = (TextView) v.findViewById(R.id.s_time);
        e_time = (TextView) v.findViewById(R.id.e_time);
        boardText = (TextView) v.findViewById(R.id.boardText);
        boardText1 = (TextView) v.findViewById(R.id.boardText1);
        noticeDate=(TextView)v.findViewById(R.id.notice_date1);
        noticeDate2=(TextView)v.findViewById(R.id.notice_date2);

        BoardDAO bdao = new BoardDAO();
        try {
            boardResult = bdao.execute("board").get();
            Log.d("공지사항 넘겨받은 값:",""+boardResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] notice_str;
        notice = boardResult.split("//");
        notice_str=notice[0].split("  ");
        noticeDate.setText(notice_str[0]);
        boardText.setText(notice_str[1]);
        notice_str = notice[1].split("  ");
        noticeDate2.setText(notice_str[0]);
        boardText1.setText(notice_str[1]);

        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(getActivity())
                        .title("전체 공지사항")
                        .positiveText("종료");



                //AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                //alertBuilder.setTitle("전체 공지사항");

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
                String text="";
                for(int i=0; i<notice.length; i++){
                    text +=notice[i]+"\n\n";
                }
                dialogBuilder.content(text);
                MaterialDialog dialog = dialogBuilder.build();
                //dialog.setContent();
                dialog.show();
                /*alertBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertBuilder.show();*/
            }
        });

        //출근시간, 퇴근시간 보이기
        TimeDAO tdao = new TimeDAO();
        phone=getPhone();
        Log.d("나리나리:",""+phone);
        try {
            timeResult = tdao.execute(phone).get();

            Log.d("출퇴근 넘겨받은 값:",""+timeResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
        time=timeResult.split("//");
        if(time[0].equals("정보 없음")){

        }else{
            Date date = new Date();
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            s_time.setText(time[0]);
            resultValue=df.format(date)+" "+time[0];
            e_time.setText(time[1]);
        }


//        beaconManager = new BeaconManager(getActivity());
//        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
//            @Override
//            public void onBeaconsDiscovered(Region region, List<Beacon> list) { //여기 함수는 계속해서 오버라이드된다. 그러므로 계속 반복!!!
//                if (!list.isEmpty()) {
//                    Beacon nearestBeacon = list.get(0);
//                    Log.d("Airport", "Nearest places: " + nearestBeacon.getRssi());
//                    if (nearestBeacon.getRssi() > -80) {
//                        if (adapter.getState() == BluetoothAdapter.STATE_TURNING_ON ||
//                                adapter.getState() == BluetoothAdapter.STATE_ON) {
//                            intime.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    CustomTask task = new CustomTask();
//                                    String count = "0";
//                                    startTime =getDate().split(" ");
//                                    Log.d("출근날짜,출근시간:",""+startTime[0]+" "+startTime[1]);
//
//                                    s_time.setText(startTime[1]);
//                                    phone = getPhone();
//
//                                    Log.d("핸드폰",""+phone);
//                                    try {
//                                        result = task.execute(refreshedToken,phone,startTime[0],startTime[1],count).get(); //jsp에서 out.print
//                                        Log.i("결과값", "" + result);
//                                        resultValue = result;
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    adapter.disable();
//                                }
//                            });
//                            outtime.setOnClickListener(new View.OnClickListener() { //퇴근버튼
//                                @Override
//                                public void onClick(View v) {
//                                    CustomTask task = new CustomTask();
//                                    String count = resultValue;
//                                    endTime =getDate().split(" ");
//                                    Log.d("퇴근날짜,퇴근시간:",endTime[0]+" "+endTime[1]);
//                                    e_time.setText(endTime[1]);
//                                    phone = getPhone();
//                                    Log.d("핸드폰",""+phone);
//                                    try {
//                                        result = task.execute(refreshedToken,phone,endTime[0],endTime[1],count).get(); //jsp에서 out.print
//                                        Log.i("결과값", "" + result);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    adapter.disable();
//                                }
//                            });
//                        }
//                    }
//                    else {
//                        Toast.makeText(getActivity(), "매장안에서 실행시켜주세요.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
        intime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTask task = new CustomTask();
                String count = "0";
                startTime =getDate().split(" ");
                Log.d("출근날짜,출근시간:",""+startTime[0]+" "+startTime[1]);

                s_time.setText(startTime[1]);
                phone = getPhone();

                Log.d("핸드폰",""+phone);
                try {
                    result = task.execute(refreshedToken,phone,startTime[0],startTime[1],count).get(); //jsp에서 out.print
                    Log.i("결과값", "" + result);
                    resultValue = result;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.disable();
            }
        });
        outtime.setOnClickListener(new View.OnClickListener() { //퇴근버튼
            @Override
            public void onClick(View v) {
                CustomTask task = new CustomTask();
                String count = resultValue;
                endTime =getDate().split(" ");
                Log.d("퇴근날짜,퇴근시간:",endTime[0]+" "+endTime[1]+" "+resultValue);
                e_time.setText(endTime[1]);
                phone = getPhone();
                Log.d("핸드폰",""+phone);
                try {
                    result = task.execute(refreshedToken,phone,endTime[0],endTime[1],count).get(); //jsp에서 out.print
                    Log.i("결과값", "" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //adapter.disable();
            }
        });
        region = new Region("ranged region", UUID.fromString("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"), 40001, 10616);

        getActivity().setTitle(alba);
        return v;
    }
        public String getPhone(){
            TelephonyManager telephone = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            String phoneNumber = telephone.getLine1Number().substring(3);
            Log.d("고은아 핸드폰",phoneNumber);
            return "0"+phoneNumber;
        }
        public String getDate(){ //출퇴근 날짜시간 출력 공백자르기는 \\s
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formatDate = simpleDateFormat.format(date);
            return formatDate;
        }
}

    public interface onKeyBackPressedListener{
        public void onBack();
    }
    private onKeyBackPressedListener monKeyBackPressedListener;
    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener){
        monKeyBackPressedListener = listener;
    }
     @Override
    public void onBackPressed(){ //백버튼 막기
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
        }
        else

        {
            backPressedTime = tempTime;
            MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this)
                    .title("종료")
                    .content("정말로 종료하시겠습니까?")
                    .negativeText("yes")
                    .positiveText("no")
                    .cancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //Toast.makeText(getApplicationContext(), "onCancle", Toast.LENGTH_SHORT).show();
                            MainActivity.super.onBackPressed();
                        }
                    });

            MaterialDialog dialog = dialogBuilder.build();
            dialog.show();
        }
    }

}



