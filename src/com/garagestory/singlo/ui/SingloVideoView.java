package com.garagestory.singlo.ui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.garagestory.singlo.R;
import com.garagestory.singlo.util.Const;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.VideoView;

public class SingloVideoView extends Activity {
	// Put in your Video URL here
	private String VideoURL;
	// Declare some variables
	private ProgressDialog pDialog;
	VideoView videoview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = this.getIntent();
		VideoURL = Const.VIDEO_URL + intent.getStringExtra("url");
		// Set the layout from video_main.xml
		setContentView(R.layout.video_view);
		// Find your VideoView in your video_main.xml layout
		videoview = (VideoView) findViewById(R.id.VideoView);
		// Execute StreamVideo AsyncTask
		new StreamVideo().execute(VideoURL);

	}

	// StreamVideo AsyncTask
	private class StreamVideo extends AsyncTask<String, Void, Void> {
		File cacheFile;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressbar
			pDialog = new ProgressDialog(SingloVideoView.this);
			// Set progressbar message
			pDialog.setMessage("동영상 로딩중...");
			pDialog.setIndeterminate(false);
			// Show progressbar
			pDialog.show();

		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				File cacheDir = SingloVideoView.this.getCacheDir();
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
				// int lengthOfFile = cn.getContentLength();

				InputStream input = new BufferedInputStream(stream);
				FileOutputStream out = new FileOutputStream(cacheFile);

				byte buf[] = new byte[16384];
				// long complete = 0;
				int numread;

				while ((numread = stream.read(buf)) > 0) {
					// complete += numread;
					// publishProgress("" + (int) ((complete * 100) /
					// lengthOfFile));
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
						SingloVideoView.this);
				mediacontroller.setAnchorView(videoview);
				// Get the URL from String VideoURL
				// Uri video = Uri.parse(VideoURL);
				videoview.setMediaController(mediacontroller);
				videoview.setVideoPath(cacheFile.getAbsolutePath());
				// videoview.setVideoURI(Uri.parse(VideoURL));
				// videoview.setVideoPath(VideoURL);

				videoview.requestFocus();
				videoview.setOnPreparedListener(new OnPreparedListener() {
					// Close the progress bar and play the video
					public void onPrepared(MediaPlayer mp) {
						pDialog.dismiss();
						videoview.start();
					}
				});
			} catch (Exception e) {
				pDialog.dismiss();
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}

		}

	}

	// Not using options menu for this tutorial
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

}