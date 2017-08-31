package com.szmy.pigapp.image;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.szmy.pigapp.R;
/**
 * 点击拍照弹出框
 * @author qing
 *
 */

public class PopupWindows extends PopupWindow {
public PopupWindows(){
	super();
}
	public PopupWindows(Activity paramActivity,
			View.OnClickListener paramOnClickListener, View parent) {
//		super(paramActivity);
		View view = View
				.inflate(paramActivity, R.layout.item_popupwindows, null);
		view.startAnimation(AnimationUtils.loadAnimation(paramActivity,
				R.anim.fade_ins));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(paramActivity,
				R.anim.push_bottom_in_2));

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
//		 update();

		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		// 设置每个子布局的事件监听器
		if (paramOnClickListener != null) {
			bt1.setOnClickListener(paramOnClickListener);
			bt2.setOnClickListener(paramOnClickListener);
			bt3.setOnClickListener(paramOnClickListener);
		}
	

	}

}
