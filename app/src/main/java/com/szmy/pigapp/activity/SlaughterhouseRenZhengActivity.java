package com.szmy.pigapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.SlaughterhouseEntry;
import com.szmy.pigapp.image.CustomConstants;
import com.szmy.pigapp.image.ImageDisplayer;
import com.szmy.pigapp.image.ImageItem;
import com.szmy.pigapp.image.PopupWindows;
import com.szmy.pigapp.tixian.ActivityWithdrawals;
import com.szmy.pigapp.tixian.Withdrawals;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.SerializableMap;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 屠宰场认证
 * 
 * @author Administrator
 * 
 */
public class SlaughterhouseRenZhengActivity extends BaseActivity implements OnClickListener {

	private TextView tv_state, tvAreaS, tvIndustryS;
	private EditText etCompany, etKillNum, etPeople, etAddress, etPhone, etQQ,
			etDemand;
	private Button btn_submitP, btn_modify;
	private String province = "";
	private String city = "";
	private String area = "";
	private String id = "";
	private ImageView mImgYyzz, mImgFmtp;
	
	private String address = "";
	private BitmapUtils bitmapUtils;
	public static final int LOCATION_ID = 29;
	public LocationService locationService;
	public Vibrator mVibrator;
	public static double x = 0;
	public static double y = 0;
	private String pathname = "";
	private String source = "0";
	private LinearLayout mLlBack;
	private Button mNextBtn, mBackBtn;
	private LinearLayout mLlFirst;
	private LinearLayout mLlTwo;
	private PopupWindows mPwDialog;

