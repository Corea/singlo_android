package com.kapp.singlo.ui;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

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
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LessonRequestPage2Slow extends Activity {

	private TextView priceTextView;
	private TextView countTextView;
	private ImageButton paymentImageButton;
	private ImageButton allProfessionalImageButton;
	private ImageButton likeProfessionalImageButton;

	private ListView listView;

	private int price;
	private int id;
	private int sw_device;

	private Uri video;
	private String question;

	private Boolean submitProcess;
	private boolean status;

	private ArrayList<Professional> arrayData;
	private LessonRequestPageAdapter adapter;

	private SparseArray<Professional> selectedData;
	private ProgressDialog processDialog;
	private SubmitQuestion submitQuestion;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lesson_request_page2_slow);

		SharedPreferences spLogin = getSharedPreferences("login",
				Activity.MODE_PRIVATE);
		id = spLogin.getInt("id", 0);

		Intent intent = this.getIntent();
		sw_device = intent.getIntExtra("swing_device", 0);
		video = Uri.parse(intent.getStringExtra("video"));
		question = intent.getStringExtra("question");

		submitProcess = false;

		priceTextView = (TextView) findViewById(R.id.PriceTextView);
		countTextView = (TextView) findViewById(R.id.CountTextView);

		listView = (ListView) findViewById(R.id.listView1);

		DBConnector db = new DBConnector(this);
		arrayData = (ArrayList<Professional>) db.getAllProfessional();
		db.close();

		selectedData = new SparseArray<Professional>();

		adapter = new LessonRequestPageAdapter(getBaseContext(),
				android.R.layout.simple_list_item_1, arrayData);
		listView.setOnItemClickListener(listViewSetOnItemClickListener);
		listView.setAdapter(adapter);

		allProfessionalImageButton = (ImageButton) findViewById(R.id.AllProfessionalImageButton);
		allProfessionalImageButton
				.setOnClickListener(allProfessionalImageButtonClickListener);
		likeProfessionalImageButton = (ImageButton) findViewById(R.id.LikeProfessionalImageButton);
		likeProfessionalImageButton
				.setOnClickListener(likeProfessionalImageButtonClickListener);
		paymentImageButton = (ImageButton) findViewById(R.id.PaymentImageButton);
		paymentImageButton
				.setOnClickListener(paymentImageButtonOnClickListener);
		price = 0;
	}

	private void loading_list() {
		DBConnector dbConnector = new DBConnector(this);

		ArrayList<Professional> professionals = (ArrayList<Professional>) dbConnector
				.getAllProfessional();

		arrayData.clear();

		boolean interested = false;
		if (adapter != null) {
			interested = adapter.getInterested();
		}

		if (!interested) {
			allProfessionalImageButton
					.setImageResource(R.drawable.seeallon_btn);
			likeProfessionalImageButton
					.setImageResource(R.drawable.seefavoritoff_btn);

			for (int i = 0; i < professionals.size(); i++) {
				arrayData.add(professionals.get(i));
			}
		} else {
			allProfessionalImageButton
					.setImageResource(R.drawable.seealloff_btn);
			likeProfessionalImageButton
					.setImageResource(R.drawable.seefavoriton_btn);

			for (int i = 0; i < professionals.size(); i++) {
				if (professionals.get(i).getLike() == 0) {
					continue;
				}
				arrayData.add(professionals.get(i));
			}

		}
		dbConnector.close();
	}

	OnItemClickListener listViewSetOnItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View v, int index,
				long id) {

			Professional professional = adapter.getItem(index);
			if (selectedData.get(professional.getServerId()) == null) {
				selectedData.append(professional.getServerId(), professional);
				adapter.setCheck(index);
				price += professional.getPrice();
			} else {
				selectedData.remove(professional.getServerId());
				adapter.removeCheck(index);
				price -= professional.getPrice();
			}

			priceTextView.setText("￦" + price);
			countTextView.setText(selectedData.size() + "명");

			adapter.notifyDataSetChanged();
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
	OnClickListener paymentImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (submitProcess) {
				return;
			}
			if (selectedData.size() == 0) {
				Toast.makeText(LessonRequestPage2Slow.this,
						"적어도 한 명 이상의 프로를 선택해야합니다.", Toast.LENGTH_LONG).show();
				return;
			}
			submitProcess = true;
			status = true;

			processDialog = new ProgressDialog(LessonRequestPage2Slow.this);
			processDialog.setMessage("동영상을 업로드 하고 있습니다.");
			processDialog.setIndeterminate(false);
			processDialog.show();

			submitQuestion = new SubmitQuestion();
			submitQuestion.execute();

		}
	};

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
			submitProcess = false;
			if (status == false) {
				Toast.makeText(LessonRequestPage2Slow.this,
						"답변 전송 중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			Toast.makeText(LessonRequestPage2Slow.this, "레슨이 신청되었습니다.",
					Toast.LENGTH_SHORT).show();

			Intent intent = new Intent(LessonRequestPage2Slow.this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			finish();
		}

		private void submitQuestion() throws IOException {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(video, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String fileName = cursor.getString(column_index);

			FileInputStream fileInputStream = new FileInputStream(fileName);
			URL url = new URL(Const.LESSON_ASK_SLOW_URL);

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
					+ Const.lineEnd + Const.lineEnd + id + Const.lineEnd);

			for (int i = 0; i < selectedData.size(); i++) {
				Professional professional = selectedData.valueAt(i);
				dos.writeBytes(Const.twoHyphens + Const.boundary
						+ Const.lineEnd);
				dos.writeBytes("Content-Disposition:form-data; name=\"teacher_id[]\""
						+ Const.lineEnd
						+ Const.lineEnd
						+ professional.getServerId() + Const.lineEnd);
			}

			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.lineEnd);
			dos.writeBytes("Content-Disposition:form-data; name=\"club_type\""
					+ Const.lineEnd + Const.lineEnd + sw_device + Const.lineEnd);

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
			dos.writeBytes(Const.twoHyphens + Const.boundary + Const.twoHyphens
					+ Const.lineEnd);

			// close streams
			Log.e("File Up", "File is written");
			fileInputStream.close();
			dos.flush(); // 버퍼에 있는 값을 모두 밀어냄

			InputStream is = conn.getInputStream();

			try {
				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromStream(is);
				if (json.getString("result").equals("fail")) {
					status = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			dos.close();
		};

	}
}
