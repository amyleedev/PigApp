package com.szmy.pigapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.Dialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.activity.LoginActivity;
import com.szmy.pigapp.image.CustomConstants;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.Constant;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseFragment extends Fragment {
	public Toast mToast = null;
	private LoadingDialog dialog = null;
	public SharedPreferences.Editor editor;
	public SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getActivity().getSharedPreferences(Constant.spName,
				Activity.MODE_PRIVATE);
		editor = sp.edit();
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getActivity().getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getActivity().getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}
	public void  clearMapTempFronPref(){
		SharedPreferences preferences = getActivity().getSharedPreferences(CustomConstants.APPPicLICATION_NAME,
				1);
		SharedPreferences.Editor editor1 = preferences.edit();
		FileUtil.deleteDirfile(FileUtil.SDPATH);
		editor1.clear();
		editor1.putString(CustomConstants.APPPicLICATION_NAME, "");
		editor1.commit();
	}
	//
	public void showLog(String tag, String msg) {
		if (Constant.IS_DEBUG)
			Log.e(tag, msg);
	}

	//
	public void showToast(String msg) {
		if (getActivity() == null) {
			return;
		}
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
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
		dialog = new LoadingDialog(getActivity());
		dialog.show();
	}

	public void disDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public void relogin(Context context) {
		editor.putString("userinfo", "");
		editor.commit();
		App.clearData();
		Intent intent = new Intent(context, LoginActivity.class);
		startActivity(intent);

	}

	public void successType(String successtype, String msg) {
		if (successtype.equals("0")) {
			showToast("用户信息错误,请重新登录！");
			relogin(getActivity());
		} else if (successtype.equals("3")) {
			showToast(msg);
		}
	}

	public void back(View v) {
		// getActivity().onBackPressed();
		getActivity().finish();
		onDestroyView();
	}

	public boolean isLogin(Context context) {
		boolean falg = false;
		SharedPreferences sp = context.getSharedPreferences("USERINFO", 0);
		if (App.mUserInfo == null) {
			App.mUserInfo = FileUtil.readUser(context);
		}
		App.setDataInfo(App.mUserInfo);
		if (TextUtils.isEmpty(App.uuid)) {
			falg = true;
			Intent intent = new Intent(context, LoginActivity.class);
			startActivityForResult(intent, 22);
		} else {
			App.setDataInfo(App.mUserInfo);

		}
		return falg;
	}
	public boolean isLoginSSzmy(Context context) {
		boolean falg = false;
		SharedPreferences sp = context.getSharedPreferences("USERINFO", 0);
		if (App.mUserInfo == null) {
			App.mUserInfo = FileUtil.readUser(context);
		}
		App.setDataInfo(App.mUserInfo);
		if (TextUtils.isEmpty(App.uuid)) {
			falg = true;
			Intent intent = new Intent(context, LoginActivity.class);
			intent.putExtra("zhuce","szmyzc");
			startActivityForResult(intent, 22);
		} else {
			App.setDataInfo(App.mUserInfo);

		}
		return falg;
	}
	public String getUsername(Context context) {
		SharedPreferences sp = context.getSharedPreferences("USERINFO", 0);
		if (App.mUserInfo == null) {
			App.mUserInfo = FileUtil.readUser(context);
		}
		App.setDataInfo(App.mUserInfo);
		if (!TextUtils.isEmpty(App.username)) {
			return App.username;
		}
		return "";
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
	public void showDialog1(Context context,String msg){
//		AlertDialog.Builder builder = new Builder(context);
//		builder.setMessage(msg);
//		builder.setTitle("提示");
//		builder.setPositiveButton("确定",null);
//		builder.create().show();
		final Dialog dialog = new Dialog(context,"提示",msg);
		dialog.addCancelButton("取消");
		dialog.show();
	}
	
	/**
	 * 清空SharedPreferences中的图片信息
	 */
	public void clearTempFromPref() {
		SharedPreferences sppic = getActivity().getSharedPreferences(
				CustomConstants.APPLICATION_NAME, 0);
		SharedPreferences.Editor editor = sppic.edit();
		editor.clear();
		editor.commit();
		FileUtil.deleteDirfile(FileUtil.SDPATH);
        editor.putString(CustomConstants.PREF_TEMP_IMAGES, "");
        editor.putString(CustomConstants.PREF_TEMP_IMAGES1, "");
        editor.putString(CustomConstants.PREF_TEMP_IMAGES2, "");
        editor.putString(CustomConstants.PREF_TEMP_IMAGES3, "");
        editor.putString(CustomConstants.PREF_TEMP_IMAGES4, "");
        editor.putString(CustomConstants.PREF_TEMP_IMAGES5, "");
        editor.commit();
	
	}
	
	public void removeTempFromPref(String temp_name){
		SharedPreferences sp = getActivity().getSharedPreferences(
				CustomConstants.APPLICATION_NAME, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(temp_name);
		editor.commit();
	}
	public void dialog(String message, final String phone) {
		final Dialog dialog = new Dialog(getActivity(), "提示", message);
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
	public void getTopLoginText(Context context,TextView view,String type){
		if (TextUtils.isEmpty(getUserUUid(context))) {
			view.setText("登录");
			return;
		} else {
			if (type.equals("myapp")){
				view.setText("猪贸通");
			}else
			view.setText("欢迎");
		}
	}
	/**
	 * 扫码登录
	 *
	 * @param newuuid
	 */
	public  void sendNewUUid(final String newuuid) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("uuid2", newuuid);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.SCAN_LOGIN_URL,
				params, new RequestCallBack<String>() {

					@Override
					public void onStart() {
					}

					@Override
					public void onLoading(long total, long current,
										  boolean isUploading) {
						// resultText.setText(current + "/" + total);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Log.i("uuid", " uuid  responseInfo.result = "
								+ responseInfo.result);
						try {
							JSONObject jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult != null) {
								if (jsonresult.optString("success").equals("1")) {
									showToast("扫码登录成功！");

									editor.putString("uuid",
											newuuid);
									editor.commit();
									App.mUserInfo.setUuid(newuuid);
									FileUtil.saveUser(getActivity(), App.mUserInfo);
									App.setDataInfo(App.mUserInfo);
								} else {
									showToast("扫码登录失败，请重试!");
								}

							}


						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("aaaa", " head  msg = " + msg);
					}
				});
	}

}
