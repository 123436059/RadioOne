package com.zyt.tx.radio;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MJS on 2017/1/6.
 */

public class Utils {


    public static ArrayList<Activity> list = new ArrayList<Activity>();
    private static NotificationManager mNotificationManager;

    /**
     * @param freq 频率
     * @param digit 要返回的位置
     * @return freq第digit位的数字
     */
    public static int getDigitValueFromFreq(int freq, int digit) {
        String strFreq = String.valueOf(freq);
        int freqLen = strFreq.length();
        if (digit < 1 || digit > freqLen) {
            return 0;
        }
        char digitValue = strFreq.charAt(freqLen - digit);
        return digitValue & 0x000F;
    }


    /**
     * 将整数化为字符串
     * @param freq
     * @return
     */
    public static String parseIntegerToFreq(int freq) {
        String strFreq = String.valueOf(freq);
        StringBuilder sb = new StringBuilder(strFreq);
        return sb.insert(sb.length() - 2, ".").toString();
    }
    /**
     * 获取频率的低字节
     *
     * @param freq
     * @return
     */
    public static byte getLowByte(int freq) {
        byte lowByte = (byte) (freq & 0xFF);
        return lowByte;
    }

    /**
     * 获取频率的高字节
     *
     * @param freq
     * @return
     */
    public static byte getHighByte(int freq) {
        byte hightByte = (byte) ((freq >>> 8) & 0xFF);
        return hightByte;
    }


    public static int digitSelectImageResId(int digit) {
        int resId = 0;
        switch (digit) {
            case 0:
                resId = R.drawable.digit_0;
                break;
            case 1:
                resId = R.drawable.digit_1;
                break;
            case 2:
                resId = R.drawable.digit_2;
                break;
            case 3:
                resId = R.drawable.digit_3;
                break;
            case 4:
                resId = R.drawable.digit_4;
                break;
            case 5:
                resId = R.drawable.digit_5;
                break;
            case 6:
                resId = R.drawable.digit_6;
                break;
            case 7:
                resId = R.drawable.digit_7;
                break;
            case 8:
                resId = R.drawable.digit_8;
                break;
            case 9:
                resId = R.drawable.digit_9;
                break;
            default:
                resId = R.drawable.digit_0;
                break;
        }
        return resId;
    }


    public static int getShowPTYTypeR2sId(byte id) {
        int resId = R.string.pty_indicate_00;
        switch (id) {
            case 0:
                resId = R.string.pty_indicate_00;
                break;
            case 1:
                resId = R.string.pty_indicate_01;
                break;
            case 2:
                resId = R.string.pty_indicate_02;
                break;
            case 3:
                resId = R.string.pty_indicate_03;
                break;
            case 4:
                resId = R.string.pty_indicate_04;
                break;
            case 5:
                resId = R.string.pty_indicate_05;
                break;
            case 6:
                resId = R.string.pty_indicate_06;
                break;
            case 7:
                resId = R.string.pty_indicate_07;
                break;
            case 8:
                resId = R.string.pty_indicate_08;
                break;
            case 9:
                resId = R.string.pty_indicate_09;
                break;
            case 10:
                resId = R.string.pty_indicate_10;
                break;
            case 11:
                resId = R.string.pty_indicate_11;
                break;
            case 12:
                resId = R.string.pty_indicate_12;
                break;
            case 13:
                resId = R.string.pty_indicate_13;
                break;
            case 14:
                resId = R.string.pty_indicate_14;
                break;
            case 15:
                resId = R.string.pty_indicate_15;
                break;
            case 16:
                resId = R.string.pty_indicate_16;
                break;
            case 17:
                resId = R.string.pty_indicate_17;
                break;
            case 18:
                resId = R.string.pty_indicate_18;
                break;
            case 19:
                resId = R.string.pty_indicate_19;
                break;
            case 20:
                resId = R.string.pty_indicate_20;
                break;
            case 21:
                resId = R.string.pty_indicate_21;
                break;
            case 22:
                resId = R.string.pty_indicate_22;
                break;
            case 23:
                resId = R.string.pty_indicate_23;
                break;
            case 24:
                resId = R.string.pty_indicate_24;
                break;
            case 25:
                resId = R.string.pty_indicate_25;
                break;
            case 26:
                resId = R.string.pty_indicate_26;
                break;
            case 27:
                resId = R.string.pty_indicate_27;
                break;
            case 28:
                resId = R.string.pty_indicate_28;
                break;
            case 29:
                resId = R.string.pty_indicate_29;
                break;
            case 30:
                resId = R.string.pty_indicate_30;
                break;
            case 31:
                resId = R.string.pty_indicate_31;
                break;
            default:
                resId = R.string.pty_indicate_00;
                break;
        }
        return resId;
    }


