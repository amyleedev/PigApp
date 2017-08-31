package com.szmy.pigapp.pigactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.entity.PigPrice;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PigPriceListActivity extends BaseActivity {

	private MySwipeRefreshLayout mSrlData;
	private ListView mLv;
	private PigPriceAdapter mAdapter;
	private int mPage;
	private static final int PAGE_SIZE = 20;
	private List<PigPrice> mList;
	private View mFooterView;
	private LinearLayout mLlFooterLoading;
	private TextView mTvFooterResult;
	private boolean mIsLoadAll = false;
	private LinearLayout mLlLoading;
	private LinearLayout mLlLoadFailure;
	public static final int LOCATION_ID = 29;
	public LocationService locationService;
	public Vibrator mVibrator;
	public static double x = 0;
	public static double y = 0;
	private final int REFRESH = 27;
	private boolean mIsLoading;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.pigprice_list_activity);
		((TextView)findViewById(R.id.def_head_tv)).setText("我今日的报价");
		((LinearLayout)findViewById(R.id.def_head_back)).setVisibility(View.VISIBLE);
		initView();
	}
	
	private void initView(){
		
		mList = new ArrayList<PigPrice>();

		mFooterView = LayoutInflater.from(PigPriceListActivity.this).inflate(
				R.layout.footer_view, null);
		mLlFooterLoading = (LinearLayout) mFooterView
				.findViewById(R.id.ll_footer_loading);
		mTvFooterResult = (TextView) mFooterView
				.findViewById(R.id.tv_footer_result);
		mSrlData = (MySwipeRefreshLayout) findViewById(R.id.srl_data);

		mSrlData.setOnRefreshListener(mRefreshListener);
		mSrlData.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mLv = (ListView) findViewById(R.id.lv);
		mLv.setOnScrollListener(mOnScrollListener);
		mLv.setOnItemClickListener(mItemClickListener);

		mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
		mLlLoadFailure = (LinearLayout)findViewById(R.id.ll_load_failure);
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
		loadData();
	}

	public void loadData() {
		mIsLoading = true;
		RequestParams params = new RequestParams();
		params.addBodyParameter("pager.offset", String.valueOf(mPage));
		params.addBodyParameter("pager.pageSize", String.valueOf(PAGE_SIZE));
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_MYPIGPRICE_URL, params,
				new RequestCallBack<String>() {
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
						Log.i("pigpricelist", "  responseInfo.result = "
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
					List<PigPrice> childJsonItem = new ArrayList<PigPrice>();
					JSONArray jArrData = jsonresult.getJSONArray("list");
					for (int i = 0; i < jArrData.length(); i++) {
						JSONObject jobj2 = jArrData.optJSONObject(i);
						GsonBuilder gsonb = new GsonBuilder();
						Gson gson = gsonb.create();
						PigPrice mInfoEntry = gson.fromJson(jobj2.toString(),
								PigPrice.class);
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
				List<PigPrice> childJsonItem = new ArrayList<PigPrice>();
				JSONArray jArrData = jsonresult.getJSONArray("list");
				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					PigPrice mInfoEntry = gson.fromJson(jobj2.toString(),
							PigPrice.class);
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

						// mLv.removeHeaderView(mFlChooseHeader);
					}
					mLv.addFooterView(mFooterView);

					// mLv.addHeaderView(mFlChooseHeader);
					mList.addAll(childJsonItem);
					mAdapter = new PigPriceAdapter(PigPriceListActivity.this, mList);
					mLv.setAdapter(mAdapter);
					if (childJsonItem.size() == 0) {
						mLlFooterLoading.setVisibility(View.GONE);
						mTvFooterResult.setVisibility(View.VISIBLE);
						mTvFooterResult.setText("查询结果为空");
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
					mAdapter = new PigPriceAdapter(PigPriceListActivity.this, mList);
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
			case R.id.ll_load_failure:
				load();
				break;

			}
		}
	};
	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int witch,
				long arg3) {
			switch (arg0.getId()) {
			case R.id.lv:
				
				if ((witch) >= mList.size() || (witch) < 0) {
					return;
				}
			
				break;

			}

		}
	};

	private OnScrollListener mOnScrollListener = new OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {			
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
		MobclickAgent.onPageStart("MainFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainFragment");
	}

}
