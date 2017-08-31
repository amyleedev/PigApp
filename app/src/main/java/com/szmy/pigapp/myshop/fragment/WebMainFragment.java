package com.szmy.pigapp.myshop.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.Dialog;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.ActivityMain;
import com.szmy.pigapp.activity.LoginActivity;
import com.szmy.pigapp.activity.ServiceCenterActivity;
import com.szmy.pigapp.distributor.JoinDistributorActivity;
import com.szmy.pigapp.distributor.JoinEnterpriseActivity;
import com.szmy.pigapp.fragment.BaseFragment;
import com.szmy.pigapp.image.CustomConstants;
import com.szmy.pigapp.myshop.CordovaContext;
import com.szmy.pigapp.myshop.FileUtils;
import com.szmy.pigapp.share.UMShareNewsPopupWindow;
import com.szmy.pigapp.tixian.ActivityWithdrawals;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.FileUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.zxing.activity.CaptureActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaChromeClient;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.PluginEntry;
import org.apache.cordova.Whitelist;

import java.io.File;
import java.util.ArrayList;

public class WebMainFragment extends BaseFragment {
    private View view = null;
    // The webview for our app
    protected CordovaWebView appView;

    // Plugin to call when activity result is received
    protected int activityResultRequestCode;
    protected CordovaPlugin activityResultCallback;

    protected CordovaPreferences prefs = new CordovaPreferences();
    protected Whitelist internalWhitelist = new Whitelist();
    protected Whitelist externalWhitelist = new Whitelist();
    protected ArrayList<PluginEntry> pluginEntries;
    private RelativeLayout ll;
    private RelativeLayout mLlLoadFailure;
    private ImageView iv;
    private boolean mIsErrorPage;
    private JsInterface JSInterface2;
    private CordovaContext cordovaContext;
    private TextView mTvLogin, mTvScan;
    private SharedPreferences usersp;
    private SwipeRefreshLayout swipeLayout;

    ValueCallback<Uri> mUploadMessage;
    public static final int FILECHOOSER_RESULTCODE = 1;
    private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE + 1;
    private static final int REQ_CHOOSE = REQ_CAMERA + 1;
    private String url;
    private String type = "";
    private Dialog dialog;
    private UMShareNewsPopupWindow mUmPopup;
    private EditText et_search;
    private FrameLayout fl_search;
    private ImageButton ib_cancel;
    private LinearLayout mLlMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cordovaContext = new CordovaContext(this.getActivity(),
                activityResultCallback, activityResultRequestCode);

        LayoutInflater localInflater = inflater.cloneInContext(cordovaContext);

