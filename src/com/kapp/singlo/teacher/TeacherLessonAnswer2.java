package com.kapp.singlo.teacher;

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

import com.androidquery.AQuery;
import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.meta.SingloTeacherActivity;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Coord;
import com.kapp.singlo.util.JSONParser;
import com.kapp.singlo.util.Utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class TeacherLessonAnswer2 extends SingloTeacherActivity {
	ProgressDialog progressDialog;

	private List<String> bitmapFilenameList;
	private List<Bitmap> bitmapList;
	private List<ArrayList<Coord>> saveLineList;
	private List<Long> changeTimeList;

	private List<SeekBar> seekBarList;
	private List<Button> causeButtonList;
	private List<Integer> scoreList;

	private ImageView profileWebView;
	private TextView questionTextView;
	private TextView nameTextView;
	private TextView datetimeTextView;
	private Lesson lesson;

	private int user_id;
	private int lesson_id;
	private int cause;
	private boolean complete;

	private ImageView downloadVideoButton;
	private Button submitButton;

	private AQuery mAq;
	private String mThummailURL;
	private String mUserThumnailURL;

	Uri selected_video;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_lesson_answer_page2);

		setTopMenu(1);

		progressDialog = new ProgressDialog(this);

		SharedPreferences spData = getSharedPreferences("login", MODE_PRIVATE);
		user_id = spData.getInt("id", 0);

		Intent intent = this.getIntent();
		lesson_id = intent.getIntExtra("lesson_id", 0);
		mThummailURL = intent.getStringExtra("thumnail");
		mUserThumnailURL = intent.getStringExtra("user_thumnail");

		DBConnector db = new DBConnector(this);
		lesson = db.getLesson(lesson_id);
		db.close();

		mAq = new AQuery(this);

		complete = false;

		questionTextView = (TextView) findViewById(R.id.QuestionTextView);
		questionTextView.setText(lesson.getQuestion());
		nameTextView = (TextView) findViewById(R.id.NameTextView);
		nameTextView.setText(lesson.getUserName());
		datetimeTextView = (TextView) findViewById(R.id.DatetimeTextView);
		datetimeTextView.setText(lesson.getCreatedDatetime());
		profileWebView = (ImageView) findViewById(R.id.ProfileWebView);

		downloadVideoButton = (ImageView) findViewById(R.id.AnswerPlayButtonImg);
		downloadVideoButton.setOnTouchListener(videoButtonTouchListener);
		mAq.id(downloadVideoButton).image(Const.CAPTURE_URL + mThummailURL);
		mAq.id(profileWebView).image(Const.PROFILE_URL + mUserThumnailURL);

		Button target = (Button) findViewById(R.id.ClubTypeButton);
		switch (lesson.getClubType()) {
		case 1:
			target.setText("드라이버");
			break;
		case 2:
			target.setText("우드");
			break;
		case 3:
			target.setText("유틸리티");
			break;
		case 4:
			target.setText("아이언");
			break;
		case 5:
			target.setText("웨지");
			break;
		case 6:
			target.setText("퍼터");
			break;
		}

		submitButton = (Button) findViewById(R.id.AnswerButton);
		submitButton.setOnClickListener(submitButtonOnClickListener);

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
	}

	protected void onResume() {
		super.onResume();

		setTopImage(1);
	}

	private OnClickListener cause1ButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			for (int i = 0; i < 12; i++) {
				causeButtonList.get(i).setBackgroundResource(
						R.drawable.noselect_back);
				if (causeButtonList.get(i).getId() == ((Button) v).getId()) {
					cause = i;
				}
			}

			((Button) v).setBackgroundResource(R.drawable.select_back);
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
				changeTimeList = new ArrayList<Long>();

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
					changeTimeList.add(data.getLongExtra("timingList" + i, 0));
				}
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
					"multipart/form-data;boundary=" + Const.boundary);

			// write data
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"lesson_id\""
					+ Const.lineEnd + Const.lineEnd
					+ String.valueOf(lesson.getServerID()) + Const.lineEnd);

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"teacher_id\""
					+ Const.lineEnd + Const.lineEnd + String.valueOf(user_id)
					+ Const.lineEnd);

			for (int i = 0; i < 8; i++) {
				dos.writeBytes(Const.twoHyphens + Const.boundary
						+ Const.lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"score"
						+ i + "\"" + Const.lineEnd + Const.lineEnd
						+ seekBarList.get(i).getProgress() + Const.lineEnd);
			}

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"cause\""
					+ Const.lineEnd + Const.lineEnd + cause + Const.lineEnd);

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"image_count\""
					+ Const.lineEnd
					+ Const.lineEnd
					+ bitmapList.size()
					+ Const.lineEnd);

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"audio\"; filename=\"record_final.wav\""
					+ Const.lineEnd + Const.lineEnd);

			String path = Utility.getCacheFilename(TeacherLessonAnswer2.this,
					"record_final.wav", false);
			FileInputStream fileInputStream = new FileInputStream(path);

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

			dos.writeBytes(Const.lineEnd);

			for (int i = 0; i < bitmapList.size(); i++) {

				dos.writeBytes(Const.twoHyphens + Const.boundary
						+ Const.lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"line" + i
						+ "\"" + Const.lineEnd + Const.lineEnd);
				for (int j = 0; j < saveLineList.get(i).size(); j++) {
					dos.writeBytes(saveLineList.get(i).get(j).x + "_"
							+ saveLineList.get(i).get(j).y + "_"
							+ saveLineList.get(i).get(j).draw_type + "_"
							+ saveLineList.get(i).get(j).paint_type);
					if (j + 1 != saveLineList.get(i).size()) {
						dos.writeBytes("-");
					}
				}
				dos.writeBytes(Const.lineEnd);

				dos.writeBytes(Const.twoHyphens + Const.boundary
						+ Const.lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"image"
						+ i + "\"; filename=\"image" + i + ".png\""
						+ Const.lineEnd + Const.lineEnd);

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

				dos.writeBytes(Const.lineEnd);
			}

			for (int i = 0; i < changeTimeList.size(); i++) {
				dos.writeBytes(Const.twoHyphens + Const.boundary
						+ Const.lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"timing"
						+ i + "\"" + Const.lineEnd + Const.lineEnd
						+ changeTimeList.get(i) + Const.lineEnd);
			}

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.twoHyphens
					+ Const.lineEnd);
			dos.flush();

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

	private OnClickListener submitButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (cause == -1) {
				Toast.makeText(TeacherLessonAnswer2.this, "증상을 진단하지 않으셨습니다.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (complete == false) {
				Toast.makeText(TeacherLessonAnswer2.this, "레슨을 진행하지 않으셨습니다.",
						Toast.LENGTH_SHORT).show();
				return;
			}

			progressDialog.setMessage("잠시 기다려주세요.");
			progressDialog.setCancelable(false);
			progressDialog.show();

			new SubmitAnswer().execute();

		}
	};
}
