package com.purezhihudaily.ui.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.purezhihudaily.R;

public abstract class MultiViewTypeBaseAdapter<T> extends BaseAdapter {

	protected Context context;
	protected ArrayList<T> dataList;
	protected DisplayImageOptions options = null;
	protected ImageLoadingListener listener = new AnimateFirstDisplayListener();

	public MultiViewTypeBaseAdapter(Context context) {
		this.context = context;
		initImageLoader(context);
		initOptions();
	}

	public void setDataList(List<T> list) {
		this.dataList = list == null ? new ArrayList<T>() : new ArrayList<T>(list);
	}

	@Override
	public int getCount() {
		return dataList != null ? dataList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		if (position >= dataList.size())
			return null;
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 使用该getItemView方法替换原来的getView方法中部分功能，需要子类实现
	 * 
	 * @param position
	 * @param convertView
	 * @param holder
	 * @param type
	 * @return
	 */
	public abstract View getItemView(int position, View convertView, ViewHolder holder, int type,
			ViewGroup parent);

	public class ViewHolder {
		private SparseArray<View> views = new SparseArray<View>();
		private View convertView;

		public ViewHolder(View convertView) {
			this.convertView = convertView;
		}

		@SuppressWarnings({ "hiding", "unchecked" })
		public <T extends View> T getView(int resId) {
			View v = views.get(resId);
			if (null == v) {
				v = convertView.findViewById(resId);
				views.put(resId, v);
			}
			return (T) v;
		}
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	private void initOptions() {
		options = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(1))
				.showImageForEmptyUri(R.drawable.ic_empty).showImageOnLoading(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true).cacheOnDisk(true).build();
	}

	private static void initImageLoader(Context context) {
		if (ImageLoader.getInstance().isInited()) {
			ImageLoader.getInstance().clearMemoryCache();
			ImageLoader.getInstance().destroy();
		}
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024).tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

}
