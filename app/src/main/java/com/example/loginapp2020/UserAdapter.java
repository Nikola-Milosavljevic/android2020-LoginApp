package com.example.loginapp2020;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {

    ArrayList<User> users;

    public UserAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater
                    = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null, false);
        }

        TextView username = (TextView) convertView.findViewById(R.id.text_view_list_username);
        TextView password = (TextView) convertView.findViewById(R.id.text_view_list_password);
        username.setText(users.get(position).getUsername());
        password.setText(users.get(position).getPassword());

        switch (users.get(position).getRole()) {
            case admin:
                convertView.setBackgroundColor(Color.RED);
                break;
            case moderator:
                convertView.setBackgroundColor(Color.BLUE);
                break;
            case guest:
                convertView.setBackgroundColor(Color.GREEN);
                break;
        }

        return convertView;
    }
}
