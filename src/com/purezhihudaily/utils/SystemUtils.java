package com.purezhihudaily.utils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.purezhihudaily.R;
import com.purezhihudaily.framework.ApplicationHelper;
import com.purezhihudaily.framework.Constants;
import com.purezhihudaily.utils.uuid.UUIDService;

/**
 * 系统自定义工具类
 * 
 */
public class SystemUtils {

	// 最近一次点击时间
	private static long lastClickTime;
	// 两次点击的时间间隔不能小于700毫秒
	private static long betweenTime = 700;

	// 用来存储设备信息和异常信息
	public static Map<String, String> infos = new HashMap<String, String>();
	
	private SystemUtils() {
		
	}

	/**
	 * 生成uuid
	 * 
	 * @return String生成的uuid
	 */
	public static String generateUUID() {
		return UUIDService.getInstance().simpleHex();
	}
	
	/**
	 * 防止快速双击
	 * 
	 * @return boolean
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeDifference = time - lastClickTime;
		if (0 < timeDifference && timeDifference < betweenTime) { 
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 打开assets文件夹中的文件
	 * 
	 * @param path
	 * @param assetManager
	 * @return InputStream
	 */
	public static InputStream openAssets(String path, AssetManager assetManager) {
		try {
			return assetManager.open(path);
		}
		catch (Exception e) {
			LogUtils.getInstance().e(e);
		}
		return null;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param context
	 */
	public static void collectDeviceInfo(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		}
		catch (NameNotFoundException e) {
			LogUtils.getInstance().e(e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			}
			catch (Exception e) {
				LogUtils.getInstance().e(e);
			}
		}
	}
	
	/**
	 * 记录错误日志
	 * 
	 * @param context
	 *            用于获取设备信息 可为空
	 * @param e
	 */
	public static void saveErrorLog(Context context, Throwable e) {
		if (context != null) {
			SystemUtils.collectDeviceInfo(context);
		}
		LogUtils.getInstance().e(e);
	}

	/**
	 * 获取SharedPreferences中信息
	 * 
	 * @param key
	 * @return
	 */
	public static String getSharedPreferences(String key) {
		SharedPreferences settings = ApplicationHelper.getApplication().getSharedPreferences(Constants.APPLICATION_INFO, 0);
		return settings.getString(key, "").toString();
	}

	/**
	 * 设置SharedPreferences中信息
	 * 
	 * @param key
	 * @param value
	 */
	public static void saveSharedPreferences(String key, String value) {
		SharedPreferences settings = ApplicationHelper.getApplication().getSharedPreferences(Constants.APPLICATION_INFO, 0);
		settings.edit().putString(key, value).commit();
	}

	/**
	 * 获取简要信息
	 * 
	 * @param sourceString
	 *            原始字符串
	 * @param length
	 *            最大长度
	 * @param appString
	 *            结尾字符 可为空 默认 ......
	 * @return XXXX......
	 */
	public static String getBriefInfo(String sourceString, int length, String appString) {
		String defaultAppString = "...";
		if (!TextUtils.isEmpty(sourceString)) {
			if (!TextUtils.isEmpty(appString)) {
				defaultAppString = appString;
			}
			if (length >= sourceString.length()) {
				length = sourceString.length();
				return sourceString;
			}
			return sourceString.substring(0, length) + defaultAppString;
		}
		return "";
	}

