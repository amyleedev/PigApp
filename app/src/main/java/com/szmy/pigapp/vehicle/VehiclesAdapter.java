package com.szmy.pigapp.vehicle;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.UrlEntry;

import java.util.List;

public class VehiclesAdapter extends BaseAdapter {
	private List<Vehicle> list;
	private LayoutInflater mInflater;
	private Context ctx;
	private BitmapUtils bitmapUtils;

	public VehiclesAdapter(Context ctx, List<Vehicle> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
		bitmapUtils = new BitmapUtils(ctx);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Vehicle getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_vehicle_list, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final Vehicle weh = getItem(arg0);
		//
		holder.tvCarNumber.setText(weh.getCarNum());
		holder.tvType.setText(weh.getType().toString());
		holder.tvAmount.setText("可装生猪" + weh.getCapacity().toString() + "头");
		if (weh.getCarRadius().equals("0")) {
			holder.tvRadius.setText("服务半径：不限");
		} else {
			holder.tvRadius.setText("服务半径："+weh.getCarRadius()+"公里");
		}
		
		if(!TextUtils.isEmpty(weh.getPigType())){
			holder.tvAmount.setText("可装"+ weh.getPigType() + weh.getCapacity().toString() + "头");
		}
		holder.tvAddress.setText("【" + weh.getProvince() + "】");
		holder.tvPrice.setText(weh.getPrice().toString() + "元/公里");
		if (weh.getIcon().toString().equals("")) {
			holder.ivImage.setImageResource(R.drawable.wutu);
		} else {
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.wutu);
			bitmapUtils.display(holder.ivImage, UrlEntry.ip
					+ weh.getIcon().toString());// 下载并显示图片
		}
		return convertView;
	}

	private class ViewHolder {
		ImageView ivImage;
		TextView tvPrice;
		TextView tvCarNumber;
		TextView tvType;
		TextView tvAmount;
		TextView tvAddress;
		TextView tvRadius;
		
		public ViewHolder(View view) {
			ivImage = (ImageView) view.findViewById(R.id.imagehead);
			tvPrice = (TextView) view.findViewById(R.id.vehicle_price_text);
			tvCarNumber=(TextView) view.findViewById(R.id.tv_car_number);
			tvType = (TextView) view.findViewById(R.id.vehicle_type_text);
			tvAmount = (TextView) view.findViewById(R.id.vehicle_count_text);
			tvAddress = (TextView) view
					.findViewById(R.id.company_vertical_address_text);
			tvRadius= (TextView) view
					.findViewById(R.id.company_vertical_radius);
			view.setTag(this);
		}
	}
}
