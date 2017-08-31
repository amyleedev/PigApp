package com.szmy.pigapp.distributor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.service.LocationService;
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
import com.szmy.pigapp.activity.ActivityScanResultXieyi;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.activity.ChoseProvinceActivity;
import com.szmy.pigapp.image.CustomConstants;
import com.szmy.pigapp.image.ImageDisplayer;
import com.szmy.pigapp.image.ImageItem;
import com.szmy.pigapp.image.PopupWindows;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 加盟分销商
 *
 * @author Administrator
 */
public class JoinDistributorActivity extends BaseActivity implements
        OnClickListener {

    private TextView tv_state,mEtUsername;
    private EditText  mEtJoinType, mEtPhone, mEtArea,
            mEtAddress;
    private Button btn_submit, btn_modify;
    private String province = "";
    private String city = "";
    private String area = "";
    private String pigFarmType = "", id = "";
    private BitmapUtils bitmapUtils;
    private String imgpath = "";

    private ImageButton mLocationBtn;
    public static final int LOCATION_ID = 29;
    public LocationService locationService;
    public Vibrator mVibrator;
    public static double x = 0;
    public static double y = 0;
    public LinearLayout mAddressLayout;
    public String source = "0";
    private static final int SCALE = 5;// 照片缩小比例

    private final int BANK_CARD_ID = 25;
    private LinearLayout mLlBack;
    private Button mNextBtn, mBackBtn;
    private LinearLayout mLlFirst;
    private LinearLayout mLlTwo;
    private Boolean add = false;
    private LinearLayout mLlYxs;
    private ImageView mIvDpmt, mIvYezz, mIvGsp;
    private ImageView mIvSfzzm, mIvSfzfm;
    private String joinType = "";
    private String yezzoldpath = "";
    private String gspoldpath = "";
    private String mtoldpath = "";
    private String sfzzmoldpath = "";
    private String sfzfmoldpath = "";
    private String yezzimagepath = "", yezzimagepath1 = "";
    private String gspimagepath = "", gspimagepath1 = "";
    private String sfzzmimagepath = "", sfzzmimagepath1 = "";
    private String sfzfmimagepath = "", sfzfmimagepath1 = "";
    private String mtimagepath = "", mtimagepath1 = "";
    public List<ImageItem> mDataList1 = new ArrayList<ImageItem>();
    public List<ImageItem> mDataList2 = new ArrayList<ImageItem>();
    public List<ImageItem> mDataList3 = new ArrayList<ImageItem>();
    public List<ImageItem> mDataList4 = new ArrayList<ImageItem>();
    public List<ImageItem> mDataList5 = new ArrayList<ImageItem>();
    private int TAKE_PHOTO;
    private int TAKE_PICTURE;
    private PopupWindows mPwDialog;
    private File mFile;
    private ImageView mIvMtShili;
    private CheckBox mCheckBtn;
    private TextView mTvXieyi;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.join_distributor_activity);
        bitmapUtils = new BitmapUtils(this);
        ((TextView) findViewById(R.id.def_head_tv)).setText("加盟分销商");
        SharedPreferences sp = getSharedPreferences(App.USERINFO, 0);
        if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
            App.mUserInfo = FileUtil.readUser(JoinDistributorActivity.this);
            App.setDataInfo(App.mUserInfo);
        }
        initView();
        // 定位
        if (AppStaticUtil.isNetwork(getApplicationContext())) {
            locationService = new LocationService(getApplicationContext());
            locationService.registerListener(mListener);

        }

        showDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        sendDataToHttp(UrlEntry.QUERY_DISTRIBUTOR_URL, params, "");
    }

    /*****
     * copy funtion to you project
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location
                    && location.getLocType() != BDLocation.TypeServerError) {
                x = location.getLatitude();
                y = location.getLongitude();

                // address = location.getStreet() + location.getStreetNumber();

                province = location.getProvince();
                city = location.getCity();
                area = location.getDistrict();
                if (TextUtils.isEmpty(province)
                        || province.contains("null")) {
                    showToast("定位失败，请确认是否开启定位服务！");
                    return;
                }
                mEtArea.setText(province + " " + city + " " + area);
                mEtAddress.setText(location.getStreet() + location.getStreetNumber());

                System.out.println(x + "----------------" + y);
                System.out.println("addrStr" + location.getAddrStr());
                System.out.println(location.getProvince() + "-"
                        + location.getCity() + "-" + location.getDistrict()
                        + "-" + location.getStreet() + "-"
                        + location.getStreetNumber());
                locationService.stop();
            } else {

            }
        }
    };

    private void initView() {
        mIvMtShili = (ImageView) findViewById(R.id.iv_dpmtsyt);
        mCheckBtn = (CheckBox) findViewById(R.id.check_fxs);
        mTvXieyi = (TextView) findViewById(R.id.xieyi_txt);
        mTvXieyi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinDistributorActivity.this, ActivityScanResultXieyi.class);
                intent.putExtra("url", UrlEntry.XIEYI_DISTRIBUTOR_URL);
                startActivity(intent);
            }
        });
        getBigImg();
        tv_state = (TextView) findViewById(R.id.tv_state);
        mLlYxs = (LinearLayout) findViewById(R.id.jxs_layout);
        mEtUsername = (TextView) findViewById(R.id.et_username);
        mEtUsername.setText(App.username);
        mEtJoinType = (EditText) findViewById(R.id.et_jointype);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtArea = (EditText) findViewById(R.id.et_area);
        mEtAddress = (EditText) findViewById(R.id.etAddress);
        mIvDpmt = (ImageView) findViewById(R.id.iv_dpmt);
        mIvDpmt.setOnClickListener(this);
        mIvYezz = (ImageView) findViewById(R.id.iv_yyezz);
        mIvGsp = (ImageView) findViewById(R.id.iv_gsp);
        mIvSfzzm = (ImageView) findViewById(R.id.iv_identity);
        mIvSfzfm = (ImageView) findViewById(R.id.iv_fidentity);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_modify = (Button) findViewById(R.id.btn_modify);
        mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
        mLlFirst = (LinearLayout) findViewById(R.id.layoutone);
        mLlTwo = (LinearLayout) findViewById(R.id.layouttwo);
        mNextBtn = (Button) findViewById(R.id.btn_next);
        mNextBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEtPhone.getText().toString())
                        || TextUtils.isEmpty(mEtAddress.getText().toString())
                        || TextUtils.isEmpty(mEtArea.getText().toString())) {
                    showToast("带'*'号为必填项,不能为空,请完善！");
                    return;
                }
                if (TextUtils.isEmpty(mEtJoinType.getText().toString())) {
                    showToast("请选择您的加盟类型！");
                    return;
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
                    mLlTwo.setVisibility(View.GONE);
                    mLlFirst.setVisibility(View.VISIBLE);
                } else if ((mLlTwo.getVisibility() == View.GONE)
                        && (mLlFirst.getVisibility() == View.VISIBLE)) {
                    finish();
                }

            }
        });
        initImage();
    }

    private void getBigImg(){
        mIvMtShili.setOnClickListener(new OnClickListener() { // 点击放大
            public void onClick(View paramView) {
                LayoutInflater inflater = LayoutInflater.from(JoinDistributorActivity.this);
                View imgEntryView = inflater.inflate(R.layout.activity_largeimg, null); // 加载自定义的布局文件
                final AlertDialog dialog = new AlertDialog.Builder(JoinDistributorActivity.this).create();
//                ImageView img1 = (ImageView)imgEntryView.findViewById(R.id.bigimg);
//                img1.setBackgroundResource(R.drawable.largemtsyt);
                dialog.setView(imgEntryView); // 自定义dialog
                dialog.show();
                // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
                imgEntryView.setOnClickListener(new OnClickListener() {
                    public void onClick(View paramView) {
                        dialog.cancel();
                    }
                });
            }
        });
    }
    private void initImage() {
        if (isShowAddItem(mDataList1, 0)) {
//            yezzimagepath = "";
            mIvYezz.setBackgroundResource(R.drawable.addfile);
        } else {
            final ImageItem item = mDataList1.get(0);
            ImageDisplayer.getInstance(this).displayBmp(mIvYezz,
                    item.thumbnailPath, item.sourcePath);
            yezzimagepath = item.sourcePath;
        }
        if (isShowAddItem(mDataList2, 0)) {
//            gspimagepath = "";
            mIvGsp.setBackgroundResource(R.drawable.addfile);
        } else {
            final ImageItem item2 = mDataList2.get(0);
            ImageDisplayer.getInstance(this).displayBmp(mIvGsp,
                    item2.thumbnailPath, item2.sourcePath);
            gspimagepath = item2.sourcePath;
        }
        if (isShowAddItem(mDataList3, 0)) {
//            mtimagepath = "";
            mIvDpmt.setBackgroundResource(R.drawable.addfile);
        } else {
            final ImageItem item2 = mDataList3.get(0);
            ImageDisplayer.getInstance(this).displayBmp(mIvDpmt,
                    item2.thumbnailPath, item2.sourcePath);
            mtimagepath = item2.sourcePath;
        }
        if (isShowAddItem(mDataList4, 0)) {
//            sfzzmimagepath = "";
            mIvSfzzm.setBackgroundResource(R.drawable.ic_sfzz);
        } else {
            final ImageItem item2 = mDataList4.get(0);
            ImageDisplayer.getInstance(this).displayBmp(mIvSfzzm,
                    item2.thumbnailPath, item2.sourcePath);
            sfzzmimagepath = item2.sourcePath;
        }
        if (isShowAddItem(mDataList5, 0)) {
//            sfzfmimagepath = "";
            mIvSfzfm.setBackgroundResource(R.drawable.ic_sfzf);
        } else {
            final ImageItem item2 = mDataList5.get(0);
            ImageDisplayer.getInstance(this).displayBmp(mIvSfzfm,
                    item2.thumbnailPath, item2.sourcePath);
            sfzfmimagepath = item2.sourcePath;
        }

    }


    /**
     * @param dis
     */
    private void setDataToView(Distributor dis) {
        id = dis.getId();
        mEtArea.setText(dis.getProvince() + " " + dis.getCity() + " "
                + dis.getArea());
        province = dis.getProvince();
        city = dis.getCity();
        area = dis.getArea();
        mEtAddress.setText(dis.getAddress());
        mEtPhone.setText(dis.getPhone());
        joinType = dis.getFxsType();
        if (dis.getFxsType().equals("1")) {
            mEtJoinType.setText("经销商加盟");
            mLlYxs.setVisibility(View.VISIBLE);
        } else if (dis.getFxsType().equals("2")) {
            mEtJoinType.setText("个人加盟");
            mLlYxs.setVisibility(View.GONE);
        }
        x = AppStaticUtil.getConvertDouble(dis.getX());
        y = AppStaticUtil.getConvertDouble(dis.getY());
        if (dis.getStatus().equals("2")) {
            tv_state.setText("审核通过");
        } else if (dis.getStatus().equals("3")) {
            tv_state.setText("审核未通过,原因：" + dis.getReason());
        } else if (dis.getStatus().equals("1")) {
            tv_state.setText("审核中");
        }
        if (!TextUtils.isEmpty(dis.getYyzz())) {
            bitmapUtils.display(mIvYezz, UrlEntry.ip
                    + dis.getYyzz().toString());// 下载并显示图片
            yezzoldpath = UrlEntry.ip
                    + dis.getYyzz().toString();
            yezzimagepath = yezzoldpath;
        }
        if (!TextUtils.isEmpty(dis.getGspzs())) {
            bitmapUtils.display(mIvGsp, UrlEntry.ip
                    + dis.getGspzs().toString());// 下载并显示图片
            gspoldpath = UrlEntry.ip
                    + dis.getGspzs().toString();
            gspimagepath = gspoldpath;
        }
        if (!TextUtils.isEmpty(dis.getMt())) {
            bitmapUtils.display(mIvDpmt, UrlEntry.ip
                    + dis.getMt().toString());// 下载并显示图片
            mtoldpath = UrlEntry.ip
                    + dis.getMt().toString();
            mtimagepath = mtoldpath;
        }
        if (!TextUtils.isEmpty(dis.getSfzZm())) {
            bitmapUtils.display(mIvSfzzm, UrlEntry.ip
                    + dis.getSfzZm().toString());// 下载并显示图片
            sfzzmoldpath = UrlEntry.ip
                    + dis.getSfzZm().toString();
            sfzzmimagepath = sfzzmoldpath;
        }
        if (!TextUtils.isEmpty(dis.getSfzFm())) {
            bitmapUtils.display(mIvSfzfm, UrlEntry.ip
                    + dis.getSfzFm().toString());// 下载并显示图片
            sfzfmoldpath = UrlEntry.ip
                    + dis.getSfzFm().toString();
            sfzfmimagepath = sfzfmoldpath;
        }
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

                        Log.i("info", " info responseInfo.result = "
                                + responseInfo.result);

                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {
//								if (jsonresult.optString("id").toString().equals("")||jsonresult.isNull("id")){
//
//								}


                                Distributor mInfoEntry;

                                btn_submit.setVisibility(View.GONE);
                                btn_modify.setVisibility(View.VISIBLE);
                                GsonBuilder gsonb = new GsonBuilder();
                                Gson gson = gsonb.create();
                                mInfoEntry = gson.fromJson(
                                        jsonresult.toString(), Distributor.class);
                                setDataToView(mInfoEntry);

                            } else if (jsonresult.get("success").toString()
                                    .equals("4")) {
                                add = true;

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
                        showToast("操作失败,请稍后再试！");
                        disDialog();

                    }
                });

    }

    private String[] mItems = {"经销商加盟", "个人加盟"};
    private int mSingleChoiceID = 0;

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.et_jointype:
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        JoinDistributorActivity.this);

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

                                if (mSingleChoiceID == 0) {
                                    mLlYxs.setVisibility(View.VISIBLE);
                                } else {
                                    mLlYxs.setVisibility(View.GONE);
                                }
                                joinType = String.valueOf(mSingleChoiceID + 1);


                                mEtJoinType
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
                break;
            case R.id.et_area:
                Intent intent = new Intent(JoinDistributorActivity.this,
                        ChoseProvinceActivity.class);
                intent.putExtra("add", "add");
                startActivityForResult(intent, App.ADDRESS_CODE);
                break;
            case R.id.btn_submit:
                showDialog();
                MyRunnable run = new MyRunnable("2", UrlEntry.INSERT_DISTRIBUTOR_URL);// add
                new Thread(run).start();
                break;
            case R.id.btn_modify:
                showDialog();
                MyRunnable runm = new MyRunnable("3", UrlEntry.UPDATE_DISTRIBUTOR_URL);// update
                new Thread(runm).start();
                break;
            case R.id.iv_yyezz:
                TAKE_PHOTO = CustomConstants.TAKE_PHOTO;
                TAKE_PICTURE = CustomConstants.TAKE_PICTURE1;
                showPopDialog(mIvYezz);
                break;
            case R.id.iv_gsp:
                TAKE_PHOTO = CustomConstants.TAKE_PHOTO2;
                TAKE_PICTURE = CustomConstants.TAKE_PICTURE2;
                showPopDialog(mIvGsp);
                break;
            case R.id.iv_dpmt:
                TAKE_PHOTO = CustomConstants.TAKE_PHOTO3;
                TAKE_PICTURE = CustomConstants.TAKE_PICTURE3;
                showPopDialog(mIvDpmt);
                break;
            case R.id.iv_identity:
                TAKE_PHOTO = CustomConstants.TAKE_PHOTO4;
                TAKE_PICTURE = CustomConstants.TAKE_PICTURE4;
                showPopDialog(mIvSfzzm);
                break;
            case R.id.iv_fidentity:
                TAKE_PHOTO = CustomConstants.TAKE_PHOTO5;
                TAKE_PICTURE = CustomConstants.TAKE_PICTURE5;
                showPopDialog(mIvSfzfm);
                break;

        }

    }

    private void showPopDialog(View parent) {

        // 自定义的单击事件
        OnClickLintener paramOnClickListener = new OnClickLintener();
        mPwDialog = new PopupWindows(JoinDistributorActivity.this,
                paramOnClickListener, parent);
        // 如果窗口存在，则更新
        mPwDialog.update();
    }

    class OnClickLintener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_popupwindows_camera: // 相机

                    if (TAKE_PHOTO == CustomConstants.TAKE_PHOTO) {
                        mFile = getFile();
                        yezzimagepath1 = getPath(mFile);
                    } else if (TAKE_PHOTO == CustomConstants.TAKE_PHOTO2) {
                        mFile = getFile();
                        gspimagepath1 = getPath(mFile);
                    } else if (TAKE_PHOTO == CustomConstants.TAKE_PHOTO3) {
                        mFile = getFile();
                        mtimagepath1 = getPath(mFile);
                    } else if (TAKE_PHOTO == CustomConstants.TAKE_PHOTO4) {
                        mFile = getFile();
                        sfzzmimagepath1 = getPath(mFile);
                    } else if (TAKE_PHOTO == CustomConstants.TAKE_PHOTO5) {
                        mFile = getFile();
                        sfzfmimagepath1 = getPath(mFile);
                    }
                    takePhoto(TAKE_PHOTO, mFile);
                    mPwDialog.dismiss();
                    break;
                case R.id.item_popupwindows_Photo: // 相册

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
            if (TextUtils.isEmpty(mEtPhone.getText().toString())
                    || TextUtils.isEmpty(mEtAddress.getText().toString())
                    || TextUtils.isEmpty(mEtArea.getText().toString())) {
                msg.what = 12;
                handler.sendMessage(msg);
                return;

            }
            if (TextUtils.isEmpty(mEtJoinType.getText().toString())) {
                msg.what = 13;
                handler.sendMessage(msg);
                return;
            }
            if (mEtPhone.getText().toString().length() != 11) {
                msg.what = 15;
                handler.sendMessage(msg);
                return;
            }
