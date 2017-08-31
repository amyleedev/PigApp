/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.szmy.pigapp.activity;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.service.LocationService;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.szmy.pigapp.image.CustomConstants;
import com.szmy.pigapp.service.BackstageService;
import com.szmy.pigapp.utils.CrashHandler;
import com.szmy.pigapp.utils.FileUtil;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
//import com.alibaba.mobileim.YWAPI;
//import com.alibaba.mobileim.YWIMKit;
//import com.umeng.openim.OpenIMAgent;

public class PigTradeApp extends Application {
	private static PigTradeApp instance;
	public static boolean isVerify;
	public LocationService locationService;
	public Vibrator mVibrator;
	public static double x = 0;
	public static double y = 0;
	private LocationClient mLocationClient;

	/**
	 * 闂備礁鎲￠〃鍡椕洪幋鐘愁偨闁靛鏅滈弲顒傦拷閿熻棄鏆熺紒鐘崇叀閺屾盯濡疯娴犳帞绱掗弬璺ㄦ憼缂佸顦甸、鏃堝礋椤撶喐绶板┑鐐存尰閻旑剟骞忛敓锟�
	 * 
	 * @return
	 */
	public static PigTradeApp getInstance() {
		if (instance == null) {

		}
		return instance;
	}

	// @Override
	// protected void attachBaseContext(Context base) {
	// // TODO Auto-generated method stub
	// super.attachBaseContext(base);
	// MultiDex.install(this);
	// }

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		SDKInitializer.initialize(this);
		// CCPAppManager.setContext(instance);
		Intent intent = new Intent();
		intent.setClassName("com.szmy.pigapp",
				"com.szmy.pigapp.activity.ActivityLead");
		PendingIntent restartIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, intent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		CrashHandler crashHandler = CrashHandler.getInstance();
//		 crashHandler.init(getApplicationContext(), restartIntent);
		// if (AppStaticUtil.isNetwork(getApplicationContext())) {
		// locationService = new LocationService(getApplicationContext());
		// locationService.registerListener(mListener);
		// // initLocation();
		// // mLocationClient.start();
		// }
		clearTempFromPref();
		startService(new Intent(this, BackstageService.class));
		SharedPreferences sp = getSharedPreferences("showonmap", MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("isShow", "y");
		edit.commit();
		PushAgent.getInstance(this).setDebugMode(true);
		PushAgent.getInstance(this).setMessageHandler(
				new UmengMessageHandler() {
					@Override
					public void dealWithNotificationMessage(Context arg0,
							UMessage msg) {
						// 调用父类方法,这里会在通知栏弹出提示信息
						super.dealWithNotificationMessage(arg0, msg);
						Log.e("", "### 自行处理推送消息");
					}
				});

//		 OpenIMAgent im = OpenIMAgent.getInstance(this);
//		 im.init();
		
		// 创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				this).writeDebugLogs() // 打印log信息
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);
		option.setScanSpan(1000 * 60 * 60 * 24);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	/*****
	 * @see copy funtion to you project
	 *      闂佽姘﹂～澶愭儗椤斿墽绀婇柛娑卞灣绾惧ジ鏌熼幆褜鍤熷ù婊庡灦閺屾盯濡疯娴犫晠鏌ｉ妸褍鏋涢柡浣哥Т閻ｆ繈宕熼
	 *      锝庝户闂備礁鎲￠崝鏍偘椤ょⅵeceiveLocation闂備礁鎼
	 *      崐浠嬶綖婢跺本鍏滈柛顐ｆ礃閺咁剟鎮橀悙鑸殿棄閻犱焦褰冮湁闁绘﹩鍠栭崝銈嗙箾閹绘帗鍋ユ
	 *      鐐村浮婵＄兘鏁冮敓鎴掔处闂佽崵濮甸崝鏍崲閸岀偑锟界憸搴ｇ矙婢跺矉鎷烽柍杞扮劍閺嗘洟姊哄ú璇叉灆缂侊拷绶氬畷鍝勭暆閸曨剙娈滃
	 *      銈呯箰鐎氼喚绮婚幒鏃撴嫹闁硅偐鍋涢崝銈夋煃鐠囨彃鎮戠紒瀣樀椤㈡ê鈹戦崶褍浼庨梻浣哄皑閹风兘骞忛敓锟�
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (null != location
					&& location.getLocType() != BDLocation.TypeServerError) {
				x = location.getLatitude();
				y = location.getLongitude();
			}
		}
	};
	
	public void clearTempFromPref() {
		SharedPreferences sppic = getSharedPreferences(
				CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
		Editor editor = sppic.edit();
		editor.clear();
		editor.commit();
		FileUtil.deleteDirfile(FileUtil.SDPATH);
        editor.putString(CustomConstants.PREF_TEMP_IMAGES, "");
        editor.putString(CustomConstants.PREF_TEMP_IMAGES1, "");
        editor.putString(CustomConstants.PREF_TEMP_IMAGES2, "");
        editor.putString(CustomConstants.PREF_TEMP_IMAGES3, "");
        editor.putString(CustomConstants.PREF_TEMP_IMAGES4, "");
        editor.commit();
		

	}
}
