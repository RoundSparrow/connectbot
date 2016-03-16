package com.cameracornet.graftssh;

import android.util.Log;

import org.connectbot.bean.HostBean;
import org.connectbot.bean.PortForwardBean;

/**
 * Created by adminsag on 3/16/16.
 */
public class ConnectLogHolder {
	public static final String TAG = "CB.LogHolder";

	public static void addConnectLogEntry(int logCode, HostBean host, int reconnectAttemptCount) {
		switch (logCode)
		{
			case 40:
				LogKeeper.addLogEntry("Connect", "SKIP_NET" + ", " + reconnectAttemptCount + ", " + "host " + hostSummaryA(host), LogKeeper.L_A_HIGH, LogKeeper.L_B_NORMAL);
				break;
			default:
				LogKeeper.addLogEntry("Connect", logCode + ", " + reconnectAttemptCount + ", " + "host " + hostSummaryA(host), LogKeeper.L_A_NORMAL, LogKeeper.L_B_NORMAL);
				break;
		}
		Log.d(TAG, "log entry added code " + logCode);
	}

	public static void addForwardEntry(int statusCode, PortForwardBean portForward) {
		switch (statusCode)
		{
			case 100:  // failure
				LogKeeper.addLogEntry("Forward", "Failure" + ", " + portForward.getNickname() + ", " + portForward.getSourcePort(), LogKeeper.L_A_HIGH, LogKeeper.L_B_NORMAL);
				break;
			default:
				LogKeeper.addLogEntry("Forward", statusCode + ", " + portForward.getNickname() + ", " + portForward.getSourcePort(), LogKeeper.L_A_NORMAL, LogKeeper.L_B_NORMAL);
				break;
		}
		Log.d(TAG, "forward log entry added code " + statusCode);
	}

	public static void addConnectLogWorkingSessionNowFailing(int failureCode, HostBean host) {
		String outReasonForCode = "?????";
		switch (failureCode)
		{
			case 10:
				outReasonForCode = "ping fail";
				break;
			case 20:
				outReasonForCode = "ping loop reports not connected";
				break;
			case 40:
				outReasonForCode = "pre onDisconnect";
				break;
		}
		LogKeeper.addLogEntry("Failure", failureCode + ", " +  outReasonForCode + ", " + "host " + hostSummaryA(host), LogKeeper.L_A_HIGH, LogKeeper.L_B_NORMAL);
	}

	public static String hostSummaryA(HostBean host)
	{
		return host.getId() + ", " + host.getUsername() + "@" + host.getHostname() + ":" + host.getPort();
	}

	public static void addConnectivityReceiverChange(int failureCode) {
		String outReasonForCode = "?????";
		switch (failureCode)
		{
			case 10:
				outReasonForCode = "onConnectivityLost";
				break;
			case 20:
				outReasonForCode = "onConnectivityRestored";
				break;
		}
		LogKeeper.addLogEntry("ConnectivityFailure", failureCode + ", " +  outReasonForCode + "", LogKeeper.L_A_SHOW_A, LogKeeper.L_B_NORMAL);
	}

	public static void addAppStartupEntry(int entryCode) {
		LogKeeper.addLogEntry("AppStartup", "bootup startService", LogKeeper.L_A_SHOW_A, LogKeeper.L_B_NORMAL);
	}

	public static void addAutoConnectHostLaunch(int entryCode, HostBean singleHost) {
		LogKeeper.addLogEntry("AppStartup", "AutoConnect, " + hostSummaryA(singleHost), LogKeeper.L_A_NORMAL, LogKeeper.L_B_NORMAL);
	}
}
