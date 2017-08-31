package com.szmy.pigapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.RippleLayout;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.ActivityDetails;
import com.szmy.pigapp.activity.ActivityScanResult;
import com.szmy.pigapp.activity.HealthAuthActivity;
import com.szmy.pigapp.activity.IntegralActivity;
import com.szmy.pigapp.activity.MyOrderActivity;
import com.szmy.pigapp.activity.MyTuiGuangActivity;
import com.szmy.pigapp.activity.MyVehicleActivity;
import com.szmy.pigapp.activity.SetActivity;
import com.szmy.pigapp.activity.SlaughterhouseRenZhengActivity;
import com.szmy.pigapp.activity.UserInfoActivity;
import com.szmy.pigapp.activity.ZhuChangRenZhengActivity;
import com.szmy.pigapp.agent.AgentRenZhengActivity;
import com.szmy.pigapp.daijinquan.DaijinquanListActivity;
import com.szmy.pigapp.distributor.DistributorIntegralActivity;
import com.szmy.pigapp.distributor.JoinDistributorActivity;
import com.szmy.pigapp.mycare.MyCareListActivity;
import com.szmy.pigapp.mynotices.MyNoticeListActivity;
import com.szmy.pigapp.natural.NaturalAuthActivity;
import com.szmy.pigapp.quotedprice.NewQuotedPriceActivity;
import com.szmy.pigapp.share.UMSharePopupWindow;
import com.szmy.pigapp.sign.SignActivity;
import com.szmy.pigapp.tongji.StatisticsAdminActivity;
import com.szmy.pigapp.tongji.StatisticsUserActivity;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.HeadUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.utils.ZmtSignBean;
import com.szmy.pigapp.vehicle.MyVehicleOrderActivity;
import com.szmy.pigapp.vehicle.VehicleCompanyAuthActivity;
import com.szmy.pigapp.widget.DaijinquanPopuWindow;
import com.szmy.pigapp.zxing.activity.CaptureActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @version 1.0
 * @since 1.0
 */
