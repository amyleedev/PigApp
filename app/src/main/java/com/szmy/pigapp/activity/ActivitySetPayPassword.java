package com.szmy.pigapp.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.MD5;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.utils.ZmtSignBean;
import com.szmy.pigapp.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

/**
 * 重置支付密码
 * 
 * @author Administrator
 * 
 */
public class ActivitySetPayPassword extends BaseActivity implements
		OnClickListener {

	private ClearEditText et_password_old, et_password_new, et_password_again;
	private TextView mTvTitle;
	private ClearEditText mEtLoginPwd;	
	private ClearEditText mEtMsg;
	private Button mBtnMsg;
	private TextView mTvTip;
	private String code = "";	
	private MyCount mc;
	private LinearLayout mLlPwd;
	private LinearLayout mLlMsg;
	private LinearLayout mLlOldPwd;
	private String type = "";
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.update_paypwd);
		initView();

	}

	public void initView() {
		mLlPwd = (LinearLayout) findViewById(R.id.layout1);
		mLlOldPwd = (LinearLayout) findViewById(R.id.layoutoldpwd);
		et_password_old = (ClearEditText) findViewById(R.id.et_password);
		et_password_old.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
		et_password_old.setHint("请输入原支付密码,没有请忽略不填");
		et_password_old
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });

		et_password_new = (ClearEditText) findViewById(R.id.et_password2);
		et_password_new.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
		et_password_new.setHint("请输入新支付密码");
		et_password_new
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
		et_password_again = (ClearEditText) findViewById(R.id.et_password3);
		et_password_again.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
		et_password_again
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
		et_password_again.setHint("请输入确认支付密码");
		mEtLoginPwd = (ClearEditText) findViewById(R.id.et_loginpassword);
		mLlMsg = (LinearLayout) findViewById(R.id.layoutmsg);
		mEtMsg = (ClearEditText) findViewById(R.id.et_msgcode);
		mEtMsg.setOnClickListener(this);
		mBtnMsg = (Button) findViewById(R.id.sendmsgBtn);
		mBtnMsg.setOnClickListener(this);
		mTvTip = (TextView) findViewById(R.id.phonetip);
		if (App.userphone.length() < 11) {
			return;
		} else
			mTvTip.setText("请输入"
					+ App.userphone.substring(0, App.userphone.length()
							- (App.userphone.substring(3)).length()) + "****"
					+ App.userphone.substring(7) + "收到的短信验证码。");
		
		if (getIntent().hasExtra("type")) {
			type = getIntent().getStringExtra("type").toString();
			if (type.equals("set")) {
				mLlMsg.setVisibility(View.VISIBLE);
				mTvTip.setVisibility(View.VISIBLE);
				mLlOldPwd.setVisibility(View.GONE);
			}else if(type.equals("add")){
				mLlPwd.setVisibility(View.VISIBLE);
				mLlOldPwd.setVisibility(View.GONE);
				mLlMsg.setVisibility(View.GONE);
			}else if(type.equals("forgot")){//忘记密码
				mLlPwd.setVisibility(View.GONE);
				mLlMsg.setVisibility(View.VISIBLE);
				mLlOldPwd.setVisibility(View.GONE);
			} 
		}else {
			mLlMsg.setVisibility(View.GONE);
			mTvTip.setVisibility(View.GONE);
			mLlOldPwd.setVisibility(View.VISIBLE);
			
		}
	}

	public void onSubmit(View view) {
		String oldpwd = et_password_old.getText().toString().trim();
		if (TextUtils.isEmpty(mEtLoginPwd.getText().toString())) {
			showToast("登录密码不能为空！");
			return;
		}
		String newpwd = et_password_new.getText().toString().trim();
		if (TextUtils.isEmpty(newpwd)) {
			showToast("新支付密码不能为空！");
			return;
		}
		if (newpwd.length() < 6) {
			showToast("支付密码不能少于6位！");
			return;
		}
		String newagainpwd = et_password_again.getText().toString().trim();
		if (TextUtils.isEmpty(newagainpwd)) {
			showToast("确认支付密码不能为空！");
			return;
		}
		if (!newpwd.equals(newagainpwd)) {
			showToast("两次输入的新支付密码不一致，请检查！");
			return;
		}
		
		sendData(oldpwd, newpwd);
	}
	
	public void onNext(View view){
		 if(TextUtils.isEmpty(mEtMsg.getText().toString())){
			 showToast("请输入验证码。");
			 return;
		 }
			if (!mEtMsg.getText().toString().equals(code)) {
				showToast("验证码输入错误！");
				return;
			}
			hideInput();
		mLlMsg.setVisibility(View.GONE);
		mLlPwd.setVisibility(View.VISIBLE);
	}

	private void sendData(String str, String str1) {
		hideInput();
		showDialog();
		Map<String, String> maplist = new TreeMap<String, String>();

		RequestParams params = new RequestParams();
		if(type.equals("forgot")||type.equals("add")){
			maplist.put("type", "3");
			params.addBodyParameter("type", "3");
		}
		if (!str.equals("")) {
			maplist.put("oldPayPassword", MD5.GetMD5Code(str));
			params.addBodyParameter("oldPayPassword", MD5.GetMD5Code(str));
		} 
		maplist.put("password",
				MD5.GetMD5Code(mEtLoginPwd.getText().toString()));
		params.addBodyParameter("password",
				MD5.GetMD5Code(mEtLoginPwd.getText().toString()));		
		maplist.put("newPayPassword", MD5.GetMD5Code(str1));
		params.addBodyParameter("newPayPassword", MD5.GetMD5Code(str1));
		
		maplist.put("uuid", App.uuid);
		System.out.println(maplist + "分割" + ZmtSignBean.sign(maplist,ZmtSignBean.secret));
		params.addBodyParameter("api_sign", ZmtSignBean.sign(maplist,ZmtSignBean.secret));
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.SET_PAYPWD_URL, params,
				new RequestCallBack<String>() {
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
						Log.i("uppwd", " uppwd responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								showToast("重置成功！");
								// onBackPressed();
								finish();
							} else if (jsonresult.optString("success")
									.toString().equals("2")) {
								showToast("重置支付密码失败,原因："
										+ jsonresult.optString("msg"));
							} else if (jsonresult.optString("success").equals(
									"7")) {
								
								if (Integer.parseInt(jsonresult
										.optString("errorNum")) >= 5) {
									showDialog1(ActivitySetPayPassword.this, "重置支付密码失败,原因：原支付密码已连续输入错误五次，需要验证您的手机进行重置支付密码。");
									mLlMsg.setVisibility(View.VISIBLE);
								mLlPwd.setVisibility(View.GONE);
								}else{
									showDialog1(ActivitySetPayPassword.this, "重置支付密码失败,原因：原支付密码输入错误");
								}

							} else {
								successType(jsonresult.get("success")
										.toString(), "重置失败！");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("重置失败,请稍后再试！");
						disDialog();
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sendmsgBtn:
			if (App.userphone.length() == 11) {
				mc = new MyCount(60000, 1000);   
			     mc.start();
			     mBtnMsg.setEnabled(false);
				code = FileUtil.randString(4);
				new Thread() {
					public void run() {
						
					    
						HttpUtil hu = new HttpUtil();
						System.out.println(code);
						hu.sendSMS("尊敬的猪贸通用户，您的验证码是：" + code + "，欢迎使用！",
								App.userphone);
					}
				}.start();

			} else {
				showToast("您的手机号码有误，请到实名认证修改手机号码！");
			}
			break;
		default:
			break;
		}

	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			mBtnMsg.setEnabled(true);
			mBtnMsg.setText("发送验证码");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			mBtnMsg.setText("重新发送(" + millisUntilFinished / 1000 + ")");

		}
	}
}
