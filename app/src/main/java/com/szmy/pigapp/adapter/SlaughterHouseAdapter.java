package com.szmy.pigapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.SlaughterhouseEntry;
import com.szmy.pigapp.utils.UrlEntry;

import java.util.List;

public class SlaughterHouseAdapter extends BaseAdapter {
	private List<SlaughterhouseEntry> list;
	private LayoutInflater mInflater;
	private Context ctx;
	private BitmapUtils bitmapUtils;

	public SlaughterHouseAdapter(Context ctx, List<SlaughterhouseEntry> list) {
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
	public SlaughterhouseEntry getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.item_slaughterhouse, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final SlaughterhouseEntry entry = getItem(arg0);
		String name = entry.getCompanyName();
		
		if (name.indexOf("屠宰") != -1 && name.indexOf("屠宰") >= 1) {
			name = "****" + name.substring(name.indexOf("屠宰"));
		}
		if (name.indexOf("双汇") != -1 && name.indexOf("双汇") >= 1) {
			name = "****" + name.substring(name.indexOf("双汇"));
		}
		if (name.indexOf("有限公司") != -1 && name.indexOf("有限公司") >= 1) {
			name = "****" + name.substring(name.indexOf("有限公司"));
		}
		if (name.indexOf("加工厂") != -1 && name.indexOf("加工厂") >= 1) {
			name = "****" + name.substring(name.indexOf("加工厂"));
		}
		if (name.indexOf("肉联厂") != -1 && name.indexOf("肉联厂") >= 1) {
			name = "****" + name.substring(name.indexOf("肉联厂"));
		}
		if (name.indexOf("猪厂") != -1 && name.indexOf("猪厂") >= 1) {
			name = "****" + name.substring(name.indexOf("猪厂"));
		}
		if (name.indexOf("食品") != -1 && name.indexOf("食品") >= 1) {
			name = "****" + name.substring(name.indexOf("食品"));
		}
		
		holder.slaughterhouse_name.setText(name);
		String address=entry.getProvince()+" " + entry.getCity()+" "+ entry.getArea();
		if (address.equals("")) {
			holder.slaughterhouse_address.setText("暂无");
		} else {
		holder.slaughterhouse_address.setText(address);
		}
		
		if (entry.getBusiness().equals("")) {
			holder.slaughterhouse_business.setText("暂无");
		} else {
		holder.slaughterhouse_business.setText(entry.getBusiness());
		}
		
		if (entry.getIntro().equals("")) {
			holder.slaughterhouse_intro.setText("暂无");
		} else {
		holder.slaughterhouse_intro.setText(entry.getIntro());
		}
		
		
//		String distance=entry.getDistance();
//		if (distance.equals("50000")) {
//			holder.slaughterhouse_distance.setText("");
//		} else {
//			double Distance=Double.parseDouble(distance);
//			if (Distance>=1000) {
//				Distance=Distance/1000;
//				BigDecimal b =new BigDecimal(Distance);
//				Distance = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
//				holder.slaughterhouse_distance.setText(Distance+"km");
//			}else {
//				holder.slaughterhouse_distance.setText(Distance+"m");
//			}
//
//		}

//		holder.tvName.setText(name);
//		holder.tvNumber.setText(entry.getNumber() + "");
//		holder.tvBusiness.setText(entry.getBusiness());
//		holder.tvIntro.setText(entry.getIntro());
//		holder.tvUsername.setText(entry.getRealName());
//		holder.tvAddress.setText(entry.getProvince() + entry.getCity()
//				+ entry.getArea() + "******");
//		String str = entry.getPhone();
//		if (str.length() >= 11) {
//			str = str.substring(0, 11);
//			holder.tvPhone.setText(str.substring(0,
//					str.length() - (str.substring(3)).length())
//					+ "****" + str.substring(7));
//		} else {
//			
//			holder.tvPhone.setText(str.substring(0, str.length())+"***");
//		}
//		String qq = entry.getQQ();
//		if (qq.length() > 10) {
//			qq = qq.substring(0, 10);
//		}
//		holder.tvQQ.setText(qq);
		if (entry.getPicture().toString().equals("")) {
			holder.picture.setImageResource(R.drawable.default_title);
		} else {
			try {
				bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_title);
				bitmapUtils.display(holder.picture, UrlEntry.ip
						+ entry.getPicture().toString());// 下载并显示图片
			} catch (Exception ex) {
				holder.picture.setImageResource(R.drawable.default_title);
			}
		}
		
		return convertView;
	}

	private class ViewHolder {
		TextView tvName;
		ImageView imgHead;
		TextView tvNumber;
		TextView tvBusiness;
		TextView tvIntro;
		TextView tvUsername;
		TextView tvPhone;
		TextView tvQQ;
		TextView tvAddress;
		
		ImageView picture;
		TextView slaughterhouse_name,slaughterhouse_address,slaughterhouse_business;
		TextView slaughterhouse_intro,slaughterhouse_distance;

		public ViewHolder(View view) {
//			tvName = (TextView) view.findViewById(R.id.tv_name);
//			imgHead = (ImageView) view.findViewById(R.id.imagehead);
//			tvNumber = (TextView) view.findViewById(R.id.tv_number);
//			tvBusiness = (TextView) view.findViewById(R.id.tv_business);
//			tvIntro = (TextView) view.findViewById(R.id.tv_intro);
//			tvUsername = (TextView) view.findViewById(R.id.tv_username);
//			tvPhone = (TextView) view.findViewById(R.id.tv_phone);
//			tvQQ = (TextView) view.findViewById(R.id.tv_qq);
//			tvAddress = (TextView) view.findViewById(R.id.tv_address);
			picture=(ImageView) view.findViewById(R.id.imageview_slaughterhouse);
			slaughterhouse_name=(TextView) view.findViewById(R.id.slaughterhouse_name);
			slaughterhouse_address=(TextView) view.findViewById(R.id.slaughterhouse_address);
			slaughterhouse_business=(TextView) view.findViewById(R.id.slaughterhouse_business);
			slaughterhouse_intro=(TextView) view.findViewById(R.id.slaughterhouse_intro);
			slaughterhouse_distance=(TextView) view.findViewById(R.id.slaughterhouse_distance);
			view.setTag(this);
		}
	}
}
