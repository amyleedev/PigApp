package com.szmy.pigapp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
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
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.szmy.pigapp.R;
import com.szmy.pigapp.daijinquan.DaijinquanListActivity;
import com.szmy.pigapp.entity.ZhuChang;
import com.szmy.pigapp.image.CustomConstants;
import com.szmy.pigapp.image.ImageDisplayer;
import com.szmy.pigapp.image.ImageItem;
import com.szmy.pigapp.image.PopupWindows;
import com.szmy.pigapp.tixian.ActivityWithdrawals;
import com.szmy.pigapp.tixian.Withdrawals;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.SerializableMap;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.DaijinquanPopuWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 猪场认证
 *
 * @author Administrator
 */
public class ZhuChangRenZhengActivity extends BaseActivity {

    private TextView tvAreaP, tvIndustryP, tvBreed, tv_stateP;
    private EditText etCompanyP, etCunNum, etYearNum, etPeopleP, etAddressP,
            etPhoneP, etEmail;
    private Button btn_submitP, btn_modifyP;
    private String province = "";
    private String city = "";
    private String area = "";
    private String pigType = "", pigFarmType = "", id = "";
    private String address = "";
    private ImageButton mLocationBtn;
    public static final int LOCATION_ID = 29;
    public LocationService locationService;
    public Vibrator mVibrator;
    public static double x = 0;
    public static double y = 0;
    public LinearLayout mAddressLayout;
    private String[] mItems = {"内三元", "外三元", "土杂猪","肥猪","仔猪","种猪"};
    private int mSingleChoiceID = 0;
    private String source = "0";
    private TextView tvBank;
    private static final int BANK_CARD_ID = 26;
    private BitmapUtils bitmapUtils;
    private String path = "";
    private ImageView iv_identity;
    private LinearLayout mLlBack;
    private Button mNextBtn, mBackBtn;
    private LinearLayout mLlFirst;
    private LinearLayout mLlTwo;
    private PopupWindows mPwDialog;
    //    private int TAKE_PHOTO;
//    private int TAKE_PICTURE;
    public List<ImageItem> mDataList1 = new ArrayList<ImageItem>();
    private String imagepath1 = "";
    private ImageView mImgSfzZm;
    private ImageView mCamSfzZm;
    private ImageView mImgSfzFm;
    private ImageView mCamSfzFm;
    private EditText etCardNum;
    private EditText etCardName;
    private EditText etIdentityCard;// 身份证
    private Map<String, ImageItem> picMaps = new HashMap<>();
    private Map<String, String> urlmaps = new HashMap<>();
    public String picUrl = "";
    public String type = "";
    public String picTypeName = "";
    private File mFile;
    private final int TAKE_PHOTO = 0x0000134;
    private final int TAKE_PICTURE = 0x0000135;
    private final int CORP_TAKE_PICTURE1 = 0x0000136;
    private Withdrawals mWdwEntry;
    private ZhuChang mInfoEntry;
    private TextView tvCardType;
    private String[] mPsItems = new String[]{"农业银行卡", "农村信用社"};
    private int mSingleChoiceIDs = 0;
    private  String banktype ="";
    private DaijinquanPopuWindow mDjqPwDialog;
    private Map<String,String> map = new HashMap<>();
    public static ZhuChangRenZhengActivity instance = null;
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(@Nullable Bundle  arg0) {
        super.onCreate(arg0);
        instance = this;
        if (arg0 != null) {
            picUrl = arg0.getString("yezzimagepath1");
            type = arg0.getString("type");
        }
        setContentView(R.layout.activity_pigfarm_auth);
        ((TextView) findViewById(R.id.def_head_tv)).setText("认证猪场");
        SharedPreferences sp = getSharedPreferences(App.USERINFO, 0);
        if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
            App.mUserInfo = FileUtil.readUser(ZhuChangRenZhengActivity.this);
            App.setDataInfo(App.mUserInfo);
        }
        bitmapUtils = new BitmapUtils(this);
        initView();
        showDialog();
        RequestParams params = new RequestParams();
        System.out.println(App.uuid);
        params.addBodyParameter("uuid", App.uuid);
        sendDataToHttp(UrlEntry.QUERY_PIGFARM_URL, params, "");
//		// 定位

