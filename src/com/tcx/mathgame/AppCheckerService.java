package com.tcx.mathgame;

import com.tcx.mathgame.ServiceBinder.ServiceMethod;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AppCheckerService extends Service implements ServiceMethod {
	
	private IBinder mBinder;
	private String mData;
	
	public void onCreate() {
		mBinder = new ServiceBinder(this);
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public String getDataFromService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDataToService(String data) {
		// TODO Auto-generated method stub
		
	}

}
