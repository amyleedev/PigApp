package com.szmy.pigapp.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gc.materialdesign.widgets.Dialog;
import com.szmy.pigapp.BuildConfig;
import com.szmy.pigapp.image.CustomConstants;
import com.szmy.pigapp.image.ImageItem;
import com.szmy.pigapp.updateservice.UpdateInfo;
import com.szmy.pigapp.updateservice.UpdateServcie;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppManager;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.widget.LoadingDialog;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TargetApi(VERSION_CODES.KITKAT)
public class BaseActivity extends FragmentActivity {
	public int FROM_FORM = 22;
	public int FROM_LIST = 33;
	private LoadingDialog dialog = null;
	public Toast mToast = null;
	public Editor editor;
	public SharedPreferences sp;
	public static final String SETTING_USERNAME = "username";
	public static final String SETTING_PASSWORD = "password";
	public static final String SETTING_INTEGRAL_POLICY = "integral_policy";
	public static final String SETTING_ISFIRST = "is_first";
	public static final String SETTING_ONLY_WIFI = "only_wifi";
	public static final String SETTING_ALIAS="set_alias";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		AppManager.addActivity(this);
		sp = getSharedPreferences(App.USERINFO, MODE_PRIVATE);
		editor = sp.edit();
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	//
	public void showLog(String tag, String msg) {
		if (BuildConfig.DEBUG)
			Log.i(tag, msg);
	}

