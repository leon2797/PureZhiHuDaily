package com.purezhihudaily.framework.db.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.purezhihudaily.framework.db.domain.DailyNews;
import com.purezhihudaily.framework.db.model.RecordModel;
import com.purezhihudaily.utils.LogUtils;

public class RecordDAO extends GenericDAO<RecordModel> {

	private final static String TABLE_NAME = "Record";

	public RecordDAO() throws SQLException {
		super();
	}

	public List<String> findNewsIsRead(List<DailyNews> newsList) {
		List<String> readList = new ArrayList<String>();
		String querySql = "select newsId from " + TABLE_NAME + " where isRead = " + 1;
		Cursor cursor = null;
		try {
			if (getReadableDatabase() != null) {
				cursor = getReadableDatabase().rawQuery(querySql, null);
				if (cursor != null) {
					while (cursor.moveToNext()) {
						readList.add(cursor.getString(0));
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
		return readList;
	}

}
