package com.purezhihudaily.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.purezhihudaily.framework.db.model.GenericModel;

public class GsonUtils {

	/**
	 * 解析一个字符串
	 * 
	 * @param content
	 * @param clazz
	 * @return
	 */
	public static GenericModel getEntity(String content, Class<?> clazz) {

		if (TextUtils.isEmpty(content))
			return null;

		Gson gson = new Gson();

		try {
			GenericModel baseModel = (GenericModel) gson.fromJson(content, clazz);
			return baseModel;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
