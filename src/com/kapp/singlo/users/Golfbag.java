package com.kapp.singlo.users;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.TextView;

import com.kapp.sginlo.meta.SingloUserActivity;
import com.kapp.singlo.R;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Utility;

@SuppressLint("NewApi")
public class Golfbag extends SingloUserActivity {

	private WebView profileWebView;
	private TextView nameTextView;

	int user_id;

	String name;
	String photo;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.golfbag);
		
		setTopMenu(2);

		SharedPreferences spLogin = getSharedPreferences("login",
				Login.MODE_PRIVATE);
		user_id = spLogin.getInt("id", 0);
		photo = spLogin.getString("photo", "");
		name = spLogin.getString("name", "");

		profileWebView = (WebView) findViewById(R.id.ProfileWebView);
		profileWebView.loadDataWithBaseURL(null,
				Utility.getImageHtmlCode(Const.PROFILE_URL + photo),
				"text/html", "utf-8", null);
		nameTextView = (TextView) findViewById(R.id.NameTextView);
		nameTextView.setText(name);
	}

	@Override
	protected void onResume() {
		super.onResume();
	
		setTopImage(2);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:
			if (Mylesson.mylessonActivity != null) {
				Mylesson.mylessonActivity.finish();
			}
			android.os.Process.killProcess(android.os.Process.myPid());

			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
