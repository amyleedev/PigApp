package com.szmy.pigapp.widget;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.szmy.pigapp.R;

/**
 * 自定义dialog
 *
 * @author
 */

public class YanzhengmaDialog extends PopupWindow {

    EditText etPay;
    public static OnDialogListener customDialogListener;

    Context mCon;

    public YanzhengmaDialog() {
        super();
    }

    public interface OnDialogListener {
        public void back(String msgcode);
    }

    public YanzhengmaDialog(Activity paramActivity, View parent, String payprice, OnDialogListener customDialogListener) {
        // super(paramActivity);
        mCon = paramActivity;
        View view = View.inflate(paramActivity, R.layout.yanzhengmadialog, null);
        view.startAnimation(AnimationUtils.loadAnimation(paramActivity,
                R.anim.fade_ins));
        LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(paramActivity,
                R.anim.push_bottom_in_2));
        //设置弹出窗体需要软键盘，
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //再设置模式，和Activity的一样，覆盖，调整大小。
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        this.customDialogListener = customDialogListener;

        etPay = (EditText) view.findViewById(R.id.et_paypwd);

        Button okBtn = (Button) view.findViewById(R.id.ok_btn);

        LinearLayout exitBtn = (LinearLayout) view.findViewById(R.id.exit_btn1);


        // 设置每个子布局的事件监听器
        okBtn.setOnClickListener(clickListener);
        exitBtn.setOnClickListener(exitclickListener);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
                customDialogListener.back(String.valueOf(etPay.getText().toString()));
                YanzhengmaDialog.this.dismiss();

        }
    };
    private View.OnClickListener exitclickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            YanzhengmaDialog.this.dismiss();

        }
    };
}
