package com.szmy.pigapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.FileUtil;

public class MyDialog extends Dialog {

    //定义回调事件，用于dialog的点击事件
   public interface OnDialogListener{
            public void back(String delayday);
    }



    private String name;
    public static OnDialogListener customDialogListener;
    EditText etDelayDay;
    TextView tvTipText;
public MyDialog(){
    super(null);

}

    public MyDialog(Context context,String name,OnDialogListener customDialogListener) {
            super(context);
            this.name = name;
            this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.mydialog);
            //设置标题
           setTitle(name);
           etDelayDay = (EditText)findViewById(R.id.etdelayday);
           tvTipText = (TextView)findViewById(R.id.tiptext);
            Button okBtn = (Button) findViewById(R.id.ok_btn);
            Button exitBtn = (Button) findViewById(R.id.exit_btn);
            okBtn.setOnClickListener(clickListener);
            exitBtn.setOnClickListener(exitclickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	if(TextUtils.isEmpty(etDelayDay.getText())||etDelayDay.getText().toString().equals("0")){
            		tvTipText.setText("请输入您要延期支付的天数！");
            	}else if(!FileUtil.isNumeric(etDelayDay.getText().toString())){
            		tvTipText.setText("您输入的格式不正确，请输入数字！");
            	}else if(Integer.parseInt(etDelayDay.getText().toString())>10){
            		tvTipText.setText("延期的时间不能超过10天！");
            	}else{
                    customDialogListener.back(String.valueOf(etDelayDay.getText()));
                MyDialog.this.dismiss();
            	}
            }
    };
    private View.OnClickListener exitclickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            MyDialog.this.dismiss();

        }
};

}