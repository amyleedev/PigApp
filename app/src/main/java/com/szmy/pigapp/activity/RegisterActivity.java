package com.szmy.pigapp.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.szmy.pigapp.utils.MD5;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.ClearEditText;
import com.szmy.pigapp.widget.TypePopuWindow;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends BaseActivity {
	private String username, pwd, pwd2, phone;
	private LinearLayout mLlBack;
	private ClearEditText mUserEdit, mPwdEdit, mPwdEdit2, mPhoneEdit;
	private Button mRegisterBtn;
	private TextView mTvProtocol, mTvProtocol2;

//	private Spinner roleSpinner;
	private TextView mTvType;
	private int roles = 0;
	private String zctype = "0";
	private LinearLayout mLlJuese;
	private String url = "";
	private TypePopuWindow mPwDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		zctype = getIntent().getStringExtra("zctype");

		mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
		mUserEdit = (ClearEditText) findViewById(R.id.et_username);
		mPwdEdit = (ClearEditText) findViewById(R.id.et_password);
		mPwdEdit2 = (ClearEditText) findViewById(R.id.et_password2);
		mPhoneEdit = (ClearEditText) findViewById(R.id.et_phone);
		mLlBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		mLlJuese = (LinearLayout) findViewById(R.id.ll_jiaose);
		mRegisterBtn = (Button) findViewById(R.id.btn_refister);
		mRegisterBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loadData();
			}
		});
		mTvProtocol = (TextView) findViewById(R.id.tv_protocol);
		mTvProtocol2 = (TextView) findViewById(R.id.tv_protocol2);
		if (zctype.equals("1")) {
			mTvProtocol2.setVisibility(View.VISIBLE);
			mTvProtocol.setVisibility(View.GONE);
			mLlJuese.setVisibility(View.GONE);
		} else {
			mTvProtocol2.setVisibility(View.GONE);
			mTvProtocol.setVisibility(View.VISIBLE);
			mLlJuese.setVisibility(View.VISIBLE);
		}
		mTvProtocol.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				startActivity(new Intent(RegisterActivity.this,
//						RegisterProtocolActivity.class));
				Intent intent = new Intent(RegisterActivity.this,
						ActivityScanResult.class);
				intent.putExtra("url", UrlEntry.AGREEMENT_URL);
				startActivity(intent);
			}
		});
		mTvProtocol2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(RegisterActivity.this,
						ActivityScanResult.class);
				intent.putExtra("url", UrlEntry.AGREEMENT_URL);
				startActivity(intent);
			}
		});
		mTvType = (TextView) findViewById(R.id.sp_type);
		mTvType.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showPopDialog(mTvType);
			}
		});
//		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//				this, R.array.roles, android.R.layout.simple_spinner_item);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		roleSpinner.setAdapter(adapter);
//		roleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//									   int position, long arg3) {
//				System.out.println(position + " ");
//				// roles = arg0.getItemAtPosition(position).toString();
//				roles = position;// String.valueOf(position + 1);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});

	}

	private void loadData() {
		username = mUserEdit.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			showToast("用户名不能为空！");
			return;
		}
		pwd = mPwdEdit.getText().toString().trim();
		if (TextUtils.isEmpty(pwd)) {
			showToast("密码不能为空！");
			return;
		}
		pwd2 = mPwdEdit2.getText().toString().trim();
		if (TextUtils.isEmpty(pwd2)) {
			showToast("确认密码不能为空！");
			return;
		}
		if (!pwd.equals(pwd2)) {
			showToast("再次密码输入不一致！");
			return;
		}
		phone = mPhoneEdit.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			showToast("手机号不能为空！");
			return;
		}
		if (phone.length() != 11) {
			showToast("请输入11位手机号！");
			return;
		}
		if (!zctype.equals("1")) {
			if (roles == 0) {
				showToast("请选择角色！");
				return;
			}
		}
		showDialog();
		RequestParams params = new RequestParams();
		if (zctype.equals("1")) {
			url = UrlEntry.REISTER_SZMY_URL;
			params.addBodyParameter("e.source", "6");
			params.addBodyParameter("METHOD_CODE", "20161114_00001_00001");
		} else {
			url = UrlEntry.REISTER_URL;
			params.addBodyParameter("e.type", String.valueOf(roles));
		}
		String result = "";
		MD5 md = new MD5();
		params.addBodyParameter("e.username", mUserEdit.getText().toString());
		params.addBodyParameter("e.password",
				md.GetMD5Code(mPwdEdit.getText().toString()));
		params.addBodyParameter("e.phone", mPhoneEdit.getText().toString());
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,url, params,
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
						getDTOArray(responseInfo.result);
						Log.i("login", " login responseInfo.result = "
								+ responseInfo.result);

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("注册失败,请稍后再试！");
						disDialog();

					}
				});

	}

	public void getDTOArray(String result) {
		try {
			JSONObject jsonresult = new JSONObject(result);
			if (jsonresult.get("success").toString().equals("1")) {
				showToast("注册成功！");
				// Editor edit = sp.edit();
				// edit.putString("username", mUserEdit.getText().toString());
				// edit.commit();
				// finish();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("username", username);
				bundle.putString("password", pwd);
				intent.putExtras(bundle);
				setResult(LoginActivity.RESULT_REGISTER, intent);
				finish();
			} else {
				showToast("注册失败！" + jsonresult.optString("message").toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	private void showPopDialog(View parent) {

		// 自定义的单击事件
		TypePopuWindow.OnDialogListener paramOnClickListener = new TypePopuWindow.OnDialogListener() {

			@Override
			public void back(String name,int typeid) {
				// TODO Auto-generated method stub
//				showToast(name+"  "+typeid);
				roles = typeid;
				mTvType.setText(name);
			}
		};
		String[] typenames = {"猪场","屠宰场","经纪人","物流公司","自然人"};
		mPwDialog = new TypePopuWindow(RegisterActivity.this,
				paramOnClickListener, parent,3,"经纪人",typenames);
		mPwDialog.update();
	}
}
