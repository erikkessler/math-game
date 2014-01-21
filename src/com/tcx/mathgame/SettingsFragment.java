package com.tcx.mathgame;

import java.util.HashSet;
import java.util.Set;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragment implements OnPreferenceClickListener, OnPreferenceChangeListener {
	
	private SharedPreferences prefs;
	private ListPreference mAdd, mSub, mMult, mDiv;
	private NumberPickerPreference mLength, mCorrect, mEarned;
	private MultiSelectListPreference mGameType;
	private Set<String> set = new HashSet<String>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settingstest);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        
        Preference mFree = (Preference) findPreference("pref_key_free_time");
        mFree.setOnPreferenceClickListener(this);
        
        SwitchPreference mSwitch = (SwitchPreference) findPreference("pref_key_restricted_mode");
        mSwitch.setOnPreferenceChangeListener(this);
        
        mAdd = (ListPreference) findPreference("pref_key_add_diff");
        mAdd.setSummary(prefs.getString("pref_key_add_diff", "Easy"));
        mAdd.setOnPreferenceChangeListener(this);
        
        mSub = (ListPreference) findPreference("pref_key_sub_diff");
        mSub.setSummary(prefs.getString("pref_key_sub_diff", "Easy"));
        mSub.setOnPreferenceChangeListener(this);
        
        mMult = (ListPreference) findPreference("pref_key_mult_diff");
        mMult.setSummary(prefs.getString("pref_key_mult_diff", "Easy"));
        mMult.setOnPreferenceChangeListener(this);
        
        mDiv = (ListPreference) findPreference("pref_key_div_diff");
        mDiv.setSummary(prefs.getString("pref_key_div_diff", "Easy"));
        mDiv.setOnPreferenceChangeListener(this);
        
        mLength = (NumberPickerPreference) findPreference("pref_key_game_length");
        mLength.setSummary(prefs.getInt("pref_key_game_length", 80) + "");
        mLength.setOnPreferenceChangeListener(this);
        
        mCorrect = (NumberPickerPreference) findPreference("pref_key_correct_needed");
        mCorrect.setSummary(prefs.getInt("pref_key_correct_needed", 25) + "");
        mCorrect.setOnPreferenceChangeListener(this);
        
        mEarned = (NumberPickerPreference) findPreference("pref_key_earned_time");
        mEarned.setSummary(prefs.getInt("pref_key_earned_time", 15) + "");
        mEarned.setOnPreferenceChangeListener(this);
        
        set.add("Addition");
        mGameType = (MultiSelectListPreference) findPreference("pref_key_game_type");
        mGameType.setSummary(fromSet(prefs.getStringSet("pref_key_game_type", set )));
        mGameType.setOnPreferenceChangeListener(this);
        
        
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
	public boolean onPreferenceClick(Preference preference) {
		
		if(preference.getKey().equals("pref_key_free_time")) {
			Intent i = new Intent("tcx.PAUSE");
			Bundle extras = new Bundle();   
			extras.putInt("time", prefs.getInt("pref_key_earned_time", 1));
	        i.putExtras(extras);  
			getActivity().sendBroadcast(i);
		    Toast.makeText( getActivity() , prefs.getInt("pref_key_earned_time", 1) + " minutes of reward time started..." , Toast.LENGTH_LONG).show();
		}
		return false;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if(preference.getKey().equals("pref_key_restricted_mode")) {
			boolean switched = ((SwitchPreference) preference).isChecked();
			if(!switched){
				prefs.edit().putBoolean("pref_key_restricted_mode", true).commit();
				getActivity().startService(new Intent(getActivity(),AppCheckerService.class));
				Intent i = new Intent("tcx.START");
				Bundle extras = new Bundle();   
		        i.putExtras(extras);  
				getActivity().sendBroadcast(i);
				Log.d("YOLO", "Settings tried to start service");
			}else {
				prefs.edit().putBoolean("pref_key_restricted_mode", false).commit();
				getActivity().stopService(new Intent(getActivity(),AppCheckerService.class));
				Intent i = new Intent("tcx.STOP");
				Bundle extras = new Bundle();   
		        i.putExtras(extras);  
				getActivity().sendBroadcast(i);
				Log.d("YOLO", "Settings tried to stop service");
//				
			}
		} else if(preference.getKey().equals("pref_key_add_diff")) {
			mAdd.setSummary((String) newValue);
		} else if(preference.getKey().equals("pref_key_sub_diff")) {
			mSub.setSummary((String) newValue);
		} else if(preference.getKey().equals("pref_key_mult_diff")) {
			mMult.setSummary((String) newValue);
		} else if(preference.getKey().equals("pref_key_div_diff")) {
			mDiv.setSummary((String) newValue);
		} else if(preference.getKey().equals("pref_key_game_length")) {
			mLength.setSummary((Integer) newValue + "");
		} else if(preference.getKey().equals("pref_key_correct_needed")) {
			mCorrect.setSummary((Integer) newValue + "");
		} else if(preference.getKey().equals("pref_key_earned_time")) {
			mEarned.setSummary((Integer) newValue + "");
		} else if(preference.getKey().equals("pref_key_game_type")) {
			mGameType.setSummary(fromSet((Set<String>) newValue));
		}
		return true;
	}

}