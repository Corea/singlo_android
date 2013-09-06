package com.kapp.singlo.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.kapp.singlo.R;
import com.kapp.singlo.data.DBConnector;
import com.kapp.singlo.data.Lesson;
import com.kapp.singlo.data.Professional;
import com.kapp.singlo.util.Const;
import com.kapp.singlo.util.Utility;

public class TeacherMyLessonAdapter extends ArrayAdapter<Lesson> {

	private Context mContext;
	private AQuery mListAq = new AQuery(getContext());

	public TeacherMyLessonAdapter(Context context) {
		super(context, 0);
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.teacher_lesson_list_item, null);
		}

		try {

			AQuery aq = mListAq.recycle(convertView);

			Lesson mData = getItem(position);

			ImageView thumnailImgView = (ImageView) convertView
					.findViewById(R.id.teacher_lesson_thumnail_img);
			TextView remainTimeTextView = (TextView) convertView
					.findViewById(R.id.RemainTimeTextView);
			TextView remainTimeTitleTextView = (TextView) convertView
					.findViewById(R.id.RemainTimeTitleTextView);
			TextView nameTextView = (TextView) convertView
					.findViewById(R.id.NameTextView);
			TextView datetimeTextView = (TextView) convertView
					.findViewById(R.id.DatetimeTextView);
			ImageView statusImageView = (ImageView) convertView
					.findViewById(R.id.LessonStatusImageView);
			TextView questionTextView = (TextView) convertView
					.findViewById(R.id.QuestionTextView);

			aq.id(thumnailImgView).image(
					Const.CAPTURE_URL + mData.getThumnail(), true, true, 100,
					R.drawable.none_thumnail);

			nameTextView.setText(mData.getUserName());
			datetimeTextView.setText(mData.getCreatedDatetime());
			questionTextView.setText(mData.getQuestion());
			if (mData.getStatus() == 0) {
				statusImageView.setImageResource(R.drawable.watinglesson_icon);

				Date today = new Date();
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy.MM.dd HH:mm", Locale.KOREA);
				Date lesson_time;
				try {
					lesson_time = format.parse(mData.getCreatedDatetime());
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
					remainTimeTextView.setText((remain_time / 60) + "시간 "
							+ (remain_time % 60) + "분 전");
				} else if (remain_time >= 0) {
					remainTimeTextView.setText(remain_time + "분 전");
				} else {
					remainTimeTitleTextView.setText("레슨 회신 ");
					remainTimeTextView.setText("만료");
				}

				remainTimeTextView.setTextColor(Color.parseColor("#ffe60019"));
			} else {
				statusImageView
						.setImageResource(R.drawable.completelesson_icon);
				remainTimeTitleTextView.setText("레슨 회신 ");
				remainTimeTextView.setText("완료");
				remainTimeTextView.setTextColor(Color.parseColor("#ff31aa39"));
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return convertView;
	}
}