        if ((AppStaticUtil.getConvertDouble(App.GPS_X) == 0.0)
                || (AppStaticUtil.getConvertDouble(App.GPS_Y) == 0.0)) {
            if (AppStaticUtil.isNetwork(getApplicationContext())) {
                locationService = new LocationService(getApplicationContext());
                locationService.registerListener(mListener);
            }
        } else {
        }

    }

    /*****
     * 啊@see copy funtion to you project
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location
                    && location.getLocType() != BDLocation.TypeServerError) {
                if ((162 <= location.getLocType()) && (location.getLocType() <= 167)) {
                    tvAreaP.setEnabled(true);
                    tvAreaP.setClickable(true);
                    showToast("定位失败，请重新进入(建议打开GPS)!");
                    return;
                }
                x = location.getLatitude();
                y = location.getLongitude();

                if (location.getProvince() == null || location.getCity() == null || location.getDistrict() == null) {
                    showToast("获取当前位置信息失败，请重新进入(建议打开GPS)!!");
                    return;
                }
                province = location.getProvince();
                city = location.getCity();
                area = location.getDistrict();
                address = location.getStreet() + location.getStreetNumber();
                if (!TextUtils.isEmpty(city)
                        && city.contains(province)) {
                    tvAreaP.setText(province + area);
                } else {
                    tvAreaP.setText(province + city
                            + area);
                }

                etAddressP.setText(address);
//                tvAreaP.setEnabled(false);
//                tvAreaP.setClickable(false);
            } else {
                tvAreaP.setEnabled(true);
                tvAreaP.setClickable(true);
                showToast("定位失败，请重新进入(建议打开GPS)!");
            }
        }
    };

    private void initView() {
        tvAreaP = (TextView) findViewById(R.id.tvAreaP);
        tvIndustryP = (TextView) findViewById(R.id.tvIndustryP);
        tvBreed = (TextView) findViewById(R.id.tvBreed);
        etCompanyP = (EditText) findViewById(R.id.etCompanyP);
        etCunNum = (EditText) findViewById(R.id.etCunNum);
        etYearNum = (EditText) findViewById(R.id.etYearNum);
        etPeopleP = (EditText) findViewById(R.id.etPeopleP);
        etAddressP = (EditText) findViewById(R.id.etAddressP);
        etPhoneP = (EditText) findViewById(R.id.etPhoneP);
        etEmail = (EditText) findViewById(R.id.etEmail);
        tvBank = (TextView) findViewById(R.id.tvBank);
        tv_stateP = (TextView) findViewById(R.id.tv_stateP);
        btn_submitP = (Button) findViewById(R.id.btn_submitP);
        btn_modifyP = (Button) findViewById(R.id.btn_modifyP);
        mLocationBtn = (ImageButton) findViewById(R.id.locationBtn);
        mAddressLayout = (LinearLayout) findViewById(R.id.addresslayout);
        iv_identity = (ImageView) findViewById(R.id.iv_identity);
        mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
        mLlFirst = (LinearLayout) findViewById(R.id.layoutone);
        mLlTwo = (LinearLayout) findViewById(R.id.layouttwo);
        mNextBtn = (Button) findViewById(R.id.btn_next);
        mImgSfzFm = (ImageView) findViewById(R.id.iv_fidentity);
        mCamSfzFm = (ImageView) findViewById(R.id.iv_fcamera);
        mImgSfzZm = (ImageView) findViewById(R.id.iv_zidentity);
        mCamSfzZm = (ImageView) findViewById(R.id.iv_camera);
        etCardNum = (EditText) findViewById(R.id.etCardNum);
        etCardName = (EditText) findViewById(R.id.etCardName);
        etIdentityCard = (EditText) findViewById(R.id.etIdentityCard);
        mBackBtn = (Button) findViewById(R.id.btn_goback);
        tvCardType = (TextView) findViewById(R.id.tvCardType);
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
//                if ((mLlTwo.getVisibility() == View.VISIBLE)
//                        && (mLlFirst.getVisibility() == View.GONE)) {
//                    mLlTwo.setVisibility(View.GONE);
//                    mLlFirst.setVisibility(View.VISIBLE);
//                } else if ((mLlTwo.getVisibility() == View.GONE)
//                        && (mLlFirst.getVisibility() == View.VISIBLE)) {
//                    clearTempFromPref();
//                    picMaps.clear();
//                    urlmaps.clear();
//                    finish();
//                }


            }
        });
//        mDataList1 = getTempFromPref(CustomConstants.PREF_TEMP_IMAGES);
        setImage();
    }
    private boolean isNullStr(){
        hideInput();
        if (TextUtils.isEmpty(tvAreaP.getText().toString())
                || TextUtils.isEmpty(tvIndustryP.getText().toString())
                || TextUtils.isEmpty(tvBreed.getText().toString())
                || TextUtils.isEmpty(etCunNum.getText().toString())
                || TextUtils.isEmpty(etYearNum.getText().toString())
                || TextUtils.isEmpty(etPeopleP.getText().toString())
                || TextUtils.isEmpty(etAddressP.getText().toString())
                || TextUtils.isEmpty(etPhoneP.getText().toString()) || TextUtils.isEmpty(tvBank.getText().toString())
                ) {
            showToast("带'*'号为必填项,不能为空,请完善！");
            return false;
        }
        if (!TextUtils.isEmpty(etCompanyP.getText().toString()) && FileUtil.isNumeric(etCompanyP.getText().toString())) {
            showToast("公司名称不能为纯数字！");
            return false;
        }
        if (etPhoneP.getText().toString().length() != 11) {
            showToast("请输入11位手机号码！");
            return false;
        }
        return true;
    }
    /**
     * @param zcinfo
     */
    private void setDataToView(ZhuChang zcinfo) {
        mInfoEntry = zcinfo;
        if (source.equals("1")) {
            if (!TextUtils.isEmpty(zcinfo.getName())) {
                etCompanyP.setEnabled(false);
                etCompanyP.setTextColor(getResources().getColor(R.color.gray));
            }
            if (!TextUtils.isEmpty(zcinfo.getUsername())) {
                etPeopleP.setEnabled(false);
                etPeopleP.setTextColor(getResources().getColor(R.color.gray));
            }
            if (!TextUtils.isEmpty(zcinfo.getAmount())) {
                etCunNum.setEnabled(false);
                etCunNum.setTextColor(getResources().getColor(R.color.gray));
            }
        }
        id = zcinfo.getId();
        province = zcinfo.getProvince();
        city = zcinfo.getCity();
        area = zcinfo.getArea();
        address = zcinfo.getAddress();
        x = AppStaticUtil.getConvertDouble(zcinfo.getX());
        y = AppStaticUtil.getConvertDouble(zcinfo.getY());
        tvAreaP.setText(zcinfo.getProvince() + " " + zcinfo.getCity() + " "
                + zcinfo.getArea());
        pigFarmType = zcinfo.getType();
        if (zcinfo.getType().equals("1")) {
            tvIndustryP.setText("小规模养殖");
        } else if (zcinfo.getType().equals("2"))
            tvIndustryP.setText("中规模养殖");
        else if (zcinfo.getType().equals("3"))
            tvIndustryP.setText("大规模养殖");
        else if (zcinfo.getType().equals("4"))
            tvIndustryP.setText("集团养殖");
        String name = "";
        if (zcinfo.getPigType().equals("1")) {
            name = "内三元";
        } else if (zcinfo.getPigType().equals("2")) {
            name = "外三元";
        } else if (zcinfo.getPigType().equals("3")) {
            name = "土杂猪";
        } else if (zcinfo.getPigType().equals("4")) {
            name = "肥猪";
        } else if (zcinfo.getPigType().equals("5")) {
            name = "仔猪";
        } else if (zcinfo.getPigType().equals("6")) {
            name = "种猪";
        }
        tvBreed.setText(name);
        etCompanyP.setText(zcinfo.getName());
        etCunNum.setText(zcinfo.getAmount());
        etYearNum.setText(zcinfo.getSales());
        etPeopleP.setText(zcinfo.getUsername());
        etAddressP.setText(zcinfo.getAddress());
        etPhoneP.setText(zcinfo.getPhone());
        etEmail.setText(zcinfo.getEmail());
        if (zcinfo.getIsSmrzBank().equals("n")) {
            tvBank.setText("未绑定银行卡");
        } else if (zcinfo.getIsSmrzBank().equals("y")) {
            tvBank.setText("已绑定银行卡");
        }
        if (zcinfo.getStatus().equals("2")) {
            tv_stateP.setText("审核通过");
        } else if (zcinfo.getStatus().equals("3")) {
            tv_stateP.setText("审核未通过,原因：" + zcinfo.getReason());
        } else if (zcinfo.getStatus().equals("1")) {
            tv_stateP.setText("审核中");
        }
        if (!TextUtils.isEmpty(zcinfo.getCoverPicture())) {
            urlmaps.put("fmtp",zcinfo.getCoverPicture().toString());
        }
        setImage();
    }

    public class MyRunnable implements Runnable {

        private String type;
        private String url;

        public MyRunnable(String type, String url) {
            this.type = type;
            this.url = url;
        }

        @Override
        public void run() {
            String result = "";
            Bundle data = new Bundle();
            Message msg = new Message();
            MultipartEntity params = new MultipartEntity();

            try {
                if (type.equals("update")) {
                    params.addPart("e.id", new StringBody(id));
                }
                if(picMaps.containsKey("fmtp")){
                    params.addPart("uploadImage", new FileBody(new File(picMaps.get("fmtp").sourcePath)));
                }
                params.addPart("e.province", new StringBody(province));
                params.addPart("e.city", new StringBody(city));
                params.addPart("e.area", new StringBody(area));
                params.addPart("e.type", new StringBody(pigFarmType));
                System.out.println("猪场类型：" + new StringBody(pigFarmType));
                params.addPart("e.name", new StringBody(etCompanyP.getText().toString()));
                params.addPart("e.amount", new StringBody(etCunNum.getText().toString()));
                params.addPart("e.sales", new StringBody(etYearNum.getText().toString()));
                params.addPart("e.pigType", new StringBody(pigType));
                params.addPart("e.username", new StringBody(etPeopleP.getText().toString()));
                params.addPart("e.address", new StringBody(etAddressP.getText().toString()));
                params.addPart("e.phone", new StringBody(etPhoneP.getText().toString()));
                params.addPart("e.email", new StringBody(etEmail.getText().toString()));
                params.addPart("e.x", new StringBody(String.valueOf(x)));
                params.addPart("e.y", new StringBody(String.valueOf(y)));
                params.addPart("e.userID", new StringBody(App.userID));
                params.addPart("uuid", new StringBody(App.uuid));
                HttpUtil http = new HttpUtil();
                result = http.postDataMethod(url, params);

                data.putString("value", result);
                msg.setData(data);
                handler.sendMessage(msg);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
//            disDialog();
            Bundle data1 = msg.getData();
            String result = data1.getString("value");
            if (result.equals("")) {
                showToast("连接服务器失败,请稍后再试！");
                disDialog();
                return;
            }


            JSONObject jsonresult;
            try {
                jsonresult = new JSONObject(result);
                if (jsonresult.get("success").toString()
                        .equals("1")) {
                    if (msg.what==110){
                        showToast("提交成功");
                        picMaps.clear();
                        urlmaps.clear();
                        clearMapTempFronPref();
                        finish();

                        return;
                    }
                    ZhuChang mInfoEntry;
                    // 新增或修改
//                    showToast("提交成功！");
                    sp = getSharedPreferences(App.USERINFO, 0);
                    if (App.mUserInfo != null) {
                        App.mUserInfo.setX(String.valueOf(x));
                        App.mUserInfo.setY(String.valueOf(y));
                        App.mUserInfo.setProvince(province);
                        App.mUserInfo.setCity(city);
                        App.mUserInfo.setArea(area);
                        App.mUserInfo.setAddress(etAddressP
                                .getText().toString());
                        App.mUserInfo.setAuthentication("1");
                        FileUtil.saveUser(
                                ZhuChangRenZhengActivity.this,
                                App.mUserInfo);
                        App.setDataInfo(App.mUserInfo);
                    }


                    MyBankRunnable bankrun = null;
                    if (mWdwEntry!=null&& !TextUtils.isEmpty(mWdwEntry.getId())) {

                        bankrun=  new MyBankRunnable("update",UrlEntry.UPP_CARD_URL);
                    }else{

                        bankrun= new MyBankRunnable("add",UrlEntry.ADD_CARD_URL);
                    }
                    new Thread(bankrun).start();
                } else if (jsonresult.get("success").toString()
                        .equals("4")) {
                    disDialog();
                } else if(jsonresult.get("success").toString()
                        .equals("5")){
                    showToast("操作失败"+jsonresult.get("msg").toString());
                    disDialog();
                } else {
                    successType(jsonresult.get("success")
                            .toString(), "操作失败！");
                    disDialog();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                disDialog();
            }

        }
    };


    private void sendData(String url, String type) {
        showDialog();
        MyRunnable run = new MyRunnable(type, url);
        new Thread(run).start();
    }

    public void sendDataToHttp(String url, RequestParams params,
                               final String type) {

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
                        Log.i("zhuchang", " zhuchang responseInfo.result = "
                                + responseInfo.result);
                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {
//                                ZhuChang mInfoEntry;
                                {
                                    if (jsonresult.has("source")) {
                                        if (jsonresult.optString("source")
                                                .equals("1")) {
                                            source = "1";
                                        }
                                    }
//                                    btn_submitP.setVisibility(View.GONE);
//                                    btn_modifyP.setVisibility(View.VISIBLE);
                                    GsonBuilder gsonb = new GsonBuilder();
                                    Gson gson = gsonb.create();
                                    mInfoEntry = gson.fromJson(
                                            jsonresult.toString(),
                                            ZhuChang.class);
                                    setDataToView(mInfoEntry);
                                }
                            } else if (jsonresult.get("success").toString()
                                    .equals("4")) {
                                if (jsonresult.has("isSmrzBank")) {
                                    if (jsonresult.optString("isSmrzBank").equals("n")) {
                                        tvBank.setText("未绑定银行卡");
                                    } else if (jsonresult.optString("isSmrzBank").equals("y")) {
                                        tvBank.setText("已绑定银行卡");
                                    }
                                }
                                if (jsonresult.has("source")) {
                                    if (jsonresult.optString("source").equals(
                                            "1")) {
                                        source = "1";
                                        GsonBuilder gsonb = new GsonBuilder();
                                        Gson gson = gsonb.create();
                                        ZhuChang mInfoEntry = gson.fromJson(
                                                jsonresult.toString(),
                                                ZhuChang.class);
                                        setDataToView(mInfoEntry);
                                    }
                                }
                            } else {
                                successType(jsonresult.get("success")
                                        .toString(), "操作失败！");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        showToast("操作失败,请稍后再试！");
                        disDialog();
                    }
                });

    }

    public void onClick(View v) {
        Intent locintent;
        AlertDialog.Builder builder;
        switch (v.getId()) {
            case R.id.tvAreaP:
                final Dialog dialog = new Dialog(this, "提示", "是否需要重新定位？");
                dialog.addCancelButton("取消");
                dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        if (AppStaticUtil.isNetwork(getApplicationContext())) {
                            locationService = new LocationService(getApplicationContext());
                            locationService.registerListener(mListener);
                        }else{
                            showToast("网络连接失败，请稍候再试");
                            x = AppStaticUtil.getConvertDouble(App.GPS_X);
                            y = AppStaticUtil.getConvertDouble(App.GPS_Y);
                        }
                    }
                });
                dialog.setOnCancelButtonClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        x = AppStaticUtil.getConvertDouble(App.GPS_X);
                        y = AppStaticUtil.getConvertDouble(App.GPS_Y);
                    }
                });
                dialog.show();
                // Intent intent = new Intent(ZhuChangRenZhengActivity.this,
                // ChoseProvinceActivity.class);
                // intent.putExtra("add", "add");
                // startActivityForResult(intent, 22);
