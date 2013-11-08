FeedReader
======

A separate activity called MainActivity is used to check the network condition, it is the first activity launched. It first checks the network connection, if there is no connection, displays an error page and uses a broadcast to track the connection changing, starts downloading when the network is connected.

If the network is connected, downloading and parsering the XML in a separate thread besides the main UI thread. XmlPullParser is used to parser the XML, I followed the andorid offical guide to do it http://developer.android.com/training/basics/network-ops/xml.html. Only articles, authors and categories are retrieved from the XML and saved in FeedManager. After parsering, FeedManager is passed to another activity to display and MainActivity is killed. 

There are two kinds of activites to display the result. Both of them are swipe-scrollview and can also be scrolled vertically. Dates are sent in the serialized form.

I tested it on "Samsung Galaxy Tab 3" and an "HTC phone", the minimum SDK version is 11.

There are several things need to be improved if I have more time:
- Able to refresh the pages and update the articles automatically.
- Handle the exceptions.
- Use parcelable objects instead of serializable objects to pass data, the serializable objects are slower.
- Change MainActivity into a welcome page. Currently, it is almost a blank page. Only used to download, parser and display network problem.
- Improve the article display.
