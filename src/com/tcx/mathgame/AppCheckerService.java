package com.tcx.mathgame;


import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class AppCheckerService extends Service {
	
	private final IBinder mBinder = new MyBinder();
	private String homeName;
//	private String oldPackage = "";
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if( homeName == null) {
			getDefaultLauncher( this );
		}
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

        ComponentName componentInfo = taskInfo.get(0).topActivity;                      
        String currentActivityName=componentInfo.getClassName();
        String packageName=componentInfo.getPackageName();
//        if(!packageName.equals(oldPackage))
        	//Toast.makeText( this.getApplicationContext() , packageName, Toast.LENGTH_SHORT).show();
        
//        oldPackage = packageName;
        if((!packageName.equals("com.tcx.mathgame") && (!packageName.equals( homeName))) && (!packageName.equals("fr.lehovetzki.ABMath"))) {
        	Toast.makeText( this.getApplicationContext() , "Hey! Do your math!", Toast.LENGTH_SHORT).show();
        	Intent startMain = new Intent(Intent.ACTION_MAIN);
        	startMain.addCategory(Intent.CATEGORY_HOME);
        	startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(startMain);
        }
		
		return Service.START_NOT_STICKY;
	}
	
	public void getDefaultLauncher(Context context) {
		Intent homeIntent= new Intent(Intent.ACTION_MAIN);
		homeIntent.addCategory(Intent.CATEGORY_HOME);
		ResolveInfo defaultLauncher= getPackageManager().resolveActivity(homeIntent, PackageManager.MATCH_DEFAULT_ONLY);
		homeName= defaultLauncher.activityInfo.packageName;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	public class MyBinder extends Binder {
		AppCheckerService getService(){
			return AppCheckerService.this;
		}
	}
	

		
	}


