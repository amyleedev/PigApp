package com.szmy.pigapp.zheshang;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.szmy.pigapp.tixian.Withdrawals;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 代收协议签约/修改
 */
public class NewAgreementsigningActivity extends BaseActivity implements
		OnClickListener {

	private EditText etCardName,etIdentityCard,etPhone,etMsgCode,etCardNum,etZkkxe,etDbkkxe,etRljkkxe;
	private Button btnSubmit,mBtnYanzhengma;
	private int mSingleChoiceID = 0;


	private ElectronicAccounts mEaEntry;
	private MyCount mc;
	private String token = "";
	private final String METHOD_CODE = "20170318001001";
	private final String APIKEY_CODE = "szmy";
	private String caozuoleixing = "";
	private String yanzhengma ="" ;
	private TextView mTvTitle;
	private LinearLayout mLlMcxe,mLlMrxe,mLlZed;
	private TextView tvKaihuhang;
	private Withdrawals mInfoEntry;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daishouxieyi);
		if (getIntent().hasExtra("type"))
		caozuoleixing = getIntent().getStringExtra("type");
		mTvTitle =(TextView) findViewById(R.id.def_head_tv);
		mTvTitle.setText("代收协议签约");
		initView();
		if (caozuoleixing.equals("upp")){
			caozuoleixing = "6";
//			getToken("load");
			mTvTitle.setText("代收协议修改");
		}else if (caozuoleixing.equals("add")){
			caozuoleixing = "4";

		}

		loadData();
	}
	private void initView() {
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		etCardName = (EditText) findViewById(R.id.etCardName);
		etIdentityCard = (EditText) findViewById(R.id.etIdentityCard);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etMsgCode = (EditText) findViewById(R.id.etMsgCode);
		etCardNum = (EditText) findViewById(R.id.etCardNum);
		mBtnYanzhengma = (Button) findViewById(R.id.mBtnYanzhengma);
		mBtnYanzhengma.setOnClickListener(this);
		etZkkxe = (EditText) findViewById(R.id.etzkkxe);
		etDbkkxe = (EditText) findViewById(R.id.etdbkkxe);
		etRljkkxe = (EditText) findViewById(R.id.etrljkkxe);
		mLlMrxe = (LinearLayout) findViewById(R.id.mtxelayout);
		mLlMcxe = (LinearLayout) findViewById(R.id.dbkkxelayout);
		mLlZed = (LinearLayout) findViewById(R.id.zkkxelayout);
		tvKaihuhang = (TextView) findViewById(R.id.tvKaihuhang);
		tvKaihuhang.setOnClickListener(this);
	}
private  void  setInitView(){

	etCardName.setText(mEaEntry.getAccountName());
	etIdentityCard.setText(mEaEntry.getIdentityNo());
	etPhone.setText(mEaEntry.getMobile());
	etCardNum.setText(mEaEntry.getAccountNo());
	etZkkxe.setText(mEaEntry.getTotalLimit());
	etDbkkxe.setText(mEaEntry.getSingleLimit());
	etRljkkxe.setText(mEaEntry.getDailyLimit());
	tvKaihuhang.setText(mEaEntry.getBankName());
	mLlMrxe.setVisibility(View.VISIBLE);
	mLlMcxe.setVisibility(View.VISIBLE);
	mLlZed.setVisibility(View.VISIBLE);
	etCardNum.setEnabled(false);
	etIdentityCard.setEnabled(false);
	etCardName.setEnabled(false);
	etPhone.setEnabled(false);
	tvKaihuhang.setEnabled(false);
}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			if (TextUtils.isEmpty(etCardName.getText().toString())
					||TextUtils.isEmpty(etIdentityCard.getText().toString())||TextUtils.isEmpty(etPhone.getText().toString())
					||TextUtils.isEmpty(etMsgCode.getText().toString())||TextUtils.isEmpty(etCardNum.getText().toString())) {
				showToast("带'*'项不能为空！");
				return;
			}
			if (caozuoleixing.equals("6")){
				if (TextUtils.isEmpty(etZkkxe.getText().toString())||TextUtils.isEmpty(etDbkkxe.getText().toString())
						||TextUtils.isEmpty(etRljkkxe.getText().toString())){
					showToast("带'*'项不能为空！");
					return;
				}
			}
			if (etIdentityCard.getText().toString().trim().length()!=18){
				showToast("请输入18位身份证号码");
				return;
			}
			String msg = FileUtil.IDCardValidate(etIdentityCard.getText().toString());
			if (!msg.equals("")){
				showToast(msg);
				return;
			}
			if (etPhone.getText().toString().length()!=11){
				showToast("请输入11位手机号码");
				return;
			}

			sendData();
			break;



			case R.id.mBtnYanzhengma:
				if(TextUtils.isEmpty(etPhone.getText().toString())||etPhone.getText().toString().trim().length()!=11){
					showToast("请输入正确的手机号码！");
					return;
				}
				mc = new MyCount(60000, 1000);
				mc.start();
				hideInput();
				mBtnYanzhengma.setEnabled(false);
