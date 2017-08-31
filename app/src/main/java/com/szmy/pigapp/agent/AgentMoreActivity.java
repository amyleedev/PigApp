package com.szmy.pigapp.agent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.BaseActivity;
import com.szmy.pigapp.entity.AgentEntry;
import com.szmy.pigapp.utils.AppStaticUtil;
import com.szmy.pigapp.utils.UrlEntry;

public class AgentMoreActivity extends BaseActivity {
	ImageButton add_btn;
	ImageView picture;
	TextView more_name, more_business, more_phone, more_qq, more_address,
			more_companyName, more_intro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_more);
		((TextView) findViewById(R.id.def_head_tv)).setText("经纪人档案");
		add_btn = (ImageButton) findViewById(R.id.add_btn);
		add_btn.setVisibility(View.GONE);
		initView();
	}

	private void initView() {
		Intent intent = getIntent();
		String address = intent.getStringExtra("province") + " "
				+ intent.getStringExtra("city") + " "
				+ intent.getStringExtra("area");
		((TextView) findViewById(R.id.more_name)).setText(intent
				.getStringExtra("name"));
		String str=intent.getStringExtra("phone");
		if (str.length() >= 11) {
			str=str.substring(0, 11);
			((TextView) findViewById(R.id.more_phone)).setText(str.substring(0,
					str.length() - (str.substring(3)).length())
					+ "****" + str.substring(7));
		} else {
			((TextView) findViewById(R.id.more_phone)).setText(str.substring(0, str.length())+"***");
		}
		if (intent.getStringExtra("qq").trim().equals("")) {
			((TextView) findViewById(R.id.more_qq)).setText("暂无");
		} else {
			((TextView) findViewById(R.id.more_qq)).setText(intent
					.getStringExtra("qq"));
		}
		if (address.trim().equals("")) {
			((TextView) findViewById(R.id.more_address)).setText("暂无");
		} else {
			((TextView) findViewById(R.id.more_address)).setText(address);
		}
		if (intent.getStringExtra("business").trim().equals("")) {
			((TextView) findViewById(R.id.more_business)).setText("暂无");
		} else {
			((TextView) findViewById(R.id.more_business)).setText(intent
					.getStringExtra("business"));
		}
		if (intent.getStringExtra("companyName").trim().equals("")) {
			((TextView) findViewById(R.id.more_companyName)).setText("暂无");
		} else {
			((TextView) findViewById(R.id.more_companyName)).setText(intent
					.getStringExtra("companyName"));
		}
		if (intent.getStringExtra("intro").trim().equals("")) {
			((TextView) findViewById(R.id.more_intro)).setText("暂无介绍");
		} else {
			((TextView) findViewById(R.id.more_intro)).setText(intent
					.getStringExtra("intro"));
		}
		picture = (ImageView) findViewById(R.id.more_imageview);
		AgentEntry entry=(AgentEntry) intent.getSerializableExtra("entity");
		if (!TextUtils.isEmpty(entry.getPicture())) {
			com.nostra13.universalimageloader.core.ImageLoader
					.getInstance().displayImage(
							UrlEntry.ip + entry.getPicture(), picture,
							AppStaticUtil.getOptions());
		} else {
			picture.setImageResource(R.drawable.photo);
		}
	}





}
