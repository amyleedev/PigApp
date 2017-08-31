package com.szmy.pigapp.tixian;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.activity.SlaughterhouseRenZhengActivity;
import com.szmy.pigapp.activity.ZhuChangRenZhengActivity;
import com.szmy.pigapp.agent.AgentRenZhengActivity;
import com.szmy.pigapp.image.ImageDisplayer;
import com.szmy.pigapp.image.ImageItem;
import com.szmy.pigapp.image.PopupWindows;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.SerializableMap;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.zheshang.KaihuhangChoose;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 绑定银行卡
 */
public class ActivityWithdrawals extends BaseActivity implements
        OnClickListener {

    private TextView tvCardType;
    private EditText etCardNum;
    private EditText etCardName;
    private Button btnSubmit;
    private String url = "";
    private String id = "";
    private LinearLayout lLbackLayout;
    private EditText etIdentityCard;// 身份证
    private ImageView iv_identity;
    private ImageView iv_fidentity;
    private BitmapUtils bitmapUtils;
    private LinearLayout mLlSfzPic;
    private LinearLayout mLlSfz;
    private Boolean isSigned = false;// userCard
    private PopupWindows mPwDialog;
    private File mFile;

    private Map<String, String> urlmaps = new HashMap<>();
    private Map<String, ImageItem> picMaps = new HashMap<>();
    public String picUrl = "";
    private final int TAKE_PHOTO = 0x0000134;
    private final int TAKE_PICTURE = 0x0000135;
    private final int CORP_TAKE_PICTURE1 = 0x0000136;
    public String type = "";
    public String picTypeName = "";
    private Map<String,String> map ;
    private String roletype = "";
    private String SEND_DATA_TO_SERVICE_URL = "";
    private LinearLayout mLlTwo;
    private LinearLayout mLlRzStatus;
    private Button mBtnBack;
    private TextView mTvStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);
        if (savedInstanceState != null) {
            picUrl = savedInstanceState.getString("yezzimagepath1");
            type = savedInstanceState.getString("type");
        }
        bitmapUtils = new BitmapUtils(this);
        if (App.mUserInfo == null) {
            App.mUserInfo = FileUtil.readUser(ActivityWithdrawals.this);
        }
        App.setDataInfo(App.mUserInfo);
        initView();

       sendDataByUUid();
    }

    private void initView() {
        mLlRzStatus = (LinearLayout) findViewById(R.id.rz_status);
        mLlTwo = (LinearLayout) findViewById(R.id.rz_two_layout);
        mTvStatus = (TextView) findViewById(R.id.tv_stateP);
        mBtnBack = (Button) findViewById(R.id.btn_goback);
        if (getIntent().hasExtra("renzhengtype")){
            Bundle bundle = getIntent().getExtras();
            roletype = getIntent().getStringExtra("renzhengtype");
            SEND_DATA_TO_SERVICE_URL = bundle.getString("url");
            mLlTwo.setVisibility(View.VISIBLE);
            mLlRzStatus.setVisibility(View.VISIBLE);
            mBtnBack.setVisibility(View.VISIBLE);
            if (bundle.containsKey("status")){
                String status = bundle.getString("status");
                if (status.equals("2")) {
                    mTvStatus.setText("审核通过");
                } else if (status.equals("3")) {
                    mTvStatus.setText("审核未通过,原因：" + bundle.getString("reason"));
                } else if (status.equals("1")) {
                    mTvStatus.setText("审核中");
                }
            }
            SerializableMap serializableMap = (SerializableMap) bundle.get("map");
            map = serializableMap.getDatamap();
        }
        mBtnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearTempFromPref();
                finish();
            }
        });
        lLbackLayout = (LinearLayout) findViewById(R.id.def_head_back);
        lLbackLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
				clearTempFromPref();
                setResult(0);
                finish();
            }
        });
        btnSubmit = (Button) findViewById(R.id.btn_submitP);
        tvCardType = (TextView) findViewById(R.id.tvCardType);
        etCardNum = (EditText) findViewById(R.id.etCardNum);
        etCardName = (EditText) findViewById(R.id.etCardName);
        etIdentityCard = (EditText) findViewById(R.id.etIdentityCard);
        iv_identity = (ImageView) findViewById(R.id.iv_identity);
        iv_fidentity = (ImageView) findViewById(R.id.iv_fidentity);
        mLlSfz = (LinearLayout) findViewById(R.id.sfzlayout);
        mLlSfzPic = (LinearLayout) findViewById(R.id.sfzpiclayout);

        initImage();
    }

    private void setView(Withdrawals winfo) {

        id = winfo.getId();
        if (TextUtils.isEmpty(winfo.getBankName())){
            if (winfo.getType().equals("nyBank")
                    || winfo.getType().equals("nybank")) {
                tvCardType.setText("农业银行卡");
            } else if (winfo.getType().equals("ncxys")) {
                tvCardType.setText("农村信用社");
            }
        }else {
            tvCardType.setText(winfo.getBankName());
        }

        if (TextUtils.isEmpty(winfo.getUserCard())
                || winfo.getUserCard() == null) {
            isSigned = false;
        } else {
            isSigned = true;
            etCardNum.setEnabled(false);
            etCardNum.setTextColor(getResources().getColor(R.color.gray));
            etCardNum.setFocusable(false);
            etCardName.setTextColor(getResources().getColor(R.color.gray));
            etCardName.setEnabled(false);
            etCardName.setFocusable(false);
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
        initImage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submitP:
                // 判断卡号是否是农业银行
                if (TextUtils.isEmpty(tvCardType.getText().toString())
                        || TextUtils.isEmpty(etCardNum.getText().toString())
                        || TextUtils.isEmpty(etCardName.getText().toString())
                        || TextUtils.isEmpty(etIdentityCard.getText().toString())) {
                    showToast("带'*'项不能为空！");
                    return;
                }

                String str = FileUtil.IDCardValidate(etIdentityCard.getText()
                        .toString());
                if (!str.equals("")) {
                    showToast(str);
                    return;
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
                if (!roletype.equals("")){
                    sendDatatoService(map);
                }else{
                    showDialog();
                    sendData();
                }

                break;
            case R.id.tvCardType:
                Intent intent = new Intent(ActivityWithdrawals.this, KaihuhangChoose.class);
                intent.putExtra("tixian","true");
                startActivityForResult(intent,App.KAIHUHANG_CODE);
                break;
            case R.id.iv_identity:
                picTypeName = "sfzzm";
                showPopDialog(iv_identity);
                break;
            case R.id.iv_fidentity:
                picTypeName = "sfzfm";
                showPopDialog(iv_fidentity);
                break;
        }

    }

    public void sendData() {
        showDialog();
        RequestParams params = new RequestParams();

        if (id != "") {
            params.addBodyParameter("e.id", id);
        }
        params.addBodyParameter("uuid", App.uuid);
        params.addBodyParameter("e.bankNum", etCardNum.getText().toString());
        params.addBodyParameter("e.name", etCardName.getText().toString());
        params.addBodyParameter("e.bankName", tvCardType.getText().toString());
        if(picMaps.containsKey("sfzzm")){
            params.addBodyParameter("sfzZm",new File(picMaps.get("sfzzm").sourcePath));
        }
        if(picMaps.containsKey("sfzfm")){
            params.addBodyParameter("sfzFm",new File(picMaps.get("sfzfm").sourcePath));
        }
        params.addBodyParameter("sfzh",
                String.valueOf(etIdentityCard.getText().toString()));
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
                        Log.i("insertyhk", " insert responseInfo.result = "
                                + responseInfo.result);

                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {
                                picMaps.clear();
                                urlmaps.clear();
								clearTempFromPref();
                                if (!isSigned) {
                                    final Dialog dialog = new Dialog(ActivityWithdrawals.this, "提示","操作成功，"+ jsonresult.get("msg")
                                            .toString());

                                    dialog.setCancelable(false);
                                    dialog.addCancelButton("取消");
                                    dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            dialog.cancel();
                                            if (roletype.equals(""))
                                            setResult(26);
                                            if (roletype.equals("zhuchang")){

                                                sp = getSharedPreferences(App.USERINFO, 0);
                                                if (App.mUserInfo != null) {
                                                    App.mUserInfo.setX(String.valueOf(map.get("e.x")));
                                                    App.mUserInfo.setY(String.valueOf(map.get("e.y")));
                                                    App.mUserInfo.setProvince(map.get("e.province"));
                                                    App.mUserInfo.setCity(map.get("e.city"));
                                                    App.mUserInfo.setArea(map.get("e.area"));
                                                    App.mUserInfo.setAddress(map.get("e.address"));
                                                    App.mUserInfo.setAuthentication("1");
                                                    FileUtil.saveUser(
                                                            ActivityWithdrawals.this,
                                                            App.mUserInfo);
                                                    App.setDataInfo(App.mUserInfo);
                                                    ZhuChangRenZhengActivity.instance.finish();
                                                }
                                            }else if(roletype.equals("agent")){
                                                if (App.mUserInfo == null) {
                                                    App.mUserInfo = FileUtil
                                                            .readUser(ActivityWithdrawals.this);
                                                }
                                                if (App.mUserInfo != null) {
                                                    App.mUserInfo.setProvince(map.get("e.province"));
                                                    App.mUserInfo.setCity(map.get("e.city"));
                                                    App.mUserInfo.setArea(map.get("e.area"));
                                                    App.mUserInfo.setAuthentication("1");
                                                    FileUtil.saveUser(ActivityWithdrawals.this,
                                                            App.mUserInfo);
                                                    App.setDataInfo(App.mUserInfo);
                                                }
                                                AgentRenZhengActivity.instance.finish();
                                            }else if (roletype.equals("tuzai")){
                                                if (App.mUserInfo == null) {
                                                    App.mUserInfo = FileUtil
                                                            .readUser(ActivityWithdrawals.this);
                                                }
                                                if (App.mUserInfo != null) {
                                                    App.mUserInfo.setProvince(map.get("e.province"));
                                                    App.mUserInfo.setCity(map.get("e.city"));
                                                    App.mUserInfo.setArea(map.get("e.area"));
                                                    App.mUserInfo.setAddress(map.get("e.address"));
                                                    App.mUserInfo.setAuthentication("1");
                                                    App.mUserInfo.setX(String.valueOf(map.get("e.x")));
                                                    App.mUserInfo.setY(String.valueOf(map.get("e.y")));

                                                    FileUtil.saveUser(ActivityWithdrawals.this,
                                                            App.mUserInfo);
                                                    App.setDataInfo(App.mUserInfo);
                                                }
                                                SlaughterhouseRenZhengActivity.instance.finish();
                                            }
                                            finish();
                                        }
                                    });
                                    dialog.setOnCancelButtonClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            dialog.cancel();
                                            if (roletype.equals(""))
                                            setResult(26);
                                            if (roletype.equals("zhuchang")){
                                                ZhuChangRenZhengActivity.instance.finish();
                                            }else if(roletype.equals("agent")){
                                                AgentRenZhengActivity.instance.finish();
                                            }else if (roletype.equals("tuzai")){
                                                SlaughterhouseRenZhengActivity.instance.finish();
                                            }
                                            finish();
                                        }
                                    });
                                    dialog.show();

                                } else {
                                    showToast(jsonresult.get("msg").toString());
                                    if (roletype.equals(""))
                                    setResult(26);
                                    finish();
                                }

                            } else {
                                successType(jsonresult.get("success")
                                        .toString(), jsonresult.get("msg")
                                        .toString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == -1 && !TextUtils.isEmpty(picUrl)) {
                    picMaps.put(picTypeName, FileUtil.saveDataPic(picUrl));
                    saveMapTempToPref(picMaps);
                }
                initImage();
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
                initImage();
                break;
            case App.KAIHUHANG_CODE:
                if (data == null)
                    return;
                Bundle bundle = data.getExtras();
//                bankName = String.valueOf(bundle.get("yinhangka"));
                tvCardType.setText(String.valueOf(bundle.get("yinhangka")));
                break;
            default:
                break;
        }
    }

    private void sendDataByUUid() {
        showDialog();
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
                                Withdrawals mInfoEntry = gson.fromJson(
                                        jsonresult.toString(),
                                        Withdrawals.class);
                                url = UrlEntry.UPP_CARD_URL;
                                setView(mInfoEntry);
                            } else if (jsonresult.get("success").toString()
                                    .equals("5")) {
                                url = UrlEntry.ADD_CARD_URL;
                                GsonBuilder gsonb = new GsonBuilder();
                                Gson gson = gsonb.create();
                                Withdrawals mInfoEntry = null;
                                mInfoEntry = gson.fromJson(
                                        jsonresult.toString(),
                                        Withdrawals.class);

                                setView(mInfoEntry);
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
                default:
                    break;
            }

        }

    }

    private void showPopDialog(View parent) {

        // 自定义的单击事件
        OnClickLintener paramOnClickListener = new OnClickLintener();
        mPwDialog = new PopupWindows(ActivityWithdrawals.this,
                paramOnClickListener, parent);
        // 如果窗口存在，则更新
        mPwDialog.update();
    }

    private void initImage() {

        setImage("sfzzm", iv_identity);
        setImage("sfzfm", iv_fidentity);
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
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("picTypeName", picTypeName);
        outState.putString("type", type);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            picTypeName = savedInstanceState.getString("picTypeName");
            type = savedInstanceState.getString("type");
            initView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//         返回这里需要刷新
        initView();

    }


    //上传猪场、屠宰场、经纪人认证基本信息
    private void sendDatatoService(Map<String,String> map) {
        showDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        for(Map.Entry<String, String> entry: map.entrySet()) {
            if (entry.getKey().equals("uploadImage")||entry.getKey().equals("uploadPic")){
                params.addBodyParameter(entry.getKey(),new File(entry.getValue()));
            } else
            params.addBodyParameter(entry.getKey(), entry.getValue());

        }

        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, SEND_DATA_TO_SERVICE_URL,
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

                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {

                                    sendData();
                            } else {
                                disDialog();
                                successType(jsonresult.get("success")
                                        .toString(), "操作失败！");
                            }
                        } catch (JSONException e) {
                            disDialog();
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


}
