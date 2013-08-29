package com.kapp.singlo.teacher;

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

import com.kapp.sginlo.meta.SingloTeacherActivity;
import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class TeacherSettingDeactive extends SingloTeacherActivity {

	private EditText absenceEditText;
	private ImageButton saveImageButton;

	private ProgressDialog progressDialog;
	private AbsenceAsyncTask absenceAsyncTask;

	private String absence;
	private boolean progress;

	private int teacher_id;
	private Professional professional;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_setting_deactive);

		setTopMenu(3);

		saveImageButton = (ImageButton) findViewById(R.id.SaveImageButton);
		saveImageButton.setOnClickListener(saveImageButtonOnClickListener);
		absenceEditText = (EditText) findViewById(R.id.AbsenceEditText);

		progress = false;

		SharedPreferences spLogin = getSharedPreferences("login",
				TeacherHome.MODE_PRIVATE);
		teacher_id = spLogin.getInt("id", 0);

		DBConnector db = new DBConnector(this);
		professional = db.getProfessionalByServerID(teacher_id);
		db.close();
	}

	private OnClickListener saveImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (progress) {
				return;
			}
			progress = true;
			absence = absenceEditText.getText().toString();
			try {
				absence = URLEncoder.encode(absence, "UTF-8");
			} catch (Exception e) {
			}
			progressDialog = ProgressDialog.show(TeacherSettingDeactive.this,
					"", "부재중 메시지를 저장하고 있습니다.", false, false);
			absenceAsyncTask = new AbsenceAsyncTask();
			absenceAsyncTask.execute();
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(TeacherSettingDeactive.this,
					TeacherSetting.class);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
		}

		return false;
	}

	public class AbsenceAsyncTask extends AsyncTask<Void, Void, Void> {
		private boolean success = false;

		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... p) {
			absence();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			if (success) {
				Toast.makeText(TeacherSettingDeactive.this,
						"부재중 메시지가 저장되었습니다.", Toast.LENGTH_SHORT).show();

				try {
					professional.setStatusMessage(URLDecoder.decode(absence,
							"UTF-8"));
				} catch (Exception e) {
					professional.setStatusMessage(absence);

				}
				DBConnector dbConnector = new DBConnector(
						TeacherSettingDeactive.this);
				dbConnector.updateProfessional(professional);
				dbConnector.close();

				Intent intent = new Intent(TeacherSettingDeactive.this,
						TeacherSetting.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
			} else {
				String msg = "부재중 메시지를 저장하는 중 문제가 발생하였습니다.";
				AlertDialog.Builder gsDialog = new AlertDialog.Builder(
						TeacherSettingDeactive.this);

				gsDialog.setTitle("부재중 메시지 저장 실패");
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
			progress = false;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			progress = false;
		}

		void absence() {
			String url = Const.TEACHER_UPDATE_ABSENCE_URL;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			InputStream is;

			Log.d("Absence", url);
			try {
				List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id", ""
						+ professional.getServerId()));
				nameValuePairs.add(new BasicNameValuePair("absence", absence));

				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				is = httpResponse.getEntity().getContent();

				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromStream(is);

				String result = json.getString("result");

				if (result.equals("success")) {
					success = true;
				}
			} catch (Exception e) {

			}
		}
	}
}
