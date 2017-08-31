package com.szmy.pigapp.zheshang;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;

/**
 * 开立/修改电子账户
 */
public class NewElectronicAccountsActivity extends BaseActivity implements
		OnClickListener {

	private EditText etCardName,etIdentityCard,etPhone,etMsgCode,etZhuzhanghu,etyuliutell,etAddress;
	private TextView tvCardType,tvZhengjianType,tvKaihuhang,tvStartdate,tvEnddate,tvKaihuhangName;
	private Button btnSubmit,mBtnYanzhengma;
	private String[] mItems = {"对公", "对私"};
	private int mSingleChoiceID = 0;

	private Calendar cal ;//= Calendar.getInstance();
	private static final int DATE_DIALOG_ID = 01;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private String type = "";
	private String UseType = "";
	private String ZhengjianType = "";
	private ElectronicAccounts mEaEntry;
	private MyCount mc;
	private String token = "";
	private final String METHOD_CODE = "20170318001001";
	private final String APIKEY_CODE = "szmy";
	private final String apiSecret = "nxt_szmy";
	private String caozuoleixing = "";
	private String yanzhengma ="" ;
	private String kaihuhang_code = "";
	private TextView mTvTitle;
	private Withdrawals mInfoEntry;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zheshangbank_opencard);
		if (getIntent().hasExtra("type"))
		caozuoleixing = getIntent().getStringExtra("type");
		mTvTitle = (TextView) findViewById(R.id.def_head_tv);
		mTvTitle.setText("开立电子账户");
		if (caozuoleixing.equals("upp")){
			caozuoleixing = "3";
			mTvTitle.setText("变更电子账户");
//			getToken("load");
		}else if (caozuoleixing.equals("add")){
			caozuoleixing = "1";
		}

		initView();
		loadData();
	}
	private void initView() {
		cal = Calendar.getInstance();
		mYear = cal.get(Calendar.YEAR);
		mMonth = cal.get(Calendar.MONTH);
		mDay = cal.get(Calendar.DAY_OF_MONTH);
		mHour = cal.get(Calendar.HOUR_OF_DAY);
		mMinute = cal.get(Calendar.MINUTE);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		etCardName = (EditText) findViewById(R.id.etCardName);
		etIdentityCard = (EditText) findViewById(R.id.etIdentityCard);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etMsgCode = (EditText) findViewById(R.id.etMsgCode);
		etZhuzhanghu = (EditText) findViewById(R.id.etZhuzhanghu);
		etyuliutell = (EditText) findViewById(R.id.etyuliutell);
		etAddress = (EditText) findViewById(R.id.etAddress);
		tvCardType = (TextView) findViewById(R.id.tvCardType);
		tvCardType.setOnClickListener(this);
		tvZhengjianType = (TextView) findViewById(R.id.tvZhengjianType);
		tvZhengjianType.setOnClickListener(this);
		tvKaihuhang = (TextView) findViewById(R.id.tvKaihuhang);
		tvKaihuhangName = (TextView) findViewById(R.id.tvKaihuhangName);
		tvKaihuhang.setOnClickListener(this);
		tvStartdate = (TextView) findViewById(R.id.tvStartdate);
		tvStartdate.setOnClickListener(this);
		tvEnddate = (TextView) findViewById(R.id.tvEnddate);
		tvEnddate.setOnClickListener(this);
		mBtnYanzhengma = (Button) findViewById(R.id.mBtnYanzhengma);
		mBtnYanzhengma.setOnClickListener(this);
		tvZhengjianType.setEnabled(false);

	}
