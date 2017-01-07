package com.zyt.tx.radio;

import android.app.Activity;

/**
 * Created by MJS on 2017/1/7.
 */

public abstract class BaseRadioActivity extends Activity {
    public final RequestHandler requestHandler = new RequestHandler(this);
    public final ResponseHandler responseHandler = new ResponseHandler(this);


}
