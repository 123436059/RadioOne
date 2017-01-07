package com.zyt.tx.radio; /**
 * Copyright(C) 2012,Kaizhen Electronic Co.,Ltd.
 * Author: Kaizhen
 * Company: Kaizhen Electronic Co.,Ltd
 * Version: 1.0
 * This file belongs to Kaizhen Electronic Co.,Ltd.
 * All rights reserved
 */


import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;


public class PtyPopup {

//	private String[] pty_types = {
//			"None", "News", "Current affairs", "Information", "Sport", "Education", "Drama", "Culture",
//			"Science", "Varied", "Pop music", "Rock music", "Easy listening", "Light classical", "Serious classical", "Other music",
//			"Weather", "Finance", "Children’s programmes", "Social affairs", "Religion", "Phone-in", "Travel", "Leisure",
//			"Jazz music", "Country music", "National music", "Oldies music", "Folk music", "Documentary", "Alarm test", "Alarm"
//	};

    private String[] pty_types = {
            "None", "News", "Affairs", "Info", "Sport", "Educate", "Drama", "Culture",
            "Science", "Varied", "Pon M", "Rock M", "Easy M", "Light M", "Clasics", "Other M",
            "Weather", "Finance", "Children", "Social", "Religion", "Phone IN", "Travel", "Leisure",
            "Jazz", "Country", "Nation M", "Oldies", "Folk M", "Document", "Test", "Alarm"
    };

    private PopupWindow popUP;

    public PtyPopup(Context context, final RequestHandler requestHandler) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View ptyView = inflater.inflate(R.layout.pty_list, null);

        RelativeLayout pty_list_layout = (RelativeLayout) ptyView.findViewById(R.id.pty_list_layout);

        final ListView pty_listview = (ListView) ptyView.findViewById(R.id.pty_listview);
        final PtyAdapter adapter = new PtyAdapter(context, pty_types);
        pty_listview.setAdapter(adapter);

        pty_listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                view.setBackgroundResource(R.drawable.pty_list_item_down);

                Bundle bundle = new Bundle();
                bundle.putByte(MainActivity.PTY_SEARCH, (byte)position);
                Message msg = requestHandler.obtainMessage();
                msg.what = RequestCommand.PTY_SEARCH;
                msg.setData(bundle);
                requestHandler.sendMessage(msg);

                pty_listview.invalidate();

                dismissPopUP();
            }
        });

        pty_list_layout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismissPopUP();
                return true;
            }
        });

        popUP = new PopupWindow(ptyView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

    public void showPopUP(View parent, int x, int y) {
        popUP.setAnimationStyle(R.style.PopupAnimation);
        popUP.showAtLocation(parent, Gravity.CENTER, x, y);
        popUP.setFocusable(true);
        popUP.update();
    }

    public void dismissPopUP() {
        popUP.dismiss();
    }
}
