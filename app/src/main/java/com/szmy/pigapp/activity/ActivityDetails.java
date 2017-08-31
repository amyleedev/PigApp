package com.szmy.pigapp.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.share.UMShareNewsPopupWindow;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class ActivityDetails extends BaseActivity {
	private TextView mTvTitle;
	private LinearLayout mIbBack;
	private WebView wv;
	private ProgressBar pb;
//	private View mErrorView;
	private ImageView iv;
	private RelativeLayout ll;
	private RelativeLayout mLlLoadFailure;
//	private boolean mIsErrorPage;
//	private WebSettings webSettings;
	private String url = "";
	private TextView mTvShare;
	private UMShareNewsPopupWindow mUmPopup;
	private String content;
	// private AnimationDrawable anim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		mTvTitle = ((TextView) findViewById(R.id.def_head_tv));
		url = getIntent().getStringExtra("url");
		content = getIntent().getStringExtra("content");

		mUmPopup = new UMShareNewsPopupWindow(ActivityDetails.this,url,content);
		mTvShare = (TextView) findViewById(R.id.tv_map);
		mTvShare.setText("分享");
		mTvShare.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(!url.equals("")){
					mUmPopup.mController.getConfig().removePlatform(
							SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,
							SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL, SHARE_MEDIA.SINA,
							SHARE_MEDIA.TENCENT);
					// //默认分享方式
					mUmPopup.mController.openShare(ActivityDetails.this, false);
				}
			}
		});
		String type = getIntent().getStringExtra("type");
		if(type.equals("news")){
			mTvShare.setVisibility(View.VISIBLE);
		}
		if (type.equals("product")) {
			mTvTitle.setText("商品介绍");
		}else if(type.equals("backstage")){
			mTvTitle.setText("后台管理");
		} else {
			mTvTitle.setText("资讯详情");
		} 
		mIbBack = (LinearLayout) findViewById(R.id.def_head_back);
		mIbBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		wv = (WebView) findViewById(R.id.wv);
		pb = (ProgressBar) findViewById(R.id.pb);
		// anim = (AnimationDrawable) pb.getBackground();
		ll = (RelativeLayout) findViewById(R.id.ll_loading);
		iv = (ImageView) findViewById(R.id.iv_load_failure);
		mLlLoadFailure = (RelativeLayout) findViewById(R.id.ll_load_failure);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				wv.reload();
				// if (wv.canGoBack())
				// wv.goBack();
			}
		});
		wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (ll.getVisibility() == View.GONE) {
					ll.setVisibility(View.VISIBLE);
					// anim.start();
				}
				if (mLlLoadFailure.getVisibility() == View.VISIBLE)
					mLlLoadFailure.setVisibility(View.GONE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (!wv.getSettings().getLoadsImagesAutomatically()) {
					wv.getSettings().setLoadsImagesAutomatically(true);
				}
				if (ll.getVisibility() == view.VISIBLE) {
					ll.setVisibility(View.GONE);
					// anim.stop();
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				showToast("加载失败");
				if (mLlLoadFailure.getVisibility() == View.GONE)
					mLlLoadFailure.setVisibility(View.VISIBLE);
				if (ll.getVisibility() == view.VISIBLE)
					ll.setVisibility(View.GONE);
				// wv.loadDataWithBaseURL(null, " ", "text/html", "utf-8",
				// null);
			}
		});
		// http://3g.qq.com http://www.mn6y.com/web
		// http://app.eshang168.com/index.php
		// wv.loadUrl("http://www.ymxfjy.com/wap/");
		// ���ÿ���֧������
		wv.getSettings().setSupportZoom(true);
		//wv.setInitialScale(100);
		// ���ó������Ź���
		wv.getSettings().setBuiltInZoomControls(true);
		// ������������
		wv.getSettings().setUseWideViewPort(true);
		// ����Ӧ��Ļ
		wv.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.loadUrl(url);
		wv.requestFocusFromTouch();
		// webSettings = wv.getSettings();
		// webSettings.setJavaScriptEnabled(true);
		// webSettings.setDomStorageEnabled(true);
		// webSettings.setDatabaseEnabled(true);
		// webSettings.setAppCacheEnabled(true);
		// webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
		// webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
