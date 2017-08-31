package com.szmy.pigapp.distributor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.AddressChoose;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.image.ImageDisplayer;
import com.szmy.pigapp.image.ImageItem;
import com.szmy.pigapp.image.PopupWindows;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 企业入驻
 *
 * @author Administrator
 */
@ContentView(R.layout.join_enterprise_activity)
public class JoinEnterpriseActivity extends BaseActivity {

    @ViewInject(R.id.gszzstar)
    private ImageView mImgGsStar;
    @ViewInject(R.id.swdjstar)
    private ImageView mImgSwStar;
    @ViewInject(R.id.scxkzstar)
    private ImageView mImgScStar;
    @ViewInject(R.id.gmpstar)
    private ImageView mImgGmpStar;
    @ViewInject(R.id.jgdmzstar)
    private ImageView mImgJgStar;
    @ViewInject(R.id.gspstar)
    private ImageView mImgGspStar;
    @ViewInject(R.id.qytbstar)
    private ImageView mImgQyStar;
    @ViewInject(R.id.sfzzmstar)
    private ImageView mImgSfzZmStar;
    @ViewInject(R.id.sfzfmstar)
    private ImageView mImgSfzFmStar;
    @ViewInject(R.id.layoutone)
    private LinearLayout mLlOne;
    @ViewInject(R.id.layouttwo)
    private LinearLayout mLlTwo;
    @ViewInject(R.id.iv_gszz)
    private ImageView mImgGs;
    @ViewInject(R.id.iv_swdjz)
    private ImageView mImgSw;
    @ViewInject(R.id.iv_gmpzs)
    private ImageView mImgGmp;
    @ViewInject(R.id.iv_scxkz)
    private ImageView mImgScXk;
    @ViewInject(R.id.iv_jgdmz)
    private ImageView mImgJg;
    @ViewInject(R.id.iv_qytb)
    private ImageView mImgQytb;
    @ViewInject(R.id.iv_gsp)
    private ImageView mImgGsp;
    @ViewInject(R.id.iv_identity)
    private ImageView mImgSfzZm;
    @ViewInject(R.id.iv_fidentity)
    private ImageView mImgSfzFm;
    @ViewInject(R.id.btn_goback)
    private Button mBtnBack;
    @ViewInject(R.id.btn_next)
    private Button mBtnNext;

//    @ViewInject(R.id.iv_camerags)
//    private ImageView mImgGsCamer;
//    @ViewInject(R.id.iv_camerasw)
//    private ImageView mImgSwCamer;
//    @ViewInject(R.id.iv_cameragmpzs)
//    private ImageView mImgGmpCamer;
//    @ViewInject(R.id.iv_camerascxkz)
//    private ImageView mImgScXkCamer;
//    @ViewInject(R.id.iv_camerajgdmz)
//    private ImageView mImgJgCamer;
//    @ViewInject(R.id.iv_cameraqytb)
//    private ImageView mImgQytbCamer;
//    @ViewInject(R.id.iv_cameragsp)
//    private ImageView mImgGspCamer;
//    @ViewInject(R.id.iv_camerasfzzm)
//    private ImageView mImgSfzZmCamer;
//    @ViewInject(R.id.iv_camerasfzfm)
//    private ImageView mImgSfzFmCamer;
    @ViewInject(R.id.et_comname)
    private EditText mEtComName;
    @ViewInject(R.id.et_frdaibiao)
    private EditText mEtFrdb;
    @ViewInject(R.id.etAddress)
    private EditText mEtAddress;
    @ViewInject(R.id.et_area)
    private EditText mEtArea;
    @ViewInject(R.id.et_bank)
    private EditText mEtBank;
    @ViewInject(R.id.et_people)
    private EditText mEtPeople;
    @ViewInject(R.id.et_phone)
    private EditText mEtPhone;
    @ViewInject(R.id.et_tellphone)
    private EditText mEtTellPhone;
    @ViewInject(R.id.et_email)
    private EditText mEtEmaill;
    @ViewInject(R.id.et_code)
    private EditText mEtCode;
    @ViewInject(R.id.etDemand)
    private EditText mEtRemark;
    @ViewInject(R.id.et_cz)
    private EditText mEtChuanz;
    @ViewInject(R.id.btn_submit)
    private Button mBtnAdd;
    @ViewInject(R.id.btn_modify)
    private Button mBtnUpp;
    @ViewInject(R.id.tv_state)
    private TextView mTvStatus;
    @ViewInject(R.id.xieyi_txt)
    private TextView mTvXieyi;
    @ViewInject(R.id.tv_sfz)
    private TextView mTvSfz;
    @ViewInject(R.id.check_fxs)
    private CheckBox mCBXieyi;
    @ViewInject(R.id.ll_fxsxy)
    private LinearLayout mLlXieyi;
    @ViewInject(R.id.sysl_layout)
    private LinearLayout mLlSySl;
    @ViewInject(R.id.gsplayout)
    private LinearLayout mLlYyfw;

