package com.szmy.pigapp.adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.ActivityMain;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.utils.App;

public class ViewFlowAdapter extends BaseAdapter {

	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	protected SharedPreferences.Editor mEditor;
	protected SharedPreferences mSettings;

	public ViewFlowAdapter(FragmentActivity context) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mSettings = mContext.getSharedPreferences(App.USERINFO,
				mContext.MODE_PRIVATE);
		mEditor = mSettings.edit();
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return position;
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
		ImageView mIVExperience = (ImageView) convertView
				.findViewById(R.id.iv_experience);
		if(position == 0){
			if (mIVExperience.getVisibility() == View.GONE)
				mIVExperience.setVisibility(View.VISIBLE);
			mIV.setImageResource(R.drawable.lead6);
		}
//		if (position == 0) {
//			if (mIVExperience.getVisibility() == View.VISIBLE)
//				mIVExperience.setVisibility(View.GONE);
//			mIV.setImageResource(R.drawable.lead1);
//		} else if (position == 1) {
//			if (mIVExperience.getVisibility() == View.VISIBLE)
//				mIVExperience.setVisibility(View.GONE);
//			mIV.setImageResource(R.drawable.lead2);
//		} else if (position == 2) {
//			if (mIVExperience.getVisibility() == View.VISIBLE)
//				mIVExperience.setVisibility(View.GONE);
//			mIV.setImageResource(R.drawable.lead4);
//		} else {
//			if (mIVExperience.getVisibility() == View.GONE)
//				mIVExperience.setVisibility(View.VISIBLE);
//			mIV.setImageResource(R.drawable.lead5);
//		}
		mIVExperience.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mEditor.putBoolean(BaseActivity.SETTING_ISFIRST, false);
				mEditor.commit();
				Intent intent = new Intent(mContext, ActivityMain.class);
				// intent.putExtra(BaseActivity.SETTING_INTEGRAL_POLICY, true);
				mContext.startActivity(intent);
				mContext.finish();
				// (FragmentActivity)mContext.finish();
			}
		});
		return convertView;
	}
}
