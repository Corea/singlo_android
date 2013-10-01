package com.garagestory.singlo.meta;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.garagestory.singlo.R;
import com.garagestory.singlo.teacher.TeacherGolfbag;
import com.garagestory.singlo.teacher.TeacherHome;
import com.garagestory.singlo.teacher.TeacherLesson;
import com.garagestory.singlo.teacher.TeacherSetting;
import com.garagestory.singlo.users.Login;

@SuppressLint("NewApi")
public class SingloTeacherActivity extends Activity {

	private ImageButton homeImageButton;
	private ImageButton mylessonImageButton;
	//private ImageButton golfbagImageButton;
	private ImageButton settingImageButton;
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
		//golfbagImageButton = (ImageButton) findViewById(R.id.GolfbagImageButton);
		//golfbagImageButton.setOnClickListener(golfbagImageButtonOnClickListener);
		settingImageButton = (ImageButton) findViewById(R.id.SettingImageButton);
		settingImageButton
				.setOnClickListener(settingImageButtonOnClickListener);

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
		homeImageButton.setImageResource(R.drawable.nogolfbag_profileoff_btn);
		mylessonImageButton.setImageResource(R.drawable.nogolfbag_mylessonoff_btn);
		//golfbagImageButton.setImageResource(R.drawable.golfbagoff_btn);
		settingImageButton.setImageResource(R.drawable.nogolfbag_settingoff_btn);

		switch (index) {
		case 0:
			homeImageButton.setImageResource(R.drawable.nogolfbag_profileon_btn);
			break;
		case 1:
			mylessonImageButton.setImageResource(R.drawable.nogolfbag_mylessonon_btn);
			break;
		case 2:
			//golfbagImageButton.setImageResource(R.drawable.golfbagon_btn);
			break;
		case 3:
			settingImageButton.setImageResource(R.drawable.nogolfbag_settingon_btn);
			break;
		}
	}

	private OnClickListener homeImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setTopImage(0);

			if (context.getClass() == TeacherHome.class) {
				return;
			}
			Intent intent = new Intent(context, TeacherHome.class);
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

			if (context.getClass() == TeacherLesson.class) {
				return;
			}
			Intent intent = new Intent(context, TeacherLesson.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
		}
	};

	/*private OnClickListener golfbagImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setTopImage(2);

			if (context.getClass() == TeacherGolfbag.class) {
				return;
			}
			Intent intent = new Intent(context, TeacherGolfbag.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);

		}
	};*/

	private OnClickListener settingImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setTopImage(3);

			if (context.getClass() == TeacherSetting.class) {
				return;
			}
			Intent intent = new Intent(context, TeacherSetting.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
		}
	};
}
