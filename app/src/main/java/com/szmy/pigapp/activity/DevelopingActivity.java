package com.szmy.pigapp.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.szmy.pigapp.R;

/*
 * 
 */
public class DevelopingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.developing);
		
		((TextView) findViewById(R.id.def_head_tv)).setText("");

	}

}
