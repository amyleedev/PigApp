package com.szmy.pigapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.service.LocationService;
import com.gc.materialdesign.utils.AdEntity;
import com.gc.materialdesign.views.ADTextView;
import com.gc.materialdesign.views.ButtonFloatSmall;
import com.gc.materialdesign.views.LinearLayoutRipple;
import com.gc.materialdesign.views.RippleView;
import com.gc.materialdesign.widgets.NoticeDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.ActivityMain;
import com.szmy.pigapp.activity.ActivityScanResult;
import com.szmy.pigapp.activity.AddressChoose;
import com.szmy.pigapp.activity.HealthAuthActivity;
import com.szmy.pigapp.activity.InfoBuyDetailActivity;
import com.szmy.pigapp.activity.InfoSellDetailActivity;
import com.szmy.pigapp.activity.IntegralActivity;
import com.szmy.pigapp.activity.LineChartActivity;
import com.szmy.pigapp.activity.NewsActivity;
import com.szmy.pigapp.activity.PigFarmListActivity;
import com.szmy.pigapp.activity.ScanActivity;
import com.szmy.pigapp.activity.ServiceCenterActivity;
import com.szmy.pigapp.activity.SlaughterListActivity2;
import com.szmy.pigapp.activity.SlaughterhouseRenZhengActivity;
import com.szmy.pigapp.activity.ZhuChangRenZhengActivity;
import com.szmy.pigapp.adapter.ImagePagerAdapter;
import com.szmy.pigapp.agent.AgentListActivity;
import com.szmy.pigapp.agent.AgentRenZhengActivity;
import com.szmy.pigapp.comment.NewsCommentActivity;
import com.szmy.pigapp.daijinquan.DaijinquanListActivity;
import com.szmy.pigapp.distributor.DistributorIntegralActivity;
import com.szmy.pigapp.distributor.DistributorMapActivity;
import com.szmy.pigapp.distributor.JoinDistributorActivity;
import com.szmy.pigapp.entity.Banner;
import com.szmy.pigapp.entity.NewsEntity;
import com.szmy.pigapp.mynotices.MyNotice;
import com.szmy.pigapp.myshop.ShopMainActivity;
import com.szmy.pigapp.natural.NaturalAuthActivity;
import com.szmy.pigapp.pds.PDSPigFarmListActivity;
import com.szmy.pigapp.pigactivity.PigOrMapActivity;
import com.szmy.pigapp.quotedprice.LookOrNewPriceActivity;
import com.szmy.pigapp.sign.PopupSign;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.vehicle.VehicleCompanyActivity;
import com.szmy.pigapp.vehicle.VehicleCompanyAuthActivity;
import com.szmy.pigapp.weather.WeatherActivity;
import com.szmy.pigapp.widget.DaijinquanPopuWindow;
import com.szmy.pigapp.widget.MyCircleFlowIndicator;
import com.szmy.pigapp.widget.MyMainScrollView;
import com.szmy.pigapp.widget.MyViewFlow;
import com.szmy.pigapp.zxing.activity.CaptureActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BaseFragment {
    private TextView mTvLogin;
    private FrameLayout mFlBanner;
    private MyMainScrollView mSv;
    private TextView mTvLocation;// 定位信息
    private TextView mTvInfo;
    private TextView mTvTemp, mTvWeather;
    private TextView mTvPrice01, mTvPrice02, mTvPrice03, mTvPrice04,
            mTvPrice05, mTvPrice06, mTvProvincePrice;// 今日猪价信息的6个价格
    private LinearLayout mLlPig1, mLlPig2, mLlPig3, mLlPig4, mLlPig5, mLlPig6,
            mLlPig7, mLlPig8;// 分别为养殖场、经纪人、物流、屠宰场、申报、溯源、资讯、服务中心
    private ButtonFloatSmall mBtnPig1, mBtnPig2, mBtnPig3, mBtnPig4, mBtnPig5, mBtnPig6, mBtnPig7, mBtnPig8;
    private int width = 0;
    private LayoutParams params;
    private List<Banner> bannerList = new ArrayList<Banner>();
    public LocationService locationService;
    public static double x = 0;
    public static double y = 0;
    public static String city="",areas = "";
    private LinearLayoutRipple mLlPigTrading;// 生猪交易
    private RippleView mLlPigMap;// 附近猪源
    private RippleView mLlNearbyPrice;// 附近猪价
    private RippleView mLlShop;// 牧易商城
    private RippleView mLlGroupon;// 团购中心
    private RippleView mLlIntegral;// 积分商城
    private View mVLine;// 分割线，如果当前位置无运营中心，则显示该线
    private LinearLayout mLlCenter;// 当地运营中心
    private TextView mTvCenter;// 当地运营中心名称
    private LinearLayout mLlVeterinaryDrug;// 兽药
    private LinearLayout mLlFeed;// 饲料
    private LinearLayout mLlDevice;// 器械设备
    private LinearLayout mLlBiologicalProduct;// 生物制品
    private LinearLayout mLlLivestock;// 畜禽
    private LinearLayout mLlService;// 服务产品
    public static final int LOCATION_ID = 0;
    public static String provinceContent = "", province = "";
    private String cityContent = "";
    private String quxianContent = "";
    private String mComId = ""; // 运营商id
    private TextView mTvSearch;
    private PopupSign mPopupSign;
    private boolean mIsSign;// 是否需要签到
    private String nearPro = "", nearCity = "";
    private LinearLayoutRipple mLlPrice01;
    private RippleView mLlPrice02;
    private RippleView mLlPrice03;
    private RippleView mLlPrice04;
    private RippleView mLlPrice05;
    private RippleView mLlPrice06;
    private View rootView;// 缓存Fragment view
    private LinearLayout mLlWeather;
    private String cityId = "";
    private TextView mBtnKf;
    private Button mBtnZyfbt;
    private SharedPreferences usersp;
    private static final int FXSTYPE = 0x1121;
    private String fxsshType = "";//分销商审核状态
    private RelativeLayout mRLZx;
    private ADTextView mADTextView;
    private TextView mBtnMyShop;
    private List<NewsEntity> childJson = new ArrayList<NewsEntity>(); // 子列表
    private int number =0;
    private boolean isRunning=false;
    private Thread newsThread = null;
    private boolean isFxsType = false;
    private MyViewFlow mViewFlow;
    private MyCircleFlowIndicator mFlowIndicator;
    private FrameLayout mFlBanner1;
    private String amount = "";
    private String toptip = "";
    private int mPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, container,
                    false);
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
        // return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
        showNoticeDialog();
        if (AppStaticUtil.isNetwork(getActivity().getApplicationContext())) {
            isRunning=false;
            loadBannerList();
            getDistributorType();
            getTodayNews();
            if (!TextUtils.isEmpty(App.uuid)){
                mPage = 1;
                mIsShow=false;
                loadNotices();
            }
            locationService = new LocationService(getActivity()
                    .getApplicationContext());
            locationService.registerListener(mListener);
        } else {
            mTvLocation.setText("定位失败");
        }
    }

    private void initView(View view) {
        usersp = getActivity().getSharedPreferences(App.USERINFO,
                0);
        if (!TextUtils.isEmpty(usersp.getString("userinfo", ""))) {
            App.mUserInfo = FileUtil.readUser(getActivity());
            App.setDataInfo(App.mUserInfo);
        }

        mTvLogin = (TextView) view.findViewById(R.id.tv_login);
        mADTextView = (ADTextView) view.findViewById(R.id.ad_textview);
//        mADTextView.setOnItemClickListener(new ADTextView.OnItemClickListener() {
//            @Override
//            public void onClick(String mUrl) {
//                startActivity(new Intent(getActivity(), NewsActivity.class));
////                Intent intent = new Intent(getActivity(),
////                        NewsDetailsActivity.class);
////
////                for(NewsEntity news:childJson){
////                    if(news.getId().equals(mUrl)){
////                        Bundle bundle = new Bundle();
////                        bundle.putSerializable("news", news);
////                        bundle.putString("url",UrlEntry.ip+"/mNews/newsInfo.html?id="+mUrl);
////                        intent.putExtras(bundle);
////                        startActivity(intent);
////                    }
////                }
//
//            }
//        });
        //代金券提示
//        if(getActivity().getIntent().hasExtra("amount")){
//            Log.i("amount",getActivity().getIntent().getStringExtra("amount"));
//            amount = getActivity().getIntent().getStringExtra("amount");
//            toptip = getActivity().getIntent().getStringExtra("toptip");
//            showPopDialog(view);
//        }
        mTvLogin.setOnClickListener(mClickListener);
        mSv = (MyMainScrollView) view.findViewById(R.id.sv);


//        mVf.setOnItemClickListener(mItemClickListener);
//        mSv.setView(mVf);
        mTvLocation = (TextView) view.findViewById(R.id.tv_location);
        mTvLocation.setMovementMethod(new ScrollingMovementMethod());
        mTvLocation.setOnClickListener(mClickListener);
        mTvInfo = (TextView) view.findViewById(R.id.tv_info);
        mTvTemp = (TextView) view.findViewById(R.id.tv_temp);
        mTvWeather = (TextView) view.findViewById(R.id.tv_weather);
        mLlPrice01 = (LinearLayoutRipple) view.findViewById(R.id.ll_price01);
        mLlPrice02 = (RippleView) view.findViewById(R.id.ll_price02);
        mLlPrice03 = (RippleView) view.findViewById(R.id.ll_price03);
        mLlPrice04 = (RippleView) view.findViewById(R.id.ll_price04);
        mLlPrice05 = (RippleView) view.findViewById(R.id.ll_price05);
        mLlPrice06 = (RippleView) view.findViewById(R.id.ll_price06);
        mLlPrice01.setOnClickListener(mClickListener);
        mLlPrice02.setOnClickListener(mClickListener);
        mLlPrice03.setOnClickListener(mClickListener);
        mLlPrice04.setOnClickListener(mClickListener);
        mLlPrice05.setOnClickListener(mClickListener);
        mLlPrice06.setOnClickListener(mClickListener);
        mTvPrice01 = (TextView) view.findViewById(R.id.tv_price01);
        mTvPrice02 = (TextView) view.findViewById(R.id.tv_price02);
        mTvPrice03 = (TextView) view.findViewById(R.id.tv_price03);
        mTvPrice04 = (TextView) view.findViewById(R.id.tv_price04);
        mTvPrice05 = (TextView) view.findViewById(R.id.tv_price05);
        mTvPrice06 = (TextView) view.findViewById(R.id.tv_price06);
        mTvProvincePrice = (TextView) view.findViewById(R.id.tv_province_price);
        mLlPig1 = (LinearLayout) view.findViewById(R.id.ll_pig01);
        mLlPig1.setOnClickListener(mClickListener);
        mLlPig2 = (LinearLayout) view.findViewById(R.id.ll_pig02);
        mLlPig2.setOnClickListener(mClickListener);
        mLlPig3 = (LinearLayout) view.findViewById(R.id.ll_pig03);
        mLlPig3.setOnClickListener(mClickListener);
        mLlPig4 = (LinearLayout) view.findViewById(R.id.ll_pig04);
        mLlPig4.setOnClickListener(mClickListener);
        mLlPig5 = (LinearLayout) view.findViewById(R.id.ll_pig05);
        mLlPig5.setOnClickListener(mClickListener);
        mLlPig6 = (LinearLayout) view.findViewById(R.id.ll_pig06);
        mLlPig6.setOnClickListener(mClickListener);
        mLlPig7 = (LinearLayout) view.findViewById(R.id.ll_pig07);
        mLlPig7.setOnClickListener(mClickListener);
        mLlPig8 = (LinearLayout) view.findViewById(R.id.ll_pig08);
        mLlPig8.setOnClickListener(mClickListener);
        mBtnPig1 = (ButtonFloatSmall) view.findViewById(R.id.btn_pig01);
        mBtnPig1.setOnClickListener(mClickListener);
        mBtnPig2 = (ButtonFloatSmall) view.findViewById(R.id.btn_pig02);
        mBtnPig2.setOnClickListener(mClickListener);
        mBtnPig3 = (ButtonFloatSmall) view.findViewById(R.id.btn_pig03);
        mBtnPig3.setOnClickListener(mClickListener);
        mBtnPig4 = (ButtonFloatSmall) view.findViewById(R.id.btn_pig04);
        mBtnPig4.setOnClickListener(mClickListener);
        mBtnPig5 = (ButtonFloatSmall) view.findViewById(R.id.btn_pig05);
        mBtnPig5.setOnClickListener(mClickListener);
        mBtnPig6 = (ButtonFloatSmall) view.findViewById(R.id.btn_pig06);
        mBtnPig6.setOnClickListener(mClickListener);
        mBtnPig7 = (ButtonFloatSmall) view.findViewById(R.id.btn_pig07);
        mBtnPig7.setOnClickListener(mClickListener);
        mBtnPig8 = (ButtonFloatSmall) view.findViewById(R.id.btn_pig08);
        mBtnPig8.setOnClickListener(mClickListener);
        width = ((ActivityMain) getActivity()).getSize();
        mLlPigTrading = (LinearLayoutRipple) view.findViewById(R.id.ll_pig_trading);
        mLlPigTrading.setOnClickListener(mClickListener);
        mLlPigMap = (RippleView) view.findViewById(R.id.ll_pig_map);
        mLlPigMap.setOnClickListener(mClickListener);
        mLlNearbyPrice = (RippleView) view.findViewById(R.id.ll_nearby_price);
        mLlNearbyPrice.setOnClickListener(mClickListener);
        mLlShop = (RippleView) view.findViewById(R.id.ll_shop);
        mLlShop.setOnClickListener(mClickListener);
        mLlGroupon = (RippleView) view.findViewById(R.id.ll_groupon);
        mLlGroupon.setOnClickListener(mClickListener);
        mLlIntegral = (RippleView) view.findViewById(R.id.ll_integral);
        mLlIntegral.setOnClickListener(mClickListener);
        mVLine = view.findViewById(R.id.v_line);
        mLlCenter = (LinearLayout) view.findViewById(R.id.ll_center);
        mLlCenter.setOnClickListener(mClickListener);
        mTvCenter = (TextView) view.findViewById(R.id.tv_center);
        mLlVeterinaryDrug = (LinearLayout) view
                .findViewById(R.id.ll_veterinary_drug);
        mLlVeterinaryDrug.setOnClickListener(mClickListener);
        mLlFeed = (LinearLayout) view.findViewById(R.id.ll_feed);
        mLlFeed.setOnClickListener(mClickListener);
        mLlDevice = (LinearLayout) view.findViewById(R.id.ll_device);
        mLlDevice.setOnClickListener(mClickListener);
        mLlBiologicalProduct = (LinearLayout) view
                .findViewById(R.id.ll_biological_product);
        mLlBiologicalProduct.setOnClickListener(mClickListener);
        mLlLivestock = (LinearLayout) view.findViewById(R.id.ll_livestock);
        mLlLivestock.setOnClickListener(mClickListener);
        mLlService = (LinearLayout) view.findViewById(R.id.ll_service);
        mLlService.setOnClickListener(mClickListener);
        mTvSearch = (TextView) view.findViewById(R.id.tv_search);
        mTvSearch.setOnClickListener(mClickListener);
        mLlWeather = (LinearLayout) view.findViewById(R.id.weatherLayout);
        mLlWeather.setOnClickListener(mClickListener);
        mBtnKf = (TextView) view.findViewById(R.id.kfdh);

        mBtnKf.setOnClickListener(mClickListener);
        mBtnZyfbt = (Button) view.findViewById(R.id.zyfbtBtn);
        mBtnMyShop = (TextView) view.findViewById(R.id.myshopBtn);
        mBtnZyfbt.setOnClickListener(mClickListener);

        if (!TextUtils.isEmpty(App.fxsType) && (App.fxsType.equals("1") || App.fxsType.equals("2"))) {
            mBtnMyShop.setText("我的店铺");
            fxsshType = "2";
        } else {
            mBtnMyShop.setText("我要开店");
            fxsshType = "";
        }
        mRLZx = (RelativeLayout) view.findViewById(R.id.rl_zx);
        mRLZx.setOnClickListener(mClickListener);
        mViewFlow = (MyViewFlow) view.findViewById(R.id.viewflow);
        mFlowIndicator = (MyCircleFlowIndicator)view.findViewById(R.id.viewflowindic);
        mFlBanner1 = (FrameLayout)view.findViewById(R.id.framelayout);
        mFlBanner1.setLayoutParams(new LayoutParams((int) (width), (int) (width / 4)));



    }

    private void loadWeather(String cityName) {
        RequestParams params = new RequestParams();
        params.addHeader("apikey", "52577b01d25733cd63fc303292312ce6");
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                "http://apis.baidu.com/apistore/weatherservice/cityname?cityname="
                        + cityName, params, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        // System.out.println("--" + responseInfo.result +
                        // "--");
                        parseWeather(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                    }
                });
    }

    private void parseWeather(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            int errNum = obj.optInt("errNum");
            if (errNum != 0) {
                mTvTemp.setText("");
                mTvWeather.setText("");
                return;
            }

            JSONObject o = obj.optJSONObject("retData");
            mTvTemp.setText(o.optString("temp") + "°");
            mTvWeather.setText(o.optString("weather") + " "
                    + o.optString("l_tmp") + "°/" + o.optString("h_tmp") + "°");
            cityId = o.optString("citycode");
        } catch (JSONException e) {
            mTvTemp.setText("");
            mTvWeather.setText("");
        }
    }

    private void loadBannerList() {
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_BANNER_LIST_URL,
                null, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        if (getActivity().getIntent().hasExtra("amount")){
//                            amount = getActivity().getIntent().getStringExtra("amount");
//                            toptip = getActivity().getIntent().getStringExtra("toptip");
//                            showPopDialog(mRLZx);
//                        }

                        try {
                            Log.i("banner",responseInfo.result);
                            parseBanner(responseInfo.result);
                        } catch (JSONException e) {
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                });
    }

    private void parseBanner(String data) throws JSONException {
        bannerList.clear();
        JSONObject obj = new JSONObject(data);
        String success = obj.optString("success");
        if (!TextUtils.isEmpty(success) && success.equals("1")) {
            JSONArray array = obj.optJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.optJSONObject(i);
                GsonBuilder gsonb = new GsonBuilder();
                Banner banner = gsonb.create().fromJson(o.toString(),
                        Banner.class);
                bannerList.add(banner);
            }
            if (bannerList.size() > 0) {
                mViewFlow.setAdapter(new ImagePagerAdapter(getActivity(),bannerList).setInfiniteLoop(true));
                mViewFlow.setmSideBuffer(bannerList.size()); // 实际图片张数，												// 我的ImageAdapter实际图片张数为3
                mViewFlow.setFlowIndicator(mFlowIndicator);
                mViewFlow.setTimeSpan(4000);
                mViewFlow.setSelection(bannerList.size() * 1000); // 设置初始位置
                mViewFlow.startAutoFlowTimer(); // 启动自动播放
//                mFlBanner1.setLayoutParams(new LayoutParams((int) (width), (int) (width / 4)));
                mFlBanner1.setVisibility(View.VISIBLE);
            }
        }
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location
                    && location.getLocType() != BDLocation.TypeServerError) {
                if (location.getLocType() == 62 || location.getLocType() == 162) {
                } else {
                    locationService.stop();
                    x = location.getLatitude();
                    y = location.getLongitude();
                    city = location.getCity();
                    provinceContent = location.getProvince();
                    cityContent = location.getCity();
                    quxianContent = location.getDistrict();
                    nearPro = location.getProvince();
                    nearCity = location.getCity();
                    if (TextUtils.isEmpty(provinceContent)
                            || provinceContent.contains("null")) {
                        mTvLocation.setText("定位失败");
                        return;
                    }
                    province = provinceContent;
                    areas = quxianContent;
                    String city = cityContent;
                    if (!TextUtils.isEmpty(cityContent)
                            && cityContent.contains(provinceContent)) {
                        mTvLocation.setText(provinceContent + quxianContent);
                        city = quxianContent;
                    } else {
                        if ((provinceContent + cityContent + quxianContent).length() > 10) {
                            mTvLocation.setText(provinceContent + "..." + quxianContent);
                        } else
                            mTvLocation.setText(provinceContent + cityContent
                                    + quxianContent);
                    }
                    if (provinceContent.contains("市")) {
                        city = provinceContent;
                    }
                    mTvInfo.setText("今日" + quxianContent + "猪贸通行情指数");
                    getNearCom();
                    getPigPrice();
                    loadWeather(city.replace("市", ""));
                }
            }
        }
    };

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.tv_login:
                    if (isLogin(getActivity())) {
                    } else if (mIsSign) {
                        mTvLogin.setEnabled(false);
                        addSign();
                    } else {
                        AppStaticUtil.toastWelcome(getActivity(),
                                getUsername(getActivity()));
                    }
                    break;
                case R.id.tv_search:
