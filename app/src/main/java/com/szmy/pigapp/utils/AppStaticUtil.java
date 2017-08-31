package com.szmy.pigapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.szmy.pigapp.R;

import org.json.JSONObject;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 无需传入Context的工具类
 * 
 */
public class AppStaticUtil {

	public static DisplayImageOptions getOptions() {
		return new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Config.RGB_565).build();
	}

	public static final void toastWelcome(final Context context,
			final String name) {
		final Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		final View view = LayoutInflater.from(context).inflate(
				R.layout.toast_welcome, null);
		final TextView tvName = (TextView) view.findViewById(R.id.tv_name);
		tvName.setText(name);
		toast.setView(view);
		toast.setDuration(3000);
		toast.show();
	}
	public static final void toastPricePoint(final Context context) {
		final Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		final View view = LayoutInflater.from(context).inflate(
				R.layout.toast_pricepoint, null);
		toast.setView(view);
		toast.setDuration(3000);
		toast.show();
	}
	/**
	 * 判断是否有网络
	 */
	public static boolean isNetwork(Context ctx) {
		if (ctx == null) {
			return false;
		}
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 判断是否为数字
	 */
	public static boolean isNumeric(String str) {
		if (str == null || str.isEmpty())
			return false;
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 是否为空
	 */
	public static boolean isBlank(String src) {
		if (src == null)
			return true;
		src = src.trim();
		if (src == null)
			return true;
		if (src.equals(""))
			return true;
		return false;
	}

	public static boolean isLogin(Context context) {
		boolean falg = false;
		SharedPreferences sp = context.getSharedPreferences("USERINFO", 0);
		if (App.mUserInfo == null) {
			App.mUserInfo = FileUtil.readUser(context);
		}
		App.setDataInfo(App.mUserInfo);
		if (TextUtils.isEmpty(App.uuid)) {
			falg = true;
		} else {
			App.setDataInfo(App.mUserInfo);
		}
		return falg;
	}

	/**
	 * 固定ListView的高度
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += (listItem.getMeasuredHeight() + 4);
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		params.height += 10;
		listView.setLayoutParams(params);
	}

	/**
	 * 解析月份，如果月份小于10，前面加0
	 */
	public static String parseMonth(int month) {
		if ((month + 1) < 10) {
			return "0" + (month + 1);
		} else {
			return String.valueOf((month + 1));
		}
	}

	/**
	 * 解析日期，如果日期小于10，前面加0
	 */
	public static String parseDayOfMonth(int day) {
		if (day < 10) {
			return "0" + day;
		} else {
			return String.valueOf(day);
		}
	}

	/**
	 * 传送公共参数用户名、密码及随机码
	 */
	public static JSONObject parm(JSONObject obj) {

		return obj;
	}

	/**
	 * 获得一个UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString().toUpperCase()
				.replaceAll("-", "");
		// return (s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)+
		// s.substring(19, 23) + s.substring(24)).toUpperCase();
		return s;
	}

	/**
	 * 验证号码 手机号 固话均可
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
		String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 字符串转double类型
	 */
	public static double getConvertDouble(String str) {
		double i = 0;
		try {
			i = Double.parseDouble(str);
		} catch (Exception e) {
			i = 0;
		}

		return i;
	}

	private static Bitmap cutBmp(Bitmap bmp) {
		Bitmap result;
		int w = bmp.getWidth();// ���볤���ο�
		int h = bmp.getHeight();// ���볤���θ�
		int nw;// ������ο�
		if (w > h) {
			// ����ڸ�
			nw = h;
			result = Bitmap.createBitmap(bmp, (w - nw) / 2, 0, nw, nw);
		} else {
			// �ߴ��ڿ�
			nw = w;
			result = Bitmap.createBitmap(bmp, 0, (h - nw) / 2, nw, nw);
		}
		return result;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPX) {
		Bitmap bmp = cutBmp(bitmap);
		Bitmap output = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bmp, rect, rect, paint);
		return output;
	}

	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符
	 * 
	 * 渠道标志为： 1，andriod（a）
	 * 
	 * 识别符来源标志： 1， wifi mac地址（wifi）； 2， IMEI（imei）； 3， 序列号（sn）； 4，
	 * id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(SharedPreferences sp, Editor editor,
			Context context) {
		StringBuilder deviceId = new StringBuilder();
		// 渠道标志
		deviceId.append("a");
		try {
			// wifi mac地址
			WifiManager wifi = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			String wifiMac = info.getMacAddress();
			if (!TextUtils.isEmpty(wifiMac)
					&& !wifiMac.equals("00:00:00:00:00:00")) {
				deviceId.append("wifi");
				deviceId.append(wifiMac);
				System.out.println("getDeviceId : " + deviceId.toString());
				return deviceId.toString();
			}
			// IMEI（imei）
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String imei = tm.getDeviceId();
			if (!TextUtils.isEmpty(imei)) {
				deviceId.append("imei");
				deviceId.append(imei);
				System.out.println("getDeviceId : " + deviceId.toString());
				return deviceId.toString();
			}
			// 序列号（sn）
			String sn = tm.getSimSerialNumber();
			if (!TextUtils.isEmpty(sn)) {
				deviceId.append("sn");
				deviceId.append(sn);
				System.out.println("getDeviceId : " + deviceId.toString());
				return deviceId.toString();
			}
			// 如果上面都没有， 则生成一个id：随机码
			String uuid = getUUID(sp, editor, context);
			if (!TextUtils.isEmpty(uuid)) {
				deviceId.append("id");
				deviceId.append(uuid);
				System.out.println("getDeviceId : " + deviceId.toString());
				return deviceId.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			deviceId.append("id").append(getUUID(sp, editor, context));
		}
		System.out.println("getDeviceId : " + deviceId.toString());
		return deviceId.toString();
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 得到全局唯一UUID
	 */
	public static String getUUID(SharedPreferences sp, Editor editor,
			Context context) {
		String uuid = sp.getString("uuid", "");
		if (TextUtils.isEmpty(uuid)) {
			uuid = UUID.randomUUID().toString();
			editor.putString("uuid", uuid);
		}
		System.out.println("getUUID : " + uuid);
		return uuid;
	}

	/**
	 * 计算地球上任意两点(经纬度)距离
	 * 
	 * @param long1
	 *            第一点经度
	 * @param lat1
	 *            第一点纬度
	 * @param long2
	 *            第二点经度
	 * @param lat2
	 *            第二点纬度
	 * @return 返回距离 单位：米
	 */
	public static double Distance(double long1, double lat1, double long2,
			double lat2) {
		double a, b, R;
		R = 6378137; // 地球半径
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		a = lat1 - lat2;
		b = (long1 - long2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2
				* R
				* Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
						* Math.cos(lat2) * sb2 * sb2));
		return d;
	}
	/**
	 * 返回当前程序版本号
	 */
	public static int getAppVersionCode(Context context) {
		int versionCode = 0;
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode = pi.versionCode;
		} catch (Exception e) {
		}
		return versionCode;
	}
}
