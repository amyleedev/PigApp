package com.szmy.pigapp.vehicle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.ActivityTransportProtocol;
import com.szmy.pigapp.activity.BaseActivity;

public class VehicleCompanyActivity extends BaseActivity implements
		OnCheckedChangeListener,OnClickListener {
	private RadioGroup mRadioGroup;
	private double x, y;
	private double  x2, y2;
	private String provinceContent, cityContent;
	 private TextView mIbtnProtocol,mTvNearBy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vehicle_activity);
		x = getIntent().getExtras().getDouble("x");
		y = getIntent().getExtras().getDouble("y");
		mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
		mRadioGroup.setOnCheckedChangeListener(this);
		mIbtnProtocol = (TextView) findViewById(R.id.Ibtn_protocol);
		mIbtnProtocol.setOnClickListener(this);
		Intent intent = getIntent();
		x2 = intent.getDoubleExtra("x", 0);
		y2 = intent.getDoubleExtra("y", 0);
		provinceContent = intent.getStringExtra("province");
		cityContent = intent.getStringExtra("city");
		mTvNearBy = (TextView) findViewById(R.id.Ibtn_nearby);
		mTvNearBy.setOnClickListener(this);
		checkvehiclefragment();
	}

	public void checkcompanyfragment() {
		CompanyFragment fragment1 = new CompanyFragment();
		Bundle bundle=new Bundle();
		bundle.putDouble("x", x);
		bundle.putDouble("y", y);
		fragment1.setArguments(bundle);
		getFragmentManager().beginTransaction()
				.replace(R.id.lv_fragment_container, fragment1).commit();

	}

	public void checkvehiclefragment() {
		VehicleFragment fragment1 = new VehicleFragment();
		getFragmentManager().beginTransaction()
				.replace(R.id.lv_fragment_container, fragment1).commit();

	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.Ibtn_protocol:
			startActivity(new Intent(VehicleCompanyActivity.this,
					ActivityTransportProtocol.class));
			break;
		case R.id.Ibtn_nearby:
			Intent intent = new Intent(VehicleCompanyActivity.this,
					LogisticsMapActivity.class);
			intent.putExtra("x", x2);
			intent.putExtra("province", provinceContent);
			intent.putExtra("city", cityContent);
			intent.putExtra("y", y2);
			startActivity(intent);
			break;
		}
	}

	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// loadData(TYPE_REFRESH);
	// }

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.radio_button_001:// 车辆
			checkvehiclefragment();
			break;
		case R.id.radio_button_002:// 企业
			checkcompanyfragment();
			break;

		}
	}

}
