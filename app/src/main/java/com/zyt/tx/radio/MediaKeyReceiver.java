package com.zyt.tx.radio; /**
 * Copyright(C) 2012,Kaizhen Electronic Co.,Ltd.
 * Author: Kaizhen
 * Company: Kaizhen Electronic Co.,Ltd
 * Version: 1.0
 * This file belongs to Kaizhen Electronic Co.,Ltd.
 * All rights reserved
 */


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;


public class MediaKeyReceiver extends BroadcastReceiver {
    private static final String TAG = "MediaKeyReceiver";
    private static final boolean DEBUG = true;

    private static final byte SWITCH_BAND = 0x01;
    private static final byte LEFT = 0x02;
    private static final byte RIGHT = 0x03;
    private static final byte DOWN = 0x04;
    private static final byte LONG_DOWN = 0x05;
    public static final byte FRESH_NOTIFY = 0x06;

    public static final String CURRENT_BAND = "currentBand";
    public static final String CURRENT_FREQ = "currentFreq";
    public static final String CURRENT_CHANNEL = "currentChannel";

    public static final int RADIO_ID = 1;

    private RequestHandler requestHandler;

//	private static Thread mBussinessThread = null;
//	// private boolean mIsBussinessThreadWaiting = false;
//	private static boolean mBussinessThreadStarted = false;
//	public static BussinessThreadHandler mBussinessThreadHandler = null;
//
//	public class BussinessThreadHandler extends Handler {
//
//		public boolean sendMessage(int what, int arg1, int arg2) {
//			removeMessages(what);
//			return super.sendMessage(obtainMessage(what, arg1, arg2));
//		}
//
//		public void handleMessage(Message msg) {
//			// Do whatever bussiness here
//			switch (msg.what) {
//				case FRESH_NOTIFY:
//					int currentBand = msg.arg1;
//					int currentFreq = msg.arg2;
//					Utils.addNotification(mContext, currentBand, currentFreq);
//					Log.e(TAG, "currentFreq = " + currentFreq);
//					break;
//			}
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//		}
//	};
//
//	private void startBusinessThread() {
//		if (mBussinessThreadStarted == true) {
//			return;
//		} else {
//			mBussinessThreadStarted = true;
//		}
//		mBussinessThread = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					Looper.prepare();
//					mBussinessThreadHandler = new BussinessThreadHandler();
//					Looper.loop();
//				} catch (Exception e) {
//					Log.e(TAG, "BussinessThread error!");
//				}
//			}
//
//		});
//		mBussinessThread.start();
//	}

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.mContext = context;

//		startBusinessThread();

        SharedPreferences sp = Utils.getSharedPreferences(context);
        requestHandler = new RequestHandler(context);
        String action = intent.getAction();

        int cmd = intent.getIntExtra(ResponseCommand.SEND_CMD_TO_APK_CMD, 0);

        // 先获取当前波段
        if (ResponseCommand.TO_RADIO_APK_ACTION.equals(action) && ResponseCommand.SEND_TO_RADIO_CMD == cmd) {
            int len = intent.getIntExtra(ResponseCommand.SEND_CMD_TO_APK_LEN, 0);

            byte[] buffer = intent.getByteArrayExtra(ResponseCommand.SEND_CMD_TO_APK_BUF);

            if (len == 0 || buffer == null || buffer.length != len) {
                return;
            }

            // 0x01
            if (ResponseCommand.CURRENT_INFO_REPORT == buffer[0]) {
                // 获取当前波段
                int currentBand = buffer[1] & 0xFF;
                int currentChannel = buffer[2] & 0xFF;
                int currentFreq = ((buffer[3] << 8) & 0xFF00) | (buffer[4] & 0xFF);

                HashMap<String, Integer> map = Utils.readSharedPreferences(sp);
                int currentBandTmp = map.get(MediaKeyReceiver.CURRENT_BAND);
                int currentFreqTmp = map.get(MediaKeyReceiver.CURRENT_FREQ);

                boolean flag = true;

                if (currentBand == currentBandTmp && currentFreq == currentFreqTmp) {
                    flag = false;
                }

//                if (null != MainActivity.mBussinessThreadHandler && flag) {
//                    MainActivity.mBussinessThreadHandler.sendMessage(FRESH_NOTIFY, currentBand, currentFreq);
//                }
                Utils.writeSharedPreferences(sp, currentBand, currentFreq, currentChannel);
            }
        }

