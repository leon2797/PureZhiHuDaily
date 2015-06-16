package com.purezhihudaily.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import android.content.Context;

/**
 * AssetsUtils
 * 
 */
public class AssetsUtils {

	public static String loadText(Context context, String assetFielPath) {
		InputStream is = null;
		try {
			is = context.getResources().getAssets().open(assetFielPath);
			Writer writer = new StringWriter();

			char[] buffer = new char[2048];
			try {
				@SuppressWarnings("resource")
				Reader reader = new BufferedReader(new UnicodeReader(is, Charset.defaultCharset().name()));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			}
			finally {
				is.close();
			}
			String text = writer.toString();
			return text;
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
