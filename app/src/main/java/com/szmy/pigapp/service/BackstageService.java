package com.szmy.pigapp.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.service.Location2Service;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

public class BackstageService extends Service {
	private static final String TAG = "LocationService";

	public Location2Service locationService;
	public Vibrator mVibrator;
	public SharedPreferences usersp;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		
		Log.i(TAG, "onCreate");

		if (AppStaticUtil.isNetwork(getApplicationContext())) {
			locationService = new Location2Service(getApplicationContext());
			locationService.registerListener(mListener);
		}

	}

	/*****
	 * @see copy funtion to you project
	 *      定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 * 
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (null != location
					&& location.getLocType() != BDLocation.TypeServerError) {

//				 Toast.makeText(getApplicationContext(),
//				 "x:"+location.getLatitude()+"y:"+location.getLongitude(),
//				 Toast.LENGTH_SHORT).show();
				String x = String.valueOf(location.getLatitude());
				String y = String.valueOf(location.getLongitude());
				if(x.equals("")||x.equals("0.0")){
					return;
				}
				
				SharedPreferences sp = getSharedPreferences("LOCATION",
						MODE_PRIVATE);
				if((x.equals(sp.getString("x", "")))&&(y.equals(sp.getString("y", "")))){
					
					return;
				}
				
				Editor edit = sp.edit();
				edit.putString("x", x);
				edit.putString("y", y);
				edit.commit();
				
				 usersp = getSharedPreferences(App.USERINFO,
						MODE_PRIVATE);
				if (!TextUtils.isEmpty(usersp.getString("userinfo", ""))) {
					App.mUserInfo = FileUtil.readUser(getApplicationContext());
					App.setDataInfo(App.mUserInfo);
				}
				if (!TextUtils.isEmpty(App.uuid)) {
					sendData(location);
				}
			} else {

			}
		}
	};

	@Override
	public void onDestroy() {
		
		Log.i(TAG, "onDestroy");

	}

	@Override
	public void onStart(Intent intent, int startid) {
		
		Log.i(TAG, "onStart");

	}

	private void sendData(BDLocation location) {

		RequestParams params = new RequestParams();
		params.addBodyParameter("e.province", location.getProvince());
		params.addBodyParameter("e.city", location.getCity());
		params.addBodyParameter("e.area", location.getDistrict());
		params.addBodyParameter("e.address",
				location.getStreet() + location.getStreetNumber());
		params.addBodyParameter("e.x", String.valueOf(location.getLatitude()));
		params.addBodyParameter("e.y", String.valueOf(location.getLongitude()));
		params.addBodyParameter("e.userName", App.username);
		params.addBodyParameter("uuid", App.uuid);

		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.INSERT_LOCATION_URL,
				params, new RequestCallBack<String>() {
					@Override
					public void onStart() {
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						JSONObject jsonresult;
						try {
							System.out.println(responseInfo.result);
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {

							} else if(jsonresult.get("success").toString()
									.equals("0")) {
								Editor edit = usersp.edit();
								edit.putString("userinfo", "");
								edit.commit();
								
								
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {

					}
				});
	}

}