package com.garagestory.singlo.teacher;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.garagestory.singlo.R;
import com.garagestory.singlo.util.AudioRecorder;
import com.garagestory.singlo.util.Const;
import com.garagestory.singlo.util.Coord;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TeacherLessonAudioRecord extends Activity {

	private List<String> bitmapFilenameList;
	private List<Bitmap> bitmapList;
	private List<Bitmap> paintBitmapList;
	private List<Canvas> canvasList;
	private List<ArrayList<Coord>> saveLineList;
	private List<Boolean> selectedList;
	private List<AudioRecorder> audioRecorderList;
	private ArrayList<Paint> paintList;
	private List<Long> changeTimeList;

	private List<ImageView> captureImageViewList;
	private List<ImageView> selectImageViewList;
	private List<ImageView> MICImageViewList;
	private List<RelativeLayout> relativeLayoutList;
	private List<TextView> captureTextViewList;

	private ImageButton deleteImageButton;
	private ImageButton changeOrderImageButton;
	private ImageButton recordImageButton;
	private ImageButton stopImageButton;
	private ImageButton cancelImageButton;

	private TextView recordTimeTextView;

	private String url;
	private int width, height;
	private int recordTime;

	private Paint redPaint;
	private Paint yellowPaint;
	private Paint bluePaint;

	private ProgressDialog processDialog;

	private GetCapture getCapture;
	private int server_id;
	private int changeOrderIndex;
	private int now_record;

	private long start_time;
	private long pause_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_lesson_audio_record);

		bitmapFilenameList = new ArrayList<String>();
		bitmapList = new ArrayList<Bitmap>();
		saveLineList = new ArrayList<ArrayList<Coord>>();
		canvasList = new ArrayList<Canvas>();
		paintBitmapList = new ArrayList<Bitmap>();
		paintList = new ArrayList<Paint>();
		changeTimeList = new ArrayList<Long>();

		captureImageViewList = new ArrayList<ImageView>();
		selectImageViewList = new ArrayList<ImageView>();
		MICImageViewList = new ArrayList<ImageView>();
		relativeLayoutList = new ArrayList<RelativeLayout>();
		captureTextViewList = new ArrayList<TextView>();
		selectedList = new ArrayList<Boolean>();
		audioRecorderList = new ArrayList<AudioRecorder>();

		Intent intent = this.getIntent();
		url = intent.getStringExtra("url");
		server_id = intent.getIntExtra("lesson_id", 0);
		int bitmapCount = intent.getIntExtra("bitmapCount", 0);
		for (int i = 0; i < bitmapCount; i++) {
			bitmapFilenameList.add(intent.getStringExtra("bitmapList" + i));

			ArrayList<Coord> tmp = intent
					.getParcelableArrayListExtra("lineList" + i);
			saveLineList.add(tmp);
			audioRecorderList.add(new AudioRecorder(
					TeacherLessonAudioRecord.this, i));
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

		relativeLayoutList
				.add((RelativeLayout) findViewById(R.id.RelativeLayout1));
		relativeLayoutList
				.add((RelativeLayout) findViewById(R.id.RelativeLayout2));
		relativeLayoutList
				.add((RelativeLayout) findViewById(R.id.RelativeLayout3));
		relativeLayoutList
				.add((RelativeLayout) findViewById(R.id.RelativeLayout4));
		relativeLayoutList
				.add((RelativeLayout) findViewById(R.id.RelativeLayout5));
		relativeLayoutList
				.add((RelativeLayout) findViewById(R.id.RelativeLayout6));
		relativeLayoutList
				.add((RelativeLayout) findViewById(R.id.RelativeLayout7));
		relativeLayoutList
				.add((RelativeLayout) findViewById(R.id.RelativeLayout8));

		selectImageViewList
				.add((ImageView) findViewById(R.id.selectedImageView1));
		selectImageViewList
				.add((ImageView) findViewById(R.id.selectedImageView2));
		selectImageViewList
				.add((ImageView) findViewById(R.id.selectedImageView3));
		selectImageViewList
				.add((ImageView) findViewById(R.id.selectedImageView4));
		selectImageViewList
				.add((ImageView) findViewById(R.id.selectedImageView5));
		selectImageViewList
				.add((ImageView) findViewById(R.id.selectedImageView6));
		selectImageViewList
				.add((ImageView) findViewById(R.id.selectedImageView7));
		selectImageViewList
				.add((ImageView) findViewById(R.id.selectedImageView8));

		MICImageViewList.add((ImageView) findViewById(R.id.MICImageView1));
		MICImageViewList.add((ImageView) findViewById(R.id.MICImageView2));
		MICImageViewList.add((ImageView) findViewById(R.id.MICImageView3));
		MICImageViewList.add((ImageView) findViewById(R.id.MICImageView4));
		MICImageViewList.add((ImageView) findViewById(R.id.MICImageView5));
		MICImageViewList.add((ImageView) findViewById(R.id.MICImageView6));
		MICImageViewList.add((ImageView) findViewById(R.id.MICImageView7));
		MICImageViewList.add((ImageView) findViewById(R.id.MICImageView8));

		captureTextViewList.add((TextView) findViewById(R.id.CaptureTextView1));
		captureTextViewList.add((TextView) findViewById(R.id.CaptureTextView2));
		captureTextViewList.add((TextView) findViewById(R.id.CaptureTextView3));
		captureTextViewList.add((TextView) findViewById(R.id.CaptureTextView4));
		captureTextViewList.add((TextView) findViewById(R.id.CaptureTextView5));
		captureTextViewList.add((TextView) findViewById(R.id.CaptureTextView6));
		captureTextViewList.add((TextView) findViewById(R.id.CaptureTextView7));
		captureTextViewList.add((TextView) findViewById(R.id.CaptureTextView8));

		deleteImageButton = (ImageButton) findViewById(R.id.RemoveImageButton);
		deleteImageButton.setOnClickListener(deleteImageButtonOnClickListener);
		changeOrderImageButton = (ImageButton) findViewById(R.id.ChangeOrderImageButton);
		changeOrderImageButton
				.setOnClickListener(changeOrderImageButtonOnClickListener);
		recordImageButton = (ImageButton) findViewById(R.id.RecordImageButton);
		recordImageButton.setOnClickListener(recordImageButtonOnClickListener);
		stopImageButton = (ImageButton) findViewById(R.id.StopImageButton);
		stopImageButton.setOnClickListener(stopImageButtonOnClickListener);
		cancelImageButton = (ImageButton) findViewById(R.id.CancelImageButton);
		cancelImageButton.setOnClickListener(cancelImageButtonOnClickListener);

		recordTimeTextView = (TextView) findViewById(R.id.RecordTimeTextView);

		getCapture = new GetCapture(this);
		getCapture.execute();
		now_record = -1;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	private boolean isRecording() {
		return (now_record != -1 && audioRecorderList.get(now_record)
				.isRecording());
	}

	private void addTiming() {
		changeTimeList.add((Long) System.currentTimeMillis() - start_time);
	}

	private OnClickListener relativeLayoutListOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			for (int i = 0; i < bitmapList.size(); i++) {
				if (((RelativeLayout) v).getId() == relativeLayoutList.get(i)
						.getId()) {
					if (isRecording()) {
						if (now_record + 1 == i) {
							MICImageViewList.get(now_record).setImageResource(
									R.drawable.mic_opacity_btn);
							audioRecorderList.get(now_record).pauseRecording();

							now_record = i;
							if (audioRecorderList.get(i) == null) {
								audioRecorderList.set(i, new AudioRecorder(
										TeacherLessonAudioRecord.this, i));
							}

							audioRecorderList.get(i).startRecording();
							addTiming();
							MICImageViewList.get(now_record).setImageResource(
									R.drawable.mic_btn);
						} else {
							Toast.makeText(TeacherLessonAudioRecord.this,
									"녹음은 순서대로 진행하여야 합니다.", Toast.LENGTH_SHORT)
									.show();
						}
					} else {
						if (selectedList.get(i)) {
							selectImageViewList.get(i).setImageResource(
									android.R.color.transparent);
							selectedList.set(i, false);
						} else {
							selectImageViewList.get(i).setImageResource(
									R.drawable.selected_icon);
							selectedList.set(i, true);
						}
					}
				}
			}

		}
	};
	OnClickListener deleteImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (isRecording()) {
				Toast.makeText(TeacherLessonAudioRecord.this,
						"음성 녹음을 일시 정지한 상태에서만 사진을 삭제하실 수 있습니다.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			int count = 0;
			for (int i = 0; i < selectedList.size(); i++) {
				if (selectedList.get(i)) {
					count++;
				}
			}

			if (count == 0) {
				Toast.makeText(TeacherLessonAudioRecord.this,
						"하나 이상의 사진을 선택하셔야 합니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			if (count == selectedList.size()) {
				Toast.makeText(TeacherLessonAudioRecord.this,
						"적어도 하나의 사진은 남겨두어야 합니다.", Toast.LENGTH_SHORT).show();
				return;
			}

			for (int i = selectedList.size() - 1; i >= 0; i--) {
				if (selectedList.get(i)) {
					selectImageViewList.get(i).setImageResource(
							android.R.color.transparent);
					selectedList.remove(i);
					bitmapList.remove(i);
					bitmapFilenameList.remove(i);
					saveLineList.remove(i);
					audioRecorderList.remove(i);
				}
			}
			drawImage();
		}
	};
	OnClickListener changeOrderImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (isRecording()) {
				Toast.makeText(TeacherLessonAudioRecord.this,
						"음성 녹음을 일시 정지한 상태에서만 사진을 이동하실 수 있습니다.",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (selectedList.size() == 1) {
				Toast.makeText(TeacherLessonAudioRecord.this, "사진이 하나뿐입니다.",
						Toast.LENGTH_SHORT).show();
				return;
			}

			int count = 0, tmp = 0;
			for (int i = 0; i < selectedList.size(); i++) {
				if (selectedList.get(i)) {
					tmp = i;
					count++;
				}
			}

			if (count == 0) {
				Toast.makeText(TeacherLessonAudioRecord.this,
						"위치를 이동시킬 사진을 하나만 선택해주세요.", Toast.LENGTH_SHORT).show();
				return;
			}
			if (count > 1) {
				Toast.makeText(TeacherLessonAudioRecord.this,
						"위치를 이동시킬 사진을 하나만 선택해주세요.", Toast.LENGTH_SHORT).show();
				return;
			}

			changeOrderIndex = tmp;

			String items[] = new String[selectedList.size() - 1];
			int j = 0;
			for (int i = 0; i < selectedList.size(); i++) {
				selectImageViewList.get(i).setImageResource(
						android.R.color.transparent);
				if (i == changeOrderIndex) {
					continue;
				}

				selectImageViewList.get(i).invalidate();
				if (i + 1 == selectedList.size()) {
					items[j] = "마지막으로";
				} else {
					items[j] = (i + 1) + "번 그림 앞으로";
				}
				j++;
			}

			AlertDialog.Builder ab = new AlertDialog.Builder(
					TeacherLessonAudioRecord.this);
			ab.setTitle("선택한 그림 위치 이동");
			ab.setSingleChoiceItems(items, -1,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Log.i("whichButton", "" + whichButton);

							for (int i = 0; i < selectedList.size(); i++) {
								audioRecorderList.set(i, new AudioRecorder(
										TeacherLessonAudioRecord.this, i));
							}

							Bitmap tmpBitmap = bitmapList.get(changeOrderIndex);
							String tmpBitmapFilename = bitmapFilenameList
									.get(changeOrderIndex);
							ArrayList<Coord> tmpSaveLine = saveLineList
									.get(changeOrderIndex);

							if (changeOrderIndex > whichButton) {
								for (int i = changeOrderIndex; i > whichButton; i--) {
									bitmapList.set(i, bitmapList.get(i - 1));
									bitmapFilenameList.set(i,
											bitmapFilenameList.get(i - 1));
									saveLineList.set(i, saveLineList.get(i - 1));
								}
							} else {
								for (int i = changeOrderIndex; i < whichButton; i++) {
									bitmapList.set(i, bitmapList.get(i + 1));
									bitmapFilenameList.set(i,
											bitmapFilenameList.get(i + 1));
									saveLineList.set(i, saveLineList.get(i + 1));
								}
							}
							bitmapList.set(whichButton, tmpBitmap);
							bitmapFilenameList.set(whichButton,
									tmpBitmapFilename);
							saveLineList.set(whichButton, tmpSaveLine);
							drawImage();
							dialog.cancel();
						}
					});
			AlertDialog alert = ab.create();
			alert.show();
		}
	};
	Handler mProgressHandler = new Handler();
	Runnable updateTime = new Runnable() {
		public void run() {
			if (now_record != -1 && audioRecorderList.get(now_record) != null
					&& audioRecorderList.get(now_record).isRecording()) {
				recordTime += 1;
				recordTimeTextView.setText(String.format("%02d : %02d : %02d",
						recordTime / 3600, (recordTime / 60) % 60,
						recordTime % 60));
				mProgressHandler.postDelayed(updateTime, 1000);
			}
		}
	};

	OnClickListener recordImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int k = now_record;
			if (now_record == -1) {
				k = 0;
			}

			if (audioRecorderList.get(k) == null) {
				audioRecorderList.set(k, new AudioRecorder(
						TeacherLessonAudioRecord.this, k));
			}

			if (audioRecorderList.get(k).isRecording()) {
				// pause
				audioRecorderList.get(k).pauseRecording();
				recordImageButton.setImageResource(R.drawable.record_btn);
				pause_time = System.currentTimeMillis();
			} else {
				now_record = k;
				// start Recording
				if (audioRecorderList.get(k).isStarted()) {
					audioRecorderList.get(k).resumeRecording();
					start_time += (System.currentTimeMillis() - pause_time);
				} else {
					start_time = System.currentTimeMillis();
					audioRecorderList.get(k).startRecording();
					addTiming();

					recordTime = 0;
					recordTimeTextView.setText("00 : 00 : 00");
				}
				MICImageViewList.get(now_record).setImageResource(
						R.drawable.mic_btn);
				recordImageButton.setImageResource(R.drawable.recordpause_btn);
				mProgressHandler.postDelayed(updateTime, 1000);
			}
		}
	};
	OnClickListener stopImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			for (int i = 0; i < audioRecorderList.size(); i++) {
				if (!audioRecorderList.get(i).isStarted()) {
					Toast.makeText(TeacherLessonAudioRecord.this,
							"레슨 녹음을 모두 마쳐야 합니다.", Toast.LENGTH_SHORT).show();
					return;
				}
			}

			AudioRecorder.mergeRecorder(TeacherLessonAudioRecord.this,
					audioRecorderList);
			Intent intent = new Intent();
			intent.putExtra("bitmapCount", bitmapList.size());
			for (int i = 0; i < bitmapList.size(); i++) {
				intent.putParcelableArrayListExtra("lineList" + i,
						saveLineList.get(i));
				intent.putExtra("bitmapList" + i, bitmapFilenameList.get(i));
				intent.putExtra("timingList" + i, changeTimeList.get(i));
			}

			setResult(RESULT_OK, intent);
			finish();
		}
	};
	OnClickListener cancelImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			AlertDialog.Builder ab = new AlertDialog.Builder(
					TeacherLessonAudioRecord.this);
			ab.setTitle("녹음 취소");
			ab.setMessage("모든 그림의 녹음이 취소되고 파일은 저장되지 않습니다.");
			ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					for (int i = 0; i < selectedList.size(); i++) {

						audioRecorderList.set(i, new AudioRecorder(
								TeacherLessonAudioRecord.this, i));
						MICImageViewList.get(i).setImageResource(
								android.R.color.transparent);
					}
					now_record = -1;
					recordTime = 0;
					recordTimeTextView.setText("00 : 00 : 00");
					recordImageButton.setImageResource(R.drawable.record_btn);
				}
			});
			ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			ab.show();
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
			intent.putExtra("lesson_id", server_id);
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

					if (!cacheFile.exists()) {
						String url = Const.CAPTURE_URL
								+ bitmapFilenameList.get(i);
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
					}
					Bitmap bitmap = BitmapFactory.decodeFile(cacheFile
							.getAbsolutePath());
					bitmapList.add(bitmap);
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			drawImage();

			processDialog.dismiss();
		}
	}

	private void drawImage() {
		paintBitmapList.clear();
		canvasList.clear();
		selectedList.clear();
		changeTimeList.clear();

		redPaint = new Paint();
		bluePaint = new Paint();
		yellowPaint = new Paint();
		for (int i = 0; i < bitmapFilenameList.size(); i++) {
			if (Build.VERSION.SDK_INT >= 16) {
				relativeLayoutList.get(i).setBackground(
						new BitmapDrawable(this.getResources(), bitmapList
								.get(i)));
			} else {
				relativeLayoutList.get(i).setBackgroundDrawable(
						new BitmapDrawable(this.getResources(), bitmapList
								.get(i)));
			}

			width = captureImageViewList.get(i).getWidth();
			height = captureImageViewList.get(i).getHeight();

			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			captureImageViewList.get(i).setImageBitmap(bitmap);

			paintBitmapList.add(bitmap);
			canvasList.add(canvas);

			canvas.drawColor(0, PorterDuff.Mode.CLEAR);

			redPaint.setARGB(255, 255, 0, 0);
			redPaint.setStrokeWidth(width / Const.STROKE_LINE_FACTOR);
			redPaint.setAntiAlias(true);
			redPaint.setStyle(Paint.Style.STROKE);

			bluePaint.setARGB(255, 0, 0, 255);
			bluePaint.setStrokeWidth(width / Const.STROKE_LINE_FACTOR);
			bluePaint.setAntiAlias(true);
			bluePaint.setStyle(Paint.Style.STROKE);

			yellowPaint.setARGB(255, 255, 255, 0);
			yellowPaint.setStrokeWidth(width / Const.STROKE_LINE_FACTOR);
			yellowPaint.setAntiAlias(true);
			yellowPaint.setStyle(Paint.Style.STROKE);

			paintList.clear();
			paintList.add(redPaint);
			paintList.add(yellowPaint);
			paintList.add(bluePaint);

			for (int j = 0; j < saveLineList.get(i).size(); j++) {
				if (saveLineList.get(i).get(j).draw_type == 0) {
					canvas.drawLine((float) saveLineList.get(i).get(j + 1).x
							* width, (float) saveLineList.get(i).get(j + 1).y
							* height, (float) saveLineList.get(i).get(j).x
							* width, (float) saveLineList.get(i).get(j).y
							* height, paintList
							.get(saveLineList.get(i).get(j).paint_type));
					j++;
				} else if (saveLineList.get(i).get(j).draw_type == 1) {
					canvas.drawCircle((float) saveLineList.get(i).get(j).x
							* width, (float) saveLineList.get(i).get(j).y
							* height, (float) width
							/ Const.RADIUS_CIRCLE_FACTOR, paintList
							.get(saveLineList.get(i).get(j).paint_type));
				}
			}
			captureImageViewList.get(i).invalidate();
			captureTextViewList.get(i).setText(
					(i + 1) + "/" + bitmapFilenameList.size());
			relativeLayoutList.get(i).setOnClickListener(
					relativeLayoutListOnClickListener);
			selectedList.add(false);
			selectImageViewList.get(i).setImageResource(
					android.R.color.transparent);

			MICImageViewList.get(i).setImageResource(
					android.R.color.transparent);
		}

		for (int i = bitmapFilenameList.size(); i < 8; i++) {
			captureTextViewList.get(i).setText("");
			captureImageViewList.get(i).setImageResource(
					android.R.color.transparent);
			captureImageViewList.get(i).setBackgroundResource(
					android.R.color.transparent);
			relativeLayoutList.get(i).setBackgroundResource(
					android.R.color.transparent);
			relativeLayoutList.get(i).setOnClickListener(null);
			MICImageViewList.get(i).setImageResource(
					android.R.color.transparent);
			selectImageViewList.get(i).setImageResource(
					android.R.color.transparent);
		}
	}
}
