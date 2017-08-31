package com.szmy.pigapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.service.LocationService;
import com.gc.materialdesign.widgets.Dialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.agent.AgentRenZhengActivity;
import com.szmy.pigapp.entity.InfoEntry;
import com.szmy.pigapp.image.CustomConstants;
import com.szmy.pigapp.image.ImageItem;
import com.szmy.pigapp.image.ImagePublishAdapter;
import com.szmy.pigapp.image.ImageZoomActivity;
import com.szmy.pigapp.image.PopupWindows;
import com.szmy.pigapp.tixian.ActivityWithdrawals;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.DateTimePickerDialog;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.zheshang.NewElectronicAccountsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 新增生猪交易
 *
 * @author qing
 */
public class NewInfoActivity extends BaseActivity implements
        OnCheckedChangeListener {

    private int type;
    private String picPath, orderStatus = "1";

    private EditText title_et, price_et, number_et, weight_et, address_et,
            remark_et, et_profit;
    private RadioGroup rg_status;
    private TextView price_tv, leave_start_timebox_tv_content;
    private LinearLayout chulan_layout;
    private LinearLayout weight_layout;
    private Button btn_submit;
    private String id;
    private static final int DATE_DIALOG_ID = 01;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    private String title, shengfen="", citys, quxian, jiedao;

    private TextView province_city_tv_content, pinzhong_tv_content, img_add_tv,
            et_pigcolor;
    private String pigType = "";
    private LinearLayout profit_layout;
    private ImageButton mLocationBtn;
    public static final int LOCATION_ID = 29;
    public LocationService locationService;
    public Vibrator mVibrator;
    public static double x = 0;
    public static double y = 0;
    public String typename = "";
    public String isShow = "";
    public static final int COLOR_ID = 30;
    private final int MONEY_TYPE = 11;
    private String[] mItems = {"内三元", "外三元", "土杂猪"};
    private String[] mPsItems = {"买家拉猪", "卖家送猪", "双方协商"};
    boolean[] selected = new boolean[]{false, false, false, false};
    private int mSingleChoiceID = 0;
    private Button mNextBtn, mBackBtn;
    private LinearLayout mLlFirst;
    private LinearLayout mLlTwo;
    private TextView mTvPeiSong;
    private LinearLayout mLlBack;
    public static final int LIKETYPE = 0x000009;
    private String matchingOrder = "";

    public static List<ImageItem> mDataList = new ArrayList<ImageItem>();
    private ImagePublishAdapter mAdapter;
    private GridView mGridView;
    private PopupWindow mPwDialog;
    private int TAKE_PHOTO;
    private int TAKE_PICTURE;
    private File mFile;
    private String path = "";
    private TextView mTvTishi,mTvTimetip,mTvAddressTip;
    private InfoEntry infoentry;
    private String url;
    private String minPrice = "10";
    private String maxPrice = "30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_info_substep);
        mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
        initView();
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 1);
        if (intent.hasExtra("upp")) {
            url = UrlEntry.UPDATE_MYINFO_BYID_URL;
            infoentry = (InfoEntry) getIntent().getSerializableExtra(
                    "infoentry");
            if (type == 1) {
                typename = "出售";
                ((TextView) findViewById(R.id.def_head_tv)).setText("修改出售信息");
            } else {
                typename = "收购";
                ((TextView) findViewById(R.id.def_head_tv)).setText("修改收购信息");
            }
            setViewData(infoentry);
        } else {
            url = UrlEntry.ADD_MYINFO_URL;
            if (type == 1) {
                typename = "出售";
                ((TextView) findViewById(R.id.def_head_tv)).setText("新增出售信息");
            } else {
                typename = "收购";
                ((TextView) findViewById(R.id.def_head_tv)).setText("新增收购信息");
            }
            getMinandMaxPrice();
        }

        SharedPreferences sp = getSharedPreferences("showonmap", MODE_PRIVATE);
        if (!TextUtils.isEmpty(sp.getString("isShow", ""))) {
            isShow = sp.getString("isShow", "");
        }

        if (App.mUserInfo == null) {
            App.mUserInfo = FileUtil.readUser(NewInfoActivity.this);
            App.setDataInfo(App.mUserInfo);
        }
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

    }

    /*****
     * @see
     * //定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location
                    && location.getLocType() != BDLocation.TypeServerError) {
                if (location.getLocType() == 62) {
                    if (App.usertype.equals("3")) {
                        province_city_tv_content.setEnabled(true);
                        province_city_tv_content.setClickable(true);
                    }
                    return;
                }
                x = location.getLatitude();
                y = location.getLongitude();
                shengfen = location.getProvince();
                citys = location.getCity();
                quxian = location.getDistrict();
                jiedao = location.getStreet() + location.getStreetNumber();
                if (!TextUtils.isEmpty(citys) && citys.contains(shengfen)) {
                    province_city_tv_content.setText(shengfen + quxian);
                } else {
                    province_city_tv_content.setText(shengfen + citys + quxian);
                }

                address_et.setText(jiedao);
                if (App.usertype.equals("3")) {

                    province_city_tv_content.setEnabled(true);
                    province_city_tv_content.setClickable(true);

                } else {
                    province_city_tv_content.setEnabled(false);
                    province_city_tv_content.setClickable(false);
                }
            } else {
                province_city_tv_content.setEnabled(true);
                province_city_tv_content.setClickable(true);
            }
        }
    };

    protected void onResume() {
        super.onResume();
        notifyDataChanged(); // 当在ImageZoomActivity中删除图片时，返回这里需要刷新
        // 定位
        if ((AppStaticUtil.getConvertDouble(App.GPS_X) == 0.0)
                || (AppStaticUtil.getConvertDouble(App.GPS_Y) == 0.0)) {
            if (App.usertype.equals("3") || App.usertype.equals("5")) {
                if (AppStaticUtil.isNetwork(getApplicationContext())) {
                    locationService = new LocationService(
                            getApplicationContext());
                    locationService.registerListener(mListener);
                }
            } else if (App.usertype.equals("1") || App.usertype.equals("2")) {
                final Dialog dialog = new Dialog(NewInfoActivity.this, "提示", "您当前没有位置信息，是否现在添加？");
                dialog.addCancelButton("取消");
                dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        if (App.usertype.equals("1")) { // 猪场
                            Intent zcintent = new Intent(
                                    NewInfoActivity.this,
                                    ZhuChangRenZhengActivity.class);
                            startActivity(zcintent);
                        } else if (App.usertype.equals("2")) {// 屠宰场
                            Intent zcintent = new Intent(
                                    NewInfoActivity.this,
                                    SlaughterhouseRenZhengActivity.class);
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
        } else {
            x = AppStaticUtil.getConvertDouble(App.GPS_X);
            y = AppStaticUtil.getConvertDouble(App.GPS_Y);
            shengfen = App.PROVINCE;
            citys = App.CITY;
            quxian = App.AREA;
            jiedao = App.ADDRESS;
            province_city_tv_content.setText(shengfen + " " + citys + " "
                    + quxian);
            address_et.setText(jiedao);
            province_city_tv_content.setEnabled(false);
            province_city_tv_content.setClickable(false);
        }
    }

    ;

    public void initView() {
        title_et = (EditText) findViewById(R.id.et_title);
        price_et = (EditText) findViewById(R.id.et_price);
        number_et = (EditText) findViewById(R.id.et_count);
        weight_et = (EditText) findViewById(R.id.et_weight);
        address_et = (EditText) findViewById(R.id.et_address);
        remark_et = (EditText) findViewById(R.id.et_remark);
        rg_status = (RadioGroup) findViewById(R.id.rg_status);
        // nsgv = (NoScrollGridView) findViewById(R.id._gridView);
        img_add_tv = (TextView) findViewById(R.id.img_add_tv);
        rg_status.setOnCheckedChangeListener(this);
        price_tv = (TextView) findViewById(R.id.price_tv);
        chulan_layout = (LinearLayout) findViewById(R.id.chulan_layout);
        weight_layout = (LinearLayout) findViewById(R.id.weight_layout);
        profit_layout = (LinearLayout) findViewById(R.id.profit_layout);
        et_profit = (EditText) findViewById(R.id.et_profit);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        mTvTishi = (TextView) findViewById(R.id.tishi);
        mTvTimetip = (TextView) findViewById(R.id.tv_sgtime);
        mTvAddressTip = (TextView) findViewById(R.id.tv_sgaddress);
        if (type==2) {
            mTvTimetip.setText("收购时间：");
            mTvAddressTip.setText("收购区域");
        }


        et_pigcolor = (TextView) findViewById(R.id.et_pigcolor);
        et_pigcolor.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 选择毛色
                // startActivityForResult(new
                // Intent(NewInfoActivity.this,ChosePigColorActivity.class),
                // COLOR_ID);

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        NewInfoActivity.this);
                builder.setTitle("请选择毛色");
                DialogInterface.OnMultiChoiceClickListener mutiListener = new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface,
                                        int which, boolean isChecked) {
                        selected[which] = isChecked;
                    }
                };
                builder.setMultiChoiceItems(R.array.pig_color, selected,
                        mutiListener);
                DialogInterface.OnClickListener btnListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,
                                        int which) {

                        String selectedStr = "";
                        for (int i = 0; i < selected.length; i++) {
                            if (selected[i] == true) {

                                selectedStr = selectedStr
                                        + " "
                                        + getResources().getStringArray(
                                        R.array.pig_color)[i];
                            }
                        }

                        et_pigcolor.setText(selectedStr);

                    }
                };
                builder.setPositiveButton("确定", btnListener);
                builder.create().show();
                ;

            }
        });
        leave_start_timebox_tv_content = (TextView) findViewById(R.id.leave_start_timebox_tv_content);
        leave_start_timebox_tv_content
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        showDialog(DATE_DIALOG_ID);

                    }
                });
        if (type == 2) {
            profit_layout.setVisibility(View.GONE);
            chulan_layout.setVisibility(View.GONE);
            // weight_layout.setVisibility(View.GONE);
            img_add_tv.setVisibility(View.GONE);
            // nsgv.setVisibility(View.GONE);

        }

        btn_submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                System.out.println(App.usertype + "  " + typename);
                if (TextUtils.isEmpty(mTvPeiSong.getText().toString())) {
                    showToast("请选择配送方式！");
                    return;
                }
                if (type == 1) {
                    if (App.usertype.equals("5") || App.usertype.equals("2")
                            || App.usertype.equals("4")) {
                        showDialog1(NewInfoActivity.this,
                                "发布信息失败，认证猪场或经纪人才可以发布出售信息。");
                        return;
                    }



                } else if (type == 2) {
                    if (App.usertype.equals("5") || App.usertype.equals("1")
                            || App.usertype.equals("4")) {
                        showDialog1(NewInfoActivity.this,
                                "发布信息失败，认证经纪人或屠宰场才可以发布收购信息。");
                        return;
                    }

                }
                //2017-2-27 修改：经纪人发布收购无需判断是否绑定银行卡
//                if(type!=1){//收购
//                    if ( App.usertype.equals("1")) {//2017-*1-12 猪场经纪人判断银行卡
//                        isFlagCard();//2017-02-27 只判断猪场是否绑定银行卡
//                    } else {
//                        sendData();//2016-09-08 修改：提交信息不用判断是否绑定/签约银行卡
//                    }
//                }else {//出售
//                    if (App.usertype.equals("3") || App.usertype.equals("1")) {//2017-*1-12 猪场经纪人判断银行卡
//                        isFlagCard();
//                    } else {
//                        sendData();//2016-09-08 修改：提交信息不用判断是否绑定/签约银行卡
////                isFlagCard();//2016-09-05 修改：提交信息先判断是否绑定/签约银行卡
//                    }
//                }
//                if ((App.usertype.equals("3")&&type==1)){//经纪人发布出售需要判断电子账户是否开户
//                    getBankStatus();
//                }else
                sendData();//2017-04-28 修改 所有角色发布无需判断银行卡
            }
        });

        province_city_tv_content = (TextView) findViewById(R.id.province_city_tv_content);
        province_city_tv_content.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (shengfen.equals("")) {
                    Intent intent = new Intent(NewInfoActivity.this,
                            ChoseProvinceActivity.class);
                    intent.putExtra("add", "add");
                    startActivityForResult(intent, App.ADDRESS_CODE);
//
                } else {

                    Intent locintent = new Intent(NewInfoActivity.this,
                            LocationMapActivity.class);
                    locintent.putExtra("x", x);
                    locintent.putExtra("y", y);
                    startActivityForResult(locintent, LOCATION_ID);
                }

            }
        });

        pinzhong_tv_content = (TextView) findViewById(R.id.pinzhong_tv_content);
        pinzhong_tv_content.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Intent intent = new
                // Intent(NewInfoActivity.this,ChoseTypeActivity.class);
                // startActivityForResult(intent, 23);

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        NewInfoActivity.this);

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

                                pigType = String.valueOf(mSingleChoiceID + 1);

                                pinzhong_tv_content
                                        .setText(mItems[mSingleChoiceID]);

                            }
                        });
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                            }
                        });
                builder.create().show();

            }
        });
        mLocationBtn = (ImageButton) findViewById(R.id.locationBtn);
        mLocationBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent locintent = new Intent(NewInfoActivity.this,
                        LocationMapActivity.class);
                locintent.putExtra("x", x);
                locintent.putExtra("y", y);
                startActivityForResult(locintent, LOCATION_ID);

            }
        });

        if (App.usertype.equals("3")) {
            address_et.setClickable(true);
            address_et.setEnabled(true);
            mLocationBtn.setVisibility(View.VISIBLE);
            province_city_tv_content.setClickable(true);
            province_city_tv_content.setEnabled(true);

        }
        if (App.usertype.equals("5")) {
            address_et.setClickable(true);
            address_et.setEnabled(true);
            mLocationBtn.setVisibility(View.VISIBLE);
            province_city_tv_content.setClickable(true);
            province_city_tv_content.setEnabled(true);
        }
        mLlFirst = (LinearLayout) findViewById(R.id.layoutone);
        mLlTwo = (LinearLayout) findViewById(R.id.layouttwo);
        mTvPeiSong = (TextView) findViewById(R.id.peisong_tv_content);
        mTvPeiSong.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        NewInfoActivity.this);

                mSingleChoiceID = 0;
                builder.setTitle("请选择");
                builder.setSingleChoiceItems(mPsItems, 0,
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

                                mTvPeiSong.setText(mPsItems[mSingleChoiceID]);

                            }
                        });
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                            }
                        });
                builder.create().show();

            }
        });
        mNextBtn = (Button) findViewById(R.id.btn_next);
        mNextBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_pigcolor.getText().toString())) {

                    showToast("毛色不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(pinzhong_tv_content.getText().toString())) {
                    showToast("请选择生猪品种");
                    return;
                }
                if (TextUtils.isEmpty(price_et.getText().toString())) {
                    showToast("价格不能为空！");
                    return;
                } else {
                    if (!FileUtil.isPrice(price_et.getText().toString())) {
                        showToast("您输入的价格格式不正确!");
                        return;
                    }
                    if (!FileUtil.isLegalPrice(0,
                            price_et.getText().toString(), Float.parseFloat(maxPrice), Float.parseFloat(minPrice))) {
                        if (Double.parseDouble(price_et.getText().toString()) > Float.parseFloat(maxPrice)) {
                            showToast("您输入的价格过高!");
                            return;

                        }
                        if (Double.parseDouble(price_et.getText().toString()) < Float.parseFloat(minPrice)) {
                            showToast("您输入的价格过低!");
                            return;
                        }
                    }

                }
                if (TextUtils.isEmpty(number_et.getText().toString())) {
                    showToast("数量不能为空！");
                    return;
                }

                if (TextUtils.isEmpty(province_city_tv_content.getText()
                        .toString())) {
                    showToast("省份不能为空！请到实名认证修改地址！");
                    if (App.usertype.equals("1")) { // 猪场

                        Intent zcintent = new Intent(NewInfoActivity.this,
                                ZhuChangRenZhengActivity.class);
                        startActivityForResult(zcintent, MONEY_TYPE);
                    } else if (App.usertype.equals("2")) {// 屠宰场
                        Intent zcintent = new Intent(NewInfoActivity.this,
                                SlaughterhouseRenZhengActivity.class);
                        startActivityForResult(zcintent, MONEY_TYPE);
                    }

                    return;
                }
                if (TextUtils.isEmpty(address_et.getText().toString())) {
                    showToast("详细地址不能为空！请到实名认证修改地址！");
                    if (App.usertype.equals("1")) { // 猪场

                        Intent zcintent = new Intent(NewInfoActivity.this,
                                ZhuChangRenZhengActivity.class);
                        startActivityForResult(zcintent, MONEY_TYPE);
                    } else if (App.usertype.equals("2")) {// 屠宰场
                        Intent zcintent = new Intent(NewInfoActivity.this,
                                SlaughterhouseRenZhengActivity.class);
                        startActivityForResult(zcintent, MONEY_TYPE);
                    }
                    return;
                }
                if (type == 1) {
                    if (TextUtils.isEmpty(leave_start_timebox_tv_content
                            .getText().toString())) {
                        showToast("出栏时间不能为空！");
                        return;
                    }
//					SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm");
//					Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
//					String    str    =    formatter.format(curDate);
//					int result = leave_start_timebox_tv_content.getText().toString()
//							.compareTo(str);
//					if (result < 0) {
//						showToast("出栏时间不能小于当前时间,请重新选择！");
//						return;
//					}
                    if (TextUtils.isEmpty(weight_et.getText().toString())) {
                        showToast("重量不能为空！");
                        return;
                    }
                }
                hideInput();
                mLlFirst.setVisibility(View.GONE);
                mLlTwo.setVisibility(View.VISIBLE);

            }
        });

        mBackBtn = (Button) findViewById(R.id.btn_goback);
        mBackBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mLlTwo.setVisibility(View.GONE);
                mLlFirst.setVisibility(View.VISIBLE);

            }
        });

        mLlBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((mLlTwo.getVisibility() == View.VISIBLE)
                        && (mLlFirst.getVisibility() == View.GONE)) {
                    System.out.print("shangyiye");
                    mLlTwo.setVisibility(View.GONE);
                    mLlFirst.setVisibility(View.VISIBLE);
                } else if ((mLlTwo.getVisibility() == View.GONE)
                        && (mLlFirst.getVisibility() == View.VISIBLE)) {
                    System.out.print("finish");
                    clearTempFromPref();
                    finish();
                }

            }
        });
        mDataList = getTempFromPref(CustomConstants.PREF_TEMP_IMAGES);
        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ImagePublishAdapter(this, mDataList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == getDataSize()) {
                    TAKE_PHOTO = CustomConstants.TAKE_PICTURE;
                    TAKE_PICTURE = CustomConstants.TAKE_PICTURE1;
                    showPopDialog(mGridView);
                } else {
                    Intent intent = new Intent(NewInfoActivity.this,
                            ImageZoomActivity.class);
                    intent.putExtra(CustomConstants.EXTRA_IMAGE_LIST,
                            (Serializable) mDataList);
                    intent.putExtra(CustomConstants.EXTRA_CURRENT_IMG_POSITION,
                            position);
                    intent.putExtra("name", "newinfo");
                    startActivity(intent);
                }
            }
        });

    }

    private int getDataSize() {
        return mDataList == null ? 0 : mDataList.size();
    }

    protected void onRestart() {

        super.onRestart();
    }
    public void isFlagCard() {
        showDialog();
        RequestParams params = new RequestParams();

        params.addBodyParameter("uuid", App.uuid);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_ISBINDING_URL,
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
                        Log.i("insert", " insert responseInfo.result = "
                                + responseInfo.result);

                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {
                                if (jsonresult.optString("status").equals("2")) {
                                    sendData();
                                }else if(jsonresult.optString("status").equals("0")){
                                    String msg = "您未绑定银行卡,无法进行发布,请先绑定银行卡再进行发布信息。";
                                    Map<String,String> map = new HashMap<String, String>();
                                    map.put("type", "add");
                                    map.put("cardtype", "nyBank");
                                    jumpActivity(NewInfoActivity.this,ActivityWithdrawals.class,msg,map);
                                } else {
//
                                    showDialog1(NewInfoActivity.this,"您的银行卡未签约或未确认签约，请用您的账号登录神州牧易商城(www.my360.cn)进行签约！");
                                }



//								}

                            } else {
                                successType(jsonresult.get("success")
                                        .toString(), "查询失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure (HttpException error, String msg) {

                        disDialog();
                    }
                });
    }
    private void sendData() {
        showDialog();
        btn_submit.setEnabled(false);
        RequestParams params = new RequestParams();
        if (url.equals(UrlEntry.UPDATE_MYINFO_BYID_URL)) {
            params.addBodyParameter("e.id", id);
        }
        params.addBodyParameter("e.title", typename
                + pinzhong_tv_content.getText().toString());
        params.addBodyParameter("e.price", price_et.getText().toString());
        params.addBodyParameter("e.marketingTime",
                leave_start_timebox_tv_content.getText().toString());
        params.addBodyParameter("e.number", number_et.getText().toString());
        params.addBodyParameter("e.weight", weight_et.getText().toString());
//        params.addBodyParameter("e.orderStatus", orderStatus);// 订单状态
        params.addBodyParameter("e.province", shengfen);// 省份
        params.addBodyParameter("e.city", citys);// 城市
        params.addBodyParameter("e.area", quxian);// 区县
        params.addBodyParameter("e.address", address_et.getText().toString());// 街道
        params.addBodyParameter("e.remark", remark_et.getText().toString());
        // params.addBodyParameter("e.profit",et_profit.getText().toString());//利润
        params.addBodyParameter("e.userName", App.username);
        params.addBodyParameter("e.userID", App.userID);
        params.addBodyParameter("e.orderType", String.valueOf(type));
        params.addBodyParameter("uuid", App.uuid);
        params.addBodyParameter("e.pigType", pigType);// 品种
        params.addBodyParameter("e.color", et_pigcolor.getText().toString());
        params.addBodyParameter("e.x", String.valueOf(x));
        params.addBodyParameter("e.y", String.valueOf(y));
        params.addBodyParameter("e.isShow", isShow);
        params.addBodyParameter("e.sendType", mTvPeiSong.getText().toString());

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
                        btn_submit.setEnabled(true);

                        Log.i("login", " login responseInfo.result = "
                                + responseInfo.result);

                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {

                                if (App.mUserInfo == null) {
                                    App.mUserInfo = FileUtil
                                            .readUser(NewInfoActivity.this);
                                }
                                App.mUserInfo.setX(String.valueOf(x));
                                App.mUserInfo.setY(String.valueOf(y));
                                FileUtil.saveUser(NewInfoActivity.this,
                                        App.mUserInfo);
                                App.setDataInfo(App.mUserInfo);
                                if (url.equals(UrlEntry.ADD_MYINFO_URL)) {
                                    id = jsonresult.optString("id").toString();
                                }

                                // 上传图片
                                if (mDataList.size() > 0) {
                                    matchingOrder = jsonresult.optString(
                                            "matchingOrder").toString();
                                    new Thread(runnable).start();
                                } else {
                                    clearTempFromPref();
                                    String str = jsonresult.optString(
                                            "matchingOrder").toString();
                                    if (TextUtils.isEmpty(str)
                                            || str.equals("0")) {
                                        disDialog();
                                        showToast("发布成功！");
                                        setResult(27);
                                        finish();
                                    } else {
                                        disDialog();
                                        showDialog2(NewInfoActivity.this,
                                                "发布成功,已为您匹配" + str
                                                        + "条信息，是否查看？");
                                    }
                                    // showToast("发布成功！");
                                    // setResult(27);
                                    // finish();
                                }
                            } else if (jsonresult.get("success").toString()
                                    .equals("5")) {
                                disDialog();
                                showDialog1(NewInfoActivity.this, jsonresult
                                        .get("msg").toString());

                            } else if (jsonresult.get("success").toString()
                                    .equals("6")) {
                                disDialog();
                                final Dialog dialog = new Dialog(NewInfoActivity.this, "提示", jsonresult.get("msg")
                                        .toString());
                                dialog.addCancelButton("取消");
                                dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                        if (App.usertype.equals("1")) { // 猪场
                                            Intent zcintent = new Intent(
                                                    NewInfoActivity.this,
                                                    ZhuChangRenZhengActivity.class);
                                            startActivity(zcintent);
                                        } else if (App.usertype
                                                .equals("2")) {// 屠宰场
                                            Intent zcintent = new Intent(
                                                    NewInfoActivity.this,
                                                    SlaughterhouseRenZhengActivity.class);
                                            startActivity(zcintent);
                                        } else if (App.usertype
                                                .equals("3")) {// 经纪人
                                            Intent zcintent = new Intent(
                                                    NewInfoActivity.this,
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
                                dialog.setCancelable(false);
                                dialog.show();
                            } else{
                                disDialog();
                                successType(jsonresult.get("success")
                                        .toString(), "发布失败！");
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        btn_submit.setEnabled(true);
                        showToast("新增失败,请稍后再试！");
                        disDialog();
                    }
                });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            String result = "";
            for (int i = 0; i < mDataList.size(); i++) {
                HttpUtil http = new HttpUtil();
                System.out.println("mDataList.get(i).sourcePath"
                        + mDataList.get(i).sourcePath);
                result = http.postFileMethod(new File(
                                mDataList.get(i).sourcePath), UrlEntry.UPLOAD_FILE_URL,
                        App.uuid, id, String.valueOf((i + 1)));

            }
            data.putString("value", result);
            msg.setData(data);
            msg.what = 3;

            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    notifyDataChanged();
                    break;
                case 2:
                    Bundle data = msg.getData();
                    String val = data.getString("value");
                    Log.i("mylog", "请求结果为-->" + val);
                    break;
                case 3:
                    disDialog();

                    Bundle data1 = msg.getData();
                    String result = data1.getString("value");
                    JSONObject jsonresult;
                    if (!result.equals("")) {
                        try {
                            jsonresult = new JSONObject(result);
                            if (jsonresult.get("success").toString().equals("1")) {
                                clearTempFromPref();
                                String str = matchingOrder;
                                if (TextUtils.isEmpty(str) || str.equals("0")) {
                                    showToast("发布成功！");

                                    setResult(27);
                                    finish();
                                } else {

                                    showDialog2(NewInfoActivity.this, "发布成功,已为您匹配"
                                            + str + "条信息，是否立即查看？");
                                }

                            } else {
                                successType(jsonresult.get("success").toString(),
                                        "发布失败！");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle b;
        Log.i("aaaa", " , " + requestCode);
        switch (requestCode) {
            case CustomConstants.TAKE_PICTURE:
                System.out.println("path" + path);
                if (mDataList.size() < CustomConstants.MAX_IMAGE_SIZE
                        && resultCode == -1 && !TextUtils.isEmpty(path)) {

                    mDataList.add(FileUtil.saveDataPic(path));
                    saveTempToPref(CustomConstants.PREF_TEMP_IMAGES, mDataList);
                }

                break;

            case CustomConstants.TAKE_PICTURE1:
                if (resultCode != -1) {
                    return;
                }
                if (data == null) {
                    return;
                }
                path = getPath(getFile());
                startPhotoZoom(data.getData(), path,
                        CustomConstants.CORP_TAKE_PICTURE1);
                break;

            case CustomConstants.CORP_TAKE_PICTURE1:
                if (resultCode != -1) {
                    return;
                }
                System.out.println(mDataList.size() + "");
                if (path.equals("")) {
                    showToast("图片选取失败，请重新选择图片");
                    return;
                }
                mDataList = setPicToView(data, path, mDataList, 1);
                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES, mDataList);
                // initView();
                break;
            case App.ADDRESS_CODE:
                if (data == null)
                    return;
                Bundle bundle = data.getExtras();
                shengfen = bundle.getString("province");
                citys = bundle.getString("city");
                quxian = bundle.getString("area");
                province_city_tv_content.setText(shengfen + " " + citys + " "
                        + quxian);
                break;
            case 23:
                if (data == null)
                    return;
                b = data.getExtras();
                pigType = b.getString("pigType");
                String pigTypeName = b.getString("pigTypeName");
                pinzhong_tv_content.setText(pigTypeName);
                break;
            case LOCATION_ID:
                if (data == null)
                    return;
                b = data.getExtras();
                if (!TextUtils.isEmpty(b.getString("province"))) {
                    shengfen = b.getString("province");
                    citys = b.getString("city");
                    quxian = b.getString("area");
                    province_city_tv_content.setText(shengfen + " " + citys + " "
                            + quxian);
                    province_city_tv_content.setEnabled(false);
                    province_city_tv_content.setClickable(false);
                    x = b.getDouble("x");
                    y = b.getDouble("y");
                    jiedao = b.getString("address");
                    address_et.setText(jiedao);
                }
                break;
            case COLOR_ID:
                if (data == null)
                    return;
                bundle = data.getExtras();

                et_pigcolor.setText(bundle.getString("colorname"));
                break;
            case MONEY_TYPE:
                province_city_tv_content.setText(App.PROVINCE + " " + App.CITY
                        + " " + App.AREA);
                address_et.setText(App.ADDRESS);
                x = Double.parseDouble(App.GPS_X);
                y = Double.parseDouble(App.GPS_Y);
                break;
            case LIKETYPE:
                setResult(27);
                finish();
                break;
            case 111:
//			mDataList.clear();
//			mDataList = (List<ImageItem>) getIntent().getSerializableExtra(
//					CustomConstants.EXTRA_IMAGE_LIST);
//			saveTempToPref(CustomConstants.PREF_TEMP_IMAGES, mDataList);
//			notifyDataChanged();
                break;
            default:

                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int witch) {
        switch (witch) {
            case R.id.radio_no:
                orderStatus = "1";
                break;
            case R.id.radio_yes:
                orderStatus = "3";
                break;
        }
    }

    @Override
    protected android.app.Dialog onCreateDialog(int id) {

        return new DateTimePickerDialog(this, mDateSetListener, mYear, mMonth,
                mDay, mHour, mMinute, false);
    }

    @Override
    protected void onPrepareDialog(int id, android.app.Dialog dialog) {

        ((DateTimePickerDialog) dialog).updateDate(mYear, mMonth, mDay, mHour,
                mMinute);
    }

    private DateTimePickerDialog.OnDateSetListener mDateSetListener = new DateTimePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth, TimePicker timeview, int hourOfDay, int minut) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            mHour = hourOfDay;
            mMinute = minut;
            leave_start_timebox_tv_content.setText(updateDisplay(0));
        }
    };

    private String updateDisplay(int i) {

        // Month is 0 based so add 1
        String timestr;
        if (i == 0) {
            timestr = new StringBuilder()
                    .append(mYear)
                    .append("-")
                    .append((mMonth + 1) < 10 ? "0" + (mMonth + 1)
                            : (mMonth + 1)).append("-")
                    .append((mDay < 10) ? "0" + mDay : mDay).append(" ")
                    .append(pad(mHour)).append(":").append(pad(mMinute))
                    .toString();
        } else {
            timestr = new StringBuilder()
                    .append(mYear)
                    .append("-")
                    .append((mMonth + 1) < 10 ? "0" + (mMonth + 1)
                            : (mMonth + 1)).append("-")
                    .append((mDay < 10) ? "0" + mDay : mDay).toString();
        }

        return timestr;
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    class OnClickLintener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_popupwindows_camera: // 相机
                    mFile = getFile();
                    path = getPath(mFile);
                    takePhoto(TAKE_PHOTO, mFile);
                    mPwDialog.dismiss();
                    break;
                case R.id.item_popupwindows_Photo: // 相册

                    // Intent intent = new Intent(NewInfoActivity.this,
                    // TestPicActivity.class);
                    // intent.putExtra(CustomConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
                    // getAvailableSize());
                    // startActivity(intent);

                    takeAlbum(TAKE_PICTURE);
                    mPwDialog.dismiss();
                    break;
                case R.id.item_popupwindows_cancel:// 取消
                    mPwDialog.dismiss();
                    break;
                default:
                    break;
            }

        }

    }

    private int getAvailableSize() {
        int availSize = CustomConstants.MAX_IMAGE_SIZE - mDataList.size();
        if (availSize >= 0) {
            return availSize;
        }
        return 0;
    }

    private void showPopDialog(View parent) {

        // 自定义的单击事件
        OnClickLintener paramOnClickListener = new OnClickLintener();
        mPwDialog = new PopupWindows(NewInfoActivity.this,
                paramOnClickListener, parent);
        mPwDialog.update();
    }

    public void showDialog2(Context context, String msg) {
        final Dialog dialog = new Dialog(context, "提示", msg);
        dialog.addCancelButton("取消");
        dialog.setOnAcceptButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(NewInfoActivity.this,
                        SearchPigActivity.class);
                intent.putExtra("id", id);
                if(type==1){
                    intent.putExtra("type", "匹配收购信息");
                }else if(type==2){
                    intent.putExtra("type", "匹配出售信息");
                }
                startActivityForResult(intent, LIKETYPE);
            }
        });
        dialog.setOnCancelButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                setResult(27);
                finish();
            }
        });
        dialog.show();

    }

    protected void onPause() {
        super.onPause();
        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES, mDataList);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("maose", et_pigcolor.getText().toString());
        outState.putString("pinzhong", pinzhong_tv_content.getText().toString());
        outState.putString("chulan", leave_start_timebox_tv_content.getText()
                .toString());
        outState.putString("peisong", mTvPeiSong.getText().toString());
        outState.putString("url", url);
        outState.putString("id", id);
        super.onSaveInstanceState(outState);
        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES, mDataList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            path = savedInstanceState.getString("path");
            et_pigcolor.setText(savedInstanceState.getString("maose"));
            pinzhong_tv_content.setText(savedInstanceState
                    .getString("pinzhong"));
            leave_start_timebox_tv_content.setText(savedInstanceState
                    .getString("chulan"));
            mTvPeiSong.setText(savedInstanceState.getString("peisong"));
            url = savedInstanceState.getString("url");
            id = savedInstanceState.getString("id");
            initView();
        }
    }

    private void notifyDataChanged() {
        mAdapter.notifyDataSetChanged();
    }

    private void setViewData(InfoEntry info) {
        id = info.getId();
        String pigTypeName = "";
        if (info.getPigType().equals("1")) {
            pigTypeName = "内三元";
        } else if (info.getPigType().equals("2")) {
            pigTypeName = "外三元";
        } else if (info.getPigType().equals("3")) {
            pigTypeName = "土杂猪";
        }
        pigType = info.getPigType();
        pinzhong_tv_content.setText(pigTypeName);

        title_et.setText(info.getTitle());
        price_et.setText(info.getPrice());
        if (type == 1) {
            leave_start_timebox_tv_content.setText(info.getMarketingTime()
                    .substring(0, 16));
        }
        number_et.setText(info.getNumber());
        weight_et.setText(info.getWeight());
        remark_et.setText(info.getRemark());

        address_et.setText(info.getAddress());
        et_pigcolor.setText(info.getColor());

        if (type == 2) {
            price_tv.setText("收购价格：");
            chulan_layout.setVisibility(View.GONE);
            weight_layout.setVisibility(View.GONE);

            profit_layout.setVisibility(View.GONE);
        }

        shengfen = info.getProvince();
        citys = info.getCity();
        quxian = info.getArea();

        province_city_tv_content.setText(info.getProvince() + " "
                + info.getCity() + " " + info.getArea());
        mLocationBtn = (ImageButton) findViewById(R.id.locationBtn);

        if (App.usertype.equals("3")) {
            address_et.setClickable(true);
            address_et.setEnabled(true);
            mLocationBtn.setVisibility(View.VISIBLE);
            province_city_tv_content.setClickable(true);
            province_city_tv_content.setEnabled(true);

        }
        mTvTishi.setVisibility(View.VISIBLE);
        mTvPeiSong.setText(info.getSendType());
        if (!TextUtils.isEmpty(info.getStartPrice())){
            minPrice = info.getStartPrice();
            maxPrice = info.getEndPrice();
        }

    }

    private void getMinandMaxPrice(){
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,UrlEntry.GET_MINMAX_PRICE_URL, null,
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
                        Log.i("result", "responseInfo.result = "
                                + responseInfo.result);
                        JSONObject jsonresult = null;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
//                            if (jsonresult.get("success").toString().equals("1")) {
                                minPrice = jsonresult.optString("startPrice").toString();
                                maxPrice = jsonresult.optString("endPrice").toString();
//                            }else{

//                            }
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

    private void getBankStatus(){
        showDialog();
        btn_submit.setEnabled(false);
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
                        btn_submit.setEnabled(true);
                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("2")) {
                                final Dialog dialog = new Dialog(NewInfoActivity.this, "提示", "为了不影响交易，请先开通快捷支付！");
//                                        dialog.addCancelButton("修改");
                                dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(NewInfoActivity.this, NewElectronicAccountsActivity.class);
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
                            }else  if (jsonresult.get("success").toString()
                                    .equals("1")){
                                sendData();
                            }else{
                                successType(jsonresult.get("success").toString(),"发布失败");
                            }
                        }catch (Exception e){

                        }



                    }
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        showToast("服务器连接失败，请稍候再试！");
                        disDialog();
                        btn_submit.setEnabled(true);
                    }
                });
    }
}
