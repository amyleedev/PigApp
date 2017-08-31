package com.szmy.pigapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
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
import com.szmy.pigapp.fragment.MainFragment;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AddressChoose extends BaseActivity {
	private ListView mListView1;
	private ListView mListView2;
	private ListView mListView3;
	private LinearLayout mLinearLayout1;
	private LinearLayout mLinearLayout2;
	private LinearLayout mLinearLayout3;
	private LinearLayout onBackLinear;
	private TextView topTxt;
	private TextView shengTxt2;
	private TextView shengTxt3, shiTxt3, topView3;
	private String shengStr = "", shiStr = "", quStr = "", shengcode = "", shicode = "", qucode = "";
	private String mainlocation = "";
	private SimpleAdapter adapter;
	private String type = "";
	// ��Ӧ������ݿ��������
	private ArrayList<HashMap<String, String>> mylist ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.my_set_addresschoose);
		((TextView)findViewById(R.id.def_head_tv)).setText("选择区域");
		mylist = new ArrayList<HashMap<String, String>>();
		if (getIntent().hasExtra("mainlocation")) {
			mainlocation = getIntent().getStringExtra("mainlocation");
		}
		mLinearLayout1 = (LinearLayout) this
				.findViewById(R.id.my_set_adresschoose_1);
		mLinearLayout2 = (LinearLayout) this
				.findViewById(R.id.my_set_adresschoose_2);
		mLinearLayout3 = (LinearLayout) this
				.findViewById(R.id.my_set_adresschoose_3);
		mLinearLayout1.setVisibility(View.VISIBLE);
		mLinearLayout2.setVisibility(View.GONE);
		mLinearLayout3.setVisibility(View.GONE);
		shengTxt2 = (TextView) this
				.findViewById(R.id.my_set_adresschoose_sheng_2);
		shengTxt3 = (TextView) this
				.findViewById(R.id.my_set_adresschoose_sheng_3);
		shiTxt3 = (TextView) this.findViewById(R.id.my_set_adresschoose_shi_3);
		topView3 = (TextView) this
				.findViewById(R.id.my_set_adresschoose_textview_3);
		mListView1 = (ListView) this
				.findViewById(R.id.my_set_adresschoose_listview_1);
		mListView2 = (ListView) this
				.findViewById(R.id.my_set_adresschoose_listview_2);
		mListView3 = (ListView) this
				.findViewById(R.id.my_set_adresschoose_listview_3);
		shengTxt2.setOnClickListener(click);
		shengTxt3.setOnClickListener(click);
		shiTxt3.setOnClickListener(click);
		// ListItem
				adapter = new SimpleAdapter(this, mylist, R.layout.item_province,
						new String[] { "name" }, new int[] { R.id.text1 });
				mListView1.setAdapter(adapter);
				sendDataToHttp(adapter, "0");
		mListView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long id) {
				 shengcode = mylist.get(position).get("code");
				shengStr = mylist.get(position).get("name");
					sendDataToHttp(adapter, shengcode);
					mLinearLayout1.setVisibility(View.GONE);
					mLinearLayout2.setVisibility(View.VISIBLE);
					mLinearLayout3.setVisibility(View.GONE);
					mListView2.setAdapter(adapter);
					shengTxt2.setText(shengStr);
			}
		});

		mListView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long id) {
				shengTxt3.setText(shengStr);
				shicode = mylist.get(position).get("code");
				shiStr = mylist.get(position).get("name");
				shiTxt3.setText(shiStr);
				sendDataToHttp(adapter, shicode);
                mLinearLayout1.setVisibility(View.GONE);
				mLinearLayout2.setVisibility(View.GONE);
				mLinearLayout3.setVisibility(View.VISIBLE);
				mListView3.setVisibility(View.VISIBLE);
				topView3.setText("请选择  县区/其他...");
				mListView3.setAdapter(adapter);
			}
		});

		mListView3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long id) {
				 qucode = mylist.get(position).get("code");
				quStr = mylist.get(position).get("name");
				sendDataToHttp(adapter, qucode);										
				shengTxt2.setText(shengStr);	
			}
		});
	}

	OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			switch (v.getId()) {
//			case R.id.my_set_adresschoose_sheng_2:
//				mLinearLayout1.setVisibility(View.VISIBLE);
//				mLinearLayout2.setVisibility(View.GONE);
//				mLinearLayout3.setVisibility(View.GONE);
//				break;
//			case R.id.my_set_adresschoose_sheng_3:
//				mLinearLayout1.setVisibility(View.VISIBLE);
//				mLinearLayout2.setVisibility(View.GONE);
//				mLinearLayout3.setVisibility(View.GONE);
//				break;
//			case R.id.my_set_adresschoose_shi_3:
//				mLinearLayout1.setVisibility(View.GONE);
//				mLinearLayout2.setVisibility(View.VISIBLE);
//				mLinearLayout3.setVisibility(View.GONE);
//				break;
//
//			default:
//				break;
//			}
		}
	};

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
								} else {
									System.out.println(quStr+"ffdfgfgdg");
									if(!mainlocation.equals(""))
									{
										Intent intent = new Intent();
										Bundle bundle = new Bundle();
										bundle.putString("province", shengStr);
										bundle.putString("city", shiStr);
										bundle.putString("area", quStr);
										intent.putExtras(bundle);
										setResult(MainFragment.LOCATION_ID, intent);
									}else{
										Intent intent = new Intent();
										Bundle bundle = new Bundle();
										bundle.putString("shengcode", shengcode);
										bundle.putString("qucode", qucode);
										bundle.putString("shicode", shicode);
										bundle.putString("province", shengStr);
										bundle.putString("city", shiStr);
										bundle.putString("area", quStr);
										intent.putExtras(bundle);
										setResult(NewAddress.LOCATION_ID, intent);
									}
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
