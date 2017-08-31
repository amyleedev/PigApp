package com.szmy.pigapp.pigactivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.service.LocationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.ChoseProvinceActivity;
import com.szmy.pigapp.activity.InfoBuyDetailActivity;
import com.szmy.pigapp.activity.InfoSellDetailActivity;
import com.szmy.pigapp.activity.SearchConditionActivity;
import com.szmy.pigapp.activity.SearchPigActivity;
import com.szmy.pigapp.adapter.OrderAdapter;
import com.szmy.pigapp.entity.InfoEntry;
import com.szmy.pigapp.fragment.MainFragment;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.CircleFlowIndicator;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;
import com.szmy.pigapp.widget.ViewFlow;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PigListFragment extends Fragment {
    private MySwipeRefreshLayout mSrlData;
    private ListView mLv;
    private OrderAdapter mAdapter;
    private int mPage;
    private static final int PAGE_SIZE = 20;
    private List<InfoEntry> mList;

    private FrameLayout mFlBanner;
    private ViewFlow mVf;
    private CircleFlowIndicator mCfi;
    // private FrameLayout mFlChooseHeader;
    private FrameLayout mFlHover1;
    private LinearLayout mLlChoose;
    private FrameLayout mFlHover2;
    private View mFooterView;
    private LinearLayout mLlFooterLoading;
    private TextView mTvFooterResult;
    private boolean mIsLoadAll = false;
    private String mProvince = "";// 省
    private String mCity = "";// 市
    private String mPrefecture = "";// 县
    private String mOrderType = "";// 供求
    private String mPigType = "";// 品种
    private String mOrderBy = "";// 价格
    private String mStartPrice = "";// 开始价格
    private String mEndPrice = "";// 结束价格
    private String mStartWeight = "";// 开始体重
    private String mEndWeight = "";// 结束体重
    private String mStartNumber = "";// 开始数量
    private String mEndNumber = "";// 结束数量
    private String mOrderStatus = "";// 订单状态
    private String zhidingtype = "1";

    private LinearLayout mLlAddress;
    private TextView mTvAddress;
    private LinearLayout mLlBuy;
    private TextView mTvBuy;
    private LinearLayout mLlSell;
    private TextView mTvSell;
    private LinearLayout mLlBolting;
    private TextView mTvBolting;
    private LinearLayout mLlLoading;
    private LinearLayout mLlLoadFailure;
    private EditText mEtSearch;
    private LinearLayout mLlNearby;
    public static final int LOCATION_ID = 29;
    public LocationService locationService;
    public Vibrator mVibrator;
    public static double x = 0;
    public static double y = 0;
    private String city;

    private final int REFRESH = 27;
    private boolean mIsLoading;
    private int margin = 0;
    private LayoutParams params;
    private RelativeLayout mRlTitle;
    private ImageView mImgBuy;
    private ImageView mImgSell;
    private ImageView mImgAddress;
    private ImageView mImgBolting;
    private String buytype = "1";
    private String selltype = "1";
    private String addresstype = "1";
    private String boltingtpe = "1";
    private int refreshint = 1;
    private String totalPages = "0";

    public static PigListFragment newInstance() {

        PigListFragment newFragment = new PigListFragment();
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pig, container, false);
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
//        reload();
        mLv.setOnScrollListener(mOnScrollListener);
        mLv.setOnItemClickListener(mItemClickListener);
        if (AppStaticUtil.isNetwork(getActivity().getApplicationContext())) {
            locationService = new LocationService(getActivity()
                    .getApplicationContext());
            locationService.registerListener(mListener);
        }
    }

    private void initView(View view) {
        mList = new ArrayList<InfoEntry>();

        mFooterView = LayoutInflater.from(getActivity()).inflate(
                R.layout.footer_view, null);
        mLlFooterLoading = (LinearLayout) mFooterView
                .findViewById(R.id.ll_footer_loading);
        mTvFooterResult = (TextView) mFooterView
                .findViewById(R.id.tv_footer_result);
        mSrlData = (MySwipeRefreshLayout) view.findViewById(R.id.srl_data);

        mSrlData.setOnRefreshListener(mRefreshListener);
        mSrlData.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mLv = (ListView) view.findViewById(R.id.lv);
//        mLv.setOnScrollListener(mOnScrollListener);
//        mLv.setOnItemClickListener(mItemClickListener);

        mLlAddress = (LinearLayout) view.findViewById(R.id.ll_address);
        mLlAddress.setOnClickListener(mClickListener);
        mTvAddress = (TextView) view.findViewById(R.id.tv_address);
        mImgAddress = (ImageView) view.findViewById(R.id.img_address);
        mLlBuy = (LinearLayout) view.findViewById(R.id.ll_buy);
        mLlBuy.setOnClickListener(mClickListener);
        mImgBuy = (ImageView) view.findViewById(R.id.img_buy);
        mTvBuy = (TextView) view.findViewById(R.id.tv_buy);
        mLlSell = (LinearLayout) view.findViewById(R.id.ll_sell);
        mLlSell.setOnClickListener(mClickListener);
        mImgSell = (ImageView) view.findViewById(R.id.img_sell);
        mTvSell = (TextView) view.findViewById(R.id.tv_sell);
        // mTvSupply = (TextView) view.findViewById(R.id.tv_supply);
        // mTvSupply = (TextView) view.findViewById(R.id.tv_supply);
        mLlBolting = (LinearLayout) view.findViewById(R.id.ll_bolting);
        mLlBolting.setOnClickListener(mClickListener);
        mImgBolting = (ImageView) view.findViewById(R.id.img_bolting);
         mTvBolting = (TextView) view.findViewById(R.id.tv_bolting);
        mLlLoading = (LinearLayout) view.findViewById(R.id.ll_loading);
        mLlLoadFailure = (LinearLayout) view.findViewById(R.id.ll_load_failure);
        mLlLoadFailure.setOnClickListener(mClickListener);
        mEtSearch = (EditText) view.findViewById(R.id.et_to_search);
        mEtSearch.setOnClickListener(mClickListener);
        //2017-07-25 区域选择改为附近
//        if(TextUtils.isEmpty(MainFragment.province)){
//            mTvAddress.setText("区域");
//        }else{
//            if(MainFragment.province.startsWith("内蒙古")){
//                mTvAddress.setText("内蒙古");
//            }else if(MainFragment.province.startsWith("广西")){
//                mTvAddress.setText("广西");
//            }else if(MainFragment.province.startsWith("西藏")){
//                mTvAddress.setText("西藏");
//            }else if(MainFragment.province.startsWith("宁夏")){
//                mTvAddress.setText("宁夏");
//            }else if(MainFragment.province.startsWith("新疆")){
//                mTvAddress.setText("新疆");
//            }else{
//                mTvAddress.setText(MainFragment.province);
//            }
//
//
//        }

    }

    private void load() {
        reload();
        mLlLoading.setVisibility(View.VISIBLE);
        mSrlData.setVisibility(View.GONE);
        mLlLoadFailure.setVisibility(View.GONE);
    }

    private void reload() {
        zhidingtype="1";
        totalPages= "0";
        mPage = 1;
        mIsLoadAll = false;
        loadData();
    }

    public void loadData() {
        mIsLoading = true;
        RequestParams params = new RequestParams();
        params.addBodyParameter("pager.offset", String.valueOf(mPage));
        params.addBodyParameter("pager.pageSize", String.valueOf(PAGE_SIZE));
        params.addBodyParameter("e.province", mProvince);
        params.addBodyParameter("e.city", mCity);
        params.addBodyParameter("e.area", mPrefecture);
        params.addBodyParameter("e.orderBy", mOrderBy); // 价格
        params.addBodyParameter("e.pigType", mPigType);// 品种
        params.addBodyParameter("e.orderType", mOrderType);// 供求
        params.addBodyParameter("e.orderStatus", mOrderStatus);
        params.addBodyParameter("e.startPrice", mStartPrice);
        params.addBodyParameter("e.endPrice", mEndPrice);
        params.addBodyParameter("e.startWeight", mStartWeight);
        params.addBodyParameter("e.endWeight", mEndWeight);
        params.addBodyParameter("e.startNumber", mStartNumber);
        params.addBodyParameter("e.endNumber", mEndNumber);
        if (!TextUtils.isEmpty(App.uuid)){
            params.addBodyParameter("uuid", App.uuid);
        }else{
            zhidingtype = "0";
        }

        params.addBodyParameter("Zdtype", zhidingtype);
        if (zhidingtype.equals("0")){
            params.addBodyParameter("zdpager", totalPages);
        }

        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_HOT_ALL_URL, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                    }

                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        mIsLoading = false;
                        Log.i("insert", " insert responseInfo.result = "
                                + responseInfo.result);
                        if(getActivity()==null){
                            return;
                        }
                        if (getActivity().isDestroyed()) {
                            System.out.println("已经关闭了");
                            return;
                        }
                        parseData(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        mIsLoading = false;
                        if (mLlLoading.getVisibility() == View.VISIBLE) {
                            mLlLoading.setVisibility(View.GONE);
                            mLlLoadFailure.setVisibility(View.VISIBLE);
                        } else {
                            mSrlData.setRefreshing(false);
                        }
                        if (mPage > 1)
                            mPage--;
                    }
                });
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location
                    && location.getLocType() != BDLocation.TypeServerError) {
                x = location.getLatitude();
                y = location.getLongitude();
                // mProvince = location.getProvince();
                city = location.getCity();
                // mPrefecture = location.getDistrict();
            }
        }
    };

    private void parseData(String result) {
//		if (!TextUtils.isEmpty(result)) {
//			System.out.println("selected_order_list:" + result);
////			try {
////				JSONObject jsonresult = new JSONObject(result);
////				if (jsonresult.optString("success").equals("1")) {
////					List<InfoEntry> childJsonItem = new ArrayList<InfoEntry>();
////					JSONArray jArrData = jsonresult.getJSONArray("list");
////					for (int i = 0; i < jArrData.length(); i++) {
////						JSONObject jobj2 = jArrData.optJSONObject(i);
////						GsonBuilder gsonb = new GsonBuilder();
////						Gson gson = gsonb.create();
////						InfoEntry mInfoEntry = gson.fromJson(jobj2.toString(),
////								InfoEntry.class);
////						childJsonItem.add(mInfoEntry);
////					}
////				}
////			} catch (Exception e) {
////			}
//		}
        JSONObject jsonresult;
        try {
            jsonresult = new JSONObject(result);
            if (jsonresult.get("success").toString().equals("1")) {
                List<InfoEntry> childJsonItem = new ArrayList<InfoEntry>();
                List<InfoEntry> childJsonItemtrue = new ArrayList<InfoEntry>();//用来存放数量不为0的
                JSONArray jArrData = jsonresult.getJSONArray("list");
                if (zhidingtype.equals("1")){
                    totalPages = jsonresult.optString("pagerSize");
                }

                String iszd= "";
                iszd = jsonresult.optString("iszd").toString();
                for (int i = 0; i < jArrData.length(); i++) {
                    JSONObject jobj2 = jArrData.optJSONObject(i);
                    GsonBuilder gsonb = new GsonBuilder();
                    Gson gson = gsonb.create();
                    InfoEntry mInfoEntry = gson.fromJson(jobj2.toString(),
                            InfoEntry.class);
                        mInfoEntry.setIszd(iszd);
                    childJsonItem.add(mInfoEntry);
                    if (Integer.parseInt(mInfoEntry.getNumber())>0){
                        childJsonItemtrue.add(mInfoEntry);
                    }
                }

                if (mLlLoading.getVisibility() == View.VISIBLE) {
                    if(zhidingtype.equals("0")){
                    mLlLoading.setVisibility(View.GONE);
                    mSrlData.setVisibility(View.VISIBLE);}
                } else {
                    mSrlData.setRefreshing(false);
                }
                if (mPage == 1) {
                    if (mList.size() >= 0) {
                        mList.clear();
                        mLv.removeFooterView(mFooterView);

                        // mLv.removeHeaderView(mFlChooseHeader);
                    }
                    mLv.addFooterView(mFooterView);

                    // mLv.addHeaderView(mFlChooseHeader);
                    System.out.println(childJsonItem.size());
//                    mList.addAll(childJsonItem);
                    mList.addAll(childJsonItemtrue);
                    mAdapter = new OrderAdapter(getActivity(), mList);
                    mLv.setAdapter(mAdapter);

                    if (childJsonItem.size() == 0) {
                        if (zhidingtype.equals("1")){
                            mPage=1;
                            zhidingtype = "0";
                            loadData();
                        }else {
                            mLlFooterLoading.setVisibility(View.GONE);
                            mTvFooterResult.setVisibility(View.VISIBLE);
                            mTvFooterResult.setText("查询结果为空");
                            mIsLoadAll = true;
                        }
                    } else if (childJsonItem.size() < 20) {
                        if (zhidingtype.equals("1")){
                            mPage+=1;
                            zhidingtype = "0";
                            loadData();
                        }else {
                        mLlFooterLoading.setVisibility(View.GONE);
                        mTvFooterResult.setVisibility(View.VISIBLE);
                        mTvFooterResult.setText("已加载全部");
                        mIsLoadAll = true;
                        }
                    } else {
                        mLlFooterLoading.setVisibility(View.VISIBLE);
                        mTvFooterResult.setVisibility(View.GONE);
                    }
                } else {
                    mList.addAll(childJsonItem);
                    mAdapter = new OrderAdapter(getActivity(), mList);
                    mLv.requestLayout();
                    mAdapter.notifyDataSetChanged();
                    if (childJsonItem.size() < 20) {
                        if (zhidingtype.equals("1")){
                            mPage+=1;
                            zhidingtype = "0";
                            loadData();
                        }else {
                            mLlFooterLoading.setVisibility(View.GONE);
                            mTvFooterResult.setVisibility(View.VISIBLE);
                            mTvFooterResult.setText("已加载全部");
                            mIsLoadAll = true;
                        }

                    } else {
                        mLlFooterLoading.setVisibility(View.VISIBLE);
                        mTvFooterResult.setVisibility(View.GONE);
                    }
                }
            }
        } catch (JSONException e) {
        }
    }

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

        @Override
        public void onRefresh() {
//            if (refreshint ==1){
//                refreshint ++;
//                return;
//            }
            reload();
        }
    };
    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_address:
                    if (buytype.equals("2")||selltype.equals("2")){
                    setInfo("", "", "", "", "", mOrderType, mOrderStatus, "", "", "", "", "", "");
                    }else{
                        setInfo("", "", "", "", "", "", "", "", "", "", "", "", "");
                    }
                    if(addresstype.equals("1")){
                        if (TextUtils.isEmpty(MainFragment.province)){
                            Toast.makeText(getActivity(),"没有获取到您的位置信息！",Toast.LENGTH_LONG).show();
                            return;
                        }
                        addresstype = "2";
                        setColorAddress(addresstype);

                        if (mTvAddress.getText().toString().equals("区域")) {
                            startActivityForResult(new Intent(getActivity(),
                                    ChoseProvinceActivity.class), App.ADDRESS_CODE);
                        }else{
                            mProvince = MainFragment.province;
                            mCity = MainFragment.city;
                            mPrefecture = MainFragment.areas;
                            load();
                        }

                    }else{
                        addresstype = "1";
                        setColorAddress(addresstype);
                        mProvince = "";
                        mCity = "";
                        mPrefecture = "";
                        load();
                    }
