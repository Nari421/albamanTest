package com.example.youngk.communication.admin;

/**
 * Created by Eun_A on 2017-07-31.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youngk.communication.R;

import java.util.ArrayList;

/**
 * Created by YoungK on 2017-07-28.
 */

public class UserListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<UserList> list = new ArrayList<UserList>() ;

    // ListViewAdapter의 생성자
    public UserListAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return list.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.admin_user_list, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView profile = (ImageView) v.findViewById(R.id.profile) ;
        TextView username = (TextView) v.findViewById(R.id.username) ;
        ImageView status = (ImageView) v.findViewById(R.id.status) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        UserList userList = list.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        profile.setImageDrawable(userList.getProfile());
        username.setText(userList.getName());
        status.setImageDrawable(userList.getStatus());

        return v;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return list.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Drawable profile, String name, Drawable status) {
        UserList userList = new UserList();

        userList.setProfile(profile);
        userList.setName(name);
        userList.setStatus(status);

        list.add(userList);
    }
}



