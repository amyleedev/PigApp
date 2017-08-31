package com.szmy.pigapp.pds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szmy.pigapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyGridAdapter extends BaseAdapter {
	private List<Object> list = new ArrayList<Object>();
	private LayoutInflater inflater;
	public MyGridAdapter(Context ctx, List<Object> list){
		this.list = list;
		inflater = LayoutInflater.from(ctx);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.grid_view_item, null);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.textView);
		Object obj = getItem(position);
		if (obj instanceof Group) {
			textView.setText(((Group) obj).getName());
		} else if (obj instanceof City) {
			textView.setText(((City) obj).getName());
		}
		return convertView;
	}

}