//                locintent = new Intent(ZhuChangRenZhengActivity.this,
//                        LocationMapActivity.class);
//                locintent.putExtra("x", x);
//                locintent.putExtra("y", y);
//                startActivityForResult(locintent, LOCATION_ID);
                break;
            case R.id.tvIndustryP:
                // Intent typeintent = new Intent(ZhuChangRenZhengActivity.this,
                // ChosePigFarmTypeActivity.class);
                // startActivityForResult(typeintent, 25);

                builder = new AlertDialog.Builder(ZhuChangRenZhengActivity.this);

                mSingleChoiceID = 0;
                builder.setTitle("请选择");
                builder.setSingleChoiceItems(R.array.pigtype, 0,
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
                                pigFarmType = String.valueOf(mSingleChoiceID + 1);
                                tvIndustryP.setText(getResources().getStringArray(
                                        R.array.pigtype)[mSingleChoiceID]);

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
            case R.id.tvBreed:
                // Intent pzintent = new Intent(ZhuChangRenZhengActivity.this,
                // ChoseTypeActivity.class);
                // startActivityForResult(pzintent, 23);

                builder = new AlertDialog.Builder(ZhuChangRenZhengActivity.this);

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

                                tvBreed.setText(mItems[mSingleChoiceID]);

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
            case R.id.btn_next:
                if(!isNullStr())return;

                getBankInfo("");
                break;
            case R.id.btn_submitP:
                if ( TextUtils.isEmpty(etCardName.getText().toString())) {
                    showToast("请输入真实姓名！");
                    return;
                }
                if(!TextUtils.isEmpty(etIdentityCard.getText()
                        .toString())) {
                    String str = FileUtil.IDCardValidate(etIdentityCard.getText()
                            .toString());
                    if (!str.equals("")) {
                        showToast(str);
                        return;
                    }
                }

                if(!TextUtils.isEmpty(etCardNum.getText().toString())){
                    if (tvCardType.getText().toString().equals("农业银行卡")) {
                        String message = FileUtil.isAgricultureCard(etCardNum.getText().toString());
                        if (!message.contains("农业银行")) {
                            showToast(message);
                            return;
                        }
                    }

                }
                String msg = "";
                msg =isPic("sfzzm","身份证正面图片");
                if(msg.equals("ok")){
                }else{
                    if (!msg.equals("")){
                        showToast(msg);
                        return;
                    }
                }

                msg = isPic("sfzfm","身份证反面图片");
                if(msg.equals("ok")){
                }else{
                    if (!msg.equals("")){
                        showToast(msg);
                        return;
                    }
                }
                //2017-3-20 认证信息和银行卡信息一起提交 先提交认证信息 成功后提交银行卡信息
                if(mInfoEntry==null||TextUtils.isEmpty(mInfoEntry.getId())){
                    sendData(UrlEntry.INSERT_PIGFARM_URL, "add");// add
                }else{
                    sendData(UrlEntry.UPDATE_PIGFARM_URL, "update");// update
                }

//                sendData();//提交银行卡信息
                break;

            case R.id.locationBtn:
                locintent = new Intent(ZhuChangRenZhengActivity.this,
                        LocationMapActivity.class);
                locintent.putExtra("x", x);
                locintent.putExtra("y", y);
                startActivityForResult(locintent, LOCATION_ID);
                break;
            case R.id.tvBank:
                clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES1);
                clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES2);
                locintent = new Intent(ZhuChangRenZhengActivity.this, ActivityWithdrawals.class);
                locintent.putExtra("type", "rz");
                startActivityForResult(locintent, BANK_CARD_ID);
                break;
            case R.id.iv_camerasfzzm:
            case R.id.iv_zidentity:

                picTypeName = "sfzzm";
                showPopDialog(mImgSfzZm);
                break;
            case R.id.iv_camerasfzfm:
            case R.id.iv_fidentity:
                ;
                picTypeName = "sfzfm";
                showPopDialog(mImgSfzFm);
                break;
            case R.id.iv_identity:

                picTypeName = "fmtp";
                showPopDialog(iv_identity);
                break;
            case R.id.tvCardType:
                 builder = new AlertDialog.Builder(
                        ZhuChangRenZhengActivity.this);

                mSingleChoiceIDs = 0;
                builder.setTitle("请选择");
                builder.setSingleChoiceItems(mPsItems, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                mSingleChoiceIDs = whichButton;

                            }
                        });
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                tvCardType.setText(mPsItems[mSingleChoiceIDs]);

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
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("resultCode" + resultCode);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == -1 && !TextUtils.isEmpty(picUrl)) {
                    picMaps.put(picTypeName, FileUtil.saveDataPic(picUrl));
                    saveMapTempToPref(picMaps);
                }
                setImage();
                break;
            case TAKE_PICTURE:
                if (resultCode != -1) {
                    return;
                }
                if (data == null) {
                    return;
                }
                picUrl = getPath(getFile());
                startPhotoZoom1(data, picUrl, CORP_TAKE_PICTURE1);
                break;
            case CORP_TAKE_PICTURE1:
                if (resultCode != -1) {
                    return;
                }
                picMaps = setPicToView(data, picUrl, picMaps, picTypeName);
                saveMapTempToPref(picMaps);
                setImage();
                break;

            case 22:
                if (data == null)
                    return;
                Bundle bundle = data.getExtras();
                province = bundle.getString("province");
                city = bundle.getString("city");
                area = bundle.getString("area");
                tvAreaP.setText(province + " " + city + " " + area);
                break;
            case 23:
                if (data == null)
                    return;
                Bundle b = data.getExtras();
                pigType = b.getString("pigType");
                String pigTypeName = b.getString("pigTypeName");
                tvBreed.setText(pigTypeName);
                break;
            case 25:
                if (data == null)
                    return;
                Bundle bt = data.getExtras();
                pigFarmType = bt.getString("pigFarmType");
                String pigFarmTypeName = bt.getString("pigFarmTypeName");
                tvIndustryP.setText(pigFarmTypeName);
                break;
            case LOCATION_ID:
                if (data == null)
                    return;
                b = data.getExtras();
                if (!TextUtils.isEmpty(b.getString("province"))) {
                    province = b.getString("province");
                    city = b.getString("city");
                    area = b.getString("area");
                    tvAreaP.setText(province + " " + city + " " + area);
                    x = b.getDouble("x");
                    y = b.getDouble("y");
                    address = b.getString("address");
                    etAddressP.setText(address);
                }
                break;
            case BANK_CARD_ID:
                if (resultCode != 0) {
                    tvBank.setText("已绑定银行卡");
                }
                break;
            default:
                break;
        }
    }

    private void setImage()
    {
        setImage("sfzzm", mImgSfzZm);
        setImage("sfzfm", mImgSfzFm);
        setImage("fmtp", iv_identity);
    }
    private void setImage(String name, ImageView view) {
        if (picMaps.size() == 0) {
            picMaps = readMapTempFromPref();
        }
        if (picMaps.containsKey(name)) {
            final ImageItem item = picMaps.get(name);
            ImageDisplayer.getInstance(this).displayBmp(view,
                    item.thumbnailPath, item.sourcePath);
        } else {
            if (urlmaps.containsKey(name)) {
                bitmapUtils.display(view, UrlEntry.ip
                        + urlmaps.get(name));// 下载并显示图片
            } else {
                if (name.equals("sfzzm")) {
                    view.setBackgroundResource(R.drawable.ic_sfzz);
                } else if (name.equals("sfzfm")) {
                    view.setBackgroundResource(R.drawable.ic_sfzf);
                }
            }
        }
    }
    private String  isPic(String pictype,String message){
        if (picMaps.containsKey(pictype)){
            if (new File(picMaps.get(pictype).sourcePath).exists()){
                return "ok";
            }else{
                return message+"选取错误，请重新选取。";
            }
        }else{
            if (urlmaps.containsKey(pictype)) {
                return "ok";
            }else

                return "请上传您的"+message;

        }

    }
