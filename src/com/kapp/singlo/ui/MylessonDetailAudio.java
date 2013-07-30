package com.kapp.singlo.ui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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
import android.widget.Toast;

public class MylessonDetailAudio extends Activity {

	private int lesson_id;
	private int now_index;

	private Lesson lesson;
	private LessonAnswer lessonAnswer;

	private List<LessonAnswerImage> lessonAnswerImageList;

	private Bitmap drawingBitmap;
	private Canvas drawingCanvas;
	private Paint drawingPaint;

	private ImageButton backImageButton;
	private ImageButton leftImageButton;
	private ImageButton rightImageButton;

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
		leftImageButton = (ImageButton) findViewById(R.id.LeftImageButton);
		leftImageButton.setOnClickListener(leftImageButtonOnClickListener);
		rightImageButton = (ImageButton) findViewById(R.id.RightImageButton);
		rightImageButton.setOnClickListener(rightImageButtonOnClickListener);

		playSeekBar = (SeekBar) findViewById(R.id.PlaySeekBar);
		playSeekBar
				.setOnSeekBarChangeListener(playSeekBarOnSeekBarChangeListener);

		drawingCanvasImageView = (ImageView) findViewById(R.id.DrawingCanvasImageView);
		nowImageView = (ImageView) findViewById(R.id.NowImageView);
		audioTimeTextView = (TextView) findViewById(R.id.AudioTimeTextView);
		imageCountTextView = (TextView) findViewById(R.id.ImageCountTextView);
		imageCountTextView.setText("1 / " + lessonAnswerImageList.size());

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
					current_position / 60000,
					(current_position / 1000) % 60,
					(current_position / 10) % 100));
			playSeekBar.setProgress(current_position);
			
			if (mediaPlayer.isPlaying()) {
				playImageButton.setImageResource(R.drawable.pause_btn);
			} else {
				playImageButton.setImageResource(R.drawable.play_btn);
			}
			mProgressHandler.postDelayed(updateTime, 30);
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

	private OnClickListener leftImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			now_index--;
			if (now_index < 0) {
				now_index = 0;
				Toast.makeText(MylessonDetailAudio.this, "처음 이미지입니다.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			setNowImage();
			setNowLine();
		}
	};
	private OnClickListener rightImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			now_index++;
			if (now_index >= lessonAnswerImageList.size()) {
				now_index = lessonAnswerImageList.size() - 1;
				Toast.makeText(MylessonDetailAudio.this, "마지막 이미지입니다.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			setNowImage();
			setNowLine();
		}
	};

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
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

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
		String line = lessonAnswerImageList.get(now_index).getLine();
		String[] lineInfo = line.split("-");
		double x = 0, y = 0;
		int stop = 1;

		imageCountTextView.setText((now_index + 1) + " / "
				+ lessonAnswerImageList.size());

		drawingCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

		for (int i = 0; i < lineInfo.length; i++) {
			double tx = Double.parseDouble(lineInfo[i].split("_")[0]);
			double ty = Double.parseDouble(lineInfo[i].split("_")[1]);
			int tstop = Integer.parseInt(lineInfo[i].split("_")[2]);
			if (stop == 0) {
				drawingCanvas.drawLine((float) x * width, (float) y * height,
						(float) tx * width, (float) ty * height, drawingPaint);
			}
			x = tx;
			y = ty;
			stop = tstop;

		}

		drawingCanvasImageView.invalidate();
	}

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

				mediaPlayer.setDataSource(cacheFile.getAbsolutePath());
				mediaPlayer.prepare();

				playSeekBar.setMax(mediaPlayer.getDuration());

				mProgressHandler.postDelayed(updateTime, 30);
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
			drawingPaint = new Paint();
			drawingPaint.setARGB(255, 255, 0, 0);
			drawingPaint.setStrokeWidth(width / 75);
			drawingPaint.setAntiAlias(true);
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
