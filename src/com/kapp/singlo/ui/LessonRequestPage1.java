package com.kapp.singlo.ui;

import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kapp.singlo.R;
import com.kapp.singlo.util.Utility;

@SuppressLint("NewApi")
public class LessonRequestPage1 extends Activity {

	ImageButton nextImageButton;
	ImageButton movieImageButton;

	Button driverButton;
	Button woodButton;
	Button utilityButton;
	Button ironButton;
	Button wedgeButton;
	Button putterButton;

	EditText questionText;

	TextView limit_text;

	int id;
	String choice_movie_mode = "";
	String choice_mode = "", select_device = "";
	int sw_device;
	String questiontext = "";
	Uri selected_video;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lesson_request_page1);

		nextImageButton = (ImageButton) findViewById(R.id.NextImageButton);
		nextImageButton.setOnClickListener(nextbuttonClickListener);
		questionText = (EditText) findViewById(R.id.QuestionText);
		questionText.setText("");

		driverButton = (Button) findViewById(R.id.DriverButton);
		driverButton.setOnClickListener(golf_device01ClickListener);
		woodButton = (Button) findViewById(R.id.WoodButton);
		woodButton.setOnClickListener(golf_device02ClickListener);
		utilityButton = (Button) findViewById(R.id.UtilityButton);
		utilityButton.setOnClickListener(golf_device03ClickListener);
		ironButton = (Button) findViewById(R.id.IronButton);
		ironButton.setOnClickListener(golf_device04ClickListener);
		wedgeButton = (Button) findViewById(R.id.WedgeButton);
		wedgeButton.setOnClickListener(golf_device05ClickListener);
		putterButton = (Button) findViewById(R.id.PutterButton);
		putterButton.setOnClickListener(golf_device06ClickListener);

		movieImageButton = (ImageButton) findViewById(R.id.MovieImageButton);
		movieImageButton.setOnClickListener(movieButtonClickListener);

		sw_device = 0;
	}

	// 아이템 하나만 선택하는 리스트형 다이얼로그
	private void DialogSelectOption() {
		final String items[] = { "신속레슨", "프로선택" };
		AlertDialog.Builder ab = new AlertDialog.Builder(
				LessonRequestPage1.this);
		ab.setTitle("레슨선택");
		ab.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// 각 리스트를 선택했을때
						choice_mode = items[whichButton];
					}
				})
				.setPositiveButton("선택", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.

						Intent intent = null;
						if (choice_mode.equals("신속레슨")) {
							intent = new Intent(LessonRequestPage1.this,
									LessonRequestPage2Fast.class);
						} else if (choice_mode.equals("프로선택")) {
							intent = new Intent(LessonRequestPage1.this,
									LessonRequestPage2Slow.class);
						} else {
							return;
						}
						intent.putExtra("swing_device", sw_device);
						intent.putExtra("question", questiontext);
						intent.putExtra("video", selected_video.toString());
						overridePendingTransition(0, 0);
						startActivity(intent);
					}
				})
				.setNegativeButton("취소",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Cancel 버튼 클릭시
							}
						});
		ab.show();
	}

	private OnClickListener golf_device01ClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			driverButton.setBackgroundResource(R.drawable.select_back);
			woodButton.setBackgroundResource(R.drawable.noselect_back);
			utilityButton.setBackgroundResource(R.drawable.noselect_back);
			ironButton.setBackgroundResource(R.drawable.noselect_back);
			wedgeButton.setBackgroundResource(R.drawable.noselect_back);
			putterButton.setBackgroundResource(R.drawable.noselect_back);

			sw_device = 1;
		}
	};
	private OnClickListener golf_device02ClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			driverButton.setBackgroundResource(R.drawable.noselect_back);
			woodButton.setBackgroundResource(R.drawable.select_back);
			utilityButton.setBackgroundResource(R.drawable.noselect_back);
			ironButton.setBackgroundResource(R.drawable.noselect_back);
			wedgeButton.setBackgroundResource(R.drawable.noselect_back);
			putterButton.setBackgroundResource(R.drawable.noselect_back);

			sw_device = 2;

		}
	};
	private OnClickListener golf_device03ClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			driverButton.setBackgroundResource(R.drawable.noselect_back);
			woodButton.setBackgroundResource(R.drawable.noselect_back);
			utilityButton.setBackgroundResource(R.drawable.select_back);
			ironButton.setBackgroundResource(R.drawable.noselect_back);
			wedgeButton.setBackgroundResource(R.drawable.noselect_back);
			putterButton.setBackgroundResource(R.drawable.noselect_back);

			sw_device = 3;
		}
	};
	private OnClickListener golf_device04ClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			driverButton.setBackgroundResource(R.drawable.noselect_back);
			woodButton.setBackgroundResource(R.drawable.noselect_back);
			utilityButton.setBackgroundResource(R.drawable.noselect_back);
			ironButton.setBackgroundResource(R.drawable.select_back);
			wedgeButton.setBackgroundResource(R.drawable.noselect_back);
			putterButton.setBackgroundResource(R.drawable.noselect_back);

			sw_device = 4;
		}
	};
	private OnClickListener golf_device05ClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			driverButton.setBackgroundResource(R.drawable.noselect_back);
			woodButton.setBackgroundResource(R.drawable.noselect_back);
			utilityButton.setBackgroundResource(R.drawable.noselect_back);
			ironButton.setBackgroundResource(R.drawable.noselect_back);
			wedgeButton.setBackgroundResource(R.drawable.select_back);
			putterButton.setBackgroundResource(R.drawable.noselect_back);

			sw_device = 5;
		}
	};
	private OnClickListener golf_device06ClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			driverButton.setBackgroundResource(R.drawable.noselect_back);
			woodButton.setBackgroundResource(R.drawable.noselect_back);
			utilityButton.setBackgroundResource(R.drawable.noselect_back);
			ironButton.setBackgroundResource(R.drawable.noselect_back);
			wedgeButton.setBackgroundResource(R.drawable.noselect_back);
			putterButton.setBackgroundResource(R.drawable.select_back);

			sw_device = 6;
		}
	};
	private OnClickListener nextbuttonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				questiontext = URLEncoder.encode(questionText.getText()
						.toString(), "UTF-8");
			} catch (Exception e) {
				questiontext = questionText.getText().toString();
			}
			select_device = String.valueOf(sw_device);

			if (questionText.getText().toString().equals("")) {
				Toast.makeText(LessonRequestPage1.this, "질문을 작성해 주시기 바랍니다.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (sw_device == 0) {
				Toast.makeText(LessonRequestPage1.this, "골프채를 선택해 주시기 바랍니다.",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (selected_video == null || selected_video.getPath().equals("")) {
				Toast.makeText(LessonRequestPage1.this, "동영상을 선택해 주시기 바랍니다.",
						Toast.LENGTH_SHORT).show();
				return;
			}

			DialogSelectOption();
		}
	};

	private OnClickListener movieButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			final String items[] = { "동영상 촬영", "동영상 선택" };
			AlertDialog.Builder ab = new AlertDialog.Builder(
					LessonRequestPage1.this);
			ab.setSingleChoiceItems(items, -1,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// 각 리스트를 선택했을때
							choice_movie_mode = items[whichButton];
						}
					})
					.setPositiveButton("선택",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									if (choice_movie_mode.equals("동영상 촬영")) {
										choice_movie_mode = "";
										Intent intent = new Intent(
												android.provider.MediaStore.ACTION_VIDEO_CAPTURE);

										intent.putExtra(
												MediaStore.EXTRA_VIDEO_QUALITY,
												0);
										intent.putExtra(
												MediaStore.EXTRA_SIZE_LIMIT,
												10 * 1024 * 1024);
										intent.putExtra(
												MediaStore.EXTRA_DURATION_LIMIT,
												8);
										startActivityForResult(intent, 2);
									} else if (choice_movie_mode
											.equals("동영상 선택")) {
										choice_movie_mode = "";

										Intent mediaChooser = new Intent(
												Intent.ACTION_GET_CONTENT);
										// comma-separated MIME types
										mediaChooser.setType("video/*");
										startActivityForResult(mediaChooser, 1);
									}
								}
							})
					.setNegativeButton("취소",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									choice_movie_mode = "";
									// Cancel 버튼 클릭시
								}
							});
			ab.show();

		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			selected_video = null;
			movieImageButton.setImageResource(R.drawable.novideo);
			return;
		} 
		
		selected_video = data.getData();

		if (Utility.getFileSize(selected_video.getPath()) > 10 * 1024) {
			Toast.makeText(LessonRequestPage1.this, "동영상의 크기는 10M를 넘을 수 없습니다.",
					Toast.LENGTH_LONG).show();
			return;
		}

		ContentResolver crThumb = getContentResolver();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		Bitmap bmThumbnail;
		try {
			String[] splited = selected_video.getPath().split("/");
			int id = Integer.parseInt(splited[splited.length - 1]);
			bmThumbnail = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id,
					MediaStore.Video.Thumbnails.MINI_KIND, options);
		} catch (Exception e) {
			bmThumbnail = ThumbnailUtils.createVideoThumbnail(
					selected_video.getPath(), Thumbnails.MINI_KIND);
		}

		movieImageButton.setImageBitmap(bmThumbnail);
		Log.d("debug", selected_video.getPath());
	};

}
