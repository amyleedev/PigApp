package com.szmy.pigapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/8/1 0001.
 */
public class SildeListView extends ListView {
    private int mScreenWidth;   // 屏幕宽度
    private int mDownX;         // 按下点的x值
    private int mDownY;         // 按下点的y值
    private int mDeleteBtnWidth;// 删除按钮的宽度

    private boolean isDeleteShown;  // 删除按钮是否正在显示

    private ViewGroup mPointChild;  // 当前处理的item
    private LinearLayout.LayoutParams mLayoutParams;    // 当前处理的item的LayoutParams

    public SildeListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SildeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // 获取屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performActionDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                return performActionMove(ev);
            case MotionEvent.ACTION_UP:
                performActionUp();
                break;
        }

        return super.onTouchEvent(ev);
    }

    // 处理action_down事件
    private void performActionDown(MotionEvent ev) {
        if(isDeleteShown) {
            turnToNormal();
        }

        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();
        // 获取当前点的item
        mPointChild = (ViewGroup) getChildAt(pointToPosition(mDownX, mDownY)
                - getFirstVisiblePosition());
        if (mPointChild==null) return;
        // 获取删除按钮的宽度
        mDeleteBtnWidth = mPointChild.getChildAt(1).getLayoutParams().width;
        mLayoutParams = (LinearLayout.LayoutParams) mPointChild.getChildAt(0)
                .getLayoutParams();
        mLayoutParams.width = mScreenWidth;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
    }

    // 处理action_move事件
    private boolean performActionMove(MotionEvent ev) {
        int nowX = (int) ev.getX();
        int nowY = (int) ev.getY();
        if(Math.abs(nowX - mDownX) > Math.abs(nowY - mDownY)) {
            // 如果向左滑动
            if(nowX < mDownX) {
                // 计算要偏移的距离
                int scroll = (nowX - mDownX) / 2;
                // 如果大于了删除按钮的宽度， 则最大为删除按钮的宽度
                if(-scroll >= mDeleteBtnWidth) {
                    scroll = -mDeleteBtnWidth;
                }
                if (mLayoutParams==null)return true;
                // 重新设置leftMargin
                mLayoutParams.leftMargin = scroll*2;
                mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    // 处理action_up事件
    private void performActionUp() {
        // 偏移量大于button的一半，则显示button
        if (mLayoutParams==null) return;
        // 否则恢复默认
        if(-mLayoutParams.leftMargin >= mDeleteBtnWidth / 2) {
            mLayoutParams.leftMargin = -mDeleteBtnWidth*2;
            isDeleteShown = true;
        }else {
            turnToNormal();
        }

        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
    }
    /**
     * 变为正常状态
     */
    public void turnToNormal() {
        mLayoutParams.leftMargin = 0;
        if (mPointChild==null) return;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
        isDeleteShown = false;
    }
    /**
     * 当前是否可点击
     * @return 是否可点击
     */
    public boolean canClick() {
        return !isDeleteShown;
    }


}