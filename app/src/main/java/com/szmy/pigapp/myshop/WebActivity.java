package com.szmy.pigapp.myshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szmy.pigapp.R;

import org.apache.cordova.CordovaChromeClient;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.PluginEntry;
import org.apache.cordova.Whitelist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebActivity extends Activity implements CordovaInterface {
	private final ExecutorService threadPool = Executors.newCachedThreadPool();
	// The webview for our app
	protected CordovaWebView appView;
	// Plugin to call when activity result is received
	protected int activityResultRequestCode;
	protected CordovaPlugin activityResultCallback;
	protected CordovaPreferences prefs = new CordovaPreferences();
	protected Whitelist internalWhitelist = new Whitelist();
	protected Whitelist externalWhitelist = new Whitelist();
	protected ArrayList<PluginEntry> pluginEntries;
	private RelativeLayout ll;
	private RelativeLayout mLlLoadFailure;
	private ImageView iv;
	private boolean mIsErrorPage;
	private WebSettings webSettings;
	private TextView mTvLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.activity_webview);
		mTvLogin = (TextView) findViewById(R.id.tv_login);
		appView = (CordovaWebView) findViewById(R.id.tutorialView);
		internalWhitelist.addWhiteListEntry("*", false);
		externalWhitelist.addWhiteListEntry("tel:*", false);
		externalWhitelist.addWhiteListEntry("sms:*", false);
		prefs.set("loglevel", "DEBUG");
		appView.init(this, makeWebViewClient(appView),
				makeChromeClient(appView), pluginEntries, internalWhitelist,
				externalWhitelist, prefs);
		appView.loadUrlIntoView("http://www.my360.cn");
		CordovaInterface cordovaInterface = (CordovaInterface) WebActivity.this;
		CordovaWebViewClient cordovaWebViewClient = new CordovaWebViewClient(
				cordovaInterface, appView) {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				System.out.println("onPageStarted " + url);
				super.onPageStarted(view, url, favicon);
				if (ll.getVisibility() == View.GONE) {
					ll.setVisibility(View.VISIBLE);
					// anim.start();
				}
				if (mLlLoadFailure.getVisibility() == View.VISIBLE)
					mLlLoadFailure.setVisibility(View.GONE);
				// progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				System.out.println("onPageFinished " + url);
				super.onPageFinished(view, url);
				// progressBar.setVisibility(View.GONE);
				if (!appView.getSettings().getLoadsImagesAutomatically()) {
					appView.getSettings().setLoadsImagesAutomatically(true);
				}
				if (ll.getVisibility() == view.VISIBLE) {
					ll.setVisibility(View.GONE);
					// anim.stop();
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				System.out.println("onReceivedError " + failingUrl);
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(WebActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
				if (mLlLoadFailure.getVisibility() == View.GONE)
					mLlLoadFailure.setVisibility(View.VISIBLE);
				if (ll.getVisibility() == view.VISIBLE)
					ll.setVisibility(View.GONE);
				// progressBar.setVisibility(View.GONE);
				// errorView.setVisibility(View.VISIBLE);
			}
		};
		appView.setWebViewClient(cordovaWebViewClient);
		ll = (RelativeLayout) findViewById(R.id.ll_loading);
		iv = (ImageView) findViewById(R.id.iv_load_failure);
		mLlLoadFailure = (RelativeLayout) findViewById(R.id.ll_load_failure);
		iv.setOnClickListener(mClickListener);
		if (check()) {
			return;
		}
	}

	private View.OnClickListener mClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_load_failure:
				appView.reload();
				break;
			case R.id.tv_login:
//				Intent intent = new Intent(WebActivity.this,
//						TestActivity.class);
//				startActivity(intent);
				break;
			}
		}
	};

	/**
	 * 再按一次退出系統
	 * */
	private long exitTime = 0;

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (appView.canGoBack()) {
				appView.goBack();
				return true;
			}
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean check() {
		PackageManager pm = getPackageManager();
		List<PackageInfo> infoList = pm
				.getInstalledPackages(PackageManager.GET_SERVICES);
		for (PackageInfo info : infoList) {
			if ("com.adobe.flashplayer".equals(info.packageName)) {
				return true;
			}
		}
		return false;
	}

	protected CordovaWebViewClient makeWebViewClient(CordovaWebView webView) {
		return webView.makeWebViewClient(this);
	}

	protected CordovaChromeClient makeChromeClient(CordovaWebView webView) {
		return webView.makeWebChromeClient(this);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public ExecutorService getThreadPool() {
		return threadPool;
	}

	@Override
	public Object onMessage(String id, Object data) {
		if ("exit".equals(id)) {
			super.finish();
		}
		return null;
	}

	@Override
	public void setActivityResultCallback(CordovaPlugin plugin) {
		if (activityResultCallback != null) {
			activityResultCallback.onActivityResult(activityResultRequestCode,
					Activity.RESULT_CANCELED, null);
		}
		this.activityResultCallback = plugin;
	}

	@Override
	public void startActivityForResult(CordovaPlugin command, Intent intent,
			int requestCode) {
		setActivityResultCallback(command);
		try {
			startActivityForResult(intent, requestCode);
		} catch (RuntimeException e) {
			activityResultCallback = null;
			throw e;
		}
	}

	public boolean copyApkFromAssets(Context context, String fileName,
			String path) {
		boolean copyIsFinish = false;
		try {
			InputStream is = context.getAssets().open(fileName);
			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			copyIsFinish = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return copyIsFinish;
	}
}
