package com.purezhihudaily.tasks;

import android.content.Context;

import com.purezhihudaily.framework.Constants;
import com.purezhihudaily.framework.db.DatabaseHelper;
import com.purezhihudaily.framework.db.dao.NewsDAO;
import com.purezhihudaily.framework.db.domain.DailyNewsList;
import com.purezhihudaily.framework.db.model.GenericModel;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.utils.DateUtils;
import com.purezhihudaily.utils.GsonUtils;
import com.purezhihudaily.utils.ListUtils;
import com.purezhihudaily.utils.NetWorkUtils;
import com.purezhihudaily.utils.ZhihuDailyUtils;

/**
 * 从服务器下载最新新闻列表
 * 
 */
public class GetLatestNewsTask extends BaseTask {

	public GetLatestNewsTask(Context context, HttpResponseListener listener) {
		super(context, listener);
	}

	@Override
	protected GenericModel doInBackground(String... params) {

		DailyNewsList newsList = null;

		try {
			String oldContent = null;
			NewsDAO newsDAO = DatabaseHelper.getInstance().getNewsDAO();
			if (NetWorkUtils.isNetworkAvailable(context)) {

				String newContent = getUrl(Constants.ZHIHU_DAILY_LATEST);
				newsList = (DailyNewsList) GsonUtils.getEntity(newContent, DailyNewsList.class);

				String date = newsList != null ? newsList.date : null;
				isRefreshSuccess = !ListUtils.isEmpty(newsList.stories);

				if (date != null) {
					oldContent = newsDAO.findNewsByKey(date);
				}

				if (isRefreshSuccess && !ZhihuDailyUtils.checkIsContentSame(oldContent, newContent)) {
					ZhihuDailyUtils.insertOrUpdateNews(date, newContent);
				}

			}
			else {
				oldContent = newsDAO.findNewsByKey(DateUtils.getCurrentDate(DateUtils.YYYYMMDD));
				
				newsList = (DailyNewsList) GsonUtils.getEntity(oldContent, DailyNewsList.class);
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
