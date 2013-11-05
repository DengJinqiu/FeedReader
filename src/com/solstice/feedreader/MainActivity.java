package com.solstice.feedreader;

import java.io.IOException;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParserException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	
	public static final String WIFI = "Wi-Fi";
	public static final String CELLULAR = "Cellular";
	private static final String URL = "http://blog.solstice-mobile.com/feeds/posts/default";

	/** Whether there is a Wi-Fi connection. */
	private static boolean wifiConnected = false;
	/** Whether there is a mobile connection. */
	private static boolean mobileConnected = false;
	/** Whether the display should be refreshed. */
	public static boolean refreshDisplay = true;

	// The user's current network preference setting.
	public static String sPref = null;

	// The BroadcastReceiver that tracks network connectivity changes.
	private NetworkReceiver receiver = new NetworkReceiver();

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Register BroadcastReceiver to track connection changes.
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new NetworkReceiver();
		this.registerReceiver(receiver, filter);

	}
	
	// Refreshes the display if the network connection and the
    // pref settings allow it.
    @Override
    public void onStart() {
        super.onStart();

        // Gets the user's network preference settings
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Retrieves a string value for the preferences. The second parameter
        // is the default value to use if a preference value is not found.
        sPref = sharedPrefs.getString("listPref", "Wi-Fi");

        updateConnectedFlags();

        // Only loads the page if refreshDisplay is true. Otherwise, keeps previous
        // display. For example, if the user has set "Wi-Fi only" in prefs and the
        // device loses its Wi-Fi connection midway through the user using the app,
        // you don't want to refresh the display--this would force the display of
        // an error page instead of stackoverflow.com content.
        if (refreshDisplay) {
            loadPage();
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    // Checks the network connection and sets the wifiConnected and mobileConnected
    // variables accordingly.
    private void updateConnectedFlags() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }

    // Uses AsyncTask subclass to download the XML feed from stackoverflow.com.
    // This avoids UI lock up. To prevent network operations from
    // causing a delay that results in a poor user experience, always perform
    // network operations on a separate thread from the UI.
    private void loadPage() {
        if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected))
                || ((sPref.equals(WIFI)) && (wifiConnected))) {
            // AsyncTask subclass
            new DownloadXmlTask().execute(URL);
        } else {
            showErrorPage();
        }
    }

    // Displays an error if the app is unable to load content.
    private void showErrorPage() {
//        setContentView(R.layout.main);
//
//        // The specified network connection is not available. Displays error message.
//        WebView myWebView = (WebView) findViewById(R.id.webview);
//        myWebView.loadData(getResources().getString(R.string.connection_error),
//                "text/html", null);
    }

    // Populates the activity's options menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    // Handles the user's menu selection.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.settings:
                return true;
        case R.id.refresh:
                loadPage();
                return true;
        default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Implementation of AsyncTask used to download XML feed from stackoverflow.com.
    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                return getResources().getString(R.string.xml_error);
            }
        }

        @Override
        protected void onPostExecute(String result) {
//            setContentView(R.layout.main);
//            // Displays the HTML string in the UI via a WebView
//            WebView myWebView = (WebView) findViewById(R.id.webview);
//            myWebView.loadData(result, "text/html", null);
        }
    }

    // Uploads XML from stackoverflow.com, parses it, and combines it with
    // HTML markup. Returns HTML string.
    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
//        InputStream stream = null;
//        StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
//        List<Entry> entries = null;
//        String title = null;
//        String url = null;
//        String summary = null;
//        Calendar rightNow = Calendar.getInstance();
//        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");
//
//        // Checks whether the user set the preference to include summary text
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean pref = sharedPrefs.getBoolean("summaryPref", false);
//
//        StringBuilder htmlString = new StringBuilder();
//        htmlString.append("<h3>" + getResources().getString(R.string.page_title) + "</h3>");
//        htmlString.append("<em>" + getResources().getString(R.string.updated) + " " +
//                formatter.format(rightNow.getTime()) + "</em>");
//
//        try {
//            stream = downloadUrl(urlString);
//            entries = stackOverflowXmlParser.parse(stream);
//        // Makes sure that the InputStream is closed after the app is
//        // finished using it.
//        } finally {
//            if (stream != null) {
//                stream.close();
//            }
//        }
//
//        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
//        // Each Entry object represents a single post in the XML feed.
//        // This section processes the entries list to combine each entry with HTML markup.
//        // Each entry is displayed in the UI as a link that optionally includes
//        // a text summary.
//        for (Entry entry : entries) {
//            htmlString.append("<p><a href='");
//            htmlString.append(entry.link);
//            htmlString.append("'>" + entry.title + "</a></p>");
//            // If the user set the preference to include summary text,
//            // adds it to the display.
//            if (pref) {
//                htmlString.append(entry.summary);
//            }
//        }
//        return htmlString.toString();
    	return "ddd";
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
//    private InputStream downloadUrl(String urlString) throws IOException {
//        URL url = new URL(urlString);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setReadTimeout(10000 /* milliseconds */);
//        conn.setConnectTimeout(15000 /* milliseconds */);
//        conn.setRequestMethod("GET");
//        conn.setDoInput(true);
//        // Starts the query
//        conn.connect();
//        InputStream stream = conn.getInputStream();
//        return stream;
//    }

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	/**
	 * 
	 * This BroadcastReceiver intercepts the
	 * android.net.ConnectivityManager.CONNECTIVITY_ACTION, which indicates a
	 * connection change. It checks whether the type is TYPE_WIFI. If it is, it
	 * checks whether Wi-Fi is connected and sets the wifiConnected flag in the
	 * main activity accordingly.
	 * 
	 */
	public class NetworkReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

			// Checks the user prefs and the network connection. Based on the
			// result, decides
			// whether
			// to refresh the display or keep the current display.
			// If the userpref is Wi-Fi only, checks to see if the device has a
			// Wi-Fi connection.
			if (WIFI.equals(sPref) && networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				// If device has its Wi-Fi connection, sets refreshDisplay
				// to true. This causes the display to be refreshed when the
				// user
				// returns to the app.
				refreshDisplay = true;
				Toast.makeText(context, R.string.wifi_connected,
						Toast.LENGTH_SHORT).show();

				// If the setting is ANY network and there is a network
				// connection
				// (which by process of elimination would be mobile), sets
				// refreshDisplay to true.
			} else if (ANY.equals(sPref) && networkInfo != null) {
				refreshDisplay = true;
				Toast.makeText(context, R.string.cellular_connected,
						Toast.LENGTH_SHORT).show();

				// Otherwise, the app can't download content--either because
				// there is no network
				// connection (mobile or Wi-Fi), or because the pref setting is
				// WIFI, and there
				// is no Wi-Fi connection.
				// Sets refreshDisplay to false.
			} else {
				refreshDisplay = false;
				Toast.makeText(context, R.string.lost_connection,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