	private File mFile;
	public  List<ImageItem> mDataList1 = new ArrayList<ImageItem>();
	public  List<ImageItem> mDataList2 = new ArrayList<ImageItem>();
	private String imagepath1 = "";
	private String imagepath2 = "";
	private String path = "", path2 = "";
	private String oldpath = "", oldpath2 = "";
	private TextView tvBank;
	private static final int BANK_CARD_ID = 26;
	private String[] mPsItems = new String[]{"农业银行卡", "农村信用社"};
	private int mSingleChoiceIDs = 0;
	private Map<String, ImageItem> picMaps = new HashMap<>();
	private Map<String, String> urlmaps = new HashMap<>();
	private ImageView mImgSfzZm;
	private ImageView mCamSfzZm;
	private ImageView mImgSfzFm;
	private ImageView mCamSfzFm;
	private EditText etCardNum;
	private EditText etCardName;
	private EditText etIdentityCard;// 身份证
	private TextView tvCardType;
	private final int TAKE_PHOTO = 0x0000134;
	private final int TAKE_PICTURE = 0x0000135;
	private final int CORP_TAKE_PICTURE1 = 0x0000136;
	private int mSingleChoiceID = 0;
	public String picUrl = "";
	public String picTypeName = "";
	private Withdrawals mWdwEntry;
	private SlaughterhouseEntry mInfoEntry;
	private Map<String,String> map ;
	public static SlaughterhouseRenZhengActivity instance = null;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_slaughterhouse_auth);
		instance = this;
		sp = getSharedPreferences(App.USERINFO, 0);
		if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
			App.mUserInfo = FileUtil
					.readUser(SlaughterhouseRenZhengActivity.this);
			App.setDataInfo(App.mUserInfo);

		}
		if (arg0 != null) {
			SerializableMap serializableMap = (SerializableMap) arg0.get("map");
			picMaps = serializableMap.getPicMaps();
			urlmaps = serializableMap.getUrlmaps();

		}
		((TextView) findViewById(R.id.def_head_tv)).setText("认证屠宰场");
		bitmapUtils = new BitmapUtils(this);
		initView();
		// 定位
		if (getIntent().hasExtra("entity")){
			SlaughterhouseEntry entry = (SlaughterhouseEntry) getIntent().getSerializableExtra("entity");
			setDataToView(entry);
		}else{
			showDialog();
			RequestParams params = new RequestParams();
			System.out.println(App.uuid);
			params.addBodyParameter("uuid", App.uuid);
			sendDataToHttp(UrlEntry.QUERY_SLAUGHTERHOUSE_URL, params, "");
		}

		if ((AppStaticUtil.getConvertDouble(App.GPS_X) == 0.0)
				|| (AppStaticUtil.getConvertDouble(App.GPS_Y) == 0.0)) {
			if (AppStaticUtil.isNetwork(getApplicationContext())) {
				locationService = new LocationService(getApplicationContext());
				locationService.registerListener(mListener);
			}
		} else {
		}

	}

	/*****
	 *
	 *      定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 * 
	 */
	private BDLocationListener mListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (null != location
					&& location.getLocType() != BDLocation.TypeServerError) {
				if (location.getLocType() == 62) {
					showToast("定位失败，请重新进入(建议打开GPS)!");
					return;
				}
				if((162  <= location.getLocType())&&(location.getLocType()<=167)){
					tvAreaS.setEnabled(true);
					tvAreaS.setClickable(true);
					showToast("定位失败，请重新进入(建议打开GPS)!");
					return;
				}
				x = location.getLatitude();
				y = location.getLongitude();
				province = location.getProvince();
				city = location.getCity();
				area = location.getDistrict();
				address = location.getStreet() + location.getStreetNumber();
				if (!TextUtils.isEmpty(city)
						&& city.contains(province)) {
					tvAreaS.setText(province + " " + area);
				} else {
					tvAreaS.setText(province + " " + city + " "
							+ area);
				}
				etAddress.setText(address);
				tvAreaS.setEnabled(false);
				tvAreaS.setClickable(false);
			} else {
				tvAreaS.setEnabled(true);
				tvAreaS.setClickable(true);
				showToast("定位失败，请重新进入(建议打开GPS)!");
			}
		}
	};

	private void initView() {
		tv_state = (TextView) findViewById(R.id.tv_state);
		etCompany = (EditText) findViewById(R.id.etCompany);
		etKillNum = (EditText) findViewById(R.id.etKillNum);
		etPeople = (EditText) findViewById(R.id.etPeople);
		etAddress = (EditText) findViewById(R.id.etAddress);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etQQ = (EditText) findViewById(R.id.etQQ);
		etDemand = (EditText) findViewById(R.id.etDemand);
		tvAreaS = (TextView) findViewById(R.id.tvAreaS);
		tvIndustryS = (TextView) findViewById(R.id.tvIndustryS);
		btn_submitP = (Button) findViewById(R.id.btn_submit);
		mImgYyzz = (ImageView) findViewById(R.id.iv_yyzzidentity);
		mImgFmtp = (ImageView) findViewById(R.id.iv_cover);
		mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
		mLlFirst = (LinearLayout) findViewById(R.id.layoutone);
		mLlTwo = (LinearLayout) findViewById(R.id.layouttwo);
		mNextBtn = (Button) findViewById(R.id.btn_next);
		tvBank = (TextView) findViewById(R.id.tvBank);
		mNextBtn.setOnClickListener(this);

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
					picMaps.clear();
					urlmaps.clear();
				}

			}
		});
		mImgSfzFm = (ImageView) findViewById(R.id.iv_fidentity);
		mCamSfzFm = (ImageView) findViewById(R.id.iv_fcamera);
		mImgSfzZm = (ImageView) findViewById(R.id.iv_identity);
		mCamSfzZm = (ImageView) findViewById(R.id.iv_camera);
		etCardNum = (EditText) findViewById(R.id.etCardNum);
		etCardName = (EditText) findViewById(R.id.etCardName);
		etIdentityCard = (EditText) findViewById(R.id.etIdentityCard);
		tvCardType = (TextView) findViewById(R.id.tvCardType);
		setImage();
	}

	private boolean isNullStr(){
		hideInput();
		if (TextUtils.isEmpty(etCompany.getText().toString())
				|| TextUtils.isEmpty(etKillNum.getText().toString())
				|| TextUtils.isEmpty(tvAreaS.getText().toString())
				|| TextUtils.isEmpty(etAddress.getText().toString())
				||TextUtils.isEmpty(etPhone.getText().toString())
				||TextUtils.isEmpty(etPeople.getText().toString())
				|| TextUtils.isEmpty(etDemand.getText().toString())) {
			showToast("带'*'号为必填项,不能为空,请完善！");
			return false;
		}
		if (FileUtil.isNumeric(etCompany.getText().toString())) {
			showToast("企业名称不能为纯数字！");
			return false;
		}
		if (etPhone.getText().toString().length() != 11) {
			showToast("请输入11位手机号码");
			return false;
		}
		String msg = "";
		msg =isPic("yyzz","营业执照");
		if(msg.equals("ok")){
		}else{
			if (!msg.equals("")){
				showToast(msg);
				return false;
			}
		}
		return true;
	}


	/**@param shinfo
	 */
	private void setDataToView(SlaughterhouseEntry shinfo) {
		mInfoEntry = shinfo;
		if (source.equals("1")) {
			if (!TextUtils.isEmpty(shinfo.getCompanyName())) {
				etCompany.setEnabled(false);
				etCompany.setTextColor(getResources().getColor(R.color.gray));
			}
			if (!TextUtils.isEmpty(shinfo.getRealName())) {
				etPeople.setEnabled(false);
				etPeople.setTextColor(getResources().getColor(R.color.gray));
			}
		}
		id = shinfo.getId();
		imagepath1 = shinfo.getCardImg();
		if (!shinfo.getCardImg().equals("")) {
			oldpath = shinfo.getCardImg();
		}
	
		tvAreaS.setText(shinfo.getProvince() + " " + shinfo.getCity() + " "
				+ shinfo.getArea());
		province = shinfo.getProvince();
		city = shinfo.getCity();
		area = shinfo.getArea();
		address = shinfo.getAddress();
		x = AppStaticUtil.getConvertDouble(shinfo.getX());
		y = AppStaticUtil.getConvertDouble(shinfo.getY());
//		if(shinfo.getIsSmrzBank().equals("n")){
//			tvBank.setText("未绑定银行卡");
//		}else if(shinfo.getIsSmrzBank().equals("y")){
//			tvBank.setText("已绑定银行卡");
//		}
		if (shinfo.getStatus().equals("2")) {
			tv_state.setText("审核通过");
		} else if (shinfo.getStatus().equals("3")) {
			tv_state.setText("审核未通过,原因：" + shinfo.getReason());
		} else if (shinfo.getStatus().equals("1")) {
			tv_state.setText("审核中");
		}

		etCompany.setText(shinfo.getCompanyName().toString());
		etKillNum.setText(shinfo.getNumber().toString());
		etPeople.setText(shinfo.getRealName().toString());

		etAddress.setText(shinfo.getAddress().toString());
		etPhone.setText(shinfo.getPhone().toString());
		etQQ.setText(shinfo.getQQ().toString());
		tvIndustryS.setText(shinfo.getBusiness().toString());
		etDemand.setText(shinfo.getIntro().toString());
		if (!shinfo.getCardImg().equals("")) {
				urlmaps.put("yyzz",shinfo.getCardImg().toString());
		}
		if (!shinfo.getPicture().equals("")) {
			urlmaps.put("fmtp",shinfo.getPicture().toString());
		}
		setImage();
	}

