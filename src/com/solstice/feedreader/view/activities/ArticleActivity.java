package com.solstice.feedreader.view.activities;

import java.io.Serializable;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.solstice.feedreader.R;
import com.solstice.feedreader.model.Article;
import com.solstice.feedreader.model.ArticleCollection;

public class ArticleActivity extends FragmentActivity {

	/** Provide fragments for each of the sections. */
	private SectionsPagerAdapter sectionsPagerAdapter;

	/** Host the section contents. */
	private ViewPager viewPager;

	private ArticleCollection articleCollection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.swipe_scroll_page);

		Bundle args = this.getIntent().getExtras();
		if (args != null) {
			articleCollection = (ArticleCollection) args
					.getSerializable(ArticleCollection.COLLECTION);
		}

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		sectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(sectionsPagerAdapter);

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new VerticalScrollFragment();
			Bundle args = new Bundle();
			args.putSerializable(Article.ARTICLE,
					(Serializable) articleCollection.getArticle(position));
			fragment.setArguments(args);
			// Log.d("getItem", Integer.toString(position));
			return fragment;
		}

		@Override
		public int getCount() {
			return articleCollection.articleNumber();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale local = Locale.getDefault();
			// Log.d("getPageTitle", Integer.toString(position));
			return articleCollection.getArticle(position).getTitle()
					.toUpperCase(local);
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class VerticalScrollFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		private Article article;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			Bundle args = getArguments();
			if (args != null) {
				article = (Article) args.getSerializable(Article.ARTICLE);
			}

			ScrollView rootView = (ScrollView) inflater.inflate(
					R.layout.article_fragment, container, false);

			TextView title = (TextView) rootView.findViewById(R.id.title);
			title.setText(article.getTitle());

			TextView author = (TextView) rootView.findViewById(R.id.author);
			author.setText("Author: " + article.getAuthorName());

			TextView category = (TextView) rootView.findViewById(R.id.category);
			category.setText("Category: " + article.getCategoryNamesString());

			WebView content = (WebView) rootView.findViewById(R.id.content);
			content.loadData(article.getContent(), "text/html", "utf-8");
			return rootView;
		}
	}

}
