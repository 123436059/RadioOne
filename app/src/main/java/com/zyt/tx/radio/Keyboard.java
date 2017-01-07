package com.zyt.tx.radio; /**
 * Copyright(C) 2012,Kaizhen Electronic Co.,Ltd.
 * Author: Kaizhen
 * Company: Kaizhen Electronic Co.,Ltd
 * Version: 1.0
 * This file belongs to Kaizhen Electronic Co.,Ltd.
 * All rights reserved
 */


import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 跳转键盘
 *
 * @author Administrator
 *
 */
public class Keyboard {
    private static final String TAG = "Keyboard";
    private static final boolean DEBUG = false;

    private View keyboardView;

    private Button key1_btn, key2_btn, key3_btn, key4_btn, key5_btn, key_back_btn;
    private Button key6_btn, key7_btn, key8_btn, key9_btn, key0_btn, key_ok_btn;
    private TextView key_info_tv;
    private RelativeLayout keyboard_bg_layout;

    private int currentBand = 0; // 当前波段是FM（0，1，2）还是AM（3，4）
    private int maxFreq = 10800;
    private int minFreq = 8750;
    @SuppressWarnings("unused")
    private int stepValue = 0;

    private RequestHandler requestHandler;

    private final ArrayList<Integer> list = new ArrayList<Integer>();

