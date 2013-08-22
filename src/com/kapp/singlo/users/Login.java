package com.kapp.singlo.users;

import java.net.URLEncoder;

import com.kapp.singlo.R;
import com.kapp.singlo.bg.CallbackListener;
import com.kapp.singlo.bg.LoginAsyncTask;
import com.kapp.singlo.teacher.TeacherHome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class Login extends Activity implements CallbackListener {

	static Activity mainActivity;

	private EditText phoneNumberEditText;
	private EditText nameEditText;
	private EditText birthdayEditText;

	private ImageButton backImageButton;
	private Button startSingloButton;

	private LoginAsyncTask loginAsyncTask;

	private boolean isProfessional;
	private boolean loginProcess;
	private boolean autoLogin;
	private String name, birthday, phone;

	private SharedPreferences spLogin;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		spLogin = getSharedPreferences("login", Login.MODE_PRIVATE);

		int id = spLogin.getInt("id", 0);

		if (id != 0) {
			name = spLogin.getString("name", "");
			phone = spLogin.getString("phone", "");
			birthday = spLogin.getString("birthday", "");

			loginAsyncTask = new LoginAsyncTask();
			loginAsyncTask.setContext(Login.this);
			loginAsyncTask.setCallbackListener(Login.this);
			loginAsyncTask.execute(name, birthday, phone);
			autoLogin = true;
			return;
		}
		autoLogin = false;

		setContentView(R.layout.login);

		phoneNumberEditText = (EditText) findViewById(R.id.PhoneNumberEditText);
		TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		phoneNumberEditText.setText(mgr.getLine1Number());

		nameEditText = (EditText) findViewById(R.id.NameEditText);
		birthdayEditText = (EditText) findViewById(R.id.BirthdayEditText);

		backImageButton = (ImageButton) findViewById(R.id.BackImageButton);
		backImageButton.setOnClickListener(backClickListener);

		startSingloButton = (Button) findViewById(R.id.StartSingloButton);
		startSingloButton.setOnClickListener(startSingloButtonClickListener);

		loginProcess = false;
	}

	private OnClickListener backClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(nameEditText.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(birthdayEditText.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(phoneNumberEditText.getWindowToken(), 0);

			finish();
		}
	};

	private OnClickListener startSingloButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (loginProcess) {
				return;
			}
			loginProcess = true;

			try {
				name = URLEncoder.encode(nameEditText.getText().toString(),
						"UTF-8");
				birthday = URLEncoder.encode(birthdayEditText.getText()
						.toString(), "UTF-8");
				phone = URLEncoder.encode(phoneNumberEditText.getText()
						.toString(), "UTF-8");
			} catch (Exception e) {
				name = nameEditText.getText().toString();
				birthday = nameEditText.getText().toString();
				phone = nameEditText.getText().toString();
			}

			loginAsyncTask = new LoginAsyncTask();
			loginAsyncTask.setContext(Login.this);
			loginAsyncTask.setCallbackListener(Login.this);
			loginAsyncTask.execute(name, birthday, phone);
		}
	};

	private void saveLoginPreferences(LoginAsyncTask loginAsyncTask) {
		SharedPreferences.Editor editor = spLogin.edit();
		editor.putInt("id", loginAsyncTask.getID());
		editor.putBoolean("type", isProfessional);
		editor.putString("name", name);
		editor.putString("birthday", birthday);
		editor.putString("phone", phone);
		editor.putString("photo", loginAsyncTask.getPhoto());
		editor.putInt("count", loginAsyncTask.getCount());
		editor.commit();
	}

	public void callback(Object... params) {
		LoginAsyncTask loginAsyncTask = (LoginAsyncTask) params[0];

		if (loginAsyncTask.getLoginSuccess()) {
			isProfessional = loginAsyncTask.isProfessional();
			saveLoginPreferences(loginAsyncTask);

			Intent intent;
			if (isProfessional) {
				intent = new Intent(Login.this, TeacherHome.class);
			} else {
				intent = new Intent(Login.this, Home.class);
			}

			if (Main.mainActivity != null) {
				Main.mainActivity.finish();
			}
			startActivity(intent);
			finish();
		} else {
			String msg = "이름, 생일, 전화번호가 일치하는 정보가 없습니다.";
			AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);

			gsDialog.setTitle("로그인 실패");
			gsDialog.setMessage(msg);
			gsDialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							//
						}
					}).create().show();
			if (autoLogin) {
				setContentView(R.layout.login);

				phoneNumberEditText = (EditText) findViewById(R.id.PhoneNumberEditText);
				TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				phoneNumberEditText.setText(mgr.getLine1Number());

				nameEditText = (EditText) findViewById(R.id.NameEditText);
				birthdayEditText = (EditText) findViewById(R.id.BirthdayEditText);

				backImageButton = (ImageButton) findViewById(R.id.BackImageButton);
				backImageButton.setOnClickListener(backClickListener);

				startSingloButton = (Button) findViewById(R.id.StartSingloButton);
				startSingloButton
						.setOnClickListener(startSingloButtonClickListener);
			}
		}
		loginProcess = false;
	}
}
