package com.kapp.singlo.users;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.kapp.singlo.R;
import com.kapp.singlo.billing.IabHelper;
import com.kapp.singlo.billing.IabResult;
import com.kapp.singlo.billing.Purchase;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class LessonRequestPage2Fast extends Activity {

	int sw_device, id;

	Uri video;
	String question;

	ImageButton paymentImageButton;

	SubmitQuestion submitQuestion;

	Boolean submitProcess;

	private ProgressDialog processDialog;

	private int REQUEST_CODE_PURCHASE = 1001;
	private String FAST_LESSON = "com.kapp.singlo.fast_lesson";
	private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;
	private IabHelper mHelper;
	private Activity mActivity;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lesson_request_page2_fast);
		
		
		mActivity = this;
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuzmpFd1j/O1CYxR6k5QKjozel0TtFhWTK6aeWB8aIp9R4W6SH//DO/9DutcGDAGs3IWBjPeYA+DLI7A3Qjx2j50K4IdYXWtKeFIQVMatuSUZxwzgB6Aj90trhmbkdPpAAlaMgqyW/ynV8lrRilV6tnzV43RNW/UcQvBL74sq+m10QMQEekOuK4i/eOw2qpwfyyz4mV5DOsMoec0d9pmHjmq+UGyRSCaV6g6Sx79PgiMfYspTtyTuYaoZDQuPQFnLvCbAV2cbcpTdZVmuAU6morsuQl+7XHYowjz+7Fg2P5jfZ6k3UmeTtEQD+8AE3YA56I7VBPIYQ3Y/1akpXymohQIDAQAB";
	   mHelper = new IabHelper(this, base64EncodedPublicKey);
		  
	   mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			@Override
			public void onIabSetupFinished(IabResult result) {			
				// TODO Auto-generated method stub
				 if (!result.isSuccess()) {
			         // Oh noes, there was a problem.
			         
			      }
				 readyForPurchase();
			         // Hooray, IAB is fully set up!  
			}
		});	   
		   
		SharedPreferences spLogin = getSharedPreferences("login", Activity.MODE_PRIVATE);
		id = spLogin.getInt("id", 0);

		Intent intent = this.getIntent();
		sw_device = intent.getIntExtra("swing_device", 0);
		video = Uri.parse(intent.getStringExtra("video"));
		question = intent.getStringExtra("question");

		submitProcess = false;

		paymentImageButton = (ImageButton) findViewById(R.id.PaymentImageButton);
		paymentImageButton.setOnClickListener(paymentImageButtonClickListener);

	}
	private void readyForPurchase(){
		mPurchaseFinishedListener  = new IabHelper.OnIabPurchaseFinishedListener() {
			@Override
			public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
				// TODO Auto-generated method stub
				if (result.isFailure()) {
					Log.d("SAJO", "Error purchasing: " + result);
				     return;
				}else{
					Log.d("SAJO", "item : " + purchase.getSku());
				    uploadVideo();
				}
			}
			
		};
	}
	private void requestPurchase(){
		mHelper.launchPurchaseFlow(mActivity, FAST_LESSON, REQUEST_CODE_PURCHASE, mPurchaseFinishedListener);
	}
	
	OnClickListener paymentImageButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			requestPurchase();

		}
	};
	private void uploadVideo(){
		if (submitProcess) {
			return;
		}
		submitProcess = true;
		
		processDialog = new ProgressDialog(LessonRequestPage2Fast.this);
		processDialog.setMessage("동영상을 업로드 하고 있습니다.");
		processDialog.setIndeterminate(false);
		processDialog.show();
		
		submitQuestion = new SubmitQuestion();
		submitQuestion.execute();
	}
	private class SubmitQuestion extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			try {
				submitQuestion();
				return "download completed";
			} catch (IOException e) {
				Log.d("debug", "The msg is : " + e.getMessage());
				return "download failed";
			}
		}

		protected void onPostExecute(String result) {
			processDialog.dismiss();
			Toast.makeText(LessonRequestPage2Fast.this, "레슨이 신청되었습니다.",
					Toast.LENGTH_SHORT).show();

			Intent intent = new Intent(LessonRequestPage2Fast.this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			finish();
		}

		private void submitQuestion() throws IOException {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(video, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String fileName = cursor.getString(column_index);

			FileInputStream fileInputStream = new FileInputStream(fileName);
			URL url = new URL(Const.LESSON_ASK_FAST_URL);

			Log.d("File Up", "mFileInputStream is " + fileInputStream);

			// open connection
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + Const.boundary);

			// write data
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			// uploadedfile 파일이 ashx 핸들러에서 파일을 찾을 때 사용함으로 이름이 반드시 동일해야함..
			// 이름을 바꾸면 ashx 파일에서도 바꿀것.

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"user_id\""
					+ Const.lineEnd + Const.lineEnd + String.valueOf(id) + Const.lineEnd);

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"club_type\""
					+ Const.lineEnd + Const.lineEnd + String.valueOf(sw_device) + Const.lineEnd);

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"question\""
					+ Const.lineEnd + Const.lineEnd + question + Const.lineEnd);

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"video\"; filename=\""
					+ fileName + "\"" + Const.lineEnd + Const.lineEnd);

			int bytesAvailable = fileInputStream.available();
			int maxBufferSize = 8192;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);

			byte[] buffer = new byte[bufferSize];
			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			Log.d("File Up", "image byte is " + bytesRead);

			// Read 파일
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);

				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			dos.writeBytes(Const.lineEnd);
			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.twoHyphens + Const.lineEnd);

			// close streams
			Log.e("File Up", "File is written");
			fileInputStream.close();
			dos.flush(); // 버퍼에 있는 값을 모두 밀어냄

			// 웹서버에서 결과를 받아 EditText 컨트롤에 보여줌
			int ch;
			InputStream is = conn.getInputStream();
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			String s = b.toString();
			Log.e("File Up", "result = " + s);
			dos.close();
		};
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		 super.onActivityResult(requestCode, resultCode, data);
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
	        // not handled, so handle it ourselves (here's where you'd
	        // perform any handling of activity results not related to in-app
	        // billing...
		}else{
	        if (requestCode == REQUEST_CODE_PURCHASE) {
	        	
		    }else{ //결제 실패
		      Log.d("SAJO", "purchase failed");
		    }
		}
		
	}
}
