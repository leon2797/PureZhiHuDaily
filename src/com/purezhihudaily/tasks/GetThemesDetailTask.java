package com.purezhihudaily.tasks;

import android.content.Context;

import com.purezhihudaily.framework.Constants;
import com.purezhihudaily.framework.db.domain.DailyNewsThemesDetail;
import com.purezhihudaily.framework.db.model.GenericModel;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.utils.GsonUtils;
import com.purezhihudaily.utils.ListUtils;
import com.purezhihudaily.utils.NetWorkUtils;
import com.purezhihudaily.utils.ZhihuDailyUtils;

public class GetThemesDetailTask extends BaseTask {

	public GetThemesDetailTask(Context context, HttpResponseListener responseListener) {
		super(context, responseListener);
	}
	
	@Override
	protected GenericModel doInBackground(String... params) {

		DailyNewsThemesDetail themesDetail = new DailyNewsThemesDetail();
		
		String id = params[0];

		try {
			if (NetWorkUtils.isNetworkAvailable(context)) {
				
				String content = getUrl(Constants.ZHIHU_DAILY_THEMES_DETAIL + id);
				themesDetail = (DailyNewsThemesDetail) GsonUtils.getEntity(content, DailyNewsThemesDetail.class);
				
				isRefreshSuccess = !ListUtils.isEmpty(themesDetail.stories);
				
			}
			
			if (themesDetail != null) {
				ZhihuDailyUtils.setReadNewsList(themesDetail.stories);
			}
		}
		catch (Exception e) {
			this.isRefreshSuccess = false;
			this.e = e;
		}

		return themesDetail;
	}

}
