package com.tcx.mathgame;

import android.app.*;
import android.util.Log;
import android.view.View.*;
import android.view.inputmethod.InputMethodManager;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.content.*;

import com.tcx.mathgame.R;

public class Settings extends Activity implements OnClickListener, CompoundButton.OnCheckedChangeListener
{

	
	

	private String pass;
	private TextView password, gLenth, eTime, cNeeded;
	public static final String PREFS_NAME = "MyPrefsFile";
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	private Switch rOn;
	private Button gType;
	private boolean settings;
	private TextView[] ranges = new TextView[8];
	private EditText current, newPass;
	
	@Override
	public void onCheckedChanged(CompoundButton p1, boolean p2)
	{
		editor.putBoolean( "restrictedMode", p2 );
		editor.commit();

	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {

	    menu.add(1, 0, 0, "Save Settings").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	
	    return super.onCreateOptionsMenu(menu); 
    }
	
	 @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		 	onPause();
		 	onBackPressed();
	    	return super.onOptionsItemSelected(item);
		}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView( R.layout.settingpass );
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// Login screen
		password = (TextView) findViewById(R.id.settingPass);
		Button submit = (Button) findViewById(R.id.settingpassButton1);
		submit.setOnClickListener(this);
		settings = false;
		
		prefs = getSharedPreferences(PREFS_NAME, 0);
		editor = prefs.edit();
		
		
		
	}

	@Override
	protected void onPause()
	{
		if (settings ) {
			
			editor.putString("gameLength", gLenth.getText().toString());
			editor.putString("earnedTime", eTime.getText().toString());
			editor.putString("correctNeeded", cNeeded.getText().toString());
		
			editor.commit();
		}
		super.onPause();
		finish();
	}
	

	@Override
	public void onClick(View p1)
	{
		if( p1.getId() == R.id.settingpassButton1 ){
			if( password.getText().toString().equals(prefs.getString("password", "admin")) ){
				
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				
				setContentView(R.layout.settings);
				
				// Actual settings
				rOn = (Switch) findViewById( R.id.gOn );
				rOn.setOnCheckedChangeListener( this );
				gLenth = (TextView) findViewById(R.id.gTime);
				eTime = (TextView) findViewById(R.id.eTime);
				cNeeded = (TextView) findViewById(R.id.cNeed);
				gType = (Button) findViewById(R.id.gType);
				Button change = (Button) findViewById(R.id.pPass);
				change.setOnClickListener(this);
				
				Button aDiff = (Button) findViewById(R.id.aRan);
				aDiff.setOnClickListener(this);
				
				Button sDiff = (Button) findViewById(R.id.sRan);
				sDiff.setOnClickListener(this);
				
				Button mDiff = (Button) findViewById(R.id.mRan);
				mDiff.setOnClickListener(this);
				
				Button dDiff = (Button) findViewById(R.id.dRan);
				dDiff.setOnClickListener(this);
				
				
				for( int i=0; i < ranges.length; i++ ) {
					String a = "r" + i;
					ranges[i] = (TextView) findViewById(getResources().getIdentifier(a, "id", getPackageName()));
				}


				
				rOn.setChecked( prefs.getBoolean( "restrictedMode", false));
				gLenth.setText( prefs.getString("gameLength", "25"));
				eTime.setText( prefs.getString("earnedTime", "15"));
				cNeeded.setText( prefs.getString("correctNeeded", "25"));
				
				gType.setOnClickListener(this);
				
				settings = true;
				
			} else{
				password.setText("");
			}
			
		} else if( p1.getId() == R.id.gType){
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
		}else if( p1.getId() == R.id.pPass) {
				final Dialog dialog = new Dialog(this);
		        dialog.setContentView(R.layout.dialog);
		        dialog.setTitle("Change Password...");

		        // set the custom dialog components - text, image and button
		        current = (EditText) dialog.findViewById(R.id.pass_cur);
		        newPass = (EditText) dialog.findViewById(R.id.pass_new);

		        Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialog_ok);
		        Button dialogButtonCan = (Button) dialog.findViewById(R.id.dialog_cancel);
		        // if button is clicked, close the custom dialog
		        dialogButtonOk.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View v) {
		    
		            	if(current.getText().toString().equals(prefs.getString("password", "admin"))){
		            		if(newPass.getText().toString().equals("")){
		            			Toast.makeText( getApplicationContext() , "Enter a new password..." , Toast.LENGTH_LONG).show();
		            			newPass.setText("");
		            		} else{
		            			editor.putString("password", newPass.getText().toString());
		            			editor.commit();
		            			Toast.makeText( getApplicationContext() , "New Password Saved!" , Toast.LENGTH_LONG).show();
		            			dialog.dismiss();
		            		}
		            		
		            	} else {
		            		Toast.makeText( getApplicationContext() , "Incorrect Password" , Toast.LENGTH_LONG).show();
		            		current.setText("");
		            		newPass.setText("");
		            	}
		            		
		                
		            }
		        });
		        
		        dialogButtonCan.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View v) {
		                dialog.dismiss();
		            }
		        });

		        dialog.show();
		} else if( p1.getId() == R.id.aRan) {
			diffDialog("a");
			
		} else if( p1.getId() == R.id.sRan) {
			diffDialog("s");
		} else if( p1.getId() == R.id.mRan) {
			diffDialog("m");
		} else if( p1.getId() == R.id.dRan) {
			diffDialog("d");
		}
	}
	
	private void diffDialog(final String type) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		final String items[] = new String[] {"Easy", "Medium", "Hard", "Expert"};
		adb.setSingleChoiceItems(items, prefs.getInt(type + "DiffPlace", 0), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					editor.putString(type + "Diff", items[p2]);
					editor.putInt(type + "DiffPlace", p2);
					editor.commit();
					p1.dismiss();
				}
				
			
		}
		
		);
		adb.show();
	}
	
}
