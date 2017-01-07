package com.zyt.tx.radio; /**
 * Copyright(C) 2012,Kaizhen Electronic Co.,Ltd.
 * Author: Kaizhen 
 * Company: Kaizhen Electronic Co.,Ltd
 * Version: 1.0
 * This file belongs to Kaizhen Electronic Co.,Ltd. 
 * All rights reserved
 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public class RequestHandler extends Handler {
    private static final String TAG = "RequestHandler";
    private static final boolean DEBUG = false;
    private Context context;

    public RequestHandler() {

    }

    public RequestHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case RequestCommand.BAND_SWITCH:
                byte bandNum = msg.getData().getByte(MainActivity.BAND_SWITCH);
                bandSwitch(bandNum);
                break;
            case RequestCommand.SINGLE_STEP:
                byte stepDirection = msg.getData().getByte(MainActivity.SIGLE_STEP);
                singleStep(stepDirection);
                break;
            case RequestCommand.SEARCH:
                byte searchDirection = msg.getData().getByte(MainActivity.SEARCH);
                search(searchDirection);
                break;
            case RequestCommand.CHANNEL_CHOOSE:
                Bundle bundle = msg.getData();
                byte chooseChannelNum = bundle.getByte(MainActivity.CHANNEL_CHOOSE);
                channelChoose(chooseChannelNum);
                break;
            case RequestCommand.ADJUSTABLE_CHANNEL:
                // 没有用的功能
                byte adjustDirection = msg.getData().getByte(MainActivity.ADJUSTABLE_CHANNEL);
                adjustableChannel(adjustDirection);
                break;
            case RequestCommand.SET_PRESTORE_CHANNEL:
                byte prestoreChannelNum2 = msg.getData().getByte(MainActivity.SET_PRESTORE_CHANNEL);
                setPrestoreChannel(prestoreChannelNum2);
                break;
            case RequestCommand.PS:
                ps();
                break;
            case RequestCommand.AS:
                as();
                break;
            case RequestCommand.LOC_DX:
                locOrDx();
                break;
            case RequestCommand.DIRECT_FREQ_CHOOSE:
                // TODO
                byte highByte = msg.getData().getByte(MainActivity.HIGH_BYTE);
                byte lowByte = msg.getData().getByte(MainActivity.LOW_BYTE);
                directFreqChoose(highByte, lowByte);
                break;
            case RequestCommand.RDS_TOGGLE:
                byte rdsToggle = msg.getData().getByte(MainActivity.RDS_TOGGLE);
                rdsToggle(rdsToggle);
                break;
            case RequestCommand.AF_TOGGLE:
                byte afToggle = msg.getData().getByte(MainActivity.AF_TOGGLE);
                afToggle(afToggle);
                break;
            case RequestCommand.TA_TOGGLE:
                byte taToggle = msg.getData().getByte(MainActivity.TA_TOGGLE);
                taToggle(taToggle);
                break;
            case RequestCommand.EON_TOGGLE:
                byte eonToggle = msg.getData().getByte(MainActivity.EON_TOGGLE);
                eonToggle(eonToggle);
                break;
            case RequestCommand.REG_TOGGLE:
                byte regToggle = msg.getData().getByte(MainActivity.REG_TOGGLE);
                regToggle(regToggle);
                break;
            case RequestCommand.CT_TOGGLE:
                byte ctToggle = msg.getData().getByte(MainActivity.CT_TOGGLE);
                ctToggle(ctToggle);
                break;
            case RequestCommand.PTY_TOGGLE:
                byte ptytoggle = msg.getData().getByte(MainActivity.PTY_TOGGLE);
                ptyToggle(ptytoggle);
                break;
            case RequestCommand.PTY_SEARCH:
                byte ptySearch = msg.getData().getByte(MainActivity.PTY_SEARCH);
                ptySearch(ptySearch);
                break;
            // 源切换
            case RequestCommand.SWITCH_SOURCE:
                switchSource();
                break;
            case RequestCommand.EXIT_SOURCE:
                exitSource();
                break;
            default:
                super.handleMessage(msg);
        }
    };

    /**
     * 波段切换
     *
     * @param bandNum
     *            波段号
     */
    private void bandSwitch(byte bandNum) {

        if (bandNum < 0x00 || bandNum > 0x04) {
            // 波段号定义
            // #define BAND_FM1 (u8)0x00
            // #define BAND_FM2 (u8)0x01
            // #define BAND_FM3 (u8)0x02
            // #define BAND_AM1 (u8)0x03
            // #define BAND_AM2 (u8)0x04
            return;
        }

        int len = 2;
        byte[] buffer = new byte[len];
        buffer[0] = 0x01;
        buffer[1] = bandNum;

        setIntentExtra(new Intent(), len, buffer);
    }

    /**
     * 单步调节
     *
     * @param stepDirection
     *            单步调节方向：0x00:向上；0x01:向下
     */
    private void singleStep(byte stepDirection) {

        if (stepDirection < 0x00 || stepDirection > 0x01) {
            // 0x00:向上
            // 0x01:向下
            return;
        }

        int len = 2;
        byte[] buffer = new byte[len];
        buffer[0] = 0x02;
        buffer[1] = stepDirection;

        setIntentExtra(new Intent(), len, buffer);

    }

    /**
     * 搜索
     *
     * @param searchDirection
     *            搜索方向：0x00:向上；0x01:向下
     */
    private void search(byte searchDirection) {

        if (searchDirection < 0x00 || searchDirection > 0x01) {
            // 0x00: 向上
            // 0x01: 向下
            return;
        }

        int len = 2;
        byte[] buffer = new byte[len];
        buffer[0] = 0x03;
        buffer[1] = searchDirection;

        setIntentExtra(new Intent(), len, buffer);
    }

    /**
     * 频道号选取
     *
     * @param channelNum
     *            频道号：0x00:NULL 0x01~0x06 :1~6频道
     */
    private void channelChoose(byte channelNum) {

        if (channelNum < 0x00 || channelNum > 0x06) {
            // 0x00:NULL
            // 0x01~0x06 :1~6频道
            return;
        }

        int len = 2;
        byte[] buffer = new byte[len];
        buffer[0] = 0x04;
        buffer[1] = channelNum;

        setIntentExtra(new Intent(), len, buffer);
    }

    /**
     * 上下调频道
     *
     * @param adjustDirection
     *            调频道方向：0x00:上一频道；0x01:下一频道
     */
    private void adjustableChannel(byte adjustDirection) {

        if (adjustDirection < 0x00 || adjustDirection > 0x01) {
            // 0x00:上一频道
            // 0x01:下一频道
            return;
        }

        int len = 2;
        byte[] buffer = new byte[len];
        buffer[0] = 0x05;
        buffer[1] = adjustDirection;

        setIntentExtra(new Intent(), len, buffer);

    }

    /**
     * 存储预存频道频率
     *
     * @param channelNum
     *            频道号：0x00:NULL 0x01~0x06:1~6 频道
     */
    private void setPrestoreChannel(byte channelNum) {

        if (channelNum < 0x01 || channelNum > 0x06) {
            // 0x00:NULL
            // 0x01~0x06:1~6 频道
            return;
        }

        int len = 2;
        byte[] buffer = new byte[len];
        buffer[0] = 0x06;
        buffer[1] = channelNum;

        setIntentExtra(new Intent(), len, buffer);
    }

    /**
     * PS
     */
    private void ps() {
        int len = 1;
        byte[] buffer = new byte[len];
        buffer[0] = 0x07;

        setIntentExtra(new Intent(), len, buffer);
    }

    /**
     * AS
     */
    private void as() {

        int len = 1;
        byte[] buffer = new byte[len];
        buffer[0] = 0x08;

        setIntentExtra(new Intent(), len, buffer);

    }

    /**
     * LOC DX 切换
     */
    private void locOrDx() {

        int len = 1;
        byte[] buffer = new byte[len];
        buffer[0] = 0x09;

        setIntentExtra(new Intent(), len, buffer);

    }

    /**
     * 直接频率选取
     *
     * @param highByte
     *            频率高字节
     * @param lowByte
     *            频率低字节
     */
    private void directFreqChoose(byte highByte, byte lowByte) {

        int len = 3;
        byte[] buffer = new byte[len];
        buffer[0] = 0x0A;
        buffer[1] = highByte;
        buffer[2] = lowByte;

        setIntentExtra(new Intent(), len, buffer);

    }

    /**
     * RDS 开关
     * @param toggle 0x00: 关; 0x01: 开
     */
    private void rdsToggle(byte toggle) {

        int len = 3;
        byte[] buffer = new byte[len];
        buffer[0] = 0x0B;
        buffer[1] = toggle;

        setIntentExtra(new Intent(), len, buffer);

    }

    /**
     * TA 开关
     * @param toggle 0x00: 关; 0x01: 开
     */
    private void taToggle(byte toggle) {

        int len = 3;
        byte[] buffer = new byte[len];
        buffer[0] = 0x0C;
        buffer[1] = toggle;

        setIntentExtra(new Intent(), len, buffer);

    }

    /**
     * AF 开关
     * @param toggle 0x00: 关; 0x01: 开
     */
    private void afToggle(byte toggle) {

        int len = 3;
        byte[] buffer = new byte[len];
        buffer[0] = 0x0D;
        buffer[1] = toggle;

        setIntentExtra(new Intent(), len, buffer);

    }

    /**
     * pty 开关
     * @param toggle 0x00: 关; 0x01: 开
     */
    private void ptyToggle(byte toggle) {

        int len = 3;
        byte[] buffer = new byte[len];
        buffer[0] = 0x0E;
        buffer[1] = toggle;

        setIntentExtra(new Intent(), len, buffer);

    }

    /**
     * PTY 搜索
     * @param type PTY类型
     */
    private void ptySearch(byte type) {

        int len = 3;
        byte[] buffer = new byte[len];
        buffer[0] = 0x0F;
        buffer[1] = type;

        setIntentExtra(new Intent(), len, buffer);

    }

    /**
     * EON 开关
     * @param toggle 0x00: 关; 0x01: 开
     */
    private void eonToggle(byte toggle) {

        int len = 3;
        byte[] buffer = new byte[len];
        buffer[0] = 0x10;
        buffer[1] = toggle;

        setIntentExtra(new Intent(), len, buffer);

    }

    /**
     * REG 开关
     * @param toggle 0x00: 关; 0x01: 开
     */
    private void regToggle(byte toggle) {

        int len = 3;
        byte[] buffer = new byte[len];
        buffer[0] = 0x11;
        buffer[1] = toggle;

        setIntentExtra(new Intent(), len, buffer);

    }

    /**
     * CT 开关
     * @param toggle 0x00: 关; 0x01: 开
     */
    private void ctToggle(byte toggle) {

        int len = 3;
        byte[] buffer = new byte[len];
        buffer[0] = 0x12;
        buffer[1] = toggle;

        setIntentExtra(new Intent(), len, buffer);

    }

    /**
     * 打开源
     */
    private void switchSource() {
        int len = 2;
        byte[] buffer = new byte[len];
        buffer[0] = 0x01;
        buffer[1] = 0x01;

        Intent intent = new Intent(RequestCommand.SEND_TO_MCU_ACTION);
        intent.putExtra(RequestCommand.SEND_CMD_TO_MCU_CMD, RequestCommand.SWITCH_SOURCE_CMD);
        intent.putExtra(RequestCommand.SEND_CMD_TO_MCU_LEN, len);
        intent.putExtra(RequestCommand.SEND_CMD_TO_MCU_BUF, buffer);

        context.sendBroadcast(intent);

    }

    /**
     * 关闭源
     */
    private void exitSource() {
        int len = 2;
        byte[] buffer = new byte[len];
        buffer[0] = 0x01;
        buffer[1] = 0x01;

        Intent intent = new Intent(RequestCommand.SEND_TO_MCU_ACTION);
        intent.putExtra(RequestCommand.SEND_CMD_TO_MCU_CMD, RequestCommand.EXIT_SOURCE_CMD);
        intent.putExtra(RequestCommand.SEND_CMD_TO_MCU_LEN, len);
        intent.putExtra(RequestCommand.SEND_CMD_TO_MCU_BUF, buffer);
        intent.putExtra("quit", 0xFF);

        context.sendBroadcast(intent);

    }

    /**
     * 设置Intent附加数据并发送广播
     *
     * @param intent
     * @param len
     *            数据长度
     * @param buf
     *            数据缓冲
     */
    private void setIntentExtra(Intent intent, int len, byte[] buf) {

        intent.setAction(RequestCommand.SEND_TO_MCU_ACTION);

        intent.putExtra(RequestCommand.SEND_CMD_TO_MCU_CMD, RequestCommand.SEND_TO_MCU_CMD);
        intent.putExtra(RequestCommand.SEND_CMD_TO_MCU_LEN, len);
        intent.putExtra(RequestCommand.SEND_CMD_TO_MCU_BUF, buf);

        context.sendBroadcast(intent);
    }
}