//					startActivity(new Intent(getActivity(), SearchPigActivity.class));
//                    if (isLogin(getActivity())) {
//                        showToast("请先登录！");
//                    } else {
//					mUmPopup.popupPublishMenu();

                        // 打开扫描界面扫描条形码或二维码
                        Intent openCameraIntent = new Intent(getActivity(),
                                CaptureActivity.class);
                        startActivityForResult(openCameraIntent, 0);

//                    }
                    break;
                case R.id.ll_pig01:
                case R.id.btn_pig01:
                    intent = new Intent(getActivity(), PigFarmListActivity.class);
                    intent.putExtra("x", x);
                    intent.putExtra("y", y);
                    intent.putExtra("province", provinceContent);
                    intent.putExtra("city", cityContent);
                    intent.putExtra("area", quxianContent);
                    startActivity(intent);
                    break;
                case R.id.ll_pig02:
                case R.id.btn_pig02:
                    intent = new Intent(getActivity(), AgentListActivity.class);
                    intent.putExtra("province", provinceContent);
                    intent.putExtra("city", cityContent);
                    intent.putExtra("area", quxianContent);
                    intent.putExtra("x", x);
                    intent.putExtra("y", y);
                    startActivity(intent);
                    break;
                case R.id.ll_pig03:
                case R.id.btn_pig03:
                    intent = new Intent(getActivity(), VehicleCompanyActivity.class);
                    intent.putExtra("x", x);
                    intent.putExtra("y", y);
                    intent.putExtra("province", provinceContent);
                    intent.putExtra("city", cityContent);
                    startActivity(intent);
                    // startActivity(new Intent(getActivity(),
                    // VehicleCompanyActivity.class));
                    break;
                case R.id.ll_pig04:
                case R.id.btn_pig04:
                    intent = new Intent(getActivity(), SlaughterListActivity2.class);
                    intent.putExtra("x", x);
                    intent.putExtra("y", y);
                    intent.putExtra("province", provinceContent);
                    intent.putExtra("city", cityContent);
                    intent.putExtra("area", quxianContent);
                    startActivity(intent);
                    break;
                case R.id.ll_pig05:
                case R.id.btn_pig05:
