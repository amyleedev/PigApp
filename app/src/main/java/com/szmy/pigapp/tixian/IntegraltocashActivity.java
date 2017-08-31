package com.szmy.pigapp.tixian;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.widgets.Dialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.activity.IntegralActivity;
import com.szmy.pigapp.image.CustomConstants;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 积分提现
 */
public class IntegraltocashActivity extends BaseActivity implements
		OnClickListener {

	private EditText etIntegral;
	private Button btnSubmit;
	private TextView tvMycard;
	private TextView tvMypoint,tvTixiantip;
	private int point = 0;
	private int kypoint = 0;
	private TextView mTvTip;
	private TextView mTvKyProint;
	private TextView mTvinfoTop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integraltocash);
		point = getIntent().getIntExtra("point", 0);
		kypoint = getIntent().getIntExtra("kypoint", 0);
		initView();
		loadTipText();
//		isFlagCard();

	}
	public void loadTipText() {

		showDialog();
		RequestParams params = new RequestParams();
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.INTEGRAL_TIPTEXT_URL,
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
								mTvTip.setText(jsonresult.optString("info"));
								mTvinfoTop.setText(jsonresult.optString("infoTop"));
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
	private void initView() {
		btnSubmit = (Button) findViewById(R.id.btn_submitP);
		etIntegral = (EditText) findViewById(R.id.etIntegral);
		etIntegral.addTextChangedListener(textWatcher);
		tvMycard = (TextView) findViewById(R.id.mycard);
		tvMycard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(IntegraltocashActivity.this,
						WithdrawalsListActivity.class));

			}
		});
		tvMypoint = (TextView) findViewById(R.id.point);
		tvMypoint.setText("总积分:"+ point);
		tvTixiantip = (TextView) findViewById(R.id.tixiantip);
		mTvTip = (TextView) findViewById(R.id.tiptxt);
		mTvKyProint = (TextView) findViewById(R.id.kypoint);
		mTvinfoTop = (TextView) findViewById(R.id.infoTop);
		mTvKyProint.setText("可提现积分:"+ kypoint);
	}
	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			Log.d("TAG","onTextChanged--------------->");
			try {
				if (Integer.parseInt(etIntegral.getText().toString()) > kypoint) {
					showToast("最大输入积分金额为"+kypoint);
					return;
				}
			}catch (Exception e){
				showToast("请输入正确的积分！");

			}


		}
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submitP:

			if (TextUtils.isEmpty(etIntegral.getText().toString())) {
				showToast("带'*'项不能为空！");
				return;
			}
			if(FileUtil.isNumeric(etIntegral.getText().toString())){

				//2016-9-26 取消提现限制
//				if(Integer.parseInt(etIntegral.getText().toString())>(1000*100)){
//					showToast("一次最多提现1000元(即100000积分)！");
//					return;
//				}
				if(Integer.parseInt(etIntegral.getText().toString())>kypoint){
					showToast("最大输入积分金额为"+kypoint);
					return;
				}
				if (TextUtils.isEmpty(etIntegral.getText().toString())) {
					showToast("请输入要提现的积分！");
					return;
				}
				Pattern p = Pattern.compile("[0-9]*");
				Matcher m = p.matcher(etIntegral.getText().toString());
				if (!m.matches()) {
					showToast("输入的正确的积分！");
					return;
				}
//				if(Integer.parseInt(etIntegral.getText().toString())<5000){
//					showToast("提现积分不能小于5000！");
//					return;
//				}
//				if(Integer.parseInt(etIntegral.getText().toString())%1000 != 0){
//					showToast("提现积分需为1000的整数倍！");
//					return;
//
//				}
				isFlagCard();
//				sendData();
			}else{
				showToast("请输入正确的积分");
				return;
			}


			break;
		default:
			break;
		}
	}

	public void sendData() {

		showDialog();
		RequestParams params = new RequestParams();

		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("score", etIntegral.getText().toString());
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.INTEGRAL_TO_CASH_URL,
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

								final Dialog dialog = new Dialog(IntegraltocashActivity.this, "提示", jsonresult.optString("msg").toString());
								dialog.setOnAcceptButtonClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										dialog.cancel();
										setResult(IntegralActivity.REFRESH);
										finish();
									}
								});
								dialog.setOnCancelButtonClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										dialog.cancel();
										setResult(IntegralActivity.REFRESH);
										finish();
									}
								});
								dialog.show();


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

	public void isFlagCard() {
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_ISBINDING_URL,
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
						Log.i("insert", " insert responseInfo.result = "
								+ responseInfo.result);

						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								tvTixiantip.setText(jsonresult.optString("msg").toString());
								if(jsonresult.optString("isTixian").toString().equals("n")){
									showDialog1(IntegraltocashActivity.this, "当前无法提现,"+jsonresult.optString("msg").toString());
									etIntegral.setEnabled(false);
									btnSubmit.setEnabled(false);
									return;
								}

									if (jsonresult.optString("status").equals("2")) {
										final Dialog dialog = new Dialog(IntegraltocashActivity.this, "提示", "请确认您的银行卡信息：\n 银行卡号："+jsonresult.optString("bankNum").toString()+"\n账户姓名："+jsonresult.optString("bankName").toString());
										dialog.setCancelable(false);
										dialog.addCancelButton("修改信息");
										dialog.addAccepteButton("确认提现");
										dialog.setOnAcceptButtonClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												dialog.cancel();
												sendData();
											}
										});
										dialog.setOnCancelButtonClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												dialog.cancel();
												clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES1);
												clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES2);
												Intent locintent = new Intent(IntegraltocashActivity.this,ActivityWithdrawals.class);
												locintent.putExtra("type", "rz");
												startActivityForResult(locintent, 11);
											}
										});
										dialog.show();

									}else if(jsonresult.optString("status").equals("0")){
										String msg = "您未绑定银行卡,无法进行提现,请先绑定银行卡再进行提现。";
										Map<String,String> map = new HashMap<String, String>();
										map.put("type", "add");
										map.put("cardtype", "nyBank");
										jumpActivity(IntegraltocashActivity.this,ActivityWithdrawals.class,msg,map);
									} else {
//										AlertDialog.Builder builder = new Builder(
//												IntegraltocashActivity.this);
//										builder.setMessage("您的银行卡未签约或未确认签约，请用您的账号登录神州牧易商城(www.my360.cn)进行签约！");
//										builder.setTitle("提示");
//										builder.setCancelable(false);
//										builder.setPositiveButton("确定", null);
//										builder.create().show();
										showDialog1(IntegraltocashActivity.this,"您的银行卡未签约或未确认签约，请用您的账号登录神州牧易商城(www.my360.cn)进行签约！");
									}



//								}

								// setResult(WithdrawalsListActivity.RESULT_TYPE);
								// finish();
							} else {
								successType(jsonresult.get("success")
										.toString(), "查询失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure (HttpException error, String msg) {

						disDialog();
					}
				});
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, data);
//		isFlagCard("1");
	}
}
