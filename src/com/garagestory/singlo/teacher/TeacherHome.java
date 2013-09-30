package com.garagestory.singlo.teacher;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.garagestory.singlo.R;
import com.garagestory.singlo.action.LessonReviewAction;
import com.garagestory.singlo.action.LessonReviewAction.lessonReviewListener;
import com.garagestory.singlo.adapter.LessonReviewAdapter;
import com.garagestory.singlo.data.DBConnector;
import com.garagestory.singlo.data.LessonReviewData;
import com.garagestory.singlo.data.Professional;
import com.garagestory.singlo.meta.SingloTeacherActivity;
import com.garagestory.singlo.util.Const;
import com.garagestory.singlo.util.JSONParser;
import com.garagestory.singlo.util.Utility;

@SuppressLint("NewApi")
public class TeacherHome extends SingloTeacherActivity implements OnClickListener{

	private ProgressDialog progressDialog;

	private Professional professional;

	private TextView nameTextView;
	private TextView priceTextView;
	private TextView scoreTextView;
	private TextView classTextView;
	private TextView certificateTextView;
	private TextView absenceTextView;

	private int teacher_id;

	private WebView profileWebView;
	private Button recommendVideoButton;
	private TextView profileTextView;
	private RatingBar scoreRatingbar;

	private ChangeProfileTask changeProfileTask;
	
	private Button mCareerBtn;
	private Button mLessonReviewBtn;
	private ScrollView mProfileScroll;
	private ListView mLessonReviewList;
	
	private LessonReviewAdapter mReviewAdapter;
	
	private lessonReviewListener mLessonReviewListener = new lessonReviewListener() {
		
		@Override
		public void result(ArrayList<LessonReviewData> list) {
			// TODO Auto-generated method stub
			setLessonReviewList(list);
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_home);

		setTopMenu(0);

		SharedPreferences spLogin = getSharedPreferences("login",
				TeacherHome.MODE_PRIVATE);
		teacher_id = spLogin.getInt("id", 0);

		DBConnector db = new DBConnector(this);
		professional = db.getProfessionalByServerID(teacher_id);
		db.close();

		nameTextView = (TextView) findViewById(R.id.NameTextView);
		priceTextView = (TextView) findViewById(R.id.PriceTextView);
		scoreTextView = (TextView) findViewById(R.id.ScoreTextView);
		classTextView = (TextView) findViewById(R.id.ClassTextView);
		certificateTextView = (TextView) findViewById(R.id.CertificationTextView);
		profileTextView = (TextView) findViewById(R.id.ProfileTextView);
		absenceTextView = (TextView) findViewById(R.id.AbsenceTextView);
		scoreRatingbar = (RatingBar) findViewById(R.id.ScoreRatingBar);
		mCareerBtn = (Button)findViewById(R.id.ProfileTabButton);
		mLessonReviewBtn = (Button)findViewById(R.id.CommentTabButton);
		
		mProfileScroll = (ScrollView)findViewById(R.id.profile_scroll_view);
		mLessonReviewList = (ListView)findViewById(R.id.lesson_review_list);
		
		mReviewAdapter = new LessonReviewAdapter(this);
		mLessonReviewList.setAdapter(mReviewAdapter);

		nameTextView.setText(professional.getName());
		priceTextView.setText("￦" + professional.getPrice());
		try {
			classTextView.setText(professional.getCertification().split("/")[1]
					.trim());
			certificateTextView.setText(professional.getCertification().split(
					"/")[0].trim());
		} catch (Exception e) {
			classTextView.setText(professional.getCertification().trim());
			certificateTextView.setText("");
		}
		profileTextView.setText(professional.getProfile());
		scoreTextView.setText(String.format("%.1f",
				professional.getEvaluationScore())
				+ "점 / " + professional.getEvaluationCount() + "명 ");
		scoreRatingbar.setRating((float) professional.getEvaluationScore());
		scoreRatingbar.setIsIndicator(true);

		if (professional.getStatus() == 0) {
			absenceTextView.setText(professional.getStatusMessage());
		} else {
			absenceTextView.setText("");
		}

		profileWebView = (WebView) findViewById(R.id.ProfileWebView);
		if (professional.getPhoto() == null) {
			profileWebView.loadDataWithBaseURL(null,
					Utility.getImageHtmlCode(Const.PROFILE_NONE_URL),
					"text/html", "utf-8", null);
		} else {
			profileWebView.loadDataWithBaseURL(
					null,
					Utility.getImageHtmlCode(Const.PROFILE_URL
							+ professional.getPhoto()), "text/html", "utf-8",
					null);
		}
		profileWebView.setOnTouchListener(profileWebViewOnTouchListener);

		recommendVideoButton = (Button) findViewById(R.id.RecommendVideoButton);
		recommendVideoButton
				.setOnClickListener(recommendVideoButtonOnClickListener);
	}
	
