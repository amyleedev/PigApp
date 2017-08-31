package com.szmy.pigapp.pigdiagnosis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szmy.pigapp.R;

import java.util.List;

public class ZhenDuanAdapter extends BaseAdapter {
	private List<ZhenDuan> list;
	private LayoutInflater mInflater;
	private Context ctx;

	public ZhenDuanAdapter(Context ctx, List<ZhenDuan> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ZhenDuan getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_textview_item, null);
			new ViewHolder(convertView);
		}
//		convertView.setBackgroundResource(R.drawable.shape_btn_selector2);
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final ZhenDuan entry = getItem(arg0);
		holder.tvName.setText(entry.getName());
		return convertView;
	}

	private class ViewHolder {
		TextView tvName;


		public ViewHolder(View view) {
			tvName = (TextView) view.findViewById(R.id.text);

			view.setTag(this);
		}
	}
}
