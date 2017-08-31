package com.szmy.pigapp.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadcastReceiverHelper extends BroadcastReceiver {
    private final String TAG = BroadcastReceiverHelper.class.getSimpleName();

    NotificationManager mn=null;
    Notification notification=null;
    Context ct=null;
    BroadcastReceiverHelper receiver;

    public BroadcastReceiverHelper(){

    }

//    public BroadcastReceiverHelper(Context c){
//        ct=c;
//        receiver=this;
//    }
//
//    //注册
//    public void registerAction(String action){
//        IntentFilter filter=new IntentFilter();
//        filter.addAction(action);
//        ct.registerReceiver(receiver, filter);
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.i(TAG, "监听到开机启动getAction");
        }else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {

            boolean isServiceRunning = false;
            ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service :manager.getRunningServices(Integer.MAX_VALUE)) {
                if("com.szmy.pigapp.service.CoreAppService".equals(service.service.getClassName())) {
                    isServiceRunning = true;
                }
            }
            if (!isServiceRunning) {
                Intent i = new Intent(context, CoreAppService.class);
                context.startService(i);
                Log.i(TAG, "没有启动，现在启动");
            }
        }
    }
}
