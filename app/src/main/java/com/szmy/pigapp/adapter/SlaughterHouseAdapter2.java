package com.szmy.pigapp.adapter;

import android.content.Context;
import android.util.Log;
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

public class SlaughterHouseAdapter2 extends BaseAdapter {
	private List<SlaughterhouseEntry> list;
	private LayoutInflater mInflater;
	private Context ctx;
	private BitmapUtils bitmapUtils;

	public SlaughterHouseAdapter2(Context ctx, List<SlaughterhouseEntry> list) {
		this.ctx = ctx;
		mInflater = LayoutInflater.from(ctx);
		this.list = list;
		bitmapUtils = new BitmapUtils(ctx);
		Log.i("sixe",list.size()+"");
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
					.inflate(R.layout.item_slaughter, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();if (holder==null) return null;
		final SlaughterhouseEntry entry = getItem(arg0);
		String name = entry.getCompanyName();
		
//		if (name.indexOf("屠宰") != -1 && name.indexOf("屠宰") >= 1) {
//			name = "****" + name.substring(name.indexOf("屠宰"));
//		}
//		if (name.indexOf("双汇") != -1 && name.indexOf("双汇") >= 1) {
//			name = "****" + name.substring(name.indexOf("双汇"));
//		}
//		if (name.indexOf("有限公司") != -1 && name.indexOf("有限公司") >= 1) {
//			name = "****" + name.substring(name.indexOf("有限公司"));
//		}
//		if (name.indexOf("加工厂") != -1 && name.indexOf("加工厂") >= 1) {
//			name = "****" + name.substring(name.indexOf("加工厂"));
//		}
//		if (name.indexOf("肉联厂") != -1 && name.indexOf("肉联厂") >= 1) {
//			name = "****" + name.substring(name.indexOf("肉联厂"));
//		}
//		if (name.indexOf("猪厂") != -1 && name.indexOf("猪厂") >= 1) {
//			name = "****" + name.substring(name.indexOf("猪厂"));
//		}
//		if (name.indexOf("食品") != -1 && name.indexOf("食品") >= 1) {
//			name = "****" + name.substring(name.indexOf("食品"));
//		}



		holder.tvName.setText(name);
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

		ImageView picture;


		public ViewHolder(View view) {
			tvName = (TextView) view.findViewById(R.id.name);
//
			picture=(ImageView) view.findViewById(R.id.imageview);

			view.setTag(this);
		}
	}
}