//    private void initImage() {
//        if (isShowAddItem(mDataList1, 0)) {
//
//        } else {
//            final ImageItem item = mDataList1.get(0);
//            ImageDisplayer.getInstance(this).displayBmp(iv_identity,
//                    item.thumbnailPath, item.sourcePath);
//            imagepath1 = item.sourcePath;
//        }
//
//    }

    protected void onPause() {
        super.onPause();
//        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES, mDataList1);

    }


    @Override
    protected void onResume() {
        super.onResume();
        // 返回这里需要刷新
        initView();
    }

    /****************************************************************/
    class OnClickLintener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_popupwindows_camera: // 相机
                    mPwDialog.dismiss();
                    mFile = getFile();
                    picUrl = getPath(mFile);
                    takePhoto(TAKE_PHOTO, mFile);
                    break;
                case R.id.item_popupwindows_Photo: // 相册
                    takeAlbum(TAKE_PICTURE);
                    mPwDialog.dismiss();
                    break;
                case R.id.item_popupwindows_cancel:// 取消
                    mPwDialog.dismiss();
                    break;
                case R.id.close:
                    mDjqPwDialog.dismiss();
                    finish();
                    break;
                case R.id.btn_look:
                    mDjqPwDialog.dismiss();
                    Intent intent = new Intent(ZhuChangRenZhengActivity.this, DaijinquanListActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }

        }

    }

    private void showPopDialog(View parent) {

        // 自定义的单击事件
        OnClickLintener paramOnClickListener = new OnClickLintener();
        mPwDialog = new PopupWindows(ZhuChangRenZhengActivity.this,
                paramOnClickListener, parent);
        // 如果窗口存在，则更新
        mPwDialog.update();
    }
    private void setView(Withdrawals winfo) {
//		id = winfo.getId();
        if (winfo.getType().equals("nyBank")
                || winfo.getType().equals("nybank")) {
            tvCardType.setText("农业银行卡");

        } else if (winfo.getType().equals("ncxys")) {
            tvCardType.setText("农村信用社");

        }else{
            tvCardType.setText("农业银行卡");
        }
        etCardNum.setText(winfo.getBankNum());
        etCardName.setText(winfo.getName());
        etIdentityCard.setText(winfo.getSfzh());

        if (!TextUtils.isEmpty(winfo.getSfzZm())) {
            urlmaps.put("sfzzm",winfo.getSfzZm().toString());
        }
        if (!TextUtils.isEmpty(winfo.getSfzFm())) {
            urlmaps.put("sfzfm",winfo.getSfzFm().toString());

        }

        setImage();


    }
    private void getBankInfo(String id){
//        mInfoEntry.setId(id);
//        mLlFirst.setVisibility(View.GONE);
//        mLlTwo.setVisibility(View.VISIBLE);
//        showDialog();
//        sendDataByUUid();


        map = new HashMap<String, String>();

        if(picMaps.containsKey("fmtp")){
            map.put("uploadImage", picMaps.get("fmtp").sourcePath);
        }
        map.put("e.province", province);
        map.put("e.city", city);
        map.put("e.area", area);
        map.put("e.type", pigFarmType);
//                System.out.println("猪场类型：" + new StringBody(pigFarmType));
        map.put("e.name", etCompanyP.getText().toString());
        map.put("e.amount", etCunNum.getText().toString());
        map.put("e.sales", etYearNum.getText().toString());
        map.put("e.pigType", pigType);
        map.put("e.username", etPeopleP.getText().toString());
        map.put("e.address",etAddressP.getText().toString());
        map.put("e.phone", etPhoneP.getText().toString());
        map.put("e.email", etEmail.getText().toString());
        map.put("e.x", String.valueOf(x));
        map.put("e.y", String.valueOf(y));
        map.put("e.userID", App.userID);
        String url = "";
        if(mInfoEntry==null||TextUtils.isEmpty(mInfoEntry.getId())){
            url = UrlEntry.INSERT_PIGFARM_URL;// add
        }else{
            url = UrlEntry.UPDATE_PIGFARM_URL;// update
            map.put("e.id",mInfoEntry.getId());
        }
        Intent intent = new Intent(ZhuChangRenZhengActivity.this, ActivityWithdrawals.class);
        intent.putExtra("renzhengtype","zhuchang");
        //传递数据
        final SerializableMap myMap=new SerializableMap();
        myMap.setDatamap(map);//将map数据添加到封装的myMap中
        Bundle bundle=new Bundle();
        bundle.putSerializable("map", myMap);
        bundle.putString("url",url);
        if (mInfoEntry!=null&&!TextUtils.isEmpty(mInfoEntry.getId())) {
            bundle.putString("status", mInfoEntry.getStatus());
            bundle.putString("reason", mInfoEntry.getReason());
        }
        intent.putExtras(bundle);
        startActivity(intent);


    }