//            if (mCheckBtn.isChecked()) {
//            } else {
//
//                msg.what = 9;
//                handler.sendMessage(msg);
//                return;
//            }
            MultipartEntity params = new MultipartEntity();
            try {
                params.addPart("", new StringBody(""));
                if (type.equals("3")) {
                    params.addPart("e.id", new StringBody(id));
                }
                params.addPart("e.province", new StringBody(province));
                params.addPart("e.city", new StringBody(city));
                params.addPart("e.area", new StringBody(area));
                params.addPart("e.address", new StringBody(mEtAddress.getText().toString()));
                params.addPart("e.phone", new StringBody(mEtPhone.getText().toString()));
                params.addPart("e.fxsType", new StringBody(joinType));
                params.addPart("e.userID", new StringBody(App.userID));
                params.addPart("uuid", new StringBody(App.uuid));
                params.addPart("e.x", new StringBody(String.valueOf(x)));
                params.addPart("e.y", new StringBody(String.valueOf(y)));

                if (joinType.equals("1") ) {

                    if(TextUtils.isEmpty(yezzimagepath)||TextUtils.isEmpty(mtimagepath)){
                        msg.what = 12;
                        handler.sendMessage(msg);
                        return;
                    }

                    if (!yezzimagepath.equals(yezzoldpath)) {
                        if ((new File(yezzimagepath).exists())) {
                            params.addPart("yyzz", new FileBody(new File(
                                    yezzimagepath)));
                        } else {
                            msg.what = 16;
                            handler.sendMessage(msg); //showToast("读取图片错误，请重新选择营业执照图片。");
                            clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES1);
                            initImage();
                            return;
                        }
                    }
                    if (!mtimagepath.equals(mtoldpath)) {
                        if ((new File(mtimagepath).exists())) {
                            params.addPart("mt", new FileBody(new File(
                                    mtimagepath)));
                        } else {
                            msg.what = 17;
                            handler.sendMessage(msg);
                            //showToast("读取图片错误，请重新选择店铺门头图片。");
                            clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES3);
                            initImage();
                            return;
                        }
                    }
                    if (!gspimagepath.equals(gspoldpath)) {
                        if (!gspimagepath.equals("")) {
                            if ((new File(gspimagepath).exists())) {
                                params.addPart("gspzs", new FileBody(new File(
                                        gspimagepath)));
                            } else {
                                msg.what = 18;
                                handler.sendMessage(msg);
                                //showToast("读取图片错误，请重新选择店铺门头兽药GSP证书。");
                                clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES2);
                                initImage();
                                return;
                            }
                        }
                    }
                }

                if(TextUtils.isEmpty(sfzzmimagepath)||TextUtils.isEmpty(sfzfmimagepath)){
                    msg.what = 12;
                    handler.sendMessage(msg);
                    return;
                }
                if (!sfzzmimagepath.equals(sfzzmoldpath)) {
                    if (!sfzzmimagepath.equals("")) {
                        if ((new File(sfzzmimagepath).exists())) {
                            params.addPart("sfzZm", new FileBody(new File(
                                    sfzzmimagepath)));
                        } else {
                            msg.what = 19;
                            handler.sendMessage(msg);
                           // showToast("读取图片错误，请重新选择身份证正面图片。");
                            clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES4);
                            initImage();
                            return;
                        }
                    }
                }
                if (!sfzfmimagepath.equals(sfzfmoldpath)) {
                    if (!sfzfmimagepath.equals("")) {
                        if ((new File(sfzfmimagepath).exists())) {
                            params.addPart("sfzFm", new FileBody(new File(
                                    sfzfmimagepath)));
                        } else {
                            msg.what = 20;
                            handler.sendMessage(msg);
                            //showToast("读取图片错误，请重新选择身份证反面图片。");
                            clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES5);
                            initImage();
                            return;
                        }
                    }
                }
                HttpUtil http = new HttpUtil();
                result = http.postDataMethod(url, params);

                data.putString("value", result);
                msg.setData(data);

                handler.sendMessage(msg);

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            disDialog();
            if(msg.what == 9){
                showToast(" 请先同意分销商入住协议");
                return;
            }else
            if (msg.what == 11) {
                showToast("请上传您的身份证证件照片！");
                return;
            } else if (msg.what == 12) {

                showToast("带'*'号为必填项,不能为空,请完善！");
                return;
            } else if (msg.what == 13) {
                showToast("请选择您的加盟类型！");
                return;
            } else if (msg.what == 14) {
                showToast("身份证格式错误！");
                return;
            } else if (msg.what == 15) {
                showToast("请输入11位电话号码！");
                return;
            } else if (msg.what == 16) {
                showToast("读取图片错误，请重新选择营业执照图片。");
                return;
            } else if (msg.what == 17) {
                showToast("读取图片错误，请重新选择店铺门头图片。");
                return;
            }else if (msg.what == 18) {
                showToast("读取图片错误，请重新选择店铺门头兽药GSP证书。");
                return;
            }else if (msg.what == 19) {
                showToast("读取图片错误，请重新选择身份证正面图片。");
                return;
            }else if (msg.what == 20) {
                showToast("读取图片错误，请重新选择身份证反面图片。");
                return;
            }

            Bundle data1 = msg.getData();
            String result = data1.getString("value");
            if (result.equals("")) {
                showToast("连接服务器失败,请稍后再试！");
                return;
            }

            JSONObject jsonresult;

            try {
                jsonresult = new JSONObject(result);
                switch (Integer.parseInt(jsonresult.getString("success"))) {
                    case 1:
                        showToast("提交成功！");

                        if (App.mUserInfo == null) {
                            App.mUserInfo = FileUtil
                                    .readUser(JoinDistributorActivity.this);
                        }
                        if (App.mUserInfo != null) {
                            App.mUserInfo.setProvince(province);
                            App.mUserInfo.setCity(city);
                            App.mUserInfo.setArea(area);
                            App.mUserInfo.setAuthentication("1");
                            FileUtil.saveUser(JoinDistributorActivity.this,
                                    App.mUserInfo);
                            App.setDataInfo(App.mUserInfo);
                        }
                        setResult(11);
                        finish();
                        break;
                    case 4:

                        break;
                    default:
                        successType(jsonresult.get("success").toString(), "操作失败！");
                        break;

                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case App.ADDRESS_CODE:
                if (data == null)
                    return;
                Bundle bundle = data.getExtras();
                province = bundle.getString("province");
                city = bundle.getString("city");
                area = bundle.getString("area");
                mEtArea.setText(province + " " + city + " " + area);
                break;
            //拍照
            case CustomConstants.TAKE_PHOTO:
                System.out.println("path" + yezzimagepath1);
                if (resultCode == -1 && !TextUtils.isEmpty(yezzimagepath1)) {
                    mDataList1.clear();
                    mDataList1.add(FileUtil.saveDataPic(yezzimagepath1));
                }
                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);
                initImage();
                break;
            case CustomConstants.TAKE_PHOTO2:
                System.out.println("path2" + gspimagepath1);
                if (resultCode == -1 && !TextUtils.isEmpty(gspimagepath1)) {
                    mDataList2.clear();
                    mDataList2.add(FileUtil.saveDataPic(gspimagepath1));
                }
                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES2, mDataList2);
                initImage();
                break;
            case CustomConstants.TAKE_PHOTO3:
                System.out.println("path2" + mtimagepath1);
                if (resultCode == -1 && !TextUtils.isEmpty(mtimagepath1)) {
                    mDataList3.clear();
                    mDataList3.add(FileUtil.saveDataPic(mtimagepath1));
                }
                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES3, mDataList3);
                initImage();
                break;
            case CustomConstants.TAKE_PHOTO4:
                System.out.println("sfzzmimagepath1" + sfzzmimagepath1);
                if (resultCode == -1 && !TextUtils.isEmpty(sfzzmimagepath1)) {
                    mDataList4.clear();
                    mDataList4.add(FileUtil.saveDataPic(sfzzmimagepath1));
                }
                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES4, mDataList4);
                initImage();
                break;
            case CustomConstants.TAKE_PHOTO5:
                System.out.println("sfzfmimagepath1" + sfzfmimagepath1);
                if (resultCode == -1 && !TextUtils.isEmpty(sfzfmimagepath1)) {
                    mDataList5.clear();
                    mDataList5.add(FileUtil.saveDataPic(sfzfmimagepath1));
                }
                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES5, mDataList5);
                initImage();
                break;

