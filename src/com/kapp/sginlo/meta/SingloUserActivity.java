package com.kapp.sginlo.meta;

import com.kapp.singlo.R;
import com.kapp.singlo.users.Golfbag;
import com.kapp.singlo.users.Home;
import com.kapp.singlo.users.LessonRequestPage1;
import com.kapp.singlo.users.Login;
import com.kapp.singlo.users.Mylesson;
import com.kapp.singlo.users.Setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

@SuppressLint("NewApi")
public class SingloUserActivity extends Activity {

	private ImageButton homeImageButton;
	private ImageButton mylessonImageButton;
	private ImageButton golfbagImageButton;
	private ImageButton settingImageButton;
	private ImageButton lessonRequestImageButton;
	private TextView mylessonAlertTextView;

	private Context context;

	private int topIndex;

	protected void setTopMenu(int index) {
		SharedPreferences spLogin = getSharedPreferences("login",
				Login.MODE_PRIVATE);
		int count = spLogin.getInt("count", 0);

		homeImageButton = (ImageButton) findViewById(R.id.HomeImageButton);
		homeImageButton.setOnClickListener(homeImageButtonOnClickListener);
		mylessonImageButton = (ImageButton) findViewById(R.id.MylessonImageButton);
		mylessonImageButton
				.setOnClickListener(mylessonImageButtonOnClickListener);
		golfbagImageButton = (ImageButton) findViewById(R.id.GolfbagImageButton);
		golfbagImageButton
				.setOnClickListener(golfbagImageButtonOnClickListener);
		settingImageButton = (ImageButton) findViewById(R.id.SettingImageButton);
		settingImageButton
				.setOnClickListener(settingImageButtonOnClickListener);

		lessonRequestImageButton = (ImageButton) findViewById(R.id.LessonRequestImageButton);
		lessonRequestImageButton
				.setOnClickListener(lessonRequestImageButtonOnClickListener);
		mylessonAlertTextView = (TextView) findViewById(R.id.MylessonAlertButton);
		if (count == 0) {
			mylessonAlertTextView.setVisibility(View.INVISIBLE);
		} else {
			mylessonAlertTextView.setVisibility(View.VISIBLE);
			mylessonAlertTextView.setText("" + count);
		}

		topIndex = index;
		setTopImage(topIndex);
		context = this;
	}

	protected void setTopImage(int index) {
		homeImageButton.setImageResource(R.drawable.prooff_btn);
		mylessonImageButton.setImageResource(R.drawable.mylessonoff_btn);
		golfbagImageButton.setImageResource(R.drawable.golfbagoff_btn);
		settingImageButton.setImageResource(R.drawable.settingoff_btn);

		switch (index) {
		case 0:
			homeImageButton.setImageResource(R.drawable.proon_btn);
			break;
		case 1:
			mylessonImageButton.setImageResource(R.drawable.mylessonon_btn);
			break;
		case 2:
			golfbagImageButton.setImageResource(R.drawable.golfbagon_btn);
			break;
		case 3:
			settingImageButton.setImageResource(R.drawable.settingon_btn);
			break;
		}
	}

	private OnClickListener homeImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setTopImage(0);

			if (context.getClass() == Home.class) {
				return;
			}
			Intent intent = new Intent(context, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
		}
	};

	private OnClickListener mylessonImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setTopImage(1);

			if (context.getClass() == Mylesson.class) {
				return;
			}
			Intent intent = new Intent(context, Mylesson.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
		}
	};

	private OnClickListener golfbagImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setTopImage(2);

			if (context.getClass() == Golfbag.class) {
				return;
			}
			Intent intent = new Intent(context, Golfbag.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
		}
	};

	private OnClickListener settingImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setTopImage(3);

			if (context.getClass() == Setting.class) {
				return;
			}
			Intent intent = new Intent(context, Setting.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
		}
	};

	private OnClickListener lessonRequestImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, LessonRequestPage1.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
		}
	};
}
