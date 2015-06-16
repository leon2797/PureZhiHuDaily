package com.purezhihudaily.ui.activity;

import java.sql.SQLException;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.purezhihudaily.R;
import com.purezhihudaily.framework.db.domain.DailyNews;
import com.purezhihudaily.framework.db.domain.DailyNewsThemesDetail;
import com.purezhihudaily.framework.db.model.GenericModel;
import com.purezhihudaily.tasks.GetThemesDetailTask;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.ui.adapter.DailyNewsThemesDetailAdapter;
import com.purezhihudaily.ui.widget.SlideListView;
import com.purezhihudaily.ui.widget.SwipeRefreshLayout;
import com.purezhihudaily.utils.LogUtils;
import com.purezhihudaily.utils.MyAsyncTask;
import com.purezhihudaily.utils.ZhihuDailyUtils;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class DailyNewsThemesDetailActivity extends SwipeActivity implements SwipeRefreshLayout.OnRefreshListener, HttpResponseListener {

	private SlideListView slideListView;

	private DailyNewsThemesDetailAdapter adapter;

	private DailyNewsThemesDetail themesDetail;

	private long themeId = 0;

	private Toolbar toolbar;

	private SwipeRefreshLayout swipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dailynews_themes_detail);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
		slideListView = (SlideListView) findViewById(R.id.list_dailynews);
		slideListView.initSlideMode(SlideListView.MOD_RIGHT);
		slideListView.setToolbar(toolbar);
		// 为ListView添加一个Header，这个Header与ToolBar一样高。这样我们可以正确的看到列表中的第一个元素而不被遮住。
		View header = new View(this);
		header.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material)));
		header.setBackgroundColor(Color.parseColor("#00000000"));
		slideListView.addHeaderView(header, null, false);
		slideListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				DailyNews dailyNews = themesDetail.stories.get(position - ((ListView) parent).getHeaderViewsCount());

				try {
					ZhihuDailyUtils.insertOrUpdateRecord(dailyNews);
					ZhihuDailyUtils.setReadNews(themesDetail.stories, dailyNews);
				}
				catch (SQLException e) {
					LogUtils.getInstance().e(e);
				}

				updateAdapter();

				Intent intent = new Intent();
				intent.setClass(DailyNewsThemesDetailActivity.this, DailyNewsDetailActivity.class);
				intent.putExtra("id", dailyNews.id);
				startActivity(intent);
			}
		});

		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setSlideListView(slideListView);
		swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
		
		slideListView.setOnScrollListener(new OnScrollListener() {

			int lastPosition = 0;// 上次滚动到的第一个可见元素在listview里的位置——firstVisibleItem
			int state = SCROLL_STATE_IDLE;

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem == 0) {
					slideListView.animateBack();
				}
				if (firstVisibleItem > 0) {
					if (firstVisibleItem > lastPosition && state == SCROLL_STATE_FLING) {
						// 如果上次的位置小于当前位置，那么隐藏头尾元素
						slideListView.animateHide();
					}

					// ================================
					if (firstVisibleItem < lastPosition && state == SCROLL_STATE_FLING) {
						// 如果上次的位置大于当前位置，那么显示头尾元素，其实本例中，这个if没用
						// 如果是滑动ListView触发的，那么，animateBack()肯定已经执行过了，所以没有必要
						// 如果是点击按钮啥的触发滚动，那么根据设计原则，按钮肯定是头尾元素之一，所以也不需要animateBack()
						// 所以这个if块是不需要的
						slideListView.animateBack();
					}
					// 这里没有判断(firstVisibleItem == lastPosition && state ==
					// SCROLL_STATE_FLING)的情况，
					// 但是如果列表中的单个item如果很长的话还是要判断的，只不过代码又要多几行
					// 但是可以取巧一下，在触发滑动的时候拖动执行一下animateHide()或者animateBack()——本例中的话就写在那个点击事件里就可以了）
					// BTW，如果列表的滑动纯是靠手滑动列表，而没有类似于点击一个按钮滚到某个位置的话，只要第一个if就够了…

				}
				lastPosition = firstVisibleItem;

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				state = scrollState;
			}
		});

		if (savedInstanceState == null) {
			themeId = getIntent().getLongExtra("themeId", 0);
		}
		else {
			themeId = savedInstanceState.getLong("ID");
		}

		new GetThemesDetailTask(this, this).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(themeId));

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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putLong("ID", themeId);
	}

	@Override
	public void onPreExecute() {
		swipeRefreshLayoutControl(true);
	}

	@Override
	public void onPostExecute(GenericModel resultFrom, boolean isRefreshSuccess) {
		if (resultFrom == null) {
			swipeRefreshLayoutControl(false);
			return;
		}

		DailyNewsThemesDetail result = (DailyNewsThemesDetail) resultFrom;
		themesDetail = result;

		toolbar.setTitle(themesDetail.name);

		if (isRefreshSuccess) {
			updateAdapter();
		}

		swipeRefreshLayoutControl(false);
	}

	@Override
	public void onFail(Exception e) {
		swipeRefreshLayoutControl(false);

		Crouton.makeText(this, getResources().getString(R.string.cannot_refresh_comments), Style.ALERT, R.id.crouton_handle).show();

		LogUtils.getInstance().e(e);
	}

	private void updateAdapter() {
		if (adapter == null) {
			adapter = new DailyNewsThemesDetailAdapter(this);
			slideListView.setAdapter(adapter);
		}
		adapter.setDataList(themesDetail.stories);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		new GetThemesDetailTask(this, this).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(themeId));
	}

	private void swipeRefreshLayoutControl(final boolean bool) {
		swipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(bool);
			}
		});
	}

}
