package com.solstice.feedreader.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FeedManager implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, Category> categories = new HashMap<String, Category>();
	private Map<String, Author> authors = new HashMap<String, Author>();
	private Map<String, Article> articles = new HashMap<String, Article>();

	public void addCategory(String categoryName) {
		if (!categories.containsKey(categoryName)) {
			categories.put(categoryName, new Category(categoryName));
		}
	}

	private void addAuthor(String authorName) {
		if (!authors.containsKey(authorName)) {
			authors.put(authorName, new Author(authorName));
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

}
