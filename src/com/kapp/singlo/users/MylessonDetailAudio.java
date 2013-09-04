package com.kapp.singlo.users;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.data.LessonAnswer;
import com.kapp.singlo.data.LessonAnswerImage;
import com.kapp.singlo.util.Const;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MylessonDetailAudio extends Activity {

	private int lesson_id;
	private int now_index;

	private Lesson lesson;
	private LessonAnswer lessonAnswer;

	private List<LessonAnswerImage> lessonAnswerImageList;
	private List<Paint> paintList;

	private Bitmap drawingBitmap;
	private Canvas drawingCanvas;

	private Paint redPaint;
	private Paint yellowPaint;
	private Paint bluePaint;

	private ImageButton backImageButton;

	private ImageView drawingCanvasImageView;
	private ImageView nowImageView;
	private TextView imageCountTextView;

	private ProgressDialog progressDialog;
	private GetFileTask getFileTask;

	private static MediaPlayer mediaPlayer;

	private static SeekBar playSeekBar;
	private static ImageButton playImageButton;
	private static TextView audioTimeTextView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylesson_detail_audio);

		Intent intent = this.getIntent();
		lesson_id = intent.getIntExtra("lesson_id", 0);

		DBConnector db = new DBConnector(this);
		lesson = db.getLesson(lesson_id);
		lessonAnswer = db.getLessonAnswerByLesson(lesson);
		lessonAnswerImageList = db
				.getAllLessonAnswerImageByLessonAnswer(lessonAnswer);
		db.close();

		now_index = 0;

		backImageButton = (ImageButton) findViewById(R.id.BackImageButton);
		backImageButton.setOnClickListener(backImageButtonOnClickListener);
		playImageButton = (ImageButton) findViewById(R.id.PlayImageButton);
		playImageButton.setOnClickListener(playImageButtonOnClickListener);

		playSeekBar = (SeekBar) findViewById(R.id.PlaySeekBar);
		playSeekBar
				.setOnSeekBarChangeListener(playSeekBarOnSeekBarChangeListener);

		drawingCanvasImageView = (ImageView) findViewById(R.id.DrawingCanvasImageView);
		nowImageView = (ImageView) findViewById(R.id.NowImageView);
		audioTimeTextView = (TextView) findViewById(R.id.AudioTimeTextView);
		imageCountTextView = (TextView) findViewById(R.id.ImageCountTextView);
		imageCountTextView.setText("1 / " + lessonAnswerImageList.size());

		paintList = new ArrayList<Paint>();

		progressDialog = ProgressDialog.show(MylessonDetailAudio.this, "",
				"준비중입니다.", true, false);
		getFileTask = new GetFileTask();
		getFileTask.execute();
	}

	private static Handler mProgressHandler = new Handler();
	private static Runnable updateTime = new Runnable() {
		public void run() {
			if (mediaPlayer == null) {
				return;
			}

			int current_position = mediaPlayer.getCurrentPosition();
			audioTimeTextView.setText(String.format("%02d : %02d : %02d",
					current_position / 60000, (current_position / 1000) % 60,
					(current_position / 10) % 100));
			playSeekBar.setProgress(current_position);

			if (mediaPlayer.isPlaying()) {
				playImageButton.setImageResource(R.drawable.pause_btn);
			} else {
				playImageButton.setImageResource(R.drawable.play_btn);
			}
			mProgressHandler.postDelayed(updateTime, 100);
		}
	};

	private OnClickListener backImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				mediaPlayer = null;
			}
			finish();
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				mediaPlayer = null;
			}
			finish();
		}
		return false;
	}

	private OnClickListener playImageButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				playImageButton.setImageResource(R.drawable.play_btn);
			} else {
				if (mediaPlayer.getCurrentPosition() == mediaPlayer
						.getDuration()) {
					mediaPlayer.seekTo(0);
				}
				mediaPlayer.start();
				playImageButton.setImageResource(R.drawable.pause_btn);
			}

		}
	};

	private OnSeekBarChangeListener playSeekBarOnSeekBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			mediaPlayer.seekTo(seekBar.getProgress());
			now_index = 0;
			for (int i = 0; i < lessonAnswerImageList.size(); i++) {
				if (lessonAnswerImageList.get(i).getTiming() >= seekBar
						.getProgress()) {
					break;
				}
				now_index = i;
			}
			setNowImage();
			setNowLine();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			int before = 1;
			while (lessonAnswerImageList.size() > before
					&& lessonAnswerImageList.get(before).getTiming() <= progress) {
				before++;
			}
			if (before - 1 != now_index) {
				now_index = before - 1;
				setNowImage();
				setNowLine();
			}

		}
	};

	private int width, height;

	private void setNowImage() {
		String path = Const.VIDEO_URL
				+ lessonAnswerImageList.get(now_index).getImage();

		File cacheDir = MylessonDetailAudio.this.getCacheDir();
		cacheDir.mkdirs();
		File cacheFile = new File(cacheDir, "" + path.hashCode());

		int dstWidth, dstHeight;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = 1;
		BitmapFactory.decodeFile(cacheFile.getAbsolutePath(), options);

		Point point = new Point();
		((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay()
				.getSize(point);
		dstWidth = point.x;
		dstHeight = dstWidth * options.outHeight / options.outWidth;
		int tmpWidth = options.outWidth;

		options.inSampleSize = 1;
		while (tmpWidth / 2 >= dstWidth) {
			tmpWidth /= 2;
			options.inSampleSize *= 2;
		}

		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeFile(cacheFile.getAbsolutePath(),
				options);
		Bitmap resized = Bitmap.createScaledBitmap(src, dstWidth, dstHeight,
				true);
		nowImageView.setImageBitmap(resized);
		nowImageView.invalidate();
		width = dstWidth;
		height = dstHeight;

	}

	private void setNowLine() {
		imageCountTextView.setText((now_index + 1) + " / "
				+ lessonAnswerImageList.size());
		drawingCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

		String line = lessonAnswerImageList.get(now_index).getLine();
		if (!line.trim().equals("")) {
			String[] lineInfo = line.split("-");

			for (int i = 0; i < lineInfo.length; i++) {
				double tx = Double.parseDouble(lineInfo[i].split("_")[0]);
				double ty = Double.parseDouble(lineInfo[i].split("_")[1]);
				int draw_type = Integer.parseInt(lineInfo[i].split("_")[2]);
				int paint_type = 0;
				if (lineInfo[i].split("_").length == 4) {
					paint_type = Integer.parseInt(lineInfo[i].split("_")[3]);
				}
				if (draw_type == 0) {
					double x = tx;
					double y = ty;
					i++;
					ty = Double.parseDouble(lineInfo[i].split("_")[1]);
					tx = Double.parseDouble(lineInfo[i].split("_")[0]);

					drawingCanvas.drawLine((float) x * width, (float) y
							* height, (float) tx * width, (float) ty * height,
							paintList.get(paint_type));
				} else if (draw_type == 1) {
					drawingCanvas.drawCircle((float) tx * width, (float) ty
							* height, (float) width
							/ Const.RADIUS_CIRCLE_FACTOR,
							paintList.get(paint_type));
				}
			}
		}

		drawingCanvasImageView.invalidate();
	}

	private OnPreparedListener mediaPlayerOnPreparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			Log.d("duration", "" + mp.getDuration());
			playSeekBar.setMax(mp.getDuration());
			mProgressHandler.postDelayed(updateTime, 100);
		}
	};

	private class GetFileTask extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... p) {
			for (int i = 0; i < lessonAnswerImageList.size(); i++) {
				getFile(lessonAnswerImageList.get(i).getImage());
			}
			getFile(lessonAnswer.getSound());

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			onCancelled();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

			if (mediaPlayer == null) {
				mediaPlayer = new MediaPlayer();
			} else {
				mediaPlayer.reset();
			}

			try {
				String path = Const.VIDEO_URL + lessonAnswer.getSound();

				File cacheDir = MylessonDetailAudio.this.getCacheDir();
				cacheDir.mkdirs();
				File cacheFile = new File(cacheDir, "" + path.hashCode());

				FileInputStream fis = new FileInputStream(
						cacheFile.getAbsolutePath());
				FileDescriptor fd = fis.getFD();
				// fis.close();

				mediaPlayer.setDataSource(fd);
				mediaPlayer.prepare();
				mediaPlayer
						.setOnPreparedListener(mediaPlayerOnPreparedListener);
			} catch (Exception e) {
				e.printStackTrace();
			}

			setNowImage();

			Log.d("height", "" + height);
			Log.d("width", "" + width);
			drawingBitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			drawingCanvas = new Canvas(drawingBitmap);
			drawingCanvasImageView.setImageBitmap(drawingBitmap);

			redPaint = new Paint();
			redPaint.setARGB(255, 255, 0, 0);
			redPaint.setStrokeWidth(width / Const.STROKE_LINE_FACTOR);
			redPaint.setAntiAlias(true);
			redPaint.setStyle(Paint.Style.STROKE);

			bluePaint = new Paint();
			bluePaint.setARGB(255, 0, 0, 255);
			bluePaint.setStrokeWidth(width / Const.STROKE_LINE_FACTOR);
			bluePaint.setAntiAlias(true);
			bluePaint.setStyle(Paint.Style.STROKE);

			yellowPaint = new Paint();
			yellowPaint.setARGB(255, 255, 255, 0);
			yellowPaint.setStrokeWidth(width / Const.STROKE_LINE_FACTOR);
			yellowPaint.setAntiAlias(true);
			yellowPaint.setStyle(Paint.Style.STROKE);

			paintList.add(redPaint);
			paintList.add(yellowPaint);
			paintList.add(bluePaint);

			setNowLine();
			progressDialog.dismiss();
		}

		void getFile(String filename) {
			String path = Const.VIDEO_URL + filename;

			File cacheDir = MylessonDetailAudio.this.getCacheDir();
			cacheDir.mkdirs();
			File cacheFile = new File(cacheDir, "" + path.hashCode());
			if (cacheFile.exists()) {
				return;
			}

			try {
				URLConnection cn = new URL(path).openConnection();
				cn.connect();
				InputStream stream = cn.getInputStream();

				InputStream input = new BufferedInputStream(stream);
				FileOutputStream out = new FileOutputStream(cacheFile);

				byte buf[] = new byte[16384];
				int numread;

				while ((numread = stream.read(buf)) > 0) {
					out.write(buf, 0, numread);
				}

				out.flush();
				out.close();
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
