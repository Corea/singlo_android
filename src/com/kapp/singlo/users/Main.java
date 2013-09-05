package com.kapp.singlo.users;

import com.google.android.gcm.GCMRegistrar;
import com.kapp.singlo.GCMIntentService;
import com.kapp.singlo.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
	private Button registerButton;
	private Button loginButton;

	public static Activity mainActivity;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// GCM 등록
		String regId = GCMRegistrar.getRegistrationId(getApplicationContext());
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

		registerButton = (Button) findViewById(R.id.RegisterButton);
		registerButton.setOnClickListener(registerButtonClickListener);
		loginButton = (Button) findViewById(R.id.LoginButton);
		loginButton.setOnClickListener(loginButtonClickListener);

		mainActivity = Main.this;
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
