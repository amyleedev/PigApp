package com.szmy.pigapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
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
import com.szmy.pigapp.adapter.IntegralAdapter;
import com.szmy.pigapp.entity.IntegralEntry;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.AutoListView;
import com.szmy.pigapp.widget.AutoListView.OnLoadListener;
import com.szmy.pigapp.widget.AutoListView.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IntegralListActivity extends BaseActivity implements OnLoadListener,
		OnRefreshListener {
	private AutoListView mXlistView;
	private LayoutInflater mInflater;
	private List<IntegralEntry> childJson = new ArrayList<IntegralEntry>(); // 子列表
	private IntegralAdapter infoAdapter;
	private ImageButton add_btn;
	private String page = "1";
	private String pageSize = "10";
	private final int TYPE_REFRESH = 1;
	private final int TYPE_LOAD = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle);
		((TextView) findViewById(R.id.def_head_tv)).setText("积分详情");
		add_btn = (ImageButton) findViewById(R.id.add_btn);
		add_btn.setVisibility(View.GONE);
		initView();
		loadData(TYPE_REFRESH);
	}

	public void initView() {
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mXlistView = (AutoListView) findViewById(R.id.info_framgment_xlistview);
		infoAdapter = new IntegralAdapter(IntegralListActivity.this, childJson);
		mXlistView.setAdapter(infoAdapter);
		mXlistView.setOnRefreshListener(this);
		mXlistView.setOnLoadListener(this);
		mXlistView.setAdapter(infoAdapter);
		mXlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int witch, long arg3) {
				if (witch > childJson.size()) {
					return;
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loadData(TYPE_REFRESH);
	}

	private void loadData(final int type) {
		RequestParams params = new RequestParams();	
		params.addBodyParameter("pager.offset", page);
		params.addBodyParameter("e.pageSize", pageSize);
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.SELECT_INTEGRAL_LIST_URL,
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
						getData(responseInfo.result, type);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// disDialog();
						mXlistView.onRefreshComplete();
					}
				});
	}

	private void getData(String result, int type) {
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
				if(type==TYPE_REFRESH){
					mXlistView.onRefreshComplete();
					childJson.clear();
					childJson.addAll(childJsonItem);
					mXlistView.setResultSize(childJsonItem.size());
				}else if(type==TYPE_LOAD){
					mXlistView.onLoadComplete();
					childJson.addAll(childJsonItem);
					if(childJsonItem.size() == 0){
						mXlistView.setLoadFullhide();
					}else
					mXlistView.setResultSize(childJsonItem.size());
				}
				infoAdapter.notifyDataSetChanged();
			} else {
				successType(jsonresult.get("success").toString(), "查询失败！");
			}
		} catch (JSONException e) {
		}
	}

	@Override
	public void onRefresh() {
		page = "1";
		loadData(TYPE_REFRESH);
	}

	@Override
	public void onLoad() {
		page = String.valueOf(Integer.parseInt(page) + 1);
		loadData(TYPE_LOAD);
	}

}