//相册
            case CustomConstants.TAKE_PICTURE1:
                if (resultCode != -1) {
                    return;
                }
                if (data == null) {
                    return;
                }
                yezzimagepath1 = getPath(getFile());
                startPhotoZoom1(data, yezzimagepath1, CustomConstants.CORP_TAKE_PICTURE1);
                break;
            case CustomConstants.TAKE_PICTURE2:
                if (resultCode != -1) {
                    return;
                }
                if (data == null) {
                    return;
                }
                gspimagepath1 = getPath(getFile());
                startPhotoZoom1(data, gspimagepath1, CustomConstants.CORP_TAKE_PICTURE2);
                break;
            case CustomConstants.TAKE_PICTURE3:
                if (resultCode != -1) {
                    return;
                }
                if (data == null) {
                    return;
                }
                mtimagepath1 = getPath(getFile());
                startPhotoZoom1(data, mtimagepath1, CustomConstants.CORP_TAKE_PICTURE3);
                break;
            case CustomConstants.TAKE_PICTURE4:
                if (resultCode != -1) {
                    return;
                }
                if (data == null) {
                    return;
                }
                sfzzmimagepath1 = getPath(getFile());
                startPhotoZoom1(data, sfzzmimagepath1, CustomConstants.CORP_TAKE_PICTURE4);
                break;
            case CustomConstants.TAKE_PICTURE5:
                if (resultCode != -1) {
                    return;
                }
                if (data == null) {
                    return;
                }
                sfzfmimagepath1 = getPath(getFile());
                startPhotoZoom1(data, sfzfmimagepath1, CustomConstants.CORP_TAKE_PICTURE5);
                break;

