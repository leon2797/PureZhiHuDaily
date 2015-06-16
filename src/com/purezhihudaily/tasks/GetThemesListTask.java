package com.purezhihudaily.tasks;

import android.content.Context;

import com.purezhihudaily.framework.Constants;
import com.purezhihudaily.framework.db.domain.DailyNewsThemesList;
import com.purezhihudaily.framework.db.model.GenericModel;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.utils.GsonUtils;
import com.purezhihudaily.utils.ListUtils;
import com.purezhihudaily.utils.NetWorkUtils;

public class GetThemesListTask extends BaseTask {

	public GetThemesListTask(Context context, HttpResponseListener responseListener) {
		super(context, responseListener);
	}
	
	@Override
	protected GenericModel doInBackground(String... params) {

		DailyNewsThemesList themesList = new DailyNewsThemesList();

		try {
			if (NetWorkUtils.isNetworkAvailable(context)) {
				
				String content = getUrl(Constants.ZHIHU_DAILY_THEMES_LIST);
				themesList = (DailyNewsThemesList) GsonUtils.getEntity(content, DailyNewsThemesList.class);
				
				isRefreshSuccess = !ListUtils.isEmpty(themesList.others);
				
			}
		}
		catch (Exception e) {
			this.isRefreshSuccess = false;
			this.e = e;
		}

		return themesList;
	}

}
