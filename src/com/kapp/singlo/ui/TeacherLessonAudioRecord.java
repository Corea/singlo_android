package com.kapp.singlo.ui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.kapp.singlo.R;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Coord;
import com.kapp.singlo.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TeacherLessonAudioRecord extends Activity {

	private List<String> bitmapFilenameList;
	private List<Bitmap> bitmapList;
	private List<Bitmap> paintBitmapList;
	private List<Canvas> canvasList;
	private List<ArrayList<Coord>> saveLineList;
	private List<ImageView> captureImageViewList;

	private ImageButton deleteImageButton;
	private ImageButton recordImageButton;
	private ImageButton stopImageButton;
	private ImageButton cancelImageButton;

	private TextView recordTimeTextView;

	private String url;
	private int width, height;
	private int recordTime;
	private Paint paint;

	private ProgressDialog processDialog;
	private MediaRecorder mediaRecorder;

	private GetCapture getCapture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_lesson_audio_record);

		bitmapFilenameList = new ArrayList<String>();
		bitmapList = new ArrayList<Bitmap>();
		saveLineList = new ArrayList<ArrayList<Coord>>();
		captureImageViewList = new ArrayList<ImageView>();
		canvasList = new ArrayList<Canvas>();
		paintBitmapList = new ArrayList<Bitmap>();

		Intent intent = this.getIntent();
		url = intent.getStringExtra("url");
		int bitmapCount = intent.getIntExtra("bitmapCount", 0);
		for (int i = 0; i < bitmapCount; i++) {
			bitmapFilenameList.add(intent.getStringExtra("bitmapList" + i));

			ArrayList<Coord> tmp = intent
					.getParcelableArrayListExtra("lineList" + i);
			saveLineList.add(tmp);
		}

		captureImageViewList
				.add((ImageView) findViewById(R.id.CaptureImageView1));
		captureImageViewList
				.add((ImageView) findViewById(R.id.CaptureImageView2));
		captureImageViewList
				.add((ImageView) findViewById(R.id.CaptureImageView3));
		captureImageViewList
				.add((ImageView) findViewById(R.id.CaptureImageView4));
		captureImageViewList
				.add((ImageView) findViewById(R.id.CaptureImageView5));
		captureImageViewList
				.add((ImageView) findViewById(R.id.CaptureImageView6));
		captureImageViewList
				.add((ImageView) findViewById(R.id.CaptureImageView7));
		captureImageViewList
				.add((ImageView) findViewById(R.id.CaptureImageView8));

		deleteImageButton = (ImageButton) findViewById(R.id.RemoveImageButton);
		deleteImageButton.setOnClickListener(deleteImageButtonOnClickListener);
		recordImageButton = (ImageButton) findViewById(R.id.RecordImageButton);
		recordImageButton.setOnClickListener(recordImageButtonOnClickListener);
		stopImageButton = (ImageButton) findViewById(R.id.StopImageButton);
		stopImageButton.setOnClickListener(stopImageButtonOnClickListener);
		cancelImageButton = (ImageButton) findViewById(R.id.CancelImageButton);
		cancelImageButton.setOnClickListener(cancelImageButtonOnClickListener);

		recordTimeTextView = (TextView) findViewById(R.id.RecordTimeTextView);

		getCapture = new GetCapture(this);
		getCapture.execute();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		/*
		 * width = nowImageView.getWidth(); height = nowImageView.getHeight();
		 * 
		 * bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		 * canvas = new Canvas(bitmap); nowImageView.setImageBitmap(bitmap);
		 * 
		 * paint = new Paint(); paint.setARGB(128, 255, 0, 0);
		 * paint.setStrokeWidth(width / 50); paint.setAntiAlias(true);
		 * 
		 * setBitmap(0);
		 */
	}

	/*
	 * private void setBitmap(int index) { }
	 */

	/*
	 * OnClickListener backImageButtonOnClickListener = new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { Intent intent = new
	 * Intent(TeacherLessonAudioRecord.this, TeacherLessonVideoView.class);
	 * intent.putExtra("url", url); intent.putExtra("audio", true);
	 * intent.putExtra("bitmapCount", bitmapList.size()); for (int i = 0; i <
	 * bitmapList.size(); i++) { intent.putParcelableArrayListExtra("lineList" +
	 * i, saveLineList.get(i)); intent.putExtra("bitmapList" + i,
	 * bitmapFilenameList.get(i)); }
	 * intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	 * startActivity(intent); finish(); } }; OnClickListener
	 * leftImageButtonOnClickListener = new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { now_index -= 1; if (now_index <
	 * 0) { now_index = 0; Toast.makeText(TeacherLessonAudioRecord.this,
	 * "처음 사진입니다.", Toast.LENGTH_SHORT).show(); } else { setBitmap(now_index); }
	 * } };
	 * 
	 * OnClickListener rightImageButtonOnClickListener = new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { now_index += 1; if (now_index >=
	 * bitmapList.size()) { now_index -= 1;
	 * Toast.makeText(TeacherLessonAudioRecord.this, "마지막 사진입니다.",
	 * Toast.LENGTH_SHORT).show(); } else { setBitmap(now_index); } } };
	 */

	OnClickListener deleteImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {/*
									 * if (bitmapList.size() == 1) {
									 * Toast.makeText
									 * (TeacherLessonAudioRecord.this,
									 * "적어도 하나의 사진은 남겨두어야 합니다.",
									 * Toast.LENGTH_SHORT).show(); return; }
									 * bitmapList.remove(now_index); if
									 * (now_index >= bitmapList.size()) {
									 * now_index -= 1; } setBitmap(now_index);
									 */
		}
	};

	Handler mProgressHandler = new Handler();
	Runnable updateTime = new Runnable() {
		public void run() {
			if (recordTime < 0) {
				return;
			}
			recordTime += 30;
			recordTimeTextView.setText(String.format("%02d : %02d : %02d",
					recordTime / 60000, (recordTime / 1000) % 60,
					(recordTime / 10) % 100));
			mProgressHandler.postDelayed(updateTime, 30);
		}
	};

	OnClickListener recordImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			recordTime = 0;
			recordTimeTextView.setText("00 : 00 : 00");

			if (mediaRecorder == null) {
				mediaRecorder = new MediaRecorder();
				mediaRecorder.reset();
			} else {
				mediaRecorder.reset();
			}

			try {
				String fileDir = Utility.makeDir("singlo_lesson");

				mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				mediaRecorder
						.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
				mediaRecorder
						.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
				mediaRecorder.setOutputFile(fileDir + "audio.amr");
				mediaRecorder.prepare();
				mediaRecorder.start();
				mProgressHandler.postDelayed(updateTime, 30);
			} catch (Exception e) {
				Log.e("Exception", e.toString());
			}

		}
	};
	OnClickListener stopImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				mediaRecorder.stop();
			} catch (Exception e) {
				Log.e("Exception", e.toString());
			} finally {
				mediaRecorder.release();
				mediaRecorder = null;
			}

			recordTime = -999;
			Intent intent = new Intent();
			intent.putExtra("bitmapCount", bitmapList.size());
			for (int i = 0; i < bitmapList.size(); i++) {
				intent.putParcelableArrayListExtra("lineList" + i,
						saveLineList.get(i));
				intent.putExtra("bitmapList" + i, bitmapFilenameList.get(i));
			}

			setResult(RESULT_OK, intent);
			finish();

		}
	};
	OnClickListener cancelImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
			/*
			 * String fileDir = Utility.makeDir("singlo_lesson");
			 * 
			 * mediaPlayer = new MediaPlayer(); try {
			 * mediaPlayer.setDataSource(fileDir + "audio.amr");
			 * mediaPlayer.prepare(); mediaPlayer.start(); } catch (Exception e)
			 * { Log.e("Exception", e.toString()); }
			 * 
			 * // TODO Auto-generated method stub
			 */
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			Intent intent = new Intent(TeacherLessonAudioRecord.this,
					TeacherLessonVideoView.class);
			intent.putExtra("url", url);
			intent.putExtra("audio", true);
			intent.putExtra("bitmapCount", bitmapList.size());
			for (int i = 0; i < bitmapList.size(); i++) {
				intent.putParcelableArrayListExtra("lineList" + i,
						saveLineList.get(i));
				intent.putExtra("bitmapList" + i, bitmapFilenameList.get(i));
			}
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
		}
		return false;
	}

	private class GetCapture extends AsyncTask<Void, Void, Void> {

		Context context;

		public GetCapture(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			processDialog = new ProgressDialog(context);
			processDialog.setMessage("로딩중...");
			processDialog.setIndeterminate(false);
			processDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			for (int i = 0; i < bitmapFilenameList.size(); i++) {
				try {
					File cacheDir = context.getCacheDir();
					cacheDir.mkdirs();
					File cacheFile = new File(cacheDir, ""
							+ bitmapFilenameList.get(i).hashCode());
					if (cacheFile.exists()) {
						continue;
					}

					String url = Const.CAPTURE_URL + bitmapFilenameList.get(i);
					URLConnection cn = new URL(url).openConnection();
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

					Bitmap bitmap = BitmapFactory.decodeFile(cacheFile
							.getAbsolutePath());
					bitmapList.add(bitmap);
				} catch (Exception e) {

				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			paint = new Paint();

			for (int i = 0; i < bitmapFilenameList.size(); i++) {
				captureImageViewList.get(i).setBackground(
						new BitmapDrawable(context.getResources(), bitmapList
								.get(i)));
			}

			for (int i = 0; i < bitmapFilenameList.size(); i++) {

				width = captureImageViewList.get(i).getWidth();
				height = captureImageViewList.get(i).getHeight();

				Bitmap bitmap = Bitmap.createBitmap(width, height,
						Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(bitmap);
				captureImageViewList.get(i).setImageBitmap(bitmap);

				paintBitmapList.add(bitmap);
				canvasList.add(canvas);

				canvas.drawColor(0, PorterDuff.Mode.CLEAR);

				paint.setARGB(255, 255, 0, 0);
				paint.setStrokeWidth(width / 75);
				paint.setAntiAlias(true);

				for (int j = 1; j < saveLineList.get(i).size(); j++) {
					if (saveLineList.get(i).get(j - 1).stop) {
						continue;
					}
					canvas.drawLine((float) saveLineList.get(i).get(j - 1).x
							* width, (float) saveLineList.get(i).get(j - 1).y
							* height, (float) saveLineList.get(i).get(j).x
							* width, (float) saveLineList.get(i).get(j).y
							* height, paint);
				}
				captureImageViewList.get(i).invalidate();
			}

			processDialog.dismiss();
		}
	}

}
