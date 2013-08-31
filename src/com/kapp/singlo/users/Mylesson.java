package com.kapp.singlo.users;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.kapp.sginlo.meta.SingloUserActivity;
import com.kapp.singlo.R;
import com.kapp.singlo.bg.APIConnector;
import com.kapp.singlo.bg.APIConnector.getAPIConnetorResultListener;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

@SuppressLint("NewApi")
public class Mylesson extends SingloUserActivity {
	public static Activity mylessonActivity;

	private Button completeLessonButton;
	private Button waitingLessonButton;

	ListView Custom_List;

	// 핵심 contents 들을 ListView로 뿌리기 위한 변수 선언.
	private Mylesson_Adapter adapter;

	private ProgressDialog progressDialog;
	// background process loading

	private List<Lesson> lessons;
	private List<Lesson> showingLessons;

	private SharedPreferences spLogin;

	private int user_id;

	private UserLessonTask userLessonTask;
	
	/*private getAPIConnetorResultListener mCaptureListener = new getAPIConnetorResultListener() {
		
		@Override
		public void result(JSONObject object) {
			// TODO Auto-generated method stub
			
			System.out.println("aaa = " + object.length());
			
		}
	};*/

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylesson);

		setTopMenu(1);

		spLogin = getSharedPreferences("login", Login.MODE_PRIVATE);
		user_id = spLogin.getInt("id", 0);

		mylessonActivity = Mylesson.this;

		showingLessons = new ArrayList<Lesson>();

		completeLessonButton = (Button) findViewById(R.id.CompleteLessonButton);
		completeLessonButton
				.setOnClickListener(completeLessonImageButtonOnClickListener);
		waitingLessonButton = (Button) findViewById(R.id.WaitingLessonButton);
		waitingLessonButton
				.setOnClickListener(waitingLessonImageButtonOnClickListener);

		completeLessonButton.setBackgroundResource(R.drawable.shorttabon_btn);
		completeLessonButton.setTextColor(Color.parseColor("#FF34A93A"));
	}

	OnClickListener completeLessonImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			completeLessonButton
					.setBackgroundResource(R.drawable.shorttabon_btn);
			completeLessonButton.setTextColor(Color.parseColor("#FF34A93A"));
			waitingLessonButton
					.setBackgroundResource(R.drawable.shorttaboff_btn);
			waitingLessonButton.setTextColor(Color.parseColor("#FF000000"));

			DBConnector db = new DBConnector(Mylesson.this);
			showingLessons.clear();

			for (int i = 0; i < lessons.size(); i++) {
				if (lessons.get(i).getStatus() == 1) {
					showingLessons.add(lessons.get(i));
				}

			}
			db.close();

			adapter = null;
			adapter = new Mylesson_Adapter(getBaseContext(),
					android.R.layout.simple_list_item_1, showingLessons);
			Custom_List.setAdapter(adapter);
		}
	};

	OnClickListener waitingLessonImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			completeLessonButton
					.setBackgroundResource(R.drawable.shorttaboff_btn);
			completeLessonButton.setTextColor(Color.parseColor("#FF000000"));
			waitingLessonButton
					.setBackgroundResource(R.drawable.shorttabon_btn);
			waitingLessonButton.setTextColor(Color.parseColor("#FF34A93A"));

			DBConnector db = new DBConnector(Mylesson.this);
			showingLessons.clear();

			for (int i = 0; i < lessons.size(); i++) {
				if (lessons.get(i).getStatus() == 0) {
					showingLessons.add(lessons.get(i));
				}
			}
			db.close();

			adapter = null;
			adapter = new Mylesson_Adapter(getBaseContext(),
					android.R.layout.simple_list_item_1, showingLessons);
			Custom_List.setAdapter(adapter);
		}
	};

	protected void onResume() {
		super.onResume();

		setTopImage(1);
		
		showingLessons.clear();
		progressDialog = ProgressDialog.show(Mylesson.this, "",
				"레슨을 가져오고 있습니다.", false, false);
		userLessonTask = new UserLessonTask();
		userLessonTask.execute();
	}

	// 각 리스트 클릭시
	private OnItemClickListener CustomListItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View v, int pos,
				long id) {

			if (showingLessons.get(pos).getStatus() == 0) {
				Toast.makeText(Mylesson.this, "진행중입니다.", Toast.LENGTH_SHORT)
						.show();
			} else {
				Intent intent = new Intent(Mylesson.this, MylessonDetail.class);
				intent.putExtra("lesson_id", showingLessons.get(pos).getID());
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);
				finish();
			}
		}
	};

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
			super.onPostExecute(result);

			DBConnector db = new DBConnector(Mylesson.this);
			lessons = db.getAllLesson();

			showingLessons.clear();

			for (int i = 0; i < lessons.size(); i++) {
				if (lessons.get(i).getStatus() == 1) {
					showingLessons.add(lessons.get(i));
				}
			}

			adapter = null;
			adapter = new Mylesson_Adapter(getBaseContext(),
					android.R.layout.simple_list_item_1, showingLessons);
			Custom_List = (ListView) findViewById(R.id.listView1);

			Custom_List.setAdapter(adapter);
			Custom_List.setOnItemClickListener(CustomListItemClickListener);

			progressDialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

			progressDialog.dismiss();
		}

		void getLesson() {
			DBConnector dbConnector = new DBConnector(Mylesson.this);
			String url = Const.LESSON_GET_LIST_USER_URL;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			dbConnector.removeLessonAll();
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
						
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("lesson_id", Integer.toString(server_id));
						params.put("current_position", "100000");
						
						getThumnailUrl(params);
						
						//new APIConnector(Const.LESSON_CAPTURE_GET_URL, mCaptureListener).execute(params);
						
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
						/*String thumnail = URLDecoder.decode(
								lesson.getString("thumnail"), "UTF-8");*/
						//임시
						String thumnail = "temp";
						
						Lesson lesson_db = new Lesson(server_id, user_id,
								teacher_id, lesson_type, video, club_type,
								question, created_datetime, status, user_name, thumnail);
						dbConnector.addLesson(lesson_db);
						Log.d("loading_lesson_list", "add " + question);
					}

				}
			} catch (Exception e) {
				Log.d("disp", "err : " + e.getMessage());
			}
			dbConnector.close();
		}
		
		String getThumnailUrl(HashMap<String, String> params){
			
			String url = Const.LESSON_CAPTURE_GET_URL;
			HttpClient httpClient = new DefaultHttpClient();
			System.out.println("url = " + url);
			HttpPost httpPost = new HttpPost(url);
			InputStream is;
			
			String result = null;
			
			try {				
				
				List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
				
				Iterator<String> iterator = params.keySet().iterator();
				while(iterator.hasNext()){
					String key = (String)iterator.next();					
					nameValuePairs.add(new BasicNameValuePair(URLEncoder.encode(key, "URF-8"), 
							URLEncoder.encode(params.get(key), "URF-8")));
					System.out.println("key = " + key);
					System.out.println("value = " + params.get(key));
				}
				
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse httpResponse = httpClient.execute(httpPost);
				is = httpResponse.getEntity().getContent();	
				
				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromStream(is);
				
				result = json.getString("result");
				System.out.println("result = " + result);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return result;
		}
		
	}

}