package com.szmy.pigapp.distributor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.activity.SlaughterMoreActivity;
import com.szmy.pigapp.entity.MapAreaOverly;
import com.szmy.pigapp.pigactivity.PigMapFragment.MyMapZoom;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DistributorMapActivity extends BaseActivity {
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private double x, y;
	private View mOverlyView;
	private PopupOverlyHolder mOverlyHolder;
	private List<Distributor> childJson = new ArrayList<Distributor>(); // 子列表
	private List<MapAreaOverly> childJson2 = new ArrayList<MapAreaOverly>();
	private Distributor entity;
	private MapAreaOverly entity2;
	private View mInfoWindowsView;
	private PopupInfoWindowsHolder mInfoWindowsHolder;
	private float mMapZoom;// 地图缩放级别
	private MyMapZoom myZoom = MyMapZoom.BIG;
	private String mProvince = "";// 省
	private String mCity = "";// 市
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private TextView tv_location;

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_logistics_map);
		((TextView) findViewById(R.id.def_head_tv)).setText("分销商分布图");
		SharedPreferences sp = getSharedPreferences(App.USERINFO, 0);
		initView();
		loadData();
	}

	private void initView() {
		Intent intent = getIntent();
		x = intent.getDoubleExtra("x", 0);
		y = intent.getDoubleExtra("y", 0);
		mProvince = intent.getStringExtra("province");
		mCity = intent.getStringExtra("city");
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		tv_location=(TextView) findViewById(R.id.tv_location);
		tv_location.setText(mProvince+"   "+mCity);
		mOverlyView = getLayoutInflater().inflate(R.layout.popup_pig_map, null);
		mOverlyHolder = new PopupOverlyHolder(mOverlyView);
		mInfoWindowsView = getLayoutInflater().inflate(
				R.layout.infowindows_pigfarm_map, null);
		mInfoWindowsHolder = new PopupInfoWindowsHolder(mInfoWindowsView);
		if (x == 0) {
			MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(10.0f);
			mBaiduMap.setMapStatus(msu);
		} else {
			LatLng point = new LatLng(x, y);
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point,
					12f);
			mBaiduMap.setMapStatus(msu);

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
				if (myZoom == MyMapZoom.BIG) {
					// 获得marker中的数据
					flag = 3;
					entity = (Distributor) marker.getExtraInfo().get("info");
					
					if (entity == null)
						return true;
					// 将marker所在的经纬度的信息转化成屏幕上的坐标
					if (entity.getUsername().equals("")) {
						mInfoWindowsHolder.tvName.setText(entity.getUsername());
					} else {
						if (entity.getUsername().length()>8) {
							mInfoWindowsHolder.tvName.setText(entity.getUsername()+"       ");
						} else {
							mInfoWindowsHolder.tvName.setText(entity.getUsername());
						}
						
					}
					
					mInfoWindowsHolder.tvCount.setText("");
					mInfoWindowsHolder.tvAddress.setText("【"
							+ entity.getProvince() + "】");
					mInfoWindowsHolder.tvType.setText(entity.getFxsType().equals("1")?"经销商":"个人");
					mInfoWindowsHolder.tvPrice.setText("");

						mInfoWindowsHolder.ivCover
								.setImageResource(R.drawable.head_default);

					final LatLng ll = marker.getPosition();
					// 为弹出的InfoWindow添加点击事件
					InfoWindow mInfoWindow = new InfoWindow(mInfoWindowsView,
							ll, -80);
					// 显示InfoWindow
					mBaiduMap.showInfoWindow(mInfoWindow);

				} else {
					if (myZoom == MyMapZoom.MIDDLE) {
						if (mMapZoom < 9.6) {
						// 获得marker中的数据
						entity2 = (MapAreaOverly) marker.getExtraInfo().get(
								"info");
						if (entity2 == null)
							return true;
						x=entity2.getX();
						y=entity2.getY();
						LatLng point = new LatLng(x, y);
						MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point,
								10f);
						mBaiduMap.setMapStatus(msu);
						
						mCity = entity2.getArea();
						myZoom = MyMapZoom.BIG;
						loadData();
						flag = 3;
						tv_location.setText(mProvince+"   "+mCity);
					}
					} else {
						if (mMapZoom < 7.6) {
							// 获得marker中的数据
							entity2 = (MapAreaOverly) marker.getExtraInfo()
									.get("info");
							if (entity2 == null)
								return true;
							x=entity2.getX();
							y=entity2.getY();
							LatLng point = new LatLng(x, y);
							MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point,
									8f);
							mBaiduMap.setMapStatus(msu);
							myZoom = MyMapZoom.MIDDLE;
							// mProvince=entity2.getArea();
							loadData2(entity2.getArea());
							tv_location.setText(mProvince);
							flag = 2;

						}
					}
					
					
				}
				return true;
			}
		});

		mBaiduMap.setOnMapStatusChangeListener(mMapStatusChangeListener);
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(mGetGeoCoderResultListener);
	}



	private OnGetGeoCoderResultListener mGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {

		@Override
		public void onGetGeoCodeResult(GeoCodeResult result) {

		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				showToast("抱歉，未能找到结果");
				return;
			}
			ReverseGeoCodeResult.AddressComponent address = result
					.getAddressDetail();
			String privince = address.province;
			String city = address.city;
			if (myZoom == MyMapZoom.BIG) {
				if (TextUtils.isEmpty(privince) || privince.contains("null")
						|| TextUtils.isEmpty(city) || city.contains("null")) {
					showToast("抱歉，未能找到结果");
					return;
				}
				if (privince.equals(mProvince) && city.equals(mCity)) {
					return;
				}
				mProvince = privince;
				if (!TextUtils.isEmpty(city) && mCity.contains(mProvince))
					mCity = address.district;
				else
					mCity = city;
				loadData();
				tv_location.setText(mProvince+"   "+mCity);
			} else {
				if (privince.equals(mProvince)) {
					return;
				}
				mProvince = privince;
				loadData2(mProvince);
				tv_location.setText(mProvince);
			}
		}
	};

	int flag = 3;
	private OnMapStatusChangeListener mMapStatusChangeListener = new OnMapStatusChangeListener() {
		LatLng startLng, finishLng;

		@Override
		public void onMapStatusChangeStart(MapStatus mapStatus) {
			startLng = mapStatus.target;
		}

		@Override
		public void onMapStatusChangeFinish(MapStatus mapStatus) {
			// showToast("级别" + mapStatus.zoom);

			mMapZoom = mapStatus.zoom;
			if (mMapZoom < 7.6) {
				myZoom = MyMapZoom.SMALL;
				// if (!TextUtils.isEmpty(mProvince)) {
				if (flag != 1) {
					loadData2("全国");
					flag = 1;
					tv_location.setText("全国");
					disDialog();
				}
				// }
				return;
			}
			if (mMapZoom < 9.6) {
				myZoom = MyMapZoom.MIDDLE;
				// if (!TextUtils.isEmpty(mProvince) &&
				// !TextUtils.isEmpty(mCity)) {
				if (flag != 2) {
					loadData2(mProvince);
					flag = 2;
					tv_location.setText(mProvince);
					disDialog();
				}
				// }

			} else {
				if (flag != 3) {
					myZoom = MyMapZoom.BIG;
					loadData();
					flag = 3;
					tv_location.setText(mProvince+"  "+mCity);
					disDialog();
				}
			}
			// 滑动搜索
			finishLng = mapStatus.target;
			// showToast("结束滑动");
			mSearch.reverseGeoCode(new ReverseGeoCodeOption()
					.location(finishLng));
		}

		@Override
		public void onMapStatusChange(MapStatus mapStatus) {
		}
	};

	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_close:
				mBaiduMap.hideInfoWindow();
				break;

			case R.id.infoBtn:
				// 查看详情
