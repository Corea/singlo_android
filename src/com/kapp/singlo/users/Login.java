package com.kapp.singlo.users;

import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.teacher.TeacherHome;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class Login extends Activity {

	static Activity mainActivity;

	private EditText phoneNumberEditText;
	private EditText nameEditText;
	private EditText birthdayEditText;

	private ImageButton backImageButton;
	private Button startSingloButton;

	private Boolean loginProcess;
	private Boolean loginSuccess;
	private Boolean isProfessional;

	private LoginTask loginTask;

	private int id, count;
	private String name, birthday, phone, photo;

	private SharedPreferences spLogin;

	protected void onCreate(Bundle savedInstanceState) {
		spLogin = getSharedPreferences("login", Login.MODE_PRIVATE);

		id = spLogin.getInt("id", 0);

		if (id != 0) {
			name = spLogin.getString("name", "");
			isProfessional = spLogin.getBoolean("type", false);
			birthday = spLogin.getString("birthday", "");
			phone = spLogin.getString("phone", "");
			photo = spLogin.getString("photo", "");
			count = spLogin.getInt("count", 0);

			if (Main.mainActivity != null) {
				Main.mainActivity.finish();
			}
			Intent intent = new Intent(Login.this, Home.class);
			startActivity(intent);
			finish();
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		phoneNumberEditText = (EditText) findViewById(R.id.PhoneNumberEditText);
		TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		phoneNumberEditText.setText(mgr.getLine1Number());

		nameEditText = (EditText) findViewById(R.id.NameEditText);
		birthdayEditText = (EditText) findViewById(R.id.BirthdayEditText);

		backImageButton = (ImageButton) findViewById(R.id.BackImageButton);
		backImageButton.setOnClickListener(backClickListener);

		startSingloButton = (Button) findViewById(R.id.StartSingloButton);
		startSingloButton.setOnClickListener(startSingloButtonClickListener);

		loginProcess = false;
		loginSuccess = false;
		isProfessional = false;

	}

	OnClickListener backClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(nameEditText.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(birthdayEditText.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(phoneNumberEditText.getWindowToken(), 0);

			finish();
		}
	};

	OnClickListener startSingloButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (loginProcess) {
				return;
			}

			loginProcess = true;

			try {
				name = URLEncoder.encode(nameEditText.getText().toString(),
						"UTF-8");
				birthday = URLEncoder.encode(birthdayEditText.getText()
						.toString(), "UTF-8");
				phone = URLEncoder.encode(phoneNumberEditText.getText()
						.toString(), "UTF-8");
			} catch (Exception e) {
				name = nameEditText.getText().toString();
				birthday = nameEditText.getText().toString();
				phone = nameEditText.getText().toString();
			}

			loginTask = new LoginTask();
			loginTask.execute();
		}
	};

	void saveLoginPreferences() {
		SharedPreferences.Editor editor = spLogin.edit();
		editor.putInt("id", id);
		editor.putBoolean("type", isProfessional);
		editor.putString("name", name);
		editor.putString("birthday", birthday);
		editor.putString("phone", phone);
		editor.putString("photo", photo);
		editor.putInt("count", count);
		editor.commit();
	}

	public class LoginTask extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... p) {
			login();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (loginSuccess) {
				saveLoginPreferences();

				Intent intent;
				if (isProfessional) {
					intent = new Intent(Login.this, TeacherHome.class);
				} else {
					intent = new Intent(Login.this, Home.class);
				}

				if (Main.mainActivity != null) {
					Main.mainActivity.finish();
				}
				startActivity(intent);
				finish();
			} else {
				String msg = "아이이와 비밀번호가 일치하지 않습니다.";
				AlertDialog.Builder gsDialog = new AlertDialog.Builder(
						Login.this);

				gsDialog.setTitle("로그인 실패");
				gsDialog.setMessage(msg);
				gsDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								//
							}
						}).create().show();

				this.cancel(true);
			}
			loginProcess = false;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			loginProcess = false;
		}

		void login() {
			String url = Const.LOGIN_URL;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			InputStream is;
			SharedPreferences prefs = getSharedPreferences("Singlo", MODE_PRIVATE);;
			String pushtoken = prefs.getString("pushtoken", "");
			Log.d("SAJO", "gogo");
			Log.d("SAJO", "pushtoken : " + pushtoken);
			Log.d("SAJO", url);
			try {
				List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs.add(new BasicNameValuePair("birthday", birthday));
				nameValuePairs.add(new BasicNameValuePair("phone", phone));
				nameValuePairs.add(new BasicNameValuePair("pushtoken", pushtoken));

				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				is = httpResponse.getEntity().getContent();

				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromStream(is);

				name = URLDecoder.decode(name, "UTF-8");
				int now_id = json.getInt("id");
				String now_photo = URLDecoder.decode(json.getString("photo"),
						"UTF-8");

				if (json.getString("type").equals("teacher")) {
					isProfessional = true;

					Professional professional = new Professional(now_id, name,
							URLDecoder.decode(json.getString("certification"),
									"UTF-8"), json.getInt("price"),
							URLDecoder.decode(json.getString("profile"),
									"UTF-8"), URLDecoder.decode(
									json.getString("photo"), "UTF-8"),
							URLDecoder.decode(json.getString("url"), "UTF-8"),
							0, json.getBoolean("active") ? 1 : 0,
							json.getBoolean("status") ? 1 : 0,
							json.getString("status_message"),
							json.getInt("evaluation_count"),
							json.getDouble("evaluation_score"));
					DBConnector db = new DBConnector(Login.this);
					db.addProfessional(professional);
					db.close();
				} else {
					isProfessional = false;
				}
				count = json.getInt("count");

				photo = now_photo;
				id = now_id;
				loginSuccess = true;
			} catch (Exception e) {
				Log.d("disp", "err : " + e.getMessage());
			}
		}
	}

}
