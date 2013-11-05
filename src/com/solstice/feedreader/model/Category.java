package com.solstice.feedreader.model;

import java.util.HashSet;
import java.util.Set;

public class Category {

	private String name;

	private Set<Article> articles = new HashSet<Article>();

	public void addArticle(Article article) {
		articles.add(article);
	}
}
