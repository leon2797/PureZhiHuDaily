package com.purezhihudaily.tasks;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.purezhihudaily.framework.db.DatabaseHelper;
import com.purezhihudaily.framework.db.dao.CollectDAO;
import com.purezhihudaily.framework.db.domain.DailyNews;
import com.purezhihudaily.framework.db.domain.DailyNewsList;
import com.purezhihudaily.framework.db.model.CollectModel;
import com.purezhihudaily.tasks.listener.HttpResponseListener;

public class GetCollectNewsTask extends BaseTask {

	public GetCollectNewsTask(Context context, HttpResponseListener listener) {
		super(context, listener);
	}

	@Override
	protected DailyNewsList doInBackground(String... params) {

		DailyNewsList newsList = null;
		List<CollectModel> collectionList = new ArrayList<CollectModel>();
		
		try {
			CollectDAO collectDAO = DatabaseHelper.getInstance().getCollectDAO();
			collectionList = collectDAO.queryForAll();
			if (!collectionList.isEmpty()) {
				newsList = new DailyNewsList();
				for(CollectModel collectModel : collectionList) {
					DailyNews dailyNews = new DailyNews();
					dailyNews.id = Long.valueOf(collectModel.getNewsid());
					dailyNews.title = collectModel.getTitle();
					dailyNews.images = new ArrayList<String>();
					dailyNews.images.add(0, collectModel.getImage());
					dailyNews.isTheme = collectModel.getIsTheme();
					newsList.stories.add(dailyNews);
				}
			}
		}
		catch (Exception e) {
			this.isRefreshSuccess = false;
			this.e = e;
		}

		return newsList;
	}

}
