package com.szmy.pigapp.share;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szmy.pigapp.R;

public class ShareHolder {
	public TextView  mTvCopy;
	public ImageView mIvCode;
	private LinearLayout mLlWechat;
	private LinearLayout mLlWechatmoments;
	private LinearLayout mLlQQ;
	private LinearLayout mLlQZone;

	public ShareHolder(View view, OnClickListener l) {
		mTvCopy = (TextView) view.findViewById(R.id.copy);
		mTvCopy.setOnClickListener(l);
		mIvCode=(ImageView) view.findViewById(R.id.codeimg);
		mLlWechat = (LinearLayout) view.findViewById(R.id.ll_wechat);
		mLlWechat.setOnClickListener(l);
		mLlWechatmoments = (LinearLayout) view
				.findViewById(R.id.ll_wechatmoments);
		mLlWechatmoments.setOnClickListener(l);
		mLlQQ = (LinearLayout) view.findViewById(R.id.ll_qq);
		mLlQQ.setOnClickListener(l);
		mLlQZone = (LinearLayout) view.findViewById(R.id.ll_qzone);
		mLlQZone.setOnClickListener(l);
	}
}
