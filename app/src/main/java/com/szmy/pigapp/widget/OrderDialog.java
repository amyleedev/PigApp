package com.szmy.pigapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.utils.FileUtil;

/**
 * 自定义dialog
 * @author 
 *
 */
/**
 * 自定义dialog
 * 
 * @author
 * 
 */

public class                                                                                                                                                                                                                                                                                                                                                            OrderDialog extends Dialog {
	// 定义回调事件，用于dialog的点击事件
	public interface OnDialogListener {
		public void back(String count, String price, String totleprice);
	}
public OrderDialog(){
	super(null);
}
	private String name;
	public static OnDialogListener customDialogListener;
	EditText etPrice;
	EditText etTotlePrice;
	EditText etCount;
	TextView tvTipText;
	LinearLayout mLlTip;
	LinearLayout mLl1,mLl2;
	String type = "";
	private String minPrice = "";
	private String maxPrice = "";
	
	public OrderDialog(Context context, String name,
			OnDialogListener customDialogListener,String type,String startPrice,String endPrice) {
		super(context);
		this.name = name;
		this.customDialogListener = customDialogListener;
		this.type = type;
		this.minPrice = startPrice;
		this.maxPrice = endPrice;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderdialog);
		// 设置标题
		setTitle(name);
		etPrice = (EditText) findViewById(R.id.etprice);
		etTotlePrice = (EditText) findViewById(R.id.ettotleprice);
		etCount = (EditText) findViewById(R.id.etcount);
		tvTipText = (TextView) findViewById(R.id.tiptext);
		mLlTip = (LinearLayout) findViewById(R.id.ll_tip);
		mLl1 = (LinearLayout) findViewById(R.id.layout1);
		mLl2 = (LinearLayout) findViewById(R.id.layout2);
		Button okBtn = (Button) findViewById(R.id.ok_btn);
		Button exitBtn = (Button) findViewById(R.id.exit_btn);
		if(type.equals("1")){//不可输入单价数量
			mLl1.setVisibility(View.GONE);
			mLl2.setVisibility(View.GONE);
		}else if(type.equals("xxfqr")){//不可输入数量
			mLl1.setVisibility(View.GONE);
		}else{
			mLl1.setVisibility(View.VISIBLE);
			mLl2.setVisibility(View.VISIBLE);
		}
		okBtn.setOnClickListener(clickListener);
		exitBtn.setOnClickListener(exitclickListener);

	}

	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if(type.equals("1")){
				if (TextUtils.isEmpty(etTotlePrice.getText())) {
					mLlTip.setVisibility(View.VISIBLE);
					tvTipText.setText("请输入正确的成交总价！");
				}else if(!FileUtil.isPrice(etTotlePrice.getText().toString())){
					mLlTip.setVisibility(View.VISIBLE);
					tvTipText.setText("请输入正确的成交总价！");
				} else {
					mLlTip.setVisibility(View.GONE);
					customDialogListener.back("","",
							String.valueOf(etTotlePrice.getText()));
					OrderDialog.this.dismiss();
				}
			}else if(type.equals("xxfqr")){
				if (TextUtils.isEmpty(etPrice.getText())
						|| etPrice.getText().toString().equals("0")) {
					mLlTip.setVisibility(View.VISIBLE);
					tvTipText.setText("请输入正确的成交单价！");
				}else if(!FileUtil.isPrice(etPrice.getText().toString())){
					mLlTip.setVisibility(View.VISIBLE);
					tvTipText.setText("请输入正确的成交单价！");
				}else
					if(!TextUtils.isEmpty(maxPrice)&&(!FileUtil.isLegalPrice(0,etPrice.getText().toString(), Float.parseFloat(maxPrice), Float.parseFloat(minPrice)))) {
						if (Double.parseDouble(etPrice.getText().toString()) > Float.parseFloat(maxPrice)) {
							mLlTip.setVisibility(View.VISIBLE);
							tvTipText.setText("您输入的单价过高!");
						}
						if (Double.parseDouble(etPrice.getText().toString()) < Float.parseFloat(minPrice)) {
							mLlTip.setVisibility(View.VISIBLE);
							tvTipText.setText("您输入的单价过低!");
						}
					}
				 else if (TextUtils.isEmpty(etTotlePrice.getText())
						|| etTotlePrice.getText().toString().equals("0")) {
					mLlTip.setVisibility(View.VISIBLE);
					tvTipText.setText("请输入正确的成交总价！");
				}else if(!FileUtil.isPrice(etTotlePrice.getText().toString())){
					mLlTip.setVisibility(View.VISIBLE);
					tvTipText.setText("请输入正确的成交总价！");
				}  else {
					mLlTip.setVisibility(View.GONE);
					customDialogListener.back("",String.valueOf(etPrice.getText()),
							String.valueOf(etTotlePrice.getText()));
					OrderDialog.this.dismiss();
				}
			}else{
				//2017-03-11 除鹤壁其他地区下单限制头数

			if (TextUtils.isEmpty(etCount.getText())
					|| etCount.getText().toString().equals("0")) {
				mLlTip.setVisibility(View.VISIBLE);
				tvTipText.setText("请输入正确的成交头数！");
			} else if (TextUtils.isEmpty(etPrice.getText())
					|| etPrice.getText().toString().equals("0")) {
				mLlTip.setVisibility(View.VISIBLE);
				tvTipText.setText("请输入正确的成交单价！");
			}else if(!FileUtil.isPrice(etPrice.getText().toString())){
				mLlTip.setVisibility(View.VISIBLE);
				tvTipText.setText("请输入正确的成交单价！");
			}else if(!TextUtils.isEmpty(maxPrice)&&(!FileUtil.isLegalPrice(0,etPrice.getText().toString(), Float.parseFloat(maxPrice), Float.parseFloat(minPrice)))) {
					if (Double.parseDouble(etPrice.getText().toString()) > Float.parseFloat(maxPrice)) {
						mLlTip.setVisibility(View.VISIBLE);
						tvTipText.setText("您输入的单价过高!");
					}
					if (Double.parseDouble(etPrice.getText().toString()) < Float.parseFloat(minPrice)) {
						mLlTip.setVisibility(View.VISIBLE);
						tvTipText.setText("您输入的单价过低!");
					}
				}else if (TextUtils.isEmpty(etTotlePrice.getText())
					|| etTotlePrice.getText().toString().equals("0")) {
				mLlTip.setVisibility(View.VISIBLE);
				tvTipText.setText("请输入正确的成交总价！");
			}else if(!FileUtil.isPrice(etTotlePrice.getText().toString())){
				mLlTip.setVisibility(View.VISIBLE);
				tvTipText.setText("请输入正确的成交总价！");
			}  else {
				if(type.equals("100")){
					if (Integer.parseInt(etCount.getText().toString())<100){
						mLlTip.setVisibility(View.VISIBLE);
						tvTipText.setText("交易头数不能少于100头！");
						return;
					}
				}
				mLlTip.setVisibility(View.GONE);
				customDialogListener.back(String.valueOf(etCount.getText().toString()),String.valueOf(etPrice.getText()),
						String.valueOf(etTotlePrice.getText()));
				OrderDialog.this.dismiss();
			}
			}
		}
	};
	private View.OnClickListener exitclickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			OrderDialog.this.dismiss();

		}
	};

}
