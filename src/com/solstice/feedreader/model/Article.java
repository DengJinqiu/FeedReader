package com.solstice.feedreader.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** Used to save the title, content, author and categories of an Article. */
public class Article implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Key when Inserting a Serializable value into the mapping of the Bundle. */
	public static final String ARTICLE = "article";

	/** The title of this article. */
	private String title;

	/** The content of this article. */
	private String content;

	/** The categories of this article. */
	private Set<String> categoryNames = new HashSet<String>();

	/** The authors name. */
	private String authorName;

	/**
	 * Add category.
	 * 
	 * @param categoryName
	 *            The name of the category.
	 */
	public void addCategory(String categoryName) {
		categoryNames.add(categoryName);
	}

	/**
	 * The names of categories.
	 * 
	 * @return Categories.
	 */
	public Iterable<String> getCategoryNames() {
		return categoryNames;
	}

	/**
	 * Get article title.
	 * 
	 * @return The article title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set title.
	 * 
	 * @param title
	 *            The title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get article content.
	 * 
	 * @return The content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Set content.
	 * 
	 * @param content
	 *            Content.
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Get the author's name.
	 * 
	 * @return The author's name.
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * Set the author's name.
	 * 
	 * @param authorName
	 *            The author's name.
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/**
	 * Get the category names in one string.
	 * 
	 * @return The string contains all the categories name.
	 */
	public String getCategoryNamesString() {
		String result = "";
		for (String string : categoryNames) {
			result += string;
			result += ", ";
		}
		if (result.length() > 2) {
			result = result.substring(0, result.length() - 2);
		}
		return result;
	}
}
