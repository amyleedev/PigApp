package com.szmy.pigapp.share;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.App;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.zxing.encoding.EncodingHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.Map;

public class UMSharePopupWindow {
	private FragmentActivity context;
	public static UMSocialService mController;
	private String text = "来自猪贸通的分享：";
	private String imageurl = UrlEntry.ip + "/appDownload/icon.png";
	private String title = "神州牧易";
	private String url = UrlEntry.ip + "/appDownload/download.jsp?u=";
	private String content = "我正在使用猪贸通,可以随时随地查看生猪收购出售信息..";
	private PopupWindow mSharePopup;
	private ShareHolder mHolder;

	public UMSharePopupWindow(FragmentActivity context) {
		this.context = context;
		initShare();
	}

	private void initShare() {
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		addQQQZonePlatform();
		addWXPlatform();
		// 支持微信朋友圈
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent(content);
		// 设置title
		weixinContent.setTitle(text);
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl(url + App.userID);
		// 设置分享图片
		weixinContent.setShareImage(new UMImage(context, imageurl));
		mController.setShareMedia(weixinContent);
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(content);
		// 设置朋友圈title
		circleMedia.setTitle(text+content);
		circleMedia.setShareImage(new UMImage(context, imageurl));
		circleMedia.setTargetUrl(url + App.userID);
		mController.setShareMedia(circleMedia);
		QQShareContent qqShareContent = new QQShareContent();
		// 设置分享文字
		qqShareContent.setShareContent(content);
		qqShareContent.setTargetUrl(url + App.userID);
		// 设置分享title
		qqShareContent.setTitle(text);
		// 设置分享图片
		qqShareContent.setShareImage(new UMImage(context, imageurl));
		// 设置点击分享内容的跳转链接
		qqShareContent.setTargetUrl(url + App.userID);
		mController.setShareMedia(qqShareContent);
		QZoneShareContent qzone = new QZoneShareContent();
		// 设置分享文字
		qzone.setShareContent(content);
		// 设置点击消息的跳转URL
		qzone.setTargetUrl(url + App.userID);
		// 设置分享内容的标题
		qzone.setTitle(text);
		// 设置分享图片
		qzone.setShareImage(new UMImage(context, R.drawable.ic_launcher));
		mController.setShareMedia(qzone);
	}

	private void addQQQZonePlatform() {
		String appId = "1104820529";
		String appKey = "mcTrF7WSv6kubutJ";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context, appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
		qqSsoHandler.addToSocialSDK();
		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context, appId,
				appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wxd9f6583aa131cdea";
		String appSecret = "4507fbf971ff3fc356d74b9dafb3a50f";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	public OnDismissListener getShareDismissListener() {
		return mDismissListener;
	}

	private OnDismissListener mDismissListener = new OnDismissListener() {

		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}
	};

