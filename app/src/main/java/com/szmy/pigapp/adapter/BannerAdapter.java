package com.szmy.pigapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.Banner;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.UrlEntry;

import java.util.List;

public class BannerAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<Banner> mList;

	public BannerAdapter(Context context, List<Banner> list) {
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
		// 显示图片的配置
	}

	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	public Banner getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.viewflow_item, null);
		}
		ImageView mIV = (ImageView) convertView.findViewById(R.id.iv_img);
		mIV.setImageResource(R.drawable.banner_default);
		final Banner banner = getItem(position);
		if (!TextUtils.isEmpty(banner.getImgPath())) {
			com.nostra13.universalimageloader.core.ImageLoader.getInstance()
					.displayImage(UrlEntry.ip + banner.getImgPath(), mIV,
							AppStaticUtil.getOptions());
		}
		return convertView;
	}
}
