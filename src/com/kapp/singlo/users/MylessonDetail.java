package com.kapp.singlo.users;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.data.LessonAnswer;
import com.kapp.singlo.data.LessonAnswerImage;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.meta.SingloUserActivity;
import com.kapp.singlo.ui.SingloVideoView;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.JSONParser;
import com.kapp.singlo.util.Utility;

@SuppressLint("NewApi")
public class MylessonDetail extends SingloUserActivity implements OnClickListener{
	private List<Integer> causeIDList;
	private List<Integer> causeStringList;
	private List<Integer> causeTitleStringList;
	private ArrayList<Integer> mRecommendList;
	
	private List<LessonAnswerImage> lessonAnswerImageList;

	private Button lessonTabButton;
	private Button mySwingTabButton;
	private Button evaluationButton;

	private WebView profileWebView;
	private ImageView causeImageView;
	private ImageView recommendImageView1;
	private ImageView recommendImageView2;
	private ImageView thumbnailImageView;

	private List<SeekBar> seekBarList;

	private ProgressDialog progressDialog;

	private TextView nameTextView;
	private TextView datetimeTextView;
	private TextView scoreTextView;
	private TextView causeTitleTextView;
	private TextView causeDetailTextView;
	private TextView question_text;

	private int lesson_id;

	private Professional professional;
	private Lesson lesson;
	private LessonAnswer lessonAnswer;
	private LessonDetailTask lessonDetailTask;

