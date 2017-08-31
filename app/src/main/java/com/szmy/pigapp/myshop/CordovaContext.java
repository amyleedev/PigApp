package com.szmy.pigapp.myshop;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CordovaContext extends ContextWrapper implements CordovaInterface {
	Activity activity;
	protected CordovaPlugin activityResultCallback;
	protected int activityResultRequestCode;
	protected final ExecutorService threadPool = Executors
			.newCachedThreadPool();

	public CordovaContext(Activity activity,
			CordovaPlugin activityResultCallback, int activityResultRequestCode) {
		super(activity.getBaseContext());
		this.activity = activity;
		this.activityResultCallback = activityResultCallback;
		this.activityResultRequestCode = activityResultRequestCode;
	}

	@Override
	public void setActivityResultCallback(CordovaPlugin plugin) {
		if (activityResultCallback != null) {
			activityResultCallback.onActivityResult(activityResultRequestCode,
					Activity.RESULT_CANCELED, null);
		}
		this.activityResultCallback = plugin;

	}

	@Override
	public void startActivityForResult(CordovaPlugin command, Intent intent,
			int requestCode) {
		setActivityResultCallback(command);
		try {
			startActivityForResult(command, intent, requestCode);
		} catch (RuntimeException e) {
			activityResultCallback = null;
			throw e;
		}
	}

	@Override
	public Activity getActivity() {
		return activity;
	}

	@Override
	public Object onMessage(String s, Object o) {
		return null;
	}

	@Override
	public ExecutorService getThreadPool() {
		return threadPool;
	}
}