//                    if (mTvAddress.getText().toString().equals("区域")) {
//                        startActivityForResult(new Intent(getActivity(),
//                                ChoseProvinceActivity.class), TYPE_ID);
//                    } else {
//                        //设置省的值
//                        if (addresstype.equals("1")){
//                            mProvince = "";
//
//                        }else{
//                            mProvince = MainFragment.province;
//                        }
//                        mCity = "";
//                        mPrefecture = "";
//                        load();
//                    }
                    break;
                case R.id.ll_buy:
                    if (addresstype.equals("1"))//附近为未选中状态
                    {
                        setInfo("", "", "", "", "", "", "", "", "", "", "", "", "");
                    }
                    if (buytype.equals("1")) {
                        buytype = "2";
                        setColorBuy(buytype);
                        selltype = "1";
                        setColorSell(selltype);
                        boltingtpe = "1";
                        setColorBolting(boltingtpe);
                        mOrderType = "1";
                        mOrderStatus = "1";
                        //请求未成交的出售信息
                        load();
                    }else {
                        buytype = "1";
                        setColorBuy(buytype);
                        mOrderType = "";
                        mOrderStatus = "";
                        load();
                    }
                    break;
                case R.id.ll_sell://我要卖猪
                    if (addresstype.equals("1"))//附近为未选中状态
                    {
                        setInfo("", "", "", "", "", "", "", "", "", "", "", "", "");
                    }
                    if(selltype.equals("1")){
                        selltype = "2";
                        setColorSell(selltype);
                        buytype = "1";
                        setColorBuy(buytype);
                        boltingtpe = "1";
                        setColorBolting(boltingtpe);
                        mOrderType = "2";
                        mOrderStatus = "1";
                        //请求未成交的收购信息1出售2收购
                        load();
                    }else{
                        selltype = "1";
                        setColorSell(selltype);
                        //请求未成交的收购信息1出售2收购
                        mOrderType = "";
                        mOrderStatus = "";
                        load();
                    }


