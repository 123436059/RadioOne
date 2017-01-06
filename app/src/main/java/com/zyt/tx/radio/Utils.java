package com.zyt.tx.radio;

/**
 * Created by MJS on 2017/1/6.
 */

public class Utils {

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
}
