package com.kapp.singlo.users;

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
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.meta.SingloUserActivity;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;
import com.kapp.singlo.util.Utility;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class MylessonEvaluation extends SingloUserActivity {

	private int user_id;
	private SharedPreferences spLogin;

	private Lesson lesson;
	private Professional professional;

	private TextView nameTextView;
	private TextView priceTextView;
	private TextView classTextView;
	private TextView certificateTextView;
	private TextView scoreTextView;

	private RatingBar scoreRatingbar;
	private RatingBar speedRatingBar;
	private RatingBar accuracyRatingBar;
	private RatingBar priceRatingBar;

	private WebView profileWebView;
	private ImageButton recommendYesImageButton;
	private ImageButton recommendNoImageButton;
	private Button completeButton;

	private EditText reviewEditText;

	private Boolean recommend;

	private ProgressDialog progressDialog;
	private EvaluationTask evaluationTask;

	private RelativeLayout recommendYesRelativeLayout;
	private RelativeLayout recommendNoRelativeLayout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylesson_evaluation);

		setTopMenu(1);

		Intent intent = this.getIntent();
		int lesson_id = intent.getIntExtra("lesson_id", 0);
		if (lesson_id == 0) {
			finish();
		}

		spLogin = getSharedPreferences("login", Login.MODE_PRIVATE);
		user_id = spLogin.getInt("id", 0);

		recommend = null;

		DBConnector db = new DBConnector(this);
		lesson = db.getLesson(lesson_id);
		professional = db.getProfessionalByServerID(lesson.getTeacherID());
		db.close();
		if (lesson.getEvaluationStatus() == 1) {
			String msg = "이미 평가를 하셨습니다.";
			AlertDialog.Builder gsDialog = new AlertDialog.Builder(
					MylessonEvaluation.this);

			gsDialog.setTitle("평가 완료");
			gsDialog.setMessage(msg);
			gsDialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					}).create().show();
			finish();
		}

		nameTextView = (TextView) findViewById(R.id.NameTextView);
		priceTextView = (TextView) findViewById(R.id.PriceTextView);
		classTextView = (TextView) findViewById(R.id.ClassTextView);
		certificateTextView = (TextView) findViewById(R.id.CertificationTextView);
		scoreTextView = (TextView) findViewById(R.id.ScoreTextView);
		scoreRatingbar = (RatingBar) findViewById(R.id.ScoreRatingBar);

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
		scoreTextView.setText(String.format("%.1f",
				professional.getEvaluationScore())
				+ "점 / " + professional.getEvaluationCount() + "명 ");
		scoreRatingbar.setRating((float) professional.getEvaluationScore());
		scoreRatingbar.setIsIndicator(true);

		profileWebView = (WebView) findViewById(R.id.ProfileWebView);
		if (professional.getPhoto() == null
				|| professional.getPhoto().trim().isEmpty()) {
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

		speedRatingBar = (RatingBar) findViewById(R.id.SpeedRatingBar);
		accuracyRatingBar = (RatingBar) findViewById(R.id.AccuracyRatingBar);
		priceRatingBar = (RatingBar) findViewById(R.id.PriceRatingBar);

		recommendYesRelativeLayout = (RelativeLayout) findViewById(R.id.RecommendYesRelativeLayout);
		recommendYesRelativeLayout
				.setOnClickListener(recommendYesImageButtonOnClickListener);
		recommendNoRelativeLayout = (RelativeLayout) findViewById(R.id.RecommendNoRelativeLayout);
		recommendNoRelativeLayout
				.setOnClickListener(recommendNoImageButtonOnClickListener);

		recommendYesImageButton = (ImageButton) findViewById(R.id.RecommendYesImageButton);
		recommendYesImageButton
				.setOnClickListener(recommendYesImageButtonOnClickListener);
		recommendNoImageButton = (ImageButton) findViewById(R.id.RecommendNoImageButton);
		recommendNoImageButton
				.setOnClickListener(recommendNoImageButtonOnClickListener);
		completeButton = (Button) findViewById(R.id.CompleteButton);
		completeButton.setOnClickListener(completeImageButtonOnClickListener);

		reviewEditText = (EditText) findViewById(R.id.ReviewEditText);
		reviewEditText
				.setOnEditorActionListener(reviewEditTextOnEditorActionListener);
	}

	private OnEditorActionListener reviewEditTextOnEditorActionListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			InputMethodManager imm = (InputMethodManager) getSystemService(LessonRequestPage1.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(reviewEditText.getWindowToken(), 0);

			return true;
		}
	};

	OnClickListener recommendYesImageButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			recommend = true;
			recommendYesImageButton
					.setImageResource(R.drawable.checkboxgrayon_icon);
			recommendNoImageButton
					.setImageResource(R.drawable.checkboxoff_icon);
		}
	};
	OnClickListener recommendNoImageButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			recommend = false;
			recommendYesImageButton
					.setImageResource(R.drawable.checkboxoff_icon);
			recommendNoImageButton
					.setImageResource(R.drawable.checkboxgrayon_icon);
		}
	};

	OnClickListener completeImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (recommend == null) {
				Toast.makeText(MylessonEvaluation.this, "프로님의 추천여부를 선택해주세요.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (reviewEditText.getText().toString().trim() == "") {
				Toast.makeText(MylessonEvaluation.this, "남기실 말을 작성해주세요.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			progressDialog = ProgressDialog.show(MylessonEvaluation.this, "",
					"평가를 보내고 있습니다.", true, false);

			evaluationTask = new EvaluationTask();
			evaluationTask.execute();

		}
	};

	private class EvaluationTask extends AsyncTask<Void, Void, Void> {
		boolean result;

		protected void onPreExecute() {
			result = false;
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... p) {

			try {
				String url = Const.LESSON_EVALUATION_URL;
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				InputStream is;

				List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("user_id", String
						.valueOf(user_id)));
				nameValuePairs.add(new BasicNameValuePair("lesson_id", String
						.valueOf(lesson.getServerID())));
				nameValuePairs.add(new BasicNameValuePair("review", URLEncoder
						.encode(reviewEditText.getText().toString(), "UTF-8")));
				nameValuePairs.add(new BasicNameValuePair("speed", String
						.valueOf(speedRatingBar.getRating())));
				nameValuePairs.add(new BasicNameValuePair("accuracy", String
						.valueOf(accuracyRatingBar.getRating())));
				nameValuePairs.add(new BasicNameValuePair("price", String
						.valueOf(priceRatingBar.getRating())));
				nameValuePairs.add(new BasicNameValuePair("recommend", String
						.valueOf(recommend ? 1 : 0)));

				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				is = httpResponse.getEntity().getContent();

				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromStream(is);
				String resultString = json.getString("result");
				if (resultString.equals("success")) {
					result = true;
				}
			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void res) {
			super.onPostExecute(res);

			if (!result) {
				progressDialog.dismiss();
				Toast.makeText(MylessonEvaluation.this, "전송 중 문제가 발생했습니다.",
						Toast.LENGTH_SHORT).show();
			} else {
				DBConnector dbConnector = new DBConnector(
						MylessonEvaluation.this);
				lesson.setEvaluationStatus(1);
				dbConnector.updateLesson(lesson);
				dbConnector.close();

				progressDialog.dismiss();
				finish();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}

}
