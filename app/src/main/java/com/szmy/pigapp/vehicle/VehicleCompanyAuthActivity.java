package com.szmy.pigapp.vehicle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.gc.materialdesign.widgets.Dialog;
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
import com.szmy.pigapp.activity.LocationMapActivity;
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

/**
 * 认证物流企业、个人
 * 
 * @author Administrator
 * 
 */
public class VehicleCompanyAuthActivity extends BaseActivity implements OnClickListener{

	private TextView tv_state, tvAreaS, tvIndustryS;
	private EditText etCompanyName, etVehicleType, etVehicleCount, etAddressP, etPhoneP, etRemark;
	private Button btn_submit, btn_modify;
	private String province = "";
	private String city = "";
	private String area = "";
	private String id = "";
	private String address = "";
	private ImageView iv;
    private String path = "";
    private BitmapUtils mBitmapUtils;
    private TextView tvCompanyType;
    private TextView tvAreaP;
    private TextView tv_stateP;
    private String companyType = "";
    private TextView paperstv;
    private ImageButton mLocationBtn;
	public static final int LOCATION_ID = 29;
	public LocationService locationService;
	public Vibrator mVibrator;
	public static double x = 0;
	public static double y = 0;
	public LinearLayout mAddressLayout;
	private LinearLayout mLlBack;
	private Button mNextBtn, mBackBtn;
	private LinearLayout mLlFirst;
	private LinearLayout mLlTwo;
	private PopupWindows mPwDialog;
	private int TAKE_PHOTO;
	private int TAKE_PICTURE;
	private File mFile;
	public  List<ImageItem> mDataList1 = new ArrayList<ImageItem>();
	private String imagepath1 = "";
	private String oldpath = "";
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_company_auth);
		SharedPreferences sp = getSharedPreferences(App.USERINFO, 0);
		((TextView) findViewById(R.id.def_head_tv)).setText("认证物流");
		if(!TextUtils.isEmpty(sp.getString("userinfo", ""))){
			App.mUserInfo = FileUtil.readUser(VehicleCompanyAuthActivity.this);
			App.setDataInfo(App.mUserInfo);
		}
		mBitmapUtils = new BitmapUtils(this);
		initView();
		System.out.println(App.GPS_X);
		// 定位
		showDialog();
		RequestParams params = new RequestParams();
		System.out.println(App.uuid);
		params.addBodyParameter("uuid", App.uuid);
		sendDataToHttp(UrlEntry.QUERY_VEHICLECOMPANY_BYUUID_URL, params, "");
		if ((AppStaticUtil.getConvertDouble(App.GPS_X) == 0.0)
				|| (AppStaticUtil.getConvertDouble(App.GPS_Y) == 0.0)) {
			if (AppStaticUtil.isNetwork(getApplicationContext())) {
				locationService = new LocationService(getApplicationContext());
				locationService.registerListener(mListener);
			}
		} else {
			final Dialog dialog = new Dialog(this, "提示", "是否需要重新定位？");
			dialog.addCancelButton("取消");
			dialog.setOnAcceptButtonClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
					if (AppStaticUtil.isNetwork(getApplicationContext())) {
						locationService = new LocationService(getApplicationContext());
						locationService.registerListener(mListener);
					}else{
						showToast("网络连接失败，请稍候再试");
						x = AppStaticUtil.getConvertDouble(App.GPS_X);
						y = AppStaticUtil.getConvertDouble(App.GPS_Y);
					}
				}
			});
			dialog.setOnCancelButtonClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
					x = AppStaticUtil.getConvertDouble(App.GPS_X);
					y = AppStaticUtil.getConvertDouble(App.GPS_Y);
				}
			});
			dialog.show();
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setMessage("是否需要重新定位？");
//			builder.setTitle("提示");
//			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					if (AppStaticUtil.isNetwork(getApplicationContext())) {
//						locationService = new LocationService(getApplicationContext());
//						locationService.registerListener(mListener);
//					}else{
//						showToast("网络连接失败，请稍候再试");
//						x = AppStaticUtil.getConvertDouble(App.GPS_X);
//						y = AppStaticUtil.getConvertDouble(App.GPS_Y);
//					}
//				}
//			});
//			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialogInterface, int i) {
//					x = AppStaticUtil.getConvertDouble(App.GPS_X);
//					y = AppStaticUtil.getConvertDouble(App.GPS_Y);
//				}
//			});
//			builder.create().show();


		}
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
				System.out.println(x + "----------------" + y);
				if(location.getLocType()==62){
					showToast("定位失败，请重新进入(建议打开GPS)!");
					return;
				}
				if((162  <= location.getLocType())&&(location.getLocType()<=167)){
					tvAreaP.setEnabled(true);
					tvAreaP.setClickable(true);
					showToast("定位失败，请重新进入(建议打开GPS)!");
					return;
				}
				x = location.getLatitude();
				y = location.getLongitude();
				province = location.getProvince();
				city = location.getCity();
				area = location.getDistrict();
				address = location.getStreet() + location.getStreetNumber();
				tvAreaP.setText(province + " " + city + " " + area);
				etAddressP.setText(address);
				tvAreaP.setEnabled(false);
				tvAreaP.setClickable(false);
				System.out.println(x + "----------------" + y);
				System.out.println("addrStr" + location.getAddrStr());
				System.out.println(location.getProvince() + "-"
						+ location.getCity() + "-" + location.getDistrict()
						+ "-" + location.getStreet() + "-"
						+ location.getStreetNumber());
			}else{
				tvAreaP.setEnabled(true);
				tvAreaP.setClickable(true);
			}
		}
	};
	private void initView() {
		etCompanyName = (EditText) findViewById(R.id.etCompanyName);
		etVehicleType = (EditText) findViewById(R.id.etVehicleType);
		etVehicleCount = (EditText) findViewById(R.id.etVehicleCount);
		etAddressP = (EditText) findViewById(R.id.etAddressP);
		etPhoneP = (EditText) findViewById(R.id.etPhoneP);
		etRemark = (EditText) findViewById(R.id.etRemark);
		tvCompanyType = (TextView) findViewById(R.id.tvCompanyType);
		tvAreaP = (TextView) findViewById(R.id.tvAreaP);
		tv_stateP = (TextView) findViewById(R.id.tv_stateP);
		iv = (ImageView) findViewById(R.id.iv_identity);
		btn_submit = (Button) findViewById(R.id.btn_submitP);
		btn_modify = (Button) findViewById(R.id.btn_modifyP);
		
		tvAreaP.setOnClickListener(this);
		tvCompanyType.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_modify.setOnClickListener(this);
		paperstv = (TextView) findViewById(R.id.paperstv);
		mLocationBtn = (ImageButton) findViewById(R.id.locationBtn);
		mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
		mLlFirst = (LinearLayout) findViewById(R.id.layoutone);
		mLlTwo = (LinearLayout) findViewById(R.id.layouttwo);
		mNextBtn = (Button) findViewById(R.id.btn_next);
		mNextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(etCompanyName.getText().toString())
						|| TextUtils.isEmpty(etVehicleType.getText().toString())
						|| TextUtils.isEmpty(etVehicleCount.getText().toString())
						|| TextUtils.isEmpty(etAddressP.getText().toString())						
						|| TextUtils.isEmpty(tvCompanyType.getText().toString())){
					showToast("带'*'号为必填项,不能为空,请完善！");
					return;
				}
				if(FileUtil.isNumeric(etCompanyName.getText().toString())){
					showToast("公司名称不能为纯数字！");
					return;
				}							
				
				hideInput();
				mLlFirst.setVisibility(View.GONE);
				mLlTwo.setVisibility(View.VISIBLE);

			}
		});

		mBackBtn = (Button) findViewById(R.id.btn_goback);
		mBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mLlTwo.setVisibility(View.GONE);
				mLlFirst.setVisibility(View.VISIBLE);

			}
		});

		mLlBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((mLlTwo.getVisibility() == View.VISIBLE)
						&& (mLlFirst.getVisibility() == View.GONE)) {
					mLlTwo.setVisibility(View.GONE);
					mLlFirst.setVisibility(View.VISIBLE);
				} else if ((mLlTwo.getVisibility() == View.GONE)
						&& (mLlFirst.getVisibility() == View.VISIBLE)) {
					clearTempFromPref();
					finish();
				}

			}
		});
		mDataList1 = getTempFromPref(CustomConstants.PREF_TEMP_IMAGES1);
		initImage();
	}

	/**
	 * @param zcinfo
	 */
	private void setDataToView(Company company) {
		id = company.getId();
		imagepath1 = company.getIcon();
		oldpath = company.getIcon();
		tvAreaP.setText(company.getProvince() + " " + company.getCity() + " "
				+ company.getArea());
		province = company.getProvince();
		city = company.getCity();
		area = company.getArea();
		address =company.getAddress();	
		x = Double.parseDouble(company.getX());
		y =  Double.parseDouble(company.getY());
		if (company.getStatus().equals("2")) {
			tv_stateP.setText("审核通过");
		} else if (company.getStatus().equals("3")) {
			tv_stateP.setText("审核未通过,原因：" + company.getReason().toString());
		} else if (company.getStatus().equals("1")) {
			tv_stateP.setText("审核中");
		}
		
		etCompanyName.setText(company.getName());	
		companyType = company.getType();
		tvCompanyType.setText(companyType(company.getType()));
		etVehicleType.setText(company.getCarType());
		etVehicleCount.setText(company.getCarNum().toString());
		etAddressP.setText(company.getAddress());
		etPhoneP.setText(company.getPhone());
		etRemark.setText(company.getRemark());
		tvAreaP.setText(company.getProvince()+company.getCity()+company.getArea());
		
		
		
		
		if(!company.getIcon().equals("")){
			mBitmapUtils.display(iv, UrlEntry.ip+company.getIcon());
		}
	}

	private String companyType(String type) {
		if (TextUtils.isEmpty(type))
			return "";
		if (type.equals("1")) {
			return "企业";
		} else if (type.equals("0"))
			return "个人";
		return type;
	}

	
	public class MyRunnable implements  Runnable {

		private String type;
		private String url;
		
		
		public MyRunnable (String type,String url){
			this.type = type;
			this.url = url;
		}
		
		@Override
		public void run() {
			String result = "";
			Bundle data = new Bundle();
			Message msg = new Message();
			if (TextUtils.isEmpty(etCompanyName.getText().toString())
					|| TextUtils.isEmpty(etVehicleType.getText().toString())
					|| TextUtils.isEmpty(etVehicleCount.getText().toString())
					|| TextUtils.isEmpty(etAddressP.getText().toString())
					|| TextUtils.isEmpty(etPhoneP.getText().toString())
					|| TextUtils.isEmpty(tvCompanyType.getText().toString())) {
//				disDialog();
//				showToast("带'*'号为必填项,不能为空,请完善！");
				msg.what = 12;
				handler.sendMessage(msg);
				return;

			}
			if(etPhoneP.getText().toString().length()!=11){
				msg.what = 14;
				handler.sendMessage(msg);
				return;
			}
			if (type.equals("2")) {
				if(imagepath1.equals("")){
//					disDialog();
//					showToast("请上传您的证件照片！");
					msg.what = 11;
					handler.sendMessage(msg);
					return;
				}
			}
			
			MultipartEntity params = new MultipartEntity();
			try {
				params.addPart("",new StringBody(""));
				if (type.equals("3")) {
					params.addPart("e.id", new StringBody(id));
				}
				params.addPart("e.name", new StringBody(etCompanyName.getText().toString()));
				params.addPart("e.province", new StringBody(province));
				params.addPart("e.city", new StringBody(city));
				params.addPart("e.area", new StringBody(area));
				params.addPart("e.type", new StringBody(companyType));
				params.addPart("e.carType", new StringBody(etVehicleType.getText().toString()));
				params.addPart("e.carNum",new StringBody( etVehicleCount.getText().toString()));
				params.addPart("e.phone", new StringBody(etPhoneP.getText().toString()));
				params.addPart("e.address",new StringBody( etAddressP.getText().toString()));
				params.addPart("e.remark",new StringBody( etRemark.getText().toString()));
				if(!imagepath1.equals("")&&!imagepath1.equals(oldpath)){
				params.addPart("uploadImage", new FileBody( new File(imagepath1)));
				}
				params.addPart("e.createAccount", new StringBody(App.username));
				params.addPart("uuid", new StringBody(App.uuid));
				params.addPart("e.x", new StringBody(String.valueOf(x)));
				params.addPart("e.y", new StringBody(String.valueOf(y)));
				
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
	
	Handler handler = new Handler(){
		
		public void handleMessage(Message msg){
			disDialog();
			if (msg.what == 11) {
				showToast("请上传您的证件照片！");
				return;
			} else if (msg.what == 12) {

				showToast("带'*'号为必填项,不能为空,请完善！");
				return;
			} else if (msg.what == 14) {

				showToast("请输入11位手机号码！");
				return;
			}
			Bundle data1 = msg.getData();
			String result = data1.getString("value");
			if(result.equals("")){
				showToast("连接服务器失败,请稍后再试！");
				return;
			}
			
			JSONObject jsonresult;
			
			
			try {
				jsonresult = new JSONObject(result);
				switch(Integer.parseInt(jsonresult.getString("success"))){
				case 1:
					clearTempFromPref();
						 showToast("提交成功！");
						 sp = getSharedPreferences(App.USERINFO, 0);
						Editor edit = sp.edit();
						
						if(App.mUserInfo!=null){
							App.mUserInfo.setProvince(province);
							App.mUserInfo.setCity(city);
							App.mUserInfo.setArea(area);
							App.mUserInfo.setAddress(address);
							App.mUserInfo.setAuthentication("1");
							App.mUserInfo.setX(String.valueOf(x));
							App.mUserInfo.setY(String.valueOf(y));
						FileUtil.saveUser(VehicleCompanyAuthActivity.this, App.mUserInfo);
						App.setDataInfo(App.mUserInfo);
						}
						
						
						
						App.authentication = "1" ;
						 
						 setResult(11);
                         finish();
					break;
				case 4:
					
					break;
				default:
					successType(jsonresult.get("success")
							.toString(), "操作失败！");
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

						Log.i("skaughterhouse", " skaughterhouse responseInfo.result = "
								+ responseInfo.result);

						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								Company mInfoEntry;
								if (type.equals("1")) {// 新增或修改
									clearTempFromPref();
									showToast("提交成功！");
									if(App.mUserInfo!=null){
										App.mUserInfo.setProvince(province);
										App.mUserInfo.setCity(city);
										App.mUserInfo.setArea(area);
										App.mUserInfo.setAddress(address);
										App.mUserInfo.setAuthentication("1");
										App.mUserInfo.setX(String.valueOf(x));
										App.mUserInfo.setY(String.valueOf(y));
									FileUtil.saveUser(VehicleCompanyAuthActivity.this, App.mUserInfo);
									App.setDataInfo(App.mUserInfo);
									}
									App.authentication = "1";
									finish();
								} else {
									btn_submit.setVisibility(View.GONE);
									btn_modify.setVisibility(View.VISIBLE);
									GsonBuilder gsonb = new GsonBuilder();
									Gson gson = gsonb.create();
									mInfoEntry = gson.fromJson(
											jsonresult.toString(),
											Company.class);
									setDataToView(mInfoEntry);

								}

							} else if (jsonresult.get("success").toString()
									.equals("4")) {

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
						showToast("网络连接失败,请稍后再试！");
						disDialog();

					}
				});

	}

	public void onClick(View v) {
		hideInput();
		Intent locintent ;
		switch (v.getId()) {

		case R.id.tvAreaP:
//			Intent intent = new Intent(VehicleCompanyAuthActivity.this,
//					ChoseProvinceActivity.class);
//			intent.putExtra("add", "add");
//			startActivityForResult(intent, 22);
	
			 locintent = new Intent(VehicleCompanyAuthActivity.this,
					LocationMapActivity.class);
			locintent.putExtra("x", x);
			locintent.putExtra("y", y);
			startActivityForResult(locintent, LOCATION_ID);
			
			break;

		case R.id.tvCompanyType:
			startActivityForResult(new Intent(VehicleCompanyAuthActivity.this,ChoseVehicleCompanyType.class), 23);
			break;
		case R.id.btn_submitP:
			showDialog();
			MyRunnable run = new MyRunnable("2", UrlEntry.VEHICLECOMPANY_INSERTAUTH_URL); //add
			new Thread(run).start();
			break;
		case R.id.btn_modifyP:
			showDialog();
			MyRunnable runm = new MyRunnable("3", UrlEntry.VEHICLECOMPANY_UPDATEAUTH_URL); //update
			new Thread(runm).start();
			break;
		case R.id.iv_identity:
			hideInput();
			TAKE_PHOTO = CustomConstants.TAKE_PHOTO;
			TAKE_PICTURE = CustomConstants.TAKE_PICTURE1;
			showPopDialog(iv);
			break;
		case R.id.locationBtn:
			 locintent = new Intent(VehicleCompanyAuthActivity.this,
					LocationMapActivity.class);
			locintent.putExtra("x", x);
			locintent.putExtra("y", y);
			startActivityForResult(locintent, LOCATION_ID);
			break;

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
			tvAreaP.setText(province + " " + city + " " + area);
			break;
			
		case 23:
			if (data == null)
				return;
			Bundle b = data.getExtras();
			companyType = b.getString("typeid");
			tvCompanyType.setText(b.getString("typename"));
			if(companyType.equals("0")){
				paperstv.setText("请上传营业执照");
			}else if(companyType.equals("1")){
				paperstv.setText("请上传身份证件照");
			}
			break;
		case LOCATION_ID:
			if(data == null) return ;
			b = data.getExtras();
			if(!TextUtils.isEmpty(b.getString("province"))){
				province = b.getString("province");
				city = b.getString("city");
				area = b.getString("area");
				tvAreaP.setText(province+" "+city+" "+area);
				x = b.getDouble("x");
				y = b.getDouble("y");
				address = b.getString("address");
				etAddressP.setText(address);
			}
			break;
		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	
	private void initImage() {
		if (isShowAddItem(mDataList1, 0)) {

		} else {
			final ImageItem item = mDataList1.get(0);
			ImageDisplayer.getInstance(this).displayBmp(iv,
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
		mPwDialog = new PopupWindows(VehicleCompanyAuthActivity.this,
				paramOnClickListener, parent);
		// 如果窗口存在，则更新
		mPwDialog.update();
	}
	
}
