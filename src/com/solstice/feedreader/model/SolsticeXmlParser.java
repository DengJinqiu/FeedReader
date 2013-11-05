/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.solstice.feedreader.model;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * This class parses XML feeds from stackoverflow.com. Given an InputStream
 * representation of a feed, it returns a List of entries, where each list
 * element represents a single entry (post) in the XML feed.
 */
public class SolsticeXmlParser {
	// Do not use namespaces
	private static final String ns = null;

	private FeedManager feedManager = new FeedManager();

	public FeedManager parse(InputStream in) throws XmlPullParserException,
			IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			readFeed(parser);
			return feedManager;
		} finally {
			in.close();
		}
	}

	private void readFeed(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		parser.require(XmlPullParser.START_TAG, ns, "feed");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("category")) {
				readCategory(parser);
				if (feedManager.categoryNumber() % 10 == 0) {
					int a = 1;
					int b = a;
				}
			} else if (name.equals("entry")) {
				readEntry(parser);
				skip(parser);
			} else {
				skip(parser);
			}
		}
	}

	private void readCategory(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "category");
		if (parser.getAttributeCount() != 1) {
			throw new XmlPullParserException(
					"XML category attribute number does not equal to one");
		} else {
			feedManager.addCategory(new Category(parser.getAttributeValue(0)));
			skip(parser);
		}
	}

	private void readEntry(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		parser.require(XmlPullParser.START_TAG, ns, "entry");
		// String title = null;
		// String summary = null;
		// String link = null;
		// while (parser.next() != XmlPullParser.END_TAG) {
		// if (parser.getEventType() != XmlPullParser.START_TAG) {
		// continue;
		// }
		// String name = parser.getName();
		// if (name.equals("title")) {
		// title = readTitle(parser);
		// } else if (name.equals("summary")) {
		// summary = readSummary(parser);
		// } else if (name.equals("link")) {
		// link = readLink(parser);
		// } else {
		// skip(parser);
		// }
		// }
		// return new Entry(title, summary, link);
	}

	private void readAuthor(XmlPullParser parser) {

	}

	// Skips tags the parser isn't interested in. Uses depth to handle nested
	// tags. i.e.,
	// if the next tag after a START_TAG isn't a matching END_TAG, it keeps
	// going until it
	// finds the matching END_TAG (as indicated by the value of "depth" being
	// 0).
	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
}
