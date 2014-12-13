package com.example.discogs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

// Abstract class used for general helper methods
public abstract class Utils {
	
	//Check whether there is an internet connection
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager 
			= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	
	public static void oAuthRequest(){
		
		
	}
	
	
	
	
}