public class MoreFragment extends BaseFragment implements OnClickListener {
    private TextView tv_username;
    private ImageView img_head;
    private RelativeLayout userinfo_layout;
    private LinearLayout
            myshoporder_layout, mycollect_layout,
            testlayout;
    private static final int CHOOSE_PICTURE_IMAGE_FIRST = 11;
    private static final int CAMERAL_PICTURE_IMAGE_FIRST = 01;
    private static final int REFRESH = 22;
    private static final int REFASH_PHOTO = 22;
    private BitmapUtils bitmapUtils;
    private File mCurPhotoFile;
    private String picPath = "";
    private View v;
    private SharedPreferences sp;
    private String text = "来自猪贸通的分享：";
    private String imageurl = UrlEntry.ip + "/appDownload/icon.png";
    private String title = "神州牧易";
    private String url = UrlEntry.ip + "/appDownload/download.jsp";
    private String content = "我正在使用猪贸通,可以随时随地查看生猪收购出售信息..";
    public static final String SHARE_APP_KEY = "a9cafa2614f8";
    private Button shareButton;
    // private SharePopupWindow share;
    public static int SHARE_TYPE = 55;
    private boolean mIsShare = false;
    private TextView mTvMenu;
    private RippleLayout mytuijian_layout, mypoint_layout, myorder_layout, set_layout, erweimalayout, shiminglayout, mywuliuorder_layout, myprice_layout, mLlStatistics;
    private TextView mTvBackstage;
    private String roletype = "1";
    private UMSharePopupWindow mUmPopup;
    private RippleLayout mLlMyCare;
    private SharedPreferences usersp;
    private RippleLayout mLlSign, mywuliu_layout, mRlFxs, mRlShopOrder, mRlScryTc,mRlMyNewsSc;
    private TextView mTvText;
    private List<String> cityList = new ArrayList<String>();
    private ImageView mImgUserVip;
    private ImageView mImgUserRenZ;
    private ViewGroup group;
    private RelativeLayout mLlTop;
    private SharedPreferences leveSp;
    private LinearLayout mLlMyNewsSc;
    private LinearLayout mLlDaiKuan;
    private RippleLayout mRlSqdk,mRlDkjl;
    private DaijinquanPopuWindow mPwDialog;
    private LinearLayout mLlDjq;
    private RippleLayout mRlDjq;
    private RippleLayout mRlMyNotice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_more, container, false);
        usersp = getActivity().getSharedPreferences(App.USERINFO,
                0);
        if (!TextUtils.isEmpty(usersp.getString("userinfo", ""))) {
            App.mUserInfo = FileUtil.readUser(getActivity());
            App.setDataInfo(App.mUserInfo);
        }
        clearTempFromPref();
        ((TextView) v.findViewById(R.id.def_head_tv)).setText("个人中心");
        mTvMenu = (TextView) v.findViewById(R.id.tv_menu);
        mTvMenu.setText("扫一扫");
        mTvMenu.setVisibility(View.GONE);
        mTvMenu.setOnClickListener(this);
        mLlSign = (RippleLayout) v.findViewById(R.id.ll_sign);
        mLlSign.setOnClickListener(this);
        bitmapUtils = new BitmapUtils(getActivity());
        this.v = v;
        initView(v);

       isScfx();


        return v;
    }

    private void  isScfx(){
        if (!TextUtils.isEmpty(App.uuid)){
            isScryData();
        }
//        if (!App.uuid.equals("")&&TextUtils.isEmpty(App.scrytype)) {
//            isScryData();
//        } else {
//            if (App.scrytype.equals("1")) {
//                mRlScryTc.setVisibility(View.VISIBLE);
//                mTvText.setText("我的分销提成");
//            } else if (App.scrytype.equals("2")) {
//                mRlScryTc.setVisibility(View.VISIBLE);
//                mTvText.setText("市场分销提成");
//            } else if (App.scrytype.equals("3")) {
//                mRlScryTc.setVisibility(View.GONE);
//            }
//        }
    }
    public void initView(View v) {
        mLlDaiKuan = (LinearLayout) v.findViewById(R.id.daikuan_layout);
        mLlDaiKuan.setVisibility(View.VISIBLE);
        mRlSqdk = (RippleLayout) v.findViewById(R.id.sqdklayout);
        mRlDkjl = (RippleLayout) v.findViewById(R.id.dkjllayout);
       leveSp = getActivity().getSharedPreferences("leve", 0);
        mRlShopOrder = (RippleLayout) v.findViewById(R.id.myshop_layout);
        img_head = (ImageView) v.findViewById(R.id.head_img);
        tv_username = (TextView) v.findViewById(R.id.username);
        myorder_layout = (RippleLayout) v.findViewById(R.id.myorder_layout);
        mywuliu_layout = (RippleLayout) v.findViewById(R.id.mywuliu_layout);
        set_layout = (RippleLayout) v.findViewById(R.id.set_layout);
        shiminglayout = (RippleLayout) v.findViewById(R.id.shiminglayout);
        erweimalayout = (RippleLayout) v.findViewById(R.id.erweimalayout);
        myshoporder_layout = (LinearLayout) v
                .findViewById(R.id.myshoporder_layout);
        mywuliuorder_layout = (RippleLayout) v
                .findViewById(R.id.mywuliuorder_layout);
        mycollect_layout = (LinearLayout) v
                .findViewById(R.id.myshopcollect_layout);
        mRlScryTc = (RippleLayout) v.findViewById(R.id.ll_myfxs);
        mTvText = (TextView) v.findViewById(R.id.srytc_tv);
        mypoint_layout = (RippleLayout) v.findViewById(R.id.mypoint_layout);
        testlayout = (LinearLayout) v.findViewById(R.id.testlayout);
        mytuijian_layout = (RippleLayout) v.findViewById(R.id.mytuijian_layout);
        myprice_layout = (RippleLayout) v.findViewById(R.id.myprice_layout);
        mTvBackstage = (TextView) v.findViewById(R.id.backstage);
        mImgUserVip = (ImageView) v.findViewById(R.id.huiyuanjibie);
        mImgUserRenZ = (ImageView) v.findViewById(R.id.rz_imageView2);
        mLlTop = (RelativeLayout) v.findViewById(R.id.top_layout);
        group = (ViewGroup) v.findViewById(R.id.xingjilayout);
        group.removeAllViews();
        if (!TextUtils.isEmpty(App.uuid)) {
            if (App.username.equals("15617806930")
                    || App.username.equals("13601111486")
                    || App.username.equals("艾米") || App.username.equals("pig")) {
                mTvBackstage.setVisibility(View.VISIBLE);
            }

        }
        mUmPopup = new UMSharePopupWindow(getActivity());
        mLlMyCare = (RippleLayout) v.findViewById(R.id.mycareuser_layout);
        mLlStatistics = (RippleLayout) v.findViewById(R.id.ll_statistics);
        mRlFxs = (RippleLayout) v.findViewById(R.id.ll_fxs);
        if (!TextUtils.isEmpty(App.fxsType) && (App.fxsType.equals("1") || App.fxsType.equals("2"))) {
            mRlFxs.setVisibility(View.VISIBLE);
        } else {
            mRlFxs.setVisibility(View.GONE);
        }
        mLlMyNewsSc = (LinearLayout) v.findViewById(R.id.mynewsshoucang_layout);
//        mLlMyNewsSc.setVisibility(View.VISIBLE);
        mRlMyNewsSc = (RippleLayout) v.findViewById(R.id.mynewsshoucang);
    mLlDjq = (LinearLayout) v.findViewById(R.id.ll_daijinquan);
        mLlDjq.setVisibility(View.VISIBLE);
        mRlDjq = (RippleLayout) v.findViewById(R.id.mydjq_layout);
        mRlMyNotice = (RippleLayout) v.findViewById(R.id.mynotice_layout);

        setViewDate();

    }
