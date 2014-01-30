package com.tcx.mathgame;

import android.content.Context;
import android.os.Build;
import android.os.UserHandle;
import android.os.UserManager;

public class Utils {
	
	public static UserReciever mUserReciever = new UserReciever();
	
	public static boolean isKitKatOrLater() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2;
    }
	
	public static boolean isJellyBeanM1() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN;
    }
	


}
