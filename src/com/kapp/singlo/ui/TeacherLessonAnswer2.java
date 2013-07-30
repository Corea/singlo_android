package com.kapp.singlo.ui;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Coord;
import com.kapp.singlo.util.JSONParser;
import com.kapp.singlo.util.Utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

@SuppressLint("NewApi")
public class TeacherLessonAnswer2 extends Activity {
	ProgressDialog progressDialog;

	private List<String> bitmapFilenameList;
	private List<Bitmap> bitmapList;
	private List<ArrayList<Coord>> saveLineList;

	private ImageButton homeImageButton;
	private ImageButton mylessonImageButton;
	private ImageButton golfbagImageButton;
	private ImageButton settingImageButton;

	private List<SeekBar> seekBarList;
	private List<Button> causeButtonList;
	private List<Integer> scoreList;

	private Spinner recommendSpinner1;
	private Spinner recommendSpinner2;

	private TextView questionText;
	private TextView questionUsernameTextView;
	private TextView scoreTextView;
	private Lesson lesson;

	int user_id;
	int lesson_id;
	int cause;
	boolean complete;

	ImageButton downloadVideoButton;
	ImageButton uploadVideoButton;
	ImageButton submitButton;
	ImageButton cameraVideoButton;

	private static final String lineEnd = "\r\n";
	private static final String twoHyphens = "--";
	private static final String boundary = "*****";