        view = localInflater.inflate(R.layout.webview, container, false);
        appView = (CordovaWebView) view.findViewById(R.id.tutorialView);
        Config.init(getActivity());
        mLlMain = (LinearLayout) view.findViewById(R.id.ll_main);
        UpdateUI(mLlMain);
        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }


    private void init() {
        usersp = getActivity().getSharedPreferences(App.USERINFO,
                App.SHAREID);
        if (!TextUtils.isEmpty(usersp.getString("userinfo", ""))) {
            App.mUserInfo = FileUtil.readUser(getActivity());
            App.setDataInfo(App.mUserInfo);
        }
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //重新刷新页面
                appView.loadUrl(appView.getUrl());
            }
        });
        //首次启动刷新页面
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
                if (appView.getUrl()==null || appView.getUrl().equals("")){}else
                appView.loadUrl(appView.getUrl());
            }
        });
        swipeLayout.setColorScheme(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);
        mTvScan = (TextView) view.findViewById(R.id.tv_scan);
        mTvScan.setOnClickListener(mClickListener);
        mTvLogin = (TextView) view.findViewById(R.id.tv_login);
        getTopLoginText(getActivity(), mTvLogin,"myapp");
        mTvLogin.setOnClickListener(mClickListener);
        fl_search = (FrameLayout) view.findViewById(R.id.fl_search);
        et_search = (EditText) view.findViewById(R.id.et_search);
        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean focus) {
                v.dispatchWindowFocusChanged(focus);
                        if(focus){
                            et_search.getHint().toString();
                            et_search.setTag( et_search.getHint().toString());
                            et_search.setHint("");
                        }else{

                            et_search.setHint(et_search.getTag().toString());
                        }
            }
        });
        ib_cancel = (ImageButton) view.findViewById(R.id.ib_cancel);
        ib_cancel.setOnClickListener(mClickListener);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    hideInput(getActivity());
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    getView().requestFocus();//强制获取焦点，不然getActivity().getCurrentFocus().getWindowToken()会报错
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    inputMethodManager.restartInput(et_search);
                    String searchurl = UrlEntry.SEARCH_SZMY_URL + et_search.getText() + "&uuid=" + (TextUtils
                            .isEmpty(getUserUUid(getActivity())) ? "1"
                            : getUserUUid(getActivity())) + "&uuid_szmy=" + (TextUtils
                            .isEmpty(getUserUUid(getActivity())) ? "1"
                            : getUserUUid(getActivity()));
                    if (appView.getUrl().contains("/mCompany/toCompany.html")) {//旗舰店
                        searchurl = UrlEntry.SEARCHQJD_SZMY_URL + et_search.getText() + "&uuid=" + (TextUtils
                                .isEmpty(getUserUUid(getActivity())) ? "1"
                                : getUserUUid(getActivity())) + "&uuid_szmy=" + (TextUtils
                                .isEmpty(getUserUUid(getActivity())) ? "1"
                                : getUserUUid(getActivity()));
                    } else if (type.equals("yx")) {
                        searchurl = UrlEntry.SEARCHYX_SZMY_URL + et_search.getText() + "&uuid=" + (TextUtils
                                .isEmpty(getUserUUid(getActivity())) ? "1"
                                : getUserUUid(getActivity())) + "&uuid_szmy=" + (TextUtils
                                .isEmpty(getUserUUid(getActivity())) ? "1"
                                : getUserUUid(getActivity()));
                    } else if (type.equals("sy")) {
                        searchurl = UrlEntry.SEARCH_SZMY_URL + et_search.getText() + "&uuid=" + (TextUtils
                                .isEmpty(getUserUUid(getActivity())) ? "1"
                                : getUserUUid(getActivity())) + "&uuid_szmy=" + (TextUtils
                                .isEmpty(getUserUUid(getActivity())) ? "1"
                                : getUserUUid(getActivity()));
                    }
                    appView.loadUrl(searchurl);
                    return true;
                }
                return false;
            }
        });
        internalWhitelist.addWhiteListEntry("*", false);
        externalWhitelist.addWhiteListEntry("tel:*", false);
        externalWhitelist.addWhiteListEntry("sms:*", false);
        prefs.set("loglevel", "DEBUG");
        appView.init(cordovaContext, makeWebViewClient(appView),
                makeChromeClient(appView), pluginEntries, internalWhitelist,
                externalWhitelist, prefs);
        appView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        Bundle bundle = getArguments();
         url = bundle.getString("URL");
        type = bundle.getString("type");
        setUrl();
        appView.loadUrlIntoView(url);
        CordovaInterface cordovaInterface = (CordovaInterface) cordovaContext;
        CordovaWebViewClient cordovaWebViewClient = new CordovaWebViewClient(
                cordovaInterface, appView) {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("onPageStarted " + url);
                super.onPageStarted(view, url, favicon);
                if (ll.getVisibility() == View.GONE) {
                    ll.setVisibility(View.VISIBLE);
                }
                if (mLlLoadFailure.getVisibility() == View.VISIBLE)
                    mLlLoadFailure.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("onPageFinished " + url);
                super.onPageFinished(view, url);
                if (appView.getUrl()!=null) {
                    if(appView.getUrl().contains("http://hnyn.365960.cn/")){
                        appView.getSettings().setBuiltInZoomControls(true);//支持缩放
                        appView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放
                        appView.getSettings().setLoadWithOverviewMode(true);
                    }else{
                        appView.getSettings().setBuiltInZoomControls(false);//不支持缩放
                        appView.getSettings().setUseWideViewPort(false);//设置此属性，不可任意比例缩放
                        appView.getSettings().setLoadWithOverviewMode(false);
                    }
                    if (appView.getUrl().contains("/mCompany/toCompany.html")) {
                        et_search.setHint("搜索旗舰店");
                        setSearchEnable(true);
                    } else if (type.equals("sy")) {
                        et_search.setHint("搜索商品");
                        setSearchEnable(true);
                    }

                    if (!appView.getSettings().getLoadsImagesAutomatically()) {
                        appView.getSettings().setLoadsImagesAutomatically(true);
                    }
                    if (ll.getVisibility() == view.VISIBLE) {
                        ll.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                System.out.println("onReceivedError " + failingUrl);
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                if (mLlLoadFailure.getVisibility() == View.GONE)
                    mLlLoadFailure.setVisibility(View.VISIBLE);
                if (ll.getVisibility() == view.VISIBLE)
                    ll.setVisibility(View.GONE);
            }
        };
        JSInterface2 = new JsInterface(appView);
        appView.addJavascriptInterface(JSInterface2, "JSInterface");
        appView.setWebChromeClient(new CordovaChromeClient(this.cordovaContext) {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //隐藏进度条
                    swipeLayout.setRefreshing(false);
                    view.setVisibility(View.VISIBLE);
                } else {
                    if (!swipeLayout.isRefreshing())
                        swipeLayout.setRefreshing(true);
                }

                super.onProgressChanged(view, newProgress);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                if (mUploadMessage != null) return;
                mUploadMessage = uploadMsg;
                selectImage();
//               Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//               i.addCategory(Intent.CATEGORY_OPENABLE);
//               i.setType("*/*");
//                   startActivityForResult( Intent.createChooser( i, "File Chooser" ), FILECHOOSER_RESULTCODE );
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }

            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg, acceptType);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
