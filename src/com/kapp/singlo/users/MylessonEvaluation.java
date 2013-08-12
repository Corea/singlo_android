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

import com.kapp.sginlo.meta.SingloUserActivity;
import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MylessonEvaluation extends SingloUserActivity {

	private int user_id;
	private SharedPreferences spLogin;

	private Lesson lesson;
	private Professional professional;

	private TextView nameAndCompanyTextView;
	private TextView certificationTextView;
	private TextView priceTextView;

	private RatingBar speedRatingBar;
	private RatingBar accuracyRatingBar;
	private RatingBar priceRatingBar;

	private ImageButton recommendYesImageButton;
	private ImageButton recommendNoImageButton;
	private ImageButton completeImageButton;

	private EditText reviewEditText;

	private Boolean recommend;

	private ProgressDialog progressDialog;
	private EvaluationTask evaluationTask;

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

		nameAndCompanyTextView = (TextView) findViewById(R.id.NameAndCompanyTextView);
		nameAndCompanyTextView.setText(professional.getName());
		certificationTextView = (TextView) findViewById(R.id.CertificationTextView);
		certificationTextView.setText(professional.getCertification());
		priceTextView = (TextView) findViewById(R.id.PriceTextView);
		priceTextView.setText("￦" + professional.getPrice());

		speedRatingBar = (RatingBar) findViewById(R.id.SpeedRatingBar);
		accuracyRatingBar = (RatingBar) findViewById(R.id.AccuracyRatingBar);
		priceRatingBar = (RatingBar) findViewById(R.id.PriceRatingBar);

		recommendYesImageButton = (ImageButton) findViewById(R.id.RecommendYesImageButton);
		recommendYesImageButton
				.setOnClickListener(recommendYesImageButtonOnClickListener);
		recommendNoImageButton = (ImageButton) findViewById(R.id.RecommendNoImageButton);
		recommendNoImageButton
				.setOnClickListener(recommendNoImageButtonOnClickListener);
		completeImageButton = (ImageButton) findViewById(R.id.CompleteImageButton);
		completeImageButton
				.setOnClickListener(completeImageButtonOnClickListener);

		reviewEditText = (EditText) findViewById(R.id.ReviewEditText);
	}

	OnClickListener recommendYesImageButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			recommend = true;
			recommendYesImageButton
					.setImageResource(R.drawable.recommendation_checkon);
			recommendNoImageButton
					.setImageResource(R.drawable.recommendation_checkoff);
		}
	};
	OnClickListener recommendNoImageButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			recommend = false;
			recommendYesImageButton
					.setImageResource(R.drawable.recommendation_checkoff);
			recommendNoImageButton
					.setImageResource(R.drawable.recommendation_checkon);
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

			progressDialog.dismiss();

			if (!result) {
				Toast.makeText(MylessonEvaluation.this, "전송 중 문제가 발생했습니다.",
						Toast.LENGTH_SHORT).show();

			} else {
				finish();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}

}
