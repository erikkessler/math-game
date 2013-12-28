package com.tcx.mathgame;

import android.os.Binder;

public class ServiceBinder extends Binder {
	
	private ServiceMethod mService;
	
	public ServiceBinder(ServiceMethod service) {
		super();
		mService = service;
	}
	


	public ServiceMethod getService() {
		return mService;
	
	}



	public interface ServiceMethod {
		public String getDataFromService();
		public void setDataToService(String data);
	
	}

}