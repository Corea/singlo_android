package com.kapp.singlo.teacher;

import com.kapp.sginlo.meta.SingloTeacherActivity;
import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.ui.SingloVideoView;

import android.annotation.SuppressLint;
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
public class TeacherLessonAnswer1 extends SingloTeacherActivity {
	private ProgressDialog progressDialog;

	private TextView questionText;
	private TextView questionUsernameTextView;
	private Lesson lesson;

	private int user_id;
	private int lesson_id;

	private ImageButton downloadVideoButton;
	private ImageButton submitButton;

	private Uri selected_video;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_lesson_answer_page1);
		
		setTopMenu(1);
		
		progressDialog = new ProgressDialog(this);

		SharedPreferences spData = getSharedPreferences("login", MODE_PRIVATE);
		user_id = spData.getInt("id", 0);

		Intent intent = this.getIntent();
		lesson_id = intent.getIntExtra("lesson_id", 0);
		DBConnector db = new DBConnector(this);
		lesson = db.getLesson(lesson_id);
		db.close();

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

	protected void onResume() {
		super.onResume();

		setTopImage(1);
	}
	
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
