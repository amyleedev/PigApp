package com.szmy.pigapp.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.szmy.pigapp.R;

import java.io.UnsupportedEncodingException;

public class ActivityScanResultText extends BaseActivity {
	private TextView mTvTraceabilityCode;// 追溯码
	private TextView mTvUniversalName;// 通用名称
	private TextView mTvApprovalNumber;// 批准文号
	private TextView mTvManufacturer;// 生产厂家
	private TextView mTvTel;// 联系电话
	private String text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_result_text);
		text = getIntent().getExtras().getString("text");
		mTvTraceabilityCode = (TextView) findViewById(R.id.tv_traceability_code);
		mTvUniversalName = (TextView) findViewById(R.id.tv_universal_name);
		mTvApprovalNumber = (TextView) findViewById(R.id.tv_approval_number);
		mTvManufacturer = (TextView) findViewById(R.id.tv_manufacturer);
		mTvTel = (TextView) findViewById(R.id.tv_tel);
		try {
			text = new String(text.getBytes("ISO-8859-1"), "GB2312");
		} catch (UnsupportedEncodingException e) {
		}
		System.out.println("utf-8---------" + text);
		if (TextUtils.isEmpty(text)) {
			showToast("二维码信息错误");
			return;
		}
		String[] arr = text.split("，");
		if (arr.length == 5) {
			mTvTraceabilityCode.setText(arr[0]);
			mTvUniversalName.setText(arr[1]);
			mTvApprovalNumber.setText(arr[2]);
			mTvManufacturer.setText(arr[3]);
			mTvTel.setText(arr[4]);
		} else {
			showToast("二维码信息错误");
		}
	}
}
