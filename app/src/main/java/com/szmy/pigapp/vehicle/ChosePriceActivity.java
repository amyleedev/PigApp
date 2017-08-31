package com.szmy.pigapp.vehicle;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChosePriceActivity extends BaseActivity 
		 {

	

	private TextView searchprice_content;
	private EditText et_minprice,et_maxprice;
   private Button btn_submit;
    private String type ="";
    private LinearLayout mLinearLayout;
    List<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();  
   
    private String pricetype = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chose_price_selector);
		((TextView) findViewById(R.id.def_head_tv)).setText("请选择");
	
		
		
		initView();

		
	}

	private void initView() {
	
		
		searchprice_content = (TextView) findViewById(R.id.searchprice_content);
		et_maxprice = (EditText) findViewById(R.id.et_maxprice);
		et_minprice = (EditText) findViewById(R.id.et_minprice);
		searchprice_content.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new PricePopupWindows(ChosePriceActivity.this,searchprice_content);
				
			}
		});
				
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				 Intent intent = new Intent();
	             Bundle bundle=new Bundle();
	             bundle.putString("priceType", pricetype); 
	             bundle.putString("minprice",et_minprice.getText().toString());
	             bundle.putString("maxprice",et_maxprice.getText().toString());
	             intent.putExtras(bundle);
	             setResult(23,intent);
	             finish();
				
			}
		});
              
//				
                
		
		
		
		
		
	
		
	}

	public class PricePopupWindows extends PopupWindow {

		public PricePopupWindows(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.time_dialog, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));
			TextView text = (TextView) view.findViewById(R.id.text);
			text.setText("请选择");
			TextView exittv = (TextView) view.findViewById(R.id.exit);
			exittv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dismiss();
					
				}
			});
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			ListView mListviewPro = (ListView) view
					.findViewById(R.id.lv_list_date);
			getcountdata();
			SimpleAdapter adapter = new SimpleAdapter(ChosePriceActivity.this, data,
					R.layout.item_date, new String[] { "name" },
					new int[] { R.id.text1 });
			mListviewPro.setAdapter(adapter);
			mListviewPro.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					HashMap<String, String> maplist = (HashMap<String, String>) parent
							.getItemAtPosition(position);
					
				searchprice_content.setText(maplist.get("name"));
				pricetype = maplist.get("type");

					dismiss();
				}
			});

		}
	}

	private void getcountdata() {
		data.clear();
		 HashMap<String,String> maplist1 = new HashMap<String, String>();
		  maplist1.put("type", "price desc");
		  maplist1.put("name", "从高到低");
		  data.add(maplist1);
		  HashMap<String,String> maplist2 = new HashMap<String, String>();
		  maplist2.put("type", "price asc");
		  maplist2.put("name", "从低到高");
		  data.add(maplist2);

	}

	
	

	
}
