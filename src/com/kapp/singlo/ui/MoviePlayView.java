package com.kapp.singlo.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.Toast;

public class MoviePlayView extends View {
	private Bitmap mBitmap;

	public MoviePlayView(Context context) {
		super(context);

	}

	public void setMovie(Context context, String fname) {

		if (initBasicPlayer() < 0) {
			Toast.makeText(context, "CPU doesn't support NEON",
					Toast.LENGTH_LONG).show();

			((Activity) context).finish();
		}

		int openResult = openMovie(fname);
		if (openResult < 0) {
			Toast.makeText(context, "Open Movie Error: " + openResult,
					Toast.LENGTH_LONG).show();

			((Activity) context).finish();
		} else
			mBitmap = Bitmap.createBitmap(getMovieWidth(), getMovieHeight(),
					Bitmap.Config.RGB_565);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		renderFrame(mBitmap);
		canvas.drawBitmap(mBitmap, 0, 0, null);

		invalidate();
	}

	static {
		System.loadLibrary("singloplayer");
	}

	public static native int initBasicPlayer();

	public static native int openMovie(String filePath);

	public static native int renderFrame(Bitmap bitmap);

	public static native int getMovieWidth();

	public static native int getMovieHeight();

	public static native void closeMovie();
}