package com.solstice.feedreader.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FeedManager implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, Category> categories = new HashMap<String, Category>();
	private Map<String, Author> authors = new HashMap<String, Author>();
	private Map<String, Article> articles = new HashMap<String, Article>();

	public void addCategory(Category category) {
		if (!categories.containsKey(category.getName())) {
			categories.put(category.getName(), category);
		}
	}

	private void addAuthor(Author author) {
		if (!authors.containsKey(author.getName())) {
			authors.put(author.getName(), author);
		}
	}

	public void addArticle(Article article) {
		articles.put(article.getTitle(), article);

		for (Category category : article.getCategories()) {
			addCategory(category);
			categories.get(category.getName()).addArticle(article);
		}

		addAuthor(article.getAuthor());
		authors.get(article.getAuthor().getName()).addArticle(article);
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
