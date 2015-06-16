package com.purezhihudaily.ui.adapter;

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.purezhihudaily.R;
import com.purezhihudaily.framework.Constants;
import com.purezhihudaily.framework.db.DatabaseHelper;
import com.purezhihudaily.framework.db.dao.RecordDAO;
import com.purezhihudaily.framework.db.domain.DailyNews;
import com.purezhihudaily.ui.widget.SlideListView;
import com.purezhihudaily.utils.LogUtils;
import com.purezhihudaily.utils.ZhihuDailyUtils;

public class DailyNewsThemesDetailAdapter extends MultiViewTypeBaseAdapter<DailyNews> {

	public DailyNewsThemesDetailAdapter(Context context) {
		super(context);
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.list_news_item, parent, false);
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
		final DailyNews dailyNews = dataList.get(position);
		final SlideListView slideListView = (SlideListView) parent;

		ImageView image = (ImageView) holder.getView(R.id.list_item_image);
		TextView text = (TextView) holder.getView(R.id.list_item_title);
		text.setText(dailyNews.title != null ? dailyNews.title : "");
		if (dailyNews.isRead) {
			text.setTextColor(Color.GRAY);
		}
		else {
			text.setTextColor(Color.BLACK);
		}
		if (dailyNews.images != null && dailyNews.images.size() > 0) {
			String image_url = dailyNews.images.get(0);
			if (image_url != null) {
				if (image_url.contains(Constants.ZHIHU_LIMIT_ADDRESS_1) || image_url.contains(Constants.ZHIHU_LIMIT_ADDRESS_2)) {
					ImageLoader.getInstance().displayImage(null, image, options, listener);
					image.setVisibility(View.GONE);
				}
				else {
					ImageLoader.getInstance().displayImage(image_url, image, options, listener);
					image.setVisibility(View.VISIBLE);
				}
			}
			else {
				ImageLoader.getInstance().displayImage(null, image, options, listener);
				image.setVisibility(View.GONE);
			}
		}
		else {
			ImageLoader.getInstance().displayImage(null, image, options, listener);
			image.setVisibility(View.GONE);
		}
		RelativeLayout collect = (RelativeLayout) holder.getView(R.id.collect);
		RelativeLayout unread = (RelativeLayout) holder.getView(R.id.unread);
		collect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slideListView.slideBack();
				try {
					ZhihuDailyUtils.insertOrUpdateCollect(dailyNews, true);
				}
				catch (SQLException e) {
					LogUtils.getInstance().e(e);
				}
			}
		});
		// 置为未读
		unread.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slideListView.slideBack();
				try {
					RecordDAO recordDAO = DatabaseHelper.getInstance().getRecordDAO();
					if (recordDAO.deleteById(dailyNews.id) == 1) {
						dataList.get(position).isRead = false;
						setDataList(dataList);
					}
				}
				catch (SQLException e) {
					LogUtils.getInstance().e(e);
				}
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

}