//	public class MyRunnable implements Runnable {
//
//		private String type;
//		private String url;
//
//		public MyRunnable(String type, String url) {
//			this.type = type;
//			this.url = url;
//		}
//
//		@Override
//		public void run() {
//			String result = "";
//			Bundle data = new Bundle();
//			Message msg = new Message();
//			MultipartEntity params = new MultipartEntity();
//			try {
//				params.addPart("", new StringBody(""));
//				if (type.equals("3")) {
//					params.addPart("e.id", new StringBody(id));
//				}
//				params.addPart("e.province", new StringBody(province));
//				params.addPart("e.city", new StringBody(city));
//				params.addPart("e.area", new StringBody(area));
//				params.addPart("e.business", new StringBody(tvIndustryS
//						.getText().toString()));
//				params.addPart("e.companyName", new StringBody(etCompany
//						.getText().toString()));
//				params.addPart("e.realName", new StringBody(etPeople.getText()
//						.toString()));
//				params.addPart("e.QQ",
//						new StringBody(etQQ.getText().toString()));
//				params.addPart("e.intro", new StringBody(etDemand.getText()
//						.toString()));
//				params.addPart("e.number", new StringBody(etKillNum.getText()
//						.toString()));
//				params.addPart("e.address", new StringBody(etAddress.getText()
//						.toString()));
//
//				if(picMaps.containsKey("fmtp")){
//					params.addPart("uploadPic", new FileBody(new File(picMaps.get("fmtp").sourcePath)));
//				}
//				if(picMaps.containsKey("yyzz")){
//					params.addPart("uploadImage", new FileBody(new File(picMaps.get("yyzz").sourcePath)));
//				}
//				params.addPart("e.phone", new StringBody(etPhone.getText()
//						.toString()));
//				params.addPart("e.x", new StringBody(String.valueOf(x)));
//				params.addPart("e.y", new StringBody(String.valueOf(y)));
//				params.addPart("e.userID", new StringBody(App.userID));
//				params.addPart("uuid", new StringBody(App.uuid));
//
//				HttpUtil http = new HttpUtil();
//				result = http.postDataMethod(url, params);
//
//				data.putString("value", result);
//				msg.setData(data);
//
//				handler.sendMessage(msg);
//
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//	}

