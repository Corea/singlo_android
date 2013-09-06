package com.kapp.singlo.teacher;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.meta.SingloTeacherActivity;
import com.kapp.singlo.users.Main;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

@SuppressLint("NewApi")
public class TeacherSetting extends SingloTeacherActivity {
	private ImageButton pushStatusImageButton;
	private ImageButton lessonStatusImageButton;

	private LinearLayout noticeLinearLayout;
	private LinearLayout helpLinearLayout;
	private LinearLayout deactiveLinearLayout;
	private LinearLayout logoutLinearLayout;

	private int teacher_id;
	private int sw = 0;
	private Professional professional;

	private void removeLoginPreferences() {
		SharedPreferences spLogin;
		spLogin = getSharedPreferences("login", MODE_PRIVATE);

		SharedPreferences.Editor editor = spLogin.edit();
		editor.clear();
		editor.commit();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_setting);

		setTopMenu(3);

		SharedPreferences spLogin = getSharedPreferences("login",
				TeacherHome.MODE_PRIVATE);
		teacher_id = spLogin.getInt("id", 0);

		DBConnector db = new DBConnector(this);
		professional = db.getProfessionalByServerID(teacher_id);
		db.close();

		pushStatusImageButton = (ImageButton) findViewById(R.id.PushStatusImageButton);
		pushStatusImageButton
				.setOnClickListener(pushStatusImageButtonOnClickListener);
		lessonStatusImageButton = (ImageButton) findViewById(R.id.LessonStatusImageButton);
		lessonStatusImageButton
				.setOnClickListener(lessonStatusImageButtonOnClickListener);

		noticeLinearLayout = (LinearLayout) findViewById(R.id.NoticeLinearLayout);
		noticeLinearLayout.setOnTouchListener(settingmenu01TouchListener);
		helpLinearLayout = (LinearLayout) findViewById(R.id.HelpLinearLayout);
		helpLinearLayout.setOnTouchListener(settingmenu02TouchListener);
		logoutLinearLayout = (LinearLayout) findViewById(R.id.LogoutLinearLayout);
		logoutLinearLayout.setOnTouchListener(settingmenu04TouchListener);
		deactiveLinearLayout = (LinearLayout) findViewById(R.id.DeactiveLinearLayout);
		deactiveLinearLayout
				.setOnClickListener(deactiveLinearLayoutOnClickListener);

		if (sw == 0) {
			pushStatusImageButton.setBackgroundResource(R.drawable.switchoff_btn);
		} else {
			pushStatusImageButton.setBackgroundResource(R.drawable.switchon_btn);
		}
		if (professional.getStatus() == 1) {
			lessonStatusImageButton.setBackgroundResource(R.drawable.switchon_btn);
		} else {
			lessonStatusImageButton.setBackgroundResource(R.drawable.switchoff_btn);
		}
	}

	protected void onResume() {
		super.onResume();

		setTopImage(3);
	}

	private OnClickListener pushStatusImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			sw = 1 - sw;

			if (sw == 0) {
				pushStatusImageButton
						.setBackgroundResource(R.drawable.switchoff_btn);
			} else {
				pushStatusImageButton.setBackgroundResource(R.drawable.switchon_btn);
			}
		}
	};

	private LessonStatusTeacherAsyncTask lessonStatusTeacherAsyncTask;
	private OnClickListener lessonStatusImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			lessonStatusTeacherAsyncTask = new LessonStatusTeacherAsyncTask();
			lessonStatusTeacherAsyncTask.execute(teacher_id,
					1 - professional.getStatus());
		}
	};

	private OnTouchListener settingmenu01TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			}
			return true;
		}
	};
	private OnTouchListener settingmenu02TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			}
			return true;
		}
	};
	private OnTouchListener settingmenu04TouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				removeLoginPreferences();
				DBConnector db = new DBConnector(TeacherSetting.this);
				db.RemoveAll();

				Intent intent = new Intent(TeacherSetting.this, Main.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.fade, R.anim.hold);

				break;
			}
			return true;
		}
	};

	private OnClickListener deactiveLinearLayoutOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(TeacherSetting.this,
					TeacherSettingDeactive.class);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
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

	public class LessonStatusTeacherAsyncTask extends
			AsyncTask<Integer, Void, Void> {

		private int teacher_id;
		private int status;

		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Integer... v) {
			teacher_id = v[0];
			status = v[1];

			change_status();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (professional.getStatus() == 1) {
				lessonStatusImageButton
						.setBackgroundResource(R.drawable.switchon_btn);
			} else {
				lessonStatusImageButton
						.setBackgroundResource(R.drawable.switchoff_btn);
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		void change_status() {
			String url = Const.TEACHER_LESSON_STATUS_URL;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			InputStream is;

			Log.d("Login", url);
			try {
				List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("teacher_id", String
						.valueOf(teacher_id)));
				nameValuePairs.add(new BasicNameValuePair("status", String
						.valueOf(status)));

				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				is = httpResponse.getEntity().getContent();

				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromStream(is);

				String result = json.getString("result");

				if (!result.equals("success")) {
					Toast.makeText(TeacherSetting.this, "상태 변경에 실패하였습니다.",
							Toast.LENGTH_LONG).show();
				} else {
					DBConnector dbConnector = new DBConnector(
							TeacherSetting.this);
					professional.setStatus(status);
					dbConnector.updateProfessional(professional);
					dbConnector.close();
				}
			} catch (Exception e) {
				Log.d("disp", "err : " + e.getMessage());
			}
		}
	}
}