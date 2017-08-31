package com.szmy.pigapp.pigactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.PigPrice;

import java.text.DecimalFormat;
import java.util.List;

public class PigPriceAdapter extends BaseAdapter {
	private List<PigPrice> list;
	private LayoutInflater mInflater;
	private Context ctx;

	public PigPriceAdapter(Context ctx, List<PigPrice> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public PigPrice getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_pigprice_list, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final PigPrice price = getItem(arg0);
		holder.tvArea.setText(price.getArea());
		holder.tvType.setText(price.getType()+"_"+price.getProductType());
		DecimalFormat df = new DecimalFormat("0.00");
		holder.tvPrice.setText(df.format(Double.parseDouble(price.getPrice()))+getPriceType(price.getType()));
		
	
		return convertView;
	}

	private String getPriceType(String type){
		String pricetype = "";
		if(type.equals("玉米")||type.equals("豆粕")){
			pricetype = "元/吨";
		}
		if(type.equals("生猪")){
			pricetype = "元/公斤"; 
		}
		return pricetype;
	}
	

	

	private class ViewHolder {
		TextView tvArea;
		TextView tvType;
		TextView tvPrice;		
		public ViewHolder(View view) {
			tvArea = (TextView) view.findViewById(R.id.area);
			tvType = (TextView) view.findViewById(R.id.type);
			tvPrice = (TextView) view.findViewById(R.id.price);
			
			view.setTag(this);
		}
	}
}