//                showDialog1(getActivity(), message);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                result.cancel();
                return true;

            }
        });
        appView.setWebViewClient(cordovaWebViewClient);
        ll = (RelativeLayout) view.findViewById(R.id.ll_loading);
        iv = (ImageView) view.findViewById(R.id.iv_load_failure);
        mLlLoadFailure = (RelativeLayout) view
                .findViewById(R.id.ll_load_failure);
        mLlLoadFailure.setOnClickListener(mClickListener);
        iv.setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_load_failure:
                    mLlLoadFailure.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);
                    appView.reload();
                    break;
                case R.id.tv_login:
                    if (isLoginSSzmy(getActivity())) {
                    } else {
                        if (mTvLogin.getText().toString().equals("猪贸通")){
                            Intent intent =  new Intent(getActivity(),ActivityMain.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else
                        AppStaticUtil.toastWelcome(getActivity(),
                                getUsername(getActivity()));
                    }
                    break;
                case R.id.tv_scan:
                    if (isLoginSSzmy(getActivity())) {
                        showToast("请先登录！");
                    } else {
                        // 打开扫描界面扫描条形码或二维码
                        Intent openCameraIntent = new Intent(getActivity(),
                                CaptureActivity.class);
                        startActivityForResult(openCameraIntent, 0);

                    }
                    break;
                case R.id.ib_cancel:
                    et_search.setText("");
                    break;
            }
        }
    };

    /**
     * 检查SD卡是否存在
     *
     * @return
     */
    public final boolean checkSDcard() {
        boolean flag = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (!flag) {
            Toast.makeText(getActivity(), "请插入手机存储卡再使用本功能", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    String compressPath = "";

    protected final void selectImage() {
        if (!checkSDcard())
            return;
        String[] selectPicTypeStr = {"相机", "相册"};
        new AlertDialog.Builder(getActivity())
                .setItems(selectPicTypeStr,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    // 相机拍摄
                                    case 0:
                                        openCarcme();
                                        break;
                                    // 手机相册
                                    case 1:
                                        chosePic();
                                        break;
                                    default:
                                        break;
                                }
                                compressPath = Environment
                                        .getExternalStorageDirectory()
                                        .getPath()
                                        + "/szmy_pic/temp";
                                new File(compressPath).mkdirs();
                                compressPath = compressPath + File.separator
                                        + "compress.jpg";
                            }
                        }).show();
    }

    String imagePaths;
    Uri cameraUri;

    /**
     * 打开照相机
     */
    private void openCarcme() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        imagePaths = Environment.getExternalStorageDirectory().getPath()
                + "/szmy_pic/temp/"
                + (System.currentTimeMillis() + ".jpg");
        // 必须确保文件夹路径存在，否则拍照后无法完成回调
        File vFile = new File(imagePaths);
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        cameraUri = Uri.fromFile(vFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(intent, REQ_CAMERA);
    }

    /**
     * 拍照结束后
     */
    private void afterOpenCamera() {
        File f = new File(imagePaths);
        addImageGallery(f);
        File newFile = FileUtils.compressFile(f.getPath(), compressPath);
    }

    /**
     * 解决拍照后在相册中找不到的问题
     */
    private void addImageGallery(File file) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    /**
     * 本地相册选择图片
     */
    private void chosePic() {
        FileUtils.delFile(compressPath);
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
        String IMAGE_UNSPECIFIED = "image/*";
        innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        startActivityForResult(wrapperIntent, REQ_CHOOSE);
    }

    /**
     * 选择照片后结束
     *
     * @param data
     */
    private Uri afterChosePic(Intent data) {

        // 获取图片的路径：
        String[] proj = {MediaStore.Images.Media.DATA};
        // 好像是android多媒体数据库的封装接口，具体的看Android文档
        Cursor cursor = getActivity().managedQuery(data.getData(), proj, null, null, null);
        if (cursor == null) {
            Toast.makeText(getActivity(), "上传的图片仅支持png或jpg格式", Toast.LENGTH_SHORT).show();
            return null;
        }
        // 按我个人理解 这个是获得用户选择的图片的索引值
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        // 将光标移至开头 ，这个很重要，不小心很容易引起越界
        cursor.moveToFirst();
        // 最后根据索引值获取图片路径
        String path = cursor.getString(column_index);
        if (path != null && (path.endsWith(".png") || path.endsWith(".PNG") || path.endsWith(".jpg") || path.endsWith(".JPG"))) {
            File newFile = FileUtils.compressFile(path, compressPath);
            return Uri.fromFile(newFile);
        } else {
            Toast.makeText(getActivity(), "上传的图片仅支持png或jpg格式", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case 0:
                    // 处理扫描结果（在界面上显示）
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
//							showToast("--------result" + scanResult);
                    if (scanResult.startsWith("loginCode")) {
                        String newuuid = scanResult.split("-")[1];
                        sendNewUUid(newuuid);
                    } else {
                        if (scanResult.startsWith("http")) {
                            Uri uri = Uri.parse(scanResult);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.addCategory("android.intent.category.BROWSABLE");
                            startActivity(intent);
                        } else {
                            showToast("暂时无法识别该二维码");
                        }
                    }
                    break;
            }
            return;
        }
        switch (resultCode) {
            case 22:
                getTopLoginText(getActivity(), mTvLogin,"myapp");
//                appView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//                appView.loadUrl(url);
                System.out.print("urlurl" + appView.getUrl());
                setUrl();
                appView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                appView.loadUrlIntoView(url);
                appView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        appView.clearHistory();
                    }
                }, 1000);
                break;
            default:
                if (null == mUploadMessage)
                    return;
                Uri uri = null;
                if (requestCode == REQ_CAMERA) {
                    afterOpenCamera();
                    uri = cameraUri;
                } else if (requestCode == REQ_CHOOSE) {
                    uri = afterChosePic(data);
                }
                mUploadMessage.onReceiveValue(uri);
                mUploadMessage = null;
                break;
        }
    }

    protected CordovaWebViewClient makeWebViewClient(CordovaWebView webView) {
        return webView.makeWebViewClient(cordovaContext);
    }

    protected CordovaChromeClient makeChromeClient(CordovaWebView webView) {
        return webView.makeWebChromeClient(cordovaContext);
    }

    private Handler mHandler = new Handler();

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getTopLoginText(getActivity(), mTvLogin,"myapp");
        setUrl();
        appView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        appView.loadUrlIntoView(url);
        //清楚历史记录
        appView.postDelayed(new Runnable() {
            @Override
            public void run() {
                appView.clearHistory();
            }
        }, 1000);
    }

    public class JsInterface {
        private WebView mWebView;

        public JsInterface(WebView webView) {
            this.mWebView = webView;
        }

        @JavascriptInterface
        public void javaFunction() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //update UI in main looper, or it will crash
                    Toast.makeText(mWebView.getContext(), "正在跳转猪贸通", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), ActivityMain.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }

        @JavascriptInterface
        public void toLogin() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //update UI in main looper, or it will crash
                    Toast.makeText(mWebView.getContext(), "正在跳转登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("zhuce", "szmyzc");
                    startActivityForResult(intent, 22);
                }
            });
        }

        @JavascriptInterface
        public void toAddBank() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //update UI in main looper, or it will crash
                    Toast.makeText(mWebView.getContext(), "正在跳转银行卡", Toast.LENGTH_SHORT).show();
                    clearTempFromPref();
                    Intent locintent = new Intent(getActivity(), ActivityWithdrawals.class);
                    locintent.putExtra("type", "add");
                    startActivityForResult(locintent, 22);
                }
            });
        }

        //入驻
        @JavascriptInterface
        public void regCompany(final String type) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    clearMapTempFronPref(getActivity());
                    Toast.makeText(mWebView.getContext(), "正在跳转企业入驻", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), JoinEnterpriseActivity.class);
                    intent.putExtra("type", type);
                    startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void toFxs() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mWebView.getContext(), "正在跳转分销商", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), JoinDistributorActivity.class);
                    startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void toShare(final String url, final String content) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mUmPopup = new UMShareNewsPopupWindow(getActivity(), UrlEntry.ip + url, content);
                    mUmPopup.mController.getConfig().removePlatform(
                            SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,
                            SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL, SHARE_MEDIA.SINA,
                            SHARE_MEDIA.TENCENT);
                    // //默认分享方式
                    mUmPopup.mController.openShare(getActivity(), false);

                }
            });

        }
        //2017-1-4 跳转服务中心
        @JavascriptInterface
        public void toFwzx() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //update UI in main looper, or it will crash
