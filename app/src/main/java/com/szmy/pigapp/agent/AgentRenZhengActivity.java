package com.szmy.pigapp.agent;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.activity.ChoseProvinceActivity;
import com.szmy.pigapp.activity.IndustryActivity;
import com.szmy.pigapp.entity.AgentEntry;
import com.szmy.pigapp.image.ImageDisplayer;
import com.szmy.pigapp.image.ImageItem;
import com.szmy.pigapp.image.PopupWindows;
import com.szmy.pigapp.tixian.ActivityWithdrawals;
import com.szmy.pigapp.tixian.Withdrawals;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.SerializableMap;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 认证经纪人
 * 
 * @author Administrator
 * 
 */
public class AgentRenZhengActivity extends BaseActivity implements OnClickListener {

	private TextView tv_state;
	private EditText et_industry, et_company, et_realname, et_occupation,
			et_qq, et_phone, et_area, et_introduce, et_identity;
	private Button btn_submit, btn_modify;
	private String province = "";
	private String city = "";
	private String area = "";
	private String pigFarmType = "", id = "";
	private BitmapUtils bitmapUtils;
	private String imgpath = "";

	private ImageButton mLocationBtn;
	public static final int LOCATION_ID = 29;
	public LocationService locationService;
	public Vibrator mVibrator;
	public static double x = 0;
	public static double y = 0;
	public LinearLayout mAddressLayout;
	public String source = "0";
	private static final int SCALE = 5;// 照片缩小比例
	private TextView tvBank;
	private final int BANK_CARD_ID = 25;
	private LinearLayout mLlBack;
	private Button mNextBtn, mBackBtn;
	private LinearLayout mLlFirst;
	private LinearLayout mLlTwo;
	private Boolean add = false;
	private Withdrawals mWdwEntry;
	private AgentEntry mAgentEntry;
	private ImageView mImgSfzZm;
	private ImageView mCamSfzZm;
	private ImageView mImgSfzFm;
	private ImageView mCamSfzFm;
	private EditText etCardNum;
	private EditText etCardName;
	private EditText etIdentityCard;// 身份证
	private Map<String, ImageItem> picMaps = new HashMap<>();
	private Map<String, String> urlmaps = new HashMap<>();
	public String picUrl = "";
	public String type = "";
	public String picTypeName = "";
	private File mFile;
	private final int TAKE_PHOTO = 0x0000134;
	private final int TAKE_PICTURE = 0x0000135;
	private final int CORP_TAKE_PICTURE1 = 0x0000136;
	private TextView tvCardType;
	private String[] mPsItems = new String[]{"农业银行卡", "农村信用社"};
	private int mSingleChoiceIDs = 0;
	private Map<String,String> map ;

