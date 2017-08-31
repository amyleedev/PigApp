package com.szmy.pigapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.widgets.Dialog;
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
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.vehicle.NewVehicleActivity;
import com.szmy.pigapp.vehicle.Vehicle;
import com.szmy.pigapp.vehicle.VehicleCompanyAuthActivity;
import com.szmy.pigapp.vehicle.VehicleDetailActivity;
import com.szmy.pigapp.vehicle.VehiclesAdapter;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyVehicleActivity extends BaseActivity {

	private LayoutInflater mInflater;
	private List<Vehicle> childJson = new ArrayList<Vehicle>(); // 子列表
	private VehiclesAdapter infoAdapter;
	private BitmapUtils bitmapUtils;
	private ImageButton add_btn;
	private final int TYPE_REFRESH = 1;
	private final int TYPE_LOAD = 2;

	private int mPage;
	private static final int PAGE_SIZE = 20;
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
	private final int MONEY_TYPE = 11;

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
		((TextView) findViewById(R.id.def_head_tv)).setText("我的物流信息");
		add_btn = (ImageButton) findViewById(R.id.add_btn);
		add_btn.setVisibility(View.VISIBLE);
		bitmapUtils = new BitmapUtils(MyVehicleActivity.this);
		SharedPreferences sp = getSharedPreferences(App.USERINFO, 0);
		if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
			App.mUserInfo = FileUtil.readUser(MyVehicleActivity.this);
			App.setDataInfo(App.mUserInfo);
		}
		initView();
		loadData();
	}

	public void initView() {
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		infoAdapter = new VehiclesAdapter(MyVehicleActivity.this, childJson);
		mContext = MyVehicleActivity.this;

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

		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				System.out.println(App.authentication);
				if (App.authentication.equals("0")
						|| App.authentication.equals("")) {

					final Dialog dialog = new Dialog(MyVehicleActivity.this, "提示", "请您先认证物流信息！");
					dialog.addCancelButton("取消");
					dialog.setOnAcceptButtonClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.cancel();
							Intent wlintent = new Intent(
									MyVehicleActivity.this,
									VehicleCompanyAuthActivity.class);
							startActivityForResult(wlintent,
									MONEY_TYPE);
						}
					});
					dialog.setOnCancelButtonClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.cancel();
						}
					});
					dialog.show();
//					new AlertDialog.Builder(MyVehicleActivity.this)
//							.setTitle("提示：")
//							.setMessage("请您先认证物流信息！")
//							.setPositiveButton("确定",
//									new DialogInterface.OnClickListener() {
//										@Override
//										public void onClick(
//												DialogInterface arg0, int arg1) {
//											Intent wlintent = new Intent(
//													MyVehicleActivity.this,
//													VehicleCompanyAuthActivity.class);
//											startActivityForResult(wlintent,
//													MONEY_TYPE);
//										}
//									}).setNegativeButton("取消", null).show();
					return;
				} else {
					NewVehicleActivity.mDataList.clear();
					clearTempFromPref();
					Intent intent = new Intent(MyVehicleActivity.this,
							NewVehicleActivity.class);

					startActivityForResult(intent, 11);
				}

			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		loadData();
	}

	private void loadData() {
		mIsLoading = true;
		RequestParams params = new RequestParams();

		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("e.createAccount", App.username);
		params.addBodyParameter("pager.offset", String.valueOf(mPage));
		params.addBodyParameter("e.pageSize", String.valueOf(PAGE_SIZE));
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_VEHIVLE_ALL_URL,
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

						Log.i("myVehicleinsert",
								" insert responseInfo.result = "
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

				List<Vehicle> childJsonItem = new ArrayList<Vehicle>();
				JSONArray jArrData = jsonresult.getJSONArray("list");

				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					Vehicle mInfoEntry = gson.fromJson(jobj2.toString(),
							Vehicle.class);
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
					infoAdapter = new VehiclesAdapter(MyVehicleActivity.this,
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
					infoAdapter = new VehiclesAdapter(MyVehicleActivity.this,
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

			if (witch >= childJson.size())
				return;
			
			Intent intent = new Intent(MyVehicleActivity.this,
					VehicleDetailActivity.class);
			intent.putExtra("id", childJson.get(witch).getId());

			startActivityForResult(intent, 11);

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
