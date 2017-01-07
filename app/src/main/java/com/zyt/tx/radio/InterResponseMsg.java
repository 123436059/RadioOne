package com.zyt.tx.radio;

import android.os.Bundle;

/**
 * Created by MJS on 2017/1/7.
 */

public interface InterResponseMsg {
    void updateCurrentInfoReport(Bundle bundle);

    /**
     * 更新常用列表
     * @param bundle
     */
    void updateListInfo(Bundle bundle);

    /**
     * 更新当前波段范围
     * @param bundle
     */
    void updateCurrentBndFreqRange(Bundle bundle);

    void updateRdsInfo(Bundle bundle);


    /**
     * 更新ps和PTY
     * @param bundle
     */
    void updatePsAndPTY(Bundle bundle);

    void updateRT(Bundle bundle);

    void updateDigitView(int digit);

}
