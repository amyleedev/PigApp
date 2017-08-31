package com.szmy.pigapp.vehicle;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.AutoListView;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CompanyFragment extends Fragment {

	private AutoListView mXlistView;
	private LayoutInflater mInflater;
	private List<Company> childJson = new ArrayList<Company>(); // 子列表
	private VehicleCompanyAdapter infoAdapter;
	private BitmapUtils bitmapUtils;
	//	private String page = "1";
//	private String pageSize = "10" ;
//	private final int TYPE_REFRESH=1;
//	private final int TYPE_LOAD = 2;
	private String searchName = "";
	private FrameLayout fl_search;
	private ImageButton ib_cancel;
	private EditText et_search;
	private Button btn_search;
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
	private double x,y;
	private String URL;
	private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

		@Override
		public void onRefresh() {
			reload();
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	/*
     * 给Fragment 加载UI的布局，返回Fragment布局文件对应的东东
     */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.companylist_fragment,container, false);
		initView(view);
		loadData();
		return view;
	}

	/*
     * 当用户离开此Fragment时调用
     */
	@Override
	public void onPause() {

		super.onPause();
	}



	public void initView(View v) {
		x=getArguments().getDouble("x");
		y=getArguments().getDouble("y");
//		Toast.makeText(getActivity(), x+"-----"+y, 1).show();
		mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


		fl_search = (FrameLayout) v.findViewById(R.id.fl_search);
		btn_search = (Button) v.findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
				searchName = et_search.getText().toString();
				loadData();
			}
		});
		ib_cancel = (ImageButton) v.findViewById(R.id.ib_cancel);
		et_search = (EditText) v.findViewById(R.id.et_search);
		et_search.addTextChangedListener(mTextWatcher);
		ib_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_search.setText("");

			}
		});

		infoAdapter = new VehicleCompanyAdapter(getActivity(), childJson);


		mContext=getActivity();

		mFooterView = LayoutInflater.from(mContext).inflate(
				R.layout.footer_view, null);
		mLlFooterLoading = (LinearLayout) mFooterView
				.findViewById(R.id.ll_footer_loading);
		mTvFooterResult = (TextView) mFooterView
				.findViewById(R.id.tv_footer_result);
		mSrlData = (MySwipeRefreshLayout) v.findViewById(R.id.srl_fragment);

		mSrlData.setOnRefreshListener(mRefreshListener);
		mSrlData.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mLv = (ListView) v.findViewById(R.id.comlstv);
		mLv.setOnScrollListener(mOnScrollListener);
		mLv.setOnItemClickListener(mItemClickListener);

		mLlLoading = (LinearLayout) v.findViewById(R.id.ll_loading);
		mLlLoadFailure = (LinearLayout)v.findViewById(R.id.ll_load_failure);
		mLlLoadFailure.setOnClickListener(mClickListener);

	}
	TextWatcher mTextWatcher = new TextWatcher() {
		private CharSequence temp;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			// TODO Auto-generated method stub
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

			if (temp.length() == 0) {
				ib_cancel.setVisibility(View.GONE);
			} else {
				ib_cancel.setVisibility(View.VISIBLE);
			}

		}
	};
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		loadData();
	}

	private void loadData() {
		mIsLoading = true;
		RequestParams params = new RequestParams();
		if (x != 0 && y != 0) {
			URL = UrlEntry.QUERY_NEAR_VEHICLECOMPANY_URL;
			params.addBodyParameter("e.x", String.valueOf(x));
			params.addBodyParameter("e.y", String.valueOf(y));
		} else {
			URL = UrlEntry.QUERY_VEHICLECOMPANY_LIST_URL;
		}
		params.addBodyParameter("e.name", searchName);

		params.addBodyParameter("pager.offset", String.valueOf(mPage));
		params.addBodyParameter("e.pageSize", String.valueOf(PAGE_SIZE));
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,
				URL, params,
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

				List<Company> childJsonItem = new ArrayList<Company>();
				JSONArray jArrData = jsonresult.getJSONArray("list");

				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					Company mInfoEntry = gson.fromJson(jobj2.toString(),
							Company.class);
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
					infoAdapter = new VehicleCompanyAdapter(getActivity(), childJson);
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
					infoAdapter = new VehicleCompanyAdapter(getActivity(), childJson);
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
//				successType(jsonresult.get("success").toString(), "查询失败！");
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
		searchName = "";
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

			Intent intent = new Intent(getActivity(),
					VehicleListActivity.class);
			if(witch>=childJson.size()) return;

			intent.putExtra("id", childJson.get(witch).getId());
			// intent.putExtra("userid", childJson.get(witch -
			// 1).getUserID());

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
				searchName = "";
				loadData();
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}
	};



}
