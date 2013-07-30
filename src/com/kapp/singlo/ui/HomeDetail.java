package com.kapp.singlo.ui;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class HomeDetail extends Activity {

	ImageButton homeImageButton;
	ImageButton mylessonImageButton;
	ImageButton golfbagImageButton;
	ImageButton settingImageButton;

	int teacher_id;

	Professional professional;

	TextView nameTextView;
	TextView priceTextView;
	TextView certificateTextView;
	TextView profileTextView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_detail);

		Intent intent = this.getIntent();
		teacher_id = intent.getIntExtra("teacher_id", 0);

		DBConnector db = new DBConnector(this);
		professional = db.getProfessionalByServerID(teacher_id);
		db.close();

		if (teacher_id == 0) {
			Toast.makeText(HomeDetail.this, "오류가 발생하였습니다.", Toast.LENGTH_SHORT)
					.show();
			intent = new Intent(HomeDetail.this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
			return;
		}

		homeImageButton = (ImageButton) findViewById(R.id.HomeImageButton);
		homeImageButton.setOnTouchListener(homeImageButtonTouchListener);
		mylessonImageButton = (ImageButton) findViewById(R.id.MylessonImageButton);
		mylessonImageButton
				.setOnTouchListener(mylessonImageButtonTouchListener);
		golfbagImageButton = (ImageButton) findViewById(R.id.GolfbagImageButton);
		settingImageButton = (ImageButton) findViewById(R.id.SettingImageButton);
		settingImageButton.setOnTouchListener(settingImageButtonTouchListener);

		nameTextView = (TextView) findViewById(R.id.TextView_01);
		priceTextView = (TextView) findViewById(R.id.TextView_02);
		certificateTextView = (TextView) findViewById(R.id.TextView_03);
		profileTextView = (TextView) findViewById(R.id.TextView_04);

		nameTextView.setText(professional.getName() + "/");
		priceTextView.setText("￦" + professional.getPrice());
		certificateTextView.setText(professional.getCertification());
		profileTextView.setText(professional.getProfile());
	}

	private OnTouchListener homeImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				Intent intent = new Intent(HomeDetail.this, Home.class);
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

				Intent intent = new Intent(HomeDetail.this, Mylesson.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
				overridePendingTransition(0, 0);

				break;

			}
			return true;
		}
	};
	private OnTouchListener settingImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				Intent intent = new Intent(HomeDetail.this, Setting.class);
				if (Home.homeActivity != null) {
					Home.homeActivity.finish();
				}
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
			Intent intent = new Intent(HomeDetail.this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);

		}

		return false;

	}

}
