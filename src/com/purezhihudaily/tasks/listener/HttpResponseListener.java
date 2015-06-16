package com.purezhihudaily.tasks.listener;

import com.purezhihudaily.framework.db.model.GenericModel;

public interface HttpResponseListener {
	
	public void onPreExecute();

	public void onPostExecute(GenericModel result, boolean isRefreshSuccess);

	public void onFail(Exception e);
}
