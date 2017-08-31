package com.szmy.pigapp.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.szmy.pigapp.R;
import com.szmy.pigapp.adapter.ViewFlowAdapter;
import com.szmy.pigapp.widget.CircleFlowIndicator;
import com.szmy.pigapp.widget.ViewFlow;

public class ActivityGuide extends BaseActivity {
	private ViewFlow mVF;
	private CircleFlowIndicator mCFI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		mVF = (ViewFlow) findViewById(R.id.vf);
		mCFI = (CircleFlowIndicator) findViewById(R.id.cfi);
		ViewFlowAdapter adapter = new ViewFlowAdapter(ActivityGuide.this);
		mVF.setAdapter(adapter);
		mVF.setmSideBuffer(1);
		mVF.setFlowIndicator(mCFI);
		// mVF.setTimeSpan(5000);
		mVF.setSelection(0);// 锟斤拷锟矫筹拷始位锟斤拷
		// showToast(ActivityLead.this, "锟斤拷锟斤拷锟揭伙拷锟斤拷");
	}

	public void onResume() {
		super.onResume();
		// MobclickAgent.onPageStart("ActivityLead"); //
		// 缁熻椤甸潰(浠呮湁Activity鐨勫簲鐢ㄤ腑SDK鑷姩璋冪敤锛屼笉闇�鍗曠嫭鍐�
		// MobclickAgent.onResume(this); // 缁熻鏃堕暱
	}

	public void onPause() {
		super.onPause();
		// MobclickAgent.onPageEnd("ActivityLead"); //
		// 锛堜粎鏈堿ctivity鐨勫簲鐢ㄤ腑SDK鑷姩璋冪敤锛屼笉闇�鍗曠嫭鍐欙級淇濊瘉
		// onPageEnd 鍦╫nPause
		// 涔嬪墠璋冪敤,鍥犱负 onPause
		// 涓細淇濆瓨淇℃伅
		// MobclickAgent.onPause(this);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}

		return false;

	}
}
