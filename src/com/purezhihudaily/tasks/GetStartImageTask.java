package com.purezhihudaily.tasks;

import android.content.Context;

import com.purezhihudaily.framework.Constants;
import com.purezhihudaily.framework.db.domain.DailyNewStartImage;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.utils.GsonUtils;
import com.purezhihudaily.utils.NetWorkUtils;

public class GetStartImageTask extends BaseTask {

	public GetStartImageTask(Context context, HttpResponseListener listener) {
		super(context, listener);
	}

	@Override
	protected DailyNewStartImage doInBackground(String... params) {

		DailyNewStartImage image = null;

		try {
			if (NetWorkUtils.isNetworkAvailable(context)) {
				String content = getUrl(Constants.ZHIHU_DAILY_START);
				image = (DailyNewStartImage) GsonUtils.getEntity(content, DailyNewStartImage.class);
			}
		}
		catch (Exception e) {
			this.isRefreshSuccess = false;
			this.e = e;
		}

		return image;
	}

}
