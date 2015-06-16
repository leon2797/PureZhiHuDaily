package com.purezhihudaily.framework;

import java.util.ArrayList;
import java.util.HashMap;

import com.purezhihudaily.framework.exception.ExceptionHandler;
import com.purezhihudaily.utils.FileUtils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * 应用帮助类
 * 
 */
public class ApplicationHelper extends Application {

	private static ApplicationHelper application = null;
	private static ArrayList<Activity> activities = null;
	// 内存存储器，用于存放全局变量，随应用创建和销毁。
	private static HashMap<Object, Object> map = null;
	private static Context context;

	public static Context getContext() {
		return context;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		context = getApplicationContext();
		map = new HashMap<Object, Object>();
		activities = new ArrayList<Activity>();
		// manageException();
	}

	/**
	 * 创建应用文件目录
	 */
	public static void initFolders() {
		FileUtils.createFolder(Constants.APPLICATION_PATH);
		FileUtils.createFolder(Constants.LOG_PATH);
	}

	/**
	 * 获取内存数据
	 * 
	 * @param object
	 * @return
	 */
	public static Object getMap(Object object) {
		return map.get(object);
	}

	/**
	 * 设置内存数据
	 * 
	 * @param key
	 * @param value
	 */
	public static void putMap(Object key, Object value) {
		if (map.containsKey(key)) {
			map.remove(key);
		}
		map.put(key, value);
	}

	/**
	 * 移除对象
	 * 
	 * @param key
	 */
	public static void removerObj(Object key) {
		map.remove(key);
	}

	/**
	 * 清空map
	 */
	public static void clearMap() {
		map.clear();
	}

	/**
	 * 全局异常捕捉处理
	 */
	@SuppressWarnings("unused")
	private void manageException() {
		ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();
		exceptionHandler.init(getApplicationContext());
	}

	/**
	 * 获取应用程序全局对象
	 * 
	 * @return ApplicationHelper
	 */
	public static ApplicationHelper getApplication() {
		return application;
	}

	/**
	 * 将活动的Activity加入到集合中
	 * 
	 * @param activity
	 */
	public static void addActivity(Activity activity) {
		if (!activities.contains(activity)) {
			activities.add(activity);
		}
	}

	/**
	 * 将活动的Activity从集合移除
	 * 
	 * @param activity
	 */
	public static void removeActivity(Activity activity) {
		for (int i = 0; i < activities.size(); i++) {
			if (activity.getLocalClassName().equals(activities.get(i).getLocalClassName())) {
				activities.remove(i);
				break;
			}
		}
	}

	/**
	 * 完全退出程序
	 * 
	 */
	public static void exit() {
		if (activities != null && activities.size() > 0) {
			for (int i = 0; i < activities.size(); i++) {
				activities.get(i).finish();
			}
		}
		System.exit(0);
	}

	/**
	 * 将所有活动的activity finish掉
	 */
	public static void finish() {
		if (activities != null && activities.size() > 0) {
			for (int i = 0; i < activities.size(); i++) {
				activities.get(i).finish();
			}
		}
	}

	public static ArrayList<Activity> getActivities() {
		return activities;
	}

	public static void setActivities(ArrayList<Activity> activities) {
		ApplicationHelper.activities = activities;
	}

}
