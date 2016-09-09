package com.caoyang.lock;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Author：艹羊
 * Created Time:2016/09/09 10:34
 */
public class LockReceiver extends DeviceAdminReceiver {
    private static final String LOG_TAG = "caoyang";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e(LOG_TAG, "onreceiver");
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.e(LOG_TAG, "激活使用");
        super.onEnabled(context, intent);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.e(LOG_TAG, "取消激活");
        super.onDisabled(context, intent);
    }
}
