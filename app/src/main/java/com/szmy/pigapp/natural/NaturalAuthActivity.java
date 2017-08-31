package com.szmy.pigapp.natural;

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
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.activity.ChoseProvinceActivity;
import com.szmy.pigapp.image.CustomConstants;
import com.szmy.pigapp.image.ImageDisplayer;
import com.szmy.pigapp.image.ImageItem;
import com.szmy.pigapp.image.PopupWindows;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 认证自然人
 * 
 * @author Administrator
 * 
 */
public class NaturalAuthActivity extends BaseActivity {

	private TextView tv_state;
	private EditText et_realname, et_phone, et_area, et_remark, et_identity;
	private ImageView iv_identity;
	private Button btn_submit, btn_modify;
	private String province = "";
	private String city = "";
	private String area = "";
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
	private String source = "0";
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
		setContentView(R.layout.activity_natural_auth);
		bitmapUtils = new BitmapUtils(this);
		((TextView) findViewById(R.id.def_head_tv)).setText("认证自然人");
		SharedPreferences sp = getSharedPreferences(App.USERINFO, 0);
		if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
			App.mUserInfo = FileUtil.readUser(NaturalAuthActivity.this);
			App.setDataInfo(App.mUserInfo);
		}
		initView();
		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		sendDataToHttp(UrlEntry.NATURAL_QUERYBYID_URL, params, "");
	}

	private void initView() {
		tv_state = (TextView) findViewById(R.id.tv_state);
		et_realname = (EditText) findViewById(R.id.et_realname);
		et_remark = (EditText) findViewById(R.id.et_remark);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_area = (EditText) findViewById(R.id.et_area);
		et_identity = (EditText) findViewById(R.id.et_identity);
		iv_identity = (ImageView) findViewById(R.id.iv_identity);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_modify = (Button) findViewById(R.id.btn_modify);
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
	 * @param naturalinfo
	 */
	private void setDataToView(NaturalEntry ne) {
		imagepath1 = ne.getCardImg();
		oldpath = ne.getCardImg();
		if (source.equals("1")) {

			if (!TextUtils.isEmpty(ne.getRealName())) {
				et_realname.setEnabled(false);
				et_realname.setTextColor(getResources().getColor(R.color.gray));
			}
		}
		id = ne.getId();
		et_area.setText(ne.getProvince() + " " + ne.getCity() + " "
				+ ne.getArea());
		province = ne.getProvince();
		city = ne.getCity();
		area = ne.getArea();
		if (ne.getStatus().equals("2")) {
			tv_state.setText("审核通过");
		} else if (ne.getStatus().equals("3")) {
			tv_state.setText("审核未通过,原因：" + ne.getReason());
		} else if (ne.getStatus().equals("1")) {
			tv_state.setText("审核中");
		}
		et_realname.setText(ne.getRealName());
		et_remark.setText(ne.getRemark());
		et_phone.setText(ne.getPhone().toString());
		et_identity.setText(ne.getIdCard().toString());
		if (!TextUtils.isEmpty(ne.getCardImg())) {
			bitmapUtils.display(iv_identity, UrlEntry.ip
					+ ne.getCardImg().toString());// 下载并显示图片

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

						Log.i("Natural", "  responseInfo.result = "
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
								NaturalEntry mInfoEntry;

								btn_submit.setVisibility(View.GONE);
								btn_modify.setVisibility(View.VISIBLE);
								GsonBuilder gsonb = new GsonBuilder();
								Gson gson = gsonb.create();
								mInfoEntry = gson.fromJson(
										jsonresult.toString(),
										NaturalEntry.class);
								setDataToView(mInfoEntry);

							} else if (jsonresult.get("success").toString()
									.equals("4")) {
								if (jsonresult.has("source")) {
									if (jsonresult.optString("source").equals(
											"1")) {
										source = "1";
										NaturalEntry mInfoEntry;

										GsonBuilder gsonb = new GsonBuilder();
										Gson gson = gsonb.create();
										mInfoEntry = gson.fromJson(
												jsonresult.toString(),
												NaturalEntry.class);
										setDataToView(mInfoEntry);
									}
								}

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
						showToast("操作失败,请稍后再试！");
						disDialog();

					}
				});

	}

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.et_area:
			Intent intent = new Intent(NaturalAuthActivity.this,
					ChoseProvinceActivity.class);
			intent.putExtra("add", "add");
			startActivityForResult(intent, App.ADDRESS_CODE);
			break;

		case R.id.btn_submit:
			showDialog();
			MyRunnable run = new MyRunnable("2", UrlEntry.NATURAl_INSERT_URL);// add
			new Thread(run).start();
			break;
		case R.id.btn_modify:
			showDialog();
			MyRunnable runm = new MyRunnable("3", UrlEntry.NATURAl_UPDATE_URL);// update
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

			if (TextUtils.isEmpty(et_realname.getText().toString())
					|| TextUtils.isEmpty(et_phone.getText().toString())
					|| TextUtils.isEmpty(et_area.getText().toString())
					|| TextUtils.isEmpty(et_identity.getText().toString())) {
				msg.what = 12;
				handler.sendMessage(msg);
				return;

			}

			if (et_phone.getText().toString().length() != 11) {
				msg.what = 15;
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
				
				if (type.equals("3")) {
					params.addPart("e.id", new StringBody(id));
				}

				params.addPart("e.province", new StringBody(province));
				params.addPart("e.city", new StringBody(city));
				params.addPart("e.area", new StringBody(area));

				params.addPart("e.realName", new StringBody(et_realname
						.getText().toString()));

				params.addPart("e.idCard", new StringBody(et_identity.getText()
						.toString()));
				if (!imagepath1.equals("")&&(!imagepath1.equals(oldpath))) {
					System.out.println(imagepath1);
					params.addPart("uploadImage", new FileBody(new File(imagepath1)));
				}
				params.addPart("e.phone", new StringBody(et_phone.getText()
						.toString()));
				params.addPart("e.remark", new StringBody(et_remark.getText()
						.toString()));
				params.addPart("e.userID", new StringBody(App.userID));
				params.addPart("uuid", new StringBody(App.uuid));

				HttpUtil http = new HttpUtil();
				result = http.postDataMethod(url, params);

				data.putString("value", result);
				msg.setData(data);

				handler.sendMessage(msg);

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			disDialog();
			if (msg.what == 11) {
				showToast("请上传您的证件照片！");
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
								.readUser(NaturalAuthActivity.this);
					}
					if (App.mUserInfo != null) {
						App.mUserInfo.setProvince(province);
						App.mUserInfo.setCity(city);
						App.mUserInfo.setArea(area);
						App.mUserInfo.setAuthentication("1");
						FileUtil.saveUser(NaturalAuthActivity.this,
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
		case App.ADDRESS_CODE:
			if (data == null)
				return;
			Bundle bundle = data.getExtras();
			province = bundle.getString("province");
			city = bundle.getString("city");
			area = bundle.getString("area");
			et_area.setText(province + " " + city + " " + area);
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
		mPwDialog = new PopupWindows(NaturalAuthActivity.this,
				paramOnClickListener, parent);
		// 如果窗口存在，则更新
		mPwDialog.update();
	}
}
