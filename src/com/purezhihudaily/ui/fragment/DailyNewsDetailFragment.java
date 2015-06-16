package com.purezhihudaily.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;

import com.purezhihudaily.R;
import com.purezhihudaily.framework.db.domain.DailyNewsDetail;
import com.purezhihudaily.framework.db.model.GenericModel;
import com.purezhihudaily.tasks.GetNewsDetailTask;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.ui.widget.SlideWebView;
import com.purezhihudaily.ui.widget.SwipeRefreshLayout;
import com.purezhihudaily.utils.LogUtils;
import com.purezhihudaily.utils.MyAsyncTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class DailyNewsDetailFragment extends GenericFragment implements HttpResponseListener, SwipeRefreshLayout.OnRefreshListener {

	private SwipeRefreshLayout swipeRefreshLayout;

	private SlideWebView webView;

	private long dailyNewsId = 0;

	private DailyNewsDetail dailyNewsDetail;

	private Toolbar toolbar;

	private boolean ifPreStart = false;

	public void setToolbar(Toolbar toolbar) {
		this.toolbar = toolbar;
	}

	public SlideWebView getWebView() {
		return webView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			Bundle bundle = getArguments();
			dailyNewsId = bundle != null ? bundle.getLong("id") : 0;
		}
		else {
			dailyNewsId = savedInstanceState.getLong("ID");
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong("ID", dailyNewsId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dailynews_detail, container, false);

		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe_refresh);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
		swipeRefreshLayout.setEnabled(false);

		webView = (SlideWebView) view.findViewById(R.id.webview);
		webView.setToolbar(toolbar);

		View header = new View(getActivity());
		header.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material)));
		header.setBackgroundColor(getResources().getColor(R.color.accent_material_dark));
		webView.setHeaderView(header);

		setUpWebViewDefaults();

		new GetNewsDetailTask(getActivity(), this).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(dailyNewsId));

		return view;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setUpWebViewDefaults() {

		/**
		 * API19（4.4）以上系统在onPageFinished时再恢复图片加载时，如果存在多张图片引用的是相同的src时，
		 * 会只有一个image标签得到加载 API19（4.4）以下的系统在onPageFinished再再恢复图片加载，减少页面空白时间
		 */
		if (Build.VERSION.SDK_INT >= 19) {
			webView.getSettings().setLoadsImagesAutomatically(true);
		}
		else {
			webView.getSettings().setLoadsImagesAutomatically(false);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		}

		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setUseWideViewPort(true);
		int screenDensity = getResources().getDisplayMetrics().densityDpi;
		// 1.6倍为ZoomDensity / dpi数值
		webView.setInitialScale((int) (screenDensity * 1.6));
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.setWebViewClient(clientHandleByThis);
		webView.setDownloadListener(new DownLoadListener());
		webView.setVerticalScrollBarEnabled(true);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setWebChromeClient(new WebChromeClient());

	}

	/**
	 * 使用当前Webview打开站外内容
	 */
	private WebViewClient clientHandleByThis = new WebViewClient() {

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			view.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			if (!ifPreStart) {
				swipeRefreshLayoutControl(true);
			}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			if (!view.getSettings().getLoadsImagesAutomatically()) {
				view.getSettings().setLoadsImagesAutomatically(true);
			}
			swipeRefreshLayoutControl(false);
			ifPreStart = false;
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			webView.animateBack();
			view.loadUrl(url);
			return false;
		}

	};

	private class DownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
		}
	}

	@Override
	public void onPreExecute() {
		swipeRefreshLayoutControl(true);
		ifPreStart = true;
	}

	@Override
	public void onPostExecute(GenericModel result, boolean isRefreshSuccess) {

		if (!isAdded()) {
			swipeRefreshLayoutControl(false);
			return;
		}

		if (getView() != null) {
			webView.setVisibility(View.VISIBLE);
		}

		if (result == null) {
			swipeRefreshLayoutControl(false);
			Crouton.makeText(getActivity(), getResources().getString(R.string.cannot_refresh), Style.ALERT, R.id.crouton_handle).show();
			return;
		}

		dailyNewsDetail = (DailyNewsDetail) result;

		if (isRefreshSuccess) {

			webView.loadUrl(dailyNewsDetail.share_url);

		}
		else {
			Crouton.makeText(getActivity(), getResources().getString(R.string.cannot_refresh), Style.ALERT, R.id.crouton_handle).show();
		}

	}

	@Override
	public void onFail(Exception e) {
		swipeRefreshLayoutControl(false);
		Crouton.makeText(getActivity(), getResources().getString(R.string.cannot_refresh), Style.ALERT, R.id.crouton_handle).show();
		LogUtils.getInstance().e(e);
	}

	@Override
	public void onRefresh() {
		new GetNewsDetailTask(getActivity(), this).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(dailyNewsId));
	}

	private void swipeRefreshLayoutControl(final boolean bool) {
		swipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(bool);
			}
		});
	}

}
