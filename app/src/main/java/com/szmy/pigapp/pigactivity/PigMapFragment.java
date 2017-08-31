package com.szmy.pigapp.pigactivity;

import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
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
import com.szmy.pigapp.activity.InfoBuyDetailActivity;
import com.szmy.pigapp.activity.InfoSellDetailActivity;
import com.szmy.pigapp.activity.LoginActivity;
import com.szmy.pigapp.activity.PayActivity;
import com.szmy.pigapp.activity.SlaughterhouseRenZhengActivity;
import com.szmy.pigapp.activity.ZhuChangRenZhengActivity;
import com.szmy.pigapp.agent.AgentRenZhengActivity;
import com.szmy.pigapp.entity.InfoEntry;
import com.szmy.pigapp.entity.MapAreaOverly;
import com.szmy.pigapp.tixian.ActivityWithdrawals;
import com.szmy.pigapp.tixian.Withdrawals;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.LoadingDialog;
import com.szmy.pigapp.widget.OrderNumDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PigMapFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private double x = 0, y = 0;
    private View mOverlyView;
    private PopupOverlyHolder mOverlyHolder;
    public static List<InfoEntry> childJson = new ArrayList<InfoEntry>();
    private InfoEntry mInfo;
    public static List<MapAreaOverly> areaList = new ArrayList<MapAreaOverly>();
    private MapAreaOverly mAreaOverly;
    private View mInfoWindowsView;
    private PopupInfoWindowsHolder mInfoWindowsHolder;
    private BitmapUtils bitmapUtils;
    private String price = "";
    private LinearLayout mLlFiltrate;
    private final int MUTI_CHOICE_DIALOG = 1;
    boolean[] selected = new boolean[]{true, true, true};
    private boolean mIsLoading = false;
    private View v;
    private Editor editor;
    private SharedPreferences sp;
    private String mProvince = "";// 省
    private String mCity = "";// 市
    private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private LoadingDialog dialog = null;
    private TextView mTvLocation;
    private float mMapZoom;// 地图缩放级别
    private MyMapZoom myZoom = MyMapZoom.BIG;// 大于7.6显示详情

    private RadioGroup radio;
    private String type = "";
    private String caretype = "0";
    private String minPrice = "";
    private String maxPrice = "";
    public static PigMapFragment newInstance(double x2, double y2, String province, String city) {

        PigMapFragment newFragment = new PigMapFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble("x", x2);
        bundle.putDouble("y", y2);
        bundle.putString("province", province);
        bundle.putString("city", city);
        newFragment.setArguments(bundle);
        return newFragment;
    }
