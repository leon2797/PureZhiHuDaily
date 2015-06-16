package com.purezhihudaily.tasks;

import android.content.Context;

import com.purezhihudaily.framework.Constants;
import com.purezhihudaily.framework.db.domain.DailyNewsCommentsList;
import com.purezhihudaily.framework.db.model.GenericModel;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.utils.GsonUtils;
import com.purezhihudaily.utils.ListUtils;
import com.purezhihudaily.utils.NetWorkUtils;

public class GetCommentsTask extends BaseTask {

	public GetCommentsTask(Context context, HttpResponseListener responseListener) {
		super(context, responseListener);
	}
	
	@Override
	protected GenericModel doInBackground(String... params) {

		DailyNewsCommentsList commentsList = new DailyNewsCommentsList();

		String id = params[0];

		try {
			if (NetWorkUtils.isNetworkAvailable(context)) {
				
				String long_content = getUrl(Constants.ZHIHU_DAILY_LONG_COMMENTS.replace("#{id}", id));
				DailyNewsCommentsList longCommentsList = (DailyNewsCommentsList) GsonUtils.getEntity(long_content, DailyNewsCommentsList.class);
				commentsList.comments.addAll(longCommentsList.comments);
				
				String short_content = getUrl(Constants.ZHIHU_DAILY_SHORT_COMMENTS.replace("#{id}", id));
				DailyNewsCommentsList shortCommentsList = (DailyNewsCommentsList) GsonUtils.getEntity(short_content, DailyNewsCommentsList.class);
				commentsList.comments.addAll(shortCommentsList.comments);
				
				isRefreshSuccess = !ListUtils.isEmpty(commentsList.comments);
				
			}
		}
		catch (Exception e) {
			this.isRefreshSuccess = false;
			this.e = e;
		}

		return commentsList;
	}

}
