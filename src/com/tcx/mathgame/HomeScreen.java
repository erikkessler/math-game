package com.tcx.mathgame;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HomeScreen extends Activity {
	
	private TextView typers;
	static MyScheduleReciver reciever;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homescreen);
		prefs = getSharedPreferences("MyPrefsFile",0);
		
		
				
		typers = (TextView) findViewById(R.id.tv_type);
		
		
		Button newGame= (Button)findViewById(R.id.button1);
		newGame.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity( intent );
				
			}
		});
		
		Button settings = (Button)findViewById(R.id.button2);
		settings.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivity( intent );
				
			}
		});
		
		Button xtra = (Button)findViewById(R.id.button3);
		xtra.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ExtraMath.class);
				startActivity( intent );
				
			}
		});
		
		Button history = (Button)findViewById(R.id.button4);
		history.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), GameHistory.class);
				startActivity( intent );
				
			}
		});
		
	
				
				
				
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		prefs = getSharedPreferences("MyPrefsFile",0);
		
		int[] probTypes = new int[4];
		int numbTypes = 0;
		boolean[] checked = { prefs.getBoolean("0Checked", false), prefs.getBoolean("1Checked", false) , prefs.getBoolean("2Checked", false),prefs.getBoolean("3Checked", false)};
		for( int i = 0; i < checked.length; i++) {
			if( checked[i] ) {
				probTypes[numbTypes] = i;
				numbTypes++;
			}
		}
		
		// In case none is selected
		if ( numbTypes == 0 ) {
			probTypes[0] = 0;
			numbTypes = 1;
		}
		
		// Get Types
				String types = "";
				for( int i = 0; i < numbTypes; i++) {
					if( i != 0 ) {
						types = types + ", ";
					}
					
					switch ( probTypes[i]) {
						case 0:
							types = types + "Addition";
							break;
						case 1:
							types = types + "Subtraction";
							break;
						case 2:
							types = types + "Multipication";
							break;
						case 3:
							types = types + "Division";
							break;
					}
					
				}
				
				typers.setText(types);
				
				if(!MyScheduleReciver.hasUser()) {
					
					reciever = new MyScheduleReciver();
					IntentFilter filter = new IntentFilter();
					filter.addAction( Intent.ACTION_USER_BACKGROUND );
					filter.addAction( Intent.ACTION_USER_FOREGROUND );
					registerReceiver( reciever, filter );
					Log.d("YOLO", "Main Added Things");
					MyScheduleReciver.setUser();
				}
				
				
//				Intent i = new Intent("tcx.THERE");
//				Bundle extras = new Bundle();   
//		        i.putExtras(extras);  
//				sendBroadcast(i);
//				Log.d("YOLO", "Sent finding intent");
//				
//				Runnable runs = new Runnable() {
//					
//					@Override
//					public void run() {
//						
//						if( prefs.getBoolean("there", false)) {
//							prefs.edit().putBoolean("there", false);
//							prefs.edit().commit();
//							Log.d("YOLO", "It was there");
//							
//						} else {
//							reciever = new MyScheduleReciver();
//							IntentFilter filter = new IntentFilter();
//							filter.addAction( Intent.ACTION_USER_BACKGROUND );
//							filter.addAction( Intent.ACTION_USER_FOREGROUND );
//							registerReceiver( reciever, filter );
//							Log.d("YOLO", "Main Started Reciver");
//						}
//						
//					}
//				};
//				
//				Handler handler = new Handler();
//				handler.postDelayed(runs, 2000);
				
//				if( reciever == null) {
//					Log.d("A", "Reciver was null");
//					reciever = new MyScheduleReciver();
//					
//					IntentFilter filter = new IntentFilter();
//					filter.addAction( Intent.ACTION_USER_BACKGROUND );
//					filter.addAction( Intent.ACTION_USER_FOREGROUND );
//					registerReceiver( reciever, filter );
//			}
//			
//			
//			boolean isNew = false;
//			if(! reciever.isStarted()) {
//				isNew = true;
//				Intent i = new Intent("tcx.YOLO");
//				Bundle extras = new Bundle();  
//		        extras.putBoolean("start_now", false );  
//		        i.putExtras(extras);  
//				sendBroadcast(i);
//				Log.d("A", "Main Started Reciver");
//				
//				
//			}
//			
			if (prefs.getBoolean("restrictedMode", false)) {
				startService(new Intent(HomeScreen.this,AppCheckerService.class));
				Intent i = new Intent("tcx.START");
				Bundle extras = new Bundle();   
		        i.putExtras(extras);  
				sendBroadcast(i);
				Log.d("YOLO", "On Resume tried to start service");
				
				
			}else {
				stopService(new Intent(HomeScreen.this,AppCheckerService.class));
				Intent i = new Intent("tcx.STOP");
				Bundle extras = new Bundle();   
		        i.putExtras(extras);  
				sendBroadcast(i);
				Log.d("YOLO", "On Resume tried to stop service");
			}
	}
	
	

}
