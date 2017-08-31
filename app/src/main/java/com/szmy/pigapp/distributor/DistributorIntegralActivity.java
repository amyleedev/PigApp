package com.szmy.pigapp.distributor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.szmy.pigapp.activity.ActivityScanResult;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.activity.ChoseProvinceCityActivity;
import com.szmy.pigapp.entity.ProFit;
import com.szmy.pigapp.tixian.IntegraltocashActivity;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.MyshopGridView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的收益
 */
public class DistributorIntegralActivity extends BaseActivity {
    private SwipeRefreshLayout mSrlData;
    private ListView mLv;
    private DistributorShouyiAdapter mAdapter;
    private int mPage;
    private static final int PAGE_SIZE = 20;
    private List<ProFit> mList;
    private LinearLayout mLlMenu;
    private LinearLayout mLlExchange;
    private FrameLayout mFlHover1;
    // private LinearLayout mLlChoose;
    private FrameLayout mFlHover2;
    private View mFooterView;
    private LinearLayout mLlFooterLoading;
    private TextView mTvFooterResult;
    private boolean mIsLoadAll = false;
    private TextView mTvBolting;
    private LinearLayout mLlLoading;
    private LinearLayout mLlLoadFailure;
    public static final int LOCATION_ID = 29;
    public LocationService locationService;
    public Vibrator mVibrator;
    public static double x = 0;
    public static double y = 0;
    private String city = "", province = "";
    private final int TYPE_ID = 22;
    public static final int REFRESH = 27;
    private boolean mIsLoading;
    private LinearLayout mLlBack;
    private TextView mTvTotlePoint;
    private TextView mTvTodayPoint;
    private TextView mTvMonthPoint;
    private TextView mBtnHelp;
    private LinearLayout mLlWthdrawals;
    private LinearLayout mLlSousuo;
    private TextView mTvShouqi;
    private EditText mEtName;
    private TextView mTvStart;
    private TextView mTvEnd;
    private Button mBtnSousuo;
    private Button mTvMyShop;
    private TextView mBtnStartClear, mBtnEndClear;
    private TextView mTvSousuo;
    private RelativeLayout mRlMouth;
    private LinearLayout mLlFxsName;
    private LinearLayout mLlAddress;
    private LinearLayout mLlXiadanren;
    private TextView mTvAddress;
    private String[] cityItem;
    private String scryfxtype = "";
    private EditText mEtFxsName;
    int mSingleChoiceID = 0;
    private TextView mTvDayTip, mTvMonthTip, mTvTotalTip;
    private LinearLayout mLlLayout1,mLlLayout2;
    private MyshopGridView myshopGridView;
    private MyAdapter myAdapter;
    private List<Map<String, String>> list = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshopsy);
        ((TextView) findViewById(R.id.def_head_tv)).setText("我的店铺");
        initView();
    }

    private void initView() {
        mLlLayout1 = (LinearLayout) findViewById(R.id.layout1);
        mLlLayout2 = (LinearLayout) findViewById(R.id.layout2);
        mList = new ArrayList<ProFit>();
        mLlMenu = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.header_shoptop_view, null);
        mLlMenu.setClickable(false);
        mLlSousuo = (LinearLayout) mLlMenu.findViewById(R.id.ll_sousuo);
        mTvStart = (TextView) mLlMenu.findViewById(R.id.tv_start_date);
        mTvStart.setOnClickListener(mClickListener);
        mTvEnd = (TextView) mLlMenu.findViewById(R.id.tv_end_date);
        mTvEnd.setOnClickListener(mClickListener);
        mBtnStartClear = (TextView) mLlMenu.findViewById(R.id.start_clear);
        mBtnEndClear = (TextView) mLlMenu.findViewById(R.id.end_clear);
        mBtnStartClear.setOnClickListener(mClickListener);
        mBtnEndClear.setOnClickListener(mClickListener);
        mEtName = (EditText) mLlMenu.findViewById(R.id.et_name);
        mBtnSousuo = (Button) mLlMenu.findViewById(R.id.btn_statictics);
        mBtnSousuo.setOnClickListener(mClickListener);
        mTvShouqi = (TextView) mLlMenu.findViewById(R.id.btn_shouqi);
        mTvShouqi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLlSousuo.setVisibility(View.GONE);
            }
        });
        mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
        mLlBack.setOnClickListener(mClickListener);
        mTvTotlePoint = (TextView) mLlMenu.findViewById(R.id.totlepoint);
        mTvTodayPoint = (TextView) mLlMenu.findViewById(R.id.tv_today);
        mRlMouth = (RelativeLayout) mLlMenu.findViewById(R.id.rl_mouth);
        mLlFxsName = (LinearLayout) mLlMenu.findViewById(R.id.ll_fxsname);
        mLlAddress = (LinearLayout) mLlMenu.findViewById(R.id.ll_scaddress);
        mTvAddress = (TextView) mLlMenu.findViewById(R.id.fzquyu);
        mTvAddress.setOnClickListener(mClickListener);
        mTvMonthPoint = (TextView) mLlMenu.findViewById(R.id.mouthtotlepoint);
        mEtFxsName = (EditText) mLlFxsName.findViewById(R.id.et_fxsname);
        mTvDayTip = (TextView) mLlMenu.findViewById(R.id.tv1);
        mTvMonthTip = (TextView) mLlMenu.findViewById(R.id.tv3);
        mTvTotalTip = (TextView) mLlMenu.findViewById(R.id.tv2);
        mLlXiadanren = (LinearLayout) mLlMenu.findViewById(R.id.xiadanren);
        mBtnHelp = (TextView) findViewById(R.id.help_btn);
        mBtnHelp.setText("我的\n商城");
        if (getIntent().hasExtra("type")) {
            mBtnHelp.setVisibility(View.GONE);
            scryfxtype = getIntent().getStringExtra("type");
            mRlMouth.setVisibility(View.VISIBLE);
            mLlAddress.setVisibility(View.VISIBLE);
            if (getIntent().getStringExtra("type").equals("1")) {
                ((TextView) findViewById(R.id.def_head_tv)).setText("我的分销提成");
                if (getIntent().hasExtra("cityList")) {
                    List<String> list = getIntent().getStringArrayListExtra("cityList");
                    cityItem = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        cityItem[i] = list.get(i);
                    }
                }
            } else if (getIntent().getStringExtra("type").equals("2")) {
                mLlFxsName.setVisibility(View.VISIBLE);
                mLlXiadanren.setVisibility(View.GONE);
                mTvDayTip.setText("今日市场提成");
                mTvMonthTip.setText("当月市场提成");
                mTvTotalTip.setText("总提成(积分)");
                ((TextView) findViewById(R.id.def_head_tv)).setText("市场分销提成");
            }
        }
        mFlHover2 = (FrameLayout) findViewById(R.id.fl_hover2);
        mFooterView = LayoutInflater.from(this).inflate(R.layout.footer_view,
                null);
        mLlFooterLoading = (LinearLayout) mFooterView
                .findViewById(R.id.ll_footer_loading);
        mTvFooterResult = (TextView) mFooterView
                .findViewById(R.id.tv_footer_result);
        mSrlData = (SwipeRefreshLayout) findViewById(R.id.srl_data);
        mSrlData.setOnRefreshListener(mRefreshListener);
        mSrlData.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mLv = (ListView) findViewById(R.id.lv);
        mLv.setOnScrollListener(mOnScrollListener);
        mLv.setOnItemClickListener(mItemClickListener);
        mBtnHelp.setOnClickListener(mClickListener);
        mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
        mLlLoadFailure = (LinearLayout) findViewById(R.id.ll_load_failure);
        mLlLoadFailure.setOnClickListener(mClickListener);
