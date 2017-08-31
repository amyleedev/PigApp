package com.szmy.pigapp.agent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;
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
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.adapter.AgentAdapter;
import com.szmy.pigapp.entity.AgentEntry;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AgentListActivity extends BaseActivity {
	private LayoutInflater mInflater;
	private List<AgentEntry> childJson = new ArrayList<AgentEntry>(); // 子列表
	private AgentAdapter infoAdapter;
	private ImageButton add_btn;
	private String page = "1";
	private String pageSize = "10";
	private final int TYPE_REFRESH = 1;
	private final int TYPE_LOAD = 2;
	private LinearLayout mLlFiltrate;
	private String[] mItems = { "查看所有经纪人", "查看附近经纪人" };
	private int mSingleChoiceID = 0;
	private String province = "", city = "",area = "";
	private static final int PAGE_SIZE = 20;
	private int mPage;
	private Context mContext;
	private MySwipeRefreshLayout mSrlData;
	private ListView mLv;
	private View mFooterView;
	private LinearLayout mLlFooterLoading;
	private TextView mTvFooterResult;
	private boolean mIsLoadAll = false;
	private LinearLayout mLlLoading;
	private LinearLayout mLlLoadFailure;
	private boolean mIsLoading;
	private String x = "";
	private String y = "";

	private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

		@Override
		public void onRefresh() {
			reload();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle);
		((TextView) findViewById(R.id.def_head_tv)).setText("经纪人");
		add_btn = (ImageButton) findViewById(R.id.add_btn);
		add_btn.setVisibility(View.GONE);
		mLlFiltrate = (LinearLayout) findViewById(R.id.ll_filtrate);
		initView();
//		getProvice();
		loadData();
	}

//	private void getProvice() {
//		if (!getIntent().getStringExtra("province").equals("")
//				&& !getIntent().getStringExtra("city").equals("")) {
//			mLlFiltrate.setVisibility(View.VISIBLE);
//		}
//		if (mSingleChoiceID == 1) {
//			if (getIntent().hasExtra("province")) {
//				province = getIntent().getStringExtra("province");
//			}
//			if (getIntent().hasExtra("city")) {
//				city = getIntent().getStringExtra("city");
//			}
//		} else {
//			province = "";
//			city = "";
//		}
//	}

	public void initView() {
		if (getIntent().hasExtra("province")) {
			province = getIntent().getStringExtra("province");
		}
		if (getIntent().hasExtra("city")) {
			city = getIntent().getStringExtra("city");
		}
		if (getIntent().hasExtra("area")) {
			area = getIntent().getStringExtra("area");
		}
//		if(getIntent().hasExtra("x")){
//			x = String.valueOf(getIntent().getDoubleExtra("x", 0));
//		}
//		if(getIntent().hasExtra("y")){
//			y =String.valueOf( getIntent().getDoubleExtra("y",0));
//		}
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		infoAdapter = new AgentAdapter(AgentListActivity.this, childJson);
		mContext = AgentListActivity.this;
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
		mLv = (ListView) findViewById(R.id.info_framgment_xlistview);
		mLv.setOnScrollListener(mOnScrollListener);
		mLv.setOnItemClickListener(mItemClickListener);
		mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
		mLlLoadFailure = (LinearLayout) findViewById(R.id.ll_load_failure);
		mLlLoadFailure.setOnClickListener(mClickListener);
		mLlFiltrate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				showpayDailog();
				Intent intent =  new Intent(AgentListActivity.this,AgentMapActivity.class);
				intent.putExtra("x", x);
				intent.putExtra("y", y);
				intent.putExtra("province", province);
				intent.putExtra("city", city);
				intent.putExtra("area", area);
				startActivity(intent);
			}
		});
		mLlFiltrate.setVisibility(View.VISIBLE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loadData();
	}

	private void loadData() {
		System.out.println(province + "province" + city);
		mIsLoading = true;
		RequestParams params = new RequestParams();
		params.addBodyParameter("e.province", province);
		params.addBodyParameter("e.city", city);
		params.addBodyParameter("e.area", area);
		params.addBodyParameter("pager.offset", String.valueOf(mPage));
		params.addBodyParameter("e.pageSize", String.valueOf(PAGE_SIZE));
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_AGENT_LIST_URL,
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
				List<AgentEntry> childJsonItem = new ArrayList<AgentEntry>();
				JSONArray jArrData = jsonresult.getJSONArray("list");
				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					AgentEntry mInfoEntry = gson.fromJson(jobj2.toString(),
							AgentEntry.class);
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
						mLv.removeFooterView(mFooterView);
					}
					mLv.addFooterView(mFooterView);
					childJson.addAll(childJsonItem);
					infoAdapter = new AgentAdapter(AgentListActivity.this,
							childJson);
					mLv.setAdapter(infoAdapter);
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
					childJson.addAll(childJsonItem);
					infoAdapter = new AgentAdapter(AgentListActivity.this,
							childJson);
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
//		getProvice();
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
			Intent intent = new Intent(AgentListActivity.this,
					AgentMoreActivity.class);
			intent.putExtra("name", childJson.get(witch).getRealName());
			intent.putExtra("business", childJson.get(witch).getBusiness());
			intent.putExtra("companyName", childJson.get(witch)
					.getCompanyName());
			intent.putExtra("qq", childJson.get(witch).getQQ());
			intent.putExtra("phone", childJson.get(witch).getPhone());
			intent.putExtra("province", childJson.get(witch).getProvince());
			intent.putExtra("city", childJson.get(witch).getCity());
			intent.putExtra("area", childJson.get(witch).getArea());
			intent.putExtra("intro", childJson.get(witch).getIntro());
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
//				getProvice();
				loadData();
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}
	};

	private void showpayDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				AgentListActivity.this);
		mSingleChoiceID = 0;
		builder.setTitle("请选择");
		builder.setSingleChoiceItems(mItems, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						mSingleChoiceID = whichButton;
					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
//				getProvice();
				if (mSingleChoiceID == 1) {// 查看所有
					loadData();
				} else { // 查看附近
					loadData();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder.create().show();
	}
}
