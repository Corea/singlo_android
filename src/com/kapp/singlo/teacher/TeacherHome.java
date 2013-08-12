package com.kapp.singlo.teacher;

import android.annotation.SuppressLint;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.kapp.sginlo.meta.SingloTeacherActivity;
import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Utility;

@SuppressLint("NewApi")
public class TeacherHome extends SingloTeacherActivity {
	private ImageButton submenu01;
	private ImageButton submenu02;

	private Professional professional;

	private TextView nameTextView;
	private TextView priceTextView;
	private TextView scoreTextView;
	private TextView certificateTextView;

	private int teacher_id;

	private Professional teacher;

	private WebView profileWebView;
	private ImageButton recommendVideoImageButton;
	private TextView profileTextView;
	private RatingBar scoreRatingbar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_home);

		setTopMenu(0);

		SharedPreferences spLogin = getSharedPreferences("login",
				TeacherHome.MODE_PRIVATE);
		teacher_id = spLogin.getInt("id", 0);

		DBConnector db = new DBConnector(this);
		professional = db.getProfessionalByServerID(teacher_id);
		db.close();

		nameTextView = (TextView) findViewById(R.id.TextView_01);
		priceTextView = (TextView) findViewById(R.id.TextView_02);
		scoreTextView = (TextView) findViewById(R.id.ScoreTextView);
		certificateTextView = (TextView) findViewById(R.id.TextView_03);
		scoreRatingbar = (RatingBar) findViewById(R.id.ScoreRatingBar);

		nameTextView.setText(professional.getName() + "/");
		priceTextView.setText("￦" + professional.getPrice());
		certificateTextView.setText(professional.getCertification());
		scoreTextView.setText(professional.getEvaluationCount() + "명 "
				+ String.format("%.1f", professional.getEvaluationScore())
				+ "점");

		recommendVideoImageButton = (ImageButton) findViewById(R.id.RecommendVideoImageButton);
		recommendVideoImageButton
				.setOnClickListener(recommendVideoImageButtonOnClickListener);

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
		profileTextView = (TextView) findViewById(R.id.ProfileTextView);
		profileTextView.setText(professional.getProfile());
		scoreRatingbar.setRating((float) professional.getEvaluationScore());
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

		setTopImage(0);
	}

	private OnTouchListener submenu01TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				submenu01.setBackgroundResource(R.drawable.pro_gyungruk02);
				submenu02.setBackgroundResource(R.drawable.pro_lessonlist01);
				profileTextView.setText(teacher.getProfile());

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
				profileTextView.setText("레슨내역 (준비중입니다.)");

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