//        mTvMyShop = (Button) findViewById(R.id.tv_szmyshop);
//        mTvMyShop.setOnClickListener(mClickListener);
        mTvSousuo = (TextView) mLlMenu.findViewById(R.id.sousuo);
        mTvSousuo.setOnClickListener(mClickListener);
        myshopGridView  = (MyshopGridView) findViewById(R.id.gridview);
        myshopGridView.setOnItemClickListener(new ItemClickListener());
    }

    private void load() {
        reload();
        mLlLoading.setVisibility(View.VISIBLE);
        mSrlData.setVisibility(View.GONE);
        mLlLoadFailure.setVisibility(View.GONE);
    }

    private void reload() {
        mPage = 1;
        mIsLoadAll = false;
        loadPoint();
        loadData();
    }

    private void loadData() {
        mIsLoading = true;
        RequestParams params = new RequestParams();
        params.addBodyParameter("pager.offset", String.valueOf(mPage));
        params.addBodyParameter("pager.pageSize", String.valueOf(PAGE_SIZE));
        params.addBodyParameter("e.presentee", mEtName.getText().toString());
        params.addBodyParameter("e.startDate", mTvStart.getText().toString());
        params.addBodyParameter("e.endDate", mTvEnd.getText().toString());
        if (!scryfxtype.equals("")) {
            params.addBodyParameter("e.type", "4");
            params.addBodyParameter("e.province", province);
            params.addBodyParameter("e.city", city);
            params.addBodyParameter("e.account", mEtFxsName.getText().toString());
        }
        params.addBodyParameter("e.source", "1");
        params.addBodyParameter("uuid", App.uuid);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.INTEGRAL_DISTRIBUTOE_URL,
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
                        mIsLoading = false;
                        Log.i("result", "responseInfo.result = "
                                + responseInfo.result);
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
//		if (!TextUtils.isEmpty(result)) {
//			System.out.println("selected_order_list:" + result);
//			try {
//				JSONObject jsonresult = new JSONObject(result);
//				if (jsonresult.optString("success").equals("1")) {
//					List<ProFit> childJsonItem = new ArrayList<ProFit>();
//					JSONArray jArrData = jsonresult.getJSONArray("list");
//					for (int i = 0; i < jArrData.length(); i++) {
//						JSONObject jobj2 = jArrData.optJSONObject(i);
//						GsonBuilder gsonb = new GsonBuilder();
//						Gson gson = gsonb.create();
//						ProFit mInfoEntry = gson.fromJson(jobj2.toString(),
//								ProFit.class);
//						childJsonItem.add(mInfoEntry);
//					}
//				}
//			} catch (Exception e) {
//			}
//		}
        JSONObject jsonresult;
        try {
            jsonresult = new JSONObject(result);
            if (jsonresult.get("success").toString().equals("1")) {
                List<ProFit> childJsonItem = new ArrayList<ProFit>();
                JSONArray jArrData = jsonresult.getJSONArray("list");
                if (jArrData.length()==0){
                    mLlLayout1.setVisibility(View.GONE);
                    mLlLayout2.setVisibility(View.VISIBLE);
                    loadProducts();
                    return;
                }
                for (int i = 0; i < jArrData.length(); i++) {
                    JSONObject jobj2 = jArrData.optJSONObject(i);
                    GsonBuilder gsonb = new GsonBuilder();
                    Gson gson = gsonb.create();
                    ProFit mInfoEntry = gson.fromJson(jobj2.toString(),
                            ProFit.class);
                    childJsonItem.add(mInfoEntry);
                }
                if (mLlLoading.getVisibility() == View.VISIBLE) {
                    mLlLoading.setVisibility(View.GONE);
                    mSrlData.setVisibility(View.VISIBLE);
                } else {
                    mSrlData.setRefreshing(false);
                }
                if (mPage == 1) {
                    if (mList.size() >= 0) {
                        mList.clear();
                        mLv.removeFooterView(mFooterView);
                        mLv.removeHeaderView(mLlMenu);
                    }
                    mLv.addFooterView(mFooterView);
                    mLv.addHeaderView(mLlMenu, null, false);
                    mList.addAll(childJsonItem);
                    mAdapter = new DistributorShouyiAdapter(DistributorIntegralActivity.this, mList, scryfxtype);
                    mLv.setAdapter(mAdapter);
                    if (childJsonItem.size() == 0) {
                        mLlFooterLoading.setVisibility(View.GONE);
                        mTvFooterResult.setVisibility(View.VISIBLE);
                        mTvFooterResult.setText("查询结果为空");
                        mIsLoadAll = true;
                    } else if (childJsonItem.size() < 20) {
                        mLlFooterLoading.setVisibility(View.GONE);
                        mTvFooterResult.setVisibility(View.VISIBLE);
                        mTvFooterResult.setText("已加载全部");
                        mIsLoadAll = true;
                    } else {
                        mLlFooterLoading.setVisibility(View.VISIBLE);
                        mTvFooterResult.setVisibility(View.GONE);
                    }
                } else {
                    mList.addAll(childJsonItem);
                    mAdapter = new DistributorShouyiAdapter(this, mList, scryfxtype);
                    mLv.requestLayout();
                    mAdapter.notifyDataSetChanged();
                    if (childJsonItem.size() < 20) {
                        mLlFooterLoading.setVisibility(View.GONE);
                        mTvFooterResult.setVisibility(View.VISIBLE);
                        mTvFooterResult.setText("已加载全部");
                        mIsLoadAll = true;
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
            reload();
        }
    };
    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_exchange:
                    Intent intent = new Intent(DistributorIntegralActivity.this,
                            ActivityScanResult.class);
                    intent.putExtra("type", "integral");
                    intent.putExtra(
                            "url",
                            UrlEntry.INTEGRAL_SHOP_URL
                                    + (TextUtils
                                    .isEmpty(getUserUUid(DistributorIntegralActivity.this)) ? "1"
                                    : getUserUUid(DistributorIntegralActivity.this)));
                    startActivity(intent);
                    break;
                case R.id.def_head_back:
                    finish();
                    break;
                case R.id.ll_load_failure:
                    load();
                    break;
                case R.id.help_btn:
                    intent = new Intent(DistributorIntegralActivity.this, ActivityScanResult.class);
                    intent.putExtra("type", "index");
                    intent.putExtra(
                            "url",
                            UrlEntry.YOUXUAN_SZMY_URL
                                    + (TextUtils
                                    .isEmpty(getUserUUid(DistributorIntegralActivity.this)) ? "1"
                                    : getUserUUid(DistributorIntegralActivity.this)));
                    startActivity(intent);
//				mLlSousuo.setVisibility(View.VISIBLE);
//				startActivity(new Intent(IntegralActivity.this,
//						ActivityIntegralProtocol.class));
//				Intent intent1 = new Intent(DistributorIntegralActivity.this,
//						ActivityScanResult.class);
//				intent1.putExtra("url", UrlEntry.JF_URL);
//				startActivity(intent1);
                    break;
                case R.id.sousuo:
                    if (mLlSousuo.getVisibility() == View.VISIBLE) {
                        mLlSousuo.setVisibility(View.GONE);
                        Drawable drawable = getResources().getDrawable(R.drawable.dakai_ic);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                        mTvSousuo.setCompoundDrawables(null, null, drawable, null);//画在右边
                    } else {
                        mLlSousuo.setVisibility(View.VISIBLE);
                        Drawable drawable = getResources().getDrawable(R.drawable.shouqi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                        mTvSousuo.setCompoundDrawables(null, null, drawable, null);//画在右边
                    }
                    break;
                case R.id.ll_wthdrawals:
//				 startActivity(new Intent(IntegralActivity.this,
//				 DevelopingActivity.class));
                    Intent intentwt = new Intent(DistributorIntegralActivity.this, IntegraltocashActivity.class);
                    if (mTvTotlePoint.getText().toString().equals("暂无")) {
                        intentwt.putExtra("point", 0);
                    } else
                        intentwt.putExtra("point", Integer.parseInt(mTvTotlePoint.getText().toString()));
                    startActivityForResult(intentwt, 0);
                    break;
                case R.id.btn_statictics://搜索
                    reload();
                    break;
                case R.id.tv_start_date:
                    showDateDialog(true);
                    break;
                case R.id.tv_end_date:
                    showDateDialog(false);
                    break;
                case R.id.tv_szmyshop:
                    intent = new Intent(DistributorIntegralActivity.this, ActivityScanResult.class);
                    intent.putExtra("type", "index");
                    intent.putExtra(
                            "url",
                            UrlEntry.MOBILE_PHONE_URL
                                    + (TextUtils
                                    .isEmpty(getUserUUid(DistributorIntegralActivity.this)) ? "1"
                                    : getUserUUid(DistributorIntegralActivity.this)));
                    startActivity(intent);
                    break;
                case R.id.start_clear:
                    if (!TextUtils.isEmpty(mTvStart.getText().toString())) {
                        mTvStart.setText("");
                    }
                    break;
                case R.id.end_clear:
                    if (!TextUtils.isEmpty(mTvEnd.getText().toString())) {
                        mTvEnd.setText("");
                    }
                    break;
                case R.id.fzquyu:

                    if (cityItem != null && cityItem.length != 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                DistributorIntegralActivity.this);
                        mSingleChoiceID = 0;
                        builder.setTitle("请选择");
                        builder.setSingleChoiceItems(cityItem, 0,
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
                                        mTvAddress.setText(cityItem[mSingleChoiceID]);
                                        province = mTvAddress.getText().toString().split(" ")[0];
                                        city = mTvAddress.getText().toString().split(" ")[1];
                                    }
                                });
                        builder.setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {

                                    }
                                });
                        builder.create().show();

                    } else {
                        intent = new Intent(DistributorIntegralActivity.this,
                                ChoseProvinceCityActivity.class);
                        intent.putExtra("add", "add");
                        startActivityForResult(intent, 22);

                    }
                    break;
            }
        }
    };

    public String getUserUUid(Context context) {
        SharedPreferences sp = context.getSharedPreferences("USERINFO", 0);
        if (App.mUserInfo == null) {
            App.mUserInfo = FileUtil.readUser(context);
        }
        App.setDataInfo(App.mUserInfo);
        if (!TextUtils.isEmpty(App.uuid)) {
            return App.uuid;
        }
        return "";
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int witch,
                                long arg3) {
        }
    };

    private OnScrollListener mOnScrollListener = new OnScrollListener() {

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem > 0) {
            } else {
            }
            if (firstVisibleItem + visibleItemCount == totalItemCount) {
                if (mIsLoading)
                    return;
                if (mIsLoadAll)
                    return;
                mPage += 1;
                if (mPage == 1) loadPoint();
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
            case 22:
                if (data == null)
                    return;
                Bundle bundle = data.getExtras();
                province = bundle.getString("province");
                city = bundle.getString("city");
                mTvAddress.setText(province + " " + city);
                if (province.equals("不限")) {
                    province = "";
                    city = "";
                }
                break;
            case REFRESH:
                load();
                break;
        }
    }

    public void onResume() {
        super.onResume();
//		reload();
        MobclickAgent.onPageStart("MainFragment"); // 统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainFragment");
    }

    public void loadPoint() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        if (!scryfxtype.equals("")) {
            params.addBodyParameter("e.province", province);
            params.addBodyParameter("e.city", city);
            params.addBodyParameter("e.account", mEtFxsName.getText().toString());
            params.addBodyParameter("e.type", "4");
        } else {
            params.addBodyParameter("e.source", "1");
        }
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.TOTAL_INTEGRAL_DISTRIBUTOE_URL,
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
                        String result = responseInfo.result;
                        System.out.println("收益：" + result);
                        try {
                            JSONObject json = new JSONObject(result);
                            if (json.optString("success").equals("1")) {

                                if (!json.optString("total").toString()
                                        .equals("0")) {
                                    mTvTotlePoint.setText(json.optString(
                                            "total").toString());
                                } else {
                                    mTvTotlePoint.setText("暂无");
                                }
                                if (!json.optString("today")
                                        .toString().equals("0")) {
                                    mTvTodayPoint.setText(json.optString(
                                            "today").toString());
                                } else {
                                    mTvTodayPoint.setText("暂无");
                                }
                                if (!json.optString("month")
                                        .toString().equals("0")) {
                                    mTvMonthPoint.setText(json.optString(
                                            "month").toString());
                                } else {
                                    mTvMonthPoint.setText("暂无");
                                }
                            } else {
                                successType(json.optString("success"), "获取失败！");
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

    private void showDateDialog(final boolean isStart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(DistributorIntegralActivity.this,
                R.layout.dialog_date, null);
        final TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        if (isStart)
            tvTitle.setText("请选择开始日期");
        else
            tvTitle.setText("请选择结束日期");
        final DatePicker datePicker = (DatePicker) view
                .findViewById(R.id.date_picker);
        builder.setView(view);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), null);
        if (isStart) {
            String date = mTvStart.getText().toString();
            String[] dates = date.split("-");
            if (TextUtils.isEmpty(date) || dates.length != 3) {
                datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH), null);
            } else {
                datePicker.init(Integer.parseInt(dates[0]), (Integer.parseInt(dates[1]) - 1), Integer.parseInt(dates[2]), null);
            }
        } else {
            String date = mTvEnd.getText().toString();
            String[] dates = date.split("-");
            if (TextUtils.isEmpty(date) || dates.length != 3) {
                datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH), null);
            } else {
                datePicker.init(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]), null);
            }
        }
        builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                datePicker.clearFocus();
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),
                        datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                if (isStart)
                    mTvStart.setText(sb.toString());
                else
                    mTvEnd.setText(sb.toString());
                dialog.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    public class MyAdapter extends BaseAdapter {
        private List<Map<String, String>> list = null;
        public LayoutInflater inflater = null;

        public MyAdapter(List list) {
            this.list = list;
            inflater = getLayoutInflater();
            // TODO Auto-generated constructor stub
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UseView useView = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_wdtjsp, null);
                useView = new UseView();

                useView.img = (ImageView) convertView.findViewById(R.id.imageview);
                useView.name = (TextView) convertView.findViewById(R.id.name);
                useView.price = (TextView) convertView.findViewById(R.id.price);
                useView.num = (TextView) convertView.findViewById(R.id.num);

                convertView.setTag(useView);
            } else {
                useView = (UseView) convertView.getTag();
            }
            Map<String,String> map = list.get(position);
            if (!TextUtils.isEmpty(map.get("img"))) {
                com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                        .displayImage(UrlEntry.ip + map.get("img"),
                                useView.img, AppStaticUtil.getOptions());
            } else {
                useView.img.setImageResource(R.drawable.default_title);
            }
            useView.name.setText(map.get("name"));
            useView.price.setText(map.get("price"));
            useView.num.setText("销量 "+map.get("num"));
            return convertView;
        }
    }

    public class UseView {
        private ImageView img;
        private TextView name;
        private TextView price;
        private TextView num;

    }
    /*
       * 给每个item设置事件监听；
       */
    class ItemClickListener implements OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int id, long location) {

            HashMap<String, String> item=(HashMap<String, String>) arg0.getItemAtPosition(id);
            Intent intent = new Intent(DistributorIntegralActivity.this,
                    ActivityScanResult.class);
            intent.putExtra(
                    "url",
                    UrlEntry.TO_PRODUCT_DETAIL_URL+ item.get("id")+".html?uuid=" + (TextUtils.isEmpty(getUserUUid(DistributorIntegralActivity.this)) ? "1": getUserUUid(DistributorIntegralActivity.this)));
            startActivity(intent);
        }
    }
    private void loadProducts() {
       showDialog();
        RequestParams params = new RequestParams();
//        params.addBodyParameter("uuid", App.uuid);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_MYYXLIST_URL,
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
                        Log.i("result", "responseInfo.result = "
                                + responseInfo.result);
                        String result = responseInfo.result;
                        System.out.println("商品：" + result);
                        JSONObject json = null;
                        try {
                            json = new JSONObject(result);
                            if (json.optString("success").equals("1")) {
                                list.clear();
                                JSONArray jArrData = json.getJSONArray("list");
                                for (int i = 0; i < jArrData.length(); i++) {
                                    JSONObject jobj2 = jArrData.optJSONObject(i);
                                    Map map = new HashMap<String, Object>();
                                    map.put("id",jobj2.optString("id"));
                                    map.put("img", jobj2.optString("picture"));
                                    map.put("name", jobj2.optString("name"));
                                    map.put("price", jobj2.optString("nowPrice"));
                                    map.put("num", jobj2.optString("sellcount"));
                                    list.add(map);
                                }
                                if (list.size()>0){
                                    myAdapter = new MyAdapter(list);
                                    myshopGridView.setAdapter(myAdapter);
                                }else{
                                    myshopGridView.setVisibility(View.GONE);
                                }
                            } else {
                                successType(json.optString("success"), "获取失败！");
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
}
