package com.szmy.pigapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
	
	private ViewFlow viewFlow;
	private String TAG = "MyViewFlow";
	boolean onHorizontal = false;
	float x = 0.0f;
	float y = 0.0f;

	public MyScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setView(ViewFlow viewFlow) {
		this.viewFlow = viewFlow;
	}

	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			onHorizontal = false;
			x = ev.getX();
			y = ev.getY();
			break;
		case MotionEvent.ACTION_UP:
			onHorizontal = false;
			break;
		// case MotionEvent.ACTION_MOVE:
		// float dx = Math.abs(ev.getX() - x);
		// Log.i("MyViewFlow", "dx:=" + dx);
		// if (dx > 20.0) {
		// onHorizontal = true;
		// } else {
		// onHorizontal = false;
		// }
		// break;
		}

		if (ViewFlow.onTouch) {
			float dx = Math.abs(ev.getX() - x);
			Log.i("MyViewFlow", "dx:=" + dx);
			if (dx > 20.0) {
				onHorizontal = true;
			}
			if (onHorizontal) {
				Log.i(TAG, "viewFlow处理");
				return true;
			} else {
				Log.i(TAG, "listview处理");
				return super.onInterceptTouchEvent(ev);
			}
		} else {
			return super.onInterceptTouchEvent(ev);
		}
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i(TAG, "viewFlow是否被点击：" + ViewFlow.onTouch);
		if (ViewFlow.onTouch) {
			return viewFlow.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}
}
