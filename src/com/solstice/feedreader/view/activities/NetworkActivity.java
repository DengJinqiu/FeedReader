package com.solstice.feedreader.view.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.solstice.feedreader.R;
import com.solstice.feedreader.model.FeedLoader;
import com.solstice.feedreader.model.FeedLoaderImpl;
import com.solstice.feedreader.model.FeedManager;

public class NetworkActivity extends Activity {
	
	private FeedLoader feedLoader = new FeedLoaderImpl();

	/** The BroadcastReceiver that tracks network connectivity changes. */
	private NetworkReceiver receiver = new NetworkReceiver();

	private static final String URL = "http://blog.solstice-mobile.com/feeds/posts/default";

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
				new DownloadXmlTask().execute(URL);
			} else {
				showNoConnectionPage();
			}
		}

	}

	// Implementation of AsyncTask used to download XML feed from
	// stackoverflow.com.
	private class DownloadXmlTask extends AsyncTask<String, Void, FeedManager> {

		@Override
		protected FeedManager doInBackground(String... urls) {
			try {
				return feedLoader.loadFeed(urls[0]);
			} finally {
				
			}
		}

		@Override
		protected void onPostExecute(FeedManager feedManager) {
			Intent intent = new Intent();
			intent.setClass(NetworkActivity.this, CategoryAuthorActivity.class);
			startActivity(intent);
			finish();
		}
	}

}
