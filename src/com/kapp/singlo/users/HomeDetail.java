package com.kapp.singlo.users;

import java.util.HashMap;

import org.json.JSONObject;

import com.kapp.sginlo.meta.SingloUserActivity;
import com.kapp.singlo.R;
import com.kapp.singlo.bg.APIGetAction;
import com.kapp.singlo.bg.APIGetAction.getAPIConnetorResultListener;
import com.kapp.singlo.bg.CallbackListener;
import com.kapp.singlo.bg.LikeTeacherAsyncTask;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Utility;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

public class HomeDetail extends SingloUserActivity implements CallbackListener, OnClickListener {

	private int user_id;
	private int teacher_id;

	private Professional professional;

	private TextView nameTextView;
	private TextView priceTextView;
	private TextView classTextView;
	private TextView certificateTextView;
	private TextView profileTextView;
	private TextView scoreTextView;
	private TextView absenceTextView;

	private WebView profileWebView;
	private Button recommendVideoButton;
	private ImageView favoriteImageView;
	private RatingBar scoreRatingbar;
	private RelativeLayout favoriteRelativeLayout;
	private Button mCareerBtn;
	private Button mLessonReviewBtn;
	
	private getAPIConnetorResultListener mLessonReviewListener = new getAPIConnetorResultListener() {
		
		@Override
		public void result(JSONObject object) {
			// TODO Auto-generated method stub
			
		}
	};
	

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
		absenceTextView = (TextView) findViewById(R.id.AbsenceTextView);
		scoreRatingbar = (RatingBar) findViewById(R.id.ScoreRatingBar);
		mCareerBtn = (Button)findViewById(R.id.ProfileTabButton);
		mLessonReviewBtn = (Button)findViewById(R.id.CommentTabButton);

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
		profileWebView.setOnTouchListener(profileWebViewOnTouchListener);

		recommendVideoButton = (Button) findViewById(R.id.RecommendVideoButton);
		recommendVideoButton
				.setOnClickListener(recommendVideoImageButtonOnClickListener);
		favoriteRelativeLayout = (RelativeLayout) findViewById(R.id.FavoriteRelativeLayout);
		favoriteRelativeLayout
				.setOnClickListener(favoriteRelativeLayoutOnClickListener);
		favoriteImageView = (ImageView) findViewById(R.id.FavoriteImageView);
		if (professional.getLike() == 1) {
			favoriteImageView.setImageResource(R.drawable.addfavorite_btn);
		} else {
			favoriteImageView.setImageResource(R.drawable.removefavorite_btn);
		}
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ProfileTabButton:
			setTabColor(mCareerBtn);
			break;
		case R.id.CommentTabButton:
			setTabColor(mLessonReviewBtn);
			HashMap<String, String> mParam = new HashMap<String, String>();
			mParam.put("teacher_id", Integer.toString(professional.getID()));
			new APIGetAction(Const.GET_LESSON_REVIEW_EVALUATION, mLessonReviewListener).execute(mParam);
			break;

		default:
			break;
		}
	}
}
