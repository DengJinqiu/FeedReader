package com.solstice.feedreader.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.SharedPreferences;
import android.os.DropBoxManager.Entry;
import android.preference.PreferenceManager;

public class FeedLoaderImpl implements FeedLoader {

	@Override
	public FeedManager loadFeed(String URL) throws IOException, XmlPullParserException {
		InputStream stream = null;
		SolsticeXmlParser xmlParser = new SolsticeXmlParser();

		try {
			stream = downloadUrl(URL);
			return xmlParser.parse(stream);

		} finally {
			if (stream != null) {
				stream.close();
			}
		}

	}

	// Given a string representation of a URL, sets up a connection and gets
	// an input stream.
	private InputStream downloadUrl(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		// Starts the query
		conn.connect();
		InputStream stream = conn.getInputStream();
		return stream;
	}

}
