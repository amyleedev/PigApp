package com.szmy.pigapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.RippleTextView2;
import com.gc.materialdesign.widgets.Dialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.szmy.pigapp.R;
import com.szmy.pigapp.entity.UserInfo;
import com.szmy.pigapp.myshop.ShopMainActivity;
import com.szmy.pigapp.share.UMSharePopupWindow;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HttpUtil;
import com.szmy.pigapp.utils.MD5;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.widget.ClearEditText;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.fragment.FeedbackFragment;
import com.umeng.message.PushAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.sso.UMSsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * com.nxt.appsale.LoginActivity
 *
 * @author Amy Create at 2015-5-19.登录2:40:01
 */
public class LoginActivity extends BaseActivity {
    private LinearLayout mLlBack;
    private ClearEditText mUserView;
    private ClearEditText mPasswordView;
    private ButtonRectangle mLoginBtn;
    private RippleTextView2 mRegister;
    private RippleTextView2 mTvForgetPassword;
    private int type = 0;
    private String username, pwd;
    private SharedPreferences sp;
    private String intentsell = "",login = "";
    public static final int RESULT_REGISTER = 109;// 注册成功时返回
    private LinearLayout mLlQQLogin;
    private LinearLayout mLlWxLogin;
    private UMSharePopupWindow mUmPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUmPopup = new UMSharePopupWindow(LoginActivity.this);
        sp = getSharedPreferences(App.USERINFO, App.SHAREID);
        mLlBack = (LinearLayout) findViewById(R.id.def_head_back);
        mLlBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                Class mClass = null;
//                if(!TextUtils.isEmpty(App.source)&&App.source.equals("3")) {
//                    mClass = ActivityMain.class;
//                }else{
//                    mClass = ShopMainActivity.class;
//                }
                Intent intent = new Intent(LoginActivity.this,
                        ActivityMain.class);
                startActivity(intent);
                finish();
            }
        });
        if (getIntent().hasExtra("sell")) {
            intentsell = getIntent().getStringExtra("sell");
        }
        if (getIntent().hasExtra("login")){
            login = "login";
        }

        System.out.print("login++"+login);
        mUserView = (ClearEditText) findViewById(R.id.et_username);
        if (!TextUtils.isEmpty(sp.getString("userinfo", ""))) {
            App.mUserInfo = FileUtil.readUser(this);
            if (!TextUtils.isEmpty(App.mUserInfo.getUsername())) {
                mUserView.setText(App.mUserInfo.getUsername());
            }
            App.setDataInfo(App.mUserInfo);
        }
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mUserView.getWindowToken(), 0);
        mPasswordView = (ClearEditText) findViewById(R.id.et_password);
        mPasswordView.setHint("请输入密码");
        mLoginBtn = (ButtonRectangle) findViewById(R.id.btn_login);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                login();
            }
        });
        mRegister = (RippleTextView2) findViewById(R.id.textView2);
        mRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this,
                    RegisterActivity.class);
            String zctype = "";
                if(getIntent().hasExtra("zhuce")){
                    if(getIntent().getStringExtra("zhuce").equals("szmyzc")){

                      zctype = "1";

                    }
                }
                intent.putExtra("zctype",zctype);
                startActivityForResult(intent, RESULT_REGISTER);

            }
        });

        mTvForgetPassword = (RippleTextView2) findViewById(R.id.tv_forget_password);
        mTvForgetPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        ActivityForgotPassword.class);
                intent.putExtra("url", UrlEntry.RESET_PASSWORD_BYUUID_URL);
                startActivity(intent);
            }
        });
        mLlQQLogin = (LinearLayout) findViewById(R.id.qq_login_btn);
        mLlQQLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mUmPopup.sflogin(LoginActivity.this,SHARE_MEDIA.QQ);
            }
        });
        mLlWxLogin = (LinearLayout) findViewById(R.id.wx_login_btn);
        mLlWxLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mUmPopup.sflogin(LoginActivity.this,SHARE_MEDIA.WEIXIN);
            }
        });
    }

    private void login() {
        username = mUserView.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            showToast("用户名不能为空！");
            return;
        }
        pwd = mPasswordView.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            showToast("密码不能为空！");
            return;
        }
        showDialog();
        new Thread(runnable).start();
    }

    private String roletype = "1";
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            disDialog();
            switch (msg.what) {
                case 1:
                    editor.putBoolean(BaseActivity.SETTING_ALIAS, true);
                    editor.commit();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                PushAgent.getInstance(LoginActivity.this).addAlias(App.uuid,
                                        "SYSTEM");
                            } catch (Exception e) {
                            }
                        }
                    }).start();
                    if (mPasswordView.getText().toString().trim().equals("123456")) {
                        final Dialog dialog = new Dialog(LoginActivity.this, "提示", "登录成功！您的密码为初始密码，是否修改密码？");
                        dialog.addCancelButton("取消");
                        dialog.addAccepteButton("修改");
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                startActivityForResult(new Intent(LoginActivity.this,UpdatePwdActivity.class),911);
                            }
                        });
                        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                                setlogin();
                            }
                        });
                        dialog.show();
                    }else{
                        showToast("登录成功");
                        if (intentsell != "") {
                            Intent intent = new Intent(LoginActivity.this,
                                    ActivityMain.class);
                            startActivity(intent);
                        } else {
                            setResult(22);
                        }
                        finish();
                    }



                    break;
                case 2:
                case 3:
                    showToast("登录失败,用户名或密码错误！");
                    break;
            }
        }
    };

    private void setlogin(){
        if (intentsell != "") {
            Class mClass = null;
            if(!TextUtils.isEmpty(App.source)&&App.source.equals("3")) {
                mClass = ActivityMain.class;
            }else{
                mClass = ShopMainActivity.class;
            }
            Intent intent = new Intent(LoginActivity.this,
                    mClass);
            startActivity(intent);
        } else if (login.equals("login")){
            Class mClass = null;
            if(!TextUtils.isEmpty(App.source)&&App.source.equals("3")) {
                mClass = ActivityMain.class;
            }else{
                mClass = ShopMainActivity.class;
            }
            Intent intent = new Intent(LoginActivity.this,
                    mClass);
            startActivity(intent);
        }else{
            setResult(22);
        }
        finish();
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            HttpUtil http = new HttpUtil();
            String result = http.postDataMethod(UrlEntry.LOGIN_URL, username,
                    MD5.GetMD5Code(pwd), android.os.Build.BRAND + ":"
                            + android.os.Build.MODEL,
                    AppStaticUtil.getDeviceId(sp, editor, LoginActivity.this));
            try {
                result = result.replace("null","");
                JSONObject jsonresult = new JSONObject(result);
                if (jsonresult.get("success").toString().equals("1")) {
                    /**************************************/
                    GsonBuilder gsonb = new GsonBuilder();
                    Gson gson = gsonb.create();
                    UserInfo mInfoEntry = gson.fromJson(jsonresult.toString(),
                            UserInfo.class);
                    App.mUserInfo = mInfoEntry;
                    FileUtil.saveUser(LoginActivity.this, mInfoEntry);
                    App.setDataInfo(mInfoEntry);
                    setUserInfo(mInfoEntry.getUsername());
                    editor.putString("pwd", MD5.GetMD5Code(pwd));
                    editor.commit();
                    App.pwd = MD5.GetMD5Code(pwd);
                    result = "登录成功！";
                    msg.what = 1;
                    /******************************************/
//					if (App.usertype.equals("")) {
//						msg.what = 3;
//					}
                } else {
                    result = jsonresult.get("success").toString();
                    msg.what = 2;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            data.putString("value", result);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    private void setUserInfo(String username) {
        final FeedbackAgent agent = new FeedbackAgent(LoginActivity.this);
        com.umeng.fb.model.UserInfo info = agent.getUserInfo();
        if (info == null)
            info = new com.umeng.fb.model.UserInfo();
        Map<String, String> contact = info.getContact();
        if (contact == null)
            contact = new HashMap<String, String>();
        contact.put(FeedbackFragment.BUNDLE_KEY_CONVERSATION_ID, username);
        // contact.put("email", "*******");
        // contact.put("qq", "*******");
        // contact.put("phone", "*******");
        contact.put("plain", username);
        info.setContact(contact);
        // optional, setting user gender information.
        info.setAgeGroup(1);
        info.setGender("male");
        // info.setGender("female");
        agent.setUserInfo(info);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = agent.updateUserInfo();
            }
        }).start();
    }

    public void sendData(final String type) {
        showDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("e.type", type);
        params.addBodyParameter("uuid", App.uuid);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.UPDATE_USERINFO_URL,
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
                        Log.i("login", " login responseInfo.result = "
                                + responseInfo.result);
                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {
                                App.usertype = jsonresult.optString("type");
                                editor.putString("TYPE",
                                        jsonresult.optString("type"));
                                editor.commit();

                                App.mUserInfo.setType(jsonresult
                                        .optString("type"));
                                FileUtil.saveUser(LoginActivity.this,
                                        App.mUserInfo);
                                App.setDataInfo(App.mUserInfo);
                                Message msg = new Message();
                                msg.what = 1;
                                handler.sendMessage(msg);
                            } else {
                                // successType(jsonresult.get("success").toString(),
                                // "修改失败！");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        showToast("修改失败,请稍后再试！");
                        disDialog();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 根据requestCode获取对应的SsoHandler
        UMSsoHandler ssoHandler = mUmPopup.mController.getConfig().getSsoHandler(
                resultCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        if (data == null)
            return;
        if (resultCode == 911) {
            Bundle b = data.getExtras();
            String type = b.getString("type");
            if (type.equals("back")) {
               setlogin();
            }else if (type.equals("ok"))
            {
                setlogin();
//                mPasswordView.setText("");
            }
        }else {
            Bundle b = data.getExtras();
            username = b.getString("username");
            pwd = b.getString("password");
            mUserView.setText(username);
            mPasswordView.setText(pwd);
            showDialog();
            new Thread(runnable).start();
        }
    }
}


