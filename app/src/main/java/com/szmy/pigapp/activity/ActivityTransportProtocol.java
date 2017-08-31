package com.szmy.pigapp.activity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.szmy.pigapp.R;

public class ActivityTransportProtocol extends BaseActivity {
	private TextView mTvProtocolContent;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_transport_protocol);
		((TextView) findViewById(R.id.def_head_tv)).setText("生猪运输协议");
		mTvProtocolContent = (TextView) findViewById(R.id.tv_protocol_content);
		mTvProtocolContent.setMovementMethod(ScrollingMovementMethod
				.getInstance());
	}
}