private  void  setInitView(){
	etCardName.setText(mEaEntry.getAccountName());
	etCardName.setEnabled(false);
	etIdentityCard.setText(mEaEntry.getCertNo());
	etIdentityCard.setEnabled(false);
//	etAddress.setText(mEaEntry.getAddress());
	tvCardType.setText(mEaEntry.getAccountType().equals("1")?"对公":"对私");
	tvCardType.setEnabled(false);
	UseType = mEaEntry.getAccountType();
	ZhengjianType = mEaEntry.getCertType();
	if (UseType.equals("2")){
		tvZhengjianType.setEnabled(false);
		tvZhengjianType.setText("身份证");
	}else{
		tvZhengjianType.setEnabled(false);
		String[] arr = getResources().getStringArray(R.array.zhengjiantype);
		tvZhengjianType.setText(arr[Integer.parseInt(mEaEntry.getCertType())]);
	}
	etPhone.setText(mEaEntry.getMobile());
	etPhone.setEnabled(false);
	etZhuzhanghu.setText(mEaEntry.getMainAccountNo());
	tvKaihuhang.setText(mEaEntry.getMainAccountBankName());
	tvKaihuhangName.setText(mEaEntry.getBandName());
	kaihuhang_code = mEaEntry.getMainAccountBranchNo();
}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			if (TextUtils.isEmpty(tvCardType.getText().toString())||TextUtils.isEmpty(etCardName.getText().toString())
					||TextUtils.isEmpty(tvZhengjianType.getText().toString())||TextUtils.isEmpty(etIdentityCard.getText().toString())||TextUtils.isEmpty(etPhone.getText().toString())
					||TextUtils.isEmpty(etMsgCode.getText().toString())||TextUtils.isEmpty(etZhuzhanghu.getText().toString())||TextUtils.isEmpty(tvKaihuhang.getText().toString())
					) {
				showToast("带'*'项不能为空！");
				return;
			}
			if (tvCardType.getText().toString().equals("对私")){
				if (etIdentityCard.getText().toString().trim().length()!=18){
					showToast("请输入18位身份证号码");
					return;
				}
				String msg = FileUtil.IDCardValidate(etIdentityCard.getText().toString());
				if (!msg.equals("")){
					showToast(msg);
					return;
				}
			}
			if (etPhone.getText().toString().length()!=11){
				showToast("请输入11位手机号码");
				return;
			}

