package com.szmy.pigapp.pigactivity;

import android.content.Context;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.service.LocationService;
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
import com.szmy.pigapp.entity.PigPrice;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PigPriceYmData extends RelativeLayout {

	private Context mContext;
	private PigPriceDataAdapter mAdapter;
	//public static List<ProductEntry> childJson = new ArrayList<ProductEntry>();
	private static String code = "";
	public static String page = "1", pageSize = "10";

	private String orderType;// 买卖状态
	private String pricepx;// 排序 BitmapUtils bitmapUtils;
	private BitmapUtils bitmapUtils;
	public static final int TYPE_ID = 22;
	private final int REFRESH = 27;
	private final int TYPE_REFRESH = 1;
	private final int TYPE_LOAD = 2;
	private ImageButton searchBtn;
	private String productType = "" , pigtype = "";
	private MySwipeRefreshLayout mSrlData;
	private ListView mLv;
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
	private boolean mIsLoading;
	public PigPriceYmData(Context context) {
		this(context, null, 0);
		
		
	}

	public PigPriceYmData(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PigPriceYmData(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		inflate(context, R.layout.pigprice_activity, this);
		bitmapUtils = new BitmapUtils(mContext);
		initView();
	}
	

private void initView(){
		
		mList = new ArrayList<PigPrice>();

		mFooterView = LayoutInflater.from(mContext).inflate(
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
		params.addBodyParameter("e.productType", "玉米");
		params.addBodyParameter("e.type", "");
		params.addBodyParameter("e.province",PigPriceFragment.pro);
		params.addBodyParameter("e.city",PigPriceFragment.city);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_NEARBY_QUOTE_URL, params,
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
//						Log.i("pigpricelist", "  responseInfo.result = "
//								+ responseInfo.result);
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
//			System.out.println("selected_order_list:" + result);
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
					mAdapter = new PigPriceDataAdapter(mContext, mList);
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
					mAdapter = new PigPriceDataAdapter(mContext, mList);
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
	
}
