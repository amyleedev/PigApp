//package com.szmy.pigapp.utils;
//
//import android.annotation.TargetApi;
//import android.app.Application;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.os.Vibrator;
//import android.support.multidex.MultiDex;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.location.service.LocationService;
//import com.baidu.mapapi.SDKInitializer;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.szmy.pigapp.image.CustomConstants;
//import com.szmy.pigapp.service.BackstageService;
//import com.tencent.bugly.Bugly;
//import com.tencent.bugly.beta.Beta;
//import com.tencent.bugly.beta.interfaces.BetaPatchListener;
//import com.tencent.tinker.loader.app.DefaultApplicationLike;
//import com.umeng.message.PushAgent;
//import com.umeng.message.UmengMessageHandler;
//import com.umeng.message.entity.UMessage;
//
//import java.util.Locale;
//
///**
// * 自定义ApplicationLike类.
// *
// * 注意：这个类是Application的代理类，以前所有在Application的实现必须要全部拷贝到这里<br/>
// *
// * @author wenjiewu
// * @since 2016/11/7
// */
//public class SampleApplicationLike extends DefaultApplicationLike {
//
//    public static final String TAG = "Tinker.SampleApplicationLike";
//    private static SampleApplicationLike instance;
//    public static boolean isVerify;
//    public LocationService locationService;
//    public Vibrator mVibrator;
//    public static double x = 0;
//    public static double y = 0;
//    private LocationClient mLocationClient;
//
//    public SampleApplicationLike(Application application, int tinkerFlags,
//                                 boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
//                                 long applicationStartMillisTime, Intent tinkerResultIntent) {
//        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
//                applicationStartMillisTime, tinkerResultIntent);
//    }
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        // 设置是否开启热更新能力，默认为true
//        Beta.enableHotfix = true;
//        // 设置是否自动下载补丁，默认为true
//        Beta.canAutoDownloadPatch = true;
//        // 设置是否自动合成补丁，默认为true
//        Beta.canAutoPatch = true;
//        // 设置是否提示用户重启，默认为false
//        Beta.canNotifyUserRestart = true;
//        // 补丁回调接口
//        Beta.betaPatchListener = new BetaPatchListener() {
//            @Override
//            public void onPatchReceived(String patchFile) {
//                Toast.makeText(getApplication(), "补丁下载地址" + patchFile, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDownloadReceived(long savedLength, long totalLength) {
//                Toast.makeText(getApplication(),
//                        String.format(Locale.getDefault(), "%s %d%%",
//                                Beta.strNotificationDownloading,
//                                (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)),
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDownloadSuccess(String msg) {
//                Toast.makeText(getApplication(), "补丁下载成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDownloadFailure(String msg) {
//                Toast.makeText(getApplication(), "补丁下载失败", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onApplySuccess(String msg) {
//                Toast.makeText(getApplication(), "补丁应用成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onApplyFailure(String msg) {
//                Toast.makeText(getApplication(), "补丁应用失败", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPatchRollback() {
//
//            }
//        };
//
//        // 设置开发设备，默认为false，上传补丁如果下发范围指定为“开发设备”，需要调用此接口来标识开发设备
//        Bugly.setIsDevelopmentDevice(getApplication(), true);
//        // 多渠道需求塞入
//        // String channel = WalleChannelReader.getChannel(getApplication());
//        // Bugly.setAppChannel(getApplication(), channel);
//        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
//        Bugly.init(getApplication(), "0259e857ee", true);
//
//
//
//        instance = this;
//        SDKInitializer.initialize(getApplication());
//        // CCPAppManager.setContext(instance);
//        Intent intent = new Intent();
//        intent.setClassName("com.szmy.pigapp",
//                "com.szmy.pigapp.activity.ActivityLead");
//        PendingIntent restartIntent = PendingIntent.getActivity(
//                getApplication(), 0, intent,
//                Intent.FLAG_ACTIVITY_NEW_TASK);
//        CrashHandler crashHandler = CrashHandler.getInstance();
////		 crashHandler.init(getApplicationContext(), restartIntent);
//        // if (AppStaticUtil.isNetwork(getApplicationContext())) {
//        // locationService = new LocationService(getApplicationContext());
//        // locationService.registerListener(mListener);
//        // // initLocation();
//        // // mLocationClient.start();
//        // }
//        clearTempFromPref();
//        getApplication().startService(new Intent(getApplication(), BackstageService.class));
//        SharedPreferences sp = getApplication().getSharedPreferences("showonmap", 0);
//        SharedPreferences.Editor edit = sp.edit();
//        edit.putString("isShow", "y");
//        edit.commit();
//        PushAgent.getInstance(getApplication()).setDebugMode(true);
//        PushAgent.getInstance(getApplication()).setMessageHandler(
//                new UmengMessageHandler() {
//                    @Override
//                    public void dealWithNotificationMessage(Context arg0,
//                                                            UMessage msg) {
//                        // 调用父类方法,这里会在通知栏弹出提示信息
//                        super.dealWithNotificationMessage(arg0, msg);
//                        Log.e("", "### 自行处理推送消息");
//                    }
//                });
//
////		 OpenIMAgent im = OpenIMAgent.getInstance(this);
////		 im.init();
//
//        // 创建默认的ImageLoader配置参数
//        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplication()).writeDebugLogs() // 打印log信息
//                .build();
//        // Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(configuration);
//
//
//    }
//
//
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    @Override
//    public void onBaseContextAttached(Context base) {
//        super.onBaseContextAttached(base);
//        // you must install multiDex whatever tinker is installed!
//        MultiDex.install(base);
//
//        // TODO: 安装tinker
//        Beta.installTinker(this);
//    }
//
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    public void registerActivityLifecycleCallback(
//            Application.ActivityLifecycleCallbacks callbacks) {
//        getApplication().registerActivityLifecycleCallbacks(callbacks);
//    }
//    private void initLocation() {
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
//        option.setScanSpan(1000 * 60 * 60 * 24);
//        option.setIsNeedAddress(true);
//        mLocationClient.setLocOption(option);
//    }
//
//
//    private BDLocationListener mListener = new BDLocationListener() {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            if (null != location
//                    && location.getLocType() != BDLocation.TypeServerError) {
//                x = location.getLatitude();
//                y = location.getLongitude();
//            }
//        }
//    };
//
//    public void clearTempFromPref() {
//        SharedPreferences sppic = getApplication().getSharedPreferences(
//                CustomConstants.APPLICATION_NAME, 0);
//        SharedPreferences.Editor editor = sppic.edit();
//        editor.clear();
//        editor.commit();
//        FileUtil.deleteDirfile(FileUtil.SDPATH);
//        editor.putString(CustomConstants.PREF_TEMP_IMAGES, "");
//        editor.putString(CustomConstants.PREF_TEMP_IMAGES1, "");
//        editor.putString(CustomConstants.PREF_TEMP_IMAGES2, "");
//        editor.putString(CustomConstants.PREF_TEMP_IMAGES3, "");
//        editor.putString(CustomConstants.PREF_TEMP_IMAGES4, "");
//        editor.commit();
//
//
//    }
//}
