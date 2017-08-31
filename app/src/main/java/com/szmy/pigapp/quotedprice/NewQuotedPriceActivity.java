package com.szmy.pigapp.quotedprice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gc.materialdesign.widgets.Dialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.activity.ChoseProvinceActivity;
import com.szmy.pigapp.activity.SlaughterhouseRenZhengActivity;
import com.szmy.pigapp.activity.ZhuChangRenZhengActivity;
import com.szmy.pigapp.agent.AgentRenZhengActivity;
import com.szmy.pigapp.tixian.ActivityWithdrawals;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.DateTimePickerDialog;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 新增报价
 */
public class NewQuotedPriceActivity extends BaseActivity implements
		OnClickListener {

	private EditText mEtFinalPrice,mEtPrice,mEtNum,mEtMinWeight,mEtMaxWeight,mEtRemark;
	private TextView mTvPrice,mTvTime,mTvNum,mTvAddress,mTvType,mTvMarkingTime,mTvAddressTip;
	private LinearLayout mLlFinalPrice,mLlZhuangzhutai,mLlAddress;
	private Button btnSubmit;
	private int newtype = 0;

	private final int PIGTYPE_CODE = 23;
	private String province = "";
	private String city = "";
	private String area = "";
	private RadioButton mRbYes,mRbNo;
	private String[] mItems = {"内三元", "外三元", "土杂猪"};
	private int mSingleChoiceID = 0;
	private QuotedPrice mQpEntry;
	private Calendar cal ;//= Calendar.getInstance();
	private static final int DATE_DIALOG_ID = 01;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private String min_price = "5",max_price = "30";
	private String addtype = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_quotedprice);
		((TextView) findViewById(R.id.def_head_tv)).setText("我要报价");
		initView();
		loadlastData();
	}
	private void initView() {
		addtype = getIntent().getStringExtra("addtype");
		cal = Calendar.getInstance();
		mYear = cal.get(Calendar.YEAR);
		mMonth = cal.get(Calendar.MONTH);
		mDay = cal.get(Calendar.DAY_OF_MONTH);
		mHour = cal.get(Calendar.HOUR_OF_DAY);
		mMinute = cal.get(Calendar.MINUTE);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		mEtFinalPrice = (EditText) findViewById(R.id.et_finalprice);
		mEtPrice = (EditText) findViewById(R.id.et_price);
		mEtNum = (EditText) findViewById(R.id.et_count);
		mEtMinWeight = (EditText) findViewById(R.id.et_weight);
		mEtMaxWeight = (EditText) findViewById(R.id.et_weight2);
		mEtRemark = (EditText) findViewById(R.id.et_remark);
		mTvAddress = (TextView) findViewById(R.id.province_city_tv_content);
		mTvAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(NewQuotedPriceActivity.this,
						ChoseProvinceActivity.class);
				intent.putExtra("add", "add");
				startActivityForResult(intent, App.ADDRESS_CODE);
			}
		});
		mTvPrice = (TextView) findViewById(R.id.price_tv);
		mTvTime = (TextView) findViewById(R.id.time_tv);
		mTvMarkingTime = (TextView) findViewById(R.id.leave_start_timebox_tv_content);
		mTvMarkingTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
