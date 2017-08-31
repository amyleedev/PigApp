package com.szmy.pigapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.gc.materialdesign.widgets.Dialog;
import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.LoginActivity;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;

/**
 * @version 1.0
 */
public class ShopFragment extends BaseFragment implements OnClickListener {
	public static WebView wv;
	private ProgressBar pb;
	private View mErrorView;
	private ImageView iv;
	private RelativeLayout ll;
	private RelativeLayout mLlLoadFailure;
	private boolean mIsErrorPage;
	private WebSettings webSettings;
	private String url;
	private LinearLayout mll_back;
	private String type = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.activity_scan_result, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView(getView());
	}

	private void initView(View view) {
		url = UrlEntry.MOBILE_PHONE_URL
				+ (TextUtils.isEmpty(getUserUUid(getActivity())) ? "1"
						: getUserUUid(getActivity()));
		wv = (WebView) view.findViewById(R.id.wv);
		pb = (ProgressBar) view.findViewById(R.id.pb);
		ll = (RelativeLayout) view.findViewById(R.id.ll_loading);
		iv = (ImageView) view.findViewById(R.id.iv_load_failure);
		mLlLoadFailure = (RelativeLayout) view
				.findViewById(R.id.ll_load_failure);
		mll_back = (LinearLayout) view.findViewById(R.id.def_head_back);
		mll_back.setVisibility(View.GONE);
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
			}
		});
		wv.loadUrl(url);
		wv.requestFocusFromTouch();
		webSettings = wv.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		wv.setWebChromeClient(new MyWebChromeClient(getActivity()));
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_load_failure:
			wv.reload();
			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		wv.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
		if (type.equals("index")) {
			url = UrlEntry.MOBILE_PHONE_URL
					+ (TextUtils.isEmpty(App.uuid) ? "1" : App.uuid);
		} else if (type.equals("integral")) {
			url = UrlEntry.INTEGRAL_SHOP_URL
					+ (TextUtils.isEmpty(App.uuid) ? "1" : App.uuid);
		} else {
			url = UrlEntry.GROUP_BUYING_URL
					+ (TextUtils.isEmpty(App.uuid) ? "1" : App.uuid);
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
		public boolean onJsAlert(WebView view, String url,
				final String message, JsResult result) {
			final Dialog dialog = new Dialog(getActivity(), "提示", message);
			dialog.addCancelButton("取消");
			dialog.setCancelable(false);
			dialog.setOnAcceptButtonClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
					if (message.contains("请先登录")) {
										Intent intent = new Intent(
												getActivity(),
												LoginActivity.class);
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
			dialog.show();
//			final AlertDialog.Builder builder = new AlertDialog.Builder(
//					view.getContext());
//
//			builder.setTitle("提示")
//					.setMessage(message)
//					.setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									if (message.contains("请先登录")) {
//										Intent intent = new Intent(
//												getActivity(),
//												LoginActivity.class);
//										startActivityForResult(intent, 22);
//									}
//
//								}
//							});
//
//			// 不需要绑定按键事件
//			// 屏蔽keycode等于84之类的按键
//			builder.setOnKeyListener(new OnKeyListener() {
//				public boolean onKey(DialogInterface dialog, int keyCode,
//						KeyEvent event) {
//					Log.v("onJsAlert", "keyCode==" + keyCode + "event=" + event);
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
			final Dialog dialog = new Dialog(getActivity(), "提示", message);
			dialog.addCancelButton("取消");
			dialog.setCancelable(false);
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
			dialog.show();
//			final AlertDialog.Builder builder = new AlertDialog.Builder(
//					view.getContext());
//			builder.setTitle("对话框")
//					.setMessage(message)
//					.setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int which) {
//									result.confirm();
//								}
//							})
//					.setNeutralButton("取消",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int which) {
//									result.cancel();
//								}
//							});
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
//				public boolean onKey(DialogInterface dialog, int keyCode,
//						KeyEvent event) {
//					Log.v("onJsConfirm", "keyCode==" + keyCode + "event="
//							+ event);
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
			final AlertDialog.Builder builder = new AlertDialog.Builder(
					view.getContext());

			builder.setTitle("对话框").setMessage(message);

			final EditText et = new EditText(view.getContext());
			et.setSingleLine();
			et.setText(defaultValue);
			builder.setView(et)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									result.confirm(et.getText().toString());
								}

							})
					.setNeutralButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									result.cancel();
								}
							});

			// 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
			builder.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					Log.v("onJsPrompt", "keyCode==" + keyCode + "event="
							+ event);
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
}
