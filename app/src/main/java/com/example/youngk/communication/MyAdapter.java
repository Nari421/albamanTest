package com.example.youngk.communication;

/**
 * Created by YoungK on 2017-05-15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ChatDTO> items;
    LayoutInflater inflater;

    public MyAdapter(Context context, ArrayList<ChatDTO> items) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.chatting_item, null);
            holder.userName = (TextView) convertView.findViewById(R.id.user);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.date = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(holder);
        }
        holder = (ViewHolder)convertView.getTag();

        ChatDTO chatting = items.get(position);

        holder.userName.setText(chatting.getUser());
        holder.content.setText(chatting.getContent());
        holder.date.setText(chatting.getDate());

        return convertView;
    }

    class ViewHolder{
        public TextView userName;
        public TextView content;
        public TextView date;
    }
}