//				showDateDialog();
				showDialog(DATE_DIALOG_ID);
			}
		});
		mTvNum = (TextView) findViewById(R.id.count_tv);
		mTvType = (TextView) findViewById(R.id.pinzhong_tv_content);
		mTvType.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						NewQuotedPriceActivity.this);

				mSingleChoiceID = 0;
				builder.setTitle("请选择");
				builder.setSingleChoiceItems(mItems, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int whichButton) {
								mSingleChoiceID = whichButton;

							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int whichButton) {

//								pigType = String.valueOf(mSingleChoiceID + 1);

								mTvType.setText(mItems[mSingleChoiceID]);

							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int whichButton) {

							}
						});
				builder.create().show();
			}
		});
		mLlFinalPrice = (LinearLayout) findViewById(R.id.jiesuanjia_layout);
		mLlZhuangzhutai = (LinearLayout) findViewById(R.id.zhuangzhutailayout);
		mLlAddress = (LinearLayout) findViewById(R.id.shouzhuquyulayout);
		mTvAddressTip = (TextView) findViewById(R.id.tv_address);
		mRbNo = (RadioButton) findViewById(R.id.radio_no);
		mRbYes = (RadioButton) findViewById(R.id.radio_yes);
		cal.setTimeInMillis(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //2017-08-17
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = format.format(ca.getTime());
		Date date=new Date();
		String nowtime=format.format(date);
		String nextdaytime = cal.get(Calendar.YEAR) + "-"
				+ AppStaticUtil.parseMonth(cal.get(Calendar.MONTH)) +"-"+ AppStaticUtil.parseDayOfMonth(cal.get(Calendar.DAY_OF_MONTH)+1)+" 00:00(明天)";
		if (nowtime.equals(last)){
			if (AppStaticUtil.parseMonth(cal.get(Calendar.MONTH)).equals("12")){//如果是12月份
				nextdaytime = cal.get(Calendar.YEAR) + "-"+ "01" +"-"+ "01"+" 00:00(明天)";
			}else
				nextdaytime = cal.get(Calendar.YEAR) + "-"
						+ AppStaticUtil.parseMonth(cal.get(Calendar.MONTH)+1) +"-"+ "01"+" 00:00(明天)";
		}
//		if (App.usertype.equals("1")){
		if (addtype.equals("cs")){
			newtype = 1;
			mLlZhuangzhutai.setVisibility(View.VISIBLE);
//		}else if(App.usertype.equals("2")){
		}else if(addtype.equals("sg")){
			newtype = 2;
			mLlFinalPrice.setVisibility(View.VISIBLE);
//			mLlAddress.setVisibility(View.VISIBLE);
			mTvPrice.setText("收购价：");
			mTvTime.setText("收购时间：");
			mTvNum.setText("收购数量：");
			mTvAddressTip.setText("收猪区域：");
//			cal.setTimeInMillis(System.currentTimeMillis());

//			mTvMarkingTime.setText(cal.get(Calendar.YEAR) + "-"
//					+ AppStaticUtil.parseMonth(cal.get(Calendar.MONTH)) +"-"+ AppStaticUtil.parseDayOfMonth(cal.get(Calendar.DAY_OF_MONTH)+1)+" 00:00(明天)");
			mTvMarkingTime.setText(nextdaytime);

		}
//		else if (App.usertype.equals("3")){
//			newtype = 2;
////			mLlAddress.setVisibility(View.VISIBLE);
//			mTvPrice.setText("收购价：");
//			mTvTime.setText("收购时间：");
//			mTvNum.setText("收购数量：");
//			mTvAddressTip.setText("收猪区域：");
//			cal.setTimeInMillis(System.currentTimeMillis());
//
////			mTvMarkingTime.setText(cal.get(Calendar.YEAR) + "-"
////					+ AppStaticUtil.parseMonth(cal.get(Calendar.MONTH)) +"-"+ AppStaticUtil.parseDayOfMonth(cal.get(Calendar.DAY_OF_MONTH)+1)+" 00:00(明天)");
//			mTvMarkingTime.setText(nextdaytime);
//		}

	}
