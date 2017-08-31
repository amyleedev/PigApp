/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.szmy.pigapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.ActivityScanResult;
import com.szmy.pigapp.entity.Banner;
import com.szmy.pigapp.utils.UrlEntry;

import java.util.List;

/**
 * @Description: 图片适配器
 * @author http://blog.csdn.net/finddreams
 */ 
public class ImagePagerAdapter extends BaseAdapter {

	private Context context;
	private int size;
	private boolean isInfiniteLoop;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private List<Banner> mList;

	public ImagePagerAdapter(Context context,List<Banner> mBannerList) {
		this.context = context;

		if (mBannerList != null) {
			this.size = mBannerList.size();
		}

		this.mList = mBannerList;
		isInfiniteLoop = false;
		// 初始化imageLoader 否则会报错
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.banner_default) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.banner_default) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.banner_default) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();

	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : mList.size();
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}
	private Banner banner;
	@Override
	public View getView(final int position, View view, ViewGroup container) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = holder.imageView = new ImageView(context);
			holder.imageView
					.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
			holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
      banner = mList.get(getPosition(position));
		imageLoader.displayImage(UrlEntry.ip +banner.getImgPath(),
				holder.imageView, options);

		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String url = banner.getSrcPath();
				  if (TextUtils.isEmpty(url)) {
				  holder.imageView.setEnabled(false); return; }
				if (url.equals(UrlEntry.ip + "/activity/zmt.jsp?img=fxsInfo")) {
					Intent intent = new Intent(context,
							ActivityScanResult.class);
					intent.putExtra("url", url);
					intent.putExtra("fxs", "fxs");
					context.startActivity(intent);
				} else {
					Intent intent = new Intent(context,
							ActivityScanResult.class);
					intent.putExtra("url", url);
					context.startActivity(intent);
				}
			}
		});

		return view;
	}

	private static class ViewHolder {

		ImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

}
