package com.szmy.pigapp.activity;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.RippleLayout;
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
import com.szmy.pigapp.adapter.BannerAdapter;
import com.szmy.pigapp.agent.AgentRenZhengActivity;
import com.szmy.pigapp.entity.Banner;
import com.szmy.pigapp.entity.Ecaluate;
import com.szmy.pigapp.entity.InfoEntry;
import com.szmy.pigapp.liulanliang.LiuLanListActivity;
import com.szmy.pigapp.tixian.ActivityWithdrawals;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppManager;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.MD5;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.utils.ZmtSignBean;
import com.szmy.pigapp.widget.ButtonFlat;
import com.szmy.pigapp.widget.CircleFlowIndicator;
import com.szmy.pigapp.widget.MyScrollView;
import com.szmy.pigapp.widget.NodeProgressBar;
import com.szmy.pigapp.widget.OrderNumDialog;
import com.szmy.pigapp.widget.PayDialog;
import com.szmy.pigapp.widget.ViewFlow;
import com.szmy.pigapp.widget.XiaDanPayDialog;
import com.szmy.pigapp.widget.YanzhengmaDialog;
import com.szmy.pigapp.zheshang.ElectronicAccounts;
import com.szmy.pigapp.zheshang.NewAgreementsigningActivity;
import com.szmy.pigapp.zheshang.NewElectronicAccountsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 收购
 *
 * @author qing
 */
public class InfoBuyDetailActivity extends BaseActivity implements
        OnClickListener {
    private TextView tv_title, tv_time, tv_status, tv_address, tv_shijian,
            tv_count, tv_weight, tv_remark, tv_price, tv_pricetx,
            tv_finalprice, tv_pigtype, tv_pigcolor, tv_buyorderphone,
            tv_buyordername, tv_builder, btn_tellphone;
    private Button order_btn, qxorder_btn;
    private SharedPreferences sp;
    private ImageButton btn_add;
    private ArrayList<String> list = new ArrayList<String>();
    private BitmapUtils bitmapUtils;
    private String id, fabuuserid, type, selluserid;
    private InfoEntry infoEntry;
    private LinearLayout purchaserlayout;
    RelativeLayout chulan_layout;
    private RelativeLayout weight_layout, finalpricelayout;
    private static final int PICK_PICTURE = 02;
    private ArrayList<String> biglist = new ArrayList<String>();// 大图集合
    private int status = 0;
    private String price = "", billnum = "", totlprice = "";
    private int mSingleChoiceID = 0;
    private int mSingleChoiceID2 = 0;
    private String[] mItems = {"立即付款", "面对面付款"};
    private Button mEcaluateBtn;
    public static int INSERT_ECALUATE_ID = 11;
    private TextView tv_ecaluate;
    private LinearLayout ll_ecaluate, ll_layout;
    private TextView tv_coutent;
    private RatingBar app_ratingbar;
    private TextView tv_date;
    private TextView tv_nodata;
    private TextView tv_delivery;
    private ButtonFlat mBtnCall;
    private ButtonFlat mBtnCallBuyer;
    private ImageView mIvCover;
    private boolean isChange = false;
    private Button mCareBtn;
    private String caretype = "0";

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
    private String pwd = "";
    private LinearLayout mLlEdit;
    private LinearLayout mBtnDel;
    private LinearLayout mBtnEdit;
    private String xdrxxf = "";
    private LinearLayout mLlXxf;
    private TextView mTvXdr;
    private ButtonFlat mBtnXxfXieYi;
    private RippleLayout mLlLiulan;
    private LinearLayout mRlOrderId;
    private TextView mTvOrderId;
    private NodeProgressBar ssl;

    private ImageView contentImageView;
    private ImageView overImageView;
    private float h, w;
    private LinearLayout mLlTImeLine;
    private TextView mTvDelaytip;
    private TextView mTvTime;
    private int recLen = 0;
    private boolean isTimeRun = false;
    private String maxPrice = "10";
    private String minPrice = "30";
    private String token = "";
    private String mobileSerial = "";
    private   Class mClass = null;
    private TextView mTvTimetip;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.info_detail_activity);
        ((TextView) findViewById(R.id.def_head_tv)).setText("订单详情");

        ((LinearLayout) findViewById(R.id.def_head_back))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (isChange) {
                            setResult(27);
                        }
                        finish();
                    }
                });
        mCareBtn = (Button) findViewById(R.id.radiocareuser);
        setColorBtn(caretype);
        mCareBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isLogin(getApplicationContext())) {
                    showToast("请先登录！");
                    return;
                }
                if (caretype.equals("1")) {

                    final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "确定取消关注该发布者？");
                    dialog.addCancelButton("取消");
                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
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
                    final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "确定关注该发布者？");
                    dialog.addCancelButton("取消");
                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
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

            }
        });

        SharedPreferences sp = getSharedPreferences(App.USERINFO, 0);
        if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
            App.mUserInfo = FileUtil.readUser(InfoBuyDetailActivity.this);
            App.setDataInfo(App.mUserInfo);
        }
        id = getIntent().getStringExtra("id");
        fabuuserid = getIntent().getStringExtra("userid");
        type = getIntent().getStringExtra("type");

        initView();