//				startActivityForResult(new Intent(getActivity(),
//						ChoseDealTypeActivity.class), 24);
                    break;
                case R.id.ll_bolting:
                    if(boltingtpe.equals("1")){
                        boltingtpe = "2";
                        setColorBolting(boltingtpe);
                        buytype = "1";
                        setColorBuy(buytype);
                        selltype = "1";
                        setColorSell(selltype);
                        addresstype = "1";
                        setColorAddress(addresstype);
                    }

                    startActivityForResult(new Intent(getActivity(),
                            SearchConditionActivity.class), 30);
                    break;
                case R.id.et_to_search:
                    startActivity(new Intent(getActivity(), SearchPigActivity.class));
                    break;
                case R.id.ll_load_failure:
                    load();
                    break;

            }
        }
    };
    private OnItemClickListener mItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int witch,
                                long arg3) {
            switch (arg0.getId()) {
                case R.id.lv:

                    if ((witch) >= mList.size() || (witch) < 0) {
                        return;
                    }

                    FileUtil.deleteDirfile(FileUtil.SDPATH);
                    if (!mList.get(witch).getOrderType().equals("2")) {
                        Intent intent = new Intent(getActivity(),
                                InfoSellDetailActivity.class);
                        intent.putExtra("id", mList.get(witch).getId());
                        intent.putExtra("userid", mList.get(witch).getUserID());
                        intent.putExtra("type", mList.get(witch).getOrderType());
                        startActivityForResult(intent, REFRESH);
                    } else {
                        Intent intent = new Intent(getActivity(),
                                InfoBuyDetailActivity.class);
                        intent.putExtra("id", mList.get(witch).getId());
                        intent.putExtra("userid", mList.get(witch).getUserID());
                        intent.putExtra("type", mList.get(witch).getOrderType());
                        startActivityForResult(intent, REFRESH);
                    }
                    break;

            }

        }
    };

    private OnScrollListener mOnScrollListener = new OnScrollListener() {

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // if (firstVisibleItem > 0) {
            // if (mLlChoose.getParent() != mFlHover2) {
            // mFlHover1.removeView(mLlChoose);
            // mFlHover2.addView(mLlChoose);
            // }
            // } else {
            // if (mLlChoose.getParent() != mFlHover1) {
            // mFlHover2.removeView(mLlChoose);
            // mFlHover1.addView(mLlChoose);
            // }
            // }
            if (firstVisibleItem + visibleItemCount == totalItemCount) {
                if (mIsLoading)
                    return;
                if (mIsLoadAll)
                    return;
                mPage += 1;
                loadData();
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case App.ADDRESS_CODE:
                if (data == null)
                    return;
                Bundle b = data.getExtras();
                mProvince = b.getString("province");
                if (mProvince.equals("不限"))
                    mProvince = "";
                mCity = b.getString("city");
                if (mCity.equals("不限"))
                    mCity = "";
                mPrefecture = b.getString("area");
                if (mPrefecture.equals("不限"))
                    mPrefecture = "";
                load();
                break;
            case 23:
                if (data == null)
                    return;
                Bundle bundle = data.getExtras();
                mPigType = bundle.getString("pigType");
                load();
                break;
            case 24:
                if (data == null)
                    return;
                Bundle bd = data.getExtras();
                mOrderType = bd.getString("orderType");
                load();
                break;
            // case 25:
            // if (data == null)
            // return;
            // Bundle d = data.getExtras();
            // pricepx = d.getString("orderBy");
            // System.out.println("根据价格排序" + provinceContent + cityContent
            // + quxianContent + "   " + orderType + "  " + pricepx
            // + "   " + pigType);
            // setData("1", TYPE_REFRESH);
            // break;
            case 30:
                if (data == null)
                    return;
                Bundle b1 = data.getExtras();
                mProvince = b1.getString("province");
                if (mProvince.equals("不限"))
                    mProvince = "";
                mCity = b1.getString("city");
                if (mCity.equals("不限"))
                    mCity = "";
                mPrefecture = b1.getString("area");
                if (mPrefecture.equals("不限"))
                    mPrefecture = "";
                mOrderBy = b1.getString("orderBy");
                mOrderType = b1.getString("orderType");
                mPigType = b1.getString("pigType");
                mOrderStatus = b1.getString("orderStatus");
                mStartPrice = b1.getString("startPrice");
                mEndPrice = b1.getString("endPrice");
                mStartWeight = b1.getString("startWeight");
                mEndWeight = b1.getString("endWeight");
                mStartNumber = b1.getString("startNumber");
                mEndNumber = b1.getString("endNumber");
                load();
                break;
            case REFRESH:
                load();
                break;
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainFragment"); // 统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainFragment");
    }

    private void setColorAddress(String caretype) {
        if (caretype.equals("1")) {
            mTvAddress.setTextColor(getResources().getColor(R.color.gray));
            mImgAddress.setImageResource(R.drawable.order_address);
        } else {
            mTvAddress.setTextColor(getResources().getColor(R.color.red));
            mImgAddress.setImageResource(R.drawable.order_address_un);
        }
    }

    private void setColorBuy(String caretype) {
        if (caretype.equals("1")) {
            mTvBuy.setTextColor(getResources().getColor(R.color.gray));
            mImgBuy.setImageResource(R.drawable.order_buy);
        } else {
            mTvBuy.setTextColor(getResources().getColor(R.color.red));
            mImgBuy.setImageResource(R.drawable.order_buy_un);
        }
    }

    private void setColorSell(String caretype) {
        if (caretype.equals("1")) {
            mTvSell.setTextColor(getResources().getColor(R.color.gray));
            mImgSell.setImageResource(R.drawable.order_sell);
        } else {
            mTvSell.setTextColor(getResources().getColor(R.color.red));
            mImgSell.setImageResource(R.drawable.order_sell_un);
        }
    }
    private void setColorBolting(String caretype) {
        if (caretype.equals("1")) {
            mTvBolting.setTextColor(getResources().getColor(R.color.gray));
            mImgBolting.setImageResource(R.drawable.order_bolting);
        } else {
            mTvBolting.setTextColor(getResources().getColor(R.color.red));
            mImgBolting.setImageResource(R.drawable.order_bolting_un);
        }
    }

    private void setInfo(String province,String city,String Prefecture, String orderBy,String pigtype,String ordertype,String OrderStatus,String StartPrice,String EndPrice,String StartWeight,
                         String EndWeight,String StartNumber,String EndNumber){
         mProvince = province;
         mCity = city;
         mPrefecture = Prefecture;
        mOrderBy =orderBy; // 价格
         mPigType = pigtype;// 品种
      mOrderType =ordertype;// 供求
         mOrderStatus = OrderStatus;
        mStartPrice = StartPrice;
        mEndPrice = EndPrice;
         mStartWeight = StartWeight;
        mEndWeight = EndWeight;
         mStartNumber = StartNumber;
        mEndNumber = EndNumber;
    }
}
