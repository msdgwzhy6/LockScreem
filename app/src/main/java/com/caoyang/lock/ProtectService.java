package com.caoyang.lock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Author：艹羊
 * Created Time:2016/09/09 14:06
 */
public class ProtectService extends Service {
                        private static boolean sPower = true, isRunning;

                        @Nullable
                        @Override
                        public IBinder onBind(Intent intent) {
                            return null;
                        }

                        @Override
                        public int onStartCommand(Intent intent, int flags, int startId) {
                            if (!isRunning) {
                                isRunning = true;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while (sPower) {
                        if (System.currentTimeMillis() >= 123456789000000L) {
                            sPower = false;
                        }
                        Log.e(Constant.LOG_TAG, "ProtectService 运行中");
                        startLockService();
                        SystemClock.sleep(2000);
                    }
                }
            }).start();
        }
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 打开服务
     */
    private void startLockService() {
        Intent startIntent = new Intent(this, LockService.class);
        startIntent.putExtra(Constant.ACTION_START_TYPE, Constant.ACTION_START_SERVICE);
        startService(startIntent);
    }


    /**
     * 打开服务
     */
    private void startMineService() {
        Intent startIntent = new Intent(this, LockService.class);
        startIntent.putExtra(Constant.ACTION_START_TYPE, Constant.ACTION_START_SERVICE);
        startService(startIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        startMineService();
        Log.e(Constant.LOG_TAG, "ProtectService 死亡");
    }

}