//裁剪
            case CustomConstants.CORP_TAKE_PICTURE1:
                if (resultCode != -1) {
                    return;
                }
                mDataList1 = setPicToView(data, yezzimagepath1, mDataList1, 0);
                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);
                initView();
                break;
            case CustomConstants.CORP_TAKE_PICTURE2:
                if (resultCode != -1) {
                    return;
                }
                mDataList2 = setPicToView(data, gspimagepath1, mDataList2, 0);
                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES2, mDataList2);
                initView();
                break;
            case CustomConstants.CORP_TAKE_PICTURE3:
                if (resultCode != -1) {
                    return;
                }
                mDataList3 = setPicToView(data, mtimagepath1, mDataList3, 0);
                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES3, mDataList3);
                initView();
                break;
            case CustomConstants.CORP_TAKE_PICTURE4:
                if (resultCode != -1) {
                    return;
                }
                mDataList4 = setPicToView(data, sfzzmimagepath1, mDataList4, 0);
                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES4, mDataList4);
                initView();
                break;
            case CustomConstants.CORP_TAKE_PICTURE5:
                if (resultCode != -1) {
                    return;
                }
                mDataList5 = setPicToView(data, sfzfmimagepath1, mDataList5, 0);
                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES5, mDataList5);
                initView();
                break;
            default:
                break;
        }

    }

    protected void onPause() {
        super.onPause();
        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);
        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES2, mDataList2);
        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES3, mDataList3);
        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES4, mDataList4);
        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES5, mDataList5);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("yezzimagepath1", yezzimagepath1);
        outState.putString("gspimagepath1", gspimagepath1);
        outState.putString("mtimagepath1", mtimagepath1);
        outState.putString("sfzzmimagepath1", sfzzmimagepath1);
        outState.putString("sfzfmimagepath1", sfzfmimagepath1);
        super.onSaveInstanceState(outState);
        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);
        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES2, mDataList2);
        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES3, mDataList3);
        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES4, mDataList4);
        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES5, mDataList5);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            yezzimagepath1 = savedInstanceState.getString("yezzimagepath1");
            gspimagepath1 = savedInstanceState.getString("gspimagepath1");
            mtimagepath1 = savedInstanceState.getString("mtimagepath1");
            sfzzmimagepath1 = savedInstanceState.getString("sfzzmimagepath1");
            sfzfmimagepath1 = savedInstanceState.getString("sfzfmimagepath1");

            initView();
        }
    }
}
