package com.szmy.pigapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.adapter.SlaughterHouseAdapter2;
import com.szmy.pigapp.entity.SlaughterhouseEntry;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class SlaughterListActivity2 extends BaseActivity  {

	private LayoutInflater mInflater;
	private List<SlaughterhouseEntry> childJson = new ArrayList<SlaughterhouseEntry>(); // 子列表
	private SlaughterHouseAdapter2 infoAdapter;
	private BitmapUtils bitmapUtils;
	private ImageButton add_btn;
	private String page = "1";
	private String pageSize = "10" ;
	private final int TYPE_REFRESH=1;
	private final int TYPE_LOAD = 2;
	private static final int PAGE_SIZE = 20;
	private int mPage;
	private Context mContext;
	private MySwipeRefreshLayout mSrlData;
	private GridViewWithHeaderAndFooter mLv;
	private View mFooterView;
	private LinearLayout mLlFooterLoading;
	private TextView mTvFooterResult;
	private boolean mIsLoadAll = false;
	private LinearLayout mLlLoading;
	private LinearLayout mLlLoadFailure;
	private boolean mIsLoading;
	private double x,y, x2, y2;;
	private String URL;
	private String provinceContent, cityContent,areaContent;
	private TextView mTvMap;
	private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

		@Override
		public void onRefresh() {
			reload();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slaughter_list);
		((TextView) findViewById(R.id.def_head_tv)).setText("屠宰场");
		add_btn = (ImageButton) findViewById(R.id.add_btn);
		add_btn.setVisibility(View.GONE);
		mTvMap = (TextView) findViewById(R.id.tv_map);
		mTvMap.setVisibility(View.VISIBLE);
		Intent intent = getIntent();
		x2 = intent.getDoubleExtra("x", 0);
		y2 = intent.getDoubleExtra("y", 0);
		provinceContent = intent.getStringExtra("province");
		cityContent = intent.getStringExtra("city");
		mTvMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SlaughterListActivity2.this,
						SlaughterMapActivity.class);
				intent.putExtra("x", x2);
				intent.putExtra("province", provinceContent);
				intent.putExtra("city", cityContent);
				intent.putExtra("area", areaContent);
				intent.putExtra("y", y2);
				startActivity(intent);
			}
		});
		initView();
		loadData();
	}

	public void initView() {
		Intent intent=getIntent();
		x=intent.getDoubleExtra("x", 0);
		y=intent.getDoubleExtra("y", 0);
		provinceContent=intent.getStringExtra("province");
		cityContent=intent.getStringExtra("city");
		areaContent=intent.getStringExtra("area");
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		infoAdapter = new SlaughterHouseAdapter2(SlaughterListActivity2.this,childJson);
		mContext=SlaughterListActivity2.this;

		mFooterView = LayoutInflater.from(mContext).inflate(
				R.layout.footer_view, null);
		mLlFooterLoading = (LinearLayout) mFooterView
				.findViewById(R.id.ll_footer_loading);
		mTvFooterResult = (TextView) mFooterView
				.findViewById(R.id.tv_footer_result);
		mSrlData = (MySwipeRefreshLayout) findViewById(R.id.srl_vehicle);

		mSrlData.setOnRefreshListener(mRefreshListener);
		mSrlData.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mLv = (GridViewWithHeaderAndFooter) findViewById(R.id.info_framgment_xlistview);
		mLv.setOnScrollListener(mOnScrollListener);
		mLv.setOnItemClickListener(mItemClickListener);
//		mLv.addFooterView(mFooterView);
		mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
		mLlLoadFailure = (LinearLayout)findViewById(R.id.ll_load_failure);
		mLlLoadFailure.setOnClickListener(mClickListener);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loadData();
	}

	private void loadData() {
		mIsLoading = true;
		RequestParams params = new RequestParams();
//		if (x!=0&&y!=0) {
//			URL=UrlEntry.QUERY_NEAR_SLAUGHTER_URL;
//			params.addBodyParameter("e.x", String.valueOf(x));
//			params.addBodyParameter("e.y", String.valueOf(y));
//		} else {
			URL=UrlEntry.QUERY_SLAUGHTER_LIST_URL;
//		}
//		params.addBodyParameter("uuid", App.uuid);
//		params.addBodyParameter("e.userID", App.userID);
		params.addBodyParameter("e.province", provinceContent);
		params.addBodyParameter("e.city", cityContent);
		params.addBodyParameter("e.area", areaContent);
		 params.addBodyParameter("pager.offset", String.valueOf(mPage));
		 params.addBodyParameter("e.pageSize", String.valueOf(PAGE_SIZE));
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,URL,
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
						getData(responseInfo.result);
						Log.i("insert", " insert responseInfo.result = "
								+ responseInfo.result);
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

	private void getData(String result) {
		JSONObject jsonresult;
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("success").toString().equals("1")) {
				// Toast.makeText(mContext, "查询成功！", Toast.LENGTH_SHORT).show();

				List<SlaughterhouseEntry> childJsonItem = new ArrayList<SlaughterhouseEntry>();
				JSONArray jArrData = jsonresult.getJSONArray("list");

				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					SlaughterhouseEntry mInfoEntry = gson.fromJson(jobj2.toString(),
							SlaughterhouseEntry.class);
					childJsonItem.add(mInfoEntry);
				}
				
				if (mLlLoading.getVisibility() == View.VISIBLE) {
					mLlLoading.setVisibility(View.GONE);
					mSrlData.setVisibility(View.VISIBLE);
				} else {
					mSrlData.setRefreshing(false);
				}
				
				if (mPage == 1) {
					if (childJson.size() >= 0) {
						childJson.clear();
//						mLv.removeFooterView(mFooterView);

					}

//					mLv.addFooterView(mFooterView);
					mFooterView.setVisibility(View.VISIBLE);

					childJson.addAll(childJsonItem);
					infoAdapter = new SlaughterHouseAdapter2(SlaughterListActivity2.this,childJson);
//					mLv.setAdapter(infoAdapter);
					if (childJsonItem.size() == 0) {
						mLlFooterLoading.setVisibility(View.GONE);
						mTvFooterResult.setVisibility(View.VISIBLE);

						mTvFooterResult.setText("查询结果为空");
						mIsLoadAll = true;
					} else if (childJsonItem.size() < 20) {
//
						mLlFooterLoading.setVisibility(View.GONE);
						mTvFooterResult.setVisibility(View.VISIBLE);
						mTvFooterResult.setText("已加载全部");
						mIsLoadAll = true;
					} else {
						mLlFooterLoading.setVisibility(View.VISIBLE);
						mTvFooterResult.setVisibility(View.GONE);
					}
					mLv.setAdapter(infoAdapter);
				} else {
					childJson.addAll(childJsonItem);
					infoAdapter = new SlaughterHouseAdapter2(SlaughterListActivity2.this,childJson);

					mLv.requestLayout();
					infoAdapter.notifyDataSetChanged();
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
				
				infoAdapter.notifyDataSetChanged();

			} else {
				mIsLoading = false;
				if (mLlLoading.getVisibility() == View.VISIBLE) {
					mLlLoading.setVisibility(View.GONE);
					mLlLoadFailure.setVisibility(View.VISIBLE);
				} else {
					mSrlData.setRefreshing(false);
				}
				if (mPage > 1)
					mPage--;
				successType(jsonresult.get("success").toString(), "查询失败！");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			if (witch >= childJson.size()) {
				return;
			}
			Intent intent = new Intent(SlaughterListActivity2.this,
					SlaughterMoreActivity.class);
			intent.putExtra("entity", childJson.get(witch));
			startActivity(intent);

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


}
