package com.tcx.mathgame;

import java.util.Calendar;
import java.util.List;

import android.R.bool;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MyScheduleReciver extends BroadcastReceiver{

	private static final long REPEAT_TIME = 1000 * 2;
	private static Context mContext;
	private static PendingIntent mPending;
	private boolean started;
	private boolean go;
	private static NotificationManager mNotifyMgr;
	private static boolean hasUserChange;
	private SharedPreferences prefs;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		mContext = context;
		
		prefs= PreferenceManager.getDefaultSharedPreferences(mContext);
		go = prefs.getBoolean("pref_key_restriced_mode", false);	
		Boolean paused = prefs.getBoolean("paused", false);
		
		if( intent.getAction().equals("tcx.START") &&!paused ) {
			Log.d("YOLO", "Reciever starting");
			startReceiver();
			
		} else if( intent.getAction().equals("tcx.STOP") && mPending != null && !paused ){
			Log.d("YOLO", "Reciever stopping");
			stopReciver();
			
		}else if(intent.getAction().equals("tcx.PAUSE") && !paused) {
			
			Log.d("YOLO", "Reciever pausing");
			prefs.edit().putBoolean("paused", true).commit();
			pauseReciever(intent.getIntExtra("time", 15));
			
		} else if( (intent.getAction().equals( Intent.ACTION_USER_BACKGROUND ) || intent.getAction().equals( Intent.ACTION_USER_FOREGROUND )) && !paused ) {
			boolean userSentBackground = intent.getAction().equals( Intent.ACTION_USER_BACKGROUND );
	        boolean userSentForeground = intent.getAction().equals( Intent.ACTION_USER_FOREGROUND );
	        Log.d( "YOLO", "Switch received. User sent background = " + userSentBackground + "; User sent foreground = " + userSentForeground + ";" );
	
	        if (started && userSentBackground)
	        	stopReciver();
	        
	        
	        if (go && userSentForeground) {
	        	mContext = context;
				startReceiver();
	        }
        	
 
		
		
		}
	}
	
	private void startReceiver() {
		
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(mContext)
			    .setSmallIcon(R.drawable.ic_stat_device_access_secure)
			    .setContentTitle("Restriced Mode Enabled")
			    .setContentText("Click to Change Settings");
		
		Intent resultIntent = new Intent(mContext, HomeScreen.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity( mContext,0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mNotifyMgr = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
		mNotifyMgr.notify(001, mBuilder.build());
	    
		started = true;
		Log.d("R", "Reciever Started");
		
		AlarmManager service = (AlarmManager) mContext
		        .getSystemService(Context.ALARM_SERVICE);
		
		Intent i = new Intent(mContext, MyStartServiceReceiver.class);
	    mPending = PendingIntent.getBroadcast(mContext, 0, i,
	        PendingIntent.FLAG_CANCEL_CURRENT);
	    
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.SECOND, 5);
	    
	    service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
	            cal.getTimeInMillis(), REPEAT_TIME, mPending);
	    
	    
		
		
	}
	
	private void stopReciver() {
		mNotifyMgr.cancel(001);
		Log.d("R", "Stop Reciver");
		AlarmManager service = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		service.cancel(mPending);
	}
	
	
	private void pauseReciever(int time) {
		stopReciver();
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(mContext)
			    .setSmallIcon(R.drawable.ic_stat_device_access_not_secure)
			    .setContentTitle("Restricted Mode Paused")
			    .setContentText("You have " + time + " minutes to play");
		mNotifyMgr.notify(002, mBuilder.build());
		
		Runnable runny = new Runnable() {
		    public void run() {
		    	prefs.edit().putBoolean("paused", false).commit();
		    	startReceiver();
			    mNotifyMgr.cancel(002); 
			    ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

				List<RunningAppProcessInfo> runningProcInfo = am.getRunningAppProcesses();
				
				        Log.d("YOLO", runningProcInfo.get(0).processName);
				       String packageName =  runningProcInfo.get(0).processName;
		        
		        if(!packageName.equals("com.tcx.mathgame")){
				    Intent startMain = new Intent(Intent.ACTION_MAIN);
		        	startMain.addCategory(Intent.CATEGORY_HOME);
		        	startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        	mContext.startActivity(startMain);
		        }
			    Toast.makeText( mContext, "Time's up buster!", Toast.LENGTH_SHORT).show();
		    }
		};
		Handler handle = new Handler();
		handle.postDelayed(runny, time * 60000);
	    
	    
		
	}
	
	static boolean hasUser(){
		return hasUserChange;
	}
	
	static void setUser(){
		hasUserChange = true;
		Log.d("YOLO", "Got 'em");
	}
	

}
