package com.szmy.pigapp.pigdiagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.ActivityScanResult;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZhenDuanActivity extends BaseActivity {
	private LinearLayout mLlBack,mLlLayout1,mLlLayout2,mLlLayout3,mLlLayout4;
	private TextView mTvName;
	private Button mBtnYes,mBtnNo,mBtnSubmit,mBtnAgain;
	private ListView mLvList1,mLvList2,mLvList3;

	private List<ZhenDuan> childJson = new ArrayList<>(); // 子列表
	private List<ZhenDuan> childJson1 = new ArrayList<>(); // 子列表
	private List<ZhenDuan> childJson2 = new ArrayList<>(); // 子列表

	private ZhenDuanAdapter infoAdapter;
	private ZhenDuanAnswerAdapter answerAdapter;
	private ZhenDuanQuestionAdapter questionAdapter;
	private String pigId = "";
	private Map<String,String> maplist = new HashMap<>();
	private HashMap<Integer, Boolean> isSelected;
	private ZhenDuan mInfoEntry;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zbzd_layout);

		initView();
		getPigType();
	}

	private void initView() {
		mLlBack = (LinearLayout) findViewById(R.id.def_head_back1);
		mLlBack.setOnClickListener(mClickListener);
		mLlLayout1 = (LinearLayout) findViewById(R.id.layout_type);
		mLlLayout2 = (LinearLayout) findViewById(R.id.layout_single);
		mLlLayout3 = (LinearLayout) findViewById(R.id.layout_more);
		mLlLayout4 = (LinearLayout) findViewById(R.id.layout_answer);
		mBtnYes = (Button) findViewById(R.id.yes_btn);
		mBtnYes.setOnClickListener(mClickListener);
		mBtnNo = (Button) findViewById(R.id.no_btn);
		mBtnNo.setOnClickListener(mClickListener);
		mBtnSubmit = (Button) findViewById(R.id.btn_submit);
		mBtnSubmit.setOnClickListener(mClickListener);
		mLvList1 = (ListView) findViewById(R.id.listView);
		mLvList1.setOnItemClickListener(mItemClickListener);
		mLvList3 = (ListView) findViewById(R.id.listView2);
		mLvList2 = (ListView) findViewById(R.id.listView1);
		mLvList3.setOnItemClickListener(mItemClickListener2);
		mBtnAgain = (Button) findViewById(R.id.btn_again);
		mBtnAgain.setOnClickListener(mClickListener);
		mTvName = (TextView) findViewById(R.id.zb_name);

	}
	private void getPigType(){
		showDialog();
		RequestParams params = new RequestParams();
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_PIG_TYPE_URL,
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
					disDialog();
						Log.i("result", "responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString().equals("1")) {
								maplist.clear();
								if(childJson.size()>0) {
									childJson.clear();
								}
								mLlLayout1.setVisibility(View.VISIBLE);
								mLlLayout4.setVisibility(View.GONE);
								List<ZhenDuan> childJsonItem = new ArrayList<>();
								JSONArray jArrData = jsonresult.getJSONArray("list");
								for (int i = 0; i < jArrData.length(); i++) {
									JSONObject jobj2 = jArrData.optJSONObject(i);
									GsonBuilder gsonb = new GsonBuilder();
									Gson gson = gsonb.create();
									ZhenDuan mInfoEntry = gson.fromJson(jobj2.toString(),
											ZhenDuan.class);
									childJsonItem.add(mInfoEntry);

								}
								childJson.addAll(childJsonItem);
								infoAdapter = new ZhenDuanAdapter(ZhenDuanActivity.this,childJson);
								mLvList1.setAdapter(infoAdapter);
								AppStaticUtil.setListViewHeightBasedOnChildren(mLvList1);

							}else{
								showToast("获取失败");
							}
						}catch (Exception e){
							showToast("获取失败");
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						disDialog();
						showToast("连接服务器失败。");
					}
				});
	}
	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int witch,
								long arg3) {
			if (witch >= childJson.size()) {
				return;
			}
			pigId = childJson.get(witch).getId();

			getPigQuestion("");

		}
	};
	private AdapterView.OnItemClickListener mItemClickListener2 = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int witch,
								long arg3) {
			if (witch >= childJson2.size()) {
				return;
			}

			Intent intent = new Intent(ZhenDuanActivity.this, ActivityScanResult.class);
			intent.putExtra("url",UrlEntry.GET_ANSWER_INFO_URL+childJson2.get(witch).getId());
			intent.putExtra("type","zhenduan");
			startActivity(intent);
		}
	};
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String str = "";
			switch (v.getId()) {
			case R.id.yes_btn:
				maplist.put(mInfoEntry.getId(),"yes");
				Log.i("id",resolveMaplist());
				getPigQuestion(resolveMaplist());
				break;
			case R.id.no_btn:
				maplist.put(mInfoEntry.getId(),"no");
				getPigQuestion(resolveMaplist());
				break;
			case R.id.btn_submit:
				isSelected = questionAdapter.getIsSelected();
				boolean flag = false;
				for (int i = 0; i < isSelected.size(); i++) {
					if (isSelected.get(i).equals(true)) {
						flag = true;
						maplist.put(childJson1.get(i).getId(),"yes");
					}else{
						maplist.put(childJson1.get(i).getId(),"no");
					}
				}
				if (!flag){
					showToast("请至少选择一个症状");
					return;
				}else
				getPigAnswer();
				break;
				case R.id.btn_again:
					getPigType();
					break;

			}
		}
	};

	private void getPigQuestion(String strmap){
		showDialog();
		RequestParams params = new RequestParams();
		final HttpUtils http = new HttpUtils();
		params.addBodyParameter("e.pigID",pigId);
		if (!TextUtils.isEmpty(strmap))
		params.addBodyParameter("params",strmap);
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_QUAESATION_URL,
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
							disDialog();
						Log.i("result", "responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString().equals("1")) {
								if(jsonresult.optString("type").equals("single")){
									mLlLayout1.setVisibility(View.GONE);
									mLlLayout2.setVisibility(View.VISIBLE);
									 mInfoEntry = new ZhenDuan();
									mInfoEntry.setId(jsonresult.optString("symptomID"));
									mInfoEntry.setName(jsonresult.optString("symptomName"));
									mTvName.setText(mInfoEntry.getName());
									mBtnYes.setText(jsonresult.optString("yes"));
									mBtnNo.setText(jsonresult.optString("no"));
								}else if(jsonresult.optString("type").equals("more")){
									if(childJson1.size()>0){
										childJson1.clear();
									}
									mLlLayout1.setVisibility(View.GONE);
									mLlLayout2.setVisibility(View.GONE);
									mLlLayout3.setVisibility(View.VISIBLE);
									List<ZhenDuan> childJsonItem = new ArrayList<>();
									Map maps = (Map) JSON.parse(jsonresult.optString("map"));
									for (Object map : maps.entrySet()){
										ZhenDuan mInfoEntry = new ZhenDuan();
										mInfoEntry.setId(((Map.Entry)map).getKey()+"");
										mInfoEntry.setName(((Map.Entry)map).getValue()+"");
										childJsonItem.add(mInfoEntry);
										System.out.println(((Map.Entry)map).getKey()+"     " + ((Map.Entry)map).getValue());
									}
									isSelected = new HashMap<Integer,Boolean>();
									childJson1.addAll(childJsonItem);
									questionAdapter = new ZhenDuanQuestionAdapter(ZhenDuanActivity.this,childJson1,isSelected);
									mLvList2.setAdapter(questionAdapter);
									mLvList2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
									AppStaticUtil.setListViewHeightBasedOnChildren(mLvList2);
								}




							}else{
								showToast("获取失败");
							}
						}catch (Exception e){
							showToast("获取失败");
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						disDialog();
						showToast("连接服务器失败。");
					}
				});
	}
	private void getPigAnswer(){
		showDialog();
		RequestParams params = new RequestParams();
		final HttpUtils http = new HttpUtils();
		params.addBodyParameter("e.pigID",pigId);
		params.addBodyParameter("params",resolveMaplist());
		Log.i("str",resolveMaplist());
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_ANSWER_URL,
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
						disDialog();
						Log.i("result", "responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString().equals("1")) {
								if(childJson2.size()>0){
									childJson2.clear();
								}
							    mLlLayout3.setVisibility(View.GONE);
								mLlLayout4.setVisibility(View.VISIBLE);
								List<ZhenDuan> childJsonItem = new ArrayList<>();
								JSONArray jArrData = jsonresult.getJSONArray("list");
								for (int i = 0; i < jArrData.length(); i++) {

									JSONObject jobj2 = jArrData.optJSONObject(i);
									ZhenDuan mInfoEntry = new ZhenDuan();
									mInfoEntry.setId(jobj2.optString("id"));
									mInfoEntry.setName(jobj2.optString("name"));
									mInfoEntry.setDiscription(jobj2.optString("discription"));
									childJsonItem.add(mInfoEntry);
								}
								childJson2.addAll(childJsonItem);
								answerAdapter = new ZhenDuanAnswerAdapter(ZhenDuanActivity.this,childJson2);
								mLvList3.setAdapter(answerAdapter);
								AppStaticUtil.setListViewHeightBasedOnChildren(mLvList3);

							}else{
								showToast("获取失败");
							}
						}catch (Exception e){
							showToast("获取失败");
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						disDialog();
						showToast("连接服务器失败。");
					}
				});
	}
	private String resolveMaplist(){
		String str = "";
		for(Map.Entry<String, String> entry : maplist.entrySet()){
			str+="{'symptomID':'"+entry.getKey()+"','answer':'"+entry.getValue()+"'}!";
		}
		return str;
	}
}
