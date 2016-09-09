package com.caoyang.lock;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Author：艹羊
 * Created Time:2016/09/09 10:46
 */
public class LockService extends Service {


    private static boolean sPower = true, isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    AlarmManager mAlarmManager = null;
    PendingIntent mPendingIntent = null;

    //onCreate()方法只会在Service第一次被创建的时候调用
    @Override
    public void onCreate() {
        super.onCreate();
        //开启AlemManager
        Intent sendIntent = new Intent(getApplicationContext(), LockService.class);
        sendIntent.putExtra(Constant.ACTION_START_TYPE, Constant.ACTION_START_SERVICE);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mPendingIntent = PendingIntent.getService(this, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long now = System.currentTimeMillis();
        mAlarmManager.setInexactRepeating(AlarmManager.RTC, now, 1000, mPendingIntent);


        //作为前台服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("LockService");
            builder.setContentText("安全保护中");
            startForeground(250, builder.build());
        } else {
            startForeground(250, new Notification());
        }

        //阻塞线程
        if (!isRunning) {
            isRunning = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (sPower) {
                        if (System.currentTimeMillis() >= 123456789000000L) {
                            sPower = false;
                        }
                        Log.e(Constant.LOG_TAG, "LockService  运行中");
                        startService(new Intent(LockService.this, ProtectService.class));
                        SystemClock.sleep(2000);
                    }
                }
            }).start();
        }
    }

    private DevicePolicyManager policyManager;
    private ComponentName componentName;

    //锁屏操作
    public void LockScreen() {
        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, LockReceiver.class);
        if (policyManager.isAdminActive(componentName)) {//判断是否有权限(激活了设备管理器)
            policyManager.lockNow();// 直接锁屏
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            activeManager();//激活设备管理器获取权限
        }
    }

    private void activeManager() {
        //使用隐式意图调用系统方法来激活指定的设备管理器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "一键锁屏");
        startActivity(intent);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    //onStartCommand 会在startService时进行调用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        String extra = intent.getStringExtra(Constant.ACTION_START_TYPE);
        if (extra.equals(Constant.ACTION_START_SERVICE_WITH_LOCK_SCREEM)) {
            LockScreen();
        }
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        startService();

        Log.e(Constant.LOG_TAG, "LockService  死亡");
    }


    /**
     * 打开服务
     */
    private void startService() {
        Intent startIntent = new Intent(this, LockService.class);
        startIntent.putExtra(Constant.ACTION_START_TYPE, Constant.ACTION_START_SERVICE);
        startService(startIntent);
    }
}
