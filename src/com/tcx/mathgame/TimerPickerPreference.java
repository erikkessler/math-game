package com.tcx.mathgame;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

public class TimerPickerPreference extends DialogPreference implements
		OnValueChangeListener {

	private NumberPicker mMinutesPicker;
	private NumberPicker mSecondsPicker;
	private static final String PREFERENCE_NS = "http://schemas.android.com/apk/res/com.tcx.mathgame";
	private static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
	private static final String ATTR_DEFAULT_VALUE = "defaultValue";
	private static final String ATTR_TEXT = "text";
	private final String mText;
	private final int mDefaultValue;

	public TimerPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.timer_picker);
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
		mMinutesPicker = (NumberPicker) view
				.findViewById(R.id.timer_picker_minutes);
		mMinutesPicker.setMinValue(0);
		mMinutesPicker.setMaxValue(9999);
		mMinutesPicker.setWrapSelectorWheel(false);
		mMinutesPicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		mSecondsPicker = (NumberPicker) view
				.findViewById(R.id.timer_picker_seconds);
		mSecondsPicker.setMinValue(0);
		mSecondsPicker.setMaxValue(59);
		mSecondsPicker.setWrapSelectorWheel(false);
		mSecondsPicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		mSecondsPicker.setFormatter(new MyTwoDigitFormatter());

		int mCurrentValue = getPersistedInt(mDefaultValue);
		int minutes = mCurrentValue / 60;
		int seconds = mCurrentValue % 60;

		setMins(minutes, seconds);

		mMinutesPicker.setValue(minutes);
		mSecondsPicker.setValue(seconds);

		mSecondsPicker.setOnValueChangedListener(this);
		mMinutesPicker.setOnValueChangedListener(this);

	}

	private void setMins(int minutes, int seconds) {
		if (minutes == 0)
			mSecondsPicker.setMinValue(1);
		else
			mSecondsPicker.setMinValue(0);

		if (seconds == 0)
			mMinutesPicker.setMinValue(1);
		else
			mMinutesPicker.setMinValue(0);

		mSecondsPicker.setWrapSelectorWheel(false);
		mMinutesPicker.setWrapSelectorWheel(false);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			int time = mMinutesPicker.getValue() * 60
					+ mSecondsPicker.getValue();
			callChangeListener(time);
			persistInt(time);
		}
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		setMins(mMinutesPicker.getValue(), mSecondsPicker.getValue());

	}

	// Used to always display 2 digits
	public class MyTwoDigitFormatter implements NumberPicker.Formatter {

		@Override
		public String format(int value) {
			if(value < 10)
				return "0" + value;
			else
				return value + "";
		}
	}

}