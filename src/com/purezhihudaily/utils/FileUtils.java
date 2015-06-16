package com.purezhihudaily.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import android.graphics.Bitmap;

/**
 * 文件工具类
 * 
 */
public class FileUtils {

	private FileUtils() {

	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 * @return File
	 */
	public static File createFile(String path) {
		File file = new File(path);
		if (file.exists())
			return file;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		}
		catch (Exception e) {
			LogUtils.getInstance().e(e);
		}
		return file;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 */
	public static File createFolder(String path) {
		File file = new File(path);
		if (file.exists())
			return file;
		try {
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		catch (Exception e) {
			LogUtils.getInstance().e(e);
		}
		return file;
	}

	/**
	 * 根据文件名和路径获取文件
	 * 
	 * @param path
	 * @return File
	 */
	public static File getFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			return file;
		}
		else {
			return null;
		}
	}

	/**
	 * 获取某个文件下的文件数组
	 * 
	 * @param path
	 * @return File[]
	 */
	public static File[] getFiles(String path) {
		File folder = new File(path);
		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles();
			return files;
		}
		else {
			return null;
		}
	}

	/**
	 * 获取某个文件下的文件名数组
	 * 
	 * @param path
	 * @return String[]
	 */
	public static String[] getFileNames(String path) {
		File folder = new File(path);
		if (folder.exists() && folder.isDirectory()) {
			String[] fileNames = folder.list();
			return fileNames;
		}
		else {
			return null;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 */
	public static void deleteFile(String path) {
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		else if (file.exists() && file.isDirectory()) {
			deleteFiles(path);
			file.delete();
		}
	}

	/**
	 * 删除文件夹下的所有文件
	 * 
	 * @param path
	 */
	public static void deleteFiles(String path) {
		try {
			File[] files = getFiles(path);
			if (files != null) {
				for (File file : files) {
					if (file.exists() && file.isFile()) {
						file.delete();
					}
					else if (file.exists() && file.isDirectory()) {
						deleteFiles(file.getAbsolutePath());
						file.delete();
					}
				}
			}
		}
		catch (Exception e) {
			LogUtils.getInstance().e(e);
		}
	}

	/**
	 * 删除文件夹下的所有文件，保留全部文件夹
	 * 
	 * @param path
	 */
	public static void deleteFilesWithoutFolder(String path) {
		try {
			File[] files = getFiles(path);
			if (files != null) {
				for (File file : files) {
					if (file.exists() && file.isFile()) {
						file.delete();
					}
					else if (file.exists() && file.isDirectory()) {
						deleteFilesWithoutFolder(file.getAbsolutePath());
					}
				}
			}
		}
		catch (Exception e) {
			LogUtils.getInstance().e(e);
		}
	}

	/**
	 * 获取文件的字节流
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] readBytesFromFile(File file) {
		if (file == null) {
			return null;
		}
		if (file != null && !file.exists()) {
			return null;
		}
		try {
			FileInputStream stream = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			byte[] b = new byte[1024];
			int n;
			while ((n = stream.read(b)) != -1) {
				out.write(b, 0, n);
			}

			stream.close();
			out.close();
			return out.toByteArray();
		}
		catch (IOException e) {
			LogUtils.getInstance().e(e);
		}
		return null;
	}

	/**
	 * 用字节流生成文件
	 * 
	 * @param path
	 * @param fileName
	 * @param content
	 * @return File
	 */
	public static File writeFileFromBytes(String path, byte[] content) {
		File file = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		if (content != null && content.length > 0) {
			inputStream = new ByteArrayInputStream(content);
		}
		try {
			file = new File(path);
			if (file.exists()) {
				file.delete();
			}
			if (file.createNewFile()) {
				outputStream = new FileOutputStream(file);
				byte data[] = new byte[4 * 1024];
				int tmp;
				while ((tmp = inputStream.read(data)) != -1) {
					outputStream.write(data, 0, tmp);
				}
			}
			if (outputStream != null) {
				outputStream.flush();
			}
		}
		catch (Exception e) {
			LogUtils.getInstance().e(e);
			return null;
		}
		finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			}
			catch (Exception e) {
				LogUtils.getInstance().e(e);
			}
		}
		return file;
	}

	/**
	 * 通过输入流生成文件
	 * 
	 * @param path
	 * @param inputStream
	 * @return File
	 */
	public static File writeFileFromInputStream(String path, InputStream inputStream) {
		File file = null;
		OutputStream outputStream = null;

		try {
			file = createFile(path);
			outputStream = new FileOutputStream(file);
			byte data[] = new byte[4 * 1024];
			int temp;
			while ((temp = inputStream.read(data)) != -1) {
				outputStream.write(data, 0, temp);
			}
			outputStream.flush();
		}
		catch (Exception e) {
			LogUtils.getInstance().e(e);
		}
		finally {
			try {
				outputStream.close();
			}
			catch (Exception e) {
				LogUtils.getInstance().e(e);
			}
		}
		return file;
	}

	/**
	 * 通过位图生成文件
	 * 
	 * @param fileName
	 * @param bitmap
	 */
	public static File writeBitmapToFile(String fileName, Bitmap bitmap) {
		File file = null;
		FileOutputStream fileOutputStream = null;
		try {
			file = new File(fileName);
			if (file.exists())
				file.delete();
			file.createNewFile();
			fileOutputStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
			fileOutputStream.flush();
		}
		catch (IOException e) {
			LogUtils.getInstance().e(e);
		}
		finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				}
				catch (IOException e) {
					LogUtils.getInstance().e(e);
				}
			}
		}
		return file;
	}

	/**
	 * 写入到文件
	 * 
	 * @param path
	 * @param content
	 * @return File
	 */
	public static File writeContentToFile(String path, String content) {
		File file = null;
		BufferedWriter out = null;
		try {
			file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(content);
		}
		catch (Exception e) {
			LogUtils.getInstance().e(e);
		}
		finally {
			try {
				out.close();
			}
			catch (IOException e) {
				LogUtils.getInstance().e(e);
			}
		}
		return file;
	}

	/**
	 * 拷贝文件到指定的文件夹
	 * 
	 * @param resourceFilePath
	 * @param targetDir
	 */
	@SuppressWarnings("resource")
	public static void copyFileToDir(String resourceFilePath, String targetDir) {
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			String targetDirString = targetDir;
			createFile(targetDirString);
			File tempFile = new File(resourceFilePath);
			File newFile = new File(targetDirString);
			inChannel = new FileInputStream(tempFile).getChannel();
			outChannel = new FileOutputStream(newFile).getChannel();
		}
		catch (FileNotFoundException e) {
			LogUtils.getInstance().e(e);
		}
		finally {
			try {
				if (inChannel != null) {
					inChannel.close();
				}
				if (outChannel != null) {
					outChannel.close();
				}
			}
			catch (IOException e) {
				LogUtils.getInstance().e(e);
			}
		}
	}

	/**
	 * 把一段字符串写入到指定的文件末尾
	 * 
	 * @param path
	 * @param content
	 * */
	public static void inssertStringToFile(String path, String content) {
		RandomAccessFile randomAccessFile = null;
		try {
			createFile(path);
			randomAccessFile = new RandomAccessFile(path, "rw");
			long fileLength = randomAccessFile.length();
			randomAccessFile.seek(fileLength);
			randomAccessFile.writeBytes(content);
		}
		catch (Exception e) {
			LogUtils.getInstance().e(e);
		}
		finally {
			try {
				if (randomAccessFile != null) {
					randomAccessFile.close();
				}
			}
			catch (IOException e) {
				LogUtils.getInstance().e(e);
			}
		}
	}
}
