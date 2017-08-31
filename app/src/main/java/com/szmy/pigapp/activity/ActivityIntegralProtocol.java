package com.szmy.pigapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.szmy.pigapp.R;

public class ActivityIntegralProtocol extends BaseActivity {
	private LinearLayout mLlBack;
	private LinearLayout mLlClose;
	private boolean mIsFirst;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_integral_protocol);
		mIsFirst = getIntent().getBooleanExtra(
				BaseActivity.SETTING_INTEGRAL_POLICY, false);
		mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
		mLlClose = (LinearLayout) findViewById(R.id.ll_close);
		mLlClose.setOnClickListener(mClickListener);
		if (mIsFirst) {
			mLlBack.setVisibility(View.GONE);
			mLlClose.setVisibility(View.VISIBLE);
		} else {
			mLlBack.setVisibility(View.VISIBLE);
			mLlClose.setVisibility(View.GONE);
		}
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_close:
				close();
				break;
			}
		}
	};

	private void close() {
		editor.putBoolean(BaseActivity.SETTING_INTEGRAL_POLICY, false);
		editor.commit();
		Intent intent = new Intent(ActivityIntegralProtocol.this,
				ActivityMain.class);
		startActivity(intent);
		finish();
	}

	public void onBackPressed() {
		if (mIsFirst)
			close();
		else
			finish();
	};
}
