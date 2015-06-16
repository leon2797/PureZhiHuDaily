package com.purezhihudaily.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.purezhihudaily.R;
import com.purezhihudaily.framework.db.domain.DailyNewsThemesList;
import com.purezhihudaily.framework.db.model.GenericModel;
import com.purezhihudaily.tasks.GetThemesListTask;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.ui.adapter.DailyNewsThemesListAdapter;
import com.purezhihudaily.ui.fragment.DailyNewsListFragment;
import com.purezhihudaily.utils.LogUtils;
import com.purezhihudaily.utils.MyAsyncTask;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class DailyNewsListActivity extends GenericActivity implements HttpResponseListener {

	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;

	private DailyNewsListFragment dailyNewsListFragment;
	
	private DailyNewsThemesList dailyNewsThemesList;
	
	private DailyNewsThemesListAdapter adapter;
	
	private ListView themesListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dailynews);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}
		toolbar.setOnMenuItemClickListener(onMenuItemClick);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		themesListView = (ListView) findViewById(R.id.list_themes);
		themesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.setClass(DailyNewsListActivity.this, DailyNewsThemesDetailActivity.class);
				intent.putExtra("themeId", dailyNewsThemesList.others.get(position).id);
				startActivity(intent);
			}
		});
		
		// 设置返回键可用
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// 创建返回键，并实现打开关/闭监听
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				new GetThemesListTask(DailyNewsListActivity.this, DailyNewsListActivity.this).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}
		};
		drawerToggle.syncState();
		
		drawerLayout.setDrawerListener(drawerToggle);

		dailyNewsListFragment = new DailyNewsListFragment();
		dailyNewsListFragment.setToolbar(toolbar);
		
		getFragmentManager().beginTransaction().replace(R.id.layout_content, dailyNewsListFragment).commit();

		new GetThemesListTask(DailyNewsListActivity.this, DailyNewsListActivity.this).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	public void onDestroy() {
		Crouton.cancelAllCroutons();

		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_list, menu);
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem menuItem) {
			switch (menuItem.getItemId()) {
			case R.id.collect:
				Intent intent = new Intent();
				intent.setClass(DailyNewsListActivity.this, DailyNewsCollectionActivity.class);
				startActivity(intent);
				break;
			}
			return true;
		}
	};

	@Override
	public void onPreExecute() {
		
	}

	@Override
	public void onPostExecute(GenericModel resultFrom, boolean isRefreshSuccess) {
		if (resultFrom == null) {
			return;
		}

		DailyNewsThemesList result = (DailyNewsThemesList) resultFrom;
		dailyNewsThemesList = result;
		
		if (isRefreshSuccess) {
			updateAdapter();
		}
	}

	@Override
	public void onFail(Exception e) {
		Crouton.makeText(this, getResources().getString(R.string.cannot_refresh), Style.ALERT, R.id.crouton_handle).show();

		LogUtils.getInstance().e(e);
	}
	
	private void updateAdapter() {
		if (adapter == null) {
			adapter = new DailyNewsThemesListAdapter(this, true);
			themesListView.setAdapter(adapter);
		}
		adapter.setDataList(dailyNewsThemesList.others);
		adapter.notifyDataSetChanged();
	}

}