//                     startActivity(new Intent(getActivity(),
//                     DevelopingActivity.class));
                    if (isLogin(getActivity())) {
                        showToast("请先登录！");
                    } else {
                        startActivity(new Intent(getActivity(),
                                PDSPigFarmListActivity.class));
//                        //2017-5-17 修改为猪场建设
//                        intent = new Intent(getActivity(), NewsListActivity.class);
//                        intent.putExtra("type", "猪场建设");
//                        startActivity(intent);
                    }
                    break;
                case R.id.ll_pig06:
                case R.id.btn_pig06:
                    intent = new Intent(getActivity(), ScanActivity.class);
                    intent.putExtra("city", cityContent);
                    intent.putExtra("area", quxianContent);
                    startActivity(intent);
                    // startActivity(new Intent(getActivity(), ScanActivity.class));
                    //2017-5-17 修改为资讯
//                    startActivity(new Intent(getActivity(), NewsActivity.class));
                    break;
                case R.id.ll_pig07:
                case R.id.btn_pig07:
//					startActivity(new Intent(getActivity(), NewsActivity.class));

                    clearTempFromPref();
                    if (!AppStaticUtil.isNetwork(getActivity().getApplicationContext())) {
                        showToast("您的网络有问题请稍后再试！");
                        return;
                    }
                    if (isLogin(getActivity())) {
                        showToast("请先登录！");
                    } else {
                        if (fxsshType.equals("2")) {//已通过审核
                            intent = new Intent(getActivity(), DistributorIntegralActivity.class);
                            startActivity(intent);
                        } else {
                            if (isFxsType) {
                                if (fxsshType.equals("1")) {//审核中
                                intent = new Intent(getActivity(), JoinDistributorActivity.class);
                                startActivity(intent);
                                } else if (fxsshType.equals("0")){//未申请
                                    intent = new Intent(getActivity(),
                                            ActivityScanResult.class);
                                    intent.putExtra("url", UrlEntry.ip + "/activity/zmt.jsp?img=fxsInfo");
                                    intent.putExtra("fxs", "fxs");
                                    startActivity(intent);
                                }
                            }else{
                                showToast("正在查询状态，请稍后再点");
                            }
                        }

                    }
                    break;
                case R.id.ll_pig08:
                case R.id.btn_pig08:
                    startActivity(new Intent(getActivity(),
                            ServiceCenterActivity.class));
                    // startActivity(new
                    // Intent(getActivity(),DevelopingActivity.class));
                    break;
                case R.id.ll_pig_trading:
                    Intent intentpig = new Intent(getActivity(),
                            PigOrMapActivity.class);
                    intentpig.putExtra("type", 1);// 生猪信息
                    intentpig.putExtra("x", x);
                    intentpig.putExtra("y", y);
                    intentpig.putExtra("province", provinceContent);
                    intentpig.putExtra("city", cityContent);
                    startActivity(intentpig);
                    break;
                case R.id.ll_pig_map:
                    Intent intentmap = new Intent(getActivity(),
                            PigOrMapActivity.class);
                    intentmap.putExtra("type", 2);// 附近猪源信息
                    intentmap.putExtra("x", x);
                    intentmap.putExtra("y", y);
                    intentmap.putExtra("province", provinceContent);
                    intentmap.putExtra("city", cityContent);
                    startActivity(intentmap);
                    break;
                case R.id.ll_nearby_price:// 查看附近豬價信息
