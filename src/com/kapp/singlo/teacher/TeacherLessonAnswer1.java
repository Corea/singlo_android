package com.kapp.singlo.teacher;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.kapp.sginlo.meta.SingloTeacherActivity;
import com.kapp.singlo.R;
import com.kapp.singlo.bg.APIPostAction;
import com.kapp.singlo.bg.APIPostAction.getAPIConnetorResultListener;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.ui.SingloVideoView;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Utility;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class TeacherLessonAnswer1 extends SingloTeacherActivity {

	private ImageView profileWebView;
	private TextView questionTextView;
	private TextView nameTextView;
	private TextView datetimeTextView;

	private Lesson lesson;
	//private 

	private int user_id;
	private int lesson_id;
	private String mThummailURL;

	private ImageView downloadVideoButton;
	private Button submitButton;
	
	private AQuery mAq;
	
	private getAPIConnetorResultListener mUserProfileListener = new getAPIConnetorResultListener() {
		
		@Override
		public void result(JSONObject object) {
			// TODO Auto-generated method stub
			JSONObject mData = object;
			try {
				String mPath = mData.getString("photo");
				mAq.id(profileWebView).image(Const.PROFILE_URL + mPath);				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_lesson_answer_page1);

		setTopMenu(1);

		/*SharedPreferences spData = getSharedPreferences("login", MODE_PRIVATE);
		user_id = spData.getInt("id", 0);*/

		Intent intent = this.getIntent();
		lesson_id = intent.getIntExtra("lesson_id", 0);
		mThummailURL = intent.getStringExtra("thumnail");
		user_id = intent.getIntExtra("userId", 0);				
		
		DBConnector db = new DBConnector(this);
		lesson = db.getLesson(lesson_id);
		db.close();
		
		mAq = new AQuery(this);		

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

		Button target = (Button) findViewById(R.id.ClubTypeButton);
		target.setText(Utility.getClubName(lesson.getClubType()));
		
		submitButton = (Button) findViewById(R.id.AnswerButton);
		submitButton.setOnTouchListener(submitButtonTouchListener);
		
		getUserThumnail();
	}
		
	private void getUserThumnail(){
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("user_id", Integer.toString(user_id));
		new APIPostAction(Const.GET_USER_PROFILE, mUserProfileListener).execute(params);
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
