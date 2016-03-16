package com.cameracornet.graftssh;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.connectbot.bean.HostBean;
import org.connectbot.service.OnHostStatusChangedListener;
import org.connectbot.service.TerminalBridge;
import org.connectbot.service.TerminalManager;
import org.connectbot.util.HostDatabase;

import java.util.List;

/**
 * Created by adminsag on 3/13/16.
 * @author Stephen A. Gutknecht
 * Port forwarding and Proxy is the name of the game - not just interactive sessions
 */
public class HeadlessSessionManagerService extends Service implements OnHostStatusChangedListener {

	private static final String TAG = "CB.HeadlessSessionMgr";
	HeadlessSessionManagerService mySelf = null;

	private HostDatabase hostdb;
	private List<HostBean> hosts;

	protected TerminalManager bound = null;
	private boolean waitingForDisconnectAll = false;


	private ServiceConnection connection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			bound = ((TerminalManager.TerminalBinder) service).getService();

			// update our listview binder to find the service
			mySelf.updateList();

			bound.registerOnHostStatusChangedListener(mySelf);

			if (waitingForDisconnectAll) {
				disconnectAll();
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			bound.unregisterOnHostStatusChangedListener(mySelf);

			bound = null;
			mySelf.updateList();
		}
	};


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		android.util.Log.i(TAG, "onStartCommand startId " + startId);
		mySelf = this;

		if (startId == 1)
		{
			// ToDo: would seem best to use this service to wait until Internet / WiFi is working - BEFORE we even start the next service
			// ToDo: have boot option only if 3G/Mobile or WiFi auto-connect

			// start the terminal manager service
			android.util.Log.i("ABR", "Issuing startService CB.TerminalManager");
			// start the terminal manager service
			this.bindService(new Intent(this, TerminalManager.class), connection, Context.BIND_AUTO_CREATE);

			hostdb = HostDatabase.get(this);
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onHostStatusChanged() {
		android.util.Log.i(TAG, "onHOstStatusChanged()");
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	protected void updateList() {
		if (hostdb == null)
			hostdb = HostDatabase.get(this);

		hosts = hostdb.getHosts(false /* No concern for sort */);

		// Don't lose hosts that are connected via shortcuts but not in the database.
		if (bound != null) {
			for (TerminalBridge bridge : bound.getBridges()) {
				if (!hosts.contains(bridge.host)) {
					android.util.Log.i(TAG, "updateList host getBridges hit blah blah");
					hosts.add(0, bridge.host);
				}
			}
		}

		// ToDo: iterate list to logcat

		// replaces each element with twice its value
		for (int index=0; index < hosts.size(); index++) {
			HostBean singleHost = hosts.get(index);
// why is this getting 13?!
// HeadlessSessionMgr: updateList host index 0 sshremote3@104.168.162.31:84 boot: 13
			android.util.Log.d(TAG, "updateList host index " + index + " " + singleHost.toString() + " boot: " + singleHost.getAndroidBootupAutoConnectBehavior());
			switch (singleHost.getAndroidBootupAutoConnectBehavior())
			{
				case 1:
					android.util.Log.i(TAG, "AUTOCONNECT DISCOVERED updateList host index " + index + " " + singleHost.toString());
					// ToDo: track a list of which ones have been launched for AUTOCONNECT - and if we were launched by AUTOCONNECT
					if (bound != null)
					{
						try {
							android.util.Log.i(TAG, "updateList going to openConnection " + index + " " + singleHost.toString() + " " + Thread.currentThread());
							bound.openConnection(singleHost.getUri());
						} catch (Exception e) {
							android.util.Log.e(TAG, "updateList openConnect Exception");
							e.printStackTrace();
						}
					}
					break;
			}
		}
	}


	/**
	 * Disconnects all active connections and closes the activity if appropriate.
	 */
	private void disconnectAll() {
		if (bound == null) {
			waitingForDisconnectAll = true;
			return;
		}
	}
}