	public static AgentRenZhengActivity instance = null;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		instance=this;
		setContentView(R.layout.activity_agent_auth);
		bitmapUtils = new BitmapUtils(this);
		((TextView) findViewById(R.id.def_head_tv)).setText("认证经纪人");
		SharedPreferences sp = getSharedPreferences(App.USERINFO, 0);
		if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
			App.mUserInfo = FileUtil.readUser(AgentRenZhengActivity.this);
			App.setDataInfo(App.mUserInfo);
		}
		initView();
		// 定位
		if (AppStaticUtil.isNetwork(getApplicationContext())) {
			locationService = new LocationService(getApplicationContext());
			locationService.registerListener(mListener);

		}

		showDialog();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		sendDataToHttp(UrlEntry.QUERY_AGENT_URL, params, "");
	}

	/*****
	 * aa@see copy funtion to you project
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
				
				// address = location.getStreet() + location.getStreetNumber();
				if (App.PROVINCE.equals("") ) {
					province = location.getProvince();
					city = location.getCity();
					area = location.getDistrict();
					et_area.setText(province + " " + city + " " + area);
				}

				System.out.println(x + "----------------" + y);
				System.out.println("addrStr" + location.getAddrStr());
				System.out.println(location.getProvince() + "-"
						+ location.getCity() + "-" + location.getDistrict()
						+ "-" + location.getStreet() + "-"
						+ location.getStreetNumber());
				locationService.stop();
			} else {
				et_area.setEnabled(true);
				et_area.setClickable(true);
			}
		}
	};

	private void initView() {
		tv_state = (TextView) findViewById(R.id.tv_state);
		et_industry = (EditText) findViewById(R.id.et_industry);
		et_company = (EditText) findViewById(R.id.et_company);
//		et_realname = (EditText) findViewById(R.id.et_realname);
		et_occupation = (EditText) findViewById(R.id.et_occupation);
		et_qq = (EditText) findViewById(R.id.et_qq);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_area = (EditText) findViewById(R.id.et_area);
		et_introduce = (EditText) findViewById(R.id.et_introduce);
		et_identity = (EditText) findViewById(R.id.et_identity);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_modify = (Button) findViewById(R.id.btn_modify);
		tvBank = (TextView) findViewById(R.id.tvBank);
		mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
		mLlFirst = (LinearLayout) findViewById(R.id.layoutone);
		mLlTwo = (LinearLayout) findViewById(R.id.layouttwo);
		mNextBtn = (Button) findViewById(R.id.btn_next_add);
		mNextBtn.setOnClickListener(this);
		mImgSfzFm = (ImageView) findViewById(R.id.iv_fidentity);
		mCamSfzFm = (ImageView) findViewById(R.id.iv_fcamera);
		mImgSfzZm = (ImageView) findViewById(R.id.iv_identity);
		mCamSfzZm = (ImageView) findViewById(R.id.iv_camera);
		etCardNum = (EditText) findViewById(R.id.etCardNum);
		etCardName = (EditText) findViewById(R.id.etCardName);
		etIdentityCard = (EditText) findViewById(R.id.etIdentityCard);
		tvCardType = (TextView) findViewById(R.id.tvCardType);
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
					clearMapTempFronPref();
					finish();
				}

			}
		});
		setImage();
	}

	private boolean isNullStr(){
		hideInput();
		if (TextUtils.isEmpty(et_industry.getText().toString())
				|| TextUtils.isEmpty(et_area.getText().toString())
				|| TextUtils.isEmpty(et_company.getText().toString())) {
			showToast("带'*'号为必填项,不能为空,请完善！");
			return false;
		}
		if (FileUtil.isNumeric(et_company.getText().toString())) {
			showToast("公司名称不能为纯数字！");
			return false;
		}
		if (et_phone.getText().toString().length() != 11) {
			showToast("请输入11位手机号码");
			return false;
		}
		return true;
	}

	/**
	 * @param agent
	 */
	private void setDataToView(AgentEntry agent) {
		id = agent.getId();
		et_area.setText(agent.getProvince() + " " + agent.getCity() + " "
				+ agent.getArea());
		province = agent.getProvince();
		city = agent.getCity();
		area = agent.getArea();
		if (agent.getIsSmrzBank().equals("n")) {
			tvBank.setText("未绑定银行卡");
		} else if (agent.getIsSmrzBank().equals("y")) {
			tvBank.setText("已绑定银行卡");
		}
		if (source.equals("1")) {
			if (!TextUtils.isEmpty(agent.getCompanyName())) {
				et_company.setEnabled(false);
				et_company.setTextColor(getResources().getColor(R.color.gray));
			}
//			if (!TextUtils.isEmpty(agent.getRealName())) {
//				et_realname.setEnabled(false);
//				et_realname.setTextColor(getResources().getColor(R.color.gray));
//			}
		}

		x = AppStaticUtil.getConvertDouble(agent.getX());
		y = AppStaticUtil.getConvertDouble(agent.getY());
		if (agent.getStatus().equals("2")) {
			tv_state.setText("审核通过");
		} else if (agent.getStatus().equals("3")) {
			tv_state.setText("审核未通过,原因：" + agent.getReason());
		} else if (agent.getStatus().equals("1")) {
			tv_state.setText("审核中");
		}
		if (agent.getIsSmrzBank().equals("n")) {
			tvBank.setText("未绑定银行卡");
		} else if (agent.getIsSmrzBank().equals("y")) {
			tvBank.setText("已绑定银行卡");
		}
		et_industry.setText(agent.getBusiness());
		et_company.setText(agent.getCompanyName());
		et_qq.setText(agent.getQQ().toString());
		et_phone.setText(agent.getPhone().toString());
		et_introduce.setText(agent.getIntro());

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

						Log.i("AGENT", " agent responseInfo.result = "
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


								GsonBuilder gsonb = new GsonBuilder();
								Gson gson = gsonb.create();
								mAgentEntry = gson.fromJson(
										jsonresult.toString(), AgentEntry.class);
								setDataToView(mAgentEntry);

							} else if (jsonresult.get("success").toString()
									.equals("4")) {
								add = true;
								if (jsonresult.has("isSmrzBank")) {
									if (jsonresult.optString("isSmrzBank")
											.equals("n")) {
										tvBank.setText("未绑定银行卡");
									} else if (jsonresult.optString(
											"isSmrzBank").equals("y")) {
										tvBank.setText("已绑定银行卡");
									}
								}
								if (jsonresult.has("source")) {
									if (jsonresult.optString("source").equals(
											"1")) {
										source = "1";
										GsonBuilder gsonb = new GsonBuilder();
										Gson gson = gsonb.create();
										 mAgentEntry = gson.fromJson(
												jsonresult.toString(),
												AgentEntry.class);
										setDataToView(mAgentEntry);
									}
								}
							} else {

								successType(jsonresult.get("success")
										.toString(), "操作失败，请稍后再试！");
								mNextBtn.setVisibility(View.GONE);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("操作失败,请稍后再试！");
						mNextBtn.setVisibility(View.GONE);
						disDialog();

					}
				});

	}

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.et_area:
			Intent intent = new Intent(AgentRenZhengActivity.this,
					ChoseProvinceActivity.class);
			intent.putExtra("add", "add");
			startActivityForResult(intent, App.ADDRESS_CODE);
			break;

		case R.id.et_industry:

			Intent pzintent = new Intent(AgentRenZhengActivity.this,
					IndustryActivity.class);
			startActivityForResult(pzintent, 25);
			break;
		case R.id.btn_next_add:
				if(!isNullStr())return;
			//2017-03-20 新增或修改成功后请求银行卡接口
			getBankInfo("");
