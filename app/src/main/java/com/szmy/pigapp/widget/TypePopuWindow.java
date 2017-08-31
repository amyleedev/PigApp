package com.szmy.pigapp.widget;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.szmy.pigapp.R;

import java.util.ArrayList;


/**
 * 点击拍照弹出框
 * @author qing
 *
 */

public class TypePopuWindow extends PopupWindow {
    private RelativeLayout rootview;
    private RelativeLayout.LayoutParams layoutParams;
    private String name = "";
    private int typeid;
    public static OnDialogListener paramOnClickListener;
    private String[] typenames = {"猪场","屠宰场","经纪人","物流公司","自然人"};
    public TypePopuWindow(){
        super();
    }
    public TypePopuWindow(Activity paramActivity,
                        OnDialogListener paramOnClickListener, View parent,int tvtypeid,String tvname,String[] typenameslist) {
//		super(paramActivity);
        this.paramOnClickListener = paramOnClickListener;
        this.name = tvname;
        this.typeid = tvtypeid;
        this.typenames = typenameslist;
        View view = View
                .inflate(paramActivity, R.layout.item_typepopuwindow, null);
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
        Button bt1 = (Button) view.findViewById(R.id.ok_btn1);
        Button bt2 = (Button) view.findViewById(R.id.exit_btn);
        bt2.setOnClickListener(exitclickListener);
        // 设置每个子布局的事件监听器
        if (paramOnClickListener != null) {
            bt1.setOnClickListener(okclickListener);

        }
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        rootview = (RelativeLayout) view.findViewById(R.id.rootview);

        LoopView loopView = new LoopView(paramActivity);
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0;i <typenames.length;i++){
            list.add(typenames[i]);
        }


        //设置是否循环播放
//        loopView.setNotLoop();
        //滚动监听
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Log.d("debug", "Item " + index);
                name = typenames[index];
                typeid = index+1;
            }
        });
        //设置原始数据
        loopView.setItems(list);
        //设置初始位置
        loopView.setInitPosition(2);
//        name = "经纪人";
//        typeid = 3;
        //设置字体大小
        loopView.setTextSize(20);
        rootview.addView(loopView, layoutParams);

    }
    public interface OnDialogListener {
        public void back(String name,int type);
    }
    private View.OnClickListener okclickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            paramOnClickListener.back(name, typeid);
            dismiss();

        }
    };
    private View.OnClickListener exitclickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            dismiss();

        }
    };
}
