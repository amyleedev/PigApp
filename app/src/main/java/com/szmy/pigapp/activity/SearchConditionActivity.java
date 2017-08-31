package com.szmy.pigapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.App;

public class SearchConditionActivity extends BaseActivity implements
		OnClickListener {

	private TextView mAddresstv;// 区域
	private TextView mPigtypetv;// 品种
	private TextView mPigsdtv;// 供求
	private TextView mPricetv;// 价格排序
	private TextView mOrderStatustv;// 成交状态
	private EditText mPriceMinet;// 价格区间
	private EditText mPriceMaxet;// 价格区间
	private EditText mWeightMinet;// 重量区间
	private EditText mWeightMaxet;// 重量区间
	private EditText mCountMinet;// 数量区间
	private EditText mCountMaxet;// 数量区间
	private Button mSubmitBtn; // 提交button
	private Button mCleatBtn;// 重置btn
	private String province = "", city = "", area = "", orderby = "",
			pigtype = "", orderType = "", orderstatus = "";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_condition);
		((TextView) findViewById(R.id.def_head_tv)).setText("筛选");

		initView();
	}

	private void initView() {

		mAddresstv = (TextView) findViewById(R.id.searchaddress_content);
		mPricetv = (TextView) findViewById(R.id.searchprice_content);
		mPigtypetv = (TextView) findViewById(R.id.searchtype_content);
		mPigsdtv = (TextView) findViewById(R.id.searchsd_content);
		mOrderStatustv = (TextView) findViewById(R.id.searchordertype);
		mPriceMinet = (EditText) findViewById(R.id.et_minprice);
		mPriceMaxet = (EditText) findViewById(R.id.et_maxprice);
		mWeightMinet = (EditText) findViewById(R.id.et_minweight);
		mWeightMaxet = (EditText) findViewById(R.id.et_maxweight);
		mCountMinet = (EditText) findViewById(R.id.et_mincount);
		mCountMaxet = (EditText) findViewById(R.id.et_maxcount);
		mSubmitBtn = (Button) findViewById(R.id.btn_submit);
		mCleatBtn = (Button) findViewById(R.id.btn_clear);
		mAddresstv.setOnClickListener(this);
		mPigsdtv.setOnClickListener(this);
		mPigtypetv.setOnClickListener(this);
		mPricetv.setOnClickListener(this);
		mOrderStatustv.setOnClickListener(this);
		mSubmitBtn.setOnClickListener(this);
		mCleatBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		Intent intent = new Intent();
		switch (v.getId()) {

		case R.id.searchaddress_content:
			intent.setClass(SearchConditionActivity.this,
					ChoseProvinceActivity.class);
			startActivityForResult(intent, App.ADDRESS_CODE);
			break;
		case R.id.searchprice_content:
			intent.setClass(SearchConditionActivity.this,
					ChoseOrderByActivity.class);
			startActivityForResult(intent, 25);

			break;
		case R.id.searchtype_content:
			intent.setClass(SearchConditionActivity.this,
					ChoseTypeActivity.class);
			startActivityForResult(intent, 23);

			break;
		case R.id.searchsd_content:
			intent.setClass(SearchConditionActivity.this,
					ChoseDealTypeActivity.class);
			startActivityForResult(intent, 24);

			break;
		case R.id.searchordertype:

			intent.setClass(SearchConditionActivity.this,
					ChoseOrderTypeActivity.class);
			startActivityForResult(intent, 26);
			break;
		case R.id.btn_clear:
			province = "";
			city = "";
			area = "";
			orderby = "";
			pigtype = "";
			orderType = "";
			orderstatus = "";
			mPriceMinet.setText("");
			mPriceMaxet.setText("");
			mWeightMinet.setText("");
			mWeightMaxet.setText("");
			mCountMinet.setText("");
			mCountMaxet.setText("");
			mAddresstv.setText("");
			mPricetv.setText("");
			mPigsdtv.setText("");
			mOrderStatustv.setText("");
			mPigtypetv.setText("");
			break;
		case R.id.btn_submit:

			Bundle bundle = new Bundle();
			bundle.putString("province", province);
			bundle.putString("city", city);
			bundle.putString("area", area);// 区域
			bundle.putString("orderBy", orderby);// 价格排序
			bundle.putString("pigType", pigtype);// 品种
			bundle.putString("orderType", orderType);// 供求
			bundle.putString("orderStatus", orderstatus);// 成交状态
			bundle.putString("startPrice", mPriceMinet.getText().toString());
			bundle.putString("endPrice", mPriceMaxet.getText().toString());
			bundle.putString("startWeight", mWeightMinet.getText().toString());
			bundle.putString("endWeight", mWeightMaxet.getText().toString());
			bundle.putString("startNumber", mCountMinet.getText().toString());
			bundle.putString("endNumber", mCountMaxet.getText().toString());

			intent.putExtras(bundle);
			setResult(30, intent);
			finish();

			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null)
			return;
		Bundle b = data.getExtras();
		switch (resultCode) {
		case App.ADDRESS_CODE:

			province = b.getString("province");
			city = b.getString("city");
			area = b.getString("area");
			mAddresstv.setText(province + " " + city + " " + area);
			break;
		case 23:

			pigtype = b.getString("pigType");
			mPigtypetv.setText(b.getString("pigTypeName"));

			break;
		case 24:
			orderType = b.getString("orderType");

			mPigsdtv.setText(b.getString("orderTypeName"));

			break;
		case 25:
			orderby = b.getString("orderBy");
			mPricetv.setText(b.getString("orderByName"));
			break;
		case 26:

			orderstatus = b.getString("orderStatus");
			if (orderstatus.equals("0")) {
				orderstatus = "";
			}
			mOrderStatustv.setText(b.getString("orderStatusName"));
			break;

		}

	}

}
