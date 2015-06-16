package com.purezhihudaily.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.purezhihudaily.R;
import com.purezhihudaily.framework.db.domain.DailyNewsThemes;

public class DailyNewsThemesListAdapter extends MultiViewTypeBaseAdapter<DailyNewsThemes> {

	public DailyNewsThemesListAdapter(Context context, boolean ifCollectMode) {
		super(context);
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.list_themes_item, parent, false);
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
		DailyNewsThemes dailyNewsThemes = dataList.get(position);
		
		ImageView thumbnail = (ImageView) holder.getView(R.id.thumbnail);
		TextView name = (TextView) holder.getView(R.id.name);
		TextView description = (TextView) holder.getView(R.id.description);
		
		ImageLoader.getInstance().displayImage(dailyNewsThemes.thumbnail, thumbnail, options, listener);
		name.setText(dailyNewsThemes.name);
		description.setText(dailyNewsThemes.description);
		
		return convertView;
	}

}
