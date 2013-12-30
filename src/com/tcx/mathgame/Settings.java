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

	
	

	private final String PASS = "ronnieboy";
	private TextView password, gLenth, eTime, cNeeded;
	public static final String PREFS_NAME = "MyPrefsFile";
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	private Switch rOn;
	private Button gType;
	private boolean settings;
	private TextView[] ranges = new TextView[8];
	
	@Override
	public void onCheckedChanged(CompoundButton p1, boolean p2)
	{
		editor.putBoolean( "restrictedMode", p2 );
		editor.commit();
//		if( p2 ) {
//			TaskerIntent i = new TaskerIntent( "TOGGLE_PROF");
//			i.addAction(ActionCodes.TOGGLE_PROFILE).addArg("Only Math").addArg(1);
//			sendBroadcast(i);
//		} else {
//			TaskerIntent i = new TaskerIntent( "TOGGLE_PROF");
//			i.addAction(ActionCodes.TOGGLE_PROFILE).addArg("Only Math").addArg(0);
//			sendBroadcast(i);
//			
//		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {

	    menu.add(1, 0, 0, "Save Settings").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	
	    return super.onCreateOptionsMenu(menu); 
    }
	
	 @Override
    public boolean onOptionsItemSelected(MenuItem item) {

		 	onBackPressed();
	    	return super.onOptionsItemSelected(item);
		}
	
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
		settings = false;
		
		
		
	}

	@Override
	protected void onPause()
	{
		if (settings ) {
			editor.putString("gameLength", gLenth.getText().toString());
			editor.putString("earnedTime", eTime.getText().toString());
			editor.putString("correctNeeded", cNeeded.getText().toString());
			//editor.putString("gameType", gType.getText().toString());
			int[] rVs =  {Integer.parseInt(ranges[0].getText().toString()),Integer.parseInt(ranges[1].getText().toString()),
								Integer.parseInt(ranges[2].getText().toString()),Integer.parseInt(ranges[3].getText().toString()),
								Integer.parseInt(ranges[4].getText().toString()),Integer.parseInt(ranges[5].getText().toString()),
								Integer.parseInt(ranges[6].getText().toString()),Integer.parseInt(ranges[7].getText().toString())};
			
			if( rVs[0] < rVs[1] && rVs[2] < rVs[3] && rVs[4] < rVs[5] && rVs[6] < rVs[7]) {
				for( int i = 0; i < rVs.length; i++)
					editor.putString("range"+i, rVs[i] + "");
				
			}
			editor.commit();
		}
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
				for( int i=0; i < ranges.length; i++ ) {
					String a = "r" + i;
					ranges[i] = (TextView) findViewById(getResources().getIdentifier(a, "id", getPackageName()));
				}


				prefs = getSharedPreferences(PREFS_NAME, 0);
				editor = prefs.edit();
				rOn.setChecked( prefs.getBoolean( "restrictedMode", false));
				gLenth.setText( prefs.getString("gameLength", "25"));
				eTime.setText( prefs.getString("earnedTime", "15"));
				cNeeded.setText( prefs.getString("correctNeeded", "25"));
				//gType.setText( prefs.getString("gameType", "Subtraction" ));
				
				for( int i = 0; i < ranges.length; i++){
					if( i % 2 == 0)
						ranges[i].setText( prefs.getString( "range" + i, "0"));
					else
						ranges[i].setText( prefs.getString( "range" + i, "12"));
				}
				
				gType.setOnClickListener(this);
				
				settings = true;
				
			} else{
				password.setText("");
			}
			
		} else{
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			final CharSequence items[] = new CharSequence[] {"Addition", "Subtraction", "Multiplication", "Division"};
			boolean[] checked = { prefs.getBoolean("0Checked", false), prefs.getBoolean("1Checked", false) , prefs.getBoolean("2Checked", false),prefs.getBoolean("3Checked", false)};
			/*adb.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						gType.setText(items[p2]);
					}
					
				
			}
			
			);*/
			adb.setMultiChoiceItems(items, checked, new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					editor.putBoolean(which + "Checked", isChecked);
					editor.commit();
					
				}
			} );
			adb.setNegativeButton("Ok", null);
			adb.setTitle("Which ones?");
			adb.show();
		}
	}
	
}
