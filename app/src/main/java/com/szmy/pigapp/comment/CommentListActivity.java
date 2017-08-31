package com.szmy.pigapp.comment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.service.LocationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.ActivityScanResult;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.entity.NewsEntity;
import com.szmy.pigapp.tixian.IntegraltocashActivity;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentListActivity extends BaseActivity {
	private SwipeRefreshLayout mSrlData;
	private ListView mLv;
	private NewsCommentAdapter mAdapter;
	private int mPage;
	private static final int PAGE_SIZE = 20;
	private List<NewsComment> mList;
	private LinearLayout mLlMenu;
	private LinearLayout mLlExchange;
	private FrameLayout mFlHover1;
	// private LinearLayout mLlChoose;
	private FrameLayout mFlHover2;
	private View mFooterView;
	private LinearLayout mLlFooterLoading;
	private TextView mTvFooterResult;
	private boolean mIsLoadAll = false;
	private TextView mTvBolting;
	private LinearLayout mLlLoading;
	private LinearLayout mLlLoadFailure;
	public static final int LOCATION_ID = 29;
	public LocationService locationService;
	public Vibrator mVibrator;
	public static double x = 0;
	public static double y = 0;
	private String city;
	private final int TYPE_ID = 22;
	public static final int REFRESH = 27;
	private boolean mIsLoading;
	private LinearLayout mLlBack;
	private TextView mTvTotlePoint;
	private TextView mTvTodayPoint;
	private TextView mBtnHelp;
	private LinearLayout mLlWthdrawals;
	private String kyPoint = "0";

	private TextView mTvTitle,mTvTime,mTvHit;
	private WebView mWv;
	private WebSettings webSettings;
	private NewsEntity mNews;
	private LinearLayout mLlComment;
	private EditText mEtComment;
	private Button mBtnSend;
	private String newsId ="", cid = "";
	private NewsComment newsComment;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_comment_details);
		((TextView)findViewById(R.id.def_head_tv)).setText("评论回复");
		Intent intent= getIntent();
		newsComment = (NewsComment) getIntent().getSerializableExtra("newsComment");
		initView();
	}

	private void initView() {
		mList = new ArrayList<NewsComment>();
		mLlComment = (LinearLayout) findViewById(R.id.commentLayout);
		mEtComment = (EditText) findViewById(R.id.fb_send_content);
		mLlComment.setVisibility(View.VISIBLE);
		mBtnSend = (Button) findViewById(R.id.fb_send_btn);
		mBtnSend.setOnClickListener(mClickListener);
		mLlMenu = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.comment_header_top_view, null);
		mLlMenu.setClickable(false);
		mTvTitle = (TextView) mLlMenu.findViewById(R.id.tv_news_title);
		mTvTime = (TextView) mLlMenu.findViewById(R.id.tv_news_time);
		mTvTitle.setText(newsComment.getContent());
		mTvTime.setText(newsComment.getCreateTime().substring(5,16));
		mTvHit = (TextView) mLlMenu.findViewById(R.id.textView2);
		mWv = (WebView) mLlMenu.findViewById(R.id.wv);
		mWv.setBackgroundColor(0);
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
		mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
		mLlBack.setOnClickListener(mClickListener);


		mFooterView = LayoutInflater.from(this).inflate(R.layout.footer_view,
				null);
		mLlFooterLoading = (LinearLayout) mFooterView
				.findViewById(R.id.ll_footer_loading);
		mTvFooterResult = (TextView) mFooterView
				.findViewById(R.id.tv_footer_result);
		mSrlData = (SwipeRefreshLayout) findViewById(R.id.srl_data);
		mSrlData.setOnRefreshListener(mRefreshListener);
		mSrlData.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mLv = (ListView) findViewById(R.id.lv);
		mLv.setOnScrollListener(mOnScrollListener);
		mLv.setOnItemClickListener(mItemClickListener);
		mBtnHelp = (TextView) findViewById(R.id.help_btn);
		mBtnHelp.setVisibility(View.GONE);
		mBtnHelp.setOnClickListener(mClickListener);
		mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
		mLlLoadFailure = (LinearLayout) findViewById(R.id.ll_load_failure);
		mLlLoadFailure.setOnClickListener(mClickListener);
	}

	private void load() {
		reload();
		mLlLoading.setVisibility(View.VISIBLE);
		mSrlData.setVisibility(View.GONE);
		mLlLoadFailure.setVisibility(View.GONE);
	}

	private void reload() {
		mPage = 1;
		mIsLoadAll = false;
//		loadPoint();
		loadData();
	}

	public void loadData() {
		mIsLoading = true;
		RequestParams params = new RequestParams();
		params.addBodyParameter("offset", String.valueOf(mPage));
		params.addBodyParameter("pageSize", String.valueOf(PAGE_SIZE));
		params.addBodyParameter("e.newId",newsComment.getNewId());
		params.addBodyParameter("e.cid",newsComment.getId());
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.COMMENT_LIST_URL,
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
						mIsLoading = false;
						Log.i("result", "responseInfo.result = "
								+ responseInfo.result);
						parseData(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						mIsLoading = false;
						if (mLlLoading.getVisibility() == View.VISIBLE) {
							mLlLoading.setVisibility(View.GONE);
							mLlLoadFailure.setVisibility(View.VISIBLE);
						} else {
							mSrlData.setRefreshing(false);
						}
						if (mPage > 1)
							mPage--;
					}
				});
	}

	private void parseData(String result) {
		if (!TextUtils.isEmpty(result)) {
			System.out.println("selected_order_list:" + result);
			try {
				JSONObject jsonresult = new JSONObject(result);
				if (jsonresult.optString("success").equals("1")) {
					List<NewsComment> childJsonItem = new ArrayList<NewsComment>();
					JSONArray jArrData = jsonresult.getJSONArray("list");
					for (int i = 0; i < jArrData.length(); i++) {
						JSONObject jobj2 = jArrData.optJSONObject(i);
						GsonBuilder gsonb = new GsonBuilder();
						Gson gson = gsonb.create();
						NewsComment mInfoEntry = gson.fromJson(jobj2.toString(),
								NewsComment.class);
						childJsonItem.add(mInfoEntry);
					}
				}
			} catch (Exception e) {
			}
		}
		JSONObject jsonresult;
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("success").toString().equals("1")) {
				List<NewsComment> childJsonItem = new ArrayList<NewsComment>();
				JSONArray jArrData = jsonresult.getJSONArray("list");
				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					NewsComment mInfoEntry = gson.fromJson(jobj2.toString(),
							NewsComment.class);
					childJsonItem.add(mInfoEntry);
				}
				if (mLlLoading.getVisibility() == View.VISIBLE) {
					mLlLoading.setVisibility(View.GONE);
					mSrlData.setVisibility(View.VISIBLE);
				} else {
					mSrlData.setRefreshing(false);
				}
				if (mPage == 1) {
					if (mList.size() >= 0) {
						mList.clear();
						mLv.removeFooterView(mFooterView);
						mLv.removeHeaderView(mLlMenu);
					}
					mLv.addFooterView(mFooterView);
					mLv.addHeaderView(mLlMenu, null, false);
					mList.addAll(childJsonItem);
					mAdapter = new NewsCommentAdapter(CommentListActivity.this, mList);
					mLv.setAdapter(mAdapter);
					if (childJsonItem.size() == 0) {
						mLlFooterLoading.setVisibility(View.GONE);
						mTvFooterResult.setVisibility(View.VISIBLE);
						mTvFooterResult.setText("暂无回复 ");
						mIsLoadAll = true;
					} else if (childJsonItem.size() < 20) {
						mLlFooterLoading.setVisibility(View.GONE);
						mTvFooterResult.setVisibility(View.VISIBLE);
						mTvFooterResult.setText("已加载全部");
						mIsLoadAll = true;
					} else {
						mLlFooterLoading.setVisibility(View.VISIBLE);
						mTvFooterResult.setVisibility(View.GONE);
					}
				} else {
					mList.addAll(childJsonItem);
					mAdapter = new NewsCommentAdapter(this, mList);
					mLv.requestLayout();
					mAdapter.notifyDataSetChanged();
					if (childJsonItem.size() < 20) {
						mLlFooterLoading.setVisibility(View.GONE);
						mTvFooterResult.setVisibility(View.VISIBLE);
						mTvFooterResult.setText("已加载全部");
						mIsLoadAll = true;
					} else {
						mLlFooterLoading.setVisibility(View.VISIBLE);
						mTvFooterResult.setVisibility(View.GONE);
					}
				}
			}
		} catch (JSONException e) {
		}
	}

	private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

		@Override
		public void onRefresh() {
			reload();
		}
	};
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ll_exchange:
					Intent intent = new Intent(CommentListActivity.this,
							ActivityScanResult.class);
					intent.putExtra("type", "integral");
					intent.putExtra(
							"url",
							UrlEntry.INTEGRAL_SHOP_URL
									+ (TextUtils
									.isEmpty(getUserUUid(CommentListActivity.this)) ? "1"
									: getUserUUid(CommentListActivity.this)));
					startActivity(intent);
					break;
				case R.id.def_head_back:
					finish();
					break;
				case R.id.ll_load_failure:
					load();
					break;
				case R.id.help_btn:
