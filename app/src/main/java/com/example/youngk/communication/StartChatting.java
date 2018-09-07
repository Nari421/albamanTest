package com.example.youngk.communication;

import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.youngk.communication.Service.BackService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by YoungK on 2017-05-15.
 */

//firebase에서 채팅 정보 저장 및 불러오기
public class StartChatting extends Fragment implements View.OnClickListener, ChildEventListener {
    ListView listView;  //메세지를 보여줄 리스트뷰
    EditText content;   //메세지를 쓸 EditText
    Button send;        //메세지를 보낼 버튼
    CustomAdapter customAdapter;
    LinearLayout layout;
    int type=0;
    String test_date = null;
    String hour=null;
    String name=null;
    String phone=null;
    MyAdapter adapter;
    Bundle bundle;
    ArrayList<ChatDTO> items;   //메세지를 담을 ArrayList
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String userName;    //채팅방 닉네임
    NotificationManager notificationManager;
    BackService backService = new BackService();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("커뮤니티");
        notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        View v = inflater.inflate(R.layout.startchatting, container, false);
        bundle = new Bundle();
        name = getArguments().getString("name");
        phone = getArguments().getString("phone");
        customAdapter = new CustomAdapter(name);
        listView = (ListView) v.findViewById(R.id.list_view);
        layout = (LinearLayout) v.findViewById(R.id.layout);
        content = (EditText) v.findViewById(R.id.content);
        send = (Button) v.findViewById(R.id.send);
        send.setOnClickListener(this);


        items = new ArrayList<ChatDTO>();
        //adapter = new MyAdapter(this, items);

        listView.setAdapter(customAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("room").addChildEventListener(this);

        userName = getPhone();
        //userName = phone;
        Log.d("userName", userName);

        //채팅 데이터 불러오기
        getChattingData();

        return v;
    }

    //채팅 데이터를 불러오는 함수
    private void getChattingData(){

        databaseReference.child("room").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("data123", dataSnapshot.toString());
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                for(Map.Entry<String, Object> entry : map.entrySet()){

                    Map data = (Map)entry.getValue();
                    //날짜 순으로 정렬하기
                    if(getPhone().equals((String)data.get("user"))){
                        //내가 보낸 메세지
                        Log.d("user22 user",(String)data.get("user"));
                        Log.d("user2233 content",(String)data.get("content"));
                        Log.d("user44 date",(String)data.get("date"));
                        type=1;
                    }else{
                        //남이 보낸 메세지
                        Log.d("user122 user",(String)data.get("user"));
                        Log.d("user33 content",(String)data.get("content"));
                        Log.d("user44 date",(String)data.get("date"));
                        // Log.d("user445",data.get("date").toString());
                        type=0;

                    }
                    Log.d("testtesttest",""+data.get("date").toString().length());
                    ChatDTO chatting = new ChatDTO((String)data.get("id"),(String)data.get("user"),(String)data.get("content"));

                }


                databaseReference.removeEventListener(this);
                Log.d("user1234 content", String.valueOf(customAdapter.getCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int talk_type;
        Log.d("아이디 출력 : ",name);
        ChatDTO chatting = new ChatDTO(name, userName, content.getText().toString());

        databaseReference.child("room").push().setValue(chatting);
        //items.add(chatting);
        if(chatting.getUser().equals(getPhone())){
            talk_type=1;
        }else{
            talk_type=0;
        }

        refresh(chatting.getContent(),talk_type);
        content.setText("");

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        int user_type;
        ChatDTO chatting = dataSnapshot.getValue(ChatDTO.class);

        //String talk_date = data.get("date").toString().substring(0,13);
        //data.get
        Log.d("온차일드",""+getPhone()+"sss"+chatting.getUser());

        if(chatting.getUser().equals(getPhone())){
            user_type=1;
        }else{
            user_type=0;
        }

        if(!chatting.getDate().substring(0,13).equals(test_date)){
            test_date=chatting.getDate().substring(0,13);
            customAdapter.add("","",chatting.getDate().substring(0,13),2);
            // Log.d("chatting_hour_2",chatting.getDate().substring(14));

        }
        customAdapter.add(chatting.getId(),chatting.getDate().substring(14),chatting.getContent(),user_type);
        customAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
    public String getPhone(){
        TelephonyManager telephone = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = telephone.getLine1Number();

        return phoneNumber;
    }
    public String getDate(){    //현재날짜
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.KOREA);

        String currentDate = CurDateFormat.format(date);
        Log.d("Date_now", currentDate);

        return currentDate;
    }
    private void refresh (String inputValue, int talk_type) {
        //customAdapter.add(inputValue , talk_type);
        customAdapter.notifyDataSetChanged();
    }


}
class Ascending implements Comparable<Date>{

    @Override
    public int compareTo(Date o) {
        return 0;
    }
}

