package com.szmy.pigapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szmy.pigapp.R;

public class ColumnAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private String[] mTitles;
	private int mIndex;

	public ColumnAdapter(Context mContext, String[] titles, int index) {
		super();
		this.mContext = mContext;
		this.mTitles = titles;
		mInflater = LayoutInflater.from(mContext);
		mIndex = index;
	}

	public void setIndex(int index) {
		this.mIndex = index;
	}

	@Override
	public int getCount() {
		return mTitles.length;
	}

	@Override
	public String getItem(int arg0) {
		return mTitles[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = this.mInflater.inflate(R.layout.item_column, null);
			holder.mTv = (TextView) arg1.findViewById(R.id.tv);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		holder.mTv.setText(mTitles[arg0]);
		if (mIndex == arg0) {
			holder.mTv.setBackgroundResource(R.drawable.bg_corners_red);
			holder.mTv.setTextColor(0xffffffff);
		} else {
			holder.mTv.setBackgroundResource(R.drawable.bg_corners_gray);
			holder.mTv.setTextColor(0xff676767);
		}
		return arg1;
	}

	private class ViewHolder {
		TextView mTv;
	}
}
