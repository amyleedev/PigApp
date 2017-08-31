package com.szmy.pigapp.pds;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szmy.pigapp.R;

import java.util.List;

public class PDSPigFarmAdapter extends BaseAdapter {
	private List<PDSPigFarm> list;
	private LayoutInflater mInflater;
	private Context ctx;

	public PDSPigFarmAdapter(Context ctx, List<PDSPigFarm> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public PDSPigFarm getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_pdspigfarm, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final PDSPigFarm qo = getItem(arg0);
		holder.tvName.setText("编号:"+qo.getCode());
		holder.tvType.setText(qo.getHuozhu());
		holder.tvAmount.setText(qo.getNum()+"头");
		holder.tvSales.setText(qo.getPjtz());
		holder.tvPigType.setText(qo.getSort());
		String str = qo.getTel();
		holder.tvPhone.setText(str);
//		if (str.length() >= 11) {
//			str=str.substring(0, 11);
//			holder.tvPhone.setText(str.substring(0,
//					str.length() - (str.substring(3)).length())
//					+ "****" + str.substring(7));
//		} else {
//			
//			holder.tvPhone.setText(str.substring(0, str.length())+"***");
//		}
		holder.tvTime.setText(qo.getSigndate().substring(0, 16));
		holder.tvAddress.setText(qo.getArea());
		return convertView;
	}

	
	

	private class ViewHolder {
		TextView tvName;
		TextView tvType;
		TextView tvAmount;
		TextView tvSales;
		TextView tvPigType;
		TextView tvTime;
		TextView tvPhone;
		TextView tvAddress;

		public ViewHolder(View view) {
			tvName = (TextView) view.findViewById(R.id.tv_name);
			tvType = (TextView) view.findViewById(R.id.tv_type);
			tvAmount = (TextView) view.findViewById(R.id.tv_amount);
			tvSales = (TextView) view.findViewById(R.id.tv_sales);
			tvPigType = (TextView) view.findViewById(R.id.tv_pig_type);
			tvTime = (TextView) view.findViewById(R.id.tv_time);
			tvPhone = (TextView) view.findViewById(R.id.tv_phone);
			tvAddress = (TextView) view.findViewById(R.id.tv_address);
			view.setTag(this);
		}
	}
}
