package com.kapp.singlo.ui;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

@SuppressLint("NewApi")
public class TeacherLessonAnswer1 extends Activity {
	ProgressDialog progressDialog;

	ImageButton homeImageButton;
	ImageButton mylessonImageButton;
	ImageButton golfbagImageButton;
	ImageButton settingImageButton;

	TextView questionText;
	TextView questionUsernameTextView;
	Lesson lesson;

	int user_id;
	int lesson_id;

	ImageButton downloadVideoButton;
	ImageButton submitButton;

	Uri selected_video;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_lesson_answer_page1);

		progressDialog = new ProgressDialog(this);

		SharedPreferences spData = getSharedPreferences("login", MODE_PRIVATE);
		user_id = spData.getInt("id", 0);

		Intent intent = this.getIntent();
		lesson_id = intent.getIntExtra("lesson_id", 0);
		DBConnector db = new DBConnector(this);
		lesson = db.getLesson(lesson_id);
		db.close();

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
	}

	private OnTouchListener homeImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				homeImageButton
						.setBackgroundResource(R.drawable.main_top_profile_on);
				mylessonImageButton
						.setBackgroundResource(R.drawable.main_top_menub01);
				Intent intent = new Intent(TeacherLessonAnswer1.this,
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
				Intent intent = new Intent(TeacherLessonAnswer1.this,
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
				Intent intent = new Intent(TeacherLessonAnswer1.this,
						SingloVideoView.class);
				intent.putExtra("url", lesson.getVideo());
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);
				break;

			}
			return true;
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(TeacherLessonAnswer1.this,
					TeacherLesson.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			finish();

			break;
		}

		return super.onKeyDown(keyCode, event);
	}

	private OnTouchListener submitButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				Intent intent = new Intent(TeacherLessonAnswer1.this,
						TeacherLessonAnswer2.class);
				intent.putExtra("lesson_id", lesson_id);
				startActivity(intent);
				finish();
			}
			return true;
		}
	};
}
