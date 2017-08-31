package com.szmy.pigapp.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.NewsEntity;
import com.szmy.pigapp.share.UMShareNewsPopupWindow;
import com.szmy.pigapp.utils.UrlEntry;
import com.umeng.socialize.bean.SHARE_MEDIA;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsDetailsActivity extends BaseActivity {
	private TextView mTvTitle;
	private TextView mTvNewsTitle;
	private TextView mTvNewsTime;
	private WebView mWv;
	private WebSettings webSettings;
	private RelativeLayout mRlLoading;
	private RelativeLayout mRlLoadFailure;
	private NewsEntity mNews;
	private TextView mTvShare;
	private UMShareNewsPopupWindow mUmPopup;
	private String url = "";
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_news_details);
		initView();
	}

	private void initView() {

		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mTvShare = (TextView) findViewById(R.id.tv_map);
			url = getIntent().getStringExtra("url");
		if (url == null||url.equals("")){
			mTvShare.setVisibility(View.GONE);
		}else
			mTvShare.setVisibility(View.VISIBLE);
			mTvShare.setOnClickListener(mClickListener);
		mTvNewsTitle = (TextView) findViewById(R.id.tv_news_title);
		mTvNewsTime = (TextView) findViewById(R.id.tv_news_time);
		mWv = (WebView) findViewById(R.id.wv);
		mWv.setBackgroundColor(0);
		mRlLoading = (RelativeLayout) findViewById(R.id.rl_loading);
		mRlLoadFailure = (RelativeLayout) findViewById(R.id.rl_load_failure);
		mRlLoadFailure.setOnClickListener(mClickListener);
		mNews = (NewsEntity) getIntent().getSerializableExtra("news");
		System.out.print("urlurlurl:"+url);
		mUmPopup = new UMShareNewsPopupWindow(NewsDetailsActivity.this,UrlEntry.ip+"/mNews/newsInfo.html?id="+mNews.getId(),mNews.getTitle());
		loadData();
		webSettings = mWv.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setDomStorageEnabled(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		//允许混合内容 解决部分手机 加载不出https请求里面的http下的图片
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
	}
	public  String getNewContent(String htmltext){

		Document doc= Jsoup.parse(htmltext);
		Elements elements=doc.getElementsByTag("img");
		for (Element element : elements) {
			element.attr("width","100%").attr("height","auto");
		}

		return doc.toString();
	}
	private void loadData() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("e.id", mNews.getId());
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,
				UrlEntry.GET_NEWS_DETAILS_BY_ID_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						if (mRlLoading.getVisibility() == View.GONE)
							mRlLoading.setVisibility(View.VISIBLE);
						if (mRlLoadFailure.getVisibility() == View.VISIBLE)
							mRlLoadFailure.setVisibility(View.GONE);
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						parseData(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						if (mRlLoading.getVisibility() == View.VISIBLE)
							mRlLoading.setVisibility(View.GONE);
						if (mRlLoadFailure.getVisibility() == View.GONE)
							mRlLoadFailure.setVisibility(View.VISIBLE);
					}
				});
	}

	private void parseData(String result) {
		JSONObject jsonresult;
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("success").toString().equals("1")) {
				GsonBuilder gsonb = new GsonBuilder();
				Gson gson = gsonb.create();
				mNews = gson.fromJson(jsonresult.toString(), NewsEntity.class);
				mTvNewsTitle.setText(mNews.getTitle());
				if (mNews.getCreatetime().length() >= 10)
					mTvNewsTime.setText(mNews.getCreatetime().substring(0, 10));
				else
					mTvNewsTime.setText(mNews.getCreatetime());
				mWv.loadDataWithBaseURL(null,getNewContent(mNews.getContent()), "text/html",
						"utf-8", null);
				mWv.requestFocusFromTouch();
				if (mRlLoading.getVisibility() == View.VISIBLE)
					mRlLoading.setVisibility(View.GONE);
			} else {
				if (mRlLoading.getVisibility() == View.VISIBLE)
					mRlLoading.setVisibility(View.GONE);
				if (mRlLoadFailure.getVisibility() == View.GONE)
					mRlLoadFailure.setVisibility(View.VISIBLE);
				successType(jsonresult.get("success").toString(), "查询失败！");
			}
		} catch (JSONException e) {
		}
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.tv_map:
					mUmPopup.mController.getConfig().removePlatform(
							SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,
							SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL, SHARE_MEDIA.SINA,
							SHARE_MEDIA.TENCENT);
					// //默认分享方式
					mUmPopup.mController.openShare(NewsDetailsActivity.this, false);
					break;
				default:
					loadData();
					break;
			}

		}
	};
}
