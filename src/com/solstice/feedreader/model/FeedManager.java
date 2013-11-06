package com.solstice.feedreader.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FeedManager implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String FEED_MANAGER = "feed_manager";

	private Map<String, ArticleCollection> categories = new HashMap<String, ArticleCollection>();
	private Map<String, ArticleCollection> authors = new HashMap<String, ArticleCollection>();
	private Map<String, Article> articles = new HashMap<String, Article>();

	public void addCategory(String categoryName) {
		if (!categories.containsKey(categoryName)) {
			categories.put(categoryName, new ArticleCollection(categoryName));
		}
	}

	private void addAuthor(String authorName) {
		if (!authors.containsKey(authorName)) {
			authors.put(authorName, new ArticleCollection(authorName));
		}
	}

	public void addArticle(Article article) {
		articles.put(article.getTitle(), article);

		for (String categoryName : article.getCategoryNames()) {
			addCategory(categoryName);
			categories.get(categoryName).addArticle(article);
		}

		addAuthor(article.getAuthorName());
		authors.get(article.getAuthorName()).addArticle(article);
	}

	public int categoryNumber() {
		return categories.size();
	}

	public int authorNumber() {
		return authors.size();
	}

	public int articleNumber() {
		return articles.size();
	}
	
	public Map<String, ArticleCollection> getCategories() {
		return categories;
	}
	
	public Map<String, ArticleCollection> getAuthors() {
		return authors;
	}

}
