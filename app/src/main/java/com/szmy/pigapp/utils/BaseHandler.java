package com.szmy.pigapp.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class BaseHandler extends Handler {
	Context c;
	public BaseHandler(Context c) {
		this.c=c;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);

		if(msg.what==-1){
		}

		if(msg.what==-2){
			Toast.makeText(c, "无网络", Toast.LENGTH_SHORT).show();
		}
		if(msg.what==-3){
			Toast.makeText(c, "请打开GPS", Toast.LENGTH_SHORT).show();
		}


	}
}