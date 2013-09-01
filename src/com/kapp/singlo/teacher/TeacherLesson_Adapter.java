package com.kapp.singlo.teacher;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kapp.singlo.R;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Utility;

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
			remainTimeTitleTextView.setText("레슨 회신 만료 ");
			remainTimeTextView.setText("0분 전");
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