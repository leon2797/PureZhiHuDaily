package com.purezhihudaily.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.purezhihudaily.R;
import com.purezhihudaily.framework.db.domain.DailyNews;
import com.purezhihudaily.framework.db.domain.DailyNewsList;
import com.purezhihudaily.framework.db.model.GenericModel;
import com.purezhihudaily.tasks.GetCollectNewsTask;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.ui.adapter.DailyNewsListAdapter;
import com.purezhihudaily.ui.widget.SlideListView;
import com.purezhihudaily.utils.LogUtils;
import com.purezhihudaily.utils.MyAsyncTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class DailyNewsCollectionActivity extends SwipeActivity implements HttpResponseListener {

	private SlideListView slideListView;

	private DailyNewsListAdapter adapter;

	private DailyNewsList dailyNewsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dailynews_collection);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}

		slideListView = (SlideListView) findViewById(R.id.list_dailynews);
		slideListView.setOnItemClickListener(onItemClickListener);
		slideListView.initSlideMode(SlideListView.MOD_RIGHT);
		slideListView.setToolbar(toolbar);

		// 为ListView添加一个Header，这个Header与ToolBar一样高。这样我们可以正确的看到列表中的第一个元素而不被遮住。
		View header = new View(this);
		header.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material)));
		header.setBackgroundColor(Color.parseColor("#00000000"));
		slideListView.addHeaderView(header, null, false);

		new GetCollectNewsTask(this, this).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);

	}

	@Override
	public void onDestroy() {
		Crouton.cancelAllCroutons();

		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			DailyNews dailyNews = dailyNewsList.stories.get(position - ((ListView) parent).getHeaderViewsCount());

			Intent intent = new Intent();
			intent.putExtra("id", dailyNews.id);
			intent.setClass(DailyNewsCollectionActivity.this, DailyNewsDetailActivity.class);
			startActivity(intent);
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

		DailyNewsList result = (DailyNewsList) resultFrom;
		dailyNewsList = result;

		if (isRefreshSuccess) {
			updateAdapter();
		}
	}

	@Override
	public void onFail(Exception e) {
		Crouton.makeText(this, getResources().getString(R.string.cannot_refresh_collection), Style.ALERT, R.id.crouton_handle).show();

		LogUtils.getInstance().e(e);
	}

	private void updateAdapter() {
		if (adapter == null) {
			adapter = new DailyNewsListAdapter(this, true);
			slideListView.setAdapter(adapter);
		}
		adapter.setDataList(dailyNewsList.stories);
		adapter.notifyDataSetChanged();
	}

}
