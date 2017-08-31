package com.szmy.pigapp.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.service.LocationService;
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
import com.szmy.pigapp.activity.ChoseDealTypeActivity;
import com.szmy.pigapp.activity.ChoseProvinceActivity;
import com.szmy.pigapp.activity.ChoseTypeActivity;
import com.szmy.pigapp.activity.InfoBuyDetailActivity;
import com.szmy.pigapp.activity.InfoSellDetailActivity;
import com.szmy.pigapp.activity.NewInfoActivity;
import com.szmy.pigapp.activity.SearchConditionActivity;
import com.szmy.pigapp.activity.SearchPigActivity;
import com.szmy.pigapp.activity.SlaughterhouseRenZhengActivity;
import com.szmy.pigapp.activity.ZhuChangRenZhengActivity;
import com.szmy.pigapp.adapter.ListViewAdapter;
import com.szmy.pigapp.agent.AgentRenZhengActivity;
import com.szmy.pigapp.entity.InfoEntry;
import com.szmy.pigapp.natural.NaturalAuthActivity;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.vehicle.NewVehicleActivity;
import com.szmy.pigapp.vehicle.VehicleCompanyAuthActivity;
import com.szmy.pigapp.widget.CircleFlowIndicator;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;
import com.szmy.pigapp.widget.ViewFlow;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0 2015-09-11 我发布的交易
 * @since 1.0
 */
public class MyDealFragment extends BaseFragment implements OnClickListener,RadioGroup.OnCheckedChangeListener {
	private MySwipeRefreshLayout mSrlData;
	private ListView mLv;
//	private OrderAdapter mAdapter;
	private ListViewAdapter mAdapter;
	private int mPage;
	private static final int PAGE_SIZE = 20;
	private List<InfoEntry> mList;

	private FrameLayout mFlBanner;
	private ViewFlow mVf;
	private CircleFlowIndicator mCfi;
	// private FrameLayout mFlChooseHeader;
	private FrameLayout mFlHover1;
	private LinearLayout mLlChoose;
	private FrameLayout mFlHover2;
	private View mFooterView;
	private LinearLayout mLlFooterLoading;
	private TextView mTvFooterResult;
	private boolean mIsLoadAll = false;
	private String mProvince = "";// 省
	private String mCity = "";// 市
	private String mPrefecture = "";// 县
	private String mOrderType = "";// 供求
	private String mPigType = "";// 品种
	private String mOrderBy = "";// 价格
	private String mStartPrice = "";// 开始价格
	private String mEndPrice = "";// 结束价格
	private String mStartWeight = "";// 开始体重
	private String mEndWeight = "";// 结束体重
	private String mStartNumber = "";// 开始数量
	private String mEndNumber = "";// 结束数量
	private String mOrderStatus = "";// 订单状态

//	private LinearLayout mLlAddress;
//	private TextView mTvAddress;
//	private LinearLayout mLlType;
//	private TextView mTvType;
//	private LinearLayout mLlSupply;
//	private TextView mTvSupply;
//	private LinearLayout mLlBolting;
//	private TextView mTvBolting;
	private LinearLayout mLlLoading;
	private LinearLayout mLlLoadFailure;
	private LinearLayout mLlNearby;
	public static final int LOCATION_ID = 29;
	public LocationService locationService;
	public Vibrator mVibrator;
	public static double x = 0;
	public static double y = 0;
	private String city;

	private final int REFRESH = 27;
	private boolean mIsLoading;
	private int margin = 0;
	private LayoutParams params;
	private RelativeLayout mRlTitle;
	private final int MONEY_TYPE = 11;
	private String[] mItems = { "发布出售信息", "发布收购信息", "发布物流信息" };
	private int mSingleChoiceID = 0;
	private TextView mTvMenu;
	private RadioGroup radio;
	private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