//				getToken("yanzhengma");
				getYanzhengma();
				break;
			case R.id.tvKaihuhang:
				Intent intent = new Intent(NewAgreementsigningActivity.this, KaihuhangChoose.class);
				intent.putExtra("tixian","true");
				startActivityForResult(intent,App.KAIHUHANG_CODE);
				break;
		default:
			break;
		}
	}

	public void sendData() {
		showDialog();
		RequestParams params = new RequestParams();
	String url = "";
		if (caozuoleixing.equals("4")){
			url = UrlEntry.AGREEMENTSIGNING_URL;
			params.addBodyParameter("identityNo",etIdentityCard.getText().toString());
			params.addBodyParameter("accountName",etCardName.getText().toString());
			params.addBodyParameter("mobile",etPhone.getText().toString());
			params.addBodyParameter("accountNo",etCardNum.getText().toString());
			params.addBodyParameter("bankName",tvKaihuhang.getText().toString());
		}else {
			url = UrlEntry.UPP_AGREEMENTSIGNING_URL;
		}

		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("totalLimit",etZkkxe.getText().toString());
		params.addBodyParameter("singleLimit",etDbkkxe.getText().toString());
		params.addBodyParameter("dailyLimit",etRljkkxe.getText().toString());
		params.addBodyParameter("mobileSerial",yanzhengma);
		params.addBodyParameter("mobileCode",etMsgCode.getText().toString());

		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,url,
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
								showToast("操作成功");
//								setResult(99);
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


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Bundle bundle;
		switch (requestCode) {
			case App.KAIHUHANG_CODE:
				if (data == null)
					return;
				 bundle = data.getExtras();
				tvKaihuhang.setText(String.valueOf(bundle.get("yinhangka")));
				break;
		}
	}

	private void loadData() {
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_AGREEMENTSIGNING_URL,
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
								caozuoleixing = "6";
								mTvTitle.setText("代收协议修改");
								GsonBuilder gsonb = new GsonBuilder();
								Gson gson = gsonb.create();
								mEaEntry = new ElectronicAccounts();
								mEaEntry = gson.fromJson(jsonresult.toString(),ElectronicAccounts.class);
								setInitView();


							}  else if(jsonresult.optString("success").toString()
									.equals("2")){
								caozuoleixing = "4";
								sendDataByUUid();
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
	/*定义一个倒计时的内部类*/
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		@Override
		public void onFinish() {
			mBtnYanzhengma.setEnabled(true);
			mBtnYanzhengma.setText("获取验证码");
		}
		@Override
		public void onTick(long millisUntilFinished) {
			mBtnYanzhengma.setText("重新发送(" + millisUntilFinished / 1000 + ")");

		}
	}

//获取验证码
	private void getYanzhengma(){
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("smsType", caozuoleixing);
		params.addBodyParameter("uuid", App.uuid);

		if (caozuoleixing.equals("4")){
			params.addBodyParameter("mobileMark", etPhone.getText().toString());
		} if(caozuoleixing.equals("6")){
//			params.addBodyParameter("mobileMark", mAsEntry.getCollectionSerialNo());

		}

		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_MESSAGE_CODE,
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
						Log.i("yanzhengma", " smrz responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult = null;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.optString("success").toString()
									.equals("1")) {
								yanzhengma = jsonresult.optString("msg");
								etMsgCode.setText(String.valueOf(yanzhengma));
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
	private void sendDataByUUid() {
		showDialog();
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
						Log.i("insert", " insert responseInfo.result = "
								+ responseInfo.result);

						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								// showToast("查询成功！");
								GsonBuilder gsonb = new GsonBuilder();
								Gson gson = gsonb.create();
								mInfoEntry = gson.fromJson(
										jsonresult.toString(),
										Withdrawals.class);
								tvKaihuhang.setText(mInfoEntry.getBankName());
								etCardName.setText(mInfoEntry.getName());
								etCardNum.setText(mInfoEntry.getBankNum());
							} else if (jsonresult.get("success").toString()
									.equals("5")) {
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
}
