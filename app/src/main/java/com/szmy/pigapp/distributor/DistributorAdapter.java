package com.szmy.pigapp.distributor;//package com.szmy.pigapp.distributor;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.lidroid.xutils.BitmapUtils;
//import com.szmy.pigapp.R;
//import com.szmy.pigapp.entity.ZhuChang;
//import com.szmy.pigapp.utils.AppStaticUtil;
//import com.szmy.pigapp.utils.UrlEntry;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//public class DistributorAdapter extends BaseAdapter {
//	private List<Distributor> list;
//	private LayoutInflater mInflater;
//	private Context ctx;
//	private BitmapUtils bitmapUtils;
//	private Distributor info;
//
//
//	public DistributorAdapter(Context ctx, List<Distributor> list) {
//		this.ctx = ctx;
//		mInflater = LayoutInflater.from(ctx);
//		this.list = list;
//		bitmapUtils = new BitmapUtils(ctx);
//
//	}
//
//
//
//	@Override
//	public int getCount() {
//		return list.size();
//	}
//
//	@Override
//	public Distributor getItem(int arg0) {
//		return list.get(arg0);
//	}
//
//	@Override
//	public long getItemId(int arg0) {
//		return arg0;
//	}
//
//	@Override
//	public View getView(int arg0, View convertView, ViewGroup arg2) {
//		if (convertView == null) {
//			convertView = mInflater.inflate(R.layout.item_farm, null);
//			new ViewHolder(convertView);
//		}
//		final ViewHolder holder = (ViewHolder) convertView.getTag();
//		info = getItem(arg0);
//
//		String name = info.getName();
//		if (name.indexOf("养") != -1) {
//			name = "***" + name.substring(name.indexOf("养"));
//		} else if (name.indexOf("猪") != -1) {
//			name = "***" + name.substring(name.indexOf("猪"));
//		} else {
//			name = "***" + name;
//		}
//		holder.farm_name.setText(name);
//
//		String address=farm.getProvince() +" "+ farm.getCity()+" "+farm.getArea();
//		if (address.equals("")) {
//			holder.farm_address.setText("暂无");
//		} else {
//			holder.farm_address.setText(address);
//		}
//
//		String pigtype=pigType(farm.getPigType());
//		if (pigtype.equals("")) {
//			holder.farm_pigtype.setText("暂无");
//		} else {
//			holder.farm_pigtype.setText(pigtype);
//		}
//
//		String type=farmType(farm.getType());
//		if (type.equals("")) {
//			holder.farm_type.setText("暂无");
//		} else {
//			holder.farm_type.setText(type);
//		}
//		if (farm.getAmount().equals("")) {
//			holder.farm_number.setText("存栏数:"+"暂无");
//		} else {
//			holder.farm_number.setText("存栏数:"+farm.getAmount()+"头");
//		}
//
//		if (!TextUtils.isEmpty(farm.getCoverPicture())) {
//			com.nostra13.universalimageloader.core.ImageLoader.getInstance()
//					.displayImage(UrlEntry.ip + farm.getCoverPicture(),
//							holder.imageview_farm, AppStaticUtil.getOptions());
//		} else {
//			holder.imageview_farm.setImageResource(R.drawable.default_title);
//		}
//		String distance=farm.getDistance();
//		if (distance.equals("50000")) {
//			holder.farm_distance.setText("");
//		} else {
//			double Distance=Double.parseDouble(distance);
//			if (Distance>=1000) {
//				Distance=Distance/1000;
//				BigDecimal b =new BigDecimal(Distance);
//				Distance = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
//				holder.farm_distance.setText(Distance+"km");
//			}else {
//				holder.farm_distance.setText(Distance+"m");
//			}
//
//		}
//
////		holder.tvName.setText(name);
////		holder.tvType.setText(farmType(farm.getType()));
////		holder.tvAmount.setText(farm.getAmount());
////		holder.tvSales.setText(farm.getSales());
////		holder.tvPigType.setText(pigType(farm.getPigType()));
////		holder.tvUsername.setText(farm.getUsername());
////		String str = farm.getPhone();
////		if (str.length() >= 11) {
////			str=str.substring(0, 11);
////			holder.tvPhone.setText(str.substring(0,
////					str.length() - (str.substring(3)).length())
////					+ "****" + str.substring(7));
////		} else {
////
////			holder.tvPhone.setText(str.substring(0, str.length())+"***");
////		}
////		holder.tvEmail.setText(farm.getEmail());
////		holder.tvAddress.setText(farm.getProvince() + farm.getCity()
////				+ farm.getArea() + "*****");
//		return convertView;
//	}
//
//	private String farmType(String type) {
//		if (TextUtils.isEmpty(type))
//			return "";
//		if (type.equals("1")) {
//			return "小规模养殖";
//		} else if (type.equals("2"))
//			return "中规模养殖";
//		else if (type.equals("3"))
//			return "大规模养殖";
//		else if (type.equals("4"))
//			return "集团规模养殖";
//		return type;
//	}
//
//	private String pigType(String type) {
//		if (TextUtils.isEmpty(type))
//			return "";
//		if (type.equals("1")) {
//			return "内三元";
//		} else if (type.equals("2")) {
//			return "外三元";
//		} else if (type.equals("3")) {
//			return "土杂猪";
//		}else if (type.equals("4")) {
//			return "肥猪";
//		}else if (type.equals("5")) {
//			return "仔猪";
//		}else if (type.equals("6")) {
//			return "种猪";
//		}
//		return type;
//	}
//
//
//
//	private class ViewHolder {
//		TextView tvName;
//		TextView tvType;
//		TextView tvAmount;
//		TextView tvSales;
//		TextView tvPigType;
//		TextView tvUsername;
//		TextView tvPhone;
//		TextView tvEmail;
//		TextView tvAddress;
//
//		ImageView imageview_farm;
//		TextView farm_name,farm_address,farm_pigtype,farm_type,farm_number,farm_distance;
//		public ViewHolder(View view) {
////			tvName = (TextView) view.findViewById(R.id.tv_name);
////			tvType = (TextView) view.findViewById(R.id.tv_type);
////			tvAmount = (TextView) view.findViewById(R.id.tv_amount);
////			tvSales = (TextView) view.findViewById(R.id.tv_sales);
////			tvPigType = (TextView) view.findViewById(R.id.tv_pig_type);
////			tvUsername = (TextView) view.findViewById(R.id.tv_username);
////			tvPhone = (TextView) view.findViewById(R.id.tv_phone);
////			tvEmail = (TextView) view.findViewById(R.id.tv_email);
////			tvAddress = (TextView) view.findViewById(R.id.tv_address);
//
//
//			farm_name = (TextView) view.findViewById(R.id.farm_name);
//			farm_address = (TextView) view.findViewById(R.id.farm_address);
//			farm_pigtype = (TextView) view.findViewById(R.id.farm_pigtype);
//			farm_type = (TextView) view.findViewById(R.id.farm_type);
//			farm_number = (TextView) view.findViewById(R.id.farm_number);
//			farm_distance = (TextView) view.findViewById(R.id.farm_distance);
//			imageview_farm = (ImageView) view.findViewById(R.id.imageview_farm);
//			view.setTag(this);
//		}
//	}
//}
