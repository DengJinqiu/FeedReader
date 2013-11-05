package com.solstice.feedreader.model;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

public interface FeedLoader {

	FeedManager loadFeed(String URL) throws IOException, XmlPullParserException;

}
