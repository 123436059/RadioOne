package com.zyt.tx.radio;

/**
 * Copyright(C) 2012,Kaizhen Electronic Co.,Ltd.
 * Author: Kaizhen
 * Company: Kaizhen Electronic Co.,Ltd
 * Version: 1.0
 * This file belongs to Kaizhen Electronic Co.,Ltd.
 * All rights reserved
 */


public class ResponseCommand {

	/* example: */
	/* Intent intent = new Intent(TO_RADIO_APK_ACTION); */
	/* intent.putExtra(SEND_CMD_TO_APK_BUF,value); 参数为数组byte[] value类型 */
	/* intent.putExtra(SEND_CMD_TO_APK_LEN,len);参数为int类型 */

    public static final String TO_RADIO_APK_ACTION = "com.android.hklt.kzcar.radio";

    public static final int SEND_TO_RADIO_CMD = 0xD1;

    /*接收按键的CMD*/
    public static final int MEDIA_BUTTON_SEND_TO_RADIO_CMD = 0x20;

    public static final String SEND_CMD_TO_APK_CMD = "receivecmd";
    public static final String SEND_CMD_TO_APK_LEN = "receivelen";
    public static final String SEND_CMD_TO_APK_BUF = "receivebuf";

    /* 当前信息报告 */
    public static final int CURRENT_INFO_REPORT = 0x01;

    /* 列表信息 */
    public static final int LIST_INFO = 0x02;

    /* 当前波段频率范围 */
    public static final int CURRENT_BND_FREQ_RANGE = 0x03;

    /* RDS 信息 */
    public static final int RDS_INFO = 0x04;

    /* PS & PTY */
    public static final int PS_PTY = 0x05;

    /* RT */
    public static final int RT = 0x06;

    /*响应视图消息*/
    public static final int UPDATE_DIGIT = 0x0100;
}
