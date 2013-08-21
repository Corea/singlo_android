package com.kapp.singlo.users;

import com.google.android.gcm.GCMRegistrar;
import com.kapp.singlo.GCMIntentService;
import com.kapp.singlo.R;
import com.kapp.singlo.billing.IabHelper;
import com.kapp.singlo.billing.IabResult;
import com.kapp.singlo.teacher.TeacherHome;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {

	Button registerButton;
	Button loginButton;
	
	public static Activity mainActivity;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// GCM 등록 ////
        String regId = GCMRegistrar.getRegistrationId(getApplicationContext());
		if("".equals(regId)){  
		      GCMRegistrar.register(getApplicationContext(), GCMIntentService.getProjectId());
		}
		else {
			
		}
		SharedPreferences spLogin;
		
		spLogin = getSharedPreferences("login", Login.MODE_PRIVATE);

		int id = spLogin.getInt("id", 0);

		if (id != 0) {
			Boolean isProfessional = spLogin.getBoolean("type", false);
			Intent intent;
			if (isProfessional) {
				intent = new Intent(Main.this, TeacherHome.class);
			} else {
				intent = new Intent(Main.this, Home.class);
			}
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
