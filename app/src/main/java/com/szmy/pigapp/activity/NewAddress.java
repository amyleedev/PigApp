package com.szmy.pigapp.activity;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.AddressInfo;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class NewAddress extends BaseActivity {

	private EditText mEtName;
	private EditText mEtPhone;
	private TextView mEtArea;
	private EditText mEtYB;
	private EditText mEtAddress;
	private ToggleButton mTbDefault;
	private Button mBtnSubmit;
	private String provinces;

	private AddressInfo addressinfo;

	private String shengcode = "", shicode = "", qucode = "";
	private String shengname = "", shiname = "", quname = "";
	private String id = "";
	public static final int LOCATION_ID = 99;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.my_newaddress);
		Intent i = getIntent();
		provinces = i.getStringExtra("address");
		Bundle b = i.getBundleExtra("address_id");

		if (b == null) {
			((TextView) findViewById(R.id.def_head_tv)).setText("新增收货地址");

		} else {
			((TextView) findViewById(R.id.def_head_tv)).setText("修改收货地址");
		}

		mEtName = (EditText) this.findViewById(R.id.et_name);
		mEtPhone = (EditText) this.findViewById(R.id.et_mobile);
		mEtArea = (TextView) this.findViewById(R.id.et_area);
		mEtAddress = (EditText) findViewById(R.id.et_address);
		mEtYB = (EditText) findViewById(R.id.et_youbian);
		mTbDefault = (ToggleButton) findViewById(R.id.tb_default);

		mBtnSubmit = (Button) this
				.findViewById(R.id.my_set_buyaddress_address_btn);

		mEtArea.setOnClickListener(click);
		mBtnSubmit.setOnClickListener(click);

		if (b != null) {
			addressinfo = (AddressInfo) b.get("address");
			id = addressinfo.getId();
			shengcode = addressinfo.getProvince();
			shicode = addressinfo.getCity();
			qucode = addressinfo.getArea();

			mEtPhone.setText(addressinfo.getMobile());

			mEtArea.setText(addressinfo.getPcadetail());
			mEtAddress.setText(addressinfo.getAddress());
			mEtName.setText(addressinfo.getName());
			mEtYB.setText(addressinfo.getZip());

			if (addressinfo.getIsdefault().equals("y")) {
				mTbDefault.setChecked(true);
			} else {
				mTbDefault.setChecked(false);
			}

		} else {

		}

	}

	private OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			Editor edit = sp.edit();
			switch (buttonView.getId()) {
			case R.id.onoffBtn:
				if (isChecked) {
					// 选中

				} else {
					// 未选中

				}
				break;

			}
			edit.commit();
		}
	};

	OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.et_area:
				Intent i = new Intent(NewAddress.this, AddressChoose.class);
				i.putExtra("Boolean", "aaa");
				startActivityForResult(i, LOCATION_ID);
				break;

			case R.id.my_set_buyaddress_address_btn:

				mBtnSubmit.requestFocus();

				mBtnSubmit.setFocusable(true);
				mBtnSubmit.setFocusableInTouchMode(true);

				if (mEtName.getText().toString().length() < 1
						|| mEtArea.getText().toString().length() < 1
						|| mEtYB.getText().toString().length() < 1
						|| mEtPhone.getText().toString().length() < 1
						|| mEtAddress.getText().toString().length()<1) {
					Toast.makeText(getBaseContext(), "请完整填写收货人资料", 0).show();
					return;
				}

				if (mEtYB.getText().toString().length() < 6) {
					Toast.makeText(getBaseContext(), "请输入六位邮政编码", 0).show();
					return;
				}
				if (mEtAddress.getText().toString().length() < 5) {
					Toast.makeText(getBaseContext(), "详细地址至少输入五个字", 0).show();
					return;
				}

				showDialog();

				if (id != "") {

					MyRunnable newrun = new MyRunnable(id,
							UrlEntry.UPDATE_MYADDRESS_URL);
					new Thread(newrun).start();
				} else {

					MyRunnable newrun = new MyRunnable("",
							UrlEntry.INSERT_MYADDRESS_URL);
					new Thread(newrun).start();

				}

				break;

			default:
				break;
			}
		}
	};

	public class MyRunnable implements Runnable {

		private String id = "";
		private String url = "";

		public MyRunnable(String id, String url) {
			this.id = id;
			this.url = url;
		}

		@Override
		public void run() {
			Message msg = new Message();
			Bundle data = new Bundle();
			String result = "";
			MultipartEntity mpEntity = new MultipartEntity();

			try {
				if (!id.equals("")) {
					mpEntity.addPart("e.id", new StringBody(id));
				}

				mpEntity.addPart("e.province", new StringBody(shengcode));
				mpEntity.addPart("e.name", new StringBody(mEtName.getText()
						.toString()));
				mpEntity.addPart("e.city", new StringBody(shicode));
				mpEntity.addPart("e.area", new StringBody(qucode));
				mpEntity.addPart("e.pcadetail", new StringBody(mEtArea
						.getText().toString()));
				mpEntity.addPart("e.address", new StringBody(mEtAddress
						.getText().toString()));
				mpEntity.addPart("e.zip", new StringBody(mEtYB.getText()
						.toString()));
				mpEntity.addPart("e.mobile", new StringBody(mEtPhone.getText()
						.toString()));
				if (mTbDefault.isChecked()) {
					mpEntity.addPart("e.isdefault", new StringBody("y"));
				} else {
					mpEntity.addPart("e.isdefault", new StringBody("n"));
				}

				mpEntity.addPart("uuid", new StringBody(App.uuid));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpUtil http = new HttpUtil();
			result = http.postDataMethod(url, mpEntity);

			data.putString("value", result);
			msg.setData(data);
			msg.what = 1;

			handler.sendMessage(msg);
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			disDialog();
			Bundle data1 = msg.getData();
			String result = data1.getString("value");
			JSONObject jsonresult;
			switch (msg.what) {

			case 1:// 删除
				if (!result.equals("")) {
					try {
						jsonresult = new JSONObject(result);
						if (jsonresult.get("success").toString().equals("1")) {
							showToast("操作成功！");
							setResult(99);
							finish();

						} else if (jsonresult.get("success").toString()
								.equals("3")) {
							showToast("操作失败！");
						} else if (jsonresult.get("success").toString()
								.equals("0")) {
							showToast("用户信息错误，请重新登录！");
							editor.putString("userinfo", "");
							editor.commit();

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case LOCATION_ID:
			if (data == null)
				return;
			Bundle b = data.getExtras();
			shengcode = b.getString("shengcode");
			shicode = b.getString("shicode");
			qucode = b.getString("qucode");
			shengname = b.getString("province");
			shiname = b.getString("city");
			quname = b.getString("area");
			mEtArea.setText(shengname + " " + shiname + " " + quname);
			break;
		}
	}

}