private void  showView(){
    mImgUserRenZ.setVisibility(View.VISIBLE);
     if(App.authentication.equals("2")){
        if(App.usertype.equals("1")){
            mImgUserRenZ.setImageResource(R.drawable.zhuchangyrz);
        }else if (App.usertype.equals("2")){
            mImgUserRenZ.setImageResource(R.drawable.tuzaichangyrz);
        }else if (App.usertype.equals("3")){
            mImgUserRenZ.setImageResource(R.drawable.jingjiren_yirenz);
        }
    }else{
        if(App.usertype.equals("1")){
            mImgUserRenZ.setImageResource(R.drawable.zhuchangwrz);
        }else if (App.usertype.equals("2")){
            mImgUserRenZ.setImageResource(R.drawable.tuzaichangwrz);
        }else if (App.usertype.equals("3")){
            mImgUserRenZ.setImageResource(R.drawable.jingjiren_renzheng);
        }
    }
}
    public void setViewDate() {
        myorder_layout.setOnClickListener(this);
        mywuliu_layout.setOnClickListener(this);
        set_layout.setOnClickListener(this);
        shiminglayout.setOnClickListener(this);
        erweimalayout.setOnClickListener(this);
        myshoporder_layout.setOnClickListener(this);
        mywuliuorder_layout.setOnClickListener(this);
        testlayout.setOnClickListener(this);
        mycollect_layout.setOnClickListener(this);
        mypoint_layout.setOnClickListener(this);
        mytuijian_layout.setOnClickListener(this);
        myprice_layout.setOnClickListener(this);
        mTvBackstage.setOnClickListener(this);
        mLlMyCare.setOnClickListener(this);
        mLlStatistics.setOnClickListener(this);
        mRlFxs.setOnClickListener(this);
        mRlShopOrder.setOnClickListener(this);
        mRlScryTc.setOnClickListener(this);
        mLlTop.setOnClickListener(this);
        mRlMyNewsSc.setOnClickListener(this);
        mRlDkjl.setOnClickListener(this);
        mRlSqdk.setOnClickListener(this);
        mRlDjq.setOnClickListener(this);
        mRlMyNotice.setOnClickListener(this);
        if (!TextUtils.isEmpty(App.uuid)) {
            img_head.setOnClickListener(this);
            tv_username.setText(App.username);
//            showView();
            //设置等级显示
//            String oldresult =  leveSp.getString("result","");
//            if (TextUtils.isEmpty(oldresult)){
                getUserLevel();
//            }else{
//                showDengji(oldresult);
//            }

            if (App.userphoto.equals("")) {
                // img_head.setImageResource(R.drawable.def);
                // Bitmap bitmap=readBitMap(getActivity(),R.drawable.def);
                // img_head.setImageBitmap(bitmap);
                Bitmap def = BitmapFactory.decodeResource(getResources(),
                        R.drawable.def);
                if (def != null) {
                    img_head.setImageBitmap(def);
                }
            } else {
                Bitmap def = BitmapFactory.decodeResource(getResources(),
                        R.drawable.def);
                if (def != null) {
                    bitmapUtils.configDefaultLoadFailedImage(R.drawable.def);
                }
                ImageLoader.getInstance().displayImage(
                        UrlEntry.ip + App.userphoto, img_head,
                        AppStaticUtil.getOptions(),
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri,
                                                          View view,
                                                          Bitmap loadedImage) {
                                super.onLoadingComplete(imageUri, view,
                                        loadedImage);
                                img_head.setImageBitmap(AppStaticUtil
                                        .toRoundBitmap(loadedImage));
                            }
                        }, new ImageLoadingProgressListener() {

                            @Override
                            public void onProgressUpdate(String imageUri,
                                                         View view, int current, int total) {
                            }
                        });
                // bitmapUtils.display(img_head, UrlEntry.ip + App.userphoto);//
                // 下载并显示图片
            }
        } else {
            tv_username.setText("请先登录");
            // img_head.setImageResource(R.drawable.def);
            Bitmap def = BitmapFactory.decodeResource(getResources(),
                    R.drawable.def);
            if (def != null) {
                img_head.setImageBitmap(def);
            }

            // Bitmap bitmap=readBitMap(getActivity(),R.drawable.def);
            // img_head.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View arg0) {
        final Intent intent;
        switch (arg0.getId()) {
            case R.id.top_layout:

                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    intent = new Intent(getActivity(), UserInfoActivity.class);
                    startActivityForResult(intent, REFASH_PHOTO);
                }
                break;
            case R.id.head_img:
                // final CharSequence[] items = { "相册", "拍照" };
                // AlertDialog dlg = new AlertDialog.Builder(getActivity())
                // .setTitle("选择图片")
                // .setItems(items, new DialogInterface.OnClickListener() {
                // public void onClick(DialogInterface dialog, int item) {
                // if (item == 1) {
                // try {
                // doTakePhoto();
                // } catch (Exception e) {
                // e.printStackTrace();
                // }
                // } else {
                // Intent openAlbumIntent = new Intent(
                // Intent.ACTION_GET_CONTENT);
                // openAlbumIntent.setType("image/*");
                // startActivityForResult(openAlbumIntent,
                // CHOOSE_PICTURE_IMAGE_FIRST);
                // }
                // }
                // }).create();
                // dlg.setCanceledOnTouchOutside(true);
                // dlg.show();
                break;
            case R.id.myorder_layout:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    Intent order = new Intent(getActivity(), MyOrderActivity.class);
                    startActivity(order);
                }
                break;
            // case R.id.myshoporder_layout:
            // if (isLogin(getActivity())) {
            // showToast("请先登录！");
            // } else {
            // Intent order = new Intent(getActivity(),
            // cn.my360.shop.activity.MyOrderActivity.class);
            // startActivity(order);
            // }
            // break;
            case R.id.mywuliu_layout:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    Intent v = new Intent(getActivity(), MyVehicleActivity.class);
                    startActivity(v);
                }
                break;
            case R.id.shiminglayout:// 1为猪场,2为屠宰场,3为经纪人4物流公司
                clearTempFromPref();
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    if (TextUtils.isEmpty(App.usertype)||App.usertype.equals("0")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                getActivity());
                        builder.setIcon(R.drawable.ic_launcher);
                        builder.setTitle("请完善角色");
                        final String[] roles = {"猪场", "屠宰场", "经纪人", "物流公司", "自然人"};
                        // 设置一个单项选择下拉框
                        builder.setSingleChoiceItems(roles, 0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        roletype = String.valueOf(which + 1);
                                    }
                                });
                        builder.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        sendData(roletype);
                                    }
                                });
                        builder.show();
                    } else
                        skipAuth();
                }
                break;
            case R.id.set_layout:
                Intent setintent = new Intent(getActivity(), SetActivity.class);
                startActivity(setintent);
                break;
            case R.id.ll_sign:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    intent = new Intent(getActivity(), SignActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_menu:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
//					mUmPopup.popupPublishMenu();

                    // 打开扫描界面扫描条形码或二维码
                    Intent openCameraIntent = new Intent(getActivity(),
                            CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 0);

                }
                break;
            case R.id.erweimalayout:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    mUmPopup.popupPublishMenu();


                }
                break;
            case R.id.mywuliuorder_layout:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    Intent v = new Intent(getActivity(),
                            MyVehicleOrderActivity.class);
                    startActivity(v);
                }
                break;
            case R.id.testlayout:
                final EditText et = new EditText(getActivity());
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("请输入要修改的ip")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(et)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        // UrlEntry.ip = et.getText().toString()
                                        // .trim();
                                    }
                                }).setNegativeButton("取消", null).create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
            // case R.id.myshopcollect_layout:
            // if (isLogin(getActivity())) {
            // showToast("请先登录！");
            // } else {
            // Intent v = new Intent(getActivity(),
            // ProductCollectActivity.class);
            // startActivity(v);
            // }
            // break;
            case R.id.mypoint_layout:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    startActivity(new Intent(getActivity(), IntegralActivity.class));
                }
                break;
            case R.id.mytuijian_layout:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    startActivityForResult(new Intent(getActivity(),
                            MyTuiGuangActivity.class), SHARE_TYPE);
                }
                break;
            case R.id.myprice_layout:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
