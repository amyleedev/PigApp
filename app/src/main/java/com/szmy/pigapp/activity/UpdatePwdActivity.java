package com.szmy.pigapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.gc.materialdesign.views.ButtonIcon;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.MD5;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePwdActivity extends BaseActivity {

	private ClearEditText et_password_old, et_password_new, et_password_again;
	private ButtonIcon mLlBack;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.update_pwd);
		et_password_old = (ClearEditText) findViewById(R.id.et_password);
		et_password_new = (ClearEditText) findViewById(R.id.et_password2);
		et_password_again = (ClearEditText) findViewById(R.id.et_password3);
		mLlBack = (ButtonIcon) findViewById(R.id.def_head_back1);
		mLlBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("type","back");
				intent.putExtras(bundle);
				setResult(911,intent);
				finish();
			}
		});
	}

	public void onSubmit(View view) {
		String oldpwd = et_password_old.getText().toString().trim();
		if (TextUtils.isEmpty(oldpwd)) {
			showToast("旧密码不能为空！");
			return;
		}
		String newpwd = et_password_new.getText().toString().trim();
		if (TextUtils.isEmpty(newpwd)) {
			showToast("新密码不能为空！");
			return;
		}
		String newagainpwd = et_password_again.getText().toString().trim();
		if (TextUtils.isEmpty(newagainpwd)) {
			showToast("确认密码不能为空！");
			return;
		}
		if (!newpwd.equals(newagainpwd)) {
			showToast("两次输入的新密码不一致，请检查！");
			return;
		}
		sendData("", oldpwd, newpwd);
	}

	private void sendData(final String type, String str, String str1) {
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("e.password", (new MD5()).GetMD5Code(str));
		params.addBodyParameter("newPassword", (new MD5()).GetMD5Code(str1));
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.UPDATE_USERINFO_URL,
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
						Log.i("uppwd", " uppwd responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								showToast("修改成功！");
								Intent intent = new Intent();
								Bundle bundle = new Bundle();
								bundle.putString("type","ok");
								intent.putExtras(bundle);
								setResult(911,intent);
								finish();
							} else {
								successType(jsonresult.get("success")
										.toString(), "修改失败！");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("修改失败,请稍后再试！");
						disDialog();
					}
				});
	}

}