//                    intent = new Intent(getActivity(), PigPriceFragment.class);
//                    intent.putExtra("nearPro", nearPro);
//                    intent.putExtra("nearCity", nearCity);
//                    startActivity(intent);
                    intent = new Intent(getActivity(), LookOrNewPriceActivity.class);
                    intent.putExtra("province", provinceContent);
                    intent.putExtra("city", cityContent);
                    startActivity(intent);
                    break;
                case R.id.ll_groupon:
                    intent = new Intent(getActivity(), ActivityScanResult.class);
                    intent.putExtra("type", "groupbuy");
                    intent.putExtra(
                            "url",
                            UrlEntry.GROUP_BUYING_URL
                                    + (TextUtils
                                    .isEmpty(getUserUUid(getActivity())) ? "1"
                                    : getUserUUid(getActivity())));
                    startActivity(intent);
                    break;
                case R.id.ll_integral:
                    intent = new Intent(getActivity(), ActivityScanResult.class);
                    intent.putExtra("type", "integral");
                    intent.putExtra(
                            "url",
                            UrlEntry.INTEGRAL_SHOP_URL
                                    + (TextUtils
                                    .isEmpty(getUserUUid(getActivity())) ? "1"
                                    : getUserUUid(getActivity())));
                    startActivity(intent);
                    break;
                case R.id.ll_shop:
                    intent = new Intent(getActivity(), ShopMainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
//                    intent = new Intent(getActivity(), ActivityScanResult.class);
//                    intent.putExtra("type", "index");
//                    intent.putExtra(
//                            "url",
//                            UrlEntry.MOBILE_PHONE_URL
//                                    + (TextUtils
//                                    .isEmpty(getUserUUid(getActivity())) ? "1"
//                                    : getUserUUid(getActivity())));
//                    startActivity(intent);
                    break;
                case R.id.tv_location:// 定位
                    if (locationService != null) {
                        locationService.stop();
                    }
                    intent = new Intent(getActivity(), AddressChoose.class);
                    intent.putExtra("mainlocation", "mainlocation");
                    startActivityForResult(intent, LOCATION_ID);
                    break;
                case R.id.ll_center:
                    // intent = new Intent(getActivity(),
                    // SearchProductActivity.class);
                    // intent.putExtra("comId", mComId);
                    // startActivity(intent);
                    intent = new Intent(getActivity(), ActivityScanResult.class);
                    intent.putExtra("type", "near");
                    intent.putExtra(
                            "url",
                            UrlEntry.NEAR_SHOP_URL
                                    + mComId
                                    + "&uuid="
                                    + (TextUtils
                                    .isEmpty(getUserUUid(getActivity())) ? "1"
                                    : getUserUUid(getActivity())));
                    startActivity(intent);
                    break;
                case R.id.tv_check:
                    mPopupSign.dismiss();
                    startActivity(new Intent(getActivity(), IntegralActivity.class));
                    break;
                case R.id.ll_price01:
//					intent = new Intent(getActivity(), PigPriceFragment.class);
//					intent.putExtra("nearPro", nearPro);
//					intent.putExtra("nearCity", nearCity);
//					intent.putExtra("type", "wsy");
//					startActivity(intent);
                    intent = new Intent(getActivity(), LineChartActivity.class);
                    intent.putExtra("province", provinceContent);
                    intent.putExtra("city", cityContent);
                    intent.putExtra("type", "wsy");
                    intent.putExtra("title", "外三元");
                    startActivity(intent);
                    break;
                case R.id.ll_price02:
                    intent = new Intent(getActivity(), LineChartActivity.class);
                    intent.putExtra("province", provinceContent);
                    intent.putExtra("city", cityContent);
                    intent.putExtra("type", "nsy");
                    intent.putExtra("title", "内三元");
                    startActivity(intent);
//					intent = new Intent(getActivity(), PigPriceFragment.class);
//					intent.putExtra("nearPro", nearPro);
//					intent.putExtra("nearCity", nearCity);
//					intent.putExtra("type", "nsy");
//					startActivity(intent);
//					intent = new Intent(getActivity(), LineDemo.class);
//					startActivity(intent);
                    break;
                case R.id.ll_price03:
                    intent = new Intent(getActivity(), LineChartActivity.class);
                    intent.putExtra("province", provinceContent);
                    intent.putExtra("city", cityContent);
                    intent.putExtra("type", "tzz");
                    intent.putExtra("title", "土杂猪");
                    startActivity(intent);
//					intent = new Intent(getActivity(), PigPriceFragment.class);
//					intent.putExtra("nearPro", nearPro);
//					intent.putExtra("nearCity", nearCity);
//					intent.putExtra("type", "tzz");
//					startActivity(intent);
                    break;
                case R.id.ll_price04:
//					intent = new Intent(getActivity(), PigPriceFragment.class);
//					intent.putExtra("nearPro", nearPro);
//					intent.putExtra("nearCity", nearCity);
//					intent.putExtra("type", "ym");
//					startActivity(intent);
                    intent = new Intent(getActivity(), LineChartActivity.class);
                    intent.putExtra("province", provinceContent);
                    intent.putExtra("city", cityContent);
                    intent.putExtra("type", "ym");
                    intent.putExtra("title", "玉米");
                    startActivity(intent);
                    break;
                case R.id.ll_price05:
                    intent = new Intent(getActivity(), LineChartActivity.class);
                    intent.putExtra("province", provinceContent);
                    intent.putExtra("city", cityContent);
                    intent.putExtra("type", "dp");
                    intent.putExtra("title", "豆粕");
                    startActivity(intent);
//					intent = new Intent(getActivity(), PigPriceFragment.class);
//					intent.putExtra("nearPro", nearPro);
//					intent.putExtra("nearCity", nearCity);
//					intent.putExtra("type", "dp");
//					startActivity(intent);
                    break;
                case R.id.ll_price06:
                    break;
                case R.id.weatherLayout:
                    if (cityId.equals("")) {
                        return;
                    }
                    intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("cityId", cityId);
                    startActivity(intent);
                    break;
                case R.id.kfdh:
                    dialog("确认拨打客服电话？", mBtnKf.getText().toString().replace("-", ""));
                    break;
                case R.id.zyfbtBtn:

                    if (!AppStaticUtil.isNetwork(getActivity().getApplicationContext())) {
                        showToast("您的网络有问题请稍后再试！");
                        return;
                    }
                    if (isLogin(getActivity())) {
                        showToast("请先登录！");
                    } else {
                        intent = new Intent(getActivity(),
                                DistributorMapActivity.class);
                        intent.putExtra("x", x);
                        intent.putExtra("province", provinceContent);
                        intent.putExtra("city", cityContent);
                        intent.putExtra("y", y);
                        startActivity(intent);
//						if (fxsshType.equals("2")){
//							intent = new Intent(getActivity(), DistributorIntegralActivity.class);
//							startActivity(intent);
//						}else{
//							intent = new Intent(getActivity(), JoinDistributorActivity.class);
//							startActivity(intent);
//						}

                    }
                    break;
                case R.id.rl_zx:
                    startActivity(new Intent(getActivity(), NewsActivity.class));
                    break;
            }
        }
    };

    private void getDistributorType() {
        isFxsType = false;
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_DISTRIBUTORTYPE_URL,
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
                        System.out.println("shtype" + responseInfo.result);
                        try {
                            JSONObject jsonresult = new JSONObject(
                                    responseInfo.result);
                            if (jsonresult.optString("success").equals("1")) {
                                isFxsType = true ;
                                fxsshType = jsonresult.optString("status").toString();
                                if (fxsshType.equals("2")) {
                                    mBtnMyShop.setText("我的店铺");
                                    App.mUserInfo.setFxsType("1");


                                } else  {
                                    mBtnMyShop.setText("我要开店");
                                    App.mUserInfo.setFxsType("");
                                }
                                FileUtil.saveUser(getActivity(), App.mUserInfo);
                                App.setDataInfo(App.mUserInfo);
                            } else if (jsonresult.optString("success").equals("0")) {
                                fxsshType = "";
                                App.uuid = "";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                });
    }

    private List<AdEntity> mList = new ArrayList<>();

    private void getTodayNews() {
        isRunning=false;
        childJson.clear();
        RequestParams params = new RequestParams();
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.TODAY_NEWS_URL,
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
                        System.out.println("news" + responseInfo.result);
                        try {
                            JSONObject jsonresult = new JSONObject(
                                    responseInfo.result);
                            if (jsonresult.optString("success").equals("1")) {
                                List<NewsEntity> childJsonItem = new ArrayList<NewsEntity>();
                                JSONArray jArrData = jsonresult.getJSONArray("list");
                                for (int i = 0; i < jArrData.length(); i++) {
                                    JSONObject obj =jArrData.optJSONObject(i);
                                    GsonBuilder gsonb = new GsonBuilder();
                                    Gson gson = gsonb.create();
                                    NewsEntity mInfoEntry = gson.fromJson(obj.toString(),
                                            NewsEntity.class);
                                    childJsonItem.add(mInfoEntry);
                                    String title = "";
                                    if (obj.optString("title").length()>16){
                                        title = obj.optString("title").substring(0,16)+"...";
                                    }else
                                    {
                                        title = obj.optString("title");
                                    }
                                    mList.add(new AdEntity("", title, obj.optString("id")));
                                }
                                childJson.addAll(childJsonItem);
                                if (childJson.size()!=0) {
                                    if (newsThread!=null&&newsThread.isAlive()){
                                        newsThread= null;
                                    }
                                    isRunning=true;
                                    number = 0;
                                    newsThread =  new Thread() {
                                        @Override
                                        public void run() {
                                            while (isRunning) {
                                                SystemClock.sleep(3000);
                                                handler.sendEmptyMessage(199);
                                            }
                                        }
                                    };
                                    newsThread.start();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                });
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 199) {
                mADTextView.next();
                number++;
                try{
                mADTextView.setText(childJson.get(number%childJson.size()).getTitle());
                }catch (Exception e){

                }
            }

        }
    };