//	public PigMapFragment(double x2, double y2, String province, String city) {
//		this.x = x2;
//		this.y = y2;
//		this.mProvince = province;
//		this.mCity = city;
//	}

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.x = args.getDouble("x");
            this.y = args.getDouble("y");
            this.mProvince = args.getString("province");
            this.mCity = args.getString("city");
        }
    }

    /*
     * 给Fragment 加载UI的布局，返回Fragment布局文件对应的东东
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_pig_map, container, false);
        ((RelativeLayout) v.findViewById(R.id.titlelayout))
                .setVisibility(View.GONE);
        sp = getActivity().getSharedPreferences(App.USERINFO, 0);
        editor = sp.edit();
        if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
            App.mUserInfo = FileUtil.readUser(getActivity());
            App.setDataInfo(App.mUserInfo);
        }
        initView();
        return v;
    }

	/*
     * 当用户离开此Fragment时调用
	 */

    @Override
    public void onPause() {
        mMapView.setVisibility(View.INVISIBLE);
        mMapView.onPause();
        super.onPause();
    }

    // 进入页面时
    @Override
    public void onResume() {
        mMapView.setVisibility(View.VISIBLE);
        mMapView.onResume();
        super.onResume();
    }

    public void initView() {
        if (x == 0 || TextUtils.isEmpty(mProvince)) {
            x = 34.806136;
            y = 113.699316;
            mProvince = "河南省";
            mCity = "郑州市";
        }
        // x = getActivity().getIntent().getExtras().getDouble("x");
        // y = getActivity().getIntent().getExtras().getDouble("y");
        mLlFiltrate = (LinearLayout) v.findViewById(R.id.ll_filtrate);
        mLlFiltrate.setOnClickListener(mClickListener);
        mMapView = (MapView) v.findViewById(R.id.bmapView);
        mTvLocation = (TextView) v.findViewById(R.id.tv_location);
        mBaiduMap = mMapView.getMap();
        mOverlyView = getActivity().getLayoutInflater().inflate(
                R.layout.popup_pig_map, null);
        mOverlyHolder = new PopupOverlyHolder(mOverlyView);
        mInfoWindowsView = getActivity().getLayoutInflater().inflate(
                R.layout.popup_map_infowindows, null);
        mInfoWindowsHolder = new PopupInfoWindowsHolder(mInfoWindowsView);
        bitmapUtils = new BitmapUtils(getActivity());
        if (x == 0) {
            mMapZoom = 7.5f;
            MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(mMapZoom);
            mBaiduMap.setMapStatus(msu);
            mProvince = "";
            mCity = "";
            loadAreaData("全国");
        } else {
            final LatLng point = new LatLng(x, y);
            mMapZoom = 11.5f;
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point,
                    mMapZoom);
            mBaiduMap.setMapStatus(msu);
            loadData();
        }
        mBaiduMap.setOnMapStatusChangeListener(mMapStatusChangeListener);
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
            public boolean onMarkerClick(Marker marker) {
                if (myZoom == MyMapZoom.BIG)
                    return showOrderDetails(marker);
                else
                    return showAreaMaybe(marker);
            }
        });
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(mGetGeoCoderResultListener);
        radio = (RadioGroup) v.findViewById(R.id.radioGroup1);
        radio.setOnCheckedChangeListener(this);

    }

    private boolean showAreaMaybe(Marker marker) {
        mAreaOverly = (MapAreaOverly) marker.getExtraInfo().get("area");
        if (mAreaOverly == null)
            return true;
        if (myZoom == MyMapZoom.SMALL) {
            LatLng point = new LatLng(mAreaOverly.getX(), mAreaOverly.getY());
            mMapZoom = 8.5f;
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point,
                    mMapZoom);
            mBaiduMap.setMapStatus(msu);
            myZoom = MyMapZoom.MIDDLE;
            mProvince = mAreaOverly.getArea();
            loadAreaData(mProvince);
        } else {
            LatLng point = new LatLng(mAreaOverly.getX(), mAreaOverly.getY());
            mMapZoom = 10.0f;
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point,
                    mMapZoom);
            mBaiduMap.setMapStatus(msu);
            myZoom = MyMapZoom.BIG;
            mCity = mAreaOverly.getArea();
            loadData();
        }
        return true;
    }

    private boolean showOrderDetails(Marker marker) {
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
        caretype = mInfo.getIsFriend();
        setColorBtn(caretype);
        if (mInfo.getCompName().equals("")) {
            mInfoWindowsHolder.tvName.setText(mInfo.getUserName());
        } else {
            mInfoWindowsHolder.tvName.setText(mInfo.getCompName());
        }
        mInfoWindowsHolder.tvTitle.setText("【" + mInfo.getProvince() + "】 "
                + mInfo.getTitle());
        String pigType = mInfo.getPigType().toString();
        // String pigType ="";
        if (pigType.equals("1")) {
            pigType = "内三元";
        } else if (pigType.equals("2")) {
            pigType = "外三元";
        } else if (pigType.equals("3")) {
            pigType = "土杂猪";
        } else {
            pigType = "其它品种";
        }
        if (TextUtils.isEmpty(mInfo.getStartPrice())){
            minPrice = mInfo.getStartPrice();
            maxPrice = mInfo.getEndPrice();
        }
        if (mInfo.getPrice().equals("0")) {
            mInfoWindowsHolder.tvPrice.setText(R.string.xieshang);
        } else
            mInfoWindowsHolder.tvPrice.setText(mInfo.getPrice() + "元/公斤");
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
                bitmapUtils.display(mInfoWindowsHolder.ivCover, UrlEntry.ip
                        + mInfo.getCoverPicture().toString());// 下载并显示图片
            } catch (Exception ex) {
                mInfoWindowsHolder.ivCover
                        .setImageResource(R.drawable.default_title);
            }
        }
        final LatLng ll = marker.getPosition();
        // 为弹出的InfoWindow添加点击事件
        InfoWindow mInfoWindow = new InfoWindow(mInfoWindowsView, ll, -80);
        // 显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//		childJson.clear();
