package com.purezhihudaily.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.purezhihudaily.R;
import com.purezhihudaily.framework.db.domain.DailyNewsComments;
import com.purezhihudaily.utils.DateUtils;

public class DailyNewsCommentsListAdapter extends MultiViewTypeBaseAdapter<DailyNewsComments> {

	public DailyNewsCommentsListAdapter(Context context, boolean ifCollectMode) {
		super(context);
		initOptions();
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.list_comments_item, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		return getItemView(position, convertView, holder, 0, parent);
		
	}

	@Override
	public View getItemView(final int position, View convertView, ViewHolder holder, int type, final ViewGroup parent) {
		DailyNewsComments dailyNewsComments = dataList.get(position);
		
		ImageView avatar = (ImageView) holder.getView(R.id.avatar);
		TextView author = (TextView) holder.getView(R.id.author);
		TextView content = (TextView) holder.getView(R.id.content);
		TextView likes = (TextView) holder.getView(R.id.likes);
		TextView time = (TextView) holder.getView(R.id.time);
		
		ImageLoader.getInstance().displayImage(dailyNewsComments.avatar, avatar, options, listener);
		author.setText(dailyNewsComments.author);
		content.setText(dailyNewsComments.content);
		likes.setText(dailyNewsComments.likes);
		time.setText(DateUtils.timestampToString(dailyNewsComments.time, DateUtils.MM_DD_HH_MM));
		
		return convertView;
	}
	
	private void initOptions() {
		options = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(100))
				.showImageForEmptyUri(R.drawable.ic_empty).showImageOnLoading(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true).cacheOnDisk(true).build();
	}

}
