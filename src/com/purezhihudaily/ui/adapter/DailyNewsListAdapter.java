package com.purezhihudaily.ui.adapter;

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.purezhihudaily.R;
import com.purezhihudaily.framework.Constants;
import com.purezhihudaily.framework.db.DatabaseHelper;
import com.purezhihudaily.framework.db.dao.CollectDAO;
import com.purezhihudaily.framework.db.dao.RecordDAO;
import com.purezhihudaily.framework.db.domain.DailyNews;
import com.purezhihudaily.ui.widget.SlideListView;
import com.purezhihudaily.utils.ChangeDateUtils;
import com.purezhihudaily.utils.LogUtils;
import com.purezhihudaily.utils.ZhihuDailyUtils;

public class DailyNewsListAdapter extends MultiViewTypeBaseAdapter<DailyNews> {

	private boolean ifCollectMode;

	public DailyNewsListAdapter(Context context, boolean ifCollectMode) {
		super(context);
		this.ifCollectMode = ifCollectMode;
	}

	@Override
	public int getItemViewType(int position) {
		if (ifCollectMode) {
			return 2;
		}
		else {
			boolean type = dataList.get(position).isTag;
			if (type) {
				return 0;
			}
			else {
				return 1;
			}
		}
	}

	@Override
	public int getViewTypeCount() {
		if (ifCollectMode) {
			return 1;
		}
		else {
			return 2;
		}
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder0 = null;
		ViewHolder holder1 = null;
		ViewHolder holder2 = null;

		int type = getItemViewType(position);

		switch (type) {
		case 0:
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.list_date_item, parent, false);
				holder0 = new ViewHolder(convertView);
				convertView.setTag(holder0);
			}
			else {
				holder0 = (ViewHolder) convertView.getTag();
			}

			return getItemView(position, convertView, holder0, type, parent);
		case 1:
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.list_news_item, parent, false);
				holder1 = new ViewHolder(convertView);
				convertView.setTag(holder1);
			}
			else {
				holder1 = (ViewHolder) convertView.getTag();
			}

			return getItemView(position, convertView, holder1, type, parent);
		case 2:
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.list_collect_item, parent, false);
				holder2 = new ViewHolder(convertView);
				convertView.setTag(holder2);
			}
			else {
				holder2 = (ViewHolder) convertView.getTag();
			}

			return getItemView(position, convertView, holder2, type, parent);
		}

		return convertView;
	}

	@Override
	public View getItemView(final int position, View convertView, ViewHolder holder, int type, final ViewGroup parent) {
		final DailyNews dailyNews = dataList.get(position);
		final SlideListView slideListView = (SlideListView) parent;

		switch (type) {
		case 0:
			TextView title = (TextView) holder.getView(R.id.date_text);
			title.setText(ChangeDateUtils.getDateTag(context, dailyNews.title));
			break;
		case 1:
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
				ImageLoader.getInstance().displayImage(image_url, image, options, listener);
				image.setVisibility(View.VISIBLE);
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
						ZhihuDailyUtils.insertOrUpdateCollect(dailyNews, false);
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
			break;
		case 2:
			ImageView image_collect = (ImageView) holder.getView(R.id.list_item_image);
			TextView text_collect = (TextView) holder.getView(R.id.list_item_title);
			text_collect.setText(dailyNews.title != null ? dailyNews.title : "");
			if (dailyNews.isTheme) {
				if (dailyNews.images != null && dailyNews.images.size() > 0) {
					String image_url = dailyNews.images.get(0);
					if (image_url != null) {
						if (image_url.contains(Constants.ZHIHU_LIMIT_ADDRESS_1) || image_url.contains(Constants.ZHIHU_LIMIT_ADDRESS_2)) {
							ImageLoader.getInstance().displayImage(null, image_collect, options, listener);
							image_collect.setVisibility(View.GONE);
						}
						else {
							ImageLoader.getInstance().displayImage(image_url, image_collect, options, listener);
							image_collect.setVisibility(View.VISIBLE);
						}
					}
					else {
						ImageLoader.getInstance().displayImage(null, image_collect, options, listener);
						image_collect.setVisibility(View.GONE);
					}

				}
				else {
					ImageLoader.getInstance().displayImage(null, image_collect, options, listener);
					image_collect.setVisibility(View.GONE);
				}
			}
			else {
				if (dailyNews.images != null && dailyNews.images.size() > 0) {
					String image_url = dailyNews.images.get(0);
					ImageLoader.getInstance().displayImage(image_url, image_collect, options, listener);
					image_collect.setVisibility(View.VISIBLE);
				}
				else {
					ImageLoader.getInstance().displayImage(null, image_collect, options, listener);
					image_collect.setVisibility(View.GONE);
				}
			}
			RelativeLayout delete = (RelativeLayout) holder.getView(R.id.delete);
			delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					slideListView.slideBack();
					try {
						CollectDAO collectDAO = DatabaseHelper.getInstance().getCollectDAO();
						if (collectDAO.deleteById(dailyNews.id) == 1) {
							dataList.remove(position);
						}
					}
					catch (SQLException e) {
						LogUtils.getInstance().e(e);
					}
					notifyDataSetChanged();
				}
			});
			break;
		}

		return convertView;
	}

}
