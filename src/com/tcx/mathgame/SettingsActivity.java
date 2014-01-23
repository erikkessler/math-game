package com.tcx.mathgame;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener {

	private SharedPreferences sharedPref;
	private TextView password;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.settings_password);
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);

		
		// Login screen
		final Button submit = (Button) findViewById(R.id.settingpassButton1);
		submit.setOnClickListener(this);
		
		password = (TextView) findViewById(R.id.settingPass);
		password.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_GO){
		            onClick(submit);
		        }
				return false;
			}
		});

		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		if (sharedPref.getBoolean("pref_key_password_first", true)) {
			TextView promt = (TextView) findViewById(R.id.settings_prompt);
			promt.setText(this.getResources().getString(
					R.string.settings_password_prompt_new));
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.settingpassButton1) {
			
			if (sharedPref.getBoolean("pref_key_password_first", true)
					&& !password.getText().equals("")) {
				sharedPref
						.edit()
						.putString("pref_key_password",
								password.getText().toString()).commit();
				sharedPref.edit().putBoolean("pref_key_password_first", false)
						.commit();
				Toast.makeText(this, "Password Saved!", Toast.LENGTH_LONG)
						.show();

				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

				setContentView(R.layout.clear);

				// Display the fragment as the main content.
				getFragmentManager().beginTransaction()
						.replace(android.R.id.content, new SettingsFragment())
						.commit();

			} else if (sharedPref.getBoolean("pref_key_password_first", true)
					&& password.getText().equals("")) {
				Toast.makeText(this, "Enter a Password...", Toast.LENGTH_LONG)
						.show();
			} else {

				String passwordString = sharedPref.getString(
						"pref_key_password", "admin");

				if (password.getText().toString().equals(passwordString)) {

					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);

					setContentView(R.layout.clear);

					// Display the fragment as the main content.
					getFragmentManager()
							.beginTransaction()
							.replace(android.R.id.content,
									new SettingsFragment()).commit();
				} else {
					password.setText("");
				}
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
		}
		return super.onOptionsItemSelected(item);
	}
}
