package com.kapp.singlo.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class Utility {

	public static String makeDir(String dirName) {
		String mRootPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + dirName;

		try {
			File fRoot = new File(mRootPath);
			if (fRoot.exists() == false) {
				if (fRoot.mkdirs() == false) {
					throw new Exception("");
				}
			}
		} catch (Exception e) {
			mRootPath = "-1";
		}
		return mRootPath + "/";
	}

	// By Kilobyte
	public static long getFileSize(String path) {
		File file = new File(path);

		long ret = file.length();

		return ret / 1024;
	}

	public static String getCacheFilename(Context context, String filename,
			Boolean... params) {
		boolean remove_old_version = false;
		if (params.length > 0) {
			remove_old_version = params[0].booleanValue();
		}

		File cacheDir = context.getCacheDir();

		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}

		File tempFile = new File(cacheDir, filename);

		if (remove_old_version && tempFile.exists())
			tempFile.delete();

		return tempFile.getAbsolutePath();
	}

	public static String getImageHtmlCode(String _imageURL) {
		StringBuffer sb = new StringBuffer("<html>");
		sb.append("<head>");
		sb.append("<style type=\"text/css\">");
		sb.append("body { margin: 0; }");
		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<img width=\"100%\" height=\"100%\" src=\"" + _imageURL
				+ "\"/>");
		sb.append("</body>");
		sb.append("</html>");
		Log.d("sb", sb.toString());
		return sb.toString();
	}

	public static String getClubName(int clubType) {
		switch (clubType) {
		case 1:
			return "드라이버";
		case 2:
			return "우드";
		case 3:
			return "유틸리티";
		case 4:
			return "아이언";
		case 5:
			return "웨지";
		case 6:
			return "퍼터";
		}
		return "";
	}
}
