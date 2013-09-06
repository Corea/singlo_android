package com.kapp.singlo.users;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.kapp.singlo.R;
import com.kapp.singlo.meta.SingloUserActivity;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;
import com.kapp.singlo.util.Utility;

@SuppressLint("NewApi")
public class Golfbag extends SingloUserActivity {

	private WebView profileWebView;
	private TextView nameTextView;

	private int user_id;
	private String name;
	private String photo;

	private ProgressDialog progressDialog;

	private ChangeProfileTask changeProfileTask;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.golfbag);

		setTopMenu(2);

		SharedPreferences spLogin = getSharedPreferences("login",
				Login.MODE_PRIVATE);
		user_id = spLogin.getInt("id", 0);
		photo = spLogin.getString("photo", "");
		name = spLogin.getString("name", "");

		profileWebView = (WebView) findViewById(R.id.ProfileWebView);
		profileWebView.loadDataWithBaseURL(null,
				Utility.getImageHtmlCode(Const.PROFILE_URL + photo),
				"text/html", "utf-8", null);
		profileWebView.setOnTouchListener(profileWebViewOnTouchListener);
		nameTextView = (TextView) findViewById(R.id.NameTextView);
		nameTextView.setText(name);
	}

	@Override
	protected void onResume() {
		super.onResume();

		setTopImage(2);
	}

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

	OnTouchListener profileWebViewOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, 1);
			}
			return false;
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

					String filename = Utility.getCacheFilename(Golfbag.this,
							"profile_image.png", true);
					FileOutputStream fout = new FileOutputStream(filename);
					bitmap.compress(CompressFormat.PNG, 100, fout);
					fout.close();

					progressDialog = ProgressDialog.show(Golfbag.this, "",
							"준비중입니다.", false, false);
					changeProfileTask = new ChangeProfileTask();
					changeProfileTask.execute(filename);
				} catch (Exception e) {

				}
			}
		}
	}

	public class ChangeProfileTask extends AsyncTask<String, Void, Void> {
		boolean changeSuccess;
		String photoPath;

		protected void onPreExecute() {
			super.onPreExecute();
			changeSuccess = false;
		}

		@Override
		protected Void doInBackground(String... p) {
			photoPath = p[0];
			changeProfile();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (changeSuccess) {
				Toast.makeText(Golfbag.this, "프로필 사진이 변경되었습니다.",
						Toast.LENGTH_LONG).show();

				SharedPreferences spLogin;
				spLogin = getSharedPreferences("login", Login.MODE_PRIVATE);

				SharedPreferences.Editor editor = spLogin.edit();
				editor.putString("photo", photo);
				editor.commit();

				profileWebView.loadDataWithBaseURL(null,
						Utility.getImageHtmlCode(Const.PROFILE_URL + photo),
						"text/html", "utf-8", null);
			} else {
				Toast.makeText(Golfbag.this, "프로필 사진 변경에 실패하였습니다.",
						Toast.LENGTH_LONG).show();
			}
			progressDialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			progressDialog.dismiss();
		}

		void changeProfile() {
			try {
				URL url = new URL(Const.CHANGE_PROFILE_URL);
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
				dos.writeBytes("Content-Disposition:form-data; name=\"user_id\""
						+ Const.lineEnd
						+ Const.lineEnd
						+ user_id
						+ Const.lineEnd);

				dos.writeBytes(Const.twoHyphens + Const.boundary
						+ Const.lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"profile\"; filename=\"profile_image.png\""
						+ Const.lineEnd + Const.lineEnd);

				FileInputStream fileInputStream = new FileInputStream(photoPath);

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
					changeSuccess = true;
					photo = json.getString("photo");
				}
				dos.close();
			} catch (Exception e) {

			}
		}
	}
}
