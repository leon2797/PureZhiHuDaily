package com.purezhihudaily.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

/**
 * SD卡工具类
 * 
 */
public class SDCardUtils {

	private static final String TYPE_CACHE = "cache";

	private static final String TYPE_FILES = "files";
	
	private SDCardUtils() {
		
	}

	/**
	 * SD卡是否挂载
	 * 
	 * @return boolean
	 */
	public static boolean isMounted() {
		String status = Environment.getExternalStorageState();
		return status.equals(Environment.MEDIA_MOUNTED) ? true : false;
	}

	/**
	 * 获取扩展存储file的绝对路径
	 * 
	 * 返回：sdcard/Android/data/{package_name}/files/
	 * 
	 * @param context
	 * @return String
	 */
	public static String getExternalFileDir(Context context) {
		return getExternalDir(context, TYPE_FILES);
	}

	/**
	 * 获取扩展存储cache的绝对路径
	 * 
	 * 返回：sdcard/Android/data/{package_name}/cache/
	 * 
	 * @param context
	 * @return String
	 */
	public static String getExternalCacheDir(Context context) {
		return getExternalDir(context, TYPE_CACHE);
	}

	/**
	 * 获取扩展存储文件的绝对路径
	 * @param context
	 * @param type
	 * @return String
	 */
	private static String getExternalDir(Context context, String type) {

		StringBuilder sb = new StringBuilder();

		if (context == null)
			return null;

		if (!isMounted()) {

			if (type.equals(TYPE_CACHE)) {
				sb.append(context.getCacheDir()).append(File.separator);
			}
			else {
				sb.append(context.getFilesDir()).append(File.separator);
			}
		}

		File file = null;

		if (type.equals(TYPE_CACHE)) {
			file = context.getExternalCacheDir();
		}
		else {
			file = context.getExternalFilesDir(null);
		}

		// In some case, even the sd card is mounted,
		// getExternalCacheDir will return null
		// may be it is nearly full.

		if (file != null) {
			sb.append(file.getAbsolutePath()).append(File.separator);
		}
		else {
			sb.append(Environment.getExternalStorageDirectory().getPath()).append("/Android/data/").append(context.getPackageName()).append("/").append(type).append("/").append(File.separator).toString();
		}

		return sb.toString();
	}
}
