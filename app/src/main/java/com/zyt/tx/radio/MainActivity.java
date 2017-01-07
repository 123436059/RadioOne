package com.zyt.tx.radio;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends BaseRadioActivity implements InterResponseMsg{

    private FreqView mFreqView;
    private Resources res;
    private Button eq_btn;
    private Button rds_btn;
    private Button band;
    private Button backward_station;
    private Button forward_station;
    private Button button_seek_backward;
    private Button button_seek_forward;
    private ImageView signal_indicate_iv;
    private ImageView loc_indicate_iv;
    private ImageView st_indicate_iv;
    private ImageView ib_digit_thousand;
    private ImageView ib_digit_hundred;
    private ImageView ib_digit_ten;
    private ImageView ib_digit_unit;
    private ImageView ib_digit_dot;
    private ImageView ib_digit_decimal_ten;
    private ImageView ib_digit_decimal_hundred;
    private TextView freq_range;
    private TextView freq;

    private Button[] common_stations = new Button[6];
    private Button af_btn;
    private Button ta_btn;
    private Button pty_btn;
    private TextView rt_tv;
    private TextView ps_tv;
    private TextView ta_indicate;
    private TextView tp_indicate;
    private TextView af_indicate;
    private TextView pty_indicate;
    private LinearLayout indicator_layout;
    private LinearLayout rt_ps_layout;
    private LinearLayout rds_layout;
    private LinearLayout stations_layout;


    //-----------

    private static final String EQ_ACTION = "com.kz.eq";
    public static final String RADIO = "radio";

    // ------------------------------------------------------MCU->MPU Bundle 的Key
    // 0x01
    public static final String CURRENT_BAND = "current_band";
    public static final String CURRENT_CHANNEL = "current_channel";
    public static final String CURRENT_FREQ = "current_freq";
    public static final String CURRENT_SIGNAL = "current_signal";
    public static final String LOC = "is_loc";
    public static final String ST = "is_st";
    public static final String RADIO_IS_IN_AS = "is_radio_is_in_AS";
    public static final String RADIO_IS_IN_PS = "is_radio_is_in_PS";
    public static final String RADIO_IS_IN_SEEK_UP = "is_radio_is_in_seek_up";
    public static final String ISRADIO_IS_IN_SEEK_DOWN = "is_Radio_is_in_seek_down";

    // 0x02
    public static final String FREQS = "freqs";
    public static final String FM1 = "fm1";
    public static final String FM2 = "fm2";
    public static final String FM3 = "fm3";
    public static final String AM1 = "am1";
    public static final String AM2 = "am2";

    // 0x03
    public static final String MIN_FREQ = "min_freq";
    public static final String MAX_FREQ = "max_freq";
    public static final String STEP_BY_STEP_FREQ = "step_by_step_freq";

    // 0x04中的RDSFlag0标志位
    public static final String F_RDS_SWITCH = "is_RDS_power_open"; // RDS 电源开关
    public static final String F_TA_SWITCH = "is_TA_open"; // TA 开关
    public static final String F_AF_SWITCH = "is_AF_open"; // AF开关
    public static final String F_PTY_SWITCH = "is_PTY_open"; // PTY开关
    public static final String F_EON_SWITCH = "is_EON_oepn"; // EON 开关
    public static final String F_REG_SWITCH = "is_REG_open"; // REG 开关
    public static final String F_CT_SWITCH = "is_CT_open";// CT 开关

    // 0x04中的RDSFlag2标志位
    public static final String F_RDS_INFO = "is_RDS_station"; // RDS电台指示 0=Not RDS station; 1=RDS station
    public static final String F_TP_INFO = "is_TP_indicate"; // TP指示
    public static final String F_TA_INFO = "is_Traffic_indicate"; // Traffic指示
    public static final String F_EWS_INFO = "is_EWS_indicate"; // EWS指示
    public static final String F_PTY_FLASH = "is_now_search_PTY_station"; // 正在搜索 PTY 电台

    // 0x05
    public static final String PS_NAME = "ps_name";
    public static final String PTY = "pty";

    // 0x06
    public static final String RT = "rt";

    // ------------------------------------------------------MPU->MCU Bundle 的Key
    // 0x01
    public static final String BAND_SWITCH = "bnd_num";
    // 0x02
    public static final String SIGLE_STEP = "up_or_down";
    // 0x03
    public static final String SEARCH = "up_or_down";
    // 0x04
    public static final String CHANNEL_CHOOSE = "channel_num";
    // 0x05 没有用的功能
    public static final String ADJUSTABLE_CHANNEL = "up_or_down_channel";
    // 0x06
    public static final String SET_PRESTORE_CHANNEL = "store_channel_num";
    // 0x0A
    public static final String LOW_BYTE = "low_byte";
    public static final String HIGH_BYTE = "high_byte";
    // 0x0B
    public static final String RDS_TOGGLE = "rdsToggle";
    public static final String AF_TOGGLE = "afToggle";
    public static final String TA_TOGGLE = "taToggle";
    public static final String EON_TOGGLE = "eonToggle";
    public static final String REG_TOGGLE = "regToggle";
    public static final String CT_TOGGLE = "ctToggle";
    public static final String PTY_TOGGLE = "ptytoggle";
    public static final String PTY_SEARCH = "ptySearch";
    /*------------------------------------------------------------------------------------------*/
    public static boolean isOnPause = true;
    private boolean isRDSPowerOpen = false;
    private boolean isTAOpen = false;
    private boolean isAFOpen = false;
    private boolean isPTYOpen = false;
    private boolean isEONOepn = false;
    private boolean isREGOpen = false;
    private boolean isCTOpen = false;
    private PtyPopup ptyPopup;
    private MenuPop menuPop;
    private int currentFreq=0;
    private Keyboard mKeyboard;
    private boolean isRDSStation;
    private boolean hasRdsDev;
    private ReceiveDataBroadcast receiveDataBroadcast;
    private IntentFilter receiveDataFilter;

    //-----------



    private static Thread mBussinessThread = null;
    // private boolean mIsBussinessThreadWaiting = false;
    private static boolean mBussinessThreadStarted = false;
    public static BussinessThreadHandler mBussinessThreadHandler = null;

    public class BussinessThreadHandler extends Handler {

        public boolean sendMessage(int what, int arg1, int arg2) {
            removeMessages(what);
            return super.sendMessage(obtainMessage(what, arg1, arg2));
        }

        public void handleMessage(Message msg) {
            // Do whatever bussiness here
            switch (msg.what) {
                case MediaKeyReceiver.FRESH_NOTIFY:
                    int currentBand = msg.arg1;
                    int currentFreq = msg.arg2;
                    Utils.addNotification(MainActivity.this, currentBand, currentFreq);
                    break;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        startBusinessThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 源切换
        requestHandler.sendEmptyMessage(RequestCommand.SWITCH_SOURCE);
        // 注册广播
        receiveDataBroadcast = new ReceiveDataBroadcast(responseHandler);
        registerReceiver(receiveDataBroadcast, getIntentFilter());
        isOnPause = false;
        HashMap<String, Integer> map = Utils.readSharedPreferences(Utils.getSharedPreferences(this));
        int currentBand = map.get(MediaKeyReceiver.CURRENT_BAND);
        int currentFreq = map.get(MediaKeyReceiver.CURRENT_FREQ);
        Utils.addNotification(this, currentBand, currentFreq);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void startBusinessThread() {
        if (mBussinessThreadStarted == true) {
            return;
        } else {
            mBussinessThreadStarted = true;
        }
        mBussinessThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Looper.prepare();
                    mBussinessThreadHandler = new BussinessThreadHandler();
                    Looper.loop();
                } catch (Exception e) {
                }
            }

        });
        mBussinessThread.start();
    }


    private IntentFilter getIntentFilter() {
        if (receiveDataFilter == null) {
            receiveDataFilter = new IntentFilter();
            receiveDataFilter.addAction(ResponseCommand.TO_RADIO_APK_ACTION);
        }
        return receiveDataFilter;
    }

    private void findViews() {
        initControlViews();
        mFreqView = (FreqView) findViewById(R.id.freq_indicator);
    }

    private void initControlViews() {
        res = getResources();
        ptyPopup = new PtyPopup(this, requestHandler);
        menuPop = new MenuPop(this, requestHandler);

        eq_btn = (Button) findViewById(R.id.eq_btn);
        rds_btn = (Button) findViewById(R.id.rds_btn);

        band = (Button) findViewById(R.id.band);
        backward_station = (Button) findViewById(R.id.backward_station);
        forward_station = (Button) findViewById(R.id.forward_station);
        button_seek_backward = (Button) findViewById(R.id.button_seek_backward);
        button_seek_forward = (Button) findViewById(R.id.button_seek_forward);

        signal_indicate_iv = (ImageView) findViewById(R.id.signal_indicate_iv);
        loc_indicate_iv = (ImageView) findViewById(R.id.loc_indicate_iv);
        st_indicate_iv = (ImageView) findViewById(R.id.st_indicate_iv);

        ib_digit_thousand = (ImageView) findViewById(R.id.ib_digit_thousand);
        ib_digit_hundred = (ImageView) findViewById(R.id.ib_digit_hundred);
        ib_digit_ten = (ImageView) findViewById(R.id.ib_digit_ten);
        ib_digit_unit = (ImageView) findViewById(R.id.ib_digit_unit);
        ib_digit_dot = (ImageView) findViewById(R.id.ib_digit_dot);
        ib_digit_decimal_ten = (ImageView) findViewById(R.id.ib_digit_decimal_ten);
        ib_digit_decimal_hundred = (ImageView) findViewById(R.id.ib_digit_decimal_hundred);

        freq_range = (TextView) findViewById(R.id.freq_range);
        freq = (TextView) findViewById(R.id.freq);

        // *****************Listener
        rds_btn.setOnClickListener(clickListener);
        eq_btn.setOnClickListener(clickListener);

        band.setOnClickListener(clickListener);
        backward_station.setOnClickListener(clickListener);
        forward_station.setOnClickListener(clickListener);
        button_seek_backward.setOnClickListener(clickListener);
        button_seek_forward.setOnClickListener(clickListener);
        // -------------------
        common_stations[0] = (Button) findViewById(R.id.pre_station1);
        common_stations[1] = (Button) findViewById(R.id.pre_station2);
        common_stations[2] = (Button) findViewById(R.id.pre_station3);
        common_stations[3] = (Button) findViewById(R.id.pre_station4);
        common_stations[4] = (Button) findViewById(R.id.pre_station5);
        common_stations[5] = (Button) findViewById(R.id.pre_station6);

        for (Button common_station : common_stations) {
            common_station.setOnClickListener(clickListener);
            common_station.setOnLongClickListener(longClickListener);
        }
        // ------------------------
        af_btn = (Button) findViewById(R.id.af_btn);
        ta_btn = (Button) findViewById(R.id.ta_btn);
        pty_btn = (Button) findViewById(R.id.pty_btn);

        rt_tv = (TextView) findViewById(R.id.rt_tv);
        ps_tv = (TextView) findViewById(R.id.ps_tv);

        ta_indicate = (TextView) findViewById(R.id.ta_indicate);
        tp_indicate = (TextView) findViewById(R.id.tp_indicate);
        af_indicate = (TextView) findViewById(R.id.af_indicate);
        pty_indicate = (TextView) findViewById(R.id.pty_indicate);

        af_btn.setOnClickListener(clickListener);
        ta_btn.setOnClickListener(clickListener);
        pty_btn.setOnClickListener(clickListener);

        // ---------------------------
        indicator_layout = (LinearLayout) findViewById(R.id.indicator_layout);
        rt_ps_layout = (LinearLayout) findViewById(R.id.rt_ps_layout);
        rds_layout = (LinearLayout) findViewById(R.id.rds_layout);
        stations_layout = (LinearLayout) findViewById(R.id.stations_layout);
    }

    private int currentBand = 0;
    private int rds_btn_count = 0;
    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                // Search
                case R.id.backward_station:
                    sendMessage(RequestCommand.SINGLE_STEP, SIGLE_STEP, (byte) 0x00); // 单步向下
                    break;
                case R.id.forward_station:
                    sendMessage(RequestCommand.SINGLE_STEP, SIGLE_STEP, (byte) 0x01); // 单步向上
                    break;
                case R.id.button_seek_backward:
                    sendMessage(RequestCommand.SEARCH, SEARCH, (byte) 0x00); // 向下搜索
                    break;
                case R.id.button_seek_forward:
                    sendMessage(RequestCommand.SEARCH, SEARCH, (byte) 0x01); // 向上搜索
                    break;
                case R.id.band:
                    if (currentBand == 0) {
                        sendMessage(RequestCommand.BAND_SWITCH, BAND_SWITCH, (byte) 0x01);
                    } else if (currentBand == 1) {
                        sendMessage(RequestCommand.BAND_SWITCH, BAND_SWITCH, (byte) 0x02);
                    } else if (currentBand == 2) {
                        sendMessage(RequestCommand.BAND_SWITCH, BAND_SWITCH, (byte) 0x03);
                    } else if (currentBand == 3) {
                        sendMessage(RequestCommand.BAND_SWITCH, BAND_SWITCH, (byte) 0x04);
                    } else if (currentBand == 4) {
                        sendMessage(RequestCommand.BAND_SWITCH, BAND_SWITCH, (byte) 0x00);
                    }
                    break;
                // EQ_btn
                case R.id.eq_btn:
                    Intent eqIntent = new Intent(EQ_ACTION);
                    sendBroadcast(eqIntent);
                    break;
                case R.id.rds_btn:
                    if (rds_btn_count % 2 == 0) {
                        rds_btn.setBackgroundResource(R.drawable.rds_down);
                        showRDSLayout();
                    } else {
                        rds_btn.setBackgroundResource(R.drawable.rds);
                        indicator_layout.setVisibility(View.INVISIBLE);
                        rt_ps_layout.setVisibility(View.INVISIBLE);
                        rds_layout.setVisibility(View.GONE);
                        stations_layout.setVisibility(View.VISIBLE);
                    }
                    break;
                // RDS
                case R.id.af_btn:
                    if (isAFOpen) {
                        sendMessage(RequestCommand.AF_TOGGLE, MainActivity.AF_TOGGLE, (byte) 0x00);
                    } else {
                        sendMessage(RequestCommand.AF_TOGGLE, MainActivity.AF_TOGGLE, (byte) 0x01);
                    }
                    break;
                case R.id.ta_btn:
                    if (isTAOpen) {
                        sendMessage(RequestCommand.TA_TOGGLE, MainActivity.TA_TOGGLE, (byte) 0x00);
                    } else {
                        sendMessage(RequestCommand.TA_TOGGLE, MainActivity.TA_TOGGLE, (byte) 0x01);
                    }
                    break;
                case R.id.pty_btn:
                    if (isPTYOpen) {
                        sendMessage(RequestCommand.PTY_TOGGLE, MainActivity.PTY_TOGGLE, (byte) 0x00);
                    } else {
                        sendMessage(RequestCommand.PTY_TOGGLE, MainActivity.PTY_TOGGLE, (byte) 0x01);
                    }
                    ptyPopup.showPopUP(getAnchor(), 0, 0);
                    break;
                case R.id.pre_station1:
                    sendMessage(RequestCommand.CHANNEL_CHOOSE, MainActivity.CHANNEL_CHOOSE, (byte) 0x01);
                    break;
                case R.id.pre_station2:
                    sendMessage(RequestCommand.CHANNEL_CHOOSE, MainActivity.CHANNEL_CHOOSE, (byte) 0x02);
                    break;
                case R.id.pre_station3:
                    sendMessage(RequestCommand.CHANNEL_CHOOSE, MainActivity.CHANNEL_CHOOSE, (byte) 0x03);
                    break;
                case R.id.pre_station4:
                    sendMessage(RequestCommand.CHANNEL_CHOOSE, MainActivity.CHANNEL_CHOOSE, (byte) 0x04);
                    break;
                case R.id.pre_station5:
                    sendMessage(RequestCommand.CHANNEL_CHOOSE, MainActivity.CHANNEL_CHOOSE, (byte) 0x05);
                    break;
                case R.id.pre_station6:
                    sendMessage(RequestCommand.CHANNEL_CHOOSE, MainActivity.CHANNEL_CHOOSE, (byte) 0x06);
                    break;
            }
        }
    };

    public View getAnchor() {
        return band;
    }

    private void showRDSLayout() {
        indicator_layout.setVisibility(View.VISIBLE);
        rt_ps_layout.setVisibility(View.VISIBLE);
        rds_layout.setVisibility(View.VISIBLE);
        stations_layout.setVisibility(View.GONE);
    }

    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.pre_station1:
                    sendMessage(RequestCommand.SET_PRESTORE_CHANNEL, MainActivity.SET_PRESTORE_CHANNEL, (byte) 0x01);
                    break;
                case R.id.pre_station2:
                    sendMessage(RequestCommand.SET_PRESTORE_CHANNEL, MainActivity.SET_PRESTORE_CHANNEL, (byte) 0x02);
                    break;
                case R.id.pre_station3:
                    sendMessage(RequestCommand.SET_PRESTORE_CHANNEL, MainActivity.SET_PRESTORE_CHANNEL, (byte) 0x03);
                    break;
                case R.id.pre_station4:
                    sendMessage(RequestCommand.SET_PRESTORE_CHANNEL, MainActivity.SET_PRESTORE_CHANNEL, (byte) 0x04);
                    break;
                case R.id.pre_station5:
                    sendMessage(RequestCommand.SET_PRESTORE_CHANNEL, MainActivity.SET_PRESTORE_CHANNEL, (byte) 0x05);
                    break;
                case R.id.pre_station6:
                    sendMessage(RequestCommand.SET_PRESTORE_CHANNEL, MainActivity.SET_PRESTORE_CHANNEL, (byte) 0x06);
                    break;
            }
            return true;
        }
    };


    protected void sendMessage(int messageWhat, String key, byte value) {
        Message msg = requestHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putByte(key, value);

        msg.what = messageWhat;
        msg.setData(bundle);
        requestHandler.sendMessage(msg);
    }


    //-----------response
    @Override
    public void updateCurrentInfoReport(Bundle bundle) {
        currentBand = bundle.getInt(MainActivity.CURRENT_BAND);
        int currentChannel = bundle.getInt(MainActivity.CURRENT_CHANNEL);
        currentFreq = bundle.getInt(MainActivity.CURRENT_FREQ);
        int currentSignal = bundle.getInt(MainActivity.CURRENT_SIGNAL);
        boolean isLOC = bundle.getBoolean(MainActivity.LOC);
        boolean isST = bundle.getBoolean(MainActivity.ST);
        // TODO 没用上的变量
        boolean isRadioIsInAS = bundle.getBoolean(MainActivity.RADIO_IS_IN_AS); // 自动搜索
        boolean isRadioIsInPS = bundle.getBoolean(MainActivity.RADIO_IS_IN_PS); // 频道预览
        boolean isRadioIsInSeekUp = bundle.getBoolean(MainActivity.RADIO_IS_IN_SEEK_UP);
        boolean isRadioIsInSeekDown = bundle.getBoolean(MainActivity.ISRADIO_IS_IN_SEEK_DOWN);

        // 频道
        updateCurrentChannel(currentChannel);

        // 波段
        setCommonStationFreqText(currentBand);

        // 设置标尺中当前波段
        mFreqView.setCurrentBand(currentBand);

        // 是否在自动搜索状态
        boolean isAutoSearch = isRadioIsInAS || isRadioIsInPS || isRadioIsInSeekUp || isRadioIsInSeekDown;

        // 频率
        updateDigitView(currentFreq);
        mFreqView.setTargetFreqHz(currentFreq, isAutoSearch);

        // 信号
        int signalResId = 0;
        if (currentSignal == 0) {
            signalResId = R.drawable.signal0;
        } else if (currentSignal == 1) {
            signalResId = R.drawable.signal1;
        } else if (currentSignal == 2) {
            signalResId = R.drawable.signal2;
        } else if (currentSignal == 3) {
            signalResId = R.drawable.signal3;
        } else if (currentSignal == 4) {
            signalResId = R.drawable.signal4;
        } else if (currentSignal == 5) {
            signalResId = R.drawable.signal5;
        }
        signal_indicate_iv.setBackgroundResource(signalResId);

        if (isST) {
            setViewBG(R.drawable.st_down, st_indicate_iv);
        } else {
            setViewBG(R.drawable.st, st_indicate_iv);
        }

        if (isLOC) {
            setViewBG(R.drawable.loc_indicate_down, loc_indicate_iv);
        } else {
            setViewBG(R.drawable.loc_indicate, loc_indicate_iv);
        }
    }

    private void setViewBG(int resId, View... views) {
        for (View view : views) {
            view.setBackgroundResource(resId);
        }
    }


    private int[] fm1 = null;
    private int[] fm2 = null;
    private int[] fm3 = null;
    private int[] am1 = null;
    private int[] am2 = null;
    boolean isInitFMAM = false;
    private void setCommonStationFreqText(int currentBand) {
        if (isInitFMAM) {
            if (currentBand == 0) {
                freq_range.setText(R.string.fm1);
                freq.setText(R.string.mhz);
                for (int i = 0; i < common_stations.length; i++) {
                    common_stations[i].setText(Utils.parseIntegerToFreq(fm1[i]));
                }
            } else if (currentBand == 1) {
                freq_range.setText(R.string.fm2);
                freq.setText(R.string.mhz);
                for (int i = 0; i < common_stations.length; i++) {
                    common_stations[i].setText(Utils.parseIntegerToFreq(fm2[i]));
                }
            } else if (currentBand == 2) {
                freq_range.setText(R.string.fm3);
                freq.setText(R.string.mhz);
                for (int i = 0; i < common_stations.length; i++) {
                    common_stations[i].setText(Utils.parseIntegerToFreq(fm3[i]));
                }
            } else if (currentBand == 3) {
                freq_range.setText(R.string.am1);
                freq.setText(R.string.khz);
                for (int i = 0; i < common_stations.length; i++) {
                    common_stations[i].setText(String.valueOf(am1[i]));
                }
            } else if (currentBand == 4) {
                freq_range.setText(R.string.am2);
                freq.setText(R.string.khz);
                for (int i = 0; i < common_stations.length; i++) {
                    common_stations[i].setText(String.valueOf(am2[i]));
                }
            }
        }

    }

    private void updateCurrentChannel(int currentChannel) {
        if (currentChannel == 0) {
            setViewTextColor(-1, common_stations);
            common_stations[0].setBackgroundResource(R.drawable.pre_station1_select);
            common_stations[1].setBackgroundResource(R.drawable.pre_station2_select);
            common_stations[2].setBackgroundResource(R.drawable.pre_station3_select);
            common_stations[3].setBackgroundResource(R.drawable.pre_station4_select);
            common_stations[4].setBackgroundResource(R.drawable.pre_station5_select);
            common_stations[5].setBackgroundResource(R.drawable.pre_station6_select);
        } else if (currentChannel == 1) {
            setViewTextColor(0, common_stations);
            common_stations[0].setBackgroundResource(R.drawable.pre_station_1_now);
            common_stations[1].setBackgroundResource(R.drawable.pre_station2_select);
            common_stations[2].setBackgroundResource(R.drawable.pre_station3_select);
            common_stations[3].setBackgroundResource(R.drawable.pre_station4_select);
            common_stations[4].setBackgroundResource(R.drawable.pre_station5_select);
            common_stations[5].setBackgroundResource(R.drawable.pre_station6_select);
        } else if (currentChannel == 2) {
            setViewTextColor(1, common_stations);
            common_stations[0].setBackgroundResource(R.drawable.pre_station1_select);
            common_stations[1].setBackgroundResource(R.drawable.pre_station_2_now);
            common_stations[2].setBackgroundResource(R.drawable.pre_station3_select);
            common_stations[3].setBackgroundResource(R.drawable.pre_station4_select);
            common_stations[4].setBackgroundResource(R.drawable.pre_station5_select);
            common_stations[5].setBackgroundResource(R.drawable.pre_station6_select);
        } else if (currentChannel == 3) {
            setViewTextColor(2, common_stations);
            common_stations[0].setBackgroundResource(R.drawable.pre_station1_select);
            common_stations[1].setBackgroundResource(R.drawable.pre_station2_select);
            common_stations[2].setBackgroundResource(R.drawable.pre_station_3_now);
            common_stations[3].setBackgroundResource(R.drawable.pre_station4_select);
            common_stations[4].setBackgroundResource(R.drawable.pre_station5_select);
            common_stations[5].setBackgroundResource(R.drawable.pre_station6_select);
        } else if (currentChannel == 4) {
            setViewTextColor(3, common_stations);
            common_stations[0].setBackgroundResource(R.drawable.pre_station1_select);
            common_stations[1].setBackgroundResource(R.drawable.pre_station2_select);
            common_stations[2].setBackgroundResource(R.drawable.pre_station3_select);
            common_stations[3].setBackgroundResource(R.drawable.pre_station_4_now);
            common_stations[4].setBackgroundResource(R.drawable.pre_station5_select);
            common_stations[5].setBackgroundResource(R.drawable.pre_station6_select);
        } else if (currentChannel == 5) {
            setViewTextColor(4, common_stations);
            common_stations[0].setBackgroundResource(R.drawable.pre_station1_select);
            common_stations[1].setBackgroundResource(R.drawable.pre_station2_select);
            common_stations[2].setBackgroundResource(R.drawable.pre_station3_select);
            common_stations[3].setBackgroundResource(R.drawable.pre_station4_select);
            common_stations[4].setBackgroundResource(R.drawable.pre_station_5_now);
            common_stations[5].setBackgroundResource(R.drawable.pre_station6_select);
        } else if (currentChannel == 6) {
            setViewTextColor(5, common_stations);
            common_stations[0].setBackgroundResource(R.drawable.pre_station1_select);
            common_stations[1].setBackgroundResource(R.drawable.pre_station2_select);
            common_stations[2].setBackgroundResource(R.drawable.pre_station3_select);
            common_stations[3].setBackgroundResource(R.drawable.pre_station4_select);
            common_stations[4].setBackgroundResource(R.drawable.pre_station5_select);
            common_stations[5].setBackgroundResource(R.drawable.pre_station_6_now);
        }

    }

    private void setViewTextColor(int specNum, Button... btns) {
        for (int i = 0; i < common_stations.length; i++) {
            if (i == specNum) {
                btns[i].setTextColor(res.getColor(R.color.common_station_select));
            } else {
                btns[i].setTextColor(res.getColor(R.color.common_station));
            }
        }
    }

    @Override
    public void updateListInfo(Bundle bundle) {
        fm1 = bundle.getIntArray(MainActivity.FM1);
        fm2 = bundle.getIntArray(MainActivity.FM2);
        fm3 = bundle.getIntArray(MainActivity.FM3);
        am1 = bundle.getIntArray(MainActivity.AM1);
        am2 = bundle.getIntArray(MainActivity.AM2);

        isInitFMAM = true;

        setCommonStationFreqText(currentBand);
    }

    @Override
    public void updateCurrentBndFreqRange(Bundle bundle) {
        int minFreq = bundle.getInt(MainActivity.MIN_FREQ);
        int maxFreq = bundle.getInt(MainActivity.MAX_FREQ);
        int stepByStepFreq = bundle.getInt(MainActivity.STEP_BY_STEP_FREQ);

        mKeyboard = menuPop.getKeyboardInstance();
        mKeyboard.setMaxFreq(maxFreq);
        mKeyboard.setMinFreq(minFreq);
        mKeyboard.setCurrentBand(currentBand);
        mKeyboard.initFreqBits();

        mFreqView.setMaxFreq(maxFreq);
        mFreqView.setMinFreq(minFreq);
        mFreqView.setStepFreq(stepByStepFreq);
    }

    @Override
    public void updateRdsInfo(Bundle bundle) {
        isRDSPowerOpen = bundle.getBoolean(MainActivity.F_RDS_SWITCH);
        isTAOpen = bundle.getBoolean(MainActivity.F_TA_SWITCH);
        isAFOpen = bundle.getBoolean(MainActivity.F_AF_SWITCH);
        isPTYOpen = bundle.getBoolean(MainActivity.F_PTY_SWITCH);
        isEONOepn = bundle.getBoolean(MainActivity.F_EON_SWITCH);
        isREGOpen = bundle.getBoolean(MainActivity.F_REG_SWITCH);
        isCTOpen = bundle.getBoolean(MainActivity.F_CT_SWITCH);

        isRDSStation = bundle.getBoolean(MainActivity.F_RDS_INFO);
        boolean isTPIndicate = bundle.getBoolean(MainActivity.F_TP_INFO);
        // TODO 没用上的变量
        boolean isTrafficIndicate = bundle.getBoolean(MainActivity.F_TA_INFO); // TA指示
        boolean isEWSIndicate = bundle.getBoolean(MainActivity.F_EWS_INFO); // 弹出警告
        boolean isNowSearchPTYStation = bundle.getBoolean(MainActivity.F_PTY_FLASH); // 正在搜索pty

        if (!hasRdsDev) {
            rds_btn.setVisibility(View.INVISIBLE);
        } else {
            rds_btn.setVisibility(View.VISIBLE);
            if (isRDSStation) {
                rds_btn.setEnabled(true);
            } else {
                // 隐藏RDS相关布局
                hideRDSLayout();
            }
        }

        if (isRDSPowerOpen) {
            // TODO
        } else {

        }
        int af_ta_tp_pty_selected = res.getColor(R.color.af_ta_tp_pty_selected);
        int af_ta_tp_pty = res.getColor(R.color.af_ta_tp_pty);

        if (isAFOpen) {
            af_indicate.setTextColor(af_ta_tp_pty_selected);
            af_btn.setBackgroundResource(R.drawable.af_now_select);
        } else {
            af_indicate.setTextColor(af_ta_tp_pty);
            af_btn.setBackgroundResource(R.drawable.af_select);
        }

        if (isTAOpen) {
            ta_indicate.setTextColor(af_ta_tp_pty_selected);
            ta_btn.setBackgroundResource(R.drawable.ta_now_select);
        } else {
            ta_indicate.setTextColor(af_ta_tp_pty);
            ta_btn.setBackgroundResource(R.drawable.ta_select);
        }

        if (isPTYOpen) {
            // TODO
            pty_btn.setBackgroundResource(R.drawable.pty_now_select);
        } else {
            pty_btn.setBackgroundResource(R.drawable.pty_select);
        }

        if (isTPIndicate) {
            tp_indicate.setTextColor(af_ta_tp_pty_selected);
        } else {
            tp_indicate.setTextColor(af_ta_tp_pty);
        }
    }

    private void hideRDSLayout() {
        rds_btn.setEnabled(false);
        indicator_layout.setVisibility(View.INVISIBLE);
        rt_ps_layout.setVisibility(View.INVISIBLE);
        rds_layout.setVisibility(View.GONE);
        stations_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void updatePsAndPTY(Bundle bundle) {
        String psName = bundle.getString(MainActivity.PS_NAME);
        byte ptytype = bundle.getByte(MainActivity.PTY);
        ps_tv.setText(psName);
        int resId = Utils.getShowPTYTypeR2sId(ptytype);
        pty_indicate.setText(resId);
    }


    @Override
    public void updateRT(Bundle bundle) {
        String rt = bundle.getString(MainActivity.RT);
        rt_tv.setText(rt);
    }

    @Override
    public void updateDigitView(int arg1) {
        int len = String.valueOf(arg1).length();
        int[] digits = new int[len];
        for (int i = 0; i < len; i++) {
            digits[i] = Utils.getDigitValueFromFreq(arg1, i + 1);
        }
        if (currentBand == 3 || currentBand == 4) {
            ib_digit_dot.setVisibility(View.GONE);
            ib_digit_decimal_ten.setVisibility(View.GONE);
            ib_digit_decimal_hundred.setVisibility(View.GONE);
        } else {
            ib_digit_dot.setVisibility(View.VISIBLE);
            ib_digit_decimal_ten.setVisibility(View.VISIBLE);
            ib_digit_decimal_hundred.setVisibility(View.VISIBLE);
        }

        if (len == 3) {
            ib_digit_thousand.setVisibility(View.GONE);
            ib_digit_hundred.setVisibility(View.VISIBLE);

            ib_digit_hundred.setBackgroundResource(Utils.digitSelectImageResId(digits[2]));
            ib_digit_ten.setBackgroundResource(Utils.digitSelectImageResId(digits[1]));
            ib_digit_unit.setBackgroundResource(Utils.digitSelectImageResId(digits[0]));
        }

        if (len == 4) {
            if (currentBand == 3 || currentBand == 4) {
                ib_digit_thousand.setVisibility(View.VISIBLE);
                ib_digit_hundred.setVisibility(View.VISIBLE);

                ib_digit_thousand.setBackgroundResource(Utils.digitSelectImageResId(digits[3]));
                ib_digit_hundred.setBackgroundResource(Utils.digitSelectImageResId(digits[2]));
                ib_digit_ten.setBackgroundResource(Utils.digitSelectImageResId(digits[1]));
                ib_digit_unit.setBackgroundResource(Utils.digitSelectImageResId(digits[0]));
            } else {
                ib_digit_thousand.setVisibility(View.GONE);
                ib_digit_hundred.setVisibility(View.GONE);

                ib_digit_ten.setBackgroundResource(Utils.digitSelectImageResId(digits[3]));
                ib_digit_unit.setBackgroundResource(Utils.digitSelectImageResId(digits[2]));

                ib_digit_decimal_ten.setBackgroundResource(Utils.digitSelectImageResId(digits[1]));
                ib_digit_decimal_hundred.setBackgroundResource(Utils.digitSelectImageResId(digits[0]));
            }
        }

        if (len == 5) {
            ib_digit_thousand.setVisibility(View.GONE);
            ib_digit_hundred.setVisibility(View.VISIBLE);
            ib_digit_hundred.setBackgroundResource(Utils.digitSelectImageResId(digits[4]));
            ib_digit_ten.setBackgroundResource(Utils.digitSelectImageResId(digits[3]));
            ib_digit_unit.setBackgroundResource(Utils.digitSelectImageResId(digits[2]));
            ib_digit_decimal_ten.setBackgroundResource(Utils.digitSelectImageResId(digits[1]));
        }
    }
}
