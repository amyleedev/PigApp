package com.szmy.pigapp.mynotices;

import android.content.Context;
import android.content.Intent;
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
import com.szmy.pigapp.activity.HealthAuthActivity;
import com.szmy.pigapp.activity.InfoBuyDetailActivity;
import com.szmy.pigapp.activity.InfoSellDetailActivity;
import com.szmy.pigapp.activity.SlaughterhouseRenZhengActivity;
import com.szmy.pigapp.activity.ZhuChangRenZhengActivity;
import com.szmy.pigapp.agent.AgentRenZhengActivity;
import com.szmy.pigapp.comment.NewsCommentActivity;
import com.szmy.pigapp.daijinquan.DaijinquanListActivity;
import com.szmy.pigapp.entity.NewsEntity;
import com.szmy.pigapp.natural.NaturalAuthActivity;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.vehicle.VehicleCompanyAuthActivity;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;
import com.szmy.pigapp.widget.SildeListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyNoticeListActivity extends BaseActivity {
	private LayoutInflater mInflater;
	private List<MyNotice> childJson = new ArrayList<MyNotice>(); // 子列表
	private MyNoticeAdapter infoAdapter;
	private ImageButton add_btn;
	private TextView mTvRead;
	private String page = "1";
	private String pageSize = "10";
	private final int TYPE_REFRESH = 1;
	private final int TYPE_LOAD = 2;

	private String[] mItems = { "查看所有经纪人", "查看附近经纪人" };
	private int mSingleChoiceID = 0;
	private String province = "", city = "",area = "";
	private static final int PAGE_SIZE = 20;
	private int mPage;
	private Context mContext;
	private MySwipeRefreshLayout mSrlData;
	private SildeListView mLv;
	private View mFooterView;
	private LinearLayout mLlFooterLoading;
	private TextView mTvFooterResult;
	private boolean mIsLoadAll = false;
	private LinearLayout mLlLoading;
	private LinearLayout mLlLoadFailure;
	private boolean mIsLoading;
	private String x = "";
	private String y = "";
	private String status = "";

	private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

		@Override
		public void onRefresh() {
			reload();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
		((TextView) findViewById(R.id.def_head_tv)).setText("我的消息");
		add_btn = (ImageButton) findViewById(R.id.add_btn);
		add_btn.setVisibility(View.GONE);
		mTvRead = (TextView) findViewById(R.id.tv_read);
		mTvRead.setVisibility(View.VISIBLE);

		initView();
		loadData();
	}

	public void initView() {

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		infoAdapter = new MyNoticeAdapter(MyNoticeListActivity.this, childJson);
//		infoAdapter.setOnClickListenerEditOrDelete(new MyNoticeAdapter.OnClickListenerEditOrDelete() {
//			@Override
//			public void OnClickListenerEdit(int position) {
//
//				showToast("edit position: " + position);
//			}
//
//			@Override
//			public void OnClickListenerDelete(int position) {
//				showToast("delete position: " + position);
//			}
//		});
	mContext = MyNoticeListActivity.this;
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
		mLv = (SildeListView) findViewById(R.id.info_framgment_xlistview);
		mLv.setOnScrollListener(mOnScrollListener);
		mLv.setOnItemClickListener(mItemClickListener);
		mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
		mLlLoadFailure = (LinearLayout) findViewById(R.id.ll_load_failure);
		mLlLoadFailure.setOnClickListener(mClickListener);
		mTvRead.setOnClickListener(mClickListener);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loadData();
	}

	private void loadData() {
		mIsLoading = true;
		RequestParams params = new RequestParams();
		params.addBodyParameter("e.province", province);
		params.addBodyParameter("e.status", status);
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("pager.offset", String.valueOf(mPage));
		params.addBodyParameter("e.pageSize", String.valueOf(PAGE_SIZE));
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_NOTICES_LIST_URL,
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
				final List<MyNotice> childJsonItem = new ArrayList<MyNotice>();
				JSONArray jArrData = jsonresult.getJSONArray("list");
				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					MyNotice mInfoEntry = gson.fromJson(jobj2.toString(),
							MyNotice.class);
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
					infoAdapter = new MyNoticeAdapter(MyNoticeListActivity.this,
							childJson);
				setListViewOnClick();
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
					infoAdapter = new MyNoticeAdapter(MyNoticeListActivity.this,
							childJson);
					setListViewOnClick();
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
				case R.id.tv_read:
					setReadNotice("","");
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
//			MyNotice  myNotice = childJson.get(witch);
//			setReadNotice(myNotice.getId(),"upp");
//			toIntent(myNotice);

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
			switch (scrollState) {
				//当listview开始滑动时，若有item的状态为Open，则Close，然后移除
				case SCROLL_STATE_TOUCH_SCROLL:
					if (sets.size() > 0) {
						for (SwipeListLayout s : sets) {
							s.setStatus(SwipeListLayout.Status.Close, true);
							sets.remove(s);
						}
					}
					break;

			}
		}
	};


	private void setReadNotice(String id,String type){
		showDialog();
		String url = "";
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		if (TextUtils.isEmpty(id)){
			url = UrlEntry.READALL_NOTICE_STATUS_URL;
		}else{
			if (type.equals("upp")){
			url = UrlEntry.UPP_NOTICE_STATUS_URL;
				params.addBodyParameter("e.id", id);
			}else{
				url = UrlEntry.DEL_NOTICE_STATUS_URL;
				params.addBodyParameter("idList", id);//;"1,2,3"
			}

		}

		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params,
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
						Log.i("upp",responseInfo.result);
						disDialog();
                    JSONObject jsonresult = null;
                    try {
                        jsonresult = new JSONObject(responseInfo.result);
                        if (jsonresult.optString("success").equals("1")) {
								showToast("操作成功");
							reload();
                        }else {
							successType(jsonresult.optString("success"),"操作失败");

						}
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						disDialog();
					}
				});
	}
	public static Set<SwipeListLayout> sets = new HashSet();
	public static class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

		private SwipeListLayout slipListLayout;

		public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
			this.slipListLayout = slipListLayout;
		}

		@Override
		public void onStatusChanged(SwipeListLayout.Status status) {
			if (status == SwipeListLayout.Status.Open) {
				//若有其他的item的状态为Open，则Close，然后移除
				if (sets.size() > 0) {
					for (SwipeListLayout s : sets) {
						s.setStatus(SwipeListLayout.Status.Close, true);
						sets.remove(s);
					}
				}
				sets.add(slipListLayout);
			} else {
				if (sets.contains(slipListLayout))
					sets.remove(slipListLayout);
			}
		}

		@Override
		public void onStartCloseAnimation() {

		}

		@Override
		public void onStartOpenAnimation() {

		}

	}
	private void setListViewOnClick(){
		infoAdapter.setOnClickListenerEditOrDelete(new MyNoticeAdapter.OnClickListenerEditOrDelete() {
			@Override
			public void OnClickListenerEdit(int position) {
				setReadNotice(childJson.get(position).getId(),"upp");

			}

			@Override
			public void OnClickListenerDelete(int position) {

				setReadNotice(childJson.get(position).getId(),"del");
			}

			@Override
			public void OnClickListenerLook(int position) {

				MyNotice  myNotice = childJson.get(position);
				setReadNotice(myNotice.getId(),"upp");
				toIntent(myNotice);
			}
		});

	}

	private void toIntent(MyNotice mNoticeEntry){
		JSONObject obj = null;
		try {
			obj = new JSONObject(mNoticeEntry.getCostom());
			String type = obj.optString("type");
			Intent intent=new Intent();
			if (type.equals("xd2u1") || type.equals("fb2u2s")) {
				String id=obj.optString("id");
				String userid=obj.optString("userid");
				String orderType=obj.optString("ordertype");
				if (!orderType.equals("2")) {
					intent.setClass(MyNoticeListActivity.this,InfoSellDetailActivity.class);
				} else {
					intent.setClass(MyNoticeListActivity.this,InfoBuyDetailActivity.class);
				}
				intent.putExtra("id", id);
				intent.putExtra("userid", userid);
				intent.putExtra("type", orderType);
			}else if (type.equals("cj2zc")){ //交易成功代金券
				String amount = obj.optString("amount");
				String toptip = "";
				intent.setClass(MyNoticeListActivity.this, DaijinquanListActivity.class);
				intent.putExtra("amount",amount);
				intent.putExtra("toptip",toptip);
			}else if(type.equals("rz2zc")){//认证代金券
				String amount = obj.optString("amount");
				String toptip = "认证成功";
				intent.setClass(MyNoticeListActivity.this, DaijinquanListActivity.class);
				intent.putExtra("amount",amount);
				intent.putExtra("toptip",toptip);
			}else if(type.equals("rz")){//认证成功
				String userType = obj.optString("userType");
				if (userType.equals("1")) { // 猪场
					intent.setClass(MyNoticeListActivity.this, ZhuChangRenZhengActivity.class);
				} else if (userType.equals("2")) {// 屠宰场
					intent.setClass(MyNoticeListActivity.this, SlaughterhouseRenZhengActivity.class);
				} else if (userType.equals("3")) {// 经纪人
					intent.setClass(MyNoticeListActivity.this, AgentRenZhengActivity.class);
				} else if (userType.equals("4")) {
					intent.setClass(MyNoticeListActivity.this, VehicleCompanyAuthActivity.class);
				} else if (userType.equals("5")) {
					intent.setClass(MyNoticeListActivity.this, NaturalAuthActivity.class);
				} else if (userType.equals("6")) {
					intent.setClass(MyNoticeListActivity.this, HealthAuthActivity.class);
					intent.putExtra("type", "6");
				} else if (userType.equals("7")) {
					intent.setClass(MyNoticeListActivity.this, HealthAuthActivity.class);
					intent.putExtra("type", "7");
				} else if (userType.equals("8")) {
					intent.setClass(MyNoticeListActivity.this, HealthAuthActivity.class);
					intent.putExtra("type", "8");
				}
			}else if(type.equals("news2all")){
				intent.setClass(MyNoticeListActivity.this, NewsCommentActivity.class);
				NewsEntity news = new NewsEntity();
				news.setId(obj.optString("id"));
				Bundle bundle = new Bundle();
				bundle.putSerializable("news", news);
				bundle.putString("url", UrlEntry.ip+"/mNews/newsInfo.html?id="+obj.optString("id"));
				intent.putExtras(bundle);
			}else{
					return;
			}
			startActivityForResult(intent,App.READ_CODE);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