    private String type = "";
    private Map<String, String> urlmaps = new HashMap<>();
    private Map<String, ImageItem> picMaps = new HashMap<>();
    private BitmapUtils bitmapUtils;
    public String picTypeName = "";
    public String picUrl = "";
    private File mFile;
    private final int TAKE_PHOTO = 0x0000134;
    private final int TAKE_PICTURE = 0x0000135;
    private final int CORP_TAKE_PICTURE1 = 0x0000136;

    private String province = "",city = "",area = "";
    private String provincecode = "",citycode = "",areacode = "";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
//        setContentView(R.layout.join_enterprise_activity);
        ViewUtils.inject(this); // 注入view和事件
        ((TextView) findViewById(R.id.def_head_tv)).setText("企业入驻");

        SharedPreferences sp = getSharedPreferences(App.USERINFO, App.SHAREID);
        if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
            App.mUserInfo = FileUtil.readUser(JoinEnterpriseActivity.this);
            App.setDataInfo(App.mUserInfo);
        }
        type = getIntent().getStringExtra("type");
        bitmapUtils = new BitmapUtils(this);
        initView();

//        showDialog();
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("uuid", App.uuid);
//        sendDataToHttp(UrlEntry.QUERY_DISTRIBUTOR_URL, params, "");
    }


    private void initView() {

        HideShowStar(type);
        final LinearLayout mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
        mLlBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLlOne.getVisibility()==View.VISIBLE){
                    clearMapTempFronPref();
                    finish();
                }else{
                    mLlOne.setVisibility(View.VISIBLE);
                    mLlTwo.setVisibility(View.GONE);
                }
            }
        });
        setImage();
    }


    private void HideShowStar(String type) {

        if (type.equals("1")) {//兽药生物制品
            mLlSySl.setVisibility(View.VISIBLE);
            mLlYyfw.setVisibility(View.GONE);
            mImgQyStar.setVisibility(View.GONE);
            mImgJgStar.setVisibility(View.GONE);
        } else if (type.equals("2")) {//饲料器械设备企业
            mLlSySl.setVisibility(View.VISIBLE);
            mLlYyfw.setVisibility(View.GONE);
            mImgQyStar.setVisibility(View.GONE);
            mImgJgStar.setVisibility(View.GONE);
        } else if(type.equals("3")){//运营服务中心
            mLlSySl.setVisibility(View.GONE);
            mLlYyfw.setVisibility(View.VISIBLE);
            mImgGsStar.setVisibility(View.GONE);
            mImgSwStar.setVisibility(View.GONE);
            mImgGspStar.setVisibility(View.GONE);
            mImgQyStar.setVisibility(View.GONE);
            mImgSfzZmStar.setVisibility(View.GONE);
            mImgSfzFmStar.setVisibility(View.GONE);
            Drawable img_off;
            Resources res = getResources();
            img_off = res.getDrawable(R.drawable.fivepic);
// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
            mTvSfz.setCompoundDrawables(img_off, null, null, null); //设置左图标
        }

    }
    private void setImage() {
        setImage("gszz", mImgGs);
        setImage("swdjz", mImgSw);
        setImage("scxkz", mImgScXk);
        setImage("gmp", mImgGmp);
        setImage("jgdmz", mImgJg);
        setImage("qytb", mImgQytb);
        setImage("gsp", mImgGsp);
        setImage("sfzzm", mImgSfzZm);
        setImage("sfzfm", mImgSfzFm);
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
                } else
                    view.setBackgroundResource(R.drawable.addfile);
            }
        }
    }

    /**
     * 提交入驻信息
     */
    public void AddDataToHttp() {

        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        params.addBodyParameter("e.type", type);
        params.addBodyParameter("METHOD_CODE", "20161213_00001_00001");
//        if (TextUtils.isEmpty(mEtComName.getText()) || TextUtils.isEmpty(mEtFrdb.getText())
//                || TextUtils.isEmpty(mEtArea.getText()) || TextUtils.isEmpty(mEtAddress.getText())
//                || TextUtils.isEmpty(mEtBank.getText()) || TextUtils.isEmpty(mEtPeople.getText())
//                || TextUtils.isEmpty(mEtTellPhone.getText())) {
//            showToast("带'*'为必填项，不能为空！");
//            return;
//        }
//        String message = FileUtil.isAgricultureCard(mEtBank.getText().toString());
//        if (!message.contains("农业银行")) {
//            showToast(message);
//            return;
//        }
//        if (mEtTellPhone.getText().toString().length() != 11) {
//            showToast("请输入11位手机号");
//            return;
//        }
        params.addBodyParameter("e.name", mEtComName.getText().toString());
        params.addBodyParameter("e.legalPerson", mEtFrdb.getText().toString());
        params.addBodyParameter("e.province", provincecode);
        params.addBodyParameter("e.city", citycode);
        params.addBodyParameter("e.area", areacode);
        params.addBodyParameter("e.address", mEtAddress.getText().toString());
        params.addBodyParameter("e.accountNumber", mEtBank.getText().toString());
        params.addBodyParameter("e.contactor", mEtPeople.getText().toString());
        params.addBodyParameter("e.contactorTelephone", mEtTellPhone.getText().toString());
        params.addBodyParameter("e.contactorPhone", mEtPhone.getText().toString());
        params.addBodyParameter("e.contactorEmail", mEtEmaill.getText().toString());
        params.addBodyParameter("e.contactorZipcode", mEtCode.getText().toString());
        params.addBodyParameter("e.contactorFax", mEtChuanz.getText().toString());
//        params.addBodyParameter("e.note", mEtRemark.getText().toString());
       String msg = "";
        msg = isPic("gszz","工商执照");
        if(msg.equals("ok")){//gsyyzz_
            params.addBodyParameter("gsyyzz_",new File(picMaps.get("gszz").sourcePath));
        }else{
            if (!msg.equals("")){
                showToast(msg);
                return;
            }
        }

        msg = isPic("swdjz","税务登记证");
        if(msg.equals("ok")){
            params.addBodyParameter("swdjz_",new File(picMaps.get("swdjz").sourcePath));
        }else{
            if (!msg.equals("")){
                showToast(msg);
                return;
            }
        }
        msg = isPic("scxkz","生产许可证");
        if(msg.equals("ok")){
            params.addBodyParameter("scxkz_",new File(picMaps.get("scxkz").sourcePath));
        }else{
            if (!msg.equals("")){
                showToast(msg);
                return;
            }
        }
        msg = isPic("gmp","兽药GMP证书");
        if(msg.equals("ok")){
            params.addBodyParameter("gmpzs_",new File(picMaps.get("gmp").sourcePath));
        }else{
            if (!msg.equals("")){
                showToast(msg);
                return;
            }
        }
        msg =isPic("jgdmz","机构代码证");
        if(msg.equals("ok")){
            params.addBodyParameter("zzjgdmz_",new File(picMaps.get("jgdmz").sourcePath));
        }else{
            if (!msg.equals("")){
                showToast(msg);
                return;
            }
        }
        msg =isPic("qytb","企业图标");
        if(msg.equals("ok")){
            params.addBodyParameter("icon_",new File(picMaps.get("qytb").sourcePath));
        }else{
            if (!msg.equals("")){
                showToast(msg);
                return;
            }
        }
        msg =isPic("sfzzm","身份证正面图片");
        if(msg.equals("ok")){
            params.addBodyParameter("sfzZm",new File(picMaps.get("sfzzm").sourcePath));
        }else{
            if (!msg.equals("")){
                showToast(msg);
                return;
            }
        }
        msg = isPic("sfzfm","身份证反面图片");
        if(msg.equals("ok")){
            params.addBodyParameter("sfzFm",new File(picMaps.get("sfzfm").sourcePath));
        }else{
            if (!msg.equals("")){
                showToast(msg);
                return;
            }
        }
        msg =isPic("gsp","GSP证书");
        if(msg.equals("ok")){
            params.addBodyParameter("gspzs_",new File(picMaps.get("gsp").sourcePath));
        }else{
            if (!msg.equals("")){
                showToast(msg);
                return;
            }
        }
        showDialog();
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.REISTER_SZMY_URL2, params,
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
                                showToast("提交成功");
                                clearMapTempFronPref();
                                finish();
                            } else if (jsonresult.optString("success").equals("2")) {
                                showDialog1(JoinEnterpriseActivity.this,jsonresult.optString("msg"));
                            } else {
                                clearMapTempFronPref();
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

    private String  isPic(String pictype,String message){
        if (picMaps.containsKey(pictype)){
            if (new File(picMaps.get(pictype).sourcePath).exists()){
//                System.out.print("ffffffffff"+picMaps.get(pictype).sourcePath);
//                params.addBodyParameter(paramsKey,new File(picMaps.get(pictype).sourcePath));
              return "ok";
            }else{
//                showToast(message+"选取错误，请重新选取。");
                return message+"选取错误，请重新选取。";
            }
        }else{
            if (pictype.equals("jgdmz")||pictype.equals("gsp")||pictype.equals("qytb")){
                return "";
            }else if (!type.equals("3")){
//                showToast("请上传您的"+message);
                return "请上传您的"+message;
            }
        }
        return "";
    }
//    public void sendDataToHttp(String url, RequestParams params,
//                               final String type) {
//
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
//
//                        Log.i("info", " info responseInfo.result = "
//                                + responseInfo.result);
//
//                        JSONObject jsonresult;
//                        try {
//                            jsonresult = new JSONObject(responseInfo.result);
//                            if (jsonresult.get("success").toString()
//                                    .equals("1")) {
//
//
//                            } else if (jsonresult.optString("success").equals("4")) {
////                                add = true;
//
//                            } else {
//
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
//                        showToast("操作失败,请稍后再试！");
//                        disDialog();
//
//                    }
//                });
//
//    }

    @OnClick({R.id.iv_gszz, R.id.iv_camerags,
            R.id.iv_swdjz, R.id.iv_camerasw,
            R.id.iv_scxkz, R.id.iv_camerascxkz,
            R.id.iv_gmpzs, R.id.iv_cameragmpzs,
            R.id.iv_jgdmz, R.id.iv_camerajgdmz,
            R.id.iv_qytb, R.id.iv_cameraqytb,
            R.id.iv_gsp, R.id.iv_cameragsp,
            R.id.iv_identity, R.id.iv_camerasfzzm,
            R.id.iv_fidentity, R.id.iv_camerasfzfm,
            R.id.et_area, R.id.btn_submit, R.id.btn_modify,
            R.id.btn_goback,R.id.btn_next
    })
    public void clickMethod(View v) {
        switch (v.getId()) {

            case R.id.btn_goback:
                mLlOne.setVisibility(View.VISIBLE);
                mLlTwo.setVisibility(View.GONE);
                break;
            case R.id.btn_next:
                if (TextUtils.isEmpty(mEtComName.getText().toString()) || TextUtils.isEmpty(mEtFrdb.getText().toString())
                        || TextUtils.isEmpty(mEtArea.getText().toString()) || TextUtils.isEmpty(mEtAddress.getText().toString())
                        || TextUtils.isEmpty(mEtBank.getText().toString()) || TextUtils.isEmpty(mEtPeople.getText().toString())
                        || TextUtils.isEmpty(mEtTellPhone.getText().toString())) {
                    showToast("带'*'为必填项，不能为空！");
                    return;
                }
                String message = FileUtil.isAgricultureCard(mEtBank.getText().toString());
                if (!message.contains("农业银行")) {
                    showToast(message);
                    return;
                }
                if (mEtTellPhone.getText().toString().length() != 11) {
                    showToast("请输入11位手机号");
                    return;
                }
                mLlTwo.setVisibility(View.VISIBLE);
                mLlOne.setVisibility(View.GONE);
                break;
            case R.id.iv_gszz:
            case R.id.iv_camerags:
                picTypeName = "gszz";
                showPopDialog(mImgGmp);
                break;
            case R.id.iv_swdjz:
            case R.id.iv_camerasw:
                picTypeName = "swdjz";
                showPopDialog(mImgSw);
                break;
            case R.id.iv_scxkz:
            case R.id.iv_camerascxkz:
                picTypeName = "scxkz";
                showPopDialog(mImgScXk);
                break;
            case R.id.iv_gmpzs:
            case R.id.iv_cameragmpzs:
                picTypeName = "gmp";
                showPopDialog(mImgGmp);
                break;
            case R.id.iv_jgdmz:
            case R.id.iv_camerajgdmz:
                picTypeName = "jgdmz";
                showPopDialog(mImgJg);
                break;
            case R.id.iv_qytb:
            case R.id.iv_cameraqytb:
                picTypeName = "qytb";
                showPopDialog(mImgQytb);
                break;
            case R.id.iv_gsp:
            case R.id.iv_cameragsp:
                picTypeName = "gsp";
                showPopDialog(mImgGsp);
                break;
            case R.id.iv_camerasfzzm:
            case R.id.iv_identity:
                picTypeName = "sfzzm";
                showPopDialog(mImgSfzZm);
                break;
            case R.id.iv_camerasfzfm:
            case R.id.iv_fidentity:
                picTypeName = "sfzfm";
                showPopDialog(mImgSfzFm);
                break;
            case R.id.et_area:
                Intent i = new Intent(JoinEnterpriseActivity.this, AddressChoose.class);
                i.putExtra("Boolean", "aaa");
                startActivityForResult(i, 99);
                break;
            case R.id.btn_submit:
                AddDataToHttp();

                break;
            case R.id.btn_modify:
                showDialog();

                break;
        }
    }

    private PopupWindows mPwDialog;

    private void showPopDialog(View parent) {

        // 自定义的单击事件
        OnClickLintener paramOnClickListener = new OnClickLintener();
        mPwDialog = new PopupWindows(JoinEnterpriseActivity.this,
                paramOnClickListener, parent);
        // 如果窗口存在，则更新
        mPwDialog.update();
    }

    class OnClickLintener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_popupwindows_camera: // 相机
                    mFile = getFile();
                    picUrl = getPath(mFile);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                initView();
                break;
            case 99:
                if (data == null)
                    return;
                Bundle b = data.getExtras();
                provincecode = b.getString("shengcode");
                citycode = b.getString("shicode");
                areacode = b.getString("qucode");
                province = b.getString("province");
                city = b.getString("city");
                area = b.getString("area");
                mEtArea.setText(province + " " + city + " " + area);
                break;
            default:
                break;
        }

    }

    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("yezzimagepath1", picUrl);
        outState.putString("type", type);

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            picUrl = savedInstanceState.getString("yezzimagepath1");
            type = savedInstanceState.getString("type");
            initView();
        }
    }
}
