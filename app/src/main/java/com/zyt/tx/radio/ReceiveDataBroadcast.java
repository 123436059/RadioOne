package com.zyt.tx.radio; /**
 * Copyright(C) 2012,Kaizhen Electronic Co.,Ltd.
 * Author: Kaizhen
 * Company: Kaizhen Electronic Co.,Ltd
 * Version: 1.0
 * This file belongs to Kaizhen Electronic Co.,Ltd.
 * All rights reserved
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 接收来自MCU的广播
 *
 * @author Administrator
 *
 */
public class ReceiveDataBroadcast extends BroadcastReceiver {
    private static final String TAG = "ReceiveDataBroadcast";
    private static final boolean D = false;

    private Handler responseHandler;

    public ReceiveDataBroadcast(Handler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        int cmd = intent.getIntExtra(ResponseCommand.SEND_CMD_TO_APK_CMD, 0);

        if (D) {
            Log.d(TAG, "action = " + action);
            Log.d(TAG, "cmd = " + cmd);
            Log.d(TAG, "ResponseCommand.SEND_TO_RADIO_CMD = " + ResponseCommand.SEND_TO_RADIO_CMD);
        }

        if (ResponseCommand.TO_RADIO_APK_ACTION.equals(action) && ResponseCommand.SEND_TO_RADIO_CMD == cmd) {

            int len = intent.getIntExtra(ResponseCommand.SEND_CMD_TO_APK_LEN, 0);

            byte[] buffer = intent.getByteArrayExtra(ResponseCommand.SEND_CMD_TO_APK_BUF);

            if (len == 0 || buffer == null || buffer.length != len) {

                if (D) Log.d(TAG, "Conditions do not conform!");

                return;
            }

            // 0x01
            if (ResponseCommand.CURRENT_INFO_REPORT == buffer[0]) {

                // 字节序号 数值 意义
                // 0x02 0x01 当前信息报告
                // 0x03 0xNN 当前波段
                // 0x04 0xNN 当前频道 0代表无
                // 0x05 0xNN 当前频率高字节
                // 0x06 0xNN 当前频率低字节
                // 0x07 0xNN 当前信号强度
                // 0x08 0xNN RadioFlag

                // RadioFlag flag状态标志
                // 第0位： LOCAL/DX 1：LOC 0:DX
                // 第1位： ST
                // 第2位： Radio_Is_In_AS
                // 第3位： Radio_Is_In_PS
                // 第4位： Radio_Is_In_Seek，方向向上
                // 第5位： Radio_Is_In_Seek，方向向下

                int currentBand = buffer[1] & 0xFF;
                int currentChannel = buffer[2] & 0xFF;
                int currentFreq = ((buffer[3] << 8) & 0xFF00) | (buffer[4] & 0xFF);
                int currentSignal = buffer[5] & 0xFF;

                // buffer[6]的第0位
                boolean isLOC = false;
                if ((buffer[6] & 0x01) == 0x00) {
                    isLOC = false;
                } else if ((buffer[6] & 0x01) == 0x01) {
                    isLOC = true;
                }

                // buffer[6]的第1位
                boolean isST = false;
                if ((buffer[6] >>> 1 & 0x01) == 0x00) {
                    isST = false;
                } else if ((buffer[6] >>> 1 & 0x01) == 0x01) {
                    isST = true;
                }

                // buffer[6]的第2位
                boolean isRadioIsInAS = false;
                if ((buffer[6] >>> 2 & 0x01) == 0x00) {
                    isRadioIsInAS = false;
                } else if ((buffer[6] >>> 2 & 0x01) == 0x01) {
                    isRadioIsInAS = true;
                }

                // buffer[6]的第3位
                boolean isRadioIsInPS = false;
                if ((buffer[6] >>> 3 & 0x01) == 0x00) {
                    isRadioIsInPS = false;
                } else if ((buffer[6] >>> 3 & 0x01) == 0x01) {
                    isRadioIsInPS = true;
                }

                // buffer[6]的第4位
                boolean isRadioIsInSeekUp = false;
                if ((buffer[6] >>> 4 & 0x01) == 0x00) {
                    isRadioIsInSeekUp = false;
                } else if ((buffer[6] >>> 4 & 0x01) == 0x01) {
                    isRadioIsInSeekUp = true;
                }

                // buffer[6]的第5位
                boolean isRadioIsInSeekDown = false;
                if ((buffer[6] >>> 5 & 0x01) == 0x00) {
                    isRadioIsInSeekDown = false;
                } else if ((buffer[6] >>> 5 & 0x01) == 0x01) {
                    isRadioIsInSeekDown = true;
                }

                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.CURRENT_BAND, currentBand);
                bundle.putInt(MainActivity.CURRENT_CHANNEL, currentChannel);
                bundle.putInt(MainActivity.CURRENT_FREQ, currentFreq);
                bundle.putInt(MainActivity.CURRENT_SIGNAL, currentSignal);
                bundle.putBoolean(MainActivity.LOC, isLOC);
                bundle.putBoolean(MainActivity.ST, isST);
                bundle.putBoolean(MainActivity.RADIO_IS_IN_AS, isRadioIsInAS);
                bundle.putBoolean(MainActivity.RADIO_IS_IN_PS, isRadioIsInPS);
                bundle.putBoolean(MainActivity.RADIO_IS_IN_SEEK_UP, isRadioIsInSeekUp);
                bundle.putBoolean(MainActivity.ISRADIO_IS_IN_SEEK_DOWN, isRadioIsInSeekDown);

                Message msg = responseHandler.obtainMessage();
                msg.what = ResponseCommand.CURRENT_INFO_REPORT;
                msg.setData(bundle);
                responseHandler.sendMessage(msg);

            }

            // 0x02
            if (ResponseCommand.LIST_INFO == buffer[0]) {

                // 字节序号 数值 意义 字节数
                // 0x02 0x02 列表信息 0
                // 0x03~0x04 0xNNNN FM1 PRESET1 频率 1~2
                // 0x05~0x06 0xNNNN FM1 PRESET2 频率 3~4
                // 0x07~0x08 0xNNNN FM1 PRESET3 频率 5~6
                // 0x09~0x0A 0xNNNN FM1 PRESET4 频率 7~8
                // 0x0B~0x0C 0xNNNN FM1 PRESET5 频率 9~10
                // 0x0D~0x0E 0xNNNN FM1 PRESET6 频率 11~12
                // 0x0F~0x10 0xNNNN FM2 PRESET1 频率 13~14
                // 0x0F~0x10 0xNNNN FM2 PRESET2 频率 15~16
                // 0x11~0x12 0xNNNN FM2 PRESET3 频率 17~18
                // 0x13~0x14 0xNNNN FM2 PRESET4 频率 19~20
                // 0x15~0x16 0xNNNN FM2 PRESET5 频率 21~22
                // 0x17~0x18 0xNNNN FM2 PRESET6 频率 23~24
                // 0x19~0x1A 0xNNNN FM3 PRESET1 频率 25~26
                // 0x1B~0x1C 0xNNNN FM3 PRESET2 频率 27~28
                // 0x1D~0x1E 0xNNNN FM3 PRESET3 频率 29~30
                // 0x1F~0x20 0xNNNN FM3 PRESET4 频率 31~32
                // 0x21~0x22 0xNNNN FM3 PRESET5 频率 33~34
                // 0x23~0x24 0xNNNN FM3 PRESET6 频率 35~36
                // 0x25~0x26 0xNNNN AM1 PRESET1 频率 37~38
                // 0x27~0x28 0xNNNN AM1 PRESET2 频率 39~40
                // 0x29~0x2A 0xNNNN AM1 PRESET3 频率 41~42
                // 0x2B~0x2C 0xNNNN AM1 PRESET4 频率 43~44
                // 0x2D~0x2E 0xNNNN AM1 PRESET5 频率 45~46
                // 0x2F~0x30 0xNNNN AM1 PRESET6 频率 47~48
                // 0x31~0x32 0xNNNN AM2 PRESET1 频率 49~50
                // 0x33~0x34 0xNNNN AM2 PRESET2 频率 51~52
                // 0x35~0x36 0xNNNN AM2 PRESET3 频率 53~54
                // 0x37~0x38 0xNNNN AM2 PRESET4 频率 55~56
                // 0x39~0x3A 0xNNNN AM2 PRESET5 频率 57~58
                // 0x3B~0x3C 0xNNNN AM2 PRESET6 频率 59~60

                int fm1_p1 = ((buffer[1] << 8) & 0xFF00) | (buffer[2] & 0xFF);
                int fm1_p2 = ((buffer[3] << 8) & 0xFF00) | (buffer[4] & 0xFF);
                int fm1_p3 = ((buffer[5] << 8) & 0xFF00) | (buffer[6] & 0xFF);
                int fm1_p4 = ((buffer[7] << 8) & 0xFF00) | (buffer[8] & 0xFF);
                int fm1_p5 = ((buffer[9] << 8) & 0xFF00) | (buffer[10] & 0xFF);
                int fm1_p6 = ((buffer[11] << 8) & 0xFF00) | (buffer[12] & 0xFF);

                int fm2_p1 = ((buffer[13] << 8) & 0xFF00) | (buffer[14] & 0xFF);
                int fm2_p2 = ((buffer[15] << 8) & 0xFF00) | (buffer[16] & 0xFF);
                int fm2_p3 = ((buffer[17] << 8) & 0xFF00) | (buffer[18] & 0xFF);
                int fm2_p4 = ((buffer[19] << 8) & 0xFF00) | (buffer[20] & 0xFF);
                int fm2_p5 = ((buffer[21] << 8) & 0xFF00) | (buffer[22] & 0xFF);
                int fm2_p6 = ((buffer[23] << 8) & 0xFF00) | (buffer[24] & 0xFF);

                int fm3_p1 = ((buffer[25] << 8) & 0xFF00) | (buffer[26] & 0xFF);
                int fm3_p2 = ((buffer[27] << 8) & 0xFF00) | (buffer[28] & 0xFF);
                int fm3_p3 = ((buffer[29] << 8) & 0xFF00) | (buffer[30] & 0xFF);
                int fm3_p4 = ((buffer[31] << 8) & 0xFF00) | (buffer[32] & 0xFF);
                int fm3_p5 = ((buffer[33] << 8) & 0xFF00) | (buffer[34] & 0xFF);
                int fm3_p6 = ((buffer[35] << 8) & 0xFF00) | (buffer[36] & 0xFF);

                int am1_p1 = ((buffer[37] << 8) & 0xFF00) | (buffer[38] & 0xFF);
                int am1_p2 = ((buffer[39] << 8) & 0xFF00) | (buffer[40] & 0xFF);
                int am1_p3 = ((buffer[41] << 8) & 0xFF00) | (buffer[42] & 0xFF);
                int am1_p4 = ((buffer[43] << 8) & 0xFF00) | (buffer[44] & 0xFF);
                int am1_p5 = ((buffer[45] << 8) & 0xFF00) | (buffer[46] & 0xFF);
                int am1_p6 = ((buffer[47] << 8) & 0xFF00) | (buffer[48] & 0xFF);

                int am2_p1 = ((buffer[49] << 8) & 0xFF00) | (buffer[50] & 0xFF);
                int am2_p2 = ((buffer[51] << 8) & 0xFF00) | (buffer[52] & 0xFF);
                int am2_p3 = ((buffer[53] << 8) & 0xFF00) | (buffer[54] & 0xFF);
                int am2_p4 = ((buffer[55] << 8) & 0xFF00) | (buffer[56] & 0xFF);
                int am2_p5 = ((buffer[57] << 8) & 0xFF00) | (buffer[58] & 0xFF);
                int am2_p6 = ((buffer[59] << 8) & 0xFF00) | (buffer[60] & 0xFF);

                int[] fm1 = { fm1_p1, fm1_p2, fm1_p3, fm1_p4, fm1_p5, fm1_p6 };
                int[] fm2 = { fm2_p1, fm2_p2, fm2_p3, fm2_p4, fm2_p5, fm2_p6 };
                int[] fm3 = { fm3_p1, fm3_p2, fm3_p3, fm3_p4, fm3_p5, fm3_p6 };
                int[] am1 = { am1_p1, am1_p2, am1_p3, am1_p4, am1_p5, am1_p6 };
                int[] am2 = { am2_p1, am2_p2, am2_p3, am2_p4, am2_p5, am2_p6 };

                Bundle bundle = new Bundle();
                bundle.putIntArray(MainActivity.FM1, fm1);
                bundle.putIntArray(MainActivity.FM2, fm2);
                bundle.putIntArray(MainActivity.FM3, fm3);
                bundle.putIntArray(MainActivity.AM1, am1);
                bundle.putIntArray(MainActivity.AM2, am2);

                Message msg = responseHandler.obtainMessage();
                msg.what = ResponseCommand.LIST_INFO;
                msg.setData(bundle);
                responseHandler.sendMessage(msg);
            }

            // 0x03
            if (ResponseCommand.CURRENT_BND_FREQ_RANGE == buffer[0]) {

                // 字节序号 数值 意义 字节数
                // 0x02 0x03 当前波段频率范围 0
                // 0x03~0x04 0xNNNN 最小频率 1~2
                // 0x05~0x06 0xNNNN 最大频率 3~4
                // 0x07 0xNN 步进频率 5

                int minFreq = ((buffer[1] << 8) & 0xFF00) | (buffer[2] & 0xFF);
                int maxFreq = ((buffer[3] << 8) & 0xFF00) | (buffer[4] & 0xFF);
                int stepByStepFreq = buffer[5] & 0xFF;

                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.MIN_FREQ, minFreq);
                bundle.putInt(MainActivity.MAX_FREQ, maxFreq);
                bundle.putInt(MainActivity.STEP_BY_STEP_FREQ, stepByStepFreq);

                Message msg = responseHandler.obtainMessage();
                msg.what = ResponseCommand.CURRENT_BND_FREQ_RANGE;
                msg.setData(bundle);
                responseHandler.sendMessage(msg);
            }

