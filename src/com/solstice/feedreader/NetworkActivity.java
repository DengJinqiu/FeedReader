package com.solstice.feedreader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;

public class NetworkActivity extends Activity {

	/** The BroadcastReceiver that tracks network connectivity changes. */
	private NetworkReceiver receiver = new NetworkReceiver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Register BroadcastReceiver to track connection changes.
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new NetworkReceiver();
		this.registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			this.unregisterReceiver(receiver);
		}
	}

	private void launchCategoryAuthorActivity() {
		Intent intent = new Intent();
		intent.setClass(NetworkActivity.this, CategoryAuthorActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * Checks the network connection and sets the wifiConnected,
	 * cellularConnected and validConnection accordingly.
	 */
	private boolean isConnected() {
		// Get the connection information.
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		return networkInfo != null;
	}

	/** Displays an error if there is no network connection. */
	private void showNoConnectionPage() {
		setContentView(R.layout.no_connection);

		// The specified network connection is not available. Displays error
		// message.
		TextView myWebView = (TextView) findViewById(R.id.no_connection);
		myWebView.setText(R.string.connection_error);
	}

	/**
	 * Recognize a connection change. Change the wifiConnected,
	 * cellularConnected and refreshDisplay variables accordingly. Notice the
	 * user about the connection change.
	 */
	private class NetworkReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (isConnected()) {
				launchCategoryAuthorActivity();
			} else {
				showNoConnectionPage();
			}
		}

	}

}
