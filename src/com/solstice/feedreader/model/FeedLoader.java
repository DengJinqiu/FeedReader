package com.solstice.feedreader.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

public class FeedLoader {

	private static final String URL = "http://blog.solstice-mobile.com/feeds/posts/default";

	public FeedManager loadFeed() throws IOException, XmlPullParserException {
		InputStream stream = null;
		SolsticeXmlParser solsticeXmlParser = new SolsticeXmlParser();

		try {
			stream = downloadUrl(URL);
			return solsticeXmlParser.parse(stream);
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
//		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
//		String inputLine = in.readLine();
//
//		in.close();
		return stream;
	}

}