		@Override
		public void onRefresh() {
			reload();
		}
	};
	private LayoutInflater mInflater;
	private BitmapUtils bitmapUtils;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.myorder_activity, container, false);
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView(getView());
		 mProvince = "";// 省
		 mCity = "";// 市
		 mPrefecture = "";// 县
		 mOrderType = "";// 供求
		 mPigType = "";// 品种
		 mOrderBy = "";// 价格
		 mStartPrice = "";// 开始价格
		 mEndPrice = "";// 结束价格
		 mStartWeight = "";// 开始体重
		 mEndWeight = "";// 结束体重
		 mStartNumber = "";// 开始数量
		 mEndNumber = "";// 结束数量
		 mOrderStatus = "";// 订单状态
		reload();

	}

	public void initView(View view) {
		mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		bitmapUtils = new BitmapUtils(getActivity());
//		View toplayout = view.findViewById(R.id.top);
		radio = (RadioGroup) view.findViewById(R.id.radioGroup1);
		radio.setOnCheckedChangeListener(this);
//		toplayout.setVisibility(View.VISIBLE);
		((TextView) view.findViewById(R.id.def_head_tv)).setText("我的发布");
		SharedPreferences sp = getActivity().getSharedPreferences(App.USERINFO,
				0);

		if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
			App.mUserInfo = FileUtil.readUser(getActivity());
			App.setDataInfo(App.mUserInfo);
		}
		((LinearLayout) view.findViewById(R.id.def_head_back))
				.setVisibility(View.GONE);
		mTvMenu = (TextView) view.findViewById(R.id.tv_menu);
		mTvMenu.setText("发布");
		mTvMenu.setVisibility(View.VISIBLE);
		mTvMenu.setOnClickListener(this);
		mList = new ArrayList<InfoEntry>();

		mFooterView = LayoutInflater.from(getActivity()).inflate(
				R.layout.footer_view, null);
		mLlFooterLoading = (LinearLayout) mFooterView
				.findViewById(R.id.ll_footer_loading);
		mTvFooterResult = (TextView) mFooterView
				.findViewById(R.id.tv_footer_result);
		mSrlData = (MySwipeRefreshLayout) view.findViewById(R.id.srl_myorder);

		mSrlData.setOnRefreshListener(mRefreshListener);
		mSrlData.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mLv = (ListView) view.findViewById(R.id.lstv_myorder);
		mLv.setOnScrollListener(mOnScrollListener);
		mLv.setOnItemClickListener(mItemClickListener);

//		mLlAddress = (LinearLayout) view.findViewById(R.id.ll_address);
//		mLlAddress.setOnClickListener(mClickListener);
		// mTvAddress = (TextView) view.findViewById(R.id.tv_address);
//		mLlType = (LinearLayout) view.findViewById(R.id.ll_type);
//		mLlType.setOnClickListener(mClickListener);
		// mTvType = (TextView) view.findViewById(R.id.tv_type);
//		mLlSupply = (LinearLayout) view.findViewById(R.id.ll_supply);
//		mLlSupply.setOnClickListener(mClickListener);
		// mTvSupply = (TextView) view.findViewById(R.id.tv_supply);
