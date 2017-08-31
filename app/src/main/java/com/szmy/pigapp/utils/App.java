package com.szmy.pigapp.utils;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.szmy.pigapp.entity.UserInfo;
import com.szmy.pigapp.service.BroadcastReceiverHelper;
import com.szmy.pigapp.service.CoreAppService;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class App extends Application implements BDLocationListener {
	static String TAG = App.class.getSimpleName();
	static final boolean DEBUG = true;
	static Toast mToast = null;
	public static Boolean mShowNoticeDialog = false;
	public AlertDialog updateDialog;
	public final static String CONF_APP_UNIQUEID = "CONF_APP_UNIQUEID";
	// public static Version mVersion = new Version();

	// 临时存储集合（供集成模块临时存储）
	private static Map<String, String> __tempStoreMap = new HashMap<String, String>();
	// 全局外部配置文件
	public static Map<String, String> __app_external_configure = new HashMap<String, String>();
	// 用户归属定位（采用国家行政区划编码）
	private static String __app_region_code = "000000";
	// 服务器域名
	public static String __app_server_uri = "";
	// BIDG服务地址
	public static String __app_bidg_uri = "";
	// 广播服务
	BroadcastReceiverHelper broadcastReceiverHelper;

	// httpclient
	private static HttpClient mHttpClient = null;
	private static final String CHARSET = HTTP.UTF_8;

	public static Context appContext;
	public static String USERINFO = "USERINFO";
	public static String username = "";
	public static String userID = "";
	public static String uuid = "";
	public static String userphone = "";
	public static String userphoto = "";
	public static String usertype = "";
	public static String province = "";
	public static String city = "";
	public static String area = "";
	public static String authentication = "";
	public static String pwd = "";
	public static String fxsType = "";//分销商类型   null不是分销商    1:经销商  2:个人(审核通过才有值)
	public static String billtype = ""; //过磅单或白条肉单状态
	public static String source = "";//猪贸通还是神州牧易
	public static UserInfo mUserInfo;
	public static String scrytype = ""; //1、普通市场 2、管理员 3、非市场人员
	static String db_path = "";

	public static String GPS_X = "";
	public static String GPS_Y = "";
	public static String PROVINCE = "";
	public static String CITY = "";
	public static String AREA= "";
	public static String ADDRESS = "";
	public static String IMEI = "";
//	public static YWIMKit mIMKit = null;
	public static String WEATHERAPIKEY = "52577b01d25733cd63fc303292312ce6";// 百度天气签名
	public static int SHAREID = 0;//MODE_PRIVATE 0
	
public static Map<String,List<Map<String,String>>> mapclass = new HashMap<String, List<Map<String,String>>>();
	// Common var
	public static String __app_name = "";
	public static String __app_region_name = "";
	// 平台模式
	public static String __platform = "pub";

	// public static BitmapUtils mImageLoader;
	public static final String SYNC_VER = "APP.1.02";

	public static LocationClient mLocationClient = null;
	// Server server;
	// WebAppContext context;

	static App mApp;
	
	static UserInfo userinfo;
	
	public static UserInfo getUserinfo() {
		return userinfo;
	}

	public static void setUserinfo(UserInfo userinfo) {
		App.userinfo = userinfo;
	}

	static String MapKEY = "L4KYM9UffmYUZSAkijo125k0";//测试
//	static String MapKEY = "YRcVhVvM1qk9Hq6KNZI4j1O5";//正式

	// Thread td;
	// boolean threadRun = true;

	public static final  int ADDRESS_CODE = 222;
	public static final  int KAIHUHANG_CODE = 223;
	public static final  int READ_CODE = 224;
	public static final  int SLAUGHTERRZ_CODE = 225;


	@Override
	public void onCreate() {
		super.onCreate();
		mApp = this; // 获得App实例
		appContext = this;// 初始化ApplicationContext

		initBDLocation();// 百度定位

		// 创建http通讯对象实例
		mHttpClient = createHttpClient();
		mUserInfo = FileUtil.readUser(this);
		if(mUserInfo!=null)
		setDataInfo(App.mUserInfo);
		

		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
		BroadcastReceiverHelper receiver = new BroadcastReceiverHelper();
		registerReceiver(receiver, filter);

		Intent service = new Intent(this, CoreAppService.class);
		startService(service);
	}
	public static App get() {
		return mApp;
	}

	/**
	 * 测试网络是否异常
	 * 
	 * @author Lee
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String checkRouter(String url) throws ClientProtocolException,
			IOException {
		HttpClient httpClient = App.getHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(get);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity(),
					"utf-8");
			return result;
		}
		return null;
	}

//	/**
//	 * 图片加载
//	 *
//	 * @param url
//	 * @param iv
//	 * @param onLoading
//	 * @param onFailed
//	 */
	// public void loadImg(String url, ImageView iv, final int onLoading, final
	// int onFailed) {
	//
	// mImageLoader.display(iv, url, new BitmapLoadCallBack<ImageView>() {
	// @Override
	// public void onLoading(ImageView container, String uri,
	// BitmapDisplayConfig config, long total, long current) {
	// super.onLoading(container, uri, config, total, current);
	// }
	//
	// @Override
	// public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {
	// arg0.setImageResource(onFailed);
	// }
	//
	// @Override
	// public void onLoadCompleted(ImageView arg0, String arg1, Bitmap arg2,
	// BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
	// arg0.setImageBitmap(arg2);
	// }
	// });
	// }

	// public String __app_external_configure() {
	// // 对象压栈
	// try {
	// LuaState L = LuaStateFactory.newLuaState();
	// L.openLibs();
	// L.LdoString(loadAssetsString(appContext, "hello.lee"));
	// // 实例1.Java调用lua函数
	// L.getField(LuaState.LUA_GLOBALSINDEX, "heihei");
	// // L.pushNumber(12);
	// // L.pushNumber(2132);
	// L.call(0, 1);
	// L.setField(LuaState.LUA_GLOBALSINDEX, "a");
	// LuaObject obj = L.getLuaObject("a");
	// App.showLog("" + obj.getString());
	// return obj.getString();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return "Unknown";
	// }

	public void initBDLocation() {
		SDKInitializer.initialize(appContext);

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		LocationClientOption option = new LocationClientOption();
		// Hight_Accuracy高精度、Battery_Saving低功耗、Device_Sensors仅设备(GPS)
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("wgs84");// 坐标系 wgs84：GPS 坐标 bd09ll：百度坐标
		option.setScanSpan(1000 * 60 * 7);// 设置发起定位请求的间隔时间
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

	public void stopBDLocation() {
		if (mLocationClient != null) {
			mLocationClient.stop();
		}
	}

	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) appContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	// public int getNetType() {
	// ConnectivityManager cm = (ConnectivityManager)
	// appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	// NetworkInfo ni = cm.getActiveNetworkInfo();
	// if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
	// return NetType.WIFI_CONNECTED;
	// } else if (ni.getType() == ConnectivityManager.TYPE_MOBILE) {
	// return NetType.MOBILE_CONNECTED;
	// }
	// return NetType.NONE_CONNECTED;
	// }

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public static PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = appContext.getPackageManager().getPackageInfo(
					appContext.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	public boolean hasFlashLight() {
		boolean hasFeature = false;
		FeatureInfo[] features = getPackageManager()
				.getSystemAvailableFeatures();
		for (FeatureInfo f : features) {
			if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) {
				hasFeature = true;
			}
		}
		return hasFeature;
	}

	// 获取手机序列号
	public String getIMEI() {
		TelephonyManager tm = (TelephonyManager) appContext
				.getSystemService(TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	// 初始化sqlite数据

	// 数据库文件 从assets文件夹 移动到SD卡
	public static File getAssetFileToDBDir(Context context, String fileName) {
		try {
			File f = new File(db_path + "/" + fileName);

			if (!f.exists()) {
				f.createNewFile();
			} else {
				if (f.delete()) {
					App.showLog("delete " + f.getName() + " ok....");
				} else {
					App.showLog("delete " + f.getName() + " error....");
				}
			}
			String databaseDir = f.getAbsolutePath();
			App.showLog(databaseDir);
			InputStream is = context.getAssets().open(fileName);
			File file = new File(databaseDir);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];

			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			return file;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String loadAssetsString(Context c, String resPath) {
		InputStream isread = null;
		byte[] luaByte = new byte[1];
		try { // 就是这里了，我们把lua 都放到asset目录下，这样系统就 //不会找不到文件路径了，哼~

			isread = c.getAssets().open(resPath);
			int len = isread.available();
			luaByte = new byte[len];
			isread.read(luaByte);
		} catch (IOException e1) {

		} finally {
			if (isread != null) {
				try {
					isread.close();
				} catch (IOException e) {
				}
			}
		}
		return EncodingUtils.getString(luaByte, "UTF-8");
	}

	// MD5加密，32位
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	public static String doException(String errorCode) {

		return "";
	}

	public static void showLog(String tag, String msg) {
		if (DEBUG) {
			Log.d("--TAG--" + tag, msg);
		}
	}

	public static void showLog(String msg) {
		if (DEBUG) {
			Log.e("sdydbj", msg);
		}
	}

	public static void showToast(String msg) {
		if (DEBUG) {
			if (mToast == null) {
				mToast = Toast.makeText(appContext, msg, Toast.LENGTH_SHORT);
			} else {
				mToast.setText(msg);
				mToast.setDuration(Toast.LENGTH_SHORT);
			}
			mToast.show();
		}
	}

	// 检查apk是否安装
	public static boolean appIsInstalled(Context context, String packageName) {
		try {
			context.getPackageManager().getPackageInfo(packageName, 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	// 安装打印程序
	// public static void installPrinter(Context c) {
	// Intent intent = new Intent(Intent.ACTION_VIEW);
	// File file = FileUtil.AssetToSD(appContext,
	// appContext.getExternalCacheDir()+File.separator+PRINTER_FILE_NAME,
	// PRINTER_FILE_NAME);
	// intent.setDataAndType(Uri.fromFile(file),
	// "application/vnd.android.package-archive");
	// c.startActivity(intent);
	// }

	// 创建目录
	public static String createDir(String dirStr) {
		File dir = new File(dirStr);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir.getPath();
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null) {
			return;
		} else {

			GPS_X = String.valueOf(location.getLongitude());
			GPS_Y = String.valueOf(location.getLatitude());

			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			showLog(sb.toString());
		}
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		this.shutdownHttpClient();
		// 取消广播接收器
		unregisterReceiver(broadcastReceiverHelper);
		// 定位关闭
		stopBDLocation();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		// this.shutdownHttpClient();
	}

	/**
	 * 创建HttpClient实例
	 * 
	 * @return
	 */
	private HttpClient createHttpClient() {

		HttpParams params = new BasicHttpParams();
		// 设置基本参数
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		// 超时设置
		/* 从连接池中取连接的超时时间 */
		ConnManagerParams.setTimeout(params, 6000);
		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout(params, 30000);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(params, 6000);
		// 设置HttpClient支持HTTp和HTTPS两种模式
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));
		// 使用线程安全的连接管理来创建HttpClient
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);
		HttpClient client = new DefaultHttpClient(conMgr, params);
		return client;
	}

	private void shutdownHttpClient() {
		if (mHttpClient != null && mHttpClient.getConnectionManager() != null) {
			mHttpClient.getConnectionManager().shutdown();
		}
	}

	public synchronized static HttpClient getHttpClient() {
		return mHttpClient;
	}

	/**
	 * @author Lee
	 * @param key
	 * @return
	 */
	public synchronized static String getTempStoreMapValue(final String key) {
		return __tempStoreMap.get(key);
	}

	/**
	 * @author Lee
	 * @param key
	 * @param value
	 * @return
	 */
	public synchronized static boolean setTempStoreMapValue(final String key,
			final String value) {
		__tempStoreMap.put(key, value);
		return true;
	}

	public static UserInfo getUserInfo() {
		return mUserInfo;
	}

	public static void setUserInfo(UserInfo userInfo) {
		mUserInfo = userInfo;
	}

	/**
	 * 获取时间
	 */
	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}

	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
	}
	
	public static void setDataInfo(UserInfo user){
		if(user == null){
			return;
		}
		App.userID = user.getId();
		App.username = user.getUsername();
		App.uuid = user.getUuid();
		App.userphone = user.getPhone();
		App.userphoto = user.getPicture();
		App.usertype = user.getType();
		App.authentication = user.getAuthentication();
		App.GPS_X = user.getX();
		App.GPS_Y = user.getY();
		App.PROVINCE =user.getProvince();
		App.CITY =user.getCity();
		App.AREA =user.getArea();
		App.ADDRESS =user.getAddress();
		App.fxsType = user.getFxsType();
		App.scrytype = user.getScrytype();
		
	}
	
	public static void clearData(){
		App.mUserInfo = null;
		App.userID = "";
		App.username = "";
		App.uuid = "";
		App.userphone = "";
		App.userphoto = "";
		App.usertype = "";
		App.authentication = "";
		App.GPS_X = "";
		App.GPS_Y = "";
		App.PROVINCE ="";
		App.CITY ="";
		App.AREA ="";
		App.ADDRESS ="";
		App.pwd = "";
		App.fxsType = "";
		App.scrytype = "";
	}

	//角色身份
	public static String getTypeName(String type){
		String name = "";
		if (FileUtil.isNumeric(type)){
			switch(Integer.parseInt(type)){
				case 1:
					name = "猪场";
					break;
				case 2:
					name = "屠宰场";
					break;
				case 3:
					name = "经纪人";
					break;
				default:
					break;
			}
		}
		return name;
	}
}
