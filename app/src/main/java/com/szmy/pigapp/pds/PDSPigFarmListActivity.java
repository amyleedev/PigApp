package com.szmy.pigapp.pds;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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

public class PDSPigFarmListActivity extends BaseActivity implements
		OnLoadListener, OnRefreshListener {

	private AutoListView mXlistView;
	private LayoutInflater mInflater;
	private List<PDSPigFarm> childJson = new ArrayList<PDSPigFarm>(); // 子列表
	private List<QuarantineOfficer> clchildJson = new ArrayList<QuarantineOfficer>(); // 子列表
	private List<Declaration> dechildJson = new ArrayList<Declaration>(); // 子列表
	private PDSPigFarmAdapter infoAdapter;
	private QuarantineAdapter clinfoAdapter;
	private DeclarationAdapter deinfoAdapter;
	private ImageButton add_btn;
	private String page = "1";
	private String pageSize = "10";
	private final int TYPE_REFRESH = 1;
	private final int TYPE_LOAD = 2;
	private String name = "";
	private String pwd = "";
	private BaseAdapter adapter;
	private LinearLayout mLlDevelop;
	private LinearLayout mLlHns,mLlPds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		((TextView) findViewById(R.id.def_head_tv)).setText("申报");
		add_btn = (ImageButton) findViewById(R.id.add_btn);
		add_btn.setVisibility(View.GONE);
		add_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PDSPigFarmListActivity.this,NewJYSBActivity.class);
				startActivityForResult(intent, 22);
			}
		});

		initView("");
		getUserName_Pwd(TYPE_REFRESH);
		// loadData(TYPE_REFRESH);
	}

	public void initView(String type) {
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mXlistView = (AutoListView) findViewById(R.id.info_framgment_xlistview);
		if (type.equals("2")) {
			clinfoAdapter = new QuarantineAdapter(PDSPigFarmListActivity.this,
					clchildJson);
			adapter = clinfoAdapter;
		} else if(type.equals("3")) {
			deinfoAdapter = new DeclarationAdapter(PDSPigFarmListActivity.this,
					dechildJson);
			adapter = deinfoAdapter;
			
		}else {
			infoAdapter = new PDSPigFarmAdapter(PDSPigFarmListActivity.this,
					childJson);
			adapter = infoAdapter;
		}
		
		if(adapter!=null)
		mXlistView.setAdapter(adapter);
		mXlistView.setOnRefreshListener(this);
		mXlistView.setOnLoadListener(this);
		mXlistView.setLoadFullshow();
		mXlistView.setAdapter(adapter);
		mXlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int witch,
					long arg3) {

			}
		});
		// add_btn.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		//
		// Bimp.bmp.clear();
		// Bimp.drr.clear();
		// Bimp.max = 0;
		//
		// Intent intent = new
		// Intent(PigFarmListActivity.this,NewVehicleActivity.class);
		//
		// startActivityForResult(intent, 11);
		//
		// }
		// });
		mLlDevelop = (LinearLayout) findViewById(R.id.lldevelop);
		mLlHns = (LinearLayout) findViewById(R.id.ll_hns);
		mLlHns.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
		mLlPds = (LinearLayout) findViewById(R.id.ll_pds);
		mLlPds.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		getUserName_Pwd(TYPE_REFRESH);
	}

	private void loadData(final int type) {

		RequestParams params = new RequestParams();

		params.addBodyParameter("api_method", "c.sblist");
		params.addBodyParameter("a_name", name);
		params.addBodyParameter("a_pwd", pwd);
		params.addBodyParameter("p_n", String.valueOf(page));

		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_PDSJYSB_URL,
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
						if (!TextUtils.isEmpty(responseInfo.result)) {
							getData(responseInfo.result, type);
							Log.i("query", " query responseInfo.result = "
									+ responseInfo.result);
						}
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
			if (jsonresult.get("result").toString().equals("success")) {
				// Toast.makeText(mContext, "查询成功！", Toast.LENGTH_SHORT).show();

				List<PDSPigFarm> childJsonItem = new ArrayList<PDSPigFarm>();
				JSONArray jArrData = jsonresult.getJSONArray("result_array");
				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					PDSPigFarm mInfoEntry = gson.fromJson(jobj2.toString(),
							PDSPigFarm.class);
					childJsonItem.add(mInfoEntry);
				}
				if (type == TYPE_REFRESH) {
					mXlistView.onRefreshComplete();
					childJson.clear();
					childJson.addAll(childJsonItem);
				} else if (type == TYPE_LOAD) {
					mXlistView.onLoadComplete();
					childJson.addAll(childJsonItem);
				}
				mXlistView.setResultSize(childJsonItem.size());
				adapter.notifyDataSetChanged();
			} else {
				mXlistView.setResultSize(0);
				adapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void loadClData(final int type) {

		RequestParams params = new RequestParams();

		params.addBodyParameter("api_method", "c.cllist");
		params.addBodyParameter("a_name", name);//
		params.addBodyParameter("a_pwd", pwd);// "c4ca4238a0b923820dcc509a6f75849b"
		params.addBodyParameter("p_n", String.valueOf(page));

		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_PDSJYSB_URL,
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
						if (!TextUtils.isEmpty(responseInfo.result)) {
							getClData(responseInfo.result, type);
							Log.i("query", " query responseInfo.result = "
									+ responseInfo.result);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// disDialog();
						mXlistView.onRefreshComplete();
					}
				});

	}

	private void getClData(String result, int type) {
		JSONObject jsonresult;
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("result").toString().equals("success")) {
				// Toast.makeText(mContext, "查询成功！", Toast.LENGTH_SHORT).show();

				List<QuarantineOfficer> childJsonItem = new ArrayList<QuarantineOfficer>();
				JSONArray jArrData = jsonresult.getJSONArray("result_array");
				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					QuarantineOfficer mInfoEntry = gson.fromJson(
							jobj2.toString(), QuarantineOfficer.class);
					childJsonItem.add(mInfoEntry);
				}
				if (type == TYPE_REFRESH) {
					mXlistView.onRefreshComplete();
					clchildJson.clear();
					clchildJson.addAll(childJsonItem);
				} else if (type == TYPE_LOAD) {
					mXlistView.onLoadComplete();
					clchildJson.addAll(childJsonItem);
				}
				mXlistView.setResultSize(childJsonItem.size());
				adapter.notifyDataSetChanged();
			} else {
				mXlistView.setResultSize(0);
				adapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void getUserName_Pwd(final int type) {
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("pgNum", page);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_DECLARATION_URL,
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
						if (!TextUtils.isEmpty(responseInfo.result)) {

							Log.i("", "  responseInfo.result = "
									+ responseInfo.result);
							getResult(responseInfo.result, type);

						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						 disDialog();
						mXlistView.onRefreshComplete();
					}
				});

	}

	private void getResult(String result, int type) {
		JSONObject jsonresult;
		disDialog();
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("result").toString().equals("success")) {
				if (jsonresult.optString("type").equals("1")) {// 养殖企业
					initView("1");
					add_btn.setVisibility(View.VISIBLE);
					getData(result, type);
				} else if (jsonresult.optString("type").equals("2")) { // 防疫员
					initView("2");
					getClData(result, type);
				} else if (jsonresult.optString("type").equals("3")) { // 河南省
					initView("3");
					getHnData(result,type);
				}else{
					Intent intent = new Intent(PDSPigFarmListActivity.this,NewJYSBActivity.class);
					intent.putExtra("others","noadd");//其他用户跳转新增页面 但不能提交
					startActivity(intent);
					finish();
				}

			} else {
//				showToast("关联用户信息错误！");
//				mLlDevelop.setVisibility(View.VISIBLE);
				Intent intent = new Intent(PDSPigFarmListActivity.this,NewJYSBActivity.class);
				intent.putExtra("others","noadd");//其他用户跳转新增页面 但不能提交
				startActivity(intent);
				finish();


			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void getHnData(String result,int type){
		
		JSONObject jsonresult;
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("result").toString().equals("success")) {
				// Toast.makeText(mContext, "查询成功！", Toast.LENGTH_SHORT).show();

				List<Declaration> childJsonItem = new ArrayList<Declaration>();
				JSONArray jArrData = jsonresult.getJSONArray("RECORD");
				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					Declaration mInfoEntry = gson.fromJson(
							jobj2.toString(), Declaration.class);
					childJsonItem.add(mInfoEntry);
				}
				if (type == TYPE_REFRESH) {
					mXlistView.onRefreshComplete();
					dechildJson.clear();
					dechildJson.addAll(childJsonItem);
				} else if (type == TYPE_LOAD) {
					mXlistView.onLoadComplete();
					dechildJson.addAll(childJsonItem);
				}
				mXlistView.setResultSize(childJsonItem.size());
				adapter.notifyDataSetChanged();
			} else {
				mXlistView.setResultSize(0);
				adapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	@Override
	public void onRefresh() {
		page = "1";
		if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {
			if (App.usertype.equals("1")) {
				loadData(TYPE_REFRESH);
			} else if (App.usertype.equals("7")) {
				loadClData(TYPE_REFRESH);
			}
		} else {
			mXlistView.onRefreshComplete();
			mXlistView.setLoadFullshow();
		}

	}

	@Override
	public void onLoad() {

		page = String.valueOf(Integer.parseInt(page) + 1);
		if (App.usertype.equals("1")) {
			loadData(TYPE_LOAD);
		} else if (App.usertype.equals("7")) {
			loadClData(TYPE_LOAD);
		}

	}

}
