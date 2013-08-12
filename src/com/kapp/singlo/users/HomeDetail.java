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
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class HomeDetail extends SingloUserActivity implements CallbackListener {

	private int user_id;
	private int teacher_id;

	private Professional professional;

	private TextView nameTextView;
	private TextView priceTextView;
	private TextView certificateTextView;
	private TextView profileTextView;
	private TextView scoreTextView;

	private WebView profileWebView;
	private TextView imageCountTextView;
	private ImageButton recommendVideoImageButton;
	private ImageButton favoriteImageButton;
	private RatingBar scoreRatingbar;

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

		nameTextView = (TextView) findViewById(R.id.TextView_01);
		priceTextView = (TextView) findViewById(R.id.TextView_02);
		certificateTextView = (TextView) findViewById(R.id.TextView_03);
		profileTextView = (TextView) findViewById(R.id.TextView_04);
		scoreTextView = (TextView) findViewById(R.id.TextView_00);
		scoreRatingbar = (RatingBar) findViewById(R.id.ScoreRatingBar);

		nameTextView.setText(professional.getName() + "/");
		priceTextView.setText("￦" + professional.getPrice());
		certificateTextView.setText(professional.getCertification());
		profileTextView.setText(professional.getProfile());
		scoreTextView.setText(professional.getEvaluationCount() + "명 "
				+ String.format("%.1f", professional.getEvaluationScore())
				+ "점");
		scoreRatingbar.setRating((float) professional.getEvaluationScore());

		profileWebView = (WebView) findViewById(R.id.ProfileWebView);
		imageCountTextView = (TextView) findViewById(R.id.ImageCountTextView);
		
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

		imageCountTextView.setText(professional.getProfile());
		recommendVideoImageButton = (ImageButton) findViewById(R.id.RecommendVideoImageButton);
		recommendVideoImageButton
				.setOnClickListener(recommendVideoImageButtonOnClickListener);
		favoriteImageButton = (ImageButton) findViewById(R.id.FavoriteImageButton);
		favoriteImageButton
				.setOnClickListener(favoriteImageButtonOnClickListener);
		if (professional.getLike() == 1) {
			favoriteImageButton.setImageResource(R.drawable.prolist_fav01);
		} else {
			favoriteImageButton.setImageResource(R.drawable.prolist_fav02);
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

	private OnClickListener favoriteImageButtonOnClickListener = new OnClickListener() {

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
			favoriteImageButton.setImageResource(R.drawable.prolist_fav01);
		} else {
			favoriteImageButton.setImageResource(R.drawable.prolist_fav02);
		}
	}
}
