package com.cameracornet.graftssh;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import org.connectbot.HostEditorFragment;
import org.connectbot.HostListActivity;
import org.connectbot.R;
import org.connectbot.bean.HostBean;
import org.connectbot.bean.PortForwardBean;
import org.connectbot.util.HostDatabase;

/**
 * Created by adminsag on 3/14/16.
 * Note about the design of this class:
 *   This class is designed to be a virus and modify the ConnectBox project code with minal changes to the ConnectBot code
 *   Assumption is that this is experimental and the features will not be quickly adopted by the ConnectBot Projtect team
 *   So wierd things are done to make calling into the class one-line of code, etc.
 */
public class UserInterfaceActivity {

	public static final String TAG = "CC:UIA";

	public static boolean graftEditHostActivityMenu(MenuItem item, Activity inActivity) {
		switch (item.getItemId()) {
			case R.id.myPopulate0:
				// .setText("sshremote3@104.168.162.31:84");
				PopulateHelper.putOnClipboard(inActivity, "sshremote3@104.168.162.31:84");
				return true;
		}
		return false;
	}

	public static void graftHostEditorFragmentMenuItems(View view, final HostEditorFragment hostEditorFragment, final HostBean mHost) {

		View mBootConnectItem;
		SwitchCompat mBootConnectSwitch;

		mBootConnectSwitch = (SwitchCompat) view.findViewById(R.id.boot_connect_switch);
		mBootConnectSwitch.setChecked(mHost.getAndroidBootupAutoConnectBehavior() != 0);
		mBootConnectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mHost.setAndroidBootupAutoConnectBehavior(1);
				}  else {
					mHost.setAndroidBootupAutoConnectBehavior(0);
				}
				hostEditorFragment.handleHostChange();
			}
		});

		mBootConnectItem = view.findViewById(R.id.boot_connect_item);
		final SwitchCompat finalMBootConnectSwitch = mBootConnectSwitch;
		mBootConnectItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finalMBootConnectSwitch.toggle();
			}
		});
	}


	public static void graftHostListActivityMenuAddA(Menu menu, final HostDatabase hostdb, final HostListActivity hostListActivity) {
		MenuItem populateA0 = menu.add("Populate_A0");
		populateA0.setIcon(android.R.drawable.ic_menu_compass);
		populateA0.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				// FYI< a nice method exists to use a parsable: public static HostBean fromContentValues(ContentValues values)
				android.util.Log.i(TAG, "Populate_A0 clicked");
				HostBean myNewHost = new HostBean();
				myNewHost.setNickname("Test000");
				myNewHost.setCompression(true);
				myNewHost.setPort(84);
				myNewHost.setHostname(DoNotShareClass.hostHostnameEntry1);
				myNewHost.setUsername("sshremote3");
				myNewHost.setPassphrase(DoNotShareClass.hostPasswordEntry1);
				myNewHost.setStayConnected(true);
				myNewHost.setAndroidBootupAutoConnectBehavior(1 /* BootBehavior_ALWAYS */);
				hostdb.saveHost(myNewHost);
				PortForwardBean myNewHostForward0 = new PortForwardBean(
						myNewHost != null ? myNewHost.getId() : -1,
						"SOCKS_Proxy8080",
						HostDatabase.PORTFORWARD_DYNAMIC5,
						"8080",
						"localhost:8080");
				hostdb.savePortForward(myNewHostForward0);
				android.util.Log.i(TAG, "saveHost " + myNewHost.getUri() + " id " + myNewHost.getId());
				android.util.Log.i(TAG, "saveHost Forward0 " + myNewHostForward0.toXML());
				// refresh output
				hostListActivity.updateList();
				return false;
			}
		});

		MenuItem bootServiceSimulate = menu.add("Boot Simulate");
		bootServiceSimulate.setIcon(android.R.drawable.ic_menu_compass);
		bootServiceSimulate.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				// start the terminal manager service
				android.util.Log.i("ABR", "Issuing startService CB.HeadlessSessionManagerService");
				Intent startServiceIntention0 = new Intent(hostListActivity.getApplicationContext(), HeadlessSessionManagerService.class);
				hostListActivity.startService(startServiceIntention0);
				return true;
			}
		});
	}


	public static boolean graftHostEditorFragmentHostDatabaseA(String mFieldType, HostBean mHost, String text) {
		if (HostDatabase.FIELD_HOST_PLAINTEXT_PASSWORD.equals(mFieldType)) {
			mHost.setPassphrase(text);
		} else if (HostDatabase.FIELD_HOST_ANDROIDBOOT_CONNECT.equals(mFieldType)) {
			mHost.setAndroidBootupAutoConnectBehavior(Integer.parseInt(text));
		} else {
			return false;
		}
		return true;
	}


	public static boolean graftConsoleActivityMenuAddA(Menu menu, final HostDatabase hostdb)
	{
		MenuItem sessionLog = menu.add("Connect Log");
		sessionLog.setIcon(android.R.drawable.ic_menu_compass);
		sessionLog.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				// start the terminal manager service
			}
		});


		return true;
	}
}
