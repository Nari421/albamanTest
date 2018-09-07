package com.example.youngk.communication.admin;

/**
 * Created by Eun_A on 2017-07-31.
 */

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.youngk.communication.DAO.BoardDAO;
import com.example.youngk.communication.DAO.DeleteDAO;
import com.example.youngk.communication.DAO.NoticeDAO;
import com.example.youngk.communication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by YoungK on 2017-07-27.
 */

public class Tab2Fragment extends Fragment {
    private ListView noticelistview;
    private EditText inputText;
    private Button btn;
    private ArrayAdapter arrayAdapter;
    private String Allnotice;
    private String[] notice,del;
    private List data = new ArrayList();
    private String result;

    private View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_tab2,container,false);

        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,data);

        noticelistview = (ListView)v.findViewById(R.id.noticelistview);
        inputText = (EditText)v.findViewById(R.id.inputnotice);
        addNotice();
        noticelistview.setAdapter(arrayAdapter);
        btn = (Button)v.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoticeDAO ndao = new NoticeDAO();

                Date dt = new Date();
                SimpleDateFormat full_sdf = new SimpleDateFormat("yyyy-MM-dd  ");
                Log.d("날짜 입니다",full_sdf.format(dt).toString());
                String notice = full_sdf.format(dt).toString()+inputText.getText().toString();

                //inputText.setText(notice);
                noticelistview.setAdapter(arrayAdapter);
                arrayAdapter.add(notice);
                Log.d("msgmsg",notice);
                try {
                    result = ndao.execute(inputText.getText().toString()).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                inputText.setText(null);
            }
        });
        noticelistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id){
                MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(getActivity())
                        .title("삭제")
                        .content(data.get(position)+"\n삭제하시겠습니까?")
                        .negativeText("yes")
                        .positiveText("no")
                        .cancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                del =  ((String) data.get(position)).split("  ");
                                for(int i=0;i<del.length;i++){
                                    Log.d("ddddddddd",del[i]);
                                }
                                DeleteDAO ddao = new DeleteDAO();
                                try {
                                    result = ddao.execute(del[0],del[1]).get();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                data.remove(position);
                                Log.d("포지션", String.valueOf(position));
                                arrayAdapter.notifyDataSetChanged();



                            }
                        });

                MaterialDialog dialog = dialogBuilder.build();
                dialog.show();

                return false;
            }
        });
        return v;
    }
    private void addNotice(){
        BoardDAO bdao = new BoardDAO();
        try{
            Allnotice = bdao.execute("borad").get();
            Log.d("공지사항 여러개들",""+Allnotice);
        }catch (Exception e){
            e.printStackTrace();
        }
        notice = Allnotice.split("//");
        for(int i=0;i<notice.length;i++){
            data.add(notice[i]);
        }

    }
}
