package com.szmy.pigapp.pds;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szmy.pigapp.R;

import java.util.List;

public class QuarantineAdapter extends BaseAdapter {
	private List<QuarantineOfficer> list;
	private LayoutInflater mInflater;
	private Context ctx;

	public QuarantineAdapter(Context ctx, List<QuarantineOfficer> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public QuarantineOfficer getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_quarantine, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final QuarantineOfficer qo = getItem(arg0);
		holder.tvName.setText("编号:"+qo.getCode());
		holder.tvUser.setText(qo.getCl_user());
		holder.tvType.setText(qo.getHuozhu());
		holder.tvAmount.setText(qo.getNum() + "头");
		holder.tvSales.setText(qo.getPjtz());
		holder.tvPigType.setText(qo.getSort());
		String str = qo.getTel();
		holder.tvPhone.setText(str);
		int type = 3; 
		if(qo.getCl_result().equals("")||qo.getCl_result()==null){
			
		}else{
			type = Integer.parseInt(qo.getCl_result());
		}
		holder.tvReason.setText(getType(type)+","+qo.getCl_reason());
		if (!TextUtils.isEmpty(qo.getSbdate())) {
			holder.tvTime.setText(qo.getSbdate().substring(0, 16));
		}
		if (!TextUtils.isEmpty(qo.getCl_date())) {
			holder.tvClTime.setText(qo.getCl_date().substring(0, 16));
		}

		holder.tvAddress.setText(qo.getArea());
		return convertView;
	}

	private String getType(int type) {
		String t = "";
		switch (type) {
		case 0:
			t = "处理未通过";
			break;
		case 1:
			t = "处理通过";
			break;
		default:
			t = "未处理";
			break;
		}
		return t;

	}

	private class ViewHolder {
		TextView tvName;
		TextView tvType;
		TextView tvAmount;
		TextView tvSales;
		TextView tvPigType;
		TextView tvTime;
		TextView tvClTime;// 处理时间
		TextView tvPhone;
		TextView tvAddress;
		TextView tvReason;// 处理结果
		TextView tvUser;// 经办人

		public ViewHolder(View view) {
			tvName = (TextView) view.findViewById(R.id.tv_name);
			tvType = (TextView) view.findViewById(R.id.tv_type);
			tvAmount = (TextView) view.findViewById(R.id.tv_amount);
			tvSales = (TextView) view.findViewById(R.id.tv_sales);
			tvPigType = (TextView) view.findViewById(R.id.tv_pig_type);
			tvTime = (TextView) view.findViewById(R.id.tv_signdate);
			tvPhone = (TextView) view.findViewById(R.id.tv_phone);
			tvAddress = (TextView) view.findViewById(R.id.tv_address);
			tvClTime = (TextView) view.findViewById(R.id.tv_cltime);
			tvReason = (TextView) view.findViewById(R.id.tv_reason);
			tvUser = (TextView) view.findViewById(R.id.tv_user);
			view.setTag(this);
		}
	}
}
