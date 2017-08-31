package com.szmy.pigapp.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.szmy.pigapp.R;
import com.szmy.pigapp.daijinquan.DaijinquanListActivity;
import com.szmy.pigapp.fragment.MainFragment;
import com.szmy.pigapp.fragment.MoreFragment;
import com.szmy.pigapp.fragment.MyDealFragment;
import com.szmy.pigapp.fragment.ShopFragment;
import com.szmy.pigapp.updateservice.UpdateInfo;
import com.szmy.pigapp.updateservice.UpdateInfoService;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppManager;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.widget.DaijinquanPopuWindow;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * @version 1.0
 * @since 1.0
 */
public class ActivityMain extends BaseActivity implements
        OnCheckedChangeListener {
    public SharedPreferences app_config;
    MainFragment mMainFragment;
    MyDealFragment mNewsFragment;
    ShopFragment mShopFragment;
    // ProductClassFragment mProductFragment;
    MoreFragment mMoreFragment;
    //	CommunityFragment mCunitFragment;
    private static final String HOME = "home";
    private static final String NEWS = "news";
    private static final String MESSAGE = "message";
    private static final String SHOP = "shop";
    private static final String MORE = "more";
    FragmentManager fm;
    FragmentTransaction ft;
    RadioGroup radio;
    Dialog dialog;
    RadioButton radiobtn0, radiobtn1, radiobtn2;
    private Handler mHandler;
    private static String newUrl;
    private static String apkName;
    public final static String UPDATE_VERSIONS_URL = "";
    private ImageView loadingImg;
    private String target;
    private UpdateInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengUpdateAgent.setUpdateOnlyWifi(sp.getBoolean(SETTING_ONLY_WIFI,
                false));
        UmengUpdateAgent.update(this);
        setContentView(R.layout.activity_main);
        AppManager.addActivity(ActivityMain.this);
        /**自动更新*/
        // 自动检查有没有新版本 如果有新版本就提示更新
        new Thread() {
            public void run() {
                try {
                    UpdateInfoService updateInfoService = new UpdateInfoService(
                            ActivityMain.this);
                    info = updateInfoService.getUpDateInfo();
                    handler1.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
        fm = getSupportFragmentManager();
        FileUtil.createSDDir(FileUtil.SDPATH);
        radio = (RadioGroup) findViewById(R.id.radioGroup1);
        radio.setOnCheckedChangeListener(this);
        /********************/
        if (App.mUserInfo == null) {
            App.mUserInfo = FileUtil.readUser(ActivityMain.this);
        }
        App.setDataInfo(App.mUserInfo);
        /**********************/
        ft = fm.beginTransaction();
        isTabMain();
        ft.commit();

        if (!sp.getBoolean(SETTING_ALIAS, false) && !TextUtils.isEmpty(App.uuid)) {
            editor.putBoolean(BaseActivity.SETTING_ALIAS, true);
            editor.commit();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        PushAgent.getInstance(ActivityMain.this).addAlias(App.uuid,
                                "SYSTEM");
                    } catch (Exception e) {
                    }
                }
            }).start();
        }
    }
    @SuppressLint("HandlerLeak")
    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            if(info == null){
                return;
            }
            // 如果有更新就提示
            if (isNeedUpdate(ActivityMain.this,info)) {   //在下面的代码段
                showUpdateDialog(ActivityMain.this,info);  //下面的代码段
            }
        };
    };
    private void isTabMain() {
        if (mMainFragment == null) {
            ft.add(R.id.realtabcontent, new MainFragment(), HOME);
        } else {
            ft.attach(mMainFragment);
        }
    }

    private void isTabScan() {
        if (mNewsFragment == null) {
            ft.add(R.id.realtabcontent, new MyDealFragment(), NEWS);
        } else {
            ft.attach(mNewsFragment);
        }

    }

    private void isTabShop() {
        if (mShopFragment == null) {
            ft.add(R.id.realtabcontent, new ShopFragment(), SHOP);
        } else {
            ft.attach(mShopFragment);
        }
    }

    /**
     * 社区
     */
    private void isTabNews() {

//		 if (mCunitFragment == null) {
//		 ft.add(R.id.realtabcontent, new CommunityFragment(), MESSAGE);
//		 } else {
//		 ft.attach(mCunitFragment);
//		 }
    }

    private void isTabMore() {
        if (mMoreFragment == null) {
            ft.add(R.id.realtabcontent, new MoreFragment(), MORE);
        } else {
            ft.attach(mMoreFragment);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.radio1) {
            if (isLogin(getApplicationContext())) {
                showToast("请先登录！");
                checkedId = R.id.radio0;
                group.check(checkedId);
            }
        }
        mMainFragment = (MainFragment) fm.findFragmentByTag(HOME);
        mNewsFragment = (MyDealFragment) fm.findFragmentByTag(NEWS);
        mShopFragment = (ShopFragment) fm.findFragmentByTag(SHOP);
//		mCunitFragment = (CommunityFragment) fm.findFragmentByTag(MESSAGE);
        // mProductFragment = (ProductClassFragment)
        // fm.findFragmentByTag(MESSAGE);
        mMoreFragment = (MoreFragment) fm.findFragmentByTag(MORE);
        ft = fm.beginTransaction();
        // mMessageFragment.webView = null;
        if (mMainFragment != null)
            ft.detach(mMainFragment);
        if (mNewsFragment != null)
            ft.detach(mNewsFragment);
        if (mShopFragment != null)
            ft.detach(mShopFragment);
        // if (mProductFragment != null)
        // ft.detach(mProductFragment);
        if (mMoreFragment != null)
            ft.detach(mMoreFragment);
//		if(mCunitFragment != null){
//			ft.detach(mCunitFragment);
//		}
        switch (checkedId) {
            case R.id.radio0:
                isTabMain();
                break;
            case R.id.radio1:
                isTabScan();
                break;
            case R.id.radio2:
                isTabShop();
                break;
            case R.id.radio3:
                isTabMore();
                break;
            case R.id.radio4:
                isTabNews();
                break;
        }
        ft.commit();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 再按一次退出系統
     */
    private long exitTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mShopFragment != null && mShopFragment.wv.canGoBack()) {
                mShopFragment.wv.goBack();
                return true;
            }
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), R.string.again_exit,
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
