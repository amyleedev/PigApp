package com.szmy.pigapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.szmy.pigapp.adapter.ListViewAdapter;
import com.szmy.pigapp.entity.InfoEntry;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppManager;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.AutoListView;
import com.szmy.pigapp.widget.AutoListView.OnLoadListener;
import com.szmy.pigapp.widget.AutoListView.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchPigActivity extends BaseActivity implements OnClickListener,
		OnRefreshListener, OnLoadListener {
	private AutoListView mlistView;
	private EditText et_search;
	private FrameLayout fl_search;
	private ImageButton ib_cancel;
	private String key = "";
	private Button btn_search;
	private LayoutInflater mInflater;
	private ListViewAdapter infoAdapter;
	public static List<InfoEntry> childJson = new ArrayList<InfoEntry>();
	public static String page = "1", pageSize = "10";
	private BitmapUtils bitmapUtils;
	private final int REFRESH = 27;
	private final int TYPE_REFRESH = 1;
	private final int TYPE_LOAD = 2;
	private RelativeLayout mRlTitleLayout;
	private String id = "";
	private String url = "";
	private String userID = "";
	private LinearLayout mLlBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_pig);
		mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
		mLlBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				finish();
			}
		});
		bitmapUtils = new BitmapUtils(this);
		childJson = new ArrayList<InfoEntry>();
		AppManager.addActivity(this);
		url = UrlEntry.QUERY_ALL_URL;
		mRlTitleLayout = (RelativeLayout) findViewById(R.id.titlelayout1);
		((TextView) findViewById(R.id.def_head_tv)).setText("搜索猪源信息");
		initView();
		if (getIntent().hasExtra("id")) {
			if(getIntent().hasExtra("type")){
				((TextView) findViewById(R.id.def_head_tv)).setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.def_head_tv)).setText(getIntent().getStringExtra("type"));
			}
			url = UrlEntry.QUERY_LIKELIST_URL;
			fl_search.setVisibility(View.GONE);
			mRlTitleLayout.setVisibility(View.VISIBLE);

			id = getIntent().getStringExtra("id");
			loadData("1", pageSize, "", TYPE_REFRESH);
		}
		if (getIntent().hasExtra("userID")) {

			userID = getIntent().getStringExtra("userID");
			System.out.println(userID);
			loadData("1", pageSize, "", TYPE_REFRESH);
		}
	}

	public void initView() {
		LinearLayout mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
		mLlBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(NewInfoActivity.LIKETYPE);
				finish();
			}
		});
		mlistView = (AutoListView) findViewById(R.id.lstv);
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		et_search = (EditText) findViewById(R.id.et_search);
		et_search.addTextChangedListener(mTextWatcher);
		fl_search = (FrameLayout) findViewById(R.id.fl_search);
		ib_cancel = (ImageButton) findViewById(R.id.ib_cancel);
		ib_cancel.setOnClickListener(this);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setVisibility(View.GONE);
		btn_search.setOnClickListener(this);
		et_search.setOnEditorActionListener(new TextView.OnEditorActionListener(){
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if(arg1 == EditorInfo.IME_ACTION_SEARCH)
				{
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
						// 根据条件进行搜索
						System.out.println("按条件搜索");
						key = et_search.getText().toString();
						loadData("1", pageSize, key, TYPE_REFRESH);
				}
				return false;
			}

		});
		infoAdapter = new ListViewAdapter(childJson, mInflater, bitmapUtils);
		mlistView.setAdapter(infoAdapter);
		mlistView.setLoadFullhide();
		mlistView.setRefashEnable(false);
		// mlistView.setOnRefreshListener(this);
		mlistView.setOnLoadListener(this);
		mlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int witch,
					long arg3) {
				if (witch >= childJson.size()) {
					return;
				}
				clearTempFromPref();
				if (!childJson.get(witch).getOrderType().equals("2")) {
					Intent intent = new Intent(SearchPigActivity.this,
							InfoSellDetailActivity.class);
					intent.putExtra("id", childJson.get(witch).getId());
					intent.putExtra("userid", childJson.get(witch).getUserID());
					intent.putExtra("type", childJson.get(witch).getOrderType());
					startActivityForResult(intent, REFRESH);
				} else {
					Intent intent = new Intent(SearchPigActivity.this,
							InfoBuyDetailActivity.class);
					intent.putExtra("id", childJson.get(witch).getId());
					intent.putExtra("userid", childJson.get(witch).getUserID());
					intent.putExtra("type", childJson.get(witch).getOrderType());
					startActivityForResult(intent, REFRESH);
				}
			}
		});
	}

	private void getDate(String result, int type) {
		hideInput();
		JSONObject jsonresult;
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("success").toString().equals("1")) {
				List<InfoEntry> childJsonItem = new ArrayList<InfoEntry>();
				JSONArray jArrData = jsonresult.getJSONArray("list");
				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					InfoEntry mInfoEntry = gson.fromJson(jobj2.toString(),
							InfoEntry.class);
					childJsonItem.add(mInfoEntry);
				}
				if (type == TYPE_REFRESH) {
					mlistView.onRefreshComplete();
					childJson.clear();
					childJson.addAll(childJsonItem);
				} else if (type == TYPE_LOAD) {
					mlistView.onLoadComplete();
					childJson.addAll(childJsonItem);
				}
				mlistView.setResultSize(childJsonItem.size());
				infoAdapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
		}
	}

	TextWatcher mTextWatcher = new TextWatcher() {
		private CharSequence temp;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (temp.length() == 0) {
				btn_search.setText("取消");
				btn_search.setVisibility(View.GONE);
				ib_cancel.setVisibility(View.GONE);
			} else {
				btn_search.setText("搜索");
				ib_cancel.setVisibility(View.VISIBLE);
				btn_search.setVisibility(View.VISIBLE);
			}
		}
	};

	public void loadData(String offset, String pagesize, String key,
			final int type) {
		showDialog();
		this.pageSize = pagesize;
		page = offset;
		RequestParams params = new RequestParams();
		System.out.println(page + "页数" + pageSize);
		params.addBodyParameter("pager.offset", page);
		params.addBodyParameter("e.pageSize", pageSize);
		System.out.println(key);
		if (!id.equals("")) {
			params.addBodyParameter("e.id", id);
		} else if(!userID.equals("")){
			params.addBodyParameter("e.userID", userID);
			params.addBodyParameter("uuid", App.uuid);
		}else {
			params.addBodyParameter("e.userName", key);
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
						Log.i("searchpig", " searchpig responseInfo.result = "
								+ responseInfo.result);
						disDialog();
						getDate(responseInfo.result, type);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						disDialog();
						mlistView.onRefreshComplete();
					}
				});
	}
	protected void onResume() {
		super.onResume();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		page = "1";
		switch (resultCode) {
		case REFRESH:
			// setData("1", TYPE_REFRESH);
			break;
		}
	}

	@Override
	public void onClick(View view) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		switch (view.getId()) {
		case R.id.login_btn:
			Intent intent = new Intent(SearchPigActivity.this,
					LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_search:
			imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
			if (btn_search.getText().equals("搜索")) {
				// 根据条件进行搜索
				System.out.println("按条件搜索");
				key = et_search.getText().toString();
				loadData("1", pageSize, key, TYPE_REFRESH);
			} else {
				// onBackPressed();
				finish();
			}
			break;
		case R.id.ib_cancel:
			et_search.setText("");
			break;
		}
	}

	@Override
	public void onLoad() {
		System.out.println("加载更多" + page);
		page = String.valueOf(Integer.parseInt(page) + 1);
		setData(page, TYPE_LOAD);
	}

	@Override
	public void onRefresh() {
		page = "1";
		System.out.println("刷新");
		key = "";
		setData(page, TYPE_REFRESH);
	}

	private void setData(String page, int type) {
		loadData(page, pageSize, key, type);
	}

//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			return false;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
}
