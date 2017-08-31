package com.szmy.pigapp.pds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.szmy.pigapp.R;

import java.util.List;

public class MyAdapter extends BaseExpandableListAdapter {
	private Context ctx;
	private List<Group> groupList;
	LayoutInflater inflater;
	
	public MyAdapter(Context ctx, List<Group> groupList){
		this.ctx = ctx;
		this.groupList = groupList;
		inflater = LayoutInflater.from(ctx);
	}

	@Override
	public int getGroupCount() {
		return groupList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groupList.get(groupPosition).getCityList().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groupList.get(groupPosition).getCityList().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		final Group group = (Group) getGroup(groupPosition);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.group_item, null);
		}
		TextView groupText = (TextView) convertView.findViewById(R.id.groupText);
		CheckBox groupCheckBox = (CheckBox) convertView.findViewById(R.id.groupCheckBox);
		ImageView arrowCheckBox = (ImageView) convertView.findViewById(R.id.arrow);
		if(isExpanded){
			arrowCheckBox.setBackgroundResource(R.drawable.group_arrow_open);
		}else{
			arrowCheckBox.setBackgroundResource(R.drawable.group_arrow_close);
		}
		groupText.setText(group.getName().split("#")[1]);
		groupCheckBox.setChecked(group.isChecked());
		groupCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				group.changeChecked();
				notifyDataSetChanged();
				((ChoseErBiao)ctx).reFlashGridView();
			}
		});
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		City city = (City) getChild(groupPosition, childPosition);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.child_item, null);
		}
		TextView childText = (TextView) convertView.findViewById(R.id.childText);
		CheckBox childCheckBox = (CheckBox) convertView.findViewById(R.id.childCheckBox);
		
			 
		childText.setText(city.getName().split("_")[1]);
		childCheckBox.setChecked(city.isChecked());
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
