package com.tcx.mathgame;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceManager;
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
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		
				
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
	
	private CharSequence fromSet(Set<String> stringSet) {
		String result = "";
		Object[] stringArray = stringSet.toArray();
		for(int i = 0; i < stringArray.length; i++){
			if(i == 0)
				result = (String) stringArray[0];
			else
				result = result + ", " + stringArray[i];
		}
		return result;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Set<String> set = new HashSet<String>();
		
		set.add("Addition");
        typers.setText(fromSet(prefs.getStringSet("pref_key_game_type", set )));
				
		if(!MyScheduleReciver.hasUser()) {
			
			reciever = new MyScheduleReciver();
			IntentFilter filter = new IntentFilter();
			filter.addAction( Intent.ACTION_USER_BACKGROUND );
			filter.addAction( Intent.ACTION_USER_FOREGROUND );
			registerReceiver( reciever, filter );
			Log.d("YOLO", "Main Added Things");
			MyScheduleReciver.setUser();
		}
				
					
		if (prefs.getBoolean("pref_key_restricted_mode", false)) {
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