//	Handler handler = new Handler() {
//
//		public void handleMessage(Message msg) {
//
//
//			Bundle data1 = msg.getData();
//			String result = data1.getString("value");
//			if (result.equals("")) {
//				showToast("连接服务器失败,请稍后再试11！");
//				disDialog();
//				return;
//			}
//
//			JSONObject jsonresult;
//
//			try {
//				jsonresult = new JSONObject(result);
//				switch (Integer.parseInt(jsonresult.getString("success"))) {
//				case 1:
//					if (msg.what==110){
//						showToast("提交成功");
//						picMaps.clear();
//						urlmaps.clear();
//						clearMapTempFronPref();
//						finish();
//					}else{
//
//
//						if (App.mUserInfo == null) {
//							App.mUserInfo = FileUtil
//									.readUser(SlaughterhouseRenZhengActivity.this);
//						}
//						if (App.mUserInfo != null) {
//							App.mUserInfo.setProvince(province);
//							App.mUserInfo.setCity(city);
//							App.mUserInfo.setArea(area);
//							App.mUserInfo.setAddress(address);
//							App.mUserInfo.setAuthentication("1");
//							App.mUserInfo.setX(String.valueOf(x));
//							App.mUserInfo.setY(String.valueOf(y));
//
//							FileUtil.saveUser(SlaughterhouseRenZhengActivity.this,
//									App.mUserInfo);
//							App.setDataInfo(App.mUserInfo);
//						}
//
//						MyBankRunnable bankrun = null;
//						if (mWdwEntry!=null&& !TextUtils.isEmpty(mWdwEntry.getId())) {
//
//							bankrun=  new MyBankRunnable("update",UrlEntry.UPP_CARD_URL);
//						}else{
//
//							bankrun= new MyBankRunnable("add",UrlEntry.ADD_CARD_URL);
//						}
//						new Thread(bankrun).start();
//					}
//
//					break;
//				case 4:
//					disDialog();
//					break;
//				default:
//					disDialog();
//					successType(jsonresult.get("success").toString(), "操作失败！");
//					break;
//
//				}
//			} catch (NumberFormatException e) {
//				e.printStackTrace();
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//
//			switch (msg.what) {
//
//			}
//		}
//
//	};

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

						Log.i("skaughterhouse",
								" skaughterhouse responseInfo.result = "
										+ responseInfo.result);

						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								SlaughterhouseEntry mInfoEntry;
								if (type.equals("1")) {// 新增或修改
									showToast("提交成功！");

									if (App.mUserInfo != null) {
										App.mUserInfo.setProvince(province);
										App.mUserInfo.setCity(city);
										App.mUserInfo.setArea(area);
										App.mUserInfo.setAddress(address);
										App.mUserInfo.setAuthentication("1");
										App.mUserInfo.setX(String.valueOf(x));
										App.mUserInfo.setY(String.valueOf(y));

										FileUtil.saveUser(
												SlaughterhouseRenZhengActivity.this,
												App.mUserInfo);
										App.setDataInfo(App.mUserInfo);
									}

									App.authentication = "1";
									finish();
								} else {

									if (jsonresult.has("source")) {
										if (jsonresult.optString("source")
												.equals("1")) {
											source = "1";
										}
									}
									GsonBuilder gsonb = new GsonBuilder();
									Gson gson = gsonb.create();
									mInfoEntry = gson.fromJson(
											jsonresult.toString(),
											SlaughterhouseEntry.class);
									setDataToView(mInfoEntry);

								}

							} else if (jsonresult.get("success").toString()
									.equals("4")) {
								if (jsonresult.has("source")) {
									if (jsonresult.optString("source").equals(
											"1")) {
										source = "1";
										GsonBuilder gsonb = new GsonBuilder();
										Gson gson = gsonb.create();
										SlaughterhouseEntry mInfoEntry = gson.fromJson(jsonresult.toString(),
												SlaughterhouseEntry.class);
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
		Intent locintent;
		AlertDialog.Builder builder;
		switch (v.getId()) {

		case R.id.tvAreaS:
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
			// Intent intent = new Intent(SlaughterhouseRenZhengActivity.this,
			// ChoseProvinceActivity.class);
			// intent.putExtra("add", "add");
			// startActivityForResult(intent, 22);
//			locintent = new Intent(SlaughterhouseRenZhengActivity.this,
//					LocationMapActivity.class);
//			locintent.putExtra("x", x);
//			locintent.putExtra("y", y);
//			startActivityForResult(locintent, LOCATION_ID);
			break;

		case R.id.tvIndustryS:

			Intent pzintent = new Intent(SlaughterhouseRenZhengActivity.this,
					IndustryActivity.class);
			startActivityForResult(pzintent, 25);
			break;
//		case R.id.btn_submit:
//			showDialog();
//			MyRunnable run = new MyRunnable("2",
//					UrlEntry.INSERT_SLAUGHTERHOUSE_URL); // add
//			new Thread(run).start();
//			break;
		case R.id.locationBtn:
			locintent = new Intent(SlaughterhouseRenZhengActivity.this,
					LocationMapActivity.class);
			locintent.putExtra("x", x);
			locintent.putExtra("y", y);
			startActivityForResult(locintent, LOCATION_ID);
			break;
		case R.id.tvBank:
//			clearTempFromPref();
			clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES1);
			clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES2);
			locintent = new Intent(SlaughterhouseRenZhengActivity.this,ActivityWithdrawals.class);
			locintent.putExtra("type", "rz");
			startActivityForResult(locintent, BANK_CARD_ID);
			break;

			case R.id.btn_next:
				if(!isNullStr())return;

				getBankInfo("");
				break;
			case R.id.btn_submit:
//				if ( TextUtils.isEmpty(etCardName.getText().toString())) {
//					showToast("请输入真实姓名！");
//					return;
//				}
//				if(!TextUtils.isEmpty(etIdentityCard.getText()
//						.toString())) {
//					String str = FileUtil.IDCardValidate(etIdentityCard.getText()
//							.toString());
//					if (!str.equals("")) {
//						showToast(str);
//						return;
//					}
//				}
//
//				if(!TextUtils.isEmpty(etCardNum.getText().toString())){
//					if (tvCardType.getText().toString().equals("农业银行卡")) {
//						String message = FileUtil.isAgricultureCard(etCardNum.getText().toString());
//						if (!message.contains("农业银行")) {
//							showToast(message);
//							return;
//						}
//					}
//
//				}
//				String msg = "";
//				msg =isPic("sfzzm","身份证正面图片");
//				if(msg.equals("ok")){
//				}else{
//					if (!msg.equals("")){
//						showToast(msg);
//						return;
//					}
//				}
//
//				msg = isPic("sfzfm","身份证反面图片");
//				if(msg.equals("ok")){
//				}else{
//					if (!msg.equals("")){
//						showToast(msg);
//						return;
//					}
//				}
//				//2017-3-20 认证信息和银行卡信息一起提交 先提交认证信息 成功后提交银行卡信息
//				if(mInfoEntry==null||TextUtils.isEmpty(mInfoEntry.getId())){
//					sendData(UrlEntry.INSERT_SLAUGHTERHOUSE_URL, "add");// add
//				}else{
//					sendData(UrlEntry.UPDATE_SLAUGHTERHOUSE_URL, "update");// update
//				}
//
////                sendData();//提交银行卡信息
				break;
			case R.id.tvCardType:
				builder = new AlertDialog.Builder(
						SlaughterhouseRenZhengActivity.this);

				mSingleChoiceIDs = 0;
				builder.setTitle("请选择");
				builder.setSingleChoiceItems(mPsItems, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int whichButton) {
								mSingleChoiceIDs = whichButton;

							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int whichButton) {

								tvCardType.setText(mPsItems[mSingleChoiceIDs]);

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
			case R.id.iv_camerasfzzm:
			case R.id.iv_identity:
				picTypeName = "sfzzm";
				showPopDialog(mImgSfzZm);
				break;
			case R.id.iv_camerasfzfm:
			case R.id.iv_fidentity:
				picTypeName = "sfzfm";
				showPopDialog(mImgSfzFm);
				break;
			case R.id.iv_cover:
			case R.id.iv_camera2:
				picTypeName = "fmtp";
				showPopDialog(mImgFmtp);
				break;
			case R.id.iv_yyzzidentity:
			case R.id.iv_yyzzcamera:
				picTypeName = "yyzz";
				showPopDialog(mImgYyzz);
				break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case TAKE_PHOTO:
				if (resultCode == -1 && !TextUtils.isEmpty(picUrl)) {
					picMaps.put(picTypeName, FileUtil.saveDataPic(picUrl));
					saveMapTempToPref(picMaps);
				}
				setImage();
				break;
			case TAKE_PICTURE:
				if (resultCode != -1) {
					return;
				}
				if (data == null) {
					return;
				}
				picUrl = getPath(getFile());
				startPhotoZoom1(data, picUrl, CORP_TAKE_PICTURE1);
				break;
			case CORP_TAKE_PICTURE1:
				if (resultCode != -1) {
					return;
				}
				picMaps = setPicToView(data, picUrl, picMaps, picTypeName);
				saveMapTempToPref(picMaps);
				setImage();
				break;
		case 22:
			if (data == null)
				return;
			Bundle bundle = data.getExtras();
			province = bundle.getString("province");
			city = bundle.getString("city");
			area = bundle.getString("area");
			tvAreaS.setText(province + " " + city + " " + area);
			break;

		case 25:
			if (data == null)
				return;
			Bundle bt = data.getExtras();
			String industry = bt.getString("industry");
			tvIndustryS.setText(industry);
			break;
		case LOCATION_ID:
			if (data == null)
				return;
			Bundle b = data.getExtras();
			if (!TextUtils.isEmpty(b.getString("province"))) {
				province = b.getString("province");
				city = b.getString("city");
				area = b.getString("area");
				tvAreaS.setText(province + " " + city + " " + area);
				x = b.getDouble("x");
				y = b.getDouble("y");
				address = b.getString("address");
				etAddress.setText(address);
			}
			break;
		case BANK_CARD_ID:
			if(resultCode != 0){
				tvBank.setText("已绑定银行卡");
			}
			break;
		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	
	/****************************************************************/
	class OnClickLintener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.item_popupwindows_camera: // 相机
					mPwDialog.dismiss();
					mFile = getFile();
					picUrl = getPath(mFile);
					takePhoto(TAKE_PHOTO, mFile);
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
		mPwDialog = new PopupWindows(SlaughterhouseRenZhengActivity.this,
				paramOnClickListener, parent);
		// 如果窗口存在，则更新
		mPwDialog.update();
	}

	private void setImage() {
		setImage("yyzz", mImgYyzz);
		setImage("fmtp", mImgFmtp);
		setImage("sfzzm", mImgSfzZm);
		setImage("sfzfm", mImgSfzFm);
	}

	protected void onPause() {
		super.onPause();

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		//传递数据
		final SerializableMap myMap=new SerializableMap();
		myMap.setPicMaps(picMaps);//将map数据添加到封装的myMap中
		myMap.setUrlmaps(urlmaps);//将map数据添加到封装的myMap中
		outState.putSerializable("map", myMap);
		super.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			SerializableMap serializableMap = (SerializableMap) savedInstanceState.get("map");
			picMaps = serializableMap.getPicMaps();
			urlmaps = serializableMap.getUrlmaps();
			initView();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 返回这里需要刷新
		initView();
	}

	private String  isPic(String pictype,String message){
		if (picMaps.containsKey(pictype)){
			if (new File(picMaps.get(pictype).sourcePath).exists()){
				return "ok";
			}else{
				return message+"选取错误，请重新选取。";
			}
		}else{
			if (urlmaps.containsKey(pictype)) {
				return "ok";
			}else

				return "请上传您的"+message;

		}

	}

	private void setImage(String name, ImageView view) {
		if (picMaps.size() == 0) {
			picMaps = readMapTempFromPref();
		}
		if (picMaps.containsKey(name)) {
			final ImageItem item = picMaps.get(name);
			ImageDisplayer.getInstance(this).displayBmp(view,
					item.thumbnailPath, item.sourcePath);
		} else {
			if (urlmaps.containsKey(name)) {
				bitmapUtils.display(view, UrlEntry.ip
						+ urlmaps.get(name));// 下载并显示图片
			} else {
				if (name.equals("sfzzm")) {
					view.setBackgroundResource(R.drawable.ic_sfzz);
				} else if (name.equals("sfzfm")) {
					view.setBackgroundResource(R.drawable.ic_sfzf);
				}else{
					view.setBackgroundResource(R.drawable.default_title);
				}
			}
		}
	}
//	private void sendData(String url, String type) {
//		showDialog();
//		MyRunnable run = new MyRunnable(type, url);
//		new Thread(run).start();
//	}
	private void getBankInfo(String id){
//        mInfoEntry.setId(id);
//		mLlFirst.setVisibility(View.GONE);
//		mLlTwo.setVisibility(View.VISIBLE);
//		showDialog();
//		sendDataByUUid();
		map = new HashMap<>();
		map.put("e.province",province);
		map.put("e.city", city);
		map.put("e.area", area);
		map.put("e.business", tvIndustryS
				.getText().toString());
		map.put("e.companyName", etCompany
				.getText().toString());
		map.put("e.realName", etPeople.getText()
				.toString());
		map.put("e.QQ",
				etQQ.getText().toString());
		map.put("e.intro", etDemand.getText()
				.toString());
		map.put("e.number", etKillNum.getText()
				.toString());
		map.put("e.address", etAddress.getText()
				.toString());

		if(picMaps.containsKey("fmtp")){
			map.put("uploadPic", picMaps.get("fmtp").sourcePath);
		}
		if(picMaps.containsKey("yyzz")){
			map.put("uploadImage", picMaps.get("yyzz").sourcePath);
		}
		map.put("e.phone", etPhone.getText()
				.toString());
		map.put("e.x",String.valueOf(x));
		map.put("e.y", String.valueOf(y));
		map.put("e.userID",App.userID);
		String url = "";
		if(mInfoEntry==null||TextUtils.isEmpty(mInfoEntry.getId())){
			url = UrlEntry.INSERT_SLAUGHTERHOUSE_URL;//, "add");// add
		}else{
			url = UrlEntry.UPDATE_SLAUGHTERHOUSE_URL;//, "update");// update
			map.put("e.id",mInfoEntry.getId());
		}
		Intent intent = new Intent(SlaughterhouseRenZhengActivity.this, ActivityWithdrawals.class);
		intent.putExtra("renzhengtype","tuzai");
		//传递数据
		final SerializableMap myMap=new SerializableMap();
		myMap.setDatamap(map);//将map数据添加到封装的myMap中
		Bundle bundle=new Bundle();
		bundle.putSerializable("map", myMap);
		bundle.putString("url",url);
		if (mInfoEntry!=null&&!TextUtils.isEmpty(mInfoEntry.getId())) {
			bundle.putString("status", mInfoEntry.getStatus());
			bundle.putString("reason", mInfoEntry.getReason());
		}
		intent.putExtras(bundle);
		startActivity(intent);
	}
//	private void sendDataByUUid() {
//
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("uuid", App.uuid);
//		final HttpUtils http = new HttpUtils();
//		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_CARDBYUUID_URL,
//				params, new RequestCallBack<String>() {
//					@Override
//					public void onStart() {
//					}
//
//					@Override
//					public void onLoading(long total, long current,
//										  boolean isUploading) {
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						disDialog();
//						Log.i("insert", " insert responseInfo.result = "
//								+ responseInfo.result);
//
//						JSONObject jsonresult;
//						try {
//							jsonresult = new JSONObject(responseInfo.result);
//							if (jsonresult.get("success").toString()
//									.equals("1")) {
//								// showToast("查询成功！");
//								GsonBuilder gsonb = new GsonBuilder();
//								Gson gson = gsonb.create();
//								mWdwEntry = gson.fromJson(
//										jsonresult.toString(),
//										Withdrawals.class);
//
//								setView(mWdwEntry);
//							} else if (jsonresult.get("success").toString()
//									.equals("5")) {
//								GsonBuilder gsonb = new GsonBuilder();
//								Gson gson = gsonb.create();
//								mWdwEntry = null;
//								mWdwEntry = gson.fromJson(
//										jsonresult.toString(),
//										Withdrawals.class);
//
//								setView(mWdwEntry);
//							} else {
//								successType(jsonresult.get("success")
//										.toString(), "操作失败！");
//								btn_submitP.setEnabled(false);
//							}
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							btn_submitP.setEnabled(false);
//							e.printStackTrace();
//						}
//
//					}
//
//					@Override
//					public void onFailure(HttpException error, String msg) {
//						showToast("服务器连接失败，请稍候再试！");
//						btn_submitP.setEnabled(false);
//						disDialog();
//
//					}
//				});
//	}
	private void setView(Withdrawals winfo) {
//		id = winfo.getId();
		if (winfo.getType().equals("nyBank")
				|| winfo.getType().equals("nybank")) {
			tvCardType.setText("农业银行卡");

		} else if (winfo.getType().equals("ncxys")) {
			tvCardType.setText("农村信用社");

		}else{
			tvCardType.setText("农业银行卡");
		}
		etCardNum.setText(winfo.getBankNum());
		etCardName.setText(winfo.getName());
		etIdentityCard.setText(winfo.getSfzh());

		if (!TextUtils.isEmpty(winfo.getSfzZm())) {
			urlmaps.put("sfzzm",winfo.getSfzZm().toString());
		}
		if (!TextUtils.isEmpty(winfo.getSfzFm())) {
			urlmaps.put("sfzfm",winfo.getSfzFm().toString());

		}

		setImage();


	}

//	public class MyBankRunnable implements Runnable {
//
//		private String type;
//		private String url;
//
//		public MyBankRunnable(String type, String url) {
//			this.type = type;
//			this.url = url;
//		}
//
//		@Override
//		public void run() {
//			String result = "";
//			Bundle data = new Bundle();
//			Message msg = new Message();
//			MultipartEntity params = new MultipartEntity();
//			try {
//				if (type.equals("update")) {
//					params.addPart("e.id", new StringBody(mWdwEntry.getId()));
//				}
//				if(picMaps.containsKey("sfzzm")){
//					params.addPart("sfzZm",new FileBody(new File(picMaps.get("sfzzm").sourcePath)));
//				}
//				if(picMaps.containsKey("sfzfm")){
//					params.addPart("sfzFm",new FileBody(new File(picMaps.get("sfzfm").sourcePath)));
//				}
//				params.addPart("sfzh", new StringBody(etIdentityCard.getText().toString()));
//				params.addPart("e.bankNum", new StringBody(etCardNum.getText().toString()));
//				params.addPart("e.name", new StringBody( etCardName.getText().toString()));
//				String type = "";
//				if (tvCardType.getText().toString().equals("农业银行卡")) {
//					type = "nyBank";
//				} else if (tvCardType.getText().toString().equals("农村信用社")) {
//					type = "ncxys";
//				}
//				params.addPart("e.type", new StringBody(type));
//
//				params.addPart("uuid", new StringBody(App.uuid));
//				HttpUtil http = new HttpUtil();
//				result = http.postDataMethod(url, params);
//
//				data.putString("value", result);
//				msg.what = 110;
//				msg.setData(data);
//				handler.sendMessage(msg);
//
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//
//	}

}
