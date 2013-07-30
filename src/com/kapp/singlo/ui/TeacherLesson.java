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
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

@SuppressLint("NewApi")
public class TeacherLesson extends Activity {
	ImageButton homeImageButton;
	ImageButton mylessonImageButton;
	ImageButton golfbagImageButton;
	ImageButton settingImageButton;

	ListView Custom_List;
	LinearLayout footer;

	// 핵심 contents 들을 ListView로 뿌리기 위한 변수 선언.
	private ArrayList<TeacherLesson_List_Data> Array_Data;
	private TeacherLesson_List_Data data;
	private TeacherLesson_Adapter adapter;

	// background process loading

	List<Lesson> lessons;

	ProgressDialog progressDialog;
	LessonTask lessonTask;

	int teacher_id;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_lesson);

		SharedPreferences spLogin = getSharedPreferences("login",
				TeacherLesson.MODE_PRIVATE);
		teacher_id = spLogin.getInt("id", 0);

		Array_Data = new ArrayList<TeacherLesson_List_Data>();
		Array_Data.clear();

		homeImageButton = (ImageButton) findViewById(R.id.HomeImageButton);
		homeImageButton.setOnTouchListener(homeImageButtonTouchListener);
		mylessonImageButton = (ImageButton) findViewById(R.id.MylessonImageButton);
		golfbagImageButton = (ImageButton) findViewById(R.id.GolfbagImageButton);
		settingImageButton = (ImageButton) findViewById(R.id.SettingImageButton);
		settingImageButton.setOnTouchListener(settingImageButtonTouchListener);

		homeImageButton.setImageResource(R.drawable.main_top_profile_off);
		mylessonImageButton.setImageResource(R.drawable.main_top_menub02);
		golfbagImageButton.setImageResource(R.drawable.main_top_menuc01);
		settingImageButton.setImageResource(R.drawable.main_top_menud01);


		progressDialog = ProgressDialog.show(TeacherLesson.this, "", "준비중입니다.",
				true, false);
		lessonTask = new LessonTask();
		lessonTask.execute();

	}

	private OnTouchListener homeImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				homeImageButton
						.setBackgroundResource(R.drawable.main_top_profile_on);
				mylessonImageButton
						.setBackgroundResource(R.drawable.main_top_menub01);
				Intent intent = new Intent(TeacherLesson.this,
						TeacherHome.class);
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

				mylessonImageButton
						.setBackgroundResource(R.drawable.main_top_menub01);
				settingImageButton
						.setBackgroundResource(R.drawable.main_top_menud02);
				Intent intent = new Intent(TeacherLesson.this,
						TeacherSetting.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
				overridePendingTransition(0, 0);

				break;

			}
			return true;
		}
	};

	public static String getImageHtmlCode(String _imageURL) {
		StringBuffer sb = new StringBuffer("<HTML>");
		sb.append("<HEAD>");
		sb.append("<style type='text/css'>");
		sb.append("body {");
		sb.append("margin-left: 0px;");
		sb.append("margin-top: 0px;");
		sb.append("margin-right: 0px;");
		sb.append("margin-bottom: 0px;");
		sb.append("}");
		sb.append("</style>");
		sb.append("</HEAD>");
		sb.append("<BODY>");
		sb.append("<img width=\"100%\" src=\"" + _imageURL + "\"/>");
		sb.append("</BODY>");
		sb.append("</HTML>");
		return sb.toString();
	}

	// 각 리스트 클릭시
	private OnItemClickListener CustomListItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View v, int pos,
				long id) {

			Log.d("click", String.valueOf(pos));
			// TeacherLesson_inside1.flag = pos;
			if (lessons.get(pos).getStatus() == 0) {
				Intent intent = new Intent(TeacherLesson.this,
						TeacherLessonAnswer1.class);
				intent.putExtra("lesson_id", lessons.get(pos).getID());
				startActivity(intent);
				overridePendingTransition(0, 0);
				finish();
			} else {
				Toast.makeText(TeacherLesson.this, "완료된 레슨입니다.",
						Toast.LENGTH_SHORT).show();
			}

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

	private class LessonTask extends AsyncTask<Void, Void, Void> {
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

			DBConnector db = new DBConnector(TeacherLesson.this);
			lessons = db.getAllLesson();
			db.close();

			for (int k = 0; k < lessons.size(); k++) {
				String image_url = ("http://garagestory.cafe24.com/img/user_profile/garage_user_"
						+ lessons.get(k).getUserID() + "_photo.jpg");

				if (lessons.get(k).getStatus() == 1) {
					data = new TeacherLesson_List_Data(image_url, "문의자 : ",
							lessons.get(k).getUserName(), "등록시간 : "
									+ lessons.get(k).getCreatedDatetime(), "",
							R.drawable.selected_checkbox);
				} else {
					data = new TeacherLesson_List_Data(image_url, "문의자 : ",
							lessons.get(k).getUserName(), "등록시간 : "
									+ lessons.get(k).getCreatedDatetime(), "",
							R.drawable.nonselected_checkbox);
				}
				Array_Data.add(data);
			}

			adapter = null;
			adapter = new TeacherLesson_Adapter(getBaseContext(),
					android.R.layout.simple_list_item_1, Array_Data);
			Custom_List = (ListView) findViewById(R.id.listView5);

			footer = (LinearLayout) View.inflate(getBaseContext(),
					R.layout.footer, null);
			Custom_List.setAdapter(adapter);
			Custom_List.setOnItemClickListener(CustomListItemClickListener);

			progressDialog.dismiss();
		}

		void getLesson() {
			DBConnector dbConnector = new DBConnector(TeacherLesson.this);
			String url = Const.LESSON_GET_LIST_URL;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			Log.d("loading_lesson_list", "loading_lesson_list");

			try {
				List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("teacher_id", String
						.valueOf(teacher_id)));
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