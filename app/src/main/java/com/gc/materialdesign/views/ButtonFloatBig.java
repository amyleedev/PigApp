package com.gc.materialdesign.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.gc.materialdesign.utils.Utils;
import com.szmy.pigapp.R;

public class ButtonFloatBig extends ButtonFloat {

	public ButtonFloatBig(Context context, AttributeSet attrs) {
		super(context, attrs);
		sizeRadius = 40;
		sizeIcon = 40;
		setDefaultProperties();
		LayoutParams params = new LayoutParams(Utils.dpToPx(sizeIcon, getResources()),Utils.dpToPx(sizeIcon, getResources()));
		params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		icon.setLayoutParams(params);
	}
	
	protected void setDefaultProperties(){
		rippleSpeed = Utils.dpToPx(2, getResources());
		rippleSize = 18;
		// Min size
		setMinimumHeight(Utils.dpToPx(sizeRadius*2, getResources()));
		setMinimumWidth(Utils.dpToPx(sizeRadius*2, getResources()));
		// Background shape
		setBackgroundResource(R.drawable.background_button_float);
//		setBackgroundColor(backgroundColor);
	}
}
