package com.szmy.pigapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.szmy.pigapp.R;

public class LocationMapActivity extends BaseActivity implements
		OnGetGeoCoderResultListener {
	private LinearLayout mLlCommit;
	private LinearLayout mLlReset;
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private double x, y, tempX, tempY;
	private GeoCoder mSearch; // 搜索模块，也可去掉地图模块独立使用
	private String city;
	// 城市名称
	private String district;
	// 区县名称
	private String province;
	// 省份名称
	private String street;
	// 街道名称
	private String streetNumber;
	// 门牌号码
	private InfoWindow mInfoWindow;

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_location_map);
		initView();
	}

	private void initView() {
		x = getIntent().getExtras().getDouble("x");
		y = getIntent().getExtras().getDouble("y");
		if (x == 0 || y == 0) {
			x = tempX = 34.806136;
			y = tempY = 113.699316;
			showToast("定位失败");
		} else {
			tempX = x;
			tempY = y;
		}
		mLlCommit = (LinearLayout) findViewById(R.id.ll_commit);
		mLlCommit.setOnClickListener(mClickListener);
//		if(App.usertype.equals("3")){//如果是经纪人 可手动选择地址
//			System.out.println(App.usertype+"type");
//			mLlCommit.setVisibility(View.VISIBLE);
//		}
		mLlReset = (LinearLayout) findViewById(R.id.ll_reset);
		mLlReset.setOnClickListener(mClickListener);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		LatLng point = new LatLng(x, y);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point, 14f);
		mBaiduMap.setMapStatus(msu);
		// 定义Maker坐标点
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.ic_location);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap).draggable(true);
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option);
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		// 对Marker的点击
//		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
//
//			@Override
//			public boolean onMapPoiClick(MapPoi arg0) {
//				return false;
//			}
//
//			@Override
//			public void onMapClick(LatLng arg0) {
//				mBaiduMap.hideInfoWindow();
//			}
//		});
//		 mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
//		 public void onMarkerDrag(Marker marker) {
//		 }
//		
//		 public void onMarkerDragEnd(Marker marker) {
//		 x = marker.getPosition().latitude;
//		 y = marker.getPosition().longitude;
//		 LatLng ptCenter = new LatLng(x, y);
//		 // 反Geo搜索
//		 mSearch.reverseGeoCode(new ReverseGeoCodeOption()
//		 .location(ptCenter));
//		 }
//		
//		 public void onMarkerDragStart(Marker marker) {
//		 }
//		 });
//		 mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
//		 public boolean onMarkerClick(final Marker marker) {
//		 Button button = new Button(getApplicationContext());
//		 button.setBackgroundResource(R.drawable.popup);
//		 OnInfoWindowClickListener listener = null;
//		 button.setText("拖动图标改变位置");
//		 button.setTextColor(0xff232323);
//		 listener = new OnInfoWindowClickListener() {
//		 public void onInfoWindowClick() {
//		 // LatLng ll = marker.getPosition();
//		 // LatLng llNew = new LatLng(ll.latitude + 0.005,
//		 // ll.longitude + 0.005);
//		 // marker.setPosition(llNew);
//		 mBaiduMap.hideInfoWindow();
//		 }
//		 };
//		 LatLng ll = marker.getPosition();
//		 mInfoWindow = new InfoWindow(BitmapDescriptorFactory
//		 .fromView(button), ll, -47, listener);
//		 mBaiduMap.showInfoWindow(mInfoWindow);
//		 return true;
//		 }
//		 });
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_commit:
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("province", province);
				bundle.putString("city", city);
				bundle.putString("area", district);
				bundle.putString("address", street + streetNumber);
				bundle.putDouble("x", x);
				bundle.putDouble("y", y);
				intent.putExtras(bundle);
				setResult(ZhuChangRenZhengActivity.LOCATION_ID, intent);
				finish();
				break;
			case R.id.ll_reset:
				LatLng ptCenter = new LatLng(tempX, tempY);
				// 反Geo搜索
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(ptCenter));
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(LocationMapActivity.this, "抱歉，未能找到结果",
					Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_location)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(LocationMapActivity.this, strInfo, Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(LocationMapActivity.this, "抱歉，未能找到结果",
					Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions()
				.position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_location)).draggable(true));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		Toast.makeText(LocationMapActivity.this, result.getAddress(),
				Toast.LENGTH_LONG).show();
		province = result.getAddressDetail().province;
		city = result.getAddressDetail().city;
		district = result.getAddressDetail().district;
		street = result.getAddressDetail().street;
		streetNumber = result.getAddressDetail().streetNumber;
	}
}
