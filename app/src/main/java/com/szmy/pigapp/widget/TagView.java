package com.szmy.pigapp.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.szmy.pigapp.R;

public class TagView extends CheckBox {
	
	private boolean mCheckEnable = true;

	public TagView(Context paramContext) {
		super(paramContext);
		init();
	}

	public TagView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		init();
	}

	public TagView(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt) {
		super(paramContext, paramAttributeSet, 0);
		init();
	}

	@SuppressLint("ResourceAsColor")
	private void init() {
		setText("");
		setBackgroundResource(R.drawable.btn_tag);
		setTextColor(R.color.black);
	}

	public void setCheckEnable(boolean paramBoolean) {
		this.mCheckEnable = paramBoolean;
		if (!this.mCheckEnable) {
			super.setChecked(false);
		}
	}

	public void setChecked(boolean paramBoolean) {
		if (this.mCheckEnable) {
			super.setChecked(paramBoolean);
		}
	}
}
