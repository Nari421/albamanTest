package com.example.youngk.communication.Borad;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.youngk.communication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YoungK on 2017-06-11.
 */

public class BoradForm extends Fragment {
    private ArrayAdapter arrayAdapter; //ListView 쓰기위한
    private ListView listView;

    String board;//intent 넘겨받은값 저장변수
    String[] notice; //intent 내 string 스플릿 하기위한 변수
    private List data = new ArrayList();
    private Bundle bundle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.boardform, container, false);
        bundle = new Bundle();
        board = getArguments().getString("boardResult");
        notice = board.split("//");
        for(int i=0; i<notice.length; i++) {
            data.add(notice[i]);
        }

        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,data);
        listView = (ListView) v.findViewById(R.id.boardList);
        listView.setAdapter(arrayAdapter);

        return v;
    }

}
