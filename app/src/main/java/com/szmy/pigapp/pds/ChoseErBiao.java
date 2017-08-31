package com.szmy.pigapp.pds;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChoseErBiao extends BaseActivity {
	private ExpandableListView expandableListView;
	private List<Group> groupList = new ArrayList<Group>();
	private MyAdapter adapter;
	private MyGridAdapter gridAdapter;
	private List<Object> gridList = new ArrayList<Object>();
	private Button mBtnOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chose_erbiao_activity);

		setUpViews();
		setUpListeners();
		init();
	}

	private void setUpViews() {
		((TextView)findViewById(R.id.def_head_tv)).setText("选择耳标号");
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		mBtnOk = (Button) findViewById(R.id.ok_count);
		mBtnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(gridList.size()!=0){
					Gson gson = new Gson();
					String str = gson.toJson(groupList);
					System.out.println("grouplist" + str);
					NewJYSBActivity.SavaData(str);
					
					Intent intent = new Intent();
					Bundle b = new Bundle();
					
					String strlist = gson.toJson(gridList);
					b.putString("list", strlist);
					
					b.putInt("count", gridList.size());
					intent.putExtras(b);
					setResult(NewJYSBActivity.EBH_ID,intent);
				}
				finish();
			}
		});
	}

	private void setUpListeners() {
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				City city = groupList.get(groupPosition).getCityList()
						.get(childPosition);
				city.changeChecked();
				adapter.notifyDataSetChanged();
				reFlashGridView();
				return false;
			}
		});
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//
//				Object obj = gridList.get(position);
//				if (obj instanceof Group) {
//					((Group) obj).changeChecked();
//				} else if (obj instanceof City) {
//					((City) obj).changeChecked();
//				}
//				adapter.notifyDataSetChanged();
//				reFlashGridView();
//			}
//		});
	}

	private void init() {
		initData();
		adapter = new MyAdapter(this, groupList);
		expandableListView.setAdapter(adapter);

//		gridAdapter = new MyGridAdapter(this, gridList);
//		gridView.setAdapter(gridAdapter);
		reFlashGridView();
	}

	public void reFlashGridView() {
		gridList.clear();

		//
		for (Group group : groupList) {
			// if (group.isChecked()) {
			// gridList.add(group);
			//
			// } else {
			for (City city : group.getCityList()) {
				if (city.isChecked()) {
					gridList.add(city);
				}
			}
			// }
		}
		mBtnOk.setText(" 完成("+gridList.size()+") ");
		
//		gridAdapter.notifyDataSetChanged();
	}

	private void initData() {
		On_showData();
	
	}

	

	public void On_showData() {
		groupList.clear();
		SharedPreferences sharedPreferences = getSharedPreferences("jysb",
				Context.MODE_PRIVATE);
		String jsondata = sharedPreferences.getString("list", "null");
		System.out.println("jysb"+jsondata);
		List<Group> list = new ArrayList<Group>();
		if (!jsondata.equals("null")) {

			try {
				JSONArray jArrData = new JSONArray(jsondata);
				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj1 = jArrData.optJSONObject(i);
					Group group = new Group();
					group.setChecked(jobj1.optBoolean("isChecked"));
					group.setName(jobj1.optString("name"));
//					String id = "";
//					if(jobj1.optString("name").contains("#")){
//						 id = jobj1.optString("name").split("#")[0];
//					}
					List<City> cityList = new ArrayList<City>();
					JSONArray jArrData2 = jobj1.getJSONArray("cityList");
					for (int j = 0; j < jArrData2.length(); j++) {
						JSONObject jobj2 = jArrData2.optJSONObject(j);
						City city = new City();
						
						city.setName(jobj2.optString("name"));
						city.setChecked(jobj2.optBoolean("isChecked"));
						cityList.add(city);
						city.addObserver(group);
						group.addObserver(city);
					}
					group.setCityList(cityList);
					groupList.add(group);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	
	

}
