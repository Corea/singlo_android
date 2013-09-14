package com.garagestory.singlo.users;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.garagestory.singlo.GCMIntentService;
import com.garagestory.singlo.R;
import com.garagestory.singlo.http.HttpPost;
import com.garagestory.singlo.util.Const;
import com.google.android.gcm.GCMRegistrar;

public class Main extends Activity {
	private Button registerButton;
	private Button loginButton;

	public static Activity mainActivity;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		registerButton = (Button) findViewById(R.id.RegisterButton);
		registerButton.setOnClickListener(registerButtonClickListener);
		loginButton = (Button) findViewById(R.id.LoginButton);
		loginButton.setOnClickListener(loginButtonClickListener);

		mainActivity = Main.this;

		// main thread 에서 http post 사용하기 위해 선언.
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		if (HttpPost.check_version(mainActivity.getBaseContext()) == true) { // 버젼체크
			// GCM 등록
			String regId = GCMRegistrar
					.getRegistrationId(getApplicationContext());
			if ("".equals(regId)) {
				GCMRegistrar.register(getApplicationContext(),
						GCMIntentService.getProjectId());
			}

			SharedPreferences spLogin;
			spLogin = getSharedPreferences("login", Login.MODE_PRIVATE);
			int id = spLogin.getInt("id", 0);

			if (id != 0) {
				Intent intent = new Intent(Main.this, Login.class);
				startActivity(intent);
				finish();
			}
		} else {
			new AlertDialog.Builder(this)
					.setIcon(R.drawable.icon)
					.setTitle("버젼 정보 안내")
					.setMessage("최신버젼이 등록되었습니다. 업데이트후 접속하실수 있습니다.")
					.setPositiveButton("예",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									startActivity(new Intent(
											Intent.ACTION_VIEW,
											Uri.parse(Const.PLAY_STORE_URL)));
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
								}
							})
					.setNegativeButton("아니오",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
								}
							}).show();
		}

	}

	OnClickListener registerButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Main.this, Register.class);
			startActivity(intent);
		}
	};

	OnClickListener loginButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Main.this, Login.class);
			startActivity(intent);

		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			android.os.Process.killProcess(android.os.Process.myPid());
		default:
			break;
		}
		return false;
	}
}