	/**
	 * 隐藏键盘
	 * 
	 * @param mcontext
	 */
	public static void KeyBoardCancle(Activity activity) {
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	/**
	 * 显示键盘
	 * 
	 * @param mcontext
	 */
	public static void keyBoardDisplay(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 获取系统的SDK版本号
	 * 
	 * @return
	 */
	public static int getSystemVersion() {
		return Build.VERSION.SDK_INT;
	}
	
	/**
	 * 获取设备SERIAL号
	 * 
	 * @return SERIAL号
	 */
	public static String getSerialFromDevice() {
		String serial = Build.SERIAL;
		return serial;
	}

	/**
	 * 取得授权
	 * 
	 * @param context
	 * @param permission
	 * @return
	 */
	public static String getAuthorityFromPermission(Context context, String permission) {
		if (permission == null)
			return null;
		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
		if (packs != null) {
			for (PackageInfo pack : packs) {

				ProviderInfo[] providers = pack.providers;
				if (providers != null) {
					for (ProviderInfo provider : providers) {
						if (provider.readPermission != null) {
							if ((provider.readPermission).contains(permission)) {
								return provider.authority;
							}
						}
					}
				}
			}

		}

		return null;
	}
	
	/**
	 * 获取通知栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		}
		catch (Exception e1) {
			LogUtils.getInstance().e(e1);
		}
		return statusBarHeight;
	}
	
	/**
	 * 判断快捷方式是否存在
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasShortCut(Context context) {
		Uri uri = null;
		String spermi = getAuthorityFromPermission(context, "READ_SETTINGS");
		if (getSystemVersion() < 8) {
			uri = Uri.parse("content://" + spermi + "/favorites?notify=true");
		}
		else {
			uri = Uri.parse("content://" + spermi + "/favorites?notify=true");
		}
		final ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(uri, new String[] { "title", "iconResource" }, "title=?", new String[] { context.getString(R.string.app_name) }, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.close();
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * 为程序创建桌面快捷方式
	 */
	public static void addShortcut(Activity activity) {
		if (hasShortCut(activity.getApplicationContext()))
			return;
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, activity.getString(R.string.app_name));
		// 不允许重复创建
		shortcut.putExtra("duplicate", false);

		ComponentName comp = new ComponentName(activity.getPackageName(), activity.getPackageName() + "." + activity.getLocalClassName());
		Intent intent = new Intent(Intent.ACTION_MAIN).setComponent(comp);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		// 快捷方式的图标
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(activity, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

		activity.sendBroadcast(shortcut);
	}

	/**
	 * 删除程序的快捷方式
	 */
	public static void deleteShortcut(Activity activity) {
		Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, activity.getString(R.string.app_name));

		ComponentName comp = new ComponentName(activity.getPackageName(), activity.getPackageName() + "." + activity.getLocalClassName());
		Intent intent = new Intent(Intent.ACTION_MAIN).setComponent(comp);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		// 快捷方式的图标
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(activity, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

		activity.sendBroadcast(shortcut);
	}

	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
	
	/**
	 * 判断应用是否处于后台
	 * @param context
	 * @return
	 */
	public static boolean isApplicationInBackground(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		// Build.VERSION_CODES.LOLLIPOP = 21
		if (Build.VERSION.SDK_INT >= 21) {
			final List<RunningAppProcessInfo> processInfos = manager.getRunningAppProcesses();
			for (RunningAppProcessInfo processInfo : processInfos) {
				if (processInfo.processName.equals(context.getPackageName())) {
					if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
						return true;
					}
					else {
						return false;
					}
				}
			}
		}
		else {
			@SuppressWarnings("deprecation")
			List<RunningTaskInfo> taskList = manager.getRunningTasks(1);
			if (taskList != null && !taskList.isEmpty()) {
				ComponentName topActivity = taskList.get(0).topActivity;
				if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 获取屏幕密度
	 * 
	 * @return
	 */
	public static float getScreenDensity() {
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		return metrics.density;
	}

	/**
	 * 获取屏幕宽度高度
	 */
	public static int[] getScreenWidthAndHeight() {
		int[] wh = new int[2];
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		int widthPixels = metrics.widthPixels;
		int heightPixels = metrics.heightPixels;
		int screenWidth = (int) (widthPixels);
		int screenHeight = (int) (heightPixels);
		wh[0] = screenWidth;
		wh[1] = screenHeight;
		return wh;
	}

	/**
	 * 获取屏幕高度
	 */
	public static int getDisplayScreenHeight(Activity activity) {
		int screenHeight = 0;

		DisplayMetrics metrics = new DisplayMetrics();
		Display display = activity.getWindowManager().getDefaultDisplay();
		display.getMetrics(metrics);

		int ver = Build.VERSION.SDK_INT;
		if (ver < 13) {
			screenHeight = metrics.heightPixels;
		}
		else if (ver == 13) {
			try {
				Method method = display.getClass().getMethod("getRealHeight");
				screenHeight = (Integer) method.invoke(display);
			}
			catch (Exception e) {
				LogUtils.getInstance().e(e);
			}
		}
		else if (ver > 13) {
			try {
				Method method = display.getClass().getMethod("getRawHeight");
				screenHeight = (Integer) method.invoke(display);
			}
			catch (Exception e) {
				LogUtils.getInstance().e(e);
			}
		}

		return screenHeight;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
