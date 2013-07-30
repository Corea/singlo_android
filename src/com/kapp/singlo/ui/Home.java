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

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

@SuppressLint("NewApi")
public class Home extends Activity {
	
	public static Activity homeActivity;
	
	public static ProgressDialog mProgress;

	private LoadingHomeAsyncTask loadingHomeAsyncTask;

	private ArrayList<Professional> professionals;

	private ImageButton homeImageButton;
	private ImageButton mylessonImageButton;
	private ImageButton golfbagImageButton;
	private ImageButton settingImageButton;
	private ImageButton allProfessionalImageButton;
	private ImageButton likeProfessionalImageButton;

	private ListView Custom_List;

	ImageButton lesson_request;

	// 핵심 contents 들을 ListView로 뿌리기 위한 변수 선언.
	private ArrayList<Professional> Array_Data;
	private Home_Adapter adapter;

	// background process loading

	private int user_id;
	private SharedPreferences spLogin;

	private ProgressDialog progressDialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		spLogin = getSharedPreferences("login", Login.MODE_PRIVATE);
		user_id = spLogin.getInt("id", 0);

		homeActivity = Home.this;
		Array_Data = new ArrayList<Professional>();

		homeImageButton = (ImageButton) findViewById(R.id.HomeImageButton);
		mylessonImageButton = (ImageButton) findViewById(R.id.MylessonImageButton);
		mylessonImageButton
				.setOnTouchListener(mylessonImageButtonTouchListener);
		golfbagImageButton = (ImageButton) findViewById(R.id.GolfbagImageButton);
		settingImageButton = (ImageButton) findViewById(R.id.SettingImageButton);
		settingImageButton.setOnTouchListener(settingImageButtonTouchListener);
		allProfessionalImageButton = (ImageButton) findViewById(R.id.AllProfessionalImageButton);
		allProfessionalImageButton
				.setOnClickListener(allProfessionalImageButtonClickListener);
		likeProfessionalImageButton = (ImageButton) findViewById(R.id.LikeProfessionalImageButton);
		likeProfessionalImageButton
				.setOnClickListener(likeProfessionalImageButtonClickListener);

		lesson_request = (ImageButton) findViewById(R.id.LessonRequestImageButton);
		lesson_request.setOnTouchListener(lesson_requestTouchListener);

		allProfessionalImageButton
				.setImageResource(R.drawable.prolist_servtop_buttona02);
		homeImageButton.setImageResource(R.drawable.main_top_menua02);
		mylessonImageButton.setImageResource(R.drawable.main_top_menub01);
		golfbagImageButton.setImageResource(R.drawable.main_top_menuc01);
		settingImageButton.setImageResource(R.drawable.main_top_menud01);