//		mLlBolting = (LinearLayout) view.findViewById(R.id.ll_bolting);
//		mLlBolting.setOnClickListener(mClickListener);
		// mTvBolting = (TextView) view.findViewById(R.id.tv_price);
		mLlLoading = (LinearLayout) view.findViewById(R.id.ll_loading);
		mLlLoadFailure = (LinearLayout) view.findViewById(R.id.ll_load_failure);
		mLlLoadFailure.setOnClickListener(mClickListener);

		

	}

	public void loadData() {
		mIsLoading = true;
		RequestParams params = new RequestParams();
		params.addBodyParameter("pager.offset", String.valueOf(mPage));
		params.addBodyParameter("pager.pageSize", String.valueOf(PAGE_SIZE));
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("e.userID", App.userID);
		params.addBodyParameter("e.province", mProvince);
		params.addBodyParameter("e.city", mCity);
		params.addBodyParameter("e.area", mPrefecture);
		params.addBodyParameter("e.orderBy", mOrderBy); // 价格
		params.addBodyParameter("e.pigType", mPigType);// 品种
		params.addBodyParameter("e.orderType", mOrderType);// 供求
		params.addBodyParameter("e.orderStatus", mOrderStatus);
		params.addBodyParameter("e.startPrice", mStartPrice);
		params.addBodyParameter("e.endPrice", mEndPrice);
		params.addBodyParameter("e.startWeight", mStartWeight);
		params.addBodyParameter("e.endWeight", mEndWeight);
		params.addBodyParameter("e.startNumber", mStartNumber);
		params.addBodyParameter("e.endNumber", mEndNumber);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_ALL_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						mIsLoading = false;
						Log.i("insert", " insert responseInfo.result = "
								+ responseInfo.result);
//						if (getActivity().isDestroyed()) {
//							System.out.println("已经关闭了");
//							return;
//						}
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
//		if (!TextUtils.isEmpty(result)) {
//			System.out.println("selected_order_list:" + result);
////			try {
////				JSONObject jsonresult = new JSONObject(result);
////				if (jsonresult.optString("success").equals("1")) {
////					List<InfoEntry> childJsonItem = new ArrayList<InfoEntry>();
////					JSONArray jArrData = jsonresult.getJSONArray("list");
////					for (int i = 0; i < jArrData.length(); i++) {
////						JSONObject jobj2 = jArrData.optJSONObject(i);
////						GsonBuilder gsonb = new GsonBuilder();
////						Gson gson = gsonb.create();
////						InfoEntry mInfoEntry = gson.fromJson(jobj2.toString(),
////								InfoEntry.class);
////						childJsonItem.add(mInfoEntry);
////					}
////				}
////			} catch (Exception e) {
////			}
//		}
		JSONObject jsonresult;
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("success").toString().equals("1")) {
				List<InfoEntry> childJsonItem = new ArrayList<InfoEntry>();
				List<InfoEntry> childJsonItemtrue = new ArrayList<InfoEntry>();
				JSONArray jArrData = jsonresult.getJSONArray("list");
				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					InfoEntry mInfoEntry = gson.fromJson(jobj2.toString(),
							InfoEntry.class);
					childJsonItem.add(mInfoEntry);
					if (!mInfoEntry.getNumber().equals("0")){
						childJsonItemtrue.add(mInfoEntry);
					}
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
					System.out.println(childJsonItem.size());
//					mList.addAll(childJsonItem);
					mList.addAll(childJsonItemtrue);
//					mAdapter = new ListViewAdapter(getActivity(),, mList);
					mAdapter = new ListViewAdapter(mList,mInflater,bitmapUtils);
					mLv.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
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
//					mAdapter = new OrderAdapter(getActivity(), mList);
					mAdapter = new ListViewAdapter(mList,mInflater,bitmapUtils);
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
			}else{
				mIsLoading = false;
				if (mLlLoading.getVisibility() == View.VISIBLE) {
					mLlLoading.setVisibility(View.GONE);
					mLlLoadFailure.setVisibility(View.VISIBLE);
				} else {
					mSrlData.setRefreshing(false);
				}
				if (mPage > 1)
					mPage--;
				successType(jsonresult.get("success").toString(), "查询失败");
			}
		} catch (JSONException e) {
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case App.ADDRESS_CODE:
			if (data == null)
				return;
			Bundle b = data.getExtras();
			mProvince = b.getString("province");
			if (mProvince.equals("不限"))
				mProvince = "";
			mCity = b.getString("city");
			if (mCity.equals("不限"))
				mCity = "";
			mPrefecture = b.getString("area");
			if (mPrefecture.equals("不限"))
				mPrefecture = "";
			load();
			break;
		case 23:
			if (data == null)
				return;
			Bundle bundle = data.getExtras();
			mPigType = bundle.getString("pigType");
			load();
			break;
		case 24:
			if (data == null)
				return;
			Bundle bd = data.getExtras();
			mOrderType = bd.getString("orderType");
			load();
			break;
		// case 25:
		// if (data == null)
		// return;
		// Bundle d = data.getExtras();
		// pricepx = d.getString("orderBy");
		// System.out.println("根据价格排序" + provinceContent + cityContent
		// + quxianContent + "   " + orderType + "  " + pricepx
		// + "   " + pigType);
		// setData("1", TYPE_REFRESH);
		// break;
		case 30:
			if (data == null)
				return;
			Bundle b1 = data.getExtras();
			mProvince = b1.getString("province");
			if (mProvince.equals("不限"))
				mProvince = "";
			mCity = b1.getString("city");
			if (mCity.equals("不限"))
				mCity = "";
			mPrefecture = b1.getString("area");
			if (mPrefecture.equals("不限"))
				mPrefecture = "";
			mOrderBy = b1.getString("orderBy");
			mOrderType = b1.getString("orderType");
			mPigType = b1.getString("pigType");
			mOrderStatus = b1.getString("orderStatus");
			mStartPrice = b1.getString("startPrice");
			mEndPrice = b1.getString("endPrice");
			mStartWeight = b1.getString("startWeight");
			mEndWeight = b1.getString("endWeight");
			mStartNumber = b1.getString("startNumber");
			mEndNumber = b1.getString("endNumber");
			load();
			break;
		case REFRESH:
			load();
			break;
		
		}
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_menu:
			clearTempFromPref();
			showDialogChose();
			break;
		}
	}

	private void showDialogChose() {
		NewInfoActivity.mDataList.clear();
		NewVehicleActivity.mDataList.clear();
		clearTempFromPref();
		if (App.authentication.equals("0") || App.authentication.equals("")) {
			final Dialog dialog = new Dialog(getActivity(), "提示", "您未认证，无法发布信息，请先认证。");//2017-03-10修改提示内容
			dialog.addCancelButton("取消");
			dialog.setOnAcceptButtonClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();

					if (App.usertype.equals("1")) { // 猪场

						Intent zcintent = new Intent(
								getActivity(),
								ZhuChangRenZhengActivity.class);
						startActivityForResult(zcintent,
								MONEY_TYPE);
					} else if (App.usertype.equals("2")) {// 屠宰场
						Intent zcintent = new Intent(
								getActivity(),
								SlaughterhouseRenZhengActivity.class);
						startActivityForResult(zcintent,
								MONEY_TYPE);
					} else if (App.usertype.equals("3")) {// 经纪人
						Intent zcintent = new Intent(
								getActivity(),
								AgentRenZhengActivity.class);
						startActivityForResult(zcintent,
								MONEY_TYPE);
					} else if (App.usertype.equals("4")) {// 物流
						Intent wlintent = new Intent(
								getActivity(),
								VehicleCompanyAuthActivity.class);
						startActivityForResult(wlintent,
								MONEY_TYPE);
					} else if (App.usertype.equals("5")) {
						Intent wlintent = new Intent(
								getActivity(),
								NaturalAuthActivity.class);
						startActivityForResult(wlintent,
								MONEY_TYPE);

					} else {
						showToast("敬请期待！");
					}
				}
			});
			dialog.setOnCancelButtonClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
			dialog.show();