	private int active_tab;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylesson_detail);

		setTopMenu(1);

		Intent intent = this.getIntent();
		lesson_id = intent.getIntExtra("lesson_id", 0);

		DBConnector db = new DBConnector(this);
		lesson = db.getLesson(lesson_id);
		professional = db.getProfessionalByServerID(lesson.getTeacherID());
		db.close();

		Button target = (Button) findViewById(R.id.ClubTypeButton);
		target.setText(Utility.getClubName(lesson.getClubType()));

		causeIDList = new ArrayList<Integer>();
		causeIDList.add(R.drawable.cause_1);
		causeIDList.add(R.drawable.cause_2);
		causeIDList.add(R.drawable.cause_3);
		causeIDList.add(R.drawable.cause_4);
		causeIDList.add(R.drawable.cause_5);
		causeIDList.add(R.drawable.cause_6);
		causeIDList.add(R.drawable.cause_7);
		causeIDList.add(R.drawable.cause_8);
		causeIDList.add(R.drawable.cause_9);
		causeIDList.add(R.drawable.cause_10);
		causeIDList.add(R.drawable.cause_11);
		causeIDList.add(R.drawable.cause_12);

		causeStringList = new ArrayList<Integer>();
		causeStringList.add(R.string.cause_1);
		causeStringList.add(R.string.cause_2);
		causeStringList.add(R.string.cause_3);
		causeStringList.add(R.string.cause_4);
		causeStringList.add(R.string.cause_5);
		causeStringList.add(R.string.cause_6);
		causeStringList.add(R.string.cause_7);
		causeStringList.add(R.string.cause_8);
		causeStringList.add(R.string.cause_9);
		causeStringList.add(R.string.cause_10);
		causeStringList.add(R.string.cause_11);
		causeStringList.add(R.string.cause_12);

		causeTitleStringList = new ArrayList<Integer>();
		causeTitleStringList.add(R.string.causeTitle_1);
		causeTitleStringList.add(R.string.causeTitle_2);
		causeTitleStringList.add(R.string.causeTitle_3);
		causeTitleStringList.add(R.string.causeTitle_4);
		causeTitleStringList.add(R.string.causeTitle_5);
		causeTitleStringList.add(R.string.causeTitle_6);
		causeTitleStringList.add(R.string.causeTitle_7);
		causeTitleStringList.add(R.string.causeTitle_8);
		causeTitleStringList.add(R.string.causeTitle_9);
		causeTitleStringList.add(R.string.causeTitle_10);
		causeTitleStringList.add(R.string.causeTitle_11);
		causeTitleStringList.add(R.string.causeTitle_12);

		

		lessonTabButton = (Button) findViewById(R.id.LessonTabButton);
		lessonTabButton.setOnClickListener(lessonTabImageButtonListener);
		mySwingTabButton = (Button) findViewById(R.id.MySwingTabButton);
		mySwingTabButton.setOnClickListener(mySwingTabImageButtonListener);
		evaluationButton = (Button) findViewById(R.id.EvaluationButton);
		evaluationButton
				.setOnClickListener(evaluationImageButtonOnClickListener);

		nameTextView = (TextView) findViewById(R.id.NameTextView);
		datetimeTextView = (TextView) findViewById(R.id.DatetimeTextView);
		causeTitleTextView = (TextView) findViewById(R.id.CauseTitleTextView);
		causeDetailTextView = (TextView) findViewById(R.id.CauseDetailTextView);
		question_text = (TextView) findViewById(R.id.QuestionTextView);
		try {
			question_text.setText("질문 내용 : "
					+ URLDecoder.decode(lesson.getQuestion(), "UTF-8"));
		} catch (Exception e) {

		}

		nameTextView.setText(professional.getName());
		datetimeTextView.setText(lesson.getCreatedDatetime());

		seekBarList = new ArrayList<SeekBar>();
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar1));
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar2));
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar3));
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar4));
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar5));
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar6));
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar7));
		seekBarList.add((SeekBar) findViewById(R.id.SeekBar8));
		for (int i = 0; i < 8; i++) {
			seekBarList.get(i).setMax(10);
			seekBarList.get(i).setEnabled(false);
		}
		scoreTextView = (TextView) findViewById(R.id.ScoreTextView);

		active_tab = 0;

		thumbnailImageView = (ImageView) findViewById(R.id.ThumbnailImageView);
		thumbnailImageView
				.setOnClickListener(thumbnailImageViewOnClickListener);
		causeImageView = (ImageView) findViewById(R.id.CauseImageView);
		/*recommendImageView1 = (ImageView) findViewById(R.id.RecommendImageView1);
		recommendImageView2 = (ImageView) findViewById(R.id.RecommendImageView2);*/
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

		progressDialog = ProgressDialog.show(MylessonDetail.this, "",
				"준비중입니다.", true, false);
		lessonDetailTask = new LessonDetailTask();
		lessonDetailTask.execute();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:
			// Toast.makeText(this, "Did's you want cancel this app?",
			// Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(MylessonDetail.this, Mylesson.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			finish();

			break;
		}

		return super.onKeyDown(keyCode, event);

	}

	/*
	 * private OnTouchListener playButtonTouchListener = new OnTouchListener() {
	 * 
	 * @Override public boolean onTouch(View v, MotionEvent event) { switch
	 * (event.getAction()) { case MotionEvent.ACTION_DOWN:
	 * 
	 * Intent intent = new Intent(MylessonDetail.this, SingloVideoView.class);
	 * intent.putExtra("url", lessonAnswer.getVideo()); startActivity(intent);
	 * 
	 * break;
	 * 
	 * } return false; } };
	 */

	private OnClickListener lessonTabImageButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			active_tab = 0;
			lessonTabButton.setBackgroundResource(R.drawable.tabon_btn);
			lessonTabButton.setTextColor(Color.parseColor("#FF34A93A"));
			mySwingTabButton.setBackgroundResource(R.drawable.taboff_btn);
			mySwingTabButton.setTextColor(Color.parseColor("#FF000000"));
		}
	};
	private OnClickListener mySwingTabImageButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			active_tab = 2;
			lessonTabButton.setBackgroundResource(R.drawable.taboff_btn);
			lessonTabButton.setTextColor(Color.parseColor("#FF000000"));
			mySwingTabButton.setBackgroundResource(R.drawable.tabon_btn);
			mySwingTabButton.setTextColor(Color.parseColor("#FF34A93A"));
		}
	};
	private OnClickListener thumbnailImageViewOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (active_tab == 0) {

				Intent intent = new Intent(MylessonDetail.this,
						MylessonDetailAudio.class);
				intent.putExtra("lesson_id", lesson_id);
				startActivity(intent);
			} else if (active_tab == 2) {

				Intent intent = new Intent(MylessonDetail.this,
						SingloVideoView.class);
				intent.putExtra("url", lesson.getVideo());
				startActivity(intent);
			}

		}
	};

	private OnClickListener evaluationImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MylessonDetail.this,
					MylessonEvaluation.class);
			intent.putExtra("lesson_id", lesson_id);
			startActivity(intent);

		}
	};

	private static final int MAX_SIZE = 1024;

	private Drawable createLargeDrawable(int resId) throws IOException {

		InputStream is = getResources().openRawResource(resId);
		BitmapRegionDecoder brd = BitmapRegionDecoder.newInstance(is, true);

		try {
			Point point = new Point();
			((WindowManager) getSystemService(WINDOW_SERVICE))
					.getDefaultDisplay().getSize(point);
			double ratio = point.x / (double) brd.getWidth();
			if (brd.getWidth() <= MAX_SIZE && brd.getHeight() <= MAX_SIZE) {
				Bitmap resize = Bitmap.createScaledBitmap(
						BitmapFactory.decodeResource(getResources(), resId),
						(int) Math.floor(brd.getWidth() * ratio),
						(int) Math.floor(brd.getHeight() * ratio), true);
				return new BitmapDrawable(getResources(), resize);
			}

			int rowCount = (int) Math.ceil((float) brd.getHeight()
					/ (float) MAX_SIZE);
			int colCount = (int) Math.ceil((float) brd.getWidth()
					/ (float) MAX_SIZE);

			BitmapDrawable[] drawables = new BitmapDrawable[rowCount * colCount];

			for (int i = 0; i < rowCount; i++) {

				int top = MAX_SIZE * i;
				int bottom = i == rowCount - 1 ? brd.getHeight() : top
						+ MAX_SIZE;

				for (int j = 0; j < colCount; j++) {

					int left = MAX_SIZE * j;
					int right = j == colCount - 1 ? brd.getWidth() : left
							+ MAX_SIZE;

					int dstWidth = (int) Math.floor((right - left) * ratio);
					int dstHeight = (int) Math.floor((bottom - top) * ratio);

					Bitmap b = brd.decodeRegion(new Rect(left, top, right,
							bottom), null);
					Bitmap resize = Bitmap.createScaledBitmap(b, dstWidth,
							dstHeight, true);
					BitmapDrawable bd = new BitmapDrawable(getResources(),
							resize);
					bd.setGravity(Gravity.TOP | Gravity.LEFT);
					drawables[i * colCount + j] = bd;
				}
			}

			LayerDrawable ld = new LayerDrawable(drawables);
			for (int i = 0; i < rowCount; i++) {
				for (int j = 0; j < colCount; j++) {

					ld.setLayerInset(i * colCount + j,
							(int) Math.floor(MAX_SIZE * j * ratio),
							(int) Math.floor(MAX_SIZE * i * ratio), 0, 0);
				}
			}

			return ld;
		} finally {
			brd.recycle();
		}
	}

	private class LessonDetailTask extends AsyncTask<Void, Void, Void> {

		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... p) {
			getLesson();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			onCancelled();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			DBConnector db = new DBConnector(MylessonDetail.this);
			lessonAnswer = db.getLessonAnswerByLesson(lesson);

			db.close();

			seekBarList.get(0).setProgress(lessonAnswer.getScore1());
			seekBarList.get(1).setProgress(lessonAnswer.getScore2());
			seekBarList.get(2).setProgress(lessonAnswer.getScore3());
			seekBarList.get(3).setProgress(lessonAnswer.getScore4());
			seekBarList.get(4).setProgress(lessonAnswer.getScore5());
			seekBarList.get(5).setProgress(lessonAnswer.getScore6());
			seekBarList.get(6).setProgress(lessonAnswer.getScore7());
			seekBarList.get(7).setProgress(lessonAnswer.getScore8());
			int sum = 0;
			for (int i = 0; i < 8; i++) {
				sum += seekBarList.get(i).getProgress();
			}
			if (sum == 80) {
				scoreTextView.setText("10");
			} else {
				scoreTextView
						.setText(sum / 8 + "." + (int) (sum * 10 / 8) % 10);
			}

			causeImageView.setImageResource(causeIDList.get(lessonAnswer
					.getCause()));
			causeDetailTextView.setText(causeStringList.get(lessonAnswer
					.getCause()));
			causeTitleTextView.setText(causeTitleStringList.get(lessonAnswer
					.getCause()));
			/*try {
				recommendImageView1
						.setImageDrawable(createLargeDrawable(recommendIDList
								.get(lessonAnswer.getRecommend1() - 1)));
				recommendImageView2
						.setImageDrawable(createLargeDrawable(recommendIDList
								.get(lessonAnswer.getRecommend2() - 1)));
			} catch (Exception e) {
				e.printStackTrace();
			}*/

			String path = Const.VIDEO_URL
					+ lessonAnswerImageList.get(0).getImage();
			File cacheDir = MylessonDetail.this.getCacheDir();
			cacheDir.mkdirs();
			File cacheFile = new File(cacheDir, "" + path.hashCode());
			Bitmap bitmap = BitmapFactory.decodeFile(cacheFile
					.getAbsolutePath());
			thumbnailImageView.setImageBitmap(bitmap);
			progressDialog.dismiss();
		}

		void getLesson() {
			DBConnector dbConnector = new DBConnector(MylessonDetail.this);
			String url = Const.LESSON_ANSWER_GET_URL;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			Log.d("get_lesson_answer", "get_lesson_answer");

			try {
				List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("lesson_id", String
						.valueOf(lesson.getServerID())));
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				InputStream is = httpResponse.getEntity().getContent();

				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromStream(is);

				Boolean exists = false;
				try {
					exists = dbConnector.checkExistQuery("SELECT * FROM "
							+ DBConnector.TABLE_LESSON_ANSWER
							+ " WHERE server_id=" + json.getInt("id"));
				} catch (Exception e) {
				}

				if (!exists) {
					int server_id = json.getInt("id");
					int score1 = json.getInt("score1");
					int score2 = json.getInt("score2");
					int score3 = json.getInt("score3");
					int score4 = json.getInt("score4");
					int score5 = json.getInt("score5");
					int score6 = json.getInt("score6");
					int score7 = json.getInt("score7");
					int score8 = json.getInt("score8");
					int cause = json.getInt("cause");
					int recommend1 = json.getInt("recommend1");
					int recommend2 = json.getInt("recommend2");
					String sound = json.getString("sound");
					String created_datetime = json
							.getString("created_datetime");
					
					mRecommendList = new ArrayList<Integer>();
					JSONArray mRecommendArray = json.getJSONArray("recommend");
					
					for(int i = 0; i < mRecommendArray.length(); i++){
						mRecommendList.add(mRecommendArray.getInt(i));												
					}

					LessonAnswer lessonAnswer = new LessonAnswer(
							lesson.getServerID(), server_id, score1, score2,
							score3, score4, score5, score6, score7, score8,
							cause, recommend1, recommend2, sound,
							created_datetime);
					dbConnector.addLessonAnswer(lessonAnswer);

					JSONArray pictures = json.getJSONArray("picture");
					for (int i = 0; i < pictures.length(); i++) {
						JSONObject picture = pictures.getJSONObject(i);
						server_id = picture.getInt("id");
						String image = picture.getString("image");
						String line = picture.getString("line");
						long timing = picture.getLong("timing");

						LessonAnswerImage lessonAnswerImage = new LessonAnswerImage(
								lessonAnswer.getServerID(), server_id, image,
								line, timing);
						dbConnector.addLessonAnswerImage(lessonAnswerImage);

					}
				}

				lessonAnswer = dbConnector.getLessonAnswerByLesson(lesson);
				lessonAnswerImageList = dbConnector
						.getAllLessonAnswerImageByLessonAnswer(lessonAnswer);
				getFile(lessonAnswerImageList.get(0).getImage());
			} catch (Exception e) {
				Log.d("disp", "err : " + e.getMessage());
			}
			dbConnector.close();
		}

		void getFile(String filename) {
			String path = Const.VIDEO_URL + filename;

			File cacheDir = MylessonDetail.this.getCacheDir();
			cacheDir.mkdirs();
			File cacheFile = new File(cacheDir, "" + path.hashCode());
			if (cacheFile.exists()) {
				return;
			}

			try {
				URLConnection cn = new URL(path).openConnection();
				cn.connect();
				InputStream stream = cn.getInputStream();

				InputStream input = new BufferedInputStream(stream);
				FileOutputStream out = new FileOutputStream(cacheFile);

				byte buf[] = new byte[16384];
				int numread;

				while ((numread = stream.read(buf)) > 0) {
					out.write(buf, 0, numread);
				}

				out.flush();
				out.close();
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.recommend_btn:
			Intent aIntent = new Intent(this, RecommendActivity.class);
			aIntent.putExtra("recommend", mRecommendList);
			startActivity(aIntent);
			
			break;

		default:
			break;
		}
	}
}