//                    Toast.makeText(mWebView.getContext(), "正在跳转登录", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(),
                            ServiceCenterActivity.class));
                }
            });
        }
        //        @JavascriptInterface
//        public void showMessage(String message) {
//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
//                    //弹出消息提示
//
//
//                }
//            });
//        }
        public void javaCallJsFunction(int code) {
            mWebView.loadUrl(String.format("javascript:javaCallJsFunction(" + code + ")"));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (appView.canGoBack()) {
            appView.goBack();
            return true;
        }
        return false;
    }

    public void setUrl() {
        if (type.equals("sy")) {
            url = UrlEntry.MOBILE_PHONE_URL + (TextUtils
                    .isEmpty(getUserUUid(getActivity())) ? "1"
                    : getUserUUid(getActivity())) + "&uuid_szmy=" + (TextUtils
                    .isEmpty(getUserUUid(getActivity())) ? "1"
                    : getUserUUid(getActivity()));
            et_search.setHint("搜索商品");
            setSearchEnable(true);
        } else if (type.equals("yx")) {
            url = UrlEntry.YOUXUAN_SZMY_URL + (TextUtils
                    .isEmpty(getUserUUid(getActivity())) ? "1"
                    : getUserUUid(getActivity())) + "&uuid_szmy=" + (TextUtils
                    .isEmpty(getUserUUid(getActivity())) ? "1"
                    : getUserUUid(getActivity()));
            ;
            et_search.setHint("搜索优选");
            setSearchEnable(true);
        } else if (type.equals("gwc")) {
            url = UrlEntry.GOUWUCHE_SZMY_URL + (TextUtils
                    .isEmpty(getUserUUid(getActivity())) ? "1"
                    : getUserUUid(getActivity())) + "&uuid_szmy=" + (TextUtils
                    .isEmpty(getUserUUid(getActivity())) ? "1"
                    : getUserUUid(getActivity()));
            ;
            et_search.setHint("搜索");
            setSearchEnable(false);

        }
    }

    public void clearMapTempFronPref(Context cox) {
        SharedPreferences preferences = cox.getSharedPreferences(CustomConstants.APPPicLICATION_NAME,
                0);
        SharedPreferences.Editor editor1 = preferences.edit();
        FileUtil.deleteDirfile(FileUtil.SDPATH);
        editor1.clear();
        editor1.putString(CustomConstants.APPPicLICATION_NAME, "");
        editor1.commit();
    }

    private void setSearchEnable(boolean flag) {
        et_search.setEnabled(flag);
        et_search.setFocusable(flag);
        et_search.setFocusableInTouchMode(flag);
    }

    public CordovaWebView getWebView() {
        return appView;
    }

    public void UpdateUI(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    // TODO Auto-generated method stub
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    getView().requestFocus();//强制获取焦点，不然getActivity().getCurrentFocus().getWindowToken()会报错
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    inputMethodManager.restartInput(et_search);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                UpdateUI(innerView);
            }
        }
    }
}
