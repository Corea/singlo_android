package com.kapp.singlo.users;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Utility;

public class Mylesson_Adapter extends ArrayAdapter<Lesson> {

	private ArrayList<Lesson> items;

	public Mylesson_Adapter(Context context, int textViewResourceId,
			List<Lesson> items) {
		super(context, textViewResourceId, items);
		this.items = (ArrayList<Lesson>) items;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.mylesson_item, null);
		}

		DBConnector dbConnector = new DBConnector(getContext());
		Lesson lesson = items.get(index);
		Professional professional = null;

		if (lesson.getTeacherID() != 0) {
			professional = dbConnector.getProfessionalByServerID(lesson
					.getTeacherID());
		}

		if (lesson != null) {
			WebView profileWebView = (WebView) v
					.findViewById(R.id.ProfileWebView);
			TextView nameTextView = (TextView) v
					.findViewById(R.id.NameTextView);
			TextView companyTextView = (TextView) v
					.findViewById(R.id.CompanyTextView);
			TextView datetimeTextView = (TextView) v
					.findViewById(R.id.DatetimeTextView);
			ImageView inlike = (ImageView) v
					.findViewById(R.id.LessonStatusImageView);
			ImageView lessonTypeImageView = (ImageView) v
					.findViewById(R.id.LessonTypeImageView);
			Button clubTypeButton = (Button) v
					.findViewById(R.id.ClubTypeButton);
			TextView questionTextView = (TextView) v
					.findViewById(R.id.QuestionTextView);

			if (professional == null) {
				profileWebView.loadDataWithBaseURL(null,
						Utility.getImageHtmlCode(Const.PROFILE_NONE_URL),
						"text/html", "utf-8", null);
				nameTextView.setText("신속 레슨");
				companyTextView.setText("");
			} else {
				profileWebView.loadDataWithBaseURL(
						null,
						Utility.getImageHtmlCode(Const.PROFILE_URL
								+ professional.getPhoto()), "text/html",
						"utf-8", null);
				nameTextView.setText(professional.getName());
				companyTextView.setText(" / " + professional.getCompany());
			}
			if (lesson.getLessonType() == 1) {
				lessonTypeImageView
						.setImageResource(R.drawable.fast_lesson_image);
			} else {
				lessonTypeImageView
						.setImageResource(R.drawable.slow_lesson_image);
			}
			clubTypeButton.setText(Utility.getClubName(lesson.getClubType()));

			if (lesson.getStatus() == 0) {
				inlike.setImageResource(R.drawable.watinglesson_icon);
			} else {
				inlike.setImageResource(R.drawable.completelesson_icon);
			}
			datetimeTextView.setText(lesson.getCreatedDatetime());
			questionTextView.setText(lesson.getQuestion());

			profileWebView.setBackgroundResource(R.anim.shape);
			profileWebView.setPadding(1, 1, 1, 1);

			// focus disable
			profileWebView.setFocusable(false);
			profileWebView.setClickable(false);
			profileWebView.setLongClickable(false);
			profileWebView.setFocusableInTouchMode(false);
			profileWebView.setHorizontalScrollBarEnabled(false);
			profileWebView.setVerticalScrollBarEnabled(false);
			profileWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

			nameTextView.setFocusable(false);
			datetimeTextView.setFocusable(false);
		}

		return v;
	}
}
