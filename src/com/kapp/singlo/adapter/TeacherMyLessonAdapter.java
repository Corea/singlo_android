package com.kapp.singlo.adapter;

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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.teacher_lesson_list_item, null);			
		}
		
		try {
			
			AQuery aq = mListAq.recycle(convertView);
			
			Lesson mData = getItem(position);			
			
			ImageView thumnailImgView = (ImageView)convertView.findViewById(R.id.teacher_lesson_thumnail_img);
			TextView remainTimeTextView = (TextView)convertView.findViewById(R.id.RemainTimeTextView);
			TextView remainTimeTitleTextView = (TextView)convertView.findViewById(R.id.RemainTimeTitleTextView);
			TextView nameTextView = (TextView)convertView.findViewById(R.id.NameTextView);
			TextView datetimeTextView = (TextView)convertView.findViewById(R.id.DatetimeTextView);
			ImageView statusImageView = (ImageView)convertView.findViewById(R.id.LessonStatusImageView);
			TextView questionTextView = (TextView)convertView.findViewById(R.id.QuestionTextView);
			
			aq.id(thumnailImgView).image(Const.CAPTURE_URL + mData.getThumnail(), true, true, 100, R.drawable.none_thumnail);
			
			nameTextView.setText(mData.getUserName());
			datetimeTextView.setText(mData.getCreatedDatetime());
			questionTextView.setText(mData.getQuestion());
			if (mData.getStatus() == 0) {
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
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		

		return convertView;
	}
}
