package com.purezhihudaily.tasks;

import android.content.Context;

import com.purezhihudaily.framework.Constants;
import com.purezhihudaily.framework.db.DatabaseHelper;
import com.purezhihudaily.framework.db.dao.NewsDAO;
import com.purezhihudaily.framework.db.domain.DailyNewsList;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.utils.ChangeDateUtils;
import com.purezhihudaily.utils.GsonUtils;
import com.purezhihudaily.utils.ListUtils;
import com.purezhihudaily.utils.NetWorkUtils;
import com.purezhihudaily.utils.ZhihuDailyUtils;

public class GetOutdatedNewsTask extends BaseTask {

	public GetOutdatedNewsTask(Context context, HttpResponseListener listener) {
		super(context, listener);
	}

	@Override
	protected DailyNewsList doInBackground(String... params) {

		DailyNewsList newsList = null;

		String key = params[0];

		try {
			NewsDAO newsDAO = DatabaseHelper.getInstance().getNewsDAO();
			String oldContent = newsDAO.findNewsByKey(key);
			if (oldContent != null) {
				newsList = (DailyNewsList) GsonUtils.getEntity(oldContent, DailyNewsList.class);
				isRefreshSuccess = !ListUtils.isEmpty(newsList.stories);
			}
			else {
				if (NetWorkUtils.isNetworkAvailable(context)) {
					String newContent = getUrl(Constants.ZHIHU_DAILY_BEFORE + ChangeDateUtils.getAddedDate(key));
					newsList = (DailyNewsList) GsonUtils.getEntity(newContent, DailyNewsList.class);
					isRefreshSuccess = !ListUtils.isEmpty(newsList.stories);
					
					if (isRefreshSuccess && !ZhihuDailyUtils.checkIsContentSame(oldContent, newContent)) {
						ZhihuDailyUtils.insertOrUpdateNews(key, newContent);
					}
				}
			}

			if (newsList != null) {
				ZhihuDailyUtils.setReadNewsList(newsList.stories);
			}
		}
		catch (Exception e) {
			this.isRefreshSuccess = false;
			this.e = e;
		}

		return newsList;
	}

}
