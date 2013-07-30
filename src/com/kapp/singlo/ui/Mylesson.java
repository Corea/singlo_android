package com.kapp.singlo.ui;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

@SuppressLint("NewApi")
public class Mylesson extends Activity {
	public static Activity mylessonActivity;

	private ImageButton homeImageButton;
	private ImageButton mylessonImageButton;
	private ImageButton golfbagImageButton;
	private ImageButton settingImageButton;

	private ImageButton lesson_request;

	private ImageButton completeLessonImageButton;
	private ImageButton waitingLessonImageButton;

	ListView Custom_List;

	// 핵심 contents 들을 ListView로 뿌리기 위한 변수 선언.
	private ArrayList<Mylesson_List_Data> Array_Data;
	private Mylesson_Adapter adapter;

	private ProgressDialog progressDialog;
	// background process loading

	private List<Lesson> lessons;
	private SharedPreferences spLogin;

	private int user_id;

	private UserLessonTask userLessonTask;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylesson);

		spLogin = getSharedPreferences("login", Login.MODE_PRIVATE);
		user_id = spLogin.getInt("id", 0);

		mylessonActivity = Mylesson.this;

		Array_Data = new ArrayList<Mylesson_List_Data>();
		Array_Data.clear();

		homeImageButton = (ImageButton) findViewById(R.id.HomeImageButton);
		homeImageButton.setOnTouchListener(homeImageButtonTouchListener);
		mylessonImageButton = (ImageButton) findViewById(R.id.MylessonImageButton);
		golfbagImageButton = (ImageButton) findViewById(R.id.GolfbagImageButton);
		settingImageButton = (ImageButton) findViewById(R.id.SettingImageButton);
		settingImageButton.setOnTouchListener(settingImageButtonTouchListener);

		homeImageButton.setImageResource(R.drawable.main_top_menua01);
		mylessonImageButton.setImageResource(R.drawable.main_top_menub02);
		golfbagImageButton.setImageResource(R.drawable.main_top_menuc01);
		settingImageButton.setImageResource(R.drawable.main_top_menud01);

		lesson_request = (ImageButton) findViewById(R.id.LessonRequestImageButton);
		lesson_request.setOnTouchListener(lesson_requestTouchListener);

		completeLessonImageButton = (ImageButton) findViewById(R.id.CompleteLessonImageButton);
		completeLessonImageButton
				.setOnClickListener(completeLessonImageButtonOnClickListener);
		waitingLessonImageButton = (ImageButton) findViewById(R.id.WaitingLessonImageButton);
		waitingLessonImageButton
				.setOnClickListener(waitingLessonImageButtonOnClickListener);

		progressDialog = ProgressDialog.show(Mylesson.this, "", "준비중입니다.",
				true, false);
		userLessonTask = new UserLessonTask();
		userLessonTask.execute();

	}

	OnClickListener completeLessonImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			completeLessonImageButton
					.setImageResource(R.drawable.completelessonon_btn);
			waitingLessonImageButton
					.setImageResource(R.drawable.waitinglessonoff_btn);
			DBConnector db = new DBConnector(Mylesson.this);
			Array_Data.clear();

			for (int i = 0; i < lessons.size(); i++) {
				String image_url;
				if (lessons.get(i).getStatus() == 1) {
					Mylesson_List_Data data;
					if (lessons.get(i).getTeacherID() == null
							|| lessons.get(i).getTeacherID() == 0) {
						image_url = "http://garagestory.cafe24.com/img/none.jpg";
						data = new Mylesson_List_Data(image_url, "신속 레슨 /",
								"￦ 9000", "", "", R.drawable.selected_checkbox);
					} else {
						Professional professional = db
								.getProfessionalByServerID(lessons.get(i)
										.getTeacherID());
						image_url = "http://garagestory.cafe24.com/img/teacher/"
								+ professional.getPhoto();
						data = new Mylesson_List_Data(image_url,
								professional.getName() + "/", "￦"
										+ String.valueOf(professional
												.getPrice()),
								professional.getCertification(), "추천 레슨 : "
										+ professional.getLesson(),
								R.drawable.selected_checkbox);
					}
					Array_Data.add(data);
				}

			}
			db.close();

			adapter = null;
			adapter = new Mylesson_Adapter(getBaseContext(),
					android.R.layout.simple_list_item_1, Array_Data);
			Custom_List.setAdapter(adapter);
		}
	};

	OnClickListener waitingLessonImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			completeLessonImageButton
					.setImageResource(R.drawable.completelessonoff_btn);
			waitingLessonImageButton
					.setImageResource(R.drawable.waitinglessonon_btn);
			DBConnector db = new DBConnector(Mylesson.this);
			Array_Data.clear();

			for (int i = 0; i < lessons.size(); i++) {
				String image_url;
				if (lessons.get(i).getStatus() == 0) {

					Mylesson_List_Data data;

					if (lessons.get(i).getTeacherID() == null
							|| lessons.get(i).getTeacherID() == 0) {
						image_url = "http://garagestory.cafe24.com/img/none.jpg";
						data = new Mylesson_List_Data(image_url, "신속 레슨 /",
								"￦ 9000", "", "",
								R.drawable.nonselected_checkbox);
					} else {
						Professional professional = db
								.getProfessionalByServerID(lessons.get(i)
										.getTeacherID());
						image_url = "http://garagestory.cafe24.com/img/teacher/"
								+ professional.getPhoto();
						data = new Mylesson_List_Data(image_url,
								professional.getName() + "/", "￦"
										+ String.valueOf(professional
												.getPrice()),
								professional.getCertification(), "추천 레슨 : "
										+ professional.getLesson(),
								R.drawable.nonselected_checkbox);
					}
					Array_Data.add(data);
				}

			}
			db.close();

			adapter = null;
			adapter = new Mylesson_Adapter(getBaseContext(),
					android.R.layout.simple_list_item_1, Array_Data);
			Custom_List.setAdapter(adapter);
		}
	};

	protected void onResume() {
		super.onResume();

		homeImageButton.setBackgroundResource(R.drawable.main_top_menua01);
		mylessonImageButton.setBackgroundResource(R.drawable.main_top_menub02);
		golfbagImageButton.setBackgroundResource(R.drawable.main_top_menuc01);
		settingImageButton.setBackgroundResource(R.drawable.main_top_menud01);
	}

	/*
	 * public static String getImageHtmlCode(String _imageURL) { StringBuffer sb
	 * = new StringBuffer("<HTML>"); sb.append("<HEAD>");
	 * sb.append("<style type='text/css'>"); sb.append("body {");
	 * sb.append("margin-left: 0px;"); sb.append("margin-top: 0px;");
	 * sb.append("margin-right: 0px;"); sb.append("margin-bottom: 0px;");
	 * sb.append("}"); sb.append("</style>"); sb.append("</HEAD>");
	 * sb.append("<BODY>"); sb.append("<img width=\"100%\" src=\"" + _imageURL +
	 * "\"/>"); sb.append("</BODY>"); sb.append("</HTML>"); return
	 * sb.toString(); }
	 */
	// 각 리스트 클릭시
	private OnItemClickListener CustomListItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View v, int pos,
				long id) {

			if (lessons.get(pos).getStatus() == 0) {
				Toast.makeText(Mylesson.this, "진행중입니다.", Toast.LENGTH_SHORT)
						.show();
			} else {
				Intent intent = new Intent(Mylesson.this, MylessonDetail.class);
				intent.putExtra("lesson_id", lessons.get(pos).getID());
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);
				finish();
			}
		}
	};

	private OnTouchListener lesson_requestTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				Intent intent = new Intent(Mylesson.this,
						LessonRequestPage1.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);

				break;

			}
			return true;
		}
	};
	private OnTouchListener homeImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				homeImageButton
						.setBackgroundResource(R.drawable.main_top_menua02);
				mylessonImageButton
						.setBackgroundResource(R.drawable.main_top_menub01);
				golfbagImageButton
						.setBackgroundResource(R.drawable.main_top_menuc01);
				settingImageButton
						.setBackgroundResource(R.drawable.main_top_menud01);
				Intent intent = new Intent(Mylesson.this, Home.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
				overridePendingTransition(0, 0);

				break;

			}
			return true;
		}
	};
	/*
	 * private OnTouchListener menu03TouchListener = new OnTouchListener() {
	 * public boolean onTouch(View v, MotionEvent event) {
	 * 
	 * switch (event.getAction()) { case MotionEvent.ACTION_DOWN:
	 * 
	 * homeImageButton .setBackgroundResource(R.drawable.main_top_menua01);
	 * mylessonImageButton .setBackgroundResource(R.drawable.main_top_menub01);
	 * golfbagImageButton .setBackgroundResource(R.drawable.main_top_menuc02);
	 * settingImageButton .setBackgroundResource(R.drawable.main_top_menud01);
	 * Intent intent = new Intent(Mylesson.this, Golfbag.class);
	 * intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	 * startActivity(intent); overridePendingTransition(R.anim.fade,
	 * R.anim.hold);
	 * 
	 * break;
	 * 
	 * } return true; } };
	 */
	private OnTouchListener settingImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				homeImageButton
						.setBackgroundResource(R.drawable.main_top_menua01);
				mylessonImageButton
						.setBackgroundResource(R.drawable.main_top_menub01);
				golfbagImageButton
						.setBackgroundResource(R.drawable.main_top_menuc01);
				settingImageButton
						.setBackgroundResource(R.drawable.main_top_menud02);
				Intent intent = new Intent(Mylesson.this, Setting.class);
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

		case KeyEvent.KEYCODE_BACK:/*
									 * if (Home.homeActivity != null) {
									 * Home.homeActivity.finish(); }
									 */
			if (Mylesson.mylessonActivity != null) {
				Mylesson.mylessonActivity.finish();
			}/*
			 * if (Setting.settingActivity != null) {
			 * Setting.settingActivity.finish(); }
			 */
			android.os.Process.killProcess(android.os.Process.myPid());

			break;
		}

		return super.onKeyDown(keyCode, event);

	}

	private class UserLessonTask extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... p) {
			getLesson();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			onCancelled();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

			DBConnector db = new DBConnector(Mylesson.this);
			lessons = db.getAllLesson();

			for (int i = 0; i < lessons.size(); i++) {
				String image_url;
				if (lessons.get(i).getStatus() == 1) {
					Mylesson_List_Data data;
					if (lessons.get(i).getTeacherID() == null
							|| lessons.get(i).getTeacherID() == 0) {
						image_url = "http://garagestory.cafe24.com/img/none.jpg";
						data = new Mylesson_List_Data(image_url, "신속 레슨 /",
								"￦ 9000", "", "", R.drawable.selected_checkbox);
					} else {
						Professional professional = db
								.getProfessionalByServerID(lessons.get(i)
										.getTeacherID());
						image_url = "http://garagestory.cafe24.com/img/teacher/"
								+ professional.getPhoto();
						data = new Mylesson_List_Data(image_url,
								professional.getName() + "/", "￦"
										+ String.valueOf(professional
												.getPrice()),
								professional.getCertification(), "추천 레슨 : "
										+ professional.getLesson(),
								R.drawable.selected_checkbox);
					}
					Array_Data.add(data);
				}

			}

			adapter = null;
			adapter = new Mylesson_Adapter(getBaseContext(),
					android.R.layout.simple_list_item_1, Array_Data);
			Custom_List = (ListView) findViewById(R.id.listView1);
			
			Custom_List.setAdapter(adapter);
			Custom_List.setOnItemClickListener(CustomListItemClickListener);
			
			progressDialog.dismiss();
		}

		void getLesson() {
			DBConnector dbConnector = new DBConnector(Mylesson.this);
			String url = Const.LESSON_GET_LIST_USER_URL;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			Log.d("loading_lesson_list_user", "loading_lesson_list_user");

			try {
				List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("user_id", String
						.valueOf(user_id)));
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				InputStream is = httpResponse.getEntity().getContent();

				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromStream(is);

				JSONArray lessons = json.getJSONArray("lessons");

				for (int i = 0; i < lessons.length(); i++) {
					JSONObject lesson = lessons.getJSONObject(i);

					Boolean exists = false;
					try {
						exists = dbConnector.checkExistQuery("SELECT * FROM "
								+ DBConnector.TABLE_LESSON
								+ " WHERE server_id=" + lesson.getInt("id"));
					} catch (Exception e) {
					}

					if (!exists) {
						int server_id = lesson.getInt("id");
						int user_id = lesson.getInt("user_id");
						Integer teacher_id;
						try {
							teacher_id = lesson.getInt("teacher_id");
						} catch (Exception e) {
							teacher_id = null;
						}
						Boolean temp = lesson.getBoolean("lesson_type");
						int lesson_type = (temp ? 1 : 0);
						String video = lesson.getString("video");
						int club_type = lesson.getInt("club_type");
						temp = lesson.getBoolean("status");
						int status = (temp ? 1 : 0);
						String question = URLDecoder.decode(
								lesson.getString("question"), "UTF-8");
						String created_datetime = lesson
								.getString("created_datetime");

						String user_name = URLDecoder.decode(
								lesson.getString("user_name"), "UTF-8");
						Lesson lesson_db = new Lesson(server_id, user_id,
								teacher_id, lesson_type, video, club_type,
								question, created_datetime, status, user_name);
						dbConnector.addLesson(lesson_db);
						Log.d("loading_lesson_list", "add " + question);
					}

				}
			} catch (Exception e) {
				Log.d("disp", "err : " + e.getMessage());
			}
			dbConnector.close();
		}
	}

}