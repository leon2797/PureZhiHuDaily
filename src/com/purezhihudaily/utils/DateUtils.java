package com.purezhihudaily.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.text.TextUtils;

/**
 * 类说明： 日期时间工具类
 * 
 * @author Cundong
 * @date Feb 15, 2012 5:30:51 AM
 * @version 1.0
 */
public class DateUtils {

	/**
	 * 默认日期格式
	 */
	public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * 时间格式：yyyy-MM-dd HH:mm:ss
	 */
	public static final String YYYY_MM_dd_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 时间格式：yyyy-MM-dd HH:mm
	 */
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	
	public static final String MM_DD_HH_MM = "MM-dd HH:mm";

	public static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH";

	/**
	 * 时间格式：yyyy-MM-dd
	 */
	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	public static final String YYYYMMDD = "yyyyMMdd";

	public static final String MMDD = "MM月dd日";

	/**
	 * 时间格式：HH:mm:ss
	 */
	public static final String HH_MM_SS = "HH:mm:ss";

	public static final String HH_MM = "HH:mm";

	public static final String YYYYmmDDhhMMss = "yyyy/MM/dd HH:mm:ss";

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getCurrentTime() {

		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sf = new SimpleDateFormat(YYYY_MM_dd_HH_MM_SS, Locale.getDefault());
		return sf.format(date);
	}

	/**
	 * 获取今天的日期，格式为：yyyy/MM/dd
	 * 
	 * @return
	 */
	public static String getCurrentDate(String format) {

		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sf = new SimpleDateFormat(format, Locale.getDefault());

		return sf.format(date);
	}

	public static int getCurrentMonth() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sf = new SimpleDateFormat("MM", Locale.getDefault());

		return Integer.parseInt(sf.format(date));
	}

	/**
	 * @param pubtime
	 *            样例：2011-06-20T17:23:11Z
	 * @return 样例：05.10 17:11
	 */
	public static String getFormatTime(String pubtime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

		Date date = null;

		try {
			date = df.parse(pubtime.replace("Z", " "));
		}
		catch (ParseException e) {
			e.printStackTrace();
		}

		return (null == date) ? null : (new SimpleDateFormat("MM.dd HH:mm", Locale.getDefault())).format(date);
	}

	public static Date getFormatTimeDate(String pubtime, String inputFormat, String outFormat) {

		if (TextUtils.isEmpty(pubtime)) {
			return null;
		}

		SimpleDateFormat df = new SimpleDateFormat(inputFormat, Locale.getDefault());

		Date date = null;
		try {
			pubtime = pubtime.replace("Z", " ");
			pubtime = pubtime.replace("T", " ");

			date = df.parse(pubtime);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	public static String getFormatTime(String pubtime, String inputFormat, String outFormat) {

		Date date = getFormatTimeDate(pubtime, inputFormat, outFormat);

		return (null == date) ? null : (new SimpleDateFormat(outFormat, Locale.getDefault())).format(date);
	}

	/**
	 * @param pubtime
	 *            样例：2011-06-20T17:23:11Z
	 * @param format
	 * @return
	 */
	public static String getFormatTime(String pubtime, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());

		Date date = null;
		try {
			date = df.parse(pubtime.replace("T", " "));
		}
		catch (ParseException e) {
			e.printStackTrace();
		}

		return (null == date) ? null : (new SimpleDateFormat(format, Locale.getDefault())).format(date);
	}

	/**
	 * 获取当前日期是星期几
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	/**
	 * 字符串转换时间戳
	 * 
	 * @param str
	 * @return
	 */
	public static long getTimestamp(String str, String format) {
		if (TextUtils.isEmpty(str))
			return 0;
		Date date = str2Date(str, format);
		return date.getTime();
	}

	@SuppressLint("SimpleDateFormat")
	public static String timestampToString(long str, String format) {
		Timestamp ts = new Timestamp(str * 1000);
		String tsStr = "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			tsStr = sdf.format(ts);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return tsStr;
	}

	/**
	 * 获取两个时间戳之间差值,并且返回小时
	 */
	public static int subTimeStamp2Hour(Timestamp one, Timestamp two) {
		int minute = subTimeStamp2Minute(one, two);
		return minute / 60;
	}

	/**
	 * 获取两个时间戳之间差值,并且返回分钟
	 */
	public static int subTimeStamp2Minute(Timestamp one, Timestamp two) {
		return (int) (two.getTime() - one.getTime()) / (1000 * 60);
	}

	/**
	 * 获取两个时间戳之间差值,并且返回秒
	 */
	public static int subTimeStamp2Second(Timestamp one, Timestamp two) {
		int minute = subTimeStamp2Minute(one, two);
		return minute * 60;
	}

	/**
	 * Date转Timestamp
	 * 
	 * @param pubtime
	 * @return
	 */
	public static Timestamp string2Timestamp(String pubtime, String format) {
		Timestamp timestamp = null;

		SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());

		Date date = null;
		try {
			date = df.parse(pubtime.replace("Z", " "));
		}
		catch (ParseException e) {
			e.printStackTrace();
		}

		if (null != date) {
			SimpleDateFormat df1 = new SimpleDateFormat(format, Locale.getDefault());
			String time = df1.format(date);
			timestamp = Timestamp.valueOf(time);
		}

		return timestamp;
	}

	/**
	 * 字符串转换成日期 如果转换格式为空，则利用默认格式进行转换操作
	 * 
	 * @param str
	 *            字符串
	 * @param format
	 *            日期格式
	 * @return 日期
	 * @throws java.text.ParseException
	 */
	public static Date str2Date(String str, String format) {
		Date date = null;

		if (!TextUtils.isEmpty(str)) {
			// 如果没有指定字符串转换的格式，则用默认格式进行转换
			if (null == format || "".equals(format)) {
				format = DEFAULT_FORMAT;
			}

			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
			try {
				date = sdf.parse(str);
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return date;
	}

	/**
	 * 获取两个日期之间的差值
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDiff(String str1, String str2) {

		if (TextUtils.isEmpty(str1) || TextUtils.isEmpty(str2))
			return 0;

		Date date1 = str2Date(str1, YYYYMMDD);
		Date date2 = str2Date(str2, YYYYMMDD);

		return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));

	}
}
