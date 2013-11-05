package com.solstice.feedreader.model;

import java.util.HashSet;
import java.util.Set;

public class Article {
	
	private String title;
	private String content;
	
	private Set<Category> categories = new HashSet<Category>();
	private Author authors;
	
	public void addCategory(Category category) {
		categories.add(category);
	}
	
	public Iterable<Category> getCategories() {
		return categories;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Author getAuthor() {
		return authors;
	}

	public void setAuthors(Author authors) {
		this.authors = authors;
	}
}
