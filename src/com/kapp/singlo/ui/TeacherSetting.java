package com.kapp.singlo.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
public class TeacherSetting extends Activity {
	ImageButton menu01;
	ImageButton menu02;
	ImageButton menu03;
	ImageButton menu04;

	ImageButton alarmsetting;
	ImageButton settingmenu01;
	ImageButton settingmenu02;
	ImageButton settingmenu04;

	static int sw = 0;

	private void removeLoginPreferences() {
		SharedPreferences spLogin; // TODO: Encryption Need!
		spLogin = getSharedPreferences("login", MODE_PRIVATE);

		SharedPreferences.Editor editor = spLogin.edit();
		editor.clear();
		editor.commit();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teachersetting);

		menu01 = (ImageButton) findViewById(R.id.menu01);
		menu01.setOnTouchListener(menu01TouchListener);
		menu02 = (ImageButton) findViewById(R.id.menu02);
		menu02.setOnTouchListener(menu02TouchListener);
		menu03 = (ImageButton) findViewById(R.id.GolfbagImageButton);
		//menu03.setOnTouchListener(menu03TouchListener);
		menu04 = (ImageButton) findViewById(R.id.menu04);
		menu04.setOnTouchListener(menu04TouchListener);

		menu01.setBackgroundResource(R.drawable.main_top_menua01);
		menu02.setBackgroundResource(R.drawable.main_top_menub01);
		menu03.setBackgroundResource(R.drawable.main_top_menuc01);
		menu04.setBackgroundResource(R.drawable.main_top_menud02);

		alarmsetting = (ImageButton) findViewById(R.id.alarmsetting);
		alarmsetting.setOnTouchListener(alarmsettingTouchListener);
		settingmenu01 = (ImageButton) findViewById(R.id.NoticeImageButton);
		settingmenu01.setOnTouchListener(settingmenu01TouchListener);
		settingmenu02 = (ImageButton) findViewById(R.id.HelpImageButton);
		settingmenu02.setOnTouchListener(settingmenu02TouchListener);
		settingmenu04 = (ImageButton) findViewById(R.id.LogoutImageButton);
		settingmenu04.setOnTouchListener(settingmenu04TouchListener);

		if (sw == 0) {
			alarmsetting.setBackgroundResource(R.drawable.edit_off);
		} else {
			alarmsetting.setBackgroundResource(R.drawable.edit_on);
		}
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
	private OnTouchListener settingmenu01TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				settingmenu01.setBackgroundResource(R.drawable.edit_notice02);
				settingmenu02.setBackgroundResource(R.drawable.edit_faq01);
				settingmenu04.setBackgroundResource(R.drawable.edit_logout01);

				Intent intent = new Intent(TeacherSetting.this,
						TeacherSetting.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);
				finish();

				break;

			}
			return true;
		}
	};
	private OnTouchListener settingmenu02TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				settingmenu01.setBackgroundResource(R.drawable.edit_notice01);
				settingmenu02.setBackgroundResource(R.drawable.edit_faq02);
				settingmenu04.setBackgroundResource(R.drawable.edit_logout01);
				
				Intent intent = new Intent(TeacherSetting.this,
						TeacherSetting.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);
				finish();

				break;

			}
			return true;
		}
	};
	private OnTouchListener settingmenu04TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				settingmenu01.setBackgroundResource(R.drawable.edit_notice01);
				settingmenu02.setBackgroundResource(R.drawable.edit_faq01);
				settingmenu04.setBackgroundResource(R.drawable.edit_logout02);


				removeLoginPreferences();
				DBConnector db = new DBConnector(TeacherSetting.this);
				db.RemoveAll();

				Intent intent = new Intent(TeacherSetting.this, Main.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.fade, R.anim.hold);

				break;
			}
			return true;
		}
	};
	private OnTouchListener menu01TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				menu01.setBackgroundResource(R.drawable.main_top_menua01);
				menu02.setBackgroundResource(R.drawable.main_top_menub01);
				menu03.setBackgroundResource(R.drawable.main_top_menuc01);
				menu04.setBackgroundResource(R.drawable.main_top_menud01);
				Intent intent = new Intent(TeacherSetting.this,
						TeacherHome.class);
				startActivity(intent);
				finish();
				overridePendingTransition(0, 0);

				break;

			}
			return true;
		}
	};
	private OnTouchListener menu02TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				menu01.setBackgroundResource(R.drawable.main_top_menua01);
				menu02.setBackgroundResource(R.drawable.main_top_menub02);
				menu03.setBackgroundResource(R.drawable.main_top_menuc02);
				menu04.setBackgroundResource(R.drawable.main_top_menud01);
				Intent intent = new Intent(TeacherSetting.this,
						TeacherLesson.class);
				startActivity(intent);
				finish();
				overridePendingTransition(0, 0);

				break;

			}
			return true;
		}
	};
	private OnTouchListener menu03TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				menu01.setBackgroundResource(R.drawable.main_top_menua01);
				menu02.setBackgroundResource(R.drawable.main_top_menub01);
				menu03.setBackgroundResource(R.drawable.main_top_menuc02);
				menu04.setBackgroundResource(R.drawable.main_top_menud01);
				Intent intent = new Intent(TeacherSetting.this,
						TeacherGolfbag.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);
				finish();

				break;

			}
			return true;
		}
	};
	private OnTouchListener menu04TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				menu01.setBackgroundResource(R.drawable.main_top_menua01);
				menu02.setBackgroundResource(R.drawable.main_top_menub01);
				menu03.setBackgroundResource(R.drawable.main_top_menuc01);
				menu04.setBackgroundResource(R.drawable.main_top_menud02);
				Intent intent = new Intent(TeacherSetting.this,
						TeacherSetting.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);
				finish();

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