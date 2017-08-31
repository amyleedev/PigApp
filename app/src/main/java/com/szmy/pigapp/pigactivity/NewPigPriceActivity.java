package com.szmy.pigapp.pigactivity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.service.LocationService;
import com.gc.materialdesign.widgets.Dialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.AddressChoose;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

public class NewPigPriceActivity extends BaseActivity {
	private TextView mTvAddress;
	private EditText mEtNsy;
	private EditText mEtWsy;
	private EditText mEtTzz;
	private EditText mEtYm;
	private EditText mEtDp;
	private Button mBtnSubmit;
	private String province = "";
	private String city = "";
	private String area = "";
	private LocationService locationService;
	private float price1, price2, price3, price4, price5;
	private String price1Str, price2Str, price3Str, price4Str, price5Str;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.new_pigprice_activity);
		((TextView) findViewById(R.id.def_head_tv)).setText("我要报价");
		((LinearLayout) findViewById(R.id.def_head_back))
				.setVisibility(View.VISIBLE);
		TextView menu = (TextView) findViewById(R.id.tv_menu);
		menu.setVisibility(View.VISIBLE);
		menu.setText("我的报价");
		menu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewPigPriceActivity.this,
						PigPriceListActivity.class);
				startActivity(intent);
				finish();
			}
		});
		initView();
		locationService = new LocationService(
				NewPigPriceActivity.this.getApplicationContext());
		locationService.registerListener(mListener);
	}

	private void initView() {
		mTvAddress = (TextView) findViewById(R.id.tv_address);
		mTvAddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewPigPriceActivity.this,
						AddressChoose.class);
				intent.putExtra("mainlocation", "mainlocation");
				startActivityForResult(intent, 0);
			}
		});
		mEtNsy = (EditText) findViewById(R.id.et_nsy);
		mEtWsy = (EditText) findViewById(R.id.et_wsy);
		mEtTzz = (EditText) findViewById(R.id.et_tzz);
		mEtYm = (EditText) findViewById(R.id.et_ym);
		mEtDp = (EditText) findViewById(R.id.et_dp);
		mBtnSubmit = (Button) findViewById(R.id.btn_submitP);
		mBtnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendData();
			}
		});
	}

	/*****
	 * @see copy funtion to you project
	 *      定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 * 
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (null != location
					&& location.getLocType() != BDLocation.TypeServerError) {
				if (location.getLocType() == 62 || location.getLocType() == 162) {
				} else {
					province = location.getProvince();
					city = location.getCity();
					area = location.getDistrict();
					mTvAddress.setText(province + " " + city + " " + area);
					// getPigPrice();
				}
			}
		}
	};

	/**
	 * 是否为合法的价格 @prarm price 昨天价格，为0的话，报价必须位于12-20之间 offer报价
	 */
	private boolean isLegalPrice(float price, String offer, int max, int min) {
		float myOffer = 0;
		try {
			myOffer = Float.parseFloat(offer);
		} catch (Exception e) {
		}
		if (price == 0) {
			if (myOffer < min || myOffer > max) {
				return false;
			} else {
				return true;
			}
		}
		if (myOffer < price * (0.97) || myOffer > price * (1.03)) {
			return false;
		}
		return true;
	}

	private void sendData() {
		if (TextUtils.isEmpty(mTvAddress.getText().toString())) {
			showToast("请选择报价区域！");
			return;
		}
		price1Str = mEtNsy.getText().toString().trim();
		price2Str = mEtWsy.getText().toString().trim();
		price3Str = mEtTzz.getText().toString().trim();
		price4Str = mEtYm.getText().toString().trim();
		price5Str = mEtDp.getText().toString().trim();
		if (TextUtils.isEmpty(price1Str) && TextUtils.isEmpty(price2Str)
				&& TextUtils.isEmpty(price3Str) && TextUtils.isEmpty(price4Str)
				&& TextUtils.isEmpty(price5Str)) {
			showToast("至少填写一个报价！");
			return;
		}
		if (!TextUtils.isEmpty(price1Str)
				&& !isLegalPrice(price1, price1Str, 25, 10)) {
			showToast("请输入有效的内三元报价！");
			return;
		}
		if (!TextUtils.isEmpty(price2Str)
				&& !isLegalPrice(price2, price2Str, 25, 10)) {
			showToast("请输入有效的外三元报价！");
			return;
		}
		if (!TextUtils.isEmpty(price3Str)
				&& !isLegalPrice(price3, price3Str, 25, 10)) {
			showToast("请输入有效的土杂猪报价！");
			return;
		}
		if (!TextUtils.isEmpty(price4Str)
				&& !isLegalPrice(price4, price4Str, 2500, 1500)) {
			showToast("请输入有效的玉米报价！");
			return;
		}
		if (!TextUtils.isEmpty(price5Str)
				&& !isLegalPrice(price5, price5Str, 3500, 2500)) {
			showToast("请输入有效的豆粕报价！");
			return;
		}
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("province", province);
		params.addBodyParameter("city", city);
		params.addBodyParameter("area", area);
		params.addBodyParameter("nsy", price1Str);
		params.addBodyParameter("wsy", price2Str);
		params.addBodyParameter("tzz", price3Str);
		params.addBodyParameter("ym", price4Str);
		params.addBodyParameter("dp", price5Str);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.ADD_MYPIGPRICE_URL,
				params, new RequestCallBack<String>() {
					@Override
					public void onStart() {
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println("pigprice" + responseInfo.result);
						try {
							JSONObject jsonresult = new JSONObject(
									responseInfo.result);
							if (jsonresult.optString("success").equals("1")) {
								final Dialog dialog = new Dialog(NewPigPriceActivity.this, "提示", "报价成功,感谢您的参与！");
								dialog.addCancelButton("取消");
								dialog.setCancelable(false);
								dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										dialog.cancel();
										Intent intent = new Intent(
												NewPigPriceActivity.this,
												PigPriceListActivity.class);
										startActivity(intent);
										finish();
									}
								});
								dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										dialog.cancel();
										finish();
									}
								});
								dialog.show();


