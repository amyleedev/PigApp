package com.szmy.pigapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.UserInfo;
import com.szmy.pigapp.service.MyPushIntentService;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppManager;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.MD5;
import com.szmy.pigapp.utils.UrlEntry;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.fragment.FeedbackFragment;
import com.umeng.message.PushAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * ����Act
 * 
 */
public class ActivityLead extends BaseActivity {
	private static String userName;
	private static String password;
	public final static String UPDATE_VERSIONS_URL = "";
	private ImageView mIvFlash;
	String target;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.updateOnlineConfig(this);
		App.appContext = ActivityLead.this;
		getParameterByIntent();
		FeedbackAgent agent = new FeedbackAgent(ActivityLead.this);
		agent.sync();
		PushAgent mPushAgent = PushAgent.getInstance(ActivityLead.this);
		mPushAgent.enable();
		PushAgent.getInstance(ActivityLead.this).onAppStart();
		mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
//		System.out.println("----"
//				+ UmengRegistrar.getRegistrationId(ActivityLead.this));
		setContentView(R.layout.activity_lead);
		mIvFlash = (ImageView) findViewById(R.id.iv_flash);
		OnlineConfigAgent.getInstance().updateOnlineConfig(ActivityLead.this);
		if (AppStaticUtil.isNetwork(getApplicationContext())) {
			String value = OnlineConfigAgent.getInstance().getConfigParams(
					ActivityLead.this, "img");
			if (!TextUtils.isEmpty(value)) {
				ImageLoader.getInstance().displayImage(value, mIvFlash,
						AppStaticUtil.getOptions());
			}
		}
		new Handler().postDelayed(new Runnable() {
			public void run() {
				loginOrSkip();
			}
		}, 3000);
	}
	public void getParameterByIntent() {
		Intent mIntent = this.getIntent();
		if(mIntent.hasExtra("username")) {
//			UserInfo userinfo = (UserInfo) mIntent.getSerializableExtra("userinfo");
			UserInfo userinfo = new UserInfo();
			userinfo.setUuid(mIntent.getStringExtra("userid"));
			userinfo.setFxsType(mIntent.getStringExtra("fxsType"));
			userinfo.setAddress(mIntent.getStringExtra("ADDRESS"));
			userinfo.setArea(mIntent.getStringExtra("AREA"));
			userinfo.setCity(mIntent.getStringExtra("CITY"));
			userinfo.setProvince(mIntent.getStringExtra("PROVINCE"));
			userinfo.setY(mIntent.getStringExtra("GPS_Y"));
			userinfo.setX(mIntent.getStringExtra("GPS_X"));
			userinfo.setAuthentication(mIntent.getStringExtra("authentication"));
			userinfo.setType(mIntent.getStringExtra("usertype"));
			userinfo.setPicture(mIntent.getStringExtra("userphoto"));
			userinfo.setPhone(mIntent.getStringExtra("userphone"));
			userinfo.setUuid(mIntent.getStringExtra("uuid"));
			userinfo.setUsername(mIntent.getStringExtra("username"));
			App.mUserInfo = userinfo;
			FileUtil.saveUser(ActivityLead.this, userinfo);
			App.setDataInfo(userinfo);
			System.out.print("aaaaaaaaaaaaaa"+userinfo.getUsername());
		}

	}
	private void loginOrSkip() {
		if (AppStaticUtil.isLogin(ActivityLead.this)) {
			userName = sp.getString(SETTING_USERNAME, "");
			password = sp.getString(SETTING_PASSWORD, "");
			if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
				showDialog();
				new Thread(runnable).start();
				return;
			}
		}
		skip();
	}

	private void skip() {
		boolean isFirst = sp.getBoolean(SETTING_ISFIRST, true);
		Class mClass = ActivityMain.class;
		if (isFirst)
			mClass = ActivityGuide.class;
		Intent intent = new Intent(ActivityLead.this, mClass);
		intent.putExtra(SETTING_INTEGRAL_POLICY, true);
		startActivity(intent);
		overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
		finish();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ActivityLead"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ActivityLead"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
													// onPageEnd 在onPause
													// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		AppManager.getAppManager().AppExit(getApplicationContext());
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			Message msg = new Message();
			Bundle data = new Bundle();
			MD5 md5 = new MD5();
			HttpUtil http = new HttpUtil();
			String result = http.postDataMethod(UrlEntry.LOGIN_URL, userName,
					md5.GetMD5Code(password), android.os.Build.BRAND + ":"
							+ android.os.Build.MODEL,
					AppStaticUtil.getDeviceId(sp, editor, ActivityLead.this));
			try {
				JSONObject jsonresult = new JSONObject(result);
				if (jsonresult.get("success").toString().equals("1")) {
					result = "登录成功！";
					msg.what = 1;
					Editor edit = sp.edit();
					/**************************************/
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					UserInfo mInfoEntry = gson.fromJson(jsonresult.toString(),
							UserInfo.class);
					App.mUserInfo = mInfoEntry;
					FileUtil.saveUser(ActivityLead.this, mInfoEntry);
					App.setDataInfo(mInfoEntry);
					setUserInfo(mInfoEntry.getUsername());
					/******************************************/
					if (App.usertype.equals("")) {
						msg.what = 3;
					}
				} else {
					result = jsonresult.get("success").toString();
					msg.what = 2;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			data.putString("value", result);
			msg.setData(data);
			handler.sendMessage(msg);
		}
	};

	private void setUserInfo(String username) {
		final FeedbackAgent agent = new FeedbackAgent(ActivityLead.this);
		com.umeng.fb.model.UserInfo info = agent.getUserInfo();
		if (info == null)
			info = new com.umeng.fb.model.UserInfo();
		Map<String, String> contact = info.getContact();
		if (contact == null)
			contact = new HashMap<String, String>();
		contact.put(FeedbackFragment.BUNDLE_KEY_CONVERSATION_ID, username);
		contact.put("plain", username);
		info.setContact(contact);
		info.setAgeGroup(1);
		info.setGender("male");
		agent.setUserInfo(info);
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = agent.updateUserInfo();
			}
		}).start();
	}

	public void sendData(final String type) {
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("e.type", type);
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.UPDATE_USERINFO_URL,
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
						disDialog();
						Log.i("login", " login responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								App.usertype = jsonresult.optString("type");
								editor.putString("TYPE",
										jsonresult.optString("type"));
								editor.commit();

								App.mUserInfo.setType(jsonresult
										.optString("type"));
								FileUtil.saveUser(ActivityLead.this,
										App.mUserInfo);
								App.setDataInfo(App.mUserInfo);
								Message msg = new Message();
								msg.what = 1;
								handler.sendMessage(msg);
							}
						} catch (JSONException e) {
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("连接超时,请稍后再试！");
						disDialog();
					}
				});
	}

	private String roletype = "1";
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			disDialog();
			switch (msg.what) {
			case 1:
				showToast("登录成功！");
				editor.putBoolean(BaseActivity.SETTING_ALIAS, true);
				editor.commit();
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							PushAgent.getInstance(ActivityLead.this).addAlias(App.uuid,
									"SYSTEM");
						} catch (Exception e) {
						}
					}
				}).start();
				skip();
				break;
			case 2:
				// showToast("登录失败,用户名或密码错误！");
				skip();
				break;
			case 3:
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ActivityLead.this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("请完善角色");
				final String[] roles = { "猪场", "屠宰场", "经纪人", "物流公司", "自然人" };
				// 设置一个单项选择下拉框
				builder.setSingleChoiceItems(roles, 0,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								roletype = String.valueOf(which + 1);
							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								sendData(roletype);
							}
						});
				builder.show();
				break;
			}
			super.handleMessage(msg);
		}
	};
}
