package com.caoyang.lock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Author：艹羊
 * Created Time:2016/09/09 10:18
 */
public class WifiStateReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "caoyang";

    @Override
    public void onReceive(Context context, Intent intent) {
        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
        if (wifiState == WifiManager.WIFI_STATE_DISABLING) {
            Log.e(LOG_TAG, "正在关闭");
        } else if (wifiState == WifiManager.WIFI_STATE_ENABLING) {
            Log.e(LOG_TAG, "正在打开");
            startService(context);
        } else if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
            Log.e(LOG_TAG, "已经关闭");
        } else if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
            Log.e(LOG_TAG, "已经打开");
            startServiceWithLockScreem(context);
        } else {
            Log.e(LOG_TAG, "未知状态");
        }
    }


    /**
     * 打开服务
     */
    private void startService(Context context) {
        Intent startIntent = new Intent(context, LockService.class);
        startIntent.putExtra(Constant.ACTION_START_TYPE, Constant.ACTION_START_SERVICE);
        context.startService(startIntent);
    }

    /**
     * 打开服务--锁屏
     */
    private void startServiceWithLockScreem(Context context) {
        Intent startIntent = new Intent(context, LockService.class);
        startIntent.putExtra(Constant.ACTION_START_TYPE, Constant.ACTION_START_SERVICE_WITH_LOCK_SCREEM);
        context.startService(startIntent);
    }
}
