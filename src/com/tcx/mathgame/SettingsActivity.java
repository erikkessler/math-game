package com.tcx.mathgame;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener{
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setContentView( R.layout.settings_password );
			
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			
			// Login screen
			Button submit = (Button) findViewById(R.id.settingpassButton1);
			submit.setOnClickListener(this);

	    }

		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			finish();
		}

		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.settingpassButton1) {
				TextView password = (TextView) findViewById(R.id.settingPass);
				SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
				String passwordString = sharedPref.getString("pref_key_password", "admin");
				
				if( password.getText().toString().equals(passwordString )) {
				
					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		            setContentView(R.layout.clear);
		            
					// Display the fragment as the main content.
			        getFragmentManager().beginTransaction()
			                .replace(android.R.id.content, new SettingsFragment())
			                .commit();
				} else{
					password.setText("");
				}
			
			}
		}
		
		 @Override
		    public boolean onOptionsItemSelected(MenuItem item) {
				 if(item.getItemId() == android.R.id.home) {
					 NavUtils.navigateUpFromSameTask(this);
				 }
			    return super.onOptionsItemSelected(item);
				}
	}
