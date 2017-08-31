package com.szmy.pigapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
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
import com.szmy.pigapp.adapter.FarmAdapter;
import com.szmy.pigapp.entity.ZhuChang;
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

public class AlaughterListActivity extends BaseActivity implements
		OnLoadListener,OnRefreshListener {
	private AutoListView mXlistView;
	private LayoutInflater mInflater;
	private List<ZhuChang> childJson = new ArrayList<ZhuChang>(); // 子列表
	private FarmAdapter infoAdapter;
	private BitmapUtils bitmapUtils;
	private ImageButton add_btn;
	private String page = "1";
	private String pageSize = "10" ;
	private final int TYPE_REFRESH=1;
	private final int TYPE_LOAD = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle);
		((TextView) findViewById(R.id.def_head_tv)).setText("养殖场");
		add_btn = (ImageButton) findViewById(R.id.add_btn);
		add_btn.setVisibility(View.GONE);
		initView();
		loadData(TYPE_REFRESH);
	}

	public void initView() {
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mXlistView = (AutoListView) findViewById(R.id.info_framgment_xlistview);
		infoAdapter = new FarmAdapter(AlaughterListActivity.this,childJson);
//		mXlistView.setAdapter(infoAdapter);
		mXlistView.setOnRefreshListener(this);
		mXlistView.setOnLoadListener(this);
		mXlistView.setAdapter(infoAdapter);
		mXlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int witch,
					long arg3) {
			}
		});
//		add_btn.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//
//				Bimp.bmp.clear();
//				Bimp.drr.clear();
//				Bimp.max = 0;
//
//				Intent intent = new Intent(PigFarmListActivity.this,NewVehicleActivity.class);
//
//				startActivityForResult(intent, 11);
//
//			}
//		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loadData(TYPE_REFRESH);
	}

	private void loadData(final int type) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("e.userID", App.userID);
		 params.addBodyParameter("pager.offset", page);
		 params.addBodyParameter("e.pageSize", pageSize);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_PIGFARM_LIST_URL,
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
						getData(responseInfo.result,type);
						Log.i("insert", " insert responseInfo.result = "
								+ responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// disDialog();
						mXlistView.onRefreshComplete();
					}
				});
	}

	private void getData(String result,int type) {
		JSONObject jsonresult;
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("success").toString().equals("1")) {
				// Toast.makeText(mContext, "查询成功！", Toast.LENGTH_SHORT).show();
				List<ZhuChang> childJsonItem = new ArrayList<ZhuChang>();
				JSONArray jArrData = jsonresult.getJSONArray("list");

				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					ZhuChang mInfoEntry = gson.fromJson(jobj2.toString(),
							ZhuChang.class);
					childJsonItem.add(mInfoEntry);
				}
				if(type==TYPE_REFRESH){
					
					mXlistView.onRefreshComplete();
					childJson.clear();
					childJson.addAll(childJsonItem);
				}else if(type==TYPE_LOAD){
					mXlistView.onLoadComplete();
					childJson.addAll(childJsonItem);
				}
				mXlistView.setResultSize(childJsonItem.size());
				infoAdapter.notifyDataSetChanged();
			} else {
				mXlistView.onRefreshComplete();
				successType(jsonresult.get("success").toString(), "查询失败！");
			}
		} catch (JSONException e) {
		}
	}

	@Override
	public void onRefresh() {
		page = "1" ;
		loadData(TYPE_REFRESH);
	}

	@Override
	public void onLoad() {
		page = String.valueOf(Integer.parseInt(page) + 1);
		loadData(TYPE_LOAD);
	}
}
