package com.purezhihudaily.ui.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.purezhihudaily.R;
import com.purezhihudaily.framework.db.domain.DailyNewsCommentsList;
import com.purezhihudaily.framework.db.model.GenericModel;
import com.purezhihudaily.tasks.GetCommentsTask;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.ui.adapter.DailyNewsCommentsListAdapter;
import com.purezhihudaily.ui.widget.SlideListView;
import com.purezhihudaily.utils.LogUtils;
import com.purezhihudaily.utils.MyAsyncTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class DailyNewsCommentsActivity extends SwipeActivity implements HttpResponseListener {

	private SlideListView slideListView;

	private DailyNewsCommentsListAdapter adapter;

	private DailyNewsCommentsList commentsList;
	
	private long dailyNewsId = 0;
	
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dailynews_comments);
		
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}

		slideListView = (SlideListView) findViewById(R.id.list_dailynews);
		slideListView.initSlideMode(SlideListView.MOD_FORBID);
		slideListView.setToolbar(toolbar);
		
		if (savedInstanceState == null) {
			dailyNewsId = getIntent().getLongExtra("id", 0);
		}
		else {
			dailyNewsId = savedInstanceState.getLong("ID");
		}

		// 为ListView添加一个Header，这个Header与ToolBar一样高。这样我们可以正确的看到列表中的第一个元素而不被遮住。
		View header = new View(this);
		header.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material)));
		header.setBackgroundColor(Color.parseColor("#00000000"));
		slideListView.addHeaderView(header, null, false);
		
		new GetCommentsTask(this, this).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(dailyNewsId));
		
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

		outState.putLong("ID", dailyNewsId);
	}

	@Override
	public void onPreExecute() {

	}

	@Override
	public void onPostExecute(GenericModel resultFrom, boolean isRefreshSuccess) {
		if (resultFrom == null) {
			return;
		}

		DailyNewsCommentsList result = (DailyNewsCommentsList) resultFrom;
		commentsList = result;
		
		toolbar.setTitle(commentsList.comments.size() + "条评论");
		
		if (isRefreshSuccess) {
			updateAdapter();
		}
	}

	@Override
	public void onFail(Exception e) {
		Crouton.makeText(this, getResources().getString(R.string.cannot_refresh_comments), Style.ALERT, R.id.crouton_handle).show();

		LogUtils.getInstance().e(e);
	}

	private void updateAdapter() {
		if (adapter == null) {
			adapter = new DailyNewsCommentsListAdapter(this, true);
			slideListView.setAdapter(adapter);
		}
		adapter.setDataList(commentsList.comments);
		adapter.notifyDataSetChanged();
	}

}