            // 0x04
            if (ResponseCommand.RDS_INFO == buffer[0]) {

                // 字节序号 数值 意义
                // 0x02 0x04 RDS信息
                // 0x03 0xNN RDSFlag0
                // 0x04 0xNN RDSFlag1
                // 0x05 0xNN RDSFlag2
                // 0x06 0xNN RDSFlag3

                // RDSFlag0
                // 第 0 位 F_RDS_SWITCH //RDS 电源开关
                // 第 1 位 F_TA_SWITCH //TA 开关
                // 第 2 位 F_AF_SWITCH //AF开关
                // 第 3位 F_PTY_SWITCH //PTY开关
                // 第 4 位 F_EON_SWITCH //EON 开关
                // 第 5 位 F_REG_SWITCH //REG 开关
                // 第 6 位 F_CT_SWITCH //CT 开关
                // RDSFlag1
                // 预留
                // RDSFlag2
                // 第0位 F_RDS_INFO //RDS电台指示 0=Not RDS station; 1=RDS station
                // 第1位 F_TP_INFO //TP指示
                // 第2位 F_TA_INFO //Traffic指示
                // 第3位 F_EWS_INFO //EWS指示
                // 第4位 F_PTY_FLASH //正在搜索 PTY 电台
                // RDSFlag3
                // 预留

                // buffer[1](RDSFlag0)的第0位
                boolean isRDSPowerOpen = false;
                if ((buffer[1] & 0x01) == 0x00) {
                    isRDSPowerOpen = false;
                } else if ((buffer[1] & 0x01) == 0x00) {
                    isRDSPowerOpen = true;
                }

                // buffer[1](RDSFlag0)的第1位
                boolean isTAOpen = false;
                if ((buffer[1] >>> 1 & 0x01) == 0x00) {
                    isTAOpen = false;
                } else if ((buffer[1] >>> 1 & 0x01) == 0x01) {
                    isTAOpen = true;
                }

                // buffer[1](RDSFlag0)的第2位
                boolean isAFOpen = false;
                if ((buffer[1] >>> 2 & 0x01) == 0x00) {
                    isAFOpen = false;
                } else if ((buffer[1] >>> 2 & 0x01) == 0x01) {
                    isAFOpen = true;
                }

                // buffer[1](RDSFlag0)的第3位
                boolean isPTYOpen = false;
                if ((buffer[1] >>> 3 & 0x01) == 0x00) {
                    isPTYOpen = false;
                } else if ((buffer[1] >>> 3 & 0x01) == 0x01) {
                    isPTYOpen = true;
                }

                // buffer[1](RDSFlag0)的第4位
                boolean isEONOepn = false;
                if ((buffer[1] >>> 4 & 0x01) == 0x00) {
                    isEONOepn = false;
                } else if ((buffer[1] >>> 4 & 0x01) == 0x01) {
                    isEONOepn = true;
                }

                // buffer[1](RDSFlag0)的第5位
                boolean isREGOpen = false;
                if ((buffer[1] >>> 5 & 0x01) == 0x00) {
                    isREGOpen = false;
                } else if ((buffer[1] >>> 5 & 0x01) == 0x01) {
                    isREGOpen = true;
                }

                // buffer[1](RDSFlag0)的第6位
                boolean isCTOpen = false;
                if ((buffer[1] >>> 6 & 0x01) == 0x00) {
                    isCTOpen = false;
                } else if ((buffer[1] >>> 6 & 0x01) == 0x01) {
                    isCTOpen = true;
                }

                // buffer[3](RDSFlag2)的第1位
                boolean isRDSStation = false;
                if ((buffer[3] & 0x01) == 0x00) {
                    isRDSStation = false;
                } else if ((buffer[1] & 0x01) == 0x01) {
                    isRDSStation = true;
                }

                // buffer[3](RDSFlag2)的第2位
                boolean isTPIndicate = false;
                if ((buffer[3] >>> 1 & 0x01) == 0x00) {
                    isTPIndicate = false;
                } else if ((buffer[3] >>> 1 & 0x01) == 0x01) {
                    isTPIndicate = true;
                }

                // buffer[3](RDSFlag2)的第3位
                boolean isTrafficIndicate = false;
                if ((buffer[3] >>> 2 & 0x01) == 0x00) {
                    isTrafficIndicate = false;
                } else if ((buffer[3] >>> 2 & 0x01) == 0x01) {
                    isTrafficIndicate = true;
                }

                // buffer[3](RDSFlag2)的第4位
                boolean isEWSIndicate = false;
                if ((buffer[3] >>> 3 & 0x01) == 0x00) {
                    isEWSIndicate = false;
                } else if ((buffer[3] >>> 3 & 0x01) == 0x01) {
                    isEWSIndicate = true;
                }

                // buffer[3](RDSFlag2)的第5位
                boolean isNowSearchPTYStation = false;
                if ((buffer[3] >>> 4 & 0x01) == 0x00) {
                    isNowSearchPTYStation = false;
                } else if ((buffer[3] >>> 4 & 0x01) == 0x01) {
                    isNowSearchPTYStation = true;
                }

                Bundle bundle = new Bundle();
                bundle.putBoolean(MainActivity.F_RDS_SWITCH, isRDSPowerOpen);
                bundle.putBoolean(MainActivity.F_TA_SWITCH, isTAOpen);
                bundle.putBoolean(MainActivity.F_AF_SWITCH, isAFOpen);
                bundle.putBoolean(MainActivity.F_PTY_SWITCH, isPTYOpen);
                bundle.putBoolean(MainActivity.F_EON_SWITCH, isEONOepn);
                bundle.putBoolean(MainActivity.F_REG_SWITCH, isREGOpen);
                bundle.putBoolean(MainActivity.F_CT_SWITCH, isCTOpen);

                bundle.putBoolean(MainActivity.F_RDS_INFO, isRDSStation);
                bundle.putBoolean(MainActivity.F_TP_INFO, isTPIndicate);
                bundle.putBoolean(MainActivity.F_TA_INFO, isTrafficIndicate);
                bundle.putBoolean(MainActivity.F_EWS_INFO, isEWSIndicate);
                bundle.putBoolean(MainActivity.F_PTY_FLASH, isNowSearchPTYStation);

                Message msg = responseHandler.obtainMessage();
                msg.what = ResponseCommand.RDS_INFO;
                msg.setData(bundle);
                responseHandler.sendMessage(msg);
            }

