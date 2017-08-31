package com.szmy.pigapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.IntegralEntry;

import java.util.List;

public class IntegralAdapter extends BaseAdapter {
	private List<IntegralEntry> list;
	private LayoutInflater mInflater;
	private Context ctx;

	public IntegralAdapter(Context ctx, List<IntegralEntry> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public IntegralEntry getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_integral_list, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final IntegralEntry entry = getItem(arg0);
		holder.tvName.setText(entry.getUserName());
		holder.tvTime.setText(entry.getCreateTime().substring(0, 10));
		holder.tvPoint.setText("+"+entry.getIntegral().toString());
		if(entry.getIsLogin().equals("y")){
			holder.tvType.setText("已登录");
		}else{
			holder.tvType.setText("未登录");
		}
		
		if(!TextUtils.isEmpty(entry.getRemark())){
			holder.llRemark.setVisibility(View.VISIBLE);
			holder.tvRemark.setText("备注："+entry.getRemark());
		}else{
			holder.llRemark.setVisibility(View.GONE);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView tvName;
		TextView tvTime;
		TextView tvType;
		TextView tvPoint;
		LinearLayout llRemark;
		TextView tvRemark;
		

		public ViewHolder(View view) {
			tvName = (TextView) view.findViewById(R.id.name_text);
			tvTime = (TextView) view.findViewById(R.id.recommend_date_text);
			tvType = (TextView) view.findViewById(R.id.login_type_text);
			tvPoint = (TextView) view.findViewById(R.id.getpoint_text);
			llRemark = (LinearLayout) view.findViewById(R.id.remark);
			tvRemark = (TextView) view.findViewById(R.id.pointremark);
			view.setTag(this);
		}
	}
}