//		mBaiduMap.clear();
//		loadData();
//		initView();
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
                    showDialog1(MUTI_CHOICE_DIALOG);
                    break;
                case R.id.iv_gz:
                    if (isLogin(getActivity())) {
                        showToast("请先登录！");
                        return;
                    }
                    if (caretype.equals("1")) {

                        final Dialog dialog = new Dialog(getActivity(), "提示", "确定取消关注该发布者？");
                        dialog.addCancelButton("取消");
                        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                                // 取消关注
                                handlerMyCare("del",
                                        UrlEntry.DELETE_MYCARE_URL);
                            }
                        });
                        dialog.setOnCancelButtonClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                    } else {
                        final Dialog dialog = new Dialog(getActivity(), "提示", "确定关注该发布者？");
                        dialog.addCancelButton("取消");
                        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                                // 取消关注
                                handlerMyCare("add",
                                        UrlEntry.ADD_MYCARE_URL);
                            }
                        });
                        dialog.setOnCancelButtonClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                    }
                    break;
            }
        }
    };
    /**
     * 处理我的关注/取消或新增
     */
    private void handlerMyCare(final String type, String url) {
        showDialog("");
        RequestParams params = new RequestParams();
        params.addBodyParameter("friendid",String.valueOf(mInfo.getUserID()));
//        params.addBodyParameter("orderid", mInfo.getId());
        params.addBodyParameter("uuid", App.uuid);
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
                        Log.i("mycare", "responseInfo.result = "
                                + responseInfo.result);
                        JSONObject jsonresult;

                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {
                                if (type.equals("add")) {
                                    showToast("关注成功！");
                                    caretype = "1";
                                } else if (type.equals("del")) {
                                    showToast("取消关注成功!");
                                    caretype = "0";
                                }
                                setColorBtn(caretype);

                            } else if (jsonresult.get("success").toString()
                                    .equals("4")) {
                                showToast("操作失败，该用户已不存在！");
                            }else if (jsonresult.get("success").toString()
                                    .equals("0")) {
                                showToast("操作失败，请重新登录后继续操作！");
                            } else {
                                if (type.equals("add")) {

                                    showToast("添加关注失败！");
                                } else if (type.equals("del")) {
                                    showToast("取消关注失败！");
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        disDialog();
                    }
                });

    }
    private void setColorBtn(String caretype) {
        if (!caretype.equals("1")) {

            mInfoWindowsHolder.ivGz.setImageResource(R.drawable.ic_mapwgz);
        } else {
            mInfoWindowsHolder.ivGz.setImageResource(R.drawable.ic_mapygz);
        }
    }
    protected void showDialog1(int id) {
        android.app.Dialog dialog = null;
        switch (id) {
            case MUTI_CHOICE_DIALOG:
                Builder builder = new Builder(getActivity());
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
    }

    // 下单
    private void buy() {
        mBaiduMap.hideInfoWindow();
        if (isLogin(getActivity())) {
            showToast("请先登录！");
        } else {
            if (mInfo.getUserID().equals(App.userID)) {
                showToast("无法对自己的订单下单");
                return;
            }
            if (!mInfo.getOrderType().equals("1")) {
                if (App.usertype.equals("1") || App.usertype.equals("3")) {
//                    final EditText et = new EditText(getActivity());
//                    et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
//                    new AlertDialog.Builder(getActivity())
//                            .setTitle("请输入最终成交金额")
//                            .setIcon(android.R.drawable.ic_dialog_info)
//                            .setView(et)
//                            .setPositiveButton("确定",
//                                    new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(
//                                                DialogInterface arg0, int arg1) {
//                                            price = et.getText().toString()
//                                                    .trim();
//                                            if (TextUtils.isEmpty(price)) {
//                                                showToast("请输入有效金额！");
//                                                return;
//                                            }
//                                            // orderdialog(
//                                            // "买卖双方需要支付2000元的保障金,是否确认订单？(提示：目前仅支持农业银行卡！)",
//                                            // mInfo.getOrderType());
//                                            orderdialog("确认订单？",
//                                                    mInfo.getOrderType());
//                                        }
//                                    }).setNegativeButton("取消", null).show();
                        getIsSmrz("2");
                } else {
                    showDialog1(getActivity(), "下单失败，只有猪场和经纪人才能对收购的订单进行操作。");
                    return;
                }
            } else {
                // orderdialog("买卖双方需要支付2000元的保障金,是否确定下单？(提示：暂仅支持农业银行卡支付)",
                // mInfo.getOrderType());
                if (App.usertype.equals("2") || App.usertype.equals("3")) {
//                    orderdialog("确认下单？", mInfo.getOrderType());
                    getIsSmrz("1");
                } else {
                    showDialog1(getActivity(), "下单失败，只有屠宰场和经纪人才能对出售的订单进行操作。");
                    return;
                }
            }
        }
    }
    private void getIsSmrz(final String type) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.IS_USERSMRX_URL,
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


                        Log.i("smrz", " smrz responseInfo.result = "
                                + responseInfo.result);
                        JSONObject jsonresult = null;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.optString("success").toString()
                                    .equals("1")) {
                                    if (type.equals("2")) {
                                        sendDataByUUid("xiadan");
                                    }else{
                                        //弹出确认下单
                                        final Dialog dialog = new Dialog(getActivity(), "提示", "确定下单？");
                                        dialog.addCancelButton("取消");
                                        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                dialog.cancel();
                                                if(type.equals("1")){
                                                    new Thread(sellrunnable).start();// 出售
                                                }else{
                                                    if (App.CITY.equals("鹤壁市")){
                                                        xiadanorder("");
                                                    }else{
                                                        if(Integer.parseInt(mInfo.getNumber())<100){
                                                            Toast.makeText(getActivity(),"下单失败，交易头数不能少于100头！",Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            xiadanorder("100");
                                                        }
                                                    }
                                                }


                                            }
                                        });
                                        dialog.setOnCancelButtonClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();
                                    }





                            } else if(jsonresult.optString("success").toString()
                                    .equals("6")){
                                final Dialog dialog = new Dialog(getActivity(), "提示",jsonresult.optString("msg").toString() );
                                dialog.addCancelButton("取消");
                                dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        if (App.usertype.equals("1")) { // 猪场
                                            Intent zcintent = new Intent(
                                                    getActivity(),
                                                    ZhuChangRenZhengActivity.class);
                                            startActivity(zcintent);
                                        } else if (App.usertype.equals("2")) {// 屠宰场
                                            Intent zcintent = new Intent(
                                                    getActivity(),
                                                    SlaughterhouseRenZhengActivity.class);
                                            startActivity(zcintent);
                                        } else if (App.usertype.equals("3")) {// 经纪人
                                            Intent zcintent = new Intent(
                                                    getActivity(),
                                                    AgentRenZhengActivity.class);
                                            startActivity(zcintent);
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
                            }else if(jsonresult.optString("success").toString()
                                    .equals("5")){
                                showDialog1(getActivity(),jsonresult.optString("msg").toString());
                            }else if(jsonresult.optString("success").toString().equals("0"))
                            {
                               showDialog1(getActivity(),"用户信息错误，请重新登录");
                            }else{
                                showToast("操作失败，请稍后再试！");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        disDialog();
                    }
                });
    }
    String totlprice = "";
    String billnum = "";
    private void xiadanorder(String numcon){

            OrderNumDialog dialog = new OrderNumDialog(
                    getActivity(), "提示",
                    new OrderNumDialog.OnDialogListener() {

                        @Override
                        public void back(String price1,
                                         String totleprice, String num) {
                            price = price1;
                            totlprice = totleprice;
                            billnum = num;
                            new Thread(buyrunnable).start(); // 收购

                        }
                    }, mInfo.getNumber().toString(),numcon,minPrice,maxPrice);
            dialog.show();

    }
    private void info() {
        FileUtil.deleteDirfile(FileUtil.SDPATH);
        if (!mInfo.getOrderType().equals("2")) {
            Intent intent = new Intent(getActivity(),
                    InfoSellDetailActivity.class);
            intent.putExtra("id", mInfo.getId());
            intent.putExtra("userid", mInfo.getUserID());
            intent.putExtra("type", mInfo.getOrderType());
            startActivityForResult(intent, 0);
        } else {
            Intent intent = new Intent(getActivity(),
                    InfoBuyDetailActivity.class);
            intent.putExtra("id", mInfo.getId());
            intent.putExtra("userid", mInfo.getUserID());
            intent.putExtra("type", mInfo.getOrderType());
            startActivityForResult(intent, 0);
        }
    }

    public boolean isLogin(Context context) {
        boolean falg = false;
        SharedPreferences sp = context.getSharedPreferences("USERINFO", 0);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.allorderbtn:
                type = "";
                loadData();
                break;
            case R.id.twobtn://待收购
                type = "2";
                loadData();
                break;
            case R.id.threebtn://待出售
                type = "1";
                loadData();
                break;
            case R.id.fourbtn://已成交
                type = "3";
                loadData();
                break;
        }
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
        public ImageView ivGz;

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
            ivGz = (ImageView) view.findViewById(R.id.iv_gz);
            ivGz.setOnClickListener(mClickListener);
        }
    }

    public void loadData() {
        if (!AppStaticUtil.isNetwork(getActivity().getApplicationContext())) {
            showToast("无网络");
            return;
        }
        mIsLoading = true;
        showDialog(" 正在加载" + mProvince + mCity + " 信息");
        mBaiduMap.clear();
        childJson.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("e.province", mProvince);
        params.addBodyParameter("e.city", mCity);
        params.addBodyParameter("type", type);
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
                        disDialog();
                        mTvLocation.setText(mProvince + mCity);
                        Log.i("all", responseInfo.result);
                        getDate(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        mIsLoading = false;
                        disDialog();
                        showToast("网络连接失败，请稍后再试！");
                    }
                });
    }

    public void loadAreaData(final String area) {
        if (!AppStaticUtil.isNetwork(getActivity().getApplicationContext())) {
            showToast("无网络");
            return;
        }
        mIsLoading = true;
        showDialog(" 正在加载" + area + " 信息");
        mBaiduMap.clear();
        areaList.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("area", area);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_MAP_AREA, params,
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
                        disDialog();
                        mTvLocation.setText(area);
                        Log.i("all", responseInfo.result);
                        parseMapArea(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        mIsLoading = false;
                        disDialog();
                        showToast("网络连接失败，请稍后再试！");
                    }
                });
    }

    private void parseMapArea(String result) {
        if (TextUtils.isEmpty(result)) {
            showToast("网络连接失败，请稍后再试！");
            return;
        }
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                MapAreaOverly overly = gson.fromJson(o.toString(),
                        MapAreaOverly.class);
                addAreaOverly(overly);
            }
        } catch (JSONException e) {
        }
    }

    private void addAreaOverly(MapAreaOverly overly) {
        if (overly.getX() == 0 || overly.getY() == 0)
            return;
        mOverlyHolder.tvNum.setBackgroundResource(R.drawable.bg_map_sell);
        mOverlyHolder.ivType.setImageResource(R.drawable.map_sell);
        LatLng latLng = new LatLng(overly.getX(), overly.getY());
        mOverlyHolder.tvNum
                .setText(overly.getArea() + overly.getZts() + "头");
        MarkerOptions overlayOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(getBitmapFromView(mOverlyView))).zIndex(5);
        overlayOptions.animateType(MarkerOptions.MarkerAnimateType.grow);
        Marker marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
        Bundle bundle = new Bundle();
        bundle.putSerializable("area", overly);
        marker.setExtraInfo(bundle);
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
        mOverlyHolder.tvNum.setText(info.getNumber() + "头");
        MarkerOptions overlayOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(getBitmapFromView(mOverlyView))).zIndex(5);
        overlayOptions.animateType(MarkerOptions.MarkerAnimateType.grow);
        Marker marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", info);
        marker.setExtraInfo(bundle);
    }

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
                if (!TextUtils.isEmpty(mProvince)) {
                    mProvince = "";
                    mCity = "";
                    loadAreaData("全国");
                }
                return;
            }
            if (mMapZoom < 9.6) {
                if (!TextUtils.isEmpty(mProvince) && !TextUtils.isEmpty(mCity)) {
                    mProvince = "";
                    mCity = "";
                }
                myZoom = MyMapZoom.MIDDLE;
            } else
                myZoom = MyMapZoom.BIG;
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
            } else if (myZoom == MyMapZoom.MIDDLE) {
                if (privince.equals(mProvince)) {
                    return;
                }
                mProvince = privince;
                loadAreaData(mProvince);
            }
        }
    };

    public void orderdialog(String message, final String type) {
        final Dialog dialog = new Dialog(getActivity(), "提示", message);
        dialog.addCancelButton("取消");
        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
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

    }

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
                mpEntity.addPart("e.finalPrice", new StringBody(
                        totlprice));
                mpEntity.addPart("e.number", new StringBody(billnum));
                mpEntity.addPart("e.price", new StringBody(price));
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
                                    showToast("下单失败：该信息已被下单!");
                                } else {
                                    Intent intent = new Intent(getActivity(),
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
                                    Intent intent = new Intent(getActivity(),
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
                                    showToast("下单失败，该订单已被下单！");
                                } else {
                                    if (mInfo.getOrderType().equals("2")){
                                        showToast("下单成功，请在"+mInfo.getPayDeadline()+"分钟内付款。");
                                    }else
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
                                relogin(getActivity());
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

    public void showDialog(String msg) {
        dialog = new LoadingDialog(getActivity(), msg);
        dialog.setCancelable(true);
        dialog.show();
    }

    public void disDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void relogin(Context context) {
        editor.putString("userinfo", "");
        editor.commit();
        App.clearData();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showDialog1(Context context, String msg) {
//		AlertDialog.Builder builder = new Builder(context);
//		builder.setMessage(msg);
//		builder.setTitle("提示");
//		builder.setPositiveButton("确定", null);
//		builder.create().show();
        final Dialog dialog = new Dialog(context, "提示", msg);
        dialog.addCancelButton("取消");
        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });
        dialog.setOnCancelButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public enum MyMapZoom {
        SMALL, MIDDLE, BIG
    }
    //查询银行卡信息
    private void sendDataByUUid(final String type) {
        mInfoWindowsHolder.tvBuy.setClickable(false);
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
                        mInfoWindowsHolder.tvBuy.setClickable(true);
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
                                final Withdrawals mInfoEntry = gson.fromJson(
                                        jsonresult.toString(),
                                        Withdrawals.class);
                                {
                                    // 弹出银行卡信息
                                    final Dialog dialog = new Dialog(getActivity(), "提示", "请先确认收款账号：\n收款人姓名：" + mInfoEntry.getName() + "\n收款人卡号：" + mInfoEntry.getBankNum() + "\n请使用与实名认证“" + mInfoEntry.getName() + "”信息一致的银行卡进行收款");
                                    dialog.addCancelButton("修改");
                                    dialog.addAccepteButton("确认");
                                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            dialog.cancel();
                                            if (type.equals("xiadan")) {
                                                //弹出确认下单
                                                final Dialog dialog = new Dialog(getActivity(), "提示", "确定下单？");
                                                dialog.addCancelButton("取消");
                                                dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                        dialog.cancel();
                                                        if (App.CITY.equals("鹤壁市")) {
                                                            xiadanorder("");
                                                        } else {
                                                            if (Integer.parseInt(mInfo.getNumber()) < 100) {
                                                                showDialog1(getActivity(), "下单失败，交易头数不能少于100头！");
                                                            } else {
                                                                xiadanorder("100");
                                                            }
                                                        }

                                                    }
                                                });
                                                dialog.setOnCancelButtonClickListener(new OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                dialog.show();
                                            }


                                        }
                                    });
                                    dialog.setOnCancelButtonClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.cancel();
                                            Intent intent = new Intent(getActivity(), ActivityWithdrawals.class);
                                            intent.putExtra("type", "rz");
                                            startActivity(intent);
                                        }
                                    });
                                    dialog.show();
                                }



                            } else if (jsonresult.get("success").toString()
                                    .equals("5")) {
                                //没有银行卡信息
                                String msg = "为保证交易及时完成，请先绑定银行卡。";
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("type", "rz");
                                jumpActivity(getActivity(), ActivityWithdrawals.class, msg, map);
                            } else if (jsonresult.get("success").toString()
                                    .equals("0")) {
                                showToast("用户信息错误,请重新登录！");
                                relogin(getActivity());
                            } else if (jsonresult.get("success").toString()
                                    .equals("3")) {
                                showToast("下单失败！");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
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

    public void jumpActivity(final Context mContxt, final Class mToContxt,
                             String msg, final Map<String, String> map) {
        final Dialog dialog = new Dialog(mContxt, "提示", msg);
        dialog.addCancelButton("取消");
        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(mContxt, mToContxt);
                if (map != null) {
                    for (String key : map.keySet()) {
                        System.out.println("key= " + key + " and value= "
                                + map.get(key));
                        intent.putExtra(key, map.get(key));
                    }
                }
                startActivity(intent);
            }
        });
        dialog.setOnCancelButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }
}
