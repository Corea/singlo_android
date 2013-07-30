package com.kapp.singlo.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;

@SuppressLint("NewApi")
public class TeacherHome extends Activity {

	WebView teacher_photo;

	TextView textview_00;
	TextView textview_01;
	TextView textview_02;
	TextView textview_03;
	TextView textview_04;

	ImageButton choicelesson;
	ImageButton recommendmovie;
	ImageButton submenu01;
	ImageButton submenu02;

	ImageButton homeImageButton;
	ImageButton mylessonImageButton;
	ImageButton golfbagImageButton;
	ImageButton settingImageButton;

	Professional professional;

	TextView nameTextView;
	TextView priceTextView;
	TextView certificateTextView;
	TextView lessonTextView;

	TextView golfer_text;

	int teacher_id;

	Professional teacher;

	ImageButton recommendVideoImageButton;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacherhome);

		SharedPreferences spLogin = getSharedPreferences("login",
				TeacherHome.MODE_PRIVATE);
		teacher_id = spLogin.getInt("id", 0);

		DBConnector db = new DBConnector(this);
		professional = db.getProfessionalByServerID(teacher_id);
		db.close();

		homeImageButton = (ImageButton) findViewById(R.id.HomeImageButton);
		mylessonImageButton = (ImageButton) findViewById(R.id.MylessonImageButton);
		mylessonImageButton
				.setOnTouchListener(mylessonImageButtonTouchListener);
		golfbagImageButton = (ImageButton) findViewById(R.id.GolfbagImageButton);
		settingImageButton = (ImageButton) findViewById(R.id.SettingImageButton);
		settingImageButton.setOnTouchListener(settingImageButtonTouchListener);

		nameTextView = (TextView) findViewById(R.id.TextView_01);
		priceTextView = (TextView) findViewById(R.id.TextView_02);
		certificateTextView = (TextView) findViewById(R.id.TextView_03);
		lessonTextView = (TextView) findViewById(R.id.TextView_04);

		nameTextView.setText(professional.getName() + "/");
		priceTextView.setText("￦" + professional.getPrice());
		certificateTextView.setText(professional.getCertification());
		lessonTextView.setText(professional.getLesson());

		recommendVideoImageButton = (ImageButton) findViewById(R.id.RecommendVideoImageButton);
		recommendVideoImageButton
				.setOnClickListener(recommendVideoImageButtonOnClickListener);

		homeImageButton.setImageResource(R.drawable.main_top_profile_on);

	}

	OnClickListener recommendVideoImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (professional.getUrl().isEmpty()) {
				return;
			}
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(professional.getUrl()));
			startActivity(intent);
		}
	};

	protected void onResume() {
		super.onResume();

		homeImageButton.setImageResource(R.drawable.main_top_profile_on);
		mylessonImageButton.setImageResource(R.drawable.main_top_menub01);
		golfbagImageButton.setImageResource(R.drawable.main_top_menuc01);
		settingImageButton.setImageResource(R.drawable.main_top_menud01);
	}

	public static String getImageHtmlCode(String _imageURL) {
		StringBuffer sb = new StringBuffer("<HTML>");
		sb.append("<HEAD>");
		sb.append("<style type='text/css'>");
		sb.append("body {");
		sb.append("margin-left: 0px;");
		sb.append("margin-top: 0px;");
		sb.append("margin-right: 0px;");
		sb.append("margin-bottom: 0px;");
		sb.append("}");
		sb.append("</style>");
		sb.append("</HEAD>");
		sb.append("<BODY>");
		sb.append("<img width=\"100%\" src=\"" + _imageURL + "\"/>");
		sb.append("</BODY>");
		sb.append("</HTML>");
		return sb.toString();
	}

	private OnTouchListener subbutton01TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				break;

			}
			return true;
		}
	};
	private OnTouchListener subbutton02TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				break;

			}
			return true;
		}
	};
	private OnTouchListener submenu01TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				submenu01.setBackgroundResource(R.drawable.pro_gyungruk02);
				submenu02.setBackgroundResource(R.drawable.pro_lessonlist01);
				golfer_text.setText(teacher.getProfile());

				break;

			}
			return true;
		}
	};
	private OnTouchListener submenu02TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				submenu01.setBackgroundResource(R.drawable.pro_gyungruk01);
				submenu02.setBackgroundResource(R.drawable.pro_lessonlist02);
				golfer_text.setText("레슨내역 (준비중입니다.)");

				break;

			}
			return true;
		}
	};
	private OnTouchListener mylessonImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				homeImageButton
						.setBackgroundResource(R.drawable.main_top_profile_off);
				mylessonImageButton
						.setBackgroundResource(R.drawable.main_top_menub02);
				Intent intent = new Intent(TeacherHome.this,
						TeacherLesson.class);
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

				homeImageButton
						.setBackgroundResource(R.drawable.main_top_profile_off);
				settingImageButton
						.setBackgroundResource(R.drawable.main_top_menud02);
				Intent intent = new Intent(TeacherHome.this,
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());

		}

		return false;

	}

}