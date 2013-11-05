package com.solstice.feedreader.model;

import java.util.HashSet;
import java.util.Set;

public class Article {
	
	private String title;
	private String content;
	
	private Set<Category> categories = new HashSet<Category>();
	private Set<Author> authors = new HashSet<Author>();
	
	public void addCategory(Category category) {
		categories.add(category);
	}
	
	public void addAuthor(Author author) {
		authors.add(author);
	}
}
