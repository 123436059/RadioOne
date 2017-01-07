package com.zyt.tx.radio; /**
 * Copyright(C) 2012,Kaizhen Electronic Co.,Ltd.
 * Author: Kaizhen 
 * Company: Kaizhen Electronic Co.,Ltd
 * Version: 1.0
 * This file belongs to Kaizhen Electronic Co.,Ltd. 
 * All rights reserved
 */


import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class ResponseHandler extends Handler {
    private static final String TAG = "ResponseHandler";
    private static final boolean DEBUG = false;
    private InterResponseMsg resopnseService;

    public ResponseHandler(Context activity) {
        this.resopnseService = (InterResponseMsg) activity;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case ResponseCommand.CURRENT_INFO_REPORT:
                resopnseService.updateCurrentInfoReport(msg.getData());
                break;
            case ResponseCommand.LIST_INFO:
                resopnseService.updateListInfo(msg.getData());
                break;
            case ResponseCommand.CURRENT_BND_FREQ_RANGE:
                resopnseService.updateCurrentBndFreqRange(msg.getData());
                break;
            case ResponseCommand.RDS_INFO:
                resopnseService.updateRdsInfo(msg.getData());
                break;
            case ResponseCommand.PS_PTY:
                resopnseService.updatePsAndPTY(msg.getData());
                break;
            case ResponseCommand.RT:
                resopnseService.updateRT(msg.getData());
                break;
            case ResponseCommand.UPDATE_DIGIT:
                resopnseService.updateDigitView(msg.arg1);
                break;
            default:
                super.handleMessage(msg);
        }
    };

}
