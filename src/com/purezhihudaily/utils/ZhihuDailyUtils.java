package com.purezhihudaily.utils;

import java.sql.SQLException;
import java.util.List;

import android.text.TextUtils;

import com.purezhihudaily.framework.Constants;
import com.purezhihudaily.framework.db.DatabaseHelper;
import com.purezhihudaily.framework.db.dao.CollectDAO;
import com.purezhihudaily.framework.db.dao.NewsDAO;
import com.purezhihudaily.framework.db.dao.RecordDAO;
import com.purezhihudaily.framework.db.domain.DailyNews;
import com.purezhihudaily.framework.db.model.CollectModel;
import com.purezhihudaily.framework.db.model.NewsModel;
import com.purezhihudaily.framework.db.model.RecordModel;

/**
 * 自定义工具类
 * 
 */
public class ZhihuDailyUtils {

	/**
	 * 数据库插入或更新新闻
	 * 
	 * @param key
	 * @param newContent
	 * @throws SQLException
	 */
	public static void insertOrUpdateNews(String key, String newContent) throws SQLException {
		NewsDAO newsDAO = DatabaseHelper.getInstance().getNewsDAO();
		NewsModel newsModel = new NewsModel();
		newsModel.setId(SystemUtils.generateUUID());
		newsModel.setKey(key);
		newsModel.setContent(newContent);
		newsDAO.createOrUpdate(newsModel);
	}

	/**
	 * 数据库插入或更新记录已读新闻
	 * 
	 * @param dailyNews
	 * @throws SQLException
	 */
	public static void insertOrUpdateRecord(DailyNews dailyNews) throws SQLException {
		RecordDAO recordDAO = DatabaseHelper.getInstance().getRecordDAO();
		RecordModel recordModel = new RecordModel();
		recordModel.setNewsId(String.valueOf(dailyNews.id));
		recordModel.setIsRead(true);
		recordDAO.createOrUpdate(recordModel);
	}

	/**
	 * 数据库插入或更新收藏夹
	 * 
	 * @param dailyNews
	 * @throws SQLException
	 */
	public static void insertOrUpdateCollect(DailyNews dailyNews, boolean isTheme) throws SQLException {
		CollectDAO collectDAO = DatabaseHelper.getInstance().getCollectDAO();
		CollectModel collectModel = new CollectModel();
		collectModel.setNewsid(String.valueOf(dailyNews.id));
		collectModel.setTitle(dailyNews.title);
		collectModel.setIsTheme(isTheme);
		if (isTheme) {
			if (dailyNews.images != null && dailyNews.images.size() > 0) {
				String image_url = dailyNews.images.get(0);
				if (image_url != null) {
					if (image_url.contains(Constants.ZHIHU_LIMIT_ADDRESS_1) || image_url.contains(Constants.ZHIHU_LIMIT_ADDRESS_2)) {
						collectModel.setImage(null);
					}
					else {
						collectModel.setImage(dailyNews.images.get(0));
					}
				}
				else {
					collectModel.setImage(null);
				}
			}
			else {
				collectModel.setImage(null);
			}
		}
		else {
			if (dailyNews.images != null && dailyNews.images.size() > 0) {
				collectModel.setImage(dailyNews.images.get(0));
			}
			else {
				collectModel.setImage(null);
			}
		}
		collectDAO.createOrUpdate(collectModel);
	}

	/**
	 * 判断内容是否相同
	 * 
	 * @param oldContent
	 * @param newContent
	 * @return
	 */
	public static boolean checkIsContentSame(String oldContent, String newContent) {

		if (TextUtils.isEmpty(oldContent) || TextUtils.isEmpty(newContent)) {
			return false;
		}

		return oldContent.equals(newContent);
	}

	/**
	 * 为List中的某一个newsEntity设置已读标示
	 * 
	 * @param newsList
	 * @param news
	 * @throws SQLException
	 */
	public static void setReadNews(List<DailyNews> newsList, DailyNews news) throws SQLException {

		if (newsList == null || newsList.isEmpty() || news == null)
			return;

		for (int i = 0; i < newsList.size(); i++) {
			if (news.id == newsList.get(i).id) {
				newsList.get(i).isRead = true;
				RecordModel recordModel = new RecordModel();
				recordModel.setNewsId(String.valueOf(newsList.get(i).id));
				recordModel.setIsRead(newsList.get(i).isRead);
				DatabaseHelper.getInstance().getRecordDAO().createOrUpdate(recordModel);
			}
		}
	}

	/**
	 * 为newsList设置已读标示
	 * 
	 * @param newsList
	 * @throws SQLException
	 */
	public static void setReadNewsList(List<DailyNews> newsList) throws SQLException {
		if (newsList == null || newsList.isEmpty())
			return;

		RecordDAO recordDAO = new RecordDAO();

		// 已读新闻列表
		List<String> readList = recordDAO.findNewsIsRead(newsList);

		for (int i = 0; i < newsList.size(); i++) {
			newsList.get(i).isRead = readList.contains(String.valueOf(newsList.get(i).id));
		}
	}

}
