package com.szmy.pigapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.service.LocationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.HealthEntry;
import com.szmy.pigapp.image.CustomConstants;
import com.szmy.pigapp.image.ImageDisplayer;
import com.szmy.pigapp.image.ImageItem;
import com.szmy.pigapp.image.PopupWindows;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * ��֤����Ա �ලԱ ����Ա
 * 
 * @author Administrator
 * 
 */
public class HealthAuthActivity extends BaseActivity {
	private TextView tv_state;
	private EditText et_email, et_company, et_realname, et_qq, et_phone,
			et_area, et_identity,et_address,et_bjaddress,et_companyphone;
	private ImageView iv_identity;
	private Button btn_submit, btn_modify;
	private String province = "";
	private String city = "";
	private String area = "";
	private String address = "";
	private String pigFarmType = "", id = "";
	private BitmapUtils bitmapUtils;
	private String path = "";
	private ImageButton mLocationBtn;
	public static final int LOCATION_ID = 29;
	public LocationService locationService;
	public Vibrator mVibrator;
	public static double x = 0;
	public static double y = 0;
	public LinearLayout mAddressLayout;
	public String source = "0";
	public String type = "";
	public String queryurl = "";
	public String inserturl = "";
	public String uppdateurl = "";
	private PopupWindows mPwDialog;
	private int TAKE_PHOTO;
	private int TAKE_PICTURE;
	private File mFile;
	public  List<ImageItem> mDataList1 = new ArrayList<ImageItem>();
	private String imagepath1 = "";
	private String oldpath = "";
	private LinearLayout mLlBack;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_health_auth);
		bitmapUtils = new BitmapUtils(this);
		TextView mTvtitle = (TextView) findViewById(R.id.def_head_tv);
		type = getIntent().getStringExtra("type");
		if (type.equals("6")) {
			mTvtitle.setText("认证防疫员");
			queryurl = UrlEntry.QUERY_FYY_URL;
			inserturl = UrlEntry.INSERT_FYY_URL;
			uppdateurl = UrlEntry.UPDATE_FYY_URL;
		} else if (type.equals("7")) {
			mTvtitle.setText("认证检疫员");
			queryurl = UrlEntry.QUERY_JYY_URL;
			inserturl = UrlEntry.INSERT_JYY_URL;
			uppdateurl = UrlEntry.UPDATE_JYY_URL;
		} else if (type.equals("8")) {
			mTvtitle.setText("认证监督员");
			queryurl = UrlEntry.QUERY_JDY_URL;
			inserturl = UrlEntry.INSERT_JDY_URL;
			uppdateurl = UrlEntry.UPDATE_JDY_URL;
		}
		SharedPreferences sp = getSharedPreferences(App.USERINFO, 0);
		if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
			App.mUserInfo = FileUtil.readUser(HealthAuthActivity.this);
			App.setDataInfo(App.mUserInfo);
		}
		initView();
		// 定位
		if ((AppStaticUtil.getConvertDouble(App.GPS_X) == 0.0)
				|| (AppStaticUtil.getConvertDouble(App.GPS_Y) == 0.0)) {
			if (AppStaticUtil.isNetwork(getApplicationContext())) {
				locationService = new LocationService(getApplicationContext());
				locationService.registerListener(mListener);
			}
		} else {
			x = AppStaticUtil.getConvertDouble(App.GPS_X);
			y = AppStaticUtil.getConvertDouble(App.GPS_Y);
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		sendDataToHttp(queryurl, params, "");
	}

	/*****
	 * @see copy funtion to you project
	 *      定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 * 
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (null != location
					&& location.getLocType() != BDLocation.TypeServerError) {
				x = location.getLatitude();
				y = location.getLongitude();
				province = location.getProvince();
				city = location.getCity();
				area = location.getDistrict();
				 address = location.getStreet() + location.getStreetNumber();
				et_area.setText(province + " " + city + " " + area);
				 et_address.setText(address);
				et_area.setEnabled(false);
				et_area.setClickable(false);
				System.out.println(x + "----------------" + y);
				System.out.println("addrStr" + location.getAddrStr());
				System.out.println(location.getProvince() + "-"
						+ location.getCity() + "-" + location.getDistrict()
						+ "-" + location.getStreet() + "-"
						+ location.getStreetNumber());
			} else {
				et_area.setEnabled(true);
				et_area.setClickable(true);
			}
		}
	};

	private void initView() {
		tv_state = (TextView) findViewById(R.id.tv_state);
		et_email = (EditText) findViewById(R.id.et_email);
		et_company = (EditText) findViewById(R.id.et_company);
		et_realname = (EditText) findViewById(R.id.et_realname);
		et_qq = (EditText) findViewById(R.id.et_qq);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_area = (EditText) findViewById(R.id.et_area);
		et_identity = (EditText) findViewById(R.id.et_identity);
		iv_identity = (ImageView) findViewById(R.id.iv_identity);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_modify = (Button) findViewById(R.id.btn_modify);
		et_address = (EditText) findViewById(R.id.et_address);
		et_bjaddress = (EditText) findViewById(R.id.et_bjaddress);
		et_companyphone = (EditText) findViewById(R.id.et_companyphone);
		mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
		mLlBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearTempFromPref();
				finish();
			}
		});
		mDataList1 = getTempFromPref(CustomConstants.PREF_TEMP_IMAGES1);
		initImage();
	}

	/**
	 * @param zcinfo
	 */
	private void setDataToView(HealthEntry health) {
		id = health.getId();
		imagepath1 = health.getPicture();
		oldpath = health.getPicture();
		et_area.setText(health.getProvince() + " " + health.getCity() + " "
				+ health.getArea());
		et_address.setText(health.getAddress());
		province = health.getProvince();
		city = health.getCity();
		area = health.getArea();
		address = health.getAddress();
		if (source.equals("1")) {
			if (!TextUtils.isEmpty(health.getCompanyName())) {
				et_company.setEnabled(false);
				et_company.setTextColor(getResources().getColor(R.color.gray));
			}
			if (!TextUtils.isEmpty(health.getRealName())) {
				et_realname.setEnabled(false);
				et_realname.setTextColor(getResources().getColor(R.color.gray));
			}
			if (!TextUtils.isEmpty(health.getCompPhone())) {
				et_companyphone.setEnabled(false);
				et_companyphone.setTextColor(getResources().getColor(R.color.gray));
			}
			if (!TextUtils.isEmpty(health.getPhone())) {
				et_phone.setEnabled(false);
				et_phone.setTextColor(getResources().getColor(R.color.gray));
			}
			if (!TextUtils.isEmpty(health.getBjd())) {
				et_bjaddress.setEnabled(false);
				et_bjaddress.setTextColor(getResources().getColor(R.color.gray));
			}
		}
		et_companyphone.setText(health.getCompPhone());
		x = AppStaticUtil.getConvertDouble(health.getX());
		y = AppStaticUtil.getConvertDouble(health.getY());
		if (health.getStatus().equals("2")) {
			tv_state.setText("审核通过");
		} else if (health.getStatus().equals("3")) {
			tv_state.setText("审核未通过,原因：" + health.getReason());
		} else if (health.getStatus().equals("1")) {
			tv_state.setText("审核中");
		}
		et_bjaddress.setText(health.getBjd());
		et_email.setText(health.getEmail());
		et_company.setText(health.getCompanyName());
		et_realname.setText(health.getRealName());
		et_qq.setText(health.getQQ().toString());
		et_phone.setText(health.getPhone().toString());
		et_identity.setText(health.getIdCard().toString());
		if (!TextUtils.isEmpty(health.getPhone())) {
			bitmapUtils.display(iv_identity, UrlEntry.ip
					+ health.getPicture().toString());// 下载并显示图片
		}
	}

	public void sendDataToHttp(String url, RequestParams params,
			final String type) {
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params,
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
						Log.i("health", "  responseInfo.result = "
								+ responseInfo.result);
						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								if (jsonresult.has("source")) {
									if (jsonresult.optString("source").equals(
											"1")) {
										source = "1";
									}
								}
								HealthEntry mInfoEntry;
								btn_submit.setVisibility(View.GONE);
								btn_modify.setVisibility(View.VISIBLE);
								GsonBuilder gsonb = new GsonBuilder();
								Gson gson = gsonb.create();
								mInfoEntry = gson.fromJson(
										jsonresult.toString(),
										HealthEntry.class);
								setDataToView(mInfoEntry);
							} else if (jsonresult.get("success").toString()
									.equals("4")) {
								if (jsonresult.has("source")) {
									if (jsonresult.optString("source").equals(
											"1")) {
										source = "1";
										GsonBuilder gsonb = new GsonBuilder();
										Gson gson = gsonb.create();
										HealthEntry mInfoEntry = gson.fromJson(
												jsonresult.toString(),
												HealthEntry.class);
										setDataToView(mInfoEntry);
									}
								}
							} else {
								successType(jsonresult.get("success")
										.toString(), "操作失败！");
							}
						} catch (JSONException e) {
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("操作失败,请稍后再试！");
						disDialog();
					}
				});
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.et_area:
			// Intent intent = new Intent(HealthAuthActivity.this,
			// ChoseProvinceActivity.class);
			// intent.putExtra("add", "add");
			// startActivityForResult(intent, 22);
			Intent locintent = new Intent(HealthAuthActivity.this,
					LocationMapActivity.class);
			locintent.putExtra("x", x);
			locintent.putExtra("y", y);
			startActivityForResult(locintent, LOCATION_ID);
			break;
		case R.id.btn_submit:
			MyRunnable run = new MyRunnable("2", inserturl);// add
			new Thread(run).start();
			break;
		case R.id.btn_modify:
			MyRunnable runm = new MyRunnable("3", uppdateurl);// update
			new Thread(runm).start();
			break;
		case R.id.iv_identity:
			hideInput();
			TAKE_PHOTO = CustomConstants.TAKE_PHOTO;
			TAKE_PICTURE = CustomConstants.TAKE_PICTURE1;
			showPopDialog(iv_identity);
			break;
		}
	}

	public class MyRunnable implements Runnable {
		private String type;
		private String url;
		public MyRunnable(String type, String url) {
			this.type = type;
			this.url = url;
		}

		@Override
		public void run() {
			String result = "";
			Bundle data = new Bundle();
			Message msg = new Message();
			if (TextUtils.isEmpty(et_email.getText().toString())
					|| TextUtils.isEmpty(et_realname.getText().toString())
					|| TextUtils.isEmpty(et_phone.getText().toString())
					|| TextUtils.isEmpty(et_area.getText().toString())
					|| TextUtils.isEmpty(et_identity.getText().toString())||TextUtils.isEmpty(et_companyphone.getText().toString())) {
				msg.what = 12;
				handler.sendMessage(msg);
				return;
			}

			if (et_phone.getText().toString().length() != 11) {
				msg.what = 15;
				handler.sendMessage(msg);
				return;
			}
			Pattern p = Pattern.compile("[0-9]*");
			Matcher m = p.matcher(et_company.getText().toString());
			if (m.matches()) {
				msg.what = 13;
				handler.sendMessage(msg);
				return;
			}
			if (et_identity.getText().toString().trim().length() != 18) {
				msg.what = 14;
				handler.sendMessage(msg);
				return;
			}

			if (type.equals("2")) {
				if (imagepath1.equals("")) {
					msg.what = 11;
					handler.sendMessage(msg);
					return;
				}
			}
			MultipartEntity params = new MultipartEntity();
			try {
				params.addPart("", new StringBody(""));
				if (type.equals("3")) {
					params.addPart("e.id", new StringBody(id));
				}
				params.addPart("e.province", new StringBody(province));
				params.addPart("e.city", new StringBody(city));
				params.addPart("e.area", new StringBody(area));
				params.addPart("e.address", new StringBody(address));
				params.addPart("e.companyName", new StringBody(et_company
						.getText().toString()));
				params.addPart("e.compPhone", new StringBody(et_companyphone
						.getText().toString()));
				params.addPart("e.realName", new StringBody(et_realname
						.getText().toString()));
				params.addPart("e.QQ", new StringBody(et_qq.getText()
						.toString()));
				params.addPart("e.email", new StringBody(et_email.getText()
						.toString()));
				params.addPart("e.idCard", new StringBody(et_identity.getText()
						.toString()));
				params.addPart("e.bjd", new StringBody(et_bjaddress.getText()
						.toString()));
				if (!imagepath1.equals("")&&!imagepath1.equals(oldpath)) {
					System.out.println(imagepath1);
					params.addPart("uploadImage", new FileBody(new File(imagepath1)));
				}
				params.addPart("e.phone", new StringBody(et_phone.getText()
						.toString()));
				params.addPart("e.userID", new StringBody(App.userID));
				params.addPart("uuid", new StringBody(App.uuid));
				HttpUtil http = new HttpUtil();
				result = http.postDataMethod(url, params);
				data.putString("value", result);
				msg.setData(data);
				handler.sendMessage(msg);
			} catch (UnsupportedEncodingException e) {
			}
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			disDialog();
			if (msg.what == 11) {
				showToast("请上传您的证件图片！");
				return;
			} else if (msg.what == 12) {
				showToast("带'*'号为必填项,不能为空,请完善！");
				return;
			} else if (msg.what == 13) {
				showToast("公司名称不能为数字！");
				return;
			} else if (msg.what == 14) {
				showToast("身份证格式错误！");
				return;
			} else if (msg.what == 15) {
				showToast("请输入11位电话号码！");
				return;
			}
			Bundle data1 = msg.getData();
			String result = data1.getString("value");
			if (result.equals("")) {
				showToast("连接服务器失败,请稍后再试！");
				return;
			}
			JSONObject jsonresult;
			try {
				jsonresult = new JSONObject(result);
				switch (Integer.parseInt(jsonresult.getString("success"))) {
				case 1:
					showToast("提交成功！");
					clearTempFromPref();
					if (App.mUserInfo == null) {
						App.mUserInfo = FileUtil
								.readUser(HealthAuthActivity.this);
					}
					if (App.mUserInfo != null) {
						App.mUserInfo.setProvince(province);
						App.mUserInfo.setCity(city);
						App.mUserInfo.setArea(area);
						App.mUserInfo.setAuthentication("1");
						FileUtil.saveUser(HealthAuthActivity.this,
								App.mUserInfo);
						App.setDataInfo(App.mUserInfo);
					}
					setResult(11);
					finish();
					break;
				case 4:
					break;
				default:
					successType(jsonresult.get("success").toString(), "操作失败！");
					break;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			switch (msg.what) {
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CustomConstants.TAKE_PHOTO:
			System.out.println("path" + path);
			if (resultCode == -1 && !TextUtils.isEmpty(path)) {
				mDataList1.clear();
				mDataList1.add(FileUtil.saveDataPic(path));
			}
			saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);
			initImage();
			break;
		case CustomConstants.TAKE_PICTURE1:
			if(resultCode!=-1){
				return;
			}
			if (data == null) {
				return;
			}
			path = getPath(getFile());
			startPhotoZoom(data.getData(), path,
					CustomConstants.CORP_TAKE_PICTURE1);
			break;
		case CustomConstants.CORP_TAKE_PICTURE1:
			if(resultCode!=-1){
				return;
			}
			mDataList1 = setPicToView(data, path, mDataList1,0);
			saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);
			initView();
			break;
		case 22:
			if (data == null)
				return;
			Bundle bundle = data.getExtras();
			province = bundle.getString("province");
			city = bundle.getString("city");
			area = bundle.getString("area");
			et_area.setText(province + " " + city + " " + area);
			break;
		case 25:
			if (data == null)
				return;
			Bundle bt = data.getExtras();
			String industry = bt.getString("industry");
			et_email.setText(industry);
			break;
		case LOCATION_ID:
			if (data == null)
				return;
			Bundle b = data.getExtras();
			if (!TextUtils.isEmpty(b.getString("province"))) {
				province = b.getString("province");
				city = b.getString("city");
				area = b.getString("area");
				et_area.setText(province + " " + city + " " + area);
				x = b.getDouble("x");
				y = b.getDouble("y");
				address = b.getString("address");
				et_address.setText(address);
			}
			break;
		default:
			break;
		}
	}

	private void initImage() {
		if (isShowAddItem(mDataList1, 0)) {
		} else {
			final ImageItem item = mDataList1.get(0);
			ImageDisplayer.getInstance(this).displayBmp(iv_identity,
					item.thumbnailPath, item.sourcePath);
			imagepath1 = item.sourcePath;
		}
	}
	
	protected void onPause() {
		super.onPause();
		saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("path", path);
		super.onSaveInstanceState(outState);
		saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			path = savedInstanceState.getString("path");
			initView();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 返回这里需要刷新
		initView();
	}
	/****************************************************************/
	class OnClickLintener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.item_popupwindows_camera: // 相机
				if (TAKE_PHOTO == CustomConstants.TAKE_PHOTO) {
					mFile = getFile();
					path = getPath(mFile);
				} 
				takePhoto(TAKE_PHOTO, mFile);
				mPwDialog.dismiss();
				break;
			case R.id.item_popupwindows_Photo: // 相册
				takeAlbum(TAKE_PICTURE);
				mPwDialog.dismiss();
				break;
			case R.id.item_popupwindows_cancel:// 取消
				mPwDialog.dismiss();
				break;
			default:
				break;
			}
		}
	}

	private void showPopDialog(View parent) {
		// 自定义的单击事件
		OnClickLintener paramOnClickListener = new OnClickLintener();
		mPwDialog = new PopupWindows(HealthAuthActivity.this,
				paramOnClickListener, parent);
		// 如果窗口存在，则更新
		mPwDialog.update();
	}
}
