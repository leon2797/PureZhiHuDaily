package com.purezhihudaily.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.purezhihudaily.R;
import com.purezhihudaily.ui.fragment.DailyNewsDetailFragment;
import com.purezhihudaily.ui.widget.SlideWebView;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class DailyNewsDetailActivity extends SwipeActivity {

	private DailyNewsDetailFragment dailyNewsDetailFragment;

	private long dailyNewsId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dailynews);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}
		toolbar.setOnMenuItemClickListener(onMenuItemClick);
		
		dailyNewsDetailFragment = new DailyNewsDetailFragment();
		dailyNewsDetailFragment.setToolbar(toolbar);

		if (savedInstanceState == null) {
			dailyNewsId = getIntent().getLongExtra("id", 0);
		}
		else {
			dailyNewsId = savedInstanceState.getLong("ID");
		}

		Bundle bundle = new Bundle();
		bundle.putLong("id", dailyNewsId);

		dailyNewsDetailFragment.setArguments(bundle);

		getFragmentManager().beginTransaction().replace(R.id.layout_content, dailyNewsDetailFragment).commit();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putLong("ID", dailyNewsId);
	}

	@Override
	public void onDestroy() {
		Crouton.cancelAllCroutons();

		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem menuItem) {
			SlideWebView webView = dailyNewsDetailFragment.getWebView();
			
			switch (menuItem.getItemId()) {
			case R.id.comments:
				Intent intent = new Intent();
				intent.putExtra("id", dailyNewsId);
				intent.setClass(DailyNewsDetailActivity.this, DailyNewsCommentsActivity.class);
				startActivity(intent);
				break;
			case R.id.back:
				if(webView != null && webView.canGoBack()) {
					webView.goBack();
				}
				break;
			case R.id.forward:
				if(webView != null && webView.canGoForward()) {
					webView.goForward();
				}
				break;
			}
			return true;
		}
	};

}
