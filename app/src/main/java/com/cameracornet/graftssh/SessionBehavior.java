package com.cameracornet.graftssh;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.connectbot.bean.HostBean;

/**
 * Created by adminsag on 3/14/16.
 */
public class SessionBehavior {
	public static final String TAG = "CB.SessionBehavior";

	public static int connectWhenInThisNetworkCondition(Context context, HostBean host) {
		boolean anyPathToInternet = hasInternetConnection(context);
		if (! anyPathToInternet) {
			com.cameracornet.graftssh.ConnectLogHolder.addConnectLogEntry(40, host, -1);
			android.util.Log.w(TAG, "network unavailable being told before connect, Host " + host.toXML());
			return 2;
		}
		return 0;
	}


	public static boolean hasInternetConnection(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected())
		{
			return true;
		}
		NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected())
		{
			return true;
		}
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected())
		{
			return true;
		}
		return false;
	}
}
