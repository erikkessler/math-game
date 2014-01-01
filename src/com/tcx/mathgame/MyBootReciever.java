package com.tcx.mathgame;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class MyBootReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Calendar cal = Calendar.getInstance();
	    // start 30 seconds after boot completed
	    cal.add(Calendar.SECOND, 5);
	    
		SharedPreferences prefs= context.getSharedPreferences("MyPrefsFile",0);
        Boolean go = prefs.getBoolean("restrictedMode", false);
        
        if( go ) {
        	MyScheduleReciver reciever = new MyScheduleReciver();
        	Log.d("A", "Boot Started Reciver");
		
			IntentFilter filter = new IntentFilter();
			filter.addAction( Intent.ACTION_USER_BACKGROUND );
			filter.addAction( Intent.ACTION_USER_FOREGROUND );
//			context.registerReceiver( reciever, filter );
//			
//			Intent i = new Intent("tcx.YOLO");
//			Bundle extras = new Bundle();  
//	        extras.putBoolean("start_now", prefs.getBoolean("restrictedMode", false) );  
//	        i.putExtras(extras);  
//			context.sendBroadcast(i);
			
        }
		
	}

}
