package com.solstice.feedreader.view.activities;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

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
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		sectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(sectionsPagerAdapter);

		Bundle args = this.getIntent().getExtras();
		if (args != null) {
			feedManager = (FeedManager) args
					.getSerializable(FeedManager.FEED_MANAGER);
		}
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
			Fragment fragment = new DummySectionFragment();
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
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		private Map<String, ArticleCollection> articleCollections;

		public DummySectionFragment() {
		}

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
					R.layout.scroll_fragment, container, false);
			
			LinearLayout rootViewContent = (LinearLayout) rootView.findViewById(R.id.fragment_content);

			for (String name : articleCollections.keySet()) {
				Button item = new Button(getActivity(), null, R.id.item);
				item.setText(name);
				rootViewContent.addView(item);

			}

			return rootView;
		}
	}

}
