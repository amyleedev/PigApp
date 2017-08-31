package com.szmy.pigapp.vehicle;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.AutoListView;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyVehicleOrderActivity extends BaseActivity implements OnCheckedChangeListener {

	private AutoListView mlistView;
	private LayoutInflater mInflater;
	private List<Vehicle> childJson = new ArrayList<Vehicle>(); // 子列表
	private VehiclesAdapter infoAdapter;
	private BitmapUtils bitmapUtils;
	private String provinceContent = "", cityContent = "", quxianContent = "";
	private String orderType;// 买卖状态
	private String pricepx;// 排序
	private String pigType = "";
	
	private RadioButton mAllBtn, daifukuanbtn, daiyunshubtn;
	private final int TYPE_REFRESH=1;
	private final int TYPE_LOAD = 2;
	private String orderStatus = "";
	private RadioGroup radio;
	
	
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
		setContentView(R.layout.myvehicleorder_activity);
		((TextView) findViewById(R.id.def_head_tv)).setText("我的物流订单");
		bitmapUtils = new BitmapUtils(MyVehicleOrderActivity.this);
		initView();
		loadData("1", "20", "", "", "", "", "", "");
	}

	public void initView() {
		
		radio = (RadioGroup) findViewById(R.id.radioGroup1);
		radio.setOnCheckedChangeListener(this);

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		infoAdapter = new VehiclesAdapter(MyVehicleOrderActivity.this,childJson);
		mContext=MyVehicleOrderActivity.this;

		mFooterView = LayoutInflater.from(mContext).inflate(
				R.layout.footer_view, null);
		mLlFooterLoading = (LinearLayout) mFooterView
				.findViewById(R.id.ll_footer_loading);
		mTvFooterResult = (TextView) mFooterView
				.findViewById(R.id.tv_footer_result);
		mSrlData = (MySwipeRefreshLayout) findViewById(R.id.srl_myVehicleOrder);

		mSrlData.setOnRefreshListener(mRefreshListener);
		mSrlData.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mLv = (ListView) findViewById(R.id.lstv_myVehicleOrder);
		mLv.setOnScrollListener(mOnScrollListener);
		mLv.setOnItemClickListener(mItemClickListener);

		mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
		mLlLoadFailure = (LinearLayout)findViewById(R.id.ll_load_failure);
		mLlLoadFailure.setOnClickListener(mClickListener);
		
		
		
		


	}
	public void loadData(String offset, String pagesize,
			String provinceContent, String cityContent, String quxianContent,
			String orderType, String pigType, String pricepx) {
//		showDialog();
		mIsLoading = true;
		this.provinceContent = provinceContent;
		this.cityContent = cityContent;
		this.quxianContent = quxianContent;
		this.orderType = orderType;
		this.pricepx = pricepx;
		this.pigType = pigType;
		mPage=Integer.parseInt(offset);
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("e.purchaserID", App.userID);
		params.addBodyParameter("e.purchaserName", App.username);
		
		params.addBodyParameter("pager.offset",
				String.valueOf(mPage));
		params.addBodyParameter("e.pageSize", String.valueOf(PAGE_SIZE));
		if (provinceContent.equals("不限"))
			provinceContent = "";
		params.addBodyParameter("e.province", provinceContent);
		if (cityContent.equals("不限"))
			cityContent = "";
		params.addBodyParameter("e.city", cityContent);
		if (quxianContent.equals("不限"))
			quxianContent = "";
		params.addBodyParameter("e.area", quxianContent);

		params.addBodyParameter("e.orderBy", pricepx);
		params.addBodyParameter("e.pigType", pigType);
		params.addBodyParameter("e.orderType", orderType);
		params.addBodyParameter("e.orderStatus", orderStatus);

		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_VEHIVLEORDER_URL, params,
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
//						disDialog();
						getDate(responseInfo.result);

						Log.i("insert", " insert responseInfo.result = "
								+ responseInfo.result);

					}

					@Override
					public void onFailure(HttpException error, String msg) {
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
					infoAdapter = new VehiclesAdapter(MyVehicleOrderActivity.this,childJson);
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
					infoAdapter = new VehiclesAdapter(MyVehicleOrderActivity.this,childJson);
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
				 successType(jsonresult.get("success").toString(), "发布失败！");

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mPage = 1;
		switch (resultCode) {
		case 22:
			if (data == null)
				return;
			Bundle b = data.getExtras();
			provinceContent = b.getString("province");
			cityContent = b.getString("city");
			quxianContent = b.getString("area");
			setData("1");
			break;
		case 23:
			if (data == null)
				return;
			Bundle bundle = data.getExtras();
			pigType = bundle.getString("pigType");
			System.out.println(pigType);
			setData("1");
			break;
		case 24:
			if (data == null)
				return;
			Bundle bd = data.getExtras();
			orderType = bd.getString("orderType");
			setData("1");
			break;
		case 25:

			if (data == null)
				return;
			Bundle d = data.getExtras();
			pricepx = d.getString("orderBy");
			System.out.println("根据价格排序" + provinceContent + cityContent
					+ quxianContent + "   " + orderType + "  " + pricepx
					+ "   " + pigType);
			setData("1");
			break;
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
	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int witch,
				long arg3) {


			

				if(witch>=childJson.size()) return;
				if (witch >=childJson.size()) {
					return;
				}
//				Intent intent = new Intent(MyOrderActivity.this,
//						InfoSellDetailActivity.class);
//				intent.putExtra("id", childJson.get(witch - 1).getId());
//				intent.putExtra("userid", childJson.get(witch - 1).getUserID());
//				intent.putExtra("type", childJson.get(witch - 1).getOrderType());
//				startActivity(intent);
				
					Intent intent = new Intent(MyVehicleOrderActivity.this,
							VehicleDetailActivity.class);
					intent.putExtra("id", childJson.get(witch).getId());
				
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
				setData(String.valueOf(mPage));
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}
	};

	

	

	


	private void setData(String page){
		loadData(page, String.valueOf(PAGE_SIZE), provinceContent, cityContent, quxianContent,
				orderType, pigType, pricepx);
	}

	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.allorderbtn:
			orderStatus = "";
			loadData("1", "20", "", "", "", "", "", "");
			break;
		case R.id.daifukuanbtn:
			orderStatus = "4";
			loadData("1", "20", "", "", "", "", "", "");
			break;
		case R.id.daiyunshubtn:
			orderStatus = "3";
			loadData("1", "20", "", "", "", "", "", "");
			break;
		
		}
		
	}

}