    private PopupWindow popUP;

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.key0_btn:
                    push(0);
                    break;
                case R.id.key1_btn:
                    push(1);
                    break;
                case R.id.key2_btn:
                    push(2);
                    break;
                case R.id.key3_btn:
                    push(3);
                    break;
                case R.id.key4_btn:
                    push(4);
                    break;
                case R.id.key5_btn:
                    push(5);
                    break;
                case R.id.key6_btn:
                    push(6);
                    break;
                case R.id.key7_btn:
                    push(7);
                    break;
                case R.id.key8_btn:
                    push(8);
                    break;
                case R.id.key9_btn:
                    push(9);
                    break;
                case R.id.key_back_btn:
                    back();
                    break;
                case R.id.key_ok_btn:
                    ok();
                    break;
            }
            showInfo();
        }

    };

    private boolean isFMFormatCorrect = false;
    private boolean isAMFormatCorrect = false;

    private void ok() {
        int size = list.size();

        if (size == 0) {
            dismissPopUP();
            return;
        }

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < size; i++) {

            sb.append(list.get(i));

        }

        String numberStr = sb.toString();

        int number = Integer.valueOf(numberStr);

        if (currentBand == 0 || currentBand == 1 || currentBand == 2) {
            if (isFMFormatCorrect) {
//				if (size != 4) {
//					return;
//				}
                if (size == 3) {
                    number *= 10;
                } else if (size == 2){
                    number *= 100;
                } else if (size == 1) {
                    number *= 1000;
                }
            } else {
//				if (size != 5) {
//					return;
//				}
                if (size == 4) {
                    number *= 10;
                } else if (size == 3) {
                    number *= 100;
                } else if (size == 2){
                    number *= 1000;
                } else if (size == 1) {
                    number *= 10000;
                }
            }
        }

        if (currentBand == 3 || currentBand == 4) {
            if (isAMFormatCorrect) {
//				if (size != 3) {
//					return;
//				}
                if (size == 2) {
                    number *= 10;
                } else if (size == 1) {
                    number *= 100;
                }
            } else {
//				if (size != 4) {
//					return;
//				}
                if (size == 3) {
                    number *= 10;
                } else if (size == 2) {
                    number *= 100;
                } else if (size == 1) {
                    number *= 1000;
                }
            }
        }

        byte highByte = Utils.getHighByte(number);
        byte lowByte = Utils.getLowByte(number);

        sendSkipMessage(highByte, lowByte);

        dismissPopUP();
    }

    protected void sendSkipMessage(byte highByte, byte lowByte) {
        Message msg = requestHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putByte(MainActivity.HIGH_BYTE, highByte);
        bundle.putByte(MainActivity.LOW_BYTE, lowByte);

        msg.what = RequestCommand.DIRECT_FREQ_CHOOSE;
        msg.setData(bundle);

        requestHandler.sendMessage(msg);
    }

    private void back() {
        if (currTextViewLen == 0) {
            dismissPopUP();
        }
        pop();
    }

    public Keyboard(Context context, final RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
        keyboardView = LayoutInflater.from(context).inflate(R.layout.keyboard, null);

        key1_btn = (Button) keyboardView.findViewById(R.id.key1_btn);
        key2_btn = (Button) keyboardView.findViewById(R.id.key2_btn);
        key3_btn = (Button) keyboardView.findViewById(R.id.key3_btn);
        key4_btn = (Button) keyboardView.findViewById(R.id.key4_btn);
        key5_btn = (Button) keyboardView.findViewById(R.id.key5_btn);
        key6_btn = (Button) keyboardView.findViewById(R.id.key6_btn);
        key7_btn = (Button) keyboardView.findViewById(R.id.key7_btn);
        key8_btn = (Button) keyboardView.findViewById(R.id.key8_btn);
        key9_btn = (Button) keyboardView.findViewById(R.id.key9_btn);
        key0_btn = (Button) keyboardView.findViewById(R.id.key0_btn);
        key_back_btn = (Button) keyboardView.findViewById(R.id.key_back_btn);
        key_ok_btn = (Button) keyboardView.findViewById(R.id.key_ok_btn);

        key_info_tv = (TextView) keyboardView.findViewById(R.id.key_info_tv);

        keyboard_bg_layout = (RelativeLayout) keyboardView.findViewById(R.id.keyboard_bg_layout);

        key1_btn.setOnClickListener(clickListener);
        key2_btn.setOnClickListener(clickListener);
        key3_btn.setOnClickListener(clickListener);
        key4_btn.setOnClickListener(clickListener);
        key5_btn.setOnClickListener(clickListener);
        key6_btn.setOnClickListener(clickListener);
        key7_btn.setOnClickListener(clickListener);
        key8_btn.setOnClickListener(clickListener);
        key9_btn.setOnClickListener(clickListener);
        key0_btn.setOnClickListener(clickListener);
        key_back_btn.setOnClickListener(clickListener);
        key_ok_btn.setOnClickListener(clickListener);

        keyboard_bg_layout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismissPopUP();
                return true;
            }
        });

        popUP = new PopupWindow(keyboardView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

    /**
     * 弹出小键盘
     *
     * @param parent
     * @param x
     * @param y
     */
    public void showPopUP(View parent, int x, int y) {
        popUP.setAnimationStyle(R.style.PopupAnimation);
        popUP.showAtLocation(parent, Gravity.CENTER, x, y);
        popUP.setFocusable(true);
        popUP.update();
        keyboard_bg_layout.setBackgroundColor(0x1E000000);
        String showNumber = "";
        if (currentBand == 0 || currentBand == 1 || currentBand == 2) {
            showNumber = "---.--";
        }

        if (currentBand == 3 || currentBand == 4) {
            showNumber = "----";
        }
        key_info_tv.setText(showNumber);
    }

    /**
     * 隐藏小键盘
     */
    public void dismissPopUP() {
        keyboard_bg_layout.setBackgroundColor(0x000000);
        int size = list.size();
        for (int i = size - 1; i >= 0; i--) {
            list.remove(i);
        }
        popUP.dismiss();
    }

    private String cacheNumber = ""; // 缓存上次显示的数字

    /**
     * 在TextView中显示信息
     */
    private void showInfo() {


        int size = list.size();
        StringBuffer sb = new StringBuffer();
        String showNumber = "";

        for (int i = 0; i < size; i++) {

            sb.append(list.get(i));

        }

        String numberStr = sb.toString();

        if ("".equals(numberStr)) {

            if (currentBand == 0 || currentBand == 1 || currentBand == 2) {
                showNumber = "---.--";
            }

            if (currentBand == 3 || currentBand == 4) {
                showNumber = "----";
            }
            key_info_tv.setText(showNumber);
            return;
        }

        int number = Integer.valueOf(numberStr);

        if (currentBand == 0 || currentBand == 1 || currentBand == 2) {
            if (judgeMin(number) || judgeBetween(number)) {
                int len = sb.length();

                StringBuffer showBuffer = new StringBuffer(sb);

                if (len == 1) {
                    isFMFormatCorrect = true;
                    showBuffer.append('-').append('.').append('-').append('-');
                    showNumber = showBuffer.toString();
                    cacheNumber = showNumber;
                }

                if (len == 2) {
                    showBuffer.append('.').append('-').append('-');
                    showNumber = showBuffer.toString();
                    cacheNumber = showNumber;
                }

                if (len == 3) {
                    String a1 = numberStr.substring(0, len - 1);
                    String a2 = numberStr.substring(len - 1);
                    showNumber = a1 + "." + a2 + "-";
                    cacheNumber = showNumber;
                }

                if (len == 4) {
                    String a1 = numberStr.substring(0, len - 2);
                    String a2 = numberStr.substring(len - 2);
                    showNumber = a1 + "." + a2;
                    cacheNumber = showNumber;
                }

                if (len == 5) {
                    String a1 = numberStr.substring(0, len - 2);
                    String a2 = numberStr.substring(len - 2);
                    showNumber = a1 + "." + a2;
                    cacheNumber = showNumber;
                }

            } else if (judgeMax(number) || judgeBetween2(number)) {

                int len = sb.length();
                StringBuffer showBuffer = new StringBuffer(sb);

                if (len == 1) {
                    isFMFormatCorrect = false;
                    showBuffer.append('-').append('-').append('.').append('-').append('-');
                    showNumber = showBuffer.toString();
                    cacheNumber = showNumber;
                }

                if (len == 2) {
                    showBuffer.append('-').append('.').append('-').append('-');
                    showNumber = showBuffer.toString();
                    cacheNumber = showNumber;
                }

                if (len == 3) {
                    showBuffer.append('.').append('-').append('-');
                    showNumber = showBuffer.toString();
                    cacheNumber = showNumber;
                }

                if (len == 4) {
                    String a1 = numberStr.substring(0, len - 1);
                    String a2 = numberStr.substring(len - 1);
                    showNumber = a1 + "." + a2 + "-";
                    cacheNumber = showNumber;
                }

                if (len == 5) {
                    String a1 = numberStr.substring(0, len - 2);
                    String a2 = numberStr.substring(len - 2);
                    showNumber = a1 + "." + a2;
                    cacheNumber = showNumber;
                }

            } else {
                pop();
                if (list.size() == 0) {
                    showNumber = "---.--";
                } else {
                    showNumber = cacheNumber;
                }
            }

        }

        if (currentBand == 3 || currentBand == 4) {
            if (judgeMin(number) || judgeBetween(number)) {

                int len = sb.length();
                StringBuffer showBuffer = new StringBuffer(sb);

                if (len == 1) {
                    isAMFormatCorrect = true;
                    showBuffer.append('-').append('-');
                    showNumber = showBuffer.toString();
                    cacheNumber = showNumber;
                }

                if (len == 2) {
                    showBuffer.append('-');
                    showNumber = showBuffer.toString();
                    cacheNumber = showNumber;
                }

                if (len == 3) {
                    showNumber = showBuffer.toString();
                    cacheNumber = showNumber;
                }

                if (len == 4) {
                    showNumber = showBuffer.toString();
                    cacheNumber = showNumber;
                }

            } else if (judgeMax(number) || judgeBetween2(number)) {

                int len = sb.length();
                StringBuffer showBuffer = new StringBuffer(sb);

                if (len == 1) {
                    isAMFormatCorrect = false;
                    showBuffer.append('-').append('-').append('-');
                    showNumber = showBuffer.toString();
                    cacheNumber = showNumber;
                }

                if (len == 2) {
                    showBuffer.append('-').append('-');
                    showNumber = showBuffer.toString();
                    cacheNumber = showNumber;
                }

                if (len == 3) {
                    showBuffer.append('-');
                    showNumber = showBuffer.toString();
                    cacheNumber = showNumber;
                }

                if (len == 4) {
                    showNumber = showBuffer.toString();
                    cacheNumber = showNumber;
                }

            } else {
                pop();
                if (list.size() == 0) {
                    showNumber = "----";
                } else {
                    showNumber = cacheNumber;
                }
            }

        }

        key_info_tv.setText(showNumber);

    }

    /**
     * 把符合的数压入栈
     *
     * @param num
     */
    private void push(int num) {
        int size = list.size();
        if (size >= maxFreqLen) {
            return;
        }
        list.add(num);
        currTextViewLen = list.size();
    }

    /**
     * 弹出栈
     */
    private void pop() {
        int size = list.size();
        if (size == 0) {
            return;
        }
        list.remove(size - 1);
        currTextViewLen = list.size();
    }

    int showBitsNum = 0;
    int minFreqLen;
    int maxFreqLen;
    int currTextViewLen = 0;

    public void initFreqBits() {
        minFreqLen = String.valueOf(minFreq).length();
        maxFreqLen = String.valueOf(maxFreq).length();
    }

    /**
     * 使用构造出来的最大数和最小数跟minFreq判断范围
     *
     * @param num
     * @return
     */
    private boolean judgeMin(int num) {

        boolean flag = false;

        int bit = (minFreqLen - currTextViewLen < 0) ? 0 : (minFreqLen - currTextViewLen);

        int[] arrayOfMinFreqLen = { constructMinNumber(num, bit), constructMaxNumber(num, bit) };

        if (minFreq > arrayOfMinFreqLen[1]) {
            flag = false;
        }

        if (minFreq >= arrayOfMinFreqLen[0] && minFreq <= arrayOfMinFreqLen[1]) {
            flag = true;
        }

        return flag;
    }

    /**
     * 使用构造出来的最大数和最小数跟maxFreq判断范围
     *
     * @param num
     * @return
     */
    private boolean judgeMax(int num) {

        boolean flag = false;

        int[] arrayOfMaxFreqLen = { constructMinNumber(num, maxFreqLen - currTextViewLen), constructMaxNumber(num, maxFreqLen - currTextViewLen) };

        if (maxFreq < arrayOfMaxFreqLen[0]) {
            flag = false;
        }

        if (maxFreq >= arrayOfMaxFreqLen[0] && maxFreq <= arrayOfMaxFreqLen[1]) {
            flag = true;
        }

        return flag;
    }

    /**
     * 介于minFreq和maxFreq之间的，2位数的情况
     *
     * @param num
     * @return
     */
    private boolean judgeBetween(int num) {

        boolean flag = false;

        int bit = (minFreqLen - currTextViewLen < 0) ? 0 : (minFreqLen - currTextViewLen);

        int[] arrayOfBetweenFreqLen = { constructMinNumber(num, bit), constructMaxNumber(num, bit) };

        if (minFreq > arrayOfBetweenFreqLen[0] || maxFreq < arrayOfBetweenFreqLen[1]) {
            flag = false;
        }

        if (minFreq < arrayOfBetweenFreqLen[0] && maxFreq > arrayOfBetweenFreqLen[1]) {
            flag = true;
        }

        return flag;
    }

    /**
     * 介于minFreq和maxFreq之间的，3位数的情况
     *
     * @param num
     * @return
     */
    private boolean judgeBetween2(int num) {

        boolean flag = false;

        int[] arrayOfBetweenFreqLen = { constructMinNumber(num, maxFreqLen - currTextViewLen), constructMaxNumber(num, maxFreqLen - currTextViewLen) };

        if (minFreq < arrayOfBetweenFreqLen[0] && maxFreq >= arrayOfBetweenFreqLen[1]) {
            flag = true;
        }

        return flag;
    }

    /**
     * 构造给定数字的最大数
     *
     * @param num
     * @param len
     * @return
     */
    private int constructMaxNumber(int num, int len) {

        double sum = num * Math.pow(10, len);
        for (int i = 1; i <= len; i++) {
            sum = sum + 9 * Math.pow(10, (len - i));
        }
        //
        return (int) sum;
    }

    /**
     * 构造给定数字的最小数
     *
     * @param num
     * @param len
     * @return
     */
    private int constructMinNumber(int num, int len) {
        return (int) (num * Math.pow(10, len));
    }

    public void setCurrentBand(int currentBand) {
        this.currentBand = currentBand;
    }

    public void setMinFreq(int minFreq) {
        this.minFreq = minFreq;
    }

    public void setMaxFreq(int maxFreq) {
        this.maxFreq = maxFreq;
    }

    public void setStepValue(int stepValue) {
        this.stepValue = stepValue;
    }

    public View getKeyboardView() {
        return this.keyboardView;
    }
}
