package com.purezhihudaily.framework.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.purezhihudaily.framework.ApplicationHelper;
import com.purezhihudaily.framework.Constants;
import com.purezhihudaily.framework.db.dao.CollectDAO;
import com.purezhihudaily.framework.db.dao.NewsDAO;
import com.purezhihudaily.framework.db.dao.RecordDAO;
import com.purezhihudaily.framework.db.model.CollectModel;
import com.purezhihudaily.framework.db.model.NewsModel;
import com.purezhihudaily.framework.db.model.RecordModel;
import com.purezhihudaily.utils.LogUtils;

/**
 * ormlite帮助类
 * 
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static DatabaseHelper databaseHelper;
	
	private NewsDAO newsDAO;
	
	private RecordDAO recordDAO;
	
	private CollectDAO collectDAO;

	/**
	 * DatabaseHelper 数据库访问类
	 * 
	 * @param context
	 */
	private DatabaseHelper(final Context context) {
		super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
	}

	public static DatabaseHelper getInstance() {
		if (databaseHelper == null) {
			synchronized (DatabaseHelper.class) {
				return new DatabaseHelper(ApplicationHelper.getApplication().getApplicationContext());
			}
		}
		else {
			return databaseHelper;
		}
	}

	@Override
	public ConnectionSource getConnectionSource() {
		return super.getConnectionSource();
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		try {
			TableUtils.createTableIfNotExists(connectionSource, NewsModel.class);
			TableUtils.createTableIfNotExists(connectionSource, RecordModel.class);
			TableUtils.createTableIfNotExists(connectionSource, CollectModel.class);
		}
		catch (SQLException e) {
			LogUtils.getInstance().e(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, NewsModel.class, true);
			TableUtils.dropTable(connectionSource, RecordModel.class, true);
			TableUtils.dropTable(connectionSource, CollectModel.class, true);
			TableUtils.createTableIfNotExists(connectionSource, NewsModel.class);
			TableUtils.createTableIfNotExists(connectionSource, RecordModel.class);
			TableUtils.createTableIfNotExists(connectionSource, CollectModel.class);
		}
		catch (SQLException e) {
			LogUtils.getInstance().e(e);
		}
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	@Override
	public void close() {
		super.close();
	}

	public NewsDAO getNewsDAO() throws SQLException {
		if(newsDAO == null) {
			newsDAO = new NewsDAO();
		}
		return newsDAO;
	}

	public RecordDAO getRecordDAO() throws SQLException {
		if(recordDAO == null) {
			recordDAO = new RecordDAO();
		}
		return recordDAO;
	}
	
	public CollectDAO getCollectDAO() throws SQLException {
		if(collectDAO == null) {
			collectDAO = new CollectDAO();
		}
		return collectDAO;
	}

}