            // 0x05
            if (ResponseCommand.PS_PTY == buffer[0]) {
                // 字节序号 数值 意义
                // 0x02 0x05 PS & PTY
                // 0x03~0x0A 0xNN~0xNN PS Name 8Bytes
                // 0x0B 0x00 截止符’\0’
                // 0x0C 0xNN PTY
                String psName = new String(buffer, 1, 8);
                byte pty = buffer[10];

                Bundle bundle = new Bundle();
                bundle.putString(MainActivity.PS_NAME, psName);
                bundle.putByte(MainActivity.PTY, pty);

                Message msg = responseHandler.obtainMessage();
                msg.what = ResponseCommand.PS_PTY;
                msg.setData(bundle);
                responseHandler.sendMessage(msg);
            }

            // 0x06
            if (ResponseCommand.RT == buffer[0]) {
                // 字节序号 数值 意义
                // 0x02 0x06 RT
                // 0x03~0x42 0xNN~0xNN RT 64Bytes
                // 0x43 0x00 截止符’\0’
                String rt = new String(buffer, 1, 64);

                Bundle bundle = new Bundle();
                bundle.putString(MainActivity.RT, rt);

                Message msg = responseHandler.obtainMessage();
                msg.what = ResponseCommand.RT;
                msg.setData(bundle);
                responseHandler.sendMessage(msg);
            }

        }
    }
}
