package com.example.youngk.communication.admin;

/**
 * Created by Eun_A on 2017-07-31.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.youngk.communication.DAO.AdminUserListDAO;
import com.example.youngk.communication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YoungK on 2017-07-27.
 */

public class Tab1Fragment extends Fragment {
    private View v;
    private ListView listView;
    private UserListAdapter adapter;
    private List data = new ArrayList();
    private String result;
    private String[] users;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate( R.layout.fragment_tab1, container, false );
        adapter = new UserListAdapter();

        listView = (ListView) v.findViewById(R.id.user_list);
        listView.setAdapter(adapter);
        AdminUserListDAO adminUserListDAO = new AdminUserListDAO();
        try {
            result = adminUserListDAO.execute("").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        users = result.split("/");
        for(int i=0; i<users.length; i+=2) {
            if(users[i+1].equals("1")) {
                adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.staff), users[i], ContextCompat.getDrawable(getActivity(), android.R.drawable.presence_online));
            }
            else if(users[i+1].equals("0")){
                adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.staff), users[i], ContextCompat.getDrawable(getActivity(), android.R.drawable.presence_invisible));
            }
        }
        Log.d("리스트결과",""+result);
        return v;
    }
}