        // 物理按键操作
        if (ResponseCommand.TO_RADIO_APK_ACTION.equals(action) && ResponseCommand.MEDIA_BUTTON_SEND_TO_RADIO_CMD == cmd) {

            int len = intent.getIntExtra(ResponseCommand.SEND_CMD_TO_APK_LEN, 0);

            byte[] buffer = intent.getByteArrayExtra(ResponseCommand.SEND_CMD_TO_APK_BUF);

            if (len == 0 || buffer == null || buffer.length != len) {

                Log.e(TAG, "Conditions do not conform!");

                return;
            }

            HashMap<String, Integer> map = Utils.readSharedPreferences(sp);
            int currentBand = map.get(CURRENT_BAND);

            // 0x01
            if (SWITCH_BAND == buffer[0]) {

                if (MainActivity.isOnPause) {
                    Intent radioIntent = new Intent(context, MainActivity.class);
                    // radioIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    radioIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(radioIntent);
                    return;
                }
                if (currentBand == 0) {
                    sendMessage(RequestCommand.BAND_SWITCH, MainActivity.BAND_SWITCH, (byte) 0x01);
                } else if (currentBand == 1) {
                    sendMessage(RequestCommand.BAND_SWITCH, MainActivity.BAND_SWITCH, (byte) 0x02);
                } else if (currentBand == 2) {
                    sendMessage(RequestCommand.BAND_SWITCH, MainActivity.BAND_SWITCH, (byte) 0x03);
                } else if (currentBand == 3) {
                    sendMessage(RequestCommand.BAND_SWITCH, MainActivity.BAND_SWITCH, (byte) 0x04);
                } else if (currentBand == 4) {
                    sendMessage(RequestCommand.BAND_SWITCH, MainActivity.BAND_SWITCH, (byte) 0x00);
                }
            }

            // 0x02
            if (LEFT == buffer[0]) {
                sendMessage(RequestCommand.SINGLE_STEP, MainActivity.SIGLE_STEP, (byte) 0x00); // 单步向上
            }

            // 0x03
            if (RIGHT == buffer[0]) {
                sendMessage(RequestCommand.SINGLE_STEP, MainActivity.SIGLE_STEP, (byte) 0x01); // 单步向下
            }

            // 0x04
            if (DOWN == buffer[0]) {
                requestHandler.sendEmptyMessage(RequestCommand.AS); // 按下 AS
            }

            // 0x05
            if (LONG_DOWN == buffer[0]) {
                requestHandler.sendEmptyMessage(RequestCommand.PS); // 按下 PS
            }

        }

        // 退出程序
        if (ResponseCommand.TO_RADIO_APK_ACTION.equals(action) && 0xfe == cmd) {

            int len = intent.getIntExtra(ResponseCommand.SEND_CMD_TO_APK_LEN, 0);

            byte[] buffer = intent.getByteArrayExtra(ResponseCommand.SEND_CMD_TO_APK_BUF);

            if (len == 0 || buffer == null || buffer.length != len) {

                Log.e(TAG, "Conditions do not conform!");

                return;
            }

            if (0x01 == buffer[0]) {
                if (DEBUG) {
                    Log.d(TAG, " receive close radio");
                    Log.d(TAG, "cmd = " + cmd + " buffer[0] = " + buffer[0]);
                }
                if (Utils.list.size() != 0) {
                    Utils.list.get(0).finish();
                }
                Log.e(TAG, "_____________________________________mediakeyReceiver");
                if (MainActivity.mBussinessThreadHandler != null) {
                    MainActivity.mBussinessThreadHandler.getLooper().quit();
                    MainActivity.mBussinessThreadHandler = null;
                }
                Utils.closeNotification();

                android.os.Process.killProcess(android.os.Process.myPid());
                // System.exit(0);
            }

        }

    }

    /**
     * 发送请求处理的消息
     *
     * @param messageWhat
     * @param key
     * @param value
     */
    protected void sendMessage(int messageWhat, String key, byte value) {
        Message msg = requestHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putByte(key, value);

        msg.what = messageWhat;
        msg.setData(bundle);

        requestHandler.sendMessage(msg);
    }
}
