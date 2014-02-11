package com.tcx.chester.mathgame;

import java.util.Calendar;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MyScheduleReciver extends BroadcastReceiver {

	private static final long REPEAT_TIME = 1000 * 2;
	private static Context mContext;
	private static PendingIntent mPending, mPending2;
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

		if (intent.getAction().equals("tcx.START") && !Utils.paused) {
			Log.d("YOLO", "Reciever starting");
			startReceiver();

		} else if (intent.getAction().equals("tcx.STOP") && mPending != null
				&& !Utils.paused) {
			Log.d("YOLO", "Reciever stopping");
			stopReciver();

		} else if (intent.getAction().equals("tcx.PAUSE") ) {
			Log.d("YOLO", "Reciever pausing");
			Utils.paused = true;
			pauseReciever(intent.getIntExtra("time", 15));

		} else if (intent.getAction().equals("tcx.END_PAUSE")) {
			Log.d("YOLO", "Ending Reward Time");
			Utils.paused = false;
			if (mNotifyMgr != null)
				mNotifyMgr.cancel(002);
			if (go)
				startReceiver();

		} else if (intent.getAction().equals("tcx.RUN")) {
			runMinute(intent.getExtras().getInt("minutesLeft"));
			
		} else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") && go) {
			startReceiver();
		}
	}

	@TargetApi(19)
	private void runMinute(int timeL) {
		mBuilder = new NotificationCompat.Builder(mContext).setSmallIcon(
				R.drawable.ic_stat_device_access_not_secure).setContentTitle(
				"Restricted Mode Paused");
		minutesLeft = timeL;
		minutesLeft--;

		if (minutesLeft == 0 && Utils.paused) {
			AlarmManager service = (AlarmManager) mContext
					.getSystemService(Context.ALARM_SERVICE);
			service.cancel(mPending2);
			mNotifyMgr.cancel(002);
			Utils.paused = false;
			if (prefs.getBoolean("pref_key_restricted_mode", false)) {
				startReceiver();
				ActivityManager am = (ActivityManager) mContext
						.getSystemService(Context.ACTIVITY_SERVICE);

				List<RunningTaskInfo> runningProcInfo = am.getRunningTasks(1);

				String packageName = runningProcInfo.get(0).topActivity
						.getPackageName();

				if ((!packageName.equals("com.tcx.chester.mathgame"))) {
					Intent startMain = new Intent(Intent.ACTION_MAIN);
					startMain.addCategory(Intent.CATEGORY_HOME);
					startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(startMain);
				}
				Toast.makeText(mContext, "Time's up!", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (Utils.paused) {
			mBuilder.setContentText("You have " + minutesLeft
					+ " minutes left to play");
			mNotifyMgr.notify(002, mBuilder.build());
			AlarmManager service = (AlarmManager) mContext
					.getSystemService(Context.ALARM_SERVICE);
			Intent i = new Intent(mContext, MyStartServiceReceiver.class);
			i.putExtra("type", 1);
			i.putExtra("minutesLeft", minutesLeft);
			PendingIntent mPending2 = PendingIntent.getBroadcast(mContext, 0,
					i, PendingIntent.FLAG_CANCEL_CURRENT);

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, 1);

			if (Utils.isKitKatOrLater())
				service.setExact(AlarmManager.RTC_WAKEUP,
						cal.getTimeInMillis(), mPending2);
			else
				service.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
						mPending2);

		} else {
			AlarmManager service = (AlarmManager) mContext
					.getSystemService(Context.ALARM_SERVICE);
			service.cancel(mPending2);
		}
	}

	private void startReceiver() {
		Utils.paused = false;

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mContext).setSmallIcon(R.drawable.ic_stat_device_access_secure)
				.setContentTitle("Restricted Mode Enabled")
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
		i.putExtra("type", 0);
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

	@TargetApi(19)
	private void pauseReciever(int time) {
		minutesLeft = time;
		stopReciver();
		mBuilder = new NotificationCompat.Builder(mContext)
				.setSmallIcon(R.drawable.ic_stat_device_access_not_secure)
				.setContentTitle("Restricted Mode Paused")
				.setContentText("You have " + time + " minutes to play");
		mNotifyMgr.notify(002, mBuilder.build());
		AlarmManager service = (AlarmManager) mContext
				.getSystemService(Context.ALARM_SERVICE);

		Intent i = new Intent(mContext, MyStartServiceReceiver.class);
		i.putExtra("type", 1);
		i.putExtra("minutesLeft", time);
		mPending2 = PendingIntent.getBroadcast(mContext, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 1);
		if (Utils.isKitKatOrLater()) {
			service.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
					mPending2);

		} else
			service.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
					mPending2);

	}
}
