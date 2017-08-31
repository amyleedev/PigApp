package com.szmy.pigapp.sign;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.activity.IntegralActivity;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

public class SignActivity extends BaseActivity implements OnClickListener {
	private TextView mTvSignTitle;
	private Button mTvSignBtn;
	private TextView mTvNum;
	private TextView mTvIntegral;
	private PopupSign mPopupSign;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_sign);
		initView();
		isSign();
	}
	
	private void initView(){
		mTvSignTitle = (TextView) findViewById(R.id.sign_title);
		mTvSignTitle.setOnClickListener(this);
		mTvSignBtn = (Button) findViewById(R.id.sign_btn);
		mTvSignBtn.setOnClickListener(this);
		mTvNum = (TextView) findViewById(R.id.sign_num);
		mTvIntegral = (TextView) findViewById(R.id.tv_myintegral);
		mTvIntegral.setOnClickListener(this);
		mPopupSign = new PopupSign(SignActivity.this, this);
		mTvSignBtn.setEnabled(false);
		mTvSignTitle.setText("点击签到");
		mTvSignBtn.setBackgroundColor(getResources().getColor(R.color.gray));
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.sign_btn:
			if (isLogin(SignActivity.this)) {
				showToast("请先登录！");
			} else {
				addSign();
			}
			break;
		case R.id.tv_check:
			mPopupSign.dismiss();
		case R.id.tv_myintegral:
			if (isLogin(SignActivity.this)) {
				showToast("请先登录！");
			} else {
			startActivity(new Intent(SignActivity.this, IntegralActivity.class));
			}
			break;
		
		
		
		}
	}
	
	/**
	 * 查询是否签到
	 */
	private void isSign(){
		mTvSignBtn.setEnabled(false);
		mTvSignBtn.setBackgroundColor(getResources().getColor(R.color.gray));
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,
				UrlEntry.QUERY_ISSIGN_URL, params,
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
						System.out.println("sign"+responseInfo.result);
						disDialog();
						try {
							JSONObject jsonresult = new JSONObject(responseInfo.result);
							if(jsonresult.optString("success").equals("1")){
								if(jsonresult.optString("status").equals("y")){
									String num = jsonresult.optString("signNum").toString();
									mTvNum.setText("您已连续签到"+(TextUtils.isEmpty(num)?"0":num)+"天,继续努力吧!");
									mTvSignBtn.setEnabled(false);
									mTvSignTitle.setText("今天已签到");
									mTvSignBtn.setBackgroundColor(getResources().getColor(R.color.gray));
								}else{
									mTvSignBtn.setBackgroundColor(getResources().getColor(R.color.red));
									String num = jsonresult.optString("signNum").toString();
									mTvNum.setText("您已连续签到"+(TextUtils.isEmpty(num)?"0":num)+"天,继续努力吧!");
									mTvSignBtn.setEnabled(true);
									mTvSignTitle.setText("今天还没签到");
								}
							}else if (jsonresult.optString("success").equals("0")){
								showToast("用户信息错误,请重新登录！");
								relogin(SignActivity.this);
							}else{
								showToast("查询失败，请稍后再试");
							}
						} catch (JSONException e) {
						}	
					}

					@Override
					public void onFailure(HttpException error, String msg) {
			
					}
				});
	}
	

	private void addSign(){
		mTvSignBtn.setEnabled(false);
		mTvSignBtn.setBackgroundColor(getResources().getColor(R.color.gray));
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,
				UrlEntry.ADD_SIGN_URL, params,
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

						System.out.println("addsign"+responseInfo.result);
						try {
							JSONObject jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.optString("success").equals("3")){
								mTvSignBtn.setEnabled(true);
								mTvSignBtn.setBackgroundColor(getResources().getColor(R.color.red));
								successType(jsonresult.optString("success"),"签到失败！");
							}else if(jsonresult.optString("success").equals("1")){
								if(jsonresult.optString("status").equals("y")){
									isSign();
									mPopupSign.getmHolder().tvPoint.setText("您获得了"
											+ jsonresult.optString("integral")
											+ "积分");
									mPopupSign.showPopupWindow(findViewById(R.id.ll_main));
								}else{
									mTvSignBtn.setEnabled(false);
									mTvSignBtn.setBackgroundColor(getResources().getColor(R.color.gray));
									successType(jsonresult.optString("success"),"签到失败！");
								}
							}else{
								mTvSignBtn.setEnabled(true);
								mTvSignBtn.setBackgroundColor(getResources().getColor(R.color.red));
								successType(jsonresult.optString("success"),"签到失败！");
							}

						} catch (JSONException e) {
						}	
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						disDialog();
						mTvSignBtn.setEnabled(true);
						mTvSignBtn.setBackgroundColor(getResources().getColor(R.color.red));
					}
				});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		isSign();
	}
}
