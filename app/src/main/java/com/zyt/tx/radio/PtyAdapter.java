package com.zyt.tx.radio; /**
 * Copyright(C) 2012,Kaizhen Electronic Co.,Ltd.
 * Author: Kaizhen
 * Company: Kaizhen Electronic Co.,Ltd
 * Version: 1.0
 * This file belongs to Kaizhen Electronic Co.,Ltd.
 * All rights reserved
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PtyAdapter extends BaseAdapter {

    private String[] data;
    private LayoutInflater inflater;

    public PtyAdapter(Context context, String[] data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pty_list_item, null);
        }

        TextView pty_item_tv = (TextView) convertView.findViewById(R.id.pty_item_tv);

        pty_item_tv.setText(data[position]);

        return pty_item_tv;
    }

}
