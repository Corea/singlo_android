package com.kapp.singlo.users;

import com.kapp.sginlo.meta.SingloUserActivity;
import com.kapp.singlo.R;
import com.kapp.singlo.bg.CallbackListener;
import com.kapp.singlo.bg.LikeTeacherAsyncTask;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Utility;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeDetail extends SingloUserActivity implements CallbackListener {

	private int user_id;
	private int teacher_id;

	private Professional professional;

	private TextView nameTextView;
	private TextView priceTextView;
	private TextView classTextView;
	private TextView certificateTextView;
	private TextView profileTextView;
	private TextView scoreTextView;

	private WebView profileWebView;
	private Button recommendVideoButton;
	private ImageView favoriteImageView;
	private RatingBar scoreRatingbar;
	private RelativeLayout favoriteRelativeLayout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_detail);

		setTopMenu(0);

		SharedPreferences spLogin = getSharedPreferences("login",
				Login.MODE_PRIVATE);
		user_id = spLogin.getInt("id", 0);

		Intent intent = this.getIntent();
		teacher_id = intent.getIntExtra("teacher_id", 0);

		DBConnector db = new DBConnector(this);
		professional = db.getProfessionalByServerID(teacher_id);
		db.close();

		if (teacher_id == 0) {
			Toast.makeText(HomeDetail.this, "오류가 발생하였습니다.", Toast.LENGTH_SHORT)
					.show();
			intent = new Intent(HomeDetail.this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
			return;
		}

		nameTextView = (TextView) findViewById(R.id.NameTextView);
		priceTextView = (TextView) findViewById(R.id.PriceTextView);
		classTextView = (TextView) findViewById(R.id.ClassTextView);
		certificateTextView = (TextView) findViewById(R.id.CertificationTextView);
		profileTextView = (TextView) findViewById(R.id.ProfileTextView);
		scoreTextView = (TextView) findViewById(R.id.ScoreTextView);
		scoreRatingbar = (RatingBar) findViewById(R.id.ScoreRatingBar);

		nameTextView.setText(professional.getName());
		priceTextView.setText("￦" + professional.getPrice());
		classTextView.setText(professional.getCertification().split("/")[0]);
		certificateTextView.setText(professional.getCertification().split("/")[1]);
		profileTextView.setText(professional.getProfile());
		scoreTextView.setText(String.format("%.1f",
				professional.getEvaluationScore())
				+ "점 / " + professional.getEvaluationCount() + "명 ");
		scoreRatingbar.setRating((float) professional.getEvaluationScore());
		profileWebView = (WebView) findViewById(R.id.ProfileWebView);

		scoreRatingbar.setIsIndicator(true);

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

		recommendVideoButton = (Button) findViewById(R.id.RecommendVideoButton);
		recommendVideoButton
				.setOnClickListener(recommendVideoImageButtonOnClickListener);
		favoriteRelativeLayout = (RelativeLayout) findViewById(R.id.FavoriteRelativeLayout);
		profileWebView.setOnTouchListener(profileWebViewOnTouchListener);
		favoriteRelativeLayout
				.setOnClickListener(favoriteRelativeLayoutOnClickListener);
		favoriteImageView = (ImageView) findViewById(R.id.FavoriteImageView);

		if (professional.getLike() == 1) {
			favoriteImageView.setImageResource(R.drawable.addfavorite_btn);
		} else {
			favoriteImageView.setImageResource(R.drawable.removefavorite_btn);
		}
	}

	private OnClickListener recommendVideoImageButtonOnClickListener = new OnClickListener() {

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

	private LikeTeacherAsyncTask likeTeacherAsyncTask;

	private OnTouchListener profileWebViewOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				likeTeacherAsyncTask = new LikeTeacherAsyncTask();
				likeTeacherAsyncTask.setContext(HomeDetail.this);
				likeTeacherAsyncTask.setListener(HomeDetail.this);
				likeTeacherAsyncTask.execute(user_id,
						professional.getServerId(), professional.getLike());
			}
			return false;
		}
	};
	private OnClickListener favoriteRelativeLayoutOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			likeTeacherAsyncTask = new LikeTeacherAsyncTask();
			likeTeacherAsyncTask.setContext(HomeDetail.this);
			likeTeacherAsyncTask.setListener(HomeDetail.this);
			likeTeacherAsyncTask.execute(user_id, professional.getServerId(),
					professional.getLike());

		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(HomeDetail.this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);

		}

		return false;

	}

	@Override
	public void callback(Object... params) {
		Professional professional = (Professional) params[0];
		if (professional.getLike() == 1) {
			favoriteImageView.setImageResource(R.drawable.addfavorite_btn);
		} else {
			favoriteImageView.setImageResource(R.drawable.removefavorite_btn);
		}
	}
}
