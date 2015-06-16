package com.purezhihudaily.ui.fragment;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.purezhihudaily.R;
import com.purezhihudaily.framework.db.domain.DailyNews;
import com.purezhihudaily.framework.db.domain.DailyNewsList;
import com.purezhihudaily.framework.db.model.GenericModel;
import com.purezhihudaily.tasks.GetLatestNewsTask;
import com.purezhihudaily.tasks.GetOutdatedNewsTask;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.ui.activity.DailyNewsDetailActivity;
import com.purezhihudaily.ui.adapter.DailyNewsListAdapter;
import com.purezhihudaily.ui.widget.SlideListView;
import com.purezhihudaily.ui.widget.SwipeRefreshLayout;
import com.purezhihudaily.utils.ChangeDateUtils;
import com.purezhihudaily.utils.DateUtils;
import com.purezhihudaily.utils.ListUtils;
import com.purezhihudaily.utils.LogUtils;
import com.purezhihudaily.utils.MyAsyncTask;
import com.purezhihudaily.utils.ZhihuDailyUtils;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class DailyNewsListFragment extends GenericFragment implements SwipeRefreshLayout.OnRefreshListener, HttpResponseListener {

	private SwipeRefreshLayout swipeRefreshLayout;
	
	private SlideListView slideListView;

	private DailyNewsListAdapter adapter;

	private String currentDate;

	private DailyNewsList dailyNewsList;

	private int previousLast = 0;

	private boolean ifRefresh = true;

	private Toolbar toolbar;

	public void setToolbar(Toolbar toolbar) {
		this.toolbar = toolbar;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dailynews_list, container, false);

		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe_refresh);
		slideListView = (SlideListView) view.findViewById(R.id.list_dailynews);
		slideListView.setOnItemClickListener(onItemClickListener);
		slideListView.initSlideMode(SlideListView.MOD_RIGHT);
		slideListView.setToolbar(toolbar);

		// 为ListView添加一个Header，这个Header与ToolBar一样高。这样我们可以正确的看到列表中的第一个元素而不被遮住。
		View header = new View(getActivity());
		header.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material)));
		header.setBackgroundColor(getResources().getColor(R.color.tranparent));
		slideListView.addHeaderView(header, null, false);

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
					// 这里没有判断(firstVisibleItem == lastPosition && state == SCROLL_STATE_FLING)的情况，
					// 但是如果列表中的单个item如果很长的话还是要判断的，只不过代码又要多几行
					// 但是可以取巧一下，在触发滑动的时候拖动执行一下animateHide()或者animateBack()——本例中的话就写在那个点击事件里就可以了）
					// BTW，如果列表的滑动纯是靠手滑动列表，而没有类似于点击一个按钮滚到某个位置的话，只要第一个if就够了…

				}
				lastPosition = firstVisibleItem;
				
				final int lastItem = firstVisibleItem + visibleItemCount;

				// 滑动到底部加载
				if (lastItem == totalItemCount) {
					if (previousLast != lastItem) {
						onLoad();
						previousLast = lastItem;
					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				state = scrollState;
			}
		});

		onRefresh();

		return view;
	}

	@Override
	public void onRefresh() {
		ifRefresh = true;
		new GetLatestNewsTask(getActivity(), this).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
	}

	public void onLoad() {
		ifRefresh = false;
		currentDate = ChangeDateUtils.getBeforeDate(currentDate);
		new GetOutdatedNewsTask(getActivity(), this).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR, currentDate);
	}

	@Override
	public void onPreExecute() {
		swipeRefreshLayoutControl(true);
	}

	@Override
	public void onPostExecute(GenericModel resultFrom, boolean isRefreshSuccess) {
		if (!isAdded()) {
			swipeRefreshLayoutControl(false);
			return;
		}

		if (getView() != null) {
			slideListView.setVisibility(View.VISIBLE);
		}

		if (resultFrom == null) {
			swipeRefreshLayoutControl(false);
			Crouton.makeText(getActivity(), getResources().getString(R.string.cannot_refresh), Style.ALERT, R.id.crouton_handle).show();
			return;
		}

		DailyNewsList result = (DailyNewsList) resultFrom;

		if (isRefreshSuccess) {

			if (dailyNewsList == null) {
				dailyNewsList = new DailyNewsList();
			}

			// 排除今天的数据
			if (DateUtils.getCurrentDate(DateUtils.MMDD).equals(DateUtils.getFormatTime(result.date, DateUtils.YYYYMMDD, DateUtils.MMDD))) {
				if (dailyNewsList.stories.size() > 0) {
					swipeRefreshLayoutControl(false);
					return;
				}
			}

			// 添加标题
			if (result != null && !ListUtils.isEmpty(result.stories)) {
				DailyNews dailyNews = new DailyNews();
				dailyNews.isTag = true;
				dailyNews.title = result.date;
				dailyNewsList.stories.add(dailyNews);
			}

			dailyNewsList.stories.addAll(result.stories);
			dailyNewsList.date = result.date;
			
			currentDate = result.date;
			
			updateAdapter();

		}
		else {
			Crouton.makeText(getActivity(), getResources().getString(R.string.cannot_refresh), Style.ALERT, R.id.crouton_handle).show();
		}

		swipeRefreshLayoutControl(false);

	}

	@Override
	public void onFail(Exception e) {
		swipeRefreshLayoutControl(false);
		Crouton.makeText(getActivity(), getResources().getString(R.string.cannot_refresh), Style.ALERT, R.id.crouton_handle).show();

		LogUtils.getInstance().e(e);
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
			DailyNews dailyNews = dailyNewsList.stories.get(position - ((ListView) parent).getHeaderViewsCount());
			
			if (dailyNews.isTag) {
				return;
			}
			
			try {
				ZhihuDailyUtils.insertOrUpdateRecord(dailyNews);
				ZhihuDailyUtils.setReadNews(dailyNewsList.stories, dailyNews);
			}
			catch (SQLException e) {
				LogUtils.getInstance().e(e);
			}
			
			updateAdapter();
			
			Intent intent = new Intent();
			intent.putExtra("id", dailyNews.id);
			intent.setClass(getActivity(), DailyNewsDetailActivity.class);
			getActivity().startActivity(intent);
		}

	};

	private void swipeRefreshLayoutControl(final boolean bool) {
		if (ifRefresh) {
			swipeRefreshLayout.post(new Runnable() {
				@Override
				public void run() {
					swipeRefreshLayout.setRefreshing(bool);
				}
			});
		}
	}
	
	private void updateAdapter() {
		if (adapter == null) {
			adapter = new DailyNewsListAdapter(getActivity(), false);
			slideListView.setAdapter(adapter);
		}
		adapter.setDataList(dailyNewsList.stories);
		adapter.notifyDataSetChanged();
	}

}