private  void  setInitView(){
	mTvType.setText(mQpEntry.getProductType());
//	mEtPrice.setText(mQpEntry.getPrice());
//	mEtFinalPrice.setText(mQpEntry.getFinalPrice());
	mEtNum.setText(mQpEntry.getNumber());
	mEtMaxWeight.setText(mQpEntry.getMaxWeight());
	mEtMinWeight.setText(mQpEntry.getMinWeight());
	mEtRemark.setText(mQpEntry.getRemark());
	if (!TextUtils.isEmpty(mQpEntry.getZzt())){
		if (mQpEntry.getZzt().equals("0")){
			mRbNo.setChecked(true);
		}else{
			mRbYes.setChecked(true);
		}
	}
	province = mQpEntry.getProvince();
	city = mQpEntry.getCity();
	area = mQpEntry.getArea();
	mTvAddress.setText(province + " "+city + " "+area);

}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			if (TextUtils.isEmpty(mTvMarkingTime.getText().toString())||TextUtils.isEmpty(mEtNum.getText().toString())
					||TextUtils.isEmpty(mEtPrice.getText().toString())||TextUtils.isEmpty(mTvAddress.getText().toString())) {
				showToast("带'*'项不能为空！");
				return;
			}
			if (Integer.parseInt(mEtNum.getText().toString())==0){
				showToast("数量不能为0！");
				return;
			}
			if (App.usertype.equals("2")){
				if (TextUtils.isEmpty(mEtFinalPrice.getText().toString())){
					showToast("带'*'项不能为空！");
					return;
				}
			}
			if(TextUtils.isEmpty(mEtMaxWeight.getText().toString())&&TextUtils.isEmpty(mEtMinWeight.getText().toString())){
				showToast("体重范围必须输入一个！");
				return;
			}
			if(!TextUtils.isEmpty(mEtMaxWeight.getText().toString())&&!TextUtils.isEmpty(mEtMinWeight.getText().toString())){
				if (Float.parseFloat(mEtMaxWeight.getText().toString())<Float.parseFloat(mEtMinWeight.getText().toString())){
					showToast("请输入正确的体重范围！");
					return;
				}
			}

			if (!FileUtil.isLegalPrice(0,
					mEtPrice.getText().toString(), Float.parseFloat(max_price), Float.parseFloat(min_price))) {
				if (Double.parseDouble(mEtPrice.getText().toString()) > Float.parseFloat(max_price)) {
					showToast("您输入的价格过高!");
					return;

				}
				if (Double.parseDouble(mEtPrice.getText().toString()) < Float.parseFloat(min_price)) {
					showToast("您输入的价格过低!");
					return;
				}
			}
			if (!TextUtils.isEmpty(mEtFinalPrice.getText())){
				if (!FileUtil.isLegalPrice(0,
						mEtFinalPrice.getText().toString(), Float.parseFloat(max_price), Float.parseFloat(min_price))) {
					if (Double.parseDouble(mEtFinalPrice.getText().toString()) > Float.parseFloat(max_price)) {
						showToast("您输入的结算价格过高!");
						return;

					}
					if (Double.parseDouble(mEtFinalPrice.getText().toString()) < Float.parseFloat(min_price)) {
						showToast("您输入的结算价格过低!");
						return;
					}
				}
			}
			getIsSmrz();
			break;
		default:
			break;
		}
	}

	public void sendData() {

		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		if ( mTvMarkingTime.getText().toString().contains("明天")){
			params.addBodyParameter("e.marketingTime", mTvMarkingTime.getText().toString().substring(0,mTvMarkingTime.getText().toString().length()-4));
		}else
		params.addBodyParameter("e.marketingTime", mTvMarkingTime.getText().toString());
		params.addBodyParameter("e.number", mEtNum.getText().toString());

		if (App.usertype.equals("1")){
			params.addBodyParameter("e.zzt", mRbYes.isChecked()?"1":"0");
		}

			params.addBodyParameter("e.province", province);
			params.addBodyParameter("e.city", city);
			params.addBodyParameter("e.area", area);

		params.addBodyParameter("e.price", mEtPrice.getText().toString());
		params.addBodyParameter("e.type", String.valueOf(newtype));
		if (App.usertype.equals("2")){
			params.addBodyParameter("e.finalPrice", mEtFinalPrice.getText().toString());
		}
		params.addBodyParameter("e.productType", mTvType.getText().toString());
		params.addBodyParameter("e.maxWeight", mEtMaxWeight.getText().toString());
		params.addBodyParameter("e.minWeight", mEtMinWeight.getText().toString());
		params.addBodyParameter("e.remark", mEtRemark.getText().toString());
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUOTED_PRICE_ADD_URL,
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
						Log.i("insert", " insert responseInfo.result = "
								+ responseInfo.result);
						disDialog();
						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.optString("success").toString()
									.equals("1")) {

								if(jsonresult.optString("isfirst").toString().equals("1")){
									AppStaticUtil.toastPricePoint(NewQuotedPriceActivity.this);
								}else
								showToast("报价成功");
								setResult(99);
								finish();

							} else {
								successType(jsonresult.optString("success")
										.toString(), jsonresult
										.optString("msg").toString());
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("服务器连接失败，请稍候再试！");
						disDialog();
					}
				});
	}

	private void getIsSmrz() {
		btnSubmit.setEnabled(false);
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.IS_USERSMRX_URL,
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
						disDialog();
						btnSubmit.setEnabled(true);
						Log.i("smrz", " smrz responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult = null;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.optString("success").toString()
									.equals("1")) {

//								sendDataByUUid();//不用判断银行卡
										sendData();

							} else if (jsonresult.optString("success").toString()
									.equals("6")) {
								final Dialog dialog = new Dialog(NewQuotedPriceActivity.this, "提示", jsonresult.optString("msg").toString());
								dialog.addCancelButton("取消");
								dialog.setOnAcceptButtonClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										dialog.dismiss();
										if (App.usertype.equals("1")) { // 猪场
											Intent zcintent = new Intent(
													NewQuotedPriceActivity.this,
													ZhuChangRenZhengActivity.class);
											startActivity(zcintent);
										} else if (App.usertype.equals("2")) {// 屠宰场
											Intent zcintent = new Intent(
													NewQuotedPriceActivity.this,
													SlaughterhouseRenZhengActivity.class);
											startActivity(zcintent);
										} else if (App.usertype.equals("3")) {// 经纪人
											Intent zcintent = new Intent(
													NewQuotedPriceActivity.this,
													AgentRenZhengActivity.class);
											startActivity(zcintent);
										}
									}
								});
								dialog.setOnCancelButtonClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										dialog.cancel();
									}
								});
								dialog.show();
							} else if (jsonresult.optString("success").toString()
									.equals("5")) {
								showDialog1(NewQuotedPriceActivity.this, jsonresult.optString("msg").toString());
							} else {
								successType(jsonresult.optString("success").toString(), "操作失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						disDialog();
					}
				});
	}

	//查询银行卡信息
	private void sendDataByUUid() {
		showDialog();
		btnSubmit.setEnabled(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_CARDBYUUID_URL,
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
						disDialog();
						btnSubmit.setEnabled(true);
						Log.i("select", " bank responseInfo.result = "
								+ responseInfo.result);

						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {

								sendData();


							} else if (jsonresult.get("success").toString()
									.equals("5")) {
								//没有银行卡信息
								String msg = "报价失败，请先绑定银行卡。";
								Map<String, String> map = new HashMap<String, String>();
								map.put("type", "rz");
								jumpActivity(NewQuotedPriceActivity.this, ActivityWithdrawals.class, msg, map);
							} else {
								successType(jsonresult.get("success")
										.toString(), "操作失败！");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("服务器连接失败，请稍候再试！");
						disDialog();

					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Bundle bundle;
		switch (resultCode) {
			case App.ADDRESS_CODE:
				if (data == null)
					return;
				bundle = data.getExtras();
				province = bundle.getString("province");
				city = bundle.getString("city");
				area = bundle.getString("area");
				mTvAddress.setText(province + " " + city + " "
						+ area);
				break;
		}
	}
	private void showDateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(NewQuotedPriceActivity.this,
				R.layout.dialog_date, null);
		final DatePicker datePicker = (DatePicker) view
				.findViewById(R.id.date_picker);
		builder.setView(view);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				(cal.get(Calendar.DAY_OF_MONTH)+1), null);

			String date = mTvMarkingTime.getText().toString();
			String[] dates=date.split("-");
			if (TextUtils.isEmpty(date)||dates.length!=3) {
				datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						(cal.get(Calendar.DAY_OF_MONTH)+1), null);
			}else {
				datePicker.init(Integer.parseInt(dates[0]),(Integer.parseInt(dates[1])-1),Integer.parseInt(dates[2]),null);
			}

		builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				datePicker.clearFocus();
				StringBuffer sb = new StringBuffer();
				sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),
						datePicker.getMonth() + 1, datePicker.getDayOfMonth()));

				mTvMarkingTime.setText(sb.toString());

				dialog.cancel();
			}
		});
		android.app.Dialog dialog = builder.create();
		dialog.show();
	}
	private void loadlastData() {
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUOTED_PRICE_LAST_URL,
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
						disDialog();
						btnSubmit.setEnabled(true);
						Log.i("smrz", " smrz responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult = null;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.optString("success").toString()
									.equals("1")) {

								GsonBuilder gsonb = new GsonBuilder();
								Gson gson = gsonb.create();
								mQpEntry = new QuotedPrice();
								mQpEntry = gson.fromJson(jsonresult.toString(),QuotedPrice.class);
								if(!TextUtils.isEmpty(jsonresult.optString("min_price"))){
								min_price =jsonresult.optString("min_price").toString();
								max_price =jsonresult.optString("max_price").toString();}
								setInitView();


							}  else if(jsonresult.optString("success").toString()
									.equals("4")){
								if(!TextUtils.isEmpty(jsonresult.optString("min_price"))){
									min_price =jsonresult.optString("min_price").toString();
									max_price =jsonresult.optString("max_price").toString();}
							}else {
								successType(jsonresult.optString("success").toString(), "操作失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						disDialog();
					}
				});
	}
	@Override
	protected android.app.Dialog onCreateDialog(int id) {

		return new DateTimePickerDialog(this, mDateSetListener, mYear, mMonth,
				mDay, mHour, mMinute, false);
	}

	@Override
	protected void onPrepareDialog(int id, android.app.Dialog dialog) {

		((DateTimePickerDialog) dialog).updateDate(mYear, mMonth, mDay, mHour,
				mMinute);
	}

	private DateTimePickerDialog.OnDateSetListener mDateSetListener = new DateTimePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth, TimePicker timeview, int hourOfDay, int minut) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			mHour = hourOfDay;
			mMinute = minut;
			mTvMarkingTime.setText(updateDisplay(0));
		}
	};

	private String updateDisplay(int i) {

		// Month is 0 based so add 1
		String timestr;
		if (i == 0) {
			timestr = new StringBuilder()
					.append(mYear)
					.append("-")
					.append((mMonth + 1) < 10 ? "0" + (mMonth + 1)
							: (mMonth + 1)).append("-")
					.append((mDay < 10) ? "0" + mDay : mDay).append(" ")
					.append(pad(mHour)).append(":").append(pad(mMinute))
					.toString();
		} else {
			timestr = new StringBuilder()
					.append(mYear)
					.append("-")
					.append((mMonth + 1) < 10 ? "0" + (mMonth + 1)
							: (mMonth + 1)).append("-")
					.append((mDay < 10) ? "0" + mDay : mDay).toString();
		}

		return timestr;
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
}
