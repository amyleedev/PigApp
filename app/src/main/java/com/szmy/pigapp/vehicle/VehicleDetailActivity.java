package com.szmy.pigapp.vehicle;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.activity.LoginActivity;
import com.szmy.pigapp.activity.PayActivity;
import com.szmy.pigapp.adapter.BannerAdapter;
import com.szmy.pigapp.entity.Banner;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.ButtonFlat;
import com.szmy.pigapp.widget.CircleFlowIndicator;
import com.szmy.pigapp.widget.MyScrollView;
import com.szmy.pigapp.widget.ViewFlow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDetailActivity extends BaseActivity implements
		OnClickListener {
	private TextView tv_price, tv_date, tv_type, tv_carnumber, tv_count,
			tv_comname, tv_address, tv_people, tv_phone, tv_remark, tv_status,
			tv_finalprice, tv_buyorderphone, tv_buyordername, btn_tellphone;
	private TextView mTvCreateTime,mTvCreatePeo;
	private String id;
	private static final int PICK_PICTURE = 02;
	private ArrayList<String> biglist = new ArrayList<String>();// 大图集合
	private List<String> list = new ArrayList<String>();
	private BitmapUtils bitmapUtils;
	private Vehicle vehicleEntry;
	private ImageButton btn_add;
	private Button order_btn, qxorder_btn;
	private String createAccount;// 发布者;
	private RelativeLayout finalpricelayout;
	private int status = 0;
	private String price = "", buyuserid;
	private LinearLayout purchaserlayout;
	private int mSingleChoiceID = 0;
	private int mSingleChoiceID2 = 0;
	private String[] mItems = { "立即付款", "面对面付款" };
	private ButtonFlat mBtnCall;
	private ButtonFlat mBtnCallBuyer;
	private ImageView mIvCover;
	private TextView mTvJuli,mTvPigType;
	private LinearLayout mLlLoading;
	private LinearLayout mLlLoadFailure;
	private FrameLayout mFlBanner;
	private MyScrollView mSv;
	private ViewFlow mVf;
	private CircleFlowIndicator mCfi;
	private List<Banner> bannerList = new ArrayList<Banner>();
	private BannerAdapter mBannerAdapter;
	private LayoutParams params;
	private int width = 0;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_vehicle_detail);
		((TextView) findViewById(R.id.def_head_tv)).setText("物流车辆详情");
		id = getIntent().getStringExtra("id");
		btn_add = (ImageButton) findViewById(R.id.add_btn);
		bitmapUtils = new BitmapUtils(getApplicationContext());
		SharedPreferences sp = getSharedPreferences(App.USERINFO, 0);
		if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
			App.mUserInfo = FileUtil.readUser(this);
			App.setDataInfo(App.mUserInfo);
		}
		initView();
		loadData(id);
	}

	private void initView() {
		mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
		mLlLoadFailure = (LinearLayout) findViewById(R.id.ll_load_failure);
		mLlLoadFailure.setOnClickListener(this);
		mSv = (MyScrollView) findViewById(R.id.sv);
		mFlBanner = (FrameLayout) findViewById(R.id.fl_banner);
		mVf = (ViewFlow) findViewById(R.id.vf);
		mCfi = (CircleFlowIndicator) findViewById(R.id.cfi);
		mSv.setView(mVf);
		width = getSize();
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_carnumber = (TextView) findViewById(R.id.tv_carnumber);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_count = (TextView) findViewById(R.id.tv_count);
		tv_remark = (TextView) findViewById(R.id.tv_remark);
		tv_comname = (TextView) findViewById(R.id.tv_comname);
		btn_add = (ImageButton) findViewById(R.id.add_btn2);
		mIvCover = (ImageView) findViewById(R.id.iv_cover);
		btn_tellphone = (TextView) findViewById(R.id.phone_btn);
		btn_add.setOnClickListener(this);
		tv_people = (TextView) findViewById(R.id.tv_people);
		tv_remark = (TextView) findViewById(R.id.tv_remark);
		qxorder_btn = (Button) findViewById(R.id.qxorder_btn);
		order_btn = (Button) findViewById(R.id.order_btn);
		tv_status = (TextView) findViewById(R.id.tv_status);
		finalpricelayout = (RelativeLayout) findViewById(R.id.finalpricelayout);
		tv_finalprice = (TextView) findViewById(R.id.tv_finalprice);
		purchaserlayout = (LinearLayout) findViewById(R.id.purchaserlayout);
		tv_buyorderphone = (TextView) findViewById(R.id.tv_buyorderphone);
		tv_buyordername = (TextView) findViewById(R.id.tv_buyordername);
		mBtnCall = (ButtonFlat) findViewById(R.id.btn_call);
		mBtnCall.setOnClickListener(this);
		mBtnCallBuyer = (ButtonFlat) findViewById(R.id.btn_call_buyer);
		mBtnCallBuyer.setOnClickListener(this);
		mTvJuli = (TextView) findViewById(R.id.tv_juli);
		mTvPigType = (TextView) findViewById(R.id.tv_pigtype);
		mTvCreateTime = (TextView) findViewById(R.id.tv_time);
		mTvCreatePeo = (TextView) findViewById(R.id.tv_builder);
	}

	private void setData(final Vehicle vehicle) {
		vehicleEntry = vehicle;
		createAccount = vehicle.getCreateAccount();
		buyuserid = vehicle.getPurchaserID();
		tv_price.setText(vehicle.getPrice());
		tv_type.setText(vehicle.getType());
		if(TextUtils.isEmpty(vehicle.getCarRadius())||vehicle.getCarRadius().equals("0")){
			mTvJuli.setText("不限");
		}else
		mTvJuli.setText(vehicle.getCarRadius()+"公里");
		if(TextUtils.isEmpty(vehicle.getPigType())){
			mTvPigType.setText("不限");
		}else
		mTvPigType.setText(vehicle.getPigType());
		if (vehicle.getStartTime().equals("")) {
			tv_date.setText("不限");
		} else if (vehicle.getEndTime().equals("")) {
			tv_date.setText(vehicle.getStartTime() + "至 不限");
		} else {
			tv_date.setText(vehicle.getStartTime() + "至" + vehicle.getEndTime());
		}
		tv_carnumber.setText(vehicle.getCarNum());
		tv_address.setText(vehicle.getProvince() + vehicle.getCity()
				+ vehicle.getArea() + vehicle.getAddress());
		tv_count.setText(vehicle.getCapacity() + "头");
		tv_remark.setText(vehicle.getRemark());
		tv_comname.setText(vehicle.getName());
		// tv_phone.setText(vehicle.getPhone());
		tv_people.setText(vehicle.getLinkman());
		mTvCreatePeo.setText(vehicle.getName());
		
		tv_finalprice.setText(vehicle.getFinalPrice());
		tv_buyordername.setText(vehicle.getPurchaserName());
		tv_buyorderphone.setText(vehicle.getPurchaserPhone());
		tv_buyorderphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog("确定拨打电话？", vehicle.getPurchaserPhone().toLowerCase());

			}
		});
		if (!TextUtils.isEmpty(App.uuid)
				&& vehicle.getCreateAccount().equals(
						sp.getString("username", ""))) {
			btn_add.setVisibility(View.VISIBLE);
		}
		
		btn_tellphone.setText(vehicle.getPhone());
		btn_tellphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 用intent启动拨打电话
				// Intent intent = new Intent(Intent.ACTION_CALL,
				// Uri.parse("tel:"
				// + vehicle.getPhone().toLowerCase()));
				// startActivity(intent);
				dialog("是否确定拨打电话？", vehicle.getPhone().toLowerCase());

			}
		});

		status = Integer.parseInt(vehicle.getStatus().toString());
		if (status == 1) {
			if (isLoginSell()) {
				btn_add.setVisibility(View.VISIBLE);
			} else {
				btn_add.setVisibility(View.GONE);
			}
		} else {
			purchaserlayout.setVisibility(View.VISIBLE);
			btn_add.setVisibility(View.GONE);
		}
		switch (status) {
		case 1:// 未下单
			if (isLoginSell()) {
				order_btn.setVisibility(View.GONE);

			} else {
				order_btn.setVisibility(View.VISIBLE);

			}
			order_btn.setText("下   单");
			qxorder_btn.setVisibility(View.GONE);
			tv_status.setText("[未成交]");

			break;
		case 2:// 已下单

			if (isLoginSell()) {
				qxorder_btn.setVisibility(View.GONE);
				order_btn.setVisibility(View.VISIBLE);
				order_btn.setText("确认订单");

			} else if (isLoginBuy()) {
				order_btn.setVisibility(View.GONE);
				qxorder_btn.setVisibility(View.VISIBLE);
			} else {

				order_btn.setVisibility(View.GONE);
				qxorder_btn.setVisibility(View.GONE);

			}

			tv_status.setText("[已下单]");

			break;

		case 3:// 确认订单
			tv_status.setText("[已确认订单]");
			finalpricelayout.setVisibility(View.GONE);

			if (isLoginSell()) {
				order_btn.setVisibility(View.VISIBLE);
				order_btn.setText("完成运输");

			} else {
				order_btn.setVisibility(View.GONE);
				qxorder_btn.setVisibility(View.GONE);
			}

			break;
		case 5:// 成交
			finalpricelayout.setVisibility(View.VISIBLE);
			qxorder_btn.setVisibility(View.GONE);
			order_btn.setVisibility(View.GONE);

			tv_status.setText("[交易成功]");

			break;
		case 4://
			tv_status.setText("[待付款]");
			finalpricelayout.setVisibility(View.VISIBLE);
			if (isLoginBuy()) {

				order_btn.setVisibility(View.VISIBLE);
				order_btn.setText("支付");

			} else {
				qxorder_btn.setVisibility(View.GONE);
				order_btn.setVisibility(View.GONE);
			}
			break;
		default:
			break;

		}
		order_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				switch (status) {
				case 1:// 未下单
					if (isLogin(getApplicationContext())) {
						showToast("请先登录！");
					} else
						// orderdialog("需要支付500元的保障金,是否确定下单？(提示：暂仅支持农业银行卡支付)",
						// "1");
						orderdialog("确定下单？", "1");
					break;
				case 2:// 已下单 卖家确认订单

					orderdialog("确认订单？", "2");

					break;
				case 3:// 已确认订单 物流确认运输完成
					final EditText et = new EditText(VehicleDetailActivity.this);
					et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
					new Builder(VehicleDetailActivity.this)
							.setTitle("请输入最终成交金额")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setView(et)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											price = et.getText().toString()
													.trim();
											if (TextUtils.isEmpty(price)) {
												showToast("请输入有效金额！");
												return;
											}
											// orderdialog("买卖双方需要支付2000元的保障金,是否确认订单？(提示：目前仅支持农业银行卡！)",
											// "2");
											orderdialog("确认完成运输？", "3");
										}
									}).setNegativeButton("取消", null).show();

					break;
				case 4:// 卖家已确认订单 买家支付价格

					Builder builder = new Builder(
							VehicleDetailActivity.this);

					mSingleChoiceID = 0;
					builder.setTitle("请选择");
					builder.setSingleChoiceItems(mItems, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									mSingleChoiceID = whichButton;

								}
							});
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									if (mSingleChoiceID == 1) {
										showbankdialog();
									} else {
										orderdialog("确认跳转到农业银行支付？", "4");
									}

								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							});
					builder.create().show();

					// orderdialog("确认跳转到农业银行支付？", "4");
					break;

				default:
					break;

				}

			}
		});
		qxorder_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// 取消订单
				orderdialog("确定取消订单？", "0");

			}
		});

	}

	private void loadData(String id) {

		RequestParams params = new RequestParams();

		params.addBodyParameter("e.id", id);

		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_VEHIVLE_BYID_URL,
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

						getData(responseInfo.result);

						Log.i("insert", " insert responseInfo.result = "
								+ responseInfo.result);

					}

					@Override
					public void onFailure(HttpException error, String msg) {

						
						if (mLlLoading.getVisibility() == View.VISIBLE) {
							mLlLoading.setVisibility(View.GONE);
							mLlLoadFailure.setVisibility(View.VISIBLE);
						}

					}
				});
	}

	public void getData(String result) {
		list.clear();
		bannerList.clear();
		try {
			JSONObject jsonresult = new JSONObject(result);
			if (jsonresult.get("success").toString().equals("1")) {
				// showToast("查询成功！");
				GsonBuilder gsonb = new GsonBuilder();
				Gson gson = gsonb.create();
				Vehicle mInfoEntry = gson.fromJson(jsonresult.toString(),
						Vehicle.class);
				vehicleEntry = mInfoEntry;
				setData(mInfoEntry);
				// 图片
				if (!jsonresult.get("pictureList").toString().equals("")) {
					JSONArray imgarray = new JSONArray(jsonresult.get(
							"pictureList").toString());
					for (int i = 0; i < imgarray.length(); i++) {
						JSONObject jobj2 = imgarray.optJSONObject(i);
						list.add(UrlEntry.ip + jobj2.getString("picture"));
						Banner banner = new Banner();
						banner.setImgPath(jobj2.getString("picture"));
						bannerList.add(banner);
					}
					if (mLlLoading.getVisibility() == View.VISIBLE) {
						mLlLoading.setVisibility(View.GONE);
					}
					if (bannerList.size() > 0) {
						mBannerAdapter = new BannerAdapter(this, bannerList);
						mVf.setAdapter(mBannerAdapter);
						mVf.setmSideBuffer(bannerList.size());
						mVf.setFlowIndicator(mCfi);
						mVf.setTimeSpan(4000);
						mVf.setSelection(0);
						mVf.startAutoFlowTimer(); // 启动自动播放
						mFlBanner.setVisibility(View.VISIBLE);
						params = new LayoutParams((int) (width),
								(int) (width / 2));
						mFlBanner.setLayoutParams(params);
					}
				}

			} else {
				if (mLlLoading.getVisibility() == View.VISIBLE) {
					mLlLoading.setVisibility(View.GONE);
					mLlLoadFailure.setVisibility(View.VISIBLE);
				}
				successType(jsonresult.get("success").toString(), "查询失败！");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	public class GridViewAdapter extends BaseAdapter {
		private List<String> list;
		private Context context;

		public GridViewAdapter(Context context, List<String> list) {
			this.list = list;
			this.context = context;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			MyView tag;
			if (convertView == null) {
				View v = LayoutInflater.from(context).inflate(
						R.layout.item_published_grida, null);
				tag = new MyView();
				tag.imageView = (ImageView) v
						.findViewById(R.id.item_grid_image);
				v.setTag(tag);
				convertView = v;
			} else {
				tag = (MyView) convertView.getTag();
			}
			String image_path = list.get(position);
			bitmapUtils.display(tag.imageView, image_path);// 下载并显示图片

			return convertView;
		}

	}

	class MyView {
		ImageView imageView;

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.add_btn2:
			NewVehicleActivity.mDataList.clear();
			clearTempFromPref();

			final Dialog dialog = new Dialog(VehicleDetailActivity.this, "提示", "请选择您要执行的操作！");
			dialog.addCancelButton("删除信息");
			dialog.addAccepteButton("编辑信息");
			dialog.setOnAcceptButtonClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
					clearTempFromPref();
					Intent intent = new Intent(
							VehicleDetailActivity.this,
							NewVehicleActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("mVehicle", vehicleEntry);
					intent.putExtras(bundle);
					intent.putExtra("upp", "upp");
					startActivityForResult(intent, 11);

				}
			});
			dialog.setOnCancelButtonClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
					MyRunnable runnable = new MyRunnable();
					new Thread(runnable).start();
				}
			});
			dialog.show();
