package com.szmy.pigapp.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.ActivitySetPayPassword;

/**
 * 自定义dialog
 *
 * @author
 */

/**
 * 自定义支付密码dialog
 *
 * @author
 *
 */

// public class PayDialog extends Dialog {
// // 定义回调事件，用于dialog的点击事件
// public interface OnDialogListener {
// public void back(String pwd);
// }
//
// private String name;
// public static OnDialogListener customDialogListener;
// EditText etPay;
// TextView tvTipText;
// LinearLayout mLlTip;
// LinearLayout mLl1,mLl2;
//
// public PayDialog(Context context, String name,
// OnDialogListener customDialogListener) {
// super(context);
// this.name = name;
// this.customDialogListener = customDialogListener;
// }
//
// @Override
// protected void onCreate(Bundle savedInstanceState) {
// super.onCreate(savedInstanceState);
// setContentView(R.layout.paydialog);
// // 设置标题
// setTitle(name);
// etPay = (EditText) findViewById(R.id.et_paypwd);
// tvTipText = (TextView) findViewById(R.id.tiptext);
// mLlTip = (LinearLayout) findViewById(R.id.ll_tip);
// Button okBtn = (Button) findViewById(R.id.ok_btn);
// Button exitBtn = (Button) findViewById(R.id.exit_btn);
//
// okBtn.setOnClickListener(clickListener);
// exitBtn.setOnClickListener(exitclickListener);
//
// }
//
// private View.OnClickListener clickListener = new View.OnClickListener() {
//
// @Override
// public void onClick(View v) {
//
// if (TextUtils.isEmpty(etPay.getText())||etPay.getText().length()<6) {
// mLlTip.setVisibility(View.VISIBLE);
// tvTipText.setText("请输入六位支付密码！");
// } else {
// mLlTip.setVisibility(View.GONE);
// customDialogListener.back(String.valueOf(etPay.getText()));
// PayDialog.this.dismiss();
// }
//
// }
// };
// private View.OnClickListener exitclickListener = new View.OnClickListener() {
//
// @Override
// public void onClick(View v) {
//
// PayDialog.this.dismiss();
//
// }
// };
//
// }
public class PayDialog extends PopupWindow {

    EditText etPay;
    public static OnDialogListener customDialogListener;
    TextView tvTipText;
    LinearLayout mLlTip;
    LinearLayout mLl1, mLl2;
    TextView mTvPaytoName;
    TextView mTvPaytoCard;
    TextView mTvPayCard;
    TextView mTvPayPrice;
    Button mBtnNext;
    Button mBtnBack;
    TextView mTvForgetPwd;
    Context mCon;

    public PayDialog() {
        super();
    }

    public interface OnDialogListener {
        public void back(String pwd);
    }

    public PayDialog(Activity paramActivity, View parent, String tip,
                     String name, String paytocard, String paycard, String payprice, OnDialogListener customDialogListener) {
        // super(paramActivity);
        mCon = paramActivity;
        View view = View.inflate(paramActivity, R.layout.paydialog, null);
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
        mTvPaytoName = (TextView) view.findViewById(R.id.tv_paytoname);
        mTvPaytoCard = (TextView) view.findViewById(R.id.tv_paytocard);
        mTvPayCard = (TextView) view.findViewById(R.id.tv_paycard);
        mTvPayPrice = (TextView) view.findViewById(R.id.tv_price);
        etPay = (EditText) view.findViewById(R.id.et_paypwd);
        tvTipText = (TextView) view.findViewById(R.id.tiptext);
        mLlTip = (LinearLayout) view.findViewById(R.id.ll_tip);
        Button okBtn = (Button) view.findViewById(R.id.ok_btn);
        mLl1 = (LinearLayout) view.findViewById(R.id.layout1);
        mLl2 = (LinearLayout) view.findViewById(R.id.layout2);
        mBtnBack = (Button) view.findViewById(R.id.btn_up);
        mBtnNext = (Button) view.findViewById(R.id.btn_next);
        LinearLayout exitBtn = (LinearLayout) view.findViewById(R.id.exit_btn1);
        mTvForgetPwd = (TextView) view.findViewById(R.id.textView2);

        // 设置每个子布局的事件监听器
        okBtn.setOnClickListener(clickListener);
        exitBtn.setOnClickListener(exitclickListener);
        mTvPaytoName.setText(name);
        mTvPaytoCard.setText("农业银行(" + paytocard + ")");
        mTvPayCard.setText("农业银行(" + paycard + ")");
        mTvPayPrice.setText(payprice + " 元");
        if (!tip.equals("0")) {
            tvTipText.setText("您的支付密码已错误输入" + tip + "次,输入五次错误该账号将被系统锁定！");
        }
        mBtnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLl1.setVisibility(View.GONE);
                mLl2.setVisibility(View.VISIBLE);
            }
        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLl1.setVisibility(View.VISIBLE);
                mLl2.setVisibility(View.GONE);

            }
        });
        mTvForgetPwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PayDialog.this.dismiss();
                Intent intent = new Intent(mCon,
                        ActivitySetPayPassword.class);
                intent.putExtra("type", "forgot");
                mCon.startActivity(intent);

            }
        });

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (TextUtils.isEmpty(etPay.getText())
                    || etPay.getText().length() < 6) {
//				mLlTip.setVisibility(View.VISIBLE);
                tvTipText.setText("请输入六位支付密码！");
            } else {
//				mLlTip.setVisibility(View.GONE);
                customDialogListener.back(String.valueOf(etPay.getText().toString()));
                PayDialog.this.dismiss();
            }

        }
    };
    private View.OnClickListener exitclickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            PayDialog.this.dismiss();

        }
    };
}
