package com.solstice.feedreader.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class FeedManager implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Set<Category> categories = new HashSet<Category>();
	private Set<Author> authors = new HashSet<Author>();
	private Set<Article> articles = new HashSet<Article>();
	
	public void addCategory(Category category) {
		categories.add(category);
	}
	
	public void addAuthor(Author author) {
		authors.add(author);
	}
	
	public void addArticle(Article article) {
		articles.add(article);
	}
}
