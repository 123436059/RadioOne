package com.zyt.tx.radio;

/**
 * Copyright(C) 2012,Kaizhen Electronic Co.,Ltd.
 * Author: Kaizhen
 * Company: Kaizhen Electronic Co.,Ltd
 * Version: 1.0
 * This file belongs to Kaizhen Electronic Co.,Ltd.
 * All rights reserved
 */


public class RequestCommand {

	/* example: */
	/* Intent intent = new Intent(SEND_TO_MCU_ACTION); */
	/* int length = 0x02; */
	/* int cmd = 0x50; */
	/* byte [] buf = new */
	/* byte[length]; */
	/* buf[0]=0x01; */
	/* buf[1]=0x08; */
	/* intent.putExtra("sendlen", length); */
	/* intent.putExtra("sendcmd", cmd); */
	/* intent.putExtra("sendbuf", buf); */

    /* SEND_TO_MCU_ACTION是发送指令到MCU的ACTION */
    public static final String SEND_TO_MCU_ACTION = "com.android.hklt.kzcar.mpu_to_mcu";
    public static final int SEND_TO_MCU_CMD = 0x51;

    public static final int SWITCH_SOURCE_CMD = 0x50;

    public static final int EXIT_SOURCE_CMD = 0xfd;

    /* 参数为int类型 */
    public static final String SEND_CMD_TO_MCU_CMD = "sendcmd";

    /* 参数为int类型 */
    public static final String SEND_CMD_TO_MCU_LEN = "sendlen";

    /* 参数为byte []数组 */
    public static final String SEND_CMD_TO_MCU_BUF = "sendbuf";

    // function constant

    /* 波段切换命令 */
    public static final int BAND_SWITCH = 0x01;

    /* 单步 */
    public static final int SINGLE_STEP = 0x02;

    /* 搜索 */
    public static final int SEARCH = 0x03;

    /* 频道号选取,0x00:NULL 0x01~0x06 :1~6频道 */
    public static final int CHANNEL_CHOOSE = 0x04;

    /* 上下调节频道号（当前频道上下）,0x00:上一频道 0x01:下一频道 */
    public static final int ADJUSTABLE_CHANNEL = 0x05;

    /* 存储预存频道频率 */
    public static final int SET_PRESTORE_CHANNEL = 0x06;

    /* PS */
    public static final int PS = 0x07;

    /* AS */
    public static final int AS = 0x08;

    /* LOC DX切换 */
    public static final int LOC_DX = 0x09;

    /* 直接频率选取 */
    public static final int DIRECT_FREQ_CHOOSE = 0x0A;

    /* RDS 开关 */
    public static final int RDS_TOGGLE = 0x0B;

    /* TA开关 */
    public static final int TA_TOGGLE = 0x0C;

    /* AF开关 */
    public static final int AF_TOGGLE = 0x0D;

    /* PTY开关 */
    public static final int PTY_TOGGLE = 0x0E;

    /* PTY搜索 */
    public static final int PTY_SEARCH = 0x0F;

    /* EON开关 */
    public static final int EON_TOGGLE = 0x10;

    /* REG开关 */
    public static final int REG_TOGGLE = 0x11;

    /* CT开关 */
    public static final int CT_TOGGLE = 0x12;

    /*自己的常量*/
    public static final int EXIT_SOURCE = 0x0F00; // 关闭源
    public static final int SWITCH_SOURCE = 0x0F01; // 打开源
}
