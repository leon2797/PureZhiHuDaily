package com.purezhihudaily.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.purezhihudaily.R;

import android.content.Context;

public class ChangeDateUtils {
	
	/**
	 * 获取listview item 日期
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String getDateTag(Context context, String dateStr) {

		String currentDate = DateUtils.getCurrentDate(DateUtils.MMDD);

		String pre = DateUtils.getFormatTime(dateStr, DateUtils.YYYYMMDD, DateUtils.MMDD);

		Date date = DateUtils.getFormatTimeDate(dateStr, DateUtils.YYYYMMDD, DateUtils.MMDD);

		String week = DateUtils.getWeekOfDate(date);

		return currentDate.equals(pre) ? context.getString(R.string.today) : new StringBuilder().append(pre).append(" ").append(week)
				.toString();
	}

	/**
	 * 获取当前日期后一天
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String getAddedDate(String dateStr) {

		Date date = DateUtils.str2Date(dateStr, DateUtils.YYYYMMDD);

		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, 1);

		return mSimpleDateFormat.format(cal.getTime());
	}

	/**
	 * 获取当前日期前一天
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String getBeforeDate(String dateStr) {

		Date date = DateUtils.str2Date(dateStr, DateUtils.YYYYMMDD);

		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, -1);

		return mSimpleDateFormat.format(cal.getTime());
	}

}
