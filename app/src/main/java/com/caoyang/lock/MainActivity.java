package com.caoyang.lock;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "caoyang";

    private DevicePolicyManager policyManager;
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        startService();
    }

    /**
     * 打开服务
     */
    private void startService() {
        Intent startIntent = new Intent(this, LockService.class);
        startIntent.putExtra(Constant.ACTION_START_TYPE, Constant.ACTION_START_SERVICE);
        startService(startIntent);
    }
    //    @Override
//    protected void onResume() {//重写此方法用来在第一次激活设备管理器之后锁定屏幕
//        if (policyManager != null && policyManager.isAdminActive(componentName)) {
//            policyManager.lockNow();
//            android.os.Process.killProcess(android.os.Process.myPid());
//        }
//        super.onResume();
//    }
//
//
//    // 解除绑定
//    public void Bind(View v) {
//        if (componentName != null) {
//            policyManager.removeActiveAdmin(componentName);
//            activeManager();
//        }
//    }


}
