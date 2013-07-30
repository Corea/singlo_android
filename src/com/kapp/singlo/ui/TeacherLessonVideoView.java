package com.kapp.singlo.ui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Coord;
import com.kapp.singlo.util.JSONParser;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class TeacherLessonVideoView extends Activity {

	private int server_id;

	private String url;
	private String videoURL;
	private List<String> captureURLList;
	private List<ArrayList<Coord>> saveLineList;

	private MediaMetadataRetriever mediaMetadataRetriever;

	private ProgressDialog pDialog;
	private VideoView videoView;
	private SeekBar seekBar;

	private ImageView drawingCanvasImageView;
	private ImageView drawingCacheCanvasImageView;
	private TextView ImageCountTextView;

	private ImageButton backImageButton;
	private ImageButton saveImageButton;
	private ImageButton removeImageButton;
	private ImageButton finishImageButton;
	private ImageButton playImageButton;
	private ImageButton rewindImageButton;
	private ImageButton forwardImageButton;

	private Canvas canvas;
	private Canvas cacheCanvas;
	private Bitmap bitmap;
	private Bitmap cacheBitmap;
	private Paint paint;
	private boolean stopHandler;

	private ArrayList<Coord> lineList;

	private int width, height;

	private boolean saveState;

	private StreamVideo streamVideo;
	private CaptureVideo captureVideo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_lesson_video_view);

		Intent intent = this.getIntent();
		url = intent.getStringExtra("url");
		server_id = intent.getIntExtra("lesson_id", 0);
		videoURL = Const.VIDEO_URL + url;

		captureURLList = new ArrayList<String>();
		lineList = new ArrayList<Coord>();
		saveLineList = new ArrayList<ArrayList<Coord>>();

		boolean audio = intent.getBooleanExtra("audio", false);
		if (audio) {
			int bitmapCount = intent.getIntExtra("bitmapCount", 0);
			for (int i = 0; i < bitmapCount; i++) {
				String path = intent.getStringExtra("bitmapList" + i);
				captureURLList.add(path);
				// Bitmap tmp_bitmap = BitmapFactory.decodeFile(path);
				// bitmapList.add(tmp_bitmap);

				ArrayList<Coord> tmp = intent
						.getParcelableArrayListExtra("lineList" + i);
				saveLineList.add(tmp);
			}

		}
		stopHandler = false;

		videoView = (VideoView) findViewById(R.id.VideoView);
		seekBar = (SeekBar) findViewById(R.id.SeekBar);
		seekBar.setOnSeekBarChangeListener(seekBarOnSeekBarChangedListener);

		// videoView.setOnTouchListener(videoViewOnTouchEvent);
		drawingCanvasImageView = (ImageView) findViewById(R.id.DrawingCanvasImageView);
		drawingCacheCanvasImageView = (ImageView) findViewById(R.id.DrawingCacheCanvasImageView);
		drawingCacheCanvasImageView.setOnTouchListener(videoViewOnTouchEvent);

		ImageCountTextView = (TextView) findViewById(R.id.ImageCountTextView);
		backImageButton = (ImageButton) findViewById(R.id.BackImageButton);
		backImageButton.setOnClickListener(backImageButtonOnClickListener);
		saveImageButton = (ImageButton) findViewById(R.id.SaveImageButton);
		saveImageButton.setOnClickListener(saveImageButtonOnClickListener);
		removeImageButton = (ImageButton) findViewById(R.id.RemoveImageButton);
		removeImageButton.setOnClickListener(removeImageButtonOnClickListener);
		finishImageButton = (ImageButton) findViewById(R.id.FinishImageButton);
		finishImageButton.setOnClickListener(finishImageButtonOnClickListener);
		playImageButton = (ImageButton) findViewById(R.id.PlayImageButton);
		playImageButton.setOnClickListener(playImageButtonOnClickListener);
		rewindImageButton = (ImageButton) findViewById(R.id.RewindImageButton);
		rewindImageButton.setOnClickListener(rewindImageButtonOnClickListener);
		forwardImageButton = (ImageButton) findViewById(R.id.ForwardImageButton);
		forwardImageButton
				.setOnClickListener(forwardImageButtonOnClickListener);

		saveState = false;
		streamVideo = new StreamVideo(this);
		streamVideo.execute(videoURL);

	}

	private float downx = 0, downy = 0, upx = 0, upy = 0;
	private OnTouchListener videoViewOnTouchEvent = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.i("ACTION_DOWN", event.getX() + " " + event.getY());
				downx = event.getX();
				downy = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				Log.i("ACTION_MOVE", event.getX() + " " + event.getY());
				upx = event.getX();
				upy = event.getY();
				cacheCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
				cacheCanvas.drawLine(downx, downy, upx, upy, paint);
				drawingCacheCanvasImageView.invalidate();
				break;
			case MotionEvent.ACTION_UP:
				Log.i("ACTION_UP", event.getX() + " " + event.getY());
				canvas.drawLine(downx, downy, upx, upy, paint);
				cacheCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
				drawingCacheCanvasImageView.invalidate();
				drawingCanvasImageView.invalidate();
				upx = event.getX();
				upy = event.getY();

				lineList.add(new Coord(downx / (double) width, downy
						/ (double) height, false));
				lineList.add(new Coord(event.getX() / (double) width, event
						.getY() / (double) height, true));
				break;
			}
			return true;
		}
	};

	private OnSeekBarChangeListener seekBarOnSeekBarChangedListener = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			videoView.seekTo(seekBar.getProgress());

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

		}
	};

	private OnClickListener backImageButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			TeacherLessonVideoView.this.onBackPressed();

		}
	};

	private OnClickListener saveImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (captureURLList.size() == 8) {
				Toast.makeText(TeacherLessonVideoView.this,
						"프레임 8장을 모두 선택하셨습니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			if (saveState) {
				Toast.makeText(TeacherLessonVideoView.this,
						"이전 그림을 저장하고 있습니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			saveState = true;

			int currentPosition = videoView.getCurrentPosition();

			captureVideo = new CaptureVideo(TeacherLessonVideoView.this);
			captureVideo.execute(server_id, currentPosition);
			saveLineList.add((ArrayList<Coord>) lineList.clone());

		}

	};

	private OnClickListener removeImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			canvas.drawColor(0, PorterDuff.Mode.CLEAR);
			drawingCanvasImageView.invalidate();
			lineList.clear();
		}
	};

	private OnClickListener finishImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (captureURLList.size() == 0) {
				Toast.makeText(TeacherLessonVideoView.this,
						"하나 이상의 프레임을 선택해야 합니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent(TeacherLessonVideoView.this,
					TeacherLessonAudioRecord.class);
			intent.putExtra("url", url);
			intent.putExtra("bitmapCount", captureURLList.size());
			for (int i = 0; i < captureURLList.size(); i++) {
				intent.putParcelableArrayListExtra("lineList" + i,
						saveLineList.get(i));
				intent.putExtra("bitmapList" + i, captureURLList.get(i));
			}

			stopHandler = true;
			intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
			startActivity(intent);
			finish();
		}
	};
	private OnClickListener playImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (videoView.isPlaying()) {
				videoView.pause();
				playImageButton.setImageResource(R.drawable.play_btn);
			} else {
				videoView.start();
				playImageButton.setImageResource(R.drawable.pause_btn);
			}

		}
	};

	private OnClickListener rewindImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			videoView.seekTo(0);
		}
	};
	private OnClickListener forwardImageButtonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			videoView.seekTo(videoView.getDuration());

		}
	};

	// Not using options menu for this tutorial
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (!stopHandler) {
				seekBar.setProgress(videoView.getCurrentPosition());
				progressHandler.sendEmptyMessageDelayed(0, 100);

			}

		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:
			stopHandler = true;
			finish();

		}

		return true;
	}

	private class CaptureVideo extends AsyncTask<Integer, Void, Void> {

		Context context;
		int server_id;
		int current_position;

		int status;

		public CaptureVideo(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			status = 0;
		}

		@Override
		protected Void doInBackground(Integer... params) {
			server_id = params[0];
			current_position = params[1];

			try {

				String url = Const.LESSON_CAPTURE_GET_URL;
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				InputStream is;

				List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("lesson_id", ""
						+ server_id));
				nameValuePairs.add(new BasicNameValuePair("current_position",
						"" + current_position));

				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				is = httpResponse.getEntity().getContent();

				JSONParser jParser = new JSONParser();
				JSONObject json = jParser.getJSONFromStream(is);

				String result = json.getString("result");
				if (result.equals("success")) {
					String path = json.getString("path");

					captureURLList.add(path);
				} else {
					status = 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 2;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			saveState = false;

			if (status == 1) {
				Toast.makeText(context, "그림을 저장하는 중 문제가 발생하였니다.",
						Toast.LENGTH_LONG).show();
			} else if (status == 2) {

				Toast.makeText(context, "그림을 저장하는 중 문제가 발생하였니다.",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "프레임이 저장되었습니다.!", Toast.LENGTH_SHORT)
						.show();
				ImageCountTextView.setText(captureURLList.size() + " / 8");
			}
		}
	}

	// StreamVideo AsyncTask
	private class StreamVideo extends AsyncTask<String, Void, Void> {

		Context context;
		File cacheFile;

		public StreamVideo(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressbar
			pDialog = new ProgressDialog(TeacherLessonVideoView.this);
			// Set progressbar title
			pDialog.setMessage("동영상 로딩중...");
			pDialog.setIndeterminate(false);
			// Show progressbar
			pDialog.show();

		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				File cacheDir = context.getCacheDir();
				cacheDir.mkdirs();
				cacheFile = new File(cacheDir, "" + params[0].hashCode());
				if (cacheFile.exists()) {
					Runtime.getRuntime().exec(
							"chmod 777 " + cacheFile.getAbsolutePath());
					return null;
				}

				URLConnection cn = new URL(params[0]).openConnection();
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

				Runtime.getRuntime().exec(
						"chmod 777 " + cacheFile.getAbsolutePath());
			} catch (Exception e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			try {
				// Start the MediaController
				MediaController mediacontroller = new MediaController(
						TeacherLessonVideoView.this);
				mediacontroller.setAnchorView(videoView);
				videoView.setVideoPath(cacheFile.getAbsolutePath());

				videoView.setOnPreparedListener(new OnPreparedListener() {
					// Close the progress bar and play the video
					public void onPrepared(MediaPlayer mp) {
						pDialog.dismiss();
						seekBar.setMax(videoView.getDuration());
						progressHandler.sendEmptyMessageDelayed(0, 100);

						mediaMetadataRetriever = new MediaMetadataRetriever();
						mediaMetadataRetriever.setDataSource(cacheFile
								.getAbsolutePath());

						mediaMetadataRetriever
								.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
						width = videoView.getWidth();
						height = videoView.getHeight();
						Log.d("height", "" + height);
						Log.d("width", "" + width);

						bitmap = Bitmap.createBitmap(width, height,
								Bitmap.Config.ARGB_8888);
						canvas = new Canvas(bitmap);
						drawingCanvasImageView.setImageBitmap(bitmap);

						cacheBitmap = Bitmap.createBitmap(width, height,
								Bitmap.Config.ARGB_8888);
						cacheCanvas = new Canvas(cacheBitmap);
						drawingCacheCanvasImageView.setImageBitmap(cacheBitmap);

						paint = new Paint();
						paint.setARGB(255, 255, 0, 0);
						paint.setStrokeWidth(width / 75);
						paint.setAntiAlias(true);

					}
				});
				videoView
						.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

							@Override
							public void onCompletion(MediaPlayer arg0) {
								playImageButton
										.setImageResource(R.drawable.play_btn);
								videoView.start();
								videoView.pause();

							}
						});

			} catch (Exception e) {
				pDialog.dismiss();
			}

		}

	}
}