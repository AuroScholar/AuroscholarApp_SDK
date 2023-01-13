package com.auro.application.util;

import android.util.Log;

import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;

public class AppLogger {

    private static final boolean enableLogger = false;

    private AppLogger() {

    }


    public static void w(final String TAG, final String MSG) {

        if (getIsLog() && MSG!=null) {
            Log.w(TAG, MSG);
        }
    }

    public static void d(final String TAG, final String MSG) {
        if (getIsLog()  && MSG!=null) {
            Log.d(TAG, MSG);
        }
    }

    public static void e(final String TAG, final String MSG) {
        if (getIsLog()  && MSG!=null) {
            Log.e(TAG, MSG);
        }
    }

    public static void i(final String TAG, final String MSG) {
        if (getIsLog()  && MSG!=null) {
            Log.i(TAG, MSG);
        }
    }

    public static void v(final String TAG, final String MSG) {
        if (getIsLog()  && MSG!=null) {
            Log.v(TAG, MSG);
        }
    }

    public static void wtf(final String TAG, final String MSG) {
        if (getIsLog()  && MSG!=null) {
            Log.wtf(TAG, MSG);
        }
    }

    public  static boolean getIsLog()
    {


       return AuroAppPref.INSTANCE.getModelInstance().isLog();

    }
}