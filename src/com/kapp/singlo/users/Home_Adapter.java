package com.kapp.singlo.users;

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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kapp.singlo.R;
import com.kapp.singlo.bg.CallbackListener;
import com.kapp.singlo.bg.LikeTeacherAsyncTask;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Utility;

public class Home_Adapter extends ArrayAdapter<Professional> implements
		CallbackListener {
	private ArrayList<Professional> items;

	private static final int likeImage = R.drawable.bookmarkon_btn;
	private static final int unlikeImage = R.drawable.bookmarkoff_btn;

	private LinearLayout likestar;
	private ImageView inlike;

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
			WebView profileWebView = (WebView) v.findViewById(R.id.webView1);
			TextView scoreTextView = (TextView) v
					.findViewById(R.id.TextView_00);
			TextView nameTextView = (TextView) v.findViewById(R.id.TextView_01);
			TextView priceTextView = (TextView) v
					.findViewById(R.id.TextView_02);
			TextView certificateTextView = (TextView) v
					.findViewById(R.id.TextView_03);
			inlike = (ImageView) v.findViewById(R.id.ImageView_9);
			RatingBar scoreRatingbar = (RatingBar) v
					.findViewById(R.id.ScoreRatingBar);

			String image_url = Const.PROFILE_URL + professional.getPhoto();
			profileWebView.loadDataWithBaseURL(null,
					Utility.getImageHtmlCode(image_url), "text/html", "utf-8",
					null);

			profileWebView.setBackgroundResource(R.anim.shape);
			profileWebView.setPadding(1, 1, 1, 1);

			scoreTextView.setText(String.format("%.1f",
					professional.getEvaluationScore())
					+ "점 / " + professional.getEvaluationCount() + "명 ");
			nameTextView.setText(professional.getName() + " / ");
			priceTextView.setText("￦ " + professional.getPrice());
			certificateTextView.setText(professional.getCertification());
			inlike.setImageResource((professional.getLike() == 1) ? likeImage
					: unlikeImage);
			scoreRatingbar.setRating((float) professional.getEvaluationScore());

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

			likestar = (LinearLayout) v.findViewById(R.id.likestar);
			likestar.setOnClickListener(likestarOnClickListener);
			likestar.setTag(position);

		}

		return v;
	}

	private OnClickListener likestarOnClickListener = new OnClickListener() {

		public void onClick(View v) {
			ImageView imglike = (ImageView) v.findViewById(R.id.ImageView_9);

			int index = (Integer) v.getTag();

			Log.d("index", String.valueOf(index));

			if (!is_interested) {
				if (items.get(index).getLike() == 0) {
					imglike.setImageResource(likeImage);
					items.get(index).setLike(1);
				} else {
					imglike.setImageResource(unlikeImage);
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

				imglike.setImageResource(unlikeImage);
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