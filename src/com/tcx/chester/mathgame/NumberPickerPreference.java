package com.tcx.chester.mathgame;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

public class NumberPickerPreference extends DialogPreference {

	private NumberPicker mNumberPicker;
	private static final String PREFERENCE_NS = "http://schemas.android.com/apk/res/com.tcx.chester.mathgame";
	private static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
	private static final String ATTR_DEFAULT_VALUE = "defaultValue";
	private static final String ATTR_TEXT = "text";
	private final String mText;
	private final int mDefaultValue;

	public NumberPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.number_picker);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);

		setDialogIcon(null);

		mText = attrs.getAttributeValue(PREFERENCE_NS, ATTR_TEXT);
		mDefaultValue = attrs.getAttributeIntValue(ANDROID_NS,
				ATTR_DEFAULT_VALUE, 0);

	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		// view is your layout expanded and added to the dialog
		// find and hang on to your views here, add click listeners etc
		// basically things you would do in onCreate
		TextView mTextView = (TextView) view
				.findViewById(R.id.number_picker_tv);
		mTextView.setText(mText);
		mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker_np);
		mNumberPicker.setMinValue(1);
		mNumberPicker.setMaxValue(9999);
		mNumberPicker.setWrapSelectorWheel(false);
		mNumberPicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		mNumberPicker.setValue(getPersistedInt(mDefaultValue));
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			callChangeListener(mNumberPicker.getValue());
			persistInt(mNumberPicker.getValue());
		}
	}

}