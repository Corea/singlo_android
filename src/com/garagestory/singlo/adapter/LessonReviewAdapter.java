package com.garagestory.singlo.adapter;

import com.androidquery.AQuery;
import com.garagestory.singlo.R;
import com.garagestory.singlo.data.LessonReviewData;
import com.garagestory.singlo.util.Const;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LessonReviewAdapter extends ArrayAdapter<LessonReviewData>{

	private Context mContext;
	private AQuery mAq;
	
	public LessonReviewAdapter(Context context) {
		super(context, 0);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		mAq = new AQuery(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lesson_review_list_item, null);			
		}
		
		try {
			
			LessonReviewData mData = getItem(position);
			ImageView mProfileImg = (ImageView)convertView.findViewById(R.id.lesson_review_thumnail_img);
			mAq.id(mProfileImg).image(Const.PROFILE_URL + mData.user_photo);
			System.out.println("photo = " + Const.PROFILE_URL + mData.user_photo);
			((TextView)convertView.findViewById(R.id.lesson_review_contents_txt)).setText(mData.review);
			((TextView)convertView.findViewById(R.id.lesson_review_username_txt)).setText(mData.user_name);
			((TextView)convertView.findViewById(R.id.lesson_review_time_txt)).setText(mData.created_datetime);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return convertView;
	}
	
	

}
