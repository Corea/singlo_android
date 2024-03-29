package com.garagestory.singlo.users;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;


import com.garagestory.singlo.R;
import com.garagestory.singlo.data.DBConnector;
import com.garagestory.singlo.meta.SingloUserActivity;

@SuppressLint("NewApi")
public class Setting extends SingloUserActivity {

	public static Activity settingActivity;

	private LinearLayout noticeLinearLayout;
	private LinearLayout helpLinearLayout;
	private LinearLayout logoutLinearLayout;

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

		setTopMenu(3);

		settingActivity = Setting.this;

		noticeLinearLayout = (LinearLayout) findViewById(R.id.NoticeLinearLayout);
		noticeLinearLayout.setOnTouchListener(noticeImageButtonTouchListener);
		helpLinearLayout = (LinearLayout) findViewById(R.id.HelpLinearLayout);
		helpLinearLayout.setOnTouchListener(helpImageButtonTouchListener);
		logoutLinearLayout = (LinearLayout) findViewById(R.id.LogoutLinearLayout);
		logoutLinearLayout.setOnTouchListener(logoutImageButtonTouchListener);
	}


	protected void onResume() {
		super.onResume();

		setTopImage(3);
	}


	private OnTouchListener noticeImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Intent intent = new Intent(Setting.this, SettingBoard.class);
                intent.putExtra("board_name", "notice");
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

                Intent intent = new Intent(Setting.this, SettingBoard.class);
                intent.putExtra("board_name", "help");
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

