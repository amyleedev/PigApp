package com.szmy.pigapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonIcon;
import com.gc.materialdesign.widgets.Dialog;
import com.szmy.pigapp.R;
import com.szmy.pigapp.distributor.JoinDistributorActivity;
import com.szmy.pigapp.distributor.JoinEnterpriseActivity;
import com.szmy.pigapp.share.UMShareNewsPopupWindow;
import com.szmy.pigapp.tixian.ActivityWithdrawals;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.AppManager;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.UrlEntry;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class ActivityScanResult extends BaseActivity {
    private WebView wv;
    private ProgressBar pb;
    private View mErrorView;
    private ImageView iv;
    private RelativeLayout ll;
    private RelativeLayout mLlLoadFailure;
    private boolean mIsErrorPage;
    private WebSettings webSettings;
    // private AnimationDrawable anim;
    private String url;
    private ButtonIcon mll_back;
    private LinearLayout mll_exit;
    private String type = "", mComId = "";
    private LinearLayout mLlFxs;
    private CheckBox mCheckBtn;
    private Button mBtnFxs;
    private TextView mTvXieyi;
    private UMShareNewsPopupWindow mUmPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        url = getIntent().getExtras().getString("url");
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");

            if (getIntent().hasExtra("mComId")) {
                mComId = getIntent().getStringExtra("mComId");
            }
        }
        mLlFxs = (LinearLayout) findViewById(R.id.ll_fxs);
        mCheckBtn = (CheckBox) findViewById(R.id.check_fxs);
        mBtnFxs = (Button) findViewById(R.id.fxs_btn);
        mBtnFxs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCheckBtn.isChecked()) {
                    Intent intent = new Intent(ActivityScanResult.this, JoinDistributorActivity.class);
                    startActivity(intent);
                } else {
                    showDialog1(ActivityScanResult.this, "请先同意分销商入住协议");
                }
            }
        });
        mTvXieyi = (TextView) findViewById(R.id.xieyi_txt);
        mTvXieyi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityScanResult.this, ActivityScanResultXieyi.class);
                intent.putExtra("url", UrlEntry.XIEYI_DISTRIBUTOR_URL);
                startActivity(intent);
            }
        });
        if (getIntent().hasExtra("fxs")) {
            mLlFxs.setVisibility(View.VISIBLE);
        }
        wv = (WebView) findViewById(R.id.wv);
        if (TextUtils.isEmpty(url)) {
            showToast("扫描结果为空");
            return;
        }
        wv.loadUrl(url);
        wv.requestFocusFromTouch();
        webSettings = wv.getSettings();
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        JsInterface  JSInterface2 = new JsInterface(wv);
        wv.addJavascriptInterface(JSInterface2, "JSInterface");
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //LOAD_CACHE_ELSE_NETWORK
        wv.setWebChromeClient(new MyWebChromeClient(ActivityScanResult.this));
        if (AppStaticUtil.isNetwork(getApplicationContext())) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 根据cache-control决定是否从网络上取数据
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 先查找缓存，没有的情况下从网络获取。
        }
        pb = (ProgressBar) findViewById(R.id.pb);
        // anim = (AnimationDrawable) pb.getBackground();

        ll = (RelativeLayout) findViewById(R.id.ll_loading);
        iv = (ImageView) findViewById(R.id.iv_load_failure);
        mll_back = (ButtonIcon) findViewById(R.id.def_head_back);
        mll_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(type.equals("zhenduan")){
                    finish();
                    return;
                }
                if (wv.canGoBack()) {
                    wv.goBack();
                } else
                if (AppManager.getAppManager().getActSize() > 1) {
                    finish();
                } else {
                    startActivity(new Intent(ActivityScanResult.this,
                            ActivityMain.class));
                    finish();
                }
            }
        });
        mll_exit = (LinearLayout) findViewById(R.id.ll_exit);
        mll_exit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AppManager.getAppManager().getActSize() > 1) {
                    finish();
                } else {
                    startActivity(new Intent(ActivityScanResult.this,
                            ActivityMain.class));
                    finish();
                }
            }
        });
        mLlLoadFailure = (RelativeLayout) findViewById(R.id.ll_load_failure);
        iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                wv.reload();
                // if (wv.canGoBack())
                // wv.goBack();
            }
        });
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (ll.getVisibility() == View.GONE) {
                    ll.setVisibility(View.VISIBLE);
                    // anim.start();
                }
                if (mLlLoadFailure.getVisibility() == View.VISIBLE)
                    mLlLoadFailure.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webSettings.setBlockNetworkImage(false);
                if (!wv.getSettings().getLoadsImagesAutomatically()) {
                    wv.getSettings().setLoadsImagesAutomatically(true);
                }
                if (ll.getVisibility() == view.VISIBLE) {
                    ll.setVisibility(View.GONE);
                    // anim.stop();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                showToast("加载失败");
                if (mLlLoadFailure.getVisibility() == View.GONE)
                    mLlLoadFailure.setVisibility(View.VISIBLE);
                if (ll.getVisibility() == view.VISIBLE)
                    ll.setVisibility(View.GONE);
                // wv.loadDataWithBaseURL(null, " ", "text/html", "utf-8",
                // null);
            }
        });

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

    /**
     * 闁告劕绉电�婊勭▔閿熺瓔鍋ч梺顐嫹閸ゎ厾鍖栭懡銈呯秼
     */
    private long exitTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (type.equals("zhenduan")){
                finish();
            }else if (wv.canGoBack()) {
                wv.goBack();
                return true;
            } else if (AppManager.getAppManager().getActSize() > 1) {
                finish();
            } else {
                startActivity(new Intent(ActivityScanResult.this,
                        ActivityMain.class));
                finish();
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, data);
        wv.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        if (type.equals("index")) {
            url = UrlEntry.MOBILE_PHONE_URL + (TextUtils.isEmpty(App.uuid) ? "1" : App.uuid);
        } else if (type.equals("integral")) {
            url = UrlEntry.INTEGRAL_SHOP_URL + (TextUtils.isEmpty(App.uuid) ? "1" : App.uuid);
        } else if (type.equals("near")) {
            url = UrlEntry.NEAR_SHOP_URL + mComId + "&uuid=" + (TextUtils.isEmpty(App.uuid) ? "1" : App.uuid);
        } else {
            url = UrlEntry.GROUP_BUYING_URL + (TextUtils.isEmpty(App.uuid) ? "1" : App.uuid);
        }
        wv.loadUrl(url);
    }

    public class MyWebChromeClient extends WebChromeClient {
        private Context context;

        public MyWebChromeClient(Context context) {
            this.context = context;
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog,
                                      boolean userGesture, Message resultMsg) {
            return super.onCreateWindow(view, dialog, userGesture, resultMsg);
        }

        /**
         * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
         */
        public boolean onJsAlert(WebView view, String url, final String message,
                                 JsResult result) {
            if (message.contains("分享")) {
                String content = message.split("##")[1];
                String url1 = message.split("##")[0];
                System.out.print(content + content.split(":")[1] +url1+url.split(":")[1]);
                mUmPopup = new UMShareNewsPopupWindow(ActivityScanResult.this,UrlEntry.ip+ message.split("##")[0].split(":")[1], message.split("##")[1].split(":")[1]);
                if (!message.split("##")[0].split(":")[1].equals("")) {
                    mUmPopup.mController.getConfig().removePlatform(
                            SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,
                            SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL, SHARE_MEDIA.SINA,
                            SHARE_MEDIA.TENCENT);
                    // //默认分享方式
                    mUmPopup.mController.openShare(ActivityScanResult.this, false);
                }else{
                    showDialog1(ActivityScanResult.this,"分享链接有误！");
                }

        }

        else

        {

            final Dialog dialog = new Dialog(ActivityScanResult.this, "提示", message);
            dialog.addCancelButton("取消");
            dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    if (message.contains("请先登录")) {
                        Intent intent = new Intent(ActivityScanResult.this, LoginActivity.class);
                        startActivityForResult(intent, 22);
                    }
                }
            });
            dialog.setOnCancelButtonClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();

                }
            });
            dialog.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    return true;
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        }
//			final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//
//			builder.setTitle("提示")
//					.setMessage(message)
//					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							if(message.contains("请先登录")){
//								Intent intent = new Intent(ActivityScanResult.this,LoginActivity.class);
//								startActivityForResult(intent, 22);
//							}
//
//
//						}
//					});

        // 不需要绑定按键事件
        // 屏蔽keycode等于84之类的按键
