package com.purezhihudaily.framework.db.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.purezhihudaily.framework.db.DatabaseHelper;

/**
 * Dao基类，封装公共的增删查改方法
 * 
 */
public class GenericDAO<T> implements IGenericDAO<T> {
	
	private Dao<T, Serializable> dao;
	
	public Dao<T, Serializable> getDao() {
		return dao;
	}

	/**
	 * 利用泛型反射，获取实际运行时<T>的类型
	 * 利用该实体类型查询Dao
	 */
	@SuppressWarnings("unchecked")
	public GenericDAO() throws SQLException {
		ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type type = parameterizedType.getActualTypeArguments()[0];
		dao = (Dao<T, Serializable>) DatabaseHelper.getInstance().getDao((Class<?>) type);
	}
	
	@Override
	public T queryById(Serializable id) throws SQLException {
		return getDao().queryForId(id);
	}

	@Override
	public List<T> queryForAll() throws SQLException {
		return getDao().queryForAll();
	}

	@Override
	public int create(T object) throws SQLException {
		return getDao().create(object);
	}

	@Override
	public CreateOrUpdateStatus createOrUpdate(T object) throws SQLException {
		return getDao().createOrUpdate(object);
	}

	@Override
	public int update(T object) throws SQLException {
		return getDao().update(object);
	}

	@Override
	public int delete(T object) throws SQLException {
		return getDao().delete(object);
	}

	@Override
	public int deleteById(Serializable id) throws SQLException {
		return getDao().deleteById(id);
	}

	@Override
	public int deleteAll() throws SQLException {
		return getDao().delete(getDao().queryForAll());
	}

	@Override
	public int executeSql(String sql) throws SQLException {
		return getDao().executeRaw(sql);
	}
	
	@Override
	public SQLiteDatabase getReadableDatabase() {
		return DatabaseHelper.getInstance().getReadableDatabase();
	}
	
	@Override
	public SQLiteDatabase getWritableDatabase() {
		return DatabaseHelper.getInstance().getWritableDatabase();
	}

}
