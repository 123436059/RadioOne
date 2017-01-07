package com.zyt.tx.radio; /**
 * Copyright(C) 2012,Kaizhen Electronic Co.,Ltd.
 * Author: Kaizhen
 * Company: Kaizhen Electronic Co.,Ltd
 * Version: 1.0
 * This file belongs to Kaizhen Electronic Co.,Ltd.
 * All rights reserved
 */


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;


@SuppressLint("ViewConstructor")
public class MenuPop extends PopupWindow implements View.OnClickListener {
    private View mMenuView;
    private RequestHandler mRequestHandler;
    private MainActivity mContext;
    private Keyboard mKeyboard;

    public MenuPop(final MainActivity context, RequestHandler requestHandler) {
        this.mContext = context;
        this.mRequestHandler = requestHandler;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.menu_layout, null);
        setContentView(mMenuView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setAnimationStyle(R.style.MenuPopupAnimation);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);

        mKeyboard = new Keyboard(context, requestHandler);

        Button skip_btn = (Button) mMenuView.findViewById(R.id.skip_btn);
        Button loc_btn = (Button) mMenuView.findViewById(R.id.loc_btn);
        Button as_btn = (Button) mMenuView.findViewById(R.id.as_btn);
        Button ps_btn = (Button) mMenuView.findViewById(R.id.ps_btn);

        skip_btn.setOnClickListener(this);
        loc_btn.setOnClickListener(this);
        as_btn.setOnClickListener(this);
        ps_btn.setOnClickListener(this);
        mMenuView.setFocusableInTouchMode(true);
        mMenuView.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_MENU:
                        if (MenuPop.this.isShowing()) {
                            MenuPop.this.dismiss();
                        }
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip_btn:
                mKeyboard.showPopUP(mContext.getAnchor(), 0, 0);
                break;
            case R.id.loc_btn:
                mRequestHandler.sendEmptyMessage(RequestCommand.LOC_DX);
                break;
            case R.id.as_btn:
                mRequestHandler.sendEmptyMessage(RequestCommand.AS);
                break;
            case R.id.ps_btn:
                mRequestHandler.sendEmptyMessage(RequestCommand.PS);
                break;
        }
        this.dismiss();
    }

    public Keyboard getKeyboardInstance() {
        return mKeyboard;
    }

}
