package com.tcx.mathgame;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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
	
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences prefs= context.getSharedPreferences("MyPrefsFile",0);
        go = prefs.getBoolean("restrictedMode", true);	
		Log.d("R", "Recieved Command");
		if((!started) && go) {
			mContext = context;
			startReceiver(); 
		}
	}
	
	public static void startReceiver() {
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(mContext)
			    .setSmallIcon(R.drawable.ic_launcher)
			    .setContentTitle("Restriced Mode Enabled")
			    .setContentText("Click to Change Settings");
		
		Intent resultIntent = new Intent(mContext, Settings.class);
		// Because clicking the notification opens a new ("special") activity, there's
		// no need to create an artificial back stack.
		PendingIntent resultPendingIntent =PendingIntent.getActivity( mContext,0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
		startReceiver();
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public void pauseReciever(int time) {
		stopReciver();
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(mContext)
			    .setSmallIcon(R.drawable.ic_launcher)
			    .setContentTitle("Restricted Mode Paused")
			    .setContentText("You have " + time + " minutes to play");
		mNotifyMgr.notify(002, mBuilder.build());
		
		Runnable runny = new Runnable() {
		    public void run() {
		    	resumeReciver();
			    mNotifyMgr.cancel(002); 
			    Intent startMain = new Intent(Intent.ACTION_MAIN);
	        	startMain.addCategory(Intent.CATEGORY_HOME);
	        	startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	mContext.startActivity(startMain);
			    Toast.makeText( mContext, "Time's up buster!", Toast.LENGTH_SHORT).show();
		    }
		};
		Handler handle = new Handler();
		handle.postDelayed(runny, time * 1000);
	    
	    
		
	}

}
