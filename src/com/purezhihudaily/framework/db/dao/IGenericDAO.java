package com.purezhihudaily.framework.db.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

/**
 * DAO接口
 *
 * @param <T>
 */
public interface IGenericDAO<T> {
	
	/**
	 * 根据id查询
	 * 
	 * @param Serializable
	 * @return T
	 * @throws SQLException
	 */
	public T queryById(Serializable id) throws SQLException;
	
	/**
	 * 查询全部
	 * 
	 * @return List<T>
	 * @throws SQLException 
	 */
	public List<T> queryForAll() throws SQLException;
	
	/**
	 * 新增
	 * 
	 * @param T
	 * @return int
	 * @throws SQLException 
	 */
	public int create(T object) throws SQLException;
	
	/**
	 * 新增或更新
	 * 
	 * @param T
	 * @return CreateOrUpdateStatus 
	 * @throws SQLException 
	 */
	public CreateOrUpdateStatus createOrUpdate(T object) throws SQLException;

	/**
	 * 更新
	 * 
	 * @param T
	 * @return int 
	 * @throws SQLException 
	 */
	public int update(T object) throws SQLException;

	/**
	 * 删除
	 * 
	 * @param T
	 * @return int 
	 * @throws SQLException 
	 */
	public int delete(T object) throws SQLException;
	
	/**
	 * 根据id删除
	 * 
	 * @param Serializable
	 * @return int 
	 * @throws SQLException 
	 */
	public int deleteById(Serializable id) throws SQLException;
	
	/**
	 * 删除全部
	 * 
	 * @return int 
	 * @throws SQLException 
	 */
	public int deleteAll() throws SQLException;

	/**
	 * 执行Sql语句
	 * 
	 * @param T
	 * @return int 
	 * @throws SQLException 
	 */
	public int executeSql(String sql) throws SQLException;
	
	/**
	 * 获得可读数据库
	 * @return
	 */
	public SQLiteDatabase getReadableDatabase();
	
	/**
	 * 获得可写数据库
	 * @return
	 */
	public SQLiteDatabase getWritableDatabase();
	
}