//			getToken("");
			sendData();
			break;
			case R.id.tvCardType:
				AlertDialog.Builder builder = new AlertDialog.Builder(
						NewElectronicAccountsActivity.this);

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

								UseType = String.valueOf(mSingleChoiceID + 1);

								tvCardType.setText(mItems[mSingleChoiceID]);
								if (UseType .equals("2")){//对私
									if (mInfoEntry!=null){
										etIdentityCard.setText(mInfoEntry.getSfzh());
									}
									tvZhengjianType.setText("身份证");
									tvZhengjianType.setEnabled(false);
									ZhengjianType = "1";
								}else{
									tvZhengjianType.setEnabled(true);
									ZhengjianType = "";
									tvZhengjianType.setText("");
								}

							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int whichButton) {

							}
						});
				builder.create().show();
				break;
			case R.id.tvZhengjianType:
				 builder = new AlertDialog.Builder(
						NewElectronicAccountsActivity.this);

				mSingleChoiceID = 0;
				builder.setTitle("请选择");
				builder.setSingleChoiceItems(R.array.zhengjiantype, 0,
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
								ZhengjianType = String.valueOf(mSingleChoiceID + 1);
								String[] arr = getResources().getStringArray(R.array.zhengjiantype);
								tvZhengjianType.setText(arr[mSingleChoiceID]);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int whichButton) {

							}
						});
				builder.create().show();
				break;
			case R.id.tvKaihuhang:
				Intent intent = new Intent(NewElectronicAccountsActivity.this,KaihuhangChoose.class);
				startActivityForResult(intent,App.KAIHUHANG_CODE);
				break;
			case R.id.tvStartdate:
				showDateDialog(tvStartdate);
				break;
			case R.id.tvEnddate:
				showDateDialog(tvEnddate);
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
		default:
			break;
		}
	}

	public void sendData() {
		showDialog();
		String url = "";
		RequestParams params = new RequestParams();
		if (caozuoleixing.equals("1")) {
			url = UrlEntry.ELECTRONICACCOUNTS_URL;
			params.addBodyParameter("certType", ZhengjianType);
			params.addBodyParameter("certNo", etIdentityCard.getText().toString());
			params.addBodyParameter("accountName", etCardName.getText().toString());
			params.addBodyParameter("accountType", UseType);
			params.addBodyParameter("mobile", etPhone.getText().toString());

		}else{
			url = UrlEntry.UPP_ELECTRONICACCOUNTS_URL;
		}
		params.addBodyParameter("mainAccountNo",etZhuzhanghu.getText().toString());
//		params.addBodyParameter("mainAccountBranchNo",kaihuhang_code);//103100000026
		params.addBodyParameter("mainAccountBranchNo","301491000031");//103100000026
		params.addBodyParameter("mobileCode",etMsgCode.getText().toString());
		params.addBodyParameter("mobileSerial",yanzhengma);
		params.addBodyParameter("bankName",tvKaihuhangName.getText().toString());
		params.addBodyParameter("uuid",App.uuid);

		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url,
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
		switch (resultCode) {
			case App.KAIHUHANG_CODE:
				if (data == null)
					return;
				bundle = data.getExtras();
				kaihuhang_code = String.valueOf(bundle.get("kaihuhangcode"));
				tvKaihuhang.setText(String.valueOf(bundle.get("kaihuhangname")));
				tvKaihuhangName.setText(String.valueOf(bundle.get("yinhangka")));
				break;
		}
	}
	private void showDateDialog(final TextView mTvView) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(NewElectronicAccountsActivity.this,
				R.layout.dialog_date, null);
		final DatePicker datePicker = (DatePicker) view
				.findViewById(R.id.date_picker);
		builder.setView(view);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				(cal.get(Calendar.DAY_OF_MONTH)), null);

			String date = mTvView.getText().toString();
			String[] dates=date.split("-");
			if (TextUtils.isEmpty(date)||dates.length!=3) {
				datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						(cal.get(Calendar.DAY_OF_MONTH)), null);
			}else {
				datePicker.init(Integer.parseInt(dates[0]),(Integer.parseInt(dates[1])-1),Integer.parseInt(dates[2]),null);
			}

		builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				datePicker.clearFocus();
				StringBuffer sb = new StringBuffer();
				sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),
						datePicker.getMonth(), datePicker.getDayOfMonth()));

				mTvView.setText(sb.toString());

				dialog.cancel();
			}
		});
		android.app.Dialog dialog = builder.create();
		dialog.show();
	}
	private void loadData() {
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_ELECTRONICACCOUNTS_URL,
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
								caozuoleixing = "3";
								mTvTitle.setText("变更电子账户");
								GsonBuilder gsonb = new GsonBuilder();
								Gson gson = gsonb.create();
								mEaEntry = new ElectronicAccounts();
								mEaEntry = gson.fromJson(jsonresult.toString(),ElectronicAccounts.class);
								setInitView();


							}  else if(jsonresult.optString("success").toString()
									.equals("2")){//新增
								caozuoleixing = "1";
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
			mBtnYanzhengma.setText("重新获取(" + millisUntilFinished / 1000 + ")");

		}
	}

	//获取验证码
	private void getYanzhengma(){
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("smsType", caozuoleixing);
		params.addBodyParameter("uuid", App.uuid);
		if (caozuoleixing.equals("1")){
			params.addBodyParameter("mobileMark", etPhone.getText().toString());
		} if(caozuoleixing.equals("3")){
//			params.addBodyParameter("mobileMark", mEaEntry.geteCardSerialNo());
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
								tvKaihuhangName.setText(mInfoEntry.getBankName());
								etCardName.setText(mInfoEntry.getName());
								etZhuzhanghu.setText(mInfoEntry.getBankNum());
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
