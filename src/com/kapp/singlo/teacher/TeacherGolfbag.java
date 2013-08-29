package com.kapp.singlo.teacher;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kapp.sginlo.meta.SingloTeacherActivity;
import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Utility;

@SuppressLint("NewApi")
public class TeacherGolfbag extends SingloTeacherActivity {

	private int teacher_id;
	private Professional professional;

	private TextView NameTextView;
	private WebView profileWebView;

	private RatingBar speedRatingBar;
	private RatingBar accuracyRatingBar;
	private RatingBar priceRatingBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_golfbag);

		setTopMenu(2);

		SharedPreferences spLogin = getSharedPreferences("login",
				TeacherHome.MODE_PRIVATE);
		teacher_id = spLogin.getInt("id", 0);

		DBConnector db = new DBConnector(this);
		professional = db.getProfessionalByServerID(teacher_id);
		db.close();

		NameTextView = (TextView) findViewById(R.id.NameTextView);
		NameTextView.setText(professional.getName());

		profileWebView = (WebView) findViewById(R.id.ProfileWebView);
		if (professional.getPhoto() == null) {
			profileWebView.loadDataWithBaseURL(null,
					Utility.getImageHtmlCode(Const.PROFILE_NONE_URL),
					"text/html", "utf-8", null);
		} else {
			profileWebView.loadDataWithBaseURL(
					null,
					Utility.getImageHtmlCode(Const.PROFILE_URL
							+ professional.getPhoto()), "text/html", "utf-8",
					null);
		}

		speedRatingBar = (RatingBar) findViewById(R.id.SpeedRatingBar);
		accuracyRatingBar = (RatingBar) findViewById(R.id.AccuracyRatingBar);
		priceRatingBar = (RatingBar) findViewById(R.id.PriceRatingBar);

		speedRatingBar.setIsIndicator(true);
		accuracyRatingBar.setIsIndicator(true);
		priceRatingBar.setIsIndicator(true);
	}

	protected void onResume() {
		super.onResume();

		setTopImage(2);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		return false;
	}
}