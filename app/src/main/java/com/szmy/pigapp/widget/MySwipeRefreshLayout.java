package com.szmy.pigapp.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MySwipeRefreshLayout extends SwipeRefreshLayout {
	/**
	 * 自定义图片
	 */
	private ViewFlow viewFlow;
	private String TAG = "MyViewFlow";
	boolean onHorizontal = false;
	float x = 0.0f;
	float y = 0.0f;

	public MySwipeRefreshLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setView(ViewFlow viewFlow) {
		this.viewFlow = viewFlow;
	}

	// 对触屏事件进行重定向
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
				Log.i(TAG, "viewFlow");
				return true;
			} else {
				Log.i(TAG, "listview");
				return super.onInterceptTouchEvent(ev);
			}
		} else {
			return super.onInterceptTouchEvent(ev);
		}
	}

	// 对触屏事件进行处理
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i(TAG, "viewFlow�Ƿ񱻵����" + ViewFlow.onTouch);
		if (ViewFlow.onTouch) {
			return viewFlow.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}
}
