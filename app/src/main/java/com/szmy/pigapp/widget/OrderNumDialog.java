package com.szmy.pigapp.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.FileUtil;
/**
* 自定义下单dialog
* @author 
*
*/

public class OrderNumDialog extends Dialog implements OnCheckedChangeListener {
    //定义回调事件，用于dialog的点击事件
   public interface OnDialogListener{
            public void back(String price, String totleprice, String num);
    }
    
    private String name;
    public static OnDialogListener customDialogListener;
    private LinearLayout mLlnum;
    private LinearLayout mLlTip;
    EditText etDelayDay;
    TextView tvTipText;
    private EditText mEtTotlePrice;
    private EditText mEtNum;
    private EditText mEtPrice;
    private RadioGroup mRgRadio;
    private Boolean flag = false;
    private String totlenum;
    private String numCon ;
    private String minPrice = "";
    private String maxPrice = "";

    public OrderNumDialog(){
        super(null);
    }
    public OrderNumDialog(Context context,String name,OnDialogListener customDialogListener,String totlenum,String numcon,String startPrice,String endPrice) {
            super(context);
            this.name = name;
            this.customDialogListener = customDialogListener;
            this.totlenum = totlenum;
        this.numCon =numcon;
        this.minPrice = startPrice;
        this.maxPrice = endPrice;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) { 
            super.onCreate(savedInstanceState);
            setContentView(R.layout.delaydaydialog);
            //设置标题
           setTitle(name); 
           mLlnum = (LinearLayout) findViewById(R.id.llnumlayout);
           mLlTip = (LinearLayout) findViewById(R.id.lltiplayout);
           tvTipText = (TextView)findViewById(R.id.tiptext);
           mEtNum = (EditText) findViewById(R.id.etCunNum);
           mEtTotlePrice = (EditText) findViewById(R.id.etprice);
           mRgRadio = (RadioGroup) findViewById(R.id.rg_status);
           mRgRadio.setOnCheckedChangeListener(this);
           mEtPrice = (EditText) findViewById(R.id.et_price);
         
            Button okBtn = (Button) findViewById(R.id.ok_btn);
            Button exitBtn = (Button) findViewById(R.id.exit_btn);
            okBtn.setOnClickListener(clickListener);
            exitBtn.setOnClickListener(exitclickListener);
    }
    
    private View.OnClickListener clickListener = new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
            	String num = "";
            	if(flag){
            		if(TextUtils.isEmpty(mEtNum.getText())||mEtNum.getText().toString().equals("0")){           		
                		showtip("请输入要下单的头数！");
                		return;
            		}
//            		if(!FileUtil.isNumeric(mEtNum.getText().toString())){
//            			showtip("您输入的格式不正确，请输入数字！");
//            			return;
//            		}
            		if(Integer.parseInt(mEtNum.getText().toString())>Integer.parseInt(totlenum)){
            			showtip("订单总头数为："+totlenum+",您已超出，请重新输入！");
                		return;
            		}
                    if(numCon.equals("100")){
                        if(Integer.parseInt(mEtNum.getText().toString())<100){
                            showtip("交易头数不能少于100头，请重新输入！");
                            return;
                        }
                    }
            		num = mEtNum.getText().toString();
            		
            	}else{
            		num = totlenum;
            	}
            	
            	if(TextUtils.isEmpty(mEtPrice.getText())||mEtPrice.getText().toString().equals("0")||!FileUtil.isPrice(mEtPrice.getText().toString())){
            		showtip("请输入要成交的最终单价！");
            		return;
            	}else
                    if(!TextUtils.isEmpty(maxPrice)&&(!FileUtil.isLegalPrice(0,mEtPrice.getText().toString(), Float.parseFloat(maxPrice), Float.parseFloat(minPrice)))) {
                        if (Double.parseDouble(mEtPrice.getText().toString()) > Float.parseFloat(maxPrice)) {
                            showtip("您输入的单价过高!");
                            return;
                        }
                        if (Double.parseDouble(mEtPrice.getText().toString()) < Float.parseFloat(minPrice)) {
                            showtip("您输入的单价过低!");
                            return;
                        }
                    }
                else if(TextUtils.isEmpty(mEtTotlePrice.getText())||mEtTotlePrice.getText().toString().equals("0")||!FileUtil.isPrice(mEtTotlePrice.getText().toString())){
            		showtip("请输入要成交的总价价格！");
            		return;
            	}
//            	else if(!FileUtil.isNumeric(mEtPrice.getText().toString())){
//            		showtip("您输入的格式不正确，请输入数字！");
//            		return;
//            	}
            	mLlTip.setVisibility(View.GONE);
            	customDialogListener.back(mEtPrice.getText().toString(),mEtTotlePrice.getText().toString(),num);
                OrderNumDialog.this.dismiss();
            	
            }
    };
    private View.OnClickListener exitclickListener = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
               
            OrderNumDialog.this.dismiss();
            
        }
};

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.radio_all:
			flag = false;
			mLlnum.setVisibility(View.GONE);
			
			break;
		case R.id.radio_other:
			flag = true;
			mLlnum.setVisibility(View.VISIBLE);
			break;
		
		}
		
	}

	private void showtip(String msg){
		mLlTip.setVisibility(View.VISIBLE);
		tvTipText.setText(msg);
	}
}