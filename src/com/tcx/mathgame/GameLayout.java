package com.tcx.mathgame;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class GameLayout extends FrameLayout {

	private int index;

	@SuppressWarnings("unused")
	public GameLayout(Context context) {
		this(context, null);
	}

	public GameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setIndex(int i) {
		index = i;
	}

	public int getIndex() {
		return index;
	}

}
