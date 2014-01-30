package com.tcx.mathgame;

import java.util.Calendar;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MyScheduleReciver extends BroadcastReceiver {

	private static final long REPEAT_TIME = 1000 * 2;
	private static Context mContext;
	private static PendingIntent mPending;
	private boolean go;
	private static NotificationManager mNotifyMgr;
	private SharedPreferences prefs;
	private int minutesLeft;
	private NotificationCompat.Builder mBuilder;

	@Override
	public void onReceive(Context context, Intent intent) {

		mContext = context;

		prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		go = prefs.getBoolean("pref_key_restricted_mode", false);
		
		//Boolean paused = prefs.getBoolean("paused", false);
		
		

		if (intent.getAction().equals("tcx.START") && !Utils.paused) {
			Log.d("YOLO", "Reciever starting");
			startReceiver();

		} else if (intent.getAction().equals("tcx.STOP") && mPending != null
				&& !Utils.paused) {
			Log.d("YOLO", "Reciever stopping");
			stopReciver();

		} else if (intent.getAction().equals("tcx.PAUSE") && !Utils.paused) {

			Log.d("YOLO", "Reciever pausing");
			//prefs.edit().putBoolean("paused", true).commit();
			Utils.paused = true;
			pauseReciever(intent.getIntExtra("time", 15));

		}  else if ( intent.getAction().equals("tcx.END_PAUSE")) {
			Log.d("YOLO", "Ending Time");
			//prefs.edit().putBoolean("paused", false).commit();
			Utils.paused = false;
			mNotifyMgr.cancel(002);
			if(go)
				startReceiver();
		}
	}

	private void startReceiver() {

		//prefs.edit().putBoolean("paused", false).commit();
		Utils.paused = false;

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mContext).setSmallIcon(R.drawable.ic_stat_device_access_secure)
				.setContentTitle("Restriced Mode Enabled")
				.setContentText("Click to Change Settings");

		Intent resultIntent = new Intent(mContext, HomeScreen.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
				0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mNotifyMgr = (NotificationManager) mContext
				.getSystemService(mContext.NOTIFICATION_SERVICE);
		mNotifyMgr.notify(001, mBuilder.build());

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
		AlarmManager service = (AlarmManager) mContext
				.getSystemService(Context.ALARM_SERVICE);
		service.cancel(mPending);

	}

	private void pauseReciever(int time) {
		minutesLeft = time;
		stopReciver();
		mBuilder = new NotificationCompat.Builder(mContext)
				.setSmallIcon(R.drawable.ic_stat_device_access_not_secure)
				.setContentTitle("Restricted Mode Paused")
				.setContentText("You have " + time + " minutes to play");
		mNotifyMgr.notify(002, mBuilder.build());

		Runnable runny = new Runnable() {
			public void run() {
				minutesLeft--;
				//if (minutesLeft == 0 && prefs.getBoolean("paused", false)) {
				if (minutesLeft == 0 && Utils.paused) {
					mNotifyMgr.cancel(002);
					//prefs.edit().putBoolean("paused", false).commit();
					Utils.paused = false;
					if (prefs.getBoolean("pref_key_restricted_mode", false)) {
						Log.d("YOLO", "Here Boy!");
						startReceiver();
						ActivityManager am = (ActivityManager) mContext
								.getSystemService(Context.ACTIVITY_SERVICE);

						List<RunningTaskInfo> runningProcInfo = am
								.getRunningTasks(1);

						String packageName = runningProcInfo.get(0).topActivity
								.getPackageName();

						Log.d("YOLO", packageName);

						if ((!packageName.equals("com.tcx.mathgame"))) {
							Intent startMain = new Intent(Intent.ACTION_MAIN);
							startMain.addCategory(Intent.CATEGORY_HOME);
							startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							mContext.startActivity(startMain);
						}
						Toast.makeText(mContext, "Time's up buster!",
								Toast.LENGTH_SHORT).show();
					}
				//} else if (prefs.getBoolean("paused", false)) {
				} else if (Utils.paused) {
					mBuilder.setContentText("You have " + minutesLeft
							+ " minutes left to play");
					mNotifyMgr.notify(002, mBuilder.build());
					Handler handle = new Handler();
					handle.postDelayed(this, 60000);
				}

			}
		};
		Handler handle = new Handler();
		handle.postDelayed(runny, 60000);

	}

}
