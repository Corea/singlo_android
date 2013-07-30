package com.kapp.singlo.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;

@SuppressLint("NewApi")
public class Setting extends Activity {

	public static Activity settingActivity;

	private ImageButton homeImageButton;
	private ImageButton mylessonImageButton;
	private ImageButton golfbagImageButton;
	private ImageButton settingImageButton;

	private ImageButton alarmsetting;
	private ImageButton noticeImageButton;
	private ImageButton helpImageButton;
	private ImageButton logoutImageButton;

	private ImageButton lesson_request;

	static int sw = 0;

	private void removeLoginPreferences() {
		SharedPreferences spLogin;
		spLogin = getSharedPreferences("login", MODE_PRIVATE);
		SharedPreferences.Editor editor = spLogin.edit();
		editor.clear();
		editor.commit();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		settingActivity = Setting.this;

		homeImageButton = (ImageButton) findViewById(R.id.HomeImageButton);
		homeImageButton.setOnTouchListener(homeImageButtonTouchListener);
		mylessonImageButton = (ImageButton) findViewById(R.id.MylessonImageButton);
		mylessonImageButton
				.setOnTouchListener(mylessonImageButtonTouchListener);
		golfbagImageButton = (ImageButton) findViewById(R.id.GolfbagImageButton);
		settingImageButton = (ImageButton) findViewById(R.id.SettingImageButton);

		homeImageButton.setBackgroundResource(R.drawable.main_top_menua01);
		mylessonImageButton.setBackgroundResource(R.drawable.main_top_menub01);
		golfbagImageButton.setBackgroundResource(R.drawable.main_top_menuc01);
		settingImageButton.setBackgroundResource(R.drawable.main_top_menud02);

		alarmsetting = (ImageButton) findViewById(R.id.alarmsetting);
		alarmsetting.setOnTouchListener(alarmsettingTouchListener);
		noticeImageButton = (ImageButton) findViewById(R.id.NoticeImageButton);
		noticeImageButton.setOnTouchListener(noticeImageButtonTouchListener);
		helpImageButton = (ImageButton) findViewById(R.id.HelpImageButton);
		helpImageButton.setOnTouchListener(helpImageButtonTouchListener);
		logoutImageButton = (ImageButton) findViewById(R.id.LogoutImageButton);
		logoutImageButton.setOnTouchListener(logoutImageButtonTouchListener);

		lesson_request = (ImageButton) findViewById(R.id.LessonRequestImageButton);
		lesson_request.setOnTouchListener(lesson_requestTouchListener);

		if (sw == 0) {
			alarmsetting.setBackgroundResource(R.drawable.edit_off);
		} else {
			alarmsetting.setBackgroundResource(R.drawable.edit_on);
		}
	}

	protected void onResume() {
		super.onResume();

		homeImageButton.setBackgroundResource(R.drawable.main_top_menua01);
		mylessonImageButton.setBackgroundResource(R.drawable.main_top_menub01);
		golfbagImageButton.setBackgroundResource(R.drawable.main_top_menuc01);
		settingImageButton.setBackgroundResource(R.drawable.main_top_menud02);
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

	private OnTouchListener lesson_requestTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Intent intent = new Intent(Setting.this,
						LessonRequestPage1.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);

				break;

			}
			return true;
		}
	};
	private OnTouchListener alarmsettingTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				if (sw == 0) {
					alarmsetting.setBackgroundResource(R.drawable.edit_on);
					sw = 1;
				} else {
					alarmsetting.setBackgroundResource(R.drawable.edit_off);
					sw = 0;
				}

				break;

			}
			return true;
		}
	};
	private OnTouchListener noticeImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				noticeImageButton
						.setBackgroundResource(R.drawable.edit_notice02);
				helpImageButton.setBackgroundResource(R.drawable.edit_faq01);
				logoutImageButton
						.setBackgroundResource(R.drawable.edit_logout01);

				Intent intent = new Intent(Setting.this, Setting.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);

				break;

			}
			return true;
		}
	};
	private OnTouchListener helpImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				noticeImageButton
						.setBackgroundResource(R.drawable.edit_notice01);
				helpImageButton.setBackgroundResource(R.drawable.edit_faq02);
				logoutImageButton
						.setBackgroundResource(R.drawable.edit_logout01);

				Intent intent = new Intent(Setting.this, Setting.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);

				break;

			}
			return true;
		}
	};
	private OnTouchListener logoutImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				noticeImageButton
						.setBackgroundResource(R.drawable.edit_notice01);
				helpImageButton.setBackgroundResource(R.drawable.edit_faq01);
				logoutImageButton
						.setBackgroundResource(R.drawable.edit_logout02);

				// Logout

				removeLoginPreferences();
				DBConnector db = new DBConnector(Setting.this);
				db.RemoveAll();

				Intent intent = new Intent(Setting.this, Main.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.fade, R.anim.hold);

				break;

			}
			return true;
		}
	};

	private OnTouchListener homeImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Intent intent = new Intent(Setting.this, Home.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
				overridePendingTransition(0, 0);

				break;

			}
			return true;
		}
	};
	private OnTouchListener mylessonImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				Intent intent = new Intent(Setting.this, Mylesson.class);
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
			if (Setting.settingActivity != null) {
				Setting.settingActivity.finish();
			}
			android.os.Process.killProcess(android.os.Process.myPid());

			break;
		}

		return super.onKeyDown(keyCode, event);

	}
}