//				info();
				break;
			default:
				break;

			}
		}
	};

	private void info() {
		Intent intent = new Intent(DistributorMapActivity.this,
				SlaughterMoreActivity.class);
		
		intent.putExtra("entity", entity);
		startActivity(intent);
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
			tvNum.setVisibility(View.GONE);
			ivType = (ImageView) view.findViewById(R.id.iv_type);
		}
	}

	private class PopupInfoWindowsHolder {
		public TextView tvName;
		public ImageView ivClose;
		public ImageView ivCover;
		public TextView tvAddress;
		public TextView tvCount;
		public TextView tvPrice;
		public TextView tvType;
		public TextView tvBuy;
		public TextView tvInfo;
		public ImageView ivGz;

		public PopupInfoWindowsHolder(View view) {
			tvName = (TextView) view.findViewById(R.id.tv_name);
			ivClose = (ImageView) view.findViewById(R.id.iv_close);
			ivClose.setOnClickListener(mClickListener);
			ivCover = (ImageView) view.findViewById(R.id.iv_cover);
			tvCount = (TextView) view.findViewById(R.id.tv_count);
			tvAddress = (TextView) view.findViewById(R.id.tv_address);
			tvType = (TextView) view.findViewById(R.id.tv_type);
			tvPrice = (TextView) view.findViewById(R.id.tv_price);
			tvInfo = (TextView) view.findViewById(R.id.infoBtn);
			tvInfo.setVisibility(View.GONE);
			tvInfo.setOnClickListener(mClickListener);
			ivGz = (ImageView) view.findViewById(R.id.iv_gz);
			ivGz.setVisibility(View.GONE);
		}
	}

	public void loadData() {
		showDialog();
		mBaiduMap.clear();
		childJson.clear();

		RequestParams params = new RequestParams();
		params.addBodyParameter("area", mProvince);
		params.addBodyParameter("e.city", mCity);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,
				UrlEntry.GET_AREA_DISTRIBUTOR_URL, params,
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
						Log.i("insert", "responseInfo.result"
								+ responseInfo.result);
						disDialog();
						getDate(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("insert", "responseInfo.result" + msg);
						showToast("网络连接失败，请稍后再试！");
						disDialog();
					}
				});
	}

	private void getDate(String result) {
		JSONArray jsonresult;
		try {
			jsonresult = new JSONArray(result);
			for (int i = 0; i < jsonresult.length(); i++) {
				JSONObject jobj2 = jsonresult.optJSONObject(i);
				GsonBuilder gsonb = new GsonBuilder();
				Gson gson = gsonb.create();
				Distributor mInfoEntry = gson.fromJson(jobj2.toString(),
						Distributor.class);
				childJson.add(mInfoEntry);
				addOverly(mInfoEntry);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void addOverly(Distributor info) {
		double x2 = Double.parseDouble(info.getX());
		double y2 = Double.parseDouble(info.getY());
		if (x2 == 0 || y2 == 0) {
			return;
		}
		LatLng latLng = new LatLng(x2, y2);
		mOverlyHolder.tvNum.setText(info.getUsername());
		mOverlyHolder.tvNum.setVisibility(View.GONE);
		mOverlyHolder.tvNum.setBackgroundResource(R.drawable.bg_map_logistics);
		mOverlyHolder.ivType.setImageResource(R.drawable.ic_location);
		OverlayOptions overlayOptions = new MarkerOptions()
				.position(latLng)
				.icon(BitmapDescriptorFactory
						.fromBitmap(getBitmapFromView(mOverlyView))).zIndex(5);
		Marker marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
		Bundle bundle = new Bundle();
		bundle.putSerializable("info", info);
		marker.setExtraInfo(bundle);
	}

	public void loadData2(String Province) {
		showDialog();
		mBaiduMap.clear();
		childJson2.clear();
		mProvince = Province;

		RequestParams params = new RequestParams();
		params.addBodyParameter("area", mProvince);
		final HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,
				UrlEntry.QUERY_AreaMap_SLAUGHTER_URL, params,
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
						Log.i("insert", "responseInfo.result"
								+ responseInfo.result);
						disDialog();
						getDate2(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("insert", "responseInfo.result" + msg);
						showToast("网络连接失败，请稍后再试！");
						disDialog();
					}
				});
	}

	private void getDate2(String result) {
		JSONArray jsonresult;
		try {
			jsonresult = new JSONArray(result);
			for (int i = 0; i < jsonresult.length(); i++) {
				JSONObject jobj2 = jsonresult.optJSONObject(i);
				GsonBuilder gsonb = new GsonBuilder();
				Gson gson = gsonb.create();
				MapAreaOverly mInfoEntry = gson.fromJson(jobj2.toString(),
						MapAreaOverly.class);
				childJson2.add(mInfoEntry);
				addOverly2(mInfoEntry);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void addOverly2(MapAreaOverly info) {

		if (info.getX() == 0 || info.getY() == 0) {
			return;
		}
		LatLng latLng = new LatLng(info.getX(), info.getY());
		mOverlyHolder.tvNum.setVisibility(View.VISIBLE);
		mOverlyHolder.tvNum.setText(info.getArea() + "   " + info.getAmount()
				+ "条");
		mOverlyHolder.tvNum.setBackgroundResource(R.drawable.bg_map_logistics);
		mOverlyHolder.ivType.setImageResource(R.drawable.ic_location);
		OverlayOptions overlayOptions = new MarkerOptions()
				.position(latLng)
				.icon(BitmapDescriptorFactory
						.fromBitmap(getBitmapFromView(mOverlyView))).zIndex(5);
		Marker marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
		Bundle bundle = new Bundle();
		bundle.putSerializable("info", info);
		marker.setExtraInfo(bundle);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		childJson.clear();
		mBaiduMap.clear();
		loadData();
		initView();
	}
}