//								AlertDialog.Builder builder = new Builder(
//										NewPigPriceActivity.this);
//								builder.setMessage("报价成功,感谢您的参与！");
//								builder.setTitle("信息提示");
//								builder.setPositiveButton("确定",
//										new OnClickListener() {
//											@Override
//											public void onClick(
//													DialogInterface dialog,
//													int which) {
//												Intent intent = new Intent(
//														NewPigPriceActivity.this,
//														PigPriceListActivity.class);
//												startActivity(intent);
//												finish();
//
//											}
//										});
//								builder.create().show();
							} else {
								successType(jsonresult.optString("success"),
										"报价失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						disDialog();
						showToast("提交失败,请稍后再试！");
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if (data == null)
				return;
			Bundle bundle = data.getExtras();
			province = bundle.getString("province");
			city = bundle.getString("city");
			area = bundle.getString("area");
			mTvAddress.setText(province + " " + city + " " + area);
			price2 = 0;
			price1 = 0;
			price3 = 0;
			price4 = 0;
			price5 = 0;
			// getPigPrice();
			break;
		}
	}

	private void getPigPrice() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("province", province);
		params.addBodyParameter("city", city);
		params.addBodyParameter("area", area);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.TODAY_PIGPRICE_URL,
				params, new RequestCallBack<String>() {
					@Override
					public void onStart() {
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println("price" + responseInfo.result);
						try {
							JSONObject jsonresult = new JSONObject(
									responseInfo.result);
							if (jsonresult.optString("success").equals("1")) {
								try {
									price1 = Float.valueOf(jsonresult
											.optString("nsy"));
								} catch (Exception e) {
									price1 = 0;
								}
								try {
									price2 = Float.valueOf(jsonresult
											.optString("wsy"));
								} catch (Exception e) {
									price2 = 0;
								}
								try {
									price3 = Float.valueOf(jsonresult
											.optString("tzz"));
								} catch (Exception e) {
									price3 = 0;
								}
								try {
									price4 = Float.valueOf(jsonresult
											.optString("ym"));
								} catch (Exception e) {
									price4 = 0;
								}
								try {
									price5 = Float.valueOf(jsonresult
											.optString("dp"));
								} catch (Exception e) {
									price5 = 0;
								}
							}
						} catch (JSONException e) {
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {

					}
				});
	}
}
