package com.szmy.pigapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class IndustryActivity extends BaseActivity {
	private ListView mListviewPro;
	private SimpleAdapter adapter;
	private String type = "";
	private LinearLayout mLinearLayout;
	List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	String[] str ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_area_selector);
		((TextView) findViewById(R.id.def_head_tv)).setText("请选择");
		mLinearLayout = (LinearLayout) findViewById(R.id.ll_choice);
		mLinearLayout.setVisibility(View.GONE);
		mListviewPro = (ListView) findViewById(R.id.lv_list);
		getData();
	}

	private void initView() {
		adapter = new SimpleAdapter(this, data, R.layout.item_province,
				new String[]{"name"}, new int[] { R.id.text1 });
		mListviewPro.setAdapter(adapter);
		mListviewPro.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> maplist = (HashMap<String, String>) parent
						.getItemAtPosition(position);
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("industry", maplist.get("name"));
				intent.putExtras(bundle);
				setResult(23, intent);
				finish();
			}
		});
	}

	public void getData() {
		RequestParams params = new RequestParams();
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_INDUSTRY_URL,
				params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						showToast(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Log.i("industry", responseInfo.result);
						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								String liststr = jsonresult.get("list").toString();
								liststr = liststr.replace("[", "");
								liststr = liststr.replace("]", "");
								String[] arr = liststr.replace("\"", "").split(",");
								List<String> list = Arrays.asList(arr);
								for(int i=0;i<list.size();i++){
									 HashMap<String,String> maplist1 = new HashMap<String, String>();
									 maplist1.put("name", list.get(i));
									 data.add(maplist1);
								}
								initView();
							} else if (jsonresult.get("success").toString()
									.equals("4")) {
							} else {
								successType(jsonresult.get("success")
										.toString(), "操作失败！");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		// HashMap<String,String> maplist1 = new HashMap<String, String>();
		// maplist1.put("type", "1");
		// maplist1.put("name", "内三元");
		// data.add(maplist1);
		// HashMap<String,String> maplist2 = new HashMap<String, String>();
		// maplist2.put("type", "2");
		// maplist2.put("name", "外三元");
		// data.add(maplist2);
		// HashMap<String,String> maplist3 = new HashMap<String, String>();
		// maplist3.put("type", "3");
		// maplist3.put("name", "土杂猪");
		// data.add(maplist3);
	}
}