//				MyRunnable runagent;
//				if(mAgentEntry==null||TextUtils.isEmpty(mAgentEntry.getId())){
//					runagent = new MyRunnable("2", UrlEntry.INSERT_AGENT_URL);// add
//				}else{
//					runagent = new MyRunnable("3", UrlEntry.UPDATE_AGENT_URL);// update
//				}
//				showDialog();
//				new Thread(runagent).start();
				break;
		case R.id.btn_submit:
			if ( TextUtils.isEmpty(etCardName.getText().toString())) {
				showToast("请输入真实姓名！");
				return;
			}
			if(!TextUtils.isEmpty(etIdentityCard.getText()
					.toString())) {
				String str = FileUtil.IDCardValidate(etIdentityCard.getText()
						.toString());
				if (!str.equals("")) {
					showToast(str);
					return;
				}
			}

			if(!TextUtils.isEmpty(etCardNum.getText().toString())){
				if (tvCardType.getText().toString().equals("农业银行卡")) {
					String message = FileUtil.isAgricultureCard(etCardNum.getText().toString());
					if (!message.contains("农业银行")) {
						showToast(message);
						return;
					}
				}
			}
			String msg = "";
			msg =isPic("sfzzm","身份证正面图片");
			if(msg.equals("ok")){
			}else{
				if (!msg.equals("")){
					showToast(msg);
					return;
				}
			}

			msg = isPic("sfzfm","身份证反面图片");
			if(msg.equals("ok")){
			}else{
				if (!msg.equals("")){
					showToast(msg);
					return;
				}
			}
			//2017-03-20 提交认证信息成功后提交银行卡信息
				MyRunnable runagent;
				if(mAgentEntry==null||TextUtils.isEmpty(mAgentEntry.getId())){
					runagent = new MyRunnable("2", UrlEntry.INSERT_AGENT_URL);// add
				}else{
					runagent = new MyRunnable("3", UrlEntry.UPDATE_AGENT_URL);// update
				}
				showDialog();
				new Thread(runagent).start();

