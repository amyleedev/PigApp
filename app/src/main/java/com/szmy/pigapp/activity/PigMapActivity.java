package com.szmy.pigapp.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
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
import com.szmy.pigapp.entity.InfoEntry;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PigMapActivity extends BaseActivity {
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private double x, y;
	private View mOverlyView;
	private PopupOverlyHolder mOverlyHolder;
	public static List<InfoEntry> childJson = new ArrayList<InfoEntry>();
	private InfoEntry mInfo;
	private View mInfoWindowsView;
	private PopupInfoWindowsHolder mInfoWindowsHolder;
	private BitmapUtils bitmapUtils;
	private String price = "";
	private LinearLayout mLlFiltrate;
	private final int MUTI_CHOICE_DIALOG = 1;
	boolean[] selected = new boolean[] { true, true, true };
	private boolean mIsLoading = false;

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_pig_map);
		SharedPreferences sp = getSharedPreferences(App.USERINFO, 0);
		if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
			App.mUserInfo = FileUtil.readUser(this);
			App.setDataInfo(App.mUserInfo);
		}
		initView();
		loadData();
	}

	private void initView() {
		x = getIntent().getExtras().getDouble("x");
		y = getIntent().getExtras().getDouble("y");
		mLlFiltrate = (LinearLayout) findViewById(R.id.ll_filtrate);
		mLlFiltrate.setOnClickListener(mClickListener);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mOverlyView = getLayoutInflater().inflate(R.layout.popup_pig_map, null);
		mOverlyHolder = new PopupOverlyHolder(mOverlyView);
		mInfoWindowsView = getLayoutInflater().inflate(
				R.layout.popup_map_infowindows, null);
		mInfoWindowsHolder = new PopupInfoWindowsHolder(mInfoWindowsView);
		bitmapUtils = new BitmapUtils(PigMapActivity.this);
		if (x == 0) {
			MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(10.0f);
			mBaiduMap.setMapStatus(msu);
		} else {
			LatLng point = new LatLng(x, y);
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point,
					12f);
			mBaiduMap.setMapStatus(msu);
			// // 构建Marker图标
			// BitmapDescriptor bitmap = BitmapDescriptorFactory
			// .fromResource(R.drawable.ic_location);
			// // 构建MarkerOption，用于在地图上添加Marker
			// OverlayOptions option = new MarkerOptions().position(point).icon(
			// bitmap);
			// // 在地图上添加Marker，并显示
			// mBaiduMap.addOverlay(option);
			// BitmapDescriptorFactory.fromBitmap(getBitmapFromView(mView))
		}
		// 对Marker的点击
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				mBaiduMap.hideInfoWindow();
			}
		});
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				mBaiduMap.hideInfoWindow();
				// 获得marker中的数据
				mInfo = (InfoEntry) marker.getExtraInfo().get("info");
				if (mInfo == null)
					return true;
				// 将marker所在的经纬度的信息转化成屏幕上的坐标
				if (mInfo.getOrderStatus().equals("1")) {
					mInfoWindowsHolder.tvBuy.setVisibility(View.VISIBLE);
					mInfoWindowsHolder.tvLine.setVisibility(View.VISIBLE);
					if (mInfo.getOrderType().equals("1")) {
						mInfoWindowsHolder.rlTop.setBackgroundColor(0xaaE60012);
						mInfoWindowsHolder.rlBuy.setBackgroundColor(0xaaE60012);
					} else {
						mInfoWindowsHolder.rlTop.setBackgroundColor(0xaa03911E);
						mInfoWindowsHolder.rlBuy.setBackgroundColor(0xaa03911E);
					}
				} else {
					mInfoWindowsHolder.rlTop.setBackgroundColor(0xaa26B5C7);
					mInfoWindowsHolder.rlBuy.setBackgroundColor(0xaa26B5C7);
					mInfoWindowsHolder.tvBuy.setVisibility(View.GONE);
					mInfoWindowsHolder.tvLine.setVisibility(View.GONE);
				}
				if (mInfo.getCompName().equals("")) {
					mInfoWindowsHolder.tvName.setText(mInfo.getUserName());
				} else {
					mInfoWindowsHolder.tvName.setText(mInfo.getCompName());
				}
				mInfoWindowsHolder.tvTitle.setText("【" + mInfo.getProvince()
						+ "】 " + mInfo.getTitle());
				String pigType = mInfo.getPigType().toString();
				// String pigType ="";
				if (pigType.equals("1")) {
					pigType = "内三元";
				} else if (pigType.equals("2")) {
					pigType = "外三元";
				} else if (pigType.equals("3")) {
					pigType = "土杂猪";
				}else if (pigType.equals("4")) {
					pigType = "肥猪";
				}else if (pigType.equals("5")) {
					pigType = "仔猪";
				}else if (pigType.equals("6")) {
					pigType = "种猪";
				} else {
					pigType = "其它品种";
				}
				if (mInfo.getPrice().equals("0")) {
					mInfoWindowsHolder.tvPrice.setText(R.string.xieshang);
				} else
					mInfoWindowsHolder.tvPrice.setText(mInfo.getPrice()
							+ "元/公斤");
				mInfoWindowsHolder.tvType.setText(pigType);
				mInfoWindowsHolder.tvNum.setText(mInfo.getNumber() + "头");
				mInfoWindowsHolder.tvTime.setText(mInfo.getCreateTime()
						.substring(5, 16));
				if (mInfo.getCoverPicture().toString().equals("")) {
					mInfoWindowsHolder.ivCover
							.setImageResource(R.drawable.default_title);
				} else {
					try {
						bitmapUtils
								.configDefaultLoadFailedImage(R.drawable.default_title);
						bitmapUtils.display(mInfoWindowsHolder.ivCover,
								UrlEntry.ip
										+ mInfo.getCoverPicture().toString());// 下载并显示图片
					} catch (Exception ex) {
						mInfoWindowsHolder.ivCover
								.setImageResource(R.drawable.default_title);
					}
				}
				final LatLng ll = marker.getPosition();
				// 为弹出的InfoWindow添加点击事件
				InfoWindow mInfoWindow = new InfoWindow(mInfoWindowsView, ll,
						-80);
				// 显示InfoWindow
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});

	}

	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.iv_close:
					mBaiduMap.hideInfoWindow();
					break;
				case R.id.info_buy:
					buy();
					break;

				case R.id.infoBtn:
					// 查看详情
					info();
					break;

				case R.id.ll_filtrate:
					showDialog(MUTI_CHOICE_DIALOG);
					break;

			}
		}
	};

	private void info() {
		clearTempFromPref();
		if (mInfo.getOrderType().equals("2")) {
			Intent intent = new Intent(PigMapActivity.this,
					InfoSellDetailActivity.class);
			intent.putExtra("id", mInfo.getId());
			intent.putExtra("userid", mInfo.getUserID());
			intent.putExtra("type", mInfo.getOrderType());
			startActivityForResult(intent, 0);
		} else {
			Intent intent = new Intent(PigMapActivity.this,
					InfoBuyDetailActivity.class);
			intent.putExtra("id", mInfo.getId());
			intent.putExtra("userid", mInfo.getUserID());
			intent.putExtra("type", mInfo.getOrderType());
			startActivityForResult(intent, 0);
		}

	}

	@Override
	protected android.app.Dialog onCreateDialog(int id) {
		android.app.Dialog dialog = null;
		switch (id) {
			case MUTI_CHOICE_DIALOG:
				Builder builder = new Builder(this);
				builder.setTitle("请选择要显示的信息");
				DialogInterface.OnMultiChoiceClickListener mutiListener = new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialogInterface, int which,
										boolean isChecked) {
						selected[which] = isChecked;
					}
				};
				builder.setMultiChoiceItems(R.array.map_filtrate, selected,
						mutiListener);
				DialogInterface.OnClickListener btnListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int which) {
						if (mIsLoading) {
							showToast("服务器正忙");
							return;
						}
						loadData();
					}
				};
				builder.setPositiveButton("确定", btnListener);
				dialog = builder.create();
				break;
		}
		return dialog;
	}

	// 下单
	private void buy() {
		mBaiduMap.hideInfoWindow();
		System.out.println(App.usertype);
		if (isLogin(getApplicationContext())) {
			showToast("请先登录！");

		} else {

			if (mInfo.getUserID().equals(App.userID)) {
				showToast("无法对自己的订单下单");
				return;
			}

			if (!mInfo.getOrderType().equals("1")) {
				if (App.usertype.equals("1") || App.usertype.equals("3")) {
					final EditText et = new EditText(PigMapActivity.this);
					et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
					new Builder(PigMapActivity.this)
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
											// orderdialog(
											// "买卖双方需要支付2000元的保障金,是否确认订单？(提示：目前仅支持农业银行卡！)",
											// mInfo.getOrderType());
											orderdialog("确认订单？",
													mInfo.getOrderType());
										}
									}).setNegativeButton("取消", null).show();
				} else {
					showDialog1(PigMapActivity.this,
							"下单失败，只有猪场和经纪人才能对收购的订单进行操作。");
					return;
				}

			} else {
				// orderdialog("买卖双方需要支付2000元的保障金,是否确定下单？(提示：暂仅支持农业银行卡支付)",
				// mInfo.getOrderType());
				if (App.usertype.equals("2") || App.usertype.equals("3")) {
					orderdialog("确认下单？", mInfo.getOrderType());
				} else {
					showDialog1(PigMapActivity.this,
							"下单失败，只有屠宰场和经纪人才能对出售的订单进行操作。");
					return;
				}

			}

		}

	}

	public boolean isLogin(Context context) {
		boolean falg = false;
		SharedPreferences sp = getSharedPreferences("USERINFO", MODE_PRIVATE);
		if (App.mUserInfo == null) {
			App.mUserInfo = FileUtil.readUser(context);
		}
		App.setDataInfo(App.mUserInfo);
		if (TextUtils.isEmpty(App.uuid)) {
			falg = true;
			Intent intent = new Intent(context, LoginActivity.class);
			startActivity(intent);
		} else {
			App.setDataInfo(App.mUserInfo);
		}
		return falg;
	}

	public static Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	private class PopupOverlyHolder {
		public TextView tvNum;
		public ImageView ivType;

		public PopupOverlyHolder(View view) {
			tvNum = (TextView) view.findViewById(R.id.tv_num);
			ivType = (ImageView) view.findViewById(R.id.iv_type);
		}
	}

	private class PopupInfoWindowsHolder {
		public RelativeLayout rlTop;
		public TextView tvName;
		public ImageView ivClose;
		public ImageView ivCover;
		public TextView tvTitle;
		public TextView tvType;
		public TextView tvPrice;
		public TextView tvNum;
		public TextView tvTime;
		public TextView tvBuy;
		public TextView tvInfo;
		public LinearLayout rlBuy;
		public TextView tvLine;

		public PopupInfoWindowsHolder(View view) {
			rlTop = (RelativeLayout) view.findViewById(R.id.rl_top);
			tvName = (TextView) view.findViewById(R.id.tv_name);
			ivClose = (ImageView) view.findViewById(R.id.iv_close);
			ivClose.setOnClickListener(mClickListener);
			ivCover = (ImageView) view.findViewById(R.id.iv_cover);
			tvTitle = (TextView) view.findViewById(R.id.tv_title);
			tvType = (TextView) view.findViewById(R.id.tv_type);
			tvPrice = (TextView) view.findViewById(R.id.tv_price);
			tvNum = (TextView) view.findViewById(R.id.tv_num);
			tvTime = (TextView) view.findViewById(R.id.tv_time);
			tvBuy = (TextView) view.findViewById(R.id.info_buy);
			tvInfo = (TextView) view.findViewById(R.id.infoBtn);
			rlBuy = (LinearLayout) view.findViewById(R.id.rl_buy);
			tvBuy.setOnClickListener(mClickListener);
			tvInfo.setOnClickListener(mClickListener);
			tvLine = (TextView) view.findViewById(R.id.tv_line);
		}
	}

	public void loadData() {
		showDialog();
		mIsLoading = true;
		mBaiduMap.clear();
		childJson.clear();
		RequestParams params = new RequestParams();
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,
				UrlEntry.QUERY_ALL_ORDER_AND12h_URL, params,
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
						mIsLoading = false;
						Log.i("all", responseInfo.result);
						disDialog();
						getDate(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						mIsLoading = false;
						showToast("网络连接失败，请稍后再试！");
						disDialog();
					}
				});
	}

	private void getDate(String result) {
		JSONObject jsonresult;
		try {
			jsonresult = new JSONObject(result);
			if (jsonresult.get("success").toString().equals("1")) {
				List<InfoEntry> childJsonItem = new ArrayList<InfoEntry>();
				JSONArray jArrData = jsonresult.getJSONArray("list");
				for (int i = 0; i < jArrData.length(); i++) {
					JSONObject jobj2 = jArrData.optJSONObject(i);
					GsonBuilder gsonb = new GsonBuilder();
					Gson gson = gsonb.create();
					InfoEntry mInfoEntry = gson.fromJson(jobj2.toString(),
							InfoEntry.class);
					childJsonItem.add(mInfoEntry);
					addOverly(mInfoEntry);
				}
				childJson.addAll(childJsonItem);
			} else {
				// successType(jsonresult.get("success").toString(), "发布失败！");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void addOverly(InfoEntry info) {
		if (info.getX() == 0 || info.getY() == 0)
			return;
		if (info.getOrderStatus().equals("1")) {
			if (info.getOrderType().equals("1")) {
				if (!selected[0])
					return;
				mOverlyHolder.tvNum
						.setBackgroundResource(R.drawable.bg_map_sell);
				mOverlyHolder.ivType.setImageResource(R.drawable.map_sell);
			} else {
				if (!selected[1])
					return;
				mOverlyHolder.tvNum
						.setBackgroundResource(R.drawable.bg_map_buy);
				mOverlyHolder.ivType.setImageResource(R.drawable.map_buy);
			}
		} else {
			if (!selected[2])
				return;
			mOverlyHolder.tvNum.setBackgroundResource(R.drawable.bg_map_finish);
			mOverlyHolder.ivType.setImageResource(R.drawable.map_finish);
		}
		LatLng latLng = new LatLng(info.getX(), info.getY());
		mOverlyHolder.tvNum.setText(info.getNumber());

		OverlayOptions overlayOptions = new MarkerOptions()
				.position(latLng)
				.icon(BitmapDescriptorFactory
						.fromBitmap(getBitmapFromView(mOverlyView))).zIndex(5);
		Marker marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
		Bundle bundle = new Bundle();
		bundle.putSerializable("info", info);
		marker.setExtraInfo(bundle);
	}

	public void orderdialog(String message, final String type) {
		final Dialog dialog = new Dialog(PigMapActivity.this, "提示", message);
		dialog.addCancelButton("取消");
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
				showDialog();
				switch (Integer.parseInt(type)) {
					case 2:// 下单
						new Thread(buyrunnable).start(); // 收购
						break;
					case 1:
						new Thread(sellrunnable).start();// 出售
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
//
//				showDialog();
//				switch (Integer.parseInt(type)) {
//					case 2:// 下单
//						new Thread(buyrunnable).start(); // 收购
//						break;
//					case 1:
//						new Thread(sellrunnable).start();// 出售
//						break;
//				}
//			}
//		});
//		builder.setNegativeButton("取消", null);
//		builder.create().show();
	}

	// // 收购
	// Runnable buyrunnable = new Runnable() {
	// @Override
	// public void run() {
	// Message msg = new Message();
	// Bundle data = new Bundle();
	// String result = "";
	//
	// MultipartEntity mpEntity = new MultipartEntity();
	//
	// try {
	// mpEntity.addPart("e.finalPrice", new StringBody(price));
	// mpEntity.addPart("e.id", new StringBody(mInfo.getId()));
	// mpEntity.addPart("type", new StringBody("DJ2SZ"));
	// mpEntity.addPart("uuid",
	// new StringBody(App.uuid));
	// mpEntity.addPart("e.orderStatus", new StringBody("2"));
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// HttpUtil http = new HttpUtil();
	// result = http.postDataMethod(UrlEntry.UPDATE_MYINFO_BYID_URL, mpEntity);
	// data.putString("value", result);
	// msg.setData(data);
	// msg.what = 5; // 下单
	//
	// handler.sendMessage(msg);
	// }
	// };
	// // 出售
	// Runnable sellrunnable = new Runnable() {
	// @Override
	// public void run() {
	// Message msg = new Message();
	// Bundle data = new Bundle();
	// String result = "";
	//
	// MultipartEntity mpEntity = new MultipartEntity();
	//
	// try {
	// mpEntity.addPart("e.id", new StringBody(mInfo.getId()));
	// mpEntity.addPart("type", new StringBody("DJ2SZ"));
	// mpEntity.addPart("uuid",
	// new StringBody(sp.getString("loginkey", "")));
	// mpEntity.addPart("e.orderStatus", new StringBody("2"));
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// HttpUtil http = new HttpUtil();
	// result = http.postDataMethod(UrlEntry.GETSTRING_URL, mpEntity);
	// ;
	// data.putString("value", result);
	// msg.setData(data);
	// msg.what = 5; // 下单
	//
	// handler.sendMessage(msg);
	// }
	// };
	// 出售
	Runnable sellrunnable = new Runnable() {
		@Override
		public void run() {
			Message msg = new Message();
			Bundle data = new Bundle();
			String result = "";
			MultipartEntity mpEntity = new MultipartEntity();
			try {
				mpEntity.addPart("e.orderStatus", new StringBody("2"));
				mpEntity.addPart("e.id", new StringBody(mInfo.getId()));
				mpEntity.addPart("uuid", new StringBody(App.uuid));
			} catch (UnsupportedEncodingException e) {
			}
			HttpUtil http = new HttpUtil();
			result = http.postDataMethod(UrlEntry.UPDATE_MYINFO_BYID_URL,
					mpEntity);
			data.putString("value", result);
			msg.setData(data);
			msg.what = 2; // 下单
			handler.sendMessage(msg);
		}
	};
	// 收购
	Runnable buyrunnable = new Runnable() {
		@Override
		public void run() {
			Message msg = new Message();
			Bundle data = new Bundle();
			String result = "";
			MultipartEntity mpEntity = new MultipartEntity();
			try {
				mpEntity.addPart("e.finalPrice", new StringBody(price));
				mpEntity.addPart("e.id", new StringBody(mInfo.getId()));
				mpEntity.addPart("uuid", new StringBody(App.uuid));
				mpEntity.addPart("e.orderStatus", new StringBody("2"));
			} catch (UnsupportedEncodingException e) {
			}
			HttpUtil http = new HttpUtil();
			result = http.postDataMethod(UrlEntry.UPDATE_MYINFO_BYID_URL,
					mpEntity);
			data.putString("value", result);
			msg.setData(data);
			msg.what = 2; // 下单
			handler.sendMessage(msg);
		}
	};
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			disDialog();
			Bundle data1 = msg.getData();
			String result = data1.getString("value");
			JSONObject jsonresult;
			switch (msg.what) {
				case 5:
					System.out.println(result + "getString");
					if (!result.equals("")) {
						JSONObject json;
						try {
							json = new JSONObject(result);
							if (json.optString("success").equals("1")) {
								if (json.get("MSG").toString().equals("1")) {
									showDialog1(PigMapActivity.this,"下单失败：该信息已被下单!");
								} else {
									Intent intent = new Intent(PigMapActivity.this,
											PayActivity.class);
									intent.putExtra("url", UrlEntry.PAY_URL
											+ "?msg=" + json.get("MSG").toString());
									startActivityForResult(intent, 11);
								}
							} else if (json.optString("success").equals("0")) {
								// successType(json.optString("success"), "");
								if (json.optString("success").equals("0")) {
									showToast("用户信息错误,请重新登录！");
									editor.putString("userinfo", "");
									editor.commit();
									App.uuid = "";
									Intent intent = new Intent(
											getApplicationContext(),
											LoginActivity.class);
									startActivityForResult(intent, 22);
								} else if (json.optString("success").equals("3")) {
									// showToast("y");
								}
							}
						} catch (JSONException e) {
						}
					}
					break;
				case 2:
					if (!result.equals("")) {
						try {
							jsonresult = new JSONObject(result);
							if (jsonresult.get("success").toString().equals("1")) {
								if (jsonresult.optString("MSG").equals("1")) {
									showDialog1(PigMapActivity.this,"下单失败：该订单已被下单!");
								} else {
									showToast("下单成功！");
								}
								childJson.clear();
								mBaiduMap.clear();
								System.out.println("刷新");
								loadData();
								initView();
							} else if (jsonresult.get("success").toString()
									.equals("0")) {
								showToast("用户信息错误,请重新登录！");
								relogin(getApplicationContext());
							} else if (jsonresult.get("success").toString()
									.equals("3")) {
								showToast("下单失败！");
							}
						} catch (JSONException e) {
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

		childJson.clear();
		mBaiduMap.clear();
		loadData();
		initView();
	}

	// public void onClick(View view) {
	//
	// switch (view.getId()) {
	// case R.id.seachbsbtn:
	// Intent intent = new Intent(PigMapActivity.this,
	// ChoseDealTypeActivity.class);
	// startActivityForResult(intent, 24);
	// break;
	// }
	//
	// }
}
