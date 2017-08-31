package com.szmy.pigapp.sign;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.szmy.pigapp.R;

public class PopupSign extends PopupWindow {
	private OnClickListener mClickListener;
	private Context context;
	private View mView;
	private Holder mHolder;

	public PopupSign(Context context, OnClickListener mClickListener) {
		super();
		this.mClickListener = mClickListener;
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.popup_sign, null);
		mHolder = new Holder();
		this.setContentView(mView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		this.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow的View
		this.setOnDismissListener(new poponDismissListener());
	}

	public Holder getmHolder() {
		return mHolder;
	}

	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			backgroundAlpha(0.8f);
			this.showAtLocation(parent, Gravity.CENTER, 0, 0);
		} else {
			this.dismiss();
		}
	}

	private class poponDismissListener implements OnDismissListener {

		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}
	}

	/**
	 * 设置添加屏幕的背景透明度
	 *
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = ((FragmentActivity) context)
				.getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		((FragmentActivity) context).getWindow().setAttributes(lp);
	}

	public class Holder {
		public ImageView ivSignClose;
		public TextView tvPoint;
		public TextView tvCheck;

		public Holder() {
			ivSignClose = (ImageView) mView.findViewById(R.id.iv_sign_close);
			ivSignClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					PopupSign.this.dismiss();
				}
			});
			tvPoint = (TextView) mView.findViewById(R.id.tv_point);
			tvCheck = (TextView) mView.findViewById(R.id.tv_check);
			tvCheck.setOnClickListener(mClickListener);
		}
	}
}
