package com.szmy.pigapp.quotedprice;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.szmy.pigapp.activity.ActivityScanResult;
import com.szmy.pigapp.activity.ChoseProvinceActivity;
import com.szmy.pigapp.activity.LoginActivity;
import com.szmy.pigapp.activity.SlaughterhouseRenZhengActivity;
import com.szmy.pigapp.activity.ZhuChangRenZhengActivity;
import com.szmy.pigapp.agent.AgentRenZhengActivity;
import com.szmy.pigapp.tixian.ActivityWithdrawals;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.CircleFlowIndicator;
import com.szmy.pigapp.widget.LoadingDialog;
import com.szmy.pigapp.widget.MySwipeRefreshLayout;
import com.szmy.pigapp.widget.ViewFlow;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuotedPriceListFragment extends Fragment {
    private MySwipeRefreshLayout mSrlData;
    private ListView mLv;
    private QuotedPriceAdapter mAdapter;
    private int mPage;
    private static final int PAGE_SIZE = 20;
    private List<QuotedPrice> mList;

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
    private String mUserType = "";// 角色
    private String mUserTypeName = "";// 角色名字
    private String mPriceTypeName = "";// 报价
    private String mPriceType = "";// 报价
    private String mPigType = "";// 品种
    private String mUserName = "";
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
    private LoadingDialog dialog = null;
    public Toast mToast = null;
    public SharedPreferences.Editor editor;
    public SharedPreferences sp;
    private QuotedPrice qpinfo;
    private String[] mPigTypeItems = {"不限","内三元", "外三元", "土杂猪"};
    private String[] mPriceTypeItems = {"不限","出售计划", "收购计划"};
    private String[] mUserTypeItems = {"不限","猪场", "屠宰场","经纪人"};
   private int mSingleChoiceID = 0;
    private LinearLayout mLlContent;

    public static QuotedPriceListFragment newInstance() {

        QuotedPriceListFragment newFragment = new QuotedPriceListFragment();
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quotedprice, container, false);
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
//        reload();
        mLv.setOnScrollListener(mOnScrollListener);
        mLv.setOnItemClickListener(mItemClickListener);

    }

    private void initView(View view) {
        mList = new ArrayList<QuotedPrice>();
        sp = getActivity().getSharedPreferences(App.USERINFO, 0);
        editor = sp.edit();
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
        mLlContent = (LinearLayout) view.findViewById(R.id.contentlayout);

        mEtSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    // 先隐藏键盘
                    ((InputMethodManager) mEtSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    //获取编辑框内容,执行相应操作
                    mProvince = "";
                    mCity = "";
                    mPriceType="";
                    mUserType="";
                    mPigType="";
                    mPrefecture = "";
                    setColorSell("1");
                    mTvSell.setText("角色分类");
                    setColorBuy("1");
                    mTvBuy.setText("生猪类型");
                    setColorBolting("1");
                    mTvBolting.setText("报价类型");
                    setColorAddress("1");
                    mTvAddress.setText("区域");
                    mUserName = mEtSearch.getText().toString();
                    load();
                    return true;
                }
                return false;
            }
        });

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
        params.addBodyParameter("e.productType", mPigType);// 品种
        params.addBodyParameter("e.type", mPriceType);// 供求
        params.addBodyParameter("e.userType", mUserType);// 角色
        params.addBodyParameter("e.userName", mUserName);// 角色


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
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUOTED_PRICE_ZDLIST_URL, params,
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



    private void parseData(String result) {

        JSONObject jsonresult;
        try {
            jsonresult = new JSONObject(result);
            if (jsonresult.get("success").toString().equals("1")) {
                List<QuotedPrice> childJsonItem = new ArrayList<QuotedPrice>();
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
                    QuotedPrice mInfoEntry = gson.fromJson(jobj2.toString(),
                            QuotedPrice.class);
                    mInfoEntry.setIszd(iszd);
                    childJsonItem.add(mInfoEntry);

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
                    mList.addAll(childJsonItem);

                    mAdapter = new QuotedPriceAdapter(getActivity(), mList,"0");
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

                    mAdapter = new QuotedPriceAdapter(getActivity(), mList,"0");
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
                         startActivityForResult(new Intent(getActivity(),
                           ChoseProvinceActivity.class), App.ADDRESS_CODE);
                    break;
                case R.id.ll_buy: //生猪品种

                    AlertDialog.Builder builder = new AlertDialog.Builder(
                           getActivity());

                    mSingleChoiceID = 0;
                    builder.setTitle("请选择生猪类别");
                    builder.setSingleChoiceItems(mPigTypeItems, 0,
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

                                    mTvBuy.setText(mPigTypeItems[mSingleChoiceID]);
                                    mPigType = mPigTypeItems[mSingleChoiceID];
                                    setColorBuy("2");
                                    if (mPigType.equals("不限")){
                                        mPigType = "";
                                        setColorBuy("1");
                                        mTvBuy.setText("生猪类型");
                                    }
                                    mUserName = "";
                                    load();

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
                case R.id.ll_sell://角色分类

                     builder = new AlertDialog.Builder(
                            getActivity());

                    mSingleChoiceID = 0;
                    builder.setTitle("请选择角色");
                    builder.setSingleChoiceItems(mUserTypeItems, 0,
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

                                    mTvSell.setText(mUserTypeItems[mSingleChoiceID]);
                                    mUserTypeName = mUserTypeItems[mSingleChoiceID];
                                    setColorSell("2");
                                    if (mUserTypeName.equals("不限")){
                                        mUserType = "";
                                        setColorSell("1");
                                        mTvSell.setText("角色分类");
                                    }else{
                                        mUserType = String.valueOf(mSingleChoiceID);
                                    }
                                    mUserName = "";
                                    load();

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
                case R.id.ll_bolting://报价类型

                    builder = new AlertDialog.Builder(
                            getActivity());

                    mSingleChoiceID = 0;
                    builder.setTitle("请选择报价类型");
                    builder.setSingleChoiceItems(mPriceTypeItems, 0,
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

                                    mTvBolting.setText(mPriceTypeItems[mSingleChoiceID]);
                                    mPriceTypeName = mPriceTypeItems[mSingleChoiceID];
                                    setColorBolting("2");
                                    if (mPriceTypeName.equals("不限")){
                                        mPriceType = "";
                                        setColorBolting("1");
                                        mTvBolting.setText("报价类型");
                                    }else{
                                        mPriceType = String.valueOf(mSingleChoiceID);
                                    }
                                    mUserName = "";
                                    load();

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
                case R.id.et_to_search:
//                    startActivity(new Intent(getActivity(), SearchPigActivity.class));
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
            HideKeyboard(mEtSearch);
            switch (arg0.getId()) {
                case R.id.lv:

                    if ((witch) >= mList.size() || (witch) < 0) {
                        return;
                    }
//                    if (!mList.get(witch).getOrderType().equals("2")) {
//                        Intent intent = new Intent(getActivity(),
//                                InfoSellDetailActivity.class);
//                        intent.putExtra("id", mList.get(witch).getId());
//                        intent.putExtra("userid", mList.get(witch).getUserID());
//                        intent.putExtra("type", mList.get(witch).getOrderType());
//                        startActivityForResult(intent, REFRESH);
//                    } else {
//                        Intent intent = new Intent(getActivity(),
//                                InfoBuyDetailActivity.class);
//                        intent.putExtra("id", mList.get(witch).getId());
//                        intent.putExtra("userid", mList.get(witch).getUserID());
//                        intent.putExtra("type", mList.get(witch).getOrderType());
//                        startActivityForResult(intent, REFRESH);
//                    }
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
                setColorAddress("2");
                if(!TextUtils.isEmpty(mPrefecture)){
                    mTvAddress.setText(mPrefecture);
                }else if(!TextUtils.isEmpty(mCity)){
                        mTvAddress.setText(mCity);
                }else if(!TextUtils.isEmpty(mProvince)){
                mTvAddress.setText(mProvince);
                 }else{
                    mTvAddress.setText("区域");
                    setColorAddress("1");
                }
                mUserName = "";
                load();
                break;
            case REFRESH:
                load();
                break;
             default:
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
            mImgBuy.setImageResource(R.drawable.ic_pigtypeg);
        } else {
            mTvBuy.setTextColor(getResources().getColor(R.color.red));
            mImgBuy.setImageResource(R.drawable.ic_pigtype);
        }
    }

    private void setColorSell(String caretype) {
        if (caretype.equals("1")) {
            mTvSell.setTextColor(getResources().getColor(R.color.gray));
            mImgSell.setImageResource(R.drawable.ic_priceuserg);
        } else {
            mTvSell.setTextColor(getResources().getColor(R.color.red));
            mImgSell.setImageResource(R.drawable.ic_priceuser);
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

    private void getIsSmrz(final TextView price, final TextView btn) {
//        btnSubmit.setEnabled(false);
        showDialog();
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
                        disDialog();
//                        btnSubmit.setEnabled(true);
                        Log.i("smrz", " smrz responseInfo.result = "
                                + responseInfo.result);
                        JSONObject jsonresult = null;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.optString("success").toString()
                                    .equals("1")) {
                                sendDataByUUid(price,btn);

                            } else if (jsonresult.optString("success").toString()
                                    .equals("6")) {
                                final Dialog dialog = new Dialog(getActivity(), "提示", jsonresult.optString("msg").toString());
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
                                            Intent zcintent = new Intent( getActivity(),
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
                            } else if (jsonresult.optString("success").toString()
                                    .equals("5")) {
                                showDialog1(getActivity(), jsonresult.optString("msg").toString());
                            } else {
                                successType(jsonresult.optString("success").toString(), "操作失败");
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

    //查询银行卡信息
    private void sendDataByUUid(final TextView price, final TextView btn) {
        showDialog();
//        btnSubmit.setEnabled(false);
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
                        Log.i("select", " bank responseInfo.result = "
                                + responseInfo.result);

                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {

//弹窗提示是否扣除积分
                               final Dialog dialog = new Dialog(getActivity(), "提示", "查看价格需要扣除10积分，是否继续查看");
                                dialog.addCancelButton("取消");
                                dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                        getPoint(price,btn);
                                    }
                                });
                                dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                    }
                                });
                                dialog.show();


                            } else if (jsonresult.get("success").toString()
                                    .equals("5")) {
                                //没有银行卡信息
                                String msg = "查看价格失败，请先绑定银行卡。";
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("type", "rz");
                                jumpActivity(getActivity(), ActivityWithdrawals.class, msg, map);
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
                        showToast("服务器连接失败，请稍候再试！");
                        disDialog();

                    }
                });
    }

    private void getPoint(final TextView price,final TextView btn){
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        params.addBodyParameter("id", qpinfo.getId());
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_QUOTED_PRICE_POINT_URL,
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
                        Log.i("insert", " insert responseInfo.result = "
                                + responseInfo.result);
                        disDialog();
                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.optString("success").toString()
                                    .equals("1")) {

                                if (jsonresult.optString("read").equals("0")){
                                    if (jsonresult.optString("free").equals("0")){
                                        final Dialog dialog = new Dialog(getActivity(), "提示", "您的积分不足");
                                        dialog.addCancelButton("取消");
                                        dialog.addAccepteButton("去赚积分");
                                        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.cancel();
                                                Intent intent = new Intent(getActivity(),
                                                        ActivityScanResult.class);
                                                intent.putExtra("url", UrlEntry.GET_POINT_URL);
                                                startActivity(intent);
                                            }
                                        });
                                        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.cancel();
                                            }
                                        });
                                        dialog.show();
                                        return;
                                    }
                                    if(jsonresult.optString("free").equals("1")){

                                        Intent intent = new Intent(getActivity(),QuotedPriceDetailActivity.class);
                                        intent.putExtra("id",qpinfo.getId());
                                        startActivity(intent);
                                    }

                                }else  if (jsonresult.optString("read").equals("1")){
                                    showDialog1(getActivity(),"此次查看无需扣除积分！");
                                    price.setVisibility(View.VISIBLE);
                                    btn.setText("点击查看详情");
                                    if (TextUtils.isEmpty(qpinfo.getUserType())){
                                        btn.setVisibility(View.GONE);
                                    }
                                }

                            } else {
                                successType(jsonresult.optString("success")
                                        .toString(), jsonresult
                                        .optString("msg").toString());
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

    // 环形加载进度条
    public void showDialog() {
        if (dialog != null) {
            dialog = null;
        }
        dialog = new LoadingDialog(getActivity());
        dialog.setCancelable(true);
        dialog.show();
    }
    public void disDialog() {
        if (dialog != null && dialog.isShowing()) {
            try{
                dialog.dismiss();
            }catch (Exception e){

            }

        }
    }
    public void successType(String successtype, String msg) {
        if (successtype.equals("0")) {
            showToast("用户信息错误,请重新登录！");
            relogin(getActivity());
        } else if (successtype.equals("3")) {
            showToast(msg);
        } else if (successtype.equals("2")) {
            showToast(msg);
        }
    }
    public void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(getActivity(), msg,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    public void relogin(Context context) {
        editor.putString("userinfo", "");
        editor.commit();
        App.clearData();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivityForResult(intent, 22);
    }
    public void jumpActivity(final Context mContxt, final Class mToContxt,
                             String msg, final Map<String, String> map) {
        final Dialog dialog = new Dialog(mContxt, "提示", msg);
        dialog.addCancelButton("取消");
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

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
        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }
    public void showDialog1(Context context, String msg) {
        final Dialog dialog = new Dialog(context,"提示",msg);
        dialog.addCancelButton("取消");
        dialog.show();
    }
    public class QuotedPriceAdapter extends BaseAdapter {

        private Context ctx;
        private List<QuotedPrice> childJson;
        private LayoutInflater mInflater;
        private boolean mBusy = false;//
        private BitmapUtils bitmapUtils;
        private String type = "0";


        public QuotedPriceAdapter(Context ctx, List<QuotedPrice> list,String looktype) {
            this.childJson = list;
            mInflater = LayoutInflater.from(ctx);
            bitmapUtils = new BitmapUtils(ctx);
            this.type = looktype;

        }

        public void setFlagBusy(boolean busy) {
            this.mBusy = busy;
        }

        @Override
        public int getCount() {
            return childJson.size();
        }

        @Override
        public Object getItem(int position) {
            return childJson.get(position);
        }

        @Override
        public long getItemId(int posotion) {
            return posotion;
        }

        @Override
        public View getView(int posotion, View convertView, ViewGroup arg2) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_quoted_fragment_list,
                        null);
                holder.mLlItem = (LinearLayout) convertView.findViewById(R.id.item);
                holder.mInfoTitle = (TextView) convertView
                        .findViewById(R.id.info_list_name_text);
                holder.mInfoType = (TextView) convertView
                        .findViewById(R.id.info_list_usertype_text);
                holder.mInfoTime = (TextView) convertView
                        .findViewById(R.id.info_list_time_text);
                holder.mInfoAddress = (TextView) convertView
                        .findViewById(R.id.info_list_address_text);
                holder.mInfoPrice = (TextView) convertView
                        .findViewById(R.id.tvPrice);
                holder.mInfoCount = (TextView) convertView
                        .findViewById(R.id.info_list_count_text);
                holder.mInfoisZd = (TextView) convertView.findViewById(R.id.info_list_zd_text);
                holder.mTvLookPrice = (TextView) convertView.findViewById(R.id.info_list_status_text);
                holder.mRlLookPrice = (RelativeLayout) convertView.findViewById(R.id.rl_lookprice);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final QuotedPrice info = childJson.get(posotion);

            if (!TextUtils.isEmpty(info.getIszd())){
                holder.mInfoisZd.setVisibility(View.VISIBLE);
            }else{
                holder.mInfoisZd.setVisibility(View.GONE);

            }
            holder.mInfoTitle.setText(info.getProductType().toString());
            if (TextUtils.isEmpty(info.getArea().toString())){
                if (TextUtils.isEmpty(info.getCity().toString())){
                    holder.mInfoAddress.setText(info.getProvince().toString());
                }else{
                    holder.mInfoAddress.setText(info.getCity().toString());
                }
            }else
            holder.mInfoAddress.setText(info.getArea().toString());
            holder.mInfoCount.setText(info.getNumber()+"头");
            holder.mInfoPrice.setText(info.getPrice()+"元/公斤");


            if (TextUtils.isEmpty(info.getUserType())){
                holder.mInfoType.setText("网络");
                holder.mInfoPrice.setVisibility(View.VISIBLE);
                holder.mTvLookPrice.setVisibility(View.GONE);
            }else {
                if(TextUtils.isEmpty(info.getUserName())||AppStaticUtil.isNumeric(info.getUserName())){
                holder.mInfoType.setText(getuserType(info.getUserType().toString()));
                }else{
                    holder.mInfoType.setText(info.getUserName().toString());
                }
                if (!TextUtils.isEmpty(info.getIszd())){
                    holder.mInfoPrice.setVisibility(View.VISIBLE);
                    holder.mTvLookPrice.setVisibility(View.VISIBLE);
                    holder.mTvLookPrice.setText("点击查看详情");

                }else{
                    holder.mInfoPrice.setVisibility(View.GONE);
                    holder.mTvLookPrice.setVisibility(View.VISIBLE);
                    holder.mTvLookPrice.setText("点击查看价格");
                    if (!TextUtils.isEmpty(info.getIsfree())&&!info.getIsfree().equals("1")){
                        holder.mInfoPrice.setVisibility(View.VISIBLE);
                        holder.mTvLookPrice.setText("点击查看详情");
                    }

                }
            }

            holder.mInfoTime.setText(TextUtils.isEmpty(info.getMarketingTime())?info.getCreateTime().substring(5, 10):info.getMarketingTime().substring(5, 10));
            // 设置每个子布局的事件监听器

            holder.mRlLookPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    qpinfo = info;
                    if (holder.mTvLookPrice.getVisibility()==View.VISIBLE&&holder.mTvLookPrice.getText().equals("点击查看详情")){

                        Intent intent = new Intent(getActivity(),QuotedPriceDetailActivity.class);
                        intent.putExtra("id",info.getId());
                        startActivity(intent);
                    }else if(holder.mTvLookPrice.getVisibility()==View.VISIBLE&&holder.mTvLookPrice.getText().equals("点击查看价格")){
                        if (TextUtils.isEmpty(App.uuid)){
                            showToast("请先登录");
                            relogin(getActivity());
                        }else
                        getIsSmrz(holder.mInfoPrice, holder.mTvLookPrice);
                    }

                }
            });


            return convertView;
        }

        private  String getuserType(String type){
            String str = "";
            if(type.equals("1")){
                str = "猪场";
            }else if(type.equals("2")){
                str = "屠宰场";
            }else if(type.equals("3")){
                str = "经纪人";
            }else{
                str = "网络";
            }
            return str;
        }
        public void addInfoItem(QuotedPrice info) {
            childJson.add(info);
        }

        public class ViewHolder {
            TextView mInfoTitle;//生猪类型
            TextView mInfoAddress;//地址
            TextView mInfoTime;//时间
            TextView mInfoType;//角色
            TextView mInfoPrice;//价格
            TextView mInfoCount;//数量
            TextView mInfoisZd;
            LinearLayout mLlItem;
            TextView mTvLookPrice;//查看价格
            RelativeLayout mRlLookPrice;
        }
    }
    /**
     * 点击空白区域隐藏键盘.
     */
    public static void HideKeyboard(View v)
    {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );

        }
    }


}
