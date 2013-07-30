package com.kapp.singlo.ui;

import java.io.InputStream;
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
import android.widget.Toast;

public class Register extends Activity {

	static Activity mainActivity;

	Boolean usingTermCheck;
	Boolean usingPersonalInfoCheck;

	ImageButton usingTermButton;
	ImageButton usingPersonalInfoButton;
	ImageButton backImageButton;
	Button startSingloButton;

	EditText phoneNumberEditText;
	EditText nameEditText;
	EditText birthdayEditText;

	RegisterTask registerTask;

	int id;
	String name, birthday, phone, photo;

	Boolean registerProcess;
	Boolean registerSuccess;

	SharedPreferences spLogin;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		phoneNumberEditText = (EditText) findViewById(R.id.PhoneNumberEditText);
		TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		phoneNumberEditText.setText(mgr.getLine1Number());

		nameEditText = (EditText) findViewById(R.id.NameEditText);
		birthdayEditText = (EditText) findViewById(R.id.BirthdayEditText);

		usingTermCheck = false;
		usingPersonalInfoCheck = false;

		backImageButton = (ImageButton) findViewById(R.id.BackImageButton);
		backImageButton.setOnClickListener(backClickListener);
		usingTermButton = (ImageButton) findViewById(R.id.UsingTermImageButton);
		usingTermButton.setOnClickListener(usingTermButtonClickListener);
		usingPersonalInfoButton = (ImageButton) findViewById(R.id.UsingPersonalInfoImageButton);
		usingPersonalInfoButton
				.setOnClickListener(usingPersonInfoButtonClickListener);

		startSingloButton = (Button) findViewById(R.id.StartSingloButton);
		startSingloButton.setOnClickListener(startSingloButtonClickListener);

		registerProcess = false;
		registerSuccess = false;

		spLogin = getSharedPreferences("login", Login.MODE_PRIVATE);

	}

	void saveLoginPreferences() {
		SharedPreferences.Editor editor = spLogin.edit();
		editor.putInt("id", id);
		editor.putString("name", name);
		editor.putString("birthday", birthday);
		editor.putString("phone", phone);
		editor.commit();
	}

	OnClickListener usingPersonInfoButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			usingPersonalInfoCheck = !usingPersonalInfoCheck;
			if (usingPersonalInfoCheck) {
				usingPersonalInfoButton
						.setImageResource(R.drawable.register_checkon);
			} else {
				usingPersonalInfoButton
						.setImageResource(R.drawable.register_checkoff);
			}

		}
	};

	OnClickListener usingTermButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			usingTermCheck = !usingTermCheck;
			if (usingTermCheck) {
				usingTermButton.setImageResource(R.drawable.register_checkon);
			} else {
				usingTermButton.setImageResource(R.drawable.register_checkoff);
			}
		}
	};

	OnClickListener startSingloButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!usingTermCheck) {
				Toast.makeText(Register.this, "이용양관에 동의해주시기 바랍니다.",
						Toast.LENGTH_SHORT).show();
			} else if (!usingPersonalInfoCheck) {
				Toast.makeText(Register.this, "개인정보활용에 동의해주시기 바랍니다.",
						Toast.LENGTH_SHORT).show();
			} else {

				if (registerProcess) {
					return;
				}
				registerProcess = true;

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

				registerTask = new RegisterTask();
				registerTask.execute();
			}
		}
	};

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

	public class RegisterTask extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... p) {
			register();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (registerSuccess) {
				Toast.makeText(Register.this, "회원 가입이 완료되었습니다.",
						Toast.LENGTH_LONG).show();
				saveLoginPreferences();

				if (Main.mainActivity != null) {
					Main.mainActivity.finish();
				}
				Intent intent = new Intent(Register.this, Home.class);
				startActivity(intent);
				finish();
			} else {
				String msg = "회원 가입에 실패하였습니다.";
				AlertDialog.Builder gsDialog = new AlertDialog.Builder(
						Register.this);

				gsDialog.setTitle("회원가입 실패");
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
			registerProcess = false;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			registerProcess = false;
		}

		void register() {
			String url = Const.REGISTER_URL;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			InputStream is;

			Log.d("Login", url);
			try {
				List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs
						.add(new BasicNameValuePair("birthday", birthday));
				nameValuePairs.add(new BasicNameValuePair("phone", phone));

				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				is = httpResponse.getEntity().getContent();

				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromStream(is);

				String result = json.getString("result");

				if (result.equals("success")) {
					registerSuccess = true;
					id = json.getInt("id");
				}
			} catch (Exception e) {
				Log.d("disp", "err : " + e.getMessage());
			}
		}
	}

}
