package com.cameracornet.graftssh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by adminsag on 3/13/16.
 * @author Stephen A. Gutknecht
 * ToDo: we need to scan all the hots, and see if ANY are set to boot, and exit this start if none are set to start on boot. As per-host is supported.
 */
public class AndroidBootReceiver extends BroadcastReceiver {

	public static int startupIsssuedCount = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		android.util.Log.i("ABR", "ConnectBot BOOTUP BroadcastReceiver got onReceive startupIsssuedCount: " + startupIsssuedCount);

		if (startupIsssuedCount == 0)
		{
			startupIsssuedCount++;

			// start the terminal manager service
			android.util.Log.i("ABR", "Issuing startService CB.HeadlessSessionManagerService");
			Intent startServiceIntention0 = new Intent(context.getApplicationContext(), HeadlessSessionManagerService.class);
			context.startService(startServiceIntention0);
		}
	}
}