package com.kapp.singlo.users;

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

import com.kapp.sginlo.meta.SingloUserActivity;
import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

@SuppressLint("NewApi")
public class Home extends SingloUserActivity {

	public static Activity homeActivity;

	public static ProgressDialog mProgress;

	private LoadingHomeAsyncTask loadingHomeAsyncTask;

	private ArrayList<Professional> professionals;

	private Button allProfessionalButton;
	private Button likeProfessionalButton;

	private ListView Custom_List;

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

		setTopMenu(0);

		spLogin = getSharedPreferences("login", Login.MODE_PRIVATE);
		user_id = spLogin.getInt("id", 0);

		homeActivity = Home.this;
		Array_Data = new ArrayList<Professional>();

		allProfessionalButton = (Button) findViewById(R.id.AllProfessionalButton);
		allProfessionalButton
				.setOnClickListener(allProfessionalButtonClickListener);
		likeProfessionalButton = (Button) findViewById(R.id.LikeProfessionalButton);
		likeProfessionalButton
				.setOnClickListener(likeProfessionalButtonClickListener);

		allProfessionalButton.setBackgroundResource(R.drawable.shorttabon_btn);
		allProfessionalButton.setTextColor(Color.parseColor("#FF34A93A"));

		progressDialog = ProgressDialog.show(Home.this, "", "준비중입니다.", true,
				false);
		loadingHomeAsyncTask = new LoadingHomeAsyncTask();
		loadingHomeAsyncTask.execute();
	}

	protected void onResume() {
		super.onResume();

		setTopImage(0);

		loading_list();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
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
			allProfessionalButton
					.setBackgroundResource(R.drawable.shorttabon_btn);
			allProfessionalButton.setTextColor(Color.parseColor("#FF34A93A"));
			likeProfessionalButton
					.setBackgroundResource(R.drawable.shorttaboff_btn);
			likeProfessionalButton.setTextColor(Color.parseColor("#FF000000"));

			for (int i = 0; i < professionals.size(); i++) {
				if (professionals.get(i).getActive() == 0) {
					continue;
				}
				Array_Data.add(professionals.get(i));
			}
		} else {
			allProfessionalButton
					.setBackgroundResource(R.drawable.shorttaboff_btn);
			allProfessionalButton.setTextColor(Color.parseColor("#FF000000"));
			likeProfessionalButton
					.setBackgroundResource(R.drawable.shorttabon_btn);
			likeProfessionalButton.setTextColor(Color.parseColor("#FF34A93A"));

			for (int i = 0; i < professionals.size(); i++) {
				if (professionals.get(i).getLike() == 0
						|| professionals.get(i).getActive() == 0) {
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

	private OnClickListener allProfessionalButtonClickListener = new OnClickListener() {

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
	private OnClickListener likeProfessionalButtonClickListener = new OnClickListener() {

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
						String profile = URLDecoder.decode(
								teacher.getString("profile"), "UTF-8");
						String url_info = URLDecoder.decode(
								teacher.getString("url"), "UTF-8");
						boolean like = teacher.getBoolean("like");
						boolean active = teacher.getBoolean("active");
						boolean status = teacher.getBoolean("status");
						String status_message = teacher
								.getString("status_message");
						int evaluation_count = teacher
								.getInt("evaluation_count");
						double evaluation_score = teacher
								.getDouble("evaluation_score");

						Professional professional = new Professional(server_id,
								name, certification, price, profile, photo,
								url_info, (like ? 1 : 0), (active ? 1 : 0),
								(status ? 1 : 0), status_message,
								evaluation_count, evaluation_score);
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