//			AlertDialog.Builder builder = new AlertDialog.Builder(
//					VehicleDetailActivity.this);
//
//			// builder.setIcon(R.drawable.icon);
//			builder.setTitle("提示");
//			builder.setMessage("请选择您要执行的操作！");
//
//			builder.setPositiveButton("编辑信息",
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							clearTempFromPref();
//							Intent intent = new Intent(
//									VehicleDetailActivity.this,
//									NewVehicleActivity.class);
//							Bundle bundle = new Bundle();
//							bundle.putSerializable("mVehicle", vehicleEntry);
//							intent.putExtras(bundle);
//							intent.putExtra("upp", "upp");
//							startActivityForResult(intent, 11);
//
//						}
//					});
//
//			builder.setNegativeButton("删除信息",
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							MyRunnable runnable = new MyRunnable();
//							new Thread(runnable).start();
//						}
//					});
//
//			builder.create().show();

			break;

		case R.id.btn_call:
			call(vehicleEntry.getPhone());
			break;
		case R.id.btn_call_buyer:
			call(vehicleEntry.getPurchaserPhone());
			break;

		}
	}

	private void call(final String phone) {
		if (FileUtil.isBlank(phone)) {
			showToast("电话为空！");
			return;
		}
		dialog("确定拨打电话？",phone);
//		AlertDialog.Builder builder = new Builder(this);
//		builder.setMessage("确定拨打电话？");
//		builder.setTitle("提示");
//		builder.setPositiveButton("确认",
//				new android.content.DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						Intent intent = new Intent(Intent.ACTION_CALL, Uri
//								.parse("tel:" + phone.toLowerCase()));
//						startActivity(intent);
//					}
//				});
//		builder.setNegativeButton("取消", null);
//		builder.create().show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		list.clear();
		loadData(id);
	}

	public class MyRunnable implements Runnable {

		@Override
		public void run() {
			Message msg = new Message();
			Bundle data = new Bundle();
			String result = "";
			MultipartEntity mpEntity = new MultipartEntity();

			try {
				mpEntity.addPart("e.id", new StringBody(id));
				mpEntity.addPart("uuid", new StringBody(App.uuid));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpUtil http = new HttpUtil();
			result = http.postDataMethod(UrlEntry.DELETE_VEHIVLE_BYID_URL,
					mpEntity);

			data.putString("value", result);
			msg.setData(data);
			msg.what = 0; // 删除

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

			case 0:// 删除
				if (!result.equals("")) {
					try {
						jsonresult = new JSONObject(result);
						if (jsonresult.get("success").toString().equals("1")) {
							showToast("删除成功！");
							setResult(11);
							finish();
						} else if (jsonresult.get("success").toString()
								.equals("3")) {
							showToast("删除失败！");
						} else if (jsonresult.get("success").toString()
								.equals("0")) {
							showToast("用户信息错误，请重新登录！");
							editor.clear();
							editor.commit();
							Intent intent = new Intent(
									VehicleDetailActivity.this,
									LoginActivity.class);
							startActivity(intent);

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				break;
			case 1:
				if (!result.equals("")) {
					try {
						jsonresult = new JSONObject(result);
						if (jsonresult.get("success").toString().equals("1")) {
							showToast("取消成功！");
							loadData(id);

							// setResult(11);
							// finish();
						} else if (jsonresult.get("success").toString()
								.equals("3")) {
							showToast("取消失败！");
						} else if (jsonresult.get("success").toString()
								.equals("0")) {
							showToast("用户信息错误，请重新登录！");
							editor.clear();
							editor.commit();
							Intent intent = new Intent(
									VehicleDetailActivity.this,
									LoginActivity.class);
							startActivity(intent);

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				break;
			case 2:
				if (!result.equals("")) {
					try {
						jsonresult = new JSONObject(result);
						if (jsonresult.get("success").toString().equals("1")) {
							Builder builder = new Builder(
									VehicleDetailActivity.this);
//							builder.setMessage("下单成功，等待对方确认订单！");
//							builder.setTitle("提示");
//							builder.setPositiveButton("确定", null);
//
//							builder.create().show();
							showDialog1(VehicleDetailActivity.this,"下单成功，等待对方确认订单！");
							loadData(id);
							// finish();
						} else if (jsonresult.get("success").toString()
								.equals("3")) {
							showToast("下单失败！");
						} else if (jsonresult.get("success").toString()
								.equals("0")) {
							showToast("用户信息错误，请重新登录！");
							editor.clear();
							editor.commit();
							Intent intent = new Intent(
									VehicleDetailActivity.this,
									LoginActivity.class);
							startActivity(intent);

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				break;
			case 3:
				if (!result.equals("")) {
					try {
						jsonresult = new JSONObject(result);
						if (jsonresult.get("success").toString().equals("1")) {
							showToast("确认订单成功！");
							loadData(id);
							// finish();
						} else if (jsonresult.get("success").toString()
								.equals("3")) {
							showToast("确认订单失败！");
						} else if (jsonresult.get("success").toString()
								.equals("0")) {
							showToast("用户信息错误，请重新登录！");
							editor.clear();
							editor.commit();
							Intent intent = new Intent(
									VehicleDetailActivity.this,
									LoginActivity.class);
							startActivity(intent);

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				break;
			case 4:
				if (!result.equals("")) {
					try {
						jsonresult = new JSONObject(result);
						if (jsonresult.get("success").toString().equals("1")) {
							showToast("确认完成！");
							loadData(id);
						} else if (jsonresult.get("success").toString()
								.equals("3")) {
							showToast("操作失败！");
						} else if (jsonresult.get("success").toString()
								.equals("0")) {
							showToast("用户信息错误，请重新登录！");
							editor.clear();
							editor.commit();
							Intent intent = new Intent(
									VehicleDetailActivity.this,
									LoginActivity.class);
							startActivity(intent);

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				break;
			case 5:

				System.out.println(result + "getString");
				if (!result.equals("")) {

					JSONObject json;
					try {
						json = new JSONObject(result);
						if (json.get("success").toString().equals("1")) {

							if (json.optString("url").toString().equals("1")) {
								showToast("下单失败：该信息已被下单!");
							} else {

								Intent intent = new Intent(
										VehicleDetailActivity.this,
										PayActivity.class);
								intent.putExtra("url", json.optString("url")
										.toString());
								startActivityForResult(intent, 11);
							}
						} else {
							successType(json.get("success").toString(), "支付失败！");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				break;
			case 55:// 付款订单
				if (!result.equals("")) {
					try {
						jsonresult = new JSONObject(result);
						if (jsonresult.get("success").toString().equals("1")) {
							// showToast("付款成功！");
							loadData(id);

						} else if (jsonresult.get("success").toString()
								.equals("0")) {
							showToast("用户信息错误,请重新登录！");
							relogin(getApplicationContext());
						} else if (jsonresult.get("success").toString()
								.equals("3")) {
							showToast("操作失败！");
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

	// 判断信息的发布者和登录者是否是同一人
	public boolean isLoginSell() {
		boolean flag = false;
		if (!TextUtils.isEmpty(App.uuid) && App.username.equals(createAccount)) {
			flag = true;
		}
		return flag;
	}

	// 判断登录者和买家是否是同一账户
	public boolean isLoginBuy() {
		boolean flag = false;
		if (!TextUtils.isEmpty(App.uuid) && App.userID.equals(buyuserid)) {
			flag = true;
		}
		return flag;
	}

	public void orderdialog(String message, final String type) {
		final Dialog dialog = new Dialog(this, "提示", message);
		dialog.addCancelButton("取消");
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
				showDialog();
				switch (Integer.parseInt(type)) {
					case 0: // 取消订单
						MyRunnable2 runnable0 = new MyRunnable2("1");
						new Thread(runnable0).start();
						break;
					case 1:// 下单
						if(App.usertype.equals("5")){
							showDialog1(VehicleDetailActivity.this,"下单失败，自然人不能对物流订单进行操作。");
							return;
						}
						// new Thread(buyrunnable).start();
						MyRunnable2 runnable1 = new MyRunnable2("2");
						new Thread(runnable1).start();
						break;
					case 2:// 确认订单
						MyRunnable2 runnable2 = new MyRunnable2("3");
						new Thread(runnable2).start();

						break;
					case 3:// 运输完成
						MyRunnable2 runnable3 = new MyRunnable2("4");
						new Thread(runnable3).start();
						break;

					case 4:// 付款
						new Thread(runnable).start();
						break;

					default:
						break;
				}
			}
		});
		dialog.setOnCancelButtonClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.show();
//		AlertDialog.Builder builder = new Builder(this);
//		builder.setMessage(message);
//		builder.setTitle("提示");
//		builder.setPositiveButton("确认", new AlertDialog.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				showDialog();
//				switch (Integer.parseInt(type)) {
//				case 0: // 取消订单
//					MyRunnable2 runnable0 = new MyRunnable2("1");
//					new Thread(runnable0).start();
//					break;
//				case 1:// 下单
//					if(App.usertype.equals("5")){
//						showDialog1(VehicleDetailActivity.this,"下单失败，自然人不能对物流订单进行操作。");
//						return;
//					}
//					// new Thread(buyrunnable).start();
//					MyRunnable2 runnable1 = new MyRunnable2("2");
//					new Thread(runnable1).start();
//					break;
//				case 2:// 确认订单
//					MyRunnable2 runnable2 = new MyRunnable2("3");
//					new Thread(runnable2).start();
//
//					break;
//				case 3:// 运输完成
//					MyRunnable2 runnable3 = new MyRunnable2("4");
//					new Thread(runnable3).start();
//					break;
//
//				case 4:// 付款
//					new Thread(runnable).start();
//					break;
//
//				default:
//					break;
//				}
//
//			}
//		});
//		builder.setNegativeButton("取消", null);
//		builder.create().show();

	}

	public class MyRunnable2 implements Runnable {
		private String type;

		public MyRunnable2(String type) {
			this.type = type;
		}

		@Override
		public void run() {
			Message msg = new Message();
			Bundle data = new Bundle();
			String result = "";
			MultipartEntity mpEntity = new MultipartEntity();
			String URL = UrlEntry.UPDATE_VEHIVLE_BYID_URL;
			try {

				if (type.equals("4")) {
					mpEntity.addPart("e.finalPrice", new StringBody(price));
					mpEntity.addPart("e.id", new StringBody(id));
					mpEntity.addPart("type", new StringBody("fk"));
					mpEntity.addPart("e.status", new StringBody(type));
					mpEntity.addPart("uuid", new StringBody(App.uuid));
				} else {
					if (type.equals("1")) {
						URL = UrlEntry.EXIT_VEHIVLEORDER_URL;
					}
					mpEntity.addPart("e.status", new StringBody(type));
					mpEntity.addPart("e.id", new StringBody(id));
					mpEntity.addPart("uuid", new StringBody(App.uuid));
				}
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}
			HttpUtil http = new HttpUtil();
			result = http.postDataMethod(URL, mpEntity);

			data.putString("value", result);
			msg.setData(data);
			if (type.equals("5")) {
				type = "55";
			}
			msg.what = Integer.parseInt(type);

			handler.sendMessage(msg);
		}

	}

	// 付款
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			Message msg = new Message();
			Bundle data = new Bundle();
			String result = "";

			MultipartEntity mpEntity = new MultipartEntity();

			try {
				// mpEntity.addPart("test", new StringBody("1111"));
				mpEntity.addPart("id", new StringBody(id));
				mpEntity.addPart("e.purchaserID", new StringBody(App.userID));
				mpEntity.addPart("uuid", new StringBody(App.uuid));
				mpEntity.addPart("type", new StringBody("fk"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpUtil http = new HttpUtil();
			result = http.postDataMethod(UrlEntry.PAY_VEHIVLEORDER_URL,
					mpEntity);

			data.putString("value", result);
			msg.setData(data);
			msg.what = 5;

			handler.sendMessage(msg);
		}
	};
	// 定金
	Runnable buyrunnable = new Runnable() {
		@Override
		public void run() {
			Message msg = new Message();
			Bundle data = new Bundle();
			String result = "";

			MultipartEntity mpEntity = new MultipartEntity();

			try {
				// mpEntity.addPart("test", new StringBody("1111"));
				mpEntity.addPart("id", new StringBody(id));
				mpEntity.addPart("type", new StringBody("dj"));
				mpEntity.addPart("uuid", new StringBody(App.uuid));
				mpEntity.addPart("e.orderStatus", new StringBody("2"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpUtil http = new HttpUtil();
			result = http.postDataMethod(UrlEntry.PAY_VEHIVLEORDER_URL,
					mpEntity);

			data.putString("value", result);
			msg.setData(data);
			msg.what = 5;

			handler.sendMessage(msg);
		}
	};

	private void showbankdialog() {
		mSingleChoiceID2 = 0;
		Builder builder = new Builder(this);
		builder.setTitle("请选择支付方式");
		builder.setSingleChoiceItems(R.array.bank_names, mSingleChoiceID2,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		DialogInterface.OnClickListener btnListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int which) {
				MyRunnable2 runnable0 = new MyRunnable2("5");
				new Thread(runnable0).start();

				String[] arr = getResources().getStringArray(R.array.bank_urls);
				Uri uri = Uri.parse(arr[mSingleChoiceID2]);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		};
		builder.setPositiveButton("去支付", btnListener);
		builder.setNegativeButton("稍后", null);
		android.app.Dialog dialog = builder.create();
		dialog.show();

	}
}