	public void popupPublishMenu() {
		if (mSharePopup != null && mSharePopup.isShowing()) {
			mSharePopup.dismiss();
		} else if (mSharePopup == null) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.share_app, null);
			mHolder = new ShareHolder(view, getShareClickListener());
			// holder.
			mSharePopup = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			mSharePopup.setBackgroundDrawable(new BitmapDrawable());
			// 设置点击窗口外边窗口消失
			mSharePopup.setOutsideTouchable(true);
			// 设置此参数获得焦点，否则无法点击
			mSharePopup.setFocusable(true);
			backgroundAlpha(0.8f);
			getImg(context.getResources(), url + App.userID);
			mHolder.mTvCopy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
			mSharePopup.setOnDismissListener(getShareDismissListener());
			mSharePopup.showAtLocation(context.findViewById(R.id.main),
					Gravity.CENTER, 0, 0);
		} else {
			backgroundAlpha(0.8f);
			getImg(context.getResources(), url + App.userID);
			mHolder.mTvCopy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
			mSharePopup.showAtLocation(context.findViewById(R.id.main),
					Gravity.CENTER, 0, 0);
		}
	}

	private void getImg(Resources r, String contentString) {
		try {
			if (!contentString.equals("")) {
				// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小
				Bitmap qrCodeBitmap = EncodingHandler.createQRCode(
						contentString, 150);
				// ------------------添加logo部分------------------//
				Bitmap logoBmp = BitmapFactory.decodeResource(r,
						R.drawable.icon);
				// 二维码和logo合并
				Bitmap bitmap = Bitmap.createBitmap(qrCodeBitmap.getWidth(),
						qrCodeBitmap.getHeight(), qrCodeBitmap.getConfig());
				Canvas canvas = new Canvas(bitmap);
				// 二维码
				canvas.drawBitmap(qrCodeBitmap, 0, 0, null);
				// // logo绘制在二维码中央
				// canvas.drawBitmap(logoBmp, qrCodeBitmap.getWidth() / 2
				// - logoBmp.getWidth() / 2, qrCodeBitmap.getHeight() / 2
				// - logoBmp.getHeight() / 2, null);
				// ------------------添加logo部分------------------//
				mHolder.mIvCode.setImageBitmap(bitmap);
			}
		} catch (WriterException e) {
		}
	}

	/**
	 * 设置添加屏幕的背景透明度
	 *
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		context.getWindow().setAttributes(lp);
	}

	public OnClickListener getShareClickListener() {
		return mClickListener;
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ll_wechat:
					performShare(SHARE_MEDIA.WEIXIN);
					break;
				case R.id.ll_wechatmoments:
					performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
					break;
				case R.id.ll_qq:
					performShare(SHARE_MEDIA.QQ);
					break;
				case R.id.ll_qzone:
					performShare(SHARE_MEDIA.QZONE);
					break;
				case R.id.copy:
					copy(url + App.userID, context);
					break;
			}
		}
	};

	/**
	 * 实现文本复制功能
	 *
	 * @param content
	 */
	public static void copy(String content, Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
		Toast.makeText(context, "复制成功！", Toast.LENGTH_SHORT).show();
		paste(context);
		Toast.makeText(context, "已复制到剪贴板！", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 实现粘贴功能
	 *
	 * @param context
	 * @return
	 */
	public static String paste(Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		return cmb.getText().toString().trim();
	}

	private void performShare(SHARE_MEDIA platform) {
		mController.postShare(context, platform, new SnsPostListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
								   SocializeEntity entity) {
				String showText = platform.toString();
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					showText += "平台分享成功";
				} else {
					showText += "平台分享失败";
				}
				Toast.makeText(context, showText, Toast.LENGTH_SHORT).show();
				if (mSharePopup.isShowing())
					mSharePopup.dismiss();
			}
		});
	}
	/**
	 * 授权。如果授权成功，则获取用户信息
	 *
	 * @param platform
	 */
	public static void sflogin(final Context mCon,final SHARE_MEDIA platform) {
		mController.doOauthVerify(mCon, platform,
				new SocializeListeners.UMAuthListener() {

					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(mCon, "授权开始...",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onError(SocializeException e,
										SHARE_MEDIA platform) {
						Toast.makeText(mCon, "授权失败...",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						// 获取uid
						String uid = value.getString("uid");
						if (!TextUtils.isEmpty(uid)) {
							// uid不为空，获取用户信息
							getUserInfo(mCon,platform);
						} else {
							Toast.makeText(mCon, "授权失败...",
									Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(mCon, "授权取消",
								Toast.LENGTH_SHORT).show();
					}
				});
	}
	/**
	 * 获取用户信息
	 *
	 * @param platform
	 */
	private static void getUserInfo(final Context mCon, SHARE_MEDIA platform) {
		mController.getPlatformInfo(mCon, platform,
				new SocializeListeners.UMDataListener() {

					@Override
					public void onStart() {

					}

					@Override
					public void onComplete(int status, Map<String, Object> info) {
						// String showText = "";
						// if (status == StatusCode.ST_CODE_SUCCESSED) {
						// showText = "用户名：" +
						// info.get("screen_name").toString();
						// Log.d("#########", "##########" + info.toString());
						// } else {
						// showText = "获取用户信息失败";
						// }

						if (info != null) {
							Toast.makeText(mCon, info.toString(),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

//	/**
//	 * 注销本次登陆
//	 * @param platform
//	 */
//	private void logout(final SHARE_MEDIA platform) {
//		mController.deleteOauth(LoginActivity.this, platform, new SocializeClientListener() {
//
//			@Override
//			public void onStart() {
//
//			}
//
//			@Override
//			public void onComplete(int status, SocializeEntity entity) {
//				String showText = "解除" + platform.toString() + "平台授权成功";
//				if (status != StatusCode.ST_CODE_SUCCESSED) {
//					showText = "解除" + platform.toString() + "平台授权失败[" + status + "]";
//				}
//				Toast.makeText(LoginActivity.this, showText, Toast.LENGTH_SHORT).show();
//			}
//		});
//	}

}
