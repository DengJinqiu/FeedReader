package com.solstice.feedreader.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArticleCollection implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String COLLECTIONS = "collections";

	public static final String COLLECTION = "collection";

	private String name;

	private List<Article> articles = new ArrayList<Article>();

	public ArticleCollection(String name) {
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

	public int articleNumber() {
		return articles.size();
	}

	public Article getArticle(int index) {
		return articles.get(index);
	}
	
	public List<Article> getArticles() {
		return articles;
	}

}
