package com.szmy.pigapp.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;



public class CoreAppService extends Service implements BDLocationListener {

    private final String TAG = CoreAppService.class.getSimpleName();
    String GPS_X = "";
    String GPS_Y = "";

    public CoreAppService() {

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Notification notification = new Notification();
//        notification.tickerText =  "注意了，我被扔到状态栏了" ;
//        startForeground(Integer.MAX_VALUE, notification);

        Intent service = new Intent(this, CoreAppService.class);
        startService(service);

        Log.i(TAG, ">>>>>>>>>>Myservice类的oncreate方法");
        initBDLocation();
//        new Thread(runnable).start();

    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent service = new Intent(this, CoreAppService.class);
        startService(service);
    }

//    /**
//     * run
//     */
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//                while (true) {
////                    List<AppInfo> appList = installAppInfo();
////                    for(AppInfo appInfo : appList){
////                        appInfo.print();
////                    }
//                    try {
//                        App.get().__mHttpPost();//post
//                        Thread.sleep(60 * 1000 * 15);
////                    Thread.sleep(60 * 1000);
//                        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//                        Log.i(TAG, ">>>>>>>>收集基础信息 " + tm.getDeviceId() + ":" + tm.getSimSerialNumber() + ":" + GPS_X + ":" + GPS_Y);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//        }
//    };

    /**
     * 获取应用
     * @author Lee
     * @return
     */
//    private List<AppInfo> installAppInfo(){
//        List<AppInfo> appList = new ArrayList<AppInfo>();
//        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
//
//        for(int i=0;i<packages.size();i++) {
//            PackageInfo packageInfo = packages.get(i);
//            AppInfo tmpInfo =new AppInfo();
//            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
//            tmpInfo.packageName = packageInfo.packageName;
//            tmpInfo.versionName = packageInfo.versionName;
//            tmpInfo.versionCode = packageInfo.versionCode;
//            tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
//            //Only display the non-system app info
//            if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0)
//            {
//                appList.add(tmpInfo);
//            }
//        }
//        return appList;
//    }


    public static LocationClient mLocationClient = null;

    public void initBDLocation() {
        SDKInitializer.initialize(this);

        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        LocationClientOption option = new LocationClientOption();
        // Hight_Accuracy高精度、Battery_Saving低功耗、Device_Sensors仅设备(GPS)
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("wgs84");// 坐标系 wgs84：GPS  坐标   bd09ll：百度坐标
        option.setScanSpan(1000 * 60 * 13);// 设置发起定位请求的间隔时间
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(this); // 注册监听函数
        mLocationClient.start();
        getBDLocal();
    }

    public void getBDLocal() {
        if (mLocationClient != null && mLocationClient.isStarted())
            mLocationClient.requestLocation();
        else
            Log.e("LocSDK5", "locClient is null or not started");
    }

    public void stopBDLocation(){
        if(mLocationClient!=null){
            mLocationClient.stop();
        }
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null) {
            return;
        } else {
            GPS_X = String.valueOf(bdLocation.getLongitude());
            GPS_Y = String.valueOf(bdLocation.getLatitude());
        }
    }
}
