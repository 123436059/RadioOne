package com.zyt.tx.radio;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class FreqView extends View {

    Bitmap[] freqs;
    Context mContext;
    private Paint mPaint;
    private Paint mFreqValuePaint;
    private Bitmap indicator;
    private Bitmap freqBg;
    private int bitmapWidth;
    private int bitmapHeight;
    private int rulerWidth;
    private int rulerHeight;
    private int indicatorWidth;
    private int indicatorHeight;

    private int currentBand = 0;
    private int currentFreqHz = 8750;
    private int currentBitmapIndex = 0;
    private int BOTTOM_OFFSET = 2;

    /*
    标尺中的每一小格距
     */
    private int CELL = 14;

    /*
    刻度长3个像素
     */
    private int MARK = 2;
    private int firstDrawFreqHz = 0;

    public FreqView(Context context) {
        this(context, null);
    }

    public FreqView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FreqView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        freqs = new Bitmap[5];
        Resources res = mContext.getResources();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFreqValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mFreqValuePaint.setColor(Color.WHITE);
        mFreqValuePaint.setTextSize(16);
        mFreqValuePaint.setTextAlign(Paint.Align.CENTER);

        freqs[0] = BitmapFactory.decodeResource(res, R.drawable.freq1);
        freqs[1] = BitmapFactory.decodeResource(res, R.drawable.freq2);
        freqs[2] = BitmapFactory.decodeResource(res, R.drawable.freq3);
        freqs[3] = BitmapFactory.decodeResource(res, R.drawable.freq4);
        freqs[4] = BitmapFactory.decodeResource(res, R.drawable.freq5);

        indicator = BitmapFactory.decodeResource(res, R.drawable.indicator);
        freqBg = BitmapFactory.decodeResource(res, R.drawable.freq_bg);

        bitmapWidth = freqBg.getWidth();
        bitmapHeight = freqBg.getHeight();
        rulerWidth = freqs[0].getWidth();
        rulerHeight = freqs[0].getHeight();
        indicatorWidth = indicator.getWidth();
        indicatorHeight = indicator.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(bitmapWidth, bitmapHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (currentBand == 0 || currentBand == 1 || currentBand == 2) {
            drawFM(canvas);
        } else if (currentBand == 3 || currentBand == 4) {
            drawAM(canvas);
        }
    }

    private void drawAM(Canvas canvas) {
        setAMCurrentBitmapIndex();
        canvas.drawBitmap(freqs[currentBitmapIndex], (bitmapWidth - rulerWidth) / 2
                , bitmapHeight - rulerHeight - BOTTOM_OFFSET, mPaint);
        int markCount = getAMCountMarkRightReq();
        int midPositionX = bitmapWidth / 2 + markCount * CELL + (markCount - 1) * MARK;
        int midPositionY = (int) (bitmapHeight - rulerHeight - mFreqValuePaint.getTextSize());
        int firstDrawFreq = getAMFirstDrawFreq();

    }


    /**
     * 得到AM第一个刻画频率
     *
     * @return
     */
    private int getAMFirstDrawFreq() {
        int digitAM = Utils.getDigitValueFromFreq(currentFreqHz, 1);
        if (digitAM == 0)
            firstDrawFreqHz = currentFreqHz;

        if (digitAM < 5 && digitAM > 0)
            firstDrawFreqHz = (int) (Math.floor((currentFreqHz + 10) / 10) * 10);

        if (digitAM >= 5)
            firstDrawFreqHz = (int) (Math.floor((currentFreqHz + 5) / 10) * 10);

        return firstDrawFreqHz;
    }

    private int getAMCountMarkRightReq() {
        int digit = Utils.getDigitValueFromFreq(currentFreqHz, 1);
        int countMark = 10 - digit;
        return countMark == 10 ? 0 : countMark;
    }

    private void setAMCurrentBitmapIndex() {
        int digit = Utils.getDigitValueFromFreq(currentFreqHz, 1);
        currentBitmapIndex = digit % 5;
    }

    private void drawFM(Canvas canvas) {

    }
}
