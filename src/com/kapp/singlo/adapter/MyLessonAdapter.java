package com.kapp.singlo.adapter;

import android.content.Context;
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

public class MyLessonAdapter extends ArrayAdapter<Lesson> {

	private Context mContext;	
	private AQuery mListAq = new AQuery(getContext());

	public MyLessonAdapter(Context context) {
		super(context, 0);	
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.mylesson_list_item, null);			
		}
		
		try {
			
			AQuery aq = mListAq.recycle(convertView);
			
			DBConnector dbConnector = new DBConnector(getContext());
			Lesson mData = getItem(position);
			Professional mProData = null;
			
			if (mData.getTeacherID() != 0) {
				mProData = dbConnector.getProfessionalByServerID(mData.getTeacherID());
			}
			dbConnector.close();
			
			ImageView thumnailImgView = (ImageView)convertView.findViewById(R.id.lesson_thumnail_img);
			TextView nameTextView = (TextView)convertView.findViewById(R.id.NameTextView);
			TextView companyTextView = (TextView)convertView.findViewById(R.id.CompanyTextView);
			TextView datetimeTextView = (TextView)convertView.findViewById(R.id.DatetimeTextView);
			ImageView inlike = (ImageView)convertView.findViewById(R.id.LessonStatusImageView);
			ImageView lessonTypeImageView = (ImageView)convertView.findViewById(R.id.LessonTypeImageView);
			Button clubTypeButton = (Button)convertView.findViewById(R.id.ClubTypeButton);
			TextView questionTextView = (TextView)convertView.findViewById(R.id.QuestionTextView);
			
			aq.id(thumnailImgView).image(Const.CAPTURE_URL + mData.getThumnail(), true, true, 100, R.drawable.none_thumnail);
			
			if (mProData == null) {				
				nameTextView.setText("신속 레슨");
				companyTextView.setText("");
			} else {
				nameTextView.setText(mProData.getName());
				companyTextView.setText(" / " + mProData.getCompany());
			}
			if (mData.getLessonType() == 1) {
				lessonTypeImageView.setImageResource(R.drawable.fast_lesson_image);
			} else {
				lessonTypeImageView.setImageResource(R.drawable.slow_lesson_image);
			}
			clubTypeButton.setText(Utility.getClubName(mData.getClubType()));

			if (mData.getStatus() == 0) {
				inlike.setImageResource(R.drawable.watinglesson_icon);
			} else {
				inlike.setImageResource(R.drawable.completelesson_icon);
			}
			datetimeTextView.setText(mData.getCreatedDatetime());
			questionTextView.setText(mData.getQuestion());
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		

		return convertView;
	}
}
