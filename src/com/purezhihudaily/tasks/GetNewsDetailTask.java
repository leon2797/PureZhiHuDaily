package com.purezhihudaily.tasks;

import android.content.Context;

import com.purezhihudaily.framework.Constants;
//import com.purezhihudaily.framework.db.DatabaseHelper;
//import com.purezhihudaily.framework.db.dao.NewsDAO;
import com.purezhihudaily.framework.db.domain.DailyNewsDetail;
import com.purezhihudaily.framework.db.model.GenericModel;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.utils.GsonUtils;
import com.purezhihudaily.utils.NetWorkUtils;
//import com.purezhihudaily.utils.ZhihuDailyUtils;

/**
 * 下载新闻详情页内容
 */
public class GetNewsDetailTask extends BaseTask {

	public GetNewsDetailTask(Context context, HttpResponseListener listener) {
		super(context, listener);
	}

	@Override
	protected GenericModel doInBackground(String... params) {
		DailyNewsDetail dailyNews = null;

		try {
			String key = params[0];

//			NewsDAO newsDAO = DatabaseHelper.getInstance().getNewsDAO();
//			String oldContent = newsDAO.findNewsByKey(key);
//			dailyNews = (DailyNewsDetail) GsonUtils.getEntity(oldContent, DailyNewsDetail.class);
//			isRefreshSuccess = (dailyNews != null);
			
			if (dailyNews == null) {
				if (NetWorkUtils.isNetworkAvailable(context)) {
					String newContent = getUrl(Constants.ZHIHU_DAILY_DETAIL + key);
					dailyNews = (DailyNewsDetail) GsonUtils.getEntity(newContent, DailyNewsDetail.class);
					isRefreshSuccess = (dailyNews != null);

//					if (isRefreshSuccess && !ZhihuDailyUtils.checkIsContentSame(oldContent, newContent)) {
//						ZhihuDailyUtils.insertOrUpdateNews(key, newContent);
//					}
				}
			}

		}
		catch (Exception e) {
			this.isRefreshSuccess = false;
			this.e = e;
		}

		return dailyNews;
	}
}
