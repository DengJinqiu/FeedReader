package com.solstice.feedreader.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** Used to save articles, categories and authors. */
public class FeedManager implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Key when Inserting a Serializable value into the mapping of the Bundle. */
	public static final String FEED_MANAGER = "feed_manager";

	/** The categories. */
	private Map<String, ArticleCollection> categories = new HashMap<String, ArticleCollection>();

	/** The authors. */
	private Map<String, ArticleCollection> authors = new HashMap<String, ArticleCollection>();

	/** The articles. */
	private Map<String, Article> articles = new HashMap<String, Article>();

	/** Add a category. */
	public void addCategory(String categoryName) {
		if (!categories.containsKey(categoryName)) {
			categories.put(categoryName, new ArticleCollection(categoryName));
		}
	}

	/** Add an author. */
	private void addAuthor(String authorName) {
		if (!authors.containsKey(authorName)) {
			authors.put(authorName, new ArticleCollection(authorName));
		}
	}

	/** Add a article and associate it with authors and categories. */
	public void addArticle(Article article) {
		articles.put(article.getTitle(), article);

		for (String categoryName : article.getCategoryNames()) {
			addCategory(categoryName);
			categories.get(categoryName).addArticle(article);
		}

		addAuthor(article.getAuthorName());
		authors.get(article.getAuthorName()).addArticle(article);
	}

	/**
	 * The number of categories.
	 * 
	 * @return The number of categories.
	 */
	public int categoryNumber() {
		return categories.size();
	}

	/**
	 * The number of authors.
	 * 
	 * @return The number of authors.
	 */
	public int authorNumber() {
		return authors.size();
	}

	/**
	 * The number of articles
	 * 
	 * @return The number of articles.
	 */
	public int articleNumber() {
		return articles.size();
	}

	/**
	 * All the categories.
	 * 
	 * @return All the categories.
	 */
	public Map<String, ArticleCollection> getCategories() {
		return categories;
	}

	/**
	 * All the authors.
	 * 
	 * @return All the authors.
	 */
	public Map<String, ArticleCollection> getAuthors() {
		return authors;
	}

}
