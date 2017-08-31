package com.szmy.pigapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import com.szmy.pigapp.adapter.IntegralAdapter;
import com.szmy.pigapp.entity.IntegralEntry;
import com.szmy.pigapp.fragment.MoreFragment;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.AutoListView;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
//import com.szmy.pigapp.share.ShareModel;
//import com.szmy.pigapp.share.SharePopupWindow;

/**
 * 我的盟友
 * 
 * @author Administrator
 * 
 */
public class MyTuiGuangActivity extends BaseActivity implements OnCheckedChangeListener {

	private AutoListView mlistView;
	private LayoutInflater mInflater;
	private List<IntegralEntry> childJson = new ArrayList<IntegralEntry>(); // 子列表
	private IntegralAdapter infoAdapter;
	private BitmapUtils bitmapUtils;
	private String provinceContent = "", cityContent = "", quxianContent = "";
	private String orderType;// 买卖状态
	private String pricepx;// 排序
	private String pigType = "";
	private RadioButton mRbOne, mRbTwo, mRbThree;
	private final int TYPE_REFRESH = 1;
	private final int TYPE_LOAD = 2;
	private String grade = "";
	private RadioGroup radio;
	private TextView mTvtitle;
	//private SharePopupWindow share;
	
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
	
	private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

		@Override
		public void onRefresh() {
			reload();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytuiguang_activity);
		mTvtitle = (TextView) findViewById(R.id.def_head_tv);
		mTvtitle.setText("我的盟友");
		bitmapUtils = new BitmapUtils(MyTuiGuangActivity.this);
		initView();
		loadCount();
	}

	private void loadCount() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_COUNT_TUIGUANG,
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
						Log.i("", "  responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								mTvtitle.setText("我的盟友("
										+ jsonresult.optString("all") + "人)");
								mRbOne.setText("一级会员\n("
										+ jsonresult.optString("count1") + "人)");
								mRbTwo.setText("二级会员\n("
										+ jsonresult.optString("count2") + "人)");
								mRbThree.setText("三级会员\n("
										+ jsonresult.optString("count3") + "人)");
								if (jsonresult.optString("all").equals("0")) {
									final Dialog dialog = new Dialog(MyTuiGuangActivity.this, "提示", "您当前没有盟友,是否立即推广？");
									dialog.addCancelButton("取消");
									dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											dialog.cancel();
											setResult(MoreFragment.SHARE_TYPE);
											finish();
										}
									});
									dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											dialog.cancel();
											finish();

										}
									});
									dialog.show();

//									AlertDialog.Builder builder = new Builder(
//											MyTuiGuangActivity.this);
//									builder.setMessage("您当前没有盟友,是否立即推广？");
//									builder.setTitle("提示");
//									builder.setPositiveButton("确定",
//											new OnClickListener() {
//
//												@Override
//												public void onClick(
//														DialogInterface dialog,
//														int which) {
//													setResult(MoreFragment.SHARE_TYPE);
//													finish();
//												}
//											});
//									builder.setNegativeButton("取消", null);
//									builder.create().show();
								}
								mPage = 1;
								loadData("1");
							}else
							{
								successType(jsonresult.get("success").toString(), "操作失败！");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						disDialog();

					}
				});

	}

	public void initView() {
		radio = (RadioGroup) findViewById(R.id.radioGroup1);
		radio.setOnCheckedChangeListener(this);
		mRbOne = (RadioButton) findViewById(R.id.allorderbtn);
		mRbTwo = (RadioButton) findViewById(R.id.twobtn);
		mRbThree = (RadioButton) findViewById(R.id.threebtn);
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		infoAdapter = new IntegralAdapter(MyTuiGuangActivity.this, childJson);

		mContext=MyTuiGuangActivity.this;

		mFooterView = LayoutInflater.from(mContext).inflate(
				R.layout.footer_view, null);
		mLlFooterLoading = (LinearLayout) mFooterView
				.findViewById(R.id.ll_footer_loading);
		mTvFooterResult = (TextView) mFooterView
				.findViewById(R.id.tv_footer_result);
		mSrlData = (MySwipeRefreshLayout) findViewById(R.id.srl_tuiguang);

		mSrlData.setOnRefreshListener(mRefreshListener);
		mSrlData.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mLv = (ListView) findViewById(R.id.lstv_tuiguang);
		mLv.setOnScrollListener(mOnScrollListener);
		mLv.setOnItemClickListener(mItemClickListener);

		mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
		mLlLoadFailure = (LinearLayout)findViewById(R.id.ll_load_failure);
		mLlLoadFailure.setOnClickListener(mClickListener);
		
		
		
		


	}

	public void loadData(String grade) {
		mIsLoading = true;
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("pager.offset", String.valueOf(mPage));
		params.addBodyParameter("e.pageSize", String.valueOf(PAGE_SIZE));
		System.out.println(grade);
		params.addBodyParameter("grade", grade);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,
				UrlEntry.SELECT_INTEGRAL_LIST_URL, params,
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
						disDialog();
						mIsLoading = false;
						getDate(responseInfo.result);
						Log.i("insert", " insert responseInfo.result = "
								+ responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						disDialog();
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

	private void getDate(String result ) {
		JSONObject jsonresult;
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("success").toString().equals("1")) {
				List<IntegralEntry> childJsonItem = new ArrayList<IntegralEntry>();
				JSONArray jArrData = jsonresult.getJSONArray("list");
				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					IntegralEntry mInfoEntry = gson.fromJson(jobj2.toString(),
							IntegralEntry.class);
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
					infoAdapter = new IntegralAdapter(MyTuiGuangActivity.this, childJson);
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
					infoAdapter = new IntegralAdapter(MyTuiGuangActivity.this, childJson);
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
				successType(jsonresult.get("success").toString(), "操作失败！");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mPage = 1;
		switch (resultCode) {

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
		setData(grade);
		mIsLoadAll = false;
		
	}
	
	
	
	private View.OnClickListener mClickListener = new View.OnClickListener() {
		
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



				if (witch > childJson.size())
					return;
				if (witch > childJson.size()) {
					return;
				}
				// Intent intent = new Intent(MyOrderActivity.this,
				// InfoSellDetailActivity.class);
				// intent.putExtra("id", childJson.get(witch - 1).getId());
				// intent.putExtra("userid", childJson.get(witch -
				// 1).getUserID());
				// intent.putExtra("type", childJson.get(witch -
				// 1).getOrderType());
				// startActivity(intent);
			
		
			
			
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
				setData(grade);
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}
	};


	

	private void setData(String grade) {
		loadData(grade);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		mPage = 1;
		switch (checkedId) {
		case R.id.allorderbtn:
			grade = "1";
			loadData(grade);
			break;
		case R.id.twobtn:
			grade = "2";
			loadData(grade);
			break;
		case R.id.threebtn:
			grade = "3";
			loadData(grade);
			break;
		}
	}
}
