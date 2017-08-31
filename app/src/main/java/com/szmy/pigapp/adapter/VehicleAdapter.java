package com.szmy.pigapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.Vehicle;
import com.szmy.pigapp.utils.UrlEntry;

import java.util.List;

public class VehicleAdapter extends BaseAdapter {

		private List<Vehicle> childJson;
		private LayoutInflater mInflater;
		private BitmapUtils bitmapUtils;
		
		public VehicleAdapter(List<Vehicle> list  ,LayoutInflater inflater,BitmapUtils bitmapUtils){
			this.childJson = list;
			this.mInflater = inflater;
			this.bitmapUtils = bitmapUtils;
		}

		private class ViewHolder {
			ImageView mInfoIcon;
			TextView mInfoTitle;
			TextView mInfoValue;
			TextView mInfoTime;
		}

		@Override
		public int getCount() {
			return childJson.size();
		}

		@Override
		public Object getItem(int position) {
			return childJson.get(position);
		}

		@Override
		public long getItemId(int posotion) {
			return posotion;
		}

		@Override
		public View getView(int posotion, View convertView, ViewGroup arg2) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.info_fragment_list_item, null);
				holder.mInfoIcon = (ImageView) convertView
						.findViewById(R.id.imagehead);
				holder.mInfoTitle = (TextView) convertView
						.findViewById(R.id.info_list_name_text);
				holder.mInfoValue = (TextView) convertView
						.findViewById(R.id.info_list_type_text);
				holder.mInfoTime = (TextView) convertView
						.findViewById(R.id.info_list_name_text2);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Vehicle info = childJson.get(posotion);
			holder.mInfoTitle.setText(info.getType());

			holder.mInfoValue.setText("经营范围：" + info.getJyfw());
			holder.mInfoTime.setText(info.getCreateTime().substring(0, 16));

			if (info.getCoverPicture().toString().equals("")) {
				holder.mInfoIcon.setImageResource(R.drawable.default_title);
			} else {
				bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_title);
				bitmapUtils.display(holder.mInfoIcon, UrlEntry.ip
						+ info.getCoverPicture().toString());// 下载并显示图片
			}

			return convertView;
		}
	
}
