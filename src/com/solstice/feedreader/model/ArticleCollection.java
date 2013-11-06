package com.solstice.feedreader.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** A collection of articles, to represents category and author. */
public class ArticleCollection implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Key when Inserting a Serializable value into the mapping of the Bundle. */
	public static final String COLLECTIONS = "collections";

	/** Key when Inserting a Serializable value into the mapping of the Bundle. */
	public static final String COLLECTION = "collection";

	/** The name of this collection. */
	private String name;

	/** The articles in this collection. */
	private List<Article> articles = new ArrayList<Article>();

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The name of this collection.
	 */
	public ArticleCollection(String name) {
		this.name = name;
	}

	/**
	 * Add article.
	 * 
	 * @param article
	 *            The article.
	 */
	public void addArticle(Article article) {
		articles.add(article);
	}

	/**
	 * Get the collection name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the collection name.
	 * 
	 * @param name
	 *            The name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the number of articles.
	 * 
	 * @return The number of articles.
	 */
	public int articleNumber() {
		return articles.size();
	}

	/**
	 * Get the article by index.
	 * 
	 * @param index
	 *            The index.
	 * @return The article.
	 */
	public Article getArticle(int index) {
		return articles.get(index);
	}

	/**
	 * Get all the articles.
	 * 
	 * @return All the articles.
	 */
	public List<Article> getArticles() {
		return articles;
	}

}
