package com.purezhihudaily.tasks;

import android.content.Context;

import com.purezhihudaily.framework.db.model.GenericModel;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.utils.MyAsyncTask;
import com.purezhihudaily.utils.http.HttpClientUtils;

import java.io.IOException;

/**
 * 从服务器获取信息基类
 *
 */
public abstract class BaseTask extends MyAsyncTask<String, String, GenericModel> {

	protected Context context = null;

	protected HttpResponseListener responseListener = null;
	
	protected Exception e = null;

	protected boolean isRefreshSuccess = true;
	
	public BaseTask(Context context, HttpResponseListener responseListener) {
		this.context = context;
		this.responseListener = responseListener;
	}
	
	protected String getUrl(String url) throws IOException, Exception {
		return HttpClientUtils.get(context, url, null);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (responseListener != null) {
			responseListener.onPreExecute();
		}
	}

	@Override
	protected void onPostExecute(GenericModel result) {
		super.onPostExecute(result);

		if (isCancelled()) {
			return;
		}

		if (responseListener != null) {
			if (isRefreshSuccess) {
				responseListener.onPostExecute(result, isRefreshSuccess);
			}
			else {
				responseListener.onFail(e);
			}
		}
	}
	
}
