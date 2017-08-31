//package com.szmy.pigapp.tixian;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.gc.materialdesign.widgets.Dialog;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.lidroid.xutils.BitmapUtils;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest;
//import com.szmy.pigapp.R;
//import com.szmy.pigapp.activity.BaseActivity;
//import com.szmy.pigapp.image.CustomConstants;
//import com.szmy.pigapp.image.ImageDisplayer;
//import com.szmy.pigapp.image.ImageItem;
//import com.szmy.pigapp.image.PopupWindows;
//import com.szmy.pigapp.utils.App;
//import com.szmy.pigapp.utils.FileUtil;
//import com.szmy.pigapp.utils.UrlEntry;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * 绑定银行卡
// */
//public class ActivityWithdrawals extends BaseActivity implements
//        OnClickListener {
//
//    private TextView tvCardType;
//    private EditText etCardNum;
//    private EditText etCardName;
//    private Button btnSubmit;
//    private String[] mPsItems = new String[]{"农业银行卡", "农村信用社"};
//    private int mSingleChoiceID = 0;
//    private String url = "";
//    private String id = "";
//    private LinearLayout lLbackLayout;
//    private EditText etIdentityCard;// 身份证
//    private ImageView iv_identity;
//    private ImageView iv_fidentity;
//    private BitmapUtils bitmapUtils;
//    private String pathname = "";
//    private String path = "", path2 = "";
//    private String oldpath = "", oldpath2 = "";
//    private LinearLayout mLlSfzPic;
//    private LinearLayout mLlSfz;
//    private Boolean isSigned = false;// userCard
//    private PopupWindows mPwDialog;
//    private int TAKE_PHOTO;
//    private int TAKE_PICTURE;
//    private File mFile;
//    public List<ImageItem> mDataList1 = new ArrayList<ImageItem>();
//    public List<ImageItem> mDataList2 = new ArrayList<ImageItem>();
//    private String imagepath1 = "";
//    private String imagepath2 = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_withdrawals);
//        bitmapUtils = new BitmapUtils(this);
//        if (App.mUserInfo == null) {
//            App.mUserInfo = FileUtil.readUser(ActivityWithdrawals.this);
//        }
//        App.setDataInfo(App.mUserInfo);
//        initView();
//        String type = getIntent().getStringExtra("type");
//        if (type.equals("rz")) {// 认证页面跳转过来
////            tvCardType.setEnabled(false);
////            tvCardType.setText("农业银行卡");
//            showDialog();
//            sendDataByUUid();
//            return;
//        } else if (type.equals("upp")) {
//            url = UrlEntry.UPP_CARD_URL;
//            tvCardType.setEnabled(false);
//            Withdrawals info = (Withdrawals) getIntent().getSerializableExtra(
//                    "info");
//            if (info.getType().equals("nyBank")
//                    || info.getType().equals("nybank")) {
//                tvCardType.setText("农业银行卡");
//            } else if (info.getType().equals("ncxys")) {
//                tvCardType.setText("农村信用社");
//            }else{
//                showToast("暂时无法对其他账号修改");
//                finish();
//            }
//
//            setView(info);
//        } else if (type.equals("add")) {
//            url = UrlEntry.ADD_CARD_URL;
//            isFlagCard();
//
////            if (getIntent().hasExtra("cardtype")) {
////                if (getIntent().getStringExtra("cardtype").equals("nyBank")) {
////                    tvCardType.setText("农业银行卡");
////                    tvCardType.setEnabled(false);
////
////                } else {
////                    mLlSfz.setVisibility(View.GONE);
////                    mLlSfzPic.setVisibility(View.GONE);
////                }
////            } else {
////                tvCardType.setEnabled(false);
////                isFlagCard();
////            }
//        }
//    }
//
//    private void initView() {
//        lLbackLayout = (LinearLayout) findViewById(R.id.def_head_back);
//        lLbackLayout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
////				clearTempFromPref();
//                clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES1);
//                clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES2);
//                setResult(0);
//                finish();
//            }
//        });
//        btnSubmit = (Button) findViewById(R.id.btn_submitP);
//        tvCardType = (TextView) findViewById(R.id.tvCardType);
//        etCardNum = (EditText) findViewById(R.id.etCardNum);
//        etCardName = (EditText) findViewById(R.id.etCardName);
//        etIdentityCard = (EditText) findViewById(R.id.etIdentityCard);
//        iv_identity = (ImageView) findViewById(R.id.iv_identity);
//        iv_fidentity = (ImageView) findViewById(R.id.iv_fidentity);
//        mLlSfz = (LinearLayout) findViewById(R.id.sfzlayout);
//        mLlSfzPic = (LinearLayout) findViewById(R.id.sfzpiclayout);
//        mDataList1 = getTempFromPref(CustomConstants.PREF_TEMP_IMAGES1);
//        mDataList2 = getTempFromPref(CustomConstants.PREF_TEMP_IMAGES2);
//        initImage();
//    }
//
//    private void setView(Withdrawals winfo) {
//        id = winfo.getId();
//        imagepath1 = winfo.getSfzZm();
//        imagepath2 = winfo.getSfzFm();
//        if (!winfo.getSfzZm().equals("")) {
//            oldpath = winfo.getSfzZm();
//        }
//        if (!winfo.getSfzFm().equals("")) {
//            oldpath2 = winfo.getSfzFm();
//        }
//        if (winfo.getType().equals("nyBank")
//                || winfo.getType().equals("nybank")) {
//            tvCardType.setText("农业银行卡");
//        } else if (winfo.getType().equals("ncxys")) {
//            tvCardType.setText("农村信用社");
//        }else{
//            tvCardType.setText("农业银行卡");
//        }
//        if (TextUtils.isEmpty(winfo.getUserCard())
//                || winfo.getUserCard() == null) {
//            isSigned = false;
//        } else {
//            isSigned = true;
//            etCardNum.setEnabled(false);
//            etCardNum.setTextColor(getResources().getColor(R.color.gray));
//            etCardNum.setFocusable(false);
//            etCardName.setTextColor(getResources().getColor(R.color.gray));
//            etCardName.setEnabled(false);
//            etCardName.setFocusable(false);
//        }
//
//        etCardNum.setText(winfo.getBankNum());
//        etCardName.setText(winfo.getName());
//        etIdentityCard.setText(winfo.getSfzh());
//        System.out.println(winfo.getSfzFm());
//        if (!TextUtils.isEmpty(winfo.getSfzZm())) {
//            bitmapUtils.display(iv_identity, UrlEntry.ip
//                    + winfo.getSfzZm().toString());// 下载并显示图片
//
//        }
//        if (!TextUtils.isEmpty(winfo.getSfzFm())) {
//            bitmapUtils.display(iv_fidentity, UrlEntry.ip
//                    + winfo.getSfzFm().toString());// 下载并显示图片
//
//        }
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_submitP:
//                // 判断卡号是否是农业银行
//                if (TextUtils.isEmpty(tvCardType.getText().toString())
//                        || TextUtils.isEmpty(etCardNum.getText().toString())
//                        || TextUtils.isEmpty(etCardName.getText().toString())
//                        || TextUtils.isEmpty(etIdentityCard.getText().toString())) {
//                    showToast("带'*'项不能为空！");
//                    return;
//                }
//
//                String str = FileUtil.IDCardValidate(etIdentityCard.getText()
//                        .toString());
//                if (!str.equals("")) {
//                    showToast(str);
//                    return;
//                }
//                // String sfzZm = "";
//                // if (getTempFromPref(CustomConstants.PREF_TEMP_IMAGES1).size() !=
//                // 0) {
//                // sfzZm = getTempFromPref(CustomConstants.PREF_TEMP_IMAGES1).get(
//                // 0).sourcePath;
//                // }
//                //
//                // String sfzFm = "";
//                // if (getTempFromPref(CustomConstants.PREF_TEMP_IMAGES2).size() !=
//                // 0) {
//                // sfzFm = getTempFromPref(CustomConstants.PREF_TEMP_IMAGES2).get(
//                // 0).sourcePath;
//                // }
//                if (TextUtils.isEmpty(imagepath1) || TextUtils.isEmpty(imagepath2)) {
//                    showToast("请先上传身份证正反面照片！");
//                    return;
//
//                }
//                if (tvCardType.getText().toString().equals("农业银行卡")) {
//                    isAgricultureCard(etCardNum.getText().toString());
//                } else {
//                    showDialog();
//                    sendData();
//                }
//
//                break;
//            case R.id.tvCardType:
//                AlertDialog.Builder builder = new AlertDialog.Builder(
//                        ActivityWithdrawals.this);
//
//                mSingleChoiceID = 0;
//                builder.setTitle("请选择");
//                builder.setSingleChoiceItems(mPsItems, 0,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int whichButton) {
//                                mSingleChoiceID = whichButton;
//
//                            }
//                        });
//                builder.setPositiveButton("确定",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int whichButton) {
//
//                                tvCardType.setText(mPsItems[mSingleChoiceID]);
//
//                            }
//                        });
//                builder.setNegativeButton("取消",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int whichButton) {
//
//                            }
//                        });
//                builder.create().show();
//                break;
//            case R.id.iv_identity:
//                // pathname = "identity";
//                // ShowPickDialog();
//
//                TAKE_PHOTO = CustomConstants.TAKE_PHOTO;
//                TAKE_PICTURE = CustomConstants.TAKE_PICTURE1;
//                showPopDialog(iv_identity);
//                break;
//            case R.id.iv_fidentity:
//                // pathname = "cover";
//                // ShowPickDialog();
//                TAKE_PHOTO = CustomConstants.TAKE_PHOTO2;
//                TAKE_PICTURE = CustomConstants.TAKE_PICTURE2;
//                showPopDialog(iv_fidentity);
//                break;
//        }
//
//    }
//
//    private void isAgricultureCard(String cardNumber) {
//
//        // cardNumber = "6228 4828 9820 3884 775";// 卡号
//        cardNumber = cardNumber.replaceAll(" ", "");
//
//        // 位数校验
//        if (cardNumber.length() == 16 || cardNumber.length() == 19) {
//
//        } else {
//            showDialog1(ActivityWithdrawals.this, "卡号位数无效");
//            return;
//        }
//
//        // 校验
//        if (checkBankCard.checkBankCard(cardNumber) == true) {
//
//        } else {
//            showDialog1(ActivityWithdrawals.this, "卡号校验失败");
//            return;
//        }
//
//        // 判断是不是银联，老的卡号都是服务电话开头，比如农行是95599
//        // http://cn.unionpay.com/cardCommend/gyylbzk/gaishu/file_6330036.html
//        if (cardNumber.startsWith("62")) {
//            System.out.println("中国银联卡");
//        } else if (cardNumber.startsWith("95599")) {
//            System.out.println("旧的银行卡");
//        } else {
//
//            System.out.println("国外的卡，或者旧银行卡，暂时没有收录");
//            showDialog1(ActivityWithdrawals.this, "请输入农业银行卡卡号");
//            return;
//        }
//
//        String name = BankCardBin.getNameOfBank(cardNumber.substring(0, 6), 0);// 获取银行卡的信息
//        System.out.println(name);
//
//        // 归属地每个银行不一样，这里只收录农行少数地区代码
//        if (name.startsWith("农业银行") == true) {
//            if (cardNumber.length() == 19) {
//                // 提交绑定信息
//                sendData();
//            }
//        }
//
//    }
//
//    public void sendData() {
//        showDialog();
//        RequestParams params = new RequestParams();
//
//        if (id != "") {
//            params.addBodyParameter("e.id", id);
//        }
//        params.addBodyParameter("uuid", App.uuid);
//        params.addBodyParameter("e.bankNum", etCardNum.getText().toString());
//        params.addBodyParameter("e.name", etCardName.getText().toString());
//        String type = "";
//        if (tvCardType.getText().toString().equals("农业银行卡")) {
//            type = "nyBank";
//        } else if (tvCardType.getText().toString().equals("农村信用社")) {
//            type = "ncxys";
//        }
//        params.addBodyParameter("e.type", String.valueOf(type));
//        System.out.print(imagepath1+"路径:"+imagepath2);
//        if (!imagepath1.equals(oldpath)) {
//            if((new File(imagepath1).exists())) {
//                params.addBodyParameter("sfzZm", new File(imagepath1));
//            }else{
//                showToast("读取图片错误，请重新选择身份证正面图片。");
//                clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES1);
//                initImage();
//                return;
//            }
//        }
//        if (!imagepath2.equals(oldpath2)) {
//            if((new File(imagepath2).exists())) {
//                params.addBodyParameter("sfzFm", new File(imagepath2));
//            }else{
//                showToast("读取图片错误，请重新选择身份证反面图片。");
//                clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES2);
//                initImage();
//                return;
//            }
//
//        }
//
//        params.addBodyParameter("sfzh",
//                String.valueOf(etIdentityCard.getText().toString()));
//        final HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, url, params,
//                new RequestCallBack<String>() {
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
//                        Log.i("insertyhk", " insert responseInfo.result = "
//                                + responseInfo.result);
//
//                        JSONObject jsonresult;
//                        try {
//                            jsonresult = new JSONObject(responseInfo.result);
//                            if (jsonresult.get("success").toString()
//                                    .equals("1")) {
//                                clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES1);
//                                clearTempFromPrefByName(CustomConstants.PREF_TEMP_IMAGES2);
////								clearTempFromPref();
//                                if (!isSigned) {
//                                    final Dialog dialog = new Dialog(ActivityWithdrawals.this, "提示", jsonresult.get("msg")
//                                            .toString());
////                                            + ",您未签约银行卡，请用您的账号登录神州牧易商城(www.my360.cn)进行签约！");
//                                    dialog.setCancelable(false);
//                                    dialog.addCancelButton("取消");
//                                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {
//
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.cancel();
//                                            setResult(26);
//                                            finish();
//                                        }
//                                    });
//                                    dialog.setOnCancelButtonClickListener(new OnClickListener() {
//
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.cancel();
//                                            setResult(26);
//                                            finish();
//                                        }
//                                    });
//                                    dialog.show();
//
//                                } else {
//                                    showToast(jsonresult.get("msg").toString());
//                                    setResult(26);
//                                    finish();
//                                }
//
//                                // showToast(jsonresult.get("msg").toString());
//
//                            } else {
//                                successType(jsonresult.get("success")
//                                        .toString(), jsonresult.get("msg")
//                                        .toString());
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(HttpException error, String msg) {
//                        showToast("服务器连接失败，请稍候再试！");
//                        disDialog();
//                    }
//                });
//    }
//
//    public void isFlagCard() {
//        /********************/
//
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("uuid", App.uuid);
//        final HttpUtils http = new HttpUtils();
//        http.send(HttpRequest.HttpMethod.POST, UrlEntry.QUERY_ISBINDING_URL,
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
//                        Log.i("isflagcardinsert",
//                                " insert responseInfo.result = "
//                                        + responseInfo.result);
//
//                        JSONObject jsonresult;
//                        try {
//                            jsonresult = new JSONObject(responseInfo.result);
//                            if (jsonresult.get("success").toString()
//                                    .equals("1")) {
//                                tvCardType.setEnabled(true);
//                                if (jsonresult.optString("nybank").toString()
//                                        .equals("y")
//                                        || jsonresult.optString("ncxys").toString()
//                                        .equals("y")) {
//                                    final Dialog dialog = new Dialog(ActivityWithdrawals.this, "提示", "无需绑定银行卡！");
//                                    dialog.setCancelable(false);
//                                    dialog.addCancelButton("取消");
//                                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {
//
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.cancel();
//                                            finish();
//                                        }
//                                    });
//                                    dialog.setOnCancelButtonClickListener(new OnClickListener() {
//
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.cancel();
//                                            finish();
//                                        }
//                                    });
//                                    dialog.show();
//
//                                } else {
//                                    if (jsonresult.optString("nybank")
//                                            .toString().equals("n")
//                                            && jsonresult.optString("ncxys")
//                                            .toString().equals("n")
//                                           ) {
//                                        tvCardType.setText("农业银行卡");
//                                        tvCardType.setEnabled(true);
//                                        return;
//
//                                    }
////                                    for (int i = 0; i < 3; i++) {
////                                        if (i == 0) {
////                                            if (jsonresult.optString("nybank")
////                                                    .toString().equals("y")) {
////                                                mPsItems = DeleAt(mPsItems,
////                                                        "农业银行卡");
////
////                                            }
////                                        } else if (i == 1) {
////                                            if (jsonresult.optString("zfb")
////                                                    .toString().equals("y")) {
////                                                mPsItems = DeleAt(mPsItems,
////                                                        "支付宝");
////                                            }
////                                        } else if (i == 2) {
////                                            if (jsonresult.optString("wx")
////                                                    .toString().equals("y")) {
////                                                mPsItems = DeleAt(mPsItems,
////                                                        "微信支付");
////                                            }
////                                        }
////                                    }
//                                }
//
//                            } else {
//                                successType(jsonresult.get("success")
//                                        .toString(), "查询失败");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(HttpException error, String msg) {
//                        showToast("服务器连接失败，请稍候再试！");
//                        disDialog();
//                    }
//                });
//    }
//
//    private String[] DeleAt(String[] str, String n) {
//        List<String> list = Arrays.asList(str);
//        List arrList = new ArrayList(list);
//        System.out.println(arrList);
//        for (Iterator it = arrList.iterator(); it.hasNext(); ) {
//            String st = (String) it.next();
//            if (st.equals(n)) {
//                it.remove();
//            }
//        }
//        System.out.println(arrList);
//        String[] array = new String[arrList.size()];
//        for (int i = 0; i < arrList.size(); i++) {
//            array[i] = (String) arrList.get(i);
//        }
//
//        return array;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case CustomConstants.TAKE_PICTURE:
//                break;
//            case CustomConstants.TAKE_PHOTO:
//                System.out.println("path" + path);
//                if (resultCode == -1 && !TextUtils.isEmpty(path)) {
//                    mDataList1.clear();
//                    mDataList1.add(FileUtil.saveDataPic(path));
//                }
//
//                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);
//                initImage();
//                break;
//            case CustomConstants.TAKE_PHOTO2:
//
//                System.out.println("path2" + path2);
//                if (resultCode == -1 && !TextUtils.isEmpty(path2)) {
//                    mDataList2.clear();
//                    mDataList2.add(FileUtil.saveDataPic(path2));
//                }
//
//                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES2, mDataList2);
//                initImage();
//                break;
//            case CustomConstants.TAKE_PICTURE1:
//                if (resultCode != -1) {
//                    return;
//                }
//                if (data == null) {
//                    return;
//                }
//                path = getPath(getFile());
//                startPhotoZoom(data.getData(), path,
//                        CustomConstants.CORP_TAKE_PICTURE1);
//                break;
//            case CustomConstants.TAKE_PICTURE2:
//                if (resultCode != -1) {
//                    return;
//                }
//                if (data == null) {
//                    return;
//                }
//                path2 = getPath(getFile());
//                startPhotoZoom(data.getData(), path2,
//                        CustomConstants.CORP_TAKE_PICTURE2);
//                break;
//            case CustomConstants.CORP_TAKE_PICTURE1:
//                if (resultCode != -1) {
//                    return;
//                }
//                mDataList1 = setPicToView(data, path, mDataList1, 0);
//
//                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);
//                initView();
//                break;
//            case CustomConstants.CORP_TAKE_PICTURE2:
//                if (resultCode != -1) {
//                    return;
//                }
//                mDataList2 = setPicToView(data, path2, mDataList2, 0);
//
//                saveTempToPref(CustomConstants.PREF_TEMP_IMAGES2, mDataList2);
//                initView();
//                break;
//
//            default:
//                isFlagCard();
//                break;
//        }
//    }
//
//    private void sendDataByUUid() {
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
//                                Withdrawals mInfoEntry = gson.fromJson(
//                                        jsonresult.toString(),
//                                        Withdrawals.class);
//                                url = UrlEntry.UPP_CARD_URL;
//                                setView(mInfoEntry);
//                            } else if (jsonresult.get("success").toString()
//                                    .equals("5")) {
//                                url = UrlEntry.ADD_CARD_URL;
//                                GsonBuilder gsonb = new GsonBuilder();
//                                Gson gson = gsonb.create();
//                                Withdrawals mInfoEntry = null;
//                                mInfoEntry = gson.fromJson(
//                                        jsonresult.toString(),
//                                        Withdrawals.class);
//
//                                setView(mInfoEntry);
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
//
//    /****************************************************************/
//    class OnClickLintener implements OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.item_popupwindows_camera: // 相机
//
//                    if (TAKE_PHOTO == CustomConstants.TAKE_PHOTO) {
//                        mFile = getFile();
//                        path = getPath(mFile);
//                    } else if (TAKE_PHOTO == CustomConstants.TAKE_PHOTO2) {
//                        mFile = getFile();
//                        path2 = getPath(mFile);
//                    }
//                    takePhoto(TAKE_PHOTO, mFile);
//                    mPwDialog.dismiss();
//                    break;
//                case R.id.item_popupwindows_Photo: // 相册
//
//                    takeAlbum(TAKE_PICTURE);
//
//                    mPwDialog.dismiss();
//                    break;
//                case R.id.item_popupwindows_cancel:// 取消
//                    mPwDialog.dismiss();
//                    break;
//                default:
//                    break;
//            }
//
//        }
//
//    }
//
//    private void showPopDialog(View parent) {
//
//        // 自定义的单击事件
//        OnClickLintener paramOnClickListener = new OnClickLintener();
//        mPwDialog = new PopupWindows(ActivityWithdrawals.this,
//                paramOnClickListener, parent);
//        // 如果窗口存在，则更新
//        mPwDialog.update();
//    }
//
//    private void initImage() {
//        if (isShowAddItem(mDataList1, 0)) {
//            imagepath1 = "";
////			iv_identity.setImageResource(R.drawable.btn_add_pic);
//			iv_identity.setBackgroundResource(R.color.bg_gray);
//        } else {
//            final ImageItem item = mDataList1.get(0);
//            ImageDisplayer.getInstance(this).displayBmp(iv_identity,
//                    item.thumbnailPath, item.sourcePath);
//            imagepath1 = item.sourcePath;
//        }
//        if (isShowAddItem(mDataList2, 0)) {
//            imagepath2 = "";
////			iv_fidentity.setImageResource(R.drawable.btn_add_pic);
//			iv_fidentity.setBackgroundResource(R.color.bg_gray);
//        } else {
//            final ImageItem item2 = mDataList2.get(0);
//            ImageDisplayer.getInstance(this).displayBmp(iv_fidentity,
//                    item2.thumbnailPath, item2.sourcePath);
//            imagepath2 = item2.sourcePath;
//        }
//    }
//
//    protected void onPause() {
//        super.onPause();
//        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);
//        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES2, mDataList2);
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putString("path", path);
//        outState.putString("path2", path2);
//        super.onSaveInstanceState(outState);
//        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES1, mDataList1);
//        saveTempToPref(CustomConstants.PREF_TEMP_IMAGES2, mDataList2);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        if (savedInstanceState != null) {
//            path = savedInstanceState.getString("path");
//            path2 = savedInstanceState.getString("path2");
//
//            initView();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // 返回这里需要刷新
//        initView();
//    }
////    @Override
////    public void onConfigurationChanged(Configuration newConfig) {
////       // 其实这里什么都不要做
////        super.onConfigurationChanged(newConfig);
////    }
//}
