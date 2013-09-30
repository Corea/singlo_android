package com.garagestory.singlo.users;

import java.util.ArrayList;

import com.garagestory.singlo.R;
import com.garagestory.singlo.data.Professional;
import com.garagestory.singlo.util.Const;
import com.garagestory.singlo.util.Utility;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class LessonRequestPageAdapter extends ArrayAdapter<Professional> {

	private ArrayList<Professional> items;
	private SparseBooleanArray checkList;

	private boolean is_interested;

	public LessonRequestPageAdapter(Context context, int textViewResourceId,
			ArrayList<Professional> items) {
		super(context, textViewResourceId, items);

		this.items = items;

		checkList = new SparseBooleanArray();
		is_interested = false;
	}

	public void setCheck(int index) {
		checkList.append(items.get(index).getServerId(), true);
	}

	public void removeCheck(int index) {
		checkList.delete(items.get(index).getServerId());
	}

	public void setInterested(Boolean interested) {
		is_interested = interested;
	}

	public boolean getInterested() {
		return is_interested;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.lesson_request_page2_slow_item, null);
		}

		Professional professional = items.get(index);
		if (professional != null) {
			WebView profileWebView = (WebView) v
					.findViewById(R.id.ProfileWebView);
			TextView nameTextView = (TextView) v
					.findViewById(R.id.NameTextView);
			TextView priceTextView = (TextView) v
					.findViewById(R.id.PriceTextView);
			TextView certificationTextView = (TextView) v
					.findViewById(R.id.CertificationTextView);
			TextView scoreTextView = (TextView) v
					.findViewById(R.id.ScoreTextView);
			TextView absenceTextView = (TextView) v
					.findViewById(R.id.AbsenceTextView);
			ImageView statusImageView = (ImageView) v
					.findViewById(R.id.StatusImageView);
			RatingBar scoreRatingbar = (RatingBar) v
					.findViewById(R.id.ScoreRatingBar);
			ImageView lessonActiveImageView = (ImageView) v
					.findViewById(R.id.LessonActiveImageView);

			nameTextView.setText(professional.getName());
			priceTextView.setText("" + professional.getPrice());
			certificationTextView.setText(professional.getCertification());
			if (checkList.get(professional.getServerId()) == true) {
				statusImageView.setImageResource(R.drawable.selecton_icon);
			} else {
				statusImageView.setImageResource(R.drawable.selectoff_icon);
			}
			profileWebView.loadDataWithBaseURL(
					null,
					Utility.getImageHtmlCode(Const.PROFILE_URL
							+ professional.getPhoto()), "text/html", "utf-8",
					null);
			scoreRatingbar.setRating((float) professional.getEvaluationScore());
			scoreTextView.setText(String.format("%.1f",
					professional.getEvaluationScore())
					+ "점 / " + professional.getEvaluationCount() + "명 ");

			if (professional.getStatus() == 1) {
				lessonActiveImageView
						.setImageResource(R.drawable.lessonon_icon);
				absenceTextView.setText("");
			} else {
				lessonActiveImageView
						.setImageResource(R.drawable.lessonoff_icon);
				absenceTextView.setText(professional.getStatusMessage());
			}

			profileWebView.setBackgroundResource(R.anim.shape);
			profileWebView.setPadding(1, 1, 1, 1);
			profileWebView.setFocusable(false);
			profileWebView.setClickable(false);
			profileWebView.setLongClickable(false);
			profileWebView.setFocusableInTouchMode(false);
			profileWebView.setHorizontalScrollBarEnabled(false);
			profileWebView.setVerticalScrollBarEnabled(false);
			profileWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

			nameTextView.setFocusable(false);
			priceTextView.setFocusable(false);
			certificationTextView.setFocusable(false);
			scoreTextView.setFocusable(false);
			statusImageView.setFocusable(false);
			scoreRatingbar.setFocusable(false);
			scoreRatingbar.setClickable(false);
			scoreRatingbar.setLongClickable(false);
			scoreRatingbar.setFocusableInTouchMode(false);
		}
		return v;

	}
}
