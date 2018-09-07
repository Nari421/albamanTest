package com.example.youngk.communication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eun_A on 2017-05-21.
 */

public class CustomAdapter extends BaseAdapter{
    Intent intent;
    String usrName;
    public class ListContents{
        String id;
        String hour;
        String msg;
        int type;
        ListContents(String _id, String _hour, String _msg,int _type)
        {
            this.hour = _hour;
            this.msg = _msg;
            this.type = _type;
            this.id=_id;
        }
    }

    private ArrayList<ListContents> m_List;
    public CustomAdapter(String name) {
        m_List = new ArrayList<ListContents>();
        usrName = name;
        Log.d("닉네임...",usrName);
    }
    // 외부에서 아이템 추가 요청 시 사용
    public void add(String id ,String _hour, String _msg,int _type) {
        m_List.add(new ListContents(id, _hour,_msg,_type));
        //Log.d("chatting_Test11", m_List.get(0).msg);
        /// Log.d("chatting_Test12", String.valueOf(m_List.size()));
    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        m_List.remove(_position);
    }
    @Override
    public int getCount() {
        return m_List.size();
    }

    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        Log.d("chatting_Test123", String.valueOf(m_List.size()));

        // LoginForm login = new LoginForm();
        //login.getName();
        // Log.d("아이디 이름 : ", login.getName());

        TextView        text    = null;
        TextView        l_hour = null;
        TextView        r_hour = null;
        TextView        userId = null;
        TextView        l_blank=null;
        TextView        r_blank=null;
        CustomHolder    holder  = null;
        LinearLayout    layout  = null;
        LinearLayout    usrLayout = null;
        View            viewRight = null;
        View            viewLeft = null;


        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_chatitem, parent, false);

            layout    = (LinearLayout) convertView.findViewById(R.id.layout);
            usrLayout = (LinearLayout) convertView.findViewById(R.id.layout2);
            text    = (TextView) convertView.findViewById(R.id.text);
            l_hour = (TextView)convertView.findViewById(R.id.l_hour);
            r_hour =(TextView)convertView.findViewById(R.id.r_hour);
            userId = (TextView)convertView.findViewById(R.id.userId);
            l_blank = (TextView)convertView.findViewById(R.id.l_blank);
            r_blank = (TextView)convertView.findViewById(R.id.r_blank);
            viewRight    = (View) convertView.findViewById(R.id.imageViewright);
            viewLeft    = (View) convertView.findViewById(R.id.imageViewleft);
            //user_name = (TextView)convertView.findViewById(R.id.user);


            // 홀더 생성 및 Tag로 등록
            holder = new CustomHolder();
            holder.m_TextView   = text;
            holder.user = userId;
            holder.left_blank = l_blank;
            holder.right_blank = r_blank;
            holder.left_hour=l_hour;
            holder.right_hour = r_hour;
            holder.layout = layout;
            holder.usrLayout = usrLayout;
            holder.viewRight = viewRight;
            holder.viewLeft = viewLeft;
            convertView.setTag(holder);
        }
        else {
            holder  = (CustomHolder) convertView.getTag();
            text    = holder.m_TextView;
            userId = holder.user;
            l_blank = holder.left_blank;
            r_blank = holder.right_blank;
            l_hour = holder.left_hour;
            r_hour = holder.right_hour;
            layout  = holder.layout;
            usrLayout = holder.usrLayout;
            viewRight = holder.viewRight;
            viewLeft = holder.viewLeft;
        }
        Log.d("select",m_List.get(position).msg);
        // Text 등록
        text.setText(m_List.get(position).msg);
        l_hour.setBackgroundColor(Color.WHITE);
        l_hour.setText("");
        r_hour.setBackgroundColor(Color.WHITE);
        r_hour.setText("");
        l_blank.setText("       ");
        r_blank.setText("       ");
        if((m_List.get(position).id)==null){
            Log.d("null 여부확인", "null입니다.");
        }else{
            Log.d("null 여부확인", m_List.get(position).id);
        }
        userId.setText(m_List.get(position).id);
        Log.d("select123",m_List.get(position).msg);

        if( m_List.get(position).type == 0 ) {
            r_hour.setText(m_List.get(position).hour);
            r_hour.setBackgroundColor(Color.LTGRAY);
            text.setBackgroundResource(R.drawable.inbox2);
            layout.setGravity(Gravity.LEFT);
            usrLayout.setGravity(Gravity.LEFT);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.GONE);
        }else if(m_List.get(position).type == 1){
            l_hour.setText(m_List.get(position).hour);
            l_hour.setBackgroundColor(Color.LTGRAY);
            text.setBackgroundResource(R.drawable.outbox2);
            layout.setGravity(Gravity.RIGHT);
            usrLayout.setGravity(Gravity.RIGHT);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.GONE);

        }else if(m_List.get(position).type == 2){
            text.setBackgroundResource(R.drawable.datebg);
            layout.setGravity(Gravity.CENTER);
            userId.setText("");
            viewRight.setVisibility(View.VISIBLE);
            viewLeft.setVisibility(View.VISIBLE);
        }

      /*  // 리스트 아이템을 터치 했을 때 이벤트 발생
        convertView.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 클릭 : "+m_List.get(pos), Toast.LENGTH_SHORT).show();
            }
        });

        // 리스트 아이템을 길게 터치 했을때 이벤트 발생
        convertView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 롱 클릭 : "+m_List.get(pos), Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/

        return convertView;
    }

    private class CustomHolder {
        TextView m_TextView;
        TextView left_hour;
        TextView right_hour;
        TextView user;
        TextView left_blank;
        TextView right_blank;
        LinearLayout layout;
        LinearLayout usrLayout;
        View viewRight;
        View viewLeft;
    }

}