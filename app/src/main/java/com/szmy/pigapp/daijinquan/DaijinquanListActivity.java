package com.szmy.pigapp.daijinquan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.DaijinquanPopuWindow;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DaijinquanListActivity extends BaseActivity implements OnCheckedChangeListener {

	private LayoutInflater mInflater;
	private List<Daijinquan> childJson = new ArrayList<Daijinquan>(); // 子列表
	private DaijinquanAdapter infoAdapter;
	private String Status = "0";
	private RadioGroup radio;
	private DaijinquanPopuWindow mPwDialog;

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
	private String amount = "",toptip = "";

	private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

		@Override
		public void onRefresh() {
			reload();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydaijinquan_activity);
		((TextView) findViewById(R.id.def_head_tv)).setText("我的代金券");
		initView();
//		loadData("1");
	}

	public void initView() {

		radio = (RadioGroup) findViewById(R.id.radioGroup1);
		radio.setOnCheckedChangeListener(this);
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		infoAdapter =new DaijinquanAdapter(DaijinquanListActivity.this,
				childJson);

		mContext=DaijinquanListActivity.this;

		mFooterView = LayoutInflater.from(mContext).inflate(
				R.layout.footer_view, null);
		mLlFooterLoading = (LinearLayout) mFooterView
				.findViewById(R.id.ll_footer_loading);
		mTvFooterResult = (TextView) mFooterView
				.findViewById(R.id.tv_footer_result);
		mSrlData = (MySwipeRefreshLayout) findViewById(R.id.srl_myorder);

		mSrlData.setOnRefreshListener(mRefreshListener);
		mSrlData.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mLv = (ListView) findViewById(R.id.lstv_myorder);
		mLv.setOnScrollListener(mOnScrollListener);
		mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
		mLlLoadFailure = (LinearLayout)findViewById(R.id.ll_load_failure);
		mLlLoadFailure.setOnClickListener(mClickListener);

	}
	public void loadData(String offset) {
		mIsLoading = true;
		mPage=Integer.parseInt(offset);
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("e.status",Status);
		params.addBodyParameter("pager.offset",
				String.valueOf(mPage));
		params.addBodyParameter("e.pageSize", String.valueOf(PAGE_SIZE));

		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,UrlEntry.GET_DAIJINQUAN_LIST_URL, params,
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
						if(getIntent().hasExtra("amount")){
							amount = getIntent().getStringExtra("amount");
							toptip = getIntent().getStringExtra("toptip");
							showPopDialog(mLv);
						}
						disDialog();
						getDate(responseInfo.result);

						Log.i("insert", " insert responseInfo.result = "
								+ responseInfo.result);

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						if(getIntent().hasExtra("amount")){
							amount = getIntent().getStringExtra("amount");
							toptip = getIntent().getStringExtra("toptip");
							showPopDialog(mLv);
						}
//						disDialog();
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

	private void getDate(String result) {

		JSONObject jsonresult;
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("success").toString().equals("1")) {

				List<Daijinquan> childJsonItem = new ArrayList<Daijinquan>();
				JSONArray jArrData = jsonresult.getJSONArray("list");

				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					Daijinquan mInfoEntry = gson.fromJson(jobj2.toString(),
							Daijinquan.class);
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
					infoAdapter =new DaijinquanAdapter(DaijinquanListActivity.this,
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
					infoAdapter = new DaijinquanAdapter(DaijinquanListActivity.this,
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
		setData(String.valueOf(mPage));
		mIsLoadAll = false;

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
				setData(String.valueOf(mPage));
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}
	};


	private void setData(String page){
		loadData(page);
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
			case R.id.allorderbtn:
				Status = "0";
				showDialog();
				loadData("1");
				break;
			case R.id.obligationbtn://已过期
				Status = "3";
				showDialog();
				loadData("1");
				break;
			case R.id.daifahuobtn://已使用
				showDialog();
				Status = "2";
				loadData("1");
				break;

		}

	}



	private void showPopDialog(View parent) {

		// 自定义的单击事件
		OnClickLintener paramOnClickListener = new OnClickLintener();
		mPwDialog = new DaijinquanPopuWindow(DaijinquanListActivity.this,
				paramOnClickListener, parent,amount,toptip);
		// 如果窗口存在，则更新
		mPwDialog.update();
	}
	class OnClickLintener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.close: // 关闭
					mPwDialog.dismiss();
					break;
				case R.id.btn_look: //
					mPwDialog.dismiss();
					break;

				default:
					break;
			}

		}

	}

}