//				startActivity(new Intent(IntegralActivity.this,
//						ActivityIntegralProtocol.class));
					Intent intent1 = new Intent(CommentListActivity.this,
							ActivityScanResult.class);
					intent1.putExtra("url", UrlEntry.JF_URL);
					startActivity(intent1);
					break;
				case R.id.ll_wthdrawals:
//				 startActivity(new Intent(IntegralActivity.this,
//				 DevelopingActivity.class));
					Intent intentwt = new Intent(CommentListActivity.this,IntegraltocashActivity.class);
					if(mTvTotlePoint.getText().toString().equals("暂无")){
						intentwt.putExtra("point", 0);
					}else{
						intentwt.putExtra("point", Integer.parseInt(mTvTotlePoint.getText().toString()));}
					intentwt.putExtra("kypoint", Integer.parseInt(kyPoint));
					startActivityForResult(intentwt, 0);
					break;
				case R.id.fb_send_btn:
					hideInput();
					if (isLogin(CommentListActivity.this)) {
						showToast("请先登录！");
					} else {
						if(TextUtils.isEmpty(mEtComment.getText().toString())){
							showToast("内容不能为空，请重新输入！");
							return;
						}
						sendComment();
					}
					break;
			}
		}
	};

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

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int witch,
								long arg3) {
		}
	};

	private OnScrollListener mOnScrollListener = new OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
							 int visibleItemCount, int totalItemCount) {
			if (firstVisibleItem > 0) {
			} else {
			}
			if (firstVisibleItem + visibleItemCount == totalItemCount) {
				if (mIsLoading)
					return;
				if (mIsLoadAll)
					return;
				mPage += 1;
				loadData();
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
			case REFRESH:
				load();
				break;
		}
	}

	public void onResume() {
		super.onResume();
//		loadPoint();
		reload();
		MobclickAgent.onPageStart("MainFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainFragment");
	}
	public void sendComment() {
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("e.content", mEtComment.getText().toString());
		params.addBodyParameter("e.type","1");
		params.addBodyParameter("e.newId",newsComment.getNewId());
		params.addBodyParameter("e.cid",newsComment.getId());
		params.addBodyParameter("uuid",App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.ADD_COMMENT_URL,
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
						String result = responseInfo.result;
						Log.i("comment",result);
						try {
							JSONObject json = new JSONObject(result);
							if (json.optString("success").equals("1")) {
								showToast("回复成功");
								mEtComment.setText("");
								reload();
							} else {
								successType(json.optString("success"), "操作失败！");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						disDialog();
					}
				});
	}
}
