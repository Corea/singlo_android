package com.kapp.singlo.users;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONObject;

import com.kapp.singlo.R;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;
import com.kapp.singlo.util.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class Register extends Activity {

	private Boolean usingTermCheck;
	private Boolean usingPersonalInfoCheck;

	private ImageButton usingTermButton;
	private ImageButton usingPersonalInfoButton;
	private ImageButton backImageButton;
	private Button startSingloButton;

	private EditText phoneNumberEditText;
	private EditText nameEditText;
	private EditText birthdayEditText;
	private ImageView profileImageView;

	private RegisterTask registerTask;

	private int id;
	private String name, birthday, phone, photo;

	private Boolean registerProcess;
	private Boolean registerSuccess;

	private SharedPreferences spLogin;

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
		profileImageView = (ImageView) findViewById(R.id.ProfileImageView);
		profileImageView.setOnClickListener(profileImageViewOnClickListener);

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
		editor.putString("photo", photo);
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

	OnClickListener profileImageViewOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, 1);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				try {
					Uri uri = intent.getData();
					String[] projection = { MediaStore.Images.Media.DATA };
					Cursor cursor = getContentResolver().query(uri, projection,
							null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(projection[0]);
					String path = cursor.getString(columnIndex);
					cursor.close();

					Bitmap bitmap = Images.Media.getBitmap(
							getContentResolver(), uri);
					int height = bitmap.getHeight();
					int width = bitmap.getWidth();
					double ratio = Math.min(height / 150., width / 150.);

					height /= ratio;
					width /= ratio;
					Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
							width, height, true);

					ExifInterface exifMedia = new ExifInterface(path);
					int rotation = exifMedia.getAttributeInt(
							ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_NORMAL);
					int rotationInDegrees = 0;
					if (rotation == ExifInterface.ORIENTATION_ROTATE_90) {
						rotationInDegrees = 90;
					} else if (rotation == ExifInterface.ORIENTATION_ROTATE_180) {
						rotationInDegrees = 180;
					} else if (rotation == ExifInterface.ORIENTATION_ROTATE_270) {
						rotationInDegrees = 270;
					}

					Matrix matrix = new Matrix();
					if (rotation != 0) {
						matrix.setRotate(rotationInDegrees, width / 2,
								height / 2);
					}

					bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
							(int) width, (int) height, matrix, true);

					profileImageView.setImageBitmap(bitmap);

					String filename = Utility.getCacheFilename(Register.this,
							"profile_image.png", true);
					FileOutputStream fout = new FileOutputStream(filename);
					bitmap.compress(CompressFormat.PNG, 100, fout);
					fout.close();

					photo = filename;
				} catch (Exception e) {

				}
			}
		}
	}

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
			try {
				URL url = new URL(Const.REGISTER_URL);
				SharedPreferences prefs = getSharedPreferences("Singlo", MODE_PRIVATE);;
				String pushtoken = prefs.getString("pushtoken", "");
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + Const.boundary);

				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());

				dos.writeBytes(Const.twoHyphens + Const.boundary
						+ Const.lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"name\""
						+ Const.lineEnd + Const.lineEnd + name + Const.lineEnd);

				dos.writeBytes(Const.twoHyphens + Const.boundary
						+ Const.lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"birthday\""
						+ Const.lineEnd
						+ Const.lineEnd
						+ birthday
						+ Const.lineEnd);

				dos.writeBytes(Const.twoHyphens + Const.boundary
						+ Const.lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"phone\""
						+ Const.lineEnd + Const.lineEnd + phone + Const.lineEnd);
				
				dos.writeBytes(Const.twoHyphens + Const.boundary
						+ Const.lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"pushtoken\""
						+ Const.lineEnd + Const.lineEnd + pushtoken + Const.lineEnd);

				dos.writeBytes(Const.twoHyphens + Const.boundary
						+ Const.lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"profile\"; filename=\"profile_image.png\""
						+ Const.lineEnd + Const.lineEnd);

				FileInputStream fileInputStream = new FileInputStream(photo);

				int bytesAvailable = fileInputStream.available();
				int maxBufferSize = 16384;
				int bufferSize = Math.min(bytesAvailable, maxBufferSize);

				byte[] buffer = new byte[bufferSize];
				int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);

					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}
				fileInputStream.close();

				dos.writeBytes(Const.lineEnd);
				dos.writeBytes(Const.twoHyphens + Const.boundary
						+ Const.twoHyphens + Const.lineEnd);
				dos.flush();

				InputStream is = conn.getInputStream();

				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromStream(is);

				String result = json.getString("result");

				if (result.equals("success")) {
					registerSuccess = true;
					id = json.getInt("id");
					photo = json.getString("photo");
				}
				dos.close();
			} catch (Exception e) {

			}
		}
	}
}
