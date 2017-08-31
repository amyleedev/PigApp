package com.szmy.pigapp.activity;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gc.materialdesign.widgets.Dialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 忘记密码
 * @author Administrator
 *
 */
public class ActivityForgotPassword extends BaseActivity {

	

	private ClearEditText mUserName,mMsgCode;
	private Button mSendMsgBtn;
	private MyCount mc;
	private String uuid,phone,code;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_forgotpassword);
		initView();
	}
	
	private void initView(){
		mUserName = (ClearEditText) findViewById(R.id.et_username);
		mMsgCode = (ClearEditText) findViewById(R.id.et_msgcode);
		mSendMsgBtn = (Button) findViewById(R.id.sendmsgBtn);
		
	}
	
	public void onSendMsg(View view){
		if(TextUtils.isEmpty(mUserName.getText().toString())){
			showToast("账号信息不能为空！");
			return;
		}		
	     mc = new MyCount(60000, 1000);   
	     mc.start(); 
		hideInput();
		view.setEnabled(false);
	     sendData();
		
		
	}
	public void onSubmit(View view){
		if(TextUtils.isEmpty(mUserName.getText().toString())){
			showToast("账号信息不能为空！");
			return;
		}
		if(TextUtils.isEmpty(mMsgCode.getText().toString())){
			showToast("验证码不能为空！");
			return;
		}
		if(!mMsgCode.getText().toString().equals(code)){
			showToast("验证码输入错误！");
			return;
		}
		hideInput();
		resetpwd();
	}

	
	/*定义一个倒计时的内部类*/   
    class MyCount extends CountDownTimer {      
        public MyCount(long millisInFuture, long countDownInterval) {      
            super(millisInFuture, countDownInterval);      
        }      
        @Override      
        public void onFinish() {   
        	mSendMsgBtn.setEnabled(true);
        	mSendMsgBtn.setText("发送验证码");         
        }      
        @Override      
        public void onTick(long millisUntilFinished) {      
        	mSendMsgBtn.setText("重新发送(" + millisUntilFinished / 1000 + ")");      
           
        }     
    }   
    
    private void sendData() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("e.username",mUserName.getText().toString());
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_UUID_BYNAME_URL,
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
						Log.i("uuid", " uuid responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.optString("success").toString()
									.equals("1")) {
								uuid = jsonresult.optString("uuid").toString();
								phone = jsonresult.optString("phone").toString();
								if(TextUtils.isEmpty(phone)){
									showToast("您的账号未绑定手机号,请联系客服进行修改！");
									return;
								}
								
								new Thread(){
									public void run(){
										code =FileUtil.randString(4);
										HttpUtil hu = new HttpUtil();
										System.out.println(code);
										 hu.sendSMS("尊敬的猪贸通用户，您的验证码是："+code+"，欢迎使用！", phone);
									}
								}.start();								
							} else {
								showDialog1(ActivityForgotPassword.this,"抱歉，未找到该账号信息!");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("网络连接失败,请稍后再试！");
					}
				});
	}
    private void resetpwd() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid",uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, getIntent().getStringExtra("url"),
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
						Log.i("uuid", " uuid responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.optString("success").toString()
									.equals("1")) {
//								AlertDialog.Builder builder = new Builder(ActivityForgotPassword.this);
//								builder.setMessage("重置成功，新密码会以短信形式发送到您的手机，请查收！");
//								builder.setTitle("提示");
//								builder.setPositiveButton("确认",new AlertDialog.OnClickListener(){
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										finish();
//									}
//								});
//								builder.setNegativeButton("取消", null);
//								builder.create().show();

								final Dialog dialog = new Dialog(ActivityForgotPassword.this, "提示", "登录密码重置成功，新密码会以短信形式发送到您的手机，请查收！");
								dialog.addCancelButton("取消");
								dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										finish();
									}
								});
								dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										dialog.cancel();
									}
								});
								dialog.show();
							} else {
								showDialog1(ActivityForgotPassword.this,"抱歉，重置密码失败!");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("网络连接失败,请稍后再试！");
						
					}
				});
	}

    
 
    
}
