package com.szmy.pigapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ChoseProvinceActivity extends BaseActivity implements
		OnClickListener {
	private ListView mListviewPro, mListviewCity, mListviewArea;
	private TextView mTextview;
	private Button mButton;
	private String choiceProContent = "";
	private String choiceCityContent = "";
	private String choiceAreaContent = "";
	private SimpleAdapter adapter;
	private String type = "";

	// 这应该向数据库请求数据
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_area_selector);
		((TextView) findViewById(R.id.def_head_tv)).setText("请选择");
		Intent intent = getIntent();
		if (intent != null) {
			if (intent.hasExtra("add")) {
				type = intent.getStringExtra("add");
			}
		}
		initView();
	}

	private void initView() {
		mTextview = (TextView) findViewById(R.id.tv_choice);
		mButton = (Button) findViewById(R.id.btn_sure);
		mButton.setOnClickListener(this);
		mListviewPro = (ListView) findViewById(R.id.lv_list);
		mListviewCity = (ListView) findViewById(R.id.lv_list_city);
		mListviewArea = (ListView) findViewById(R.id.lv_list_area);
		// 生成适配器，数组===》ListItem
		adapter = new SimpleAdapter(this, mylist, R.layout.item_province,
				new String[] { "name" }, new int[] { R.id.text1 });
		mListviewPro.setAdapter(adapter);
		sendDataToHttp(adapter, "0");
		mListviewPro.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String pcode = mylist.get(position).get("code");
				choiceProContent = mylist.get(position).get("name");
				if (choiceProContent.equals("不限")) {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("province", choiceProContent);
					bundle.putString("city", choiceCityContent);
					bundle.putString("area", choiceAreaContent);
					intent.putExtras(bundle);
					setResult(App.ADDRESS_CODE, intent);
					finish();
				} else {
					sendDataToHttp(adapter, pcode);
					mListviewPro.setVisibility(View.GONE);
					mListviewCity.setVisibility(View.VISIBLE);
					mListviewCity.setAdapter(adapter);
					mTextview.setText(choiceProContent);
				}
			}
		});

		mListviewCity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String pcode = mylist.get(position).get("code");
				choiceCityContent = mylist.get(position).get("name");
				sendDataToHttp(adapter, pcode);
				mListviewPro.setVisibility(View.GONE);
				mListviewCity.setVisibility(View.GONE);
				mListviewArea.setVisibility(View.VISIBLE);
				mListviewArea.setAdapter(adapter);
				mTextview.setText(choiceProContent + " " + choiceCityContent);
			}
		});

		mListviewArea.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String pcode = mylist.get(position).get("code");
				choiceAreaContent = mylist.get(position).get("name");
				sendDataToHttp(adapter, pcode);
				mTextview.setText(choiceProContent + " " + choiceCityContent
						+ " " + choiceAreaContent);
				if (type.equals("")) {
					App.province = choiceProContent;
					App.city = choiceCityContent;
					App.area = choiceAreaContent;
				}
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("province", choiceProContent);
				bundle.putString("city", choiceCityContent);
				bundle.putString("area", choiceAreaContent);
				intent.putExtras(bundle);
				setResult(App.ADDRESS_CODE, intent);
				finish();
			}
		});
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.btn_sure:
			if (choiceProContent.equals("")) {
				finish();
			}
			if (type.equals("")) {
				App.province = choiceProContent;
				App.city = choiceCityContent;
				App.area = choiceAreaContent;
			}
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("province", choiceProContent);
			bundle.putString("city", choiceCityContent);
			bundle.putString("area", choiceAreaContent);
			intent.putExtras(bundle);
			setResult(App.ADDRESS_CODE, intent);
			finish();
			break;
		}
	}

	

	public void sendDataToHttp(final SimpleAdapter adapter, final String code) {
		showDialog();
		String url = UrlEntry.GET_ADDRESS_URL;
		RequestParams params = new RequestParams();
		params.addBodyParameter("pcode", code);
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
						disDialog();
						Log.i("address", " address responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								JSONArray jArrData = jsonresult
										.getJSONArray("list");
								if (jArrData.length() > 0) {
									mylist.clear();
									if (code.equals("0")) {
										HashMap<String, String> map = new HashMap<String, String>();
										map.put("id", "");
										map.put("code", "");
										map.put("pcode", "");
										map.put("name", "不限");
										mylist.add(map);
									}
								} else {
									Intent intent = new Intent();
									Bundle bundle = new Bundle();
									bundle.putString("province",
											choiceProContent);
									bundle.putString("city", choiceCityContent);
									bundle.putString("area", choiceAreaContent);
									intent.putExtras(bundle);
									setResult(App.ADDRESS_CODE, intent);
									finish();
								}
								for (int i = 0; i < jArrData.length(); i++) {
									JSONObject jobj2 = jArrData
											.optJSONObject(i);
									HashMap<String, String> map = new HashMap<String, String>();
									map.put("id", jobj2.optString("id"));
									map.put("code", jobj2.optString("code"));
									map.put("pcode", jobj2.optString("pcode"));
									map.put("name", jobj2.optString("name"));
									mylist.add(map);
								}
								adapter.notifyDataSetChanged();

							} else {
								showToast("获取数据失败");
							}
						} catch (JSONException e) {
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("操作失败,请稍后再试！");
						disDialog();
					}
				});
	}
}