//			sendData();//提交银行卡信息

			break;
		case R.id.tvBank:
			Intent locintent = new Intent(AgentRenZhengActivity.this,
					ActivityWithdrawals.class);
			locintent.putExtra("type", "rz");
			startActivityForResult(locintent, 27);
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
			case R.id.tvCardType:
				AlertDialog.Builder builder = new AlertDialog.Builder(
						AgentRenZhengActivity.this);

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

			if (TextUtils.isEmpty(et_industry.getText().toString())
					|| TextUtils.isEmpty(et_phone.getText().toString())
					|| TextUtils.isEmpty(et_area.getText().toString())
					|| TextUtils.isEmpty(et_company.getText().toString())
					|| TextUtils.isEmpty(tvBank.getText().toString())) {
				msg.what = 12;
				handler.sendMessage(msg);
				return;

			}
			//2016-9-5 修改:经纪人认证银行卡不是必选项
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


			MultipartEntity params = new MultipartEntity();
			try {
				params.addPart("", new StringBody(""));
				if (type.equals("3")) {
					params.addPart("e.id", new StringBody(id));
				}

				params.addPart("e.province", new StringBody(province));
				params.addPart("e.city", new StringBody(city));
				params.addPart("e.area", new StringBody(area));

				params.addPart("e.business", new StringBody(et_industry
						.getText().toString()));
				params.addPart("e.companyName", new StringBody(et_company
						.getText().toString()));
				// params.addPart("e.realName", new StringBody(et_realname
				// .getText().toString()));
				params.addPart("e.QQ", new StringBody(et_qq.getText()
						.toString()));
				params.addPart("e.intro", new StringBody(et_introduce.getText()
						.toString()));
				params.addPart("e.phone", new StringBody(et_phone.getText()
						.toString()));
				// params.addPart("e.card", new StringBody(mEtCard.getText()
				// .toString()));
				params.addPart("e.userID", new StringBody(App.userID));
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

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == 11) {
				disDialog();
				showToast("请上传您的证件照片！");
				return;
			} else if (msg.what == 12) {
				disDialog();
				showToast("带'*'号为必填项,不能为空,请完善！");
				return;
			} else if (msg.what == 13) {
				disDialog();
				showToast("公司名称不能为数字！");
				return;
			} else if (msg.what == 14) {
				disDialog();
				showToast("身份证格式错误！");
				return;
			} else if (msg.what == 15) {
				disDialog();
				showToast("请输入11位电话号码！");
				return;
			} else if (msg.what == 16) {
				disDialog();
				showToast("您输入的农业银行卡号不正确！");
				return;
			} else if (msg.what == 17) {
				disDialog();
				showToast("请先绑定银行卡");
				return;
			}

			Bundle data1 = msg.getData();
			String result = data1.getString("value");
			if (result.equals("")) {
				disDialog();
				showToast("连接服务器失败,请稍后再试！");
				return;
			}

			JSONObject jsonresult;

			try {
				jsonresult = new JSONObject(result);
				switch (Integer.parseInt(jsonresult.getString("success"))) {
				case 1:
//					showToast("提交成功！");
					if (msg.what==110){
						showToast("提交成功");
						picMaps.clear();
						urlmaps.clear();
						clearMapTempFronPref();
						finish();

						return;
					}
					if (App.mUserInfo == null) {
						App.mUserInfo = FileUtil
								.readUser(AgentRenZhengActivity.this);
					}
					if (App.mUserInfo != null) {
						App.mUserInfo.setProvince(province);
						App.mUserInfo.setCity(city);
						App.mUserInfo.setArea(area);
						App.mUserInfo.setAuthentication("1");
						FileUtil.saveUser(AgentRenZhengActivity.this,
								App.mUserInfo);
						App.setDataInfo(App.mUserInfo);
					}

					MyBankRunnable bankrun = null;
					if (mWdwEntry!=null&& !TextUtils.isEmpty(mWdwEntry.getId())) {

						bankrun=  new MyBankRunnable("update",UrlEntry.UPP_CARD_URL);
					}else{

						bankrun= new MyBankRunnable("add",UrlEntry.ADD_CARD_URL);
					}
					new Thread(bankrun).start();
					break;
				case 4:
					disDialog();
					break;
				default:
					disDialog();
					successType(jsonresult.get("success").toString(), "操作失败！");
					break;

				}
			} catch (NumberFormatException e) {
				disDialog();
				e.printStackTrace();
			} catch (JSONException e) {
				disDialog();
				e.printStackTrace();
			}

		}

	};
	public class MyBankRunnable implements Runnable {

		private String type;
		private String url;

		public MyBankRunnable(String type, String url) {
			this.type = type;
			this.url = url;
		}

		@Override
		public void run() {
			String result = "";
			Bundle data = new Bundle();
			Message msg = new Message();
			MultipartEntity params = new MultipartEntity();

			try {
				if (type.equals("update")) {
					params.addPart("e.id", new StringBody(mWdwEntry.getId()));
				}
				if(picMaps.containsKey("sfzzm")){
					params.addPart("sfzZm",new FileBody(new File(picMaps.get("sfzzm").sourcePath)));
				}
				if(picMaps.containsKey("sfzfm")){
					params.addPart("sfzFm",new FileBody(new File(picMaps.get("sfzfm").sourcePath)));
				}
				params.addPart("sfzh", new StringBody(etIdentityCard.getText().toString()));
				params.addPart("e.bankNum", new StringBody(etCardNum.getText().toString()));
				params.addPart("e.name", new StringBody( etCardName.getText().toString()));
				String type = "";
				if (tvCardType.getText().toString().equals("农业银行卡")) {
					type = "nyBank";
				} else if (tvCardType.getText().toString().equals("农村信用社")) {
					type = "ncxys";
				}
				params.addPart("e.type", new StringBody(type));

				params.addPart("uuid", new StringBody(App.uuid));
				HttpUtil http = new HttpUtil();
				result = http.postDataMethod(url, params);

				data.putString("value", result);
				msg.what = 110;
				msg.setData(data);
				handler.sendMessage(msg);

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}
	private void getBankInfo(String id){
//		mAgentEntry.setId(id);
//		mLlFirst.setVisibility(View.GONE);
//		mLlTwo.setVisibility(View.VISIBLE);
//		showDialog();
//		sendDataByUUid();
		map = new HashMap<>();
		map.put("e.province", province);
		map.put("e.city", city);
		map.put("e.area", area);

		map.put("e.business", et_industry
				.getText().toString());
		map.put("e.companyName",et_company
				.getText().toString());
		map.put("e.QQ", et_qq.getText()
				.toString());
		map.put("e.intro", et_introduce.getText()
				.toString());
		map.put("e.phone", et_phone.getText()
				.toString());

		map.put("e.userID",App.userID);
		map.put("e.x", String.valueOf(x));
		map.put("e.y", String.valueOf(y));
		String url = "";
		if(mAgentEntry==null||TextUtils.isEmpty(mAgentEntry.getId())){
			url = UrlEntry.INSERT_AGENT_URL;// add
		}else{
			url = UrlEntry.UPDATE_AGENT_URL;// update
			map.put("e.id",mAgentEntry.getId());
		}

		Intent intent = new Intent(AgentRenZhengActivity.this, ActivityWithdrawals.class);
		intent.putExtra("renzhengtype","agent");
		//传递数据
		final SerializableMap myMap=new SerializableMap();
		myMap.setDatamap(map);//将map数据添加到封装的myMap中
		Bundle bundle=new Bundle();
		bundle.putSerializable("map", myMap);
		bundle.putString("url",url);
		if (mAgentEntry!=null&&!TextUtils.isEmpty(mAgentEntry.getId())){
			bundle.putString("status",mAgentEntry.getStatus());
			bundle.putString("reason",mAgentEntry.getReason());
		}

		intent.putExtras(bundle);
		startActivity(intent);


	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case App.ADDRESS_CODE:
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
			et_industry.setText(industry);
			break;
		case 27:
			if (resultCode != 0) {
				tvBank.setText("已绑定银行卡");
			}

			break;
		case TAKE_PHOTO:
				if (resultCode == -1 && !TextUtils.isEmpty(picUrl)) {
					picMaps.put(picTypeName, FileUtil.saveDataPic(picUrl));
					System.out.print("picMapspicMapspicMaps"+picMaps);
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
		default:
			break;
		}

	}




	private void sendDataByUUid() {

		RequestParams params = new RequestParams();
		params.addBodyParameter("uuid", App.uuid);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_CARDBYUUID_URL,
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
						Log.i("insert", " insert responseInfo.result = "
								+ responseInfo.result);

						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.get("success").toString()
									.equals("1")) {
								// showToast("查询成功！");
								GsonBuilder gsonb = new GsonBuilder();
								Gson gson = gsonb.create();
								 mWdwEntry = gson.fromJson(
										jsonresult.toString(),
										Withdrawals.class);

								setView(mWdwEntry);
							} else if (jsonresult.get("success").toString()
									.equals("5")) {
								GsonBuilder gsonb = new GsonBuilder();
								Gson gson = gsonb.create();
								mWdwEntry = null;
								mWdwEntry = gson.fromJson(
										jsonresult.toString(),
										Withdrawals.class);

								setView(mWdwEntry);
							} else {
								successType(jsonresult.get("success")
										.toString(), "操作失败！");
								btn_submit.setVisibility(View.GONE);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							btn_submit.setVisibility(View.GONE);
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("服务器连接失败，请稍候再试！"+msg+error);
						btn_submit.setVisibility(View.GONE);
						disDialog();

					}
				});
	}

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
		System.out.println(winfo.getSfzFm());

		if (!TextUtils.isEmpty(winfo.getSfzZm())) {
//			bitmapUtils.display(mImgSfzZm, UrlEntry.ip
//					+ winfo.getSfzZm().toString());// 下载并显示图片
			urlmaps.put("sfzzm",winfo.getSfzZm().toString());
		}
		if (!TextUtils.isEmpty(winfo.getSfzFm())) {
//			bitmapUtils.display(mImgSfzZm, UrlEntry.ip
//					+ winfo.getSfzFm().toString());// 下载并显示图片
			urlmaps.put("sfzfm",winfo.getSfzFm().toString());

		}

		setImage();


	}
	private void setImage()
	{
		setImage("sfzzm", mImgSfzZm);
		setImage("sfzfm", mImgSfzFm);
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
				}
			}
		}
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

	public void sendData() {

		RequestParams params = new RequestParams();
		String url = "";

		if (mWdwEntry!=null&& !TextUtils.isEmpty(mWdwEntry.getId())) {
			url = UrlEntry.UPP_CARD_URL;
			params.addBodyParameter("e.id", mWdwEntry.getId());
		}else{
			url = UrlEntry.ADD_CARD_URL;
		}
		params.addBodyParameter("uuid", App.uuid);
		params.addBodyParameter("e.bankNum", etCardNum.getText().toString());
		params.addBodyParameter("e.name", etCardName.getText().toString());
		params.addBodyParameter("e.type", "nyBank");
		if(picMaps.containsKey("sfzzm")){
			params.addBodyParameter("sfzZm",new File(picMaps.get("sfzzm").sourcePath));
		}
		if(picMaps.containsKey("sfzfm")){
			params.addBodyParameter("sfzFm",new File(picMaps.get("sfzfm").sourcePath));
		}

		params.addBodyParameter("sfzh",String.valueOf(etIdentityCard.getText().toString()));
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
						Log.i("insertyhk", " insert responseInfo.result = "
								+ responseInfo.result);

						JSONObject jsonresult;
						try {
							jsonresult = new JSONObject(responseInfo.result);
							if (jsonresult.optString("success").toString()
									.equals("1")) {
								showToast("提交成功");
								picMaps.clear();
								urlmaps.clear();
								clearMapTempFronPref();
								finish();


							} else {
								successType(jsonresult.get("success")
										.toString(), "操作失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToast("服务器连接失败，请稍候再试！");
						disDialog();
					}
				});
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("yezzimagepath1", picUrl);
		outState.putString("type", type);

		super.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			picUrl = savedInstanceState.getString("yezzimagepath1");
			type = savedInstanceState.getString("type");
			initView();
		}
	}
	private PopupWindows mPwDialog;

	private void showPopDialog(View parent) {

		// 自定义的单击事件
		OnClickLintener paramOnClickListener = new OnClickLintener();
		mPwDialog = new PopupWindows(AgentRenZhengActivity.this,
				paramOnClickListener, parent);
		// 如果窗口存在，则更新
		mPwDialog.update();
	}

	class OnClickLintener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.item_popupwindows_camera: // 相机
					mFile = getFile();
					picUrl = getPath(mFile);
					System.out.print("picUrl"+picUrl);
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
}