	Uri selected_video;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_lesson_answer_page2);

		progressDialog = new ProgressDialog(this);

		SharedPreferences spData = getSharedPreferences("login", MODE_PRIVATE);
		user_id = spData.getInt("id", 0);

		Intent intent = this.getIntent();
		lesson_id = intent.getIntExtra("lesson_id", 0);
		DBConnector db = new DBConnector(this);
		lesson = db.getLesson(lesson_id);
		db.close();

		complete = false;

		homeImageButton = (ImageButton) findViewById(R.id.HomeImageButton);
		homeImageButton.setOnTouchListener(homeImageButtonTouchListener);
		mylessonImageButton = (ImageButton) findViewById(R.id.MylessonImageButton);
		golfbagImageButton = (ImageButton) findViewById(R.id.GolfbagImageButton);
		settingImageButton = (ImageButton) findViewById(R.id.SettingImageButton);
		settingImageButton.setOnTouchListener(settingImageButtonTouchListener);

		homeImageButton.setImageResource(R.drawable.main_top_profile_off);
		mylessonImageButton.setImageResource(R.drawable.main_top_menub02);
		golfbagImageButton.setImageResource(R.drawable.main_top_menuc01);
		settingImageButton.setImageResource(R.drawable.main_top_menud01);

		questionText = (TextView) findViewById(R.id.QuestionText);
		questionText.setText(lesson.getQuestion());
		questionUsernameTextView = (TextView) findViewById(R.id.QuestionUsernameTextView);
		questionUsernameTextView.setText(lesson.getUserName() + "님");

		downloadVideoButton = (ImageButton) findViewById(R.id.AnswerPlayButton);
		downloadVideoButton.setOnTouchListener(videoButtonTouchListener);

		Button target = null;
		switch (lesson.getClubType()) {
		case 1:
			target = (Button) findViewById(R.id.DriverButton);
			break;
		case 2:
			target = (Button) findViewById(R.id.WoodButton);
			break;
		case 3:
			target = (Button) findViewById(R.id.UtilityButton);
			break;
		case 4:
			target = (Button) findViewById(R.id.IronButton);
			break;
		case 5:
			target = (Button) findViewById(R.id.WedgeButton);
			break;
		case 6:
			target = (Button) findViewById(R.id.PutterButton);
			break;
		}
		target.setBackgroundResource(R.drawable.select_back);

		submitButton = (ImageButton) findViewById(R.id.AnswerButton);
		submitButton.setOnTouchListener(submitButtonTouchListener);
		
		scoreList = new ArrayList<Integer>();

		seekBarList = new ArrayList<SeekBar>();
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar1));
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar2));
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar3));
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar4));
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar5));
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar6));
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar7));
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar8));
		for (int i = 0; i < 8; i++) {
			scoreList.add(0);
			seekBarList.get(i).setMax(10);
			seekBarList.get(i).setOnSeekBarChangeListener(seekBarListener);
		}

		causeButtonList = new ArrayList<Button>();
		causeButtonList.add((Button) findViewById(R.id.CauseButton1));
		causeButtonList.add((Button) findViewById(R.id.CauseButton2));
		causeButtonList.add((Button) findViewById(R.id.CauseButton3));
		causeButtonList.add((Button) findViewById(R.id.CauseButton4));
		causeButtonList.add((Button) findViewById(R.id.CauseButton5));
		causeButtonList.add((Button) findViewById(R.id.CauseButton6));
		causeButtonList.add((Button) findViewById(R.id.CauseButton7));
		causeButtonList.add((Button) findViewById(R.id.CauseButton8));
		causeButtonList.add((Button) findViewById(R.id.CauseButton9));
		causeButtonList.add((Button) findViewById(R.id.CauseButton10));
		causeButtonList.add((Button) findViewById(R.id.CauseButton11));
		causeButtonList.add((Button) findViewById(R.id.CauseButton12));
		for (int i = 0; i < 12; i++) {
			causeButtonList.get(i).setOnClickListener(
					cause1ButtonOnClickListener);
		}
		cause = -1;

		scoreTextView = (TextView) findViewById(R.id.scoreTextView);

		recommendSpinner1 = (Spinner) findViewById(R.id.RecommendSpinner1);
		ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,
				R.array.cause, android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		recommendSpinner1.setAdapter(adapter1);
		recommendSpinner2 = (Spinner) findViewById(R.id.RecommendSpinner2);
		recommendSpinner2.setAdapter(adapter1);

	}

	private OnClickListener cause1ButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			for (int i = 0; i < 12; i++) {
				causeButtonList.get(i).setBackgroundResource(
						R.drawable.option_off_btn);
				if (causeButtonList.get(i).getId() == ((Button) v).getId()) {
					cause = i;
				}
			}

			((Button) v).setBackgroundResource(R.drawable.option_on_btn);

			// TODO Auto-generated method stub

		}
	};
	private OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			for (int i = 0; i < 8; i++) {
				if (seekBarList.get(i).getId() == seekBar.getId()) {
					scoreList.set(i, seekBar.getProgress());
				}
			}

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub

		}
	};

	private OnClickListener cameraVideoButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(
					android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, 2);
		}
	};
	private OnTouchListener homeImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				homeImageButton
						.setBackgroundResource(R.drawable.main_top_profile_on);
				mylessonImageButton
						.setBackgroundResource(R.drawable.main_top_menub01);
				Intent intent = new Intent(TeacherLessonAnswer2.this,
						TeacherHome.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
				overridePendingTransition(0, 0);

				break;

			}
			return true;
		}
	};
	private OnTouchListener settingImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				mylessonImageButton
						.setBackgroundResource(R.drawable.main_top_menub01);
				settingImageButton
						.setBackgroundResource(R.drawable.main_top_menud02);
				Intent intent = new Intent(TeacherLessonAnswer2.this,
						TeacherSetting.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
				overridePendingTransition(0, 0);

				break;

			}
			return true;
		}
	};
	private OnTouchListener videoButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Intent intent = new Intent(TeacherLessonAnswer2.this,
						TeacherLessonVideoView.class);
				intent.putExtra("url", lesson.getVideo());
				intent.putExtra("lesson_id", lesson.getServerID());
				startActivityForResult(intent, 99);
				overridePendingTransition(R.anim.fade, R.anim.hold);
				break;

			}
			return true;
		}
	};

	private OnTouchListener uploadVideoButtonTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
				// comma-separated MIME types
				mediaChooser.setType("video/*");
				startActivityForResult(mediaChooser, 1);
			default:
				break;
			}
			return false;
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(TeacherLessonAnswer2.this,
					TeacherLesson.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			finish();

			break;
		}

		return super.onKeyDown(keyCode, event);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 99) {
				complete = true;

				bitmapFilenameList = new ArrayList<String>();
				bitmapList = new ArrayList<Bitmap>();
				saveLineList = new ArrayList<ArrayList<Coord>>();

				int bitmapCount = data.getIntExtra("bitmapCount", 0);
				for (int i = 0; i < bitmapCount; i++) {
					bitmapFilenameList.add(data
							.getStringExtra("bitmapList" + i));
					Bitmap tmp_bitmap = BitmapFactory.decodeFile(""
							+ bitmapFilenameList.get(i).hashCode());
					bitmapList.add(tmp_bitmap);

					ArrayList<Coord> tmp = data
							.getParcelableArrayListExtra("lineList" + i);
					saveLineList.add(tmp);
				}
			} else {
				selected_video = data.getData();

				ContentResolver crThumb = getContentResolver();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;
				Bitmap bmThumbnail;
				try {
					String[] splited = selected_video.getPath().split("/");
					int id = Integer.parseInt(splited[splited.length - 1]);
					bmThumbnail = MediaStore.Video.Thumbnails.getThumbnail(
							crThumb, id, MediaStore.Video.Thumbnails.MINI_KIND,
							options);
				} catch (Exception e) {
					bmThumbnail = ThumbnailUtils.createVideoThumbnail(
							selected_video.getPath(), Thumbnails.MINI_KIND);
				}

				Drawable d = new BitmapDrawable(bmThumbnail);
				// uploadVideoButton.setBackgroundDrawable(d);
				Log.d("debug", selected_video.getPath());
			}
		}
	};

	private class SubmitAnswer extends AsyncTask<String, String, String> {

		boolean status;

		@Override
		protected String doInBackground(String... arg0) {
			try {
				status = true;
				submitAnswer();
				return "download completed";
			} catch (IOException e) {
				status = false;
				Log.d("debug", "The msg is : " + e.getMessage());
				return "download failed";
			}
		}

		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			if (status == false) {
				Toast.makeText(TeacherLessonAnswer2.this,
						"답변 전송 중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			Toast.makeText(TeacherLessonAnswer2.this, "답변을 전송했습니다.",
					Toast.LENGTH_SHORT).show();

			lesson.SetStatus(1);
			lesson.SetTeacherID(user_id);
			DBConnector db = new DBConnector(TeacherLessonAnswer2.this);
			db.updateLesson(lesson);
			db.close();

			Intent intent = new Intent(TeacherLessonAnswer2.this,
					TeacherLesson.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			finish();
		}

		private void submitAnswer() throws IOException {
			/*
			 * String[] proj = { MediaStore.Images.Media.DATA }; Cursor cursor =
			 * managedQuery(selected_video, proj, null, null, null); int
			 * column_index = cursor
			 * .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			 * cursor.moveToFirst(); String fileName =
			 * cursor.getString(column_index);
			 * 
			 * FileInputStream fileInputStream = new FileInputStream(fileName);
			 */
			// Log.d("File Up", "mFileInputStream is " + fileInputStream);

			URL url = new URL(Const.LESSON_ANSWER_URL);

			// open connection
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			// write data
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			// uploadedfile 파일이 ashx 핸들러에서 파일을 찾을 때 사용함으로 이름이 반드시 동일해야함..
			// 이름을 바꾸면 ashx 파일에서도 바꿀것.

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"lesson_id\""
					+ lineEnd + lineEnd + String.valueOf(lesson.getServerID())
					+ lineEnd);

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"teacher_id\""
					+ lineEnd + lineEnd + String.valueOf(user_id) + lineEnd);

			for (int i = 0; i < 8; i++) {
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"score"
						+ i + "\"" + lineEnd + lineEnd
						+ seekBarList.get(i).getProgress() + lineEnd);
			}

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"cause\""
					+ lineEnd + lineEnd + cause + lineEnd);

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"recommend1\""
					+ lineEnd + lineEnd
					+ recommendSpinner1.getSelectedItemPosition() + lineEnd);

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"recommend2\""
					+ lineEnd + lineEnd
					+ recommendSpinner2.getSelectedItemPosition() + lineEnd);

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"image_count\""
					+ lineEnd + lineEnd + bitmapList.size() + lineEnd);

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"audio\"; filename=\"audio.amr\""
					+ lineEnd + lineEnd);

			String path = Utility.makeDir("singlo_lesson");
			FileInputStream fileInputStream = new FileInputStream(path
					+ "audio.amr");

			int bytesAvailable = fileInputStream.available();
			int maxBufferSize = 16384;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);

			byte[] buffer = new byte[bufferSize];
			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);

				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			fileInputStream.close();

			dos.writeBytes(lineEnd);

			for (int i = 0; i < bitmapList.size(); i++) {

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"line" + i
						+ "\"" + lineEnd + lineEnd);
				for (int j = 0; j < saveLineList.get(i).size(); j++) {
					dos.writeBytes(saveLineList.get(i).get(j).x + "_"
							+ saveLineList.get(i).get(j).y + "_"
							+ (saveLineList.get(i).get(j).stop ? 1 : 0));
					if (j + 1 != saveLineList.get(i).size()) {
						dos.writeBytes("-");
					}
				}
				dos.writeBytes(lineEnd);

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"image"
						+ i + "\"; filename=\"image" + i + ".png\"" + lineEnd
						+ lineEnd);

				File cacheDir = TeacherLessonAnswer2.this.getCacheDir();
				cacheDir.mkdirs();
				File cacheFile = new File(cacheDir, ""
						+ bitmapFilenameList.get(i).hashCode());

				fileInputStream = new FileInputStream(cacheFile);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);

					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}
				fileInputStream.close();

				dos.writeBytes(lineEnd);
			}

			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			dos.flush();
			// dos.writeBytes(twoHyphens + boundary + lineEnd);
			// dos.writeBytes("Content-Disposition:form-data; name=\"video\"; filename=\""
			// + fileName + "\"" + lineEnd + lineEnd);

			// Log.d("File Up", "image byte is " + bytesRead);

			// Read 파일
			/*
			 * while (bytesRead > 0) { dos.write(buffer, 0, bufferSize);
			 * bytesAvailable = fileInputStream.available(); bufferSize =
			 * Math.min(bytesAvailable, maxBufferSize);
			 * 
			 * bytesRead = fileInputStream.read(buffer, 0, bufferSize); }
			 * 
			 * dos.writeBytes(lineEnd); dos.writeBytes(twoHyphens + boundary +
			 * twoHyphens + lineEnd);
			 * 
			 * // close streams Log.e("File Up", "File is written");
			 * dos.flush(); // 버퍼에 있는 값을 모두 밀어냄
			 */
			// 웹서버에서 결과를 받아 EditText 컨트롤에 보여줌
			int ch;
			InputStream is = conn.getInputStream();

			try {
				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromStream(is);
				if (json.getString("result").equals("fail")) {
					status = false;
				}
			} catch (Exception e) {

			}
			dos.close();

		};

	}

	private OnTouchListener submitButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (cause == -1) {
					Toast.makeText(TeacherLessonAnswer2.this,
							"증상을 진단하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
					return false;
				}
				if (recommendSpinner1.getSelectedItemPosition() == Spinner.INVALID_POSITION
						|| recommendSpinner1.getSelectedItemPosition() == 0
						|| recommendSpinner2.getSelectedItemPosition() == Spinner.INVALID_POSITION
						|| recommendSpinner2.getSelectedItemPosition() == 0) {
					Toast.makeText(TeacherLessonAnswer2.this,
							"추천 훈련을 선택하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
					return false;
				}
				if (complete == false) {
					Toast.makeText(TeacherLessonAnswer2.this,
							"레슨을 진행하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
					return false;
				}

				progressDialog.setMessage("잠시 기다려주세요.");
				progressDialog.setCancelable(false);
				progressDialog.show();

				new SubmitAnswer().execute();

				break;
			}
			return true;
		}
	};
}