	private void setTabColor(Button btn){
		Button[] mBtnArray = {mCareerBtn, mLessonReviewBtn};
		
		for(int i = 0; i < mBtnArray.length; i++){			
			if(btn == mBtnArray[i]){				
				mBtnArray[i].setBackgroundResource(R.drawable.tabon_btn);	
				mBtnArray[i].setTextColor(Color.parseColor("#ff34a93a"));
				continue;
			}	
			mBtnArray[i].setBackgroundResource(R.drawable.taboff_btn);
			mBtnArray[i].setTextColor(Color.parseColor("#ff434343"));
		}
	}
	
	private void setTabContent(View view){
		View[] mContentView = {mProfileScroll, mLessonReviewList};
		
		for(int i = 0; i < mContentView.length; i++){
			if(view == mContentView[i]){
				mContentView[i].setVisibility(View.VISIBLE);
				continue;
			}
			mContentView[i].setVisibility(View.GONE);
		}
	}	
	
	private void setLessonReviewList(ArrayList<LessonReviewData> list){
		mReviewAdapter.clear();
		for(int i = 0; i < list.size(); i++){
			mReviewAdapter.add(list.get(i));			
		}
		mReviewAdapter.notifyDataSetChanged();
	}

	private OnClickListener recommendVideoButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (professional.getUrl().isEmpty()) {
				return;
			}
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(professional.getUrl()));
			startActivity(intent);
		}
	};

	private OnTouchListener profileWebViewOnTouchListener = new OnTouchListener() {

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

					String filename = Utility.getCacheFilename(
							TeacherHome.this, "profile_image.png", true);
					FileOutputStream fout = new FileOutputStream(filename);
					bitmap.compress(CompressFormat.PNG, 100, fout);
					fout.close();

					progressDialog = ProgressDialog.show(TeacherHome.this, "",
							"준비중입니다.", false, false);
					changeProfileTask = new ChangeProfileTask();
					changeProfileTask.execute(filename);
				} catch (Exception e) {

				}
			}
		}
	}

	protected void onResume() {
		super.onResume();

		setTopImage(0);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		return false;
	}

	public class ChangeProfileTask extends AsyncTask<String, Void, Void> {
		boolean changeSuccess;
		String professionalPhoto;
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
				Toast.makeText(TeacherHome.this, "프로필 사진이 변경되었습니다.",
						Toast.LENGTH_LONG).show();

				DBConnector dbConnector = new DBConnector(TeacherHome.this);
				professional.setPhoto(professionalPhoto);
				dbConnector.updateProfessional(professional);
				dbConnector.close();
				profileWebView.loadDataWithBaseURL(
						null,
						Utility.getImageHtmlCode(Const.PROFILE_URL
								+ professional.getPhoto()), "text/html",
						"utf-8", null);
			} else {
				Toast.makeText(TeacherHome.this, "프로필 사진 변경에 실패하였습니다.",
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
				dos.writeBytes("Content-Disposition:form-data; name=\"teacher_id\""
						+ Const.lineEnd
						+ Const.lineEnd
						+ professional.getServerId() + Const.lineEnd);

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
					professionalPhoto = json.getString("photo");
				}
				dos.close();
			} catch (Exception e) {

			}
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ProfileTabButton:
			setTabColor(mCareerBtn);
			setTabContent(mProfileScroll);
			break;
		case R.id.CommentTabButton:
			setTabColor(mLessonReviewBtn);
			setTabContent(mLessonReviewList);
			HashMap<String, String> mParam = new HashMap<String, String>();
			mParam.put("teacher_id", Integer.toString(professional.getServerId()));
			new LessonReviewAction(Const.GET_LESSON_REVIEW_EVALUATION, mLessonReviewListener).execute(mParam);
			break;

		default:
			break;
		}
	}
}