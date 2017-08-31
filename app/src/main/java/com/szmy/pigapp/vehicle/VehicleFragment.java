package com.szmy.pigapp.vehicle;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
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
import com.szmy.pigapp.activity.ChoseProvinceActivity;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.AutoListView;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class VehicleFragment extends Fragment  {

	private AutoListView mXlistView;
	private LayoutInflater mInflater;
	private List<Vehicle> childJson = new ArrayList<Vehicle>(); // 子列表
	private VehiclesAdapter infoAdapter;
	private String page = "1";
	private String pageSize = "10";
	private final int TYPE_REFRESH = 1;
	private final int TYPE_LOAD = 2;
	private String provinceContent;
	private String cityContent;
	private String areaContent;
	private String startTime;
	private String startCapacity, endCapacity;
	private String priceType,minprice,maxprice;
	
	
	
	private Button seachareabtn, seachtimebtn, seachtypebtn, seachpricebtn;
	private List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> typedata = new ArrayList<HashMap<String, String>>();
	
	private int mPage=0;
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
		View view = inflater.inflate(R.layout.vehiclelist_activity, container,
				false);
		initView(view);
		clearStr();
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
		mInflater = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		infoAdapter = new VehiclesAdapter(getActivity(), childJson);
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
		mLv = (ListView) v.findViewById(R.id.lstv_fragment);
		mLv.setOnScrollListener(mOnScrollListener);
		mLv.setOnItemClickListener(mItemClickListener);
		mLlLoading = (LinearLayout) v.findViewById(R.id.ll_loading);
		mLlLoadFailure = (LinearLayout)v.findViewById(R.id.ll_load_failure);
		mLlLoadFailure.setOnClickListener(mClickListener);
		
		


		seachareabtn = (Button) v.findViewById(R.id.seachareabtn);
		seachareabtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(),
						ChoseProvinceActivity.class);
				startActivityForResult(intent, App.ADDRESS_CODE);
			}
		});
		seachtimebtn = (Button) v.findViewById(R.id.seachtimebtn);
		seachtimebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new TimePopupWindows(getActivity(), v);

			}
		});
		seachtypebtn = (Button) v.findViewById(R.id.seachtypebtn);
		seachtypebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new TypePopupWindows(getActivity(), v);
				
			}
		});
		seachpricebtn = (Button) v.findViewById(R.id.seachpricebtn);
		seachpricebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivityForResult(new Intent(getActivity(),ChosePriceActivity.class), 23);
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case App.ADDRESS_CODE:
			if (data == null)
				return;
			Bundle b = data.getExtras();
			provinceContent = b.getString("province");
			cityContent = b.getString("city");
			areaContent = b.getString("area");
			loadData();
			break;
		case 23:
			if (data == null)
				return;
			Bundle b1 = data.getExtras();
             priceType = b1.getString("priceType");
             minprice = b1.getString("minprice");
             maxprice = b1.getString("maxprice");
             System.out.println(priceType+ "" +minprice +"  "+ maxprice);
			loadData();
			break;
		}
	}

	private void loadData() {
		mIsLoading = true;
		RequestParams params = new RequestParams();
		if (provinceContent.equals("不限"))
			provinceContent = "";
		params.addBodyParameter("e.province", provinceContent);
		if (cityContent.equals("不限"))
			cityContent = "";
		params.addBodyParameter("e.city", cityContent);
		if (areaContent.equals("不限"))
			areaContent = "";
		params.addBodyParameter("e.area", areaContent);
		params.addBodyParameter("e.startPrice", minprice);
		params.addBodyParameter("e.endPrice", maxprice);
		params.addBodyParameter("e.orderBy", priceType);
		System.out.println(startCapacity+"    "+ endCapacity);
		params.addBodyParameter("e.startCapacity", startCapacity);
		params.addBodyParameter("e.endCapacity", endCapacity);
		params.addBodyParameter("e.startTime", startTime);
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
					infoAdapter = new VehiclesAdapter(getActivity(), childJson);
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
					infoAdapter = new VehiclesAdapter(getActivity(), childJson);
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
				// successType(jsonresult.get("success").toString(), "查询失败！");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	



	public class TimePopupWindows extends PopupWindow {

		public TimePopupWindows(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.time_dialog, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));
			TextView exittv = (TextView) view.findViewById(R.id.exit);
			exittv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dismiss();
					
				}
			});
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			ListView mListviewPro = (ListView) view
					.findViewById(R.id.lv_list_date);
			gettimedata();
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), data,
					R.layout.item_date, new String[] { "time" },
					new int[] { R.id.text1 });
			mListviewPro.setAdapter(adapter);
			mListviewPro.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					HashMap<String, String> maplist = (HashMap<String, String>) parent
							.getItemAtPosition(position);
					System.out.println(maplist.get("time"));

					startTime = maplist.get("time");
					loadData();

					dismiss();
				}
			});

		}
	}

	private void gettimedata() {
		data.clear();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = sDateFormat.format(new Date());
		HashMap<String, String> maplist = new HashMap<String, String>();
		maplist.put("time", date1);
		data.add(maplist);
		for (int i = 1; i < 30; i++) {
			HashMap<String, String> maplist1 = new HashMap<String, String>();
			Date date;
			try {
				date = (new SimpleDateFormat("yyyy-MM-dd")).parse(date1);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, 1);

				maplist1.put("time", (new SimpleDateFormat("yyyy-MM-dd"))
						.format(cal.getTime()));
				date1 = (new SimpleDateFormat("yyyy-MM-dd")).format(cal
						.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			data.add(maplist1);
		}
	}

	public class TypePopupWindows extends PopupWindow {

		public TypePopupWindows(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.time_dialog, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));
			TextView text = (TextView) view.findViewById(R.id.text);
			text.setText("请选择容量(头)");
			TextView exittv = (TextView) view.findViewById(R.id.exit);
			exittv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dismiss();
					
				}
			});
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			ListView mListviewPro = (ListView) view
					.findViewById(R.id.lv_list_date);
			if(typedata.size()==0){
				getcountdata();
			}
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), typedata,
					R.layout.item_date, new String[] { "count" },
					new int[] { R.id.text1 });
			mListviewPro.setAdapter(adapter);
			mListviewPro.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					HashMap<String, String> maplist = (HashMap<String, String>) parent
							.getItemAtPosition(position);
					System.out.println(maplist.get("count"));
					if (maplist.get("count").equals("小于50")) {
						startCapacity = "0";
						endCapacity = "50";

					} else if (maplist.get("count").equals("201以上")) {
						startCapacity = "201";
						endCapacity = "";
					} else {
						startCapacity = maplist.get("count").split("-")[0];
						endCapacity = maplist.get("count").split("-")[1];
					}
					loadData();

					dismiss();
				}
			});

		}
	}

	private void getcountdata() {
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("count", "小于50");
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("count", "51-100");
		HashMap<String, String> map3 = new HashMap<String, String>();
		map3.put("count", "101-150");
		HashMap<String, String> map4 = new HashMap<String, String>();
		map4.put("count", "151-200");
		HashMap<String, String> map5 = new HashMap<String, String>();
		map5.put("count", "201以上");
		typedata.add(map1);
		typedata.add(map2);
		typedata.add(map3);
		typedata.add(map4);
		typedata.add(map5);

	}

	private void clearStr() {
		provinceContent = "";
		cityContent = "";
		areaContent = "";
		startTime = "";
		startCapacity = "";
		endCapacity = "";
		priceType = "";
		minprice = "";
		maxprice = "";
	}
	
	
	private void load() {
		reload();
		mLlLoading.setVisibility(View.VISIBLE);
		mSrlData.setVisibility(View.GONE);
		mLlLoadFailure.setVisibility(View.GONE);
	}

	private void reload() {
		mPage = 1;
		clearStr();
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
					VehicleDetailActivity.class);
			if (witch >= childJson.size())
				return;
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
				clearStr();
				loadData();
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}
	};
	
	

}
