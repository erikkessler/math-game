package com.tcx.chester.mathgame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MyStartServiceReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getExtras().getInt("type") == 0) {
			Intent service = new Intent(context, AppCheckerService.class);
			context.startService(service);

		} else if (intent.getExtras().getInt("type") == 1) {
			Intent i = new Intent("tcx.RUN");
			Bundle extras = new Bundle();
			extras.putInt("minutesLeft", intent.getExtras().getInt("minutesLeft"));
			i.putExtras(extras);
			context.sendBroadcast(i);
		}
		
	}

}
