package com.garagestory.singlo.teacher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.garagestory.singlo.R;
import com.garagestory.singlo.data.Lesson;
import com.garagestory.singlo.util.Const;
import com.garagestory.singlo.util.Utility;

public class TeacherLesson_Adapter extends ArrayAdapter<Lesson> {

	private ArrayList<Lesson> items;

	public TeacherLesson_Adapter(Context context, int textViewResourceId,
			ArrayList<Lesson> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {

			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.teacher_lesson_item_backup, null);
		}
		Lesson lesson = items.get(position);

		WebView thumbnailWebView = (WebView) v
				.findViewById(R.id.ThumbnailWebView);
		TextView remainTimeTextView = (TextView) v
				.findViewById(R.id.RemainTimeTextView);
		TextView remainTimeTitleTextView = (TextView) v
				.findViewById(R.id.RemainTimeTitleTextView);
		TextView nameTextView = (TextView) v.findViewById(R.id.NameTextView);
		TextView datetimeTextView = (TextView) v
				.findViewById(R.id.DatetimeTextView);
		ImageView statusImageView = (ImageView) v
				.findViewById(R.id.LessonStatusImageView);
		TextView questionTextView = (TextView) v
				.findViewById(R.id.QuestionTextView);

		thumbnailWebView.loadDataWithBaseURL(null,
				Utility.getImageHtmlCode(Const.PROFILE_NONE_URL), "text/html",
				"utf-8", null);

		nameTextView.setText(lesson.getUserName());
		datetimeTextView.setText(lesson.getCreatedDatetime());
		questionTextView.setText(lesson.getQuestion());
		if (lesson.getStatus() == 0) {
			statusImageView.setImageResource(R.drawable.watinglesson_icon);

			Date today = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm",
					Locale.KOREA);
			Date lesson_time;
			try {
				lesson_time = format.parse(lesson.getCreatedDatetime());
			} catch (Exception e) {
				lesson_time = today;
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(lesson_time);
			cal.add(Calendar.HOUR_OF_DAY, 12);
			lesson_time = cal.getTime();

			long today_long_time = today.getTime();
			long lesson_long_time = lesson_time.getTime();
			long remain_time = lesson_long_time - today_long_time;

			remain_time /= (1000 * 60);

			remainTimeTitleTextView.setText("레슨 회신 만료 ");
			if (remain_time >= 60) {
				remainTimeTextView.setText((remain_time / 60) + "시간 전");
			} else if (remain_time >= 0) {
				remainTimeTextView.setText(remain_time + "분 전");
			} else {
				remainTimeTitleTextView.setText("레슨 회신 ");
				remainTimeTextView.setText("만료");
			}
			remainTimeTextView.setTextColor(Color.parseColor("#ffe60019"));
		} else {
			statusImageView.setImageResource(R.drawable.completelesson_icon);
			remainTimeTitleTextView.setText("레슨 회신 ");
			remainTimeTextView.setText("완료");
			remainTimeTextView.setTextColor(Color.parseColor("#ff31aa39"));
		}

		thumbnailWebView.setBackgroundResource(R.anim.shape);
		thumbnailWebView.setPadding(1, 1, 1, 1);

		// focus disable
		thumbnailWebView.setFocusable(false);
		thumbnailWebView.setClickable(false);
		thumbnailWebView.setLongClickable(false);
		thumbnailWebView.setFocusableInTouchMode(false);
		thumbnailWebView.setHorizontalScrollBarEnabled(false);
		thumbnailWebView.setVerticalScrollBarEnabled(false);
		thumbnailWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

		return v;
	}
}