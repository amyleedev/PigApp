package com.szmy.pigapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.adapter.ColorAdapter;

import java.util.ArrayList;
/**
 * ѡ��ëɫ
 * @author Administrator
 *
 */
public class ChosePigColorActivity extends BaseActivity {  
    private ListView lv;  
    private ColorAdapter mAdapter;  
    private ArrayList<String> list;  
    private ArrayList<String> colorlist ;  
    private int checkNum; // 记录选中的条目数量  
//    private TextView tv_show;// 用于显示选中的条目数量  
   private TextView tvName;
   private Button btn_sure;
   private TextView tv_choice;
   private String colorname = "";   
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_area_selector);  
        /* 实例化各个控件 */  
        lv = (ListView) findViewById(R.id.lv_list);        
        tvName = (TextView) findViewById(R.id.tv_name);  
        tvName.setText("请选择");
        tv_choice = (TextView) findViewById(R.id.tv_choice);
        list = new ArrayList<String>(); 
        colorlist = new ArrayList<String>();
        // 为Adapter准备数据  
        initDate();  
        // 实例化自定义的MyAdapter  
        mAdapter = new ColorAdapter(list, this);  
        // 绑定Adapter  
        lv.setAdapter(mAdapter);  
        // 绑定listView的监听器  
        lv.setOnItemClickListener(new OnItemClickListener() {  
            @Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤  
            	ColorAdapter.ViewHolder holder = (ColorAdapter.ViewHolder) arg1.getTag();  
                // 改变CheckBox的状态  
                holder.cb.toggle();  
                // 将CheckBox的选中状况记录下来  
                ColorAdapter.getIsSelected().put(arg2, holder.cb.isChecked());                 
                // 调整选定条目  
                if (holder.cb.isChecked() == true) {  
                	colorlist.add(holder.tv.getText().toString());
                    checkNum++;  
                } else {  
                	colorlist.remove(holder.tv.getText().toString());
                    checkNum--;  
                }  
                colorname = "";
                int i = 0;
                for(String str:colorlist){
                	i++;               	
                		if(i == colorlist.size()){
                			colorname += str;
                		}else{
                	    colorname += (str+",");
                		}
               }
                tv_choice.setText(colorname);  
            }  
        });  
        btn_sure = (Button) findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!TextUtils.isEmpty(tv_choice.getText().toString())){
				 Intent intent = new Intent();
	             Bundle bundle=new Bundle();	           
	             bundle.putString("colorname", tv_choice.getText().toString()); 
	             intent.putExtras(bundle);
	             setResult(30,intent);
				}
	             finish();
			}
		});
    }  
  
    // 初始化数据  
    private void initDate() {  
       list.clear();
            list.add("白色"); 
            list.add("黑色"); 
            list.add("花色"); 
            list.add("其他"); 
    }    
}