//    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
//
//        @Override
//        public void onItemClick(AdapterView<?> arg0, View arg1, int witch,
//                                long arg3) {
//            switch (arg0.getId()) {
//                case R.id.vf:
//                    String url = bannerList.get(witch % bannerList.size())
//                            .getSrcPath();
//                    if (TextUtils.isEmpty(url))
//                        return;
//                    if (url.equals(UrlEntry.ip + "/activity/zmt.jsp?img=fxsInfo")) {
//                        Intent intent = new Intent(getActivity(),
//                                ActivityScanResult.class);
//                        intent.putExtra("url", url);
//                        intent.putExtra("fxs", "fxs");
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(getActivity(),
//                                ActivityScanResult.class);
//                        intent.putExtra("url", url);
//                        startActivity(intent);
//                    }
//
//                    break;
//            }
//        }
//    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case 0:
                    // 处理扫描结果（在界面上显示）
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
//							showToast("--------result" + scanResult);
                    if (scanResult.startsWith("loginCode")) {
                        if (isLogin(getActivity())) {
                            showToast("请先登录！");
                        } else {
                            String newuuid = scanResult.split("-")[1];
                            sendNewUUid(newuuid);
                        }
                    } else  {
                        if (scanResult.startsWith("http://")
                                || scanResult.startsWith("https://")){
                            Uri uri = Uri.parse(scanResult);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.addCategory("android.intent.category.BROWSABLE");
                            startActivity(intent);
                        }else{
                           showDialog1(getActivity(),scanResult);
                        }
                    }
                    break;
            }
            return;
        }
        switch (resultCode) {

            case 22:
                mTvLogin.setText("欢迎");
                getDistributorType();
                if (fxsshType.equals("2") || !TextUtils.isEmpty(App.fxsType)) {
                    mBtnMyShop.setText("我的店铺");
                } else {
                    mBtnMyShop.setText("我要开店");
                }
                isSign();
                break;
            case FXSTYPE:
                if (fxsshType.equals("2") || !TextUtils.isEmpty(App.fxsType)) {
                    mBtnMyShop.setText("我的店铺");
                } else {
                    mBtnMyShop.setText("我要开店");
                }
                break;
            case LOCATION_ID:
                if (data == null)
                    return;
                Bundle b = data.getExtras();
                provinceContent = b.getString("province");
                cityContent = b.getString("city");
                quxianContent = b.getString("area");
                System.out.println(quxianContent + "quxianContent");
                if ((provinceContent + cityContent + quxianContent).length() > 11) {
                    mTvLocation.setText(provinceContent + "..." + quxianContent);
                } else
                    mTvLocation.setText(provinceContent + cityContent
                            + quxianContent);
                if (TextUtils.isEmpty(quxianContent)) {
                    mTvInfo.setText("今日" + cityContent + "猪贸通行情指数");
                } else
                    mTvInfo.setText("今日" + quxianContent + "猪贸通行情指数");
                getPigPrice();
                String city = cityContent;
                if (provinceContent.contains("市")) {
                    city = provinceContent;
                }
                mTvTemp.setText("");
                mTvWeather.setText("");
                loadWeather(city.replace("市", ""));
                break;

        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainFragment"); // 统计页面
        if (AppStaticUtil.isLogin(getActivity())) {
            mTvLogin.setText("登录");
        } else {
            mTvLogin.setText("欢迎");
            isSign();
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainFragment");
    }

    private void getPigPrice() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("province", provinceContent);
        params.addBodyParameter("city", cityContent);
        params.addBodyParameter("area", quxianContent);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.TODAY_PIGPRICE_URL,
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
                        System.out.println("price" + responseInfo.result);
                        try {
                            JSONObject jsonresult = new JSONObject(
                                    responseInfo.result);
                            if (jsonresult.optString("success").equals("1")) {
                                mTvPrice01.setText((TextUtils
                                        .isEmpty(jsonresult.optString("wsy")) || jsonresult
                                        .optString("wsy").equals("0")) ? "暂无"
                                        : jsonresult.optString("wsy"));
                                mTvPrice02.setText((TextUtils
                                        .isEmpty(jsonresult.optString("nsy")) || jsonresult
                                        .optString("nsy").equals("0")) ? "暂无"
                                        : jsonresult.optString("nsy"));
                                mTvPrice03.setText((TextUtils
                                        .isEmpty(jsonresult.optString("tzz")) || jsonresult
                                        .optString("tzz").equals("0")) ? "暂无"
                                        : jsonresult.optString("tzz"));
                                mTvPrice04.setText((TextUtils
                                        .isEmpty(jsonresult.optString("ym")) || jsonresult
                                        .optString("ym").equals("0")) ? "暂无"
                                        : jsonresult.optString("ym"));
                                mTvPrice05.setText((TextUtils
                                        .isEmpty(jsonresult.optString("dp")) || jsonresult
                                        .optString("dp").equals("0")) ? "暂无"
                                        : jsonresult.optString("dp"));
                                mTvPrice06.setText((TextUtils
                                        .isEmpty(jsonresult.optString("zlb")) || jsonresult
                                        .optString("zlb").equals("0")) ? "暂无"
                                        : jsonresult.optString("zlb"));
                                String pWsy = (TextUtils.isEmpty(jsonresult
                                        .optString("pWsy")) || jsonresult
                                        .optString("pWsy").equals("0")) ? "暂无"
                                        : jsonresult.optString("pWsy");
                                String pNsy = (TextUtils.isEmpty(jsonresult
                                        .optString("pNsy")) || jsonresult
                                        .optString("pNsy").equals("0")) ? "暂无"
                                        : jsonresult.optString("pNsy");
                                String pTzz = (TextUtils.isEmpty(jsonresult
                                        .optString("pTzz")) || jsonresult
                                        .optString("pTzz").equals("0")) ? "暂无"
                                        : jsonresult.optString("pTzz");
                                mTvProvincePrice.setText("外三元 " + pWsy
                                        + " 内三元 " + pNsy + " 土杂猪 " + pTzz);
                            } else {
                                mTvPrice01.setText("暂无");
                                mTvPrice02.setText("暂无");
                                mTvPrice03.setText("暂无");
                                mTvPrice04.setText("暂无");
                                mTvPrice05.setText("暂无");
                                mTvPrice06.setText("暂无");
                                mTvProvincePrice.setText("暂无");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                });
    }

    private void getNearCom() {
        System.out.println(cityContent + quxianContent);
        RequestParams params = new RequestParams();
        params.addBodyParameter("city", cityContent);
        params.addBodyParameter("area", quxianContent);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.NEAR_COMPANY_URL,
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
                        System.out.println("near" + responseInfo.result);
                        try {
                            JSONObject jsonresult = new JSONObject(
                                    responseInfo.result);
                            if (jsonresult.optString("success").equals("1")) {
                                if (!TextUtils.isEmpty(jsonresult
                                        .optString("companyID"))) {
                                    mComId = jsonresult.optString("companyID");
                                    mLlCenter.setVisibility(View.VISIBLE);
                                    mVLine.setVisibility(View.GONE);
                                    mTvCenter.setText(jsonresult
                                            .optString("companyName"));
                                } else {
                                    mLlCenter.setVisibility(View.GONE);
                                    mVLine.setVisibility(View.VISIBLE);
                                }
                            } else {
                                mLlCenter.setVisibility(View.GONE);
                                mVLine.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                });

    }

    /**
     * 查询是否签到
     */
    private void isSign() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_ISSIGN_URL,
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
                        System.out.println("sign" + responseInfo.result);
                        try {
                            JSONObject jsonresult = new JSONObject(
                                    responseInfo.result);
                            if (jsonresult.optString("success").equals("1")) {
                                if (jsonresult.optString("status").equals("y")) {
                                    mIsSign = false;
                                    mTvLogin.setText("欢迎");
                                    // mTvLogin.setEnabled(false);
                                } else {
                                    mIsSign = true;
                                    mTvLogin.setText("签到");
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                });
    }

    private void addSign() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.ADD_SIGN_URL, params,
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
                        System.out.println("addsign" + responseInfo.result);
                        mTvLogin.setEnabled(true);
                        try {

                            JSONObject jsonresult = new JSONObject(
                                    responseInfo.result);
                            if (jsonresult.optString("success").equals("1")) {
                                if (jsonresult.optString("status").equals("y")) {
                                    mIsSign = false;
                                    mPopupSign = new PopupSign(getActivity(),
                                            mClickListener);
                                    mPopupSign.getmHolder().tvPoint.setText("您获得了"
                                            + jsonresult.optString("integral")
                                            + "积分");
                                    mPopupSign.showPopupWindow(getView()
                                            .findViewById(R.id.ll_main));
                                    mTvLogin.setText("欢迎");

                                }
                            } else {
                                successType(jsonresult.optString("success"),
                                        "签到失败！");
                            }
                        } catch (JSONException e) {
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        mTvLogin.setEnabled(true);
                    }
                });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning=false;
    }
    private  DaijinquanPopuWindow mPwDialog;
    private void showPopDialog(View parent) {

        // 自定义的单击事件
        OnClickLintener paramOnClickListener = new OnClickLintener();
        mPwDialog = new DaijinquanPopuWindow(getActivity(),
                paramOnClickListener, parent,amount,toptip);
        // 如果窗口存在，则更新
        mPwDialog.update();
    }
    class OnClickLintener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close: // 关闭
                    mPwDialog.dismiss();
                    break;
                case R.id.btn_look: //
                    Intent intent= new Intent(getActivity(), DaijinquanListActivity.class);
                    startActivity(intent);
                    mPwDialog.dismiss();
                    break;

                default:
                    break;
            }

        }

    }

    //获取未读消息
    private void loadNotices(){
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        params.addBodyParameter("e.status", "0");
        params.addBodyParameter("e.pageSize", "1");
        params.addBodyParameter("pager.offset", String.valueOf(mPage));
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_NOTICES_LIST_URL, params,
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
                        System.out.println("notices————" + responseInfo.result);

                        try {

                            JSONObject jsonresult = new JSONObject(
                                    responseInfo.result);
                            if (jsonresult.optString("success").equals("1")) {
                              if (!TextUtils.isEmpty(jsonresult.optString("total"))&&!jsonresult.optString("total").equals("0")){
                              total =    Integer.parseInt(jsonresult.optString("total"));
                                  JSONArray jArrData = jsonresult.getJSONArray("list");


                                      JSONObject jobj2 = jArrData.optJSONObject(0);
                                      GsonBuilder gsonb = new GsonBuilder();
                                      Gson gson = gsonb.create();
                                  mNoticeEntry = gson.fromJson(jobj2.toString(),
                                              MyNotice.class);

                                      if (!mIsShow){
                                          mNoticeDialog.show();
                                          mNoticeDialog.setTitle(mNoticeEntry.getTitle());
                                          mNoticeDialog.setMessage(mNoticeEntry.getContent());
                                          mIsShow = true;
                                      }else{
                                          mNoticeDialog.setTitle(mNoticeEntry.getTitle());
                                          mNoticeDialog.setMessage(mNoticeEntry.getContent());
                                          mNoticeDialog.setStatus(true);
                                      }

                              }
                            } else {
                                if (mNoticeDialog.isShowing())
                                mNoticeDialog.setStatus(true);
                            }
                        } catch (JSONException e) {
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        if (mNoticeDialog.isShowing())
                        mNoticeDialog.setStatus(true);
                        mPage --;
                    }
                });
    }
 private  NoticeDialog mNoticeDialog;
    private int total = 0 ;
    private Boolean mIsShow = false;
    private MyNotice mNoticeEntry;
    private void showNoticeDialog(){
        mNoticeDialog = new NoticeDialog(getActivity());
        mNoticeDialog.addCancelButton("立即查看");
        mNoticeDialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            //下一步
                if(mPage == total ){
                    showToast("当前已是最后一条消息");
                    return;
                }
                mNoticeDialog.setStatus(false);
                mPage+=1;
                loadNotices();
            }
        });
        mNoticeDialog.setOnCancelButtonClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                mNoticeDialog.cancel();
                //立即查看
                showToast("正在跳转");
                setReadNotice();
                toIntent();

            }
        });
        mNoticeDialog.setOnCloseButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mNoticeDialog.cancel();
                App.mShowNoticeDialog = true;
            }
        });

    }