	//
	public void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(getApplicationContext(), msg,
					Toast.LENGTH_SHORT);
		} else {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	public void loadFail() {
		showToast("加载失败");
	}

	// 环形加载进度条
	public void showDialog() {
		if (dialog != null) {
			dialog = null;
		}
		dialog = new LoadingDialog(BaseActivity.this);
		dialog.setCancelable(true);
		dialog.show();
	}

	public void showDialog(String msg) {
		dialog = new LoadingDialog(this, msg);
		dialog.show();
	}

	public void disDialog() {
		if (dialog != null && dialog.isShowing()) {
			try{
				dialog.dismiss();
			}catch (Exception e){

			}

		}
	}

	//
	public void back(View v) {
		// onBackPressed();
		finish();
	}

	public void logout() {
		editor.putString("userinfo", "");
		App.clearData();
		editor.commit();
		// AppManager.getAppManager().AppExit(getApplicationContext());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

	public boolean isLogin(Context context) {
		boolean falg = false;
		SharedPreferences sp = getSharedPreferences("USERINFO", MODE_PRIVATE);
		if (App.mUserInfo == null) {
			App.mUserInfo = FileUtil.readUser(context);
		}
		App.setDataInfo(App.mUserInfo);
		if (TextUtils.isEmpty(App.uuid)) {
			falg = true;
			Intent intent = new Intent(context, LoginActivity.class);
			startActivity(intent);
		} else {
			App.setDataInfo(App.mUserInfo);
			// App.userID = sp.getString("id", "");
			// App.username = sp.getString("username", "");
			// App.uuid = sp.getString("loginkey", "");
			// App.userphone = sp.getString("phone", "");
			// App.userphoto = sp.getString("PHOTO", "");
			// App.usertype = sp.getString("TYPE", "");
			// App.authentication = sp.getString("authentication", "");
			// App.GPS_X = sp.getString("x", "");
			// App.GPS_Y = sp.getString("y", "");
			// App.PROVINCE = sp.getString("province", "");
			// App.CITY = sp.getString("city", "");
			// App.AREA = sp.getString("area", "");
			// App.ADDRESS = sp.getString("address", "");
		}
		return falg;
	}

	public void relogin(Context context) {
		editor.putString("userinfo", "");
		editor.commit();
		App.clearData();
		Intent intent = new Intent(context, LoginActivity.class);
		startActivityForResult(intent, 22);
	}

	public void successType(String successtype, String msg) {
		if (successtype.equals("0")) {
			showToast("用户信息错误,请重新登录！");
			relogin(getApplicationContext());
		} else if (successtype.equals("3")) {
			showToast(msg);
		} else if (successtype.equals("2")) {
			showToast(msg);
		}
	}
	public void saveMapTempToPref(Map<String,ImageItem> maps){

		SharedPreferences sp = getSharedPreferences(
				CustomConstants.APPPicLICATION_NAME, MODE_PRIVATE);
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(maps);
			// 将字节流编码成base64的字符窜
			String oAuth_Base64 = new String(Base64.encodeBase64(baos.toByteArray()));
			Editor editor = sp.edit();
			editor.putString(CustomConstants.APPPicLICATION_NAME, oAuth_Base64);

			editor.commit();
		} catch (IOException e) {
			// TODO Auto-generated
		}

	}

	public Map<String,ImageItem> readMapTempFromPref(){
		Map<String,ImageItem> maps = new HashMap<>();
		SharedPreferences preferences = getSharedPreferences(CustomConstants.APPPicLICATION_NAME,
				1);
		String productBase64 = preferences.getString(CustomConstants.APPPicLICATION_NAME, "");

		if(productBase64.equals("")){
			return maps;
		}
		//读取字节
		byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

		//封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			//再次封装
			ObjectInputStream bis = new ObjectInputStream(bais);
			try {
				//读取对象
				maps = (Map<String, ImageItem>) bis.readObject();
			} catch (ClassNotFoundException e) {

			}
		} catch (StreamCorruptedException e) {

		} catch (IOException e) {

		}
		return maps;
	}
	public void  clearMapTempFronPref(){
		SharedPreferences preferences = getSharedPreferences(CustomConstants.APPPicLICATION_NAME,
				1);
		Editor editor1 = preferences.edit();
		FileUtil.deleteDirfile(FileUtil.SDPATH);
		editor1.clear();
		editor1.putString(CustomConstants.APPPicLICATION_NAME, "");
		editor1.commit();
	}
	public Map<String,ImageItem> setPicToView(Intent picdata, String path,
											  Map<String,ImageItem> maps, String picTypeName) {

		System.out.println(path);
		System.out.println(FileUtil.getFileOrFilesSize(path,
				CustomConstants.SIZETYPE_KB) + "kb");
		if (FileUtil.getFileOrFilesSize(path, CustomConstants.SIZETYPE_KB) == 0.0) {
			return maps;
		}
		if (FileUtil.getFileOrFilesSize(path, CustomConstants.SIZETYPE_KB) > 150) {
			maps.put(picTypeName,FileUtil.saveDataPic(path));
		} else {
			ImageItem item = new ImageItem();
			item.sourcePath = path;
			maps.put(picTypeName,item);
		}
		return maps;
	}
	public void dialog(String message, final String phone) {
//		AlertDialog.Builder builder = new Builder(this);
//		builder.setMessage(message);
//		builder.setTitle("提示");
//		builder.setPositiveButton("确认", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
//						+ phone));
//				startActivity(intent);
//			}
//		});
//		builder.setNegativeButton("取消", null);
//		builder.create().show();
		final Dialog dialog = new Dialog(this, "提示", message);
		dialog.addCancelButton("取消");
		dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phone));
				startActivity(intent);
			}
		});
		dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.show();
	}

	/**
	 * 获得屏幕高或宽度
	 * 
	 * @return
	 */
	public int getSize() {
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		// int height = display.getHeight();
		// if (mIsWidth)
		// return width < height ? width : height; // 屏幕宽度（像素）
		// else
		// return height > width ? height : width; // 屏幕高度（像素）
		return width;
	}

	public void hideInput() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		// 接受软键盘输入的编辑文本或其它视图
	}

	public String getUserUUid(Context context) {
		SharedPreferences sp = context.getSharedPreferences("USERINFO", 0);
		if (App.mUserInfo == null) {
			App.mUserInfo = FileUtil.readUser(context);
		}
		App.setDataInfo(App.mUserInfo);
		if (!TextUtils.isEmpty(App.uuid)) {
			return App.uuid;
		}
		return "";
	}

	public void showDialog1(Context context, String msg) {
//		AlertDialog.Builder builder = new Builder(context);
//		builder.setMessage(msg);
//		builder.setTitle("提示");
//		builder.setPositiveButton("确定", null);
//		builder.create().show();
		
		final Dialog dialog = new Dialog(context,"提示",msg);
		dialog.addCancelButton("取消");		
		dialog.show();
	}

	/**
	 * 选择提示对话框
	 */
	public void ShowPickDialog(final String pathname, final Uri uri) {
		new AlertDialog.Builder(this)
				.setTitle("请选择")
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						Intent intent = new Intent(Intent.ACTION_PICK, null);

						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent, 1);
					}
				})
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();

						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						// 下面这句指定调用相机拍照后的照片存储的路径
						intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
						startActivityForResult(intent, 2);
					}
				}).show();
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri, String picname, Uri imageuri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);// 图像输出
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, 3);
	}
	public void startPhotoZoom1(Intent data, String path, int returncode) {
		Uri uri = geturi(data);// data.getData();
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));// 图像输出
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, returncode);
	}
	/**
	 * 解决小米手机上获取图片路径为null的情况
	 *
	 * @param intent
	 * @return
	 */
	public Uri geturi(Intent intent) {
		Uri uri = intent.getData();
		String type = intent.getType();
		if (uri.getScheme().equals("file") && (type.contains("image/"))) {
			String path = uri.getEncodedPath();
			if (path != null) {
				path = Uri.decode(path);
				ContentResolver cr = this.getContentResolver();
				StringBuffer buff = new StringBuffer();
				buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
						.append("'" + path + "'").append(")");
				Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						new String[] { MediaStore.Images.ImageColumns._ID },
						buff.toString(), null, null);
				int index = 0;
				for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
					index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
					// set _id value
					index = cur.getInt(index);
				}
				if (index == 0) {
					// do nothing
				} else {
					Uri uri_temp = Uri
							.parse("content://media/external/images/media/"
									+ index);
					if (uri_temp != null) {
						uri = uri_temp;
					}
				}
			}
		}
		return uri;
	}
	public String setPicToView(Intent picdata, ImageView iv, String picname) {
		String path = "";
		Bitmap photo = null;
		if (picdata != null) {
			Bundle extras = picdata.getExtras();
			photo = extras.getParcelable("data");
		}
		if (photo == null)// 加载本地截图
		{
			if (FileUtil.isFileExist(picname + ".jpg")) {
				Bitmap bm = BitmapFactory.decodeFile(FileUtil.SDPATH + picname
						+ ".jpg");
				FileUtil.saveBitmap(bm, picname);
				iv.setImageBitmap(bm);
			}
		} else// 加载接收的图片数据
		{
			FileUtil.saveBitmap(photo, picname);
			iv.setImageBitmap(photo);
		}
		path = FileUtil.SDPATH + picname + ".jpg";
		return path;
	}

	/**
	 * 保存图片信息到SharedPreferences
	 * 
	 * @param temp_name
	 * @param mDataList
	 */
	public void saveTempToPref(String temp_name, List<ImageItem> mDataList) {
		SharedPreferences sp = getSharedPreferences(
				CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
		String prefStr = JSON.toJSONString(mDataList);
		sp.edit().putString(temp_name, prefStr).commit();
	}

	/**
	 * 从SharedPreferences中取出图片信息
	 * 
	 * @param temp_name
	 * @return
	 */
	public List<ImageItem> getTempFromPref(String temp_name) {
		List<ImageItem> mDataList = new ArrayList<ImageItem>();
		SharedPreferences sp = getSharedPreferences(
				CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
		String prefStr1 = sp.getString(temp_name, null);
		if (!TextUtils.isEmpty(prefStr1)) {
			List<ImageItem> tempImages = JSON.parseArray(prefStr1,
					ImageItem.class);
			mDataList = tempImages;
		}
		return mDataList;
	}

	/**
	 * 清空SharedPreferences中的图片信息
	 */
	public void clearTempFromPref() {
		SharedPreferences sppic = getSharedPreferences(
				CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
		Editor editor1 = sppic.edit();
		editor1.clear();
		editor1.commit();
		FileUtil.deleteDirfile(FileUtil.SDPATH);
		editor1.putString(CustomConstants.PREF_TEMP_IMAGES, "");
		editor1.putString(CustomConstants.PREF_TEMP_IMAGES1, "");
		editor1.putString(CustomConstants.PREF_TEMP_IMAGES2, "");
		editor1.putString(CustomConstants.PREF_TEMP_IMAGES3, "");
		editor1.putString(CustomConstants.PREF_TEMP_IMAGES4, "");
		editor1.commit();
	}
	public void clearTempFromPrefByName(String name) {
		SharedPreferences sppic = getSharedPreferences(
				CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
		Editor editor1 = sppic.edit();
		editor1.putString(name, "");
		editor1.commit();
	}
	public void removeTempFromPref(String temp_name) {
		SharedPreferences sp = getSharedPreferences(
				CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.remove(temp_name);
		editor.commit();
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	public boolean isShowAddItem(List<ImageItem> list, int position) {
		int size = list == null ? 0 : list.size();
		return position == size;
	}

	/**
	 * 获取file
	 * 
	 * @return
	 */
	public File getFile() {
		File vFile = new File(FileUtil.SDPATH, String.valueOf(System
				.currentTimeMillis()) + ".jpg");
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		} else {
			if (vFile.exists()) {
				vFile.delete();
			}
		}
		return vFile;
	}

	/**
	 * 获取图片路径
	 * 
	 * @param vFile
	 * @return
	 */
	public String getPath(File vFile) {
		return vFile.getPath();
	}

	/***
	 * 拍照
	 * 
	 * @param resultcode
	 * @param file
	 */
	public void takePhoto(int resultcode, File file) {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri cameraUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		startActivityForResult(openCameraIntent, resultcode);
	}

	public void takeAlbum(int resultcode) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, resultcode);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri, String path, int returncode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));// 图像输出
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, returncode);
	}

	/**
	 * 裁剪图片方法实现 按比例剪裁
	 * 
	 * @param uri
	 */
	public void startPhotoZoombyRatio(Uri uri, String path, int returncode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));// 图像输出
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, returncode);
	}

	public List<ImageItem> setPicToView(Intent picdata, String path,
			List<ImageItem> mDataList, int i) {
		if (i == 0) {
			mDataList.clear();
		}
		System.out.println(path);
		System.out.println(FileUtil.getFileOrFilesSize(path,
				CustomConstants.SIZETYPE_KB) + "kb");
		if (FileUtil.getFileOrFilesSize(path, CustomConstants.SIZETYPE_KB) == 0.0) {
			return mDataList;
		}
		if (FileUtil.getFileOrFilesSize(path, CustomConstants.SIZETYPE_KB) > 150) {
			mDataList.add(FileUtil.saveDataPic(path));
		} else {
			ImageItem item = new ImageItem();
			item.sourcePath = path;
			mDataList.add(item);
		}
		return mDataList;
	}

	public void jumpActivity(final Context mContxt, final Class mToContxt,
			String msg, final Map<String, String> map) {
		final Dialog dialog = new Dialog(mContxt, "提示", msg);
		dialog.addCancelButton("取消");
		dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
				Intent intent = new Intent(mContxt, mToContxt);
				if (map != null) {
					for (String key : map.keySet()) {
						System.out.println("key= " + key + " and value= "
								+ map.get(key));
						intent.putExtra(key, map.get(key));
					}
				}
				startActivity(intent);
			}
		});
		dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.show();

	}

	public void showGzDialog(Context mCon,String caretype){
		if (isLogin(mCon)) {
			showToast("请先登录！");
			return;
		}
		if (caretype.equals("1")) {

			final Dialog dialog = new Dialog(mCon, "提示", "确定取消关注该发布者？");
			dialog.addCancelButton("取消");
			dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
					// 取消关注
//					handlerMyCare("del",
//							UrlEntry.DELETE_MYCARE_URL);
				}
			});
			dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
			dialog.show();
		} else {
			final Dialog dialog = new Dialog(mCon, "提示", "确定关注该发布者？");
			dialog.addCancelButton("取消");
			dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
					// 添加关注
//					handlerMyCare("add",
//							UrlEntry.ADD_MYCARE_URL);
				}
			});
			dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
			dialog.show();
		}
	}
	public static void showUpdateDialog(final Context context,final UpdateInfo info) {

		// 发现新版本，提示用户更新
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("发现新版本 "+ info.getVersionName())
				.setMessage("更新内容：\n"+info.getDescription())
				.setPositiveButton("更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int which) {
								Toast.makeText(context,"开始下载...",Toast.LENGTH_SHORT).show();
								// 开启更新服务UpdateService
								// 这里为了把update更好模块化，可以传一些updateService依赖的值
								// 如布局ID，资源ID，动态获取的标题,这里以app_name为例
								Intent updateIntent = new Intent(context,UpdateServcie.class);
								updateIntent.putExtra("appName",
										"猪贸通");
								updateIntent.putExtra("appUrl",
										info.getUrl());
								context.startService(updateIntent);
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int which) {
								dialog.dismiss();
							}
						});
		alert.create().show();
	}

	public static boolean isNeedUpdate(Context context,UpdateInfo info) {

		String v = info.getVersion(); // 最新版本的版本号
		Log.i("update",v);
		if (Integer.parseInt(v)>getVersion(context)) {
			return true;
		} else {
			// 清理工作，
			FileUtil.deleteFile("猪贸通");
			return false;
		}
	}

	// 获取当前版本的版本号
	public static int getVersion(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
