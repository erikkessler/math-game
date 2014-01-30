package com.tcx.mathgame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UserReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals(Intent.ACTION_USER_BACKGROUND)){
			Log.d("YOLO", "USER BACKGROUND");
			Utils.userPresent = false;
		} else if(intent.getAction().equals(Intent.ACTION_USER_FOREGROUND)) {
			Log.d("YOLO", "USER FOREGROUND");
			Utils.userPresent = true;
		}
		
	}

}
