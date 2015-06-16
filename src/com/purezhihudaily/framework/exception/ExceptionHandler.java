package com.purezhihudaily.framework.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import com.purezhihudaily.framework.ApplicationHelper;
import com.purezhihudaily.utils.LogUtils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * UncaughtException处理类，当程序发生Uncaught异常的时候，有该类来接管程序，并记录发送错误报告。
 * 
 */
public class ExceptionHandler implements UncaughtExceptionHandler {

	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static ExceptionHandler INSTANCE = new ExceptionHandler();
	// 程序的Context对象
	private Context context;
	
	/** 
	 * 保证只有一个CrashHandler实例
	 */
	private ExceptionHandler() {
	}

	/**
	 *  单例模式 
	 */
	public static ExceptionHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		this.context = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		}
		else {
			try {
				Thread.sleep(3000);
			}
			catch (InterruptedException e) {
				LogUtils.getInstance().e(e);
			}
			ApplicationHelper.exit();
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, "程序出现异常，即将退出。", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		LogUtils.getInstance().e(ex);
		return true;
	}

}
