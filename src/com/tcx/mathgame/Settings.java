package com.tcx.mathgame;

import android.app.*;
import android.view.View.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.content.*;
import com.tcx.mathgame.R;

public class Settings extends Activity implements OnClickListener, CompoundButton.OnCheckedChangeListener
{

	@Override
	public void onCheckedChanged(CompoundButton p1, boolean p2)
	{
		editor.putBoolean( "restrictedMode", p2 );
		editor.commit();
		if( p2 ) {
			TaskerIntent i = new TaskerIntent( "TOGGLE_PROF");
			i.addAction(ActionCodes.TOGGLE_PROFILE).addArg("Only Math").addArg(1);
			sendBroadcast(i);
		} else {
			TaskerIntent i = new TaskerIntent( "TOGGLE_PROF");
			i.addAction(ActionCodes.TOGGLE_PROFILE).addArg("Only Math").addArg(0);
			sendBroadcast(i);
			
		}
	}
	

	private final String PASS = "ronnieboy";
	private TextView password, gLenth, eTime, cNeeded;
	public static final String PREFS_NAME = "MyPrefsFile";
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	private Switch rOn;
	private Button gType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView( R.layout.settingpass );
		
		// Login screen
		password = (TextView) findViewById(R.id.settingPass);
		Button submit = (Button) findViewById(R.id.settingpassButton1);
		submit.setOnClickListener(this);
		
		
		
	}

	@Override
	protected void onPause()
	{
		editor.putString("gameLength", gLenth.getText().toString());
		editor.putString("earnedTime", eTime.getText().toString());
		editor.putString("correctNeeded", cNeeded.getText().toString());
		editor.putString("gameType", gType.getText().toString());
		editor.commit();
		super.onPause();
	}
	

	@Override
	public void onClick(View p1)
	{
		if( p1.getId() == R.id.settingpassButton1 ){
			if( password.getText().toString().equals(PASS) ){
				
				setContentView(R.layout.settings);
				
				// Actual settings
				rOn = (Switch) findViewById( R.id.gOn );
				rOn.setOnCheckedChangeListener( this );
				gLenth = (TextView) findViewById(R.id.gTime);
				eTime = (TextView) findViewById(R.id.eTime);
				cNeeded = (TextView) findViewById(R.id.cNeed);
				gType = (Button) findViewById(R.id.gType);


				prefs = getSharedPreferences(PREFS_NAME, 0);
				editor = prefs.edit();
				rOn.setChecked( prefs.getBoolean( "restrictedMode", false));
				gLenth.setText( prefs.getString("gameLength", "25"));
				eTime.setText( prefs.getString("earnedTime", "15"));
				cNeeded.setText( prefs.getString("correctNeeded", "25"));
				gType.setText( prefs.getString("gameType", "Subtraction" ));
				gType.setOnClickListener(this);
				
			} else{
				password.setText("");
			}
			
		} else{
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			final CharSequence items[] = new CharSequence[] {"Addition", "Subtraction", "Division", "Multiplication"};
			adb.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						gType.setText(items[p2]);
					}
					
				
			}
			
			);
			adb.setNegativeButton("Close", null);
			adb.setTitle("Which one?");
			adb.show();
		}
	}
	
}
