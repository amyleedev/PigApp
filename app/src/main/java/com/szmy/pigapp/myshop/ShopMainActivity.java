package com.szmy.pigapp.myshop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.ActivityMain;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.myshop.fragment.ShopMoreFragment;
import com.szmy.pigapp.myshop.fragment.WebMainFragment;
import com.szmy.pigapp.updateservice.UpdateInfo;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.apache.cordova.CordovaWebView;

import java.util.HashMap;
import java.util.Map;


/**
 * @version 1.0
 * @since 1.0
 */
@SuppressLint("NewApi")
public class ShopMainActivity extends BaseActivity {
	public SharedPreferences app_config;
	FragmentManager fm;
	FragmentTransaction ft;
	/**
	 * FragmentTabhost
	 */
	private FragmentTabHost mTabHost;
	/**
	 * 布局填充器
	 * 
	 */
	private LayoutInflater mLayoutInflater;
	/**
	 * Fragment数组界面
	 * 
	 */
	private Class mFragmentArray[] = { WebMainFragment.class, WebMainFragment.class,
			WebMainFragment.class, ShopMoreFragment.class };
	/**
	 * 存放图片数组
	 * 
	 */
	private int mImageArray[] = { R.drawable.tab_home,
			R.drawable.tab_preferred, R.drawable.tab_cart, R.drawable.tab_mine };
	/**
	 * 选修卡文字
	 * 
	 */
	private String mTextArray[] = { "首页", "牧易优选", "购物车", "我的" };
	public static Map<String, String> maps = new HashMap<String, String>();
	private String tag = "牧易优选";
	private Fragment fg;
	private String target;
	private UpdateInfo info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_main);
		// 自动检查有没有新版本 如果有新版本就提示更新
		fm = getSupportFragmentManager();
		mLayoutInflater = LayoutInflater.from(this);
		// 找到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		// 得到fragment的个数
		int count = mFragmentArray.length;
		for (int i = 0; i < count; i++) {
			// 给每个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i]).setIndicator(
					getTabItemView(i));
			Bundle arguments = new Bundle();
			if (i==0) {
				arguments.putString("URL", UrlEntry.MOBILE_PHONE_URL+ (TextUtils
						.isEmpty(getUserUUid(this)) ? "1"
						: getUserUUid(this))+"&uuid_szmy="+(TextUtils
						.isEmpty(getUserUUid(this)) ? "1"
						: getUserUUid(this)));
				System.out.print("aaaaaaaa"+UrlEntry.MOBILE_PHONE_URL+ (TextUtils
						.isEmpty(getUserUUid(this)) ? "1"
						: getUserUUid(this))+"&uuid_szmy="+(TextUtils
						.isEmpty(getUserUUid(this)) ? "1"
						: getUserUUid(this))+"aaaaaaa");
				arguments.putString("type","sy");
			}else if(i==1){
				arguments.putString("URL", UrlEntry.YOUXUAN_SZMY_URL+ (TextUtils
						.isEmpty(getUserUUid(this)) ? "1"
						: getUserUUid(this))+"&uuid_szmy="+(TextUtils
						.isEmpty(getUserUUid(this)) ? "1"
						: getUserUUid(this)));
				arguments.putString("type","yx");
			}else if(i==2) {
				arguments.putString("URL", UrlEntry.GOUWUCHE_SZMY_URL+ (TextUtils
						.isEmpty(getUserUUid(this)) ? "1"
						: getUserUUid(this))+"&uuid_szmy="+(TextUtils
						.isEmpty(getUserUUid(this)) ? "1"
						: getUserUUid(this)));
				arguments.putString("type","gwc");
			}
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, mFragmentArray[i], arguments);
		}
		mTabHost.setCurrentTab(1);
		fg = fm.findFragmentByTag("牧易优选");
		mTabHost.getTabWidget()
				.requestFocus(View.FOCUS_FORWARD);
		mTabHost.getTabWidget().getChildTabViewAt(0)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						fg = fm.findFragmentByTag("牧易优选");
						if(fg==null){
							mTabHost.setCurrentTab(0);
							mTabHost.getTabWidget().requestFocus(View.FOCUS_FORWARD);
							return;
						}
						final CordovaWebView appView = ((WebMainFragment) fg).getWebView();
						if (appView.canGoBack()) {
							appView.loadUrlIntoView(UrlEntry.MOBILE_PHONE_URL+ (TextUtils
									.isEmpty(getUserUUid(ShopMainActivity.this)) ? "1"
									: getUserUUid(ShopMainActivity.this))+"&uuid_szmy="+(TextUtils
									.isEmpty(getUserUUid(ShopMainActivity.this)) ? "1"
									: getUserUUid(ShopMainActivity.this)));
							appView.postDelayed(new Runnable() {
								@Override
								public void run() {
									appView.clearHistory();
								}
							}, 1000);
						}
						// 如果已经登录，执行默认点击操作
						// 由于已经覆写了点击方法，所以需要实现tab切换
						mTabHost.setCurrentTab(0);
						mTabHost.getTabWidget()
								.requestFocus(View.FOCUS_FORWARD);

					}
				});
		mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

	}
	TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {

		@Override
		public void onTabChanged(String tabId) {
			tag = tabId;
		}
	};
	/**
	 * 
	 * 给每个Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageArray[index]);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextArray[index]);
		return view;
	}

	/**
	 * 再按一次退出系統
	 */
	private long exitTime = 0;
	private boolean isGoBack = false;

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (tag.equals("首页") || tag.equals("牧易优选") || tag.equals("购物车")) {
				fg = fm.findFragmentByTag(tag);
				isGoBack = ((WebMainFragment) fg).onKeyDown(keyCode, event);
			}
			if (isGoBack)
				return true;
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次返回猪贸通",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
//				System.exit(0);
				Intent intent = new Intent(ShopMainActivity.this, ActivityMain.class);
				startActivity(intent);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
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

	@SuppressLint("HandlerLeak")
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			if(info == null){
				return;
			}
			// 如果有更新就提示
			if (isNeedUpdate(ShopMainActivity.this,info)) {   //在下面的代码段
				showUpdateDialog(ShopMainActivity.this,info);  //下面的代码段
			}
		};
	};
}