//			new AlertDialog.Builder(getActivity())
//					.setTitle("提示：")
//					.setMessage("请您先认证信息！")
//					.setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface arg0,
//										int arg1) {
//
//									if (App.usertype.equals("1")) { // 猪场
//
//										Intent zcintent = new Intent(
//												getActivity(),
//												ZhuChangRenZhengActivity.class);
//										startActivityForResult(zcintent,
//												MONEY_TYPE);
//									} else if (App.usertype.equals("2")) {// 屠宰场
//										Intent zcintent = new Intent(
//												getActivity(),
//												SlaughterhouseRenZhengActivity.class);
//										startActivityForResult(zcintent,
//												MONEY_TYPE);
//									} else if (App.usertype.equals("3")) {// 经纪人
//										Intent zcintent = new Intent(
//												getActivity(),
//												AgentRenZhengActivity.class);
//										startActivityForResult(zcintent,
//												MONEY_TYPE);
//									} else if (App.usertype.equals("4")) {// 物流
//										Intent wlintent = new Intent(
//												getActivity(),
//												VehicleCompanyAuthActivity.class);
//										startActivityForResult(wlintent,
//												MONEY_TYPE);
//									} else if (App.usertype.equals("5")) {
//										Intent wlintent = new Intent(
//												getActivity(),
//												NaturalAuthActivity.class);
//										startActivityForResult(wlintent,
//												MONEY_TYPE);
//
//									} else {
//										showToast("敬请期待！");
//									}
//
//								}
//							}).setNegativeButton("取消", null).show();
			return;
		}

		// if(App.usertype.equals(""))
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		mSingleChoiceID = 0;
		// builder.setIcon(R.drawable.icon);
		builder.setTitle("请选择");
		builder.setSingleChoiceItems(mItems, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						mSingleChoiceID = whichButton;

					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				if (mSingleChoiceID == 2) {
					startActivityForResult(new Intent(getActivity(),
							NewVehicleActivity.class), REFRESH);
				} else {
					Intent intent = new Intent(getActivity(),
							NewInfoActivity.class);
					intent.putExtra("type", mSingleChoiceID + 1);
					startActivityForResult(intent, REFRESH);
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		builder.create().show();
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
			case R.id.ll_address:
				startActivityForResult(new Intent(getActivity(),
						ChoseProvinceActivity.class), App.ADDRESS_CODE);
				break;
			case R.id.ll_type:
				startActivityForResult(new Intent(getActivity(),
						ChoseTypeActivity.class), 23);
				break;
			case R.id.ll_supply:
				startActivityForResult(new Intent(getActivity(),
						ChoseDealTypeActivity.class), 24);
				break;
			case R.id.ll_bolting:
				startActivityForResult(new Intent(getActivity(),
						SearchConditionActivity.class), 30);
				break;
			case R.id.et_to_search:
				startActivity(new Intent(getActivity(), SearchPigActivity.class));
				break;
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

				clearTempFromPref();
				if ((witch) >= mList.size() || (witch) < 0) {
					return;
				}
				if (!mList.get(witch).getOrderType().equals("2")) {
					Intent intent = new Intent(getActivity(),
							InfoSellDetailActivity.class);
					intent.putExtra("id", mList.get(witch).getId());
					intent.putExtra("userid", mList.get(witch).getUserID());
					intent.putExtra("type", mList.get(witch).getOrderType());
					startActivityForResult(intent, REFRESH);
				} else {
					Intent intent = new Intent(getActivity(),
							InfoBuyDetailActivity.class);
					intent.putExtra("id", mList.get(witch).getId());
					intent.putExtra("userid", mList.get(witch).getUserID());
					intent.putExtra("type", mList.get(witch).getOrderType());
					startActivityForResult(intent, REFRESH);
				}




		}
	};

	private OnScrollListener mOnScrollListener = new OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// if (firstVisibleItem > 0) {
			// if (mLlChoose.getParent() != mFlHover2) {
			// mFlHover1.removeView(mLlChoose);
			// mFlHover2.addView(mLlChoose);
			// }
			// } else {
			// if (mLlChoose.getParent() != mFlHover1) {
			// mFlHover2.removeView(mLlChoose);
			// mFlHover1.addView(mLlChoose);
			// }
			// }
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

	

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainFragment");
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
			case R.id.allorderbtn:
				mOrderStatus = "";
				load();
				break;
			case R.id.obligationbtn:
				mOrderStatus = "4";
				load();
				break;
			case R.id.daifahuobtn://已下单待确认
				mOrderStatus = "2";
				load();
				break;
			case R.id.daishouhuobtn:
				mOrderStatus = "6";
				load();
				break;
			case R.id.yichengjiao:
				mOrderStatus = "3";
				load();
				break;
		}

	}
}
