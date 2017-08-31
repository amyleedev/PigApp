package com.szmy.pigapp.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szmy.pigapp.R;


/**
 * 点击拍照弹出框
 * @author qing
 *
 */

public class DaijinquanPopuWindow extends PopupWindow {
    private RelativeLayout rootview;
    private RelativeLayout.LayoutParams layoutParams;
    private String money = "";
    private int typeid;

    private String[] typenames = {"猪场","屠宰场","经纪人","物流公司","自然人"};
    public DaijinquanPopuWindow(){
        super();
    }
    public DaijinquanPopuWindow(Activity paramActivity, View.OnClickListener paramOnClickListener, View parent,String money,String topname) {

        View view = View
                .inflate(paramActivity, R.layout.youhuiquan_dialog, null);
        view.startAnimation(AnimationUtils.loadAnimation(paramActivity,
                R.anim.fade_ins));
        LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(paramActivity,
                R.anim.push_bottom_in_2));
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        setBackgroundDrawable(dw);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.CENTER, 0, 0);
        this.money = money;
//		 update();
        ImageView bt1 = (ImageView) view.findViewById(R.id.close);
        Button bt2 = (Button) view.findViewById(R.id.btn_look);
        TextView mTvMoney = (TextView) view.findViewById(R.id.money);
        mTvMoney.setText(Html.fromHtml("获得<font color = '#ff0000'>"+money+"</font>元代金券"));
        TextView mTvTopName = (TextView) view.findViewById(R.id.topname);
        mTvTopName.setText(topname);
        // 设置每个子布局的事件监听器
        if (paramOnClickListener != null) {
            bt1.setOnClickListener(paramOnClickListener);
            bt2.setOnClickListener(paramOnClickListener);
        }
    }
}
