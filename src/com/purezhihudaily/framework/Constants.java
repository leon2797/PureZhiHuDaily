package com.purezhihudaily.framework;

import java.io.File;

import android.os.Environment;

/**
 * 常量列表
 * 
 */
public class Constants {

	/**
	 * 地址相关 Urls由"https://github.com/izzyleung/ZhihuDailyPurify/wiki/知乎日报-API-分析"提供
	 */
	
	// 启动界面图片
	public static final String ZHIHU_DAILY_START = "http://news-at.zhihu.com/api/4/start-image/1080*1920";

	// 获取最新新闻
	public static final String ZHIHU_DAILY_LATEST = "http://news-at.zhihu.com/api/4/news/latest";

	// 获取新闻详情
	public static final String ZHIHU_DAILY_DETAIL = "http://news-at.zhihu.com/api/4/news/";

	// 获取过往新闻
	public static final String ZHIHU_DAILY_BEFORE = "http://news.at.zhihu.com/api/4/news/before/";

	// 新闻的额外信息
	public static final String ZHIHU_DAILY_EXTRA = "http://news-at.zhihu.com/api/4/story-extra/";

	// 长评论
	public static final String ZHIHU_DAILY_LONG_COMMENTS = "http://news-at.zhihu.com/api/4/story/#{id}/long-comments";

	// 短评论
	public static final String ZHIHU_DAILY_SHORT_COMMENTS = "http://news-at.zhihu.com/api/4/story/#{id}/short-comments";

	// 主题日报列表
	public static final String ZHIHU_DAILY_THEMES_LIST = "http://news-at.zhihu.com/api/4/themes";

	// 主题日报详情
	public static final String ZHIHU_DAILY_THEMES_DETAIL = "http://news-at.zhihu.com/api/4/theme/";

	// 知乎限制访问地址1
	public static final String ZHIHU_LIMIT_ADDRESS_1 = "http://pic1.zhimg.com/";
	
	// 知乎限制访问地址2
	public static final String ZHIHU_LIMIT_ADDRESS_2 = "http://pic2.zhimg.com/";
	
	/**
	 * 应用相关
	 */
	// 应用名
	public static final String APPLICATION_NAME = "PureZhihuDaily";
	// 程序信息 （存放在SharedPreferences）
	public static final String APPLICATION_INFO = "PureZhihuDaily_Info";
	// 应用根目录
	public static final String APPLICATION_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Constants.APPLICATION_NAME + File.separator;

	/**
	 * 日志相关
	 */
	// 日志文件名
	public static final String LOG_NAME = "Log";
	// 日志目录
	public static final String LOG_PATH = Constants.APPLICATION_PATH + Constants.LOG_NAME;

	/**
	 * 数据库相关
	 */
	// 数据库文件名
	public static final String DATABASE_NAME = "PureZhihuDaily.db";
	// 数据库目录
	public static final String DATABASE_PATH = APPLICATION_PATH + Constants.DATABASE_NAME;
	// 数据库版本
	public static final int DATABASE_VERSION = 3;
}
