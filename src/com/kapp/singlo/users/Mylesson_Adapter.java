package com.kapp.singlo.users;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Utility;

public class Mylesson_Adapter extends ArrayAdapter<Lesson> {

	private ArrayList<Lesson> items;

	LinearLayout likestar;
	ImageView inlike;

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
			WebView profileWebView = (WebView) v.findViewById(R.id.webView1);
			TextView nameTextView = (TextView) v.findViewById(R.id.TextView_01);
			TextView priceTextView = (TextView) v
					.findViewById(R.id.TextView_02);
			TextView certifcateTextView = (TextView) v
					.findViewById(R.id.TextView_03);
			TextView datetimeTextView = (TextView) v
					.findViewById(R.id.DatetimeTextView);
			TextView scoreTextView = (TextView) v
					.findViewById(R.id.TextView_00);
			inlike = (ImageView) v.findViewById(R.id.ImageView_9);
			RatingBar scoreRatingbar = (RatingBar) v
					.findViewById(R.id.ScoreRatingBar);

			if (professional == null) {
				profileWebView.loadDataWithBaseURL(null,
						Utility.getImageHtmlCode(Const.PROFILE_NONE_URL),
						"text/html", "utf-8", null);
				nameTextView.setText("신속 레슨 /");
				priceTextView.setText("￦ 9000");
				certifcateTextView.setText("");
				scoreTextView.setText("");
			} else {
				profileWebView.loadDataWithBaseURL(
						null,
						Utility.getImageHtmlCode(Const.PROFILE_URL
								+ professional.getPhoto()), "text/html",
						"utf-8", null);
				nameTextView.setText(professional.getName() + "/");
				priceTextView.setText("￦ " + professional.getPrice());
				certifcateTextView.setText(professional.getCertification());
				scoreTextView.setText(professional.getEvaluationCount()
						+ "명 "
						+ String.format("%.1f",
								professional.getEvaluationScore()) + "점");
				scoreRatingbar.setRating((float) professional
						.getEvaluationScore());
			}

			if (lesson.getStatus() == 0) {
				inlike.setImageResource(R.drawable.watinglesson_icon);
			} else {
				inlike.setImageResource(R.drawable.completelesson_icon);
			}
			datetimeTextView.setText(lesson.getCreatedDatetime());

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
			priceTextView.setFocusable(false);
			certifcateTextView.setFocusable(false);
			datetimeTextView.setFocusable(false);
			scoreRatingbar.setFocusable(false);
			scoreRatingbar.setClickable(false);
			scoreRatingbar.setLongClickable(false);
			scoreRatingbar.setFocusableInTouchMode(false);
			scoreRatingbar.setIsIndicator(true);
		}

		return v;
	}
}