		progressDialog = ProgressDialog.show(Home.this, "", "준비중입니다.", true,
				false);
		loadingHomeAsyncTask = new LoadingHomeAsyncTask();
		loadingHomeAsyncTask.execute();

	}

	protected void onResume() {
		super.onResume();

		homeImageButton.setImageResource(R.drawable.main_top_menua02);
		mylessonImageButton.setImageResource(R.drawable.main_top_menub01);
		golfbagImageButton.setImageResource(R.drawable.main_top_menuc01);
		settingImageButton.setImageResource(R.drawable.main_top_menud01);
	}

	private void loading_list() {
		DBConnector dbConnector = new DBConnector(this);

		professionals = (ArrayList<Professional>) dbConnector
				.getAllProfessional();

		Array_Data.clear();

		boolean interested = false;
		if (adapter != null) {
			interested = adapter.getInterested();
		}

		if (!interested) {
			allProfessionalImageButton
					.setImageResource(R.drawable.prolist_servtop_buttona02);
			likeProfessionalImageButton
					.setImageResource(R.drawable.prolist_servtop_buttonb01);

			for (int i = 0; i < professionals.size(); i++) {
				Array_Data.add(professionals.get(i));
			}
		} else {
			allProfessionalImageButton
					.setImageResource(R.drawable.prolist_servtop_buttona01);
			likeProfessionalImageButton
					.setImageResource(R.drawable.prolist_servtop_buttonb02);

			for (int i = 0; i < professionals.size(); i++) {
				if (professionals.get(i).getLike() == 0) {
					continue;
				}
				Array_Data.add(professionals.get(i));
			}

		}
		dbConnector.close();
	}

	// 각 리스트 클릭시
	private OnItemClickListener CustomListItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View v, int pos,
				long id) {

			Intent intent = new Intent(Home.this, HomeDetail.class);
			intent.putExtra("teacher_id", adapter.getItem(pos).getServerId());
			startActivity(intent);
			overridePendingTransition(0, 0);
		}
	};

	private OnTouchListener lesson_requestTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Intent intent = new Intent(Home.this, LessonRequestPage1.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);

				break;

			}
			return true;
		}
	};
	private OnClickListener allProfessionalImageButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!adapter.getInterested()) {
				return;
			}
			adapter.setIntrested(false);
			loading_list();
			adapter.notifyDataSetChanged();
		}
	};
	private OnClickListener likeProfessionalImageButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (adapter.getInterested()) {
				return;
			}
			adapter.setIntrested(true);
			loading_list();
			adapter.notifyDataSetChanged();
		}
	};

	private OnTouchListener mylessonImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				Intent intent = new Intent(Home.this, Mylesson.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
				overridePendingTransition(0, 0);

				break;
			}
			return true;
		}
	};
	private OnTouchListener golfbagImageButtonTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				Intent intent = new Intent(Home.this, Golfbag.class);
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

				Intent intent = new Intent(Home.this, Setting.class);
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
			if (Home.homeActivity != null) {
				Home.homeActivity.finish();
			}
			android.os.Process.killProcess(android.os.Process.myPid());

		}

		return false;

	}

	public class LoadingHomeAsyncTask extends AsyncTask<Void, Void, Void> {

		// 이곳에 포함된 code는 AsyncTask가 execute 되자 마자 UI 스레드에서 실행됨.
		// 작업 시작을 UI에 표현하거나
		// background 작업을 위한 ProgressBar를 보여 주는 등의 코드를 작성.

		protected void onPreExecute() {
			super.onPreExecute();
		}

		// UI 스레드에서 AsynchTask객체.execute(...) 명령으로 실행되는 callback
		@Override
		protected Void doInBackground(Void... v) {
			GetproList();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			onCancelled();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

			loading_list();
			adapter = new Home_Adapter(getBaseContext(),
					android.R.layout.simple_list_item_1, Array_Data);
			adapter.setUserId(user_id);

			Custom_List = (ListView) findViewById(R.id.listView1);
			Custom_List.setAdapter(adapter);
			Custom_List.setOnItemClickListener(CustomListItemClickListener);

			progressDialog.dismiss();
		}

		private void GetproList() {
			DBConnector dbConnector = new DBConnector(Home.this);
			Log.d("loading_teacher_list", "loading_teacher_list");

			try {
				String url = Const.TEACHER_GET_LIST_URL;
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				InputStream is;

				List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("user_id", String
						.valueOf(user_id)));

				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				is = httpResponse.getEntity().getContent();

				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromStream(is);

				JSONArray teachers = json.getJSONArray("teachers");

				for (int i = 0; i < teachers.length(); i++) {
					JSONObject teacher = teachers.getJSONObject(i);

					boolean exists = false;
					try {
						exists = dbConnector.checkExistQuery("SELECT * FROM "
								+ DBConnector.TABLE_PROFESSIONAL
								+ " WHERE server_id=" + teacher.getInt("id"));
					} catch (Exception e) {
					}

					if (!exists) {
						int server_id = teacher.getInt("id");
						int price = teacher.getInt("price");
						String name = URLDecoder.decode(
								teacher.getString("name"), "UTF-8");
						String photo = URLDecoder.decode(
								teacher.getString("photo"), "UTF-8");
						String certification = URLDecoder.decode(
								teacher.getString("certification"), "UTF-8");
						String lessons = URLDecoder.decode(
								teacher.getString("lessons"), "UTF-8");
						boolean status = teacher.getBoolean("like");

						Professional professional = new Professional(server_id,
								name, certification, lessons, price, "", photo,
								"", (status ? 1 : 0));
						dbConnector.addProfessional(professional);
						Log.d("loading_teacher_list", "add " + name);
					}

				}
			} catch (Exception e) {
				Log.d("disp", "err : " + e.getMessage());
			}
			dbConnector.close();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// mHandler.removeCallbacks(mPendingLauncherRunnable);
	}

}