//
        downLoadInfo(id);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void initView() {
//        ssl=(NodeProgressBar) findViewById(R.id.ssl);
//        ssl.setProgressAndIndex(20);
        mTvTimetip = (TextView) findViewById(R.id.tv_sgtime);
        mTvTimetip.setText("收购时间：");

        contentImageView = (ImageView) findViewById(R.id.content);
        overImageView = (ImageView) findViewById(R.id.over);
        mLlTImeLine = (LinearLayout) findViewById(R.id.ll_timeline);
        mTvTime = (TextView) findViewById(R.id.daojishi);
        mRlOrderId = (LinearLayout) findViewById(R.id.layout_orderid);
        mTvOrderId = (TextView) findViewById(R.id.tv_orderid);
        mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
        mLlLoadFailure = (LinearLayout) findViewById(R.id.ll_load_failure);
        mLlLoadFailure.setOnClickListener(this);
        mSv = (MyScrollView) findViewById(R.id.sv);
        mFlBanner = (FrameLayout) findViewById(R.id.fl_banner);
        mVf = (ViewFlow) findViewById(R.id.vf);
        mCfi = (CircleFlowIndicator) findViewById(R.id.cfi);
        mSv.setView(mVf);
        width = getSize();
        bitmapUtils = new BitmapUtils(getApplicationContext());
        btn_add = (ImageButton) findViewById(R.id.add_btn2); // 编辑/删除
        btn_add.setOnClickListener(this);
        mIvCover = (ImageView) findViewById(R.id.iv_cover);
        tv_title = (TextView) findViewById(R.id.title_tv);
        tv_builder = (TextView) findViewById(R.id.tv_builder);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_address = (TextView) findViewById(R.id.tv_address);
        chulan_layout = (RelativeLayout) findViewById(R.id.chulan_layout1);
        weight_layout = (RelativeLayout) findViewById(R.id.weight_layout1);
        tv_shijian = (TextView) findViewById(R.id.tv_shijian);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_remark = (TextView) findViewById(R.id.tv_remark);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_pricetx = (TextView) findViewById(R.id.pricetv);
        btn_tellphone = (TextView) findViewById(R.id.phont_btn);
        order_btn = (Button) findViewById(R.id.order_btn);
        qxorder_btn = (Button) findViewById(R.id.qxorder_btn);
        finalpricelayout = (RelativeLayout) findViewById(R.id.finalpricelayout);
        tv_finalprice = (TextView) findViewById(R.id.tv_finalprice);
        tv_pigtype = (TextView) findViewById(R.id.tv_pigtype);
        purchaserlayout = (LinearLayout) findViewById(R.id.purchaserlayout);
        tv_buyorderphone = (TextView) findViewById(R.id.tv_buyorderphone);
        tv_buyordername = (TextView) findViewById(R.id.tv_buyordername);
        mBtnCall = (ButtonFlat) findViewById(R.id.btn_call);
        mBtnCall.setOnClickListener(this);
        mBtnCallBuyer = (ButtonFlat) findViewById(R.id.btn_call_buyer);
        mBtnCallBuyer.setOnClickListener(this);
        chulan_layout.setVisibility(View.GONE);
        tv_pigcolor = (TextView) findViewById(R.id.tv_pigcolor);
        mTvDelaytip = (TextView) findViewById(R.id.delaytext);
        // 评价
        mEcaluateBtn = (Button) findViewById(R.id.order_ecaluate);
        mEcaluateBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InfoBuyDetailActivity.this,
                        EcaluateActivity.class);
                Bundle bundle = new Bundle();
                // bundle.putSerializable("info", infoEntry);
                bundle.putString("orderID", infoEntry.getId());
                bundle.putString("orderType", "2");
                bundle.putString("productName", infoEntry.getTitle());
                if (isLoginSell()) {
                    bundle.putString("bpjrType", "2");
                    bundle.putString("bpjr", infoEntry.getUserName());
                } else if (isLoginBuy()) {
                    bundle.putString("bpjrType", "1");
                    bundle.putString("bpjr", infoEntry.getPurchaserName());
                }
                intent.putExtras(bundle);
                startActivityForResult(intent, INSERT_ECALUATE_ID);
            }
        });
        ll_ecaluate = (LinearLayout) findViewById(R.id.ll_ecaluate);
        tv_ecaluate = (TextView) findViewById(R.id.tv_ecaluate);
        tv_ecaluate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // loadEcaluate();

            }
        });

        tv_coutent = (TextView) findViewById(R.id.tv_coutent);
        app_ratingbar = (RatingBar) findViewById(R.id.app_ratingbar);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
        ll_layout = (LinearLayout) findViewById(R.id.ll_layout);
        tv_delivery = (TextView) findViewById(R.id.tv_delivery);
        mLlEdit = (LinearLayout) findViewById(R.id.editlayout);
        mBtnDel = (LinearLayout) findViewById(R.id.order_del);
        mBtnEdit = (LinearLayout) findViewById(R.id.order_toedit);
        mBtnDel.setOnClickListener(this);
        mBtnEdit.setOnClickListener(this);
        mLlXxf = (LinearLayout) findViewById(R.id.rl_xxf);
        mTvXdr = (TextView) findViewById(R.id.xxf_tv);
        mBtnXxfXieYi = (ButtonFlat) findViewById(R.id.btn_xieyi);
        mBtnXxfXieYi.setOnClickListener(this);
        mLlLiulan = (RippleLayout) findViewById(R.id.ll_liulan);
        mLlLiulan.setOnClickListener(this);
    }

    /**
     * 设置进度条
     */
    private float fl = 0;

    private void setTimeLine(String type) {

        if (type.equals("1")) {
            fl = 0.120f;
        } else if (type.equals("2") || type.equals("4")) {
            fl = 0.260f;
        } else if (type.equals("7")) {
            fl = 0.440f;
        } else if (type.equals("5")) {
            fl = 0.620f;
        } else if (type.equals("3")) {
            fl = 1;
        }
        /*
UI事件队列会按顺序处理事件。在setContentView()被调用后
事件队列中会包含一个要求重新layout的message
所以任何你post到队列中的东西都会在Layout发生变化后执行
*/
        contentImageView.post(new Runnable() {
            @Override
            public void run() {
                h = contentImageView.getHeight();
                w = contentImageView.getWidth();
//                Toast.makeText(InfoBuyDetailActivity.this, "h:" + h + "w:" + w, Toast.LENGTH_SHORT).show();
                ObjectAnimator.ofFloat(overImageView, "translationX", w * fl).setDuration(500).start();
            }
        });
    }

    public void setData(final InfoEntry info) {
        selluserid = info.getPurchaserID().toString();
        infoEntry = info;
        mTvDelaytip.setVisibility(View.GONE);

        if (isLoginSell() || isLoginBuy()) {

            if (info.getOrderStatus().equals("7")) { //2017-03-03只有为线上支付时显示进度条    2017-03-10修改订单状态为等待后台确认时显示进度条，其他状态判断支付类型
                mLlTImeLine.setVisibility(View.VISIBLE);
            } else {
                if (info.getPayType().equals("1")) {
                    mLlTImeLine.setVisibility(View.VISIBLE);
                } else {
                    mLlTImeLine.setVisibility(View.GONE);
                }
            }
            setTimeLine(info.getOrderStatus());
        }
        if (!TextUtils.isEmpty(info.getStartPrice())){
            minPrice = info.getStartPrice();
            maxPrice = info.getEndPrice();
        }
        if (!isLoginBuy()) {// 不是卖家登录,显示关注
            mCareBtn.setVisibility(View.GONE); //2017-03-10 修改 取消详情页关注
            mLlLiulan.setVisibility(View.GONE);

        } else {
            mLlLiulan.setVisibility(View.VISIBLE);
            mCareBtn.setVisibility(View.GONE);
        }

        //判断显示信息服务费
        if (isLoginBuy()) {
            if (App.usertype.equals("3") && info.getUserType().equals("3")) {
                if ((!TextUtils.isEmpty(infoEntry.getFbrxxf())) && (Double.parseDouble(infoEntry.getFbrxxf()) > 0)) {
                    mLlXxf.setVisibility(View.VISIBLE);
                    mTvXdr.setText(Html.fromHtml("： 已支付<font color = '#ff0000'>" + info.getFbrxxf() + "</font>元"));
                }
            }

        }
        if (isLoginSell()) {
            if (App.usertype.equals("1") && info.getUserType().equals("3")) {
                if ((!TextUtils.isEmpty(infoEntry.getXdrxxf())) && (Double.parseDouble(infoEntry.getXdrxxf()) > 0)) {
                    mLlXxf.setVisibility(View.VISIBLE);
                    mTvXdr.setText(Html
                            .fromHtml("： 已支付<font color = '#ff0000'>" + info.getXdrxxf() + "</font>元"));
                }
            }
        }

        caretype = info.getIsFriend();
        if (info.getIsFriend().equals("1")) {
            setColorBtn(caretype);
            mCareBtn.setText("已关注用户");
        } else {
            mCareBtn.setText("关注用户");
            setColorBtn(caretype);
        }
        String ordertype = info.getOrderType();
        if (ordertype.equals("1")) {
            ordertype = "出售";
        } else if (ordertype.equals("2")) {
            ordertype = "收购";
        }


        tv_title.setText("【" + ordertype + "】  " + info.getTitle().toString());
        tv_time.setText(info.getCreateTime().toString().substring(0, 16));
        if (info.getCompName().equals("")) {
            tv_builder.setText("发布人：" + info.getUserName()+"("+App.getTypeName(info.getUserType())+")");

        } else {
            tv_builder.setText("发布人：" + info.getCompName()+"("+App.getTypeName(info.getUserType())+")");

        }
        if (!info.getPigType().equals("")) {
            String name = "";
            switch (Integer.parseInt(info.getPigType())) {
                case 1:
                    name = "内三元";
                    break;
                case 2:
                    name = "外三元";
                    break;
                case 3:
                    name = "土杂猪";
                    break;
                case 4:
                    name = "肥猪";
                    break;
                case 5:
                    name = "仔猪";
                    break;
                case 6:
                    name = "种猪";
                    break;
            }
            tv_pigtype.setText(name);
        }
        status = Integer.parseInt(info.getOrderStatus().toString());
        tv_address.setText(info.getProvince() + info.getCity() + info.getArea()
                + "*****");
        String str = info.getPhone();
        if (info.getPhone().length() >= 11) {
            str = str.substring(0, 11);
            btn_tellphone.setText(str.substring(0,
                    str.length() - (str.substring(3)).length())
                    + "****" + str.substring(7));
        } else {

            btn_tellphone.setText(str.substring(0, str.length()) + "***");
            btn_tellphone.setEnabled(false);
            mBtnCall.setVisibility(View.GONE);
        }

        if (status == 1) {
            if (isLoginBuy()) {
                mLlEdit.setVisibility(View.VISIBLE);
            } else {
                mLlEdit.setVisibility(View.GONE);
            }
        } else {
            purchaserlayout.setVisibility(View.VISIBLE);
            mLlEdit.setVisibility(View.GONE);
            if (!isLoginBuy() && !isLoginSell()) {
                btn_tellphone.setEnabled(false);
                mBtnCall.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(info.getPurchaserName())) {
                    if (FileUtil.isNumeric(info.getPurchaserName())
                            && info.getPurchaserName().length() == 11) {
                        tv_buyordername
                                .setText(info
                                        .getPurchaserName()
                                        .substring(
                                                0,
                                                info.getPurchaserName()
                                                        .length()
                                                        - (info.getPurchaserName()
                                                        .substring(3))
                                                        .length())
                                        + "****"
                                        + info.getPurchaserName().substring(7));
                    } else {

                        tv_buyordername.setText(info.getPurchaserName()
                                .substring(0, 1) + "***");
                    }
                }

                if (TextUtils.isEmpty(info.getPurchaserPhone())) {
                    tv_buyorderphone.setEnabled(false);
                } else if (info.getPurchaserPhone().length() < 11) {
                    tv_buyorderphone.setText(info.getPurchaserPhone()
                            .substring(0, info.getPurchaserPhone().length())
                            + "***");
                } else {
                    mBtnCallBuyer.setVisibility(View.GONE);
                    tv_buyorderphone.setEnabled(false);
                    String strphone = info.getPurchaserPhone().substring(0, 11);
                    tv_buyorderphone.setText(strphone.substring(
                            0,
                            strphone.length()
                                    - (strphone.substring(3)).length())
                            + "****" + strphone.substring(7));
                }

            } else {
                btn_tellphone.setText(str);
                tv_address.setText(info.getProvince() + info.getCity()
                        + info.getArea() + info.getAddress());
                tv_buyordername.setText(info.getPurchaserName());
                tv_buyorderphone.setText(info.getPurchaserPhone());
            }

        }
        if (status != 1) {//判断订单状态不为1  且只有买方和卖方可查看订单号
            if (isLoginSell() || isLoginBuy()) {
                mRlOrderId.setVisibility(View.VISIBLE);
                mTvOrderId.setText("订单号：" + info.getId());
            }
        }
        switch (status) {
            case 1:// 未下单
                if (isLoginBuy()) {
                    order_btn.setVisibility(View.GONE);
                } else {
                    order_btn.setVisibility(View.VISIBLE);
                }
                order_btn.setText("下   单");
                qxorder_btn.setVisibility(View.GONE);
                if (info.getCheckStatus().equals("1")) {
                    tv_status.setText("[审核中]");
                } else if (info.getCheckStatus().equals("3")) {
                    tv_status.setText("[审核不通过]\n(点击查看原因)");

                    tv_status.setOnClickListener(this);
                } else
                    tv_status.setText("[未成交]");

                break;
            case 2:// 已下单

                if (isLoginBuy()) {
                    qxorder_btn.setVisibility(View.GONE);
                    order_btn.setVisibility(View.VISIBLE);
                    order_btn.setText("确认订单");
                } else if (isLoginSell()) {
                    order_btn.setVisibility(View.GONE);
                    qxorder_btn.setVisibility(View.VISIBLE);
                } else {
                    order_btn.setVisibility(View.GONE);
                    qxorder_btn.setVisibility(View.GONE);
                }

                tv_status.setText("[已下单]");

                break;
            case 3:// 交易成功
                order_btn.setVisibility(View.GONE);
                tv_status.setText("[交易成功]");
                ll_ecaluate.setVisibility(View.VISIBLE);
                if (isLoginBuy()) {
                    finalpricelayout.setVisibility(View.VISIBLE);
                    mEcaluateBtn.setVisibility(View.VISIBLE);
                }
                break;
            case 4:// 确认订单
                tv_status.setText("[已确认订单]");
                if (TextUtils.isEmpty(App.uuid)) {
                    qxorder_btn.setVisibility(View.GONE);
                    order_btn.setVisibility(View.GONE);
                } else {
                    if (isLoginBuy()) {
                        if (!TextUtils.isEmpty(info.getPayDeadline())){
                            if(info.getPurchaserType().equals("1")){
                                if (!TextUtils.isEmpty(infoEntry.getDiff())){

                                    recLen=Integer.parseInt(infoEntry.getDiff());
                                    if (recLen<0){
//                                        order_btn.setEnabled(false);
//                                      MyRunnable  runnable2 = new MyRunnable("8"); // orderStatus状态改为 订单已失效8
//                                        new Thread(runnable2).start();
                                    }else {
                                        mTvTime.setVisibility(View.VISIBLE);
                                        if(isTimeRun){
                                            isTimeRun = false;
                                            timehandler.removeCallbacks(timerunnable);
                                        }
                                        isTimeRun = true;
                                        timehandler.postDelayed(timerunnable, 1000);
                                    }
                                }

                            }
                        }
                        order_btn.setVisibility(View.VISIBLE);
                        order_btn.setText("付   款");
                        finalpricelayout.setVisibility(View.VISIBLE);
                    } else if (isLoginSell()) {
                        finalpricelayout.setVisibility(View.VISIBLE);
                        order_btn.setVisibility(View.GONE);
                        qxorder_btn.setVisibility(View.GONE);
                    } else {
                        order_btn.setVisibility(View.GONE);
                        qxorder_btn.setVisibility(View.GONE);
                    }
                }
                break;
            case 5:// 已确认支付
                order_btn.setVisibility(View.GONE);
                tv_status.setText("[已确认支付]");
                ll_ecaluate.setVisibility(View.VISIBLE);
                if (isLoginBuy()) {
                    finalpricelayout.setVisibility(View.VISIBLE);
                    mEcaluateBtn.setVisibility(View.GONE);
                }
//                if (isLoginBuy()) {
//                    finalpricelayout.setVisibility(View.VISIBLE);
//                }
//                if (isLoginSell()) {
//                    finalpricelayout.setVisibility(View.VISIBLE);
//                    order_btn.setVisibility(View.VISIBLE);
//                    order_btn.setText("发     货");
//
//                } else {
//                    qxorder_btn.setVisibility(View.GONE);
//                    order_btn.setVisibility(View.GONE);
//
//                }
//                tv_status.setText("[已支付]");
                break;
            case 7:
                order_btn.setVisibility(View.GONE);
                tv_status.setText("[等待后台确认]");
                ll_ecaluate.setVisibility(View.VISIBLE);
                if (isLoginBuy()) {
                    finalpricelayout.setVisibility(View.VISIBLE);
                    mEcaluateBtn.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(info.getPayDeadline())){
                        if(info.getPurchaserType().equals("1")){
                            if (!TextUtils.isEmpty(infoEntry.getDiff())){

                                recLen=Integer.parseInt(infoEntry.getDiff());
                                if (recLen<0){
//                                    order_btn.setEnabled(false);
//                                    MyRunnable  runnable2 = new MyRunnable("8"); // orderStatus状态改为 订单已失效8
//                                    new Thread(runnable2).start();
                                }else {
                                    mTvTime.setVisibility(View.VISIBLE);
                                   if(isTimeRun){
                                       isTimeRun = false;
                                       timehandler.removeCallbacks(timerunnable);
                                   }
                                    isTimeRun = true;
                                    timehandler.postDelayed(timerunnable, 1000);
                                }
                            }

                        }
                    }
                }
                break;
            case 8:// 交易成功
                order_btn.setVisibility(View.GONE);
                tv_status.setText("[订单已失效]");
                ll_ecaluate.setVisibility(View.VISIBLE);
                if(mTvTime.getVisibility()==View.VISIBLE){
                    mTvTime.setVisibility(View.GONE);
                }
