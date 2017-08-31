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

public class ChoseDealTypeActivity extends BaseActivity  {
	private ListView mListviewPro;
    private SimpleAdapter adapter;
    private String type ="";
    private LinearLayout mLinearLayout;
    List<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();   
	
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
		adapter = new SimpleAdapter(this, data, R.layout.item_province, new String[]{"name"}, new int[]{R.id.text1}); 
		mListviewPro.setAdapter(adapter);
		mListviewPro.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				 HashMap<String,String> maplist = (HashMap<String, String>) parent.getItemAtPosition(position);
//				 showToast(maplist.get("type"));
				 Intent intent = new Intent();
	             Bundle bundle=new Bundle();
	             bundle.putString("orderType", maplist.get("type")); 
	             bundle.putString("orderTypeName", maplist.get("name")); 
	             intent.putExtras(bundle);
	             setResult(24,intent);
	             finish();
			}
		});
	}

	public void getData(){
		  HashMap<String,String> maplist1 = new HashMap<String, String>();
		  maplist1.put("type", "1");
		  maplist1.put("name", "出售");
		  data.add(maplist1);
		  HashMap<String,String> maplist2 = new HashMap<String, String>();
		  maplist2.put("type", "2");
		  maplist2.put("name", "收购");
		  data.add(maplist2);
	}
}