//                    startActivityForResult(new Intent(getActivity(),
//                            NewPigPriceActivity.class), SHARE_TYPE);
                    startActivityForResult(new Intent(getActivity(),
                            NewQuotedPriceActivity.class), SHARE_TYPE);
                }

                break;
            case R.id.backstage:
                intent = new Intent(getActivity(), ActivityDetails.class);
                intent.putExtra("type", "backstage");
                intent.putExtra("url", "http://www.my360.cn/manage/admin.jsp");
                startActivity(intent);
                break;
            case R.id.mycareuser_layout:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    startActivity(new Intent(getActivity(), MyCareListActivity.class));
                }
                break;
            case R.id.ll_statistics:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else
                    isAdmin();
                break;
            case R.id.ll_fxs:
                //加盟分销商
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    intent = new Intent(getActivity(), JoinDistributorActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.myshop_layout:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    intent = new Intent(getActivity(), ActivityScanResult.class);
                    intent.putExtra(
                            "url",
                            UrlEntry.MYSHOP_ORDER_URL
                                    + (TextUtils
                                    .isEmpty(getUserUUid(getActivity())) ? "1"
                                    : getUserUUid(getActivity())));
                    startActivity(intent);
                }
                break;
            case R.id.ll_myfxs://市场提成
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    intent = new Intent (getActivity(), DistributorIntegralActivity.class);
                    intent.putExtra("type",App.scrytype);
                    if (cityList.size()!=0){
                       intent.putStringArrayListExtra("cityList", (ArrayList<String>) cityList);
                    }
                    startActivity(intent);
                }
                break;
            case R.id.mynewsshoucang://我的咨询收藏
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
//                    intent = new Intent (getActivity(), MyNewsListActivity.class);
//                    startActivity(intent);
                }
                break;
            case R.id.sqdklayout:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    intent = new Intent(getActivity(), ActivityScanResult.class);
                    intent.putExtra(
                            "url",
                            UrlEntry.SHENQING_DAIKUAN_URL
                                    + (TextUtils
                                    .isEmpty(getUserUUid(getActivity())) ? "1"
                                    : getUserUUid(getActivity())));
                    startActivity(intent);
                }
                break;
            case R.id.dkjllayout:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    intent = new Intent(getActivity(), ActivityScanResult.class);
                    intent.putExtra(
                            "url",
                            UrlEntry.SHENQING_DAIKUAN_LIST_URL
                                    + (TextUtils
                                    .isEmpty(getUserUUid(getActivity())) ? "1"
                                    : getUserUUid(getActivity())));
                    startActivity(intent);
                }
                break;
            case R.id.mydjq_layout:
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    intent = new Intent(getActivity(), DaijinquanListActivity.class);
                    startActivity(intent);

                }
