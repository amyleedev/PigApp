package com.szmy.pigapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.szmy.pigapp.R;
import com.szmy.pigapp.updateservice.UpdateInfo;
import com.szmy.pigapp.updateservice.UpdateInfoService;
import com.szmy.pigapp.utils.UrlEntry;
import com.umeng.update.UmengUpdateAgent;

public class SetActivity extends BaseActivity {
	private LinearLayout mLlBack;
	private RelativeLayout mRlDisclaimer;
	private RelativeLayout mRlAbout;
	private TextView mTvVersion;
	private RelativeLayout mRlCheckVersion;
	private ToggleButton mToggleBtn;
	private ToggleButton mTbOnlyWifi;
	private RelativeLayout mRlTickling;
	private UpdateInfo info;
	// private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		sp = getSharedPreferences("showonmap", MODE_PRIVATE);
		initView();
	}

	private void initView() {
		mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
		mLlBack.setOnClickListener(mClickListener);
		mRlDisclaimer = (RelativeLayout) findViewById(R.id.rl_disclaimer);
		mRlDisclaimer.setOnClickListener(mClickListener);
		mRlAbout = (RelativeLayout) findViewById(R.id.rl_about);
		mRlAbout.setOnClickListener(mClickListener);
		mRlTickling = (RelativeLayout) findViewById(R.id.rl_tickling);
		mRlTickling.setOnClickListener(mClickListener);
		mTvVersion = (TextView) findViewById(R.id.tv_version);
		mTvVersion.setText(getVersion());
		mRlCheckVersion = (RelativeLayout) findViewById(R.id.rl_check_version);
		mRlCheckVersion.setOnClickListener(mClickListener);
		mToggleBtn = (ToggleButton) findViewById(R.id.onoffBtn);
		mTbOnlyWifi = (ToggleButton) findViewById(R.id.tb_only_wifi);
		if (TextUtils.isEmpty(sp.getString("isShow", ""))) {
			mToggleBtn.setChecked(true);
		} else {
			if (sp.getString("isShow", "").equals("y")) {
				mToggleBtn.setChecked(true);
			} else {
				mToggleBtn.setChecked(false);
			}
		}
		mTbOnlyWifi.setChecked(sp.getBoolean(SETTING_ONLY_WIFI, false));
		mToggleBtn.setOnCheckedChangeListener(mCheckedChangeListener);
		mTbOnlyWifi.setOnCheckedChangeListener(mCheckedChangeListener);
	}

	private OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			Editor edit = sp.edit();
			switch (buttonView.getId()) {
			case R.id.onoffBtn:
				if (isChecked) {
					// 选中
					edit.putString("isShow", "y");
				} else {
					// 未选中
					edit.putString("isShow", "n");
				}
				break;
			case R.id.tb_only_wifi:
				if (isChecked) {
					// 选中
					edit.putBoolean(SETTING_ONLY_WIFI, true);
				} else {
					// 未选中
					edit.putBoolean(SETTING_ONLY_WIFI, false);
				}
				UmengUpdateAgent.setUpdateOnlyWifi(isChecked);
				break;
			}
			edit.commit();
		}
	};

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.def_head_back:
				finish();
				break;
			case R.id.rl_disclaimer:
//				startActivity(new Intent(SetActivity.this,
//						ActivityDisclaimer.class));
				Intent intent = new Intent(SetActivity.this,
						ActivityScanResult.class);
				intent.putExtra("url", UrlEntry.MZSM_URL);
				startActivity(intent);
				break;
			case R.id.rl_about:
//				startActivity(new Intent(SetActivity.this, ActivityAbout.class));
				Intent intent1 = new Intent(SetActivity.this,
						ActivityScanResult.class);
				intent1.putExtra("url", UrlEntry.ABOUT_URL);
				startActivity(intent1);
				break;
			case R.id.rl_tickling:
				// FeedbackAgent agent = new FeedbackAgent(SetActivity.this);
				// agent.startFeedbackActivity();
				if (isLogin(SetActivity.this)) {
					showToast("请先登录！");
				} else {
					startActivity(new Intent(SetActivity.this,
							ActivityTickling.class));
				}
				break;
			case R.id.rl_check_version:
				showDialog();
				// 自动检查有没有新版本 如果有新版本就提示更新
				new Thread() {
					public void run() {
						try {
							UpdateInfoService updateInfoService = new UpdateInfoService(
									SetActivity.this);
							info = updateInfoService.getUpDateInfo();
							handler1.sendEmptyMessage(0);
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.start();
//				UmengUpdateAgent.setUpdateAutoPopup(false);
//				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
//					@Override
//					public void onUpdateReturned(int updateStatus,
//							UpdateResponse updateInfo) {
//						disDialog();
//						switch (updateStatus) {
//						case UpdateStatus.Yes: // has update
//							UmengUpdateAgent.showUpdateDialog(SetActivity.this,
//									updateInfo);
//							break;
//						case UpdateStatus.No: // has no update
//							showToast("已是最新");
//							break;
//						case UpdateStatus.NoneWifi: // none wifi
//							showToast("没有wifi连接， 只在wifi下更新");
//							break;
//						case UpdateStatus.Timeout: // time out
//							showToast("超时");
//							break;
//						}
//					}
//				});
//				UmengUpdateAgent.forceUpdate(SetActivity.this);
				break;
			}
		}
	};

	private String getVersion() {
		String versionName = "";
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(),
					0).versionName;
		} catch (NameNotFoundException e2) {
			versionName = "";
		}
		return versionName;
	}
	@SuppressLint("HandlerLeak")
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			disDialog();
			if(info==null){
				showToast("当前已是最新版本。");
				return;
			}

			// 如果有更新就提示
			if (isNeedUpdate(SetActivity.this,info)) {   //在下面的代码段
				showUpdateDialog(SetActivity.this,info);  //下面的代码段
			}else{
				showToast("当前已是最新版本。");
			}
		};
	};
}
