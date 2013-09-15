package com.garagestory.singlo.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

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
	
	public static String strEncoder(String str){
		
		String encodeStr = null;
		
		try {
			encodeStr = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return encodeStr;
	}
	
	public static String strDecoder(String str){
		
		String decodeStr = null;
		
		try {
			decodeStr = URLDecoder.decode(str, "UTF-8");			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return decodeStr;
	}

    private static final int MAX_SIZE = 1024;

    public static Drawable createLargeDrawable(Context context, int resId) throws IOException {

        InputStream is = context.getResources().openRawResource(resId);
        BitmapRegionDecoder brd = BitmapRegionDecoder.newInstance(is, true);

        try {
            Point point = new Point();
            ((WindowManager) context.getSystemService(context.WINDOW_SERVICE))
                    .getDefaultDisplay().getSize(point);
            double ratio = point.x / (double) brd.getWidth();
            if (brd.getWidth() <= MAX_SIZE && brd.getHeight() <= MAX_SIZE) {
                Bitmap resize = Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(context.getResources(), resId),
                        (int) Math.floor(brd.getWidth() * ratio),
                        (int) Math.floor(brd.getHeight() * ratio), true);
                return new BitmapDrawable(context.getResources(), resize);
            }

            int rowCount = (int) Math.ceil((float) brd.getHeight()
                    / (float) MAX_SIZE);
            int colCount = (int) Math.ceil((float) brd.getWidth()
                    / (float) MAX_SIZE);

            BitmapDrawable[] drawables = new BitmapDrawable[rowCount * colCount];

            for (int i = 0; i < rowCount; i++) {

                int top = MAX_SIZE * i;
                int bottom = i == rowCount - 1 ? brd.getHeight() : top
                        + MAX_SIZE;

                for (int j = 0; j < colCount; j++) {

                    int left = MAX_SIZE * j;
                    int right = j == colCount - 1 ? brd.getWidth() : left
                            + MAX_SIZE;

                    int dstWidth = (int) Math.floor((right - left) * ratio);
                    int dstHeight = (int) Math.floor((bottom - top) * ratio);

                    Bitmap b = brd.decodeRegion(new Rect(left, top, right,
                            bottom), null);
                    Bitmap resize = Bitmap.createScaledBitmap(b, dstWidth,
                            dstHeight, true);
                    BitmapDrawable bd = new BitmapDrawable(context.getResources(),
                            resize);
                    bd.setGravity(Gravity.TOP | Gravity.LEFT);
                    drawables[i * colCount + j] = bd;
                }
            }

            LayerDrawable ld = new LayerDrawable(drawables);
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {

                    ld.setLayerInset(i * colCount + j,
                            (int) Math.floor(MAX_SIZE * j * ratio),
                            (int) Math.floor(MAX_SIZE * i * ratio), 0, 0);
                }
            }

            return ld;
        } finally {
            brd.recycle();
        }
    }

}
