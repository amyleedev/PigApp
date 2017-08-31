package com.szmy.pigapp.zheshang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
import com.szmy.pigapp.widget.MySwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class KaihuhangChoose extends BaseActivity {
	private LayoutInflater mInflater;
	private ImageButton add_btn;
	private String page = "1";
	private String pageSize = "10";
	private final int TYPE_REFRESH = 1;
	private final int TYPE_LOAD = 2;
	private RelativeLayout mRlSearch;
	private EditText mEtSearch;
	private Button mBtnSearch;
	private List<Map<String, Object>> mylist ;
	private int mSingleChoiceID = 0;
	private String province = "", city = "";
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
	private String bankName = "" ,KaihuhangName = "", KaihuhangCode = "";
	private String y = "";
	private String typeName = "";
	private String[] listData;
	private SimpleAdapter adapterlist;
	private String ZHIHANGTYPE = "subbranchs";
	private String BANKTYPE = "banks";
	private String PROVINCETYPE = "provinces";
	private String CITYTYPE = "citys";
	private Boolean tixianflag = false;
	private TextView mTvTip;
	private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

		@Override
		public void onRefresh() {
			reload();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kaihuhangchoose_activity2);
		((TextView) findViewById(R.id.def_head_tv)).setText("选择开户行");
		if (getIntent().hasExtra("tixian")){
			((TextView)findViewById(R.id.def_head_tv)).setText("选择银行卡类型");
			tixianflag = true;
		}
		initView();
		typeName = "banks";
		load();
	}

	public void initView() {
		mylist = new ArrayList<>();
		mTvTip = (TextView) findViewById(R.id.my_set_adresschoose_textview_4);
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		adapterlist = new SimpleAdapter(KaihuhangChoose.this, mylist, R.layout.item_province,
				new String[] { "name" }, new int[] { R.id.text1 });
		mContext = KaihuhangChoose.this;
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
		mRlSearch = (RelativeLayout) findViewById(R.id.searchRl);
		mEtSearch = (EditText) findViewById(R.id.et_search);
		mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener(){
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if(arg1 == EditorInfo.IME_ACTION_SEARCH)
				{
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
					// 根据条件进行搜索
					System.out.println("按条件搜索");
					typeName = ZHIHANGTYPE;
					load();
				}
				return false;
			}

		});
		mBtnSearch = (Button) findViewById(R.id.btn_search);
		mBtnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);

				// 根据条件进行搜索
				System.out.println("按条件搜索");
				typeName = ZHIHANGTYPE;
				load();
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loadData();
	}

	private void loadData() {
		System.out.println(province + "province" + city);
		mIsLoading = true;
		String url ="";
		RequestParams params = new RequestParams();
		if(typeName.equals(BANKTYPE)){
			url = UrlEntry.BANK_LIST_URL;
		}else if(typeName.equals(PROVINCETYPE)){
			url = UrlEntry.PROVINCE_LIST_URL;
			params.addBodyParameter("bankName", bankName);
		}else if(typeName.equals(CITYTYPE)){
			url = UrlEntry.CITYS_LIST_URL;
			params.addBodyParameter("bankName", bankName);
			params.addBodyParameter("province", province);
		}else if(typeName.equals(ZHIHANGTYPE)){
			 url = UrlEntry.SUBBRANCHS_LIST_URL;
			params.addBodyParameter("subbranch", mEtSearch.getText().toString().trim());
			params.addBodyParameter("bankName", bankName);
			params.addBodyParameter("province", province);
			params.addBodyParameter("city", city);
			params.addBodyParameter("offset", String.valueOf(mPage));
			params.addBodyParameter("pageSize", String.valueOf(PAGE_SIZE));
		}

		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url,
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
						getData(responseInfo.result,typeName);
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

	private void getData(String result,String type) {
		JSONObject jsonresult;
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("success").toString().equals("1")) {
				List<Map<String,Object>> list1 = new ArrayList<Map<String, Object>>();
				if (!type.equals(ZHIHANGTYPE)) {
					String jArrData = jsonresult.optString(type);
					if (jArrData.contains(",")) {
						listData = new String[]{};
						listData = jArrData.split(",");
						for (int i = 0; i < listData.length; i++) {
							HashMap<String, Object> map = new HashMap<>();
							map.put("type",type);
							map.put("name", listData[i]);
							list1.add(map);
						}
					} else {
						HashMap<String, Object> map = new HashMap<>();
						map.put("name", jArrData);
						map.put("type",type);
						list1.add(map);
					}
				}else{
					JSONObject mapJSON = jsonresult.getJSONObject(type);
					Iterator iterator = mapJSON.keys();                       // joData是JSONObject对象
					while(iterator.hasNext()){
						HashMap<String, Object> map = new HashMap<>();
						String key = iterator.next() + "";
						map.put("code",key);
						map.put("type",type);
						map.put("name",mapJSON.get(key));
//									map.put(key,mapJSON.get(key));
						list1.add(map);
					}
				}
				if (mLlLoading.getVisibility() == View.VISIBLE) {
					mLlLoading.setVisibility(View.GONE);
					mSrlData.setVisibility(View.VISIBLE);
				} else {
					mSrlData.setRefreshing(false);
				}

				if (mPage == 1) {
					if (mylist.size() >= 0) {
						mylist.clear();
						mLv.removeFooterView(mFooterView);
					}
					mLv.addFooterView(mFooterView);
					mylist.addAll(list1);
					adapterlist = new SimpleAdapter(KaihuhangChoose.this, mylist, R.layout.item_province,
							new String[] { "name" }, new int[] { R.id.text1 });
					mLv.setAdapter(adapterlist);
					if (list1.size() == 0) {
						mLlFooterLoading.setVisibility(View.GONE);
						mTvFooterResult.setVisibility(View.VISIBLE);
						mTvFooterResult.setText("查询结果为空");
						mIsLoadAll = true;
					}else{
						if (!type.equals(ZHIHANGTYPE)){
							mLlFooterLoading.setVisibility(View.GONE);
							mTvFooterResult.setVisibility(View.VISIBLE);
							mTvFooterResult.setText("已加载全部");
							mIsLoadAll = true;
						}else{
							if (list1.size() < 20) {
								mLlFooterLoading.setVisibility(View.GONE);
								mTvFooterResult.setVisibility(View.VISIBLE);
								mTvFooterResult.setText("已加载全部");
								mIsLoadAll = true;
							} else {
								if (mLlLoading.getVisibility() == View.VISIBLE) {
									mLlLoading.setVisibility(View.GONE);
									mLlLoadFailure.setVisibility(View.VISIBLE);
								} else {
									mSrlData.setRefreshing(false);
								}
							}
						}

					}
				} else {
					mylist.addAll(list1);
					adapterlist = new SimpleAdapter(KaihuhangChoose.this, mylist, R.layout.item_province,
							new String[] { "name" }, new int[] { R.id.text1 });
					mLv.requestLayout();
					adapterlist.notifyDataSetChanged();
					if (list1.size() < 20) {
						mLlFooterLoading.setVisibility(View.GONE);
						mTvFooterResult.setVisibility(View.VISIBLE);
						mTvFooterResult.setText("已加载全部");
						mIsLoadAll = true;
					} else {
						mLlFooterLoading.setVisibility(View.VISIBLE);
						mTvFooterResult.setVisibility(View.GONE);
					}
				}
				adapterlist.notifyDataSetChanged();
			} else {
				mIsLoading = false;
//				mLlLoadFailure.setVisibility(View.VISIBLE);
				mSrlData.setRefreshing(false);

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
			}
		}
	};
	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int witch,
								long arg3) {

			if (witch >= mylist.size()) {
				return;
			}
			Map<String,Object> map = mylist.get(witch);
			String type = String.valueOf(map.get("type"));
			if (type.equals(ZHIHANGTYPE)){
				KaihuhangCode = String.valueOf(map.get("code"));
				KaihuhangName = String.valueOf(map.get("name"));
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("kaihuhangcode",KaihuhangCode);
				bundle.putString("kaihuhangname",KaihuhangName);
				bundle.putString("yinhangka",bankName);
				intent.putExtras(bundle);
				setResult(App.KAIHUHANG_CODE,intent);
				finish();
			}else if (type.equals(BANKTYPE)){
				typeName = PROVINCETYPE;
				bankName = String.valueOf(map.get("name"));
				if (tixianflag){
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("yinhangka",bankName);
					intent.putExtras(bundle);
					setResult(App.KAIHUHANG_CODE,intent);
					finish();
				}else{
					mTvTip.setText("请选择省份");
				load();
				}
			}else if (type.equals(PROVINCETYPE)){
				typeName = CITYTYPE;
				province = String.valueOf(map.get("name"));
				mTvTip.setText("请选择城市");
				load();
			}else if (type.equals(CITYTYPE)){
				typeName = ZHIHANGTYPE;
				city = String.valueOf(map.get("name"));
				mTvTip.setText("请选择开户行");
				load();
			}

		}
	};

	private OnScrollListener mOnScrollListener = new OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
							 int visibleItemCount, int totalItemCount) {
			if (firstVisibleItem + visibleItemCount == totalItemCount) {
				if (!typeName.equals(ZHIHANGTYPE)) return;
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
