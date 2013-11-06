package com.solstice.feedreader.view.activities;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.solstice.feedreader.R;
import com.solstice.feedreader.model.ArticleCollection;
import com.solstice.feedreader.model.FeedManager;

public class CategoryAuthorActivity extends FragmentActivity {

	/** Provide fragments for each of the sections. */
	private SectionsPagerAdapter sectionsPagerAdapter;

	/** Host the section contents. */
	private ViewPager viewPager;

	private FeedManager feedManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.swipe_scroll_page);
		
		Bundle args = this.getIntent().getExtras();
		if (args != null) {
			feedManager = (FeedManager) args
					.getSerializable(FeedManager.FEED_MANAGER);
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
	private class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public void setPrimaryItem (ViewGroup container, int position, Object object) {
			
		}
		
		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new VerticalScrollFragment();
			Bundle args = new Bundle();
			if (position == 0) {
				args.putSerializable(ArticleCollection.COLLECTIONS,
						(Serializable) feedManager.getCategories());
				fragment.setArguments(args);
			} else if (position == 1) {
				args.putSerializable(ArticleCollection.COLLECTIONS,
						(Serializable) feedManager.getAuthors());
				fragment.setArguments(args);
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale local = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.category).toUpperCase(local);
			case 1:
				return getString(R.string.author).toUpperCase(local);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class VerticalScrollFragment extends Fragment {

		private Map<String, ArticleCollection> articleCollections;

		@SuppressWarnings("unchecked")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			Bundle args = getArguments();
			if (args != null) {
				articleCollections = (Map<String, ArticleCollection>) args
						.getSerializable(ArticleCollection.COLLECTIONS);
			}

			ScrollView rootView = (ScrollView) inflater.inflate(
					R.layout.collection_fragment, container, false);

			LinearLayout rootViewContent = (LinearLayout) rootView
					.findViewById(R.id.fragment_content);

			for (String name : articleCollections.keySet()) {
				ArticleCollectionButton item = new ArticleCollectionButton(
						articleCollections.get(name));
				item.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						Intent intent = new Intent();
						Bundle args = new Bundle();
						args.putSerializable(ArticleCollection.COLLECTION,
								((ArticleCollectionButton) view)
												.getArticleCollection());
						intent.putExtras(args);
						intent.setClass(
								(CategoryAuthorActivity) VerticalScrollFragment.this
										.getActivity(),
								ArticleActivity.class);
						startActivity(intent);
					}
				});
				rootViewContent.addView(item);
			}

			return rootView;
		}

		private class ArticleCollectionButton extends Button {

			private ArticleCollection articleCollection;

			public ArticleCollectionButton(ArticleCollection articleCollection) {
				super(VerticalScrollFragment.this.getActivity());
				this.articleCollection = articleCollection;
				String name = "";
				name = articleCollection.getName();
				if (articleCollection.articleNumber() == 0) {
					name += " (NO ARTICLE)";
				}
				setText(name);
			}

			public ArticleCollection getArticleCollection() {
				return articleCollection;
			}
		}
	}

}
