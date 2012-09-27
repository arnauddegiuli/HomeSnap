package com.adgsoftware.mydomo.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyDomoServiceImpl extends Service {

	private MyDomoBinder binder = new MyDomoBinder();
	
	@Override
	public void onCreate() {
		super.onCreate();
		try {
			binder.onCreate(getApplication());
		} catch (Exception e) {
			Log.e("Server connection", "Impossible to connect", e);
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		binder.onDestroy();
	}
}
