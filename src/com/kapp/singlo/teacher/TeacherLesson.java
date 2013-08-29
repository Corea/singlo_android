package com.kapp.singlo.teacher;

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
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kapp.sginlo.meta.SingloTeacherActivity;
import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

@SuppressLint("NewApi")
public class TeacherLesson extends SingloTeacherActivity {
	private ListView Custom_List;

	// 핵심 contents 들을 ListView로 뿌리기 위한 변수 선언.
	private ArrayList<Lesson> showingLessons;
	private TeacherLesson_Adapter adapter;

	private List<Lesson> lessons;

	private ProgressDialog progressDialog;
	private LessonTask lessonTask;

	private int teacher_id;
	private int lesson_type;

	private Button fastLessonButton;
	private Button slowLessonButton;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_lesson);

		setTopMenu(1);

		SharedPreferences spLogin = getSharedPreferences("login",
				TeacherLesson.MODE_PRIVATE);
		teacher_id = spLogin.getInt("id", 0);

		showingLessons = new ArrayList<Lesson>();
		showingLessons.clear();

		fastLessonButton = (Button) findViewById(R.id.FastLessonButton);
		fastLessonButton.setOnClickListener(fastLessonButtonOnClickListener);
		slowLessonButton = (Button) findViewById(R.id.SlowLessonButton);
		slowLessonButton.setOnClickListener(slowLessonButtonOnClickListener);

		progressDialog = ProgressDialog.show(TeacherLesson.this, "", "준비중입니다.",
				false, false);
		lessonTask = new LessonTask();
		lessonTask.execute();
	}

	protected void onResume() {
		super.onResume();

		setTopImage(1);
	}

	// 각 리스트 클릭시
	private OnItemClickListener CustomListItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View v, int pos,
				long id) {
			Log.d("click", String.valueOf(pos));

			for (int i = 0; i < lessons.size(); i++) {
				if (lessons.get(i).getLessonType() == lesson_type) {
					pos--;
					if (pos < 0) {
						if (lessons.get(i).getStatus() == 0) {
							Intent intent = new Intent(TeacherLesson.this,
									TeacherLessonAnswer1.class);
							intent.putExtra("lesson_id", lessons.get(i).getID());
							startActivity(intent);
							overridePendingTransition(0, 0);
							finish();
						} else {
							Toast.makeText(TeacherLesson.this, "완료된 레슨입니다.",
									Toast.LENGTH_SHORT).show();
						}
						return;
					}
				}
			}

		}
	};

	private OnClickListener fastLessonButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			lesson_type = 1;
			setLessonType();
		}
	};
	private OnClickListener slowLessonButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			lesson_type = 0;
			setLessonType();
		}
	};

	private void setLessonType() {
		if (lesson_type == 1) {
			fastLessonButton.setBackgroundResource(R.drawable.shorttabon_btn);
			fastLessonButton.setTextColor(Color.parseColor("#FF34A93A"));
			slowLessonButton.setBackgroundResource(R.drawable.shorttaboff_btn);
			slowLessonButton.setTextColor(Color.parseColor("#FF000000"));
		} else {
			fastLessonButton.setBackgroundResource(R.drawable.shorttaboff_btn);
			fastLessonButton.setTextColor(Color.parseColor("#FF000000"));
			slowLessonButton.setBackgroundResource(R.drawable.shorttabon_btn);
			slowLessonButton.setTextColor(Color.parseColor("#FF34A93A"));
		}

		showingLessons.clear();
		DBConnector db = new DBConnector(TeacherLesson.this);
		lessons = db.getAllLesson();
		db.close();

		for (int k = 0; k < lessons.size(); k++) {
			if (lessons.get(k).getLessonType() == lesson_type) {
				showingLessons.add(lessons.get(k));
				/*
				 * String image_url = Const.PROFILE_URL + "user_" +
				 * lessons.get(k).getUserID() + ".png";
				 * 
				 * if (lessons.get(k).getStatus() == 1) { data = new
				 * TeacherLesson_List_Data(image_url, "문의자 : ",
				 * lessons.get(k).getUserName(), "등록시간 : " +
				 * lessons.get(k).getCreatedDatetime(), "",
				 * R.drawable.completelesson_icon); } else { data = new
				 * TeacherLesson_List_Data(image_url, "문의자 : ",
				 * lessons.get(k).getUserName(), "등록시간 : " +
				 * lessons.get(k).getCreatedDatetime(), "",
				 * R.drawable.watinglesson_icon); }
				 */
			}
		}

		adapter = null;
		adapter = new TeacherLesson_Adapter(getBaseContext(),
				android.R.layout.simple_list_item_1, showingLessons);
		Custom_List.setAdapter(adapter);
	}

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
			Custom_List = (ListView) findViewById(R.id.listView5);
			Custom_List.setOnItemClickListener(CustomListItemClickListener);
			lesson_type = 0;
			setLessonType();

			progressDialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

			progressDialog.dismiss();
		}

		void getLesson() {
			DBConnector dbConnector = new DBConnector(TeacherLesson.this);
			String url = Const.LESSON_GET_LIST_URL;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			dbConnector.removeLessonAll();
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