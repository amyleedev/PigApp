package com.szmy.pigapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.szmy.pigapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChoseTypeActivity extends BaseActivity {
	private ListView mListviewPro;
	private String choiceProContent = "";
	private String choiceCityContent = "";
	private String choiceAreaContent = "";
	private SimpleAdapter adapter;
	private String type = "";
	private LinearLayout mLinearLayout;
	List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_area_selector);
		((TextView) findViewById(R.id.def_head_tv)).setText("请选择");
		mLinearLayout = (LinearLayout) findViewById(R.id.ll_choice);
		mLinearLayout.setVisibility(View.GONE);
		initView();
	}

	private void initView() {
		mListviewPro = (ListView) findViewById(R.id.lv_list);
		getData();
		adapter = new SimpleAdapter(this, data, R.layout.item_province,
				new String[] { "name" }, new int[] { R.id.text1 });
		mListviewPro.setAdapter(adapter);
		mListviewPro.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> maplist = (HashMap<String, String>) parent
						.getItemAtPosition(position);
				// showToast(maplist.get("type"));
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("pigType", maplist.get("type"));
				bundle.putString("pigTypeName", maplist.get("name"));
				intent.putExtras(bundle);
				setResult(23, intent);
				finish();
			}
		});
	}

	public void getData() {
		HashMap<String, String> maplist1 = new HashMap<String, String>();
		maplist1.put("type", "1");
		maplist1.put("name", "内三元");
		data.add(maplist1);
		HashMap<String, String> maplist2 = new HashMap<String, String>();
		maplist2.put("type", "2");
		maplist2.put("name", "外三元");
		data.add(maplist2);
		HashMap<String, String> maplist3 = new HashMap<String, String>();
		maplist3.put("type", "3");
		maplist3.put("name", "土杂猪");
		data.add(maplist3);
//		HashMap<String, String> maplist4 = new HashMap<String, String>();
//		maplist4.put("type", "4");
//		maplist4.put("name", "肥猪");
//		data.add(maplist4);
//		HashMap<String, String> maplist5 = new HashMap<String, String>();
//		maplist5.put("type", "5");
//		maplist5.put("name", "仔猪");
//		data.add(maplist5);
//		HashMap<String, String> maplist6 = new HashMap<String, String>();
//		maplist6.put("type", "6");
//		maplist6.put("name", "种猪");
//		data.add(maplist6);
	}
}