//                showPopDialog(mRlDjq);
                break;
            case R.id.mynotice_layout://我的消息
                if (isLogin(getActivity())) {
                    showToast("请先登录！");
                } else {
                    intent = new Intent(getActivity(), MyNoticeListActivity.class);
                    startActivity(intent);
                }
                break;
        }
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
                                showToast("修改成功");
                                App.usertype = jsonresult.optString("type");
                                editor.putString("TYPE",
                                        jsonresult.optString("type"));
                                editor.commit();
                                App.mUserInfo.setType(jsonresult
                                        .optString("type"));
                                FileUtil.saveUser(getActivity(), App.mUserInfo);
                                App.setDataInfo(App.mUserInfo);
                                skipAuth();
                            } else {
                                showToast("修改失败,请稍后再试！");
                            }
                        } catch (JSONException e) {
                            showToast("修改失败,请稍后再试！");
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        showToast("修改失败,请稍后再试！");
                        disDialog();
                    }
                });
    }

    private void skipAuth() {

        Intent intent;
        if (App.usertype.equals("1")) { // 猪场
            clearMapTempFronPref();
            Intent zcintent = new Intent(getActivity(),
                    ZhuChangRenZhengActivity.class);
            startActivity(zcintent);
        } else if (App.usertype.equals("2")) {// 屠宰场
            Intent zcintent = new Intent(getActivity(),
                    SlaughterhouseRenZhengActivity.class);
            startActivity(zcintent);
        } else if (App.usertype.equals("3")) {// 经纪人
            clearMapTempFronPref();
            Intent zcintent = new Intent(getActivity(),
                    AgentRenZhengActivity.class);
            startActivity(zcintent);
        } else if (App.usertype.equals("4")) {
            startActivity(new Intent(getActivity(),
                    VehicleCompanyAuthActivity.class));
        } else if (App.usertype.equals("5")) {
            startActivity(new Intent(getActivity(), NaturalAuthActivity.class));
        } else if (App.usertype.equals("6")) {
            intent = new Intent(getActivity(), HealthAuthActivity.class);
            intent.putExtra("type", "6");
            startActivity(intent);
        } else if (App.usertype.equals("7")) {
            intent = new Intent(getActivity(), HealthAuthActivity.class);
            intent.putExtra("type", "7");
            startActivity(intent);
        } else if (App.usertype.equals("8")) {
            intent = new Intent(getActivity(), HealthAuthActivity.class);
            intent.putExtra("type", "8");
            startActivity(intent);
        } else {
            showToast("敬请期待！");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("car", "  resultCode = " + resultCode);
        if (!TextUtils.isEmpty(usersp.getString("userinfo", ""))) {
            App.mUserInfo = FileUtil.readUser(getActivity());
            App.setDataInfo(App.mUserInfo);
        }
        isScfx();
        if (!TextUtils.isEmpty(App.uuid)) {
            if (App.username.equals("15617806930")
                    || App.username.equals("13601111486")
                    || App.username.equals("艾米") || App.username.equals("pig")) {
                mTvBackstage.setVisibility(View.VISIBLE);
            }
        } else {
            mTvBackstage.setVisibility(View.GONE);
        }
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PICTURE_IMAGE_FIRST:
                    HeadUtil.setViewByRequest(data, getActivity(), img_head);
                    upHeadImage();
                    break;
                case 0:
                    // 处理扫描结果（在界面上显示）
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
//							showToast("--------result" + scanResult);
                    if (scanResult.startsWith("loginCode")) {
                        String newuuid = scanResult.split("-")[1];
                        sendNewUUid(newuuid);
                    } else {
                        showToast("二维码信息错误");
                    }
                    break;
                default:
                    break;
            }
        }
        if (resultCode == REFASH_PHOTO) {
            System.out.println("刷新当前页面" + App.uuid);
            if (!TextUtils.isEmpty(App.uuid)) {
                // img_head.setOnClickListener(this);
                SharedPreferences  leveSp = getActivity().getSharedPreferences("leve", 0);
                Editor editor1 = leveSp.edit();
                editor1.putString("result","");
                editor1.commit();
//                showView();
                getUserLevel();
                tv_username.setText(App.username);
                if (TextUtils.isEmpty(App.fxsType)) {
                    mRlFxs.setVisibility(View.GONE);
                } else {
                    mRlFxs.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(App.userphoto)) {
                    Bitmap def = BitmapFactory.decodeResource(getResources(),
                            R.drawable.def);
                    if (def != null) {
                        img_head.setImageBitmap(def);
                    }
                } else {
                    // Bitmap def = BitmapFactory.decodeResource(getResources(),
                    // R.drawable.def);
                    // if (def != null) {
                    //
                    // }
                    bitmapUtils.configDefaultLoadFailedImage(R.drawable.def);
                    // bitmapUtils.display(img_head, UrlEntry.ip +
                    // App.userphoto);// 下载并显示图片
                    ImageLoader.getInstance().displayImage(
                            UrlEntry.ip + App.userphoto, img_head,
                            AppStaticUtil.getOptions(),
                            new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingComplete(String imageUri,
                                                              View view,
                                                              Bitmap loadedImage) {
                                    super.onLoadingComplete(imageUri, view,
                                            loadedImage);
                                    img_head.setImageBitmap(AppStaticUtil
                                            .toRoundBitmap(loadedImage));
                                }
                            }, new ImageLoadingProgressListener() {

                                @Override
                                public void onProgressUpdate(String imageUri,
                                                             View view, int current, int total) {
                                }
                            });
                }
            } else {
                tv_username.setText("请先登录");
                // img_head.setImageResource(R.drawable.def);
                // Bitmap bitmap=readBitMap(getActivity(),R.drawable.def);
                // img_head.setImageBitmap(bitmap);
                mImgUserRenZ.setVisibility(View.GONE);
                group.removeAllViews();
                mImgUserVip.setImageResource(R.drawable.ic_vip0);
                Bitmap def = BitmapFactory.decodeResource(getResources(),
                        R.drawable.def);
                if (def != null) {
                    img_head.setImageBitmap(def);
                }
            }
        }
        if (resultCode == SHARE_TYPE) {
            // setShare();
            mIsShare = true;
        }
    }

    public void upHeadImage() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        params.addBodyParameter("uploadImage", new File(FileUtil.IMG_PATH
                + "/head.jpg"));
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.UPLOAD_USERINFO_URL,
                params, new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                        // resultText.setText(current + "/" + total);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.i("aaaa", " head  responseInfo.result = "
                                + responseInfo.result);
                        getDTOArrayUp(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.i("aaaa", " head  msg = " + msg);
                    }
                });
    }

    public void getDTOArrayUp(String jsonString) {
        try {
            JSONObject jobj = new JSONObject(jsonString);
            if (jobj != null && jobj.getString("success").equals("1")) {
                // 保存头像信息
                SharedPreferences mSharedPreferences = getActivity()
                        .getSharedPreferences(App.USERINFO, 0);
                Editor edit = mSharedPreferences.edit();
                edit.putString("PHOTO", jobj.getString("picture"));
                edit.commit();
                App.userphoto = jobj.getString("picture");
            } else {
                Toast.makeText(getActivity(), "上传失败！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MoreFragment"); // 统计页面
        if (mIsShare) {
            mIsShare = false;
            mUmPopup.popupPublishMenu();
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MoreFragment");
    }

    private void skipStatistics(boolean isAdmin) {
        Class c = StatisticsUserActivity.class;
        if (isAdmin)
            c = StatisticsAdminActivity.class;
        Intent zcintent = new Intent(getActivity(),
                c);

        startActivity(zcintent);
    }

    private void isAdmin() {
        showDialog();
        RequestParams params = new RequestParams();
        Map<String, String> maplist = new TreeMap<String, String>();
        maplist.put("uuid", App.uuid);
        params.addBodyParameter("api_sign", ZmtSignBean.sign(maplist,ZmtSignBean.secret));
        params.addBodyParameter("uuid", App.uuid);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.IS_ADMIN_URL,
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
                        Log.i("isAdmin", "isAdmin responseInfo.result = "
                                + responseInfo.result);
                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {
                                String isAdmin = jsonresult.optString("isAdmin");
                                if (!TextUtils.isEmpty(isAdmin) && isAdmin.equals("1"))
                                    skipStatistics(true);
                                else
                                    skipStatistics(false);
                            } else {
                                successType(jsonresult.get("success").toString(), "操作失败");

                            }
                        } catch (JSONException e) {
                            showToast("用户验证失败，请重试");
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        showToast("用户验证失败，请重试");
                        disDialog();
                    }
                });
    }

    /***
     * 判断是否是市场人员
     *
     * @param
     */
    public void isScryData() {
        cityList.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        params.addBodyParameter("METHOD_CODE", "20161123_00001_00001");
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.IS_SCRY_URL,
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
                                if (jsonresult.optString("isScfx").equals("n")) {
                                    App.scrytype = "3";
                                    App.mUserInfo.setScrytype("3");
                                    mRlScryTc.setVisibility(View.GONE);
                                } else {
                                    mRlScryTc.setVisibility(View.VISIBLE);
                                    if (jsonresult.optString("type").equals("1")){
                                        mTvText.setText("我的分销提成");
                                        JSONArray jsonarray = jsonresult.optJSONArray("list");
                                        for (int i =0 ;i <jsonarray.length();i++) {
                                            JSONObject jobj2 = jsonarray.optJSONObject(i);
                                            cityList.add(jobj2.optString("province")+" "+jobj2.optString("city"));
                                        }
                                    }else  if (jsonresult.optString("type").equals("2")){
                                        mTvText.setText("市场分销提成");
                                    }
                                    App.scrytype = jsonresult.optString("type");
                                    App.mUserInfo.setScrytype(jsonresult.optString("type"));
                                }
                                FileUtil.saveUser(getActivity(), App.mUserInfo);
                                App.setDataInfo(App.mUserInfo);
                            } else {

                            }
                        } catch (JSONException e) {

                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                });
    }


    public void getUserLevel() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("uuid", App.uuid);
        final HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlEntry.GET_USER_LEVE_URL,
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
                        Log.i("level", " level responseInfo.result = "
                                + responseInfo.result);
                        String oldresult =  leveSp.getString("result","");
                        JSONObject jsonresult;
                        try {
                            jsonresult = new JSONObject(responseInfo.result);// 0普通会员  1 白银会员 2 黄金会员 3 铂金会员
//                            jsonresult = new JSONObject("{'s':1,'lv':3,'success':1,'k':2,'y':2,'x':5}");
                            if (jsonresult.get("success").toString()
                                    .equals("1")) {
                                if(TextUtils.isEmpty(oldresult)){
                                    Editor editor = leveSp.edit();
                                    editor.putString("result",responseInfo.result);
                                    editor.commit();
                                    oldresult = responseInfo.result;
                                    showDengji(oldresult);
                                }else{
                                    if (oldresult.equals(responseInfo.result)){
                                        showDengji(oldresult);
                                    }else{
                                        Editor editor = leveSp.edit();
                                        editor.putString("result",responseInfo.result);
                                        editor.commit();
                                        oldresult = responseInfo.result;
                                        showDengji(oldresult);
                                    }

                                }

                            } else {
                                if(!TextUtils.isEmpty(oldresult)){
                                showDengji(oldresult);
                                }
                            }
                        } catch (JSONException e) {

                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        disDialog();
                    }
                });
    }
    private  void showDengji(String  result){
        JSONObject  jsonresult = null;// 0普通会员  1 白银会员 2 黄金会员 3 铂金会员
        try {
            jsonresult = new JSONObject(result);
            if(jsonresult.optString("vip").equals("0")){
                mImgUserVip.setImageResource(R.drawable.ic_vip0);
            }else  if(jsonresult.optString("vip").equals("1")){
                mImgUserVip.setImageResource(R.drawable.ic_vip1);
            }else  if(jsonresult.optString("vip").equals("2")){
                mImgUserVip.setImageResource(R.drawable.ic_vip2);
            }else  if(jsonresult.optString("vip").equals("3")){
                mImgUserVip.setImageResource(R.drawable.ic_vip3);
            }

            if (Integer.parseInt(jsonresult.optString("k"))>0){
                ImageView[] imageViews = new ImageView[Integer.parseInt(jsonresult.optString("k"))];
                for (int i = 0; i < imageViews.length; i++) {
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(45, ViewGroup.LayoutParams.WRAP_CONTENT));
                    imageViews[i] = imageView;
                    imageView.setImageResource(R.drawable.ic_huangguan);
                    group.addView(imageView);
                }
            }
            if (Integer.parseInt(jsonresult.optString("s"))>0){
                ImageView[] imageViews = new ImageView[Integer.parseInt(jsonresult.optString("s"))];
                for (int i = 0; i < imageViews.length; i++) {
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(45, ViewGroup.LayoutParams.WRAP_CONTENT));
                    imageViews[i] = imageView;
                    imageView.setImageResource(R.drawable.ic_sun);
                    group.addView(imageView);
                }
            }
            if (Integer.parseInt(jsonresult.optString("y"))>0){
                ImageView[] imageViews = new ImageView[Integer.parseInt(jsonresult.optString("y"))];
                for (int i = 0; i < imageViews.length; i++) {
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(45, ViewGroup.LayoutParams.WRAP_CONTENT));
                    imageViews[i] = imageView;
                    imageView.setImageResource(R.drawable.ic_moon);
                    group.addView(imageView);
                }
            }
            if (Integer.parseInt(jsonresult.optString("x"))>0){
                ImageView[] imageViews = new ImageView[Integer.parseInt(jsonresult.optString("x"))];
                for (int i = 0; i < imageViews.length; i++) {
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(45, ViewGroup.LayoutParams.WRAP_CONTENT));
                    imageViews[i] = imageView;
                    imageView.setImageResource(R.drawable.ic_star);
                    group.addView(imageView);
                }

            }
            App.authentication = jsonresult.optString("authentication");
            showView();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void showPopDialog(View parent) {

        // 自定义的单击事件
        OnClickLintener paramOnClickListener = new OnClickLintener();
        mPwDialog = new DaijinquanPopuWindow(getActivity(),
                paramOnClickListener, parent,"22","");
        // 如果窗口存在，则更新
        mPwDialog.update();
    }
    class OnClickLintener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close: // 关闭
                    mPwDialog.dismiss();
                    break;
                case R.id.ok_btn: //
                    Intent intent= new Intent(getActivity(), DaijinquanListActivity.class);
                    startActivity(intent);
                    mPwDialog.dismiss();
                    break;

                default:
                    break;
            }

        }

    }
}
