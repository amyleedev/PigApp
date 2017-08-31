package com.szmy.pigapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.AgentEntry;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.UrlEntry;

import java.util.List;

public class AgentAdapter extends BaseAdapter {
	private List<AgentEntry> list;
	private LayoutInflater mInflater;
	private Context ctx;

	public AgentAdapter(Context ctx, List<AgentEntry> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public AgentEntry getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_broker, null);
			new ViewHolder(convertView);
		}

		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final AgentEntry entry = getItem(arg0);
		// holder.tvName.setText(entry.getRealName());
		// holder.tvBusiness.setText(entry.getBusiness());
		// holder.tvIntro.setText(entry.getIntro());
		// holder.tvCompanyName.setText(entry.getCompanyName());
		// String str = entry.getPhone();
		// if(str.length()>=11){
		// str = str.substring(0, 11);
		// holder.tvPhone.setText(str.substring(0,str.length()-(str.substring(3)).length())+"****"+str.substring(7));
		// }else{
		// holder.tvPhone.setText(str.substring(0, str.length())+"***");
		// }
		// holder.tvQQ.setText(entry.getQQ());
		// holder.tvAddress.setText(entry.getProvince() + entry.getCity()
		// + entry.getArea());
		if(TextUtils.isEmpty(entry.getRealName())){
			holder.name.setText(entry.getCompanyName());
		}else {
			holder.name.setText(entry.getRealName());
		}
		holder.address.setText(entry.getProvince() + " " + entry.getCity()
				+ " " + entry.getArea());
		holder.business.setText(entry.getBusiness());
		if (entry.getIntro().equals("")) {
			holder.intro.setText("暂无介绍");
		} else {
			holder.intro.setText(entry.getIntro());
		}
		if (!TextUtils.isEmpty(entry.getPicture())) {
			com.nostra13.universalimageloader.core.ImageLoader.getInstance()
					.displayImage(UrlEntry.ip + entry.getPicture(),
							holder.picture, AppStaticUtil.getOptions());
		} else {
			holder.picture.setImageResource(R.drawable.photo);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView tvName;
		// TextView tvType;
		TextView tvStar;
		TextView tvBusiness;
		TextView tvIntro;
		TextView tvCompanyName;
		TextView tvPhone;
		TextView tvQQ;
		TextView tvAddress;

		TextView name;
		TextView address;
		TextView business;
		TextView intro;

		ImageView picture;

		public ViewHolder(View view) {
			// tvName = (TextView) view.findViewById(R.id.tv_name);
			// // tvType = (TextView) view.findViewById(R.id.tv_type);
			// tvStar = (TextView) view.findViewById(R.id.tv_star);
			// tvBusiness = (TextView) view.findViewById(R.id.tv_business);
			// tvIntro = (TextView) view.findViewById(R.id.tv_intro);
			// tvCompanyName = (TextView)
			// view.findViewById(R.id.tv_company_name);
			// tvPhone = (TextView) view.findViewById(R.id.tv_phone);
			// tvQQ = (TextView) view.findViewById(R.id.tv_qq);
			// tvAddress = (TextView) view.findViewById(R.id.tv_address);

			name = (TextView) view.findViewById(R.id.broker_name);
			address = (TextView) view.findViewById(R.id.broker_address);
			business = (TextView) view.findViewById(R.id.broker_business);
			intro = (TextView) view.findViewById(R.id.broker_intro);
			picture = (ImageView) view.findViewById(R.id.imageview_broker);
			view.setTag(this);
		}
	}
}
