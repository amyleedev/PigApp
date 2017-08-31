package com.szmy.pigapp.tixian;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szmy.pigapp.R;

import java.util.List;

public class WithdrawalsAdapter extends BaseAdapter {
	private List<Withdrawals> list;
	private LayoutInflater mInflater;
	private Context ctx;

	public WithdrawalsAdapter(Context ctx, List<Withdrawals> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Withdrawals getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_withdrawals, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final Withdrawals entry = getItem(arg0);
		holder.tvName.setText(entry.getName());
		holder.tvType.setText(TextUtils.isEmpty(entry.getBankName())?getType(entry.getType()):entry.getBankName());
		holder.tvCardNum.setText(entry.getBankNum());

		return convertView;
	}

	private String getType(String type) {
		String t = "";
		if(type.equals("nyBank")||type.equals("nybank")){
			t = "农业银行卡";
		}else if(type.equals("zfb")){
			t = "支付宝";
		}else if(type.equals("wxzf")){
			t = "微信账号";
		}
			


		return t;
	}

	private class ViewHolder {
		TextView tvName;
		TextView tvType;
		TextView tvCardNum;

		public ViewHolder(View view) {
			tvName = (TextView) view.findViewById(R.id.tv_name);
			tvType = (TextView) view.findViewById(R.id.tv_type);
			tvCardNum = (TextView) view.findViewById(R.id.tv_cardnum);

			view.setTag(this);
		}
	}
}
