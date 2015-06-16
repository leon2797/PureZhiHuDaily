package com.purezhihudaily.ui.activity;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.purezhihudaily.R;
import com.purezhihudaily.framework.db.domain.DailyNewStartImage;
import com.purezhihudaily.framework.db.model.GenericModel;
import com.purezhihudaily.tasks.GetStartImageTask;
import com.purezhihudaily.tasks.listener.HttpResponseListener;
import com.purezhihudaily.utils.LogUtils;
import com.purezhihudaily.utils.MyAsyncTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 欢迎界面
 */
public class WelcomeActivity extends GenericActivity implements HttpResponseListener {

	protected DisplayImageOptions options = null;

	private ImageView image;
	private TextView text;

	private DailyNewStartImage dailyNewStartImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		new GetStartImageTask(this, this).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_welcome);

		image = (ImageView) findViewById(R.id.image_start);
		text = (TextView) findViewById(R.id.image_text);

		initOptions();
		initImageLoader(this);
	}

	@Override
	public void onPreExecute() {

	}

	@Override
	public void onPostExecute(GenericModel resultFrom, boolean isRefreshSuccess) {
		if (resultFrom == null) {
			new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(WelcomeActivity.this, DailyNewsListActivity.class);
					startActivity(intent);
					finish();
				}
			}, 0);
			return;
		}

		dailyNewStartImage = (DailyNewStartImage) resultFrom;

		if (isRefreshSuccess) {

			ImageLoader.getInstance().displayImage(dailyNewStartImage.img, image, options, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View imageView) {
					final ScaleAnimation animation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					animation.setDuration(2000);
					animation.setFillAfter(true);
					imageView.setAnimation(animation);
					animation.startNow();
				}

				@Override
				public void onLoadingFailed(String imageUri, View imageView, FailReason reason) {

				}

				@Override
				public void onLoadingComplete(String imageUri, View imageView, Bitmap bitmao) {
					text.setText(dailyNewStartImage.text);
				}

				@Override
				public void onLoadingCancelled(String imageUri, View imageView) {

				}
			});

			new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(WelcomeActivity.this, DailyNewsListActivity.class);
					startActivity(intent);
					finish();
				}
			}, 2000);
		}
		else {
			new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(WelcomeActivity.this, DailyNewsListActivity.class);
					startActivity(intent);
					finish();
				}
			}, 0);
		}
	}

	@Override
	public void onFail(Exception e) {
		Toast.makeText(this, getResources().getString(R.string.cannot_refresh), Toast.LENGTH_SHORT).show();

		LogUtils.getInstance().e(e);

		finish();
	}

	private void initOptions() {
		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_empty).showImageOnLoading(R.color.tranparent).showImageOnFail(R.drawable.ic_error).cacheInMemory(true).cacheOnDisk(true).build();
	}

	private static void initImageLoader(Context context) {
		if (ImageLoader.getInstance().isInited()) {
			ImageLoader.getInstance().clearMemoryCache();
			ImageLoader.getInstance().destroy();
		}
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(50 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

}
