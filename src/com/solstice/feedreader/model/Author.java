package com.solstice.feedreader.model;

import java.util.HashSet;
import java.util.Set;

public class Author {
	private String name;
	
	private Set<Article> articles = new HashSet<Article>();
	
	public Author(String name) {
		this.name = name;
	}
	
	public void addArticle(Article article) {
		articles.add(article);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
