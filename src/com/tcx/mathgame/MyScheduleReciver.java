package com.tcx.mathgame;

import java.util.Calendar;
import java.util.List;

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
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MyScheduleReciver extends BroadcastReceiver{

	private static final long REPEAT_TIME = 1000 * 2;
	private static Context mContext;
	private static PendingIntent mPending;
	private static boolean started;
	private boolean go;
	private static NotificationManager mNotifyMgr;
	private boolean paused;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		boolean userSentBackground = intent.getAction().equals( Intent.ACTION_USER_BACKGROUND );
        boolean userSentForeground = intent.getAction().equals( Intent.ACTION_USER_FOREGROUND );
        Log.d( "Frisck", "Switch received. User sent background = " + userSentBackground + "; User sent foreground = " + userSentForeground + ";" );

        if (isStarted() && userSentBackground)
        	stopReciver();
        
        SharedPreferences prefs= context.getSharedPreferences("MyPrefsFile",0);
        go = prefs.getBoolean("restrictedMode", false);	
        
        if (go && userSentForeground) {
        	mContext = context;
			startReceiver();
        }
        	
        int user = intent.getExtras().getInt( "android.intent.extra.user_handle" );
        Log.d( "Frick", "user = " + user );
		
		
		Log.d("R", "Recieved Command");
		if((!started) && go) {
			mContext = context;
			startReceiver(); 
		}
	}
	
	public static void startReceiver() {
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(mContext)
			    .setSmallIcon(R.drawable.ic_stat_device_access_secure)
			    .setContentTitle("Restriced Mode Enabled")
			    .setContentText("Click to Change Settings");
		
		Intent resultIntent = new Intent(mContext, HomeScreen.class);
		// Because clicking the notification opens a new ("special") activity, there's
		// no need to create an artificial back stack.
		PendingIntent resultPendingIntent = PendingIntent.getActivity( mContext,0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mNotifyMgr = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(001, mBuilder.build());
	    
		started = true;
		Log.d("R", "Reciever Started");
		
		AlarmManager service = (AlarmManager) mContext
		        .getSystemService(Context.ALARM_SERVICE);
		
		Intent i = new Intent(mContext, MyStartServiceReceiver.class);
	    mPending = PendingIntent.getBroadcast(mContext, 0, i,
	        PendingIntent.FLAG_CANCEL_CURRENT);
	    
	    Calendar cal = Calendar.getInstance();
	    // start 30 seconds after boot completed
	    cal.add(Calendar.SECOND, 5);
	    
	    service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
	            cal.getTimeInMillis(), REPEAT_TIME, mPending);
	    
	    
		
		
	}
	
	public void stopReciver() {
		mNotifyMgr.cancel(001);
		Log.d("R", "Stop Reciver");
		AlarmManager service = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		service.cancel(mPending);
	}
	
	public void resumeReciver() {
		paused = false;
		startReceiver();
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public void pauseReciever(int time) {
		paused = true;
		stopReciver();
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(mContext)
			    .setSmallIcon(R.drawable.ic_stat_device_access_not_secure)
			    .setContentTitle("Restricted Mode Paused")
			    .setContentText("You have " + time + " minutes to play");
		mNotifyMgr.notify(002, mBuilder.build());
		
		Runnable runny = new Runnable() {
		    public void run() {
		    	resumeReciver();
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
	

}
