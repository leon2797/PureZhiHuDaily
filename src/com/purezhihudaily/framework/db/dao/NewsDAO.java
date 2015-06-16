package com.purezhihudaily.framework.db.dao;

import java.sql.SQLException;

import android.database.Cursor;

import com.purezhihudaily.framework.db.model.NewsModel;
import com.purezhihudaily.utils.LogUtils;

public class NewsDAO extends GenericDAO<NewsModel> {

	private final static String TABLE_NAME = "News";

	public NewsDAO() throws SQLException {
		super();
	}

	public String findNewsByKey(String key) {
		String result = null;
		String querySql = "select * from " + TABLE_NAME + " where key = " + key;
		Cursor cursor = null;
		try {
			if (getReadableDatabase() != null) {
				cursor = getReadableDatabase().rawQuery(querySql, null);
				if (cursor != null) {
					if (cursor.moveToNext()) {
						result = cursor.getString(0);
					}
				}
			}
		}
		catch (Exception e) {
			LogUtils.getInstance().e(e);
		}
		finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return result;
	}

}