    private static final String TAG = "Utils";

    public static final String FILE_NAME = "radio";
    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean readSystemSetKey() {
        boolean flag = false; // 没有RDS
        File file = new File("/system/etc/System_set_key.conf");
        if (file.canRead()) {
            try {
                BufferedReader buf = new BufferedReader(new FileReader(file));
                String str = null;
                while ((str = buf.readLine())!= null) {
                    Log.e(TAG, str);
                    if ("rds_system".equalsIgnoreCase(str)) {
                        String n = str.substring(str.length() - 1);
                        int num = Integer.valueOf(n);
                        if (num == 0) {
                            flag = false;
                        } else if (num == 1) {
                            flag = true;
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "File is not read");
        }
        return flag;
    }

    public static HashMap<String, Integer> readSharedPreferences(SharedPreferences sp) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int currentBand = sp.getInt(MediaKeyReceiver.CURRENT_BAND, 0);
        int currentFreq = sp.getInt(MediaKeyReceiver.CURRENT_FREQ, 8750);
        int currentChannel = sp.getInt(MediaKeyReceiver.CURRENT_CHANNEL, 1);

        map.put(MediaKeyReceiver.CURRENT_BAND, currentBand);
        map.put(MediaKeyReceiver.CURRENT_FREQ, currentFreq);
        map.put(MediaKeyReceiver.CURRENT_CHANNEL, currentChannel);
        return map;
    }

    public static void writeSharedPreferences(SharedPreferences sp, int currentBand, int currrentFreq, int currentChannel) {
        SharedPreferences.Editor editor = sp.edit();
        // if (sp.getInt(MediaKeyReceiver.CURRENT_BAND, 0) != currentBand) {
        // editor.putInt(MediaKeyReceiver.CURRENT_BAND, currentBand);
        // }
        // if (sp.getInt(MediaKeyReceiver.CURRENT_FREQ, 8750) != currrentFreq) {
        // editor.putInt(MediaKeyReceiver.CURRENT_FREQ, currrentFreq);
        // }
        editor.putInt(MediaKeyReceiver.CURRENT_BAND, currentBand);
        editor.putInt(MediaKeyReceiver.CURRENT_FREQ, currrentFreq);
        editor.putInt(MediaKeyReceiver.CURRENT_CHANNEL, currentChannel);
        editor.commit();
    }

    //-------------------------------------------------
    private static final int REMOTEVIEWS_REQUEST_CODE = 0;
    private static RemoteViews rv;
    private static Notification notify;

    private static PendingIntent prevIntent;
    private static PendingIntent nextIntent;
    private static PendingIntent exitIntent;
    private static PendingIntent contentIntent;
    private static Intent openRadioActivityIntent;

    @TargetApi(16)
    public static void addNotification(Context context, int currentBand, int currentFreq) {
        // Context context = ctx.getApplicationContext();
        if (openRadioActivityIntent == null) {
            openRadioActivityIntent = new Intent(context, MainActivity.class);
        }
        if (contentIntent == null) {
            contentIntent = PendingIntent.getActivity(context, REMOTEVIEWS_REQUEST_CODE, openRadioActivityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        // 自定义通知View的按钮
        if (prevIntent == null) {
            prevIntent = PendingIntent.getBroadcast(context, 0, getPrevIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
        }
        if (nextIntent == null) {
            nextIntent = PendingIntent.getBroadcast(context, 0, getNextIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
        }
        if (exitIntent == null) {
            exitIntent = PendingIntent.getBroadcast(context, 0, getExitIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
        }

        if (rv == null) {
            rv = new RemoteViews(context.getPackageName(), R.layout.notify);
            rv.setOnClickPendingIntent(R.id.notity_prev, prevIntent);
            rv.setOnClickPendingIntent(R.id.notity_next, nextIntent);
            rv.setOnClickPendingIntent(R.id.notity_close, exitIntent);
        }

        rv.setTextViewText(R.id.notify_band, getBandStr(currentBand));
        if (currentBand == 3 || currentBand == 4) {
            rv.setTextViewText(R.id.notify_freq, String.valueOf(currentFreq));
        } else {
            rv.setTextViewText(R.id.notify_freq, parseIntegerToFreq(currentFreq) + "0");
        }
        rv.setTextViewText(R.id.notify_sign, judgeBand(currentBand) ? "MHz" : "KHz");

        if (notify == null) {
            notify = new Notification.Builder(context).setSmallIcon(R.drawable.notity_icon).setWhen(System.currentTimeMillis())
                    .setContent(rv).setContentIntent(contentIntent).build();
            notify.flags = Notification.FLAG_NO_CLEAR;
        }

        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        mNotificationManager.notify(MediaKeyReceiver.RADIO_ID, notify);
    }

    /**
     * 判断是FM AM哪个频段
     *
     * @param currentBand
     * @return true FM; false AM
     */
    public static boolean judgeBand(int currentBand) {
        if (currentBand == 0 || currentBand == 1 || currentBand == 2) {
            return true;
        }
        return false;
    }

    public static String getBandStr(int currentBand) {
        if (currentBand == 0) {
            return "FM1";
        } else if (currentBand == 1) {
            return "FM2";
        }
        if (currentBand == 2) {
            return "FM3";
        }
        if (currentBand == 3) {
            return "AM1";
        }
        if (currentBand == 4) {
            return "AM2";
        } else {
            return "FM1";
        }
    }

    /**
     * 退出广播的Intent
     *
     * @return
     */
    private static Intent getExitIntent() {
        int len = 2;
        byte[] buffer = new byte[len];
        buffer[0] = 0x01;
        buffer[1] = 0x01;
        // SEND_TO_MCU_ACTION == com.android.hklt.kzcar.mpu_to_mcu
        Intent intent = new Intent(RequestCommand.SEND_TO_MCU_ACTION);
        // EXIT_SOURCE_CMD == 0xfd
        intent.putExtra(RequestCommand.SEND_CMD_TO_MCU_CMD, RequestCommand.EXIT_SOURCE_CMD);
        intent.putExtra(RequestCommand.SEND_CMD_TO_MCU_LEN, len);
        intent.putExtra(RequestCommand.SEND_CMD_TO_MCU_BUF, buffer);
        intent.putExtra("quit", 0xFF);

        return intent;
    }

    public static final String PREV_PLAY = "com.kz.radio.prev";
    public static final String NEXT_PLAY = "com.kz.radio.next";
    /**
     * 响应通知栏中按“上一首”按钮的Intent
     *
     */
    private static Intent getPrevIntent() {
        return new Intent(PREV_PLAY);
    }

    /**
     * 响应通知栏中按“下一首”按钮的Intent
     *
     */
    private static Intent getNextIntent() {
        return new Intent(NEXT_PLAY);
    }

    public static void closeNotification() {
        // if (manager != null) {
        // manager.cancel(MediaKeyReceiver.RADIO_ID);
        // }
        if (mNotificationManager != null) {
            mNotificationManager.cancel(MediaKeyReceiver.RADIO_ID);
        }
    }
}