//                if (isLoginBuy()) {
//                    finalpricelayout.setVisibility(View.VISIBLE);
//                    mEcaluateBtn.setVisibility(View.VISIBLE);
//                }
                break;
            default:
                break;

        }

        if (!TextUtils.isEmpty(info.getMarketingTime().toString()))
            tv_shijian.setText(info.getMarketingTime().toString()
                    .substring(0, 16));
        tv_count.setText(info.getNumber().toString());
        tv_weight.setText(info.getWeight().toString());
        tv_pigcolor.setText(info.getColor());
        tv_remark.setText(info.getRemark().toString());
        if (info.getPrice().equals("0")) {
            tv_price.setText("协商");
            tv_pricetx.setVisibility(View.GONE);
        } else
            tv_price.setText(info.getPrice().toString());
        tv_finalprice.setText(info.getFinalPrice().toString());
        tv_buyorderphone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isLogin(getApplicationContext())) {
                    showToast("请先登录！");
                    return;
                }
                if (App.authentication.equals("2")) {
                    dialog("确定拨打电话？", info.getPurchaserPhone().toLowerCase());
                } else {
                    final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "认证信息审核通过后才能拨打电话！");
                    dialog.addCancelButton("取消");
                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (App.usertype.equals("1")) { // 猪场
                                Intent zcintent = new Intent(
                                        InfoBuyDetailActivity.this,
                                        ZhuChangRenZhengActivity.class);
                                startActivity(zcintent);
                            } else if (App.usertype.equals("2")) {// 屠宰场
                                Intent zcintent = new Intent(
                                        InfoBuyDetailActivity.this,
                                        SlaughterhouseRenZhengActivity.class);
                                startActivity(zcintent);
                            } else if (App.usertype.equals("3")) {// 经纪人
                                Intent zcintent = new Intent(
                                        InfoBuyDetailActivity.this,
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
                }
            }
        });
        btn_tellphone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isLogin(getApplicationContext())) {
                    showToast("请先登录！");
                    return;
                }
                if (App.authentication.equals("2")) {
                    dialog("确定拨打电话？", info.getPhone().toLowerCase());
                } else {
                    final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "认证信息审核通过后才能拨打电话！");
                    dialog.addCancelButton("取消");
                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (App.usertype.equals("1")) { // 猪场
                                Intent zcintent = new Intent(
                                        InfoBuyDetailActivity.this,
                                        ZhuChangRenZhengActivity.class);
                                startActivity(zcintent);
                            } else if (App.usertype.equals("2")) {// 屠宰场
                                Intent zcintent = new Intent(
                                        InfoBuyDetailActivity.this,
                                        SlaughterhouseRenZhengActivity.class);
                                startActivity(zcintent);
                            } else if (App.usertype.equals("3")) {// 经纪人
                                Intent zcintent = new Intent(
                                        InfoBuyDetailActivity.this,
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
                }
            }
        });

        order_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                switch (status) {
                    case 1:// 未下单
                        if (isLogin(getApplicationContext())) {
                            showToast("请先登录！");
                        } else {
                            if ((!App.usertype.equals("1")) && (!App.usertype.equals("3"))) {
                                showDialog1(InfoBuyDetailActivity.this,
                                        "下单失败，只有猪场和经纪人才能对收购的订单进行操作。");
                                return;
                            } else {
                                getIsSmrz();
                            }

//                            if (App.CITY.equals("鹤壁市")){
//                                xiadanorder("");
//                            }else{
//                                if(Integer.parseInt(infoEntry.getNumber())<100){
//                                    showDialog1(InfoBuyDetailActivity.this,"下单失败，交易头数不能少于100头！");
//                                }else{
//                                    xiadanorder("100");
//                                }
//                            }
                        }

                        break;
                    case 2:// 已下单 买家确认订单
//                        if (App.usertype.equals("3") && (infoEntry.getUserType().equals("3"))) {
//                            showDialog();
//                            checkXxfInfo("queren");
//                        } else {
                            orderdialog("确认订单？", "2");//2017-2-27修改：取消确认订单提示
//                            MyRunnable  runnable2 = new MyRunnable("4"); //2017-05-02 确认订单判断银行卡信息
//                            new Thread(runnable2).start();
//                            sendDataByUUid("querendingdan");
//                        }

                        break;
                    case 3:// 已经交易成功
                        break;
                    case 4:// 买家已确认订单 买家支付价格

                        if (App.usertype.equals("3")) {//2017-03-03经纪人付款时给出提示

                            Builder builder = new Builder(
                                    InfoBuyDetailActivity.this);

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

                                            if (mSingleChoiceID == 1) {//面对面交易

                                                showbankdialog();

                                                // orderdialog("确认跳转到农业银行支付？", "5");
                                            } else { //打款到平台
//
                                                if (!TextUtils.isEmpty(infoEntry.getDiff())){

                                                    if ((App.usertype.equals("3")||App.usertype.equals("2"))&&infoEntry.getPurchaserType().equals("1")){
                                                        getPayOrderTime("xianshifukuan");

                                                    }
                                                }else
                                                getPayOrderTime("");
//                                                // 判断是否已签约(未签约提示到牧易商城签约,已签约判断是否有支付密码有则弹出输入支付密码框没有则提示跳转到新增支付密码页面)
//                                                checkType();

                                                // orderdialog("确认跳转到农业银行支付？", "5");
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
                        } else {
                            //2016年最后一次付款修改为线下支付打款到平台账号
//                        orderdialog("为保障交易双方合法权益，请先付款至平台托管（农行）账户\n\n" +
//                                "户名：河南神州牧易电子商务有限公司\n\n" +
//                                "账号：1643 1201 0400 13750\n" + "#13#", "6");//提示线下支付
                            //2017-2-24 添加支付时间段判断
                            if (!TextUtils.isEmpty(infoEntry.getDiff())){
                                if ((App.usertype.equals("3")||App.usertype.equals("2"))&&infoEntry.getPurchaserType().equals("1")){
//                            if((App.usertype.equals("3")&&infoEntry.getPurchaserType().equals("1"))||(App.usertype.equals("2")&&infoEntry.getPurchaserType().equals("1"))){
                                    getPayOrderTime("xianshifukuan");

                                }
                            }else
                            getPayOrderTime("");
                        }


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
        String sendtype = "";
        if (info.getSendType().equals("")) {
            sendtype = "无";
        } else {
            sendtype = info.getSendType();
        }
        tv_delivery.setText(sendtype);

    }

    private void xiadanorder(String numcon) {
        if (App.usertype.equals("1")) {
            OrderNumDialog dialog = new OrderNumDialog(
                    InfoBuyDetailActivity.this, "提示",
                    new OrderNumDialog.OnDialogListener() {

                        @Override
                        public void back(String price1,
                                         String totleprice, String num) {
                            price = price1;
                            totlprice = totleprice;
                            billnum = num;
                            System.out.println(billnum + "數量");
                            showDialog();
                            checkXxfInfo("xiadan");
                        }
                    }, tv_count.getText().toString(), numcon,minPrice,maxPrice);
            dialog.show();


        } else if (App.usertype.equals("3")) {
            OrderNumDialog dialog = new OrderNumDialog(
                    InfoBuyDetailActivity.this, "提示",
                    new OrderNumDialog.OnDialogListener() {

                        @Override
                        public void back(String price1,
                                         String totleprice, String num) {
                            price = price1;
                            totlprice = totleprice;
                            billnum = num;
                            MyRunnable runnable2 = new MyRunnable("2");
                            new Thread(runnable2).start();
//                            orderdialog("确认下单？", "1");
                        }
                    }, tv_count.getText().toString(), numcon,minPrice,maxPrice);
            dialog.show();
        } else {
            showDialog1(InfoBuyDetailActivity.this,
                    "下单失败，只有猪场和经纪人才能对收购的订单进行操作。");
            return;
        }
    }

    public void orderdialog(String message, final String type) {
        final Dialog dialog = new Dialog(this, "提示", message);
        dialog.addCancelButton("取消");
        if (type.equals("6")) {
            dialog.addAccepteButton("去支付");
        }
        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                showDialog();
                MyRunnable runnable2;
                switch (Integer.parseInt(type)) {
                    case 0: // 取消订单
                        order_btn.setEnabled(false);
                        MyRunnable runnable0 = new MyRunnable("1");
                        new Thread(runnable0).start();
                        break;
                    case 1:// 下单
                        order_btn.setEnabled(false);
                        runnable2 = new MyRunnable("2");
                        new Thread(runnable2).start();
                        break;

                    case 2:// 确认订单
                        order_btn.setEnabled(false);
                        runnable2 = new MyRunnable("4");
                        new Thread(runnable2).start();
                        break;
                    case 5:// 付款
                        new Thread(runnable).start();
                        break;
                    case 6://线下支付
                        order_btn.setEnabled(false);
                        runnable2 = new MyRunnable("7"); //点击已支付 orderStatus状态改为  等待后台确认7
                        new Thread(runnable2).start();
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
                order_btn.setEnabled(true);
            }
        });
        dialog.show();

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            order_btn.setEnabled(true);
            disDialog();
            Bundle data1 = msg.getData();
            String result = data1.getString("value");
            JSONObject jsonresult;
            switch (msg.what) {
                case 0:// 删除订单
                    if (!result.equals("")) {
                        try {
                            jsonresult = new JSONObject(result);
                            if (jsonresult.get("success").toString().equals("1")) {
                                isChange = true;
                                showToast("删除成功！");
                                setResult(27);
                                finish();
                            } else if (jsonresult.get("success").toString()
                                    .equals("0")) {
                                showToast("用户信息错误,请重新登录！");
                                relogin(getApplicationContext());
                            } else if (jsonresult.get("success").toString()
                                    .equals("3")) {
                                showToast("删除失败！");
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                    break;
                case 1:// 取消订单
                    if (!result.equals("")) {
                        try {
                            jsonresult = new JSONObject(result);
                            if (jsonresult.get("success").toString().equals("1")) {
                                isChange = true;
                                id = jsonresult.optString("id").toString();
                                showToast("订单取消成功！");
                                downLoadInfo(id);

                            } else if (jsonresult.get("success").toString()
                                    .equals("2")) {
                                showDialog1(InfoBuyDetailActivity.this, jsonresult
                                        .get("msg").toString());
                                downLoadInfo(id);

                            } else if (jsonresult.get("success").toString()
                                    .equals("0")) {
                                showToast("用户信息错误,请重新登录！");
                                relogin(getApplicationContext());
                            } else if (jsonresult.get("success").toString()
                                    .equals("3")) {
                                showToast("订单取消失败！");
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                    break;
                case 2:// 下单
                    if (!result.equals("")) {
                        try {
                            jsonresult = new JSONObject(result);
                            if (jsonresult.get("success").toString().equals("1")) {
                                id = jsonresult.optString("id").toString();
                                isChange = true;
                                if (jsonresult.optString("MSG").equals("1")) {
                                    showToast("该订单已被下单！");
                                } else {
                                    showDialog1(InfoBuyDetailActivity.this, "下单成功，等待对方确认订单！");
                                }
                                downLoadInfo(id);

                            } else {
                                parseRenZhenResult(jsonresult);
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                    break;
                case 3:
                    if (!result.equals("")) {
                        try {
                            jsonresult = new JSONObject(result);
                            if (jsonresult.get("success").toString().equals("1")) {
                                isChange = true;
                                showToast("已成交！");
                                downLoadInfo(id);

                            } else if (jsonresult.get("success").toString()
                                    .equals("0")) {
                                showToast("用户信息错误,请重新登录！");
                                relogin(getApplicationContext());
                            } else if (jsonresult.get("success").toString()
                                    .equals("3")) {
                                showToast("交易失败！");
                            }else if(jsonresult.optString("success").toString() .equals("8")){
                                showToast("操作失败，"+jsonresult.optString("msg").toString());
                                downLoadInfo(id);
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                    break;
                case 4:// 确认下单
                    if (!result.equals("")) {
                        try {
                            jsonresult = new JSONObject(result);
                            if (jsonresult.get("success").toString().equals("1")) {
                                isChange = true;

                                    showToast("已确认订单,请付款！");
//                                }
                                downLoadInfo(id);
                            } else if (jsonresult.get("success").toString()
                                    .equals("0")) {
                                showToast("用户信息错误,请重新登录！");
                                relogin(getApplicationContext());
                            } else if (jsonresult.get("success").toString()
                                    .equals("3")) {
                                showToast("确认订单失败！");
                            } else {
                                parseRenZhenResult(jsonresult);
                            }
                        } catch (JSONException e) {

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
                                isChange = true;
                                downLoadInfo(id);

                            } else if (jsonresult.get("success").toString()
                                    .equals("0")) {
                                showToast("用户信息错误,请重新登录！");
                                relogin(getApplicationContext());
                            } else if (jsonresult.get("success").toString()
                                    .equals("3")) {
                                showToast("操作失败！");
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }
                    break;
                case 77:
                    if (!result.equals("")) {
                        try {
                            jsonresult = new JSONObject(result);
                            if (jsonresult.optString("success").toString()
                                    .equals("1")) {
                                showDialog1(InfoBuyDetailActivity.this, "订单交易成功。");
                                downLoadInfo(id);
                            } else if (jsonresult.optString("success").toString()
                                    .equals("2")) {
                                showToast(jsonresult.optString("msg").toString());
                            } else if (jsonresult.optString("success").toString()
                                    .equals("7")) {
                                if (jsonresult.optString("errorNum").equals("5")) {
                                    final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "您的支付密码已输入错误5次,请重置支付密码！");
                                    dialog.addCancelButton("取消");
                                    dialog.addAccepteButton("重置");
                                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            Intent intent = new Intent(
                                                    InfoBuyDetailActivity.this,
                                                    ActivitySetPayPassword.class);
                                            intent.putExtra("type", "set");
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
                                } else {
                                    showToast("支付失败，您的支付密码输入错误,请重新支付！");

                                }
                            } else if (jsonresult.optString("success").toString()
                                    .equals("0")) {
                                showToast("用户信息错误,请重新登录！");
                                relogin(getApplicationContext());
                            } else if (jsonresult.optString("success").toString()
                                    .equals("3")) {
                                showToast("支付失败！");
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                    break;
                case 7://等待后台确认
                    if (!result.equals("")) {
                        try {
                            jsonresult = new JSONObject(result);
                            if (jsonresult.optString("success").toString()
                                    .equals("1")) {
                                showDialog1(InfoBuyDetailActivity.this, "请等待后台确认。");
                                downLoadInfo(id);
                            } else if (jsonresult.optString("success").toString()
                                    .equals("2")) {
                                showToast(jsonresult.optString("msg").toString());
                                downLoadInfo(id);
                            } else if (jsonresult.optString("success").toString()
                                    .equals("0")) {
                                showToast("用户信息错误,请重新登录！");
                                relogin(getApplicationContext());
                            } else if (jsonresult.optString("success").toString()
                                    .equals("3")) {
                                showToast("操作失败,请重试！");
                            }else if(jsonresult.optString("success").toString() .equals("8")){
                                showToast("操作失败，"+jsonresult.optString("msg").toString());
                                downLoadInfo(id);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                    break;
                case 8://修改失效状态
                    showToast("该订单已失效！");
                    downLoadInfo(id);
                    break;

            }

            super.handleMessage(msg);
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            String result = "";
            if (App.mUserInfo == null) {
                App.mUserInfo = FileUtil.readUser(InfoBuyDetailActivity.this);
            }
            App.setDataInfo(App.mUserInfo);
            MultipartEntity mpEntity = new MultipartEntity();
            try {
                mpEntity.addPart("e.id", new StringBody(id));
                mpEntity.addPart("e.purchaserID", new StringBody(App.userID));
                mpEntity.addPart("uuid", new StringBody(App.uuid));
                mpEntity.addPart("type", new StringBody("SZ"));
                mpEntity.addPart("e.orderStatus", new StringBody("5"));
                mpEntity.addPart("e.payType", new StringBody("1"));
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
            HttpUtil http = new HttpUtil();
            result = http.postDataMethod(UrlEntry.GETSTRING_URL, mpEntity);

            data.putString("value", result);
            msg.setData(data);
            msg.what = 5; // 下单

            handler.sendMessage(msg);
        }
    };

    Runnable delrunnable = new Runnable() {
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

                e.printStackTrace();
            }
            HttpUtil http = new HttpUtil();
            result = http.postDataMethod(UrlEntry.DELETE_MYINFO_BYID_URL,
                    mpEntity);

            data.putString("value", result);
            msg.setData(data);
            msg.what = 0; // 删除

            handler.sendMessage(msg);
        }
    };

    public void downLoadInfo(final String id) {
        if (mLlLoading.getVisibility() != View.VISIBLE) {
            showDialog();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("e.id", id);
        params.addBodyParameter("uuid", App.uuid);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_INFODETAIL_URL,
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
                        loadEcaluate(id);
                        getDate(responseInfo.result);

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

    public void getDate(String result) {
        list.clear();
        bannerList.clear();
        try {
            JSONObject jsonresult = new JSONObject(result);
            if (jsonresult.get("success").toString().equals("1")) {
                // showToast("查询成功！");
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                InfoEntry mInfoEntry = gson.fromJson(jsonresult.toString(),
                        InfoEntry.class);

                // 图片
                if (!jsonresult.get("pictureList").toString().equals("")) {
                    JSONArray imgarray = new JSONArray(jsonresult.get(
                            "pictureList").toString());
                    // if(imgarray.length() == 0){
                    // Banner banner = new Banner();
                    // banner.setImgPath("");
                    // bannerList.add(banner);
                    // }
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
                    mInfoEntry.setPicturelist(list);
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
                setData(mInfoEntry);
            } else if (jsonresult.get("success").toString().equals("0")) {
                showToast("用户信息错误,请重新登录！");
                relogin(getApplicationContext());
            } else if (jsonresult.get("success").toString().equals("3")) {
                showToast("查询失败！");
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.order_toedit:
                clearTempFromPref();
                Intent intent = new Intent(
                        InfoBuyDetailActivity.this,
                        NewInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("infoentry", infoEntry);

                intent.putExtras(bundle);
                Log.i("type", type);
                intent.putExtra("type", Integer.parseInt(type));// 1、为生猪出售
                // 2、为生猪收购
                intent.putExtra("upp", "upp");
                startActivityForResult(intent, 11);
                break;
            case R.id.order_del:
                final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "确认删除订单？");
                dialog.addCancelButton("取消");
                dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        new Thread(delrunnable).start();
                    }
                });
                dialog.setOnCancelButtonClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;
            case R.id.ok_btn:
                orderdialog("确定成交订单？", "3");

                break;
            case R.id.btn_call:
                call(infoEntry.getPhone());
                break;
            case R.id.btn_call_buyer:
                call(infoEntry.getPurchaserPhone());
                break;
            case R.id.tv_status:
                showDialog1(InfoBuyDetailActivity.this, infoEntry.getReason());
                break;
            case R.id.ll_load_failure:
                downLoadInfo(id);
                break;
            case R.id.btn_xieyi:
                intent = new Intent(InfoBuyDetailActivity.this, ActivityScanResult.class);
                intent.putExtra("url", UrlEntry.XIEYI_XXF_URL);
                startActivity(intent);
                break;
            case R.id.ll_liulan://浏览量
                if (isLogin(this)) {
                    showToast("请先登录！");
                } else {
                    // 自定义的单击事件
                    intent = new Intent(InfoBuyDetailActivity.this, LiuLanListActivity.class);
                    intent.putExtra("orderid", id);
                    startActivityForResult(intent, 11);
                }
                break;
        }
    }

    private void call(final String phone) {
        if (FileUtil.isBlank(phone)) {
            showToast("电话为空！");
            return;
        }
        if (isLogin(getApplicationContext())) {
            showToast("请先登录！");
            return;
        }

        if (App.authentication.equals("2")) {
            dialog("确定拨打电话？", phone);
        } else {
            final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "认证信息审核通过后才能拨打电话！");
            dialog.addCancelButton("取消");
            dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (App.usertype.equals("1")) { // 猪场
                        Intent zcintent = new Intent(
                                InfoBuyDetailActivity.this,
                                ZhuChangRenZhengActivity.class);
                        startActivity(zcintent);
                    } else if (App.usertype.equals("2")) {// 屠宰场
                        Intent zcintent = new Intent(
                                InfoBuyDetailActivity.this,
                                SlaughterhouseRenZhengActivity.class);
                        startActivity(zcintent);
                    } else if (App.usertype.equals("3")) {// 经纪人
                        Intent zcintent = new Intent(
                                InfoBuyDetailActivity.this,
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
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Bundle b = data.getExtras();
        // infoEntry = (InfoEntry) b.getSerializable("infoentry");
        // setData(infoEntry);

        list.clear();
        downLoadInfo(id);
        // loadEcaluate(id);
    }

    public class MyRunnable implements Runnable {
        private String type;

        public MyRunnable(String type) {
            this.type = type;
        }

        @Override
        public void run() {

            Message msg = new Message();
            Bundle data = new Bundle();
            String result = "";
            MultipartEntity mpEntity = new MultipartEntity();
            String URL = UrlEntry.UPDATE_MYINFO_BYID_URL;
            try {
                if (type.equals("77")) {
                    mpEntity.addPart("payPassword",
                            new StringBody(MD5.GetMD5Code(pwd)));
                    mpEntity.addPart("e.id", new StringBody(id));
                    mpEntity.addPart("uuid", new StringBody(App.uuid));
                    Map<String, String> maplist = new TreeMap<String, String>();
                    maplist.put("payPassword", MD5.GetMD5Code(pwd));
                    maplist.put("e.id", id);
                    maplist.put("uuid", App.uuid);
                    mpEntity.addPart("api_sign",
                            new StringBody(ZmtSignBean.sign(maplist,ZmtSignBean.secret)));
                    URL = UrlEntry.PHONE_PAY_URL;
                } else if (type.equals("22")) {//下单支付服务费
                    mpEntity.addPart("e.price", new StringBody(price));
                    mpEntity.addPart("e.number", new StringBody(billnum));
                    mpEntity.addPart("e.finalPrice", new StringBody(totlprice));
                    mpEntity.addPart("e.id", new StringBody(id));
                    mpEntity.addPart("uuid", new StringBody(App.uuid));
                    mpEntity.addPart("payPassword",
                            new StringBody(MD5.GetMD5Code(pwd)));
                    mpEntity.addPart("e.xdrxxf", new StringBody(xdrxxf));
                    Map<String, String> maplist = new TreeMap<String, String>();
                    maplist.put("payPassword", MD5.GetMD5Code(pwd));
                    maplist.put("e.id", id);
                    maplist.put("e.xdrxxf", xdrxxf);
                    maplist.put("e.number", billnum);
                    maplist.put("e.price", price);
                    maplist.put("e.finalPrice", totlprice);
                    maplist.put("uuid", App.uuid);
                    mpEntity.addPart("api_sign",
                            new StringBody(ZmtSignBean.sign(maplist,ZmtSignBean.secret)));
                    URL = UrlEntry.PAY_XIADAN_PRICE_URL;
                    type = "2";
                } else if (type.equals("44")) {
                    mpEntity.addPart("e.id", new StringBody(id));
                    mpEntity.addPart("uuid", new StringBody(App.uuid));
                    mpEntity.addPart("e.fbrxxf", new StringBody(xdrxxf));
                    mpEntity.addPart("payPassword",
                            new StringBody(MD5.GetMD5Code(pwd)));
                    Map<String, String> maplist = new TreeMap<String, String>();
                    maplist.put("payPassword", MD5.GetMD5Code(pwd));
                    maplist.put("e.id", id);
                    maplist.put("e.fbrxxf", xdrxxf);
                    maplist.put("uuid", App.uuid);
                    mpEntity.addPart("api_sign",
                            new StringBody(ZmtSignBean.sign(maplist,ZmtSignBean.secret)));
                    URL = UrlEntry.PAY_QUEREN_PRICE_URL;
                    type = "4";
                } else {
                    if (type.equals("2")) {
                        mpEntity.addPart("e.finalPrice", new StringBody(
                                totlprice));
                        mpEntity.addPart("e.number", new StringBody(billnum));
                        mpEntity.addPart("e.price", new StringBody(price));

                    }
                    if (type.equals(3)) {
                        mpEntity.addPart("e.payType", new StringBody("2"));
                    }
                    mpEntity.addPart("e.orderStatus", new StringBody(type));
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

    // 判断信息的发布者和登录者是否是同一人
    public boolean isLoginBuy() {
        boolean flag = false;
        if (!TextUtils.isEmpty(App.uuid) && App.userID.equals(fabuuserid)) {
            flag = true;
        }
        return flag;
    }

    // 判断登录者和卖家是否是同一账户
    public boolean isLoginSell() {
        boolean flag = false;
        if (!TextUtils.isEmpty(App.uuid) && App.userID.equals(selluserid)) {
            flag = true;
        }
        return flag;
    }

    // 下载评价
    private void loadEcaluate(String id) {
        showDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("e.productID", id);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.SELECT_EVALUATE_URL,
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
                        try {
                            JSONObject json = new JSONObject(result);
                            if (json.optString("success").equals("1")) {
                                System.out.println(result);

                                List<Ecaluate> childJsonItem = new ArrayList<Ecaluate>();
                                JSONArray jArrData = json.getJSONArray("list");

                                if (jArrData.length() == 0) {
                                    tv_nodata.setVisibility(View.VISIBLE);
                                    ll_layout.setVisibility(View.GONE);
                                } else {
                                    JSONObject jobj2 = jArrData
                                            .optJSONObject(0);
                                    GsonBuilder gsonb = new GsonBuilder();
                                    Gson gson = gsonb.create();
                                    Ecaluate mInfoEntry = gson.fromJson(
                                            jobj2.toString(), Ecaluate.class);
                                    childJsonItem.add(mInfoEntry);
                                    tv_nodata.setVisibility(View.GONE);
                                    mEcaluateBtn.setVisibility(View.GONE);
                                    ll_layout.setVisibility(View.VISIBLE);

                                    tv_coutent.setText(mInfoEntry.getContent());
                                    tv_date.setText(mInfoEntry.getCreateTime()
                                            .substring(0, 16));
                                    app_ratingbar.setRating(Float
                                            .parseFloat(mInfoEntry.getStar()));
                                }

                            } else {
                                // showToast("查询失败！");
                                tv_nodata.setVisibility(View.VISIBLE);
                                ll_layout.setVisibility(View.GONE);
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

    private void showbankdialog() {
        mSingleChoiceID2 = 0;
        Builder builder = new Builder(this);
        builder.setTitle("请选择支付方式");
        builder.setSingleChoiceItems(R.array.bank_names, 0,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        DialogInterface.OnClickListener btnListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                MyRunnable runnable0 = new MyRunnable("3");
                new Thread(runnable0).start();

                String[] arr = getResources().getStringArray(R.array.bank_urls);
                Uri uri = Uri.parse(arr[mSingleChoiceID2]);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        };
        builder.setPositiveButton("去支付", btnListener);
        builder.setNegativeButton("稍后", null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * 处理我的关注/取消或新增
     */
    private void handlerMyCare(final String type, String url) {
        showDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("friendid",
                String.valueOf(infoEntry.getUserID()));
        params.addBodyParameter("orderid", id);
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
                                downLoadInfo(id);
                            } else if (jsonresult.get("success").toString()
                                    .equals("4")) {
                                showToast("操作失败，该用户已不存在！");
                            } else {
                                if (type.equals("add")) {

                                    successType(jsonresult.get("success")
                                            .toString(), "关注失败！");
                                } else if (type.equals("del")) {
                                    successType(jsonresult.get("success")
                                            .toString(), "取消关注失败！");
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
            mCareBtn.setTextColor(getResources().getColor(R.color.gray));
            mCareBtn.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.xingxing1), null,
                    null);
        } else {
            mCareBtn.setTextColor(getResources().getColor(R.color.red));
            mCareBtn.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.xingxing2), null,
                    null);
        }
    }

    /**
     * // 判断是否已签约(未签约提示到牧易商城签约,已签约判断是否有支付密码有则弹出输入支付密码框没有则提示跳转到新增支付密码页面)
     */
    private void checkType() {
        showDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        params.addBodyParameter("orderid", id);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.CHECK_TYPE_URL, params,
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
                        Log.i("签约", "responseInfo.result = "
                                + responseInfo.result);
                        JSONObject jsonresult;

                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {
                                if (jsonresult.optString("skfStatus").equals("0")) {
                                    showDialog1(InfoBuyDetailActivity.this, "收款方未绑定银行卡，无法进行支付！");
                                    return;
                                }
                                parseResult(jsonresult, "fukuan");

                            } else if (jsonresult.get("success").toString()
                                    .equals("4")) {
                            } else {
                                successType(jsonresult.get("success").toString(), "操作失败");
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


    /**
     * // 判断是否已签约(未签约提示到牧易商城签约,已签约判断是否有支付密码有则弹出输入支付密码框没有则提示跳转到新增支付密码页面)
     */
    private void checkXxfInfo(final String type) {

        RequestParams params = new RequestParams();
        Map<String, String> maplist = new TreeMap<String, String>();
        maplist.put("orderid", id);
//		maplist.put("e.price", price);
        maplist.put("e.number", billnum);
//		maplist.put("e.finalPrice", totlprice);
        maplist.put("uuid", App.uuid);
        params.addBodyParameter("api_sign", ZmtSignBean.sign(maplist,ZmtSignBean.secret));
        params.addBodyParameter("uuid", App.uuid);
        params.addBodyParameter("orderid", id);
//		params.addBodyParameter("e.price", price);
        params.addBodyParameter("e.number", billnum);
//		params.addBodyParameter("e.finalPrice", totlprice);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.Get_XXF_INFO_URL, params,
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
                        Log.i("下单", "responseInfo.result = "
                                + responseInfo.result);
                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {
                                if (jsonresult.optString("MSG").equals("1")) {
                                    showToast("下单失败，该订单已被其他用户下单！");
                                } else {
                                    parseResult(jsonresult, type);
                                }

                            } else if (jsonresult.get("success").toString()
                                    .equals("4")) {
                            } else {
                                //角色审核状态判断
                                parseRenZhenResult(jsonresult);
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

    private void parseResult(JSONObject jsonresult, String status) {
        disDialog();
        if (status.equals("xiadan")) { //2016-9-8修改：下单和确认订单的时候无需判断银行卡信息)
            showDialog();
            MyRunnable runnable1 = new MyRunnable("2");
            new Thread(runnable1).start();

        } else if (status.equals("queren")) {//2016-9-8修改：下单和确认订单的时候无需判断银行卡信息)//2017-05-02 确认订单判断银行卡信息
            showDialog();
            MyRunnable runnable1 = new MyRunnable("4");
            new Thread(runnable1).start();

        } else if (status.equals("fukuan")) {//2016-9-8修改：在线支付的时候需判断银行卡信息)
            if (jsonresult.optString("fkfStatus").equals("2")) {
                if (jsonresult.optString("iszfpwd").equals(
                        "y")) {

                    if (jsonresult.optString("errorNum")
                            .equals("5")) {
                        final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "由于您的支付密码已连续错误输入5次,支付账号已被系统锁定,请重置支付密码！");
                        dialog.addCancelButton("取消");
                        dialog.addAccepteButton("重置");
                        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(
                                        InfoBuyDetailActivity.this,
                                        ActivitySetPayPassword.class);
                                intent.putExtra(
                                        "type",
                                        "forgot");
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

                    } else {

                        if (status.equals("xiadan")) {
                            if (jsonresult.optDouble("xxf") <= 0) {//如果信息服务费为0则表示不需要支付费用
                                showDialog();
                                MyRunnable runnable1 = new MyRunnable("2");
                                new Thread(runnable1).start();
                            } else {
                                xdrxxf = jsonresult.optString(
                                        "payamount").toString();
                                // 弹出支付密码输入框
                                XiaDanPayDialog mPayDialog = new XiaDanPayDialog(
                                        InfoBuyDetailActivity.this, order_btn,
                                        jsonresult.optString(
                                                "errorNum")
                                                .toString(),
                                        jsonresult.optString(
                                                "number").toString(),
                                        jsonresult.optString(
                                                "xxf").toString(),
                                        jsonresult.optString(
                                                "bankNum")
                                                .toString(),
                                        jsonresult.optString(
                                                "payamount")
                                                .toString(),
                                        new XiaDanPayDialog.OnDialogListener() {

                                            @Override
                                            public void back(
                                                    String pwd1) {
                                                pwd = pwd1;
                                                showDialog();
                                                // 付款
                                                MyRunnable run = new MyRunnable("22");
                                                new Thread(run)
                                                        .start();
                                            }
                                        });
                                mPayDialog.update();

                            }


                        } else if (status.equals("queren")) {
                            if (jsonresult.optDouble("xxf") <= 0) {//如果信息服务费为0则表示不需要支付费用
                                showDialog();
                                MyRunnable runnable1 = new MyRunnable("4");
                                new Thread(runnable1).start();
                            } else {
                                xdrxxf = jsonresult.optString(
                                        "payamount").toString();
                                // 弹出支付密码输入框
                                XiaDanPayDialog mPayDialog = new XiaDanPayDialog(
                                        InfoBuyDetailActivity.this, order_btn,
                                        jsonresult.optString(
                                                "errorNum")
                                                .toString(),
                                        jsonresult.optString(
                                                "number").toString(),
                                        jsonresult.optString(
                                                "xxf").toString(),
                                        jsonresult.optString(
                                                "bankNum")
                                                .toString(),
                                        jsonresult.optString(
                                                "payamount")
                                                .toString(),
                                        new XiaDanPayDialog.OnDialogListener() {

                                            @Override
                                            public void back(
                                                    String pwd1) {
                                                pwd = pwd1;
                                                showDialog();
                                                // 付款
                                                MyRunnable run = new MyRunnable("44");
                                                new Thread(run)
                                                        .start();
                                            }
                                        });
                                mPayDialog.update();

                            }


                        } else if (status.equals("fukuan")) {
                            // 弹出支付密码输入框
                            PayDialog mPayDialog = new PayDialog(
                                    InfoBuyDetailActivity.this,
                                    order_btn,
                                    jsonresult.optString(
                                            "errorNum")
                                            .toString(),
                                    jsonresult.optString(
                                            "skfxm").toString(),
                                    jsonresult.optString(
                                            "skfkh").toString(),
                                    jsonresult.optString(
                                            "bankNum")
                                            .toString(),
                                    jsonresult.optString(
                                            "payamount")
                                            .toString(),
                                    new PayDialog.OnDialogListener() {

                                        @Override
                                        public void back(
                                                String pwd1) {
                                            pwd = pwd1;
                                            showDialog();
                                            // 付款
                                            MyRunnable run = new MyRunnable(
                                                    "77");
                                            new Thread(run)
                                                    .start();
                                        }
                                    });
                            mPayDialog.update();
                        }


                    }

                } else {
                    String msg = "您未设置支付密码，无法进行交易，请先设置支付密码。";
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("type", "add");
                    jumpActivity(InfoBuyDetailActivity.this, ActivitySetPayPassword.class, msg, map);
                }

            } else if (jsonresult.optString("fkfStatus").equals("0")) {
                String msg = "您未绑定银行卡,无法进行交易,请先绑定银行卡再进行交易。";
                Map<String, String> map = new HashMap<String, String>();
                map.put("type", "add");
                map.put("cardtype", "nyBank");
                jumpActivity(InfoBuyDetailActivity.this, ActivityWithdrawals.class, msg, map);
            } else {
                showDialog1(InfoBuyDetailActivity.this, "您的银行卡未签约或未确认签约，请用您的账号登录神州牧易商城(www.my360.cn)进行签约！");

            }
        }

    }

    private void parseRenZhenResult(JSONObject jsonresult) {
        try {
            if (jsonresult.get("success").toString()
                    .equals("2")) {
                showDialog1(InfoBuyDetailActivity.this, jsonresult
                        .get("msg").toString());
                downLoadInfo(id);

            } else if (jsonresult.get("success").toString()
                    .equals("5")) {
                showDialog1(InfoBuyDetailActivity.this, jsonresult
                        .get("msg").toString());

            } else if (jsonresult.get("success").toString()
                    .equals("6")) {
                final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", jsonresult.get("msg").toString());
                dialog.addCancelButton("取消");
                dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (App.usertype.equals("1")) { // 猪场
                            Intent zcintent = new Intent(
                                    InfoBuyDetailActivity.this,
                                    ZhuChangRenZhengActivity.class);
                            startActivity(zcintent);
                        } else if (App.usertype.equals("2")) {// 屠宰场
                            Intent zcintent = new Intent(
                                    InfoBuyDetailActivity.this,
                                    SlaughterhouseRenZhengActivity.class);
                            startActivity(zcintent);
                        } else if (App.usertype.equals("3")) {// 经纪人
                            Intent zcintent = new Intent(
                                    InfoBuyDetailActivity.this,
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
                    .equals("7")) {

                if (jsonresult.optString("errorNum")
                        .equals("5")) {
                    final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "您的支付密码已输入错误5次,请重置支付密码！");
                    dialog.addCancelButton("取消");
                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(
                                    InfoBuyDetailActivity.this,
                                    ActivitySetPayPassword.class);
                            intent.putExtra(
                                    "type",
                                    "set");
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

                } else {
                    showToast("支付失败，您的支付密码输入错误,请重新支付！");
                }
            } else if (jsonresult.get("success").toString()
                    .equals("0")) {
                showToast("用户信息错误,请重新登录！");
                relogin(getApplicationContext());
            } else if (jsonresult.get("success").toString()
                    .equals("3")) {
                showToast("下单失败！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void back(View v) {
        if (AppManager.getAppManager().getActSize() < 2) {
            Intent intent = new Intent(InfoBuyDetailActivity.this,
                    ActivityMain.class);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

    private void getPayOrderTime(final String type) {
        showDialog();
        order_btn.setEnabled(false);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_PAYTIME_URL,
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
                        order_btn.setEnabled(true);
                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.optString("success").toString()
                                    .equals("1")) {
                                if(!TextUtils.isEmpty(type)){
//                                    sendDataByUUid(type);
                                    getBankStatus(type);

                                }else
//                                sendDataByUUid("fukuan");
                                getBankStatus("fukuan");
//                                2017-03-14 提示银行卡与实名认证名字一致
//                                final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示","请使用与实名认证“"+jsonresult.optString("realName").toString()+"”信息一致的银行卡进行付款");
//                                dialog.addCancelButton("修改");
//                                dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog.dismiss();
//                                        orderdialog("为保障交易双方合法权益，请先付款至平台托管（农行）账户，并备注订单号:"+infoEntry.getId()+"！\n\n" +
//                                                "户名：河南神州牧易电子商务有限公司\n\n" +
//                                                "账号：1643 1201 0400 13750\n"+"#13#", "6");//提示线下支付
//                                    }
//                                });
//                                dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog.dismiss();
//                                     Intent  intent = new Intent(InfoBuyDetailActivity.this, ActivityWithdrawals.class);
//                                        intent.putExtra("type", "rz");
//                                        startActivity(intent);
//                                    }
//                                });
//                                dialog.show();

                            } else if (jsonresult.optString("success").toString()
                                    .equals("2")) {
                                final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", jsonresult.optString("msg").toString());
                                dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.setOnCancelButtonClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();

                                    }
                                });
                                dialog.show();
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

    private void getIsSmrz() {
        order_btn.setEnabled(false);
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
                        order_btn.setEnabled(true);
                        Log.i("smrz", " smrz responseInfo.result = "
                                + responseInfo.result);
                        JSONObject jsonresult = null;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.optString("success").toString()
                                    .equals("1")) {

//                                sendDataByUUid("xiadan");
                                getBankStatus("xiadan");

//                                    //弹出确认下单
//                                    final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "确定下单？");
//                                    dialog.addCancelButton("取消");
//                                    dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.dismiss();
//                                            dialog.cancel();
//                                            if (App.CITY.equals("鹤壁市")){
//                                                xiadanorder("");
//                                            }else{
//                                                if(Integer.parseInt(infoEntry.getNumber())<100){
//                                                    showDialog1(InfoBuyDetailActivity.this,"下单失败，交易头数不能少于100头！");
//                                                }else{
//                                                    xiadanorder("100");
//                                                }
//                                            }
//
//                                        }
//                                    });
//                                    dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//                                    dialog.show();


                            } else if (jsonresult.optString("success").toString()
                                    .equals("6")) {
                                final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", jsonresult.optString("msg").toString());
                                dialog.addCancelButton("取消");
                                dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        if (App.usertype.equals("1")) { // 猪场
                                            Intent zcintent = new Intent(
                                                    InfoBuyDetailActivity.this,
                                                    ZhuChangRenZhengActivity.class);
                                            startActivity(zcintent);
                                        } else if (App.usertype.equals("2")) {// 屠宰场
                                            Intent zcintent = new Intent(
                                                    InfoBuyDetailActivity.this,
                                                    SlaughterhouseRenZhengActivity.class);
                                            startActivity(zcintent);
                                        } else if (App.usertype.equals("3")) {// 经纪人
                                            Intent zcintent = new Intent(
                                                    InfoBuyDetailActivity.this,
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
                                showDialog1(InfoBuyDetailActivity.this, jsonresult.optString("msg").toString());
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
//    private void sendDataByUUid(final String type) {
//        showDialog();
//        order_btn.setEnabled(false);
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("uuid", App.uuid);
//        final HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_CARDBYUUID_URL,
//                params, new RequestCallBack<String>() {
//                    @Override
//                    public void onStart() {
//                    }
//
//                    @Override
//                    public void onLoading(long total, long current,
//                                          boolean isUploading) {
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        disDialog();
//                        order_btn.setEnabled(true);
//                        Log.i("insert", " insert responseInfo.result = "
//                                + responseInfo.result);
//
//                        JSONObject jsonresult;
//                        try {
//                            jsonresult = new JSONObject(responseInfo.result);
//                            if (jsonresult.get("success").toString()
//                                    .equals("1")) {
//                                // showToast("查询成功！");
//                                GsonBuilder gsonb = new GsonBuilder();
//                                Gson gson = gsonb.create();
//                                final Withdrawals mInfoEntry = gson.fromJson(
//                                        jsonresult.toString(),
//                                        Withdrawals.class);
//                                if (type.equals("fukuan")) {
//                                    final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "请使用与实名认证“" + mInfoEntry.getName() + "”信息一致的银行卡进行付款");
//                                    dialog.addCancelButton("修改");
//                                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {
//
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.dismiss();
//                                            orderdialog("为保障交易双方合法权益，请先付款至平台托管（农行）账户，并备注订单号:" + infoEntry.getId() + "！\n\n" +
//                                                    "户名：河南神州牧易电子商务有限公司\n\n" +
//                                                    "账号：1643 1201 0400 13750\n" + "#13#", "6");//提示线下支付
//                                        }
//                                    });
//                                    dialog.setOnCancelButtonClickListener(new OnClickListener() {
//
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.dismiss();
//                                            Intent intent = new Intent(InfoBuyDetailActivity.this, ActivityWithdrawals.class);
//                                            intent.putExtra("type", "rz");
//                                            startActivity(intent);
//                                        }
//                                    });
//                                    dialog.show();
//                                }else if(type.equals("xianshifukuan")){
//                                    final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "请使用与实名认证“" + mInfoEntry.getName() + "”信息一致的银行卡进行付款");
//                                    dialog.addCancelButton("修改");
//                                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {
//
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.dismiss();
//                                            orderdialog("为保障交易双方合法权益，请在"+infoEntry.getPayDeadline()+"分钟内付款至平台托管（农行）账户，并备注订单号:" + infoEntry.getId() + "！\n\n" +
//                                                    "户名：河南神州牧易电子商务有限公司\n\n" +
//                                                    "账号：1643 1201 0400 13750\n" + "#13#", "6");//提示线下支付
//                                        }
//                                    });
//                                    dialog.setOnCancelButtonClickListener(new OnClickListener() {
//
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.dismiss();
//                                            Intent intent = new Intent(InfoBuyDetailActivity.this, ActivityWithdrawals.class);
//                                            intent.putExtra("type", "rz");
//                                            startActivity(intent);
//                                        }
//                                    });
//                                    dialog.show();
//                                }else if(type.equals("xianshifukuan1")){
//                                    final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "请使用与实名认证“" + mInfoEntry.getName() + "”信息一致的银行卡进行付款");
//                                    dialog.addCancelButton("修改");
//                                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {
//
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.dismiss();
//                                            order_btn.setEnabled(false);
//                                            MyRunnable   runnable2 = new MyRunnable("7"); //点击已支付 orderStatus状态改为  等待后台确认7
//                                            new Thread(runnable2).start();
//                                        }
//                                    });
//                                    dialog.setOnCancelButtonClickListener(new OnClickListener() {
//
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.dismiss();
//                                            Intent intent = new Intent(InfoBuyDetailActivity.this, ActivityWithdrawals.class);
//                                            intent.putExtra("type", "rz");
//                                            startActivity(intent);
//                                        }
//                                    });
//                                    dialog.show();
//
//                                } else {
//                                    // 弹出银行卡信息
//                                    final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "请先确认收款账号：\n收款人姓名：" + mInfoEntry.getName() + "\n收款人卡号：" + mInfoEntry.getBankNum() + "\n请使用与实名认证“" + mInfoEntry.getName() + "”信息一致的银行卡进行收款");
//                                    dialog.addCancelButton("修改");
//                                    dialog.addAccepteButton("确认");
//                                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {
//
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.cancel();
//                                            if (type.equals("xiadan")) {
//                                                //弹出确认下单
//                                                final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "确定下单？");
//                                                dialog.addCancelButton("取消");
//                                                dialog.setOnAcceptButtonClickListener(new OnClickListener() {
//
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        dialog.dismiss();
//                                                        dialog.cancel();
//                                                        if (App.CITY.equals("鹤壁市")) {
//                                                            xiadanorder("");
//                                                        } else {
//                                                            if (Integer.parseInt(infoEntry.getNumber()) < 100) {
//                                                                showDialog1(InfoBuyDetailActivity.this, "下单失败，交易头数不能少于100头！");
//                                                            } else {
//                                                                xiadanorder("100");
//                                                            }
//                                                        }
//
//                                                    }
//                                                });
//                                                dialog.setOnCancelButtonClickListener(new OnClickListener() {
//
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        dialog.dismiss();
//                                                    }
//                                                });
//                                                dialog.show();
//                                            } else if (type.equals("querendingdan")) {
//                                                showDialog();
//                                                MyRunnable runnable1 = new MyRunnable("4");
//                                                new Thread(runnable1).start();
//                                            }
//
//
//                                        }
//                                    });
//                                    dialog.setOnCancelButtonClickListener(new OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.cancel();
//                                            Intent intent = new Intent(InfoBuyDetailActivity.this, ActivityWithdrawals.class);
//                                            intent.putExtra("type", "rz");
//                                            startActivity(intent);
//                                        }
//                                    });
//                                    dialog.show();
//                                }
//
//
//
//                            } else if (jsonresult.get("success").toString()
//                                    .equals("5")) {
//                                //没有银行卡信息
//                                String msg = "为保证交易及时完成，请先绑定银行卡。";
//                                Map<String, String> map = new HashMap<String, String>();
//                                map.put("type", "rz");
//                                jumpActivity(InfoBuyDetailActivity.this, ActivityWithdrawals.class, msg, map);
//                            } else {
//                                successType(jsonresult.get("success")
//                                        .toString(), "操作失败！");
//                            }
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(HttpException error, String msg) {
//                        showToast("服务器连接失败，请稍候再试！");
//                        disDialog();
//
//                    }
//                });
//    }



    //查询电子账户和代收协议状态
    private void getBankStatus(final String  type){
        showDialog();
        order_btn.setEnabled(false);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_PAYBANK_STATUS_URL,
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
                        order_btn.setEnabled(true);
                        Log.i("status", "  responseInfo.result = "
                                + responseInfo.result);

                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {
                                // showToast("查询成功！");
                                GsonBuilder gsonb = new GsonBuilder();
                                Gson gson = gsonb.create();
                                final ElectronicAccounts mInfoEntry = gson.fromJson(
                                        jsonresult.toString(),
                                        ElectronicAccounts.class);

                                if (type.equals("fukuan")) {

                                        //判断代收协议
                                    if(mInfoEntry.getBandStatus().equals("0")||mInfoEntry.getBandStatus().equals("2")){//0 未开 2 解约
                                        final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "您的代收协议未签约，请先进行签约！");
//                                        dialog.addCancelButton("修改");
                                        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(InfoBuyDetailActivity.this, NewAgreementsigningActivity.class);
                                                startActivity(intent);

                                            }
                                        });
                                        dialog.setOnCancelButtonClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();
                                    }else{
                                        final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "请先确认您的代收协议账号：\n账 号："+mInfoEntry.getBandNo()+"\n账户名："+mInfoEntry.getBandName());
                                        dialog.addCancelButton("修改");
                                        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                //获取验证码
                                                smartPay();
                                            }
                                        });
                                        dialog.setOnCancelButtonClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(InfoBuyDetailActivity.this, NewAgreementsigningActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                        dialog.show();
                                    }
                                }else if(type.equals("xianshifukuan")){
                                    //判断代收协议
                                    if(mInfoEntry.getBandStatus().equals("0")||mInfoEntry.getBandStatus().equals("2")){//0 未开 2 解约
                                        final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "您的代收协议未签约，请先进行签约！");
//                                        dialog.addCancelButton("修改");
                                        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(InfoBuyDetailActivity.this, NewAgreementsigningActivity.class);
                                                startActivity(intent);

                                            }
                                        });
                                        dialog.setOnCancelButtonClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();
                                    }else{
                                        final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "请在"+infoEntry.getPayDeadline()+"分钟内付款\n请先确认您的代收协议账号：\n账 号："+mInfoEntry.getBandNo()+"\n账户名："+mInfoEntry.getBandName());
                                        dialog.addCancelButton("修改");
                                        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                //获取验证码
                                                smartPay();
                                            }
                                        });
                                        dialog.setOnCancelButtonClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(InfoBuyDetailActivity.this, NewAgreementsigningActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                        dialog.show();
                                    }

                                }else {
                                    //判断电子账户是否开户
                                    if (mInfoEntry.getCardStatus().equals("0")||mInfoEntry.getCardStatus().equals("2")){//未开户 已解约
                                        final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "请先开通快捷支付！");
//                                        dialog.addCancelButton("修改");
                                        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(InfoBuyDetailActivity.this, NewElectronicAccountsActivity.class);
                                                startActivity(intent);

                                            }
                                        });
                                        dialog.setOnCancelButtonClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();
                                    }else{
                                        String showmsg = "";

                                        if (type.equals("xiadan")){
                                            if (!App.usertype.equals("1")&&!App.usertype.equals("3")){
                                                showDialog1(InfoBuyDetailActivity.this,
                                                        "下单失败，只有猪场和经纪人才能对收购的订单进行操作。");
                                                return;
                                            }else{
                                                showmsg = "请先确认代收协议账号：\n姓名：" + mInfoEntry.getBandName() + "\n卡号：" + mInfoEntry.getBandNo();
                                                mClass = NewAgreementsigningActivity.class;
                                            }
                                        }else if (type.equals("querendingdan")){
                                            showmsg = "请先确认快捷支付账号：\n姓名：" + mInfoEntry.getCardName() + "\n卡号：" + mInfoEntry.getCardNo();
                                            mClass = NewElectronicAccountsActivity.class;
                                        }
                                        // 弹出银行卡信息
                                        final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", showmsg);
                                        dialog.addCancelButton("修改");
                                        dialog.addAccepteButton("确认");
                                        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.cancel();
                                                if (type.equals("xiadan")) {
                                                    //弹出确认下单
                                                    final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "确定下单？");
                                                    dialog.addCancelButton("取消");
                                                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();
                                                            dialog.cancel();
                                                            if (App.CITY.equals("鹤壁市")) {
                                                                xiadanorder("");
                                                            } else {
                                                                if (Integer.parseInt(infoEntry.getNumber()) < 100) {
                                                                    showDialog1(InfoBuyDetailActivity.this, "下单失败，交易头数不能少于100头！");
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
                                                } else if (type.equals("querendingdan")) {
                                                    showDialog();
                                                    MyRunnable runnable1 = new MyRunnable("4");
                                                    new Thread(runnable1).start();
                                                }


                                            }
                                        });

                                        dialog.setOnCancelButtonClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.cancel();
                                                if (mClass ==null) return;
                                                Intent intent = new Intent(InfoBuyDetailActivity.this, mClass);
                                                startActivity(intent);
                                            }
                                        });
                                        dialog.show();
                                    }

                                }



                            } else if(jsonresult.get("success").toString().equals("2")){
                                if (type.equals("xiadan")){
                                    final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "为了不影响交易，请先开通快捷支付！");
//                                        dialog.addCancelButton("修改");
                                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            Intent intent = new Intent(InfoBuyDetailActivity.this, NewElectronicAccountsActivity.class);
                                            startActivity(intent);

                                        }
                                    });
                                    dialog.setOnCancelButtonClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }else{
                                    final Dialog dialog = new Dialog(InfoBuyDetailActivity.this, "提示", "为了不影响交易，请先签约代收协议！");
//                                        dialog.addCancelButton("修改");
                                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            Intent intent = new Intent(InfoBuyDetailActivity.this, NewAgreementsigningActivity.class);
                                            startActivity(intent);

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

                            }else {
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
//获取验证码
    private void smartPay(){
        showDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        params.addBodyParameter("tradeNo", infoEntry.getId());
        params.addBodyParameter("receiptInfo", infoEntry.getPurchaserID());
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.PAYBANK_ORDER_URL,
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
                        Log.i("smartPay", " smartPay responseInfo.result = "
                                + responseInfo.result);
                        JSONObject jsonresult = null;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.optString("success").toString()
                                    .equals("1")) {
                                token = jsonresult.optString("token");
                                mobileSerial = jsonresult.optString("mobileSerial");
                                //弹出验证码输入框
                                YanzhengmaDialog mPayDialog = new YanzhengmaDialog(
                                        InfoBuyDetailActivity.this,
                                        order_btn,"",
                                        new YanzhengmaDialog.OnDialogListener() {

                                            @Override
                                            public void back(
                                                    String msgcode) {
                                              String  msg = msgcode;
                                                unbindECard(msg);
                                                // 付款

                                            }
                                        });
                                mPayDialog.update();


                            }  else if(jsonresult.optString("success").toString()
                                    .equals("2")){

                            }else {
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

    //确认支付
    private void unbindECard(String msgcode){
        showDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("mobileCode", msgcode);
        params.addBodyParameter("mobileSerial", mobileSerial);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.PAYBANK_ORDER_YZM_URL,
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
                        Log.i("smartPay", " smartPay responseInfo.result = "
                                + responseInfo.result);
                        JSONObject jsonresult = null;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.optString("success").toString()
                                    .equals("1")) {
                                if (jsonresult.optString("result").equals("1")||jsonresult.optString("result").equals("3")){
                                    showToast("支付成功");
                                }else{
                                    showToast("支付失败");
                                }
                                downLoadInfo(id);

                            }  else if(jsonresult.optString("success").toString()
                                    .equals("2")){
                              showToast(jsonresult.optString("msg").toString());
                            }else {
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

    Handler timehandler = new Handler();
    Runnable timerunnable = new Runnable() {
        @Override
        public void run() {

            recLen-=1000;
            long day = recLen / (24 * 60 * 60 * 1000);
            long hour = (recLen / (60 * 60 * 1000) - day * 24);
            long min = ((recLen / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long sec = (recLen/1000-day*24*60*60-hour*60*60-min*60);
            System.out.println(day + "天" + hour + "小时" + min + "分" + sec + "秒");
            String time = "";
            if(day>0){
                time = day + "天" + hour + "小时" + min + "分" + sec + "秒";
            }else  if(hour>0){
                time =  hour + "小时" + min + "分" + sec + "秒";
            }else{
                time =  min + "分" + sec + "秒";
            }
            if(infoEntry.getOrderStatus().equals("4")){
                mTvTime.setText(Html.fromHtml("此订单将在<font color = '#ff0000'>"+time+"</font>后自动失效，请及时点击付款"));
            }else if(infoEntry.getOrderStatus().equals("7")){
                mTvTime.setText(Html.fromHtml("此订单将在<font color = '#ff0000'>"+time+"</font>后自动失效，请及时完成付款"));
            }
//            mTvTime.setText(time);
            if (min==0&&sec==0){
                timehandler.removeCallbacks(timerunnable);
                isTimeRun = false;
                downLoadInfo(id);
//                order_btn.setEnabled(false);
//                MyRunnable  runnable2 = new MyRunnable("8"); // orderStatus状态改为 订单已失效8
//                new Thread(runnable2).start();
                return;
            }
            timehandler.postDelayed(this, 1000);
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isTimeRun){
            timehandler.removeCallbacks(timerunnable);
        }
    }
}