//    private void sendDataByUUid() {
//
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
//                                mWdwEntry = gson.fromJson(
//                                        jsonresult.toString(),
//                                        Withdrawals.class);
//
//                                setView(mWdwEntry);
//                            } else if (jsonresult.get("success").toString()
//                                    .equals("5")) {
//                                GsonBuilder gsonb = new GsonBuilder();
//                                Gson gson = gsonb.create();
//                                mWdwEntry = null;
//                                mWdwEntry = gson.fromJson(
//                                        jsonresult.toString(),
//                                        Withdrawals.class);
//
//                                setView(mWdwEntry);
//                            } else {
//                                successType(jsonresult.get("success")
//                                        .toString(), "操作失败！");
//                                btn_submitP.setEnabled(false);
//                            }
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            btn_submitP.setEnabled(false);
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(HttpException error, String msg) {
//                        showToast("服务器连接失败，请稍候再试！");
//                        btn_submitP.setEnabled(false);
//                        disDialog();
//
//                    }
//                });
//    }


    public class MyBankRunnable implements Runnable {

        private String type;
        private String url;

        public MyBankRunnable(String type, String url) {
            this.type = type;
            this.url = url;
        }

        @Override
        public void run() {
            String result = "";
            Bundle data = new Bundle();
            Message msg = new Message();
            MultipartEntity params = new MultipartEntity();
            try {
                if (type.equals("update")) {
                    params.addPart("e.id", new StringBody(mWdwEntry.getId()));
                }
                if(picMaps.containsKey("sfzzm")){
                    params.addPart("sfzZm",new FileBody(new File(picMaps.get("sfzzm").sourcePath)));
                }
                if(picMaps.containsKey("sfzfm")){
                    params.addPart("sfzFm",new FileBody(new File(picMaps.get("sfzfm").sourcePath)));
                }
                params.addPart("sfzh", new StringBody(etIdentityCard.getText().toString()));
                params.addPart("e.bankNum", new StringBody(etCardNum.getText().toString()));
                params.addPart("e.name", new StringBody( etCardName.getText().toString()));
                String type = "";
                if (tvCardType.getText().toString().equals("农业银行卡")) {
                    type = "nyBank";
                } else if (tvCardType.getText().toString().equals("农村信用社")) {
                    type = "ncxys";
                }
                params.addPart("e.type", new StringBody(type));

                params.addPart("uuid", new StringBody(App.uuid));
                HttpUtil http = new HttpUtil();
                result = http.postDataMethod(url, params);

                data.putString("value", result);
                 msg.what = 110;
                msg.setData(data);
                handler.sendMessage(msg);

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("yezzimagepath1", picUrl);
        outState.putString("picTypeName", picTypeName);
//        outState.putSerializable();
        outState.putString("type", type);

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            picUrl = savedInstanceState.getString("yezzimagepath1");
            picTypeName = savedInstanceState.getString("picTypeName");
            type = savedInstanceState.getString("type");
            initView();
        }
    }
}
