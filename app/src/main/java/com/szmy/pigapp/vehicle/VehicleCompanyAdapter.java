package com.szmy.pigapp.vehicle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szmy.pigapp.R;

import java.math.BigDecimal;
import java.util.List;

public class VehicleCompanyAdapter extends BaseAdapter {
	private List<Company> list;
	private LayoutInflater mInflater;
	private Context ctx;

	public VehicleCompanyAdapter(Context ctx, List<Company> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Company getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_vehiclecompany_list,
					null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final Company com = getItem(arg0);
		holder.tvName.setText(com.getName().toString());
		holder.tvType.setText(com.getCarType().toString());
		holder.tvAmount.setText("车辆数：" + com.getCarNum().toString()+"辆");
		holder.tvAddress.setText("【" + com.getProvince() + "】");
//		holder.tvRadius.setText();
		String distance = com.getDistance();
		if (distance.equals("50000")) {
			holder.tvDistance.setText("");
		} else {
			double Distance = Double.parseDouble(distance);
			if (Distance >= 1000) {
				Distance = Distance / 1000;
				BigDecimal b = new BigDecimal(Distance);
				Distance = b.setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
				holder.tvDistance.setText("距离："+Distance + "km");
			} else {
				holder.tvDistance.setText("距离："+Distance + "m");
			}
		}
		return convertView;
	}

	private class ViewHolder {
		TextView tvName;
		TextView tvType;
		TextView tvAmount;
		TextView tvAddress;
		TextView tvRadius;
		TextView tvDistance;
		public ViewHolder(View view) {
			tvName = (TextView) view.findViewById(R.id.company_name_text);
			tvType = (TextView) view
					.findViewById(R.id.company_vehicle_type_text);
			tvAmount = (TextView) view
					.findViewById(R.id.company_vehicle_count_text);
			tvAddress = (TextView) view.findViewById(R.id.company_address_text);
			tvDistance = (TextView) view.findViewById(R.id.vehicle_price_text);
			view.setTag(this);
		}
	}
}
