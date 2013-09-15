package com.garagestory.singlo.users;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.garagestory.singlo.R;
import com.garagestory.singlo.bg.CallbackListener;
import com.garagestory.singlo.bg.LikeTeacherAsyncTask;
import com.garagestory.singlo.data.Professional;
import com.garagestory.singlo.util.Const;
import com.garagestory.singlo.util.Utility;

public class Home_Adapter extends ArrayAdapter<Professional> implements
		CallbackListener {
	private ArrayList<Professional> items;

	private static final int likeImage = R.drawable.bookmarkon_btn;
	private static final int unlikeImage = R.drawable.bookmarkoff_btn;

	private ImageView likeImageView;

	private boolean is_interested;

	private int user_id;
	private Context context;

	public Home_Adapter(Context context, int textViewResourceId,
			ArrayList<Professional> items) {
		super(context, textViewResourceId, items);

		this.context = context;
		this.items = items;

		is_interested = false;
	}

	public void setUserId(int user_id) {
		this.user_id = user_id;
	}

	public void setIntrested(Boolean interested) {
		is_interested = interested;
	}

	public boolean getInterested() {
		return is_interested;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.home_item, null);
		}
		Professional professional = items.get(position);

		if (professional != null) {
			WebView profileWebView = (WebView) v
					.findViewById(R.id.ProfileWebView);
			TextView scoreTextView = (TextView) v
					.findViewById(R.id.ScoreTextView);
			TextView nameTextView = (TextView) v
					.findViewById(R.id.NameTextView);
			TextView priceTextView = (TextView) v
					.findViewById(R.id.PriceTextView);
			TextView certificateTextView = (TextView) v
					.findViewById(R.id.CertificationTextView);
			TextView absenceTextView = (TextView) v
					.findViewById(R.id.AbsenceTextView);
			likeImageView = (ImageView) v.findViewById(R.id.LikeImageView);
			RatingBar scoreRatingbar = (RatingBar) v
					.findViewById(R.id.ScoreRatingBar);
			ImageView lessonActiveImageView = (ImageView) v
					.findViewById(R.id.LessonActiveImageView);

			String image_url = Const.PROFILE_URL + professional.getPhoto();
			profileWebView.loadDataWithBaseURL(null,
					Utility.getImageHtmlCode(image_url), "text/html", "utf-8",
					null);

			profileWebView.setBackgroundResource(R.anim.shape);
			profileWebView.setPadding(1, 1, 1, 1);

			scoreTextView.setText(String.format("%.1f",
					professional.getEvaluationScore())
					+ "점 / " + professional.getEvaluationCount() + "명 ");
			nameTextView.setText(professional.getName());
			priceTextView.setText("" + professional.getPrice());
			certificateTextView.setText(professional.getCertification());
			likeImageView
					.setImageResource((professional.getLike() == 1) ? likeImage
							: unlikeImage);
			likeImageView.setOnClickListener(likestarOnClickListener);
			likeImageView.setTag(position);
			scoreRatingbar.setRating((float) professional.getEvaluationScore());

			if (professional.getStatus() == 1) {
				lessonActiveImageView
						.setImageResource(R.drawable.lessonon_icon);
				absenceTextView.setText("");
			} else {
				lessonActiveImageView
						.setImageResource(R.drawable.lessonoff_icon);
				absenceTextView.setText(professional.getStatusMessage());
			}

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
			certificateTextView.setFocusable(false);
			scoreRatingbar.setFocusable(false);
			scoreRatingbar.setClickable(false);
			scoreRatingbar.setLongClickable(false);
			scoreRatingbar.setFocusableInTouchMode(false);
			scoreRatingbar.setIsIndicator(true);
		}

		return v;
	}

	private OnClickListener likestarOnClickListener = new OnClickListener() {

		public void onClick(View v) {
        ImageView likeImageView = (ImageView) v
                .findViewById(R.id.LikeImageView);

        int index = (Integer) v.getTag();

        Log.d("index", String.valueOf(index));

        if (!is_interested) {
            if (items.get(index).getLike() == 0) {
                likeImageView.setImageResource(likeImage);
                items.get(index).setLike(1);
            } else {
                likeImageView.setImageResource(unlikeImage);
                items.get(index).setLike(0);
            }

            likestar(index);

        } else {
            int count = 0;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getLike() == 1) {
                    if (count == index) {
                        index = i;
                        break;
                    }
                    count++;
                }
            }

            likeImageView.setImageResource(unlikeImage);
            items.get(index).setLike(0);
            likestar(index);
            items.remove(index);
            Home_Adapter.this.notifyDataSetChanged();
        }
		}
	};

	private LikeTeacherAsyncTask likeTeacherAsyncTask;

	private void likestar(int index) {
		Log.d("index", String.valueOf(index));

		String return_like = "";

		likeTeacherAsyncTask = new LikeTeacherAsyncTask();
		likeTeacherAsyncTask.setContext(context);
		likeTeacherAsyncTask.setListener(this);
		likeTeacherAsyncTask.execute(user_id, items.get(index).getServerId(),
				items.get(index).getLike());

		Log.d("upload", "change_userlikestar : " + return_like);

	}

	@Override
	public void callback(Object... params) {
		Professional professional = (Professional) params[0];
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getServerId() == professional.getServerId()) {
				items.get(i).setLike(professional.getLike());
				break;
			}
		}
		Home_Adapter.this.notifyDataSetChanged();
	}
}