package com.kapp.singlo.util;

import java.io.File;

import android.os.Environment;

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

}
