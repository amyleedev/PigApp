package com.szmy.pigapp.quotedprice;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.BaseActivity;

public class LookOrNewPriceActivity extends BaseActivity implements
		OnCheckedChangeListener {
	private RadioGroup mRadioGroup;
	private RadioButton mRadioPig, mRadioMap;
	private double x = 0, y = 0;
	private String mProvince = "";// 省
	private String mCity = "";// 市
	// private ImageButton mIbtnProtocol;
	private Fragment currentFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quotedprice_look_activity);
		mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
		mRadioGroup.setOnCheckedChangeListener(this);
		mRadioPig = (RadioButton) findViewById(R.id.radio_button_001);
		mRadioMap = (RadioButton) findViewById(R.id.radio_button_002);
//		Intent intent = getIntent();
//		if (intent.hasExtra("type")) {
//			if (intent.hasExtra("x")) {
//				x = intent.getExtras().getDouble("x");
//				y = intent.getExtras().getDouble("y");
//				mProvince = intent.getExtras().getString("province");
//				mCity = intent.getExtras().getString("city");
//			}
////			if (intent.getIntExtra("type", 1) == 1) {
//				mRadioPig.setChecked(true);
////			} else {
////				mRadioMap.setChecked(true);
////			}
////			changeFragment(1);
//		}
		 changeFragment(1);
	}

	public void changeFragment(int flag) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment fragment = getFragmentManager().findFragmentByTag(flag + "");
		boolean isShowOrAdd = true;// true表示show，false表示add
		if (fragment == null) {
			isShowOrAdd = false;
			switch (flag) {
			case 1:
				fragment =QuotedPriceListFragment.newInstance();
				break;
			case 2:
				fragment =MyQuotedPriceListFragment.newInstance();
				break;
			default:
				break;
			}
		}
		if (currentFragment != null) {
			ft.hide(currentFragment);
		}
		if (isShowOrAdd) {
			ft.show(fragment);
			fragment.onResume();
		} else {
			ft.add(R.id.lv_fragment_container, fragment, flag + "");
		}
		currentFragment = fragment;
		ft.commit();
	}

	// public void checkcompanyfragment() {
	// PigMapFragment fragment1 = new PigMapFragment();
	// getFragmentManager().beginTransaction()
	// .replace(R.id.lv_fragment_container, fragment1).commit();
	//
	// }

//	public void checkvehiclefragment() {
//		PigListFragment fragment1 = new PigListFragment();
//		getFragmentManager().beginTransaction()
//				.replace(R.id.lv_fragment_container, fragment1).commit();
//
//	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.radio_button_001:// 查猪价
			changeFragment(1);
			break;
		case R.id.radio_button_002:// 报猪价
			changeFragment(2);
			break;

		}
	}

}
