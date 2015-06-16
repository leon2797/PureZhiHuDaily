package com.purezhihudaily.utils;

import android.util.Log;

/**
 * 日志工具类
 * 
 */
public class LogUtils {

	/** 
	 * 是否开启 
	 */
	private static final boolean IF_DEBUG = true;
	/**
	 *  Log标识，用于Logcat过滤
	 */
	private static final String LOG_TAG = "MyZhihuDaily_log";
	
	private static LogUtils instance = new LogUtils();
	
	private LogUtils() {
		
	}
	
	public static LogUtils getInstance() {
		return instance;
	}

	/**
	 * 获得当前函数名
	 * 
	 * @return
	 */
	private String getFunctionName() {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		if (stackTraceElements == null) {
			return null;
		}
		for (StackTraceElement stackTraceElement : stackTraceElements) {
			if (stackTraceElement.isNativeMethod()) {
				continue;
			}
			if (stackTraceElement.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (stackTraceElement.getClassName().equals(this.getClass().getName())) {
				continue;
			}
			return "[ " + Thread.currentThread().getName() + ": " + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + " " + stackTraceElement.getMethodName() + " ]";
		}
		return null;
	}

	/**
	 * Log，类型info
	 * 
	 * @param string
	 */
	public void i(Object string) {
		if (IF_DEBUG) {
			String name = getFunctionName();
			if (name != null) {
				Log.i(LOG_TAG, name + " - " + string);
			}
			else {
				Log.i(LOG_TAG, string.toString());
			}
		}

	}

	/**
	 * Log，类型debug
	 * 
	 * @param string
	 */
	public void d(Object string) {
		if (IF_DEBUG) {
			String name = getFunctionName();
			if (name != null) {
				Log.d(LOG_TAG, name + " - " + string);
			}
			else {
				Log.d(LOG_TAG, string.toString());
			}
		}
	}

	/**
	 * Log，类型verbose
	 * 
	 * @param string
	 */
	public void v(Object string) {
		if (IF_DEBUG) {
			String name = getFunctionName();
			if (name != null) {
				Log.v(LOG_TAG, name + " - " + string);
			}
			else {
				Log.v(LOG_TAG, string.toString());
			}
		}
	}

	/**
	 * Log，类型warn
	 * 
	 * @param string
	 */
	public void w(Object string) {
		if (IF_DEBUG) {
			String name = getFunctionName();
			if (name != null) {
				Log.w(LOG_TAG, name + " - " + string);
			}
			else {
				Log.w(LOG_TAG, string.toString());
			}
		}
	}

	/**
	 * Log，类型Error
	 * 
	 * @param str
	 */
	public void e(Object str) {
		if (IF_DEBUG) {
			String name = getFunctionName();
			if (name != null) {
				Log.e(LOG_TAG, name + " - " + str);
			}
			else {
				Log.e(LOG_TAG, str.toString());
			}
		}
	}

}
