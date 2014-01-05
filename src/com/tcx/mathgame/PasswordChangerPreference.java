package com.tcx.mathgame;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordChangerPreference extends DialogPreference {

	private TextView mCurrent, mNew;
	
    public PasswordChangerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        setDialogLayoutResource(R.layout.number_picker);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        
        setDialogIcon(null);
       
    }
    
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mCurrent = (TextView)view.findViewById(R.id.pass_changer_cur);
        mNew = (TextView)view.findViewById(R.id.pass_changer_new);

        
        }

        @Override
        protected void onDialogClosed(boolean positiveResult) {
           super.onDialogClosed(positiveResult);

            if (positiveResult) {
            	if(mCurrent.getText().toString().equals(getPersistedString("admin"))){
            		if(mNew.getText().toString().equals("")){
            			Toast.makeText( getContext() , "Enter a new password..." , Toast.LENGTH_LONG).show();
            			mNew.setText("");
            		} else{
            			persistString(mNew.getText().toString());
            			Toast.makeText( getContext() , "New Password Saved!" , Toast.LENGTH_LONG).show();
            		}
            		
            	} else {
            		Toast.makeText( getContext() , "Incorrect Password" , Toast.LENGTH_LONG).show();
            		mCurrent.setText("");
            		mNew.setText("");
            	}
            }
        }

}