private void toIntent(){
    JSONObject obj = null;
    try {
        obj = new JSONObject(mNoticeEntry.getCostom());
        String type = obj.optString("type");
        Intent intent=new Intent();
        if (type.equals("xd2u1") || type.equals("fb2u2s")) {
            String id=obj.optString("id");
            String userid=obj.optString("userid");
            String orderType=obj.optString("ordertype");
            if (!orderType.equals("2")) {
                intent.setClass(getActivity(),InfoSellDetailActivity.class);
            } else {
                intent.setClass(getActivity(),InfoBuyDetailActivity.class);
            }
            intent.putExtra("id", id);
            intent.putExtra("userid", userid);
            intent.putExtra("type", orderType);
        }else if (type.equals("cj2zc")){ //交易成功代金券
            String amount = obj.optString("amount");
            String toptip = "";
            intent.setClass(getActivity(), DaijinquanListActivity.class);
            intent.putExtra("amount",amount);
            intent.putExtra("toptip",toptip);
        }else if(type.equals("rz2zc")){//认证代金券
            String amount = obj.optString("amount");
            String toptip = "认证成功";
            intent.setClass(getActivity(), DaijinquanListActivity.class);
            intent.putExtra("amount",amount);
            intent.putExtra("toptip",toptip);
        }else if(type.equals("rz")){//认证成功
            String userType = obj.optString("userType");
            if (userType.equals("1")) { // 猪场
                intent.setClass(getActivity(), ZhuChangRenZhengActivity.class);
            } else if (userType.equals("2")) {// 屠宰场
                intent.setClass(getActivity(), SlaughterhouseRenZhengActivity.class);
            } else if (userType.equals("3")) {// 经纪人
                intent.setClass(getActivity(), AgentRenZhengActivity.class);
            } else if (userType.equals("4")) {
                intent.setClass(getActivity(), VehicleCompanyAuthActivity.class);
            } else if (userType.equals("5")) {
                intent.setClass(getActivity(), NaturalAuthActivity.class);
            } else if (userType.equals("6")) {
                intent.setClass(getActivity(), HealthAuthActivity.class);
                intent.putExtra("type", "6");
            } else if (userType.equals("7")) {
                intent.setClass(getActivity(), HealthAuthActivity.class);
                intent.putExtra("type", "7");
            } else if (userType.equals("8")) {
                intent.setClass(getActivity(), HealthAuthActivity.class);
                intent.putExtra("type", "8");
            }
        }else if(type.equals("news2all")){
            intent.setClass(getActivity(), NewsCommentActivity.class);
            NewsEntity news = new NewsEntity();
            news.setId(obj.optString("id"));
            Bundle bundle = new Bundle();
            bundle.putSerializable("news", news);
            bundle.putString("url", UrlEntry.ip+"/mNews/newsInfo.html?id="+obj.optString("id"));
            intent.putExtras(bundle);
        }else{
            return;
        }
        startActivity(intent);
    } catch (JSONException e) {
        e.printStackTrace();
    }

}
//标记已读
private void setReadNotice(){
    RequestParams params = new RequestParams();
    params.addBodyParameter("uuid", App.uuid);
    params.addBodyParameter("e.id", mNoticeEntry.getId());
    final HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, UrlEntry.UPP_NOTICE_STATUS_URL, params,
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
                    Log.i("upp",responseInfo.result);
                    JSONObject jsonresult = null;
                    try {
                        jsonresult = new JSONObject(responseInfo.result);
                        if (jsonresult.optString("success").equals("1")) {
                            mPage -- ;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(HttpException error, String msg) {
                }
            });
}

}