//			builder.setOnKeyListener(new OnKeyListener() {
//				public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
//					Log.v("onJsAlert", "keyCode==" + keyCode + "event="+ event);
//					return true;
//				}
//			});
//			// 禁止响应按back键的事件
//			builder.setCancelable(false);
//			AlertDialog dialog = builder.create();
//			dialog.show();
        result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
        return true;
        // return super.onJsAlert(view, url, message, result);
    }

    public boolean onJsBeforeUnload(WebView view, String url,
                                    String message, JsResult result) {
        return super.onJsBeforeUnload(view, url, message, result);
    }

    /**
     * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
     */
    public boolean onJsConfirm(WebView view, String url, String message,
                               final JsResult result) {
        final Dialog dialog = new Dialog(ActivityScanResult.this, "提示", message);
        dialog.addCancelButton("取消");
        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                result.confirm();
            }
        });
        dialog.setOnCancelButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                result.cancel();

            }
        });
        dialog.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        dialog.setCancelable(false);
        dialog.show();
//			final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//			builder.setTitle("对话框")
//					.setMessage(message)
//					.setPositiveButton("确定",new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,int which) {
//									result.confirm();
//								}
//							})
//					.setNeutralButton("取消", new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							result.cancel();
//						}
//					});
//			builder.setOnCancelListener(new OnCancelListener() {
//				@Override
//				public void onCancel(DialogInterface dialog) {
//					result.cancel();
//				}
//			});
//
//			// 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
//			builder.setOnKeyListener(new OnKeyListener() {
//				@Override
//				public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
//					Log.v("onJsConfirm", "keyCode==" + keyCode + "event="+ event);
//					return true;
//				}
//			});
//			// 禁止响应按back键的事件
//			// builder.setCancelable(false);
//			AlertDialog dialog = builder.create();
//			dialog.show();
        return true;
        // return super.onJsConfirm(view, url, message, result);
    }

    /**
     * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
     * window.prompt('请输入您的域名地址', '618119.com');
     */
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, final JsPromptResult result) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("对话框").setMessage(message);

        final EditText et = new EditText(view.getContext());
        et.setSingleLine();
        et.setText(defaultValue);
        builder.setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                result.confirm(et.getText().toString());
            }

        })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });

        // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
        builder.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                Log.v("onJsPrompt", "keyCode==" + keyCode + "event=" + event);
                return true;
            }
        });

        // 禁止响应按back键的事件
        // builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
        // return super.onJsPrompt(view, url, message, defaultValue,
        // result);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
    }

    @Override
    public void onRequestFocus(WebView view) {
        super.onRequestFocus(view);
    }
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
                    Intent intent = new Intent(ActivityScanResult.this, ActivityMain.class);
                    startActivity(intent);
                    finish();
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
                    Intent intent = new Intent(ActivityScanResult.this, LoginActivity.class);
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
                    Intent locintent = new Intent(ActivityScanResult.this, ActivityWithdrawals.class);
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
//                    clearMapTempFronPref(this);
                    Toast.makeText(mWebView.getContext(), "正在跳转企业入驻", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityScanResult.this, JoinEnterpriseActivity.class);
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
                    Intent intent = new Intent(ActivityScanResult.this, JoinDistributorActivity.class);
                    startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void toShare(final String url, final String content) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mUmPopup = new UMShareNewsPopupWindow(ActivityScanResult.this, UrlEntry.ip + url, content);
                    mUmPopup.mController.getConfig().removePlatform(
                            SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,
                            SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL, SHARE_MEDIA.SINA,
                            SHARE_MEDIA.TENCENT);
                    // //默认分享方式
                    mUmPopup.mController.openShare(ActivityScanResult.this, false);

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
                    startActivity(new Intent(ActivityScanResult.this,
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
}
