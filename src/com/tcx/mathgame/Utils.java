package com.tcx.mathgame;

import android.os.Build;

public class Utils {
	
	public static Boolean paused = false;
	public static UserReciever mUserReciever = new UserReciever();
	public static Boolean userPresent = true;
	
	public static boolean isKitKatOrLater() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2;
    }
	
	public static boolean isJellyBeanM1() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN;
    }
	


}
