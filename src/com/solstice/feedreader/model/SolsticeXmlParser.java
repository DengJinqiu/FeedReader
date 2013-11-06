package com.solstice.feedreader.model;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/** Parser the string get from the give url and initiate feedManager */
/**
 * @author jinqiu
 * 
 */
public class SolsticeXmlParser {
	/** Do not use namespaces. */
	private static final String ns = null;

	/** Used to save authors, categories and articles. */
	private FeedManager feedManager = new FeedManager();

	/**
	 * Parse the input string.
	 * 
	 * @param in
	 *            The input string.
	 * @return FeedManager used to save authors, articles and categories.
	 * @throws IOException
	 *             The exception when network went wrong.
	 * @throws XmlPullParserException
	 *             The exception when parser xml.
	 */
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

	/**
	 * Parser for the whole string.
	 * 
	 * @param parser
	 *            Parser.
	 * @throws IOException
	 *             The exception when network went wrong.
	 * @throws XmlPullParserException
	 *             The exception when parser xml.
	 */
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
			} else if (name.equals("entry")) {
				readEntry(parser);
			} else {
				skip(parser);
			}
		}
	}

	/**
	 * Parser for categories.
	 * 
	 * @param parser
	 *            The parser.
	 * @throws IOException
	 *             The exception when network went wrong.
	 * @throws XmlPullParserException
	 *             The exception when parser xml.
	 */
	private void readCategory(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "category");
		if (parser.getAttributeCount() != 1) {
			throw new XmlPullParserException(
					"XML category attribute number does not equal to one");
		} else {
			feedManager.addCategory(parser.getAttributeValue(0));
			skip(parser);
		}
	}

	/**
	 * Parser for entries.
	 * 
	 * @param parser
	 *            The parser.
	 * @throws IOException
	 *             The exception when network went wrong.
	 * @throws XmlPullParserException
	 *             The exception when parser xml.
	 */
	private void readEntry(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		parser.require(XmlPullParser.START_TAG, ns, "entry");

		Article article = new Article();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("category") && parser.getAttributeCount() == 2
					&& parser.getAttributeName(1).equals("term")) {
				article.addCategory(parser.getAttributeValue(1));
				parser.next();
			} else if (name.equals("title")) {
				parser.require(XmlPullParser.START_TAG, ns, "title");
				String title = readText(parser);
				parser.require(XmlPullParser.END_TAG, ns, "title");
				article.setTitle(title);
			} else if (name.equals("content")) {
				parser.require(XmlPullParser.START_TAG, ns, "content");
				String content = readText(parser);
				parser.require(XmlPullParser.END_TAG, ns, "content");
				article.setContent(content);
			} else if (name.equals("author")) {
				article.setAuthorName(readAuthor(parser));
			} else {
				skip(parser);
			}
		}
		feedManager.addArticle(article);
	}

	/**
	 * Parser for authors.
	 * 
	 * @param parser
	 *            The parser.
	 * @throws IOException
	 *             The exception when network went wrong.
	 * @throws XmlPullParserException
	 *             The exception when parser xml.
	 */
	private String readAuthor(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		String authorName = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("name")) {
				parser.require(XmlPullParser.START_TAG, ns, "name");
				authorName = readText(parser);
				parser.require(XmlPullParser.END_TAG, ns, "name");
			} else {
				skip(parser);
			}
		}
		return authorName;
	}

	/**
	 * For the tags title and summary, extracts their text values.
	 * 
	 * @param parser
	 *            The parser.
	 * @throws IOException
	 *             The exception when network went wrong.
	 * @throws XmlPullParserException
	 *             The exception when parser xml.
	 */
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	/**
	 * Skips tags the parser isn't interested in. Uses depth to handle nested
	 * tags.
	 * 
	 * @param parser
	 *            The parser
	 * @throws IOException
	 *             The exception when network went wrong.
	 * @throws XmlPullParserException
	 *             The exception when parser xml.
	 */
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
